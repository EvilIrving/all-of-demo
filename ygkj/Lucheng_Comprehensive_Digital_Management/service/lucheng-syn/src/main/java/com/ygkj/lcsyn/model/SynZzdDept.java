package com.ygkj.lcsyn.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 * @description 浙政钉同步部门表
 * @author lxl
 * @date 2022-05-26
 */
@Data
public class SynZzdDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String organizationId;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * full_name
     */
    private String fullName;

    /**
     * 地址
     */
    private String address;

    /**
     * uncode
     */
    private String uncode;

    /**
     * 排序
     */
    private Integer orderId;

    /**
     * 责任人
     */
    private String chargePerson;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 移动号码
     */
    private String mobileNumber;

    /**
     * 传真号码
     */
    private String faxNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * intruduction
     */
    private String intruduction;

    /**
     * staff_count
     */
    private Integer staffCount;

    /**
     * unit_id
     */
    private String unitId;

    /**
     * type_code
     */
    private String typeCode;

    /**
     * 组织编码
     */
    private String organizationCode;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政区划名称
     */
    private String adName;

    /**
     * 是否浙政钉
     */
    private String isZzd;

    /**
     * 同步时间
     */
    private Date createTime;

    public SynZzdDept() {
    }

}

