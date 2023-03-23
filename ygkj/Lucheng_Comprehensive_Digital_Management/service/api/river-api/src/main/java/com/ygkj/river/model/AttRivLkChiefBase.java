package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河长/湖长基础信息表
 * @date 2021-08-04
 */
@Data
@ApiModel("河长/湖长基础信息表")
public class AttRivLkChiefBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String post;

    /**
     * 等级2:省级;3:市级;4:县级;5:镇级;6:村级
     */
    @ApiModelProperty("等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    private Integer level;

    /**
     * type=1时，身份1:河长;2:督察;5:警长
     * type=2时，类型[1:库长;2:湖长;3:督察长]
     */
    @ApiModelProperty("type=1时，身份1:河长;2:督察;5:警长" +
            "/type=2时，类型[1:库长;2:湖长;3:督察长]")
    private Integer rpost;

    /**
     * 所属地区
     */
    @ApiModelProperty("所属地区")
    private String pathStr;

    /**
     * 所管河段
     */
    @ApiModelProperty("所管河段")
    private String riverName;

    /**
     * 管理湖库信息
     */
    @ApiModelProperty("管理湖库信息")
    private String reservoirName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    /**
     * 1，河长/警长 信息；2，湖/库长信息
     */
    @ApiModelProperty("1，河长/警长 信息；2，湖/库长信息")
    private Integer type;

    public AttRivLkChiefBase() {
    }

}
