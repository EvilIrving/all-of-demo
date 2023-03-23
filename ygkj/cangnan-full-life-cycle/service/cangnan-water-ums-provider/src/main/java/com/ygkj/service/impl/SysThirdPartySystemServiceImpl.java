package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.api.thirdPartSystem.model.SysSystemApply;
import com.ygkj.auth.api.thirdPartSystem.model.SysThirdPartySystem;
import com.ygkj.mapper.SysSystemApplyMapper;
import com.ygkj.mapper.SysThirdPartySystemMapper;
import com.ygkj.service.SysThirdPartySystemService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:40
 * @Description:
 */
@Service
public class SysThirdPartySystemServiceImpl implements SysThirdPartySystemService {

    @Autowired
    private SysThirdPartySystemMapper sysThirdPartySystemMapper;

    @Autowired
    private SysSystemApplyMapper sysSystemApplyMapper;

    @Override
    public CommonResult load(SysThirdPartySystemQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPageNum(),queryVO.getPageSize());
        sysThirdPartySystemMapper.selectAll(queryVO);
        return CommonResult.success(sysThirdPartySystemMapper.selectAll(queryVO));
    }

    @Override
    public CommonResult insert(SysThirdPartySystemServiceAddVO addVo) {
        try {
            SysThirdPartySystem sysThirdPartySystem = new SysThirdPartySystem();
            sysThirdPartySystem.setId(UUID.randomUUID().toString());
            BeanUtils.copyProperties(addVo,sysThirdPartySystem);
            sysThirdPartySystemMapper.insert(sysThirdPartySystem);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult update(SysThirdPartySystemServiceUpdateVO updateVo) {
        try {
            SysThirdPartySystem sysThirdPartySystem = sysThirdPartySystemMapper.load(updateVo.getId());
            BeanUtils.copyProperties(updateVo,sysThirdPartySystem);
            sysThirdPartySystemMapper.update(sysThirdPartySystem);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult delete(String id) {
        try {
            sysThirdPartySystemMapper.delete(id);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult applySystem(SysSystemApplyVO applyVO) {
        SysSystemApply apply = new SysSystemApply();
        apply.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(applyVO,apply);
        sysSystemApplyMapper.insert(apply);
        return CommonResult.success("操作成功");
    }

    @Override
    public CommonResult examineApply(SysSystemApplyExamineVO examineVO) {
        SysSystemApply apply = new SysSystemApply();
        BeanUtils.copyProperties(examineVO,apply);
        sysSystemApplyMapper.update(apply);
        return CommonResult.success("操作成功");
    }
}
