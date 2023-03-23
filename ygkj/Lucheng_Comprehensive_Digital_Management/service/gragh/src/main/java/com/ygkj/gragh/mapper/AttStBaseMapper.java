package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.vo.request.StationRequestVo;
import com.ygkj.gragh.vo.request.SynWaterRequestVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Huangzh
 * @description 水文监测站基础信息表
 * @date 2020-12-08
 */
public interface AttStBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int insert(AttStBase attStBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int update(AttStBase attStBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    AttStBase load(String stationCode);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    List<AttStBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int pageListCount(int offset, int pagesize);

    List<AttStBase> selectByCondition(StationRequestVo requestVo);

    List<ChartResVo<String, Long>> countStTypeNumByAdcdBasin(@Param("adcd") String adcd, @Param("basin") String basin);

    List<Map<String, Object>> stationTypeStatistic(@Param("types") List<String> types);

    @Select("SELECT st_code FROM att_st_mark_base WHERE mark = '温瑞平水闸'")
    List<String> selectWrpWagaCodes();

    List<Map<String, Object>> rainfallHistoryMax(String stationCode);
}

