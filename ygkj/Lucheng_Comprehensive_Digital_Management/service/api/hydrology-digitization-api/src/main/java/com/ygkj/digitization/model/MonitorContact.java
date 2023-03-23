package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @Date 2021/1/14
 */
@Data
public class MonitorContact implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键，自动增长
     */
    private Integer id;

    /**
     * 联系类型：1为人员，2为单位
     */
    private int contactType;

    /**
     * 联系名称：1为人员名，2为单位名
     */
    private String contactName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

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
    private int delFlag;


    /**
     * 联系类型名：人员或单位，额外添加的属性，并不存在于数据库，方便导出excel
     */
    private String contactTypeName;
}
