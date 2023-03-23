package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.BusDuplicateCollectionReq;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 13:33
 */
@Api(tags = "复盘收藏")
public interface BusDuplicateCollectionControllerApi {

    @ApiOperation("新增或修改")
    CommonResult saveOrUpdate(BusDuplicateCollectionReq entity);

    @ApiOperation("根据主键id删除")
    CommonResult del(String id);

    @ApiOperation("获取列表")
    CommonResult getList(BusDuplicateCollectionReq entity);
}
