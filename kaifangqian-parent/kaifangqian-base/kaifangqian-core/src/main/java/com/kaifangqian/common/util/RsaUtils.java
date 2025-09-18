package com.kaifangqian.common.util;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.common.system.vo.Authorization;
import com.kaifangqian.utils.MyStringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author https://www.cnblogs.com/nihaorz/p/10690643.html
 * @description Rsa 工具类，公钥私钥生成，加解密
 * @date 2020-05-18
 **/
public class RsaUtils {

    private static final String SRC = "123456";

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static void main(String[] args) throws Exception {
//        String s = "781042746dca4084f4ae078c4b9ea7d9fe5f8891d0733224cdda963d969a902dd54546c00ebbf3a48da96142c50f4ec3d880ba55450376be1c6c6898c61103bd61f79079d126b814e3f7bbe1e1dd05f1ee156631bbd84829742ed7cdf6f1518db2b54c8c319e16b6cd642ce5969667e27437da2b9aa42e60a4e24a27343fd30b";
//        String pk = "30819f300d06092a864886f70d010101050003818d003081890281810095d77b9ab8b17cceda10c4d11648b6196a3897b196a79ca81bc5e21d4bf90f7b7559827e831cdb48011e54c64a7a872bce4aa369d124b4c4b3d0f83cb63f8b210e7c25be346ce14e5fe44404788c3360651f29e4894e1412a86638cf22dc81c5ece53ddf0b5e6409cd6a7e509f34558644b4be703d9d55c9b6cc5dee4b090ba90203010001";
//        String ss = RsaUtils.decryptByPublicKeyByte(s, pk);
//        System.out.println(ss);
        //   test2(generateKeyPair());
//        test3();

        String vk = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALYW9GOLSx0FXuC5WtR+U9X/N9O/yhqtwoCJM0DOeRK5Tgl+pSAt3mZ7EAi342gXl2dOdlXwXW1ZfL3Zd2al84ACAfGbLIYUzxACn7Y7wv+8DvAnJTBeRfLrZ16aT8O4H7DKWwqbBA04BK1HRLCyxs2F7k/K7vrGTU3yTz9omdkVAgMBAAECgYEAr9uPf6wBiS+sqCCbZmnzEdQA2NJ7oDR8iqL3Cvnd2IV6ppTXaTKjfhoQLDtctyVBphYTF4Ci2n74iGpEdLCFb1FFe1/IorEcLSJ5ahiQidr0Ufc6XzF76yVyIkU5rjgIY8LMg0UUbdTssq0i56mXgUMzJp7pBwrihkc8aG8igcECQQD0VfoPKRyjfyjKx8ldzBCF2woX4dK1brSkIz5LWk6PjHc5JsDy7KFkn4riVPbtwrymQ1r86ZafdqJgZBMoUbS5AkEAvshEwvrJbjXe/R8Zogl3FJ+kv7yMiYftABVxwvNXLEDUKfGuQYeAfxIbqipxMvXEzsq/aFEIqa8FKMOPq22RPQJAJJ9KZsFTwJHLrHE7lmqCw31sSt4XNgiM3NlHegXkJpH4QMG1Q/QB0NI0/+2aQVLh8c3Aso3UfLxMZEQ7ttxgSQJBAIPMYMx+aoerybf+Mzwg49Yoj60x+bjNYWpsZiHy8CcPRkMPxn1YuemPPfNpzLgS13qw0FilmqF22s6Vg3w/flUCQQCTt1zHJDj0WbEDtaVxv0XwlYjXo0JGPRtSq6RUAsznWpHacmNEO1+pI7BBMJv+fX6SaGJVEQ3Q6qrm4jaHxK+M";
        String decrypt = "h6hC50TrptIo+Yvu/HgDwZHGXimJ21uu83kQTnuEyJC6D93wyURyG1LJszz77joByB4CQi/ydQl1KXKBW/W4IFVO4hB/tnD383yz9N6rJE+DqB1Xoa45ZuNpZxz9LciMaV1ZmZprchhw1JVeQJMQ0CtBN3qVMgvTMkrJTjxAuIsJLjU7ZIQdDRilLmmHiDC169n5Ct596jxy1AKN4qNN+Isdws2yG0UPe88AuwQlXRpq4aXsxXL/Cof+nHQlyO6In62gyBTGRFYaWY1uzur8hh6HoldqKo9GKgMBfXy74rWEK/w7NqGDKCPvNcI+cQ5IEP5PqwI7ghDTxmU3c2Afjmkx8a6IaQoV+zybOAa+3/SifDW9adQQNTheSkH+VcJAggPkX9hQe9lIiyFK3wfpk7T45fgt+bPDNOqFBWhFQYoKTeWtXehBdJFTzVE5oAMExBiKuXMMEExMAOzkXE3clSeyLoBCTw5HgPRbm5qO+RcFoDDzDgcl2vR4Gyv69oIIFR1SQV8MIh8SiTQm04w4C8JkajTbRQwBaASv9MY4khkQMBtxbNMB0I1efK7FSt/xQh2FjShkdL8ooOPuKYbiYFfFz1lUZ6yPIVkdcVUSUQXSUsjUOy9YRAdAhzveOiNs2T7gw3uCpvQO+G45nFWFBMb+0478AhhKnZEv9x60jUtSqELzuuVJ+cCBOGN5m+2aAhOfT6vWvHI/QJzQjgpkNS5dmsEYN1Nyx0CuVRWEbkpdtcqL3uDJC6gMFU81M/c1X2bkD62TZKI+Txxzmx5FVtkTHqGhHRyI3Fbws9OV6jq335UvqpMXH+W92sbOL/9lWfZ7njUlnecCCrcYg4sOqA==";
//        String encrypt = RsaUtils.decryptByPrivateKey(vk, decrypt);
        String encrypt = RsaUtils.decrypt(decrypt, vk);
        System.out.println("加密后：" + encrypt);
    }

