package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhDepManagefund {
    @TableId(type = IdType.INPUT)
    /**
     * 编号
     */
    private Integer id;

    /**
     * 年度
     */
    private Integer year;

    /**
     * 经费类别
     */
    private Integer category;

    /**
     * 经费来源
     */
    private String fundSource;

    /**
     * 经费数额
     */
    private BigDecimal foudTotal;

    /**
     * 经费落实
     */
    private BigDecimal foudFulfil;

    /**
     * 经费使用
     */
    private String foudUsage;

    /**
     * 填报时间
     */
    private Date createTime;

    /**
     * 单位编号
     */
    private Integer depId;

    /**
     * aid
     */
    private String aid;

    /**
     * pjcd
     */
    private String pjcd;

    /**
     * quarter
     */
    private Integer quarter;
}
