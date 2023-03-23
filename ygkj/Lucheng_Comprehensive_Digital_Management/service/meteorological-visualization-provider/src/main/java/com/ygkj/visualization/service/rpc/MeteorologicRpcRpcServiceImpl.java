package com.ygkj.visualization.service.rpc;

import com.ygkj.visualization.rpc.MeteorologicalRpcService;
import com.ygkj.visualization.service.impl.WeatherServiceImpl;
import org.apache.dubbo.config.annotation.Service;

import java.util.Map;

@Service(interfaceClass = MeteorologicalRpcService.class)
public class MeteorologicRpcRpcServiceImpl implements MeteorologicalRpcService {

    @Override
    public Map<String, float[][]> candidateRainfallPredictData() {
        return WeatherServiceImpl.rainfallPredictData();
    }
}
