package com.ygkj.water.project.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.DataMaintainDirectory;
import com.ygkj.project.model.DataMaintainInfo;
import com.ygkj.project.model.SixPeopleInfo;
import com.ygkj.project.model.ThirdPartyUnitInfo;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.project.vo.response.AttSixchargeBaseRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.ProjectManageApplication;
import com.ygkj.water.project.mapper.AttSixchargeBaseMapper;
import com.ygkj.water.project.mapper.DataMaintainDirectoryMapper;
import com.ygkj.water.project.service.AttSixchargeService;
import com.ygkj.water.project.service.DataMaintainDirectoryService;
import com.ygkj.water.project.service.DataMaintainInfoService;
import com.ygkj.water.project.service.DataMaintainTaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = ProjectManageApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@Component
@Slf4j
public class DataMaintainTask {
    @Autowired
    private AttSixchargeBaseMapper attSixchargeBaseMapper;
    @Resource
    private DataMaintainDirectoryService maintainDirectoryService;
    @Resource
    private DataMaintainDirectoryMapper maintainDirectoryMapper;
    @Resource
    private DataMaintainInfoService infoService;

//    @Scheduled(cron = "0 0 0/1 * * ?")
    @Test
    public void task(){
        try {
            AttSixchargeBaseReqVo vo = new AttSixchargeBaseReqVo();
            List<AttSixchargeBaseRespVo> list = attSixchargeBaseMapper.queryPage(vo);
            List<String> projectSyns = attSixchargeBaseMapper.queryProjectSyn();
            DataMaintainDirectory sixCharge = maintainDirectoryMapper.queryByName("工程运管");
            if (sixCharge == null) {
                sixCharge = new DataMaintainDirectory();
                sixCharge.setPid("1");
                sixCharge.setDirectoryName("工程运管");
                maintainDirectoryService.dirInsert(sixCharge);
            }
            if (list != null && list.size() > 0) {
                for (AttSixchargeBaseRespVo respVo : list) {
                    if (!projectSyns.contains(respVo.getProjectCode())){
                        continue;
                    }
                    //根据业主需求，工程运管下的工程目录还要归到各工程类型目录下
                    DataMaintainDirectory projectType = maintainDirectoryMapper.queryByPidAndName(sixCharge.getId(), respVo.getProjectType());
                    if (projectType == null) {
                        projectType = new DataMaintainDirectory();
                        projectType.setPid(sixCharge.getId());
                        projectType.setDirectoryName(respVo.getProjectType());
                        maintainDirectoryService.dirInsert(projectType);
                    }
                    DataMaintainDirectory directory = maintainDirectoryMapper.queryByPidAndName(projectType.getId(),respVo.getProjectName());
                    //为空就新增目录，同时新增信息，否则更新信息
                    if (directory == null) {
                        DataMaintainDirectory directory1 = new DataMaintainDirectory();
                        directory1.setPid(projectType.getId());
                        directory1.setDirectoryName(respVo.getProjectName());
                        maintainDirectoryService.dirInsert(directory1);

                        DataMaintainInfo info = new DataMaintainInfo();
                        info.setId(IdWorker.get32UUID());
                        info.setDirectoryId(directory1.getId());
                        info.setDirectoryName(directory1.getDirPath() + "-" + directory1.getDirectoryName());
                        info.setDataItem("工程信息");
                        info.setSource("工程运管");
                        info.setSixPeople(assemble(respVo));;
                        infoService.save(info);
                    } else {
                        QueryWrapper<DataMaintainInfo> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("directory_id", directory.getId());
                        queryWrapper.eq("data_item", "工程信息");
                        queryWrapper.eq("source", "工程运管");
                        DataMaintainInfo dataMaintainInfo = infoService.getOne(queryWrapper);
                        if (dataMaintainInfo != null) {
                            dataMaintainInfo.setUnitName(respVo.getCompetentDepartmentChargeDept());
                            dataMaintainInfo.setUnitDirector(respVo.getCompetentDepartmentChargeName());
                            dataMaintainInfo.setUnitDirectorPhone(respVo.getCompetentDepartmentChargePhone());
                            dataMaintainInfo.setSixPeople(assemble(respVo));
                            infoService.updateById(dataMaintainInfo);
                        } else {
                            DataMaintainInfo info = new DataMaintainInfo();
                            info.setId(IdWorker.get32UUID());
                            info.setDirectoryId(directory.getId());
                            info.setDirectoryName(directory.getDirPath() + "-" + directory.getDirectoryName());
                            info.setDataItem("工程信息");
                            info.setSource("工程运管");
                            info.setSixPeople(assemble(respVo));
                            infoService.save(info);
                        }

                    }
                }
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    private String assemble(AttSixchargeBaseRespVo respVo){
        List<SixPeopleInfo> sixPeopleInfos = new ArrayList<>();
        if (StringUtils.isNotBlank(respVo.getCompetentDepartmentChargeName())){
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName(respVo.getCompetentDepartmentChargeDept());
            sixPeopleInfo.setUnitDirector(respVo.getCompetentDepartmentChargeName());
            sixPeopleInfo.setUnitDirectorPhone(respVo.getCompetentDepartmentChargePhone());
            sixPeopleInfo.setUnitJob(respVo.getCompetentDepartmentChargeJob());
            sixPeopleInfo.setUnitType("产权人责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }else {
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName("");
            sixPeopleInfo.setUnitDirector("");
            sixPeopleInfo.setUnitDirectorPhone("");
            sixPeopleInfo.setUnitJob("");
            sixPeopleInfo.setUnitType("产权人责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }
        if (StringUtils.isNotBlank(respVo.getManageDepartmentChargeName())){
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName(respVo.getManageDepartmentChargeDept());
            sixPeopleInfo.setUnitDirector(respVo.getManageDepartmentChargeName());
            sixPeopleInfo.setUnitDirectorPhone(respVo.getManageDepartmentChargePhone());
            sixPeopleInfo.setUnitJob(respVo.getManageDepartmentChargeJob());
            if ("有".equals(respVo.getIfDepartment())){
                sixPeopleInfo.setUnitType("管理单位责任人");
            }else if ("无".equals(respVo.getIfDepartment())){
                sixPeopleInfo.setUnitType("物业单位管理责任人");
            }
            sixPeopleInfos.add(sixPeopleInfo);
        }else {
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName("");
            sixPeopleInfo.setUnitDirector("");
            sixPeopleInfo.setUnitDirectorPhone("");
            sixPeopleInfo.setUnitJob("");
            if ("有".equals(respVo.getIfDepartment())){
                sixPeopleInfo.setUnitType("管理单位责任人");
            }else if ("无".equals(respVo.getIfDepartment())){
                sixPeopleInfo.setUnitType("物业单位管理责任人");
            }
            sixPeopleInfos.add(sixPeopleInfo);
        }
        if (StringUtils.isNotBlank(respVo.getPatrolChargeName())){
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName(respVo.getPatrolChargeDept());
            sixPeopleInfo.setUnitDirector(respVo.getPatrolChargeName());
            sixPeopleInfo.setUnitDirectorPhone(respVo.getPatrolChargePhone());
            sixPeopleInfo.setUnitJob(respVo.getPatrolChargeJob());
            sixPeopleInfo.setUnitType("巡查责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }else {
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName("");
            sixPeopleInfo.setUnitDirector("");
            sixPeopleInfo.setUnitDirectorPhone("");
            sixPeopleInfo.setUnitJob("");
            sixPeopleInfo.setUnitType("巡查责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }

        if (StringUtils.isNotBlank(respVo.getTechnologyChargeName())){
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName(respVo.getTechnologyChargeDept());
            sixPeopleInfo.setUnitDirector(respVo.getTechnologyChargeName());
            sixPeopleInfo.setUnitDirectorPhone(respVo.getTechnologyChargePhone());
            sixPeopleInfo.setUnitJob(respVo.getTechnologyChargeJob());
            sixPeopleInfo.setUnitType("技术责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }else {
            SixPeopleInfo sixPeopleInfo = new SixPeopleInfo();
            sixPeopleInfo.setUnitName("");
            sixPeopleInfo.setUnitDirector("");
            sixPeopleInfo.setUnitDirectorPhone("");
            sixPeopleInfo.setUnitJob("");
            sixPeopleInfo.setUnitType("技术责任人");
            sixPeopleInfos.add(sixPeopleInfo);
        }
        return JSON.toJSONString(sixPeopleInfos);
    }
}
