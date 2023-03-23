package com.ygkj.river.controller;

import com.ygkj.river.AttSwhsBaseControllerApi;
import com.ygkj.river.service.AttSwhsBaseService;
import com.ygkj.river.vo.request.AttSwhsBaseReqVo;
import com.ygkj.river.vo.response.AttSwhsBaseRespVo;
import com.ygkj.river.vo.response.StatisSwarnRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水源地基础信息表
 * @date 2021-08-17
 */
@RestController
@RequestMapping(value = "/AttSwhsBase")
public class AttSwhsBaseController implements AttSwhsBaseControllerApi {

    @Resource
    private AttSwhsBaseService attSwhsBaseService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(AttSwhsBaseReqVo vo) {
        if (StringUtils.isEmpty(vo.getSwhsCode())) {
            attSwhsBaseService.insert(vo);
        } else {
            attSwhsBaseService.update(vo);
        }
        return CommonResult.success("操作成功");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String swhsCode) {
        int flag = attSwhsBaseService.delete(swhsCode);
        if (flag > 0) {
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttSwhsBaseRespVo>> loadPage(AttSwhsBaseReqVo vo) {
        return CommonResult.success(attSwhsBaseService.loadPage(vo));
    }

    @PostMapping("waterSuantityStatistic")
    @Override
    public CommonResult waterSuantityStatistic(AttSwhsBaseReqVo vo) {
        return CommonResult.success(attSwhsBaseService.waterSuantityStatistic(vo));
    }

    @GetMapping("statisSwhsNumByScale")
    @Override
    public CommonResult statisSwhsNumByScale(String type) {
        return CommonResult.success(attSwhsBaseService.statisSwhsNumByScale(type));
    }

    @GetMapping("statisSwhsNumByType")
    @Override
    public CommonResult statisSwhsNumByType(String type) {
        return CommonResult.success(attSwhsBaseService.statisSwhsNumByType(type));
    }

    @GetMapping("statisSwhsNumByArea")
    @Override
    public CommonResult statisSwhsNumByArea(String type) {
        return CommonResult.success(attSwhsBaseService.statisSwhsNumByArea(type));
    }

    @GetMapping("statisSwarnGradeNum")
    @Override
    public CommonResult statisSwarnGradeNum(String type) {
        return CommonResult.success(attSwhsBaseService.statisSwarnGradeNum(type));
    }

    @GetMapping("statisSwarnGradeByAdcd")
    @Override
    public CommonResult statisSwarnGradeByAdcd(String type) {
        return CommonResult.success(attSwhsBaseService.statisSwarnGradeByAdcd(type));
    }

    @GetMapping("statisC")
    @Override
    public CommonResult<StatisSwarnRespVo> statisC() {
        return CommonResult.success(attSwhsBaseService.statisC());
    }
}
