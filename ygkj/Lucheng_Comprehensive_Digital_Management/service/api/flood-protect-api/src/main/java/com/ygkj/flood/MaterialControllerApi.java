package com.ygkj.flood;

import com.ygkj.flood.vo.request.AttMaterialBaseAddVo;
import com.ygkj.flood.vo.request.AttMaterialQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "物资管理", value = "物资管理")
public interface MaterialControllerApi {
    @ApiOperation("物资查询")
    CommonResult material(AttMaterialQueryVo queryVo);

    @ApiOperation("物资更新（新增通用）")
    CommonResult addMaterial(List<AttMaterialBaseAddVo> list);
}
