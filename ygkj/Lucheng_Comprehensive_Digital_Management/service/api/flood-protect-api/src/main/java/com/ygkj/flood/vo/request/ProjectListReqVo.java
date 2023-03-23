package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ProjectListReqVo {

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("工程名称")
    private String projectName;

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
