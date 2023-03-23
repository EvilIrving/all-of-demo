package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.WaveField;
import com.ygkj.visualization.model.WindFarm;

import java.util.List;
import java.util.Map;

/**
 * 浪场数据记录表
 * @author huangzh
 * @date 2021/02/22
 */
@DS("business")
public interface WaveFieldMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/02/22
     **/
    int insert(WaveField waveField);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/02/22
     **/
    int delete(int id);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/02/22
     **/
    int update(WaveField waveField);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/02/22
     **/
    WindFarm load(int id);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/02/22
     **/
    List<WaveField> pageList(int offset,int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/02/22
     **/
    int pageListCount(int offset,int pagesize);

    List<String> selectTimes();

    List<Map<String,String>> selectWithoutDetail();

}

