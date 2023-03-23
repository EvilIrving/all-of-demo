package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.entity.SysLog;
import com.ygkj.service.LogService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.req.SysLogPageReqVO;
import com.ygkj.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: SysLogController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/23 16:15
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/23 16:15
 * @Version: 0.0.1
 */
@RequestMapping("/sys")
@Api(tags = "系统模块-系统操作日志管理")
@RestController
public class SysLogController {

    @Autowired
    private LogService logService;

    @PostMapping("/logs")
    @ApiOperation(value = "分页查询系统操作日志接口")
    @LogAnnotation(title = "系统操作日志管理",action = "分页查询系统操作日志")
    //@RequiresPermissions("sys:log:list")
    public DataResult<PageVO<SysLog>> pageInfo(@RequestBody SysLogPageReqVO vo){
        PageVO<SysLog> sysLogPageVO = logService.pageInfo(vo);
        DataResult<PageVO<SysLog>> result=DataResult.success();
        result.setData(sysLogPageVO);
        return result;
    }

    @DeleteMapping("/logs")
    @ApiOperation(value = "删除日志接口")
    @LogAnnotation(title = "系统操作日志管理",action = "删除系统操作日志")
    //@RequiresPermissions("sys:log:deleted")
    public DataResult deleted(@RequestBody List<String> logIds){
        logService.deleted(logIds);
        return DataResult.success();
    }
}
