package com.ygkj.digitization.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水位预警模板表
 * @date 2021-07-13
 */
@Data
@ApiModel("水位预警模板表")
public class WaterWarnTemplateRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String name;

    /**
     * 模板内容
     */
    @ApiModelProperty("模板内容")
    private String content;


    public WaterWarnTemplateRespVo() {
    }

}
