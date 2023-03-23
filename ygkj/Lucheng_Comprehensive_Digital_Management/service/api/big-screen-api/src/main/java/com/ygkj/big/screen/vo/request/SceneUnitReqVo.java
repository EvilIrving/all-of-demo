package com.ygkj.big.screen.vo.request;

import com.ygkj.big.screen.model.BsScene;
import com.ygkj.big.screen.model.BsUnitScene;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
@Data
@ApiModel("场景新增修改入参")
public class SceneUnitReqVo extends BsScene {

    private static final long serialVersionUID = 3912972566730685931L;

    @ApiModelProperty("组件列表")
    List<BsUnitScene> unitList;
}
