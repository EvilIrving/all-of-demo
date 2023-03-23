package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.SpaceWaterArea;

import java.util.List;

/**
 * @author Huangzh
 * @description 水域信息表
 * @date 2020-12-11
 */
@DS("monitor")
public interface SpaceWaterAreaMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int insert(SpaceWaterArea spaceWaterArea);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int update(SpaceWaterArea spaceWaterArea);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    SpaceWaterArea load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    List<SpaceWaterArea> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int pageListCount(int offset, int pagesize);

    List<SpaceWaterArea> allWaterAreas();
}

