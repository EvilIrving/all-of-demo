package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.enums.WaterQualityLevelEnum;
import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.mapper.AttRivQuaStBaseMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.model.AttRivQuaStBase;
import com.ygkj.gragh.service.WaterQualityV2Service;
import com.ygkj.gragh.vo.request.RivQuaReqVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.RivQuaResVo;
import com.ygkj.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
@Service
public class WaterQualityV2ServiceImpl implements WaterQualityV2Service {

    @Resource
    AttRivQuaStBaseMapper rivQuaStBaseMapper;

    @Resource
    AttAdcdBaseMapper attAdcdBaseMapper;

    Map<Integer, String> map = Builder.of(HashMap<Integer, String>::new)
            .with(HashMap::put, 1, "国控")
            .with(HashMap::put, 2, "省控")
            .with(HashMap::put, 3, "市控")
            .with(HashMap::put, 4, "县控")
            .with(HashMap::put, 5, "乡镇控")
            .build();

    /**
     * 查询最新的水质监测列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<RivQuaResVo> list(RivQuaReqVo reqVo) {
        if (StringUtils.isNotBlank(reqVo.getAreaCode()) && !StringUtils.startsWith(reqVo.getAreaCode(), "3303")) {
            reqVo.setAreaCode(attAdcdBaseMapper.selectWzAdcdByAdnm(reqVo.getAreaCode()));
        }
        List<AttRivQuaStBase> stations = rivQuaStBaseMapper.selectByCondition(reqVo);
        if (CollectionUtils.isBlank(stations)) {
            return Collections.emptyList();
        }
        Map<String, String> adcdMap = adcdCodeNameMap(stations.stream().map(AttRivQuaStBase::getAreaCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()));
        List<RivQuaResVo> values = rivQuaStBaseMapper.selectNewestValueBySiteName(null, stations.stream().map(AttRivQuaStBase::getSiteName).collect(Collectors.joining(",")));
        List<RivQuaResVo> result = new ArrayList<>(stations.size());
        if (CollectionUtils.isNotBlank(values)) {
            Map<String, RivQuaResVo> map = values.stream().collect(Collectors.toMap(RivQuaResVo::getSiteName, Function.identity()));
            for (AttRivQuaStBase station : stations) {
                RivQuaResVo temp = map.get(station.getSiteName());
                if (temp == null) {
                    temp = new RivQuaResVo();
                }
                BeanUtils.copyProperties(station, temp);
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
                temp.setQualityLevel(computeQualityLevel(temp).type());
                temp.buildCoordinate();
                result.add(temp);
            }
        }
        if (reqVo.getQualityLevel() != null) {
            result = result.stream().filter(o -> reqVo.getQualityLevel().equals(o.getQualityLevel())).collect(Collectors.toList());
        }
        computeWorseOrBetter(result);
        return result;
    }

    private void computeWorseOrBetter(List<RivQuaResVo> list) {
        if (CollectionUtils.isBlank(list)) {
            return;
        }
        List<RivQuaResVo> recentValues = rivQuaStBaseMapper.selectRecentMonthValueBySiteName(list.stream().map(RivQuaResVo::getSiteName).collect(Collectors.joining(",")), 2);
        recentValues = recentValues == null ? Collections.emptyList() : recentValues;
        Map<String, List<RivQuaResVo>> recentGroupMap = recentValues.stream().collect(Collectors.groupingBy(RivQuaResVo::getSiteName));
        for (RivQuaResVo rivQuaResVo : list) {
            List<RivQuaResVo> tempList = recentGroupMap.getOrDefault(rivQuaResVo.getSiteName(), Collections.emptyList());
            if (tempList.size() < 2) {
                continue;
            }
            // 按时间降序
            tempList.sort(Comparator.comparing(RivQuaResVo::getTm).reversed());
            for (int i = 0; i < tempList.size(); i++) {
                RivQuaResVo tempVal = tempList.get(i);
                if (tempVal.getTm().equals(rivQuaResVo.getTm()) && (i + 1 < tempList.size())) {
                    RivQuaResVo lastVal = list.get(i);
                    rivQuaResVo.computeQualityWorse(lastVal);
                    break;
                }
            }
        }
    }


    Map<String, String> adcdCodeNameMap(List<String> adcdList) {
        if (CollectionUtils.isBlank(adcdList)) {
            return Collections.emptyMap();
        }
        List<AttAdcdBase> list = this.attAdcdBaseMapper.selectAdnmByAdcdList(adcdList);
        list = list == null ? Collections.emptyList() : list;
        return list.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    /**
     * 查询最新的水质监测列表-表头统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4List(RivQuaReqVo reqVo) {
        List<RivQuaResVo> records = list(reqVo);
        JSONObject result = new JSONObject();
        Map<Integer, Long> recordMap = records.stream().filter(o -> o.getQualityLevel() != null)
                .collect(Collectors.groupingBy(RivQuaResVo::getQualityLevel, Collectors.counting()));
        WaterQualityLevelEnum[] enums = WaterQualityLevelEnum.values();
        for (WaterQualityLevelEnum tempEnum : enums) {
            result.put(tempEnum.getDesc(), recordMap.getOrDefault(tempEnum.type(), 0L));
        }
        return result;
    }

    /**
     * 导出最新的水质监测列表
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportList(RivQuaReqVo reqVo, HttpServletResponse response) {
        List<RivQuaResVo> records = list(reqVo);
        Function<Object, Object> qualityLevelMapping = (o) -> {
            try {
                WaterQualityLevelEnum qualityLevel = WaterQualityLevelEnum.valueOfType((Integer) o);
                if (qualityLevel != null) {
                    return qualityLevel.getDesc();
                } else {
                    return com.ygkj.utils.StringUtils.EMPTY;
                }
            } catch (Exception e) {
                return com.ygkj.utils.StringUtils.EMPTY;
            }

        };
        ExportUtils.exportExcel(records, "水质监控",
                new String[]{"站点名称", "更新时间", "高锰酸盐(mg/L)", "氨氮(mg/L)", "总磷(mg/L)", "溶解氧", "水质等级"},
                new String[]{"siteName", "tm", "codmn", "nh3N", "tp", "dO", "qualityLevel"}, response, null, Collections.singletonMap("qualityLevel", qualityLevelMapping));


    }

    /**
     * 查询水质监测
     *
     * @param siteNo
     * @param month
     * @return
     */
    @Override
    public List<RivQuaResVo> qualityCurve(String siteNo, int month) {
        AttRivQuaStBase station = this.rivQuaStBaseMapper.load(siteNo);
        if (station == null) {
            return Collections.emptyList();
        }
        if (month < 0) {
            month = 12;
        }
        LocalDate now = LocalDate.now();
        Date end = DateUtils.localDate2Date(now);
        LocalDate ago = now.minusMonths(month).withDayOfMonth(1);
        Date start = DateUtils.localDate2Date(ago);
        List<RivQuaResVo> values = this.rivQuaStBaseMapper.selectValueByTimeInterval(station.getSiteName(), start, end);
//        if (CollectionUtils.isNotBlank(values)) {
//            for (RivQuaResVo temp : values) {
//                temp.setLatitude(station.getLatitude());
//                temp.setLongitude(station.getLongitude());
//                temp.setName(station.getName());
//                temp.setAdcd(station.getAdcd());
//                temp.setPmLimit(station.getPmLimit());
//            }
//        }
        return values;
    }

