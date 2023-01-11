package com.spring;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin84
 * @since 2023/1/10
 */
public class BeanDefinition {
    private Class clazz;

    private String scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
