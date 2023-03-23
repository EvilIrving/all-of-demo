package com.dcy.datamanage.api.model.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author：dcy
 * @Description: 分页类
 * @Date: 2020/11/26 8:33
 */
@Getter
@Setter
public class PageModel {

    @ApiModelProperty(value = "是否分页 true分页 false和不传不分页")
    private boolean page;

    @ApiModelProperty(value = "当前页面", notes = "默认1", example = "1")
    private Long current = 1L;

    @ApiModelProperty(value = "每页显示条数", notes = "默认30", example = "30")
    private Long size = 30L;

    @ApiModelProperty(value = "排序字段", notes = "对于model字段")
    private String sort;

    @ApiModelProperty(value = "排序类型", notes = "asc 或者 desc", allowableValues = "asc,desc")
    private String order;
}
