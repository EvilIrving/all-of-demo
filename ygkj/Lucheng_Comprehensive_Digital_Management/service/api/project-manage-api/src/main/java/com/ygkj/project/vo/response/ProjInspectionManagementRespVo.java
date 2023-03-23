package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 检查管理
 * @date 2021-07-08
 */
@Data
@ApiModel("检查管理回参")
public class ProjInspectionManagementRespVo implements Serializable {

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
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件地址，多个文件用逗号隔开
     */
    @ApiModelProperty("文件地址，多个文件用逗号隔开")
    private String fileUrl;

    /**
     * 类型 0:质量体系 1:质量行为
     */
    @ApiModelProperty("类型 0:质量体系 1:质量行为")
    private String type;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploadName;

    /**
     * 上传人id
     */
    @ApiModelProperty("上传人id")
    private String uploadId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 审核状态 3:已驳回 2:未上报 1:待审核 0:已通过
     */
    @ApiModelProperty("审核状态 3:已驳回 2:未上报 1:待审核 0:已通过")
    private String status;

    @ApiModelProperty("审核标识")
    private Boolean checkFlag;

    /**
     * 驳回意见
     */
    @ApiModelProperty("驳回意见")
    private String dismissedSuggestion;

    public ProjInspectionManagementRespVo() {
    }

}