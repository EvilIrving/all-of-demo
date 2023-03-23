package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
@Data
public class ProjSupervisionReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 附件
     */
    private String fileUrl;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 类型 0:监理月报 1:质量评定 2:缺陷备案
     */
    private String type;

    public ProjSupervisionReport() {
    }

}