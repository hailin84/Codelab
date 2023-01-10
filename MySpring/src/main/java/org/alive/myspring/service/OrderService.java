package org.alive.myspring.service;

import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.InitializingBean;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin
 * @since 2023/1/10
 */
@Component("orderService")
public class OrderService implements BeanNameAware, InitializingBean {
    private String myName;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @Override
    public void setBeanName(String name) {
        myName = name;
    }
}
