package com.ygkj.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lxl
 * @date 2021/4/27
 * @Description: 根据传值获取不同的 appKey 和 appSecret
 */
@Component
@PropertySource("classpath:judge.properties")
public class JudgeSystemAuthMap implements InitializingBean {
    @Value("#{'${judge.map}'}")
    private String jsonString;

    JSONObject rules;

    public Map<String, String> getMap(String type) {
        Map<String, String> map = rules.getObject(type, Map.class);
        return map;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(jsonString)) {
            rules = JSONObject.parseObject(jsonString);
        }
        jsonString = null; //help gc
    }
}
