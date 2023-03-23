package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.AppAchievementReqVo;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:13
 * @Description: 归档管理服务层抽象
 */
public interface SysAchievementService{

    List<SysAchievement> selectAll(String name);

    String addSysAchievement(SysAchievementAddVO sysAchievementAddVO) throws Exception;

    void updateSysAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) throws Exception;

    void deleteSysAchievement(String id) throws Exception;

    PageVO<SysFile> findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO);

    CommonResult statisticsTotalArchives();

    /**
     * 文档结构树
     *
     * @return
     */
    List<SysAchievement> achievementStructure(String projectCode,String userId);

    /**
     * app文档结构树
     *
     * @return
     */
    List<JSONObject> appAchievement(AppAchievementReqVo reqVo);

    /**
     * 根据文件查找文件夹
     *
     * @param fileId
     * @return
     */
    List<SysAchievement> findAchievementByFileId(String fileId);

    /**
     * 根据id查找数据
     *
     * @param id
     * @return
     */
    SysAchievement findById(String id);

    /**
     * 分页查找全部
     *
     * @param vo
     * @return
     */
    PageInfo<SysAchievement> selectDataAll(AchievementFilePageQueryVO vo);

    int insert(SysAchievement entity);

    int delete(String id);

    int update(SysAchievement entity);

    /**
     * 查找档案路径
     *
     * @param entity
     */
    void findSysAchievementAchiPath(SysAchievement entity);

//    /**
//     * 档案督办
//     * @param id 档案id
//     * @return 1:发送成功 2:发送失败 3: 不需要督办
//     */
//    Integer achievementOversee(String id);

    /**
     * 修改管理员信息
     *
     * @param jurisdiction
     */
    void modifyAdmin(SysAchievementJurisdiction jurisdiction);


    //修改文件归属
    void editFileOfSysAchievement(String fileId,String achievementId,String AfterachievementId);
}
