package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.DisasterStatistic;
import com.ygkj.visualization.vo.request.DisasterPageQueryVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @description 灾害汇总表
 * @author Huangzh
 * @date 2021-10-12
 */
@DS("business")
public interface DisasterStatisticMapper {

    /**
     * 新增
     * @author Huangzh
     * @date 2021/10/12
     **/
    int insert(DisasterStatistic disasterStatistic);

    /**
     * 刪除
     * @author Huangzh
     * @date 2021/10/12
     **/
    int delete(int id);

    /**
     * 更新
     * @author Huangzh
     * @date 2021/10/12
     **/
    int update(DisasterStatistic disasterStatistic);

    /**
     * 查询 根据主键 id 查询
     * @author Huangzh
     * @date 2021/10/12
     **/
    DisasterStatistic load(int id);

    /**
     * 查询 分页查询
     * @author Huangzh
     * @date 2021/10/12
     **/
    List<DisasterStatistic> pageList(int offset,int pagesize);

    /**
     * 查询 分页查询 count
     * @author Huangzh
     * @date 2021/10/12
     **/
    int pageListCount(int offset,int pagesize);

    @Select("SELECT COUNT(DISTINCT(id)) AS total,SUM(affected_population) AS totalPopulation,sum(economic_losses) AS totalLosses FROM disaster_statistic")
    Map<String,Object> statistic();

    List<DisasterStatistic> selectByCondition(DisasterPageQueryVo queryVo);

    String selectFileUrl(String id);

    @Select("SELECT * FROM disaster_statistic ORDER BY economic_losses DESC limit 5")
    List<DisasterStatistic> top5DamageDisaster();


    @Select("SELECT `year` FROM disaster_statistic GROUP BY year ORDER BY sum(economic_losses)  DESC LIMIT 1")
    Integer maxLossesYear();

}

