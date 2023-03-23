package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.FloodProtectClient;
import com.ygkj.big.screen.mapper.AttDictBaseMapper;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.flood.model.AttDictBase;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.flood.vo.request.EmergencyDispatchRequestVo;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 抢险支持组件
 * @Date 2021/7/15
 */
@Component
public class RescueSupportManager extends AbstractUnitManager {

    @Autowired
    FloodProtectClient floodProtectClient;

    @Resource
    AttDictBaseMapper dictBaseMapper;

    public RescueSupportManager(@Autowired ApplicationContext context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

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
            case "listTeam": {
                AttTeamBasePageQueryVo param = reqVo.getParam("reqVo", AttTeamBasePageQueryVo.class);
                return listTeam(param);
            }
            case "listWarehouses": {
                AttWarehousePageQueryVo param = reqVo.getParam("reqVo", AttWarehousePageQueryVo.class);
                return listWarehouses(param);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {


    }

    public CommonResult listTeam(AttTeamBasePageQueryVo reqVo) {
        List<LinkedHashMap> list = Collections.emptyList();
        CommonResult<PageVO<LinkedHashMap>> resp = floodProtectClient.teams(reqVo);
        Map<String, Long> map = Collections.emptyMap();
        if (CollectionUtils.isNotBlank(resp.getData().getList())) {
            list = resp.getData().getList();
            map = list.stream().filter(o -> o.get("typeId") != null).collect(Collectors.groupingBy(o -> (String) o.get("typeId"), Collectors.counting()));
        }
        List<AttDictBase> dictList = dictBaseMapper.selectDict("teamType");
        dictList = dictList == null ? Collections.emptyList() : dictList;
        List<ChartResVo<String, Long>> stats = new ArrayList<>();
        for (AttDictBase dict : dictList) {
            stats.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, dict.getDictName())
                    .with(ChartResVo::setValue, map.getOrDefault(dict.getId(), 0L))
                    .build());
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("stats", stats);
        result.put("total", list.size());
        return CommonResult.success(result);
    }

    public CommonResult listWarehouses(AttWarehousePageQueryVo reqVo) {
        List<LinkedHashMap> list = Collections.emptyList();
        CommonResult<PageVO<LinkedHashMap>> resp = floodProtectClient.warehouses(reqVo);
        Map<String, Long> map = Collections.emptyMap();
        if (CollectionUtils.isNotBlank(resp.getData().getList())) {
            list = resp.getData().getList();
            map = list.stream().filter(o -> o.get("typeId") != null).collect(Collectors.groupingBy(o -> (String) o.get("typeId"), Collectors.counting()));
        }
        List<AttDictBase> dictList = dictBaseMapper.selectDict("warehouseType");
        dictList = dictList == null ? Collections.emptyList() : dictList;
        List<ChartResVo<String, Long>> stats = new ArrayList<>();
        for (AttDictBase dict : dictList) {
            stats.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, dict.getDictName())
                    .with(ChartResVo::setValue, map.getOrDefault(dict.getId(), 0L))
                    .build());
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("stats", stats);
        result.put("total", list.size());
        return CommonResult.success(result);
    }
}
