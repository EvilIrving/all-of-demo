package com.ygkj.digitization.vo.request;

import com.ygkj.digitization.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@ApiModel("分析列表入参")
public class AnalysisListReqVo extends PageEntity {

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("名称")
    private String name;

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
