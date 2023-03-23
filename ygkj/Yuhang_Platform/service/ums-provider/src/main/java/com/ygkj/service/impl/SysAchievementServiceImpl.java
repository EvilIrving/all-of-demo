package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.auth.api.user.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.*;
import com.ygkj.service.SysAchievementService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:14
 * @Description: 归档管理服务层具体实现
 */
@Service("sysAchievementService")
public class SysAchievementServiceImpl implements SysAchievementService {
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysFileMapper sysFileMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAchievementJurisdictionMapper jurisdictionMapper;

    @Override
    public List<SysAchievement> selectAll(String name) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(name)) {
            return sysAchievementMapper.selectAll();
        } else {
            List<SysAchievement> sysAchievements = sysAchievementMapper.selectAllByName(name);
            return sysAchievements;
        }
    }

    @Override
    public String addSysAchievement(SysAchievementAddVO sysAchievementAddVO) throws Exception {
        SysAchievement sysAchievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementAddVO, sysAchievement);
        if (StringUtils.isEmpty(sysAchievement.getParentId())) {
            sysAchievement.setParentId(null);
        }
        sysAchievement.setId(UUID.randomUUID().toString());
        int row = sysAchievementMapper.insert(sysAchievement);
        if (row != 1) {
            throw new RuntimeException("操作失败！请联系管理员");
        }
        return sysAchievement.getId();
    }

    @Override
    public void updateSysAchievement(SysAchievementUpdateVO sysAchievementUpdateVO) throws Exception {
        SysAchievement achievement = new SysAchievement();
        BeanUtils.copyProperties(sysAchievementUpdateVO, achievement);
        int row = sysAchievementMapper.update(achievement);
        if (row != 1) {
            throw new RuntimeException("操作失败！请联系管理员");
        }
    }

    @Override
    public void deleteSysAchievement(String id) {
        List<SysAchievement> treeList = sysAchievementMapper.load(id);
        if (!treeList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            for (SysAchievement achievement : treeList) {
                buffer.append(recursionGetAchievementIds(achievement, new StringBuffer()));
            }
            String ids = buffer.toString();
            SysAchievement achievement = new SysAchievement();
            achievement.setId(ids);
            achievement.setDeleted(true);
            int row = sysAchievementMapper.update(achievement);
            if (row < 1) {
                throw new RuntimeException("操作失败！请联系管理员");
            }
        } else {
            List<SysFile> files = sysFileMapper.load(id);
            if (!files.isEmpty()) {
                sysFileMapper.batchLogicDelete(id);
            }
        }
    }

    /**
     * @Author Huang.zh
     * @Description 递归拼接id
     * @Date 2020/9/15 20:11
     */
    private String recursionGetAchievementIds(SysAchievement sysAchievement, StringBuffer buffer) {
        buffer.append(sysAchievement.getId() + ",");
        if (sysAchievement.hasChild()) {
            List<SysAchievement> children = sysAchievement.getChildren();
            for (SysAchievement child : children) {
                buffer = new StringBuffer(recursionGetAchievementIds(child, buffer));
            }
        }
        return buffer.toString();
    }

    @Override
    public PageVO<SysFile> findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO) {
        PageHelper.startPage(achievementFilePageQueryVO.getPageNum(), achievementFilePageQueryVO.getPageSize());
        List<SysFile> files = sysFileMapper.load(achievementFilePageQueryVO.getAchievementFileIds());
        return PageUtils.getPageVO(files);
    }

    @Override
    public CommonResult statisticsTotalArchives() {
        try {
            List<JSONObject> jsonObjectList = sysAchievementMapper.statisticsTotalArchives();
            return CommonResult.success(jsonObjectList);
        } catch (Exception e) {
            return CommonResult.success(e.getMessage());
        }
    }

    @Override
    public List<SysAchievement> achievementStructure() {
        List<SysAchievement> sysAchievements = sysAchievementMapper.selectAll();
        checkJurisdiction(sysAchievements);
        List<SysAchievement> list = sysAchievements.stream().filter(e -> "0".equals(e.getId()))
                .map((entity) -> {
                    entity.setChildren(getChildrens(entity, sysAchievements));
                    return entity;
                })
                .sorted((s1, s2) -> {
                    return (s1.getSort() == null ? 0 : s1.getSort()) - (s2.getSort() == null ? 0 : s2.getSort());
                }).collect(Collectors.toList());
//        SysAchievement sysAchievement = new SysAchievement();
//        sysAchievement.setName("全部文件夹");
//        sysAchievement.setId("0");
//        sysAchievement.setChildren(list);
//        List<SysAchievement> res = new ArrayList<>();
//        res.add(sysAchievement);
        return list;
    }

    /**
     * 检查访问用户得文件夹权限
     * @param sysAchievements
     */
    private void checkJurisdiction(List<SysAchievement> sysAchievements) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (ygkj.com.util.StringUtils.isEmpty(token)) {
            return;
        }
        String userId = JwtTokenUtil.getUserId(token);
        if (ygkj.com.util.StringUtils.isEmpty(userId)) {
            return;
        }
        for (SysAchievement sysAchievement : sysAchievements) {
            //权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限
            if (checkachievementIdUserHasPer(sysAchievement.getId(),"1",userId)) {
                sysAchievement.setLookFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(),"2",userId)) {
                sysAchievement.setUploadFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(),"3",userId)) {
                sysAchievement.setDeleteFlag("1");
            }

            if (checkachievementIdUserHasPer(sysAchievement.getId(),"4",userId)) {
                sysAchievement.setDownloadFlag("1");
            }

        }
    }

    /**
     * 检查用户文件夹权限
     * @param achievementId
     * @param type
     * @return
     */
    private Boolean checkachievementIdUserHasPer(String achievementId,String type ,String userId) {
        boolean flag = false;
        List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = jurisdictionMapper.findDataByAchievementIdAndType(achievementId,type);
        if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() >0) {
            for (com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction jurisdiction : jurisdictionList) {
                String userIds = jurisdiction.getUserIds();//授权的用户ids
                if (ygkj.com.util.StringUtils.isNotEmpty(userIds)) {
                    String[] userIdSpi = userIds.split(",");
                    for (int i = 0; i < userIdSpi.length; i++) {
                        if (userId.equals(userIdSpi[i])) {//判断用户id是否相等，相等，则有权限，返回true；反之，没有权限
                            return true;
                        }
                    }
                }
                String deportIds = jurisdiction.getDeportIds();//授权的单位id
                if (ygkj.com.util.StringUtils.isNotEmpty(deportIds)) {
                    Integer num = sysUserMapper.findDeportUser(deportIds,userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
                    if (num > 0) {
                        return true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 全部数据,递归查找文档子结构
     *
     * @param root
     * @param all
     * @return
     */
    private List<SysAchievement> getChildrens(SysAchievement root, List<SysAchievement> all) {

        List<SysAchievement> list = all.stream().filter(e -> e.getParentId().equals(root.getId().toString()))
                .map(sysAchievement -> {
                    sysAchievement.setChildren(getChildrens(sysAchievement, all));
                    return sysAchievement;
                })
                .sorted((s1, s2) -> {
                    return (s1.getSort() == null ? 0 : s1.getSort()) - (s2.getSort() == null ? 0 : s2.getSort());
                }).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<SysAchievement> findAchievementByFileId(String fileId) {
        return sysAchievementMapper.findAchievementByFileId(fileId);
    }

    @Override
    public SysAchievement findById(String id) {
        List<SysAchievement> achievementList = sysAchievementMapper.load(id);
        if (CollectionUtils.isNotEmpty(achievementList) && achievementList.size() >0) {
            return achievementList.get(0);
        }
        return null;
    }

    @Override
    public PageInfo<SysAchievement> selectDataAll(AchievementFilePageQueryVO vo) {
        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysAchievement> selectAll = sysAchievementMapper.selectDataAll(vo);
        if (CollectionUtils.isNotEmpty(selectAll) && selectAll.size() >0) {
            for (SysAchievement achievement : selectAll) {
                if (ygkj.com.util.StringUtils.isNotEmpty(achievement.getUserIds())) {
                    List<SysUser> users = sysUserMapper.findByIds(achievement.getUserIds());
                    if (CollectionUtils.isNotEmpty(users) && users.size() >0) {
                        achievement.setUserList(users);
                    }
                }

                if (ygkj.com.util.StringUtils.isNotEmpty(achievement.getDeportIds())) {
                    List<SysDept> sysDeptList = sysDeptMapper.findByIds(achievement.getDeportIds());
                    if (CollectionUtils.isNotEmpty(sysDeptList) && sysDeptList.size() >0) {
                        achievement.setDeptList(sysDeptList);
                    }
                }

                List<SysAchievementJurisdiction> jurisdictionListction = jurisdictionMapper
                        .findDataByAchievementIdAndType(achievement.getId(), null);
                achievement.setJurisdictionListction(jurisdictionListction);
            }
        }
        return new PageInfo<SysAchievement>(selectAll);
    }

    @Override
    public int insert(SysAchievement entity) {
        return sysAchievementMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return sysAchievementMapper.delete(id);
    }

    @Override
    public int update(SysAchievement entity) {
        return sysAchievementMapper.update(entity);
    }
}
