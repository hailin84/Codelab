package org.alive.idealab.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpUtil {
    /**
     * 连接超时时间，设置成5秒，其他几个超时时间也设置成相同的时间
     */
    private static final int CONNECTION_TIMEOUT = 5000;

    private static final int READ_TIMEOUT = 5000;

    private static final int WRITE_TIMEOUT = 5000;

    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient httpsClient;

    private static OkHttpClient httpClient;

    static {
        try {
            httpsClient = getUnsafeHttpsClient();
            httpClient = getHttpClient();
        } catch (Exception e) {
            log.error("初始化https/http客户端失败", e);
        }
    }

    /**
     * 访问Https的客户端，跳过证书校验，本地也不加载证书，只适合服务端单向证书模式。
     *
     * @return OkHttpClient
     * @throws Exception 异常
     */
    public static OkHttpClient getUnsafeHttpsClient() throws Exception {
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), tm)
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 生创建并返回httpClient
     *
     * @return OkHttpClient
     */
    public static OkHttpClient getHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 调用接口，返回响应
     *
     * @param url 请求地址
     * @param xmlStr xml参数
     * @param transCode 交易码
     * @return 成功或者异常
     */
    public static String post(String url, String xmlStr, String transCode) {
        OkHttpClient client = url.startsWith("https://") ? httpsClient : httpClient;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(XML, xmlStr))
                .build();
        log.info("中信接口请求报文: \n" + xmlStr);
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new IOException("response.body为null");
            }
            // 响应需要转换为UTF-8编码字符串
            String respStr = new String(response.body().bytes(), StandardCharsets.UTF_8);
            log.info("中信接口响应报文: \n" + respStr);
            return respStr;
        } catch (IOException e) {
            log.error("中信接口异常", e);
            // 网络异常设置错误码为77777777，业务方需要根据这个错误码，决定是否查询交易状态
            return null;
        }
    }

    public static String get(String url) {
        OkHttpClient client = url.startsWith("https://") ? httpsClient : httpClient;
        Request request = new Request.Builder()
                .url(url).get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new IOException("response.body为null");
            }
            // 响应需要转换为UTF-8编码字符串
            String respStr = new String(response.body().bytes(), StandardCharsets.UTF_8);
            return respStr;
        } catch (UnknownHostException e) {
            log.error("接口异常", e);
        } catch (ConnectException e) {
            log.error("接口异常", e);
        } catch (SocketTimeoutException e) {
            log.error("接口异常", e);
        } catch (IOException e) {
            log.error("接口异常", e);
        }

        return null;
    }

    public static String postJson(String url, String json) {
        OkHttpClient client = url.startsWith("https://") ? httpsClient : httpClient;
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, json))
                .build();
        log.info("宝付接口请求报文: \n" + json);
        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) {
                throw new IOException("response.body为null");
            }
            // 响应需要转换为UTF-8编码字符串
            String respStr = new String(response.body().bytes(), StandardCharsets.UTF_8);
            log.info("宝付接口响应报文: \n" + respStr);
            return respStr;
        } catch (IOException e) {
            log.error("中信接口异常", e);
        }

        return null;
    }
}
