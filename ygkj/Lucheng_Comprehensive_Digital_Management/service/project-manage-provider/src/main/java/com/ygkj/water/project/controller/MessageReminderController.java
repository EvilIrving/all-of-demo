package com.ygkj.water.project.controller;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.MessageReminderControllerApi;
import com.ygkj.project.model.MessageReminder;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.MessageReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageReminderController implements MessageReminderControllerApi {
    @Autowired
    private MessageReminderService messageReminderService;

    @Override
    @PostMapping("/loadMessage")
    public CommonResult loadMessage(MessageReminder reminder) {
        try{
            return CommonResult.success(messageReminderService.loadMessage(reminder));
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @PostMapping("/countMessage")
    public CommonResult countMessage(MessageReminder reminder) {
        try{
            return CommonResult.success(messageReminderService.countMessage(reminder));
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @PostMapping("/readMessage")
    public CommonResult readMessage(Integer id) {
        return messageReminderService.readMessage(id) ? CommonResult.success() : CommonResult.failed();
    }


    @Override
    @PostMapping("/readAllMessage")
    public CommonResult readAllMessage(MessageReminder reminder) {

        try{
            return CommonResult.success(messageReminderService.readAllMessage(reminder));
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }
}
