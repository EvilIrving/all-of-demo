package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.SysThresholdAddOrUpdateVo;
import com.ygkj.platform.management.vo.request.SysThresholdPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "指标管理-----阈值设置", value = "指标管理-----阈值设置")
public interface SysThresholdControllerApi {

    @ApiOperation("列表查询")
    CommonResult thresholds(SysThresholdPageQueryVo queryVo);

    @ApiOperation("新增阈值")
    CommonResult add(SysThresholdAddOrUpdateVo addOrUpdateVo);

    @ApiOperation("更新阈值")
    CommonResult update(SysThresholdAddOrUpdateVo addOrUpdateVo);

    @ApiOperation("删除阈值")
    CommonResult delete(Long id);
}
