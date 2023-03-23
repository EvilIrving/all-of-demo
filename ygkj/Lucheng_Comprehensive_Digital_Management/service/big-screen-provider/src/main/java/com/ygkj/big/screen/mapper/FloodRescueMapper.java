package com.ygkj.big.screen.mapper;

import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.StQueryReqVo;
import com.ygkj.big.screen.vo.response.FrWaterLevelStaRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fml
 * @date 2021-03-29 14:23
 * @description 防汛抢险专题
 */
public interface FloodRescueMapper {

    List<AttResBase> getResBases(@Param("id") String id);

    List<AttMnpdBase> getMnpdBases(@Param("id") String id);

    List<AttDikeBase> getDikeBases(@Param("id") String id);

    List<AttSeawallBase> getSeawallBases(@Param("id") String id);

    List<AttHystBase> getHystsBases(@Param("id") String id);

    List<AttPustBase> getPustBases(@Param("id") String id);

    List<AttWagaBase> getWagaBases(@Param("id") String id);

    List<WaterSupplyProject> getWsProjs(@Param("id") String id);

    List<AttStBase> getRiverStBases(@Param("id") String id);

    List<AttWarehouseBase> getWarehouseBases(@Param("id") String id);

    List<AttTeamBase> getTeamBases(@Param("id") String id);

    List<HikvisionCameraRecord> getCameraRecords(@Param("id") String id);

    List<Map<String, Object>> wareHouseAdcdStats();

    List<Map<String, Object>> teamAdcdStats();

    List<Map<String, Object>> teamPersonAdcdStats();

    List<Map<String, Object>> projAdcdStats();

    List<WzAdcdInfo> projTypeStats();

    List<FrWaterLevelStaRespVo> getWatertLevelStas();

    List<Map<String, Object>> getResRealWaterLevels(@Param("tableName") String tableName);

    List<Map<String, Object>> getRiverRealWaterLevels(@Param("tableName") String tableName);

    List<FrWaterLevelStaRespVo> getStBases(@Param("stType") String stType);

    List<Map<String, Object>> getSzWaterLevels(@Param("tableName") String tableName);

    List<AttStBase> queryStByCondtion(StQueryReqVo reqVo);
}
