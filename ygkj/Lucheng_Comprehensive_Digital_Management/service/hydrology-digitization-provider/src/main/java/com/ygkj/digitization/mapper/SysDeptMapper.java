package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.Map;

/**
 * @author xq
 * 
 * @Date 2021/2/1
 */
@DS("main")
public interface SysDeptMapper {
    Map selectDeptNameAdcdByDeptId(String deptId);
}
