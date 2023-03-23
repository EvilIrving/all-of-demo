package com.ygkj.digitization.vo.request;


import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

public class BaseReqVo {

    @ApiModelProperty(value = "流域")
    private String bas;

    @ApiModelProperty(value = "行政区划")
    private String adcd;

    public String getBas() {
        return bas;
    }

    public void setBas(String bas) {
        this.bas = bas;
    }

    public String getAdcd() {
        return adcd;
    }

    public void setAdcd(String adcd) {
        if (!StringUtils.isEmpty(adcd)) {
            if (adcd.endsWith("00000000")) {
                adcd = adcd.substring(0, 4);
            } else if (adcd.endsWith("000000")) {
                adcd = adcd.substring(0, 6);
            }
            adcd += "%";
        }
        this.adcd = adcd;
    }
}
