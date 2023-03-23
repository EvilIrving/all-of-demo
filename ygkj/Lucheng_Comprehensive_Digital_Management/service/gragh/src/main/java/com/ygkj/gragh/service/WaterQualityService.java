package com.ygkj.gragh.service;

import com.ygkj.gragh.model.BswqRatio;
import com.ygkj.gragh.vo.request.BswqReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
public interface WaterQualityService {
    /**
     * 列出水质监测点
     *
     * @param reqVo
     * @return
     */
    List<BswqResVo> list(BswqReqVo reqVo);

    /**
     * @param stationCode 测站编码
     * @param days        往前推的天数
     * @return
     */
    List<BswqResVo> bswqLevel(String stationCode, int days);

    /**
     * 查询水质监测比例
     *
     * @param type   1：省控；2：国考
     * @param months 往前推月份数，如12个月传12
     * @return
     */
    List<BswqRatio> bswqRatio(int type, int months);


    /**
     * 水质监测站点统计
     *
     * @param type
     * @return
     */
    List<ChartResVo<String, Long>> bswqStatistic(int type);


}
