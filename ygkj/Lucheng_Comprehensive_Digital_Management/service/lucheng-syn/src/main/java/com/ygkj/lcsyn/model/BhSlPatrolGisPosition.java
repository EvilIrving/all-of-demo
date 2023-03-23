package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
public class BhSlPatrolGisPosition {

    /**
     * id
     */
    private Integer id;

    /**
     * 工程编号
     */
    private String skid;

    /**
     * 巡查总记录编号
     */
    private Integer gid;

    /**
     * 巡查点名称
     */
    private String projectPosition;

    /**
     * 巡查人员编号
     */
    private Integer userrid;

    /**
     * 巡查上报时间
     */
    private Date patrolTm;

    /**
     * 是否安全
     */
    private int issafe;

    /**
     * 隐患详情
     */
    private String hiddenTrouble;

    /**
     * 巡查审核情况（弃）
     */
    private String hiddenTroublePropose;

    /**
     * 隐患状态（未处理、处理中、已完成）
     */
    private Integer hiddenTroubleStatus;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;

    /**
     * 校核时间
     */
    private Date proposeTm;

    /**
     * 隐患处理前附件编号，多个逗号隔开
     */
    private String beforePic;

    /**
     * 隐患处理后附件编号，多个逗号隔开
     */
    private String afterPic;

    /**
     * 审核时间
     */
    private Date verifyTime;

    /**
     * 隐患指派处理人员
     */
    private String assignUid;

    /**
     * 安全检查点描述编号
     */
    private String checkpointSaveId;

    /**
     * 隐患检查点描述编号
     */
    private String checkpointHiddenId;

    /**
     * 巡查点编号
     */
    private Integer cellId;

    /**
     * 巡查类型
     */
    private Integer patrolType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BhSlPatrolGisPosition that = (BhSlPatrolGisPosition) o;
        return issafe == that.issafe && Objects.equals(id, that.id) && Objects.equals(skid, that.skid) && Objects.equals(gid, that.gid) && Objects.equals(projectPosition, that.projectPosition) && Objects.equals(userrid, that.userrid) && Objects.equals(patrolTm, that.patrolTm) && Objects.equals(hiddenTrouble, that.hiddenTrouble) && Objects.equals(hiddenTroublePropose, that.hiddenTroublePropose) && Objects.equals(hiddenTroubleStatus, that.hiddenTroubleStatus) && Objects.equals(lgtd, that.lgtd) && Objects.equals(lttd, that.lttd) && Objects.equals(proposeTm, that.proposeTm) && Objects.equals(beforePic, that.beforePic) && Objects.equals(afterPic, that.afterPic) && Objects.equals(verifyTime, that.verifyTime) && Objects.equals(assignUid, that.assignUid) && Objects.equals(checkpointSaveId, that.checkpointSaveId) && Objects.equals(checkpointHiddenId, that.checkpointHiddenId) && Objects.equals(cellId, that.cellId) && Objects.equals(patrolType, that.patrolType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
