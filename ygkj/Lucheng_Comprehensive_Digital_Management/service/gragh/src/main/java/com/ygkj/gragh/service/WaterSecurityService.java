package com.ygkj.gragh.service;

import com.ygkj.gragh.model.WiuPlan;
import com.ygkj.gragh.model.WiuUse;
import com.ygkj.gragh.vo.request.KpakmaReqVo;
import com.ygkj.gragh.vo.request.WainReqVo;
import com.ygkj.gragh.vo.request.WiuReqVo;
import com.ygkj.gragh.vo.response.*;

import java.util.List;

public interface WaterSecurityService {

    /**
     * 水土流失
     *
     * @param year
     * @return
     */
    List<KpakmaResVo> listKpakma(KpakmaReqVo year);

    /**
     * 取水户列表
     *
     * @param wainReqVo
     * @return
     */
    List<WiuResVo> listWiu(WainReqVo wainReqVo);

    /**
     * 取水口列表
     *
     * @param wainReqVo
     * @return
     */
    List<WainResVo> listWain(WainReqVo wainReqVo);

    /**
     * 水源地列表
     *
     * @param wainReqVo
     * @return
     */
    List<SwhsResVo> listSwhs(WainReqVo wainReqVo);

    /**
     * 取水户基本信息
     *
     * @param wiuCode
     * @return
     */
    WiuBaseResVo wiuBase(String wiuCode);

    /**
     * 取水计划
     *
     * @param wiuCode
     * @return
     */
    List<WiuPlan> wiuPlan(String wiuCode);

    WiuWptResVo wiuWpt(String wiuCode);

    List<WiuRecordResVo> wiuRecord(String wiuCode);

    List<WiuUse> wiuUse(WiuReqVo wiuReqVo);

    WainBaseResVo wainBase(String wiuReqVo);

    SwhsBaseResVo swhsBase(String swhsCode);

    KpakmaBaseResVo kpakmaBase(String kpakmaCode);
}
