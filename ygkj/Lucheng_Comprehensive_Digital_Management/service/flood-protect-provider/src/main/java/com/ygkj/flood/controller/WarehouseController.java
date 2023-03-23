package com.ygkj.flood.controller;

import com.ygkj.flood.WarehouseControllerApi;
import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.service.AttWarehouseService;
import com.ygkj.flood.vo.request.AttWarehouseAddVo;
import com.ygkj.flood.vo.request.AttWarehouseDeviceBaseReqVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/warehouse")
public class WarehouseController implements WarehouseControllerApi {

    @Autowired
    private AttWarehouseService warehouseService;

    @PostMapping("/warehouses")
    @Override
    public CommonResult warehouses(AttWarehousePageQueryVo queryVo) {
        return CommonResult.success(warehouseService.attWarehouses(queryVo));
    }

    @PostMapping("/stats4PageWareHouses")
    @Override
    public CommonResult stats4PagedWareHouses(AttWarehousePageQueryVo queryVo) {
        return CommonResult.success(warehouseService.stats4PagedWareHouses(queryVo));
    }

    @GetMapping("/exportWareHouses")
    @Override
    public void exportWareHouses(AttWarehousePageQueryVo reqVo, HttpServletResponse response) {
        warehouseService.exportWareHouses(reqVo,response);
    }

    @PutMapping("/insertWarehouse")
    @Override
    public CommonResult insertWarehouse(AttWarehouseAddVo attWarehouseAddVo) {
        if (warehouseService.insert(attWarehouseAddVo)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed();
    }
    @PutMapping("/updateWarehouse")
    @Override
    public CommonResult updateWarehouse(AttWarehouseAddVo attWarehouseAddVo) {
        if (warehouseService.update(attWarehouseAddVo)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed();
    }

    @PostMapping("/delete")
    @Override
    public CommonResult delete(String ids) {
        if (warehouseService.delete(ids)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed();
    }

    @GetMapping("/warehousesGroupByArea")
    @Override
    public CommonResult warehousesGroupByArea() {
        return CommonResult.success(warehouseService.warehousesGroupByArea());
    }

    @GetMapping("warehousesGroupByType")
    @Override
    public CommonResult warehousesGroupByType() {
        return CommonResult.success(warehouseService.warehousesGroupByType());
    }

    @GetMapping("staticAcreageByArea")
    @Override
    public CommonResult staticAcreageByArea() {
        return CommonResult.success(warehouseService.staticAcreageByArea());
    }

    @GetMapping("staticAcreageByType")
    @Override
    public CommonResult staticAcreageByType() {
        return CommonResult.success(warehouseService.staticAcreageByType());
    }

    @GetMapping("staticMaterialPriceByArea")
    @Override
    public CommonResult staticMaterialPriceByArea() {
        return CommonResult.success(warehouseService.staticMaterialPriceByArea());
    }

    @GetMapping("staticMaterialPriceByType")
    @Override
    public CommonResult staticMaterialPriceByType() {
        return CommonResult.success(warehouseService.staticMaterialPriceByType());
    }

    @PostMapping("wareHouseSendSms")
    @Override
    public CommonResult wareHouseSendSms(String phone, String userName,String content) {
        warehouseService.wareHouseSendSms(phone,userName,content);
        return CommonResult.success();
    }

    @Override
    @GetMapping("/warehousesCount")
    public CommonResult warehousesCount() {
        return CommonResult.success(warehouseService.warehousesCount());
    }

    @PostMapping("/selectDevice")
    @Override
    public CommonResult selectDevice(AttWarehouseDeviceBaseReqVo vo) {
        return CommonResult.success(warehouseService.selectDevice(vo));
    }

    @PostMapping("/insertOrUpdateDevice")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult insertOrUpdateDevice(AttWarehouseDeviceBase base,String warehouseId) {
        int count=warehouseService.insertOrUpdateDevice(base,warehouseId);
        return count>0?CommonResult.success("新增更新成功"):CommonResult.failed("新增更新失败");
    }


    @PostMapping("/deleteDevice")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult deleteDevice(AttWarehouseDeviceBaseReqVo vo) {
        int count=warehouseService.deleteDevice(vo);
        return count>0?CommonResult.success("删除成功"):CommonResult.failed("删除失败");
    }

    @PostMapping("/exportDevice")
    @Override
    public void exportDevice(AttWarehouseDeviceBaseReqVo vo, HttpServletResponse response) {
        warehouseService.exportDevice(vo,response);
    }
}
