package com.ygkj.auth.api.archivelog.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 17:03
 * @description 档案操作日志表
 */
@Data
public class SysArchiveLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 档案归属
     */
    @ApiModelProperty(value = "档案归属")
    private String archiveName;

    /**
     * 档案名称
     */
    @ApiModelProperty(value = "档案名称")
    private String fileName;

    /**
     * 操作类型 1:查看 2:下载 3:删除 4:更新
     */
    @ApiModelProperty(value = "操作状态")
    private String status;

    /**
     * 操作人员姓名
     */
    @ApiModelProperty(value = "操作人员姓名")
    private String createName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id")
    private String creatorId;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String delFlag;

    @ApiModelProperty(value = "操作方法")
    private String operationType;

    @ApiModelProperty(value = "访问文件id")
    private String fileId;

    public SysArchiveLog() {
    }

}
