package com.ygkj.auth.api.achievement;

import com.ygkj.auth.VO.request.AchievementFilePageQueryVO;
import com.ygkj.auth.VO.request.SysAchievementAddVO;
import com.ygkj.auth.VO.request.SysAchievementUpdateVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/9/15 17:19
 * @Description:
 */
@Api(tags = "归档服务")
public interface SysAchievementApi {

    @ApiOperation("获取归档目录（树形结构）")
    CommonResult selectAll(String name);
    @ApiOperation("添加归档目录信息")
    CommonResult addAchievement(SysAchievementAddVO sysAchievementAddVO);
    @ApiOperation("更新归档目录信息")
    CommonResult updateAchievement(SysAchievementUpdateVO sysAchievementUpdateVO);
    @ApiOperation("删除指定的目录")
    CommonResult deleteSysAchievement(String id);
    @ApiOperation("查找目录下的文件")
    CommonResult findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO);
}
