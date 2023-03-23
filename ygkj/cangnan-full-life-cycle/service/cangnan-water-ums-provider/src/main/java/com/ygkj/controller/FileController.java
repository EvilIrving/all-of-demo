package com.ygkj.controller;

import com.ygkj.service.SysFileService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("file")
@Api(tags = "文件")
public class FileController {

    @Resource
    private SysFileService sysFileService;

    @PostMapping("load")
    @ApiOperation("查询文件")
    private CommonResult load(@RequestParam("ids") String ids) {
        return CommonResult.success(sysFileService.load(ids));
    }
}
