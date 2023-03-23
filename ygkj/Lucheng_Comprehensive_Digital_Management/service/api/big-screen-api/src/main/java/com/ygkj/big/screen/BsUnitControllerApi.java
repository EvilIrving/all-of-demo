package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.BsUnitQueryReqVo;
import com.ygkj.big.screen.vo.request.SceneUnitReqVo;
import com.ygkj.big.screen.model.BsUnit;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
@Api(tags = "大屏组件接口")
public interface BsUnitControllerApi {

    @ApiOperation("各个大屏组件")
    CommonResult unitNumStatistic();

    @ApiOperation("查询大屏组件信息")
    CommonResult listUnit(BsUnitQueryReqVo reqVo);

    @ApiOperation("添加或更新一项大屏组件信息")
    CommonResult addOrUpdateUnit(BsUnit record);

    @ApiOperation("删除一项大屏组件信息")
    CommonResult deleteUnit(@ApiParam("组件id") String id);

    @ApiOperation("查询大屏场景信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "bsName", value = "大屏名称"),
            @ApiImplicitParam(name = "mode", value = "模式：1、大屏模式；2、PC模式，前端不传默认为大屏模式")})
    CommonResult listScene(String bsName, Integer mode);

    @ApiOperation("添加或更新一项大屏场景信息")
    CommonResult addOrUpdateScene(SceneUnitReqVo record);

    @ApiOperation("删除一项大屏场景信息")
    CommonResult deleteScene(@ApiParam("场景id") String id);

    @ApiOperation("设置场景为默认场景")
    CommonResult setSceneAsDefault(String sceneId, String bsName);

    @ApiOperation("查询一项完整的大屏")
    CommonResult listFullBigScreen(@ApiParam("场景id") String id,
                                   @ApiParam("大屏名称") String bsName,
                                   @ApiParam("场景名称") String sceneName,
                                   @ApiParam("模式") Integer mode);

    @ApiOperation("立即执行缓存")
    CommonResult doCacheRightNow();
}
