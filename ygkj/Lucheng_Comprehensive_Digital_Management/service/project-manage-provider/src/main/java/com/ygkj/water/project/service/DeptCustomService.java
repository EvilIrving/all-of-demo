package com.ygkj.water.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.DeptCustom;
import com.ygkj.project.model.SysDept;

import java.util.List;

/**
 * @author wgf
 * @date 2021/10/21 16:56
 * @Description:
 */
public interface DeptCustomService extends IService<DeptCustom> {

    /**
     * 新增
     */
     void insert(DeptCustom deptCustom);

    /**
     * 删除
     */
    boolean delete(String id);

    /**
     * 更新
     */
    boolean update(DeptCustom deptCustom);

    /**
     * 查询自己和所有子集的id
     * @param id
     * @return
     */
    List<String> deptIds(String id);

    SysDept getZzdDeptById(String id);

    List<SysDept> getDeptList(String id);

    List<String> deptZzdIds(String deptid);
}
