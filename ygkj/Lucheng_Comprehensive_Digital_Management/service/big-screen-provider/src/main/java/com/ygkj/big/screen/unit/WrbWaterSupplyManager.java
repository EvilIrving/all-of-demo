package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.WrbWaterSupply;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Component
public class WrbWaterSupplyManager extends AbstractUnitManager {

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    public WrbWaterSupplyManager(@Autowired ApplicationContext context) {
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
            case "stats4WrbWaterSupply": {
                Integer year = reqVo.getParam("year", Integer.class);
                Integer type = reqVo.getParam("type", Integer.class);
                return stats4WrbWaterSupply(year, type);
            }
            case "statsCwindow4WrbWaterSupply": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindow4WrbWaterSupply(year);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }


    /**
     * 供水量公报统计
     *
     * @param year
     * @param type 1：供水量，2：供水工程
     * @return
     */
    public List<JSONObject> stats4WrbWaterSupply(Integer year, Integer type) {
        switch (type) {
            case 1:
                return stats4WrbWaterSupplyWithAmount(year);
            case 2:
                return stats4WrbWaterSupplyWithNum(year);
            default:
                return null;
        }
    }

    public List<JSONObject> stats4WrbWaterSupplyWithAmount(Integer year) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_supply", null);
        }
        List<WrbWaterSupply> supplyList = wrbMapper.selectWrbWaterSupply(year);
        supplyList = supplyList == null ? Collections.emptyList() : supplyList;
        Integer tempYear = CollectionUtils.isNotBlank(supplyList) ? supplyList.get(0).getYear() : year;
        List<JSONObject> list = new ArrayList<>(3);
        JSONObject temp = new JSONObject();
        temp.put("classifyName", "蓄水");
        temp.put("classifyValue", supplyList.stream().filter(o -> o.getSupStoreProj() != null).mapToDouble(o -> o.getSupStoreProj().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "引水");
        temp.put("classifyValue", supplyList.stream().filter(o -> o.getSupDiverProj() != null).mapToDouble(o -> o.getSupDiverProj().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "提水");
        temp.put("classifyValue", supplyList.stream().filter(o -> o.getSupLiftProj() != null).mapToDouble(o -> o.getSupLiftProj().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);
        return list;
    }

    public List<JSONObject> stats4WrbWaterSupplyWithNum(Integer year) {
        List<JSONObject> list = new ArrayList<>(3);
        JSONObject temp = new JSONObject();
        temp.put("classifyName", "蓄水");
        temp.put("classifyValue", 0);
        temp.put("year", year);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "引水");
        temp.put("classifyValue", 0);
        temp.put("year", year);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "提水");
        temp.put("classifyValue", 0);
        temp.put("year", year);
        list.add(temp);
        return list;
    }

    /**
     * 供水量公报C位统计
     *
     * @return
     */
    public JSONObject statsCwindow4WrbWaterSupply(Integer year) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_supply", null);
        }
        List<WrbWaterSupply> supplyList = wrbMapper.selectWrbWaterSupply(year);
        supplyList = supplyList == null ? Collections.emptyList() : supplyList;
        JSONObject result = new JSONObject();
        result.put("totalSupply", supplyList.stream().filter(o -> o.getTotalSupply() != null).mapToDouble(o -> o.getTotalSupply().doubleValue()).sum());
        result.put("totalProjNum", 0);

        result.put("year", CollectionUtils.isNotBlank(supplyList) ? supplyList.get(0).getYear() : year);
        return result;
    }
}
