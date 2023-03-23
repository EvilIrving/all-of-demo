package com.ygkj.flood.service.impl;

import com.ygkj.flood.mapper.AttDictBaseMapper;
import com.ygkj.flood.model.AttDictBase;
import com.ygkj.flood.service.AttDictService;
import com.ygkj.flood.vo.request.AttDictAddVo;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.flood.vo.response.AttDictResVo;
import com.ygkj.utils.SnowFlake;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("attDictService")
public class AttDictServiceImpl implements AttDictService {

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private AttDictBaseMapper attDictBaseMapper;

    @Override
    public List<AttDictResVo> selectDict(AttDictRequestVo attDictRequestVo) {
        return attDictBaseMapper.selectDict(attDictRequestVo);
    }

    @Override
    public boolean insert(AttDictAddVo attDictAddVo) {
        AttDictBase attDictBase = new AttDictBase();
        BeanUtils.copyProperties(attDictAddVo, attDictBase);
        attDictBase.setId(String.valueOf(snowFlake.nextId()));
        if (attDictBaseMapper.insert(attDictBase) > 0) {
            return true;
        }
        return false;
    }
}
