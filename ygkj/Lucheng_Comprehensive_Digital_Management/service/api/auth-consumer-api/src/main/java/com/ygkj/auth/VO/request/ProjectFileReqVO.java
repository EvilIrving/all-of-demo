package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/9/17 14:26
 * @Description: 项目申请 相关材料
 */
@Data
public class ProjectFileReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("材料名称")
    private String materialName;

    @ApiModelProperty("是否必要(1必要，0不必要)")
    private Integer isMust;

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("文件名称")
    private String fileName;
}
