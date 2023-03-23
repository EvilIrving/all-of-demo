package com.ygkj.controller;

import com.ygkj.entity.DingDingDept;
import com.ygkj.service.DingDingDeptService;
import com.ygkj.vo.req.DingDingDeptVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping()
@Api(tags = "水管理平台单位和系统单位绑定")
public class DingDingDeptController {

    @Resource
    private DingDingDeptService dingDingDeptService;

    @PutMapping("bind")
    @ApiOperation(value = "更新绑定关系")
    public CommonResult bind(@RequestBody DingDingDept dingDingDept) {
        dingDingDeptService.bind(dingDingDept);
        return CommonResult.success("");
    }

    @PutMapping("batchBind")
    @ApiOperation(value = "批量更新绑定关系")
    public CommonResult batchBind(@RequestBody List<DingDingDept> list) {
        dingDingDeptService.batchBind(list);
        return CommonResult.success("");
    }

    @PostMapping("page")
    @ApiOperation(value = "分页获取绑定关系")
    public CommonResult page(@RequestBody DingDingDeptVo vo) {
        return CommonResult.success(dingDingDeptService.page(vo));
    }
}
