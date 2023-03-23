package com.ygkj.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/10/10 10:26
 * @Description: 流程定制-操作人员
 */
@Data
public class OperatorRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("是否已选择，默认为未选择")
    private boolean choose;
}
