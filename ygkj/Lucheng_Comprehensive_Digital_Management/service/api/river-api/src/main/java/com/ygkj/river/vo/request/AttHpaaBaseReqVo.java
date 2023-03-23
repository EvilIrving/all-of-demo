package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-山塘水域面数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-山塘水域面数据表")
public class AttHpaaBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 山塘名称
     */
    @ApiModelProperty("山塘名称")
    private String name;

    /**
     * 山塘编码
     */
    @ApiModelProperty("山塘编码")
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
     * 坝高
     */
    @ApiModelProperty("坝高")
    private String dmh;

    /**
     * 集雨面积
     */
    @ApiModelProperty("集雨面积")
    private String rcarea;

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
     * 所在乡镇（街道）
     */
    @ApiModelProperty("所在乡镇（街道）")
    private String town;

    /**
     * 整治时间
     */
    @ApiModelProperty("整治时间")
    private String rnvtm;

    /**
     * 总容积
     */
    @ApiModelProperty("总容积")
    private String tcr;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 坝顶高程
     */
    @ApiModelProperty("坝顶高程")
    private String ce;

    /**
     * 设计洪水位
     */
    @ApiModelProperty("设计洪水位")
    private String dfl;

    /**
     * 正常蓄水位
     */
    @ApiModelProperty("正常蓄水位")
    private String npl;

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

    public AttHpaaBaseReqVo() {
    }

}

