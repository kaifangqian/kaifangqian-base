package org.resrun.sdk.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HttpUtils {

//    public static final String TOKEN = "SESSION";
    public static final String TOKEN = "token";


    public static void main(String[] args) {
        String url = "https://scpic.chinaz.net/files/default/imgs/2023-01-03/e0f209137275d9b1.jpg";
        download(url);
    }

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

    public static byte[] download(String url) {

        byte[] fileByte = null ;

        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse httpResponse = null;
        try {
//            File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
//            Path targetPath = Paths.get(homeDirectory.getAbsolutePath(), url.substring(url.lastIndexOf("/")));

            // 1、创建 CloseableHttpClient 同步请求对象
            httpClient = HttpClientBuilder.create().build();
            // 2、HttpGet(final String uri)：创建 http get 请求对象
            httpGet = new HttpGet(url);
            // 3、设置超时时间、请求时间、socket 时间都为 15 秒，允许重定向
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(15000)
                    .setConnectionRequestTimeout(15000)
                    .setSocketTimeout(15000)
                    .setRedirectsEnabled(true)
                    .build();
            httpGet.setConfig(requestConfig);
            // 4、CloseableHttpResponse execute(final HttpUriRequest request)：执行请求
            // 如果连接不上服务器，则抛出:java.net.ConnectException: Connection refused: connect
            httpResponse = httpClient.execute(httpGet);
            // 5、获取响应结果, 状态码 200 表示请求成功
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
//                IOUtils.write(bytes,new FileOutputStream(new File("/Users/gongfenglai/Desktop/test/image/e0f209137275d9b1.png")));
//                Files.deleteIfExists(targetPath);
//                Files.copy(inputStream, targetPath);
//                System.out.println(targetPath);
                if(inputStream != null){
                    fileByte = IOUtils.toByteArray(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /**
             * consume(final HttpEntity entity)：确保实体内容已完全使用，并且内容流（如果存在）已关闭。
             *  httpResponse.close()：关闭此流并释放与之关联的所有系统资源。如果流已关闭，则调用此方法无效。
             */
            try {
                if (httpResponse != null) {
                    EntityUtils.consume(httpResponse.getEntity());
                    httpResponse.close();
                }
                //无论成功与否,都要释放连接并终止
                if (httpGet != null && !httpGet.isAborted()) {
                    httpGet.releaseConnection();
                    httpGet.abort();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileByte ;
    }


}
