package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.WiuPlan;
import com.ygkj.gragh.model.WiuUse;
import com.ygkj.gragh.vo.request.KpakmaReqVo;
import com.ygkj.gragh.vo.request.WainReqVo;
import com.ygkj.gragh.vo.request.WiuReqVo;
import com.ygkj.gragh.vo.response.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WaterSecurityMapper {

    /**
     * 水土流失区基本信息
     *
     * @return
     */
    List<KpakmaBaseResVo> listKpakma(KpakmaReqVo kpakmaReqVo);

    /**
     * 水土流失面积列表
     *
     * @param year
     * @return
     */
    @DS("business")
    List<KpakmaResVo> listKpakmaYear(Integer year);

    /**
     * 取水户列表
     *
     * @param wainReqVo
     * @return
     */
    List<WiuResVo> listWiu(WainReqVo wainReqVo);

    /**
     * 取水户累计取水量
     *
     * @param list
     * @return
     */
    @DS("business")
    List<WiuUseResVo> listWiuUse(@Param("list") List<String> list, @Param("year") String year);

    /**
     * 取水口名称
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
     * 根据时间和取水户编码查询取水量
     *
     * @param time
     * @param wiuCode
     * @return
     */
    @DS("business")
    List<WiuUse> listWiuUseByTime(@Param("time") String time, @Param("wiuCode") String wiuCode);

    /**
     * 根据取水户编码获取取水计划
     *
     * @param wiuCode
     * @return
     */
    @DS("business")
    List<WiuPlan> wiuPlan(String wiuCode);

    /**
     * 根据取水户编码获取取水许可证
     *
     * @param wiuCode
     * @return
     */
    WiuWptResVo wiuWpt(String wiuCode);

    /**
     * 根据取水户编码获取取水档案
     *
     * @param wiuCode
     * @return
     */
    @DS("business")
    List<WiuRecordResVo> wiuRecord(String wiuCode);

    /**
     * 查询取水量
     *
     * @param wiuReqVo
     * @return
     */
    @DS("business")
    List<WiuUse> wiuUse(WiuReqVo wiuReqVo);

    /**
     * 取水口基本信息
     *
     * @param wainCode
     * @return
     */
    WainBaseResVo wainBase(String wainCode);

    /**
     * 水源地基本信息
     *
     * @param swhsCode
     * @return
     */
    SwhsBaseResVo swhsBase(String swhsCode);

    /**
     * 水源地标示牌
     *
     * @param swhsCode
     * @return
     */
    List<String> listSwhsFile(String swhsCode);

    /**
     * 根据年份和取水户编码查询累计取水量
     *
     * @param year
     * @param wiuCode
     * @return
     */
    @DS("business")
    BigDecimal queryWiuUse(@Param("year") String year, @Param("wiuCode") String wiuCode);

    /**
     * 水土流失基本信息
     *
     * @param kpakmaCode
     * @return
     */
    KpakmaBaseResVo kpakmaBase(String kpakmaCode);

    /**
     * 水土流失实时
     *
     * @param kpakmaCode
     * @param year
     * @return
     */
    @DS("business")
    KpakmaBaseResVo listKpakmaLoss(@Param("kpakmaCode") String kpakmaCode, @Param("year") int year);
}
