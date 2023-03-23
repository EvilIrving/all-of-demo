package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.WarningUser;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.aop.annotation.LogAnnotation;
import com.ygkj.water.project.service.DeptCustomService;
import com.ygkj.water.project.service.WarningUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/21 17:48
 * @Description:
 */
@Api(value = "预警人员",tags = "预警人员")
@RestController
@RequestMapping("/warningUser")
public class WarningUserController {

    @Autowired
    private WarningUserService warningUserService;
    @Autowired
    private DeptCustomService deptCustomService;

    /**
     * 新增
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "新增预警人员")
    @PostMapping("/insert")
    @LogAnnotation(title = "风险预警-人员管理",action = "新增预警人员")
    public CommonResult insert(WarningUser warningUser){
        warningUserService.insert(warningUser);
        return CommonResult.success();
    }

    /**
     * 刪除
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "根据id删除预警人员")
    @GetMapping("/delete")
    @LogAnnotation(title = "风险预警-人员管理",action = "删除预警人员")
    public CommonResult delete(String id){
        if (warningUserService.removeById(id)){
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 更新
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "更新预警人员")
    @PostMapping("/update")
    @LogAnnotation(title = "风险预警-人员管理",action = "更新预警人员")
    public CommonResult update(WarningUser warningUser){
        if (warningUserService.updateById(warningUser)){
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 根据deptid查询列表
     * @param warningUser
     * @return
     */
    @ApiOperation(value = "根据deptid查询预警人员列表")
    @PostMapping("/list")
    @LogAnnotation(title = "风险预警-人员管理",action = "根据deptid查询预警人员列表")
    public CommonResult list(WarningUser warningUser){
        /*if (StringUtils.isBlank(warningUser.getDeptid())) {
            return CommonResult.failed("缺少必传参数");
        }*/
        QueryWrapper<WarningUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(warningUser.getName()),"name",warningUser.getName());
        if (StringUtils.isNotBlank(warningUser.getDeptid()) && warningUser.getType() != null){
            List<String> list = new ArrayList<>();
            //查询它和他的子集用户  type 类型1.浙政钉同步的部门，2.自定义的部门表
            Integer type = warningUser.getType();
            if (type == 1){
                list = deptCustomService.deptZzdIds(warningUser.getDeptid());
            }
            if (type == 2){
                list = deptCustomService.deptIds(warningUser.getDeptid());
            }
            queryWrapper.in("deptId", list);
        }
        queryWrapper.eq(warningUser.getType() != null, "type", warningUser.getType());
        queryWrapper.eq(StringUtils.isNotBlank(warningUser.getId()),"id",warningUser.getId());
        queryWrapper.orderByDesc("tm");
        warningUser.page();
        return CommonResult.success(PageUtils.getPageVO(warningUserService.list(queryWrapper)));
    }

    @ApiOperation(value = "查询预警人员列表")
    @PostMapping("/userList")
    public CommonResult userList(String name){
        return CommonResult.success(warningUserService.userList(name));
    }

    /**
     * 导出预警人员数据
     * @param warningUser
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "导出预警人员数据")
    @PostMapping("/exportWarning")
    public void exportWarning(WarningUser warningUser, HttpServletResponse response){

        QueryWrapper<WarningUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(warningUser.getName()),"name",warningUser.getName());
        if (StringUtils.isNotBlank(warningUser.getDeptid()) && warningUser.getType() != null){
            List<String> list = new ArrayList<>();
            //查询它和他的子集用户  type 类型1.浙政钉同步的部门，2.自定义的部门表
            Integer type = warningUser.getType();
            if (type == 1){
                list = deptCustomService.deptZzdIds(warningUser.getDeptid());
            }
            if (type == 2){
                list = deptCustomService.deptIds(warningUser.getDeptid());
            }
            queryWrapper.in("deptId", list);
        }
        queryWrapper.eq(warningUser.getType() != null, "type", warningUser.getType());
        queryWrapper.orderByDesc("tm");
        List<WarningUser> list = warningUserService.list(queryWrapper);
        ExportUtils.exportExcel(list,
                "预警人员数据",
                new String[]{"姓名","电话","职务"},
                new String[]{"name","phone","duty"},
                response,WarningUser.class);
    }



    @ApiOperation("水利局通讯录查询")
    @GetMapping("/addressBookList")
    public List<JSONObject> addressBookList(@RequestParam(required = false) String key){
        //返回  部门 + 职务 + 电话 + 名字
        List<JSONObject> json = warningUserService.addressBookList(key);
        return  json;
    }


}
