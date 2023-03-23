package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.AttStBaseMapper;
import com.ygkj.big.screen.mapper.ProjectMapper;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.MultiWaterRainResVo;
import com.ygkj.big.screen.vo.response.StPrrwtResVo;
import com.ygkj.big.screen.vo.response.WagaStationWaterResVo;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.enums.StationType;
import com.ygkj.utils.BeanComparator;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.HydrologyUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Component
@Slf4j
public class WagaManager extends AbstractUnitManager {

    @Autowired
    private GraphClient graphClient;


    @Autowired
    private ProjectManageClient projectManageClient;

    @Resource
    private AttStBaseMapper stBaseMapper;

    @Resource
    private ProjectMapper projectMapper;

    List<String> basList = new ArrayList<>();

    private ConcurrentHashMap<String, Object> cache;

    public WagaManager(@Autowired ApplicationContext context) {
        super(context);
        cache = new ConcurrentHashMap<>();
        Collections.addAll(basList, "瓯江", "飞云江", "鳌江");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        cache.clear();
    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "wagaStaStats": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Integer hours = reqVo.getParam("hours", Integer.class);
                return wagaStaStats(dataTime, hours);
            }
            case "wagaProjStats": {
                Date dataTime = reqVo.getParam("dataTime", Date.class);
                Integer hours = reqVo.getParam("hours", Integer.class);
                Object result = null;
                if ((result = cache.get("wagaProjStats" + "," + hours + "," + String.valueOf(dataTime))) == null) {
                    result = wagaProjStats(dataTime, hours);
                }
                return result;
            }
            case "wagaProjList": {
                WrpWagaQueryReqVo reqVoParam = reqVo.getParam("reqVo", WrpWagaQueryReqVo.class);
                return wagaProjList(reqVoParam);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项水闸缓存任务");
        Date dataTime = null;
        Integer hours = null;
        cache.put("wagaProjStats" + "," + hours + "," + String.valueOf(dataTime), wagaProjStats(dataTime, hours));
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每十分钟提交一次任务
     */
    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }

    /**
     * 水闸测站统计:水闸测站总数/温瑞平(水闸数目/开闸数目/泄流总量
     *
     * @param dataTime 复盘时刻
     * @param hours    过去多少小时
     * @return
     */
    public JSONObject wagaStaStats(Date dataTime, Integer hours) {
        hours = hours == null ? 24 : hours;
        int wagaStNum = 0;
        int wrpNum = 0;
        long wrpOpenNum = 0;
        double drainage = 0D;
//        // 水闸测站总数
//        Integer totalNum = stBaseMapper.countByCondition(Builder.of(HashMap<String, Object>::new).with(HashMap::put, "stType", StationType.GATE.type()).build());
//        wagaStNum = totalNum == null ? 0 : totalNum;

        CommonResult<List<JSONObject>> wagaProjResp = projectManageClient.projects(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setType, "水闸").build());
        if (wagaProjResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(wagaProjResp.getData())) {
            wagaStNum = wagaProjResp.getData().size();
        }

        // 温瑞平水闸测站stcd
        List<String> wrpStcds = stBaseMapper.selectStcdByMark("温瑞平水闸");
        wrpStcds = wrpStcds == null ? Collections.emptyList() : wrpStcds;
        wrpNum = wrpStcds.size();

        JSONObject result = new JSONObject();
        result.put("wagaStNum", wagaStNum);
        result.put("wrpNum", wrpNum);
        result.put("wrpOpenNum", wrpOpenNum);
        result.put("drainage", drainage);
        if (dataTime == null) {
            dataTime = new Date();
        }

