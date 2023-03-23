package com.ygkj.river.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 水源地基础信息表
 * @date 2021-08-17
 */
@Data
@NoArgsConstructor
public class AttSwhsBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水源地代码
     */
    private String swhsCode;

    /**
     * 水源地名称
     */
    private String swhsName;

    /**
     * 水源地经度
     */
    private BigDecimal swhsLong;

    /**
     * 水源地纬度
     */
    private BigDecimal swhsLat;

    /**
     * 水源地所在位置
     */
    private String swhsLoc;

    /**
     * 取水水源类型 1:水库 2:山塘 3:河流湖泊 4:溪沟堰坝 5:蓄水池 6:地下水 7:海水淡化 8:山泉水 9:其他
     */
    private String wainWasoType;

    /**
     * 水面面积
     */
    private BigDecimal watArea;

    /**
     * 取水口数量
     */
    private Integer wainNum;

    /**
     * 主要取水用途
     */
    private String mainWainUse;

    /**
     * 供水对象类型
     */
    private String wasuObjType;

    /**
     * 主要供水城镇名称
     */
    private String wasuTownName;

    /**
     * 设计年取水量
     */
    private BigDecimal desAnnWain;

    /**
     * 设计年供水人口
     */
    private BigDecimal desAnnWasuPop;

    /**
     * 投入运行年
     */
    private String runYear;

    /**
     * 水质目标
     */
    private String waquGoal;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

    /**
     * 最大日供水能力
     */
    private String dateMaxWat;

    /**
     * 负责人
     */
    private String master;

    /**
     * 联系电话
     */
    private String masterPhone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    /**
     * 规模 1：县级以上 2：千吨万人 3：200吨-1000吨 4：200吨以下
     */
    private String supplyScale;

}