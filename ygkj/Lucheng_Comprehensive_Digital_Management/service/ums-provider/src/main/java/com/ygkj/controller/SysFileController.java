package com.ygkj.controller;

import com.alibaba.excel.util.DateUtils;
import com.ygkj.aop.annotation.ArchiveLogAnnotation;
import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.auth.api.file.model.SysFileControllerApi;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysAchievementService;
import com.ygkj.service.SysFileService;
import com.ygkj.vo.req.SysFileReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Huang.zh
 * @date 2020/11/26 16:38
 * @Description:
 */
@RestController
@RequestMapping("/ums/sysFile")
public class SysFileController implements SysFileControllerApi {

    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private SysAchievementService sysAchievementService;

    @GetMapping("")
    @Override
    public CommonResult files(@RequestParam(value = "fileIds", required = false) String fileIds) {
        return CommonResult.success(sysFileService.load(fileIds));
    }

    @PostMapping("/archivalDataStatistics")
    @Override
    public CommonResult archivalDataStatistics() {
        return sysFileService.archivalDataStatistics();
    }

    @PostMapping("/statUploadFile")
    @Override
    public CommonResult statUploadFile(@RequestParam(value = "year", required = false) Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        return sysFileService.statUploadFile(year);
    }

    @PostMapping("/fileParameterType")
    @Override
    public CommonResult fileParameterType() {
        return sysFileService.fileParameterType();
    }


    /**
     * 查询 分页查询
     *
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/pageList")
    @ApiOperation(value = "分页查询接口")
    public CommonResult pageList(SysFileReqVo vo) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        if (StringUtils.isNotEmpty(token)) {
            String userId = JwtTokenUtil.getUserId(token);
            vo.setUserId(userId);
        }
        return CommonResult.success(sysFileService.pageList(vo));
    }


    @ApiOperation(value = "文件删除")
    @PostMapping("/delFile")
    @ArchiveLogAnnotation(type = "文件删除", status = "3")
    @LogAnnotation(title = "档案管理",action = "删除文件")
    public CommonResult delFile(String id) {
        try {
            int flag = sysFileService.del(id);
            if (flag > 0) {
                return CommonResult.success("删除成功");
            } else {
                return CommonResult.failed("删除成功");
            }
        } catch (Exception e) {
            throw new RuntimeException("系统错误,请联系管理员");
        }
    }

    @ApiOperation(value = "文件更新")
    @PostMapping("/updateFile")
    @ArchiveLogAnnotation(type = "文件更新", status = "5")
    @Transactional(rollbackFor = Exception.class)
    @LogAnnotation(title = "档案管理",action = "更新文件")
    public CommonResult updateFile(SysFile entity,String achievementId,String AfterachievementId) {
        try {
            sysAchievementService.editFileOfSysAchievement(entity.getId(),achievementId,AfterachievementId);
            int flag = sysFileService.update(entity);
            if (flag > 0) {
                return CommonResult.success("修改成功");
            } else {
                return CommonResult.failed("修改成功");
            }
        } catch (Exception e) {
            throw new RuntimeException("系统错误,请联系管理员");
        }
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/downloadFile")
    @ArchiveLogAnnotation(type = "文件下载", status = "4")
    public CommonResult downloadFile(String id) {
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            List<SysFile> sysFiles = sysFileService.load(id);
            if (CollectionUtils.isNotEmpty(sysFiles) && sysFiles.size() > 0) {
                SysFile sysFile = sysFiles.get(0);
                String fileName = sysFile.getFileName();
                String filePath = sysFile.getFileUrl();
                String fileType = sysFile.getFileType();
                // 构造URL
                URL weburl = new URL(filePath);
                // 打开连接
                URLConnection con = weburl.openConnection();
                // 设置请求超时为5s
                con.setConnectTimeout(5 * 1000);
                // 输入流
                InputStream is = con.getInputStream();
                BufferedInputStream br = new BufferedInputStream(is);
                byte[] buf = new byte[1024];
                int len = 0;

                boolean isOnLine = false;
                response.reset(); // 非常重要
                if (isOnLine) { // 在线打开方式
                    URL u = new URL("file:///" + filePath);
                    response.setContentType(u.openConnection().getContentType());
                    response.setHeader("Content-Disposition", "inline; filename=" + fileName);
                    // 文件名应该编码成UTF-8
                } else { // 纯下载方式
                    response.setContentType("application/x-msdownload");
                    response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + "." + fileType);
                }
                OutputStream out = response.getOutputStream();
                while ((len = br.read(buf)) > 0)
                    out.write(buf, 0, len);
                br.close();
                out.close();
            }
            return CommonResult.success("");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "文件查看")
    @PostMapping("/viewFile")
    @ArchiveLogAnnotation(type = "文件查看", status = "1")
    public CommonResult viewFile(String id) {
        return CommonResult.success("访问成功");
    }

    @ApiOperation(value = "文件保存")
    @PostMapping("/save")
    @ArchiveLogAnnotation(type = "文件上传", status = "2")
    @LogAnnotation(title = "档案管理",action = "保存文件")
    public CommonResult save(SysFile entity) {
        try {
            String achievementName = getParentFolder(entity.getParentAchievementId());
            if (StringUtils.isNotEmpty(achievementName)) {
                entity.setAchievementName(achievementName);
            }
            entity.setId(UUID.randomUUID().toString());
            entity.setFileCode(entity.getFileCode());
            sysFileService.saveFile(entity);
            return CommonResult.success("修改成功");
        } catch (Exception e) {
            throw new RuntimeException("系统错误,请联系管理员");
        }
    }

    /**
     * 查找文件目录归属
     *
     * @param parentAchievementId
     * @return
     */
    private String getParentFolder(String parentAchievementId) {
        StringBuffer buffer = new StringBuffer();
        String parentId = parentAchievementId;
        if (StringUtils.isNotEmpty(parentId)) {
            SysAchievement parentAchi = sysAchievementService.findById(parentId);
            if (parentAchi != null) {
                buffer.append("/" + parentAchi.getName());
                getParentFolder(parentAchi.getParentId());
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

}
