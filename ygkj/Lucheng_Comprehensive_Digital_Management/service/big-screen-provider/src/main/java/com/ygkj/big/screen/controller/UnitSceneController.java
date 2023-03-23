package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.unit.notify.event.UnitCacheEvent;
import com.ygkj.big.screen.BsUnitControllerApi;
import com.ygkj.big.screen.model.BsUnit;
import com.ygkj.big.screen.service.UnitSceneService;
import com.ygkj.big.screen.vo.request.BsUnitQueryReqVo;
import com.ygkj.big.screen.vo.request.SceneUnitReqVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
@RestController
@RequestMapping("unitScene")
public class UnitSceneController implements BsUnitControllerApi {

    @Autowired
    UnitSceneService unitSceneService;

    @GetMapping("unitNumStatistic")
    @Override
    public CommonResult unitNumStatistic() {
        return CommonResult.success(unitSceneService.unitNumStatistic());
    }

    @GetMapping("listUnit")
    @Override
    public CommonResult listUnit(BsUnitQueryReqVo reqVo) {
        return CommonResult.success(unitSceneService.listUnit(reqVo));
    }

    @PostMapping("addOrUpdateUnit")
    @Override
    public CommonResult addOrUpdateUnit(@RequestBody BsUnit record) {
        return unitSceneService.addOrUpdateUnit(record);
    }

    @DeleteMapping("deleteUnit")
    @Override
    public CommonResult deleteUnit(@RequestParam("id") String id) {
        return unitSceneService.deleteUnit(id);
    }

    @GetMapping("listScene")
    @Override
    public CommonResult listScene(String bsName, Integer mode) {
        return CommonResult.success(unitSceneService.listScene(bsName, mode));
    }

    @PostMapping("addOrUpdateScene")
    @Override
    public CommonResult addOrUpdateScene(@RequestBody SceneUnitReqVo record) {
        return unitSceneService.addOrUpdateScene(record);
    }

    @DeleteMapping("deleteScene")
    @Override
    public CommonResult deleteScene(@RequestParam("id") String id) {
        return unitSceneService.deleteScene(id);
    }

    @PostMapping("setSceneAsDefault")
    @Override
    public CommonResult setSceneAsDefault(@RequestParam("sceneId") String sceneId, @RequestParam("bsName") String bsName) {
        return unitSceneService.setSceneAsDefault(sceneId, bsName);
    }

    @GetMapping("listFullBigScreen")
    @Override
    public CommonResult listFullBigScreen(@RequestParam(value = "id", required = false) String id,
                                          @RequestParam(value = "bsName", required = false) String bsName,
                                          @RequestParam(value = "sceneName", required = false) String sceneName,
                                          @RequestParam(value = "mode", required = false) Integer mode) {
        return CommonResult.success(unitSceneService.listFullBigScreen(id, bsName, sceneName, mode));
    }

    @GetMapping("doCacheRightNow")
    @Override
    public CommonResult doCacheRightNow() {
        NotifyCenter.publishEvent(new UnitCacheEvent());
        return CommonResult.success();
    }
}
