package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.HardwareMaintenance;

/**
 * @author wgf
 * @date 2022/4/24 10:36
 * @Description:
 */
public interface HardwareMaintenanceService extends IService<HardwareMaintenance> {

    String getByPrcdName(String prcd);
}
