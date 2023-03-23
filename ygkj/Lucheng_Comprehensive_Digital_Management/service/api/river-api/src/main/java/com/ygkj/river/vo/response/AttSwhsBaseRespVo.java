package com.ygkj.river.vo.response;

import com.ygkj.river.model.AttSwhsSignageBase;
import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水源地基础信息表
 * @date 2021-08-17
 */
@Data
@ApiModel("水源地基础信息表 ")
@NoArgsConstructor
public class AttSwhsBaseRespVo implements Serializable {

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
    @ApiModelProperty("水源地所在位置编码")
    private String swhsLoc;

    @ApiModelProperty("水源地所在位置名称")
    private String adnm;

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

    @ApiModelProperty("累计年取水量")
    private BigDecimal cumulatAnnWain;

    @ApiModelProperty("取水量")
    private BigDecimal withdrawal;

    @ApiModelProperty("取水月份")
    private String waterMonth;

    /**
     * 设计年供水人口
     */
    @ApiModelProperty("设计年供水人口（可供取水量）")
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
    @ApiModelProperty("记录生效时间(数据更新时间)")
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

    /**
     * 取水水源类型 1:水库 2:山塘 3:河流湖泊 4:溪沟堰坝 5:蓄水池 6:地下水 7:海水淡化 8:山泉水 9:其他
     */
    @ApiModelProperty("取水水源类型")
    private String wainWasoTypeName;

    /**
     * 规模 1：县级以上 2：千吨万人 3：200吨-1000吨 4：200吨以下
     */
    @ApiModelProperty("规模")
    private String supplyScaleName;


    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String swhsLocName;

    /**
     * 可供水量
     */
    @ApiModelProperty("可供水量")
    private BigDecimal leftWaterSupply;

    /**
     * 保护范围线
     */
    @ApiModelProperty("保护范围线")
    private String protectRangeLine;

    @ApiModelProperty("行政区划")
    private String areaCode;

    // 同步下来的字段（行政村~是否制作矢量边界）
    /**
     * 行政村
     */
    @ApiModelProperty("行政村")
    private String adminAdnm;

    /**
     * 自然村
     */
    @ApiModelProperty("自然村")
    private String naturalAdnm;

    /**
     * 总可供水量
     */
    @ApiModelProperty("总可供水量")
    private BigDecimal availSuly;

    /**
     * 是否划定保护区
     */
    @ApiModelProperty("是否划定保护区")
    private String isPrtcAr;

    /**
     * 是否划定保护范围
     */
    @ApiModelProperty("是否划定保护范围")
    private String isPrtcBdy;

    /**
     * 标识标牌
     */
    @ApiModelProperty("标识标牌")
    private String haveSign;

    /**
     * 是否设立界标标志
     */
    @ApiModelProperty("是否设立界标标志")
    private String haveBdySign;

    /**
     * 是否制作矢量边界
     */
    @ApiModelProperty("是否制作矢量边界")
    private String haveVecBdy;

    @ApiModelProperty("行政区划名称")
    private String areaName;

    @ApiModelProperty("标识牌列表")
    private List<AttSwhsSignageBase> singageList= Collections.emptyList();

    public void setWainWasoType(String wainWasoType) {
        this.wainWasoType = wainWasoType;
        if (StringUtils.isNotBlank(wainWasoType)) {
            switch (wainWasoType) {
                case "1":
                    this.wainWasoTypeName = "水库";
                    break;
                case "2":
                    this.wainWasoTypeName = "山塘";
                    break;
                case "3":
                    this.wainWasoTypeName = "河流湖泊";
                    break;
                case "4":
                    this.wainWasoTypeName = "溪沟堰坝";
                    break;
                case "5":
                    this.wainWasoTypeName = "蓄水池";
                    break;
                case "6":
                    this.wainWasoTypeName = "地下水";
                    break;
                case "7":
                    this.wainWasoTypeName = "海水淡化";
                    break;
                case "8":
                    this.wainWasoTypeName = "山泉水";
                    break;
                case "9":
                    this.wainWasoTypeName = "其他";
                    break;
            }
        }
    }

    public void setSupplyScale(String supplyScale) {
        this.supplyScale = supplyScale;
        if (StringUtils.isNotBlank(supplyScale)) {
            switch (supplyScale) {
                case "1":
                    this.supplyScaleName = "县级以上";
                    break;
                case "2":
                    this.supplyScaleName = "千吨万人";
                    break;
                case "3":
                    this.supplyScaleName = "200吨-1000吨";
                    break;
                case "4":
                    this.supplyScaleName = "200吨以下";
                    break;
            }
        }
    }

    /**
     * 计算可供水量
     */
    public void computeLeftWaterSupply(){
        if (this.cumulatAnnWain!=null&&this.desAnnWain!=null&&this.desAnnWain.compareTo(cumulatAnnWain)>=0){
            leftWaterSupply=this.desAnnWain.subtract(cumulatAnnWain);
        }
    }
}