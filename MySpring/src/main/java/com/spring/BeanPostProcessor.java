package com.spring;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin
 * @since 2023/1/10
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}
