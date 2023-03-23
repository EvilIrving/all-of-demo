package com.ygkj.data.management.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author java.devtools.cn
 * @description 标段授权表
 * @date 2021-09-08
 */
@Data
public class AttBlockAuthoriza implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 人员姓名
     */
    private String userName;

    /**
     * 乙方系统的用户id
     */
    private String userId;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 用户类型 1:浙政钉用户 2:对外用户
     */
    private String type;

    /**
     * 标段表id
     */
    private String blockId;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private String creatorId;

    public AttBlockAuthoriza() {
    }

}
