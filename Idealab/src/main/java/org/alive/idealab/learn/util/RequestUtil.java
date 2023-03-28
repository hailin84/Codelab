package org.alive.idealab.learn.util;

import com.google.common.base.Splitter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/15 20:17
 */
public final class RequestUtil {

    /**
     * 将queryString解析成Map，注意，这里没有做url encode/decode
     *
     * @param queryString 类似于a=1&b=2&c=3这样的queryString串
     * @return 参数Map
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return Splitter.on('&').omitEmptyStrings().withKeyValueSeparator('=').split(queryString);
    }

    public static String formEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String formDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
