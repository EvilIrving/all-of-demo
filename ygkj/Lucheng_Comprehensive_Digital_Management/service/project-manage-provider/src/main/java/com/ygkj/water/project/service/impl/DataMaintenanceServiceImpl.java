package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.DataMaintenance;
import com.ygkj.water.project.mapper.DataMaintenanceMapper;
import com.ygkj.water.project.service.DataMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wgf
 * @date 2022/4/24 10:41
 * @Description:
 */
@Service
public class DataMaintenanceServiceImpl extends ServiceImpl<DataMaintenanceMapper, DataMaintenance> implements DataMaintenanceService {
    @Autowired
    private DataMaintenanceMapper dataMaintenanceMapper;

    @Override
    public String getByPrcdName(String prcd) {
        return dataMaintenanceMapper.getByPrcdName(prcd);
    }
}
