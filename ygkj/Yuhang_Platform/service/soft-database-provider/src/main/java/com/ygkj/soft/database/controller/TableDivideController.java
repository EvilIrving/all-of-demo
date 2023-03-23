package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.api.TableDivideApi;
import com.ygkj.soft.database.api.VO.TableDivideVO;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.soft.database.service.BusinessDataService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Huang.zh
 * @date 2020/9/15 14:15
 * @Description: 库表划分服务控制层
 */
@RestController
@RequestMapping("/tableDivide")
public class TableDivideController implements TableDivideApi {
    @Autowired
    private BusinessDataService businessDataService;

    @PostMapping("/busDataResource")
    @Override
    public CommonResult addResource(@RequestBody BusDataResource busDataResource){
        try {
            businessDataService.addResource(busDataResource);
            return CommonResult.success("");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/divideTableResource")
    @Override
    public CommonResult divideTableResource(@RequestBody @Valid TableDivideVO tableDivideVO) {
        try {
            businessDataService.divideTableResource(tableDivideVO);
            return CommonResult.success("");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @GetMapping("/busDataResource")
    @Override
    public CommonResult busDataResource(@RequestBody BusDataResource busDataResource) {
        return CommonResult.success(businessDataService.findDataResource(busDataResource));
    }

    @DeleteMapping("/busDataResource")
    @Override
    public CommonResult deleteBusDataResource(@RequestParam String resourceId) {
        try {
            businessDataService.deleteBusDataResource(resourceId);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PutMapping("/busDataResource")
    @Override
    public CommonResult updateBusDataResource(@RequestBody BusDataResource busDataResource) {
        try {
            businessDataService.updateBusDataResource(busDataResource);
            return CommonResult.success("");
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }
}
