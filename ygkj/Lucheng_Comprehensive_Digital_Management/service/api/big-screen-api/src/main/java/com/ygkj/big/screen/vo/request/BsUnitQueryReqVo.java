package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
@Data
@ApiModel("大屏组件查询入参")
public class BsUnitQueryReqVo extends PageEntity {

    private static final long serialVersionUID = -3338128937773414591L;

    @ApiModelProperty("组件id")
    String id;

    @ApiModelProperty("组件名称")
    String unitName;

    @ApiModelProperty("大屏名称")
    String bsName;
}
