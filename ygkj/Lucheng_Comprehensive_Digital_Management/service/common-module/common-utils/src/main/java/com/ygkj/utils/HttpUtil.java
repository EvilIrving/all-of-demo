package com.ygkj.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

    public static Map<String, String> getParametersFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String type = request.getContentType();
        Map<String, String> parameters = new HashMap<>();
        if ("application/x-www-form-urlencoded".equals(type)) {
            //表单提交
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String key = String.valueOf(enu.nextElement());
                String value = request.getParameter(key);
                parameters.put(key, value);
            }
        } else {
            //text/plain、application/json
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            if (jsonObject != null) {
                Map<String, Object> map = jsonObject.getInnerMap();
                map.forEach((key, value) -> {
                    parameters.put(key, String.valueOf(value));
                });
            }
        }
        return parameters;
    }

    public static Map<String, Object> convertHttpRequestParameters(HttpServletRequest request) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            StringBuffer sb = new StringBuffer();
            String line;
            try {
                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null)
                    sb.append(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将空格和换行符替换掉避免使用反序列化工具解析对象时失败
            String jsonString = sb.toString().replaceAll("\\s", "").replaceAll("\n", "");
            if (StringUtils.isBlank(jsonString)) {
                return Collections.emptyMap();
            }
            return JSONObject.parseObject(jsonString).getInnerMap();
        } else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, Object> parameters = new HashMap<>(parameterMap.size());
            parameterMap.forEach((key, values) -> {
                parameters.put(key, values[0]);
            });
            return parameters;
        }
        return Collections.emptyMap();
    }
}
