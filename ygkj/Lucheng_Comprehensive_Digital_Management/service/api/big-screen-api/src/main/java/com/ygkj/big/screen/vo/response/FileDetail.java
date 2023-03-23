package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 18:07
 */
@Data
@ApiModel("文件描述")
public class FileDetail implements Serializable {
    @ApiModelProperty("文件名称")
    private String fileName;
    @ApiModelProperty("文件路径")
    private String absolutePath;
    @ApiModelProperty("文件类型")
    private String fileExt;
}
