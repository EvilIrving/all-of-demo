package com.ygkj.digitization;

import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.AttTideFcstValBase;
import com.ygkj.digitization.vo.request.FcstValReqVo;
import com.ygkj.digitization.vo.request.TideFcstQueryVo;
import com.ygkj.digitization.vo.response.AttTideFcstValResVo;
import com.ygkj.digitization.vo.response.FcstStResVo;
import com.ygkj.digitization.vo.response.TideFcstResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Api(tags = "风暴潮预报接口")
public interface TideFcstControllerApi {

    @ApiOperation(value = "查询风暴潮预报列表", response = TideFcstResVo.class)
    CommonResult pageTideFcst(TideFcstQueryVo queryVo);

    @ApiOperation("新增或更新风暴潮预报")
    CommonResult addOrUpdateTideFcst(TideFcstResVo resVo);

    @ApiOperation(value = "根据id风暴潮预报", response = TideFcstResVo.class)
    CommonResult getTideFcst(String id);

    @ApiOperation("删除风暴潮预报")
    CommonResult deleteTideFcst(String id);

    @ApiOperation(value = "查询最新一项风暴潮手动预报", response = TideFcstResVo.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "releaseType", value = "发布类型1：手动预报，2自动预报")})
    CommonResult newestTideFcst(Integer releaseType);

    @ApiOperation(value = "手动报警潮位测站列表", response = AttStBase.class)
    CommonResult manualFcstTideList();

    @ApiOperation(value = "根据预报id查询风暴潮列表", response = AttTideFcstValResVo.class)
    CommonResult listTileFcst(String fcstId);

    @ApiOperation(value = "根据预报id查询风暴潮站点列表", response = FcstStResVo.class)
    CommonResult listTileFcstSt(@PathVariable("fcstId") String fcstId);

    @ApiOperation(value = "风暴潮列表", response = AttTideFcstValResVo.class)
    CommonResult listTileFcstVal(FcstValReqVo reqVo);
}
