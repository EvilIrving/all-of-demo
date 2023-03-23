package com.ygkj.digitization.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.vo.request.RainfallReqVo;
import com.ygkj.digitization.vo.response.DayRainResVo;
import com.ygkj.digitization.vo.response.RainfallResVo;
import com.ygkj.digitization.vo.response.StResVo;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.project.model.AttStBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DroughtAnalysisMapper {

    /**
     * 一个时间段内每天的降雨量
     *
     * @param param
     * @return
     */
    @DS("business")
    List<RainfallResVo> listRainfall(Map<String, Object> param);

    /**
     * 所有有经纬度的雨量测站
     *
     * @return
     */
    List<StResVo> listRainSt();

    /**
     * 查询日降雨量统计到哪天了
     *
     * @return
     */
    @DS("business")
    @Select("SELECT MAX(date) FROM `st_pptn_r_day`")
    String getLastDate();

    /**
     * 批量保存日降雨量
     *
     * @param list
     */
    @DS("business")
    void batchInsert(List<RainfallResVo> list);

    /**
     * 从统计表中查每天的实时雨量
     *
     * @param reqVo
     */
    @DS("business")
    List<RainfallResVo> listRainfallNew(RainfallReqVo reqVo);

    /**
     * 查询日期内平均降雨量
     * @param beginTime
     * @param endTime
     * @return
     */
    Double findRainAvarge(@Param("beginTime") String beginTime, @Param("endTime") String endTime,@Param("flag") boolean flag);

    @Select("select * from att_drought_warn_standard")
    List<JSONObject> selectDroughtStandard();

    @Select("SELECT st_code FROM att_st_base WHERE is_surface_rain_station = 1")
    List<String> listSurfaceSt();

    @DS("business")
    List<DayRainResVo> list30DayRain(@Param("tables") List<String> tables, @Param("date") Date date, @Param("stList")List<String> stList);
}
