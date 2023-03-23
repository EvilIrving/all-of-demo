package com.ygkj.river.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.AttSwhsBaseMapper;
import com.ygkj.river.model.AttAdcdBase;
import com.ygkj.river.model.AttSwhsBase;
import com.ygkj.river.model.AttSwhsSignageBase;
import com.ygkj.river.service.AttSwhsBaseService;
import com.ygkj.river.vo.request.AttSwhsBaseReqVo;
import com.ygkj.river.vo.response.AttSwhsBaseRespVo;
import com.ygkj.river.vo.response.StatisSwarnRespVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @description 水源地基础信息表
 * @date 2021-08-17
 */
@Service
public class AttSwhsBaseServiceImpl implements AttSwhsBaseService {

    @Resource
    private AttSwhsBaseMapper attSwhsBaseMapper;

    @Resource
    private AttAdcdBaseMaper adcdBaseMapper;

    Map<String, String> wainWasoTypeMap = Builder.of(HashMap<String, String>::new)
            .with(HashMap::put, "1", "水库")
            .with(HashMap::put, "2", "山塘")
            .with(HashMap::put, "3", "河流湖泊")
            .with(HashMap::put, "4", "溪沟堰坝")
            .with(HashMap::put, "5", "蓄水池")
            .with(HashMap::put, "6", "地下水")
            .with(HashMap::put, "7", "海水淡化")
            .with(HashMap::put, "8", "山泉水")
            .with(HashMap::put, "9", "其他")
            .build();

    Map<String, String> supplyScaleMap = Builder.of(HashMap<String, String>::new)
            .with(HashMap::put, "1", "县级以上")
            .with(HashMap::put, "2", "千吨万人")
            .with(HashMap::put, "3", "200吨-1000吨")
            .with(HashMap::put, "4", "200吨以下")
            .build();

    @Override
    public int insert(AttSwhsBaseReqVo vo) {
        AttSwhsBase attSwhsBase = new AttSwhsBase();
        BeanUtils.copyProperties(vo, attSwhsBase);
        attSwhsBase.setCreateTime(new Date());
        attSwhsBase.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return attSwhsBaseMapper.insert(attSwhsBase);
    }

    @Override
    public int delete(String swhsCode) {
        return attSwhsBaseMapper.delete(swhsCode);
    }

    @Override
    public int update(AttSwhsBaseReqVo vo) {
        AttSwhsBase attSwhsBase = new AttSwhsBase();
        BeanUtils.copyProperties(vo, attSwhsBase);
        attSwhsBase.setUpdateTime(new Date());
        attSwhsBase.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return attSwhsBaseMapper.update(attSwhsBase);
    }

    @Override
    public PageVO<AttSwhsBaseRespVo> loadPage(AttSwhsBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        PageVO<AttSwhsBaseRespVo> result = PageUtils.getPageVO(attSwhsBaseMapper.loadPage(vo));
        List<AttAdcdBase> adcdList=adcdBaseMapper.countyOfWenZhou();
        Map<String,String> adcdMap=adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd,AttAdcdBase::getAdnm));
