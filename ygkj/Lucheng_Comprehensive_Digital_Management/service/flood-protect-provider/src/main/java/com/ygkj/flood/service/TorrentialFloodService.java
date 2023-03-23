package com.ygkj.flood.service;

import com.ygkj.flood.model.AttPrevBase;
import com.ygkj.flood.model.AttPrevTfBase;
import com.ygkj.flood.model.AttPrevTfBaseResponsibilities;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvFloodAffectPopulationResVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
public interface TorrentialFloodService {

    /**
     * 查询防治对象
     *
     * @return
     */
    List<AttPrevBase> listPrevInfo();

    /**
     * 查询防治对象
     *
     * @return
     */
    List<AttPrevTfBase> listPrevTfInfo(PrevTfInfoReqVo reqVo);

    PageVO<AttPrevTfBaseResponsibilities> listPrevTfResponsibilities(PrevTfInfoReqVo reqVo);

    /**
     * 发送短信
     * @param reqVo
     * @return
     */
    void sendTfMsg(PrevTfInfoReqVo reqVo);

    /**
     * 查询 灾害防御调度-山洪灾害责任人 列表
     * @param reqVo
     * @return
     */
    PageVO<PvPchargeResVo> pvPchargeList(PrevTfInfoReqVo reqVo);

    /**
     * 更新
     * @param resVo
     * @return
     */
    Boolean updatePcharge(PvPchargeResVo resVo);

    /**
     * 新增
     * @param resVo
     * @return
     */
    Boolean insertPcharge(PvPchargeResVo resVo);

    Boolean deletePcharge(String code);

    List<AttPrevTfBase> listPrevObj(AttPrevTfBaseVo vo);

    /**
     * 影响人口列表
     * @param reqVo
     * @return
     */
    PageVO<PvFloodAffectPopulationResVo> bigScreenAffectPopulationList(PrevTfInfoReqVo reqVo);
}
