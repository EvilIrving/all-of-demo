package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.project.model.WarningRule;
import com.ygkj.project.vo.request.RuleStReqVo;


import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:09
 * @Description:
 */
public interface WarningRuleService extends IService<WarningRule> {

    List<JSONObject> getSeawallByIds(List<String> asList);

    List<JSONObject> getStationByIds(List<String> asList);

    List<AttStBase> getStList(AttStBase attStBase);

    List<AttStBase> listRuleSt(RuleStReqVo reqVo);

    Object projectList(String projectName);

    List<JSONObject> getProjectByIds(List<String> asList);
}
