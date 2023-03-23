package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 检查管理
 * @date 2021-07-08
 */
@Data
@ApiModel("检查管理新增或修改入参")
public class ProjInspectionManagementInsReqVo implements Serializable {

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
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 审核状态 0:未上传 1:待审核 2:已通过 3:已驳回
     */
    @ApiModelProperty("审核状态 0:未上传 1:待审核 2:已通过 3:已驳回")
    private String status;

    /**
     * 驳回意见
     */
    @ApiModelProperty("驳回意见")
    private String dismissedSuggestion;

    public ProjInspectionManagementInsReqVo() {
    }

}

