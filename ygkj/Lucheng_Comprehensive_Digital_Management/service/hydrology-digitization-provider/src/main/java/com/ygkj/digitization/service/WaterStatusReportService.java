package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.request.MrwlCurveReqVo;
import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.digitization.vo.request.WaterStatusReportReqVo;
import com.ygkj.digitization.vo.response.ZqCurveConfigResVo;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.project.vo.response.RsvrReplayResVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
public interface WaterStatusReportService {
    /**
     * 河网水位
     * @param dataTime
     * @return
     */
    CommonResult rivernetWaterLevel(Date dataTime,String name);

    /**
     * 列出山区河道水位的流量曲线配置列表
     * @return
     */
    Map<String,List<ZqCurveConfigResVo>> listMrwlZqCurveConfig();

    /**
     * 山区河道水位流量曲线
     * @param reqVo
     * @return
     */
    List<StPrrwtResVo> mrwlCurve(MrwlCurveReqVo reqVo);

    /**
     * 山区河道水位
     * @param reqVo
     * @return
     */
    List<JSONObject> mountainRiverWaterLevel(MrwlReqVo reqVo);
    
    /**
     * 河道水位统计表
     * @param start
     * @param end
     * @return
     */

    List<JSONObject> riverWaterStats(Date start, Date end,String name);

    /**
     * 河道日平均水位
     * @param start
     * @param end
     * @return
     */
    List<JSONObject> riverDayAvgWater(Date start, Date end,String name);

    /**
     * 河道水位超警表
     * @param start
     * @param end
     * @return
     */
    List<JSONObject> riverOverWarn(Date start, Date end,String name);

    /**
     * 逐时潮位增水
     * @param start
     * @param end
     * @return
     */
    Map<String,JSONObject> tideSubPerHour(Date start, Date end,String name);

    /**
     * 高潮位增水
     * @param start
     * @param end
     * @return
     */
    List<JSONObject> highTideSub(Date start, Date end,String name);

    /**
     * 水库蓄水统计表
     * @param dataTime
     * @return
     */
    List<RsvrStationWaterResVo> rsvrStorageStats(Date dataTime);

    /**
     * 水库水位统计表
     * @param start
     * @param end
     * @return
     */
    List<RsvrReplayResVo> rsvrWaterStats(Date start, Date end);

    /**
     * 水库水位时段表
     * @param start
     * @param end
     * @return
     */
    List<LinkedHashMap> rsvrWaterTimeInterval(Date start, Date end,String name);

    /**
     * 水库水位超讯表
     * @param start
     * @param end
     * @return
     */
    List<JSONObject>  rsvrWaterOverLimit(Date start, Date end,String name);

    /**
     * 水库水雨情过程表
     * @param start 起始时刻，默认为前24小时
     * @param end 截止时刻，默认为当前时刻
     * @param resCode 水库编码
     * @return java.util.List
     */
    List<JSONObject> rsvrWaterRainInfo(Date start,Date end,String resCode);

    /**
     * 河网水位-导出
     * @param reqVo
     * @param response
     */
    void rivernetWaterLevelExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 河道水位统计表-导出
     * @param reqVo
     * @param response
     */
    void riverWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 河道日平均水位-导出
     * @param reqVo
     * @param response
     */
    void riverDayAvgWaterExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 河道水位超警表-导出
     * @param reqVo
     * @param response
     */
    void riverOverWarnExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 逐时潮位增水-导出
     * @param reqVo
     * @param response
     */
    void tideSubPerHourExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 高潮位增水-导出
     * @param reqVo
     * @param response
     */
    void highTideSubExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 水库蓄水统计表-导出
     * @param reqVo
     * @param response
     */
    void rsvrStorageStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 水库水位统计表-导出
     * @param reqVo
     * @param response
     */
    void rsvrWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 水库水位时段表-导出
     * @param reqVo
     * @param response
     */
    void rsvrWaterTimeIntervalExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 水库水位超讯表-导出
     * @param reqVo
     * @param response
     */
    void rsvrWaterOverLimitExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);

    /**
     * 水库水雨情过程表-导出
     * @param reqVo
     * @param response
     */
    void rsvrWaterRainInfoExport(WaterStatusReportReqVo reqVo, HttpServletResponse response);
}
