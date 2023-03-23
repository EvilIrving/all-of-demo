package com.ygkj.flood;

import com.ygkj.flood.model.AttPustBase;
import com.ygkj.flood.model.AttWagaBase;
import com.ygkj.flood.vo.request.ProjectListReqVo;
import com.ygkj.flood.vo.response.AllStorageResVo;
import com.ygkj.flood.vo.response.PustListResVo;
import com.ygkj.flood.vo.response.StorageResVo;
import com.ygkj.flood.vo.response.WagaListResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "防汛研判-工程分析")
public interface FloodProjectControllerApi {

    @ApiOperation(value = "可蓄水量/纳蓄能力统计", response = StorageResVo.class)
    @ApiImplicitParam(name = "type", value = "1可蓄水量 2纳蓄能力")
    CommonResult countStorage(Integer type, Integer resScal);

    @ApiOperation(value = "大中型水库蓄水率", response = AllStorageResVo.class)
    CommonResult countAllStorageRate();

    @ApiOperation(value = "水闸工情列表", response = WagaListResVo.class)
    CommonResult listWaga(ProjectListReqVo reqVo);

    @ApiOperation(value = "泵站列表", response = PustListResVo.class)
    CommonResult listPust(ProjectListReqVo reqVo);

    @ApiOperation(value = "水闸工程列表", response = AttWagaBase.class)
    CommonResult listWagaProj(Integer pageSize, Integer pageNum);

    @ApiOperation(value = "泵站工程列表", response = AttPustBase.class)
    CommonResult listPustProj(Integer pageSize, Integer pageNum, String pustCode);

    @ApiOperation(value = "查询所有水库的实时水位、纳蓄能力、实时库容", response = AttPustBase.class)
    CommonResult listRsvrStorageWaterLevel();

}
