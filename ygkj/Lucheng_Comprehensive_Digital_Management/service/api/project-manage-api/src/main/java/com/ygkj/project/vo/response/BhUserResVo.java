package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-26 9:27
 * @description
 */
@Data
@ApiModel
public class BhUserResVo {


    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer uId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String realName;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private Integer departId;

    /**
     * 角色ids
     */
    @ApiModelProperty("角色ids")
    private String roleIds;

    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;

    /**
     * 排序号码
     */
    @ApiModelProperty("排序号码")
    private Integer orderId;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String telphone;

}
