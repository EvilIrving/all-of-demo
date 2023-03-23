package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.AssoStationReservoirReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.digitization.vo.response.*;
import com.ygkj.gragh.model.AttRnBase;
import com.ygkj.gragh.model.StZqrlB;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 水文监测站基础信息表
 * @author huangzh
 * @date 2021/01/12
 */
public interface AttStBaseMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/01/12
     **/
    int insert(AttStBase attStBase);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/01/12
     **/
    int delete(int id);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/01/12
     **/
    int update(AttStBase attStBase);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/01/12
     **/
    AttStBase load(String stCode);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/01/12
     **/
    List<AttStBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/01/12
     **/
    int pageListCount(int offset,int pagesize);

    List<RainfallStationResVo> selectByCondition(StationQueryVo queryVo);

    List<WaterLevelStationResVo> selectWaterLevelStationByCondition(StationQueryVo queryVo);

    List<GateStationResVo> gateStations(StationQueryVo queryVo);

    List<AttStBase> selectByStCodeList(@Param("stCodeList")List<String> stCodeList);

    @Select("select * from att_st_base where st_type=#{stType}")
    @ResultMap("BaseResultMap")
    List<AttStBase> selectByType(@Param("stType")String stType);

    List<AttStBase> selectByConditionT(StationRequestVo requestVo2);

    @Select("select st_code from att_st_mark_base where mark=#{mark}")
    @ResultType(String.class)
    List<String> selectStCodeByMark(String mark);

    List<com.ygkj.gragh.model.AttStBase> selectStationByCondition(com.ygkj.gragh.vo.request.StationRequestVo requestVo);

    @DS("business")
    @Select("select mgstcd ,lnnm,bgtm from st_zqrl_b where find_in_set(mgstcd,#{stcds}) GROUP BY mgstcd,lnnm,bgtm ORDER BY MGSTCD,bgtm desc")
    @ResultType(com.ygkj.digitization.vo.response.ZqCurveConfigResVo.class)
    List<ZqCurveConfigResVo> selectZqCurveConfig(String stcds);

    @DS("business")
    List<StZqrlB> selectZqCurveByStcdLnnm(@Param("param") Map<String,String> param);

    @DS("business")
    @Select("select mgstcd,lnnm,z,q from st_zqrl_b where mgstcd=#{stcd} and lnnm=#{lnnm}")
    @ResultType(StZqrlB.class)
    List<StZqrlB> selectZqCurveBySingleStcdLnnm(@Param("stcd") String stcd,@Param("lnnm") String lnnm);

    @DS("business")
    @Select("select lnnm from st_zqrl_b where mgstcd=#{stcd} GROUP BY lnnm ORDER BY bgtm desc limit 1")
    @ResultType(String.class)
    String selectNewestLnnm(@Param("stcd") String stcd);

    @DS("business")
    @Select("select mgstcd,lnnm from st_zqrl_b where find_in_set(mgstcd,#{stcds}) GROUP BY mgstcd ORDER BY bgtm desc")
    @ResultType(ZqCurveConfigResVo.class)
    List<ZqCurveConfigResVo> selectMultiNewestLnnmByStcds(@Param("stcds") String stcds);


    /**
     * 根据条件查询河网
     * @return
     */
    List<AttRnBase> selectRnByCondition(RivernetWaterLevelReqVo reqVo);

    List<AssoStationReservoirRespVo> assoStationReservoirList(AssoStationReservoirReqVo vo);

    /**
     * 批量更新 disPlay是否显示
     * @param stationCodes
     * @param disPlay
     * @return
     */
    Integer batchUpdate(@Param("stationCodes") String[] stationCodes,@Param("disPlay") Integer disPlay);

    /**
     * 根据条件查询断面流量基础信息
     * @param reqVo
     * @return
     */
    List<SectFlowResVo> selectSectFlowByCondition(SectFlowQueryReqVo reqVo);

    List<com.ygkj.gragh.model.AttStBase> unBindStationCodes(@Param("mainStationCodes") List<String> mainStationCodes);

    @Update("UPDATE att_st_base SET proj_code = null WHERE proj_code = #{resCode}")
    int cancelBind(String resCode);
}

