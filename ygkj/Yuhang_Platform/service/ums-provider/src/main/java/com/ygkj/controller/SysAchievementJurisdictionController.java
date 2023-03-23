package com.ygkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.DeptService;
import com.ygkj.service.SysAchievementJurisdictionService;
import com.ygkj.service.UserService;
import com.ygkj.vo.req.SysAchievementJurisdictionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description 文档权限表
 * @author hucong
 * @date 2021-01-20
 */
@RestController
@RequestMapping(value = "/ums/SysAchievementJurisdiction")
@Api(value = "文档权限服务",tags = "文档权限服务")
public class SysAchievementJurisdictionController {

    @Resource
    private SysAchievementJurisdictionService sysAchievementJurisdictionService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 新增
     * @author hucong
     * @date 2021/01/20
     **/
    @PostMapping("/insert")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("新增或修改")
    public CommonResult insert(String data){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("authorization");

//            entity.setCreateTime(new Date());
//            int i = sysAchievementJurisdictionService.insert(entity);
//            if (i > 0) {
//                return CommonResult.success("新增成功");
//            } else {
//                entity.setUpdateTime(new Date());
//                return CommonResult.failed("新增失败");
//            }
            List<SysAchievementJurisdiction> jurisdictionList = JSONObject.parseArray(data, SysAchievementJurisdiction.class);
            if (CollectionUtils.isNotEmpty(jurisdictionList) && jurisdictionList.size() >0) {
                for (SysAchievementJurisdiction jurisdiction : jurisdictionList) {
                    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> dataList = sysAchievementJurisdictionService
                            .findDataByAchievementIdAndType(jurisdiction.getAchievementId(), jurisdiction.getType());
                    if (CollectionUtils.isNotEmpty(dataList) && dataList.size() > 0) {
                        com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction jurisdictionDb = dataList.get(0);
                        jurisdiction.setId(jurisdictionDb.getId());
                        sysAchievementJurisdictionService.update(jurisdiction);
                    } else {
                        if (StringUtils.isNotEmpty(token)) {
                            String userId = JwtTokenUtil.getUserId(token);
                            jurisdiction.setCreateId(userId);
                        }
                        sysAchievementJurisdictionService.insert(jurisdiction);
                    }
                }
            }
            return CommonResult.success("");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 刪除
     * @author hucong
     * @date 2021/01/20
     **/
    @PostMapping("/delete")
    public CommonResult delete(int id){
        try {
            int i = sysAchievementJurisdictionService.delete(id);
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
     * @author hucong
     * @date 2021/01/20
     **/
    @PostMapping("/update")
    public CommonResult update(SysAchievementJurisdiction entity){
        try {
            entity.setUpdateTime(new Date());
            int i = sysAchievementJurisdictionService.update(entity);
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
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/01/20
     **/
    @PostMapping("/load")
    public CommonResult load(int id){
        try {
            SysAchievementJurisdiction entity = sysAchievementJurisdictionService.load(id);
            return CommonResult.success(entity);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查询 分页查询
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/pageList")
    @ResponseBody
    @ApiOperation(value = "分页查询接口")
    public CommonResult pageList(SysAchievementJurisdictionReqVo vo) {
        return CommonResult.success(sysAchievementJurisdictionService.pageList(vo));
    }

    @PostMapping("/asJurisdictionUDList")
    @ResponseBody
    @ApiOperation(value = "根据文档id和权限类型查找用户和组织list")
    public CommonResult asJurisdictionUDList(SysAchievementJurisdictionReqVo vo) {
        PageInfo<SysAchievementJurisdiction> pageInfo = sysAchievementJurisdictionService.pageList(vo);
        List<SysAchievementJurisdiction> list = pageInfo.getList();
        if (CollectionUtils.isNotEmpty(list) && list.size() >0) {
            for (SysAchievementJurisdiction jurisdiction : list) {
                String userIds = jurisdiction.getUserIds();
                String deportIds = jurisdiction.getDeportIds();
                if (StringUtils.isNotEmpty(userIds)) {
                    List<com.ygkj.auth.api.user.model.SysUser> userList = userService.findByIds(userIds);
                    if (CollectionUtils.isNotEmpty(userList) && userList.size() >0) {
                        jurisdiction.setUserList(userList);
                    }
                }
                if (StringUtils.isNotEmpty(deportIds)) {
                    List<com.ygkj.auth.api.department.model.SysDept> deptList = deptService.findByIds(deportIds);
                    if (CollectionUtils.isNotEmpty(deptList) && deptList.size() >0) {
                        jurisdiction.setDeptList(deptList);
                    }
                }
            }
        }
        return CommonResult.success(list);
    }

    @PostMapping("/checkPermissions")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "achievementId", value = "文档id", paramType = "query")
    })
    @ApiOperation(value = "根据文档id检查用户权限")
    public CommonResult checkPermissions(String achievementId){
        JSONObject jsonObject = sysAchievementJurisdictionService.checkPermissions(achievementId);
        return CommonResult.success(jsonObject);
    }

}

