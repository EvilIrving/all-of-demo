package com.ygkj.data.management.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 告警记录
 * @date 2021-07-29
 */
@Data
public class SysAlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 检测事项id
     */
    private String validateId;

    /**
     * 检测类型：正则表达式/更新频率
     */
    private String validateType;

    /**
     * 数据类型：基础数据/实时数据
     */
    private String dataType;

    /**
     * 名称（表名称或者测站名称）
     */
    private String name;

    /**
     * 分类（表分类或者测站类型）
     */
    private String category;

    /**
     * 告警时间
     */
    private Date validateTime;

    /**
     * 告警内容
     */
    private String validateContent;

    /**
     * 处理状态：已处理/未处理
     */
    private String status;

    /**
     * 处理意见
     */
    private String opinion;

    /**
     * 附件id
     */
    private String attachmentIds;

    /**
     * 接收人id
     */
    private String userId;

    private String primary;

    public SysAlarmRecord() {
    }

}