    /**
     * @param type 1. 根据行政区划统计，2. 根据测站级别统计，3，根据水质类别统计
     * @return
     */
    @Override
    public List<ChartResVo<String, Long>> qualityStats(int type) {
        switch (type) {
            case 1: {
                List<AttAdcdBase> adcdList = attAdcdBaseMapper.selectWenZhouCounty();
                List<ChartResVo<String, Long>> records = this.rivQuaStBaseMapper.countByAreaCode();
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, adcd.getAdnm())
                            .with(ChartResVo::setValue, recordMap.getOrDefault(adcd.getAdcd(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 2: {
                List<ChartResVo<String, Long>> records = this.rivQuaStBaseMapper.countByLevel();
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
                map.forEach((level, name) -> {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, name)
                            .with(ChartResVo::setValue, recordMap.getOrDefault(level.toString(), 0L))
                            .build());
                });
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 3: {
                List<RivQuaResVo> records = list(new RivQuaReqVo());
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<Integer, Long> recordMap = records.stream().filter(o -> o.getQualityLevel() != null)
                        .collect(Collectors.groupingBy(RivQuaResVo::getQualityLevel, Collectors.counting()));
                WaterQualityLevelEnum[] enums = WaterQualityLevelEnum.values();
                for (WaterQualityLevelEnum tempEnum : enums) {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, tempEnum.getDesc())
                            .with(ChartResVo::setValue, recordMap.getOrDefault(tempEnum.type(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            default:
                return Collections.emptyList();
        }
    }


    public WaterQualityLevelEnum computeQualityLevel(RivQuaResVo rivQua) {
        List<WaterQualityLevelEnum> tempQuaList = new ArrayList<>();
        OptionalString.ofBlank(rivQua.getCodmn()).ifNullPresent(o -> tempQuaList.add(computeCodmnQualityLevel(Double.valueOf((String) o))));
        OptionalString.ofBlank(rivQua.getNh3N()).ifNullPresent(o -> tempQuaList.add(computeNh3NQualityLevel(Double.valueOf((String) o))));
        OptionalString.ofBlank(rivQua.getTp()).ifNullPresent(o -> tempQuaList.add(computeTpQualityLevel(Double.valueOf((String) o))));
        OptionalString.ofBlank(rivQua.getDO()).ifNullPresent(o -> tempQuaList.add(computeDOQualityLevel(Double.valueOf((String) o))));
        Optional<WaterQualityLevelEnum> optional = tempQuaList.stream().max(Comparator.comparing(WaterQualityLevelEnum::type));
        return optional.orElse(WaterQualityLevelEnum.CLASS_1);
    }

    /**
     * 计算高锰酸盐水质等级
     *
     * @param codmn 高锰酸盐指数(mg/L)
     * @return
     */
    public WaterQualityLevelEnum computeCodmnQualityLevel(Double codmn) {
        if (codmn == null) {
            return WaterQualityLevelEnum.CLASS_1;
        }
        if (codmn <= 2) {
            return WaterQualityLevelEnum.CLASS_1;
        } else if (codmn > 2 && codmn <= 4) {
            return WaterQualityLevelEnum.CLASS_2;
        } else if (codmn > 4 && codmn <= 6) {
            return WaterQualityLevelEnum.CLASS_3;
        } else if (codmn > 6 && codmn <= 10) {
            return WaterQualityLevelEnum.CLASS_4;
        } else if (codmn > 10 && codmn <= 15) {
            return WaterQualityLevelEnum.CLASS_5;
        } else {
            return WaterQualityLevelEnum.CLASS_6;
        }
    }

    /**
     * 计算氨氮水质等级
     *
     * @param nh3N 氨氮(mg/L)
     * @return
     */
    public WaterQualityLevelEnum computeNh3NQualityLevel(Double nh3N) {
        if (nh3N == null) {
            return WaterQualityLevelEnum.CLASS_1;
        }
        if (nh3N <= 0.15D) {
            return WaterQualityLevelEnum.CLASS_1;
        } else if (nh3N > 0.15D && nh3N <= 0.5D) {
            return WaterQualityLevelEnum.CLASS_2;
        } else if (nh3N > 0.5D && nh3N <= 1D) {
            return WaterQualityLevelEnum.CLASS_3;
        } else if (nh3N > 1D && nh3N <= 1.5D) {
            return WaterQualityLevelEnum.CLASS_4;
        } else if (nh3N > 1.5D && nh3N <= 2D) {
            return WaterQualityLevelEnum.CLASS_5;
        } else {
            return WaterQualityLevelEnum.CLASS_6;
        }
    }

    /**
     * 计算总磷水质等级
     *
     * @param tp 总磷(mg/L)
     * @return
     */
    public WaterQualityLevelEnum computeTpQualityLevel(Double tp) {
        if (tp == null) {
            return WaterQualityLevelEnum.CLASS_1;
        }
        if (tp <= 0.02D) {
            return WaterQualityLevelEnum.CLASS_1;
        } else if (tp > 0.02D && tp <= 0.1D) {
            return WaterQualityLevelEnum.CLASS_2;
        } else if (tp > 0.1D && tp <= 0.2D) {
            return WaterQualityLevelEnum.CLASS_3;
        } else if (tp > 0.2D && tp <= 0.3D) {
            return WaterQualityLevelEnum.CLASS_4;
        } else if (tp > 0.3D && tp <= 0.4D) {
            return WaterQualityLevelEnum.CLASS_5;
        } else {
            return WaterQualityLevelEnum.CLASS_6;
        }
    }

    /**
     * 计算溶解氧等级
     *
     * @param dO 溶解氧(mg/L)
     * @return
     */
    public WaterQualityLevelEnum computeDOQualityLevel(Double dO) {
        if (dO == null) {
            return WaterQualityLevelEnum.CLASS_1;
        }
        if (dO >= 7.5) {
            return WaterQualityLevelEnum.CLASS_1;
        } else if (dO > 6 && dO <= 7.5) {
            return WaterQualityLevelEnum.CLASS_2;
        } else if (dO > 5 && dO <= 6) {
            return WaterQualityLevelEnum.CLASS_3;
        } else if (dO > 3 && dO <= 5) {
            return WaterQualityLevelEnum.CLASS_4;
        } else if (dO > 2 && dO <= 3) {
            return WaterQualityLevelEnum.CLASS_5;
        } else {
            return WaterQualityLevelEnum.CLASS_6;
        }
    }

    /**
     * 水质监测组件C位
     *
     * @return
     */
    @Override
    public JSONObject qualityUnitCWindow() {
        JSONObject result = new JSONObject();
        // 合格率，就是3类及以上水质站数量占比
        BigDecimal percentOfPass = BigDecimal.ZERO;
        List<RivQuaResVo> list = list(new RivQuaReqVo());
        if (CollectionUtils.isNotBlank(list)) {
            long goodNum = list.stream().filter(o -> o.getQualityLevel() <= 3).count();
            percentOfPass = BigDecimal.valueOf(goodNum * 100 / list.size()).setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        // 本月恶化数量
        // 本月最新值和上月最后的值对比

        List<RivQuaResVo> values = rivQuaStBaseMapper.selectNewestValueBySiteName(
                DateUtils.localDateTime2Date(LocalDateTime.now()
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)
                        .minusSeconds(1)// 上个月最后一个时刻
                ),
                null);
        values = values == null ? Collections.emptyList() : values;
        Map<String, RivQuaResVo> map = values.stream().collect(Collectors.toMap(RivQuaResVo::getSiteName, Function.identity()));
        for (RivQuaResVo value : values) {
            value.computeQualityWorse(map.get(value.getSiteName()));
        }
        long thisMonthWorseNum = values.stream().filter(RivQuaResVo::isQualityWorse).count();

        result.put("percentOfPass", percentOfPass);
        result.put("thisMonthWorseNum", thisMonthWorseNum);
        return result;
    }
}
