package org.alive.springcloudalibaba.order.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/28 20:18
 */
@Component
public class SimpleRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = ((HttpServletRequest) servletRequest).getHeader("AP-TRACE-ID");
        System.out.println("收到请求 " + traceId);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
