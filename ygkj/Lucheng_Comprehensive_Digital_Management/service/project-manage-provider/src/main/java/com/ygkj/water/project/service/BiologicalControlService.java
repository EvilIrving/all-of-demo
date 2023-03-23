package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.BiologicalControl;

/**
 * @author wgf
 * @date 2022/4/24 10:36
 * @Description:
 */
public interface BiologicalControlService extends IService<BiologicalControl> {

    String getByPrcdName(String prcd);
}
