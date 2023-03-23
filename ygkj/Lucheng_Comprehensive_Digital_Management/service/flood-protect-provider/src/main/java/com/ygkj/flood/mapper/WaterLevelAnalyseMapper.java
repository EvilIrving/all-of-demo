package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.model.StRiverRYyyymm;
import com.ygkj.flood.vo.request.WaterLevelReqVo;
import com.ygkj.flood.vo.response.WarningStationResVo;
import com.ygkj.flood.vo.response.WlaFloatWinWaterLevelRespVo;
import com.ygkj.flood.vo.response.WlaHisHighestWaterLevelRespVo;
import com.ygkj.flood.vo.response.WlaWarnNumStatisticsRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaterLevelAnalyseMapper {


    List<WlaHisHighestWaterLevelRespVo> getHisHighestWaterLevels(@Param("stType") String stType, @Param("scale") String scale,
                                                                 @Param("stName") String stName, @Param("stCode") String stCode);

    List<WlaWarnNumStatisticsRespVo> warnNumStatistics();

    List<WlaFloatWinWaterLevelRespVo> floatWinWaterLevel(@Param("stType") String stType, @Param("scale") String scale,
                                                         @Param("name") String name, @Param("adcd") String adcd,
                                                         @Param("stCode") String stCode);

    @DS("business")
    List<StRiverRYyyymm> getRealWaterLevel(@Param("tableName") String tableName);

    List<WarningStationResVo> selectTodayWarningStation();

    List<StBase> stBases(WaterLevelReqVo requestVo);

    @DS("business")
    List<StRiverRYyyymm> getRealRiverWaterLevel(@Param("riverTableName") String riverTableName);


    List<StBase> stBasesByCodeList(@Param("stCodeList") List<String> stCodeList, @Param("stName") String stName);

    @DS("business")
    List<StRiverRYyyymm> getRiverRealWaterLevelByStCodeList(@Param("tableName") String tableName, @Param("stCodeList") List<String> stCodeList);

    @DS("business")
    List<StRiverRYyyymm> getGateRealWaterLevelByStCodeList(@Param("tableName") String tableName, @Param("stCodeList") List<String> stCodeList);

    @DS("business")
    List<StRiverRYyyymm> getTideRealWaterLevelByStCodeList(@Param("tableName") String tableName, @Param("stCodeList") List<String> stCodeList);

    @DS("business")
    List<StRiverRYyyymm> getRsvrRealWaterLevelByStCodeList(@Param("tableName") String tableName, @Param("stCodeList") List<String> stCodeList);
}
