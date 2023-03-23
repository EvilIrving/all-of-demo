package com.ygkj.river.vo.response;

import com.ygkj.river.model.AttRiverOutletBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/10/9
 */
@Data
@ApiModel("入河排污口回参")
public class RiverOutletResVo extends AttRiverOutletBase {
    private static final long serialVersionUID = -6712058188646859662L;

    @ApiModelProperty("最近排污口距离，单位km")
    Double nearestWainDistance;
}
