package com.ygkj.water.project.enums;

import lombok.Getter;

@Getter
public enum StormTideEnum {

    TYPHOON("http://www.nmefc.cn/fengbaochao/taifengfengbaochaodetail.aspx",
            "storm_typhoon","http://www.nmefc.cn/chanpin/hyzh/surge/c-surge/"),

    TEMPERATE("http://www.nmefc.cn/fengbaochao/wendaifengbaochaodetail.aspx",
            "storm_temperate","http://www.nmefc.cn/chanpin/hyzh/surge/c-surge1/");



    String url;
    String redisKey;
    String detailUrl;

    StormTideEnum(String url, String redisKey,String detailUrl) {
        this.url = url;
        this.redisKey = redisKey;
        this.detailUrl = detailUrl;
    }
}
