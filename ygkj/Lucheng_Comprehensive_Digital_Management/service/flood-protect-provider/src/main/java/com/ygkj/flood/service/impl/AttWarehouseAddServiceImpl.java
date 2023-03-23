package com.ygkj.flood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.flood.mapper.AttWarehouseAddMapper;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.service.AttWarehouseAddService;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:24
 * @description
 */
@Service
public class AttWarehouseAddServiceImpl extends ServiceImpl<AttWarehouseAddMapper, AttWarehouseAdd> implements AttWarehouseAddService {


    @Override
    public List<WarehouseMaterialResVo> staticsAddList(String warehouseId) {
        return baseMapper.staticsAddList(warehouseId);
    }


    @Override
    public Integer setUnivalent(BigDecimal univalent, String materialName) {

        return baseMapper.setUnivalent(univalent,materialName);
    }
}
