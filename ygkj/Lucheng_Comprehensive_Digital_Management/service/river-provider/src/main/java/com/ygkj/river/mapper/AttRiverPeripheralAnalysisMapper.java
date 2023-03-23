package com.ygkj.river.mapper;

import com.ygkj.entity.StatisticEntity;
import com.ygkj.river.model.AttRiverPeripheralAnalysis;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Huangzh
 * @description 河道周边分析关联对象表
 * @date 2021-10-11
 */
public interface AttRiverPeripheralAnalysisMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    int insert(AttRiverPeripheralAnalysis attRiverPeripheralAnalysis);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    int update(AttRiverPeripheralAnalysis attRiverPeripheralAnalysis);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    AttRiverPeripheralAnalysis load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    List<AttRiverPeripheralAnalysis> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2021/10/11
     **/
    int pageListCount(int offset, int pagesize);

    List<StatisticEntity> RiverAnalysisObjectTypeStatistic(Integer riverSegId, Integer distance);

    @Select("SELECT DISTINCT(object_id)  FROM att_river_peripheral_analysis WHERE river_seg_id = #{riverSegId} AND type = #{type} AND distance <= #{distance}")
    List<String> riverAnalysisObjectIds(Integer riverSegId, String type, Integer distance);

    List<Map<String, Object>> selectPoints(String table, String primary, @Param("ids") List<String> ids);
}

