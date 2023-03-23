package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.gragh.mapper.TyphoonDataMapper;
import com.ygkj.gragh.model.TyphoonData;
import com.ygkj.gragh.service.DisasterPreventionService;
import com.ygkj.gragh.vo.request.TyphoonReqVo;
import com.ygkj.utils.DistanceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("disasterPreventionService")
public class DisasterPreventionServiceImpl implements DisasterPreventionService {

    @Resource
    private TyphoonDataMapper typhoonDataMapper;

    /**
     * 根据id查询台风详细数据
     *
     * @param id
     * @return
     */
    @Override
    public TyphoonData selectTyphoon(Integer id) {
        TyphoonData typhoonData = typhoonDataMapper.load(id);
        JSONArray array = JSONArray.parseArray(typhoonData.getPoints());
        for (Object o : array) {
            JSONObject jsonObject = (JSONObject) o;
            jsonObject.put("distance", distanceFarFromWenZhou(jsonObject.getString("lng"), jsonObject.getString("lat")));
        }
        typhoonData.setPoints(array.toJSONString());
        return typhoonData;
    }

    private double distanceFarFromWenZhou(String longitude, String latitude) {
        double lng = 120.707312d, lat = 28.001819d;
        return DistanceUtil.getDistance(lat, lng, Double.parseDouble(latitude), Double.parseDouble(longitude)).doubleValue();
    }

    /**
     * 实时台风
     *
     * @return
     */
    @Override
    public List<TyphoonData> realTimeTyphoon() {
        QueryWrapper<TyphoonData> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().isNull(TyphoonData::getEndTime).or().eq(TyphoonData::getEndTime, "");
        return typhoonDataMapper.selectList(queryWrapper);
    }


    /**
     * 历史台风
     *
     * @param typhoonReqVo
     * @return
     */
    @Override
    public List<TyphoonData> historyTyphoon(TyphoonReqVo typhoonReqVo) {
        return typhoonDataMapper.listHistory(typhoonReqVo);
    }
}
