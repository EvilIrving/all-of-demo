package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.HardwareMaintenance;
import com.ygkj.water.project.mapper.HardwareMaintenanceMapper;
import com.ygkj.water.project.service.HardwareMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wgf
 * @date 2022/4/24 10:41
 * @Description:
 */
@Service
public class HardwareMaintenanceServiceImpl extends ServiceImpl<HardwareMaintenanceMapper, HardwareMaintenance> implements HardwareMaintenanceService {
    @Autowired
    private HardwareMaintenanceMapper hardwareMaintenanceMapper;

    @Override
    public String getByPrcdName(String prcd) {
        return hardwareMaintenanceMapper.getByPrcdName(prcd);
    }
}
