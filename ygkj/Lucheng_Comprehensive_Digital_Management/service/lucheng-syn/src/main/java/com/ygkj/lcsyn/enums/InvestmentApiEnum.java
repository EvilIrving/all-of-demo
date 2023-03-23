package com.ygkj.lcsyn.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-07-07 14:58
 * @description
 */
@Getter
public enum InvestmentApiEnum {

    INVESTMENT_INFO("投资项目办件信息","https://api-dshare.wenzhou.gov.cn/webapi2/LC-tzxmbjxx"),
    INVESTMENT_PROJECT("投资项目基本信息","https://api-dshare.wenzhou.gov.cn/webapi2/LC-tzxmjbxx"),
    INVESTMENT_FILE("投资项目批文信息","https://api-dshare.wenzhou.gov.cn/webapi2/LC-tzxbpwxx");


    private String desc;

    private String url;

    InvestmentApiEnum(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }


}
