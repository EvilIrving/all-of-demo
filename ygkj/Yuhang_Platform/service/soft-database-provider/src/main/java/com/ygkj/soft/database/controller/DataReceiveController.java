package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.service.DataReceiveService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/1 14:48
 * @Description: 数据接收控制层
 */
@Validated
//@RestController
@RequestMapping("/data")
public class DataReceiveController {

    @Autowired
    private DataReceiveService dataReceiveService;

    @PostMapping("/receive")
    public Object receive(@RequestBody  Object data){
        try {
            dataReceiveService.DataReceive(data);
//            return CommonResult.success(dataQueryService.queryByGlobalId("aaa","sys_user"));
            return CommonResult.success("操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/changeStatus")
    public Object changeStatus(@NotNull(message = "tableName不允许为空！")String tableName,
                               @NotNull(message = "globalId不允许为空！")String globalId,
                               @NotNull(message = "status不允许为空！")int status){
        try {
            dataReceiveService.changeStatus(tableName, globalId, status);
//            return CommonResult.success(dataQueryService.queryByGlobalId("aaa","sys_user"));
            return CommonResult.success("操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
}
