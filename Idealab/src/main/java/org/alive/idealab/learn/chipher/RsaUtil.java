package org.alive.idealab.learn.chipher;

import com.google.common.base.Stopwatch;
import com.xiaoleilu.hutool.io.FileUtil;
import org.alive.idealab.learn.util.RequestUtil;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @author: xuhailin
 * @date: 2023/3/10 9:47
 */
public class RsaUtil {

    // RSA/ECB/OAEPPadding RSA/ECB/PKCS1Padding
    private static String RSA_ALG= "RSA/ECB/PKCS1Padding";

    /** */
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static Map<String, String> generateRsaKeys() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        generator.initialize(1024, new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();
        System.out.println(keyPair.getPublic().getEncoded().length);
        System.out.println(keyPair.getPrivate().getEncoded().length);

        String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
        System.out.println(publicKey);

        String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        System.out.println(privateKey);

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("PUBLIC", publicKey);
        keyMap.put("PRIVATE", privateKey);
        // keyMap.put("PUBLIC", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn+tm6o8GGXWM4K4PYTX6KLbGVbUJ7R0M+MQHp02MOqyWsTWJ6Yx/M8tMcGSmS3yDKK5X46axTuRvRe68aqmoN5xixk3bBXfo4iYVVrS4g+xEliFPTJ8MXTOqq/3qvs1woXbxGLcQlU4LZjX8U8wV2kQs9WTpCPVGVmvcMm/zkvA216WfZ42Yac/Ta/4Fso0AXXmZoBGj6nwtpN5int0JU96nyX6ricQTluSq3Cb+u4XR8SKk295aYv55WIMZDZl2R8HzHjpaZkJDXfi7vh+2BK1jxfSSkSR0GuJdqMd33XpWBhMEL0PXp5Zexgv0AeKq9dkyznM01P1AnshX3F+FFwIDAQAB");
        // keyMap.put("PRIVATE", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCf62bqjwYZdYzgrg9hNfootsZVtQntHQz4xAenTYw6rJaxNYnpjH8zy0xwZKZLfIMorlfjprFO5G9F7rxqqag3nGLGTdsFd+jiJhVWtLiD7ESWIU9MnwxdM6qr/eq+zXChdvEYtxCVTgtmNfxTzBXaRCz1ZOkI9UZWa9wyb/OS8DbXpZ9njZhpz9Nr/gWyjQBdeZmgEaPqfC2k3mKe3QlT3qfJfquJxBOW5KrcJv67hdHxIqTb3lpi/nlYgxkNmXZHwfMeOlpmQkNd+Lu+H7YErWPF9JKRJHQa4l2ox3fdelYGEwQvQ9enll7GC/QB4qr12TLOczTU/UCeyFfcX4UXAgMBAAECggEAMUYV+tXAul1238+h24EHKD5Q1B2pBk86LPgxp4IEYBvHfqiRas2ob+wHDGZmusGNpB475F/7J+TIyFJr6a2GsEPWR2W7w26T6gJz9rhOVg+ZfmybA2/KHnmJ796X0tx4BLgiR0w4pthaOM8PhT5PyWXSOHY0O1dfqccoHL5T3YT6pi4vTZGXqFYHNBjeZAiteFUDZ+3sTbwXa/jxuVtIQ/GNdQXkGKeDRaqgKJplXW01B9La//W85/u2BeYEK/bqKoagl4C7AVV6RC1Nj1Zwh4QPHTvqvsIMJBcmiIzieKR1IeWafdy40E4cnNMQB0U77IxyFfXP00eDSJMNSulHcQKBgQDlVzo+zuMQ4FdJcejITovO62yV9B56BClH07l/QJUy9z+sWSu2eJpvrl3cgg64CA8KV7q7TJ1qTtfyf/e4zvMxhK7mLlC7VCyaExn0y68rlu5fPcbixoS4KqBNvnLw/4NKoOw51WOGZHA5/EB7pShxwX9hn6IXg1Jrpt4OinhsyQKBgQCyglK9DsKGf5LWmV065PYBS66eq2NLbHyKtujMxdtlvkeDkG6hkKd3jAGd5R+q65FsHeoM/vj36sNF9DGkgGHOvXX5C1SNDgwIBjnlEt57rCacjwlkC+3haXPqXi+fM5QCbXIxNjLXJpZREnefOkqlGBmvewLJz2MrrrkJDq2y3wKBgBiPELOtH++stmwtAIqCrsIHv+cG70SNsYX5jEGuPsvoYi8olV/C5nbTK0Xzv3PBmSq4Xky0VZ8Otl1HitWFg9FnrfsENqz4xJXzObJXJXj7fqdATE34iDnYhrYMkO5xzjNtrB49VZ0MoF/1PUqAUyZrXCKuH4N5EpholBrmhSLRAoGAQ8dgCxDzo4YFBx6sW5RcJBnMb8q7pQPINhNFSZtGttcM6FOK7iWysJ2kb4N0Br3zNY4WNGELv3aQNpuFzeRBPvKJPMqki/ptbw6OJQCJIYQ4FPHqquYsqI4VyUOB6Xz4EgPke/MMUsz//kB40yia9xyzhAG9CUuJaCircVj1cxkCgYEAr+N+pl366MvAkDxaXEMMEsG4M9NB6uSDokfngjL67xUqNyphbl4BnzgBCR5R0cu204r5OkUmEJuSGHWlr65YFl+IOh3vFFOWFdZk6Lz0gEwGS7SR8bmd4FabTiV74hDWj8VeZWj8fJZ6XcEur8sHieYWmns5OcKuOsVfkoOF1QA=");

        return keyMap;
    }

