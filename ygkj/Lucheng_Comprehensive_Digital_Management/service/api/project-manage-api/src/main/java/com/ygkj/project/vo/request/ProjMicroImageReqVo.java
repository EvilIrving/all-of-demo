package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-微影像管理表")
public class ProjMicroImageReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 所属工程编码
     */
    @ApiModelProperty("所属工程编码")
    private String projectCode;

    /**
     * 单位工程编码
     */
    @ApiModelProperty("单位工程编码")
    private String unitProjectCode;

    /**
     * 分布工程编码
     */
    @ApiModelProperty("分布工程编码")
    private String distProjectCode;

    /**
     * 图片ids
     */
    @ApiModelProperty("图片ids")
    private String imgIds;

    /**
     * 类型 0:重要隐蔽工程 1:每日主要作业面
     */
    @ApiModelProperty("类型 0:重要隐蔽工程 1:每日主要作业面")
    private String type;

    @ApiModelProperty(value = "上传人姓名", hidden = true)
    private String uploadId;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public ProjMicroImageReqVo() {
    }

}
