package com.ygkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.aop.annotation.ArchiveLogAnnotation;
import com.ygkj.auth.VO.request.SysArchiveLogReqVo;
import com.ygkj.auth.api.archivelog.model.SysArchiveLog;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysArchiveLogService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.DateUtil;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 档案操作日志表
 * @date 2021-01-19
 */
@RestController
@RequestMapping(value = "/ums/SysArchiveLog")
@Api(tags = "档案操作日志模块")
public class SysArchiveLogController {

    @Resource
    private SysArchiveLogService sysArchiveLogService;

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增接口")
    public CommonResult insert(SysArchiveLog entity) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("authorization");
            if (StringUtils.isNotEmpty(token)) {
                String userId = JwtTokenUtil.getUserId(token);
                String userName = JwtTokenUtil.getUserName(token);
                entity.setCreateName(userName);
                entity.setCreatorId(userId);
            }
            entity.setCreateTime(new Date());
            int i = sysArchiveLogService.insert(entity);
            if (i > 0) {
                return CommonResult.success("新增成功");
            } else {
                return CommonResult.failed("新增失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除接口")
    public CommonResult delete(int id) {
        try {
            int i = sysArchiveLogService.delete(id);
            if (i > 0) {
                return CommonResult.success("刪除成功");
            } else {
                return CommonResult.failed("刪除失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新接口")
    public CommonResult update(SysArchiveLog sysArchiveLog) {
        try {
            int i = sysArchiveLogService.update(sysArchiveLog);
            if (i > 0) {
                return CommonResult.success("更新成功");
            } else {
                return CommonResult.failed("更新失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查询 根据主键 id 查询
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/findById")
    @ResponseBody
    @ApiOperation(value = "根据主键id查找数据接口")
    public CommonResult findById(int id) {
        try {
            SysArchiveLog sysArchiveLog = sysArchiveLogService.load(id);
            return CommonResult.success(sysArchiveLog);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查询 分页查询
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/pageList")
    @ApiOperation(value = "分页查询接口")
    public CommonResult pageList(SysArchiveLogReqVo vo) {
        return CommonResult.success(sysArchiveLogService.pageList(vo));
    }

    @PostMapping("/archiveLogTimeParameter")
    @ApiOperation(value = "日志时间参数查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "时间 格式:yyyy-MM(默认本月)", paramType = "query")
    })
    public CommonResult archiveLogTimeParameter(String time) {
        try {
            if (StringUtils.isEmpty(time)) {
                time = DateUtil.format(new Date(),"yyyy-MM");
            }
            List<String> data = sysArchiveLogService.archiveLogTimeParameter(time);
            return CommonResult.success(data);
        } catch (Exception e) {
            throw new RuntimeException("系统出现异常,请联系管理员");
        }
    }


    @PostMapping("/trafficFileRank")
    @ApiOperation(value = "访问量排行")
    public Object trafficFileRank() {
        try {
            List<JSONObject> objectList = sysArchiveLogService.trafficFileRank();
        	return CommonResult.success(objectList);
        } catch (Exception e) {
        	throw new RuntimeException("系统出现异常,请联系管理员");
        }
    }

    @PostMapping("/trafficFileGraph")
    @ApiOperation(value = "数字档案访问量曲线图接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time", value = "时间 格式:yyyy-MM(默认本月)", paramType = "query")
    })
    public Object trafficFileGraph(String time) {
        try {
            if (StringUtils.isEmpty(time)) {
                time = DateUtil.format(new Date(),"yyyy-MM");
            }
            List<JSONObject> objectList = sysArchiveLogService.trafficFileGraph(time);
            return CommonResult.success(objectList);
        } catch (Exception e) {
            throw new RuntimeException("系统出现异常,请联系管理员");
        }
    }


}