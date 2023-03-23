package com.ygkj.project.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 变更管理表
 * @date 2021-07-08
 */
@Data
@ApiModel("变更管理/审批情况/强制条文/质量核备 回参")
public class ProjChangeManagementRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 文件名称/人员名称
     */
    @ApiModelProperty("文件名称/人员名称")
    private String name;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String fileUrl;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 上传人员姓名
     */
    @ApiModelProperty("上传人员姓名")
    private String uploadName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 类型 0: 合同变更 1: 人员变更
     */
    @ApiModelProperty("类型 0: 合同变更 1: 人员变更")
    private String type;

    public ProjChangeManagementRespVo() {
    }

}
