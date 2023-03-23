package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏记录表
 *
 * @author huangzh 2021-02-23
 */
@Data
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 设备编码（测站/工程/...）
     */
    private String code;

    /**
     * 收藏设备的类型（水位/雨量/工程...）
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    public UserCollection() {
    }

}

