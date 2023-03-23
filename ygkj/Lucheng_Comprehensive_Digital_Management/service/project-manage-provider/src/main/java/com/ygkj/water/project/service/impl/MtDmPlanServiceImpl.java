package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.AttOverhaulElectrical;
import com.ygkj.project.model.MtDmPlan;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.MtDmPlanReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.MtDmPlanMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.MtDmPlanService;
import com.ygkj.water.project.utils.DESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author lxl
 * @create 2022-04-18 17:00
 * @description
 */
@Service
public class MtDmPlanServiceImpl implements MtDmPlanService {

    @Resource
    private MtDmPlanMapper mtDmPlanMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;

    @Value("${access.key}")
    private String key;

    @Value("${access.secret}")
    private String secret;

    @Override
    public int insert(MtDmPlanReqVo vo) {
        MtDmPlan mtDmPlan = new MtDmPlan();
        BeanUtils.copyProperties(vo, mtDmPlan);
        mtDmPlan.setId(idService.nextID());
        mtDmPlan.setCreatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return mtDmPlanMapper.insert(mtDmPlan);
    }

    @Override
    public int update(MtDmPlanReqVo vo) {
        MtDmPlan mtDmPlan = new MtDmPlan();
        BeanUtils.copyProperties(vo,mtDmPlan);
        mtDmPlan.setUpdatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return mtDmPlanMapper.updateById(mtDmPlan);
    }

    @Override
    public int delete(String id) {
        return mtDmPlanMapper.deleteByMtDmId(id);
    }

    @Override
    public PageVO<MtDmPlan> loadPage(MtDmPlanReqVo vo) {

        String encrypt = getEncrypt(vo.getPid());
        if (encrypt == null){
            throw new RuntimeException("获取工程运管token异常");
        }
        String url = "https://sk.zjwater.com/mgt/mt/dmplan/listJson?outsideToken="+encrypt ;
//        String url = "https://sk.zjwater.com/mgt/lookup/fileListJson?outsideToken="+encrypt+"&dataId=E9249CFDC4F0448298385BF5B9A0753C";
        String json = HttpClientUtil.httpGet(url);
        try {
            JSONObject result = JSONObject.parseObject(json);
            if (result.getBoolean("success")){
                JSONArray rows = result.getJSONArray("rows");
                List<MtDmPlan> mtDmPlans = rows.toJavaList(MtDmPlan.class);
                return PageUtils.getPageVO(mtDmPlans);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return PageUtils.getPageVO(Collections.emptyList());
    }


    @Override
    public void exportMtDmPlan(MtDmPlanReqVo vo, HttpServletResponse response) {
        vo.setPageNum(null);
        PageVO<MtDmPlan> mtDmPlanPageVO = loadPage(vo);
        List<MtDmPlan> list = mtDmPlanPageVO.getList();


        ExportUtils.exportExcel(list,"导出年度计划",
                new String[]{"年份","年底计划说明"},
                new String[]{"annual","comment"},response);
    }


    @Override
    public String getYgFile(String pid, String fileId) {

        String encrypt = getEncrypt(pid);
        if (encrypt == null){
            throw new RuntimeException("获取工程运管token异常");
        }
        String resultStr = "";
        String url = "https://sk.zjwater.com/mgt/lookup/fileListJson?outsideToken="+encrypt+"&dataId=" + fileId;
        String json = HttpClientUtil.httpGet(url);
        try {
            JSONObject result = JSONObject.parseObject(json);
            if (1 == result.getInteger("result")){
                JSONArray list = result.getJSONArray("list");
                if (list.size() > 0){
                    List<JSONObject> jsonObjects = list.toJavaList(JSONObject.class);
                    for (JSONObject jsonObject : jsonObjects) {
                        String fileurl = jsonObject.getString("filurl");
                        if (StringUtils.isNotBlank(fileurl)){
                            //加上运管平台前缀
                            resultStr = "https://sk.zjwater.com/" + fileurl;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultStr;
    }

    public String getEncrypt(String pid){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", "13858860396");
        jsonObject.put("prcd", pid);
        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        String encrypt = DESUtil.encrypt(DESUtil.KEY, data);
        return encrypt;
    }



}
