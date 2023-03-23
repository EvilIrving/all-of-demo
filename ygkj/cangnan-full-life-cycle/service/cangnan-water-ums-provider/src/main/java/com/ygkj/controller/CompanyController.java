package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysCompany;
import com.ygkj.entity.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.CompanyService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.req.CompanyAddReqVO;
import com.ygkj.vo.req.CompanyUpdateReqVO;
import com.ygkj.vo.req.UserPageUserByDeptReqVO;
import com.ygkj.vo.req.UserUpdateReqVO;
import com.ygkj.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author: huangzh
 * @Date: 2020/8/24 16:36
 * @Description:
 */

@RestController
@RequestMapping("/sys")
@Api(tags = "组织模块-企业管理")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companys/getCompanyAll")
    @ApiOperation(value = "获取企业列表接口")
    @LogAnnotation(title = "企业管理", action = "获取所有企业信息")
    ////@RequiresPermissions("sys:company:list")
    public DataResult<List<SysCompany>> getCompanyAll(HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getUserId(accessToken);
        DataResult<List<SysCompany>> result = DataResult.success();
        result.setData(companyService.selectAll(userId));

        return result;
    }

    @PutMapping("/company/changeStatus")
    @ApiOperation(value = "更新企业审批状态")
    @LogAnnotation(title = "企业管理", action = "更新企业审批状态")
    ////@RequiresPermissions("sys:company:update")
    public DataResult changeStatus(@RequestBody @Valid CompanyUpdateReqVO vo) {
        companyService.changeStatus(vo);
        return DataResult.success();
    }

    @PostMapping("/company/users")
    @ApiOperation(value = "分页获取企业下所有用户接口")
    @LogAnnotation(title = "企业管理", action = "分页获取企业下所有用户")
    ////@RequiresPermissions("sys:company:user:list")
    public DataResult<PageVO<SysUser>> pageCompanyUserInfos(@RequestBody @Valid UserPageUserByDeptReqVO vo) {
        DataResult<PageVO<SysUser>> result = DataResult.success();
        result.setData(companyService.pageCompanyUserInfos(vo));
        return result;
    }

    @GetMapping("/company/checkComp/{socialCreditCode}")
    @ApiOperation(value = "企业注册校验")
    @LogAnnotation(title = "企业管理", action = "企业注册校验")
    public DataResult<SysCompany> checkCompanyRegister(@PathVariable("socialCreditCode") String socialCreditCode) {
        SysCompany sysCompany = companyService.checkCompanyRegister(socialCreditCode);
        return DataResult.success(sysCompany);
    }

    @PostMapping("/company/add")
    @ApiOperation(value = "新增企业接口")
    @LogAnnotation(title = "企业管理", action = "新增企业")
    ////@RequiresPermissions("sys:company:add")
    public DataResult<SysCompany> addCompany(@RequestBody @Valid CompanyAddReqVO vo) {
        DataResult<SysCompany> result = DataResult.success();
        result.setData(companyService.addCompany(vo));
        return result;
    }

    @GetMapping("/company/getCompany")
    @ApiOperation(value = "根据当前用户获取所在企业信息")
    @LogAnnotation(title = "企业管理", action = "获取所在企业信息")
    public DataResult<SysCompany> getCompanyInfo(HttpServletRequest request) {
        // 从token中获取用户id，企业用户登录成功后用户id
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId = JwtTokenUtil.getUserId(accessToken);

        return DataResult.success(companyService.queryCompanyByUser(userId));
    }

    @PutMapping("/company/update")
    @ApiOperation(value = "编辑修改企业信息")
    @LogAnnotation(title = "企业管理", action = "编辑修改企业信息")
    public DataResult updateCompanyInfo(@RequestBody @Valid CompanyUpdateReqVO vo) {
        companyService.updateCompanyInfo(vo);
        return DataResult.success();
    }

    // 根据企业用户id获取用户信息
    @GetMapping("/company/getCompanyUser")
    @ApiOperation(value = "根据企业用户id获取用户信息")
    @LogAnnotation(title = "企业管理", action = "根据企业用户id获取用户信息")
    public DataResult getCompanyUser(@RequestParam("id") String id) {

        return DataResult.success(companyService.getCompanyUser(id));
    }

    // 修改企业用户信息
    @PutMapping("/company/updateCompanyUser")
    @ApiOperation(value = "编辑修改企业用户")
    @LogAnnotation(title = "企业管理", action = "编辑修改企业用户")
    public DataResult updateCompanyUser(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        companyService.updateCompanyUser(vo, userId);
        return DataResult.success();
    }

    // 修改企业用户停启用状态
    @PostMapping("/company/updateCompanyUserStatus")
    @ApiOperation(value = "修改企业用户停启用状态")
    @LogAnnotation(title = "企业管理", action = "修改企业用户停启用状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "(1启用，2停用)", paramType = "query")
    })
    public DataResult updateCompanyUserStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        companyService.updateCompanyUserStatus(id, status);
        return DataResult.success();
    }

}
