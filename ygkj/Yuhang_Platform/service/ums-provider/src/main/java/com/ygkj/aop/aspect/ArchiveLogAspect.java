package com.ygkj.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.ygkj.aop.annotation.ArchiveLogAnnotation;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.archivelog.model.SysArchiveLog;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.mapper.SysArchiveLogMapper;
import com.ygkj.service.SysAchievementJurisdictionService;
import com.ygkj.service.SysAchievementService;
import com.ygkj.service.SysFileService;
import com.ygkj.service.UserService;
import com.ygkj.utils.HttpContextUtils;
import com.ygkj.utils.IPUtils;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/20 10:57
 */
@Aspect
@Component
@Slf4j
public class ArchiveLogAspect {

    @Autowired
    private SysArchiveLogMapper archiveLogMapper;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private SysAchievementService sysAchievementService;
    @Autowired
    private SysAchievementJurisdictionService sysAchievementJurisdictionService;
    @Autowired
    private UserService userService;

    /**
     * 此处的切点是注解的方式
     * 只要出现 @ArchiveLogAnnotation注解都会进入
     */
    @Pointcut("@annotation(com.ygkj.aop.annotation.ArchiveLogAnnotation)")
    public void logPointCut() {

    }

    /**
     * 环绕增强
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //保存日志
        try {

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysArchiveLog sysArchiveLog = new SysArchiveLog();
            ArchiveLogAnnotation archiveLogAnnotation = method.getAnnotation(ArchiveLogAnnotation.class);
            if (archiveLogAnnotation != null) {
                //注解上的描述
                sysArchiveLog.setOperationType(archiveLogAnnotation.type());
                sysArchiveLog.setStatus(archiveLogAnnotation.status());
            }

            //获取request
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            //设置IP地址
            sysArchiveLog.setIp(IPUtils.getIpAddr(request));

            String className = joinPoint.getTarget().getClass().getName();//请求的类名
            String methodName = signature.getName();//请求的方法名

            String params = null;
            //请求的参数
            Object[] args = joinPoint.getArgs();
            if (args.length != 0) {
                params = JSON.toJSONString(args);
            }

            if ("viewFile".equals(methodName)) {//文件查看
                String fileId = (String) args[0];

//                    //检查用户是否有权限，没有，则返回没有权限的信息
//                    if (!checkUserHasPer(fileId,"1")) {
//                        return CommonResult.failed(CommonCode.FORBIDDEN, "没有文件查看权限");
//                    }

                sysArchiveLog.setFileId(fileId);

                //查询档案归属
                if (fileId.contains(",")) {
                    sysArchiveLog.setArchiveName(",,,");//多文件查看时，取消保存，只保存单文件查看
                } else {
                    String archiveName = findFileFolder(fileId);
                    if (StringUtils.isNotEmpty(archiveName)) {
                        sysArchiveLog.setArchiveName(archiveName);
                    }
                }
                SysFile sysFile = findFileById(fileId);
                if (sysFile != null) {
                    sysArchiveLog.setFileName(sysFile.getFileName());
                }
            } else if ("delFile".equals(methodName)) {//文件删除
                String fileId = (String) args[0];

//                    //检查用户是否有权限，没有，则返回没有权限的信息
//                    if (!checkUserHasPer(fileId,"3")) {
//                        return CommonResult.failed(CommonCode.FORBIDDEN, "没有文件删除权限");
//                    }

                sysArchiveLog.setFileId(fileId);

                SysFile sysFile = findFileById(fileId);
                if (sysFile != null) {
                    sysArchiveLog.setFileName(sysFile.getFileName());//设置文件名
                }

                //查询档案归属
                String archiveName = findFileFolder(fileId);
                if (StringUtils.isNotEmpty(archiveName)) {
                    sysArchiveLog.setArchiveName(archiveName);
                }
            } else if ("updateFile".equals(methodName)) {//文件更新
                SysFile sysFile = (SysFile) args[0];
                String fileId = sysFile.getId();

//                    //检查用户是否有权限，没有，则返回没有权限的信息
//                    if (!checkUserHasPer(fileId,"5")) {
//                        return CommonResult.failed(CommonCode.FORBIDDEN, "没有文件更新权限");
//                    }

                if (sysFile != null) {
                    sysArchiveLog.setFileId(fileId);

                    SysFile sysFileDb = findFileById(fileId);
                    if (sysFileDb != null) {
                        sysArchiveLog.setFileName(sysFileDb.getFileName());//设置文件名
                    }

                    //查询档案归属
                    String archiveName = findFileFolder(fileId);
                    if (StringUtils.isNotEmpty(archiveName)) {
                        sysArchiveLog.setArchiveName(archiveName);
                    }
                }
            } else if ("downloadFile".equals(methodName)) {//文件下载
                String fileId = (String) args[0];

//                    //检查用户是否有权限，没有，则返回没有权限的信息
//                    if (!checkUserHasPer(fileId,"4")) {
//                        return CommonResult.failed(CommonCode.FORBIDDEN, "没有文件下载权限");
//                    }

                sysArchiveLog.setFileId(fileId);

                SysFile sysFile = findFileById(fileId);
                if (sysFile != null) {
                    sysArchiveLog.setFileName(sysFile.getFileName());//设置文件名

                    //查询档案归属
                    String archiveName = findFileFolder(fileId);
                    if (StringUtils.isNotEmpty(archiveName)) {
                        sysArchiveLog.setArchiveName(archiveName);
                    }
                }
            }

            log.info("Ip{}，接口地址{}，请求方式{}，入参：{}", sysArchiveLog.getIp(), request.getRequestURL(), request.getMethod(), params);


            long beginTime = System.currentTimeMillis();
            //执行方法
            Object result = joinPoint.proceed();
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;

            log.info("请求{}.{}耗时{}毫秒", className, methodName, time);

            if ("save".equals(methodName)) {
                SysFile sysFile = (SysFile) args[0];
                String fileId = sysFile.getId();

//                    //检查用户是否有权限，没有，则返回没有权限的信息
//                    if (!checkachievementIdUserHasPer(sysFile.getParentAchievementId(),"2")) {
//                        return CommonResult.failed(CommonCode.FORBIDDEN, "没有文件保存权限");
//                    }

                if (sysFile != null) {
                    sysArchiveLog.setFileId(fileId);

                    SysFile sysFileDb = findFileById(fileId);
                    if (sysFileDb != null) {
                        sysArchiveLog.setFileName(sysFileDb.getFileName());//设置文件名
                    }

                    //查询档案归属
                    String archiveName = findFileFolder(fileId);
                    if (StringUtils.isNotEmpty(archiveName)) {
                        sysArchiveLog.setArchiveName(archiveName);
                    }
                }

            }

            //用户名
            String token = request.getHeader(Constant.ACCESS_TOKEN);
            if (StringUtils.isNotEmpty(token)) {
                String userId = JwtTokenUtil.getUserId(token);
                String username = JwtTokenUtil.getUserName(token);
                sysArchiveLog.setCreateName(username);
                sysArchiveLog.setCreatorId(userId);
            }
            sysArchiveLog.setCreateTime(new Date());
            if (!",,,".equals(sysArchiveLog.getArchiveName())) {
                archiveLogMapper.insert(sysArchiveLog);
                log.info(sysArchiveLog.toString());
            }
            return result;
        } catch (Exception e) {
            log.error("e={}", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 根据文件id查找文件
     *
     * @param fileId
     * @return
     */
    private SysFile findFileById(String fileId) {
        List<SysFile> files = sysFileService.load(fileId);
        if (CollectionUtils.isNotEmpty(files) && files.size() > 0) {
            return files.get(0);
        }
        return null;
    }

