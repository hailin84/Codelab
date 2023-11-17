package org.alive.idealab;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/4/25 10:20
 */
public class StreamApiTest {

    @Test
    public void mapFuzzyFilter() {
        Map<String, String> data = new HashMap<>();
        data.put("001", "深圳市");
        data.put("002", "广州市");
        data.put("003", "北京市");
        data.put("004", "深圳");

        String ret = data.entrySet().stream().filter(e -> e.getValue().startsWith("深圳")).map(Map.Entry::getKey).collect(Collectors.joining(","));
        System.out.println(ret);
    }
}
