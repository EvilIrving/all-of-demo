package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-30 15:16
 * @description 水土流失相关信息
 */
@Data
public class WaSoilErosionRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("图斑边界信息")
    private String boundary;
}
