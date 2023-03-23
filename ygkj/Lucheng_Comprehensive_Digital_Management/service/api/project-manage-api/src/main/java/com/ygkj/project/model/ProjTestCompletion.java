package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-检测完成情况
 * @date 2021-07-09
 */
@Data
public class ProjTestCompletion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 检测内容
     */
    private String content;

    /**
     * 检测时间
     */
    private Date checkTime;

    /**
     * 检测部位
     */
    private String checkSite;

    /**
     * 检测数量
     */
    private Integer checkNum;

    /**
     * 检测结果 0:合格 1:不合格 2:不做评定
     */
    private String checkResult;

    /**
     * 附件
     */
    private String fileUrl;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人id
     */
    private String uploadId;

    /**
     * 上传人姓名
     */
    private String uploadName;

    /**
     * 删除标识
     */
    private String delFlag;

    public ProjTestCompletion() {
    }

}