package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StListReqVo extends BaseReqVo {

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("测站类型 全部不传 水库RR 闸坝DD 河道ZZ 潮位TT")
    private String stType;
}
