package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.service.WaterProtectionService;
import com.ygkj.big.screen.mapper.RiverProtectionMapper;
import com.ygkj.big.screen.mapper.WaterProtectionMapper;
import com.ygkj.big.screen.model.AttSwhsBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fml
 * @date 2021-04-02 9:39
 * @description 水资源保障专题
 */
@Service("waterProtectionService")
public class WaterProtectionServiceImpl implements WaterProtectionService {

    @Resource
    private WaterProtectionMapper waterProtectionMapper;

    @Resource
    private RiverProtectionMapper riverProtectionMapper;

    @Override
    public CommonResult<List<WzAdcdInfo>> waterSourceAdcdStats() {

        // 获取温州行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }
        // 获取水源地行政区划统计信息
        List<Map<String, Object>> waterSourceAdcdStats = waterProtectionMapper.waterSourceAdcdStats();
        if (CollectionUtils.isNotBlank(waterSourceAdcdStats)) {
            Map<String, Long> collect = waterSourceAdcdStats.stream().filter(ws -> ws.get("adcd") != null)
                    .collect(Collectors.toMap(ws -> (String) ws.get("adcd"), ws -> (Long) ws.get("num")));
            if (CollectionUtils.isNotBlank(collect)) {
                for (WzAdcdInfo wzAdcd : wzAdcds) {
                    String adcd = wzAdcd.getAdcd();
                    if (collect.containsKey(adcd)) {
                        wzAdcd.setNum(collect.get(adcd).intValue());
                    }
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> waterSourceTypeStats() {

        List<DataStatisticsInfo> statisticsInfos = waterProtectionMapper.waterSourceTypeStats();
        return CommonResult.success(statisticsInfos);
    }

    @Override
    public CommonResult<List<AttSwhsBase>> waterSources(String type, String scale) {

        List<AttSwhsBase> swhsBases = waterProtectionMapper.getWaterSource(type, scale);
        return CommonResult.success(swhsBases);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> wiuValidStats() {

        // 取水许可有效期内有效取水户数量
        List<DataStatisticsInfo> statisticsInfos = waterProtectionMapper.wiuValidStats();
        return CommonResult.success(statisticsInfos);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> wiuScaleStats() {

        //取水户用水规模（>50万吨,40-50,30-40,20-30,<20）
        List<DataStatisticsInfo> statisticsInfos = waterProtectionMapper.wiuScaleStats();
        return CommonResult.success(statisticsInfos);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> wiuUseWaterMonthStats(String year) {

        if (StringUtils.isBlank(year)) {
            year = String.valueOf(LocalDate.now().getYear());
        }

        List<DataStatisticsInfo> statisticsInfos = waterProtectionMapper.wiuUseWaterMonthStats(year);
        return CommonResult.success(statisticsInfos);
    }

    @Override
    public CommonResult<List<WzAdcdInfo>> wiuUseWaterAdcdStats(String year) {

        if (StringUtils.isBlank(year)) {
            year = String.valueOf(LocalDate.now().getYear());
        }

        // 获取温州行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取水源地行政区划统计信息
        List<Map<String, Object>> waterUses = waterProtectionMapper.wiuUseWaterAdcdStats(year);
        if (CollectionUtils.isNotBlank(waterUses)) {
            Map<String, BigDecimal> collect = waterUses.stream().filter(ws -> ws.get("adcd") != null && ws.get("num") != null)
                    .collect(Collectors.toMap(ws -> (String) ws.get("adcd"), ws -> (BigDecimal) ws.get("num")));

            if (CollectionUtils.isNotBlank(collect)) {
                for (WzAdcdInfo wzAdcd : wzAdcds) {
                    String adcd = wzAdcd.getAdcd();
                    if (collect.containsKey(adcd)) {
                        wzAdcd.setValue(collect.get(adcd).doubleValue());
                    }
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    @Override
    public JSONObject patrolInfoProcessStatisticss() {
        String url = "http://117.149.228.59/mgt/appPatrolInfo/processStatisticss";
        Map<String, String> params = new HashMap<>();
        params.put("queryType", "0,1");
        params.put("queryStratTime", DateUtils.format(new Date(), "yyyy") + "-01-01 00:00");
        params.put("queryEndTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm"));
        Map<String, String> header = new HashMap<>();
        header.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.Kva-STrmOYZUF2e45RHXuRUTor4zVwlEpNALxGNa6tctkNF2d6CXTLqg6KTMzypuwXob7HOMI4AfgSZaJImYee8hU44TykjUPM52ytlmL8OrwCa9H1XBjUKI0U4xM8lJeOjriVfwcHgjKzxJHvGddS330ipjnaZ7KGEiHBXTQyU.2hvyYc3XqcR_RxL7.ogkNqcdv9S8eyxSlh-AO51gVpG5SXg_QQyiAifOUA4C485PHb-h1FOmckuLMKAzKwmOKtI-qI_CHum6t5JI9oWqqtCPlDmnuhMGX_4vSho1g7M1vqjHgv-BeqjMMy-Glid4xUdX7UwfMXwMvyXHd1vlyGcsoII0KGYk8jiGWONiKyxIl.QdJJ7AJetxUaa382oL0oxQ");
        try {
            String resData = HttpClientUtil.httpPost(url, params, header);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                JSONObject data = jo.getJSONObject("data");
                return data;
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    //巡查数量统计
    @Override
    public JSONObject patrolInfoPatrolChaosHappStatic() {
        String url = "http://117.149.228.59/mgt/happening/patrolChaosHappStatic";
        Map<String, String> header = new HashMap<>();
        header.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.Kva-STrmOYZUF2e45RHXuRUTor4zVwlEpNALxGNa6tctkNF2d6CXTLqg6KTMzypuwXob7HOMI4AfgSZaJImYee8hU44TykjUPM52ytlmL8OrwCa9H1XBjUKI0U4xM8lJeOjriVfwcHgjKzxJHvGddS330ipjnaZ7KGEiHBXTQyU.2hvyYc3XqcR_RxL7.ogkNqcdv9S8eyxSlh-AO51gVpG5SXg_QQyiAifOUA4C485PHb-h1FOmckuLMKAzKwmOKtI-qI_CHum6t5JI9oWqqtCPlDmnuhMGX_4vSho1g7M1vqjHgv-BeqjMMy-Glid4xUdX7UwfMXwMvyXHd1vlyGcsoII0KGYk8jiGWONiKyxIl.QdJJ7AJetxUaa382oL0oxQ");
        try {
            String resData = HttpClientUtil.httpPost(url, new HashMap<>(), header);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                JSONObject data = jo.getJSONObject("data");
                return data;
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    //巡查按行政区划统计
    @Override
    public JSONArray patrolChaosHappStaticByAreaCode() {
        String url = "http://117.149.228.59/mgt/happening/patrolChaosHappStaticByAreaCode";
        Map<String, String> header = new HashMap<>();
        header.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.Kva-STrmOYZUF2e45RHXuRUTor4zVwlEpNALxGNa6tctkNF2d6CXTLqg6KTMzypuwXob7HOMI4AfgSZaJImYee8hU44TykjUPM52ytlmL8OrwCa9H1XBjUKI0U4xM8lJeOjriVfwcHgjKzxJHvGddS330ipjnaZ7KGEiHBXTQyU.2hvyYc3XqcR_RxL7.ogkNqcdv9S8eyxSlh-AO51gVpG5SXg_QQyiAifOUA4C485PHb-h1FOmckuLMKAzKwmOKtI-qI_CHum6t5JI9oWqqtCPlDmnuhMGX_4vSho1g7M1vqjHgv-BeqjMMy-Glid4xUdX7UwfMXwMvyXHd1vlyGcsoII0KGYk8jiGWONiKyxIl.QdJJ7AJetxUaa382oL0oxQ");
        try {
            String resData = HttpClientUtil.httpPost(url, new HashMap<>(), header);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                return jo.getJSONArray("data");
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    //巡查乱点分析统计
    @Override
    public JSONArray patrolChaosHappStaticByChaosType() {
        String url = "http://117.149.228.59/mgt/happening/patrolChaosHappStaticByChaosType";
        Map<String, String> header = new HashMap<>();
        header.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.Kva-STrmOYZUF2e45RHXuRUTor4zVwlEpNALxGNa6tctkNF2d6CXTLqg6KTMzypuwXob7HOMI4AfgSZaJImYee8hU44TykjUPM52ytlmL8OrwCa9H1XBjUKI0U4xM8lJeOjriVfwcHgjKzxJHvGddS330ipjnaZ7KGEiHBXTQyU.2hvyYc3XqcR_RxL7.ogkNqcdv9S8eyxSlh-AO51gVpG5SXg_QQyiAifOUA4C485PHb-h1FOmckuLMKAzKwmOKtI-qI_CHum6t5JI9oWqqtCPlDmnuhMGX_4vSho1g7M1vqjHgv-BeqjMMy-Glid4xUdX7UwfMXwMvyXHd1vlyGcsoII0KGYk8jiGWONiKyxIl.QdJJ7AJetxUaa382oL0oxQ");
        try {
            String resData = HttpClientUtil.httpPost(url, new HashMap<>(), header);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                return jo.getJSONArray("data");
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    //巡查处置状态统计
    @Override
    public JSONArray patrolChaosHappStaticByChaosStatus() {
        String url = "http://117.149.228.59/mgt/happening/patrolChaosHappStaticByChaosStatus";
        Map<String, String> header = new HashMap<>();
        header.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.Kva-STrmOYZUF2e45RHXuRUTor4zVwlEpNALxGNa6tctkNF2d6CXTLqg6KTMzypuwXob7HOMI4AfgSZaJImYee8hU44TykjUPM52ytlmL8OrwCa9H1XBjUKI0U4xM8lJeOjriVfwcHgjKzxJHvGddS330ipjnaZ7KGEiHBXTQyU.2hvyYc3XqcR_RxL7.ogkNqcdv9S8eyxSlh-AO51gVpG5SXg_QQyiAifOUA4C485PHb-h1FOmckuLMKAzKwmOKtI-qI_CHum6t5JI9oWqqtCPlDmnuhMGX_4vSho1g7M1vqjHgv-BeqjMMy-Glid4xUdX7UwfMXwMvyXHd1vlyGcsoII0KGYk8jiGWONiKyxIl.QdJJ7AJetxUaa382oL0oxQ");
        try {
            String resData = HttpClientUtil.httpPost(url, new HashMap<>(), header);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                return jo.getJSONArray("data");
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public List<JSONObject> happeningListJson(Integer pageSize, Integer pageNum, String chaosType, String chaosName, String chaosStatus) {
        String url = "http://117.149.228.59/mgt/happening/listJson?fetchAll=true";
        JSONObject params = new JSONObject();
        params.put("chaosType", chaosType);
        params.put("chaosName", chaosName);
        params.put("chaosStatus", chaosStatus);
//        if (pageSize != null && pageSize > 0 && pageNum != null && pageNum > 0) {
//            params.put("rows",pageSize + "");
//            params.put("page",pageNum + "");
//        } else {
//            params.put("fetchAll","true");
//        }
        params.put("fetchAll", true);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("token", "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.kSvbs7Hx8CWBby0LQSaZxbJ9ZLAwh6s2r7lpbGxj-4mYAwtJYPG4TjX3jwXeFSXrtzOGAxW0r9y-AP-EHC6a1Owb78bVfYe4Mex3FV1pvprQ53JEoD5LR4funmU9hgRDgAfLI8Lgs-jlKAPwVrluUMxX5Jrs4BoVHYusE2jMyv8.NC4Mv03xf_Xu63Hm.3pBIYiGPtoLxfQcKsendvMHdhvIrI7wwp5_AEbhg_6v4wTv2edWTOteY1NfXkN8eqBzgh93BdOiX2x1sMh9oqp2BiTgGI-P6Ip5Y2LZBNKltWY7k9ilZBdqZUsltz4eltXNGo5YJn6ltj4bmAfKua2E3KJUIMi5k8Y0H.wq1t4wuyM3x4CCmOP8hJdw");
        try {
            String resData = HttpClientUtil.httpPost(url, params.toJSONString(), headers);
            JSONObject jo = JSONObject.parseObject(resData);
            Boolean success = jo.getBoolean("success");
            if (success) {
                JSONArray rows = jo.getJSONArray("rows");
                List<JSONObject> jsonObjects = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    jsonObjects.add(rows.getJSONObject(i));
                }
                if (StringUtils.isNotBlank(chaosType)) {
                    jsonObjects = jsonObjects.stream().filter(e -> e.getString("chaosType").contains(chaosType)).collect(Collectors.toList());
                }
                if (StringUtils.isNotBlank(chaosName)) {
                    jsonObjects = jsonObjects.stream().filter(e -> e.getString("chaosName").contains(chaosName)).collect(Collectors.toList());
                }
                if (StringUtils.isNotBlank(chaosStatus)) {
                    jsonObjects = jsonObjects.stream().filter(e -> e.getString("chaosStatus").contains(chaosStatus)).collect(Collectors.toList());
                }
                return jsonObjects;
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }
}
