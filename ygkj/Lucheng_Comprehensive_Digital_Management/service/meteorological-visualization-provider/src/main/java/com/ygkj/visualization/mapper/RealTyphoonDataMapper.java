package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.entity.SingletonMap;
import com.ygkj.visualization.model.RealTyphoonData;
import com.ygkj.visualization.vo.request.RealTyphoonDataRequestVo;
import com.ygkj.visualization.vo.response.RealTyphoonDataResVo;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 台风历史记录表（从四创获取）
 * @author huangzh
 * @date 2021/02/01
 */
@DS("business")
public interface RealTyphoonDataMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/02/01
     **/
    int insert(RealTyphoonData realTyphoonData);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/02/01
     **/
    int delete(int id);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/02/01
     **/
    int update(RealTyphoonData realTyphoonData);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/02/01
     **/
    RealTyphoonData load(int id);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/02/01
     **/
    List<RealTyphoonData> pageList(int offset,int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/02/01
     **/
    int pageListCount(int offset,int pagesize);

    List<RealTyphoonDataResVo> selectByCondition(RealTyphoonDataRequestVo realTyphoonDataRequestVo);

    RealTyphoonData selectDetail(String code);

    RealTyphoonData selectNewestTyphoon();

    @Select("select id,name,begin_time,end_time,code,year,land_power,english_name from real_typhoon_data")
    @ResultType(RealTyphoonData.class)
    List<RealTyphoonData> selectAllTyphoonWithoutPoint();

    @Select("select a.monthVal,count(monthVal) as num from (select DATE_FORMAT(begin_time,'%m') as monthVal,begin_time from real_typhoon_data where year=#{year}) as a GROUP BY a.monthVal")
    @ResultType(Map.class)
    List<Map<String,Object>> selectTyphoonDataByYearGroupByMonth(Integer year);


    @Select("SELECT id,code,affected_area,land,begin_time,end_time FROM real_typhoon_data WHERE FIND_IN_SET(code,#{codes})" )
    List<Map<String,String>> selectCandidateTyphoonAffectedArea(String codes);

    @Select("select code as `key`,land as value FROM real_typhoon_data")
    List<SingletonMap<String,String>> selectAllLandInfo();

}

