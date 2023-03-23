package com.ygkj.flood.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-22 15:48
 */
@Data
public class AttWarehouseDeviceBase {
    /**
     * id
     */
    private Integer id;

    /**
     * 物资名称
     */
    private String name;

    /**
     * 物资类型
     */
    private String type;

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 规格
     */
    private String measure;

    /**
     * 物资价值
     */
    private String materialPrice;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private int delFlag;
}
