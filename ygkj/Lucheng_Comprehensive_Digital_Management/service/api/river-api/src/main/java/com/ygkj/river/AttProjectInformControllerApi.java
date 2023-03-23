package com.ygkj.river;

import com.ygkj.river.vo.request.InformReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-07-12 9:50
 * @description
 */
@Api(tags = "项目举报接口")
public interface AttProjectInformControllerApi {

    @ApiOperation("新增或更新举报")
    CommonResult saveOrUpdateInform(InformReqVo reqVo);

    @ApiOperation("根据项目查询对应的举报信息列表")
    CommonResult getInformList(InformReqVo reqVo);

    @ApiOperation("根据id 删除对应举报信息")
    CommonResult deleteInform(String id);

    @ApiOperation("查看详情")
    CommonResult detail(String id);



}
