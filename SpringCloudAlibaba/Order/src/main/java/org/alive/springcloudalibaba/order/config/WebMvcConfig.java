package org.alive.springcloudalibaba.order.config;

import org.alive.springcloudalibaba.order.filter.SimpleRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/28 20:18
 */
@Configuration
public class WebMvcConfig {
    @Bean
    public FilterRegistrationBean<Filter> filter(SimpleRequestFilter filter) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);
        bean.setUrlPatterns(Collections.singletonList("/*"));
        // 值越小，Filter越靠前。
        bean.setOrder(1);
        return bean;
    }
}
