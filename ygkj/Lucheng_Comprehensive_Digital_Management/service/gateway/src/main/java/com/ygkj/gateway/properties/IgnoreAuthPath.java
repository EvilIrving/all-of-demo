package com.ygkj.gateway.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/11 11:43
 * @Description: 读取配置文件中忽略token拦截和鉴权的请求路径
 */
@Component
@PropertySource("classpath:ignore.properties")
public class IgnoreAuthPath {
    @Value("#{'${ignore.path}'.split(',')}")
    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
