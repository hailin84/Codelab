package org.alive.springlab.constants;

import java.util.Random;

/**
 * <p>
 * Redis Key常量定义
 * </p>
 *
 * @author hailin84
 * @since 2023-01-09
 */
public final class RedisKeyConstants {

    public static final String PRODUCT_PREFIX = "product:";

    /** 商品redis缓存默认过期时间，单位秒，默认15天 */
    public static final int PRODUCT_CACHE_EXPIRE = 60 * 60 * 24 * 15;

    public static final String EMPTY_CACHE = "{}";

    /** 读取商品时，用到的分布式锁前缀，防止并发从DB读取并写缓存 */
    public static final String LOCK_PREFIX_PRODUCT = "lock:cache:product:";

    /** 更新商品时，用到的分布式读写锁前缀，防止并发读写导致的缓存跟DB不一致 */
    public static final String LOCK_PREFIX_PRODUCT_UPDATE = "lock:update:product:";

    public static String getProductCacheKey(Integer productId) {
        return PRODUCT_PREFIX + productId;
    }

    /**
     * 商品缓存超时时间，15天加上随机的分钟。
     * @return 超时时间，单位为秒
     */
    public static int getProductCacheExpire() {
        return PRODUCT_CACHE_EXPIRE + new Random().nextInt(60) * 60;
    }

    public static String getLockKeyForProduct(Integer productId) {
        return LOCK_PREFIX_PRODUCT + productId;
    }

    public static String getLockPrefixProductUpdate(Integer productId) {
        return LOCK_PREFIX_PRODUCT_UPDATE + productId;
    }
}
