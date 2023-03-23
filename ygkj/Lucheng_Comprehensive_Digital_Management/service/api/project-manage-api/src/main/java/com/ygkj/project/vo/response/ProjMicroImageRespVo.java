package com.ygkj.project.vo.response;

import java.io.Serializable;

import com.ygkj.project.model.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-微影像管理表")
public class ProjMicroImageRespVo implements Serializable {

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
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 上传人id
     */
    @ApiModelProperty("上传人id")
    private String uploadId;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploadName;

    /**
     * 类型 0:重要隐蔽工程 1:每日主要作业面
     */
    @ApiModelProperty("类型 0:重要隐蔽工程 1:每日主要作业面")
    private String type;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "图片文件list")
    private List<SysFile> imgList;

    @ApiModelProperty(value = "app图片文件")
    private SysFile imgFile;

    public ProjMicroImageRespVo() {
    }

}