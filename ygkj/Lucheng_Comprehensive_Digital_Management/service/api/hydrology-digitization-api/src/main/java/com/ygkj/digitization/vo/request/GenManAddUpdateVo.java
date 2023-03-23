package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description 日常管理-查询参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("日常管理-查询参数")
public class GenManAddUpdateVo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 任务目标
     */
    @ApiModelProperty("任务目标")
    private String taskTarget;

    /**
     * 存在的问题
     */
    @ApiModelProperty("存在的问题")
    private String existProblem;

    /**
     * 填报单位id
     */
    @ApiModelProperty("填报单位id")
    private String fillDeptId;

    /**
     * 填报单位名称
     */
    @ApiModelProperty("填报单位名称")
    private String fillDeptName;

    /**
     * 填报时间
     */
    @ApiModelProperty("填报时间")
    private Date reportTime;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private String taskState;

    /**
     * 附件id
     */
    @ApiModelProperty("附件id")
    private String attachId;

    /**
     * 行政编码
     */
    @ApiModelProperty("行政编码")
    private String adcd;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String adnm;
}
