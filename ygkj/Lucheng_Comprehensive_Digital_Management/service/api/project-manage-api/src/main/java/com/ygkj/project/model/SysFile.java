package com.ygkj.project.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description 附件表
 * @date 2021-05-04
 */
@Data
@ApiModel("附件表")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件url
     */
    @ApiModelProperty("文件url")
    private String fileUrl;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private Double fileSize;

    /**
     * 是否删除(1已删除；0未删除)
     */
    @ApiModelProperty("是否删除(1已删除；0未删除)")
    private boolean deleted;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * update_time
     */
    @ApiModelProperty("update_time")
    private Date updateTime;

    public SysFile() {
    }

}
