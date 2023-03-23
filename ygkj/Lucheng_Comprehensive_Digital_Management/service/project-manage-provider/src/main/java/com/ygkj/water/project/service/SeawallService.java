package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.project.model.SeawallData;
import com.ygkj.project.vo.request.SeawallQueryReqVo;
import com.ygkj.project.vo.response.AttStResVo;
import com.ygkj.project.vo.response.SeawallResVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
public interface SeawallService extends IService<AttSeawallBase> {
    /**
     * 导出海塘工程数据
     *
     * @param response
     * @param reqVo
     */
    void exportSeawall(HttpServletResponse response, SeawallQueryReqVo reqVo);

    /**
     * 海塘工程分页列表
     *
     * @param reqVo
     * @return
     */
    PageVO<AttSeawallBase> pageSeawall(SeawallQueryReqVo reqVo);

    /**
     * 对海塘工程分页统计
     *
     * @param reqVo
     * @return
     */
    JSONObject stats4PageSeawall(SeawallQueryReqVo reqVo);

    /**
     * 海塘报警热力图
     *
     * @return
     */
    List<? extends AttSeawallBase> seawallHeatMap();

    /**
     * 海塘组件C位
     *
     * @return
     */
    JSONObject seawallUnitCWindow(boolean forceNewestWarnState, Date dataTime);

    /**
     * 海塘组件报警卡片
     *
     * @param warnType
     * @return
     */
    JSONObject seawallUnitWarnCard(Integer warnType, boolean forceNewestWarnState, Date dataTime);

    /**
     * 为海塘填充实时监控潮位以及报警状态
     *
     * @param list
     * @param forceNewestWarnState
     * @param dataTime
     */
    void fillTdzMonitorWarnState(List<? extends SeawallResVo> list, boolean forceNewestWarnState, Date dataTime);

    /**
     * 为海塘填充未来3天预报最高潮位以及最高潮报警状态
     *
     * @param list
     * @param dataTime
     */
    void fillTdzForecastWarnState(List<? extends SeawallResVo> list, Date dataTime);

    /**
     * 监测海塘报警并入库
     */
    void monitorSeawallWarn();

    /**
     * 海塘工程列表
     *
     * @param reqVo
     * @return
     */
    List<SeawallResVo> warnSeawallList(Integer warnType, boolean forceNewestWarnState, Date dataTime);

    /**
     * 导出海塘工程数据
     *
     * @param response
     * @param reqVo
     */
    void exportWarnSeawallList(HttpServletResponse response, Integer warnType, boolean forceNewestWarnState, Date dataTime);

    HashMap<String, Object> seawallAqjd();

    HashMap<String, Object> seawallHtdj();

    /**
     * 海塘关联测站
     *
     * @param seawallCode
     * @return
     */
    List<AttStResVo> listSt(String seawallCode);

    /**
     * 获取海塘数据
     *
     * @param seawallCode
     * @return
     */
    AttSeawallBase getSeawallInfo(String seawallCode);

    /**
     * 查询是否有排今天的值班数据
     *
     * @param seawallCode
     * @return
     */
    int isScheduling(String seawallCode);

    SeawallData getSeawallGcyg(String seawallCode);

    /**
     * 查询是否绑定测站
     *
     * @param seawallCode
     * @return
     */
    int isBindSt(String seawallCode);

    /**
     * 降雨预报
     *
     * @param seawallCode
     * @return
     */
    JSONArray forecastRainfall(String seawallCode);


}
