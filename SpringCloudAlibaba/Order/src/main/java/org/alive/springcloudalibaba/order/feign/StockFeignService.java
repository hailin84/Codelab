package org.alive.springcloudalibaba.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * Feign接口
 * </p>
 *
 * @author hailin84
 * @since 2023/2/14
 */
@FeignClient(name = "stock-service", path = "/stock")
public interface StockFeignService {
    @RequestMapping(value = "/deduct")
    String deduct();
}
