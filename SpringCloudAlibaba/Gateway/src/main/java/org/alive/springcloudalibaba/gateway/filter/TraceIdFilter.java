package org.alive.springcloudalibaba.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/28 18:13
 */
@Component
@Slf4j
public class TraceIdFilter implements GlobalFilter, Ordered {
    private static final String TRACE_ID_HEADER = "AP-TRACE-ID";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest oldRequest = exchange.getRequest();
        String traceId = oldRequest.getHeaders().getFirst(TRACE_ID_HEADER);
        if (traceId == null || traceId.length() == 0) {
            // System.out.println("Missing trace id " + TRACE_ID_HEADER);
            traceId = UUID.randomUUID().toString();
            log.warn("Missing trace id {}, generate {}", TRACE_ID_HEADER, traceId);

            ServerHttpRequest newRequest = oldRequest.mutate().header(TRACE_ID_HEADER, traceId).build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
