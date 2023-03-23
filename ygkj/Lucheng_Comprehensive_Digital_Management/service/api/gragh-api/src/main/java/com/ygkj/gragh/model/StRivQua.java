package com.ygkj.gragh.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河道水质测站实时数据表
 * @date 2021-08-04
 */
@Data
@ApiModel("河道水质测站实时数据表")
public class StRivQua implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 断面名称
     */
    @ApiModelProperty("断面名称")
    private String siteName;

    /**
     * 上传时刻
     */
    @ApiModelProperty("上传时刻")
    private Date tm;

    /**
     * 透明度
     */
    @ApiModelProperty("透明度")
    private String transparent;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadDate;

    /**
     * 锌
     */
    @ApiModelProperty("锌")
    private String zn;

    /**
     * ph
     */
    @ApiModelProperty("ph")
    private String ph;

    /**
     * 五日生化需氧量
     */
    @ApiModelProperty("五日生化需氧量")
    private String fiveDayCod;

    /**
     * 硒
     */
    @ApiModelProperty("硒")
    private String se;

    /**
     * 镉
     */
    @ApiModelProperty("镉")
    private String cd;

    /**
     * 挥发酚
     */
    @ApiModelProperty("挥发酚")
    private String cr;

    /**
     * 氰化物
     */
    @ApiModelProperty("氰化物")
    private String cyanide;

    /**
     * 化学需氧量
     */
    @ApiModelProperty("化学需氧量")
    private String cod;

    /**
     * 汞
     */
    @ApiModelProperty("汞")
    private String hg;

    /**
     * 石油类
     */
    @ApiModelProperty("石油类")
    private String petro;

    /**
     * 阴离子表面活性剂
     */
    @ApiModelProperty("阴离子表面活性剂")
    private String surfactant;

    /**
     * 硫化物
     */
    @ApiModelProperty("硫化物")
    private String sulfide;

    /**
     * 铅
     */
    @ApiModelProperty("铅")
    private String pb;

    /**
     * 氟化物
     */
    @ApiModelProperty("氟化物")
    private String fluoride;

    /**
     * 铜
     */
    @ApiModelProperty("铜")
    private String cu;

    /**
     * 挥发酚
     */
    @ApiModelProperty("挥发酚")
    private String volatilePhenol;

    /**
     * 溶解氧
     */
    @ApiModelProperty("溶解氧")
    private String dO;

    /**
     * 高锰酸盐
     */
    @ApiModelProperty("高锰酸盐")
    private String codmn;

    /**
     * 砷
     */
    @ApiModelProperty("砷")
    private String arsenic;

    /**
     * 总磷
     */
    @ApiModelProperty("总磷")
    private String tp;

    /**
     * 氨氮
     */
    @ApiModelProperty("氨氮")
    private String nh3N;

    public StRivQua() {
    }

}
