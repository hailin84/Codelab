package org.alive.simpleweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/16 15:51
 */
@RestController()
@RequestMapping("/echo")
public class EchoController {

    @RequestMapping(value = "/showRequest", method = {RequestMethod.GET, RequestMethod.POST})
    public String sayHello(HttpServletRequest request) {

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String h = headers.nextElement();
            System.out.println(h + ":" + request.getHeader(h));
        }

        String qs = request.getQueryString();
        System.out.println(qs);
        return "Hello " + LocalDateTime.now();
    }
}
