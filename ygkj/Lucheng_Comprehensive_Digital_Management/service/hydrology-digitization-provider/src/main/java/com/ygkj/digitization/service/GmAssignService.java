package com.ygkj.digitization.service;

import com.ygkj.digitization.model.GmAssign;
import com.ygkj.digitization.vo.request.AssManAddUpdateVo;
import com.ygkj.digitization.vo.request.AssManQueryVo;
import com.ygkj.vo.PageVO;

/**
 * @author xq
 * @Description 任务书下达服务层
 * @Date 2021/2/1
 */
public interface GmAssignService {
    PageVO<GmAssign> upperQuery(AssManQueryVo queryVo);

    GmAssign upperQueryOneById(String id);

    int upperAdd(AssManAddUpdateVo addUpdateVo);

    int upperUpdate(AssManAddUpdateVo addUpdateVo);

    int upperDeleteById(String id);

    int sendOrder(String id);

    int syncTask(Integer year);

    int alertTask(String id);
}
