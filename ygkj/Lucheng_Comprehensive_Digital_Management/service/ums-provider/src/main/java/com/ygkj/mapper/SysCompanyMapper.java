package com.ygkj.mapper;

import com.ygkj.entity.SysCompany;
import com.ygkj.vo.req.CompanyUpdateReqVO;

import java.util.List;

/**
 * @author: fml
 * @Date: 2020/8/24 17:06
 * @Description:
 */
public interface SysCompanyMapper {

    SysCompany selectByPrimaryKey(String id);

    int insertSelective(SysCompany sysCompany);

    List<SysCompany> selectAll();

    int updateStatusById(CompanyUpdateReqVO vo);

    SysCompany checkCompany(String socialCreditCode);

    int updateCompanyInfo(CompanyUpdateReqVO vo);

    int updateByPrimaryKeySelective(CompanyUpdateReqVO vo);
}
