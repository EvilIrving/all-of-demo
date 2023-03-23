package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.biz.enums.DataManageRoleEnum;
import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.api.model.BusDataApply;
import com.dcy.datamanage.biz.dto.input.*;
import com.dcy.datamanage.biz.service.DataApplyService;
import com.dcy.datamanage.biz.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("dataApply")
@Api(tags = "数据申请")
public class DataApplyController {

    @Autowired
    private DataApplyService dataApplyService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("applySave")
    @ApiOperation("申请数据新增")
    public R<Boolean> applySave(@RequestBody DataSaveInputDTO dto, HttpServletRequest request) {
        addOperate(dto, request);
        return dataApplyService.applySave(dto);
    }


    @PostMapping("applyDel")
    @ApiOperation("申请数据删除")
    public R<Boolean> applyDel(@RequestBody DataDelInputDTO dto, HttpServletRequest request) {
        addOperate(dto, request);
        return dataApplyService.applyDel(dto);
    }


    @PostMapping("applyUpdate")
    @ApiOperation("申请数据修改")
    public R<Boolean> applyUpdate(@RequestBody DataUpdateInputDTO dto, HttpServletRequest request) {
        addOperate(dto, request);
        return dataApplyService.applyUpdate(dto);
    }


    @GetMapping("page")
    @ApiOperation("分页查询")
    public R<PageResult<BusDataApply>> page(DataApplySearchInputDTO dto, PageModel pageModel, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.getUserInfo(request);
        dto.setDeptId(userInfo.getDeptId());
        dto.setUserId(userInfo.getUserId());
        dto.setRoleType(userInfo.getRoleType());
        return R.success(dataApplyService.page(dto, pageModel));
    }


    @PostMapping("update")
    @ApiOperation("处理待办")
    public R<Boolean> update(@RequestBody BusDataApply busDataApply, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.getUserInfo(request);
        String roleType = userInfo.getRoleType();
        boolean canUpdate = DataManageRoleEnum.ADMIN.getRoleType().equals(roleType) ||
                (DataManageRoleEnum.DEPT_ADMIN.getRoleType().equals(roleType) && busDataApply.getDeptId().equals(userInfo.getDeptId()));
        if (!canUpdate) {
            return R.error("您无审批权限");
        }
        busDataApply.setUserId(userInfo.getUserId());
        return R.success(dataApplyService.update(busDataApply));
    }


    @DeleteMapping("del/{id}")
    @ApiOperation("删除待办")
    public R<Boolean> del(@PathVariable("id") String id, HttpServletRequest request) {
        BusDataApply busDataApply = dataApplyService.getById(id);
        UserInfo userInfo = userInfoService.getUserInfo(request);
        boolean canDel = busDataApply.getCreateId().equals(userInfo.getUserId());
        if (!canDel) {
            return R.error("您无法删除他人发起的申请");
        }
        return R.success(dataApplyService.removeById(id));
    }

    private void addOperate(DataEditInputDTO dto, HttpServletRequest request) {
        UserInfo userInfo = userInfoService.getUserInfo(request);
        dto.setOperateUserId(userInfo.getUserId());
        dto.setOperateDeptId(userInfo.getDeptId());
        dto.setRoleType(userInfo.getRoleType());
    }
}
