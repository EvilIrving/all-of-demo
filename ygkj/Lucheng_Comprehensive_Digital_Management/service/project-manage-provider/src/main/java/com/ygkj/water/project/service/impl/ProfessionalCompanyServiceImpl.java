package com.ygkj.water.project.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.ProfessionalCompany;
import com.ygkj.project.vo.request.WyhUserVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.ProfessionalCompanyMapper;
import com.ygkj.water.project.service.IProfessionalCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 物业化公司 服务实现类
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@Service
public class ProfessionalCompanyServiceImpl extends ServiceImpl<ProfessionalCompanyMapper, ProfessionalCompany> implements IProfessionalCompanyService {

    @Resource
    private ProfessionalCompanyMapper professionalCompanyMapper;
    @Override
    public List<WyhUserVo> getWyhUserByDeptId(WyhUserVo wyhUserVo) {
        return professionalCompanyMapper.getWyhUserByDeptId(wyhUserVo);
    }

    @Override
    public List<ProfessionalCompany> getWaterManageDeptList(String key) {
        if (StringUtils.isNotBlank(key)){
            key = "";
        }
        return baseMapper.getWaterManageDeptList(key);
    }
}
