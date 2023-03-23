package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 水源地基础信息表
 * @author hucong
 * @date 2021-08-17
 */
@Data
@ApiModel("水源地基础信息表 ")
@NoArgsConstructor
public class AttSwhsBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水源地代码
     */
    @ApiModelProperty("水源地代码")
    private String swhsCode;

    /**
     * 水源地名称
     */
    @ApiModelProperty("水源地名称")
    private String swhsName;

    /**
     * 水源地经度
     */
    @ApiModelProperty("水源地经度")
    private BigDecimal swhsLong;

    /**
     * 水源地纬度
     */
    @ApiModelProperty("水源地纬度")
    private BigDecimal swhsLat;

    /**
     * 水源地所在位置
     */
    @ApiModelProperty("水源地所在位置")
    private String swhsLoc;

    /**
     * 取水水源类型 1:水库 2:山塘 3:河流湖泊 4:溪沟堰坝 5:蓄水池 6:地下水 7:海水淡化 8:山泉水 9:其他
     */
    @ApiModelProperty("取水水源类型 1:水库 2:山塘 3:河流湖泊 4:溪沟堰坝 5:蓄水池 6:地下水 7:海水淡化 8:山泉水 9:其他")
    private String wainWasoType;

    /**
     * 水面面积
     */
    @ApiModelProperty("水面面积")
    private BigDecimal watArea;

    /**
     * 取水口数量
     */
    @ApiModelProperty("取水口数量")
    private Integer wainNum;

    /**
     * 主要取水用途
     */
    @ApiModelProperty("主要取水用途")
    private String mainWainUse;

    /**
     * 供水对象类型
     */
    @ApiModelProperty("供水对象类型")
    private String wasuObjType;

    /**
     * 主要供水城镇名称
     */
    @ApiModelProperty("主要供水城镇名称")
    private String wasuTownName;

    /**
     * 设计年取水量
     */
    @ApiModelProperty("设计年取水量")
    private BigDecimal desAnnWain;

    /**
     * 设计年供水人口
     */
    @ApiModelProperty("设计年供水人口")
    private BigDecimal desAnnWasuPop;

    /**
     * 投入运行年
     */
    @ApiModelProperty("投入运行年")
    private String runYear;

    /**
     * 水质目标
     */
    @ApiModelProperty("水质目标")
    private String waquGoal;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private Date effDate;

    /**
     * 记录失效时间
     */
    @ApiModelProperty("记录失效时间")
    private Date exprDate;

    /**
     * 最大日供水能力
     */
    @ApiModelProperty("最大日供水能力")
    private String dateMaxWat;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String master;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String masterPhone;

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
    private boolean delFlag;

    /**
     * 规模 1：县级以上 2：千吨万人 3：200吨-1000吨 4：200吨以下
     */
    @ApiModelProperty("规模 1：县级以上 2：千吨万人 3：200吨-1000吨 4：200吨以下")
    private String supplyScale;

    @ApiModelProperty("预警等级 1:暂无风险 2:黄色预警 3:橙色预警 4:红色预警")
    private String warnType;

    @ApiModelProperty("查询开始时间 格式:yyyy-MM-dd")
    private String queryStartTime;

    @ApiModelProperty("查询结束时间 格式:yyyy-MM-dd")
    private String queryEndTime;

    @ApiModelProperty("统计类型 1：水源地 2：水量预警")
    private String statisticalType;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("是否带上水源地标识")
    private boolean withSignage=false;

}