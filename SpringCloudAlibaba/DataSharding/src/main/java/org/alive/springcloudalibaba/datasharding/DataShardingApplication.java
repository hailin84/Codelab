package org.alive.springcloudalibaba.datasharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.alive.springcloudalibaba.datasharding.DataShardingApplication.PACKAGE_NAME;

/**
 * shardingsphere示例
 */
@SpringBootApplication
@MapperScan(basePackages = {PACKAGE_NAME + ".biz.**.mapper"})
public class DataShardingApplication {
    public static final String PACKAGE_NAME = "org.alive.springcloudalibaba.datasharding";

    public static void main(String[] args) {
        SpringApplication.run(DataShardingApplication.class, args);
    }
}