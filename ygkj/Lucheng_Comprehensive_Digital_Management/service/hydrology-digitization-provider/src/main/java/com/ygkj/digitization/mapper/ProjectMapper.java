package com.ygkj.digitization.mapper;

import com.ygkj.digitization.vo.request.CwsReqVo;
import com.ygkj.digitization.vo.request.ProjectListReqVo;
import com.ygkj.digitization.vo.response.CwsResVo;
import com.ygkj.digitization.vo.response.GateStationResVo;
import com.ygkj.digitization.vo.response.ReservoirResVo;
import com.ygkj.entity.SingletonMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProjectMapper {

    List<ReservoirResVo> selectReservoirs(String name,String code);

    List<GateStationResVo> selectPumps();

    List<GateStationResVo> selectSluice();

    List<CwsResVo> listCws(ProjectListReqVo reqVo);

    String selectCwsStationCode(String stationCode);

    @Update("UPDATE att_res_base SET main_stcd = #{stationCode} WHERE res_code = #{reservoirCode} AND del_flag = 0")
    int updateReservoirMainStation(String reservoirCode,String stationCode);

    @Select("select main_stcd from att_res_base where del_flag=0 and res_code=#{resCode}")
    @ResultType(String.class)
    String selectMainStcdOfRsvr(String resCode);

    @Select("select res_code as `key`,res_name as `value` from att_res_base where del_flag=0 and eng_scal<=3")
    @ResultType(SingletonMap.class)
    List<SingletonMap<String,String>> selectLargeMidRsvr();
}
