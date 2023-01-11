package org.alive.myspring;

import com.spring.MyApplicationContext;
import org.alive.myspring.service.UserService;

/**
 * <p>
 * 手写Spring框架，测试类. 实践图灵学院周瑜老师的手写Spring课程。
 * </p>
 *
 * @author hailin84
 * @since 2023/1/10
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);
        userService.show();
        System.out.println(context.getBean("userService"));
    }
}
