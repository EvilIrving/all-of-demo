package com.ygkj.lcsyn.model;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 山洪预警责任人信息表  数据来源-省厅数据
 * @author
 * @date 2022-05-06
 */
@Data
public class PvPchargeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 责任人编码
     */
    private String pchargeCode;

    /**
     * 责任人姓名
     */
    private String pchargeName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 所在部门
     */
    private String deptName;

    /**
     * 所在单位
     */
    private String unit;

    /**
     * 单位职务
     */
    private String position;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 同步时间
     */
    private Date tongTime;

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

    public PvPchargeInfo() {
    }

}

