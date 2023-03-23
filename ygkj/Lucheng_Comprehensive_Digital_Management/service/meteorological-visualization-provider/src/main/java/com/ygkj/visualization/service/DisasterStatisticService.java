package com.ygkj.visualization.service;

import com.ygkj.visualization.model.DisasterStatistic;
import com.ygkj.visualization.vo.request.DisasterPageQueryVo;
import com.ygkj.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface DisasterStatisticService {

    PageVO<DisasterStatistic> selectByCondition(DisasterPageQueryVo queryVo);

    List<DisasterStatistic> top5Disaster();

    Map<String, Object> statistic();

    DisasterStatistic load(Integer id);
}
