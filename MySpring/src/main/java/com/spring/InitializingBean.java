package com.spring;

/**
 * <p>
 * 仿Spring InitializingBean
 * </p>
 *
 * @author hailin84
 * @since 2023/1/10
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
