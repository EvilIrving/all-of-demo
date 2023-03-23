package com.ygkj.service;

import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.entity.SysFile;
import com.ygkj.vo.resp.PageVO;

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

}
