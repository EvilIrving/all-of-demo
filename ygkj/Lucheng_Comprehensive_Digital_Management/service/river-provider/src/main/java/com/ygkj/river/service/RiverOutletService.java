package com.ygkj.river.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.vo.request.RiverOutletQueryVo;
import com.ygkj.river.vo.response.RiverOutletResVo;
import com.ygkj.vo.PageVO;

/**
 * @author xq
 * @Description
 * @Date 2021/10/9
 */
public interface RiverOutletService {

    String outletGradGt = "规模以上";
    String outletGradLt = "规模以下";

    /**
     * 入河排污口分页查询
     *
     * @param queryVo
     * @return
     */
    PageVO<RiverOutletResVo> pageList(RiverOutletQueryVo queryVo);

    /**
     * 入河排污口表头统计
     *
     * @param queryVo
     * @return
     */
    JSONObject stats4PageList(RiverOutletQueryVo queryVo);
}
