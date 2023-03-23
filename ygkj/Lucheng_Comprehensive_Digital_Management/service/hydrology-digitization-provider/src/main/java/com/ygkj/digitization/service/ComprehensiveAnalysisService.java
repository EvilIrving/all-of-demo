package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.digitization.model.RaderData;
import com.ygkj.digitization.vo.request.AnalysisListReqVo;
import com.ygkj.digitization.vo.request.DesignReqVo;
import com.ygkj.digitization.vo.response.DesignSoftResVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface ComprehensiveAnalysisService {

    /**
     * 水库安全分析
     *
     * @param reqVo
     * @return
     */
    PageVO<ResSoftResVo> resSoftAnalysis(AnalysisListReqVo reqVo);

    /**
     * 海塘安全分析
     *
     * @param reqVo
     * @return
     */
    PageVO<SeawallSoftResVo> seawallSoftAnalysis(AnalysisListReqVo reqVo);

    /**
     * 设计安全分析
     *
     * @param reqVo
     * @return
     */
    PageVO<DesignSoftResVo> designSoftAnalysis(DesignReqVo reqVo);

    JSONArray wenzhouCloudData();

    List<RaderData> wenzhouRadarData();
}
