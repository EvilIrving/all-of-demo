package com.ygkj.project.service;

import com.ygkj.project.api.vo.res.PolderRangeResVo;
import com.ygkj.project.mapper.PolderMapper;
import com.ygkj.water.utils.PageUtils;
import com.ygkj.water.utils.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolderService {

    @Autowired
    private PolderMapper polderMapper;

    public PageVO<PolderRangeResVo> listRange(String adcd) {
        return PageUtils.getPageVO(polderMapper.listRange(adcd));
    }
}
