package org.alive.springlab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/hello")
public class HelloController {

    public HelloController() {
        System.out.println("Controller crreated");
    }

    @RequestMapping(value = "/sayHello", method = {RequestMethod.GET})
    public String sayHello() {
        return "Hello " + LocalDateTime.now();
    }
}
