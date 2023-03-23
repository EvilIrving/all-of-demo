package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.service.BusRiskListService;
import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.response.BusRiskListResp;
import com.ygkj.flood.model.AttDictBase;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/15
 */
@Component
public class LatentRiskManager extends AbstractUnitManager {

    @Autowired
    BusRiskListService riskListService;

    public LatentRiskManager(@Autowired ApplicationContext context) {
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
            case "listRisk": {
                BusRiskListReq param = reqVo.getParam("reqVo", BusRiskListReq.class);
                return listRisk(param);
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }


    CommonResult listRisk(BusRiskListReq entity) {
        long latentRiskNum = 0;
        long highRiskNum = 0;
        long weakNum = 0;
        List<BusRiskListResp> list = Collections.emptyList();
        PageVO<BusRiskListResp> resp = riskListService.busRiskListPage(entity);
        Map<String, Long> map = Collections.emptyMap();
        if (CollectionUtils.isNotBlank(resp.getList())) {
            list = resp.getList();
            map = list.stream().filter(o -> StringUtils.isNotBlank(o.getSbSign())).collect(Collectors.groupingBy(BusRiskListResp::getSbSign, Collectors.counting()));
            latentRiskNum = map.getOrDefault("251", 0L);
            highRiskNum = map.getOrDefault("252", 0L);
            weakNum = map.getOrDefault("253", 0L);
        }
        JSONObject result = new JSONObject();
        result.put("list", list);
        result.put("latentRiskNum", latentRiskNum);
        result.put("highRiskNum", highRiskNum);
        result.put("weakNum", weakNum);
        return CommonResult.success(result);
    }

}
