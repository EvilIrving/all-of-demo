package com.ygkj.auth.api.file.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 附件表
 * @author Huangzh
 * @date 2020-09-15
 */
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("主键id")
    private String id;

    @ApiModelProperty("归档编号")
    private String fileNo;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件预览图地址
     */
    private String fileImage;

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
    private Double fileSize;

    /**
     * 是否删除(1已删除；0未删除)
     */
    private boolean deleted;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("上传时间")
    private Date createTime;

    /**
     * update_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private boolean modify = true;

    /**
     * 标签ids
     */
    @ApiModelProperty("标签ids")
    private String labelIds;

    /**
     * 是否收藏 1:不收藏 0:收藏
     */
    @ApiModelProperty("是否收藏 1:不收藏 0:收藏")
    private String collectionFlag;

    @ApiModelProperty("浏览数量")
    private Integer browseNum = 0;

    @ApiModelProperty("对应文件夹id")
    private String parentAchievementId;

    @ApiModelProperty("文件目录归属")
    private String achievementName;

    @ApiModelProperty("查看权限 0:没有 1:有")
    private String lookFlag = "0";

    @ApiModelProperty("上次权限 0:没有 1:有")
    private String uploadFlag = "0";

    @ApiModelProperty("删除权限 0:没有 1:有")
    private String deleteFlag = "0";

    @ApiModelProperty("删除权限 0:没有 1:有")
    private String downloadFlag = "0";

    @ApiModelProperty("收藏id")
    private String collectionId;

    @ApiModelProperty("文件编号")
    private String fileCode;

    public SysFile() {
    }

    public SysFile(String id, String fileName, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public SysFile(String id, String fileName, String fileUrl, String fileType, Double fileSize, String createId, Date createTime) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.createId = createId;
        this.createTime = createTime;
    }
}
