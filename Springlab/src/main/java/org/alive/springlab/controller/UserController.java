package org.alive.springlab.controller;


import org.alive.springlab.entity.User;
import org.alive.springlab.service.EntityOperateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private EntityOperateService service;

    @RequestMapping(path = "/getUsers", method = {RequestMethod.GET})
    public List<User> getUsers() {
        List<User> userList = service.getUsers();
        return userList;
    }

    @RequestMapping(path = "/addUser", method = {RequestMethod.GET, RequestMethod.POST})
    public Long addUser() {
        User user = new User();
        user.setName("ABC");
        user.setAge(18);
        user.setEmail("abc@abc.com");
        return this.service.addUser(user);
    }
}
