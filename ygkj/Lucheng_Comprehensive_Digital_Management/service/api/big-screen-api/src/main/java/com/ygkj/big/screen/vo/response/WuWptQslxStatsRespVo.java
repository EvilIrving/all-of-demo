package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 取水许可类型统计
 */
@Data
public class WuWptQslxStatsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("取水类型")
    private String qslx;

    @ApiModelProperty("数量")
    private Integer num;
}
