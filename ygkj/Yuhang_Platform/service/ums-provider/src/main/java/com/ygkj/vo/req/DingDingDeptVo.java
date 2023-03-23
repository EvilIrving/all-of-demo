package com.ygkj.vo.req;

import com.ygkj.entity.DingDingDept;
import lombok.Data;

@Data
public class DingDingDeptVo extends DingDingDept {

    /**
     * 系统部门名称
     */
    private String sysDeptName;

    /**
     * 页码
     */
    private Integer pageSize;

    /**
     * 每页展示条数
     */
    private Integer pageNum;
}
