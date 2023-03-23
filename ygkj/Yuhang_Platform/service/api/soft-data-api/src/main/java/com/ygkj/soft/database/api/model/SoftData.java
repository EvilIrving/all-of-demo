package com.ygkj.soft.database.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huang.zh
 * @date 2020/9/9 15:20
 * @Description: 中间数据实体
 */
@Data
public class SoftData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 全局id，代码生成
     */
    private String globalId;

    /**
     * 对应业务库的id
     */
    private String id;
    /**
     * 数据库名称
     */
    private String dataBaseName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 数据json串
     */
    private String data;

    /**
     * 数据版本号
     */
    private Integer version;

    /**
     * 数据操作时间
     */
    private Date updateTime;

    /**
     * 操作类型，1新增 2修改 3删除
     */
    private String operationType;

    /**
     * 数据状态 1审核通过并推送 2拒绝推送 0未审核
     */
    private String status;
    /**
     * 审核理由
     */
    private String reason;

    public SoftData() {
    }

}