package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 组件数据影响关联表
 * @date 2021-07-09
 */
@Data
public class BsInfluentRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 组件数据id
     */
    private String groupDataId;

    /**
     * 被影响组件数据id
     */
    private String infGroupDataId;

    public BsInfluentRel() {
    }

}

