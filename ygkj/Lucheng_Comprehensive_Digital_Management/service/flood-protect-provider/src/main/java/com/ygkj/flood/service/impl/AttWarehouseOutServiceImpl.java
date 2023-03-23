package com.ygkj.flood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.flood.mapper.AttWarehouseOutMapper;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.service.AttWarehouseOutService;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:25
 * @description
 */
@Service
public class AttWarehouseOutServiceImpl extends ServiceImpl<AttWarehouseOutMapper, AttWarehouseOut> implements AttWarehouseOutService {


    @Override
    public List<WarehouseMaterialResVo> staticsOutList(String warehouseId) {

        return baseMapper.staticsOutList(warehouseId);
    }
}
