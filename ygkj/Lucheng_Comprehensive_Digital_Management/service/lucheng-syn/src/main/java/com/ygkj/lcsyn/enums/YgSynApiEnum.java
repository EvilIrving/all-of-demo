package com.ygkj.lcsyn.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-07-15 10:39
 * @description
 */
@Getter
public enum YgSynApiEnum {

    SYN_TOKEN("获取token","https://synapi.ygwjg.com/SynApi/login/getToken"),
    SYN_PP("推送雨量数据","https://synapi.ygwjg.com/SynApi/waterAndRain/uploadSynPrecipitation1"),
    SYN_RSVR("推送水位数据","https://synapi.ygwjg.com/SynApi/waterAndRain/uploadSynThereservoirSituation1");

    private String desc;

    private String url;

    YgSynApiEnum(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }


}
