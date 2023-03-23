package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.RpProblemStatsReqVo;
import com.ygkj.big.screen.vo.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fml
 * @date 2021-03-22 17:17
 * @description 河湖保护专题
 */
public interface RiverProtectionMapper {

    @DS("basic")
    List<WzAdcdInfo> getWzAdcds();

    @DS("basic")
    List<RpRiverNumStatitstcsRespVo> riverNumStatistics(@Param("adcd") String adcd);

    @DS("basic")
    List<RpWaterLevelsRespVo> getZZStations(@Param("adcd") String adcd);

    @DS("business")
    List<Map<String, Object>> getRealWaterLevels(@Param("tableName") String tableName);

    @DS("business")
    List<RpProblemStatsRespVo> problemStatistics(@Param("type") String type);

    @DS("basic")
    List<RpRailfallInfo> getRainStations(@Param("adcd") String adcd);

    @DS("business")
    List<StPptnR> getRealRainfall(@Param("tableName") String tableName);

    @DS("basic")
    List<AttSwhsBase> getSwshs(@Param("id") String id);

    @DS("basic")
    List<AttResBase> getResBases(@Param("id") String id);

    @DS("basic")
    List<AttCwsstationBase> getCwsStas(@Param("id") String id);

    @DS("basic")
    List<AttStBase> getWaterStas(@Param("id") String id);

    @DS("basic")
    List<AttStBase> getRainStas(@Param("id") String id);

    @DS("basic")
    List<RiverUnionInfo> getRuInfos(@Param("id") String id);

    @DS("basic")
    List<HikvisionCameraRecord> getCameraRecords(@Param("id") String id);

    @DS("business")
    List<WtDynProblem> getDynProblems(@Param("id") String id);

    @DS("basic")
    List<RpCwsStasRealDataInfo> getCwsStaCodes();

    @DS("business")
    List<RpCwsStasRealDataInfo> getCwsStaRealDatas();

    @DS("basic")
    List<WzAdcdInfo> getAdcdStStats(@Param("stType") String stType);

    @DS("basic")
    List<WzAdcdInfo> getAdcdCwsStats();

    @DS("basic")
    List<WzAdcdInfo> getAdcdCameraStats();

    @DS("basic")
    List<WzAdcdInfo> getAdcdRiverNum();

    @DS("basic")
    List<WzAdcdInfo> getAdcdLakeNum();

    @DS("basic")
    List<WzAdcdInfo> getAdcdResNum();
}
