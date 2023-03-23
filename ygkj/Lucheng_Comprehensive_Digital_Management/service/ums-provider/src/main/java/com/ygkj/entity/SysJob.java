package com.ygkj.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wgf
 * @since 2021-01-06
 */
@Data
public class SysJob {

    private static final long serialVersionUID = 1L;

    private String id;

    private String jobName;

    private String jobDescribe;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String orgId;

    @Override
    public String toString() {
        return "SysJob{" +
            "id=" + id +
            ", jobName=" + jobName +
            ", jobDescribe=" + jobDescribe +
            ", createTime=" + createTime +
        "}";
    }
}
