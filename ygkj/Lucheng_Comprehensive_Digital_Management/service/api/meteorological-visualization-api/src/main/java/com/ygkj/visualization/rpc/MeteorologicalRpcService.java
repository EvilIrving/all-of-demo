package com.ygkj.visualization.rpc;

import java.util.Map;

public interface MeteorologicalRpcService {

    Map<String, float[][]> candidateRainfallPredictData();

}
