package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
@Data
public class ProjMicroImage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 所属工程编码
     */
    private String projectCode;

    /**
     * 单位工程编码
     */
    private String unitProjectCode;

    /**
     * 分布工程编码
     */
    private String distProjectCode;

    /**
     * 图片ids
     */
    private String imgIds;

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

    /**
     * 类型 0:重要隐蔽工程 1:每日主要作业面
     */
    private String type;

    public ProjMicroImage() {
    }

}