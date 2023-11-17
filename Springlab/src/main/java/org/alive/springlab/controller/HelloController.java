package org.alive.springlab.controller;

import com.alive.springlab.proto.ResponseOuterClass;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/hello")
public class HelloController {

    public HelloController() {
        System.out.println("Controller crreated");
    }

    @RequestMapping(value = "/sayHello", method = {RequestMethod.GET})
    public String sayHello(HttpServletRequest request) {
        String qs = request.getQueryString();
        System.out.println(qs);
        return "Hello " + LocalDateTime.now();
    }

    @RequestMapping(value = "/protoBuf", method = {RequestMethod.GET})
    public String protoBuf(HttpServletRequest request) {
        ResponseOuterClass.Response.Builder builder = ResponseOuterClass.Response.newBuilder();
        // 设置字段值
        builder.setData("hello www.tizi365.com").setStatus(200);
        ResponseOuterClass.Response response = builder.build();

        // 将数据根据protobuf格式，转化为字节数组
        byte[] byteArray = response.toByteArray();

        // 反序列化,二进制数据
        try {
            ResponseOuterClass.Response newResponse = ResponseOuterClass.Response.parseFrom(byteArray);
            System.out.println(newResponse.getData());
            System.out.println(newResponse.getStatus());
            return newResponse.toString();
        } catch (Exception e) {

        }

        return "";
    }
}
