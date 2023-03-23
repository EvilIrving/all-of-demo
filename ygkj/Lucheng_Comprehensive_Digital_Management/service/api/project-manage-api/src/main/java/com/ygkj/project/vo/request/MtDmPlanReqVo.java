package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-04-18 16:50
 * @description
 */
@Data
public class MtDmPlanReqVo extends PageEntity {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String prcd;

    private String pid;

    /**
     * 年度
     */
    @ApiModelProperty("年度")
    private Integer annual;

    /**
     * 维修养护计划文件
     */
    @ApiModelProperty("维修养护计划文件")
    private String file;

    /**
     * 维修养护内容
     */
    @ApiModelProperty("comment")
    private String comment;

}
