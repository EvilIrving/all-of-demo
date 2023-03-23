package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.entity.SysFile;
import com.ygkj.mapper.SysAchievementMapper;
import com.ygkj.mapper.SysFileMapper;
import com.ygkj.service.SysAchievementService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.resp.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:14
 * @Description: 归档管理服务层具体实现
 */
@Service("sysAchievementService")
public class SysAchievementServiceImpl implements SysAchievementService{
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    public List<SysAchievement> selectAll(String name) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(name)){
            return sysAchievementMapper.selectAll();
        }else{
            List<SysAchievement> sysAchievements = sysAchievementMapper.selectAllByName(name);
            return sysAchievements;
        }
    }

    @Override
    public String addSysAchievement(SysAchievementAddVO sysAchievementAddVO) throws Exception {
        SysAchievement sysAchievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementAddVO,sysAchievement);
        if (StringUtils.isEmpty(sysAchievement.getParentId())){
            sysAchievement.setParentId(null);
        }
        sysAchievement.setId(UUID.randomUUID().toString());
        int row = sysAchievementMapper.insert(sysAchievement);
        if (row != 1){
            throw new RuntimeException("操作失败！请联系管理员");
        }
        return sysAchievement.getId();
    }

    @Override
    public void updateSysAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) throws Exception {
        SysAchievement achievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementUpdateVO,achievement);
        int row = sysAchievementMapper.update(achievement);
        if (row != 1){
            throw new RuntimeException("操作失败！请联系管理员");
        }
    }

    @Override
    public void deleteSysAchievement(String id){
        List<SysAchievement> treeList = sysAchievementMapper.load(id);
        if (!treeList.isEmpty()){
            StringBuffer buffer = new StringBuffer();
            for (SysAchievement achievement : treeList) {
                buffer.append(recursionGetAchievementIds(achievement,new StringBuffer()));
            }
            String ids = buffer.toString();
            SysAchievement achievement = new SysAchievement();
            achievement.setId(ids);
            achievement.setDeleted(true);
            int row = sysAchievementMapper.update(achievement);
            if (row < 1){
                throw new RuntimeException("操作失败！请联系管理员");
            }
        }else{
            List<SysFile> files = sysFileMapper.load(id);
            if (!files.isEmpty()){
                sysFileMapper.batchLogicDelete(id);
            }
        }
    }

    /**
     * @Author Huang.zh
     * @Description 递归拼接id
     * @Date 2020/9/15 20:11
     */
    private String recursionGetAchievementIds(SysAchievement sysAchievement,StringBuffer buffer){
        buffer.append(sysAchievement.getId()+",");
        if (sysAchievement.hasChild()){
            List<SysAchievement> children = sysAchievement.getChildren();
            for (SysAchievement child : children) {
                buffer = new StringBuffer(recursionGetAchievementIds(child,buffer));
            }
        }
        return buffer.toString();
    }

    @Override
    public PageVO<SysFile> findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO) {
        PageHelper.startPage(achievementFilePageQueryVO.getPageNum(),achievementFilePageQueryVO.getPageSize());
        List<SysFile> files = sysFileMapper.load(achievementFilePageQueryVO.getAchievementFileIds());
        return PageUtils.getPageVO(files);
    }
}
