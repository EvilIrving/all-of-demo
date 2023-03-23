package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.RiverUnionInfoMapper;
import com.ygkj.river.service.RiverUnionInfoService;
import com.ygkj.river.model.RiverUnionInfo;
import com.ygkj.river.vo.request.RiverUnionInfoAddVo;
import com.ygkj.river.vo.request.RiverUnionInfoPageQueryVo;
import com.ygkj.river.vo.response.RiverUnionInfoResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("riverUnionInfoService")
public class RiverUnionInfoServiceImpl implements RiverUnionInfoService {

    private RiverUnionInfoMapper riverUnionInfoMapper;

    private GenerateDistributionIDService generateDistributionIDService;

    private AttAdcdBaseMaper adcdMapper;

    public RiverUnionInfoServiceImpl(@Autowired RiverUnionInfoMapper riverUnionInfoMapper,
                                     @Autowired GenerateDistributionIDService generateDistributionIDService,
                                     @Autowired AttAdcdBaseMaper attAdcdBaseMapper) {
        this.riverUnionInfoMapper = riverUnionInfoMapper;
        this.generateDistributionIDService = generateDistributionIDService;
        this.adcdMapper = attAdcdBaseMapper;
    }

    @Override
    public PageVO riverUnionInfos(RiverUnionInfoPageQueryVo queryVo) {

        if (null != queryVo.getPageSize() && null != queryVo.getPageNum()) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        PageVO result = PageUtils.getPageVO(riverUnionInfoMapper.selectByCondition(queryVo));
        List<RiverUnionInfoResVo> list = result.getList();
        List<Map<String, String>> adList = adcdMapper.selectNamesOfAdcds(list.stream()
                .filter(o -> StringUtils.isNotBlank(o.getAreaName()))
                .map(RiverUnionInfoResVo::getAreaName)
                .collect(Collectors.joining(",")));
        adList = adList == null ? Collections.emptyList() : adList;
        Map<String, String> adMap = adList.stream().collect(Collectors.toMap(o -> o.get("adcd"), o -> o.get("adnm")));
        for (RiverUnionInfoResVo riverUnionInfoResVo : list) {
            riverUnionInfoResVo.setAreaName(adMap.getOrDefault(riverUnionInfoResVo.getAreaName(), StringUtils.EMPTY));
        }
        return result;
    }

    @Override
    public boolean insert(RiverUnionInfoAddVo addVo) {
        RiverUnionInfo info = new RiverUnionInfo();
        BeanUtils.copyProperties(addVo, info);
        info.setId(generateDistributionIDService.nextID());
        if (riverUnionInfoMapper.insert(info) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(RiverUnionInfoAddVo addVo) {
        RiverUnionInfo info = new RiverUnionInfo();
        BeanUtils.copyProperties(addVo, info);
        if (riverUnionInfoMapper.update(info) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        if (riverUnionInfoMapper.delete(id) > 0) {
            return true;
        }
        return false;
    }
}
