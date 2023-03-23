package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.entity.SingletonMap;
import com.ygkj.flood.model.AttFloodPoint;
import com.ygkj.flood.model.FloodPoint;
import com.ygkj.flood.vo.request.FloodPointReqVo;
import com.ygkj.flood.vo.response.ChartResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/25
 */

@DS("business")
public interface FloodPointMapper {

    /**
     * 查询过去多少天最新的积水点点位
     *
     * @param reqVo
     * @return
     */
    List<FloodPoint> listNewestFloodPoint(FloodPointReqVo reqVo);

    FloodPoint selectNewestFloodPointByName(String name);

    List<FloodPoint> selectByName(@Param("name") String name, @Param("start") Date start, @Param("end") Date end);

    @DS("basic")
    @Select("select basin as `key`,count(1) as `value` from att_flood_point where del_flag=0 group by basin")
    List<ChartResVo<String, Long>> countByBasin();

    @DS("basic")
    @Select("select name as `key`,basin as `value` from att_flood_point where del_flag=0")
    List<ChartResVo<String, String>> selectNameBasinFromAttFloodPoint();

    @DS("basic")
    @Select("select area_name as `key`,count(1) as `value` from att_flood_point where del_flag=0 group by area_name")
    List<ChartResVo<String, Long>> countByAreaName();

    @DS("basic")
    List<AttFloodPoint> selectFloodPointDefine(FloodPointReqVo reqVo);

    @DS("basic")
    AttFloodPoint loadFloodPointDefine(String name);

    /**
     * 查询积水次数的点，以及次数
     *
     * @param dataTime
     * @return
     */
    SingletonMap<String, Long> maxPondingTimes(Date dataTime);

    /**
     * 查询积水点积水次数
     *
     * @param dataTime
     * @return
     */
    List<SingletonMap<String, Long>> pondingTimes(Date dataTime);
}
