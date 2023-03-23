package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageReminder implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 关联id
     */
    private String associationId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 是否已读 1-未读 2-已读
     */
    private String status;

    /**
     * 提醒用户
     */
    private String reminderUser;

    /**
     * 提醒时间
     */
    private Date reminderTime;
}
