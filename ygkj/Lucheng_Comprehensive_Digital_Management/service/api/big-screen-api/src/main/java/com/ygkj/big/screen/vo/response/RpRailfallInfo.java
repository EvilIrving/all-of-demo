package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.StPptnR;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RpRailfallInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名")
    private String stName;
    @ApiModelProperty("流域")
    private String bas;
    @ApiModelProperty("行政区划")
    private String areaCode;
    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty(value = "1正常，0不正常", hidden = true)
    private String status = "1";

    @ApiModelProperty("雨量信息")
    private List<StPptnR> stPptnRS;
}
