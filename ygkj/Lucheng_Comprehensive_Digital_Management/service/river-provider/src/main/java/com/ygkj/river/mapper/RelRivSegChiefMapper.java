package com.ygkj.river.mapper;

import com.ygkj.river.model.RelRivSegChief;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 河长通-河段、河长关联信息表
 * @date 2021-08-10
 */
public interface RelRivSegChiefMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/10
     **/
    int insert(RelRivSegChief relRivSegChief);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/10
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/10
     **/
    int update(RelRivSegChief relRivSegChief);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/10
     **/
    RelRivSegChief load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/10
     **/
    List<RelRivSegChief> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/10
     **/
    int pageListCount(int offset, int pagesize);

}
