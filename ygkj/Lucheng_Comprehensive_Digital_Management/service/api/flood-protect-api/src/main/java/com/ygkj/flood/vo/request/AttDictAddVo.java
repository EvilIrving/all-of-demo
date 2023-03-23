package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttDictAddVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 字典码，用于区分枚举值类型
     */
    @ApiModelProperty("字典码")
    private String dictCode;

    /**
     * 字典枚举具体释义
     */
    @ApiModelProperty("字典枚举具体释义")
    private String dictName;

    /**
     * 父级id
     */
    @ApiModelProperty("父级id")
    private String parentId;
}
