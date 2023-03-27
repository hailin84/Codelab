package org.alive.springcloudalibaba.order.controller;

import org.alive.springcloudalibaba.order.feign.StockFeignService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin84
 * @since 2023/2/14
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    RestTemplate restTemplate;

    @Resource
    StockFeignService feignService;

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addOrder() {
        System.out.println("下单成功");
        // stock-service为服务名，必须加了负载均衡才可以识别
        // String msg = restTemplate.getForObject("http://stock-service/stock/deduct", String.class);

        // 通过Feign调用，就像是调用本地方法一样。
        String msg = feignService.deduct();

        return "OK: " + msg;
    }
}
