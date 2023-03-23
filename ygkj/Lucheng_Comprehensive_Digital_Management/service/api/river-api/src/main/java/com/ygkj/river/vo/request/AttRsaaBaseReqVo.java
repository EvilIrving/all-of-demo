package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-水库水域面数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-水库水域面数据表")
public class AttRsaaBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 水库名称
     */
    @ApiModelProperty("水库名称")
    private String name;

    /**
     * 水库编码
     */
    @ApiModelProperty("水库编码")
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
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 集雨面积
     */
    @ApiModelProperty("集雨面积")
    private String rcarea;

    /**
     * 总库容
     */
    @ApiModelProperty("总库容")
    private String tcr;

    /**
     * 兴利库容
     */
    @ApiModelProperty("兴利库容")
    private String ucr;

    /**
     * 设计洪水位
     */
    @ApiModelProperty("设计洪水位")
    private String npl;

    /**
     * 移民水位
     */
    @ApiModelProperty("移民水位")
    private String iml;

    /**
     * 设计洪水位
     */
    @ApiModelProperty("设计洪水位")
    private String dfl;

    /**
     * 校核洪水位
     */
    @ApiModelProperty("校核洪水位")
    private String mfl;

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
     * 水域面积
     */
    @ApiModelProperty("水域面积")
    private String area;

    /**
     * 坝顶高程
     */
    @ApiModelProperty("坝顶高程")
    private String ce;

    /**
     * 主要功能
     */
    @ApiModelProperty("主要功能")
    private String function;

    /**
     * 是否重要水域
     */
    @ApiModelProperty("是否重要水域")
    private String imp;

    /**
     * 建成时间
     */
    @ApiModelProperty("建成时间")
    private String bldtm;

    /**
     * 管理单位
     */
    @ApiModelProperty("管理单位")
    private String mu;

    /**
     * 所在乡镇（街道）
     */
    @ApiModelProperty("所在乡镇（街道）")
    private String town;

    /**
     * 湖长
     */
    @ApiModelProperty("湖长")
    private String lchief;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * shape_leng
     */
    @ApiModelProperty("shape_leng")
    private String shapeLeng;

    /**
     * shape_area
     */
    @ApiModelProperty("shape_area")
    private String shapeArea;

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
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttRsaaBaseReqVo() {
    }

}


