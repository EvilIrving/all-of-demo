package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.UserCollection;
import com.ygkj.gragh.vo.request.UserCollectionDelVo;
import com.ygkj.gragh.vo.request.UserCollectionQueryVo;

import java.util.List;
import java.util.Set;

/**
 * 用户收藏记录表
 *
 * @author huangzh
 * @date 2021/02/23
 */
public interface UserCollectionMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    int insert(UserCollection userCollection);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    int delete(UserCollectionDelVo delVo);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    int update(UserCollection userCollection);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    UserCollection load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    List<UserCollection> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/02/23
     **/
    int pageListCount(int offset, int pagesize);

    Set<String> selectByCondition(UserCollectionQueryVo queryVo);

}

