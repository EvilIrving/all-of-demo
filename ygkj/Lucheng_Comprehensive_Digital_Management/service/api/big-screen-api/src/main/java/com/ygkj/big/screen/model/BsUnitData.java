package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 组件数据表
 * @date 2021-07-09
 */
@Data
public class BsUnitData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据id
     */
    private String dataId;

    /**
     * json数据
     */
    private String dataValue;

    public BsUnitData() {
    }

}
