package com.ygkj.lcsyn.model;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;

/**
 * @description 重点防御对象信息表   数据来源-省厅数据
 * @author
 * @date 2022-05-06
 */
@Data
public class PvDefenObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 防御对象编码
     */
    private String defenCode;

    /**
     * 对象类型
     */
    private String objtp;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系方式
     */
    private String contacttel;

    /**
     * 人口数量
     */
    private Integer pcount;

    /**
     * 转移责任人
     */
    private String tcharger;

    /**
     * 转移责任人联系方式
     */
    private String tchargertel;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 数据更新时间
     */
    private Date tongTime;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

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

    public PvDefenObject() {
    }

}
