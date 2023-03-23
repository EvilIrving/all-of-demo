package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
public interface UnitManager {


    void doSubscribe();

    void doPublish();

    Object getData(UnitDataReqVo reqVo);

    void buildData(UnitDataBuilderReqVo reqVo);


}
