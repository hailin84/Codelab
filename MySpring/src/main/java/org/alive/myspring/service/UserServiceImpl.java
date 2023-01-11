package org.alive.myspring.service;

import com.spring.Autowired;
import com.spring.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin84
 * @since 2023/1/10
 */
@Component("userService")
//@Scope("prototype")
public class UserServiceImpl implements UserService {

    @Autowired
    private OrderService orderService;

    private String myName;

    @Override
    public void show() {
        System.out.println(orderService);
        System.out.println(myName);
    }
}
