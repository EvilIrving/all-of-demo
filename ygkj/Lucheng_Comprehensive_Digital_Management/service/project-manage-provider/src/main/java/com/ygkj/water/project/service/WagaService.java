package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.AttWagaBase;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.vo.request.WagaReplayReqVo;
import com.ygkj.project.vo.request.WagaStationWaterReqVo;
import com.ygkj.project.vo.request.WrpWagaQueryReqVo;
import com.ygkj.project.vo.response.WagaStationWaterResVo;
import com.ygkj.vo.PageVO;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/15
 */
public interface WagaService {
    /**
     * 水闸及其测站及其水位数据
     *
     * @param reqVo
     * @return
     */
    PageVO wagaWaterLevel(WagaStationWaterReqVo reqVo);


    /**
     * 为水闸填充水位信息
     *
     * @param wagaList
     * @return
     */
    List<WagaStationWaterResVo> fillWater2Waga(List<AttWagaBase> wagaList, WagaStationWaterReqVo reqVo);


    /**
     * 水闸复盘统计
     *
     * @return
     */
    JSONObject wagaReplayStats(WagaReplayReqVo reqVo);

    double wagaTotalDrainage(List<StWasR> data, Date end);


    /**
     * 导出水闸
     *
     * @param reqVo
     * @param response
     */
    void exportWrpWagaUnitTable(WrpWagaQueryReqVo reqVo, HttpServletResponse response);
}
