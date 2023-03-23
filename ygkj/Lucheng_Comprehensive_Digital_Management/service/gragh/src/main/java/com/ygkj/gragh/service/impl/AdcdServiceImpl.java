package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.service.AdcdService;
import com.ygkj.gragh.vo.request.AdcdQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdcdServiceImpl implements AdcdService {

    @Autowired
    private AttAdcdBaseMapper adcdBaseMapper;

    @Override
    public List<AttAdcdBase> adcd(AdcdQueryVo adcdQueryVo) {
        adcdQueryVo.setOrderSql(null);
        if (adcdQueryVo.getAreaCode().startsWith("3303")) {
            adcdQueryVo.setOrderSql(orderSql);
        }
        return adcdBaseMapper.selectAreaCode(adcdQueryVo);
    }

    @Override
    public List<AttAdcdBase> selectAdnmByAdcdList(List<String> adcdList) {
        if (adcdList != null && !adcdList.isEmpty()) {
            return adcdBaseMapper.selectAdnmByAdcdList(adcdList);
        } else {
            return null;
        }
    }
}
