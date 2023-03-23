package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttSlfzBase;
import com.ygkj.gragh.vo.request.LineQueryVo;

import java.util.List;

/**
 * @author Huangzh
 * @description 岸线功能分区基础信息表
 * @date 2020-12-11
 */
public interface AttSlfzBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int insert(AttSlfzBase attSlfzBase);

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
    int update(AttSlfzBase attSlfzBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    AttSlfzBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    List<AttSlfzBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/11
     **/
    int pageListCount(int offset, int pagesize);

    List<AttSlfzBase> selectByCondition(LineQueryVo lineQueryVo);

}

