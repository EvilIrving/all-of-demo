package com.ygkj.flood.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 种粮大户清单
 * @author
 * @date 2022-05-09
 */
@Data
@ApiModel("种粮大户清单")
public class AttGrowGrainBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 种粮大户名单
     */
    @ApiModelProperty("种粮大户名单")
    private String name;

    /**
     * 城镇
     */
    @ApiModelProperty("城镇")
    private String city;

    /**
     * 村庄
     */
    @ApiModelProperty("村庄")
    private String village;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

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

    public AttGrowGrainBase() {
    }

}

