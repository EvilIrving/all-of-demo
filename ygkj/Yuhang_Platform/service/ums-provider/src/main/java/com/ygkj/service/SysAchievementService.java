package com.ygkj.service;

import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:13
 * @Description: 归档管理服务层抽象
 */
public interface SysAchievementService {

    List<SysAchievement> selectAll(String name);

    String addSysAchievement(SysAchievementAddVO sysAchievementAddVO) throws Exception;

    void updateSysAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) throws Exception;

    void deleteSysAchievement(String id) throws Exception;

    PageVO<SysFile> findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO);

    CommonResult statisticsTotalArchives();

    /**
     * 文档结构树
     * @return
     */
    List<SysAchievement> achievementStructure();

    /**
     * 根据文件查找文件夹
     * @param fileId
     * @return
     */
    List<SysAchievement> findAchievementByFileId(String fileId);

    /**
     * 根据id查找数据
     * @param id
     * @return
     */
    SysAchievement findById(String id);

    /**
     * 分页查找全部
     * @param vo
     * @return
     */
    PageInfo<SysAchievement> selectDataAll(AchievementFilePageQueryVO vo);

    int insert(SysAchievement entity);

    int delete(String id);

    int update(SysAchievement entity);
}
