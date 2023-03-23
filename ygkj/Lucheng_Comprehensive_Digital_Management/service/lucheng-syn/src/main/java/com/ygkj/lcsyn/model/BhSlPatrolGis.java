package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhSlPatrolGis {

    /**
     * 巡查总记录编号
     */
    private Integer id;

    /**
     * 工程编号
     */
    private String skid;

    /**
     * 巡查开始时间
     */
    private Date patrolBegintime;

    /**
     * 巡查结束时间
     */
    private Date patrolEndtime;

    /**
     * 隐患数量
     */
    private Integer hiddenTroubleCount;

    /**
     * 巡查人员
     */
    private Integer uId;

    /**
     * 巡查类型
     */
    private String patrolType;

    /**
     * 校核人编号
     */
    private String proposeId;

    /**
     * 审核人编号
     */
    private String verifyId;

    /**
     * is_complete
     */
    private Integer isComplete;
}
