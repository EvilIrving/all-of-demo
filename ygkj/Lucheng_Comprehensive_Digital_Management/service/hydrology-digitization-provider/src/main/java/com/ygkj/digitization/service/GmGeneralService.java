package com.ygkj.digitization.service;

import com.ygkj.digitization.model.GmGeneral;
import com.ygkj.digitization.vo.request.GenManAddUpdateVo;
import com.ygkj.digitization.vo.request.GenManUpperQueryVo;
import com.ygkj.vo.PageVO;

/**
 * @author xq
 * @Description 日常管理服务层
 * @Date 2021/2/1
 */
public interface GmGeneralService {

    PageVO<GmGeneral> upperQuery(GenManUpperQueryVo queryVo);


    PageVO<GmGeneral> subQuery(GenManUpperQueryVo queryVo);


    int subAdd(GenManAddUpdateVo addUpdateVo);


    int subUpdate(GenManAddUpdateVo addUpdateVo);


    int subDelete(String genManId);
}
