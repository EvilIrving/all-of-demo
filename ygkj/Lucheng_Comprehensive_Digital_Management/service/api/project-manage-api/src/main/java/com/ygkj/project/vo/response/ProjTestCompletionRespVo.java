package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 工程监督-检测完成情况表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-检测完成情况回参")
public class ProjTestCompletionRespVo implements Serializable {

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
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    private Date checkTime;

    /**
     * 检测部位
     */
    @ApiModelProperty("检测部位")
    private String checkSite;

    /**
     * 检测数量
     */
    @ApiModelProperty("检测数量")
    private Integer checkNum;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果 0:合格 1:不合格 2:不做评定")
    private String checkResult;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String fileUrl;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploadName;


    public ProjTestCompletionRespVo() {
    }

}