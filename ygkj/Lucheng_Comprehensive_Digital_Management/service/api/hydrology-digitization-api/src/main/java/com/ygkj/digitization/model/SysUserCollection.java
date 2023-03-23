package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 用户收藏记录表
 * @date 2021-08-10
 */
@Data
public class SysUserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 收藏项具体表id
     */
    private String objectId;

    /**
     * 收藏项名称
     */
    private String name;

    /**
     * 收藏项类别（可不填）
     */
    private String category;

    /**
     * 收藏项预览地址（文件类型特有）
     */
    private String url;

    /**
     * 收藏时间
     */
    private Date collectDate;

    /**
     * 收藏人id
     */
    private String userId;

    /**
     * 收藏人姓名
     */
    private String username;

    private String collectionType;

    private String uploadName;

    public SysUserCollection() {
    }

    public SysUserCollection(String id, String objectId,
                             String name, String category,
                             String url, Date collectDate,
                             String userId, String username, String collectionType, String uploadName) {
        this.id = id;
        this.objectId = objectId;
        this.name = name;
        this.category = category;
        this.url = url;
        this.collectDate = collectDate;
        this.userId = userId;
        this.username = username;
        this.collectionType = collectionType;
        this.uploadName = uploadName;
    }
}