    /**
     * 查找文件所处目录
     *
     * @param fileIds
     * @return
     */
    private String findFileFolder(String fileIds) {
        List<SysAchievement> sysAchievementList = sysAchievementService.findAchievementByFileId(fileIds);
        if (CollectionUtils.isNotEmpty(sysAchievementList) && sysAchievementList.size() > 0) {
            if (sysAchievementList.size() == 1) {
                SysAchievement sysAchievement = sysAchievementList.get(0);
                //只有一个文件夹且是最顶级父文件，直接返回
                if ("0".equals(sysAchievement.getParentId())) {
                    return sysAchievement.getName();
                }
                StringBuffer buffer = new StringBuffer();
                buffer.append(sysAchievement.getName());
                String archiveName = getParentFolder(buffer, sysAchievement);
                return archiveName;
            }
        }
        return null;
    }

    /**
     * 递归查询父文件，并拼接
     *
     * @param buffer
     * @param sysAchievement
     * @return
     */
    private String getParentFolder(StringBuffer buffer, SysAchievement sysAchievement) {
        String parentId = sysAchievement.getParentId();
        if (StringUtils.isNotEmpty(parentId)) {
            SysAchievement parentAchi = sysAchievementService.findById(parentId);
            if (parentAchi != null) {
                buffer.append("/" + parentAchi.getName());
                getParentFolder(buffer, parentAchi);
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
            List<SysAchievement> sysAchievements = sysAchievementService.findAchievementByFileId(fileId);
            if (CollectionUtils.isNotEmpty(sysAchievements) && sysAchievements.size() > 0) {
                for (SysAchievement sysAchievement : sysAchievements) {
                    String achievementId = sysAchievement.getId();//文件夹id
                    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = sysAchievementJurisdictionService.findDataByAchievementIdAndType(achievementId, type);
                    if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() > 0) {
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
                                Integer num = userService.findDeportUser(deportIds, userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
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


    /**
     * 检查用户文件夹权限
     *
     * @param achievementId
     * @param type
     * @return
     */
    private Boolean checkachievementIdUserHasPer(String achievementId, String type) {
        boolean flag = false;
        if (StringUtils.isEmpty(achievementId)) {
            return flag;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (StringUtils.isEmpty(token)) {
            return flag;
        }
        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isEmpty(userId)) {
            return flag;
        }
        List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> jurisdictionList = sysAchievementJurisdictionService.findDataByAchievementIdAndType(achievementId, type);
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
                    Integer num = userService.findDeportUser(deportIds, userId);//查找当前请求的用户在授权的部门，大于0，则是；反之不是
                    if (num > 0) {
                        return true;
                    }
                }
            }
        }
        return flag;
    }

}
