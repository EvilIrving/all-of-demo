package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.request.AssoStationReservoirReqVo;
import com.ygkj.digitization.vo.request.StationDivideQueryVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.AssoStationReservoirRespVo;
import com.ygkj.digitization.vo.response.RainfallStationResVo;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface StationService {

    JSONArray stationDivide(StationDivideQueryVo queryVo);

    JSONObject reservoirStationTree(String name);

    PageVO<RainfallStationResVo> stationList(StationQueryVo queryVo);

    boolean focus(String stationCode,Boolean focus);

    boolean display(String stationCode,Boolean display);

    boolean reservoirRainfallSurfaceCalculate(String stationCode,Boolean reservoirSurfaceRainStation);

    boolean bindReservoirStation(String reservoirCode,String stationCode);

    boolean updateReservoirMainStation(String reservoirCode,String stationCode);

    PageVO<AssoStationReservoirRespVo> assoStationReservoirList(AssoStationReservoirReqVo vo);

    void bindBatchReservoirStation(String reservoirCode, String stationCodes);

    List<AttStBase> unBindReservoirStationInfo();
}
