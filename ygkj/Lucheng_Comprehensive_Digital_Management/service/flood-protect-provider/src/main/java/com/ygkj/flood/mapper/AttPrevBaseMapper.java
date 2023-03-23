package com.ygkj.flood.mapper;

import com.ygkj.flood.model.AttPrevBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 山洪防治对象信息表
 * @date 2021-06-08
 */
public interface AttPrevBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/06/08
     **/
    int insert(AttPrevBase attPrevBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/06/08
     **/
    int delete(String prevCode);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/06/08
     **/
    int update(AttPrevBase attPrevBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/06/08
     **/
    AttPrevBase load(String prevCode);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/06/08
     **/
    List<AttPrevBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/06/08
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 根据条件查询，条件待定
     *
     * @return
     */
    List<AttPrevBase> selectByCondition();

}
