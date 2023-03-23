package com.ygkj.visualization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.utils.PageUtils;
import com.ygkj.visualization.mapper.DisasterStatisticMapper;
import com.ygkj.visualization.model.DisasterStatistic;
import com.ygkj.visualization.service.DisasterStatisticService;
import com.ygkj.visualization.vo.request.DisasterPageQueryVo;
import com.ygkj.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("disasterStatisticService")
public class DisasterStatisticServiceImpl implements DisasterStatisticService {


    private DisasterStatisticMapper disasterStatisticMapper;

    public DisasterStatisticServiceImpl(@Autowired DisasterStatisticMapper disasterStatisticMapper) {
        this.disasterStatisticMapper = disasterStatisticMapper;
    }

    @Override
    public PageVO<DisasterStatistic> selectByCondition(DisasterPageQueryVo queryVo) {
        if (queryVo.getPageNum() != null && queryVo.getPageSize() != null){
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        return PageUtils.getPageVO(disasterStatisticMapper.selectByCondition(queryVo));
    }

    @Override
    public List<DisasterStatistic> top5Disaster() {
        return disasterStatisticMapper.top5DamageDisaster();
    }

    @Override
    public Map<String, Object> statistic() {
        Map<String, Object> map = disasterStatisticMapper.statistic();
        map.put("maxYear",disasterStatisticMapper.maxLossesYear());
        return map;
    }

    @Override
    public DisasterStatistic load(Integer id) {
        return disasterStatisticMapper.load(id.intValue());
    }
}
