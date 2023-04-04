package org.alive.springcloudalibaba.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin84
 * @since 2023/2/14
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @RequestMapping(value = "/deduct", method = {RequestMethod.GET, RequestMethod.POST})
    public String deduct(HttpServletRequest request) {
        System.out.println("扣减库存成功");
        return "OK";
    }

}
