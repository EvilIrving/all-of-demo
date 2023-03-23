package com.ygkj.digitization.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 风暴潮预报表
 * @date 2021-09-22
 */
@Data
@ApiModel("风暴潮预报表")
public class AttTideFcstBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预报id
     */
    @ApiModelProperty("预报id")
    private String id;

    /**
     * 预报发布时刻
     */
    @ApiModelProperty("预报发布时刻")
    private Date releaseTime;

    /**
     * 发布类型1：手动预报，2自动预报
     */
    @ApiModelProperty("发布类型1：手动预报，2自动预报")
    private Integer releaseType;

    /**
     * 附件名称
     */
    @ApiModelProperty("附件名称")
    private String attachName;

    /**
     * 附件url
     */
    @ApiModelProperty("附件url")
    private String attachUrl;

    @ApiModelProperty("预报起始时刻")
    private String fcstStart;

    @ApiModelProperty("预报结束时刻")
    private String fcstEnd;

    public AttTideFcstBase() {
    }

}
