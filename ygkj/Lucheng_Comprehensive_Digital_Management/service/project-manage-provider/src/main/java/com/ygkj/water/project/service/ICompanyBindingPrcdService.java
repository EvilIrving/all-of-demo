package com.ygkj.water.project.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.CompanyBindingPrcd;

import java.util.List;

/**
 * <p>
 * 物业化公司绑定工程表 服务类
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
public interface ICompanyBindingPrcdService extends IService<CompanyBindingPrcd> {

    List<JSONObject> getAllProject(String name, String type);
}
