package com.kaifangqian.modules.cert.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Map;

public class HttpUtils {

//    public static final String TOKEN = "SESSION";
    public static final String TOKEN = "token";

    public static String executeGetMap(String url,String jsonParam,Map<String,String> headers) {
        String result = "" ;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient http = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        if(headers != null){
            for(String key : headers.keySet()){
                String value = headers.get(key);
                httpGet.addHeader(key,value);
            }
        }

//        httpGet.setHeader("Cookie", cookie);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = (CloseableHttpResponse) http.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (http != null) {
                    ((CloseableHttpClient) http).close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result ;
    }

    public static String executeGet(String url,String jsonParam,String cookie) {
        String result = "" ;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient http = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(TOKEN, cookie);

//        httpGet.setHeader("Cookie", cookie);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = (CloseableHttpResponse) http.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (http != null) {
                    ((CloseableHttpClient) http).close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result ;
    }

//    public static CloseableHttpResponse execute(String url,String jsonParam,String cookie) throws Exception{
//        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
//        CloseableHttpClient http = HttpClientBuilder.create().build();
//        // 声明httpPost请求
//        HttpPost httpPost = new HttpPost(url);
//        StringEntity entity = new StringEntity(jsonParam, "utf-8");//解决中文乱码问题
//        entity.setContentEncoding("UTF-8");
//        entity.setContentType("application/json");
//        httpPost.setEntity(entity);
////        httpPost.setHeader("Cookie",cookie);
//        // 响应模型
//        CloseableHttpResponse response = null;
//        HttpEntity responseEntity = null;
//        try {
//            // 由客户端执行(发送)Get请求
//            response = (CloseableHttpResponse) http.execute(httpPost);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 释放资源
//                if (http != null) {
//                    ((CloseableHttpClient) http).close();
//                }
//                if (response != null) {
//                    response.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return response;
//    }


    public static String executePost(String url, String jsonParam, Map<String,String> headers) throws Exception{
        String result = "" ;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient http = HttpClientBuilder.create().build();
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonParam, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        if(headers != null){
            for(String key : headers.keySet()){
                String value = headers.get(key);
                httpPost.addHeader(key,value);
            }
        }
        // 响应模型
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            // 由客户端执行(发送)Get请求
            response = (CloseableHttpResponse) http.execute(httpPost);
            // 从响应模型中获取响应实体
            responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (http != null) {
                    ((CloseableHttpClient) http).close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String executePost(String url,String jsonParam,String cookie) throws Exception{
        String result = "" ;
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient http = HttpClientBuilder.create().build();
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonParam, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        if(cookie != null && cookie.length() > 0){
            httpPost.setHeader(TOKEN,cookie);
        }

//        httpPost.setHeader("Cookie",cookie);
//        Cookie c = new Cookie("SESSION",cookie);

//        httpPost.setHeader("Cookie",cookie);
        // 响应模型
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            // 由客户端执行(发送)Get请求
            response = (CloseableHttpResponse) http.execute(httpPost);
            // 从响应模型中获取响应实体
            responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (http != null) {
                    ((CloseableHttpClient) http).close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
