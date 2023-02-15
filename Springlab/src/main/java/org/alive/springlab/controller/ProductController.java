package org.alive.springlab.controller;

import com.alibaba.fastjson.JSON;
import jodd.util.StringUtil;
import org.alive.springlab.constants.RedisKeyConstants;
import org.alive.springlab.entity.Product;
import org.alive.springlab.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author hailin84
 * @since 2023-01-09
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Resource
    private IProductService service;

    @Resource
    private Redisson redisson;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/addProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public String addProduct(Product product) {
        service.save(product);
        // 设置缓存超时时间，添加一个随机的时间值，避免同时过期导致缓存击穿(雪崩)
        redisTemplate.opsForValue().set(RedisKeyConstants.getProductCacheKey(product.getId()), JSON.toJSONString(product),
                RedisKeyConstants.getProductCacheExpire(), TimeUnit.SECONDS);
        return "success: " + product.getId();
    }

    @RequestMapping(path = "/updateProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateProduct(Product product) {
        Integer productId = product.getId();
        // 读写锁，防止并发读写导致的DB和缓存不一致现象
        RReadWriteLock lock = redisson.getReadWriteLock(RedisKeyConstants.getLockPrefixProductUpdate(productId));
        lock.writeLock().lock();
        try {
            service.updateById(product);
            // 设置缓存超时时间，添加一个随机的时间值，避免同时过期导致缓存击穿(雪崩)
            redisTemplate.opsForValue().set(RedisKeyConstants.getProductCacheKey(product.getId()), JSON.toJSONString(product),
                    RedisKeyConstants.getProductCacheExpire(), TimeUnit.SECONDS);
        } finally {
            lock.writeLock().unlock();
        }
        return "success: " + product.getId();
    }

    @RequestMapping(path = "/getProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public Product getProduct(Integer productId) {
        Product product = getFromCache(productId);
        if (product != null) {
            return product;
        }

        RLock rlock = redisson.getLock(RedisKeyConstants.getLockKeyForProduct(productId));
        rlock.lock();
        try {
            product = getFromCache(productId);
            if (product != null) {
                return product;
            }

            RReadWriteLock lock = redisson.getReadWriteLock(RedisKeyConstants.getLockPrefixProductUpdate(productId));
            lock.readLock().lock(); // 读锁
            try {
                product = service.getById(productId);
                if (product != null) {
                    redisTemplate.opsForValue().set(RedisKeyConstants.getProductCacheKey(productId), JSON.toJSONString(product),
                            RedisKeyConstants.getProductCacheExpire(), TimeUnit.SECONDS);
                } else {
                    // 设置一个空缓存，防止缓存穿透问题，这个超时时间设置短一点，最长不要超过5分钟，因为这个数据是没什么意义的垃圾数据，仅仅为了应对请示高峰期的缓存穿透问题。
                    redisTemplate.opsForValue().set(RedisKeyConstants.getProductCacheKey(productId), RedisKeyConstants.EMPTY_CACHE, 5 * 60, TimeUnit.SECONDS);
                }
            } finally {
                lock.readLock().unlock();
            }

        } finally {
            rlock.unlock();
        }
        return product;
    }

    private Product getFromCache(Integer productId) {
        String productStr = (String) redisTemplate.opsForValue().get(RedisKeyConstants.getProductCacheKey(productId));
        if (StringUtils.isNotEmpty(productStr)) {
            if (RedisKeyConstants.EMPTY_CACHE.equals(productStr)) {
                return new Product(); // 返回空对象，避免后续再去DB查询，因为已经可以确定这个产品在DB里是不存在的
            }
            return JSON.parseObject(productStr, Product.class);
        }
        return null;
    }
}
