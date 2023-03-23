package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户收藏参数封装")
public class UserCollectionAddVo {

    /**
     *
     */
    @ApiModelProperty("前端忽略")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty("前端忽略")
    private String userId;

    /**
     * 设备编码（测站/工程/...）
     */
    @ApiModelProperty("设备编码")
    private String code;

    /**
     * 收藏设备的类型（水位/雨量/工程...）
     */
    @ApiModelProperty("收藏设备的类型（水位/雨量/工程...）")
    private String type;
}
