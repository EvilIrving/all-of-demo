package com.ygkj.auth.api.archivecollection.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.auth.api.file.model.SysFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 档案收藏表
 * @author hucong
 * @date 2021-01-19
 */
@Data
public class SysArchiveCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;

    /**
     * 档案id
     */
    @ApiModelProperty(value = "档案id")
    private String achievementId;

    /**
     * 操作人员姓名
     */
    @ApiModelProperty(value = "操作人员姓名")
    private String createName;

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

    private SysFile sysFile;

    @ApiModelProperty(value = "文件id")
    private String fileId;

    public SysArchiveCollection() {
    }

}
