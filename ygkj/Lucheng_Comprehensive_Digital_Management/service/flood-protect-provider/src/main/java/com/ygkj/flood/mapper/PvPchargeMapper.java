package com.ygkj.flood.mapper;

import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-06 17:49
 * @description
 */
public interface PvPchargeMapper {

    /**
     * 查询责任人列表
     * @param reqVo
     * @return
     */
    List<PvPchargeResVo> pvPchargeListByConfition(PrevTfInfoReqVo reqVo);


    int updatePchargeBase(PvPchargeResVo resVo);

    int updatePchargeInfo(PvPchargeResVo resVo);


    int insertPvPchargeInfo(PvPchargeResVo resVo);


    int insertPvPchargeBase(PvPchargeResVo resVo);

    @Update("update pv_pcharge_info set del_flag = 1 where pcharge_code = #{code}")
    int deletePvPchargeInfo(@Param("code") String code);

    @Update("update pv_pcharge_base set del_flag = 1 where pcharge_code = #{code}")
    int deletePvPchargeBase(@Param("code") String code);
}
