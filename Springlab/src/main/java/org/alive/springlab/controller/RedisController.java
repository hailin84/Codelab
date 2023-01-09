package org.alive.springlab.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private Redisson redisson;

    @RequestMapping(path = "/set", method = {RequestMethod.GET})
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "true";
    }

    @RequestMapping(path = "/lock", method = {RequestMethod.GET})
    public String lock(String key, String value) {
        RLock rlock = redisson.getLock("test-lockkey");
        boolean locked = rlock.tryLock();
        try {
            if (locked) {
                System.out.println("lock success");
            } else {
                System.out.println("lock failed");
            }
        } finally {
            rlock.unlock();
        }

        return "true";
    }
}
