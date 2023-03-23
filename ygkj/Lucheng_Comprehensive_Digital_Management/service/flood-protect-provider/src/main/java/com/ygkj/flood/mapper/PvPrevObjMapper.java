package com.ygkj.flood.mapper;

import com.ygkj.flood.model.AttPrevTfBase;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvFloodAffectPopulationResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-11 10:51
 * @description
 */
public interface PvPrevObjMapper {


    List<AttPrevTfBase> selectPrevObj(AttPrevTfBaseVo vo);

    /**
     * 查询影响人口信息
     * @param reqVo
     * @return
     */
    List<PvFloodAffectPopulationResVo> findAffectPopulationList(PrevTfInfoReqVo reqVo);

    @Select("select sum(pcount) from pv_flood_affect_population where prev_code = #{baseId}")
    Integer findSumPeople(@Param("baseId") String baseId);
}
