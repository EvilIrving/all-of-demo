package com.ygkj.water.project.service.impl;


import com.github.pagehelper.PageHelper;
import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.AttSixchargeChangeRecord;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.water.project.enums.EngScalEnum;
import com.ygkj.water.project.mapper.AttSixchargeBaseMapper;
import com.ygkj.water.project.service.AttSixchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AttSixchargeBaseServiceImpl implements AttSixchargeService {
    @Autowired
    private GenerateDistributionIDService idService;
    @Autowired
    private AttSixchargeBaseMapper attSixchargeBaseMapper;
    @Override
    public int save(AttSixchargeBase entity) {
        String id = idService.nextID();
        entity.setId(id);
        return attSixchargeBaseMapper.insert(entity);
    }

    @Override
    public int update(AttSixchargeBase  entity) {
        return attSixchargeBaseMapper.updateById(entity);
    }

    @Override
    public PageVO<AttSixchargeBaseRespVo> queryPage(AttSixchargeBaseReqVo vo) {
        if (vo.getPageNum() > 0 && vo.getPageSize() > 0) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        PageVO<AttSixchargeBaseRespVo> pageVO = PageUtils.getPageVO(attSixchargeBaseMapper.queryPage(vo));
        List<AttSixchargeBaseRespVo> list = pageVO.getList();
        list.forEach(o -> {
            Optional.ofNullable(EngScalEnum.getByLevel(o.getProjectScale()))
                    .ifPresent(e -> o.setProjectScale(e.getDesc()));
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public AttSixchargeBaseRespVo queryOne(AttSixchargeBaseReqVo vo) {
        return attSixchargeBaseMapper.queryOne(vo);
    }

    @Override
    public PageVO<AttSixchargeChangeRecord> queryChangeRecord(AttSixchargeChangeRecord record) {
        if (record.getPageNum() > 0 && record.getPageSize() > 0) {
            PageHelper.startPage(record.getPageNum(), record.getPageSize());
        }
        PageVO<AttSixchargeChangeRecord> pageVO = PageUtils.getPageVO(attSixchargeBaseMapper.queryChangeRecord(record));
        return pageVO;
    }


}
