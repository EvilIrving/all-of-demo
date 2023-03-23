package com.ygkj.digitization;

import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.HydrFcstQueryVo;
import com.ygkj.digitization.vo.response.HydrFcstResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Api(tags = "水文预报接口")
public interface HydrFcstControllerApi {

    @ApiOperation(value = "查询水文预报列表", response = HydrFcstResVo.class)
    CommonResult pageHydrFcst(HydrFcstQueryVo queryVo);

    @ApiOperation("新增或更新水文预报")
    CommonResult addOrUpdateHydrFcst(HydrFcstResVo resVo);

    @ApiOperation(value = "根据id水文预报", response = HydrFcstResVo.class)
    CommonResult getHydrFcst(String id);

    @ApiOperation("删除水文预报")
    CommonResult deleteHydrFcst(String id);

    @ApiOperation(value = "最新的水文预报", response = HydrFcstResVo.class)
    CommonResult newestHydrFcst();

    @ApiOperation(value = "列出水文测站", response = AttStBase.class)
    CommonResult listHydrStation(String stType, String stName);

}
