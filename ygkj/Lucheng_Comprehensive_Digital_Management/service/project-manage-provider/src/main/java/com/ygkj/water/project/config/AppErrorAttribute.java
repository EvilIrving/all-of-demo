package com.ygkj.water.project.config;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class AppErrorAttribute extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, false); // 这里参数可以配置为false
//        map.put("url","www.blogdgw.com");
//        map.put("ext",webRequest.getAttribute("ext",0));
        // 禁止trace 覆盖
//        map.remove("trace");
//        map.remove("error");
        return map;
    }
}
