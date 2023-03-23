package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物资信息
 *
 * @author huangzh 2021-01-18
 */
@Data
public class AttMaterialBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 物资类型id
     */
    private String typeId;

    /**
     * 物资类型
     */
    private String type;

    /**
     * 物资名称
     */
    private String name;

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 数量
     */
    private String num;

    /**
     * 单位
     */
    private String measure;

    /**
     * 价格
     */
    private String price;

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

    public AttMaterialBase() {
    }

}

