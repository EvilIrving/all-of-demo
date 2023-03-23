package com.ygkj.flood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:18
 * @description
 */
public interface AttWarehouseOutService extends IService<AttWarehouseOut> {


    List<WarehouseMaterialResVo> staticsOutList(String warehouseId);
}
