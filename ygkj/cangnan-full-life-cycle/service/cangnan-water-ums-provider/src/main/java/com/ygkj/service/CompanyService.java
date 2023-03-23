package com.ygkj.service;

import com.ygkj.entity.SysCompany;
import com.ygkj.entity.SysUser;
import com.ygkj.vo.req.CompanyAddReqVO;
import com.ygkj.vo.req.CompanyUpdateReqVO;
import com.ygkj.vo.req.UserPageUserByDeptReqVO;
import com.ygkj.vo.req.UserUpdateReqVO;
import com.ygkj.vo.resp.PageVO;

import java.util.List;

/**
 * @author: huangzh
 * @Date: 2020/8/24 17:03
 * @Description:
 */
public interface CompanyService {

    List<SysCompany> selectAll(String userId);

    void changeStatus(CompanyUpdateReqVO vo);

    PageVO<SysUser> pageCompanyUserInfos(UserPageUserByDeptReqVO vo);

    /**
     * @description: 新增企业
     * @return: com.ygkj.entity.SysCompany
     * @Date: 2020/8/25 15:25
     * @params:[vo]
     */
    SysCompany addCompany(CompanyAddReqVO vo);

    SysCompany checkCompanyRegister(String socialCreditCode);

    SysCompany queryCompanyByUser(String userId);

    void updateCompanyInfo(CompanyUpdateReqVO vo);

    SysUser getCompanyUser(String id);

    void updateCompanyUser(UserUpdateReqVO vo, String userId);

    void updateCompanyUserStatus(String id, Integer status);
}
