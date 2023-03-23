package com.ygkj.mapper;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/20 14:56
 */

import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.vo.req.SysAchievementJurisdictionReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hucong
 * @description 文档权限表
 * @date 2021-01-20
 */
@Repository
public interface SysAchievementJurisdictionMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/01/20
     **/
    int insert(SysAchievementJurisdiction sysAchievementJurisdiction);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/01/20
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/01/20
     **/
    int update(SysAchievementJurisdiction sysAchievementJurisdiction);

    /**
     * 查询 根据主键 id 查询
     *
     * @author hucong
     * @date 2021/01/20
     **/
    SysAchievementJurisdiction load(int id);

    /**
     * 分页查询
     *
     * @param vo
     * @return
     */
    List<SysAchievementJurisdiction> selectAll(SysAchievementJurisdictionReqVo vo);

    /**
     * 根据文件夹id和操作类型查找数据
     *
     * @param achievementId 文件夹id
     * @param type          权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     * @return
     */
    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> findDataByAchievementIdAndType(String achievementId, String type);
}

