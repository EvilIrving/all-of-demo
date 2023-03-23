package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("档案目录出参")
public class CatalogResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("所属父主键")
    private String parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("文档数量")
    private int count;

    @ApiModelProperty("包含子目录数量")
    private Integer childNumber;

    @ApiModelProperty("子集")
    private List<CatalogResVo> subCatalogs;

    private String createTime;

    private String createBy;
}
