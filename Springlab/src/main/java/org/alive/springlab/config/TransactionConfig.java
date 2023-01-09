package org.alive.springlab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 全局事务配置
 */
@Configuration
@EnableTransactionManagement // 全局事务
public class TransactionConfig {

}
