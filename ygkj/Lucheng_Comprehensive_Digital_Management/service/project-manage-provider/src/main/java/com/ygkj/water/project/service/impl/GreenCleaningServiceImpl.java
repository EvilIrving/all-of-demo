package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.GreenCleaning;
import com.ygkj.water.project.mapper.GreenCleaningMapper;
import com.ygkj.water.project.service.GreenCleaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wgf
 * @date 2022/4/24 10:41
 * @Description:
 */
@Service
public class GreenCleaningServiceImpl extends ServiceImpl<GreenCleaningMapper, GreenCleaning> implements GreenCleaningService {
    @Autowired
    private GreenCleaningMapper greenCleaningMapper;

    @Override
    public String getByPrcdName(String prcd) {
        return greenCleaningMapper.getByPrcdName(prcd);
    }
}
