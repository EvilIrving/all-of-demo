package com.ygkj.river.model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 通知人员时间记录表
 * @author lxl
 * @date 2022-06-06
 */
@Data
public class AttBlueNoticeRecord {

    /**
     * id
     */
    private String id;

    /**
     * 对应巡查id
     */
    private String inspectionId;

    /**
     * 通知人名称
     */
    private String userName;

    /**
     * 通知人id
     */
    private String userId;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知手机号码
     */
    private String phone;

    /**
     * 通知时间
     */
    private Date tm;

    /**
     * 创建时间
     */
    private Date createTime;

    public AttBlueNoticeRecord() {
    }

}