//        List<AttSwhsBaseRespVo> list = result.getList().stream()
//                .peek(AttSwhsBaseRespVo::computeLeftWaterSupply)
//                .peek(o -> o.setSwhsLocName(adcdMap.get(o.getSwhsLoc())))
//                .collect(Collectors.toList());
//        result.setList(list);
        List<AttSwhsBaseRespVo> list = result.getList();
        for (AttSwhsBaseRespVo swhs : list) {
            swhs.computeLeftWaterSupply();
            swhs.setSwhsLocName(adcdMap.get(swhs.getSwhsLoc()));
            swhs.setAreaName(adcdMap.get(swhs.getAreaCode()));
        }
        if (vo.isWithSignage()){
            List<AttSwhsSignageBase> singnageList= this.attSwhsBaseMapper.listSwhsSinageByCondition(list.stream().map(AttSwhsBaseRespVo::getSwhsCode).collect(Collectors.toList()));
            Map<String,List<AttSwhsSignageBase>> singnageMap=singnageList.stream().collect(Collectors.groupingBy(AttSwhsSignageBase::getSwhsCode));
            for (AttSwhsBaseRespVo swhs : list) {
                swhs.setSingageList(singnageMap.getOrDefault(swhs.getSwhsCode(),Collections.emptyList()));
            }
        }
        return result;
    }

    /**
     * 水源详表组件导出
     *
     * @param vo
     * @param response
     */
    @Override
    public void exportSwhsPageList(AttSwhsBaseReqVo vo, HttpServletResponse response) {
        vo.setPageNum(null);
        vo.setPageSize(null);
        Function<Object, Object> wainWasoTypeMapping = (o) -> {
            try {
                return wainWasoTypeMap.getOrDefault(o, StringUtils.EMPTY);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        };
        Function<Object, Object> supplyScaleMapping = (o) -> {
            try {
                return supplyScaleMap.getOrDefault(o, StringUtils.EMPTY);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        };
        Map<String, Function<Object, Object>> mappingMap = new HashMap<>(2);
        mappingMap.put("wainWasoType", wainWasoTypeMapping);
        mappingMap.put("supplyScale", supplyScaleMapping);
        ExportUtils.exportExcel(loadPage(vo).getList(), "水源地",
                new String[]{"水源地名称", "水源地位置", "类型", "规模",
                        "可供水量(万m³)", "取水量(万m³/月)", "取水月份", "本年累计取水量(万m³)", "数据更新时间"},
                new String[]{"swhsName", "adnm", "wainWasoType", "supplyScale",
                        "leftWaterSupply", "withdrawal", "waterMonth", "cumulatAnnWain", "updateTime"},
                response, null, mappingMap
        );
    }

    //规模 1：县级以上 2：千吨万人 3：200吨-1000吨 4：200吨以下
    //type类型 1：数量 2：人口
    @Override
    public List<JSONObject> statisSwhsNumByScale(String type) {
        List<JSONObject> list;
        if ("1".equals(type)) {
            list = attSwhsBaseMapper.statisSwhsNumByScale();
        } else {
            list = attSwhsBaseMapper.statisSwhsDesAnnWainByScale();
        }

        String[] scales = {"1", "2", "3", "4"};
        List<JSONObject> result = new ArrayList<>(scales.length);
        Map<String, JSONObject> map = list.stream().collect(Collectors.toMap(o -> o.getString("supplyScale"), Function.identity()));
        for (String scale : scales) {
            JSONObject temp = map.getOrDefault(scale, Builder.of(JSONObject::new)
                    .with(JSONObject::put, "supplyScale", scale)
                    .with(JSONObject::put, "num", 0)
                    .build());
            result.add(temp);
        }
        return result;
    }

    //取水水源类型 1:水库 2:山塘 3:河流湖泊 4:溪沟堰坝 5:蓄水池 6:地下水 7:海水淡化 8:山泉水 9:其他
    //type类型 1：数量 2：人口
    @Override
    public List<JSONObject> statisSwhsNumByType(String type) {
        List<JSONObject> list;
        if ("1".equals(type)) {
            list = attSwhsBaseMapper.statisSwhsNumByType();
        } else {
            list = attSwhsBaseMapper.statisSwhsDesAnnWainByType();
        }
        String[] types = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<JSONObject> result = new ArrayList<>(types.length);
        Map<String, JSONObject> map = list.stream().collect(Collectors.toMap(o -> o.getString("wainWasoType"), Function.identity()));
        for (String wainWasoType : types) {
            JSONObject temp = map.getOrDefault(wainWasoType, Builder.of(JSONObject::new)
                    .with(JSONObject::put, "wainWasoType", wainWasoType)
                    .with(JSONObject::put, "num", 0)
                    .build());
            result.add(temp);
        }
        return result;
    }

    //type类型 1：数量 2：人口
    @Override
    public List<JSONObject> statisSwhsNumByArea(String type) {
        List<JSONObject> list = null;
        if ("1".equals(type)) {
            list = attSwhsBaseMapper.statisSwhsNumByArea();
        } else {
            list = attSwhsBaseMapper.statisSwhsDesAnnWainByArea();
        }
        String[] areaNames = new String[]{"鹿城区", "龙湾区", "瓯海区", "洞头区", "瑞安市", "乐清市", "龙港市", "永嘉县", "平阳县", "苍南县"
                , "文成县", "泰顺县"};
        for (String areaName : areaNames) {
            if (list.stream().filter(e -> areaName.equals(e.getString("adnm"))).count() == 0) {
                JSONObject jo = new JSONObject();
                jo.put("adnm", areaName);
                jo.put("num", 0);
                list.add(jo);
            }
        }
        return list;
    }

    //1：数量 2：人口
    @Override
    public List<JSONObject> statisSwarnGradeNum(String type) {
        List<JSONObject> jsonObjects = attSwhsBaseMapper.statisSwarnGradeNum();
        if ("1".equals(type)) {
            Integer total = jsonObjects.stream().mapToInt(e -> e.getInteger("num")).sum();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("num", total);
            jsonObject.put("warnType", "总和");
            jsonObjects.add(jsonObject);
        } else {
            BigDecimal total = attSwhsBaseMapper.totaldesannwasupop();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("num", total.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());
            jsonObject.put("warnType", "总和");
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }

    @Override
    public List<JSONObject> statisSwarnGradeByAdcd(String type) {
        List<JSONObject> list = attSwhsBaseMapper.statisSwarnGradeByAdcd();
        String[] areaNames = new String[]{"鹿城区", "龙湾区", "瓯海区", "洞头区", "瑞安市", "乐清市", "龙港市", "永嘉县", "平阳县", "苍南县"
                , "文成县", "泰顺县"};
        for (String areaName : areaNames) {
            if (list.stream().filter(e -> areaName.equals(e.getString("adnm"))).count() == 0) {
                JSONObject jo = new JSONObject();
                jo.put("adnm", areaName);
                jo.put("num", 0);
                list.add(jo);
            }
        }
        return list;
    }

    @Override
    public StatisSwarnRespVo statisC() {
        List<JSONObject> jsonObjects = attSwhsBaseMapper.statisSwarnGradeNum();
        BigDecimal total = attSwhsBaseMapper.calDesAnnWasuPop();
        Integer redNum = 0;
        List<JSONObject> collect1 = jsonObjects.stream().filter(e -> "红色预警".equals(e.getString("warnType"))).collect(Collectors.toList());
        if (!collect1.isEmpty()) {
            redNum = collect1.get(0).getInteger("num");
        }
        Integer orangeNum = 0;
        List<JSONObject> collect2 = jsonObjects.stream().filter(e -> "橙色预警".equals(e.getString("warnType"))).collect(Collectors.toList());
        if (!collect2.isEmpty()) {
            orangeNum = collect2.get(0).getInteger("num");
        }
        Integer yellowNum = 0;
        List<JSONObject> collect3 = jsonObjects.stream().filter(e -> "黄色预警".equals(e.getString("warnType"))).collect(Collectors.toList());
        if (!collect3.isEmpty()) {
            yellowNum = collect3.get(0).getInteger("num");
        }
        Integer noNum = 0;
        List<JSONObject> collect4 = jsonObjects.stream().filter(e -> "暂无风险".equals(e.getString("warnType"))).collect(Collectors.toList());
        if (!collect4.isEmpty()) {
            noNum = collect4.get(0).getInteger("num");
        }
        return new StatisSwarnRespVo(total, noNum, redNum, orangeNum, yellowNum, noNum + redNum + orangeNum + yellowNum);
    }


    @Override
    public List<JSONObject> waterSuantityStatistic(AttSwhsBaseReqVo vo) {
        //统计类型 1：水源地 2：水量预警
        if ("1".equals(vo.getStatisticalType())) {
            List<JSONObject> list = attSwhsBaseMapper.waterSuantityStatisticNum(vo);
            String[] areaNames = new String[]{"县级以上", "千吨万人", "200吨-1000吨", "200吨以下"};
            for (String areaName : areaNames) {
                if (list.stream().filter(e -> areaName.equals(e.getString("supplyScale"))).count() == 0) {
                    JSONObject jo = new JSONObject();
                    jo.put("supplyScale", areaName);
                    jo.put("num", 0);
                    list.add(jo);
                }
            }
            long total = list.stream().mapToInt(e -> e.getInteger("num")).sum();
            JSONObject jo = new JSONObject();
            jo.put("num", total);
            jo.put("supplyScale", "全部");
            list.add(jo);
            return list;
        } else {
            List<JSONObject> list = attSwhsBaseMapper.waterSuantityStatisticWarn(vo);

            String[] areaNames = new String[]{"暂无风险", "黄色预警", "橙色预警", "红色预警"};
            for (String areaName : areaNames) {
                if (list.stream().filter(e -> areaName.equals(e.getString("warnType"))).count() == 0) {
                    JSONObject jo = new JSONObject();
                    jo.put("warnType", areaName);
                    jo.put("num", 0);
                    list.add(jo);
                }
            }

            long total = list.stream().mapToInt(e -> e.getInteger("num")).sum();
            JSONObject jo = new JSONObject();
            jo.put("num", total);
            jo.put("warnType", "全部");
            list.add(jo);
            return list;
        }
    }
}
