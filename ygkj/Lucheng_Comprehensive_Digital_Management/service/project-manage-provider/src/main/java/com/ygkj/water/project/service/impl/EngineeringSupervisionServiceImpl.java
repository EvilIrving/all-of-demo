package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.project.model.AttProjectOtherBase;
import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.MountainPondWarnResponse;
import com.ygkj.project.vo.request.AllProjectReqVo;
import com.ygkj.project.vo.request.DelProjectReqVo;
import com.ygkj.project.vo.request.ProjectEditReqVo;
import com.ygkj.project.vo.response.AllProjectResVo;
import com.ygkj.project.vo.response.DictResVo;
import com.ygkj.project.vo.response.ProjectResVo;
import com.ygkj.project.vo.response.ThreeChargeResVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.factory.SysSendMessageLogCreator;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.EngScalEnum;
import com.ygkj.water.project.enums.ProjectTypeEnum;
import com.ygkj.water.project.feign.SmsFeignClient;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.EngineeringSupervisionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("engineeringSupervisionService")
public class EngineeringSupervisionServiceImpl implements EngineeringSupervisionService {

    @Value("${dict.basin}")
    private List<String> basList;

    @Value("${dict.scale}")
    private List<String> scaleList;

    @Value("${dict.grad}")
    private List<String> gradList;

    @Resource
    private EngineeringSupervisionMapper engineeringSupervisionMapper;

    @Resource
    private AttProjectOtherMapper attProjectOtherMapper;

    @Resource
    private AttAdcdBaseMapper attAdcdBaseMapper;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private AttSixchargeBaseMapper attSixchargeBaseMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private SmsFeignClient smsClient;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private MountainPondWarnResponseMapper responseMapper;