        // 根据温瑞平查询历史水闸工情
        CommonResult<List<LinkedHashMap>> resp = graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setEnd, dataTime)
                .with(MultiWaterRainReqVo::setStart, new Date(dataTime.getTime() - hours * 3600 * 1000))
                .with(MultiWaterRainReqVo::setStcds, String.join(",", wrpStcds))
                .build());
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), MultiWaterRainResVo.class);

            // 统计闸门开度数量，由于provider 是按时间正序排序 所以这里应该取工情的最后一条
            wrpOpenNum = data.stream().filter(o -> !CollectionUtils.isBlank(o.getWaterRains()) && HydrologyUtils.isWagaOpenGate(o.getWaterRains().get(o.getWaterRains().size() - 1).getOvs(), o.getGateOpenThreshold())).count();
            drainage = wagaTotalDrainage(data, dataTime);
        }
        result.put("wrpOpenNum", wrpOpenNum);
        result.put("drainage", String.format("%.1f", drainage));

        return result;
    }

    public JSONObject wagaProjStats(Date dataTime, Integer hours) {
        hours = hours == null ? 24 : hours;
        int wagaProjNum = projectMapper.countWagaNum();
        int wrpNum = 0;
        long wrpOpenNum = 0;
        double drainage = 0D;

        JSONObject result = new JSONObject();
        JSONObject temp = wagaProjList(Builder.of(WrpWagaQueryReqVo::new)
                .with(WrpWagaQueryReqVo::setHours, hours)
                .with(WrpWagaQueryReqVo::setDataTime, dataTime)
                .build());
        wrpNum = temp.getIntValue("totalNum");
        wrpOpenNum = temp.getIntValue("openGateWagaNum");
        drainage = temp.getDoubleValue("drainageTotal");
        result.put("wagaProjNum", wagaProjNum);
        result.put("wrpNum", wrpNum);
        result.put("wrpOpenNum", wrpOpenNum);
        result.put("drainage", drainage);
        return result;
    }

    /**
     * 水闸工程列表 同时统计水闸数量、小型水闸数量、闸门开启数量、24h累计排洪量（所有水闸的过闸流量*24小时）
     *
     * @param reqVo
     * @return
     */
    public JSONObject wagaProjList(WrpWagaQueryReqVo reqVo) {
        List<WagaStationWaterResVo> list = Collections.emptyList();
        long totalNum = 0;
        long openGateWagaNum = 0;
        double drainageTotal = 0D;
        JSONObject result = new JSONObject();
        // 只要温瑞平的水闸
        CommonResult<PageVO<LinkedHashMap>> projResp = projectManageClient.wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new)
                .with(WagaStationWaterReqVo::setWagaCodes, reqVo.getWagaCode())
                .with(WagaStationWaterReqVo::setWagaName, reqVo.getWagaName())
                .with(WagaStationWaterReqVo::setWagaAdcd, reqVo.getAreaCode())
                .with(WagaStationWaterReqVo::setWagaBas, reqVo.getBas())
                .with(WagaStationWaterReqVo::setWrpWaga, null)
                .with(WagaStationWaterReqVo::setHours, reqVo.getHours())
                .with(WagaStationWaterReqVo::setDataTime, reqVo.getDataTime())
//                .with(WagaStationWaterReqVo::setFilterOpenGate, reqVo.getFilterOpenGate())
                .build());
        if (projResp != null && projResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(projResp.getData().getList())) {
            List<WagaStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(projResp.getData().getList()), WagaStationWaterResVo.class);
            totalNum = data.size();
            List<WagaStationWaterResVo> openGateList = data.stream().filter(WagaStationWaterResVo::isGateOpened).collect(Collectors.toList());
            openGateWagaNum = openGateList.size();
            list = reqVo.getFilterOpenGate() != null && reqVo.getFilterOpenGate() ? openGateList : data;
            if (StringUtils.isNotBlank(reqVo.getEngScal())) {
                EngScalEnum tempScalEnum = EngScalEnum.getByDesc(reqVo.getEngScal());
                if (tempScalEnum == null) {
                    list = Collections.emptyList();
                } else {
                    list = list.stream().filter(o -> tempScalEnum.getDesc().equals(o.getEngScal())).collect(Collectors.toList());
                }
            }
