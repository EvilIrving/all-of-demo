package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.DeptCustom;
import com.ygkj.project.model.SysDept;
import com.ygkj.water.project.mapper.DeptCustomMapper;
import com.ygkj.water.project.service.DeptCustomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author wgf
 * @date 2021/10/21 17:00
 * @Description:
 */
@Service
public class DeptCustomServiceImpl extends ServiceImpl<DeptCustomMapper, DeptCustom> implements DeptCustomService {

    @Resource
    private DeptCustomMapper deptCustomMapper;

    @Override
    public void insert(DeptCustom deptCustom) {
        deptCustom.setId(IdWorker.get32UUID());
        deptCustomMapper.insert(deptCustom);
    }

    @Override
    public boolean delete(String id) {
        int ret = deptCustomMapper.deleteById(id);
        return ret > 0;
    }

    @Override
    public boolean update(DeptCustom deptCustom) {
        int ret = deptCustomMapper.updateById(deptCustom);
        return ret > 0;
    }

    @Override
    public List<String> deptIds(String id) {
        String depts = deptCustomMapper.getDeptIdsString(id);
        String[] split = depts.split(",");
        List<String> list = Arrays.asList(split);
        return list;
    }

    @Override
    public SysDept getZzdDeptById(String id) {
        return deptCustomMapper.getZzdDeptById(id);
    }

    @Override
    public List<SysDept> getDeptList(String id) {
        return deptCustomMapper.getDeptList(id);
    }

    @Override
    public List<String> deptZzdIds(String deptid) {
        String depts = deptCustomMapper.deptZzdIds(deptid);
        String[] split = depts.split(",");
        List<String> list = Arrays.asList(split);
        return list;
    }


}
