package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.BiologicalControl;
import com.ygkj.water.project.mapper.BiologicalControlMapper;
import com.ygkj.water.project.service.BiologicalControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wgf
 * @date 2022/4/24 10:41
 * @Description:
 */
@Service
public class BiologicalControlServiceImpl extends ServiceImpl<BiologicalControlMapper, BiologicalControl> implements BiologicalControlService {
    @Autowired
    private BiologicalControlMapper biologicalControlMapper;

    @Override
    public String getByPrcdName(String prcd) {
        return biologicalControlMapper.getByPrcdName(prcd);
    }
}
