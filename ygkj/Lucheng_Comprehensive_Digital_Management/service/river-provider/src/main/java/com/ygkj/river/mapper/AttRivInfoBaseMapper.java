package com.ygkj.river.mapper;

import com.ygkj.river.model.AttRivInfoBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 另一个河道基础信息表
 * @date 2021-08-04
 */
public interface AttRivInfoBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/04
     **/
    int insert(AttRivInfoBase attRivInfoBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/04
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/04
     **/
    int update(AttRivInfoBase attRivInfoBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    AttRivInfoBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    List<AttRivInfoBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/04
     **/
    int pageListCount(int offset, int pagesize);

}