    /**
     * 工程类型统计
     *
     * @param type 统计方式 1流域 2类型 3行政区划
     * @return
     */
    @Override
    public List<DictResVo> countProject(Integer type) {
        List<DictResVo> dictResVos = engineeringSupervisionMapper.countProject(type == 1 ? "bas" : type == 2 ? "project_type" : "adcd");
        if (type == 1) {
            //流域翻译
            dictResVos.forEach(dictResVo -> {
                if (StringUtils.isEmpty(dictResVo.getDictCode())) {
                    dictResVo.setDictCode("未知");
                }
            });
        } else if (type == 2) {
            String[] strings = new String[]{"水库", "水电站", "泵站", "水闸", "堤防", "海塘", "山塘", "河道", "其他"};
            Set<String> typeSet = dictResVos.stream().map(DictResVo::getDictCode).collect(Collectors.toSet());
            //没有的类型给个0
            for (String s : strings) {
                if (!typeSet.contains(s)) {
                    DictResVo dictResVo = new DictResVo();
                    dictResVo.setDictCode(s);
                    dictResVo.setDictValue("0");
                    dictResVos.add(dictResVo);
                }
            }
        } else {
            QueryWrapper<AttAdcdBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AttAdcdBase::getParentAdcd, "330300000000").or().eq(AttAdcdBase::getAdcd, "330300000000");
            //行政区划翻译
            List<AttAdcdBase> attAdcdBases = attAdcdBaseMapper.selectList(queryWrapper);
            Map<String, String> adcdMap = attAdcdBases.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
            dictResVos.forEach(dictResVo -> {
                String adcd = dictResVo.getDictCode();
                if (adcdMap.containsKey(adcd)) {
                    dictResVo.setDictCode(adcdMap.get(adcd));
                } else {
                    dictResVo.setDictCode("未知");
                }
            });
        }
        return dictResVos;
    }

    /**
     * 流域列表
     *
     * @return
     */
    @Override
    public List<DictResVo> listBas() {
        return turn(basList);
    }

    private List<DictResVo> turn(List<String> sourceList) {
        List<DictResVo> result = new ArrayList<>();
        sourceList.forEach(s -> {
            DictResVo dictResVo = new DictResVo();
            dictResVo.setDictValue(s);
            EngScalEnum engScalEnum = EngScalEnum.getByDesc(s);
            if (engScalEnum != null) {
                dictResVo.setDictCode(engScalEnum.getLevel());
            }
            result.add(dictResVo);
        });
        return result;
    }

    /**
     * 工程规模列表
     *
     * @return
     */
    @Override
    public List<DictResVo> listScale() {
        return turn(scaleList);
    }

    /**
     * 工程等别列表
     *
     * @return
     */
    @Override
    public List<DictResVo> listGrad() {
        return turn(gradList);
    }

    /**
     * 工程列表
     *
     * @param allProjectReqVo
     * @return
     */
    @Override
    public PageVO<AllProjectResVo> pageProject(AllProjectReqVo allProjectReqVo) {
        PageHelper.startPage(allProjectReqVo.getPageNum(), allProjectReqVo.getPageSize());
        List<AllProjectResVo> allProjectResVos = listProject(allProjectReqVo);
        return PageUtils.getPageVO(allProjectResVos);
    }

    /**
     * 工程列表
     *
     * @param allProjectReqVo
     * @return
     */
    @Override
    public List<AllProjectResVo> listProject(AllProjectReqVo allProjectReqVo) {
        List<AllProjectResVo> allProjectResVos = engineeringSupervisionMapper.listProject(allProjectReqVo);
        if (!CollectionUtils.isEmpty(allProjectResVos)) {
            //获取用户列表
            List<String> idList = new ArrayList<>();
            allProjectResVos.forEach(allProjectResVo -> {
                addId(idList, allProjectResVo.getGovermentChargeId());
                addId(idList, allProjectResVo.getWaterDepartmentChargeId());
                addId(idList, allProjectResVo.getCompetentDepartmentChargeId());
                addId(idList, allProjectResVo.getManageDepartmentChargeId());
                addId(idList, allProjectResVo.getTechnologyChargeId());
                addId(idList, allProjectResVo.getPatrolChargeId());
            });
            if (!CollectionUtils.isEmpty(idList)) {
                String userIds = String.join(",", idList);
                CommonResult data = userFeignClient.users(userIds);
                List<SysUser> users = (List<SysUser>) data.getData();
                Map<String, SysUser> userMap = users.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
                //拼上电话和名称
//                addPhoneAndName(allProjectResVos, userMap);
            }
            String projectType = allProjectReqVo.getProjectType();
            //TODO 除了河道和其他,其它再加上是否是病险工程
            if (!ProjectTypeEnum.RIVER.getType().equals(projectType) && !ProjectTypeEnum.OTHER.getType().equals(projectType)) {

            }
        }
        return allProjectResVos;
    }

    private void addPhoneAndName(List<AllProjectResVo> allProjectResVos, Map<String, SysUser> userMap) {
        allProjectResVos.forEach(allProjectResVo -> {
            SysUser sysUser;
            String userId = allProjectResVo.getGovermentChargeId();
            //政府负责人
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setGovermentChargeName(sysUser.getUsername());
                allProjectResVo.setGovermentChargePhone(sysUser.getPhone());
            }

            //水行政部门负责人
            userId = allProjectResVo.getWaterDepartmentChargeId();
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setWaterDepartmentChargeName(sysUser.getUsername());
                allProjectResVo.setWaterDepartmentChargePhone(sysUser.getPhone());
            }

            //主管部门负责人
            userId = allProjectResVo.getCompetentDepartmentChargeId();
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setCompetentDepartmentChargeName(sysUser.getUsername());
                allProjectResVo.setCompetentDepartmentChargePhone(sysUser.getPhone());
            }

            //管理单位责任人
            userId = allProjectResVo.getManageDepartmentChargeId();
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setManageDepartmentChargeName(sysUser.getUsername());
                allProjectResVo.setManageDepartmentChargePhone(sysUser.getPhone());
            }

            //技术责任人
            userId = allProjectResVo.getTechnologyChargeId();
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setTechnologyChargeName(sysUser.getUsername());
                allProjectResVo.setTechnologyChargePhone(sysUser.getPhone());
            }

            //巡查责任人
            userId = allProjectResVo.getPatrolChargeId();
            if (userMap.containsKey(userId)) {
                sysUser = userMap.get(userId);
                allProjectResVo.setPatrolChargeName(sysUser.getUsername());
                allProjectResVo.setPatrolChargePhone(sysUser.getPhone());
            }
        });
    }

    private void addId(List<String> idList, String userId) {
        if (!StringUtils.isEmpty(userId)) {
            idList.add(userId);
        }
    }

    /**
     * 编辑工程信息
     *
     * @param projectEditReqVo
     */
    @Override
    public void editProject(ProjectEditReqVo projectEditReqVo) {
        //其他类型特殊处理
        String projectType = projectEditReqVo.getProjectType();
        if (ProjectTypeEnum.OTHER.getType().equals(projectType)) {
            AttProjectOtherBase attProjectOtherBase = new AttProjectOtherBase();
            BeanUtils.copyProperties(projectEditReqVo, attProjectOtherBase);
            attProjectOtherBase.setAreaCode(projectEditReqVo.getAdcd());
            if (StringUtils.isEmpty(projectEditReqVo.getProjectCode())) {
                attProjectOtherBase.setProjectCode(String.valueOf(snowFlake.nextId()));
                attProjectOtherMapper.insert(attProjectOtherBase);
            } else {
                UpdateWrapper<AttProjectOtherBase> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(AttProjectOtherBase::getProjectCode, attProjectOtherBase.getProjectCode());
                attProjectOtherMapper.update(attProjectOtherBase, updateWrapper);
            }
        } else {
            //线图层更新线
            if (ProjectTypeEnum.DIKE.getType().equals(projectType)) {
                projectEditReqVo.setProjectType("dike");
                engineeringSupervisionMapper.updateLine(projectEditReqVo);
            } else if (ProjectTypeEnum.SEAWALL.getType().equals(projectType)) {
                projectEditReqVo.setProjectType("seawall");
                engineeringSupervisionMapper.updateLine(projectEditReqVo);
            } else
                //点图层更新经纬度
                if (ProjectTypeEnum.RESERVOIR.getType().equals(projectType)) {
                    engineeringSupervisionMapper.updateResPoint(projectEditReqVo);
                } else if (ProjectTypeEnum.HYDROPOWER.getType().equals(projectType)) {
                    engineeringSupervisionMapper.updateHyPoint(projectEditReqVo);
                } else if (ProjectTypeEnum.PUMP.getType().equals(projectType)) {
                    engineeringSupervisionMapper.updatePuPoint(projectEditReqVo);
                } else if (ProjectTypeEnum.SLUICE.getType().equals(projectType)) {
                    engineeringSupervisionMapper.updateWaPoint(projectEditReqVo);
                } else if (ProjectTypeEnum.POOL.getType().equals(projectType)) {
                    engineeringSupervisionMapper.updateMnPoint(projectEditReqVo);
                }
        }
    }

    /**
     * 工程详细信息
     *
     * @param projectCode
     * @return
     */
    @Override
    public AllProjectResVo projectDetail(String projectCode) {
        AllProjectResVo allProjectResVo = engineeringSupervisionMapper.projectDetail(projectCode);
        String projectType = allProjectResVo.getProjectType();
        //海塘,堤防加上线
        if (ProjectTypeEnum.DIKE.getType().equals(projectType)) {
            allProjectResVo.setPoints(engineeringSupervisionMapper.queryPoints("dike", projectCode));
        } else if (ProjectTypeEnum.SEAWALL.getType().equals(projectType)) {
            allProjectResVo.setPoints(engineeringSupervisionMapper.queryPoints("seawall", projectCode));
        }
        return allProjectResVo;
    }

    /**
     * 三个责任人详情
     *
     * @param projectCode
     * @return
     */
    @Override
    public List<ThreeChargeResVo> threeCharge(String projectCode) {
        List<ThreeChargeResVo> result = new ArrayList<>();
        ProjectResVo projectResVo = projectMapper.selectProjectByProCode(projectCode);
        if (ObjectUtils.isEmpty(projectResVo)){
            return result;
        }
        if ("山塘".equals(projectResVo.getProjectType())){
            QueryWrapper<MountainPondWarnResponse> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MountainPondWarnResponse::getMnpdCode, projectCode);
            MountainPondWarnResponse warnResponse = responseMapper.selectOne(queryWrapper);
            if (warnResponse != null) {
                ThreeChargeResVo superCharger = new ThreeChargeResVo();
                superCharger.setType("监管部门分管负责人");
                superCharger.setUsername(warnResponse.getSuperChargerName());
                superCharger.setPhone(warnResponse.getSuperChargerPhone());
                superCharger.setJob(warnResponse.getSuperChargerPosition());
                result.add(superCharger);

                ThreeChargeResVo superBasic = new ThreeChargeResVo();
                superBasic.setType("基层水利员");
                superBasic.setUsername(warnResponse.getSuperBasicName());
                superBasic.setPhone(warnResponse.getSuperBasicPhone());
                superBasic.setJob("");
                result.add(superBasic);

                ThreeChargeResVo manageCharger = new ThreeChargeResVo();
                manageCharger.setType("管理部门负责人");
                manageCharger.setUsername(warnResponse.getManageChargerName());
                manageCharger.setPhone(warnResponse.getManageChargerPhone());
                manageCharger.setJob(warnResponse.getManageChargerPosition());
                result.add(manageCharger);

                ThreeChargeResVo managePatrol= new ThreeChargeResVo();
                managePatrol.setType("巡查员");
                managePatrol.setUsername(warnResponse.getManagePatrolName());
                managePatrol.setPhone(warnResponse.getManagePatrolPhone());
                managePatrol.setJob("");
                result.add(managePatrol);
            }
        }else {
            QueryWrapper<AttSixchargeBase> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AttSixchargeBase::getProjectCode, projectCode);
            AttSixchargeBase attSixchargeBase = attSixchargeBaseMapper.selectOne(queryWrapper);
            if (attSixchargeBase != null) {
                ThreeChargeResVo goverment = new ThreeChargeResVo();
                goverment.setType("政府负责人");
                goverment.setUsername(attSixchargeBase.getGovermentChargeName());
                goverment.setPhone(attSixchargeBase.getGovermentChargePhone());
                goverment.setJob(attSixchargeBase.getGovermentChargeJob());
                result.add(goverment);

                ThreeChargeResVo waterDepartment = new ThreeChargeResVo();
                waterDepartment.setType("水行政部门负责人");
                waterDepartment.setUsername(attSixchargeBase.getWaterDepartmentChargeName());
                waterDepartment.setPhone(attSixchargeBase.getWaterDepartmentChargePhone());
                waterDepartment.setJob(attSixchargeBase.getWaterDepartmentChargeJob());
                result.add(waterDepartment);

                ThreeChargeResVo competentDepartment = new ThreeChargeResVo();
                competentDepartment.setType("主管部门负责人");
                competentDepartment.setUsername(attSixchargeBase.getCompetentDepartmentChargeName());
                competentDepartment.setPhone(attSixchargeBase.getCompetentDepartmentChargePhone());
                competentDepartment.setJob(attSixchargeBase.getCompetentDepartmentChargeJob());
                result.add(competentDepartment);

                ThreeChargeResVo manageDepartment = new ThreeChargeResVo();
                manageDepartment.setType("管理单位责任人");
                manageDepartment.setUsername(attSixchargeBase.getManageDepartmentChargeName());
                manageDepartment.setPhone(attSixchargeBase.getManageDepartmentChargePhone());
                manageDepartment.setJob(attSixchargeBase.getManageDepartmentChargeJob());
                result.add(manageDepartment);

                ThreeChargeResVo technology = new ThreeChargeResVo();
                technology.setType("技术责任人");
                technology.setUsername(attSixchargeBase.getTechnologyChargeName());
                technology.setPhone(attSixchargeBase.getTechnologyChargePhone());
                technology.setJob(attSixchargeBase.getTechnologyChargeJob());
                result.add(technology);

                ThreeChargeResVo patrol = new ThreeChargeResVo();
                patrol.setType("巡查责任人");
                patrol.setUsername(attSixchargeBase.getPatrolChargeName());
                patrol.setPhone(attSixchargeBase.getTechnologyChargePhone());
                patrol.setJob(attSixchargeBase.getTechnologyChargeJob());
                result.add(patrol);
            }

        }
        return result;
