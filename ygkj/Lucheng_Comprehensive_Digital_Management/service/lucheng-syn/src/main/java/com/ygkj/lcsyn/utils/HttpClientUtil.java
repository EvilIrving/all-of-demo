package com.ygkj.lcsyn.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: huang.zh
 * @Date: 2019/9/9 11:12
 * @Description:发起网络请求工具类
 */
public class HttpClientUtil {

    private static RequestConfig requestConfig;

    public static String httpPost (String url, String json, Map<String,String> headers) throws Exception {
        try {
            //初始HttpClient
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //创建Post对象
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            if (headers != null && !headers.isEmpty()){
                headers.forEach((key,value)->httpPost.setHeader(key,value));
            }
            //写入JSON数据
            if(StringUtils.isNotEmpty(json)){
                httpPost.setEntity(new StringEntity(json));
            }
            //发起请求，获取response对象
            CloseableHttpResponse response = httpClient.execute(httpPost);
            //获取返回数据实体对象
            HttpEntity entity = response.getEntity();
            //转为字符串
            String result = EntityUtils.toString(entity,"UTF-8");
            return result;
        } catch (Exception e) {
            return "";
        }

    }

    public static String httpPost(String url, Map<String,String> params, Map<String,String> headers) throws Exception {
        //初始HttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建Post对象
        HttpPost httpPost = new HttpPost(url);
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
        String result = EntityUtils.toString(entity,"UTF-8");
        return result;
    }

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
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送 POST 请求，K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static JSONObject doPost(String apiUrl, Map<String, String> params) {
        SSLClient httpClient = null;
        try {
            httpClient = new SSLClient();
            String httpStr = null;
            HttpPost httpPost = new HttpPost(apiUrl);
            CloseableHttpResponse response = null;

            try {
                httpPost.setConfig(requestConfig);
                List<NameValuePair> pairList = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                    pairList.add(pair);
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
                response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (response != null) {
                    try {
                        EntityUtils.consume(response.getEntity());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
            return JSON.parseObject(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject("1");
    }

    /**
     * 发送 POST 请求，K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @param k 参数header键
     * @param v 参数header值
     * @return
     */
    public static JSONObject doPost(String apiUrl, Map<String, String> params, String k, String v) {
        SSLClient httpClient = null;
        try {
            httpClient = new SSLClient();
            String httpStr = null;
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader(k, v);
            CloseableHttpResponse response = null;

            try {
                httpPost.setConfig(requestConfig);
                List<NameValuePair> pairList = new ArrayList<>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                    pairList.add(pair);
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
                response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (response != null) {
                    try {
                        EntityUtils.consume(response.getEntity());
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
            return JSON.parseObject(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject("1");
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
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)// 请求超时设置为10s
                .setConnectTimeout(2000)// 连接超时设置为2s
                .build();
        return sendGet(url,headers,params,encoding,requestConfig);
    }

    /**
     * 发起GET请求，可拼装参数
     * @param url
     * @param headers
     * @param params
     * @param encoding
     * @return
     */
    public static String sendGet(String url, Map<String,String> headers, Map<String,Object> params, String encoding,RequestConfig requestConfig){
        // 请求返回结果
        String resultJson;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();

        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        if (requestConfig!=null) {
            httpGet.setConfig(requestConfig);
        }
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


}

