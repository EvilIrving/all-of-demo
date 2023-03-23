package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lxl
 * @create 2022-06-21 16:20
 * @description
 */
@Data
public class InvestProjectReqVo extends PageEntity {

    /**
     * 是否归档  0 未归档 1 已归档
     */
    private String archive;

    /**
     * 年份筛选 赋码时间
     */
    private String year;

    /**
     * 关键字查询
     */
    private String key;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 涉河水域
     */
    private String wadingWaters;

    /**
     * 占用水域面积
     */
    private String occupiedWaterArea;

    /**
     * 涉河时长
     */
    private String wadingTime;

    /**
     * 经度
     */
    private BigDecimal prLong;

    /**
     * 纬度
     */
    private BigDecimal prLat;
}
