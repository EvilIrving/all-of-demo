package com.ygkj.water.project.mapper;

import com.ygkj.flood.model.AttTeamBase;
import com.ygkj.flood.model.AttWarehouseBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmergencySupportMapper {

    @Select("SELECT * FROM att_warehouse_base WHERE del_flag = 0 AND longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AttWarehouseBase> wareHouses();

    @Select("SELECT * FROM att_team_base WHERE del_flag = 0 AND longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AttTeamBase> teams();

    /**
     * 查询 符合 公里数条件的仓库
     *
     * @param codes 符合条件的编码集合
     * @return
     */
    List<AttWarehouseBase> candidateWareHouse(@Param("codes") List<String> codes);

    /**
     * 查询 符合 公里数条件的队伍
     *
     * @param codes 符合条件的编码集合
     * @return
     */
    List<AttTeamBase> candidateTeam(@Param("codes") List<String> codes);

}
