package com.ygkj.mapper;

import com.ygkj.auth.api.achievement.model.SysAchievement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 16:39
 * @Description:
 */
@Repository
public interface SysAchievementMapper {

    /**
     * 新增
     * @author Huangzh
     * @date 2020/09/15
     **/
    int insert(SysAchievement sysAchievement);

    /**
     * 刪除
     * @author Huangzh
     * @date 2020/09/15
     **/
    int delete(int id);

    /**
     * 更新
     * @author Huangzh
     * @date 2020/09/15
     **/
    int update(SysAchievement sysAchievement);

    /**
     * 查询 根据主键 id 查询
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysAchievement> load(String id);

    List<SysAchievement> selectByParentId(String parentId);


    /**
     * 查询 分页查询
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysAchievement> pageList(int offset, int pagesize);

    List<SysAchievement> selectAll();

    List<SysAchievement> selectAllByName(String name);

    /**
     * 查询 分页查询 count
     * @author Huangzh
     * @date 2020/09/15
     **/
    int pageListCount(int offset,int pagesize);

}