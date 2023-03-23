package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.CatalogResVo;
import com.ygkj.digitization.vo.response.RecordListResVo;
import com.ygkj.digitization.vo.response.RecordResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "档案管理")
public interface RecordControllerApi {

    @ApiOperation(value = "获取档案目录树", response = CatalogResVo.class)
    CommonResult listCatalog();

    @ApiOperation(value = "获取档案目录列表，支持分页查询")
    CommonResult catalogs(CatalogPageQueryVo queryVo);

    @ApiOperation("编辑档案(新增传parentId 修改传id)")
    CommonResult editCatalog(CatalogReqVo catalogReqVo);

    @ApiOperation("删除档案目录")
    CommonResult delCatalog(String id);

    @ApiOperation("编辑档案")
    CommonResult editRecord(RecordReqVo reqVo, HttpServletRequest request);

    @ApiOperation("删除档案")
    CommonResult delRecord(String id);

    @ApiOperation(value = "档案列表", response = RecordListResVo.class)
    CommonResult listRecord(RecordListReqVo reqVo);

    @ApiOperation(value = "档案详情", response = RecordResVo.class)
    CommonResult selectRecord(String id);

    @ApiOperation("收藏文件")
    CommonResult collect(String id);

    @ApiOperation("取消收藏文件")
    CommonResult unCollect(String objectId);

    @ApiOperation("我的收藏")
    CommonResult myCollection(CollectionPageQueryVo queryVo);
}
