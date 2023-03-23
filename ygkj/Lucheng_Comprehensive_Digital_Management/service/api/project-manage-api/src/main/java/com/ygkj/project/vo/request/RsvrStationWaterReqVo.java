package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/10
 */
@Data
@ApiModel("水库、测站、水位数据 查询入参")
public class RsvrStationWaterReqVo extends PageEntity implements Serializable {
    private static final long serialVersionUID = 5724981373697077136L;
    @ApiModelProperty("水库编号，多个水库编号用半角逗号隔开")
    String resCodes;

    @ApiModelProperty("水库编号，多个水库编号用半角逗号隔开")
    String resName;

    @ApiModelProperty("测站测量时间点")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("工程行政区划编码")
    String resAdcd;

    @ApiModelProperty("流域：飞云江、瓯江、鳌江")
    String resBas;

    @ApiModelProperty("水库工程等级：大（Ⅰ）型、大（Ⅱ）型、中型、小（Ⅰ）型、小（Ⅱ）型")
    String engScales;

    @ApiModelProperty("是否计算实时库容")
    Boolean waterLevelStorage = false;

    @ApiModelProperty("是否计算蓄水率")
    Boolean storageRatio = false;

    @ApiModelProperty("是否计算纳蓄能力")
    Boolean storageAbility = false;

    @ApiModelProperty("是否计算至汛限纳蓄水能力")
    Boolean limitStorageAbility = false;

    @ApiModelProperty("是否计算至防洪高纳蓄水能力")
    Boolean floodStorageAbility = false;

    @ApiModelProperty("是否计算至正常水位纳蓄水能力")
    Boolean normStorageAbility = false;

//    @ApiModelProperty("是否计算水库泄流量")
//    boolean drainage = false;

    @ApiModelProperty("泄流时长")
    Integer drainageMinutes;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    /**
     * 水库小时泄流量
     */
    @ApiModelProperty("水库24小时泄流量")
    Boolean drainage24H = false;

    /**
     * 水库48小时泄流量
     */
    @ApiModelProperty("水库48小时泄流量")
    Boolean drainage48H = false;

    /**
     * 水库72小时泄流量
     */
    @ApiModelProperty("水库72小时泄流量")
    private Boolean drainage72H = false;

    @ApiModelProperty("计算历史平均蓄水量")
    private Boolean hisAvgCapacity = false;

    @ApiModelProperty("计算历史平均蓄水量对比")
    private Boolean compareHisAvgCapStat = false;

    @ApiModelProperty("安全鉴定结论")
    private String safetyConclusion;

    @ApiModelProperty("导出策略：0：水库组件列表表头，1：大中型水库组件列表导出")
    private Integer exportStrategy = 0;

    public boolean isWaterLevelStorage() {
        if (waterLevelStorage != null) {
            return waterLevelStorage;
        } else {
            return false;
        }
    }

    public boolean isStorageRatio() {
        if (storageRatio != null) {
            return storageRatio;
        } else {
            return false;
        }
    }

    public boolean isStorageAbility() {
        if (storageAbility != null) {
            return storageAbility;
        } else {
            return false;
        }
    }

    public boolean isLimitStorageAbility() {
        if (limitStorageAbility != null) {
            return limitStorageAbility;
        } else {
            return false;
        }
    }

    public boolean isFloodStorageAbility() {
        if (floodStorageAbility != null) {
            return floodStorageAbility;
        } else {
            return false;
        }
    }

    public boolean isNormStorageAbility() {
        if (normStorageAbility != null) {
            return normStorageAbility;
        } else {
            return false;
        }
    }

    public boolean isAscOrDesc() {
        if (ascOrDesc != null) {
            return ascOrDesc;
        } else {
            return false;
        }
    }

    public boolean isDrainage24H() {
        if (drainage24H != null) {
            return drainage24H;
        } else {
            return false;
        }
    }

    public boolean isDrainage48H() {
        if (drainage48H != null) {
            return drainage48H;
        } else {
            return false;
        }
    }

    public boolean isDrainage72H() {
        if (drainage72H != null) {
            return drainage72H;
        } else {
            return false;
        }
    }

    public boolean isHisAvgCapacity() {
        if (hisAvgCapacity != null) {
            return hisAvgCapacity;
        } else {
            return false;
        }
    }

    public boolean isCompareHisAvgCapStat() {
        if (compareHisAvgCapStat != null) {
            return compareHisAvgCapStat;
        } else {
            return false;
        }
    }
}
