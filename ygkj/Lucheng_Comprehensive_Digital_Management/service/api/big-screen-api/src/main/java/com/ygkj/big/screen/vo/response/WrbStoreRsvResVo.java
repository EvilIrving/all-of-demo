package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.WrbStoreRsvr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Data
@ApiModel("大中型水库蓄水量")
public class WrbStoreRsvResVo extends WrbStoreRsvr {

    private static final long serialVersionUID = -6012535380902010902L;

    @ApiModelProperty("当年蓄水量")
    BigDecimal currentStore;

    @ApiModelProperty("去年年蓄水量")
    BigDecimal lastYearStore;

    @ApiModelProperty("差值")
    BigDecimal subtract;

    public void doSubtract() {
        if (currentStore != null && lastYearStore != null) {
            subtract = currentStore.subtract(lastYearStore);
        }
    }

}
