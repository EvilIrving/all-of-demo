package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wgf
 * @date 2021/11/11 19:58
 * @Description: 避灾点
 */
@Data
public class AvoidancePoint implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 避灾点名称
     */
    private String pointName;

    /**
     * 避灾点经度
     */
    private BigDecimal longitude;

    /**
     * 避灾点纬度
     */
    private BigDecimal latitude;

    /**
     * 可容纳人数
     */
    private Integer pointCapacity;

    /**
     * 现容纳人数
     */
    private Integer pointNowCapacity;

    /**
     * 避灾点地址
     */
    private String pointAddress;

    /**
     * 避灾点说明(多行文字)
     */
    private String pointDirections;

    /**
     * 照片
     */
    private String pointPhoto;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 删除标记 0未删除 1已删除
     */
    private String delFlag;

    public AvoidancePoint() {}
}
