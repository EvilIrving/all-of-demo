package com.dcy.datamanage.biz.controller;



import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.DataDeptInputDTO;
import com.dcy.datamanage.biz.service.DataDeptRelService;
import com.dcy.datamanage.biz.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("dataDeptRel")
@Api(tags = "数据责任科室")
public class DataDeptRelController {

    @Autowired
    private DataDeptRelService dataDeptRelService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("assignedData")
    @ApiOperation("指派数据")
    public R<Boolean> saveBatch(@RequestBody DataDeptInputDTO dto, HttpServletRequest request) {
        dto.setOperateUserId(userInfoService.getUserInfo(request).getUserId());
        return dataDeptRelService.saveBatch(dto);
    }


    @PostMapping("delAssigned")
    @ApiOperation("取消指派")
    public R<Boolean> delBatch(@RequestBody List<String> idList) {
        return R.success(dataDeptRelService.delBatch(idList));
    }
}
