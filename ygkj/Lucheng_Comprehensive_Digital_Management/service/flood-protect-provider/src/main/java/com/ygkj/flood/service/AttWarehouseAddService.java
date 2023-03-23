package com.ygkj.flood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:18
 * @description
 */
public interface AttWarehouseAddService extends IService<AttWarehouseAdd> {

    List<WarehouseMaterialResVo> staticsAddList(String warehouseId);

    Integer setUnivalent(BigDecimal univalent, String materialName);
}
