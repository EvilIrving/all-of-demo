package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.AttStBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/13
 */
@Data
@ApiModel("根据时间区间查询多个测站水雨工情数据回参")
public class MultiWaterRainResVo extends AttStBase {

    /**
     * 测站水雨工情集合
     */
    @ApiModelProperty("测站水雨工情集合")
    List<StPrrwtResVo> waterRains;
}
