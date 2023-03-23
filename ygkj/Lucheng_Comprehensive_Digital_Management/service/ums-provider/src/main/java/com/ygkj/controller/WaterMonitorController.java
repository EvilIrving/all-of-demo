package com.ygkj.controller;

import com.ygkj.auth.VO.request.*;
import com.ygkj.client.WmsClient;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author: fml
 * @Date: 2020/9/8 17:32
 * @Description:
 */
//@Api(tags = "水域监测")
//@RestController
//@RequestMapping("/waterbase")
public class WaterMonitorController {

    @Autowired
    private WmsClient wmsClient;

    @ApiOperation(value = "新增水域基本信息")
    @PostMapping("/insert")
    public Object insert(@RequestBody @Valid WaterBaseAddReqVo vo) {
        return wmsClient.insert(vo);
    }

    @ApiOperation(value = "删除水域基本信息")
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") String id) {
        return wmsClient.delete(id);
    }

    @ApiOperation(value = "修改水域基本信息")
    @PutMapping("/update")
    public Object update(@RequestBody @Valid WaterBaseUpdateReqVo vo) {
        return wmsClient.update(vo);
    }

    @ApiOperation(value = "获取水域")
    @GetMapping("/load/{id}")
    public Object load(@PathVariable("id") String id) {
        return wmsClient.load(id);
    }

    @ApiOperation(value = "分页 根据条件筛选")
    @PostMapping("/pageListByInfo")
    public Object pageListByInfo(@RequestBody @Valid WaterBaseByInfoPageReqVo vo) {
        return wmsClient.pageListByInfo(vo);
    }

    @ApiOperation(value = "获取行政区划列表")
    @GetMapping("/queryAreaNames")
    public Object queryAreaNames() {
        return wmsClient.queryAreaNames();
    }

    @ApiOperation(value = "上传解析经纬度excel")
    @PostMapping(value = "/parseExcel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object parseExcel(MultipartFile file) {

        return wmsClient.parseExcel(file);
    }

    @ApiOperation(value = "获取附近5KM的水域基本情况")
    @PostMapping("/queryNearbyWaterBaseInfo")
    @ApiImplicitParams({@ApiImplicitParam(name = "lng", value = "经度"),
            @ApiImplicitParam(name = "lat", value = "纬度")})
    public Object queryNearbyWaterBaseInfo(@RequestParam("lng") Double lng, @RequestParam("lat") Double lat) {

        return wmsClient.queryNearbyWaterBaseInfo(lng, lat);
    }

    // --------------------------------------------- 水域变化-----------------------------------------------

    //根据行政区划和水域类型查询水域变化信息
    @ApiOperation(value = "分页查询水域变化信息(现状)接口")
    @PostMapping("/queryWaterChangeInfo")
    public Object queryWaterChangeInfo(
            @RequestBody @Valid WaterChangePageReqVo vo) {

        return wmsClient.queryWaterChangeInfo(vo);
    }

    //根据id获取水域变化详情记录
    @ApiOperation(value = "根据水域变化id获取水域变化详情记录")
    @GetMapping("/getChangeDetails/{id}")
    public Object getChangeDetails(@PathVariable("id") String id) {

        return wmsClient.getChangeDetails(id);
    }

    // 新增水域详情记录
    @ApiOperation(value = "新增水域详情记录")
    @PostMapping("/addChangeDetail")
    public Object addChangeDetail(@RequestBody @Valid WaterChangeDetailAddReqVo vo) {

        return wmsClient.addChangeDetail(vo);
    }

    // 根据id查询水域详情记录
    @ApiOperation(value = "根据id查询水域详情记录")
    @GetMapping("/queryChangeDetail/{id}")
    public Object queryChangeDetail(@PathVariable("id") String id) {

        return wmsClient.queryChangeDetail(id);
    }

    // 更新水域详情记录
    @ApiOperation(value = "更新水域详情记录")
    @PutMapping("/updateChangeDetail")
    public Object updateWaterChangeInfo(@RequestBody @Valid ChangeDetailUpdateReqVo vo) {

        return wmsClient.updateWaterChangeInfo(vo);
    }

    //删除水域详情记录
    @ApiOperation(value = "删除水域详情记录")
    @DeleteMapping("/deleteChangeDetail/{id}")
    public Object deleteChangeDetail(@PathVariable("id") String id) {

        return wmsClient.deleteChangeDetail(id);
    }

    @ApiOperation(value = "根据水域id查询水域详情记录")
    @GetMapping("/queryWaterDetail/{id}")
    public Object queryWaterDetail(@PathVariable("id") String id) {

        return wmsClient.queryWaterDetail(id);
    }

    @ApiOperation(value = "根据年度、所需字段获取当前水域变化详情相关数据")
    @PostMapping("/queryYearWaterDetailInfo")
    public Object queryYearWaterDetailInfo(@RequestBody @Valid WaterChangeYearReqVo vo) {

        return wmsClient.queryYearWaterDetailInfo(vo);
    }
}
