package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.MessageRecordControllerApi;
import com.ygkj.big.screen.service.MessageService;
import com.ygkj.big.screen.vo.request.MessageRecordQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messageRecord")
public class MessageRecordController implements MessageRecordControllerApi {

    private MessageService messageService;

    public MessageRecordController(@Autowired MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("")
    @Override
    public CommonResult messageRecords(MessageRecordQueryVo queryVo) {
        return CommonResult.success(messageService.selectByCondition(queryVo));
    }
}
