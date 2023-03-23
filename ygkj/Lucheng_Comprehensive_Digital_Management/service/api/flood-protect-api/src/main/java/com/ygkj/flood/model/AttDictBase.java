package com.ygkj.flood.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 数据字典
 *
 * @author huangzh 2021-01-18
 */
@Data
public class AttDictBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 字典码，用于区分枚举值类型
     */
    private String dictCode;

    /**
     * 字典枚举具体释义
     */
    private String dictName;

    /**
     * 父级id
     */
    private String parentId;

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

    public AttDictBase() {
    }

}

