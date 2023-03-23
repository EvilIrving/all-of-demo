package com.ygkj.river.vo.request;

import com.ygkj.entity.PageEntity;
import com.ygkj.utils.PageUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/10/9
 */
@Data
@ApiModel("入河排污口入参")
public class RiverOutletQueryVo extends PageEntity {

    private static final long serialVersionUID = -7209814652509037702L;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("排污口规模筛选")
    String outletGrad;

    @ApiModelProperty("排放方式")
    String outletType;

    @ApiModelProperty("入河排污口名称")
    String outletName;

    @ApiModelProperty("是否查找最新的入河排污口")
    boolean computeNearestWain;
}
