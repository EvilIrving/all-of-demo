package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水域面数据表
 * @date 2021-07-06
 */
@Data
@ApiModel("水域调查数据-其他水域水域面数据表")
public class AttOwaaBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 其他水域名称
     */
    @ApiModelProperty("其他水域名称")
    private String name;

    /**
     * 其他水域编码
     */
    @ApiModelProperty("其他水域编码")
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
     * 所在乡镇（街道）
     */
    @ApiModelProperty("所在乡镇（街道）")
    private String town;

    /**
     * 跨界类型
     */
    @ApiModelProperty("跨界类型")
    private String trntype;

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
     * 平均水深
     */
    @ApiModelProperty("平均水深")
    private String averdep;

    /**
     * 水域容积
     */
    @ApiModelProperty("水域容积")
    private String vol;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public AttOwaaBaseReqVo() {
    }

}
