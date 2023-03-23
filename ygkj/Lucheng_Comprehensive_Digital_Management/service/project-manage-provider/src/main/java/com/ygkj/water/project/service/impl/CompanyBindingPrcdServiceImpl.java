package com.ygkj.water.project.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.water.project.mapper.CompanyBindingPrcdMapper;
import com.ygkj.water.project.service.ICompanyBindingPrcdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物业化公司绑定工程表 服务实现类
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@Service
public class CompanyBindingPrcdServiceImpl extends ServiceImpl<CompanyBindingPrcdMapper, CompanyBindingPrcd> implements ICompanyBindingPrcdService {

    @Autowired
    private CompanyBindingPrcdMapper companyBindingPrcdMapper;

    @Override
    public List<JSONObject> getAllProject(String name, String type) {
        return companyBindingPrcdMapper.getAllProject(name,type);
    }
}
