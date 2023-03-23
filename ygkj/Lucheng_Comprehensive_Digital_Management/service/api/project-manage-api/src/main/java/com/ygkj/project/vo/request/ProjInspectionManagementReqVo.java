package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 检查管理
 * @date 2021-07-08
 */
@Data
@ApiModel("检查管理查询入参")
public class ProjInspectionManagementReqVo extends PageEntity {

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
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 类型 0:质量体系 1:质量行为
     */
    @ApiModelProperty("类型 0:质量体系 1:质量行为")
    private String type;

    /**
     * 审核状态 3:已驳回 2:未上报 1:待审核 0:已通过
     */
    @ApiModelProperty("审核状态 3:已驳回 2:未上报 1:待审核 0:已通过")
    private String status;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间 格式:yyyy-MM-dd")
    private String uploadTime;

    /**
     * 上传人id
     */
    @ApiModelProperty(value = "上传人id", hidden = true)
    private String uploadId;

    /**
     * 驳回意见
     */
    @ApiModelProperty("驳回意见")
    private String dismissedSuggestion;

    @ApiModelProperty(value = "建管中心的人员标识 true:是 false:否", hidden = true)
    private Boolean constructPersonFlag;

    public ProjInspectionManagementReqVo() {
    }

}
