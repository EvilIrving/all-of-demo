package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.MessageReminder;
import com.ygkj.utils.Builder;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.enums.MessageReminderEnum;
import com.ygkj.water.project.mapper.MessageReminderMapper;
import com.ygkj.water.project.service.MessageReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageReminderServiceImpl implements MessageReminderService {
    @Autowired
    private MessageReminderMapper messageReminderMapper;

    @Override
    public List<MessageReminder> loadMessage(MessageReminder reminder) {
        QueryWrapper<MessageReminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getStatus()),"status",reminder.getStatus());
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getReminderUser()),"reminder_user",reminder.getReminderUser());
        queryWrapper.apply(StringUtils.isNotBlank(reminder.getType()),"FIND_IN_SET(type,'"+reminder.getType()+"')");
        queryWrapper.orderByAsc("status");
        queryWrapper.orderByDesc("reminder_time");
        return messageReminderMapper.selectList(queryWrapper);
    }

    @Override
    public Integer countMessage(MessageReminder reminder) {
        QueryWrapper<MessageReminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getStatus()),"status",reminder.getStatus());
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getReminderUser()),"reminder_user",reminder.getReminderUser());
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getType()),"type",reminder.getType());
        return messageReminderMapper.selectCount(queryWrapper);
    }

    @Override
    public boolean readMessage(Integer id) {
        MessageReminder reminder = new MessageReminder();
        reminder.setId(id);
        reminder.setStatus("2");
        int i = messageReminderMapper.updateById(reminder);
        return i > 0 ? true : false;
    }


    @Override
    public boolean readAllMessage(MessageReminder reminder) {

        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        QueryWrapper<MessageReminder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reminder.getReminderUser()),"reminder_user",userId);
        queryWrapper.apply(StringUtils.isNotBlank(reminder.getType()),"FIND_IN_SET(type,'"+reminder.getType()+"')");
        int i = messageReminderMapper.update(Builder.of(MessageReminder::new).with(MessageReminder::setStatus, MessageReminderEnum.READ.getValue()).build(), queryWrapper);

        return i > 0 ? true : false;
    }
}
