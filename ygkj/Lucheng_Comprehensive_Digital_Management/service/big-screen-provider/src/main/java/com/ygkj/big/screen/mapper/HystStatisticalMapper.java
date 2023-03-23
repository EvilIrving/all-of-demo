package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.HystStatistical;
import com.ygkj.big.screen.vo.request.HystStatisticalReqVo;

import java.util.List;

/**
 * 水电站实时数据
 */
@DS("sdz")
public interface HystStatisticalMapper extends BaseMapper<HystStatistical> {

    @DS("business")
    List<HystStatistical> selectRealTimeData(HystStatisticalReqVo build);
}
