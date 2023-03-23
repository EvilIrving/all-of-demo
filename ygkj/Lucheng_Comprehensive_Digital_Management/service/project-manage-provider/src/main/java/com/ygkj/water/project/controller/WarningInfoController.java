package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.WarningInfo;
import com.ygkj.project.model.WarningMsg;
import com.ygkj.project.model.WarningUser;
import com.ygkj.utils.Builder;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.SendFeignClient;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.WarningInfoService;
import com.ygkj.water.project.service.WarningMsgService;
import com.ygkj.water.project.service.WarningUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:03
 * @Description:
 */
@Api(tags = "预警记录表")
@RestController
@Slf4j
@RequestMapping("/warningInfo")
public class WarningInfoController {

    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private WarningUserService warningUserService;
    @Autowired
    private SendFeignClient sendFeignClient;
    @Autowired
    private WarningMsgService warningMsgService;
    @Autowired
    private UserFeignClient userFeignClient;

    @ApiOperation(value = "转发预警记录")
    @PostMapping("/saveOrUpdate")
    @Transactional
    public CommonResult saveOrUpdate(WarningInfo warningInfo){
        //重新复制一条新增，并发送短信，然后把原先的这条设置成已转发更新数据
        WarningInfo byId = warningInfoService.getById(warningInfo.getId());
        WarningInfo info = new WarningInfo();
        BeanUtils.copyProperties(warningInfo,info);
        info.setRuletype(byId.getRuletype());
        info.setId(IdWorker.get32UUID());
        warningInfoService.save(info);
        //发送短信
        String content = info.getWarningcontent();
        String sendcode = info.getSendcode();
        if (StringUtils.isNotBlank(sendcode)){
            String[] split = sendcode.split(",");
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            for (WarningUser warningUser : warningUsers) {
                //转发添加消息
                warningMsgService.save(Builder.of(WarningMsg::new)
                        .with(WarningMsg::setId,IdWorker.get32UUID())
                        .with(WarningMsg::setWarningId,info.getId())
                        .with(WarningMsg::setPhone,warningUser.getPhone())
                        .build());

                String userPhone = warningUser.getPhone();
                MsgSendRecord msgSendRecord = new MsgSendRecord();
                msgSendRecord.setReceivePhone(userPhone);
                msgSendRecord.setReceiveName(warningUser.getName());
                msgSendRecord.setSendType(1);
                msgSendRecord.setBusinessType(4);
                msgSendRecord.setContext(content);
                sendFeignClient.send(msgSendRecord);
                log.info("给"+userPhone+"号码发送短信了");
            }
        }
        byId.setStatus(1);
        warningInfoService.updateById(byId);
        return CommonResult.success();
    }

    @ApiOperation(value = "预警记录列表")
    @PostMapping("/list")
    public CommonResult list(WarningInfo warningInfo){
        warningInfo.page();
        QueryWrapper<WarningInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(warningInfo.getStatus() != null, "status", warningInfo.getStatus());
        queryWrapper.eq(warningInfo.getWarningrule() != null, "warningRule", warningInfo.getWarningrule());
        queryWrapper.eq(warningInfo.getRuletype()!= null, "ruletype", warningInfo.getRuletype());
        queryWrapper.ge(StringUtils.isNotBlank(warningInfo.getStartTime()),"warningTime",warningInfo.getStartTime());
        queryWrapper.le(StringUtils.isNotBlank(warningInfo.getEndTime()),"warningTime",warningInfo.getEndTime()+" 23:59:59");
        queryWrapper.orderByDesc("warningTime");
        List<WarningInfo> list = warningInfoService.list(queryWrapper);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation(value = "预警记录详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        WarningInfo byId = warningInfoService.getById(id);
        String sendcode = byId.getSendcode();
        if (StringUtils.isNotBlank(sendcode)){
            String[] split = sendcode.split(",");
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            byId.setUsers(warningUsers);
        }
        return CommonResult.success(byId);
    }

}



