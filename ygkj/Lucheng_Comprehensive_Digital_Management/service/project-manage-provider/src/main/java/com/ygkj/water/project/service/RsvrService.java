package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.project.vo.request.RsvrReplayReqVo;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.request.RsvrWatSrcQueryVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.project.vo.response.WatSrcResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/8/11
 */
public interface RsvrService {


    /**
     * 列出安全鉴定结论
     *
     * @return
     */
    List<String> listSafetyConclusion();

    /**
     * 水库及其测站及其最新水位数据
     *
     * @param reqVo
     * @return
     */
    List<RsvrStationWaterResVo> rsvrNewestWaterLevel(RsvrStationWaterReqVo reqVo);

    /**
     * 水库/大中型组件详表
     *
     * @param reqVo
     * @return
     */
    PageVO<RsvrStationWaterResVo> rsvrUnitList(RsvrStationWaterReqVo reqVo);


    /**
     * 水库/大中型组件详表统计
     *
     * @param reqVo
     * @return
     */
    JSONObject stats4RsvrUnitList(RsvrStationWaterReqVo reqVo);

    /**
     * 水库及其测站及其水位数据
     *
     * @param reqVo
     * @return
     */
    PageVO rsvrWaterLevel(RsvrStationWaterReqVo reqVo);

    /**
     * 为水库填充水位信息
     *
     * @param rsvrList
     * @return
     */
    List<RsvrStationWaterResVo> fillWater2Rsvr(List<AttResBase> rsvrList, RsvrStationWaterReqVo reqVo);

    /**
     * 计算水库的超汛限信息
     *
     * @param rsvrList
     */
    void computeRsvrOverLimitInfo(List<RsvrStationWaterResVo> rsvrList);

    /**
     * 计算水位的额外信息
     *
     * @param rsvrList
     * @param reqVo
     */
    void computeRsvrExternalInfo(List<RsvrStationWaterResVo> rsvrList, RsvrStationWaterReqVo reqVo);

    /**
     * 查询水库水位
     * 约定waterLevel=Double.MIN_VALUE 为无水位数据
     *
     * @param rsvrList 水库列表
     * @param dataTime 复盘时刻
     * @return
     */
    Map<String, AttStBase> getRsvrWaterLevelWithStation(List<? extends AttResBase> rsvrList, Date dataTime);

    /**
     * 查询水库水位
     * 约定waterLevel=Double.MIN_VALUE 为无水位数据
     *
     * @param mainStcdList 水库主测站列表
     * @param dataTime     复盘时刻
     * @return
     */
    Map<String, AttStBase> getRsvrWaterLevelByMainStcd(List<String> mainStcdList, Date dataTime);

    /**
     * 查询水库水位
     *
     * @param stcdList 水库主测站列表
     * @param dataTime 数据时刻
     * @return
     */
    Map<String, StRsvrR> getRsvrWaterLevelByStcd(List<String> stcdList, Date dataTime);


    /**
     * 获取汛限状态 0 为正常蓄水水位 1为梅汛限水位  2为台汛限水位
     *
     * @param dataTime
     * @return
     */
    int getLimitStatus(LocalDate dataTime);

    /**
     * 获取水库的当前汛限水位
     *
     * @param rsvrBase 水库信息
     * @param dataTime
     * @return
     */
    BigDecimal getCurrentLimitWaterLevel(AttResBase rsvrBase, LocalDate dataTime);

    /**
     * 水库工程统计，统计大中型水库24小时的泄流量
     *
     * @param dataTime 复盘时刻：格式是"yyyy-MM-dd HH:mm:ss"，不传则为最新值
     * @return
     */
    JSONObject rsvrProjDrainageStats(Date dataTime);

    List<RsvrStationWaterResVo> getRsvrDrillByTime(LocalDateTime dataTime);

    /**
     * 水库复盘统计
     *
     * @return
     */
    JSONObject rsvrReplayStats(RsvrReplayReqVo reqVo);

    /**
     * 导出水库数据
     *
     * @param response
     * @param reqVo
     */
    void exportRsvr(HttpServletResponse response, RsvrStationWaterReqVo reqVo);

    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源地名称
     * @return
     */
    List<WatSrcResVo> rsvrWatSrcTable(String watSrcName);

    /**
     * 水源保证列表 统计
     *
     * @param queryVo
     * @return
     */
    JSONObject stats4rsvrWatSrcTable(String watSrcName);

    /**
     * 水源保障c位
     *
     * @return
     */
    JSONObject rsvrWatSrc4CWindow();

    /**
     * 导出水源保障列表
     *
     * @param queryVo
     * @param response
     */
    void exportRsvrWatSrc(RsvrWatSrcQueryVo queryVo, HttpServletResponse response);


}
