package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttWasBase;

import java.util.List;

/**
 * @author Huangzh
 * @description 堰闸信息表
 * @date 2020-12-09
 */
public interface AttWasBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    int insert(AttWasBase attWasBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    int update(AttWasBase attWasBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    AttWasBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    List<AttWasBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/09
     **/
    int pageListCount(int offset, int pagesize);

}

