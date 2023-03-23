package com.ygkj.auth.api.archivecollection;

import com.ygkj.auth.VO.request.SysArchiveCollectionReqVO;
import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 18:37
 */
@Api(tags = "档案收藏服务")
public interface SysArchiveCollectionApi {

    @ApiOperation(value = "档案收藏详情")
    CommonResult detailInfo(String id);

    @ApiOperation(value = "档案收藏分页查询")
    PageVO<SysArchiveCollection> pageInfo(SysArchiveCollectionReqVO vo);

    @ApiOperation(value = "新增档案收藏")
    CommonResult addSysArchiveCollection(SysArchiveCollection vo);

    @ApiOperation(value = "删除档案收藏")
    CommonResult deleteSysArchiveCollection(String id);

    @ApiOperation(value = "更新档案收藏")
    CommonResult updateSysArchiveCollection(SysArchiveCollection vo);

}
