package com.ygkj.water.project.service;

import com.ygkj.project.model.MessageReminder;

import java.util.List;

public interface MessageReminderService {
    List<MessageReminder> loadMessage(MessageReminder reminder);

    Integer countMessage(MessageReminder reminder);

    boolean readMessage(Integer id);


    boolean readAllMessage(MessageReminder reminder);
}
