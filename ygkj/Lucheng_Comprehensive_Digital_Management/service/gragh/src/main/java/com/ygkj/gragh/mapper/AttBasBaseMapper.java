package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttBasBase;

import java.util.List;

/**
 * @author Huangzh
 * @description 流域基础信息表
 * @date 2020-12-07
 */
public interface AttBasBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int insert(AttBasBase attBasBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int update(AttBasBase attBasBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    AttBasBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    List<AttBasBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int pageListCount(int offset, int pagesize);


    List<AttBasBase> selectAll();
}

