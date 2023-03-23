package com.ygkj.controller;

import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.service.SysAchievementService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:10
 * @Description: 归档管理服务控制层
 */
@RestController
@RequestMapping("/sysAchievement")
public class SysAchievementController {

    @Autowired
    private SysAchievementService sysAchievementService;

    @GetMapping("")
    public CommonResult sysAchievement(String name){
        return CommonResult.success(sysAchievementService.selectAll(name));
    }

    @PostMapping("")
    public CommonResult addSysAchievement(@RequestBody SysAchievementAddVO sysAchievementAddVO){
        try {
            String id = sysAchievementService.addSysAchievement(sysAchievementAddVO);
            return CommonResult.success(id);
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
    @PutMapping("")
    public CommonResult updateSysAchievement(@RequestBody SysAchievementUpdateVO sysAchievementUpdateVO){
        try {
            sysAchievementService.updateSysAchievement(sysAchievementUpdateVO);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @DeleteMapping("")
    public CommonResult deleteSysAchievement(String id){
        try {
            sysAchievementService.deleteSysAchievement(id);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/files")
    public CommonResult getAchievementFiles(@RequestBody AchievementFilePageQueryVO achievementFilePageQueryVO){
        return CommonResult.success(sysAchievementService.findFiles(achievementFilePageQueryVO));
    }
}
