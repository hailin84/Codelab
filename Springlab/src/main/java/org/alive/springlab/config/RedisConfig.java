package org.alive.springlab.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis相关的配置
 */
@Configuration
public class RedisConfig {
//    @Bean
//    public Redisson redisson() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("AaZz1234");
//        return (Redisson) Redisson.create(config);
//    }
}
