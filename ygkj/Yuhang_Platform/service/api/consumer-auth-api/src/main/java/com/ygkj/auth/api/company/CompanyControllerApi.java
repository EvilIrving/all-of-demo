package com.ygkj.auth.api.company;

import com.ygkj.auth.VO.request.CompanyAddReqVO;
import com.ygkj.auth.VO.request.CompanyUpdateReqVO;
import com.ygkj.auth.VO.request.UserPageUserByDeptReqVO;
import com.ygkj.auth.VO.request.UserUpdateReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author: fml
 * @Date: 2020/8/27 16:00
 * @Description:
 */
@Api(value = "CompanyControllerApi", tags = "企业管理服务")
public interface CompanyControllerApi {

    @ApiOperation(value = "获取企业列表")
    Object getCompanyList();

    @ApiOperation(value = "更新企业审批状态")
    Object changeStatus(CompanyUpdateReqVO vo);

    @ApiOperation(value = "分页获取企业下所有用户")
    Object pageCompanyUsers(UserPageUserByDeptReqVO vo);

    @ApiOperation(value = "企业注册校验")
    Object checkCompanyRegister(String socialCreditCode);

    @ApiOperation(value = "新增企业接口")
    Object addCompany(CompanyAddReqVO vo);

    @ApiOperation(value = "根据当前用户获取所在企业信息")
    Object getCompanyInfo();

    @ApiOperation(value = "更新企业信息")
    Object updateCompanyInfo(CompanyUpdateReqVO vo);

    @ApiOperation(value = "根据企业用户id获取用户信息")
    Object getCompanyUser(String id);

    @ApiOperation(value = "编辑修改企业用户")
    Object updateCompanyUser(UserUpdateReqVO vo);

    @ApiOperation(value = "修改企业用户停启用状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "(1启用，2停用)", paramType = "query")
    })
    Object updateCompanyUserStatus(String id, Integer status);
}
