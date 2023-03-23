package com.ygkj.water.project.controller;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.StringUtils;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.SimpleWarnReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.SendFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("ding")
@Api(tags = "钉钉")
public class DingController {

    @Resource
    private SendFeignClient sendFeignClient;

    @PostMapping("sendWarn")
    @ApiOperation("发送预警信息")
    public CommonResult sendWarn(SimpleWarnReqVo warnReqVo, HttpServletRequest request) throws Exception {
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        String token = request.getHeader("authorization");
        if(StringUtils.isNotBlank(token)){
            msgSendRecord.setSendId(JwtTokenUtil.getUserId(token));
        }
        msgSendRecord.setReceivePhone(warnReqVo.getPhone());
        msgSendRecord.setSendType(warnReqVo.getType());
        msgSendRecord.setContext(warnReqVo.getContext());
        msgSendRecord.setBusinessType(2);
        return sendFeignClient.synSend(msgSendRecord);
    }
}
