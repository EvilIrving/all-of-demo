package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @description 水域调查数据-河道水域面数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-河道水域面数据表")
public class AttRvaaBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 河道（段）名称
     */
    @ApiModelProperty("河道（段）名称")
    private String name;

    /**
     * 河道（段）编码
     */
    @ApiModelProperty("河道（段）编码")
    private String code;

    /**
     * 所在市
     */
    @ApiModelProperty("所在市")
    private String city;

    /**
     * 所在县（市、区）
     */
    @ApiModelProperty("所在县（市、区）")
    private String county;

    /**
     * 长度r
     */
    @ApiModelProperty("长度r")
    private String length;

    /**
     * 平均宽度
     */
    @ApiModelProperty("平均宽度")
    private String width;

    /**
     * 起点位置名称
     */
    @ApiModelProperty("起点位置名称")
    private String sname;

    /**
     * 终点位置名称
     */
    @ApiModelProperty("终点位置名称")
    private String ename;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private String grade;

    /**
     * 干支流
     */
    @ApiModelProperty("干支流")
    private String mntrb;

    /**
     * 跨界类型
     */
    @ApiModelProperty("跨界类型")
    private String trntype;

    /**
     * 流经乡镇（街道）
     */
    @ApiModelProperty("流经乡镇（街道）")
    private String town;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String bas;

    /**
     * 所属地形地貌
     */
    @ApiModelProperty("所属地形地貌")
    private String landform;

    /**
     * 主要功能
     */
    @ApiModelProperty("主要功能")
    private String function;

    /**
     * 水域面积
     */
    @ApiModelProperty("水域面积")
    private String area;

    /**
     * 水域容积
     */
    @ApiModelProperty("水域容积")
    private String vol;

    /**
     * 管理单位
     */
    @ApiModelProperty("管理单位")
    private String mu;

    /**
     * 是否重要水域
     */
    @ApiModelProperty("是否重要水域")
    private String imp;

    /**
     * 与三调差异
     */
    @ApiModelProperty("与三调差异")
    private String contdiff;

    /**
     * 是否属于特殊区域
     */
    @ApiModelProperty("是否属于特殊区域")
    private String spcl;

    /**
     * 起点设计水位
     */
    @ApiModelProperty("起点设计水位")
    private String sdl;

    /**
     * 终点设计水位
     */
    @ApiModelProperty("终点设计水位")
    private String edl;

    /**
     * 河长
     */
    @ApiModelProperty("河长")
    private String rchief;

    /**
     * 河流码
     */
    @ApiModelProperty("河流码")
    private String rcode;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;


    public AttRvaaBaseReqVo() {
    }

}

