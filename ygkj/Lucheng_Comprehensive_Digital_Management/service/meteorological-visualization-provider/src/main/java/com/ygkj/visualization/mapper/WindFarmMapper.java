package com.ygkj.visualization.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.WindFarm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 风场预报数据记录表
 * @author huang.zh
 * @date 2021/02/22
 */
@DS("business")
public interface WindFarmMapper {

    /**
     * [新增]
     * @author 黄政豪
     * @date 2021/02/22
     **/
    int insert(WindFarm windFarm);

    /**
     * [刪除]
     * @author 黄政豪
     * @date 2021/02/22
     **/
    int delete(int id);

    /**
     * [更新]
     * @author 黄政豪
     * @date 2021/02/22
     **/
    int update(WindFarm windFarm);

    /**
     * [查询] 根据主键 id 查询
     * @author 黄政豪
     * @date 2021/02/22
     **/
    WindFarm load(int id);

    /**
     * [查询] 分页查询
     * @author 黄政豪
     * @date 2021/02/22
     **/
    List<WindFarm> pageList(int offset,int pagesize);

    /**
     * [查询] 分页查询 count
     * @author 黄政豪
     * @date 2021/02/22
     **/
    int pageListCount(int offset,int pagesize);

    int batchInsert(@Param("list") List<WindFarm> list);

    List<String> selectTimes();

    List<Map<String,String>> selectWithoutDetail();

}

