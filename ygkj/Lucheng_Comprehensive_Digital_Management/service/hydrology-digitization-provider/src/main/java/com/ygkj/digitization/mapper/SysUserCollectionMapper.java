package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.SysUserCollection;
import com.ygkj.digitization.vo.request.CollectionPageQueryVo;

import java.util.List;

/**
 * @description 用户收藏记录表
 * @author Huangzh
 * @date 2021-08-10
 */
@DS("business")
public interface SysUserCollectionMapper {

    /**
     * 新增
     * @author Huangzh
     * @date 2021/08/10
     **/
    int insert(SysUserCollection sysUserCollection);

    /**
     * 刪除
     * @author Huangzh
     * @date 2021/08/10
     **/
    int delete(String id);

    /**
     * 更新
     * @author Huangzh
     * @date 2021/08/10
     **/
    int update(SysUserCollection sysUserCollection);

    /**
     * 查询 根据主键 id 查询
     * @author Huangzh
     * @date 2021/08/10
     **/
    SysUserCollection load(int id);

    /**
     * 查询 分页查询
     * @author Huangzh
     * @date 2021/08/10
     **/
    List<SysUserCollection> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author Huangzh
     * @date 2021/08/10
     **/
    int pageListCount(int offset,int pagesize);

    List<SysUserCollection> selectByCondition(CollectionPageQueryVo queryVo);

}

