package com.ygkj.water.project.mapper;

import com.ygkj.project.model.DtsRaBasicDataDbWaterExpert;
import com.ygkj.project.vo.request.WaterExpertReqVo;
import com.ygkj.project.vo.response.WaterExpertResVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 14:24
 */
@Repository
public interface WaterExpertMapper {
    /**
     * 水利专家列表
     *
     * @param reqVo
     * @return
     */
    List<WaterExpertResVo> listWaterExpert(WaterExpertReqVo reqVo);

    /**
     * 新增水利专家
     *
     * @param waterExpert
     */
    void saveWaterExpert(WaterExpertResVo waterExpert);

    /**
     * 更新水利专家
     *
     * @param waterExpert
     */
    void updateWaterExpert(WaterExpertResVo waterExpert);

    /**
     * 获取水利专家详情
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
     * 统计各级水利专家数量
     *
     * @param reqVo
     * @return
     */
    List<Map<String, Object>> countWaterExpert(WaterExpertReqVo reqVo);
}
