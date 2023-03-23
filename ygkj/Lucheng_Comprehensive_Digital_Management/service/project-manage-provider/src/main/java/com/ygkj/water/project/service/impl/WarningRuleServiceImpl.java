package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.project.model.WarningRule;
import com.ygkj.project.vo.request.RuleStReqVo;
import com.ygkj.water.project.mapper.WarningRuleMapper;
import com.ygkj.water.project.service.WarningRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:09
 * @Description:
 */
@Service
public class WarningRuleServiceImpl extends ServiceImpl<WarningRuleMapper, WarningRule> implements WarningRuleService {

    @Autowired
    private WarningRuleMapper warningRuleMapper;

    @Override
    public List<JSONObject> getSeawallByIds(List<String> asList) {
        return warningRuleMapper.getSeawallByIds(asList);
    }

    @Override
    public List<JSONObject> getStationByIds(List<String> asList) {
        return warningRuleMapper.getStationByIds(asList);
    }

    @Override
    public List<AttStBase> getStList(AttStBase attStBase) {
        return warningRuleMapper.getStList(attStBase);
    }

    @Override
    public List<AttStBase> listRuleSt(RuleStReqVo reqVo) {
        return warningRuleMapper.listRuleSt(reqVo);
    }

    @Override
    public Object projectList(String projectName) {
        return warningRuleMapper.projectList(projectName);
    }

    @Override
    public List<JSONObject> getProjectByIds(List<String> asList) {
        return warningRuleMapper.getProjectByIds(asList);
    }
}
