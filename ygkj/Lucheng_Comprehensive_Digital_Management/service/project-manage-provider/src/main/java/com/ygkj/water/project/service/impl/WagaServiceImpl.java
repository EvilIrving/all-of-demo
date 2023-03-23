package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.AttWagaBase;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.vo.request.ProjectManageQueryVo;
import com.ygkj.project.vo.request.WagaReplayReqVo;
import com.ygkj.project.vo.request.WagaStationWaterReqVo;
import com.ygkj.project.vo.request.WrpWagaQueryReqVo;
import com.ygkj.project.vo.response.RsvrReplayResVo;
import com.ygkj.project.vo.response.WagaReplayResVo;
import com.ygkj.project.vo.response.WagaStationWaterResVo;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import com.ygkj.water.project.service.AdcdBaseService;
import com.ygkj.water.project.service.WagaService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/9/15
 */
@Service
public class WagaServiceImpl implements WagaService, InitializingBean {

    private DateTimeFormatter yyyyMM;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private AdcdBaseService adcdBaseService;

    @Resource
    private StationMapper stationMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Override
    public void afterPropertiesSet() {
        yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    }

    /**
     * 水闸及其测站及其水位数据
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO wagaWaterLevel(WagaStationWaterReqVo reqVo) {
        ProjectManageQueryVo queryVo = new ProjectManageQueryVo();
        if (StringUtils.isNotBlank(reqVo.getWagaCodes())) {
            queryVo.setCodes(Arrays.asList(reqVo.getWagaCodes().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            queryVo.setLevels(Arrays.stream(reqVo.getEngScales().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER))
                    .map(o -> Optional.ofNullable(EngScalEnum.getByDesc(o)))
                    .filter(Optional::isPresent)
                    .map(o -> o.get().getEngScal())
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        queryVo.setBas(reqVo.getWagaBas());
        queryVo.setAreaCode(reqVo.getWagaAdcd());
        queryVo.setWrpWaga(reqVo.getWrpWaga());
        queryVo.setProjectName(reqVo.getWagaName());
        // 实时数据不分页
        if (reqVo.isPaged() && (reqVo.getFilterOpenGate() == null || reqVo.getFilterOpenGate())) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        PageVO wagaPageVo = PageUtils.getPageVO(this.projectMapper.selectWagaByCondition(queryVo));
        List<AttWagaBase> wagaList = wagaPageVo.getList();
        if (CollectionUtils.isEmpty(wagaList)) {
            return wagaPageVo;
        }
        List<WagaStationWaterResVo> result = fillWater2Waga(wagaList, reqVo);
        // 开始统计列表中24小时泄流量
        Date dataTime = reqVo.getDataTime();
        Integer hours = reqVo.getHours();
        if (hours != null && hours > 0) {
            dataTime = dataTime == null ? new Date() : dataTime;
            Date start = new Date(dataTime.getTime() - hours * 3600 * 1000);
            // 查询列表工程关联测站的历史测站曲线。
            List<StWasR> staData = this.waterAndRainMapper.candidateSluiceWaterLevelWithTimeInternal(echoWaterRainTableByDateInternal(StationType.GATE.table(), start, dataTime),
                    result.stream().map(WagaStationWaterResVo::getStCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()),
                    start, dataTime);
            Map<String, List<StWasR>> staMap = staData.stream().collect(Collectors.groupingBy(StWasR::getMgstcd));

            for (WagaStationWaterResVo temp : result) {
                List<StWasR> tempSta = staMap.get(temp.getStCode());
                double drainage = wagaTotalDrainage(tempSta, dataTime);
                temp.setDrainage(Double.valueOf(String.format("%.1f", drainage)));
            }
        }
        for (WagaStationWaterResVo temp : result) {
            Optional.ofNullable(EngScalEnum.getByLevel(temp.getEngScal())).ifPresent(o1 -> temp.setEngScal(o1.getDesc()));
            //为避免开度值很小时，显示开度值为0注释掉
//            temp.setOvs(keepOvs2Scale(temp.getOvs()));
        }
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<WagaStationWaterResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            result = result.stream().sorted(comparator).collect(Collectors.toList());
        }
        wagaPageVo.setList(result);
        return wagaPageVo;
    }

    final static String comma = ",";

    public String keepOvs2Scale(String ovs) {
        if (StringUtils.isBlank(ovs)) {
            return ovs;
        }
        if (ovs.contains(comma)) {
            String[] array = ovs.split(comma);
            String[] target = new String[array.length];
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                try {
                    target[i] = new BigDecimal(s).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                } catch (Exception e) {
                    target[i] = s;
                }
            }
            return String.join(comma, target);
        } else if (NumberUtils.isCreatable(ovs)) {
            try {
                return new BigDecimal(ovs).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            } catch (Exception e) {
                return ovs;
            }
        } else {
            return ovs;
        }
    }


    /**
     * 为水闸填充水位信息
     *
     * @param wagaList
     * @param reqVo
     * @return
     */
    @Override
    public List<WagaStationWaterResVo> fillWater2Waga(List<AttWagaBase> wagaList, WagaStationWaterReqVo reqVo) {
        List<WagaStationWaterResVo> result = new ArrayList<>(wagaList.size());
        List<WagaStationWaterResVo> hasMainStcdResult = new ArrayList<>(wagaList.size());
        List<WagaStationWaterResVo> withoutMainStcdResult = new ArrayList<>(wagaList.size());
        List<String> adcdList = new ArrayList<>(wagaList.size());
        // 行政区划编码 不为空 的才填充 行政区划名称
        adcdList.addAll(wagaList.stream().filter(o -> StringUtils.isNotBlank(o.getAreaCode())).map(AttWagaBase::getAreaCode).distinct().collect(Collectors.toList()));
        Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
        for (AttWagaBase attWagaBase : wagaList) {
            WagaStationWaterResVo temp = new WagaStationWaterResVo();
            BeanUtils.copyProperties(attWagaBase, temp);
            if (StringUtils.isNotBlank(temp.getAreaCode())) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
            }
            if (StringUtils.isNotBlank(temp.getMainStcd())) {
                hasMainStcdResult.add(temp);
            } else {
                withoutMainStcdResult.add(temp);
            }
//            withoutMainStcdResult.addAll(result);
            result.add(temp);
        }
        List<String> sixstCodes=Arrays.asList("2021101101","2021101102","2021101103","2021101104","2021101105","2021101106");
        // 没有主测站编码的水闸，根据去测站表里根据关联的工程，查询测站和实时工情
        if (!CollectionUtils.isEmpty(withoutMainStcdResult)) {
            String projCode = withoutMainStcdResult.stream()
                    .filter(o -> o != null && StringUtils.isNotBlank(o.getWagaCode()))
                    .map(AttWagaBase::getWagaCode)
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));

            // 根据工程编码查询所有的测站
            List<AttStBase> stationList = this.stationMapper.selectStationByProjCode(projCode, StationType.GATE.type());
            stationList = stationList == null ? Collections.emptyList() : stationList;
            Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
            Map<String, List<String>> projStcdMap = stationList.stream()
                    .collect(Collectors.groupingBy(AttStBase::getProjCode,
                            Collectors.mapping(AttStBase::getStCode,
                                    Collectors.toList())));
            List<String> stcds = new ArrayList<>(stationMap.keySet());
            if (!CollectionUtils.isEmpty(stcds)) {
                List<StWasR> waterLevel = Collections.emptyList();
                if (reqVo.getDataTime() == null) {
                    // 查询最新的水位
                    LocalDateTime nowLdt = LocalDateTime.now();
                    waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + nowLdt.format(yyyyMM), stcds, null, 1);
                } else {
                    // 查询某个时刻左右的水位
                    waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + DateUtils.date2LocalDate(reqVo.getDataTime()).format(yyyyMM), stcds, reqVo.getDataTime(), 1);
                }
                Map<String, StWasR> waterLevelMap = waterLevel.stream().collect(Collectors.toMap(StWasR::getStcd, o -> o));

                for (WagaStationWaterResVo temp : withoutMainStcdResult) {
                    List<String> tempStcds = projStcdMap.get(temp.getWagaCode());
                    if (CollectionUtils.isEmpty(tempStcds)) {
                        continue;
                    }
                    StWasR finalWas = null;
                    // 拿到关联测站中最高的水位
                    for (String stcd : tempStcds) {
                        StWasR tempWas = waterLevelMap.get(stcd);
                        if (finalWas == null) {
                            finalWas = tempWas;
                        } else if (tempWas != null) {
                            finalWas = wasEvaluation(finalWas, tempWas);
                        }
                    }
                    if (finalWas != null) {
                        temp.setUpz(finalWas.getUpz());
                        temp.setDwz(finalWas.getDwz());
                        temp.setMxgtq(finalWas.getMxgtq());
                        if(finalWas.getOvs()!=null){
                            if(sixstCodes.contains(temp.getStCode())){
                                //开度值取 大于10 小于 1000 cm 的值
                                if (BigDecimal.valueOf(Double.valueOf(finalWas.getOvs())).compareTo(BigDecimal.valueOf(10)) >= 0
                                        && BigDecimal.valueOf(Double.valueOf(finalWas.getOvs())).compareTo(BigDecimal.valueOf(1000)) < 0){
                                    temp.setOvs(finalWas.getOvs());
                                }
                            }else {
                                temp.setOvs(finalWas.getOvs());
                            }
                        }
                        temp.setLl(finalWas.getLl());
                        temp.setDataTime(finalWas.getTm());
                        // 设置测站信息
                        AttStBase stBase = stationMap.get(finalWas.getMgstcd());
                        if (stBase == null) {
                            // 出现这情况说明数据断了
                            stBase = stationMap.get(tempStcds.get(0));
                        }
                        temp.setStCode(stBase.getStCode());
                        temp.setStType(stBase.getStType());
                        temp.setStLat(stBase.getStLat());
                        temp.setStLong(stBase.getStLong());
                        temp.setStName(stBase.getStName());
                        temp.setGateOpenThreshold(stBase.getGateOpenThreshold());
                        temp.setGateOpened(HydrologyUtils.isWagaOpenGate(temp.getOvs(), temp.getGateOpenThreshold()));
                    }
                }
            }
        }
        // 没有主测站编码的水闸，根据去测站表里根据关联的工程，查询测站和实时工情
        if (!CollectionUtils.isEmpty(hasMainStcdResult)) {
            List<String> stcds = hasMainStcdResult.stream()
                    .filter(o -> o != null && StringUtils.isNotBlank(o.getMainStcd()))
                    .map(AttWagaBase::getMainStcd)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stcds)) {
                // 根据工程编码查询所有的测站
                List<AttStBase> stationList = this.stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, stcds));
                stationList = stationList == null ? Collections.emptyList() : stationList;
                Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
                List<StWasR> waterLevel = Collections.emptyList();
                if (reqVo.getDataTime() == null) {
                    // 查询最新的工情
                    LocalDateTime nowLdt = LocalDateTime.now();
                    waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + nowLdt.format(yyyyMM), stcds, null, 1);
                } else {
                    // 查询某个时刻左右的工情
                    waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + DateUtils.date2LocalDate(reqVo.getDataTime()).format(yyyyMM), stcds, reqVo.getDataTime(), 1);
                }
                Map<String, StWasR> waterLevelMap = waterLevel.stream().collect(Collectors.toMap(StWasR::getStcd, o -> o));
                for (WagaStationWaterResVo temp : hasMainStcdResult) {
                    // 设置测站信息
                    AttStBase stBase = stationMap.get(temp.getMainStcd());
                    if (stBase != null) {
                        // 出现这情况说明数据断了
                        temp.setStCode(stBase.getStCode());
                        temp.setStType(stBase.getStType());
                        temp.setStLat(stBase.getStLat());
                        temp.setStLong(stBase.getStLong());
                        temp.setStName(stBase.getStName());
                        temp.setGateOpenThreshold(stBase.getGateOpenThreshold());
                    }
                    StWasR finalWas = waterLevelMap.get(temp.getMainStcd());
                    if (finalWas != null) {
                        temp.setUpz(finalWas.getUpz());
                        temp.setDwz(finalWas.getDwz());
                        temp.setMxgtq(finalWas.getMxgtq());
                        if(sixstCodes.contains(temp.getStCode())){
                            //开度值取 大于10 小于 1000 cm 的值
                            if (BigDecimal.valueOf(Double.valueOf(finalWas.getOvs())).compareTo(BigDecimal.valueOf(10)) >= 0
                                    && BigDecimal.valueOf(Double.valueOf(finalWas.getOvs())).compareTo(BigDecimal.valueOf(1000)) < 0){
                                temp.setOvs(finalWas.getOvs());
                            }
                        }else {
                            temp.setOvs(finalWas.getOvs());
                        }
                        temp.setLl(finalWas.getLl());
                        temp.setDataTime(finalWas.getTm());
                        temp.setGateOpened(HydrologyUtils.isWagaOpenGate(temp.getOvs(), temp.getGateOpenThreshold()));
                    }
                }
            }
        }

        return result;
    }

    /**
     * 评估同一个工程下面多个水闸测站的工情，选择信息最全的一个返回
     *
     * @param w1
     * @param w2
     * @return
     */
    private StWasR wasEvaluation(StWasR w1, StWasR w2) {
        if (w1.getUpz() == null || w2.getUpz() == null) {
            return w1.getUpz() == null ? w2 : w1;
        } else if (w1.getDwz() == null || w2.getDwz() == null) {
            return w1.getDwz() == null ? w2 : w1;
        } else if (StringUtils.isBlank(w1.getOvs()) || StringUtils.isBlank(w2.getOvs())) {
            return StringUtils.isBlank(w1.getOvs()) ? w2 : w1;
        } else if (StringUtils.isBlank(w1.getMxgtq()) || StringUtils.isBlank(w2.getMxgtq())) {
            return StringUtils.isBlank(w1.getMxgtq()) ? w2 : w1;
        } else {
            // 信息都全的选择闸上水位最高的返回
            return w1.getUpz().compareTo(w2.getUpz()) > 0 ? w1 : w2;
        }
    }


    /**
     * 温瑞平水闸复盘统计
     *
     * @return
     */
    @Override
    public JSONObject wagaReplayStats(WagaReplayReqVo reqVo) {
        Date start = reqVo.getStart();
        Date end = reqVo.getEnd();
        JSONObject result = new JSONObject();
        if (start == null || end == null) {
            return result;
        }

        ProjectManageQueryVo queryVo = new ProjectManageQueryVo();
        if (StringUtils.isNotBlank(reqVo.getWagaCodes())) {
            queryVo.setCodes(Arrays.asList(reqVo.getWagaCodes().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            queryVo.setLevels(Arrays.stream(reqVo.getEngScales().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER))
                    .map(o -> Optional.ofNullable(EngScalEnum.getByDesc(o)))
                    .filter(Optional::isPresent)
                    .map(o -> o.get().getEngScal())
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        queryVo.setBas(reqVo.getWagaBas());
        queryVo.setAreaCode(reqVo.getWagaAdcd());
        queryVo.setWrpWaga(reqVo.getWrpWaga());
        List<AttWagaBase> wagaList = this.projectMapper.selectWagaByCondition(queryVo);
        if (CollectionUtils.isEmpty(wagaList)) {
            return result;
        }

        String projCode = wagaList.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getWagaCode()))
                .map(AttWagaBase::getWagaCode)
                .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));

        // 根据工程编码查询所有的测站
        List<AttStBase> stationList = this.stationMapper.selectStationByProjCode(projCode, StationType.GATE.type());
        stationList = stationList == null ? Collections.emptyList() : stationList;
        Map<String, List<AttStBase>> projStaTempMap = stationList.stream()
                .collect(Collectors.groupingBy(AttStBase::getProjCode));
        Map<String, AttStBase> projStaMap = new HashMap<>(projStaTempMap.size());
        projStaTempMap.forEach((key, values) -> {
            if (!CollectionUtils.isEmpty(values)) {
                projStaMap.put(key, values.get(0));
            }
        });

        List<String> tables = echoWaterRainTableByDateInternal(StationType.GATE.table(), start, end);
        List<StWasR> waterLevelTempList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = waterAndRainMapper.candidateSluiceWaterLevelWithTimeInternal(tables,
                    projStaMap.values().stream().map(AttStBase::getStCode).collect(Collectors.toList()),
                    start,
                    end
            );
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;
        Map<String, List<StWasR>> waterLevelMap = waterLevelTempList.stream().collect(Collectors.groupingBy(StWasR::getMgstcd));

        Comparator<StWasR> waterLevelComparator = (o1, o2) -> {
            if (o1.getUpz() != null && o2.getUpz() != null) {
                return o1.getUpz().compareTo(o2.getUpz());
            } else if (o1.getUpz() == null & o2.getUpz() != null) {
                return -1;
            } else if (o1.getUpz() != null & o2.getUpz() == null) {
                return 1;
            } else {
                return 0;
            }
        };
        int openGateNum = 0;
        double drainageTotal = 0D;
        WagaReplayResVo maxOpenGateTimeWaga = null;
        List<WagaReplayResVo> tempList = new ArrayList<>(wagaList.size());
        for (AttWagaBase attWagaBase : wagaList) {
            WagaReplayResVo temp = new WagaReplayResVo();
            BeanUtils.copyProperties(attWagaBase, temp);
            tempList.add(temp);
            AttStBase station = projStaMap.get(temp.getWagaCode());
            if (station == null) {
                continue;
            }
            List<StWasR> waterLevelList = waterLevelMap.get(station.getStCode());

            if (CollectionUtils.isEmpty(waterLevelList)) {
                temp.setWaterLevelList(waterLevelList);
                continue;
            }
            waterLevelList = waterLevelList.parallelStream().sorted(Comparator.comparing(StWasR::getTm)).collect(Collectors.toList());
            temp.setWaterLevelList(waterLevelList);

            waterLevelList.parallelStream().max(waterLevelComparator).ifPresent(o -> {
                temp.setHighestUpz(o.getUpz());
                temp.setHighestUpzTm(o.getTm());
            });
            waterLevelList.parallelStream().min(waterLevelComparator).ifPresent(o -> {
                temp.setLowestUpz(o.getUpz());
                temp.setLowestUpzTm(o.getTm());
            });
            int size = waterLevelList.size();
            int openGateTime = 0;
            for (int index = 0; index < size; index++) {
                StWasR c = waterLevelList.get(index);
                if (!HydrologyUtils.isWagaOpenGate(c.getOvs(), station.getGateOpenThreshold())) {
                    continue;
                }
                Date tempEnd;
                if (index + 1 < size) {
                    StWasR n = waterLevelList.get(index + 1);
                    tempEnd = n.getTm();
                } else {
                    tempEnd = end;
                }
                openGateTime += (tempEnd.getTime() - c.getTm().getTime()) / 60000;
            }
            if (openGateTime > 0) {
                temp.setGateOpened(true);
                temp.setOpenGateMinutes(openGateTime);
                openGateNum++;
                if (maxOpenGateTimeWaga == null) {
                    maxOpenGateTimeWaga = temp;
                }
                if (maxOpenGateTimeWaga.getOpenGateMinutes() < temp.getOpenGateMinutes()) {
                    maxOpenGateTimeWaga = temp;
                }
            }
            double drainage = wagaTotalDrainage(waterLevelList, end);
            if (drainage > 0) {
                temp.setDrainage(Double.parseDouble(String.format("%.2f", drainage)));
            }
            drainageTotal += drainage;
        }

        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<WagaReplayResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            tempList = tempList.stream().sorted(comparator).collect(Collectors.toList());
        }

        result.put("openGateNum", openGateNum);
        result.put("drainageTotal", Double.parseDouble(String.format("%.2f", drainageTotal)));
        result.put("maxOpenGateTimeWaga", maxOpenGateTimeWaga == null ? new RsvrReplayResVo() : maxOpenGateTimeWaga);
        if (!reqVo.isWaterLevelCurve()) {
            tempList.forEach(o -> {
                o.setWaterLevelList(Collections.emptyList());
            });
        }
        result.put("list", tempList);
        return result;
    }

    public double wagaTotalDrainage(List<StWasR> data, Date end) {
        double drainage = 0D;
        if (com.ygkj.utils.CollectionUtils.isBlank(data)) {
            return drainage;
        }
        if (com.ygkj.utils.CollectionUtils.isNotBlank(data)) {
            // 过滤掉最大过闸流量po无法转换数字的值。
            data = data.stream().filter(o ->
                    NumberUtils.isCreatable(o.getLl())
            ).sorted(Comparator.comparing(StWasR::getTm)).collect(Collectors.toList());
            if (com.ygkj.utils.CollectionUtils.isBlank(data)) {
                return drainage;
            }
            int size = data.size();
            double singleDrainage = 0D;
            for (int index = 0; index < size; index++) {
                StWasR c = data.get(index);
                Date tempEnd;
                if (index + 1 < size) {
                    StWasR n = data.get(index + 1);
                    tempEnd = n.getTm();
                } else {
                    tempEnd = end;
                }
                singleDrainage += (new BigDecimal(c.getLl()).doubleValue() * (tempEnd.getTime() - c.getTm().getTime()) / 1000) / 10000;
            }
            drainage += singleDrainage;
        }
        return drainage;
    }

    /**
     * 导出水闸
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportWrpWagaUnitTable(WrpWagaQueryReqVo reqVo, HttpServletResponse response) {
        // 禁止分页
        reqVo.noPage();
        List<WagaStationWaterResVo> list = wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new)
                .with(WagaStationWaterReqVo::setWagaName, reqVo.getWagaName())
                .with(WagaStationWaterReqVo::setWagaAdcd, reqVo.getAreaCode())
                .with(WagaStationWaterReqVo::setWagaBas, reqVo.getBas())
                .with(WagaStationWaterReqVo::setWrpWaga, true)
                .with(WagaStationWaterReqVo::setHours, reqVo.getHours())
                .with(WagaStationWaterReqVo::setFilterOpenGate, reqVo.getFilterOpenGate())
                .build()).getList();
        int hours = reqVo.getHours() == null ? 0 : reqVo.getHours();
        ExportUtils.exportExcel(list, "水闸",
                new String[]{"名称", "流域", "行政区划", "工程规模", "闸上水位", "闸下水位", "闸门开度", "最大过闸流量(㎡/s)", "实际过闸流量(㎡/s)", "累计" + hours + "小时泄洪量(万m³)"},
                new String[]{"wagaName", "bas", "areaName", "engScal", "upz", "dwz", "ovs", "mxgtq", "ll", "drainage"},
                response, null, Collections.emptyMap());
    }

    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (StringUtils.isNotBlank(this.waterAndRainMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }

}
