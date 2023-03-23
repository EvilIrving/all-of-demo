package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.*;
import com.ygkj.project.vo.request.WarningWaterRainReqVo;

import java.util.List;

public interface WarningWaterRainMapper {

    /**
     * 测站列表
     *
     * @param stCodes
     * @return
     */
    List<AttStBase> listSt(String stCodes);

    /**
     * 河道水位一小时最新数据
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<StRiverR> listZz(WarningWaterRainReqVo reqVo);

    /**
     * 水库水位最新数据
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<StRsvrR> listRr(WarningWaterRainReqVo reqVo);

    /**
     * 潮位自行数据
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<StTideR> listTt(WarningWaterRainReqVo reqVo);

    /**
     * 水闸水位最新数据
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<StWasR> listDd(WarningWaterRainReqVo reqVo);

    /**
     * 雨量最新数据
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<StPptnR> listPp(WarningWaterRainReqVo reqVo);
}
