package org.alive.idealab;

import org.alive.idealab.http.OkHttpUtil;
import org.junit.jupiter.api.Test;

public class HttpTest {

    @Test
    public void testUnknownHostException() {
        OkHttpUtil.get("http://abc.unknow.com");
    }

    @Test
    public void testConnectionTimeoutException() {
        OkHttpUtil.get("http://127.0.0.1:8001");
    }
}
