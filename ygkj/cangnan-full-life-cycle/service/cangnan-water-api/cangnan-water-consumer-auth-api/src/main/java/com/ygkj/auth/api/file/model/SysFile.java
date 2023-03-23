package com.ygkj.auth.api.file.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 附件表
 * @author Huangzh
 * @date 2020-09-15
 */
@Data
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件url
     */
    private String fileUrl;

    /**
     * 文件类型
     */
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
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    private boolean modify = true;

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
