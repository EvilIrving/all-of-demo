package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.RiverInfoMapper;
import com.ygkj.river.model.RiverInfo;
import com.ygkj.river.service.RiverService;
import com.ygkj.river.vo.request.RiverInfoAddVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverInfoResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("riverService")
public class RiverServiceImpl implements RiverService {

    private RiverInfoMapper riverInfoMapper;

    private GenerateDistributionIDService generateDistributionIDService;

    public RiverServiceImpl(@Autowired RiverInfoMapper riverInfoMapper,
                            @Autowired GenerateDistributionIDService generateDistributionIDService) {
        this.riverInfoMapper = riverInfoMapper;
        this.generateDistributionIDService = generateDistributionIDService;
    }

    @Override
    public PageVO riverPageQuery(RiverInfoPageQueryVo queryVo) {
        if (queryVo.getPageNum() != null && queryVo.getPageSize() != null) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<RiverInfoResVo> list = riverInfoMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }

    @Override
    public boolean insert(RiverInfoAddVo addVo) {
        RiverInfo riverInfo = new RiverInfo();
        BeanUtils.copyProperties(addVo, riverInfo);
        riverInfo.setId(generateDistributionIDService.nextID());
        if (riverInfoMapper.insert(riverInfo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(RiverInfoAddVo addVo) {
        RiverInfo riverInfo = new RiverInfo();
        BeanUtils.copyProperties(addVo, riverInfo);
        if (riverInfoMapper.update(riverInfo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String ids) {
        if (riverInfoMapper.delete(ids) > 0) {
            return true;
        }
        return false;
    }
}
