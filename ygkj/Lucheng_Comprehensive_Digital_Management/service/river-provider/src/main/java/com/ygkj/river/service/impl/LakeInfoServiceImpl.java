package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttLkBaseMapper;
import com.ygkj.river.model.AttLkBase;
import com.ygkj.river.service.LakeInfoService;
import com.ygkj.river.vo.request.LakeInfoAddVo;
import com.ygkj.river.vo.request.LakeInfoPageQueryVo;
import com.ygkj.river.vo.response.LakeInfoResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("lakeInfoService")
public class LakeInfoServiceImpl implements LakeInfoService {

    private AttLkBaseMapper lakeInfoMapper;

    private GenerateDistributionIDService generateDistributionIDService;

    public LakeInfoServiceImpl(@Autowired AttLkBaseMapper lakeInfoMapper,
                               @Autowired GenerateDistributionIDService generateDistributionIDService) {
        this.lakeInfoMapper = lakeInfoMapper;
        this.generateDistributionIDService = generateDistributionIDService;
    }

    @Override
    public PageVO lakes(LakeInfoPageQueryVo queryVo) {
        if (null != queryVo.getPageNum() && null != queryVo.getPageSize()) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<LakeInfoResVo> list = lakeInfoMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }

    @Override
    public boolean insert(LakeInfoAddVo addVo) {
        AttLkBase attLkBase = new AttLkBase();
        BeanUtils.copyProperties(addVo, attLkBase);
        attLkBase.setLkCode(generateDistributionIDService.nextID());
        if (lakeInfoMapper.insert(attLkBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(LakeInfoAddVo addVo) {
        AttLkBase attLkBase = new AttLkBase();
        BeanUtils.copyProperties(addVo, attLkBase);
        if (lakeInfoMapper.update(attLkBase) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String code) {
        if (lakeInfoMapper.delete(code) > 0) {
            return true;
        }
        return false;
    }
}
