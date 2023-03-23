package com.ygkj.warning.vo.response;

import lombok.Data;

@Data
public class SysWarnGroupResVo {
    /**
     * id
     */
    private Long id;

    /**
     * 预警组名称
     */
    private String name;

    /**
     * 任务类
     */
    private String taskClass;

}
