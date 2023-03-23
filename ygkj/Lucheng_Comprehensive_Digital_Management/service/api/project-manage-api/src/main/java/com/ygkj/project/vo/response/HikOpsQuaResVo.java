package com.ygkj.project.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@Data
public class HikOpsQuaResVo extends HikCameraResVo {
    private static final long serialVersionUID = -4350374930642252305L;

    /**
     * 在线次数
     */
    private Integer onlineTimes;

    /**
     * 检查在线次数
     */
    private Integer checkOnlineTimes;

    private Double onlinePercent;

    /**
     * 黑白图像结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scMonoResult;

    /**
     * 视频巡检结果（0-视频异常，1-视频正常，2-登录失败，3-取流异常，4-解码失败，5-码流延时，6-诊断失败）
     */
    private Integer inspectResult;

    /**
     * 图像模糊结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scBlurResult;

    /**
     * 场景变更结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scSceneResult;

    private Date lastWarnTime;
}
