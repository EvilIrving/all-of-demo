package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzh
 * @Date: 2020/9/17 09:16
 * @Description:
 */
@Data
public class BusProjectProcessRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String projectId;

    private String processName;

    private String assignee;

    private String assigneeOpinion;
}
