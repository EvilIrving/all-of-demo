package com.ygkj.water.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.ProfessionalCompany;
import com.ygkj.project.vo.request.WyhUserVo;

import java.util.List;

/**
 * <p>
 * 物业化公司 服务类
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
public interface IProfessionalCompanyService extends IService<ProfessionalCompany> {

    List<WyhUserVo> getWyhUserByDeptId(WyhUserVo wyhUserVo);

    List<ProfessionalCompany> getWaterManageDeptList(String key);
}
