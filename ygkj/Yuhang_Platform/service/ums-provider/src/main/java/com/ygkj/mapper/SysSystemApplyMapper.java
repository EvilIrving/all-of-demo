package com.ygkj.mapper;

import com.ygkj.auth.api.thirdPartSystem.model.SysSystemApply;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/10/27 11:41
 * @Description:
 */
public interface SysSystemApplyMapper {
    /**
     * 新增
     * @author Huangzh
     * @date 2020/10/27
     **/
    int insert(SysSystemApply sysSystemApply);

    /**
     * 刪除
     * @author Huangzh
     * @date 2020/10/27
     **/
    int delete(int id);

    /**
     * 更新
     * @author Huangzh
     * @date 2020/10/27
     **/
    int update(SysSystemApply sysSystemApply);

    /**
     * 查询 根据主键 id 查询
     * @author Huangzh
     * @date 2020/10/27
     **/
    SysSystemApply load(int id);

    /**
     * 查询 分页查询
     * @author Huangzh
     * @date 2020/10/27
     **/
    List<SysSystemApply> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author Huangzh
     * @date 2020/10/27
     **/
    int pageListCount(int offset,int pagesize);
}
