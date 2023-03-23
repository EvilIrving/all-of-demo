package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2021/4/9 11:03
 * @Description:
 */
@Data
public class GroupWarnLogQueryVo {

    @ApiModelProperty("组id")
    private Long groupId;

    @ApiModelProperty("是否查全部，查看更多传true，否则false")
    private boolean fetchAll;
}
