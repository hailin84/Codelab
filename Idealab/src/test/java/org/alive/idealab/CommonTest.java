package org.alive.idealab;

import com.google.common.collect.Sets;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

public class CommonTest {

    @Test
    public void testArrayInit() {
        int[] data = new int[2];
        Assertions.assertArrayEquals(new int[] {0 , 0}, data);
    }

    @Test
    public void testJson() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "xuhailin");
        data.put("age", "");
        System.out.println(JSONUtil.toJsonStr(data));
    }

    @Test
    public void testSplit() {
        String source = " - - ";
        String[] parts = source.split("#");
        System.out.println(parts.length);
    }

    @Test
    public void testSet() {
        System.out.println(Sets.newHashSet("1", "12").contains("1"));
        List<String> data = new ArrayList<>();
    }

    @Test
    public void testFormat() {
        System.out.println(String.format("%d{}:%d", 766668739859619843L, 7));
    }

    @Test
    public void testOptional() {
        String name = Optional.ofNullable("").orElse("null");
        System.out.println(name);
        name = Optional.ofNullable("This is abc").orElse("");
        System.out.println(name);

        String[] keyCandidates = new String[] { null, "", "" };
        String lockKey = Stream.of(keyCandidates).filter(e -> e != null && e.length() != 0).findFirst().orElse("");
        System.out.println(lockKey);
    }

    @Test
    public void showRepeat() {
        String serialNo = "202309081817036103 | FPO45436418801015851";
        String parts[] = serialNo.split("\\|");
        System.out.println(parts[parts.length - 1].trim());
    }
}