    public static String rsaEncrypt(String plainData, String b64PublicKey) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(b64PublicKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] dataBytes = plainData.getBytes(StandardCharsets.UTF_8);
        int inputLen = dataBytes.length;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    public static String rsaDecrypt(String b64Data, String b64PrivateKey) throws Exception {
        // 私钥默认是采用PKCS8编码
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(b64PrivateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedData = Base64.decodeBase64(b64Data);
        int inputLen = encryptedData.length;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Security.addProvider(new BouncyCastlePQCProvider());
        Map<String, String> keyMap = new HashMap<>();
        // server public key
        keyMap.put("PUBLIC", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWFYNuLNMibZ/UeZmn1QIicyZ+KfuHIoearbo1lRJMpww8W45ZG7W6ArhwP7mATHNjIBl+/UTiZZbo5iRLqwYfEs68TmlymbG+hEOsRefkeqV5wDD43yCwYlsg9KnXuf/jopWtlC8wUH1uDcQ0cpPCvhxScu5XA3SF7BT336ioiwIDAQAB");

        // server private key
        keyMap.put("PRIVATE", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJYVg24s0yJtn9R5mafVAiJzJn4p+4cih5qtujWVEkynDDxbjlkbtboCuHA/uYBMc2MgGX79ROJllujmJEurBh8SzrxOaXKZsb6EQ6xF5+R6pXnAMPjfILBiWyD0qde5/+Oila2ULzBQfW4NxDRyk8K+HFJy7lcDdIXsFPffqKiLAgMBAAECgYEAg3rDUhCYwm9w9o20q4/yt8VS8nHK9T6ttzb8ixZWGqq5EGNQ2AVrdVIOy983ngrbhvpG+7Xa088VnmCHaXag+9I6iuRwq0JHNBGUF8lAvfT5I5umDI48cNogTuwJlhVeuOb2IWBNn6q/P+gBHKLDUtQEaq7MUV+sZYPgi0dBmRECQQD/ouWctOajk0e2AN05/jCr32CIe4nJaymiTblxIJtEGCNfbri4PgF3CkPFx21RJ3AJxhxMqfWImbXDACAj1SWpAkEAlkwsmGhE8cT9ZjGJqghkZmq0tKB9TZMtzRbrYoUccz6BzHgMovq6zMYFudCNKkdbIVoQu9UqZh4I89iaHMAVEwJARDHlhP9xQ5PHnpLucUkRHNiTPPWP1U6kJasMXFRxdyHlVdEIsAXCiEoRuFIyJGMb7U+PPxhb6tvudB21dTmoOQJAXkyW4lzxfPvKvXaxWBSB4dxrWrUjSSvh3b7Wmu7HhesfB0n/K7bCwz4vU4FtQKyamddnjmJVgtKvbUdmkklWLwJBAI8/XsHpNuPW03YXFIAhVBV1oceWfB3+h4JZPp+WAS07emD+tV7dqqCGLjlR+dWYIbK1BN9iQ76EfIBVliTXQF0=");


        // serverKey
        // keyMap.put("PRIVATE", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALSjvNQ9C1uZ5uQJ0XA27TcMc/qi3SoFxzhizGAWoRAVOpfk/zJqwsMw4aLX9B5zMHkGGHQt8aMJhaAobL1/Tt51KsegRxXh3xLLRvoumE8lZbbJmG61xgE/asHPb+Y7EUO1rUb9C03NBKPodY4VIW75Y3CkOPaC7Nju27kyLiJlAgMBAAECgYB2As53HZZQ9p+H9P4VwFyC7SjL4iB4tqOrK6h3UfFRL31DBIDQMD0jBcky+MBsMDbHqfrJ08YcDDl3I1G/Du+3fmvwaXz8spxBZRtHPNu5WpfTN3/6owVHA30Zdz6pLwkuLwBfWTlQxeQPvvNL766ln+L6gku2s/doGouJBwDBoQJBANudZvjMeZ0ArHapkMV2SU0A7S22hjOH3TSrxAxA+uKb9HUdYxtzJ3U0MUY1iBbcY0gnfvSBhdgrhbrbTAJZYykCQQDSkUPZi+a05stlHpWpebZzjybB00tSPHRWlfG5ZokPoRaLVCx1lnzRs5US8KfwurJiNAsN9y87f9KDSD7SIEjdAkAkh4D6P2C3KcaGElxfS/aTVWUeJm8aIS49NZjthN12VgSKenHivfvBNgZALGVPkCb/eHpYhzbRJyQeWZ3CAkZJAkEAju2ltZQ9/Iswgqn5ArmoO9Zug2BuHxsECQXst2E/Jcm9aMOxDfwhK6KPTPTnugKwNPKIkmk3uVZxSUWPxse9TQJAT4tYQBaAFkdZYgLkViW9312/Nx4N/g9kO0KX77yu6aAONHXN2djxIU9hhXwNA8PQ4OoHm7j55tQQaHSWIa4xaw==");// String data = "a=1&b=2&c=3";

        testNormalData(keyMap);
        // testQueryString(keyMap);
    }

    private static void testNormalData(Map<String, String> keyMap) throws Exception {
        System.out.println("Normal:");
        String data = "Hello DST";
        data = "{\"phone\":\"13331234123\",\"verifyCode\":\"999999\",\"userName\":\"123\",\"typeCode\":\"000002\",\"registerExtendInfo\":{“labelId”: \"62\"}}";
        String ciphered = rsaEncrypt(data, keyMap.get("PUBLIC"));
        // ciphered = "C9R9IaWDEj+V5QLOVarr+4t5wg+JQlN3T8GQkPdxnJV28rwYlJQjjEpOBE0BgZ7RhXwWdAtwnTsjm+v+LIutEI5aVbMho2h9BG1J23YpnJRMPaA0N4dLInckNG63pvGwdzEenQx16k01PcN/K57EW6t19Qup6DjuIv1EHekNnpc=";

        // ciphered = "L4P3HpGgq2L9YVLWGX/J8oWVZrVNNkCn+sFCzqfBIpW0RxWwlsxpIqRCysWFteFfE6exu+uFOIgLZaLIkxXH3dDRlkD+PwkilX3MlXzhn7tia/ME9JjFFtEujxB3Uj87ZPuO6xYX8DY/LdpgkZ095XHqkMqzMm7Zsa/QDAg6qLM=";
        String plain = rsaDecrypt(ciphered, keyMap.get("PRIVATE"));

        System.out.println("A: " + data + "\nB: " + ciphered + "\nC: " + plain);
    }

    private static void testQueryString(Map<String, String> keyMap) throws Exception {
        System.out.println("queryString:");
        // 包含特殊符号的请求参数值，先做url encode
        String abc = URLEncoder.encode(" =#", "UTF-8");
        String data = "name=abc&age=99&phone=33333333333&special=" + abc;
        // 加密(包含B64编码)
        String ciphered = rsaEncrypt(data, keyMap.get("PUBLIC"));
        // url encode
        ciphered = URLEncoder.encode(ciphered, "UTF-8");

        ciphered = "aKu%2BQLWuuUv9H30yeB0B4UsUJp2NZB26nvR0MyREQpKDmzokTEd3AivqtjjSXOAnZ6c8kV%2FFL4tJXabk6KXreAts93a2gs8lCo%2BQMBtQNBlb%2FhfHxdoV59fqQ6MfDcr3Vv1WWx9U8P7HpokR3yL74Eyyex%2Bu2wAtGYW%2B1WX1Dew4%3D";
        // 服务端收到数据，先做url decode，再解密(包含B64解码)
        String plain = URLDecoder.decode(ciphered, "UTF-8");
        plain = rsaDecrypt(plain, keyMap.get("PRIVATE"));

        System.out.println("A: " + data + "\nB: " + ciphered + "\nC: " + plain);
    }
}
