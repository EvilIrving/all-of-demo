package com.ygkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.aop.annotation.ArchiveLogAnnotation;
import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.AppAchievementReqVo;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysAchievementService;
import com.ygkj.service.SysFileService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:10
 * @Description: 归档管理服务控制层
 */
@RestController
@RequestMapping("/ums/sysAchievement")
@Api(value = "归档服务", tags = "归档服务")
public class SysAchievementController {

    @Autowired
    private SysAchievementService sysAchievementService;
    @Autowired
    private SysFileService fileService;

    @GetMapping("")
    public CommonResult sysAchievement(String name) {
        return CommonResult.success(sysAchievementService.selectAll(name));
    }

    @PostMapping("")
    public CommonResult addSysAchievement(@RequestBody SysAchievementAddVO sysAchievementAddVO) {
        try {
            String id = sysAchievementService.addSysAchievement(sysAchievementAddVO);
            return CommonResult.success(id);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PutMapping("")
    public CommonResult updateSysAchievement(@RequestBody SysAchievementUpdateVO sysAchievementUpdateVO) {
        try {
            sysAchievementService.updateSysAchievement(sysAchievementUpdateVO);
            return CommonResult.success("");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @DeleteMapping("")
    public CommonResult deleteSysAchievement(String id) {
        try {
            sysAchievementService.deleteSysAchievement(id);
            return CommonResult.success("");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/files")
    public CommonResult getAchievementFiles(@RequestBody AchievementFilePageQueryVO achievementFilePageQueryVO) {
        return CommonResult.success(sysAchievementService.findFiles(achievementFilePageQueryVO));
    }

    @PostMapping("/pageList")
    @ApiOperation(value = "分页查询接口")
    public CommonResult<PageInfo<SysAchievement>> pageList(AchievementFilePageQueryVO vo) {
        return CommonResult.success(sysAchievementService.selectDataAll(vo));
    }

    /**
     * 档案总量统计
     *
     * @return
     */
    @ApiOperation("档案总量统计")
    @PostMapping("/statisticsTotalArchives")
    public CommonResult statisticsTotalArchives() {
        return sysAchievementService.statisticsTotalArchives();
    }

    @ApiOperation("档案结构")
    @PostMapping("/achievementStructure")
    @ApiImplicitParam(name = "fileName", value = "文件名", paramType = "query")
    @ArchiveLogAnnotation(type = "档案结构", status = "1")
    public CommonResult achievementStructure(String projectCode,HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader("authorization"));
        List<SysAchievement> list=sysAchievementService.achievementStructure(projectCode,userId);
        if((list.size()>0&&"".equals(projectCode))||(list.size()>0&&projectCode==null)){
            return CommonResult.success(list);
        }else {
            return CommonResult.success(list.get(0).getChildren());
        }
    }


    @ApiOperation("app档案结构")
    @PostMapping("/appAchievement")
    @ArchiveLogAnnotation(type = "app档案结构", status = "1")
    public CommonResult appAchievement(AppAchievementReqVo reqVo) {
        return CommonResult.success(sysAchievementService.appAchievement(reqVo));

    }

//    @ApiOperation("档案督办")
//    @PostMapping("/achievementOversee")
//    @ApiImplicitParam(name = "id", value = "档案id")
//    @ArchiveLogAnnotation(type = "档案督办",status = "1")
//    public CommonResult achievementOversee(String id){
//        if (StringUtils.isEmpty(id)) {
//            return CommonResult.failed("档案id为空");
//        }
//        Integer flag = sysAchievementService.achievementOversee(id);//1:发送成功 2:发送失败 3: 不需要督办
//        if (flag == 1) {
//            return CommonResult.success("发送成功");
//        } else if (flag == 2) {
//            return CommonResult.failed("发送成功");
//        } else {
//            return CommonResult.success("无效督办");
//        }
//    }

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增接口")
    @Transactional(rollbackFor = Exception.class)
    @LogAnnotation(title = "权限管理",action = "新增文件夹")
    public CommonResult insert(SysAchievement entity) {
        try {
            sysAchievementService.findSysAchievementAchiPath(entity);//查找档案路径
            if (StringUtils.isEmpty(entity.getId())) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String token = request.getHeader("authorization");
                if (StringUtils.isNotEmpty(token)) {
                    String userId = JwtTokenUtil.getUserId(token);
                    entity.setCreateId(userId);
                }
                entity.setId(UUID.randomUUID().toString());
                entity.setCreateTime(new Date());
                int i = sysAchievementService.insert(entity);
                if (i > 0) {
                    return CommonResult.success("新增成功");
                } else {
                    return CommonResult.failed("新增失败");
                }
            } else {
                SysAchievement achievement = sysAchievementService.findById(entity.getId());
                if (achievement == null) {
                    return CommonResult.failed("保存失败");
                }
                String parentFolder = getParentFolder(achievement.getId(), achievement.getName());
                StringBuffer stringBuffer = new StringBuffer();
                if (StringUtils.isNotEmpty(entity.getFileStr())) {
                    String fileStr = entity.getFileStr().replace("\\","");
                    List<SysFile> fileList = JSONObject.parseArray(fileStr, SysFile.class);
                    if (CollectionUtils.isNotEmpty(fileList) && fileList.size() > 0) {
                        for (SysFile sysFile : fileList) {
                            sysFile.setAchievementName(parentFolder);
                            if (stringBuffer.length() == 0) {
                                stringBuffer.append(sysFile.getId());
                            } else {
                                stringBuffer.append("," + sysFile.getId());
                            }
                            if (StringUtils.isNotEmpty(entity.getLabelIds())) {
                                sysFile.setLabelIds(entity.getLabelIds());
                            }
                            if (StringUtils.isNotEmpty(sysFile.getId())) {
                                fileService.update(sysFile);
                            }
                        }
                    }
                }
                String fileIds = achievement.getFileIds();
                if (StringUtils.isEmpty(fileIds)) {
                    entity.setFileIds(stringBuffer.toString());
                } else {
                    entity.setFileIds(fileIds + "," + stringBuffer.toString());
                }
                int i = sysAchievementService.update(entity);
                if (i > 0) {
                    return CommonResult.success("更新成功");
                } else {
                    return CommonResult.failed("更新失败");
                }
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除接口")
    @LogAnnotation(title = "权限管理",action = "删除文件夹")
    public CommonResult delete(String id) {
        try {
            int i = sysAchievementService.delete(id);
            if (i > 0) {
                return CommonResult.success("刪除成功");
            } else {
                return CommonResult.failed("刪除失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }


    /**
     * 更新
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新接口")
    @LogAnnotation(title = "权限管理",action = "更新文件夹")
    public CommonResult update(SysAchievement entity) {
        try {
            int i = sysAchievementService.update(entity);
            if (i > 0) {
                return CommonResult.success("更新成功");
            } else {
                return CommonResult.failed("更新失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查找文件目录归属
     *
     * @param parentAchievementId
     * @return
     */
    private String getParentFolder(String parentAchievementId, String achievementName) {
        StringBuffer buffer = new StringBuffer();
        String parentId = parentAchievementId;
        if (StringUtils.isNotEmpty(parentId)) {
            SysAchievement parentAchi = sysAchievementService.findById(parentId);
            if (parentAchi != null) {
                buffer.append("/" + parentAchi.getName());
                getPFolder(parentAchi.getParentId(), buffer);
            }
        }
        StringBuffer newBuffer = new StringBuffer();
        String data = buffer.toString();
        String[] split = data.split("/");
        //倒序组合
        for (int i = split.length - 1; i >= 0; i--) {
            if (newBuffer.length() == 0) {
                newBuffer.append(split[i]);
            } else {
                newBuffer.append("/" + split[i]);
            }
        }
        return newBuffer.toString();
    }

    private void getPFolder(String parentId, StringBuffer buffer) {
        SysAchievement parentAchi = sysAchievementService.findById(parentId);
        if (parentAchi == null) {
            return;
        }
        buffer.append("/" + parentAchi.getName());
        getPFolder(parentAchi.getParentId(), buffer);
    }
}
