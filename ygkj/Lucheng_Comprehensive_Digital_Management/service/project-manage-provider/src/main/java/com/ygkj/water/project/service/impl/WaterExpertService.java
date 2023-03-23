package com.ygkj.water.project.service.impl;

import com.ygkj.project.model.DtsRaBasicDataDbWaterExpert;
import com.ygkj.project.vo.request.WaterExpertReqVo;
import com.ygkj.project.vo.response.WaterExpertResVo;
import com.ygkj.vo.PageVO;

import java.util.List;
import java.util.Map;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 14:22
 */
public interface WaterExpertService {
    /**
     * 水利专家列表查询
     *
     * @param reqVo
     * @return
     */
    PageVO<WaterExpertResVo> listWaterExpert(WaterExpertReqVo reqVo);

    /**
     * 新增或修改水利专家
     *
     * @param waterExpert
     */
    void editWaterExpert(WaterExpertResVo waterExpert);

    /**
     * 查询水利专家详情
     *
     * @param dtsCmfId
     */
    DtsRaBasicDataDbWaterExpert selectWaterExpert(String dtsCmfId);

    /**
     * 删除水利专家
     *
     * @param dtsCmfId
     */
    void delWaterExpert(String dtsCmfId);

    /**
     * 统计数量
     *
     * @param reqVo
     * @return
     */
    List<Map<String, Object>> countWaterExpert(WaterExpertReqVo reqVo);
}
