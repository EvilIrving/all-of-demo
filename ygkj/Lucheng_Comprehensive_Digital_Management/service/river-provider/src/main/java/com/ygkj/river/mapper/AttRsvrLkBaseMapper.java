package com.ygkj.river.mapper;

import com.ygkj.river.model.AttRsvrLkBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 另一个湖库基础信息表
 * @date 2021-08-07
 */
public interface AttRsvrLkBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/07
     **/
    int insert(AttRsvrLkBase attRsvrLkBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/07
     **/
    int delete(String reservoirNo);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/07
     **/
    int update(AttRsvrLkBase attRsvrLkBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/07
     **/
    AttRsvrLkBase load(String reservoirNo);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/07
     **/
    List<AttRsvrLkBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/07
     **/
    int pageListCount(int offset, int pagesize);

}

