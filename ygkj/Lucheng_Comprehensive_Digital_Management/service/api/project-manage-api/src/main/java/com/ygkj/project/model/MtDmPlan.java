package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-18 16:49
 * @description 存储维养计划信
 */
@Data
@ApiModel("存储维养计划信")
public class MtDmPlan implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creatorId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updatorId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    /**
     * 数据来源 0-省厅数据 1-鹿城自身数据
     */
    @ApiModelProperty("数据来源 0-省厅数据 1-鹿城自身数据")
    private Integer source;

    @TableField(exist = false)
    private List<SysFile> files;

    public MtDmPlan() {
    }

}

