package com.ygkj.digitization.service;

import com.ygkj.digitization.model.GmBusiness;
import com.ygkj.digitization.vo.request.BusManAddUpdateVo;
import com.ygkj.digitization.vo.request.BusManQueryVo;
import com.ygkj.vo.PageVO;

/**
 * @author xq
 * @Description 业务管理服务层
 * @Date 2021/2/1
 */
public interface GmBusinessService {
    PageVO<GmBusiness> upperQuery(BusManQueryVo queryVo);

    GmBusiness upperQueryOneById(String id);

    int upperAdd(BusManAddUpdateVo addUpdateVo);

    int upperUpdate(BusManAddUpdateVo addUpdateVo);

    int upperDeleteById(String id);

    int sendOrder(String id);

    int syncTask(Integer year);

    int alertTask(String id);
}
