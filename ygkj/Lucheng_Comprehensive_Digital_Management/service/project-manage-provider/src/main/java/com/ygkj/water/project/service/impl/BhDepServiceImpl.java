package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.BhMapper;
import com.ygkj.water.project.service.BhDepService;
import com.ygkj.water.project.service.ProjectRelService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("bhDepService")
public class BhDepServiceImpl implements BhDepService {

    @Resource
    private ProjectRelService projectRelService;

    @Resource
    private BhMapper bhMapper;

    /**
     * 工程简介
     *
     * @param seawallCode
     * @return
     */
    @Override
    public String introduction(String seawallCode) {
        seawallCode = projectRelService.transBhSeawall(seawallCode);
        return bhMapper.introduction(seawallCode);
    }

    /**
     * 组织架构
     *
     * @param seawallCode
     * @return
     */
    @Override
    public List<JobSettingResVo> organization(String seawallCode) {
        seawallCode = projectRelService.transBhSeawall(seawallCode);
        List<JobSettingResVo> list = bhMapper.organization(seawallCode);
        return list;
    }

    /**
     * 组织架构详细信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<JobSettingDetailResVo> organizationDetail(JobSettingReqVo reqVo) {
        List<JobSettingDetailResVo> list = bhMapper.organizationDetail(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            //合并
            Map<String, JobSettingDetailResVo> map = new HashMap<>();
            Iterator<JobSettingDetailResVo> it = list.iterator();
            while (it.hasNext()) {
                JobSettingDetailResVo jobSettingDetailResVo = it.next();
                String id = jobSettingDetailResVo.getId();
                JobPersonResVo jobPersonResVo = getJobPersonResVo(jobSettingDetailResVo);
                if (map.containsKey(id)) {
                    map.get(id).getPeople().add(jobPersonResVo);
                    it.remove();
                } else {
                    List<JobPersonResVo> people = new ArrayList<>();
                    people.add(jobPersonResVo);
                    jobSettingDetailResVo.setPeople(people);
                    jobSettingDetailResVo.setName(null);
                    jobSettingDetailResVo.setTelphone(null);
                    map.put(id, jobSettingDetailResVo);
                }
            }
        }
        return list;
    }

    /**
     * 人员信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<JobPersonResVo> listPeople(JobPersonReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        reqVo.page();
        List<JobPersonResVo> list = bhMapper.listPeople(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 年度培训
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<PersonTrainResVo> personTrain(PersonTrainReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        reqVo.page();
        List<PersonTrainResVo> list = bhMapper.personTrain(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 管护经费
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<ManageFundResVo> managefund(ManageFundReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        List<ManageFundResVo> result = new ArrayList<>();
        List<ManageFundResVo> list = bhMapper.managefund(reqVo);
        if(!CollectionUtils.isEmpty(list)){
            ManageFundResVo total = new ManageFundResVo();
            total.setQuarter(reqVo.getYear() + "年全年");
            total.setFoudTotal(new BigDecimal(0));
            total.setFoudFulfil(new BigDecimal(0));
            result.add(total);
            for (ManageFundResVo manageFundResVo:list){
                if (!ObjectUtils.isEmpty(manageFundResVo.getFoudTotal())){
                    total.setFoudTotal(total.getFoudTotal().add(manageFundResVo.getFoudTotal()));
                }
                if (!ObjectUtils.isEmpty(manageFundResVo.getFoudFulfil())){
                    total.setFoudFulfil(total.getFoudFulfil().add(manageFundResVo.getFoudFulfil()));
                }
                result.add(manageFundResVo);
            }
        }
        return result;
    }

    /**
     * 管理制度附件
     *
     * @param seawallCode
     * @return
     */
    @Override
    public List<ManageinstitutionResVo> manageinstitution(String seawallCode) {
        seawallCode = projectRelService.transBhSeawall(seawallCode);
        return bhMapper.manageinstitution(seawallCode);
    }

    /**
     * 项目维护
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<MfpmResVo> mfpm(MfpmReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        reqVo.page();
        List<MfpmResVo> list = bhMapper.mfpm(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 资金申报
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<MfdResVo> mfd(MfdReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        reqVo.page();
        List<MfdResVo> list = bhMapper.mfd(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 资金使用
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<MfuResVo> mfu(MfuReqVo reqVo) {
        reqVo.setSeawallCode(projectRelService.transBhSeawall(reqVo.getSeawallCode()));
        reqVo.page();
        List<MfuResVo> list = bhMapper.mfu(reqVo);
        return PageUtils.getPageVO(list);
    }

    private JobPersonResVo getJobPersonResVo(JobSettingDetailResVo jobSettingDetailResVo) {
        JobPersonResVo jobPersonResVo = new JobPersonResVo();
        jobPersonResVo.setTelphone(jobSettingDetailResVo.getTelphone());
        jobPersonResVo.setName(jobSettingDetailResVo.getName());
        return jobPersonResVo;
    }

    @Override
    public List<BhUserResVo> userList(BhDeptUserReqVo reqVo) {

        List<BhUserResVo> userList = bhMapper.selectBhUserListByReqVo(reqVo);
        return userList;
    }


    @Override
    public List<JSONObject> deptList(BhDeptUserReqVo reqVo) {
        List<JSONObject> userList = bhMapper.selectBhDeptListByReqVo(reqVo);
        return userList;
    }
}
