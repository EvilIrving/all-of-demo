package com.ygkj.big.screen.drill;

import com.ygkj.big.screen.model.BsInfluentRel;
import com.ygkj.big.screen.vo.request.DoDrillParamReqVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
public interface DrillManagerStrategy {

    void doDrill(DoDrillParamReqVo doDrillParam);

    void doSubscribe();

    void shutdown();

    default BsInfluentRel buildInfluence(String id, String groupId, String infGroupId) {
        BsInfluentRel result = new BsInfluentRel();
        result.setId(id);
        result.setGroupDataId(groupId);
        result.setInfGroupDataId(infGroupId);
        return result;
    }


}
