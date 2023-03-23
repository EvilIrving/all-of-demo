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
public class BsUnitDataIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单条组件数据id
     */
    private String dataId;

    /**
     * 多个组件数据id
     */
    private String groupId;

    /**
     * 方案id
     */
    private String caseId;

    /**
     * 组件id
     */
    private String unitId;

    /**
     * 组件名称
     */
    private String unitName;

    /**
     * 时序，rn如果是实时数据，就为空，或者当前unix时间戳，rn如果是非实时数据，那就是时刻的序号
     */
    private Long timeIndex;

    /**
     * 大屏组件的api，不是调用服务的api
     */
    private String dataApi;

    /**
     * 大屏组件的入参，不是调用服务的入参
     */
    private String dataParam;

    /**
     * 0标识演练数据，1标识实时数据
     */
    private Integer dataType;

    /**
     * 如果是非实时数据，那就是时刻的序号
     */
    private Date timeStamp;

    public BsUnitDataIndex() {
    }

}
