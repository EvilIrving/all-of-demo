package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 方案测站关联表
 * @date 2021-07-08
 */
@Data
public class BsDrillStcase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联id
     */
    private String id;

    /**
     * 演练测站编码测站或工程编码，如雨量测站编码和水库工程编码
     */
    private String drillStCode;

    /**
     * 演练测站名称，水雨工情测站名称，或工程名称
     */
    private String drillStName;

    /**
     * 演练测站类型水库工程编码，雨量测站，山洪
     */
    private String drillStType;

    /**
     * 演练方案id
     */
    private String caseId;

    public BsDrillStcase() {
    }

}
