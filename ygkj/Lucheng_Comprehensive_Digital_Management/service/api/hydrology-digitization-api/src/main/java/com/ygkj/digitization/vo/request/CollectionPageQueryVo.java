package com.ygkj.digitization.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CollectionPageQueryVo extends PageEntity {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("前端忽略")
    private String userId;
    @ApiModelProperty("前端忽略")
    private String collectionType;
    @ApiModelProperty("前端忽略")
    private String objectId;

}
