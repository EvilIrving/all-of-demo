package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 浙政钉用户同步记录表
 * @author lxl
 * @date 2022-05-26
 */
@Data
public class SynZzdUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String userCode;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 职务
     */
    private Integer orderId;

    /**
     * 部门id
     */
    private String job;

    /**
     * dept
     */
    private String dept;

    @TableField(exist = false)
    private String salt;

    @TableField(exist = false)
    private String password;

    @TableField(exist = false)
    private String status;

    @TableField(exist = false)
    private String deleted;

    @TableField(exist = false)
    private String userType;

    public SynZzdUser() {
    }

}

