package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.QrCodeReqVo;
import com.ygkj.big.screen.vo.request.SeawallAmbitusVo;
import com.ygkj.big.screen.vo.response.SeawallWarnResVo;
import com.ygkj.project.model.WarningRule;
import com.ygkj.water.model.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wgf
 * @date 2021/10/18 19:27
 * @Description:
 */
public interface GiveAnAlarmService {

    /**
     * 海塘预警
     *
     * @return
     */
    CommonResult seawallWarning(String seawallCode, String time,Boolean warning);

    /**
     * 海塘报警
     *
     * @return
     */
    CommonResult seawallGiveAnAlarm(String seawallCode, String time,Boolean warning );

    /**
     * 海塘报警
     *
     * @return
     */
    CommonResult seawallGiveAnAlarm(String seawallCode, String time,String fcstId ,Boolean warning);

    /**
     * 水位报警
     *
     * @return
     */
    CommonResult waterGiveAnAlarm();

    /**
     * 水位报警
     *
     * @return
     */
    CommonResult waterGiveAnAlarm2(WarningRule warningRule);

    /**
     * 降雨报警
     *
     * @return
     */
    CommonResult rainGiveAnAlarm();

    /**
     * 降雨报警
     *
     * @return
     */
    CommonResult rainGiveAnAlarm2(WarningRule warningRule);

    /**
     * 降雨报警
     *
     * @return
     */
    CommonResult rainGiveAnAlarm3(WarningRule warningRule);

    /**
     * 台风报警
     *
     * @return
     */
    CommonResult typhoonGiveAnAlarm();

    /**
     * 运行报警
     *
     * @return
     */
    CommonResult runGiveAnAlarm();

    /**
     * 统计报警所有的数值
     *
     * @return
     */
    CommonResult giveAnAlarmCount();

    /**
     * 海塘预警
     *
     * @param seawallCode
     * @param time
     * @return
     */
    SeawallWarnResVo selectWarn(String seawallCode,String time,String fcstId);

    /**
     * 台风影响
     *
     * @param date
     * @return
     */
    CommonResult typhoonInfluence(String date) throws ParseException;

    /**
     * 各海塘台风影响
     *
     * @param date 所选时间
     * @param id   台风编码
     * @return
     */
    CommonResult seawallTyphoon(String date, String id) throws ParseException;

    /**
     * 当前台风和经纬度关系
     *
     * @param lat
     * @param lng
     * @return
     */
    JSONArray fuckingCurrentTyphoon(Double lat, Double lng);

    /**
     * 海塘极简模式周边数据查询
     * @param vo
     * @return
     */
    CommonResult seawallAmbitus(SeawallAmbitusVo vo) throws UnsupportedEncodingException;

    /**
     * 位移预警
     * @param date
     * @return jclx  1位移，2沉降
     */
    CommonResult displacement(String date,Integer jclx);

    /**
     * 安全监测站点数据
     * @param days  往前推的天数
     * @param stationCode 测站编码
     * @param startTime
     * @param endTime
     * @return
     */
    CommonResult getAqjcData(Integer days, String stationCode, String startTime, String endTime);

    /**
     * 海塘二维码
     * @param reqVo
     * @return
     */
    String qrCode(QrCodeReqVo reqVo);

    /**
     * 安全监测警报
     * @param date
     * @param seawallCode
     * @return
     */
    Map<String,Integer> aqjcWarn(String date, String seawallCode);

    String qrCode(String seawallCode);

    void exportGetAqjcData(Integer days, String stationCode, String startTime, String endTime, String stationName, HttpServletResponse response);

    void floodPreventionBulletin();

    /**
     * 通过水位信息获取工程的对应的库容
     * @param rz
     * @param prcd
     * @return
     */
    Float getXByY(Float rz, String prcd);

    /**
     * 计算查询的表名字
     * @param tablePrefix 表名前缀
     * @param start  数据的开始时间
     * @param end 数据的结束时间
     * @return
     */
    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end);

    /**
     * App 上面的防汛
     * @return
     */
    Object floodPreventionBulletinApp();

    void waterAndRainBriefing();

    List<Map<String, Object>> rainfallReport(String startTime, String endTime, Double minRain, Double maxRain);

    void exportRainfallReport(String startTime, String endTime, Double minRain, Double maxRain, HttpServletResponse response) throws IOException;

    JSONObject riskHiddenDanger();

    List<JSONObject> latestMonitoringData();
}
