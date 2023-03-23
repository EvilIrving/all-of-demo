package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.feign.CommonClient;
import com.ygkj.digitization.mapper.ComprehensiveAnalysisMapper;
import com.ygkj.digitization.mapper.BusinessMapper;
import com.ygkj.digitization.model.RaderData;
import com.ygkj.digitization.model.ReservoirCapacityCurve;
import com.ygkj.digitization.service.ComprehensiveAnalysisService;
import com.ygkj.digitization.vo.request.AnalysisListReqVo;
import com.ygkj.digitization.vo.request.DesignReqVo;
import com.ygkj.digitization.vo.response.DesignSoftResVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service("comprehensiveAnalysisService")
public class ComprehensiveAnalysisServiceImpl implements ComprehensiveAnalysisService {

    private static final String rsvrName = "st_rsvr_r_";
    private static final String tideName = "st_tide_r_";

    @Resource
    private ComprehensiveAnalysisMapper comprehensiveAnalysisMapper;

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private CommonClient commonClient;

    /**
     * 水库安全分析
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ResSoftResVo> resSoftAnalysis(AnalysisListReqVo reqVo) {
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        //获取水库的一些基本信息
        List<ResSoftResVo> list = comprehensiveAnalysisMapper.listResSoftAnalysis(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> stCodeSet = list.stream().map(ResSoftResVo::getStCode).collect(Collectors.toSet());

            //获取水库的当前水位
            String tableName = getTableName(rsvrName);
            List<ResSoftResVo> rzList = businessMapper.lastRz(tableName, stCodeSet);
            Map<String, Double> rzMap = rzList.stream().collect(Collectors.toMap(ResSoftResVo::getStCode, ResSoftResVo::getRz));

            Map<String, BigDecimal> krMap = getCurrentCap(rzList);

            //计算纳蓄能力
            list.forEach(resSoftResVo -> {
                String stCode = resSoftResVo.getStCode();
                Double rz = rzMap.get(stCode);
                BigDecimal kr = krMap.get(stCode);

                resSoftResVo.setRz(rz);
                if(kr != null){
                    resSoftResVo.setCurrentCap(kr.doubleValue());
                    //纳蓄能力
                    computeStorageCap(resSoftResVo, kr.doubleValue());
                }
            });
        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 实时库容
     * @param rzList
     * @return
     */
    private Map<String, BigDecimal> getCurrentCap(List<ResSoftResVo> rzList) {
        Map<String, BigDecimal> result = new HashMap<>();
        List<ReservoirCapacityCurve> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(rzList)){
            rzList.forEach(resSoftResVo -> {
                ReservoirCapacityCurve reservoirCapacityCurve = new ReservoirCapacityCurve();
                list.add(reservoirCapacityCurve);
                reservoirCapacityCurve.setCode(resSoftResVo.getStCode());
                reservoirCapacityCurve.setWaterLevel(new BigDecimal(resSoftResVo.getRz()));
            });

            CommonResult<Map<String, BigDecimal>> mapCommonResult = commonClient.calculationCapacity(list);
            result = mapCommonResult.getData();
        }
        return result;
    }

    private String getTableName(String prefix) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        String format = dtf.format(now);
        return prefix + format;
    }

    /**
     * 计算纳蓄能力
     *
     * @param resSoftResVo
     * @param kr
     */
    private void computeStorageCap(ResSoftResVo resSoftResVo, Double kr) {
        BigDecimal totCap = resSoftResVo.getTotCap();
        BigDecimal watShedArea = resSoftResVo.getWatShedArea();
        if (totCap != null && watShedArea != null && kr != null) {
            resSoftResVo.setStorageCap((totCap.subtract(new BigDecimal(kr))).divide(watShedArea.multiply(new BigDecimal(0.1)), 1, RoundingMode.HALF_UP));
        }
    }

    /**
     * 海塘安全分析
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<SeawallSoftResVo> seawallSoftAnalysis(AnalysisListReqVo reqVo) {
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        //获取海塘基本信息
        List<SeawallSoftResVo> list = comprehensiveAnalysisMapper.listSeawallSoftAnalysis(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            Set<String> set = list.stream().map(SeawallSoftResVo::getStCode).collect(Collectors.toSet());
            //TODO 获取实时潮位
            String tableName = getTableName(tideName);
            List<SeawallSoftResVo> tideList = businessMapper.lastTide(tableName, set);
            if (!CollectionUtils.isEmpty(tideList)) {
                Map<String, BigDecimal> tideMap = new HashMap<>();
                //计算最低点与潮位高差
                list.forEach(seawallSoftResVo -> {
                    String stCode = seawallSoftResVo.getStCode();
                    if (!StringUtils.isEmpty(stCode) && tideMap.containsKey(stCode)) {
                        BigDecimal tide = tideMap.get(stCode);
                        BigDecimal min = null;
                        if (seawallSoftResVo.getStartCrestAltitude() == null) {
                            min = seawallSoftResVo.getFinalCrestAltitude();
                        }
                        if (seawallSoftResVo.getFinalCrestAltitude() == null) {
                            min = seawallSoftResVo.getStartCrestAltitude();
                        }
                        if (min != null && tide != null) {
                            seawallSoftResVo.setDifTide(min.subtract(tide));
                        }
                    }
                });
            }
        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 设计安全分析
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<DesignSoftResVo> designSoftAnalysis(DesignReqVo reqVo) {
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        return PageUtils.getPageVO(comprehensiveAnalysisMapper.designSoftAnalysis(reqVo));
    }

    @Override
    public JSONArray wenzhouCloudData() {
        String url = "https://img.istrongcloud.com/data/modelimagejson/cloud/h8_transparent.json?v=" + System.currentTimeMillis();
        try {
            String str = HttpClientUtil.httpGet(url);
            return JSON.parseArray(str);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<RaderData> wenzhouRadarData() {
//        String url = "http://data.istrongcloud.com/data/images/radar/mingle/caiyun_transparent.json?v=" + System.currentTimeMillis();
//        try {
//            String str = HttpClientUtil.httpGet(url);
//            return JSON.parseArray(str);
//        } catch (Exception e) {
//            return null;
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String endTime = format.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,-2);
        String startTime = format.format(calendar.getTime());
        return businessMapper.raderData(startTime,endTime);
    }

}