    public static void test3() throws Exception {
//        Map<String, String> keyMap = RsaUtils.genKeyPair();
//        String publicKey = keyMap.get("publicKey");
//        String privateKey = keyMap.get("privateKey");

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2FvRji0sdBV7guVrUflPV/zfTv8oarcKAiTNAznkSuU4JfqUgLd5mexAIt+NoF5dnTnZV8F1tWXy92XdmpfOAAgHxmyyGFM8QAp+2O8L/vA7wJyUwXkXy62demk/DuB+wylsKmwQNOAStR0SwssbNhe5Pyu76xk1N8k8/aJnZFQIDAQAB";
        String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALYW9GOLSx0FXuC5WtR+U9X/N9O/yhqtwoCJM0DOeRK5Tgl+pSAt3mZ7EAi342gXl2dOdlXwXW1ZfL3Zd2al84ACAfGbLIYUzxACn7Y7wv+8DvAnJTBeRfLrZ16aT8O4H7DKWwqbBA04BK1HRLCyxs2F7k/K7vrGTU3yTz9omdkVAgMBAAECgYEAr9uPf6wBiS+sqCCbZmnzEdQA2NJ7oDR8iqL3Cvnd2IV6ppTXaTKjfhoQLDtctyVBphYTF4Ci2n74iGpEdLCFb1FFe1/IorEcLSJ5ahiQidr0Ufc6XzF76yVyIkU5rjgIY8LMg0UUbdTssq0i56mXgUMzJp7pBwrihkc8aG8igcECQQD0VfoPKRyjfyjKx8ldzBCF2woX4dK1brSkIz5LWk6PjHc5JsDy7KFkn4riVPbtwrymQ1r86ZafdqJgZBMoUbS5AkEAvshEwvrJbjXe/R8Zogl3FJ+kv7yMiYftABVxwvNXLEDUKfGuQYeAfxIbqipxMvXEzsq/aFEIqa8FKMOPq22RPQJAJJ9KZsFTwJHLrHE7lmqCw31sSt4XNgiM3NlHegXkJpH4QMG1Q/QB0NI0/+2aQVLh8c3Aso3UfLxMZEQ7ttxgSQJBAIPMYMx+aoerybf+Mzwg49Yoj60x+bjNYWpsZiHy8CcPRkMPxn1YuemPPfNpzLgS13qw0FilmqF22s6Vg3w/flUCQQCTt1zHJDj0WbEDtaVxv0XwlYjXo0JGPRtSq6RUAsznWpHacmNEO1+pI7BBMJv+fX6SaGJVEQ3Q6qrm4jaHxK+M";
//        System.out.println("publicKey = " + publicKey);
//        System.out.println("privateKey = " + privateKey);

        Authorization authorization = new Authorization();
        authorization.setVersion("专业版");
        authorization.setEnvironment("prod");
        authorization.setLicenseStart(new Date());
        Calendar myCalendar = new GregorianCalendar(2025, 8, 27);
        authorization.setLicenseExpire(myCalendar.getTime());

        authorization.setTemplateUseFlag(true);
        authorization.setTemplateCeiling(1000);
        authorization.setTemplateScope("");

        authorization.setBusinessLineUseFlag(true);
        authorization.setBusinessLineCeiling(1000);

        authorization.setApiAuthorizationUseFlag(true);
        authorization.setApiAuthorizationCeiling(2);

        authorization.setCoreFirmUseFlag(true);
        authorization.setCoreFirmCeiling(2);

        authorization.setFaceRecognitionUseFlag(true);

        authorization.setDocumentsCeiling(5);

        authorization.setPagingSealUseFlag(true);

        authorization.setWebsiteCustomizateUseFlag(true);

        String originValue = JSONObject.toJSON(authorization).toString();

        //System.out.println("加密前：" + originValue);
        String encrypt = RsaUtils.encrypt(originValue, publicKey);
        System.out.println("加密后：" + encrypt);
//        String decrypt = RsaUtils.decrypt(encrypt, privateKey);
//        System.out.println("解密后：" + decrypt);
//        Authorization grant = JSONObject.parseObject(decrypt, Authorization.class);
//        System.out.println("解密后对象：" + grant);
//        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }

    /**
     * 公钥加密私钥解密
     */
    private static void test1() throws Exception {
        String sec = "2c6793ff2585314aa97ffa987e397e955f9efd1fc3e66bf73dfd283aae1f140ad3003dab207704b3a9f5af2e837c1f32c07b9bcee244aa9e5024ebccdc71f5d0d8e4168fe7a5af81cce22e36b9e42cfcba1afd1361812499c6f2e11e539ff766d649c4ae847518762e3e59c20bc629fc0e760f57ffde5c8662b1cde61af3462c";
        String pk = "30819f300d06092a864886f70d010101050003818d003081890281810095d77b9ab8b17cceda10c4d11648b6196a3897b196a79ca81bc5e21d4bf90f7b7559827e831cdb48011e54c64a7a872bce4aa369d124b4c4b3d0f83cb63f8b210e7c25be346ce14e5fe44404788c3360651f29e4894e1412a86638cf22dc81c5ece53ddf0b5e6409cd6a7e509f34558644b4be703d9d55c9b6cc5dee4b090ba90203010001";

        String text2 = decryptByPublicKeyByte(pk, sec);


        System.out.println("***************** 公钥加密私钥解密开始 *****************");
//        String text1 = encryptByPublicKey(keyPair.getPublicKey(), RsaUtils.SRC);
//
//        System.out.println("加密前：" + RsaUtils.SRC);
//        System.out.println("加密后：" + text1);
//        System.out.println("解密后：" + text2);
//        if (RsaUtils.SRC.equals(text2)) {
//            System.out.println("解密字符串和原始字符串一致，解密成功");
//        } else {
//            System.out.println("解密字符串和原始字符串不一致，解密失败");
//        }
//        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception /
     */
    private static void test2(RsaKeyPair keyPair) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        Authorization authorization = new Authorization();
        authorization.setVersion("专业版");
        authorization.setEnvironment("prod");
        authorization.setLicenseStart(new Date());
        authorization.setLicenseExpire(new Date());

        authorization.setTemplateUseFlag(true);
        authorization.setTemplateCeiling(2);
        authorization.setTemplateScope("base");

        authorization.setApiAuthorizationUseFlag(true);
        authorization.setApiAuthorizationCeiling(2);


        String text1 = encryptByPrivateKey(keyPair.getPrivateKey(), JSONObject.toJSON(authorization).toString());
        String text2 = decryptByPublicKey(keyPair.getPublicKey(), text1);
        System.out.println("加密前：" + JSONObject.toJSON(authorization).toString());
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        Authorization grant = JSONObject.parseObject(text2, Authorization.class);
        System.out.println("解密后对象：" + grant);
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText 公钥
     * @param text          待解密的信息
     * @return /
     * @throws Exception /
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    public static String decryptByPublicKeyByte(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Hex.decode(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Hex.decode(text));
        return new String(result);
    }


    /**
     * 私钥加密
     *
     * @param privateKeyText 私钥
     * @param text           待加密的信息
     * @return /
     * @throws Exception /
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText 私钥
     * @param text           待解密的文本
     * @return /
     * @throws Exception /
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        if (MyStringUtils.isBlank(privateKeyText) || MyStringUtils.isBlank(text)) {
            return text;
        }
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText 公钥
     * @param text          待加密的文本
     * @return /
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        if (MyStringUtils.isBlank(publicKeyText) || MyStringUtils.isBlank(text)) {
            return text;
        }
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return /
     * @throws NoSuchAlgorithmException /
     */
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        System.out.println("公钥：" + publicKeyString);
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        System.out.println("私钥：" + privateKeyString);
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }


    /**
     * RSA密钥对对象
     */
    public static class RsaKeyPair {

        private final String publicKey;
        private final String privateKey;

        public RsaKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

    }


    /**
     * 获取公钥和私钥对，key为公钥，value为私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = null;
        String privateKeyString = null;

        try {
            publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()), "UTF-8");
            privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()), "UTF-8");
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", publicKeyString);
        keyPairMap.put("privateKey", privateKeyString);
        return keyPairMap;
    }

    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(1, pubKey);
        // 分段加密
        // URLEncoder编码解决中文乱码问题
        byte[] data = URLEncoder.encode(str, "UTF-8").getBytes("UTF-8");
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        String outStr = Base64.encodeBase64String(enBytes);
        return outStr;
    }

    /**
     * 公钥分段解密
     *
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        // 获取公钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, priKey);
        byte[] data = Base64.decodeBase64(str.getBytes("UTF-8"));

        // 返回UTF-8编码的解密信息
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return URLDecoder.decode(new String(decryptedData, "UTF-8"), "UTF-8");
    }
}