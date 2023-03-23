package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.AttHystBase;
import com.ygkj.big.screen.vo.request.HystReqVo;
import com.ygkj.big.screen.vo.response.HystDropPoint;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 水电站
 */
@DS("sdz")
public interface AttHystBaseMapper extends BaseMapper<AttHystBase> {

    /**
     * 查询总数
     *
     * @param reqVo
     * @return
     */
    Integer selectTotal(@Param("reqVo") HystReqVo reqVo);

    /**
     * 查询落点
     *
     * @param nowTime
     * @return
     */
    List<HystDropPoint> selectDropPoint();
}
