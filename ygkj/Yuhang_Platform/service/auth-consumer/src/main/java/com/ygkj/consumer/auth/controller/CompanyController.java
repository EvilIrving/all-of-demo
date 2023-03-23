package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.CompanyAddReqVO;
import com.ygkj.auth.VO.request.CompanyUpdateReqVO;
import com.ygkj.auth.VO.request.UserPageUserByDeptReqVO;
import com.ygkj.auth.VO.request.UserUpdateReqVO;
import com.ygkj.auth.api.company.CompanyControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fml
 * @Date: 2020/8/28 10:44
 * @Description:
 */
@RestController
@RequestMapping("/company")
public class CompanyController implements CompanyControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @GetMapping("/list")
    @Override
    public Object getCompanyList() {
        return authenticationClient.getCompanyList();
    }

    @PutMapping("/changeStatus")
    @Override
    public Object changeStatus(CompanyUpdateReqVO vo) {
        return authenticationClient.changeStatus(vo);
    }

    @PostMapping("/getCompanyUsers")
    @Override
    public Object pageCompanyUsers(UserPageUserByDeptReqVO vo) {
        return authenticationClient.pageCompanyUsers(vo);
    }

    @GetMapping("/check/{socialCreditCode}")
    @Override
    public Object checkCompanyRegister(@PathVariable("socialCreditCode") String socialCreditCode) {
        return authenticationClient.checkCompanyRegister(socialCreditCode);
    }

    @PostMapping("/add")
    @Override
    public Object addCompany(CompanyAddReqVO vo) {
        return authenticationClient.addCompany(vo);
    }

    @GetMapping("/getCompanyInfo")
    @Override
    public Object getCompanyInfo() {
        return authenticationClient.getCompanyInfo();
    }

    @PutMapping("/update")
    @Override
    public Object updateCompanyInfo(CompanyUpdateReqVO vo) {
        return authenticationClient.updateCompanyInfo(vo);
    }

    @GetMapping("/getCompanyUser")
    @Override
    public Object getCompanyUser(@RequestParam("id") String id) {
        return authenticationClient.getCompanyUser(id);
    }

    @PutMapping("/updateCompanyUser")
    @Override
    public Object updateCompanyUser(UserUpdateReqVO vo) {
        return authenticationClient.updateCompanyUser(vo);
    }

    @PostMapping("/updateCompanyUserStatus")
    @Override
    public Object updateCompanyUserStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        return authenticationClient.updateCompanyUserStatus(id, status);
    }


}
