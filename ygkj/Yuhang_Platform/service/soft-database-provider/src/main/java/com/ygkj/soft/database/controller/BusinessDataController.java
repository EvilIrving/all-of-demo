package com.ygkj.soft.database.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.BusinessDataApi;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.soft.database.service.BusApiService;
import com.ygkj.soft.database.service.BusinessDataService;
import com.ygkj.soft.database.service.SchemaService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/8 18:07
 * @Description: 业务数据服务控制层具体实现
 */
@RestController
@RequestMapping("/businessData")
public class BusinessDataController implements BusinessDataApi {

    @Autowired
    private BusinessDataService businessDataService;

    @Autowired
    private BusApiService busApiService;

    @Autowired
    private SchemaService schemaService;

    @PostMapping("/queryBusinessDataByPage/{dataBaseName}/{tableName}")
    public CommonResult queryBusinessData(@PathVariable String dataBaseName,@PathVariable String tableName){
        return queryBusinessDataByPage(new BusinessDataQueryVO(dataBaseName,tableName));
    }

    @PostMapping("/queryBusinessDataByPage")
    @Override
    public CommonResult queryBusinessDataByPage(@RequestBody @Valid BusinessDataQueryVO queryVO) {
        EasyuiPager easyuiPager = new EasyuiPager();
        BeanUtils.copyProperties(queryVO,easyuiPager);
        Pager pager = businessDataService.queryBusinessDataByPage(queryVO, easyuiPager);
        BusDataTable table = new BusDataTable();
        table.setTableName(queryVO.getTableName());
        table = schemaService.getBusDataTable(table).get(0);
        List<BusDataColumn> column = schemaService.getBusDataColumn(new BusDataColumn(table.getId()));
        JSONObject result = new JSONObject();
        result.put("page",pager);
        result.put("column",column);
        return CommonResult.success(result);
    }

    @PostMapping("/insertSoftData")
    @Override
    public CommonResult insertSoftData(@RequestBody @Valid SoftDataAddVO softDataAddVO) {
        try {
            businessDataService.insertSoftData(softDataAddVO);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/querySoftData")
    @Override
    public CommonResult querySoftData(@RequestBody @Valid SoftDataQueryVO softDataQueryVO) {
        try {
            EasyuiPager easyuiPager = new EasyuiPager();
            BeanUtils.copyProperties(softDataQueryVO,easyuiPager);
            return CommonResult.success(businessDataService.querySoftData(softDataQueryVO,easyuiPager));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("操作失败！请联系管理员！");
    }

    @PostMapping("/updateSoftDataStatus")
    @Override
    public CommonResult updateSoftDataStatus(@RequestBody @Valid SoftDataUpdateVO softDataUpdateVO) {
        try {
            businessDataService.updateSoftDataStatus(softDataUpdateVO);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/generateApi")
    @Override
    public CommonResult generateApi(@PathVariable("dataSourceName") String dataSourceName, @PathVariable("tableName")String tableName){
        return busApiService.generateApi(dataSourceName,tableName,"");
    }
}
