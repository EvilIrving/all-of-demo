package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author java.devtools.cn
 * @description 标段授权表
 * @date 2021-09-08
 */
@Data
@ApiModel("标段授权入参")
public class AttBlockAuthorizaRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 人员姓名
     */
    @ApiModelProperty("人员姓名")
    private String userName;

    /**
     * 乙方系统的用户id
     */
    @ApiModelProperty("乙方系统的用户id")
    private String userId;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String phone;

    /**
     * 用户类型 1:浙政钉用户 2:对外用户
     */
    @ApiModelProperty("用户类型 1:浙政钉用户 2:对外用户")
    private String type;

    /**
     * 标段表id
     */
    @ApiModelProperty("标段表id")
    private String blockId;

    public AttBlockAuthorizaRespVo() {
    }

}