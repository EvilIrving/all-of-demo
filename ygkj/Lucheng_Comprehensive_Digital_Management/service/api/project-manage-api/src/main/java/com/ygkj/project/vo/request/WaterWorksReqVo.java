package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@Data
@ApiModel("水厂查询入参")
public class WaterWorksReqVo {

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("水厂规模：全部、两百吨千人以下、两百吨千人以上、千吨万人、城市水厂")
    String wasuRang;

    @ApiModelProperty("水厂类型：全部、城市、乡镇、联村、单村")
    String engType;

    @ApiModelProperty("水厂名称")
    String cwsName;

    @ApiModelProperty(value = "数据库列名", hidden = true)
    String columnNames;

    @ApiModelProperty("水厂运行状态，0，正常，1供水预警，2水质预警，3水质供水预警")
    Integer runtimeState;

    @ApiModelProperty("是否带上水厂运行状态")
    boolean withRuntimeInfo = false;


}
