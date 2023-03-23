package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.MessageReminder;
import com.ygkj.project.model.WarningInfo;
import com.ygkj.project.model.WarningMsg;
import com.ygkj.project.vo.request.ProjectOverseeReqVo;
import com.ygkj.project.vo.response.ProjectOverseeResVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.MessageReminderEnum;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.MessageReminderService;
import com.ygkj.water.project.service.OverseeService;
import com.ygkj.water.project.service.WarningMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-06-13 14:49
 * @description
 */
@Api(tags = "预警消息记录")
@RestController
@Slf4j
@RequestMapping("/warningMsg")
public class WarningMsgController {

    @Autowired
    private WarningMsgService warningMsgService;

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private MessageReminderService messageReminderService;
    @Autowired
    private OverseeService overseeService;

    @ApiOperation(value = "预警记录列表")
    @PostMapping("/list")
    public CommonResult list(WarningInfo warningInfo){
        //当前登录用户
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("-");
        SysUser sysUser = userFeignClient.detailInfo(currentUserId);
        if (ObjectUtils.isEmpty(sysUser)){
            return CommonResult.failed("未获取到登录用户信息");
        }
        warningInfo.setPhone(sysUser.getPhone());
        List<WarningInfo> list = warningMsgService.getListByCondition(warningInfo);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation("获取报警数量")
    @GetMapping("/getAlarmNum")
    public CommonResult getTotalAlarm(){
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("-");
        SysUser sysUser = userFeignClient.detailInfo(currentUserId);
        if (ObjectUtils.isEmpty(sysUser)){
            return CommonResult.failed("未获取到登录用户信息");
        }
        JSONObject result = new JSONObject();
        result.put("alarmNum",0);
        result.put("remindNum",0);
        result.put("overseeNum",0);
        result.put("approvalNum",0);
        if (StringUtils.isNotBlank(sysUser.getPhone())){
            Integer num = warningMsgService.getTotalAlarmNum(sysUser.getPhone());
            result.put("alarmNum",num);
        }
        if (StringUtils.isNotBlank(sysUser.getPhone())){
            Integer num = warningMsgService.getTotalRemindNum(sysUser.getPhone());
            result.put("remindNum",num);
        }
        if (StringUtils.isNotBlank(currentUserId)){
            ProjectOverseeReqVo projectOverseeReqVo = new ProjectOverseeReqVo();
            projectOverseeReqVo.setUserId(currentUserId);
            PageVO<ProjectOverseeResVo> page = overseeService.page(projectOverseeReqVo);
//            result.put("overseeNum",page.getTotalRows());
            result.put("overseeNum",0);
        }
        CommonResult approval = getApproval("1");
        List<MessageReminder> approvalData = (List<MessageReminder>) approval.getData();
        result.put("approvalNum",approvalData.size());
        return CommonResult.success(result);
    }

    @ApiOperation("根据当前登录用户全部已读 参数只传一个 ruleType")
    @GetMapping("/readAll")
    public CommonResult readAll(WarningInfo warningInfo){

        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        //查询未读的数据
        warningInfo.setReadStatus(MessageReminderEnum.UNREAD.getValue());
        SysUser sysUser = userFeignClient.detailInfo(userId);
        if (ObjectUtils.isEmpty(sysUser)){
            return CommonResult.failed("未获取到登录用户信息");
        }
        warningInfo.setPhone(sysUser.getPhone());
        List<WarningInfo> list = warningMsgService.getListByCondition(warningInfo);
        List<String> ids = list.stream().map(WarningInfo::getId).collect(Collectors.toList());
        if (CollectionUtils.isBlank(ids)){
            return CommonResult.success("无未读消息");
        }
        WarningMsg warningMsg = new WarningMsg();
        warningMsg.setStatus(Integer.parseInt(MessageReminderEnum.READ.getValue()));
        if (warningMsgService.update(warningMsg, Wrappers.lambdaQuery(WarningMsg.class)
                .in(WarningMsg::getWarningId,ids)
                .eq(WarningMsg::getPhone,sysUser.getPhone()))){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }

    @ApiOperation("根据预警信息id已读")
    @GetMapping("/readById")
    public CommonResult readById(String warningId){

        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        SysUser sysUser = userFeignClient.detailInfo(userId);
        if (ObjectUtils.isEmpty(sysUser)){
            return CommonResult.failed("未获取到登录用户信息");
        }
        WarningMsg warningMsg = new WarningMsg();
        warningMsg.setStatus(Integer.parseInt(MessageReminderEnum.READ.getValue()));
        if (warningMsgService.update(warningMsg, Wrappers.lambdaQuery(WarningMsg.class)
                .eq(WarningMsg::getPhone,sysUser.getPhone()).eq(WarningMsg::getWarningId,warningId))){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }

    @ApiOperation("审批信息列表")
    @GetMapping("/getApproval")
    public CommonResult getApproval(String status){
        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        SysUser sysUser = userFeignClient.detailInfo(userId);
        if (ObjectUtils.isEmpty(sysUser)){
            return CommonResult.failed("未获取到登录用户信息");
        }
        MessageReminder messageReminder = new MessageReminder();
        messageReminder.setReminderUser(sysUser.getId());
        messageReminder.setStatus(status);
        messageReminder.setType(MessageReminderEnum.HIDDEN_DANGER.getValue() + "," + MessageReminderEnum.CONTRACT.getValue());
        List<MessageReminder> list = messageReminderService.loadMessage(messageReminder);
        if (CollectionUtils.isNotBlank(list)){
            //未读排前面，后续按时间排序
            list = list.stream().sorted(Comparator.comparing(MessageReminder::getStatus)).collect(Collectors.toList());
            return CommonResult.success(list);
        }else {
            return CommonResult.success(Collections.emptyList());
        }

    }

}
