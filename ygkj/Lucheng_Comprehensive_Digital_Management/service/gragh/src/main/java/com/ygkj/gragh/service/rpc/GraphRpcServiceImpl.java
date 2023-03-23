package com.ygkj.gragh.service.rpc;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.gragh.service.SynWaterAndRainService;
import com.ygkj.gragh.vo.request.SynRainRequestVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
@Service(interfaceClass = GraphRpcService.class)
public class GraphRpcServiceImpl implements GraphRpcService {

    @Autowired
    SynWaterAndRainService waterAndRainService;
    @Autowired
    SynRainAndWaterMapper synRainAndWaterMapper;

    /**
     * 大屏实时降雨统计
     *
     * @param areaName
     * @param endTime
     * @return
     */
    @Override
    public JSONObject rainfallStatistic(String areaName, String endTime) {
        return waterAndRainService.rainfallStatistic(areaName, endTime);
    }

    @Override
    public List<StRsvrR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime){
        return synRainAndWaterMapper.selectRsvrWaterLevelByTimeCode(table,stcds,dataTime);
    }

    @Override
    public List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime) {
        return synRainAndWaterMapper.selectRiverByTimeCode(table,stcds,dataTime);
    }

    @Override
    public List<StWasR> selectWasByTimeCode(String table, List<String> stcds, Date dataTime) {
        return synRainAndWaterMapper.selectWasByTimeCode(table,stcds,dataTime);
    }

    @Override
    public List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime) {
        return synRainAndWaterMapper.selectTideByTimeCode(table,stcds,dataTime);
    }
}
