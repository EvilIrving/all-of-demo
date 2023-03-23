package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报警模板配置
 *
 * @author huangzh 2020-12-29
 */
@ApiModel("报警模板列表查询接口返回字段释义")
@Data
@JsonIgnoreProperties(value = {"handler"})
public class WarningTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id（统一采用雪花算法策略）
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 报警类型（摄像机/水位/雨量/台风）
     */
    @ApiModelProperty("摄像机/水位/雨量/台风")
    private String warningType;

    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 模板内容
     */
    @ApiModelProperty("模板内容")
    private String templateContent;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 删除标记
     */
    private boolean deleted;

    public WarningTemplate() {
    }

}

