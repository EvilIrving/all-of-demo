package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.InvestmentApiEnum;
import com.ygkj.lcsyn.mapper.IrsApprovalFileMapper;
import com.ygkj.lcsyn.mapper.IrsApprovalInfoMapper;
import com.ygkj.lcsyn.mapper.IrsInvestProjectBaseMapper;
import com.ygkj.lcsyn.model.IrsApprovalFile;
import com.ygkj.lcsyn.model.IrsApprovalInfo;
import com.ygkj.lcsyn.model.IrsInvestProjectBase;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import com.ygkj.lcsyn.utils.TokenUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-07-07 14:57
 * @description 投资数据同步任务
 */
@Component
@Log4j2
@RunWith(SpringRunner.class)
@ActiveProfiles("prod")
@SpringBootTest(classes = LcSynApplication.class)
public class InvestmentDataTask {

    @Resource
    private IrsApprovalInfoMapper irsApprovalInfoMapper;
    @Resource
    private IrsInvestProjectBaseMapper irsInvestProjectBaseMapper;
    @Resource
    private IrsApprovalFileMapper irsApprovalFileMapper;


    @Scheduled(cron = "0 0 2 * * ?")
    @Test
    public void investmentFileDataSyn(){

        String token = TokenUtil.getDshareToken();

        String headerkey = "Authorization";
        Map<String, String> parameters = new HashMap<>();
        //事项编码，具体编码需参照业务要求填写
        parameters.put("powermatters", "");
        //事项子编码，具体编码需参照业务要求填写
        parameters.put("subpowermatters", "");
        //部门编码，鹿城区固定编码
        parameters.put("p_dept_code", "ff8080815e01f0b9015e038918610f94");

        JSONObject jsonObject = HttpClientUtil.doPost(InvestmentApiEnum.INVESTMENT_INFO.getUrl(), parameters,headerkey,token);
        log.info("请求大数据局投资项目办件信息回参为：{}",jsonObject.toJSONString());
        if (ObjectUtils.isNotEmpty(jsonObject)){
            JSONArray data = jsonObject.getJSONArray("datas");
            List<IrsApprovalInfo> irsApprovalInfos = data.toJavaList(IrsApprovalInfo.class);
            if (CollectionUtils.isNotEmpty(irsApprovalInfos)){
                irsApprovalInfoMapper.replaceIrsApprovalInfo(irsApprovalInfos);
            }
        }

        List<IrsApprovalInfo> irsApprovalInfos = irsApprovalInfoMapper.selectList(Wrappers.lambdaQuery(IrsApprovalInfo.class));
        List<String> approvalItemId = irsApprovalInfos.stream().map(IrsApprovalInfo::getApprovalItemid).collect(Collectors.toList());
        approvalFileSyn(token,approvalItemId);
        //项目基本信息同步
        Map<String, List<IrsApprovalInfo>> listMap = irsApprovalInfos.stream().collect(Collectors.groupingBy(IrsApprovalInfo::getProjectCode));
        Set<String> projectSet = listMap.keySet();
        investmentProjectDataSyn(token,projectSet);
    }


    /**
     * 投资项目批文信息同步
     * @param token
     * @param approvalItemId
     */
    public void approvalFileSyn(String token,List<String> approvalItemId){
        List<IrsApprovalFile> irsApprovalFileList = new ArrayList<>();
        for (String itemId : approvalItemId) {
            String headerkey = "Authorization";
            Map<String, String> parameters = new HashMap<>();
            //事项编码，具体编码需参照业务要求填写
            parameters.put("powermatters", "");
            //事项子编码，具体编码需参照业务要求填写
            parameters.put("subpowermatters", "");
            //部门编码，鹿城区固定编码
            parameters.put("p_approvalItemId", itemId);

            JSONObject jsonObject = HttpClientUtil.doPost(InvestmentApiEnum.INVESTMENT_FILE.getUrl(), parameters,headerkey,token);
            log.info("请求大数据局批文基本信息回参为：{}",jsonObject.toJSONString());
            if (ObjectUtils.isNotEmpty(jsonObject)){
                JSONArray data = jsonObject.getJSONArray("datas");
                List<IrsApprovalFile> files = data.toJavaList(IrsApprovalFile.class);
                irsApprovalFileList.addAll(files);
            }
        }

        if (CollectionUtils.isNotEmpty(irsApprovalFileList)){
            irsApprovalFileMapper.replaceIrsApprovalFile(irsApprovalFileList);
        }

    }


    /**
     * 投资项目基本信息
     * @param token
     * @param projectSet
     */
    public void investmentProjectDataSyn(String token, Set<String> projectSet){

        for (String projectCode : projectSet) {
            String headerkey = "Authorization";
            Map<String, String> parameters = new HashMap<>();
            //事项编码，具体编码需参照业务要求填写
            parameters.put("powermatters", "");
            //事项子编码，具体编码需参照业务要求填写
            parameters.put("subpowermatters", "");
            //部门编码，鹿城区固定编码
            parameters.put("p_projectCode", projectCode);

            JSONObject jsonObject = HttpClientUtil.doPost(InvestmentApiEnum.INVESTMENT_PROJECT.getUrl(), parameters,headerkey,token);
            log.info("请求大数据局投资项目基本信息回参为：{}",jsonObject.toJSONString());
            if (ObjectUtils.isNotEmpty(jsonObject)){
                JSONArray data = jsonObject.getJSONArray("datas");
                List<IrsInvestProjectBase> irsInvestProjectBaseList = data.toJavaList(IrsInvestProjectBase.class);
                if (CollectionUtils.isNotEmpty(irsInvestProjectBaseList)){
                    for (IrsInvestProjectBase irsInvestProjectBase : irsInvestProjectBaseList) {
                        if (ObjectUtils.isNotEmpty(irsInvestProjectBaseMapper.selectById(irsInvestProjectBase.getProjectCode()))){
                            irsInvestProjectBaseMapper.updateById(irsInvestProjectBase);
                        }else{
                            irsInvestProjectBaseMapper.insert(irsInvestProjectBase);
                        }
                    }
                }
            }
        }
    }





}
