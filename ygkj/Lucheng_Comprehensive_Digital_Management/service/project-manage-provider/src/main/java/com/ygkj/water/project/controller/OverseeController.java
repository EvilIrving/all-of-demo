package com.ygkj.water.project.controller;


import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.vo.request.OverseeCodeReqVo;
import com.ygkj.project.vo.request.OverseeReqVo;
import com.ygkj.project.vo.request.ProjectOverseeReqVo;
import com.ygkj.project.vo.response.ProjectOverseeResVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.OverseeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("oversee")
@Api(tags = "督办中心")
public class OverseeController {

    @Resource
    private OverseeService overseeService;

    @ApiOperation("新增")
    @PostMapping("save")
    public CommonResult save(@RequestBody OverseeReqVo reqVo, HttpServletRequest request) {
        if (StringUtils.isEmpty(reqVo.getCreateId())) {
            String token = request.getHeader("authorization");
            String userId = JwtTokenUtil.getUserId(token);
            reqVo.setCreateId(userId);
        }
        overseeService.save(reqVo);
        return CommonResult.success();
    }

    @ApiOperation("整改")
    @PostMapping("update")
    public CommonResult update(ProjectOversee oversee, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(oversee.getReportPhone())) {
            String token = request.getHeader("authorization");
            String userId = JwtTokenUtil.getUserId(token);
            oversee.setCreateId(userId);
        }
        overseeService.update(oversee);
        return CommonResult.success();
    }

    @ApiOperation("删除督办")
    @DeleteMapping("del/{id}")
    public CommonResult del(@PathVariable("id") String id) {
        overseeService.del(id);
        return CommonResult.success();
    }

    @ApiOperation(value = "督办查询", response = ProjectOverseeResVo.class)
    @PostMapping("page")
    public CommonResult page(ProjectOverseeReqVo reqVo, HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (!StringUtils.isEmpty(token)) {
            String userId = JwtTokenUtil.getUserId(token);
            reqVo.setUserId(userId);
        }
        return CommonResult.success(overseeService.page(reqVo));
    }

    @ApiOperation(value = "发送验证码")
    @PostMapping("sendVerificationCode")
    public CommonResult sendVerificationCode(OverseeCodeReqVo reqVo) throws Exception {
        overseeService.sendVerificationCode(reqVo);
        return CommonResult.success();
    }

    @ApiOperation(value = "督办登陆")
    @GetMapping("loginByOverseeWorkId/{workId}")
    public CommonResult loginByOverseeWorkId(@PathVariable("workId") String workId) throws Exception {
        return CommonResult.success(overseeService.loginByOverseeWorkId(workId),"登陆成功");
    }
}
