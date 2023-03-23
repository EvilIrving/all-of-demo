package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.SysAchievementJurisdictionMapper;
import com.ygkj.mapper.SysAchievementMapper;
import com.ygkj.mapper.SysFileMapper;
import com.ygkj.mapper.SysUserMapper;
import com.ygkj.service.SysFileService;
import com.ygkj.vo.req.SysFileReqVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:47
 * @Description: 文件服务service层具体实现
 */
@Service("sysFileService")
public class SysFileServiceImpl implements SysFileService {
    @Autowired
    private SysFileMapper sysFileMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysAchievementMapper sysAchievementMapper;
    @Autowired
    private SysAchievementJurisdictionMapper jurisdictionMapper;

    @Override
    public void saveFile(SysFile sysFile) throws Exception {
        int row = sysFileMapper.insert(sysFile);
        if (row != 1) {
            throw new RuntimeException("操作失败，请联系管理员！");
        }
    }

    @Override
    public List<SysFile> load(String ids) {
        return sysFileMapper.load(ids);
    }

    @Override
    public CommonResult statUploadFile(Integer year) {
        try {
            List<JSONObject> jsonObjectList = sysFileMapper.statUploadFile(year);
            return CommonResult.success(jsonObjectList);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 档案数据统计
     *
     * @return
     */
    @Override
    public CommonResult archivalDataStatistics() {
        try {
            List<JSONObject> dataStatistics = sysFileMapper.archivalDataStatistics();
            Map<String, Object> data = new HashMap<>();
            Integer archiveNum = 0;//档案文件数量
            Integer imgNum = 0;//图像文件数量
            Integer videoNum = 0;//视频影像文件数量
            Integer zipNum = 0;//zip文件数量
            Integer cadNum = 0;//cad文件数量
            Integer otherNum = 0;//其他文件数量
            if (CollectionUtils.isNotEmpty(dataStatistics) && dataStatistics.size() > 0) {
                for (JSONObject jo : dataStatistics) {
                    int num = jo.getIntValue("num");
                    String fileType = jo.getString("fileType");
                    if ("pdf".equals(fileType) || "xls".equals(fileType) || "xlsx".equals(fileType) || "docx".equals(fileType) || "txt".equals(fileType) || "doc".equals(fileType)) {
                        archiveNum += num;
                    } else if ("gif".equals(fileType) || "jpeg".equals(fileType) || "jpg".equals(fileType) || "png".equals(fileType)) {
                        imgNum += num;
                    } else if ("mp4".equals(fileType) || "avi".equals(fileType)) {
                        videoNum += num;
                    } else if ("zip".equals(fileType) || "rar".equals(fileType)) {
                        zipNum += num;
                    } else if ("cad".equals(fileType)) {
                        cadNum += num;
                    } else {
                        otherNum += num;
                    }
                }
            }
            data.put("archiveNum", archiveNum);
            data.put("imgNum", imgNum);
            data.put("videoNum", videoNum);
            data.put("zipNum", zipNum);
            data.put("cadNum", cadNum);
            data.put("otherNum", otherNum);
            return CommonResult.success(data);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

    }


    @Override
    public CommonResult fileParameterType() {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            List<String> times = sysFileMapper.findFileGroupTimes();
            List<String> types = sysFileMapper.findFileGroupTypes();
            result.put("times", times);
            result.put("types", types);
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.success(e.getMessage());
        }

    }

    @Override
    public PageInfo<SysFile> pageList(SysFileReqVo vo) {
        if (!vo.isFetchAll()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysFile> selectAll = sysFileMapper.selectAll(vo);
        if (CollectionUtils.isNotEmpty(selectAll) && selectAll.size() > 0) {
            for (SysFile sysFile : selectAll) {
                //权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限
                if (checkUserHasPer(sysFile.getId(), "1")) {
                    sysFile.setLookFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(), "2")) {
                    sysFile.setUploadFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(), "3")) {
                    sysFile.setDeleteFlag("1");
                }

                if (checkUserHasPer(sysFile.getId(), "4")) {
                    sysFile.setDownloadFlag("1");
                }
            }
        }
        return new PageInfo(selectAll);
    }

    /**
     * 检查用户是否有权限，没有，则返回没有权限的信息
     *
     * @param fileId 文件id
     * @param type   权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     * @return
     */
    private Boolean checkUserHasPer(String fileId, String type) {
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
            if (CollectionUtils.isNotEmpty(sysAchievements) && sysAchievements.size() > 0) {
                for (SysAchievement sysAchievement : sysAchievements) {
                    String achievementId = sysAchievement.getId();//文件夹id
                    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = jurisdictionMapper.findDataByAchievementIdAndType(achievementId, type);
                    if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() > 0) {
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
                                Integer num = userMapper.findDeportUser(deportIds, userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
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
    public int del(String id) {
        return sysFileMapper.del(id);
    }

    @Override
    public int update(SysFile entity) {
        return sysFileMapper.update(entity);
    }
}