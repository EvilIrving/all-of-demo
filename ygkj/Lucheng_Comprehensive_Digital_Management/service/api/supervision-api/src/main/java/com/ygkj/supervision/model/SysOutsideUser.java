package com.ygkj.supervision.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description sys_outside_user  外部用户表
 * @date 2021-05-14
 */
@Data
@ApiModel("外部用户表")
public class SysOutsideUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String id;

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String username;

    /**
     * 加密盐值
     */
    @ApiModelProperty("加密盐值")
    private String salt;

    /**
     * 用户密码密文
     */
    @ApiModelProperty("用户密码密文")
    private String password;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String deptId;

    /**
     * 真实名称
     */
    @ApiModelProperty("真实名称")
    private String realName;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 邮箱(唯一)
     */
    @ApiModelProperty("邮箱(唯一)")
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    @ApiModelProperty("账户状态(1.正常 2.锁定 )")
    private boolean status;

    /**
     * 性别(1.男 2.女)
     */
    @ApiModelProperty("性别(1.男 2.女)")
    private boolean sex;

    /**
     * 是否删除(1未删除；0已删除)
     */
    @ApiModelProperty("是否删除(1未删除；0已删除)")
    private boolean deleted;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateId;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    @ApiModelProperty("创建来源(1.web 2.android 3.ios )")
    private boolean createWhere;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * update_time
     */
    @ApiModelProperty("update_time")
    private String updateTime;

    /**
     * 用户类型，1水利局用户 2普通企业用户
     */
    @ApiModelProperty("用户类型，1水利局用户 2普通企业用户")
    private String userType;

    /**
     * 是否为管理员，1是 2否
     */
    @ApiModelProperty("是否为管理员，1是 2否")
    private boolean isAdmin;

    /**
     * 头像url
     */
    @ApiModelProperty("头像url")
    private String slogan;

    /**
     * 用户职务
     */
    @ApiModelProperty("用户职务")
    private String job;

    /**
     * 排序号
     */
    @ApiModelProperty("排序号")
    private Integer sort;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String identityNumber;

    public SysOutsideUser() {
    }

}

