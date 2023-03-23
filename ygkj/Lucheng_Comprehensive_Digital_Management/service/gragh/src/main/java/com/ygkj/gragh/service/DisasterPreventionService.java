package com.ygkj.gragh.service;

import com.ygkj.gragh.model.TyphoonData;
import com.ygkj.gragh.vo.request.TyphoonReqVo;

import java.util.List;

public interface DisasterPreventionService {

    /**
     * 根据id查询台风详细数据
     *
     * @param id
     * @return
     */
    TyphoonData selectTyphoon(Integer id);

    /**
     * 实时台风
     *
     * @return
     */
    List<TyphoonData> realTimeTyphoon();

    /**
     * 历史台风
     *
     * @param typhoonReqVo
     * @return
     */
    List<TyphoonData> historyTyphoon(TyphoonReqVo typhoonReqVo);
}
