package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.AppAchievementReqVo;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 16:39
 * @Description:
 */
@Repository
public interface SysAchievementMapper{

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int insert(SysAchievement sysAchievement);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int update(SysAchievement sysAchievement);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysAchievement> load(String id);

    List<SysAchievement> selectByParentId(String parentId);


    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysAchievement> pageList(int offset, int pagesize);

    List<SysAchievement> selectAll(String projectCode);

    List<SysAchievement> selectAllByName(String name);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 档案总量统计
     *
     * @return
     */
    List<JSONObject> statisticsTotalArchives();

    /**
     * 根据文件查找文件夹
     *
     * @param fileId
     * @return
     */
    List<SysAchievement> findAchievementByFileId(String fileId);

    /**
     * 分页查找全部
     *
     * @param vo
     * @return
     */
    List<SysAchievement> selectDataAll(AchievementFilePageQueryVO vo);

    List<SysAchievement> selectAll2(List<String> list);

    List<SysAchievement> getFolders(AppAchievementReqVo reqVo);
}