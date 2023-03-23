package com.ygkj.auth.api.archiveLabel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 档案标签表
 * @author hucong
 * @date 2021-01-19
 */
@Data
public class SysArchiveLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String name;

    /**
     * 标签类型
     */
    @ApiModelProperty(value = "标签类型")
    private String type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private String creatorId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    private String delFlag;

    public SysArchiveLabel() {
    }

}

