package com.ygkj.flood.service.impl;

import com.ygkj.flood.mapper.AttMaterialBaseMapper;
import com.ygkj.flood.service.AttMaterialService;
import com.ygkj.flood.vo.request.AttMaterialBaseAddVo;
import com.ygkj.flood.vo.request.AttMaterialQueryVo;
import com.ygkj.flood.vo.response.AttMaterialResVo;
import com.ygkj.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("attMaterialService")
public class AttMaterialServiceImpl implements AttMaterialService {

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private AttMaterialBaseMapper attMaterialBaseMapper;

    @Override
    public List<AttMaterialResVo> selectByCondition(AttMaterialQueryVo queryVo) {
        return attMaterialBaseMapper.selectByCondition(queryVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addMaterials(List<AttMaterialBaseAddVo> list) {
        if (!list.isEmpty()) {
            AttMaterialBaseAddVo attMaterialBaseAddVo = list.get(0);
            String typeId = attMaterialBaseAddVo.getTypeId();
            String warehouseId = attMaterialBaseAddVo.getWarehouseId();
            attMaterialBaseMapper.spacialDelete(warehouseId, typeId);
            for (AttMaterialBaseAddVo materialBaseAddVo : list) {
                materialBaseAddVo.setId(String.valueOf(snowFlake.nextId()));
            }
            if (attMaterialBaseMapper.batchInsert(list) > 0) {
                return true;
            }
        }
        return false;
    }
}
