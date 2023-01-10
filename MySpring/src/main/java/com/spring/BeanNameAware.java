package com.spring;

/**
 * <p>
 * 仿Spring BeanNameAware
 * </p>
 *
 * @author hailin
 * @since 2023/1/10
 */
public interface BeanNameAware {
    void setBeanName(String name);
}
