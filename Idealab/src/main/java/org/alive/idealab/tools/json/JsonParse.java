package org.alive.idealab.tools.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/23 14:33
 */
public class JsonParse {
    public static String path = "D:/工作内容/开发/20230322 网关路由整理/";

    private static String[] files = {
            "dev_内网网关.gwd.json",
            "dev_外网网关.gwd.json",
            "prod_内网网关.gwd.json",
            "prod_外网网关.gwd.json",
            "test_内网网关.gwd.json",
            "test_外网网关.gwd.json"
    };

    public static void main(String[] args) throws IOException {
        Map<String, Route> routeMap1 = loadFromFile(files[3]);
        Map<String, Route> routeMap2 = loadFromFile(files[5]);

        for (Map.Entry<String, Route> entry : routeMap1.entrySet()) {
            String routeUrl = entry.getKey();
            Route route1 = entry.getValue();
            Route route2 = routeMap2.get(routeUrl);
            if (route2 != null) {
                boolean a = route1.equals(route2);
                if (!a) {
                    System.out.println(a + "|" + route1 + " " + route2);
                }
                routeMap2.remove(routeUrl);
            } else {
                // System.out.println("Missing: " + route1);
            }
        }

        System.out.println("Left: " + routeMap2);
    }


    public static Map<String, Route> loadFromFile(String fileName) throws IOException {
        String json = Files.asCharSource(new File(path + fileName), StandardCharsets.UTF_8).read();
        List<Route> routeList = JSON.parseArray(json, Route.class);

        Map<String, Route> ret = new HashMap<>();
        routeList.forEach(e -> {
            ret.put(e.getRouteUri(), e);
        });
        return ret;
    }
}
