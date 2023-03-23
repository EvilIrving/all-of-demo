package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 变更管理表
 * @date 2021-07-08
 */
@Data
@ApiModel("变更管理/审批情况/强制条文/质量核备 新增或修改入参")
public class ProjChangeManagementInsVo implements Serializable {

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
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 类型 0: 合同变更 1: 人员变更
     */
    @ApiModelProperty("类型 0: 合同变更 1: 人员变更 2:危大工程 3:专项方案 4: 强制条文 5:质量核备-重要隐蔽 " +
            "6:质量核备-关键部位 7:质量核备-分部工程 8:质量核备-外观评定 9:质量核备-单位工程 10:批复文件 11:检查计划")
    private String type;

    public ProjChangeManagementInsVo() {
    }

}