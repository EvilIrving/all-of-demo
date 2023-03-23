package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 摄像头质量记录表
 * @date 2021-04-13
 */
@Data
public class AttCameraQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监控点编号
     */
    private String indexCode;

    /**
     * 偏色检测结果(2-诊断失败，-1-未知，1-正常，0-异常)
     */
    private Integer scChromaResult;

    /**
     * 雪花点干扰结果(2-诊断失败，-1-未知，1-正常，0-异常)
     */
    private Integer scSnowResult;

    /**
     * 图像过暗结果(2-诊断失败，-1-未知，1-正常，0-异常)
     */
    private Integer scDarkResult;

    /**
     * 巡检日期（格式：yyyy-mm-dd）
     */
    private Date inspectDate;

    /**
     * 亮度异常结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scLumaResult;

    /**
     * 画面冻结结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scFreezeResult;

    /**
     * 视频抖动结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scShakeResult;

    /**
     * 分辨率（720为基准 1-高清，0-标清，-1-未知）
     */
    private Integer resolution;

    /**
     * 对比度检测结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scContrastResult;

    /**
     * 条纹干扰结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scStreakResult;

    /**
     * 图片url地址
     */
    private String picUrl;

    /**
     * 视频遮挡结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scCoverResult;

    /**
     * 视频丢失结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scSignalResult;

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

    /**
     * 视频剧变结果（2-诊断失败，-1-未知，1-正常，0-异常）
     */
    private Integer scFlashResult;

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
    private Integer delFlag;

    /**
     * 部门id
     */
    private String deptId;

    public AttCameraQuality() {
    }

}

