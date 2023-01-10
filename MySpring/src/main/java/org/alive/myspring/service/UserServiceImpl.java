package org.alive.myspring.service;

import com.spring.Autowired;
import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.Scope;

/**
 * <p>
 *
 * </p>
 *
 * @author hailin
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
