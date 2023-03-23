package com.ygkj.project.vo.request;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/9/29
 */
@Data
@ApiModel("水源保障查询入参")
public class RsvrWatSrcQueryVo {
    @ApiModelProperty("水源地名称")
    String watSrcName;
}
