package com.ygkj.digitization.mapper;

import com.ygkj.digitization.vo.request.AnalysisListReqVo;
import com.ygkj.digitization.vo.request.DesignReqVo;
import com.ygkj.digitization.vo.response.DesignSoftResVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface ComprehensiveAnalysisMapper {

    /**
     * 水库一些基本信息
     *
     * @param reqVo
     * @return
     */
    List<ResSoftResVo> listResSoftAnalysis(AnalysisListReqVo reqVo);

    /**
     * 海塘基本信息
     *
     * @param reqVo
     * @return
     */
    List<SeawallSoftResVo> listSeawallSoftAnalysis(AnalysisListReqVo reqVo);

    /**
     * 设计安全分析
     *
     * @param reqVo
     * @return
     */
    List<DesignSoftResVo> designSoftAnalysis(DesignReqVo reqVo);
}