//            Date dataTime = reqVo.getDataTime();
//            Integer hours = reqVo.getHours();
//            // 开始统计列表中24小时泄流量
//            dataTime = dataTime == null ? new Date() : dataTime;
//            hours = hours == null ? 24 : hours;
//            // 查询列表工程关联测站的历史测站曲线。
//            CommonResult<List<LinkedHashMap>> staResp = graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
//                    .with(MultiWaterRainReqVo::setEnd, dataTime)
//                    .with(MultiWaterRainReqVo::setStart, new Date(dataTime.getTime() - hours * 3600 * 1000))
//                    .with(MultiWaterRainReqVo::setStcds, String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, data.stream().filter(o -> StringUtils.isNotBlank(o.getStCode())).map(WagaStationWaterResVo::getStCode).collect(Collectors.toList())))
//                    .build());
//            Map<String, MultiWaterRainResVo> staMap = Collections.emptyMap();
//            if (staResp != null && staResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(staResp.getData())) {
//                List<MultiWaterRainResVo> staData = JSONArray.parseArray(JSONArray.toJSONString(staResp.getData()), MultiWaterRainResVo.class);
//                staMap = staData.stream().collect(Collectors.toMap(MultiWaterRainResVo::getStCode, Function.identity()));
//            }
            for (WagaStationWaterResVo temp : list) {
//                Optional.ofNullable(EngScalEnum.getByLevel(temp.getEngScal())).ifPresent(o1 -> temp.setEngScal(o1.getDesc()));
//                MultiWaterRainResVo tempSta = staMap.get(temp.getStCode());
//                if (tempSta != null) {
//                    double drainage = wagaTotalDrainage(Collections.singletonList(tempSta), dataTime);
//                    temp.setDrainage(Double.valueOf(String.format("%.1f", drainage)));
//                    drainageTotal += drainage;
//                }
                if (temp.getDrainage() != null) {
                    drainageTotal += temp.getDrainage();
                }
            }
//            if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
//                Comparator<WagaStationWaterResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
//                if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
//                    comparator = comparator.reversed();
//                }
//                list = list.stream().sorted(comparator).collect(Collectors.toList());
//            }
            Comparator<WagaStationWaterResVo> comparator = new BeanComparator<>("upz");
            if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
                comparator = comparator.reversed();
            }
            list = list.stream().sorted(comparator.reversed().thenComparing(o -> basList.indexOf(o.getBas()))).collect(Collectors.toList());
        }
        result.put("list", list);
        // 水闸总数量
        result.put("totalNum", totalNum);
        // 闸门开启数量
            result.put("openGateWagaNum", openGateWagaNum);
        // 累计排洪量
        result.put("drainageTotal", String.format("%.1f", drainageTotal));
        return result;
    }

    /**
     * 多个测站的水站总泄流量
     *
     * @param data 带着水闸历史监测数据的水闸测站
     * @param end  结束时刻
     * @return
     */
    private double wagaTotalDrainage(List<MultiWaterRainResVo> data, Date end) {
        double drainage = 0D;
        if (CollectionUtils.isBlank(data)) {
            return drainage;
        }
        if (CollectionUtils.isNotBlank(data)) {
            for (MultiWaterRainResVo temp : data) {
                List<StPrrwtResVo> waterRains = temp.getWaterRains();
                if (CollectionUtils.isBlank(waterRains)) {
                    continue;
                }
                // 过滤掉最大过闸流量po无法转换数字的值。
                waterRains = waterRains.stream().filter(o ->
                        NumberUtils.isCreatable(o.getLl())
                ).collect(Collectors.toList());
                if (CollectionUtils.isBlank(waterRains)) {
                    continue;
                }
                int size = waterRains.size();
                double singleDrainage = 0D;
                for (int index = 0; index < size; index++) {
                    StPrrwtResVo c = waterRains.get(index);
                    Date tempEnd;
                    if (index + 1 < size) {
                        StPrrwtResVo n = waterRains.get(index + 1);
                        tempEnd = n.getTm();
                    } else {
                        tempEnd = end;
                    }
                    singleDrainage += (new BigDecimal(c.getLl()).doubleValue() * (tempEnd.getTime() - c.getTm().getTime()) / 1000) / 10000;
                }
                drainage += singleDrainage;
            }
        }
        return drainage;
    }

}
