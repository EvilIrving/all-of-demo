package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UnitPositionSetBean implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("负责人")
    private String leader;


    @ApiModelProperty("负责人电话")
    private String leaderPhone;


    @ApiModelProperty("负责人证书文件")
    private String leaderCertificatefile;

    @ApiModelProperty("负责人职务")
    private String leaderPost;

    @ApiModelProperty("负责人证书文件名称")
    private String leaderCertificatefileName;

    @ApiModelProperty("负责人证书文件地址")
    private String leaderCertificatefileUrl;
}
