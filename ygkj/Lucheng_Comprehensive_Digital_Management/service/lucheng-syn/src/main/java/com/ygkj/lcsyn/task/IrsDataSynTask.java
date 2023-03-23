package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.lcsyn.enums.IrsDataApiEnum;
import com.ygkj.lcsyn.mapper.IrsApprovalInfoMapper;
import com.ygkj.lcsyn.mapper.IrsInvestProjectBaseMapper;
import com.ygkj.lcsyn.model.IrsApprovalInfo;
import com.ygkj.lcsyn.model.IrsInvestProjectBase;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import com.ygkj.lcsyn.utils.IrsTokenUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxl
 * @create 2022-06-20 9:34
 * @description
 */
@Component
@Log4j2
public class IrsDataSynTask {

    @Resource
    private IrsInvestProjectBaseMapper irsInvestProjectBaseMapper;

    @Resource
    private IrsApprovalInfoMapper irsApprovalInfoMapper;


    @Scheduled(cron = "0 30 3 * * ?")
    public void synData(){
        //TODO 因只有工程编码，暂定通过表格录入工程编码  后续应有接口获取对应的工程编码信息
        List<IrsInvestProjectBase> bases = irsInvestProjectBaseMapper.selectList(Wrappers.lambdaQuery(IrsInvestProjectBase.class)
                .eq(IrsInvestProjectBase::getValidityFlag, "1"));

        int projNum = 0, approvalNum = 0;

        for (IrsInvestProjectBase basis : bases) {

            projNum += InvestmentProjectSynData(basis.getProjectCode());
            approvalNum += ApprovalSharingSynData(basis.getProjectCode());

        }
        log.info("更新工程信息数量为：{} ， 更新批文信息数量为：{}", projNum,approvalNum);

    }


    private int InvestmentProjectSynData(String projectCode){
        long currentTimeMillis = System.currentTimeMillis();
        log.info("requestime ： {}",currentTimeMillis);
        String sign = IrsTokenUtil.getIrsRequestSign(currentTimeMillis);
        log.info("sign ： {}",sign);
        Map<String,Object> params = new HashMap<>();

        log.info("工程编码为：{}",projectCode);

        params.put("sign",sign);
        params.put("requestTime",currentTimeMillis);
        params.put("appKey", IrsTokenUtil.APP_KEY);
        params.put("requestparam",projectCode);
        params.put("type","P");
        String result = HttpClientUtil.sendGet(IrsDataApiEnum.INVESTMENT_PROJECT.getUrl(), new HashMap<>(), params, "UTF-8");
        log.info("查询对应的接口返回为：{}",result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String datas = jsonObject.getString("datas");
        int i = 0;
        if (StringUtils.isNotBlank(datas)){
            JSONArray array = JSONArray.parseArray(datas);
            List<IrsInvestProjectBase> irsInvestProjectBaseList = array.toJavaList(IrsInvestProjectBase.class);
            for (IrsInvestProjectBase irsInvestProjectBase : irsInvestProjectBaseList) {
                int num = irsInvestProjectBaseMapper.updateById(irsInvestProjectBase);
                i += num;
            }
        }
        return i;
    }



    private int ApprovalSharingSynData(String projectCode){

        long currentTimeMillis = System.currentTimeMillis();
        log.info("requestime ： {}",currentTimeMillis);
        String sign = IrsTokenUtil.getIrsRequestSign(currentTimeMillis);
        log.info("sign ： {}",sign);
        Map<String,Object> params = new HashMap<>();

        log.info("批文请求工程编码为：{}",projectCode);

        params.put("sign",sign);
        params.put("requestTime",currentTimeMillis);
        params.put("appKey", IrsTokenUtil.APP_KEY);
        params.put("requestparam",projectCode);
        params.put("type","P");
        String result = HttpClientUtil.sendGet(IrsDataApiEnum.APPROVAL_SHARING.getUrl(), new HashMap<>(), params, "UTF-8");
        log.info("查询对应的接口返回为：{}",result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String datas = jsonObject.getString("datas");
        int i = 0;
        if (StringUtils.isNotBlank(datas)){
            JSONArray array = JSONArray.parseArray(datas);
            List<IrsApprovalInfo> irsApprovalInfos = array.toJavaList(IrsApprovalInfo.class);
            for (IrsApprovalInfo irsApprovalInfo : irsApprovalInfos) {
                int num;
                try {
                    num = irsApprovalInfoMapper.insert(irsApprovalInfo);
                }catch (Exception e){
                    num = irsApprovalInfoMapper.updateById(irsApprovalInfo);
                    e.printStackTrace();
                }
                i += num;
            }
        }
        return i;
    }


}