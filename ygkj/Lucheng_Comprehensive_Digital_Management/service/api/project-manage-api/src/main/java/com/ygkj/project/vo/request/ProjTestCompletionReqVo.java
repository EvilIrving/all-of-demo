package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 工程监督-检测完成情况
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-检测完成情况 查询入参")
public class ProjTestCompletionReqVo extends PageEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 检测内容
     */
    @ApiModelProperty("检测内容")
    private String content;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果 0:合格 1:不合格 2:不做评定")
    private String checkResult;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private String uploadTime;

    /**
     * 上传人id
     */
    @ApiModelProperty(value = "上传人id", hidden = true)
    private String uploadId;

    public ProjTestCompletionReqVo() {
    }

}