package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.AttHydrDiagBase;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/2
 */
@Data
public class HydrDiagResVo extends AttHydrDiagBase {
    /**
     * 编码工程对应的名称
     */
    String codeName;

    /**
     * 实时水位、实时潮位
     */
    Double waterLevel;

    /**
     * 警戒水位、警戒潮位、汛限水位
     */
    Double warnLimitLevel;

    /**
     * 入库流量
     */
    Double inFlow;

    /**
     * 出库流量
     */
    Double outFlow;

    /**
     * 流量、过闸流量
     */
    String flow;

    /**
     * 闸上水位
     */
    Double upz;

    /**
     * 闸下水位
     */
    Double dwz;

    /**
     * 闸门开度
     */
    String ovs;

    /**
     * 是否超汛限、超警戒
     */
    boolean overWarnLimit = false;

    /**
     * 闸门是否开启
     */
    boolean gateOpened = false;

    /**
     * 额外信息
     */
    Object extraInfo;

    /**
     * 是否暂无数据
     */
    boolean noData;

    Date tm;

    /**
     * 水库入库出库流量上报时间
     */
    Date reportTime;
}