//        AllProjectResVo allProjectResVo = projectDetail(projectCode);
//        List<String> idList = new ArrayList<>();
//        addId(idList, allProjectResVo.getGovermentChargeId());
//        addId(idList, allProjectResVo.getWaterDepartmentChargeId());
//        addId(idList, allProjectResVo.getCompetentDepartmentChargeId());
//        addId(idList, allProjectResVo.getManageDepartmentChargeId());
//        addId(idList, allProjectResVo.getTechnologyChargeId());
//        addId(idList, allProjectResVo.getPatrolChargeId());
//        List<ThreeChargeResVo> result = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(idList)) {
//            String userIds = String.join(",", idList);
//            List<SysUser> users = userFeignClient.users(userIds);
//            users.forEach(sysUser -> {
//                String userId = sysUser.getId();
//                ThreeChargeResVo threeChargeResVo = new ThreeChargeResVo();
//                threeChargeResVo.setId(userId);
//                threeChargeResVo.setJob(sysUser.getJob());
//                threeChargeResVo.setUsername(sysUser.getUsername());
//                threeChargeResVo.setDeptName(sysUser.getDept().getName());
//                addThreeCharge(allProjectResVo.getGovermentChargeId(), userId, "政府负责人", result, sysUser);
//                addThreeCharge(allProjectResVo.getWaterDepartmentChargeId(), userId, "水行政部门负责人", result, sysUser);
//                addThreeCharge(allProjectResVo.getCompetentDepartmentChargeId(), userId, "主管部门负责人", result, sysUser);
//                addThreeCharge(allProjectResVo.getManageDepartmentChargeId(), userId, "管理单位责任人", result, sysUser);
//                addThreeCharge(allProjectResVo.getTechnologyChargeId(), userId, "技术责任人", result, sysUser);
//                addThreeCharge(allProjectResVo.getPatrolChargeId(), userId, "巡查责任人", result, sysUser);
//            });
//        }
//        return result;
    }

    /**
     * 添加责任人
     *
     * @param chargeId
     * @param userId
     * @param chargeType
     * @param result
     * @param sysUser
     */
    private void addThreeCharge(String chargeId, String userId, String chargeType, List<ThreeChargeResVo> result, SysUser sysUser) {
        if (userId.equals(chargeId)) {
            ThreeChargeResVo threeChargeResVo = new ThreeChargeResVo();
            threeChargeResVo.setPhone(sysUser.getPhone());
            threeChargeResVo.setId(userId);
            threeChargeResVo.setJob(sysUser.getJob());
            threeChargeResVo.setUsername(sysUser.getUsername());
            threeChargeResVo.setDeptName(sysUser.getDept().getName());
            threeChargeResVo.setType(chargeType);
            result.add(threeChargeResVo);
        }
    }

    private static Map<String, String> typeMap = new HashMap<String, String>() {{
        put("水库", "res");
        put("水电站", "hyst");
        put("泵站", "pust");
        put("水闸", "waga");
        put("堤防", "dike");
        put("海塘", "seawall");
        put("山塘", "mnpd");
        put("河道", "river");
        put("其他", "project_other");
    }};

    /**
     * 批量删除工程
     *
     * @param reqVo
     */
    @Override
    public void delProject(DelProjectReqVo reqVo) {
        reqVo.setIdList(Arrays.asList(reqVo.getIds().split(",").clone()));
        if (!"其他".equals(reqVo.getProjectType())) {
            reqVo.setProjectType(typeMap.get(reqVo.getProjectType()));
            engineeringSupervisionMapper.delProject(reqVo);
        } else {
            engineeringSupervisionMapper.delOtherProject(reqVo);
        }
    }


    /**
     * 给责任人发送消息
     *
     * @param phone
     * @param content
     * @param sendType
     * @return
     */
    @Override
    public Object sendMsg(String phone, String content, String sendType) {

        List<SysSendMessageLog> logs = new ArrayList<>();
//        SysUser sysUser = authenticationClient.detailInfo(reqVo.getRecUserId());

        SysSendMessageLog log = SysSendMessageLogCreator.createMessage(content, phone, sendType,
                "水库预警", SysSendMessageLog.SystemType.WATER_MANAGE.type(), SysSendMessageLog.messageType.NOTICE.type());
        logs.add(log);
        smsClient.sendMessage(logs);
        return null;
    }
}
