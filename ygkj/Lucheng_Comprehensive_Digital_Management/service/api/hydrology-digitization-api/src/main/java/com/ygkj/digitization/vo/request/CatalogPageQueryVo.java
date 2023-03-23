package com.ygkj.digitization.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CatalogPageQueryVo extends PageEntity {

    @ApiModelProperty("档案名称")
    private String name;

    @ApiModelProperty("父级目录id")
    private String parentId;
}
