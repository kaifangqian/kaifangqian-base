/**
 * @description API签名生成类
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.Consts;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;
import java.net.URLEncoder;

public class SignGenerator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        // 输入参数
        String appId = "123456";
        String timestamp = "1735664461000";
        String nonce = "013718f2720d402a818ebccff28cfa45";
        String uri = "/api/contracts/path?paramA=1&paramB=2";
        String jsonBody = "{\"unionId\":\"11\",\"account\":\"est reprehenderit laborum in incididunt\",\"userIdentInfo\":{\"name\":\"术品事能土素收\",\"idCard\":\"59\",\"idCardType\":55,\"mobile\":\"18117517665\",\"bankCard\":\"Ut non consectetur\"},\"userIdentConfig\":{\"personVerifyMethods\":[\"aliquip\",\"eu\",\"ut deserunt exercitation esse nisi\"],\"unmodifiableParams\":[\"aliqua consequat exercitation\"]},\"callbackPage\":\"in in sed labore\",\"authCallbackUrl\":\"http://jgo.pl/tlwgboss\"}";

        // 1. 计算biz_content
        String canonicalJson = canonicalizeJson(jsonBody);
        String bizContent = computeBizContent(canonicalJson);

        // 2. 组装签名字符串（含URL编码）
        String signString = buildSignString(appId, timestamp, nonce, uri, bizContent);

        // 3. 加载私钥（示例私钥，需替换实际值）
        String privateKeyPem = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCM19A45gipMuzcG+otPFl+rTZLmoJS0t5QIJoAwwBzCp0eqMFlPqeTZkxwsvT3ZhirHk7KxAHBEeQJNtKhVce8byicKot8vA3lDl0lQ4qY3dEqKvp7jJsPPSnNwwbgNjbV9QXwezxKzD0cQntLWvI2GaHEtGRCCEzB9BBmK5HCwRl6FG+3ubBRO8Yv5LTJbcowlodqF+7eHXOXXqJ4DMA87VRAVMeFWqwUi0gToxBYVYys9aD+5GSi1mD28XyEnVocjSw7vdLLFsBComAXZBgfrATYwdiXNA9l32mECm9DGZoyTTBjoHcQp1WfM40Q9kZldYvfjVNtRjN+RPoRfThxAgMBAAECggEAfxmK2K4OhtJkGcY/MA9UHBMn+lY24ZNV+C1TjPhtAWREohRcYoeGJmic9t8oXdKO1G2HYUg85QTGKuKEdf+5Pf16QmPKeQgj6d9qg2ROQOF8Dne2j6Q1kiz5+qvTGqcSGUcC0yADKuThCQ2dwkUSa2v6Jk88YrKmwikEW49Z/Mt68Zft493i4YQaZIfQ57RepRuKhyQ+bPSTZE+s7GeCe5PHzyYAO/7FIuoa6VzMfoeLmxQy4W8Lib1ifuA4HTDpXUKRQ6Rzxh8NN11fc8WiOr6VIV7uSwGSTRDZpr/1QBzkfkNYf9J+A+ncejc1Hn+IZcH5MaXXNlCtrfH9229wAQKBgQDBfSY20kdLO/giVGyGPYXnPCUcJn5t4bNIUMp1aWDvQuRy3PkNrU0P01yRHPLWeruD9aD6UHfcy/niVUpU9YYsJuglvm2rXZ0yyHC+Xpw0DwoOVES14fCxRruMOt5K/8NThLoj3p7+trGsGDFD5qPlHIFMZUhs2VTyxs4hMS5+8QKBgQC6WH5FPgKgyQRAwaK762Wn47d3Jbh0eKPkrn6P/Zsn+eUhDqqajp55UNi09R0xpyXuxJyOsauyjxBQshKKvNJckZyd9CjPVIexTrByTT7zz4cMaV/Zlxf485/XDe7xxAQXYQ4C8JD4rtWsJDlJlQbZ8UXoE6171+1kST46+zVRgQKBgQCOIe7GaqVwgLcAEOA/pEYO3MGt8iEBesCGKMLwiN5qI8tJDUXuZBWtwGfJOWixYSsJ/o+oESBbk4MWXeziiBFtsY7q2v4Y2gW13kPjtAAuxbVvXTu0GCsFLp5qnnvYwBSt71wFKW3GUlIlxwe5h11x2gSpJ+WR//xE8wFMpKIxIQKBgFDTVWGfNfCbgBa+ZmFwBtW9dbReThVmxl/IN+korQW35GRkQidruN0R7gz/TDxfLZrU+LsD4V8/omn98wDuSagRGIeCgbj8Bycm5G2Ph8CkwQTnJ8XvPVyj/y5+Qqfmx4eEd1lbEuZqnw9dXuPFmgUqG5JABr1yuOsIwc8/aq8BAoGAJHNqUbV08txEDSVH5HcrkR+bJKqwT0UfvMt/qSyXWC1+EWSM//tXpMvdQHDdu0VOJEc0wfbsHLREnsBnsFGUNX74kty+i71M/eCjItsP8oyz0r+GgdtaSnjKwg/OYYrpn7Px1viDy2dS+xD4z2+UWgz0c1JTmV3E69vhecLp2NI="; // 签名私钥
        PrivateKey privateKey = loadPrivateKey(privateKeyPem);

        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjNfQOOYIqTLs3BvqLTxZfq02S5qCUtLeUCCaAMMAcwqdHqjBZT6nk2ZMcLL092YYqx5OysQBwRHkCTbSoVXHvG8onCqLfLwN5Q5dJUOKmN3RKir6e4ybDz0pzcMG4DY21fUF8Hs8Ssw9HEJ7S1ryNhmhxLRkQghMwfQQZiuRwsEZehRvt7mwUTvGL+S0yW3KMJaHahfu3h1zl16ieAzAPO1UQFTHhVqsFItIE6MQWFWMrPWg/uRkotZg9vF8hJ1aHI0sO73SyxbAQqJgF2QYH6wE2MHYlzQPZd9phApvQxmaMk0wY6B3EKdVnzONEPZGZXWL341TbUYzfkT6EX04cQIDAQAB";


        // 4. 生成签名
        String sign = signData(signString, privateKey);
        System.out.println("\n签名值sign:\n" + sign);

        String encoding = Consts.UTF_8.name();
        boolean isValid = ApiSignature.check(signString.toString(), sign, publicKey, encoding, "RSA2");

        System.out.println("\n验签:\n" + isValid);
    }

    /**
     * URL编码（空格转%20，保留~不编码）
     */
    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name())
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            throw new RuntimeException("URL编码失败", e);
        }
    }

    /**
     * 规范化JSON（递归排序键）
     */
    public static String canonicalizeJson(String json) throws Exception {
        JsonNode node = mapper.readTree(json);
        JsonNode sortedNode = sortJsonNode(node);
        return mapper.writeValueAsString(sortedNode);
    }

    private static JsonNode sortJsonNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode sortedNode = mapper.createObjectNode();
            TreeMap<String, JsonNode> fields = new TreeMap<>();
            node.fields().forEachRemaining(entry ->
                    fields.put(entry.getKey(), sortJsonNode(entry.getValue()))
            );
            fields.forEach(sortedNode::set);
            return sortedNode;
        } else if (node.isArray()) {
            ArrayNode arrayNode = mapper.createArrayNode();
            node.forEach(element -> arrayNode.add(sortJsonNode(element)));
            return arrayNode;
        }
        return node;
    }

    /**
     * 计算biz_content（SHA-256 + Base64）
     */
    public static String computeBizContent(String canonicalJson) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(canonicalJson.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 组装签名字符串（按字典序排序并URL编码）
     */
    public static String buildSignString(String appId, String timestamp,
                                          String nonce, String uri, String bizContent) {
        Map<String, String> params = new TreeMap<>();
        params.put("appId", urlEncode(appId));
        if(bizContent == null){
            params.put("biz_content", "");
        }else{
            params.put("biz_content", urlEncode(bizContent));
        }

        params.put("nonce", urlEncode(nonce));
        params.put("timestamp", urlEncode(timestamp));
        params.put("uri", urlEncode(uri));

        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * 加载RSA私钥（PKCS#8格式）
     */
    private static PrivateKey loadPrivateKey(String privateKeyPem) throws Exception {
        String pemContent = privateKeyPem
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(pemContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }

    /**
     * 使用私钥签名数据
     */
    private static String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
