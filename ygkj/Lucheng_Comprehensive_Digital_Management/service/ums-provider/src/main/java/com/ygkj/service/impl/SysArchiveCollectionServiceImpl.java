package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.SysAchievementJurisdictionMapper;
import com.ygkj.mapper.SysAchievementMapper;
import com.ygkj.mapper.SysArchiveCollectionMapper;
import com.ygkj.mapper.SysUserMapper;
import com.ygkj.service.SysArchiveCollectionService;
import com.ygkj.vo.req.SysFileReqVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hucong
 * @description 档案收藏表
 * @date 2021-01-19
 */
@Service
public class SysArchiveCollectionServiceImpl implements SysArchiveCollectionService {

    @Resource
    private SysArchiveCollectionMapper sysArchiveCollectionMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysAchievementJurisdictionMapper jurisdictionMapper;

    @Override
    public int insert(SysArchiveCollection sysArchiveCollection) {
        return sysArchiveCollectionMapper.insert(sysArchiveCollection);
    }


    @Override
    public int delete(int id) {
        int ret = sysArchiveCollectionMapper.delete(id);
        return ret;
    }


    @Override
    public int update(SysArchiveCollection sysArchiveCollection) {
        int ret = sysArchiveCollectionMapper.update(sysArchiveCollection);
        return ret;
    }


    @Override
    public SysArchiveCollection load(int id) {
        return sysArchiveCollectionMapper.load(id);
    }


    @Override
    public PageInfo<SysFile> pageList(SysFileReqVo vo) {
        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysFile> selectAll = sysArchiveCollectionMapper.selectAll(vo);
        if (CollectionUtils.isNotEmpty(selectAll) && selectAll.size() >0) {
            for (SysFile sysFile : selectAll) {
                //权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限
                if (checkUserHasPer(sysFile.getId(),"1")) {
                    sysFile.setLookFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(),"2")) {
                    sysFile.setUploadFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(),"3")) {
                    sysFile.setDeleteFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(),"4")) {
                    sysFile.setDownloadFlag("1");
                }
            }
        }
        return new PageInfo<SysFile>(selectAll);
    }

    /**
     * 检查用户是否有权限，没有，则返回没有权限的信息
     *
     * @param fileId 文件id
     * @param type 权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     * @return
     */
    private Boolean checkUserHasPer(String fileId,String type) {
        boolean flag = false;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (StringUtils.isEmpty(token)) {
            return flag;
        }
        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isEmpty(userId)) {
            return flag;
        }
        if (StringUtils.isNotEmpty(fileId)) {
            List<SysAchievement> sysAchievements = sysAchievementMapper.findAchievementByFileId(fileId);
            if (CollectionUtils.isNotEmpty(sysAchievements) && sysAchievements.size() >0) {
                for (SysAchievement sysAchievement : sysAchievements) {
                    String achievementId = sysAchievement.getId();//文件夹id
                    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = jurisdictionMapper.findDataByAchievementIdAndType(achievementId,type);
                    if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() >0) {
                        for (com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction jurisdiction : jurisdictionList) {
                            String userIds = jurisdiction.getUserIds();//授权的用户ids
                            if (StringUtils.isNotEmpty(userIds)) {
                                String[] userIdSpi = userIds.split(",");
                                for (int i = 0; i < userIdSpi.length; i++) {
                                    if (userId.equals(userIdSpi)) {//判断用户id是否相等，相等，则有权限，返回true；反之，没有权限
                                        return true;
                                    }
                                }
                            }
                            String deportIds = jurisdiction.getDeportIds();//授权的单位id
                            if (StringUtils.isNotEmpty(deportIds)) {
                                Integer num = userMapper.findDeportUser(deportIds,userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
                                if (num > 0) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public int cancelCollection(String userId, String achievementId, String fileId) {
        return sysArchiveCollectionMapper.cancelCollection(userId,achievementId,fileId);
    }
}

