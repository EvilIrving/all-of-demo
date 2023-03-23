package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhDepPersontrain {

    @TableId(type = IdType.INPUT)
    /**
     * 编号
     */
    private Integer id;

    /**
     * 培训人员
     */
    private String person;

    /**
     * 培训时间
     */
    private Date time;

    /**
     * 培训内容
     */
    private String content;

    /**
     * 培训经费
     */
    private BigDecimal fund;

    /**
     * 培训证明
     */
    private String prove;

    /**
     * 附件
     */
    private String aid;

    /**
     * dep_id
     */
    private Integer depId;

    /**
     * pjcd
     */
    private String pjcd;

    /**
     * 培训计划
     */
    private String trainPlan;

    /**
     * 执行情况
     */
    private String execStatus;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 批号
     */
    private String batch;

    /**
     * 提交人
     */
    private Integer submitPerson;
}
