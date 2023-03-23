package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.SysAchievementJurisdictionMapper;
import com.ygkj.mapper.SysAchievementMapper;
import com.ygkj.mapper.SysUserMapper;
import com.ygkj.service.SysAchievementJurisdictionService;
import com.ygkj.vo.req.SysAchievementJurisdictionReqVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hucong
 * @description 文档权限表
 * @date 2021-01-20
 */
@Service
public class SysAchievementJurisdictionServiceImpl implements SysAchievementJurisdictionService {

    @Resource
    private SysAchievementJurisdictionMapper sysAchievementJurisdictionMapper;

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysAchievementJurisdictionMapper jurisdictionMapper;

    @Override
    public int insert(SysAchievementJurisdiction sysAchievementJurisdiction) {
        return sysAchievementJurisdictionMapper.insert(sysAchievementJurisdiction);
    }


    @Override
    public int delete(int id) {
        int ret = sysAchievementJurisdictionMapper.delete(id);
        return ret;
    }


    @Override
    public int update(SysAchievementJurisdiction sysAchievementJurisdiction) {
        int ret = sysAchievementJurisdictionMapper.update(sysAchievementJurisdiction);
        return ret;
    }


    @Override
    public SysAchievementJurisdiction load(int id) {
        return sysAchievementJurisdictionMapper.load(id);
    }


    @Override
    public PageInfo<SysAchievementJurisdiction> pageList(SysAchievementJurisdictionReqVo vo) {

        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysAchievementJurisdiction> selectAll = sysAchievementJurisdictionMapper.selectAll(vo);
        return new PageInfo<SysAchievementJurisdiction>(selectAll);
    }

    @Override
    public List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> findDataByAchievementIdAndType(String achievementId, String type) {
        return sysAchievementJurisdictionMapper.findDataByAchievementIdAndType(achievementId,type);
    }

    @Override
    public JSONObject checkPermissions(String achievementId) {
        JSONObject jsonObject = new JSONObject();
        //"查看权限 0:没有 1:有"
         String lookFlag = "0";
        //"上次权限 0:没有 1:有"
         String uploadFlag = "0";
        //"删除权限 0:没有 1:有"
         String deleteFlag = "0";
        //"删除权限 0:没有 1:有"
         String downloadFlag = "0";
        //权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限
        if (checkUserHasPer(achievementId,"1")) {
            lookFlag = "1";
        }
        if (checkUserHasPer(achievementId,"2")) {
            uploadFlag = "1";
        }
        if (checkUserHasPer(achievementId,"3")) {
            deleteFlag = "1";
        }
        if (checkUserHasPer(achievementId,"4")) {
            downloadFlag = "1";
        }
        jsonObject.put("lookFlag",lookFlag);
        jsonObject.put("uploadFlag",uploadFlag);
        jsonObject.put("deleteFlag",deleteFlag);
        jsonObject.put("downloadFlag",downloadFlag);
        return jsonObject;
    }


    /**
     * 检查用户是否有权限，没有，则返回没有权限的信息
     *
     * @param achievementId 文档id
     * @param type 权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     * @return
     */
    private Boolean checkUserHasPer(String achievementId,String type) {
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
        if (StringUtils.isNotEmpty(achievementId)) {
            List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = jurisdictionMapper.findDataByAchievementIdAndType(achievementId,type);
            if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() >0) {
                for (com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction jurisdiction : jurisdictionList) {
                    String userIds = jurisdiction.getUserIds();//授权的用户ids
                    if (StringUtils.isNotEmpty(userIds)) {
                        String[] userIdSpi = userIds.split(",");
                        for (int i = 0; i < userIdSpi.length; i++) {
                            if (userId.equals(userIdSpi[i])) {//判断用户id是否相等，相等，则有权限，返回true；反之，没有权限
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
        return flag;
    }
}

