package com.ygkj.project.model;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 预警消息发送人员关联表
 * @author lxl
 * @date 2022-06-13
 */
@Data
public class WarningMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 关联预警id
     */
    private String warningId;

    /**
     * 发送用户id
     */
    private String phone;

    /**
     * 消息是否已读(1-未读 2-已读）
     */
    private Integer status;

    public WarningMsg() {
    }

}
