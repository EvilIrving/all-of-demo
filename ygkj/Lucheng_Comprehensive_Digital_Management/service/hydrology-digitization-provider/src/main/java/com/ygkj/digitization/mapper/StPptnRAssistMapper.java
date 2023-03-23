package com.ygkj.digitization.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.model.StPptnRAssist;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.digitization.vo.response.StPptnRAssistResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StPptnRAssistMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/06/08
     **/
    int insert(StPptnRAssist stPptnRAssist);

    /**
     * 刪除
     * @author hucong
     * @date 2021/06/08
     **/
    int delete(int id);

    /**
     * 更新
     * @author hucong
     * @date 2021/06/08
     **/
    int update(StPptnRAssist stPptnRAssist);

    /**
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/06/08
     **/
    StPptnRAssist load(int id);

    /*每小时累计雨量统计查询大于30*/
    List<StPptnRAssistResp> selectDataGreaterByHour(@Param("startTime") String startTime,
                                                    @Param("endTime") String endTime,
                                                    @Param("areaCode") String areaCode,
                                                    @Param("stationName")String stationName,
                                                    @Param("bas") String bas);

    /*三小时累计雨量统计查询大于60*/
    List<StPptnRAssistResp> selectDataGreaterByThreeHour(@Param("startTime") String startTime,
                                                         @Param("endTime") String endTime,
                                                         @Param("areaCode") String areaCode,
                                                         @Param("stationName")String stationName,
                                                         @Param("bas") String bas);

    /*1天累计雨量统计查询大于120*/
    List<StPptnRAssistResp> selectDataGreaterByDay(@Param("startTime") String startTime,
                                                   @Param("endTime") String endTime,
                                                   @Param("areaCode") String areaCode,
                                                   @Param("stationName")String stationName,
                                                   @Param("bas") String bas);

    List<StPptnRAssistResp> dailyRainfall(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("areaCode") String areaCode,@Param("stationName")String stationName);

    void insertForeach(@Param("list") List<StPptnRAssist> stPptnRAssistList);

    List<JSONObject> findIntervalDataRainOneRain(StationRequestVo requestVo);

    List<JSONObject> findIntervalDataRainThreeRain(StationRequestVo requestVo);
}
