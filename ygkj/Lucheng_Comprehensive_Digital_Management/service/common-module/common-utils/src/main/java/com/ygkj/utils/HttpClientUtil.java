package com.ygkj.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @Auther: huang.zh
 * @Date: 2019/9/9 11:12
 * @Description:发起网络请求工具类
 */
public class HttpClientUtil {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String HTTPS = "https";

    public static String httpPost (String url, String json, Map<String,String> headers) throws Exception {
        //初始HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Post对象
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        if (headers != null && !headers.isEmpty()){
            headers.forEach((key,value)->httpPost.setHeader(key,value));
        }
        //写入JSON数据
        httpPost.setEntity(new StringEntity(json,DEFAULT_ENCODING));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String result = EntityUtils.toString(entity,DEFAULT_ENCODING);
        return result;
    }


    public static String httpPostHttps (String url, String json, Map<String,String> headers) throws Exception {
        //初始HttpClient
        CloseableHttpClient httpClient;
        if (url.startsWith(HTTPS)){
            httpClient = SSLClientCustom.getHttpClinet();
        }else {
            httpClient = HttpClients.createDefault();
        }
        //创建Post对象
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        if (headers != null && !headers.isEmpty()){
            headers.forEach((key,value)->httpPost.setHeader(key,value));
        }
        //写入JSON数据
        httpPost.setEntity(new StringEntity(json,DEFAULT_ENCODING));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String result = EntityUtils.toString(entity,DEFAULT_ENCODING);
        return result;
    }



    public static String httpPostByJson(String url, Map<String,Object> params){
        Map<String,String> map = new HashMap<>(params.size());
        params.forEach((key,value) -> map.put(key,String.valueOf(value)));
        try {
            Map<String,String> headers = new HashMap<>();
            headers.put("Content-Type","application/json");
            return httpPost(url,map, Collections.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    public static String httpPost(String url, Map<String,String> params, Map<String,String> headers) throws Exception {
        //初始HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Post对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //设置请求头
        if (headers != null && !headers.isEmpty()){
            headers.forEach((key,value)->httpPost.setHeader(key,value));
        }
        List<BasicNameValuePair> pairs =new ArrayList<>();
        params.forEach((key,value) -> {
            pairs.add(new BasicNameValuePair(key,value));
        });
        httpPost.setEntity(new UrlEncodedFormEntity(pairs));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String result = EntityUtils.toString(entity,DEFAULT_ENCODING);
        return result;
    }



    public static String httpPostHttps(String url, Map<String,String> params, Map<String,String> headers) throws Exception {
        //初始HttpClient
        CloseableHttpClient httpClient;
        if (url.startsWith(HTTPS)){
            httpClient = SSLClientCustom.getHttpClinet();
        }else {
            httpClient = HttpClients.createDefault();
        }
        //创建Post对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //设置请求头
        if (headers != null && !headers.isEmpty()){
            headers.forEach((key,value)->httpPost.setHeader(key,value));
        }
        List<BasicNameValuePair> pairs =new ArrayList<>();
        params.forEach((key,value) -> {
            pairs.add(new BasicNameValuePair(key,value));
        });
        httpPost.setEntity(new UrlEncodedFormEntity(pairs));
        //发起请求，获取response对象
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取返回数据实体对象
        HttpEntity entity = response.getEntity();
        //转为字符串
        String result = EntityUtils.toString(entity,DEFAULT_ENCODING);
        return result;
    }



    /**
     * 普通GET请求
     * @param url
     * @return
     */
    public static String httpGet(String url){
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), DEFAULT_ENCODING);
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发起GET请求，可拼装参数
     * @param url
     * @param headers
     * @param params
     * @param encoding
     * @return
     */
    public static String sendGet(String url, Map<String,String> headers, Map<String,Object> params, String encoding){
        // 请求返回结果
        String resultJson;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        try {
            if (headers != null) {
                for (Map.Entry<String, String> entry: headers.entrySet()){
                    httpGet.setHeader(entry.getKey(),entry.getValue());
                }
            }
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            // 封装参数
            if(params != null){
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == org.apache.http.HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
                // 获取响应结果
                return resultJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 发起GET请求，可拼装参数
     * @param url
     * @param headers
     * @param params
     * @param encoding
     * @return
     */
    public static String sendGetHttps(String url, Map<String,String> headers, Map<String,Object> params, String encoding){
        // 请求返回结果
        String resultJson;
        // 创建Client
        CloseableHttpClient client = null;

        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        try {
            if (url.startsWith(HTTPS)){
                client = SSLClientCustom.getHttpClinet();
            }else {
                client = HttpClients.createDefault();
            }
            if (headers != null) {
                for (Map.Entry<String, String> entry: headers.entrySet()){
                    httpGet.setHeader(entry.getKey(),entry.getValue());
                }
            }
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            // 封装参数
            if(params != null){
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == org.apache.http.HttpStatus.SC_OK) {
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
                // 获取响应结果
                return resultJson;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

