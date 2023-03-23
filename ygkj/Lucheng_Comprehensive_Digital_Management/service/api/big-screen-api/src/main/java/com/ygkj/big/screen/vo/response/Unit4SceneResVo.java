package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.BsUnit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/6/29
 */
@Data
public class Unit4SceneResVo extends BsUnit {

    private static final long serialVersionUID = -2803601623101279777L;
    /**
     * 组件在大屏上的定位
     */
    @ApiModelProperty("组件在大屏上的定位")
    private String unitSceneProperty;

    /**
     * 场景名称
     */
    @ApiModelProperty("场景名称")
    private String sceneName;
}
