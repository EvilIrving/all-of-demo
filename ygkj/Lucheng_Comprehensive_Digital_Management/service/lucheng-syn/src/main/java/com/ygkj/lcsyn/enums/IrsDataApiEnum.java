package com.ygkj.lcsyn.enums;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lxl
 * @create 2022-06-17 13:39
 * @description
 */
@Getter
public enum IrsDataApiEnum {

    INVESTMENT_PROJECT("在线平台-投资项目基本信息共享接口","https://interface.zjzwfw.gov.cn/gateway/api/001003002/dataSharing/603Y3h4ZcD81TEH8.htm"),
    APPROVAL_SHARING("在线平台-投资项目办件信息与批文共享接口","https://interface.zjzwfw.gov.cn/gateway/api/001003002/dataSharing/462FVD4GpaMJB289.htm"),
    REFRESH_TOKEN_BY_KEY("首次刷新获取请求秘钥","https://interface.zjzwfw.gov.cn/gateway/app/refreshTokenByKey.htm"),
    REFRESH_TOKEN_BY_SEC("后续刷新获取请求秘钥","https://interface.zjzwfw.gov.cn/gateway/app/refreshTokenBySec.htm");


    private String desc;

    private String url;

    IrsDataApiEnum(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }
}
