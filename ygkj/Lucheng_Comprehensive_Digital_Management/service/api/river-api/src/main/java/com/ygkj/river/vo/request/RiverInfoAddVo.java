package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RiverInfoAddVo {

    /**
     * id
     */
    @ApiModelProperty("更新用")
    private String id;

    /**
     * 河道名称
     */
    @ApiModelProperty("河道名称")
    private String name;

    /**
     * 河流类型
     */
    @ApiModelProperty("河流类型")
    private String riverType;

    /**
     * 河流等级
     */
    @ApiModelProperty("河流等级")
    private String riverGrade;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String basin;

    /**
     * 河流长度
     */
    @ApiModelProperty("河流长度")
    private Double riverLength;

    /**
     * 河源位置
     */
    @ApiModelProperty("河源位置")
    private String resourceLocation;

    /**
     * 河口位置
     */
    @ApiModelProperty("河口位置")
    private String estuaryLocation;

    /**
     * 河长级别
     */
    @ApiModelProperty("河长级别")
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    @ApiModelProperty("河长姓名")
    private String riverManagerName;

    /**
     * 河长电话
     */
    @ApiModelProperty("河长电话")
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    @ApiModelProperty("河长职务")
    private String riverManagerJob;

    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    private String department;
}
