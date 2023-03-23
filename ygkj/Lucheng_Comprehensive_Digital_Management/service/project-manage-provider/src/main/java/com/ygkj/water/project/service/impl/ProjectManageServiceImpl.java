package com.ygkj.water.project.service.impl;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.google.common.util.concurrent.AtomicDouble;
import com.ygkj.entity.PointEntity;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.SysUser;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.exception.CustomException;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.EngScalEnum;
import com.ygkj.water.project.enums.ProjectTypeTableEnum;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("projectManageService")
@Slf4j
public class ProjectManageServiceImpl implements ProjectManageService {
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private CameraRecordMapper cameraRecordMapper;
    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserFeignClient userFeignClient;
    @Resource
    private WarningTemplateMapper warningTemplateMapper;
    @Autowired
    private SnowFlake snowFlake;
    @Resource
    private EngineeringSupervisionMapper supervisionMapper;
    @Resource
    private WarningConfigMapper warningConfigMapper;
    @Resource
    private WarningRecordMapper warningRecordMapper;
    @Autowired
    private EngineeringSupervisionService engineeringSupervisionService;
    @Resource
    private AttAdcdBaseMapper attAdcdBaseMapper;
    @Resource
    private AttProjBuildContentMapper attProjBuildContentMapper;
    @Resource
    private AttProjLinkMapper attProjLinkMapper;
    @Resource
    StationMapper stationMapper;
    @Resource
    WaterAndRainMapper waterAndRainMapper;

    @Autowired
    AdcdBaseService adcdBaseService;

    @Autowired
    ProjectStatusService projectStatusService;

    @Autowired
    RsvrService rsvrService;

    @Autowired
    WagaService wagaService;

    @Autowired
    SeawallService seawallService;

    @Autowired
    private BmSeawallStructinfoMapper bmSeawallStructinfoMapper;

    private final static String grad = "级";

    /**
     * L1("大（Ⅰ）型","1"),
     * L2("大（Ⅱ）型","2"),
     * MID("中型","3"),
     * S1("小（Ⅰ）型","4"),
     * S2("小（Ⅱ）型","5");
     *
     * @param projectManageQueryVo
     * @return
     */
    @Override
    public List<?> projectList(ProjectManageQueryVo projectManageQueryVo) {
        if (StringUtils.isNotBlank(projectManageQueryVo.getAreaCode())) {
            projectManageQueryVo.setAreaCode(projectManageQueryVo.getAreaCode().startsWith("330") ? projectManageQueryVo.getAreaCode() :
                    attAdcdBaseMapper.getWenzhouAreaCodeByAdnm(projectManageQueryVo.getAreaCode()));
        }
        // 转换工程规模
        if (StringUtils.isNotBlank(projectManageQueryVo.getLevel())) {
            EngScalEnum engScalEnum = EngScalEnum.getByDesc(projectManageQueryVo.getLevel());
            if (engScalEnum != null) {
                projectManageQueryVo.setLevel(engScalEnum.getLevel());
            }
        }
        // 转换工程等别
        if (StringUtils.isNotBlank(projectManageQueryVo.getGrade())) {
            projectManageQueryVo.setGrade(projectManageQueryVo.getGrade().replace(grad, StringUtils.EMPTY));
        }
        switch (projectManageQueryVo.getType()) {
            case "大中型水库":
            case "小型水库":
            case "水库": {
                List<AttResBase> temp = projectMapper.selectReservoirByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;

                // 2021-05-31 张仁贡要求加上水库水位
                if (projectManageQueryVo.getWithWaterLevel() != null
                        && projectManageQueryVo.getWithWaterLevel()) {
                    return rsvrService.fillWater2Rsvr(temp, new RsvrStationWaterReqVo());
                } else {
                    List<RsvrStationWaterResVo> result = new ArrayList<>(temp.size());
                    List<String> adcdList = temp.stream().map(AttResBase::getAreaCode).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                    Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
                    // 设置工程等别以及是否病险工程信息
                    for (AttResBase o : temp) {
                        RsvrStationWaterResVo target = new RsvrStationWaterResVo();
                        BeanUtils.copyProperties(o, target);
                        Optional.ofNullable(EngScalEnum.getByLevel(target.getEngScal()))
                                .ifPresent(e -> target.setEngScal(e.getDesc()));
                        Optional.ofNullable(adcdMap.get(target.getAreaCode())).ifPresent(target::setAreaName);
                        o.judgeDilapProj();
                        result.add(target);
                    }
                    return result;
                }
            }
            case "泵站": {
                List<AttPustBase> temp = projectMapper.selectPumpByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                temp.forEach(o -> {
                    Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
                            .ifPresent(e -> o.setEngScal(e.getDesc()));
                });
                return temp;
            }
            case "堤防": {
                List<AttDikeBase> temp = projectMapper.selectDikeByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                temp.forEach(o -> {
                    if (StringUtils.isNotBlank(o.getDikeGrad())) {
                        o.setDikeGrad(o.getDikeGrad() + grad);
                        if ("7".equals(o.getDikeGrad())){
                            o.setDikeGrad("护岸工程");
                        }
                    }
                });
                return temp;
            }
            case "山塘": {
                List<AttMnpdBase> temp = projectMapper.selectMountainPoolByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                temp.forEach(AttMnpdBase::judgeDilapProj);
                return temp;
            }
            case "水闸": {
                List<AttWagaBase> temp = projectMapper.selectWagaByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                temp.forEach(o -> {
                    Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
                            .ifPresent(e -> o.setEngScal(e.getDesc()));
                    o.judgeDilapProj();
                    //尺寸进行更改
                    if(StringUtils.isNotBlank(o.getGateSize())){
                        List<String> strings = Arrays.asList(o.getGateSize().split("\\*"));
                        if (strings.size() > 1){
                            o.setGatewidth(strings.get(0));
                            o.setGateSize(strings.get(1));
                        }
                    }
                });
                // 2021-05-31 张仁贡要求加上水闸工情
                if (projectManageQueryVo.getWithWaterLevel() != null
                        && projectManageQueryVo.getWithWaterLevel()) {
                    return wagaService.fillWater2Waga(temp, new WagaStationWaterReqVo());
                } else {
                    return temp;
                }
            }
            case "海塘": {
//                List<AttSeawallBase> temp = projectMapper.selectSeawallByCondition(projectManageQueryVo);
//                temp = temp == null ? Collections.emptyList() : temp;
//                temp.forEach(o -> {
//                    if (StringUtils.isNotBlank(o.getSeawallLevel())) {
//                        o.setSeawallLevel(o.getSeawallLevel() + grad);
//                    }
//                    o.judgeDilapProj();
//                });
//                return temp;
                return this.projectList4Seawall(projectManageQueryVo);
            }
            case "水电站": {
                List<AttHystBase> temp = projectMapper.selectHydristByCondition(projectManageQueryVo);
//                temp = temp == null ? Collections.emptyList() : temp;
//                temp.forEach(o -> {
//                    Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
//                            .ifPresent(e -> o.setEngScal(e.getDesc()));
//                });
                return temp;
            }
            case "农村供水工程":
                return projectMapper.selectWaterSupplyProjectByCondition(projectManageQueryVo);
            case "闸站": {
                List<AttGateBase> temp = projectMapper.selectGateProjectByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                temp.forEach(o -> {
                    Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
                            .ifPresent(e -> o.setEngScal(e.getDesc()));
                });
                return temp;
            }

            case "农饮水":
                return projectMapper.selectCwsProjectByCondition(projectManageQueryVo);
            case "灌区": {
                List<AttIrrBase> temp = projectMapper.selectIrrProjectByCondition(projectManageQueryVo);
                temp = temp == null ? Collections.emptyList() : temp;
                List<String> adcdList = temp.stream().map(AttIrrBase::getAdcd).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
                for (AttIrrBase o : temp) {
                    Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
                            .ifPresent(e -> o.setEngScal(e.getDesc()));
                    Optional.ofNullable(adcdMap.get(o.getAdcd())).ifPresent(o::setAdnm);
                }
                return temp;
            }
            case "在建工程":
                return projectMapper.selectBuildingProjectByCondition(projectManageQueryVo);
            case "海塘道路交叉出入口":
                projectManageQueryVo.setType("道路");
                return projectMapper.selectBmAccBuildByCondition(projectManageQueryVo);
            case "码头":
            case "桥梁":
            case "旱闸":
            case "箱涵":
            case "管理房":
            case "测站平台":
            case "规模以下水闸":
                return projectMapper.selectBmAccBuildByCondition(projectManageQueryVo);
            case "桩号":
                return bmSeawallStructinfoMapper.loadPage(Builder
                        .of(BmSeawallStructinfoReqVo::new)
                        .with(BmSeawallStructinfoReqVo::setPrcd, projectManageQueryVo.getCode())
                        .with(BmSeawallStructinfoReqVo::setPageNum, projectManageQueryVo.getPageNum())
                        .with(BmSeawallStructinfoReqVo::setPageSize, projectManageQueryVo.getPageSize())
                        .build());
            default:
                return supervisionMapper.listProject(new AllProjectReqVo("其他", projectManageQueryVo.getCode()
                        , projectManageQueryVo.getProjectName()));
        }
    }

    public List<? extends AttSeawallBase> projectList4Seawall(ProjectManageQueryVo projectManageQueryVo) {
        List<AttSeawallBase> temp = projectMapper.selectSeawallByCondition(projectManageQueryVo);
        temp = temp == null ? Collections.emptyList() : temp;
        for (AttSeawallBase o : temp) {
            if (StringUtils.isNotBlank(o.getSeawallLevel())) {
                o.setSeawallLevel(o.getSeawallLevel() + grad);
            }
            o.judgeDilapProj();
        }
        if (!projectManageQueryVo.isSeawallMonitorWarn()) {
            return temp;
        } else {
            List<SeawallResVo> result = new ArrayList<>(temp.size());
            for (AttSeawallBase o : temp) {
                SeawallResVo target = new SeawallResVo();
                BeanUtils.copyProperties(o, target);
                result.add(target);
            }
            seawallService.fillTdzMonitorWarnState(result, false, null);
            return result;
        }
    }

    /**
     * 工程详情查询的统计
     *
     * @param queryVo
     * @return
     * @see ProjectManageService#projectList(ProjectManageQueryVo)
     */
    @Override
    public Map<String, Object> stats4ProjectList(ProjectManageQueryVo queryVo) {
        List<?> projects = projectList(queryVo);
        switch (queryVo.getType()) {
            case "水库": {
                return null;
            }
            case "泵站": {
                return null;
            }
            case "堤防": {
                return null;
            }
            case "山塘": {
                return null;
            }
            case "水闸": {
                return null;
            }
            case "海塘": {
                return null;
            }
            case "水电站": {
                return null;
            }
            case "农村供水工程": {
                return null;
            }
            case "闸站": {
                Map<String, Object> result = new HashMap<>();
                List<AttGateBase> tempList = (List<AttGateBase>) projects;
                result.put("gateNum", tempList.size());
                result.put("totalInsPow", tempList.stream().map(AttGateBase::getSingleInsPow).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                return result;
            }
            case "农饮水": {
                return null;
            }
            case "灌区": {
                List<AttIrrBase> tempList = (List<AttIrrBase>) projects;
                tempList = tempList == null ? Collections.emptyList() : tempList;
                Map<String, Object> result = new HashMap<>();
                result.put("irrNum", tempList.size());
                result.put("desIrrArea", tempList.stream().filter(o -> o.getDesIrrArea() != null).mapToInt(AttIrrBase::getDesIrrArea).sum());
                result.put("effIrrArea", tempList.stream().filter(o -> o.getEffIrrArea() != null).mapToInt(AttIrrBase::getEffIrrArea).sum());
                return result;
            }
            case "在建工程":
                return null;
            default:
                return null;
        }
    }

    @Override
    public List<HikvisionCameraRecord> projectCameras(CameraQueryVo cameraQueryVo) {
        return cameraRecordMapper.selectByCondition(cameraQueryVo);
    }

    @Override
    public List<JSONObject> projectChargePerson(ProjectManageQueryVo queryVo) {
        List<JSONObject> result = new ArrayList<>();
        List<?> projects = projectList(queryVo);
        Object project = projects.get(0);
        if (project != null) {
            //反射，获取不同表统一的责任人字段
            List<String> list = new ArrayList<>();
            try {
                Class<?> projectClass = project.getClass();
                Field govermentChargeIdField = projectClass.getDeclaredField("govermentChargeId");
                govermentChargeIdField.setAccessible(true);
                String govermentChargeId = String.valueOf(govermentChargeIdField.get(project));
                list.add(govermentChargeId);
                Field waterDepartmentChargeIdField = projectClass.getDeclaredField("waterDepartmentChargeId");
                waterDepartmentChargeIdField.setAccessible(true);
                String waterDepartmentChargeId = String.valueOf(waterDepartmentChargeIdField.get(project));
                list.add(waterDepartmentChargeId);
                Field competentDepartmentChargeIdField = projectClass.getDeclaredField("competentDepartmentChargeId");
                competentDepartmentChargeIdField.setAccessible(true);
                String competentDepartmentChargeId = String.valueOf(competentDepartmentChargeIdField.get(project));
                list.add(competentDepartmentChargeId);
                Field manageDepartmentChargeIdField = projectClass.getDeclaredField("manageDepartmentChargeId");
                manageDepartmentChargeIdField.setAccessible(true);
                String manageDepartmentChargeId = String.valueOf(manageDepartmentChargeIdField.get(project));
                list.add(manageDepartmentChargeId);
                //筛一下，不可以为空
                list = list.stream().filter(s -> StringUtils.isNotBlank(s) && !s.equals("null")).collect(Collectors.toList());
                String userCodes = String.join(",", list);
                //判断一下，避免无谓的IO
                if (StringUtils.isNotBlank(userCodes)) {
                    List<SysUser> users = userMapper.load(userCodes);
                    for (SysUser user : users) {
                        String id = user.getId();
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(user);
                        if (id.equals(govermentChargeId)) {
                            jsonObject.put("type", "goverment");
                            jsonObject.put("description", "政府负责人");
                        } else if (id.equals(waterDepartmentChargeId)) {
                            jsonObject.put("type", "waterDepartment");
                            jsonObject.put("description", "水行政部门负责人");
                        } else if (id.equals(competentDepartmentChargeId)) {
                            jsonObject.put("type", "competentDepartment");
                            jsonObject.put("description", "主管部门负责人");
                        } else if (id.equals(manageDepartmentChargeId)) {
                            jsonObject.put("type", "manageDepartment");
                            jsonObject.put("description", "管理单位责任人");
                        }
                        result.add(jsonObject);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public PageVO<CameraResVo> projectCameras(ProjectCameraPageQueryVo queryVo) {
        List<CameraResVo> result = new ArrayList<>();
        AllProjectReqVo reqVo = new AllProjectReqVo();
        BeanUtils.copyProperties(queryVo, reqVo);
        if ("河道".equals(queryVo.getProjectType())) {
            result = cameraRecordMapper.selectByRiverCodes(queryVo.getCameraName(), queryVo.getCameraIndexCode());
        } else {
            List<AllProjectResVo> projects = supervisionMapper.listProject(reqVo);
            List<String> list = projects.stream().map(AllProjectResVo::getProjectCode).collect(Collectors.toList());
            List<String> userCodes = projects.stream().filter(project -> StringUtils.isNotBlank(project.getTechnologyChargeId()))
                    .map(AllProjectResVo::getTechnologyChargeId).collect(Collectors.toList());
            Map<String, List<AllProjectResVo>> map = projects.stream().collect(Collectors.groupingBy(AllProjectResVo::getProjectCode));
            PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
            if (!list.isEmpty()) {
//            list = null;
                result = cameraRecordMapper.selectByProjectCodes(list, queryVo.getCameraName(), queryVo.getCameraIndexCode());
            }
            for (CameraResVo record : result) {
                if (map.containsKey(record.getProjectCode())) {
                    AllProjectResVo vo = map.get(record.getProjectCode()).get(0);
                    record.setProjectName(vo.getProjectName());
                    record.setProjectType(vo.getProjectType());
                    record.setProjectGrade(vo.getEngScal());
                    record.setProjectScale(vo.getEngGrad());
//                    result.add(record);
                }
            }
            if (!userCodes.isEmpty()) {
                CommonResult data = userFeignClient.users(String.join(",", userCodes));
                List<com.ygkj.gragh.model.SysUser> users = (List<com.ygkj.gragh.model.SysUser>) data.getData();
                Map<String, List<com.ygkj.gragh.model.SysUser>> userMap = users.stream().collect(Collectors.groupingBy(com.ygkj.gragh.model.SysUser::getId));
                for (CameraResVo vo : result) {
                    String projectCode = vo.getProjectCode();
                    if (StringUtils.isNotBlank(projectCode) && map.containsKey(projectCode)) {
                        String technologyChargeId = map.get(projectCode).get(0).getTechnologyChargeId();
                        if (userMap.containsKey(technologyChargeId)) {
                            com.ygkj.gragh.model.SysUser sysUser = userMap.get(technologyChargeId).get(0);
                            vo.setTechnologyPerson(sysUser.getRealName());
                            vo.setTechnologyPersonMobile(sysUser.getPhone());
                        }
                    }
                }
            }
        }

        return PageUtils.getPageVO(result);
    }

    @Override
    public boolean addCamera(CameraAddVo cameraAddVo) {
        HikvisionCameraRecord record = new HikvisionCameraRecord();
        BeanUtils.copyProperties(cameraAddVo, record);
        CameraRecord load = cameraRecordMapper.load(cameraAddVo.getCameraIndexCode());
        if (null != load) {
            throw new CustomException(CommonCode.FAILED);
        }
        if (cameraRecordMapper.insert(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCameras(CameraAddVo cameraAddVo) {
        HikvisionCameraRecord record = new HikvisionCameraRecord();
        BeanUtils.copyProperties(cameraAddVo, record);
        if (cameraRecordMapper.update(record) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean batchDeleteCameras(String cameraIndexCodes) {
        if (StringUtils.isNotBlank(cameraIndexCodes)) {
            if (cameraRecordMapper.batchDelete(Arrays.asList(cameraIndexCodes.split(","))) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> countCamerasByProjectType() {
        List<Map<String, Object>> maps = cameraRecordMapper.countCamerasByProjectType();
        String[] strings = new String[]{"水库", "水电站", "泵站", "水闸", "堤防", "海塘", "山塘", "河道", "其他"};
        Set<String> typeSet = maps.stream().map(map -> String.valueOf(map.get("project_type"))).collect(Collectors.toSet());
        //没有的类型给个0
        for (String s : strings) {
            if (!typeSet.contains(s)) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("project_type", s);
                hashMap.put("num", 0);
                maps.add(hashMap);
            }
        }
        return maps;
    }

    @Override
    public List<Map<String, Object>> countCamerasByAdcd() {
        return cameraRecordMapper.countCamerasByAdcd();
    }

    @Override
    public PageVO<WarningTemplate> selectWarningTemplate(WarningTemplateQueryVo queryVo) {
        PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
        return PageUtils.getPageVO(warningTemplateMapper.selectByCondition(queryVo));
    }

    @Override
    public List<WarningTemplate> listWarningTemplate(WarningTemplateQueryVo queryVo) {
        return warningTemplateMapper.selectByCondition(queryVo);
    }

    @Override
    public boolean addWarningTemplate(WarningTemplateAddVo vo) {
        if (StringUtils.isBlank(vo.getId())) {
            vo.setId(String.valueOf(snowFlake.nextId()));
        }
        if (warningTemplateMapper.insert(vo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateWarningTemplate(WarningTemplateAddVo vo) {
        if (warningTemplateMapper.update(vo) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean batchDeleteWarningTemplate(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            if (warningTemplateMapper.batchDelete(Arrays.asList(ids.split(","))) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PageVO<WarningConfig> warningConfigs(WarningConfigPageQueryVo queryVo) {
        AllProjectReqVo reqVo = new AllProjectReqVo();
        BeanUtils.copyProperties(queryVo, reqVo);
        List<AllProjectResVo> projects = supervisionMapper.listProject(reqVo);
        List<String> list = projects.stream().map(AllProjectResVo::getProjectCode).collect(Collectors.toList());
        if (!list.isEmpty()) {
            queryVo.setProjectCodes(list);
        }
        PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
        List<WarningConfig> warningConfigs = warningConfigMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(warningConfigs);
    }

    @Override
    public boolean addWarningConfig(WarningConfigAddVo warningConfigAddVo) {
        WarningConfig warningConfig = new WarningConfig();
        BeanUtils.copyProperties(warningConfigAddVo, warningConfig);
        warningConfig.setId(String.valueOf(snowFlake.nextId()));
        if (warningConfigMapper.insert(warningConfig) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateWarningConfig(WarningConfigAddVo warningConfigAddVo) {
        WarningConfig warningConfig = new WarningConfig();
        BeanUtils.copyProperties(warningConfigAddVo, warningConfig);
        if (warningConfigMapper.update(warningConfig) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean batchDeleteWarningConfig(String ids) {
        if (warningConfigMapper.batchDelete(Arrays.asList(ids.split(","))) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PageVO<WarningRecordResVo> selectWarningRecord(WarningRecordPageQueryVo queryVo) {
        AllProjectReqVo reqVo = new AllProjectReqVo();
        BeanUtils.copyProperties(queryVo, reqVo);
        List<AllProjectResVo> projects = supervisionMapper.listProject(reqVo);
        List<String> list = projects.stream().map(AllProjectResVo::getProjectCode).collect(Collectors.toList());
        if (!list.isEmpty()) {
            queryVo.setProjectCodes(list);
        }
        if (!queryVo.isFetchAll()) {
            PageHelper.startPage(queryVo.getPage(), queryVo.getRows());
        }
        List<WarningRecordResVo> warningConfigList = warningRecordMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(warningConfigList);
    }

    @Override
    public List<Map<String, Object>> countByProjectType() {
        return cameraRecordMapper.countCamerasByProjectType();
    }

    private JSONObject statistics(List<Map<String, Object>> maps) {
        JSONObject result = new JSONObject();
//        result.put("byProjectType",countByProjectType());

        List<Map<String, Object>> byProjectType = new ArrayList<>();
        List<String> projectTypes = projectMapper.selectProjectType();
        for (String projectType : projectTypes) {
            Map<String, Object> map = new HashMap<>();
            map.put("projectType", projectType);
            map.put("num", maps.stream().filter(m -> null != m.get("project_type"))
                    .filter(m -> m.get("project_type").equals(projectType)).count());
            byProjectType.add(map);
        }
        result.put("byProjectType", byProjectType);

        List<DictResVo> list = engineeringSupervisionService.listBas();
        List<Map<String, Object>> byBasin = new ArrayList<>();
        for (DictResVo dictResVo : list) {
            String bas = dictResVo.getDictValue();
            Map<String, Object> map = new HashMap<>();
            map.put("bas", bas);
            map.put("num", maps.stream().filter(m -> null != m.get("bas"))
                    .filter(m -> m.get("bas").equals(bas)).count());
            byBasin.add(map);
        }
        result.put("byBasin", byBasin);

        QueryWrapper<AttAdcdBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AttAdcdBase::getParentAdcd, "330300000000").or().eq(AttAdcdBase::getAdcd, "330300000000");
        //行政区划
        List<AttAdcdBase> attAdcdBases = attAdcdBaseMapper.selectList(queryWrapper);
        List<Map<String, Object>> byAdcd = new ArrayList<>();
        for (AttAdcdBase attAdcdBase : attAdcdBases) {
            String adcd = attAdcdBase.getAdcd();
            Map<String, Object> map = new HashMap<>();
            map.put("areaName", attAdcdBase.getAdnm());
            map.put("num", maps.stream().filter(m -> null != m.get("adcd"))
                    .filter(m -> m.get("adcd").equals(adcd)).count());
            byAdcd.add(map);
        }
        result.put("byAdcd", byAdcd);
        return result;
    }

    @Override
    public JSONObject cameraStatistics() {
        List<Map<String, Object>> maps = cameraRecordMapper.selectCameras();
        return statistics(maps);
    }

    @Override
    public JSONObject cameraOutLineStatistics() {
        List<Map<String, Object>> maps = cameraRecordMapper.selectOutLineCameras();
        return statistics(maps);
    }

    @Override
    public List<OutLineCameraResVo> outLineCameras() {
        return cameraRecordMapper.outLineCameras();
    }

    @Override
    public List<OutLineCameraResVo> outLineCamerasOrderList() {
        return cameraRecordMapper.outLineCameraOrderList();
    }

    @Override
    public JSONArray outLineCamerasCountByBasin() {
        JSONArray result = new JSONArray();
        List<DictResVo> list = engineeringSupervisionService.listBas();
        List<Map<String, Object>> outLineMap = cameraRecordMapper.selectOutLineCameras();
        List<Map<String, Object>> totalMap = cameraRecordMapper.selectCameras();
        for (DictResVo dictResVo : list) {
            String bas = dictResVo.getDictValue();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bas", bas);
            long count = totalMap.stream().filter(m -> m.get("bas") != null && bas.equals(m.get("bas"))).count();
            if (count == 0l) {
                jsonObject.put("rate", 0l);
            } else {
                long count1 = outLineMap.stream().filter(m -> m.get("bas") != null && bas.equals(m.get("bas"))).count();
                jsonObject.put("rate", (double) count1 / count);
            }
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JSONArray outLineCamerasCountByAdcd() {
        JSONArray result = new JSONArray();
        QueryWrapper<AttAdcdBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AttAdcdBase::getParentAdcd, "330300000000").or().eq(AttAdcdBase::getAdcd, "330300000000");
        //行政区划
        List<AttAdcdBase> attAdcdBases = attAdcdBaseMapper.selectList(queryWrapper);
        List<Map<String, Object>> outLineMap = cameraRecordMapper.selectOutLineCameras();
        List<Map<String, Object>> totalMap = cameraRecordMapper.selectCameras();
        for (AttAdcdBase attAdcdBase : attAdcdBases) {
            String areaName = attAdcdBase.getAdnm();
            String areaCode = attAdcdBase.getAdcd();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            long count = totalMap.stream().filter(m -> m.get("adcd") != null && areaCode.equals(m.get("adcd"))).count();
            if (count == 0l) {
                jsonObject.put("rate", 0l);
            } else {
                long count1 = outLineMap.stream().filter(m -> m.get("adcd") != null && areaCode.equals(m.get("adcd"))).count();
                jsonObject.put("rate", (double) count1 / count);
            }
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JSONArray outLineCamerasCountByProjectType() {
        JSONArray result = new JSONArray();
        List<String> projectTypes = projectMapper.selectProjectType();
        List<Map<String, Object>> outLineMap = cameraRecordMapper.selectOutLineCameras();
        List<Map<String, Object>> totalMap = cameraRecordMapper.selectCameras();
        for (String projectType : projectTypes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("projectType", projectType);
            long count = totalMap.stream().filter(m -> m.get("project_type") != null && projectType.equals(m.get("project_type"))).count();
            if (count == 0l) {
                jsonObject.put("rate", 0l);
            } else {
                long count1 = outLineMap.stream().filter(m -> m.get("project_type") != null && projectType.equals(m.get("project_type"))).count();
                jsonObject.put("rate", (double) count1 / count);
            }
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JSONObject cameraStatisticByStatus() {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> list = cameraRecordMapper.countByStatus();
        int total = 0;
        for (Map<String, Object> map : list) {
            long num = (long) map.get("num");
            int status = (int) map.get("status");
            String statusCh;
            if (0 == status) {
                statusCh = "离线";
            } else {
                statusCh = "在线";
            }
            result.put(statusCh, num);
            total += num;
        }
        result.put("全部", total);
        return result;
    }

    @Override
    public PageVO<RainWarningConfig> warningConfigQuery(WarningConfigQueryVo vo) {

        PageHelper.startPage(vo.getPage(), vo.getRows());

        // List<RainWarningConfigResp> resps = new ArrayList<>();

        // 报警类型(1降雨，2水位，3流量，4蓄水量)
        // warning_threshold 报警阈值信息
        String warnType = StringUtils.isNotBlank(vo.getWarnType()) ? vo.getWarnType() : "1";
        if ("1".equals(warnType)) {
            vo.setWarningType("雨");
        }

        if ("2".equals(warnType)) {
            // 站点类型、阈值类型
            vo.setWarningType("水位");
        }

        if ("3".equals(warnType)) {
            vo.setWarningType("流量");
        }

        if ("4".equals(warnType)) {
            vo.setWarningType("蓄水");
        }

        // 获取报警配置信息
        List<RainWarningConfig> rainWarnConfigs = warningConfigMapper.getRainWarnConfigs(vo);
        // List<RainWarningConfig> collect = rainWarnConfigs.stream().filter(con -> StringUtils.isNotBlank(con.getStCode())).collect(Collectors.toList());
        /*if (!CollectionUtils.isEmpty(rainWarnConfigs)) {
            // 获取所有配置id
            List<String> configIds = rainWarnConfigs.stream().map(RainWarningConfig::getId).distinct().collect(Collectors.toList());

            Map<String, List<RainWarningConfig>> collect = rainWarnConfigs.stream().collect(Collectors.groupingBy(rwc -> rwc.getId()));
            // 获取所有接收人信息
            List<WarnUserInfo> warnUserInfos = warningConfigMapper.getWarnUserInfo(configIds);
            //if (!CollectionUtils.isEmpty(warnUserInfos)) {

            collect.forEach((k, v) -> {
                RainWarningConfigResp rainWarningConfigResp = new RainWarningConfigResp();
                List<WarnUserInfo> userInfos = warnUserInfos.stream().filter(warnUserInfo -> k.equals(warnUserInfo.getConfigId())).collect(Collectors.toList());
                rainWarningConfigResp.setRainWarningConfigs(v);
                rainWarningConfigResp.setUserInfos(userInfos);
                resps.add(rainWarningConfigResp);
            });

            //}
        }*/

        //PageVO<RainWarningConfigResp> pageVO = PageUtils.getPageVO(resps);

        return PageUtils.getPageVO(rainWarnConfigs);
    }

    @Override
    public List<WarnConfigStaInfo> staInfos(StaInfoQueryVo vo, String stCodes) {

        List<WarnConfigStaInfo> staInfos = warningConfigMapper.getStaInfos(vo);
        if (StringUtils.isNotBlank(stCodes)) {
            staInfos.forEach(e -> {
                if (stCodes.contains(e.getStCode())) {
                    e.setSort(999);
                } else {
                    e.setSort(0);
                }
            });
            staInfos.stream().sorted(Comparator.comparing(WarnConfigStaInfo::getSort));
        }
        return staInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String warnThresholdOperate(WarnThresholdOperateVo vo) {

        String configId = String.valueOf(snowFlake.nextId());

        try {

            List<String> stCodes = new ArrayList<>();
            // 获取所有已选中的测站编码
            stCodes.addAll(Arrays.asList(vo.getStCodes().split(",")));

            // 查询已有值的阈值信息
            // warningConfigMapper.queryExistWarnT(stCodes, vo.getRainfallPeriod());

            // 判断当前测站在当前降雨历时是否有值，有值更新，无值插入
            List<WarningThreshold> wts = warningConfigMapper.queryExistWarnT(stCodes, vo.getRainfallPeriod());

            if (!CollectionUtils.isEmpty(wts)) {
                // 更新操作
                int i = warningConfigMapper.udpWarnThreshold(wts, vo.getThreshold(), configId);

                // 对比获取不存在的测站编码，进行插入操作
                List<String> existStCodes = wts.stream().map(wt -> wt.getStCode()).collect(Collectors.toList());
                stCodes.removeAll(existStCodes);
            }
            if (!CollectionUtils.isEmpty(stCodes)) {
                List<WarningThreshold> wtList = new ArrayList<>();
                for (String stCode : stCodes) {

                    WarningThreshold wt = new WarningThreshold();
                    wt.setId(String.valueOf(snowFlake.nextId()));
                    wt.setConfigId(configId);
                    wt.setStCode(stCode);
                    wt.setRainfallPeriod(vo.getRainfallPeriod());
                    wt.setThreshold(vo.getThreshold());

                    wtList.add(wt);
                }
                int i = warningConfigMapper.addWarnThreshold(wtList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonCode.FAILED);
        }

        return configId;
    }

    @Override
    public List<DeptUserInfo> userInfos() {

        // 企业+水利局名称信息
        //List<DeptUserInfo> duis = userMapper.getDeptInfos();

        // 获取企业信息
        List<DeptUserInfo> depts = userMapper.getDepts();

        // 获取人员信息
        List<SysUserSimpleResVo> users = userMapper.getUsers();

        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        Map<String, List<SysUserSimpleResVo>> deptMap = users.stream().filter(user -> StringUtils.isNotBlank(user.getDeptId()))
                .collect(Collectors.groupingBy(user -> user.getDeptId()));
        for (DeptUserInfo dept : depts) {
            String deptId = dept.getId();
            if (deptMap.containsKey(deptId)) {
                dept.setUserInfos(deptMap.get(deptId));
            }
        }
        return depts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String warningConfigAdd(WarningConfigAddReqVo vo) {

        try {
            // 报警配置id
            // vo.setId(String.valueOf(snowFlake.nextId()));

            // 保存用户信息(warn_user_info)
            if (StringUtils.isNotBlank(vo.getUserIds())) {
                List<String> userIds = Arrays.asList(vo.getUserIds().split(","));
                // 根据用户id获取用户相关信息
                List<SysUser> users = userMapper.getUserByIds(userIds);
                if (!CollectionUtils.isEmpty(users)) {
                    List<WarnUserInfo> warnUserInfos = new ArrayList<>();
                    for (SysUser user : users) {
                        WarnUserInfo warnUserInfo = new WarnUserInfo();
                        warnUserInfo.setId(String.valueOf(snowFlake.nextId()));
                        warnUserInfo.setConfigId(vo.getId());
                        warnUserInfo.setUserId(user.getId());
                        warnUserInfo.setUserName(user.getUsername());
                        warnUserInfo.setPhone(user.getPhone());
                        warnUserInfo.setDeptName(user.getDeptName());
                        warnUserInfos.add(warnUserInfo);
                    }
                    int count = warningConfigMapper.saveWarnUsers(warnUserInfos);
                }

            }

            // 保存配置信息
            int count = warningConfigMapper.saveWarningConfig(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonCode.FAILED);
        }

        return "save success";
    }

    @Override
    public List<WaterStaInfoRespVo> waterStaInfos(StaInfoQueryVo vo) {

        List<WaterStaInfoRespVo> waterStaInfos = warningConfigMapper.getWaterStaInfos(vo);
        return waterStaInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String waterWarnThresholdOperate(WaterWarnThresholdOperateVo vo) {
        String configId = String.valueOf(snowFlake.nextId());

        try {

            List<String> stCodes = new ArrayList<>();
            // 获取所有已选中的测站编码
            stCodes.addAll(Arrays.asList(vo.getStCodes().split(",")));

            // 查询已有值的阈值信息
            // 判断当前测站在当前测站类型下是否有值，有值更新，无值插入
            List<WarningThreshold> wts = warningConfigMapper.queryExistWaterWarnT(stCodes, vo.getStType());

            if (!CollectionUtils.isEmpty(wts)) {
                // 更新操作
                int i = warningConfigMapper.udpWaterWarnThreshold(wts, vo.getThresholdType(), configId);

                // 对比获取不存在的测站编码，进行插入操作
                List<String> existStCodes = wts.stream().map(wt -> wt.getStCode()).collect(Collectors.toList());
                stCodes.removeAll(existStCodes);
            }
            if (!CollectionUtils.isEmpty(stCodes)) {
                List<WarningThreshold> wtList = new ArrayList<>();
                for (String stCode : stCodes) {

                    WarningThreshold wt = new WarningThreshold();
                    wt.setId(String.valueOf(snowFlake.nextId()));
                    wt.setConfigId(configId);
                    wt.setStCode(stCode);
                    wt.setStType(vo.getStType());
                    wt.setThresholdType(vo.getThresholdType());

                    wtList.add(wt);
                }
                int i = warningConfigMapper.addWaterWarnThreshold(wtList);
            }
        } catch (Exception e) {
            throw new CustomException(CommonCode.FAILED);
        }

        return configId;
    }

    @Override
    public List<FlowStaInfoRespVo> flowStaInfos(StaInfoQueryVo vo) {

        List<FlowStaInfoRespVo> flowStaInfos = warningConfigMapper.getFlowStaInfos(vo);
        return flowStaInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String flowWarnThresholdOperate(FlowWarnThresholdOperateVo vo) {
        String configId = String.valueOf(snowFlake.nextId());

        try {

            List<String> stCodes = new ArrayList<>();
            // 获取所有已选中的测站编码
            stCodes.addAll(Arrays.asList(vo.getStCodes().split(",")));

            // 查询已有值的阈值信息
            // 判断当前测站在当前测站类型下是否有值，有值更新，无值插入
            List<WarningThreshold> wts = warningConfigMapper.queryExistFlowWarnT(stCodes, vo.getWarnCondition());

            if (!CollectionUtils.isEmpty(wts)) {
                // 更新操作
                int i = warningConfigMapper.udpFlowWarnThreshold(wts, vo.getFlowThreshold(), configId);

                // 对比获取不存在的测站编码，进行插入操作
                List<String> existStCodes = wts.stream().map(wt -> wt.getStCode()).collect(Collectors.toList());
                stCodes.removeAll(existStCodes);
            }
            if (!CollectionUtils.isEmpty(stCodes)) {
                List<WarningThreshold> wtList = new ArrayList<>();
                for (String stCode : stCodes) {

                    WarningThreshold wt = new WarningThreshold();
                    wt.setId(String.valueOf(snowFlake.nextId()));
                    wt.setConfigId(configId);
                    wt.setStCode(stCode);
                    wt.setWarnCondition(vo.getWarnCondition());
                    wt.setFlowThreshold(vo.getFlowThreshold());

                    wtList.add(wt);
                }
                int i = warningConfigMapper.addFlowWarnThreshold(wtList);
            }
        } catch (Exception e) {
            throw new CustomException(CommonCode.FAILED);
        }

        return configId;
    }

    @Override
    public List<CapacityStaInfoRespVo> capacityStaInfos(StaInfoQueryVo vo) {

        List<CapacityStaInfoRespVo> capacityStaInfos = warningConfigMapper.getCapacityStaInfos(vo);
        return capacityStaInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String capacityWarnThresholdOperate(CapacityWarnThresholdOperateVo vo) {
        String configId = String.valueOf(snowFlake.nextId());

        try {

            List<String> stCodes = new ArrayList<>();
            // 获取所有已选中的测站编码
            stCodes.addAll(Arrays.asList(vo.getStCodes().split(",")));

            // 查询已有值的阈值信息
            // 判断当前测站在当前测站类型下是否有值，有值更新，无值插入
            List<WarningThreshold> wts = warningConfigMapper.queryExistCapacityWarnT(stCodes);

            if (!CollectionUtils.isEmpty(wts)) {
                // 更新操作
                int i = warningConfigMapper.udpCapacityWarnThreshold(wts, vo.getAvgCapacity(), configId);

                // 对比获取不存在的测站编码，进行插入操作
                List<String> existStCodes = wts.stream().map(wt -> wt.getStCode()).collect(Collectors.toList());
                stCodes.removeAll(existStCodes);
            }
            if (!CollectionUtils.isEmpty(stCodes)) {
                List<WarningThreshold> wtList = new ArrayList<>();
                for (String stCode : stCodes) {

                    WarningThreshold wt = new WarningThreshold();
                    wt.setId(String.valueOf(snowFlake.nextId()));
                    wt.setConfigId(configId);
                    wt.setStCode(stCode);
                    wt.setAvgCapacity(vo.getAvgCapacity());

                    wtList.add(wt);
                }
                int i = warningConfigMapper.addCapacityWarnThreshold(wtList);
            }
        } catch (Exception e) {
            throw new CustomException(CommonCode.FAILED);
        }

        return configId;
    }

    @Override
    public int warningConfigDel(String configIds, String stCodes) {

        List<String> cids = Arrays.asList(configIds.split(","));

        List<String> scs = Arrays.asList(stCodes.split(","));
        int count = warningConfigMapper.delWarnSta(cids, scs);
        return count;
    }

    @Override
    public int warningConfigUpd(WarnConfigUdpReqVo vo) {

        int count = warningConfigMapper.updWarnThreshold(vo);
        return 0;
    }

    @Override
    public PageVO<WarnAnalyseRespVo> warnAnalysisQuery(WarningAnalyseQueryVo vo) {

        if (!vo.isFetchAll()) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }

        // 报警类型(1降雨，2水位，3流量，4蓄水量)
        // warning_threshold 报警阈值信息
        String warnType = StringUtils.isNotBlank(vo.getWarnType()) ? vo.getWarnType() : "1";
        if ("1".equals(warnType)) {
            vo.setWarningType("雨");
        }
        if ("2".equals(warnType)) {
            // 站点类型、阈值类型
            vo.setWarningType("水位");
        }
        if ("3".equals(warnType)) {
            vo.setWarningType("流量");
        }
        if ("4".equals(warnType)) {
            vo.setWarningType("蓄水");
        }

        // 获取报警配置信息
        List<WarnAnalyseRespVo> rainWarnConfigs = warningConfigMapper.getWarnAnalyes(vo);

        //PageVO<RainWarningConfigResp> pageVO = PageUtils.getPageVO(resps);

        return PageUtils.getPageVO(rainWarnConfigs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int forwordWarnAnalyse(ForwordWarnAnalyseReqVo vo) {

        vo.setId(String.valueOf(snowFlake.nextId()));

        // 保存转发就收人信息
        String userIds = vo.getUserIds();
        if (StringUtils.isEmpty(userIds)) {
            throw new CustomException(CommonCode.VALIDATE_FAILED);
        }

        List<WarningForwordUser> forwordUsers = new ArrayList<>();
        List<String> us = Arrays.asList(userIds.split(","));
        for (String u : us) {
            WarningForwordUser forwordUser = new WarningForwordUser();
            forwordUser.setId(String.valueOf(snowFlake.nextId()));
            forwordUser.setUserId(u);
            forwordUser.setForwordId(vo.getId());
            forwordUsers.add(forwordUser);
        }
        int i = warningConfigMapper.saveWarnForwordUsers(forwordUsers);

        // 修改转发状态
        int ui = warningConfigMapper.updateWarnForwordStat(vo.getRecordId());

        // 保存转发记录
        int count = warningConfigMapper.saveWarnForword(vo);

        return count;
    }

    @Override
    public List<AttProjBuildContent> proBuildContents(String projCode, String type) {

        List<AttProjBuildContent> projBuildContents = attProjBuildContentMapper.getProBuildContentsByProC(projCode, type);
        return projBuildContents;
    }

    @Override
    public List<AttProjLink> proLinks(String projCode) {

        List<AttProjLink> projLinks = attProjLinkMapper.getProjLinksByProC(projCode);
        return projLinks;
    }

    @Override
    public List<ProjectResVo> projectOverview() {
        return projectMapper.selectAllProject();
    }

    @Override
    public ProjectResVo projectInfoByCode(String projCode) {
//        BufferedImage image;
        return projectMapper.selectProjectByProCode(projCode);

    }

    @Override
    public void getQrCodeByProjCode(String httpUrl, HttpServletResponse response) throws IOException {
        BufferedImage image;
        ServletOutputStream os = response.getOutputStream();
        try {
            image = QrcodeUtils.createImage(httpUrl, null, false);
            ImageIO.write(image, "JPG", os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("二维码生成异常：" + e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new RuntimeException("二维码生成失败");
            }
        }
    }

    @Override
    public List<ChartResVo<String, Long>> countProjTypeNumByAdcdBasin(String adcd, String basin) {
        List<ChartResVo<String, Long>> records = this.projectMapper.countProjTypeNumByAdcdBasin(adcd, basin, null);
        records = records == null ? Collections.emptyList() : records;
        Map<String, Long> countMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
        List<String> projectTypes = this.projectMapper.selectProjectType();
        projectTypes = projectTypes == null ? Collections.emptyList() : projectTypes;
        List<ChartResVo<String, Long>> result = new ArrayList<>(projectTypes.size());
        for (String projectType : projectTypes) {
            result.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, projectType)
                    .with(ChartResVo::setValue, countMap.getOrDefault(projectType, 0L))
                    .build());
        }
        return result;
    }

    @Override
    public Map<String, List<ProjectResVo>> projectInScope(List<PointEntity> list) {
        List<ProjectResVo> projects = projectOverview();
        projects = projects.stream()
                .filter(project -> !(project.getProjectType().equals("其他") || project.getProjectType().equals("其他")))
                .filter(project -> project.getLongitude() != null && project.getLatitude() != null)
                .filter(project -> GeomUtil.inScope(list, project.getLongitude().doubleValue(), project.getLatitude().doubleValue())).collect(Collectors.toList());
        return projects.stream().collect(Collectors.groupingBy(ProjectResVo::getProjectType));
    }

    @Override
    public Map<String, List<ProjectResVo>> projectInCircle(CircleReqVo reqVo) {
        if (reqVo != null
                && reqVo.getCircleCenter() != null
                && reqVo.getCircleCenter().getLongitude() != null
                && reqVo.getCircleCenter().getLatitude() != null
                && reqVo.getRadius() != null
        ) {
            List<ProjectResVo> projects = projectOverview();
            projects = projects.stream()
                    .filter(project -> !(project.getProjectType().equals("其他") || project.getProjectType().equals("其他")))
                    .filter(project -> project.getLongitude() != null && project.getLatitude() != null)
                    .filter(project -> GeomUtil.isInCircle(reqVo.getRadius(), reqVo.getCircleCenter().getLatitude(), reqVo.getCircleCenter().getLongitude(), project.getLatitude(), project.getLongitude())).collect(Collectors.toList());
            return projects.stream().collect(Collectors.groupingBy(ProjectResVo::getProjectType));
        } else {
            return Collections.emptyMap();
        }

    }

    @Override
    public Map<String, List<Object>> dikeAndSeawallLengthCount() {
        List<AttDikeBase> attDikeBases = projectMapper.selectDikeByCondition(null);
        List<AttSeawallBase> attSeawallBases = projectMapper.selectSeawallByCondition(null);
        List<Map<String, String>> maps = projectMapper.dikeAndSeawallLengthCount();
        Map<String, List<Object>> map = MapUtil.newHashMap();
        //置入提防信息列表
        map.put("attDikeBases", Collections.singletonList(attDikeBases));
        //置入海塘信息列表
        map.put("attSeawallBases", Collections.singletonList(attSeawallBases));
        //置入提防海塘总长度
        map.put("lengthCount", Collections.singletonList(maps));
        return map;
    }

    @Override
    public Collection projectCount(String type, String stType) {
        switch (type) {
            case "行政区划":
                return countProjectByAdcd(stType);
            case "流域":
                return projectMapper.countProjectTypeByBasin(stType).stream().sorted(Comparator.comparing(o -> ((ChartResVo<String, Long>) o).getValue()).reversed()).collect(Collectors.toList());
            case "工程类型":
                return projectMapper.countProjTypeNumByAdcdBasin(null, null, stType);
        }
        return null;
    }

    private List<JSONObject> countProjectByAdcd(String stType) {
//        JSONArray array = new JSONArray();
        List<JSONObject> array = new ArrayList<>();
        Map<String, String> map = attAdcdBaseMapper.selectWZCounty().stream()
                .collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        List<ChartResVo<String, Long>> list = projectMapper.countProjectTypeByAdcd(stType);
        Map<String, List<ChartResVo<String, Long>>> collect = list.stream().collect(Collectors.groupingBy(ChartResVo::getKey));
        map.forEach((areaCode, areaName) -> {
            JSONObject object = new JSONObject();
            object.put("areaName", areaName);
            object.put("areaCode", areaCode);
            object.put("number", collect.containsKey(areaCode) ? collect.get(areaCode).get(0).getValue() : "0");
            array.add(object);
        });
        return array.stream().sorted((o1, o2) -> {
            Integer v1 = o1.getInteger("number");
            Integer v2 = o2.getInteger("number");
            if (v1 != null && v2 != null) {
                return -v1.compareTo(v2);
            } else if (v1 == null && v2 != null) {
                return 1;
            } else if (v1 != null && v2 == null) {
                return -1;
            } else {
                return 0;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<StCountResVo> attStBaseCountByType(String type, String stType, Boolean isWrp) {
        List<StCountResVo> result = null;
        switch (type) {
            case "行政区域": {
                Map<String, String> adcdMap = selectWzCounty();
                result = new ArrayList<>(adcdMap.size());
                List<StCountResVo> list = projectMapper.attStBaseCountByAreaCode(stType, isWrp);
                list = list == null ? Collections.emptyList() : list;
                Map<String, Long> valueMap = list.stream().collect(Collectors.toMap(StCountResVo::getCountKey, StCountResVo::getCountNum));
                List<StCountResVo> finalResult = result;
                adcdMap.forEach((adcd, adnm) -> {
                    finalResult.add(Builder.of(StCountResVo::new)
                            .with(StCountResVo::setCountKey, adnm)
                            .with(StCountResVo::setCountNum, valueMap.getOrDefault(adnm, 0L))
                            .build());
                });
                result = finalResult.stream().sorted(Comparator.comparing(StCountResVo::getCountNum).reversed()).collect(Collectors.toList());
            }
            break;
            case "流域": {
                String[] basArray = {"瓯江", "飞云江", "鳌江", "其他"};
                result = new ArrayList<>(basArray.length);
                List<StCountResVo> list = projectMapper.attStBaseCountByCondition(stType, isWrp);
                list = list == null ? Collections.emptyList() : list;
                Map<String, Long> valueMap = list.stream().collect(Collectors.toMap(StCountResVo::getCountKey, StCountResVo::getCountNum));
                for (String bas : basArray) {
                    result.add(Builder.of(StCountResVo::new)
                            .with(StCountResVo::setCountKey, bas)
                            .with(StCountResVo::setCountNum, valueMap.getOrDefault(bas, 0L))
                            .build());
                }
                result = result.stream().sorted(Comparator.comparing(StCountResVo::getCountNum).reversed()).collect(Collectors.toList());
            }
            break;
            case "工程类型":
                List<String> projCodeList = projectMapper.selectProjCode(stType);
                result = projectMapper.attStBaseCountByProjType(projCodeList);
                break;
            case "站点类型":
                result = projectMapper.attStBaseCountByStType(stType);
                break;
            default:
                break;
        }
        return result;
    }

    private Map<String, String> selectWzCounty() {
        List<AttAdcdBase> records = this.attAdcdBaseMapper.selectWZCounty();
        if (org.springframework.util.CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    /**
     * 工程测站统计
     * 水库：无数据、不需要、正常、接近汛限、超汛限、超防洪高；
     * 河网/堰闸：无数据、正常、接近警戒、超警戒、超保证；
     * 潮位：无数据、正常、接近警戒、超警戒；
     *
     * @param type 水库/河网/堰闸/潮位
     * @return
     */
    @Override
    public Object projStatStatistic(String type, String dimension) {
        if (StringUtils.isBlank(type)) {
            return Collections.emptyList();
        }
        if ("水库".equals(type)) {
            return rsvrStatistic(dimension);
        } else {
            return stationStatistic(type, dimension);
        }
    }

    DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");


    private Object rsvrStatistic(String dimension) {

        List<RsvrStationWaterResVo> allRsvrs = rsvrService.rsvrWaterLevel(new RsvrStationWaterReqVo()).getList();
        String projCode = allRsvrs.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getResCode()))
                .map(AttResBase::getResCode)
                .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));

//        // 根据工程编码查询所有的测站
        List<AttStBase> stationList = this.stationMapper.selectStationByProjCode(projCode, StationType.RESERVOIR.type());
        stationList = stationList == null ? Collections.emptyList() : stationList;
        if ("类型".equals(dimension)) {
            Map<String, Object> result = new HashMap<>();
            // 将水库按大中型、小型、是否超汛限分组
            Map<String, List<RsvrStationWaterResVo>> rsvrMap = allRsvrs.stream().collect(Collectors.groupingBy(o -> {
                int engScal = 0;
                try {
                    engScal = Integer.parseInt(o.getEngScal());
                } catch (Exception e) {
                    return StringUtils.EMPTY;
                }
                boolean normal = !(o.getOverLimitVal() != null && o.getOverLimitVal() > 0);
                if (engScal < 4) {
                    return normal ? "lmNormal" : "lmOver";
                } else {
                    return normal ? "smNormal" : "smOver";
                }
            }));
            result.put("lmNormalNum", rsvrMap.getOrDefault("lmNormal", Collections.emptyList()).size());
            result.put("lmNormalRemark", rsvrMap.getOrDefault("lmNormal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("lmOverNum", rsvrMap.getOrDefault("lmOver", Collections.emptyList()).size());
            result.put("lmOverRemark", rsvrMap.getOrDefault("lmOver", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("smNormalNum", rsvrMap.getOrDefault("smNormal", Collections.emptyList()).size());
            result.put("smNormalRemark", rsvrMap.getOrDefault("smNormal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("smOverNum", rsvrMap.getOrDefault("smOver", Collections.emptyList()).size());
            result.put("smOverRemark", rsvrMap.getOrDefault("smOver", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("rsvrNumNum", allRsvrs.size());
            result.put("rsvrNumRemark", allRsvrs.stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("stationNum", stationList.size());
            result.put("stationRemark", stationList.stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));
            return result;
        } else if ("区域".equals(dimension)) {
            List<AttAdcdBase> adcdBases = this.attAdcdBaseMapper.selectWZCounty();
            adcdBases = adcdBases == null ? Collections.emptyList() : adcdBases;
            List<ChartResVo<String, Map<String, Object>>> result = new ArrayList<>(adcdBases.size());
            String areaCodeEnd = "000000";
            // 将水库按行政区划分组
            Map<String, List<RsvrStationWaterResVo>> rsvrGroupMap = allRsvrs.stream().collect(Collectors.groupingBy(o -> {
                if (StringUtils.isNotBlank(o.getAreaCode()) && o.getAreaCode().length() > 6) {
                    return o.getAreaCode().substring(0, 6) + areaCodeEnd;
                } else {
                    return StringUtils.EMPTY;
                }
            }));
            for (AttAdcdBase adcdBase : adcdBases) {
                ChartResVo<String, Map<String, Object>> tempRes = new ChartResVo<>(adcdBase.getAdnm(), null);
                // 拿到当前行政区划下面的所有水库
                List<RsvrStationWaterResVo> groupRsvrList = rsvrGroupMap.get(adcdBase.getAdcd());
                groupRsvrList = groupRsvrList == null ? Collections.emptyList() : groupRsvrList;

                // 将水库按是否超汛限分组
                Map<String, List<RsvrStationWaterResVo>> rsvrTempMap = groupRsvrList.stream().collect(Collectors.groupingBy(o -> {
                    if (!(o.getOverLimitVal() != null && o.getOverLimitVal() > 0)) {
                        return "normal";
                    } else {
                        return "over";
                    }
                }));
                tempRes.setValue(Builder.of(HashMap<String, Object>::new)

                        .with(HashMap::put, "totalNum", groupRsvrList.size())
                        .with(HashMap::put, "totalRemark", groupRsvrList.stream().map(AttResBase::getResCode).collect(Collectors.joining(",")))

                        .with(HashMap::put, "normalNum", rsvrTempMap.getOrDefault("normal", Collections.emptyList()).size())
                        .with(HashMap::put, "normalRemark", rsvrTempMap.getOrDefault("normal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")))

                        .with(HashMap::put, "overNum", rsvrTempMap.getOrDefault("over", Collections.emptyList()).size())
                        .with(HashMap::put, "overRemark", rsvrTempMap.getOrDefault("over", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")))
                        .build());
                result.add(tempRes);
            }
            return result.stream().sorted(Comparator.comparing(o -> Integer.valueOf(String.valueOf(((ChartResVo<String, Map<String, Object>>) o).getValue().get("totalNum")))).reversed()).collect(Collectors.toList());
        } else if ("流域".equals(dimension)) {
            Map<String, Object> result = new HashMap<>();
            //
            Map<String, List<RsvrStationWaterResVo>> rsvrMap = allRsvrs.stream().collect(Collectors.groupingBy(o -> {
                if (StringUtils.isBlank(o.getBas())) {
                    return StringUtils.EMPTY;
                }
                boolean normal = !(o.getOverLimitVal() != null && o.getOverLimitVal() > 0);
                switch (o.getBas().trim()) {
                    // 前端固定要，不要从数据字典里取
                    case "飞云江":
                        return normal ? "fyjNormal" : "fyjOver";
                    case "鳌江":
                        return normal ? "ajNormal" : "ajOver";
                    case "瓯江":
                        return normal ? "ojNormal" : "ojOver";
                    default:
                        return StringUtils.EMPTY;
                }
            }));
            result.put("fyjNormalNum", rsvrMap.getOrDefault("fyjNormal", Collections.emptyList()).size());
            result.put("fyjNormalRemark", rsvrMap.getOrDefault("fyjNormal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("fyjOverNum", rsvrMap.getOrDefault("fyjOver", Collections.emptyList()).size());
            result.put("fyjOverRemark", rsvrMap.getOrDefault("fyjOver", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("ajNormalNum", rsvrMap.getOrDefault("ajNormal", Collections.emptyList()).size());
            result.put("ajNormalRemark", rsvrMap.getOrDefault("ajNormal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("ajOverNum", rsvrMap.getOrDefault("ajOver", Collections.emptyList()).size());
            result.put("ajOverRemark", rsvrMap.getOrDefault("ajOver", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("ojNormalNum", rsvrMap.getOrDefault("ojNormal", Collections.emptyList()).size());
            result.put("ojNormalRemark", rsvrMap.getOrDefault("ojNormal", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));

            result.put("ojOverNum", rsvrMap.getOrDefault("ojOver", Collections.emptyList()).size());
            result.put("ojOverRemark", rsvrMap.getOrDefault("ojOver", Collections.emptyList()).stream().map(AttResBase::getResCode).collect(Collectors.joining(",")));
            return result;
        } else {
            return null;
        }
    }

    private Object stationStatistic(String type, String dimension) {
//        List<ChartResVo<String, Long>> result = new ArrayList<>(10);
        List<AttStBase> totalStationList = Collections.emptyList();
        Map<String, Optional<Double>> waterLevelMap = Collections.emptyMap();
        switch (type) {
            case "河道": {
                totalStationList = stationMapper.selectStationByType(StationType.RIVER.type());
                totalStationList = totalStationList == null ? Collections.emptyList() : totalStationList;
                List<StRiverR> waterLevels = this.waterAndRainMapper.candidateRiverWaterLevel(StationType.RIVER.table() + LocalDateTime.now().format(yyyyMM), totalStationList.stream().map(AttStBase::getStCode).collect(Collectors.toList()), null);
                waterLevels = waterLevels == null ? Collections.emptyList() : waterLevels;
                waterLevelMap = waterLevels.stream().collect(Collectors.toMap(StRiverR::getMgstcd, o -> Optional.ofNullable(o.getZ())));
            }
            break;
            case "水闸": {
                totalStationList = stationMapper.selectStationByType(StationType.GATE.type());
                totalStationList = totalStationList == null ? Collections.emptyList() : totalStationList;
                List<StWasR> waterLevels = this.waterAndRainMapper.candidateSluiceWaterLevel(StationType.GATE.table() + LocalDateTime.now().format(yyyyMM), totalStationList.stream().map(AttStBase::getStCode).collect(Collectors.toList()));
                waterLevels = waterLevels == null ? Collections.emptyList() : waterLevels;
                waterLevelMap = waterLevels.stream().collect(Collectors.toMap(StWasR::getMgstcd, o -> Optional.ofNullable(o.getUpz())));
            }
            break;
            case "潮位": {
                totalStationList = stationMapper.selectStationByType(StationType.TIDE.type());
                totalStationList = totalStationList == null ? Collections.emptyList() : totalStationList;
                List<StTideR> waterLevels = this.waterAndRainMapper.candidateTide(StationType.TIDE.table() + LocalDateTime.now().format(yyyyMM), totalStationList.stream().map(AttStBase::getStCode).collect(Collectors.toList()));
                waterLevels = waterLevels == null ? Collections.emptyList() : waterLevels;
                waterLevelMap = waterLevels.stream().collect(Collectors.toMap(StTideR::getMgstcd, o -> Optional.ofNullable(o.getTdz())));
            }
            break;
            default:
                break;
        }
        if ("区域".equals(dimension)) {
            List<AttAdcdBase> adcdBases = this.attAdcdBaseMapper.selectWZCounty();
            adcdBases = adcdBases == null ? Collections.emptyList() : adcdBases;
            List<ChartResVo<String, Map<String, Object>>> result = new ArrayList<>(adcdBases.size());
            String areaCodeEnd = "000000";
            Map<String, List<AttStBase>> stationMap = totalStationList.stream().collect(Collectors.groupingBy(o -> {
                if (StringUtils.isNotBlank(o.getAreaCode()) && o.getAreaCode().length() > 6) {
                    return o.getAreaCode().substring(0, 6) + areaCodeEnd;
                } else {
                    return StringUtils.EMPTY;
                }
            }));
            for (AttAdcdBase adcdBase : adcdBases) {
                ChartResVo<String, Map<String, Object>> tempRes = new ChartResVo<>(adcdBase.getAdnm(), null);
                List<AttStBase> groupStationList = stationMap.get(adcdBase.getAdcd());
                groupStationList = groupStationList == null ? Collections.emptyList() : groupStationList;
                Map<String, Optional<Double>> finalWaterLevelMap = waterLevelMap;
                Map<String, List<AttStBase>> stTempMap = groupStationList.stream().collect(Collectors.groupingBy(o -> {
                    if (isStationWaterLevelNormal(o, finalWaterLevelMap)) {
                        return "normal";
                    } else {
                        return "over";
                    }
                }));
                tempRes.setValue(Builder.of(HashMap<String, Object>::new)
                        .with(HashMap::put, "totalNum", groupStationList.size())
                        .with(HashMap::put, "totalRemark", groupStationList.stream().map(AttStBase::getStCode).collect(Collectors.joining(",")))

                        .with(HashMap::put, "normalNum", stTempMap.getOrDefault("normal", Collections.emptyList()).size())
                        .with(HashMap::put, "normalRemark", stTempMap.getOrDefault("normal", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")))

                        .with(HashMap::put, "overNum", stTempMap.getOrDefault("over", Collections.emptyList()).size())
                        .with(HashMap::put, "overRemark", stTempMap.getOrDefault("over", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")))
                        .build());
                result.add(tempRes);
            }
            return result;
        } else if ("流域".equals(dimension)) {
            Map<String, Object> result = new HashMap<>();
            Map<String, Optional<Double>> finalWaterLevelMap = waterLevelMap;
            Map<String, List<AttStBase>> rsvrMap = totalStationList.stream().collect(Collectors.groupingBy(o -> {
                if (StringUtils.isBlank(o.getBas())) {
                    return StringUtils.EMPTY;
                }
                boolean normal = isStationWaterLevelNormal(o, finalWaterLevelMap);
                switch (o.getBas().trim()) {
                    case "飞云江":
                        return normal ? "fyjNormal" : "fyjOver";
                    case "鳌江":
                        return normal ? "ajNormal" : "ajOver";
                    case "瓯江":
                        return normal ? "ojNormal" : "ojOver";
                    default:
                        return StringUtils.EMPTY;
                }
            }));
            result.put("fyjNormalNum", rsvrMap.getOrDefault("fyjNormal", Collections.emptyList()).size());
            result.put("fyjNormalRemark", rsvrMap.getOrDefault("fyjNormal", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));

            result.put("fyjOverNum", rsvrMap.getOrDefault("fyjOver", Collections.emptyList()).size());
            result.put("fyjOverRemark", rsvrMap.getOrDefault("fyjOver", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));

            result.put("ajNormalNum", rsvrMap.getOrDefault("ajNormal", Collections.emptyList()).size());
            result.put("ajNormalRemark", rsvrMap.getOrDefault("ajNormal", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));

            result.put("ajOverNum", rsvrMap.getOrDefault("ajOver", Collections.emptyList()).size());
            result.put("ajOverRemark", rsvrMap.getOrDefault("ajOver", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));

            result.put("ojNormalNum", rsvrMap.getOrDefault("ojNormal", Collections.emptyList()).size());
            result.put("ojNormalRemark", rsvrMap.getOrDefault("ojNormal", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));

            result.put("ojOverNum", rsvrMap.getOrDefault("ojOver", Collections.emptyList()).size());
            result.put("ojOverRemark", rsvrMap.getOrDefault("ojOver", Collections.emptyList()).stream().map(AttStBase::getStCode).collect(Collectors.joining(",")));
            return result;
        } else {
            return null;
        }

    }

    /**
     * @param station
     * @param waterLevelMap
     * @return
     */
    private boolean isStationWaterLevelNormal(AttStBase station, Map<String, Optional<Double>> waterLevelMap) {
        boolean normal = true;
        Optional<Double> tempWaterLevel = null;
        if (com.ygkj.utils.CollectionUtils.isNotBlank(waterLevelMap)
                && station != null
                && station.getWarningLevel() != null
                && (tempWaterLevel = waterLevelMap.get(station.getStCode())) != null) {
            if (tempWaterLevel.isPresent() && tempWaterLevel.get().compareTo(station.getWarningLevel()) >= 0) {
                normal = false;
            }
        }
        return normal;
    }


    /**
     * 查询病险工程
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<DilapProjResVo> dilapProjList(DilapProjReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null && reqVo.getPageNum() > 0 && reqVo.getPageSize() > 0) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        PageVO<DilapProjResVo> projPageVo = PageUtils.getPageVO(this.projectMapper.selectDilapProjByCondition(reqVo));
        List<DilapProjResVo> projList = projPageVo.getList();
        if (CollectionUtils.isEmpty(projList)) {
            return projPageVo;
        }

        // 先填充所属街道和行政区划名称
        List<String> adcdList = new ArrayList<>(projList.size() * 2);
        // 所属乡镇行政区划 编码不为空 ，且所属乡镇 名称为空 的才填充乡镇名称
        adcdList.addAll(projList.stream().filter(o -> StringUtils.isNotBlank(o.getProjLocCode()) && StringUtils.isBlank(o.getProjLocName())).map(DilapProjResVo::getProjLocCode).distinct().collect(Collectors.toList()));
        // 行政区划编码 不为空 的才填充 行政区划名称
        adcdList.addAll(projList.stream().filter(o -> StringUtils.isNotBlank(o.getAdcd())).map(DilapProjResVo::getAdcd).distinct().collect(Collectors.toList()));
        Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
        for (DilapProjResVo temp : projList) {
            if (StringUtils.isNotBlank(temp.getProjLocCode()) && StringUtils.isBlank(temp.getProjLocName())) {
                temp.setProjLocName(adcdMap.get(temp.getProjLocCode()));
            }
            if (StringUtils.isNotBlank(temp.getAdcd())) {
                temp.setAdnm(adcdMap.get(temp.getAdcd()));
            }
            if (StringUtils.isNotBlank(temp.getEngGrad())) {
                temp.setEngGrad(temp.getEngGrad() + grad);
            }
            Optional.ofNullable(EngScalEnum.getByLevel(temp.getEngScal()))
                    .ifPresent(e -> temp.setEngScal(e.getDesc()));
        }
        if (reqVo.isResWaterLevel()) {
            fillRsvrInfo4DilapProjWithWater(projList);
        } else if (reqVo.isResInfo()) {
            fillRsvrInfo4DilapProj(projList);
        }
        if (reqVo.isWagaWaterLevel()) {
            fillWagaInfo4DilapProjWithWater(projList);
        } else if (reqVo.isResInfo()) {
            fillWagaInfo4DilapProj(projList);
        }
        if (reqVo.isSeawallInfo()) {
            fillSeawallInfo4DilapProj(projList);
        }
        if (reqVo.isMnpdInfo()) {
            fillMnpdInfo4DilapProj(projList);
        }
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<DilapProjResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            projList = projList.stream().sorted(comparator).collect(Collectors.toList());
        }
        projPageVo.setList(projList);
        return projPageVo;
    }


    /**
     * @param projList
     */
    public void fillRsvrInfo4DilapProj(List<DilapProjResVo> projList) {
        List<String> projectCodes = projList.stream().filter(o -> "水库".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.toList());
        List<AttResBase> projectList = this.projectMapper.selectReservoirByCondition(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setCodes, projectCodes).build());
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        Map<String, AttResBase> projectMap = projectList.stream().collect(Collectors.toMap(AttResBase::getResCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            AttResBase project = projectMap.get(temp.getProjectCode());
            if (project == null) {
                continue;
            }
            temp.setRsvrInfo(project);
        }
    }

    /**
     * @param projList
     */
    public void fillRsvrInfo4DilapProjWithWater(List<DilapProjResVo> projList) {
        String rsvrCodes = projList.stream().filter(o -> "水库".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
        if (StringUtils.isEmpty(rsvrCodes)) {
            return;
        }
        List<RsvrStationWaterResVo> rsvrList = rsvrService.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new).with(RsvrStationWaterReqVo::setResCodes, rsvrCodes).build()).getList();
        if (CollectionUtils.isEmpty(rsvrList)) {
            return;
        }
        Map<String, RsvrStationWaterResVo> rsvrMap = rsvrList.stream().collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            RsvrStationWaterResVo rsvr = rsvrMap.get(temp.getProjectCode());
            if (rsvr == null) {
                continue;
            }
            temp.setRsvrInfo(rsvr);
            temp.setWaterLevel(rsvr.getWaterLevel());
            temp.setDataTime(rsvr.getDataTime());
            temp.setFloodLevel(rsvr.getUppLevFlco());
            temp.setLimitLevel(rsvr.getCurrentLimitLevel());
            temp.setStCode(rsvr.getStCode());
            temp.setStType(rsvr.getStType());
            temp.setStLat(rsvr.getStLat());
            temp.setStLong(rsvr.getStLong());
            temp.setStName(rsvr.getStName());
        }
    }


    public void fillWagaInfo4DilapProj(List<DilapProjResVo> projList) {
        List<String> projectCodes = projList.stream().filter(o -> "水闸".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(projectCodes)) {
            return;
        }
        List<AttWagaBase> projectList = this.projectMapper.selectWagaByCondition(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setCodes, projectCodes).build());
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        Map<String, AttWagaBase> projectMap = projectList.stream().collect(Collectors.toMap(AttWagaBase::getWagaCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            AttWagaBase project = projectMap.get(temp.getProjectCode());
            if (project == null) {
                continue;
            }
            temp.setWagaInfo(project);
        }
    }

    public void fillWagaInfo4DilapProjWithWater(List<DilapProjResVo> projList) {
        String wagaCodes = projList.stream().filter(o -> "水闸".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
        if (StringUtils.isEmpty(wagaCodes)) {
            return;
        }
        List<WagaStationWaterResVo> wagaList = wagaService.wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new).with(WagaStationWaterReqVo::setWagaCodes, wagaCodes).build()).getList();
        if (CollectionUtils.isEmpty(wagaList)) {
            return;
        }
        Map<String, WagaStationWaterResVo> wagaMap = wagaList.stream().collect(Collectors.toMap(WagaStationWaterResVo::getWagaCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            WagaStationWaterResVo waga = wagaMap.get(temp.getProjectCode());
            if (waga == null) {
                continue;
            }
            temp.setWagaInfo(waga);
        }
    }


    public void fillSeawallInfo4DilapProj(List<DilapProjResVo> projList) {
        List<String> projectCodes = projList.stream().filter(o -> "海塘".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(projectCodes)) {
            return;
        }
        List<AttSeawallBase> projectList = this.projectMapper.selectSeawallByCondition(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setCodes, projectCodes).build());
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        Map<String, AttSeawallBase> projectMap = projectList.stream().collect(Collectors.toMap(AttSeawallBase::getSeawallCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            AttSeawallBase project = projectMap.get(temp.getProjectCode());
            if (project == null) {
                continue;
            }
            temp.setSeawallInfo(project);
        }
    }


    public void fillMnpdInfo4DilapProj(List<DilapProjResVo> projList) {
        List<String> projectCodes = projList.stream().filter(o -> "山塘".equals(o.getProjectType())).map(DilapProjResVo::getProjectCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(projectCodes)) {
            return;
        }
        List<AttMnpdBase> projectList = this.projectMapper.selectMountainPoolByCondition(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setCodes, projectCodes).build());
        if (CollectionUtils.isEmpty(projectList)) {
            return;
        }
        Map<String, AttMnpdBase> projectMap = projectList.stream().collect(Collectors.toMap(AttMnpdBase::getMnpdCode, Function.identity()));
        for (DilapProjResVo temp : projList) {
            AttMnpdBase project = projectMap.get(temp.getProjectCode());
            if (project == null) {
                continue;
            }
            temp.setMnpdInfo(project);
        }
    }


    /**
     * 查询工程风险
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ProjRiskResVo> projRiskList(ProjRiskReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null && reqVo.getPageNum() > 0 && reqVo.getPageSize() > 0) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        if (StringUtils.isEmpty(reqVo.getStartDate())) {
            reqVo.setStartDate(null);
        }
        if (StringUtils.isEmpty(reqVo.getEndDate())) {
            reqVo.setEndDate(null);
        }
        PageVO<ProjRiskResVo> result = PageUtils.getPageVO(this.projectMapper.selectProjRiskByCondition(reqVo));
        List<ProjRiskResVo> projList = result.getList();
        if (CollectionUtils.isEmpty(projList)) {
            return result;
        }
        List<String> adcdList = new ArrayList<>(projList.size());
        // 行政区划编码 不为空 的才填充 行政区划名称
        adcdList.addAll(projList.stream().filter(o -> StringUtils.isNotBlank(o.getAreaCode())).map(ProjRiskResVo::getAreaCode).distinct().collect(Collectors.toList()));
        Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
        for (ProjRiskResVo temp : projList) {
            if (StringUtils.isNotBlank(temp.getAreaCode())) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
            }
        }

        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<ProjRiskResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            projList = projList.stream().sorted(comparator).collect(Collectors.toList());
        }
        result.setList(projList);
        return result;
    }

    /**
     * 大屏海塘统计
     *
     * @return
     */
    @Override
    public JSONObject seawallStatsForBigScreen() {
        JSONObject result = new JSONObject();
        String[] keys = {"lt10Year", "eq10Year", "eq20Year", "eq50Year", "eq100Year", "gt100Year", "notStandard"};
        List<AttSeawallBase> seawallList = this.projectMapper.selectSeawallByCondition(Builder.of(ProjectManageQueryVo::new).build());
        seawallList = seawallList == null ? Collections.emptyList() : seawallList;
        Map<String, List<AttSeawallBase>> map = seawallList.stream().collect(Collectors.groupingBy(o -> {
            if (o.getDesignTideStandard() == null) {
                return keys[6];
            } else if (o.getDesignTideStandard() < 10) {
                return keys[0];
            } else if (o.getDesignTideStandard() > 100) {
                return keys[5];
            } else {
                switch (o.getDesignTideStandard().intValue()) {
                    case 10:
                        return keys[1];
                    case 20:
                        return keys[2];
                    case 50:
                        return keys[3];
                    case 100:
                        return keys[4];
                    default:
                        return StringUtils.EMPTY;
                }
            }
        }));

        int totalNum = seawallList.size();
        double totalLength = seawallList.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
        List<JSONObject> list = new ArrayList<>(keys.length);
        for (String key : keys) {
            List<AttSeawallBase> tempList = map.get(key);
            tempList = tempList == null ? Collections.emptyList() : tempList;
            int partNum = 0;
            double lengthPercent = 0D;
            partNum = tempList.size();
            AtomicDouble partLength = new AtomicDouble(0);
            tempList.forEach(o -> {
                if (o.getSeawallLength() != null) {
                    partLength.addAndGet(o.getSeawallLength().doubleValue());
                }
            });
            lengthPercent = partLength.get() * 100 / totalLength;
            JSONObject temp = new JSONObject();
            temp.put("type", key);
            temp.put("partNum", partNum);
            temp.put("partLength", String.format("%.1f", partLength.get() / 1000));
            temp.put("lengthPercent", String.format("%.1f", lengthPercent));
            temp.put("seawallList", tempList);
            list.add(temp);
        }
        result.put("list", list);
        result.put("totalNum", totalNum);
        result.put("totalLength", String.format("%.1f", totalLength / 1000));
        return result;
    }

    @Override
    public List<Map<String, String>> haha() {
        return projectMapper.haha();
    }


    @Override
    public List<String> getAllProjType() {
        return projectMapper.selectProjectType();
    }

    @Override
    public void saveChooseProject(String dataStr) {
        userFeignClient.putUserChooseProject("userChooseProject_" + JwtTokenUtil.getCurrentUserId().orElse(""), dataStr);
    }

    @Override
    public JSONArray getChooseProject() {
        String str = userFeignClient.getUserChooseProject("userChooseProject_" + JwtTokenUtil.getCurrentUserId().orElse(""));
        if (StringUtils.isNotBlank(str)) {
            JSONArray jsonArray = JSON.parseArray(str);
            return jsonArray;
        }
        return null;
    }

    @Override
    public Map<String, Object> countProjectNum(String type) {
        Map<String, Object> result = new HashMap<>();

        switch (type) {
            case "大中型水库":
            case "小型水库":
            case "水库": {
                //水库总数、病险数量
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.RES.getTableName());
                result.put("total", total);
                Integer dilapProj = projectMapper.countProjectNumByDilapProjAndType(true, ProjectTypeTableEnum.RES.getTableName());
                result.put("dilapProj", dilapProj);
                return result;
            }
            case "泵站": {
                //泵站总数、总装机流量
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.PUST.getTableName());
                result.put("total", total);
                Integer insFlowCount = projectMapper.countInsFlow();
                result.put("insFlow", insFlowCount);
                return result;
            }
            case "堤防": {
                //堤防总数、堤防总长度
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.DIKE.getTableName());
                result.put("total", total);
                Integer dikeLenCount = projectMapper.countDikeLen();
                result.put("dikeLen", dikeLenCount);
                return result;
            }
            case "山塘": {
                //山塘总数、病险数量
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.MNPD.getTableName());
                result.put("total", total);
                Integer dilapProj = projectMapper.countProjectNumByDilapProjAndType(true, ProjectTypeTableEnum.MNPD.getTableName());
                result.put("dilapProj", dilapProj);
                return result;
            }
            case "水闸": {
                //水闸总数、病险数量
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.WAGA.getTableName());
                result.put("total", total);
                Integer dilapProj = projectMapper.countProjectNumByDilapProjAndType(true, ProjectTypeTableEnum.WAGA.getTableName());
                result.put("dilapProj", dilapProj);
                return result;
            }
            case "海塘": {
                //海塘总数、病险数量、海塘总长度
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.SEAWALL.getTableName());
                result.put("total", total);
                Integer dilapProj = projectMapper.countProjectNumByDilapProjAndType(true, ProjectTypeTableEnum.SEAWALL.getTableName());
                result.put("dilapProj", dilapProj);
                Integer seawallLenCount = projectMapper.countSeawallLen();
                result.put("seawallLen", seawallLenCount);
                return result;
            }
            case "水电站": {
                //水电站总数、总装机容量
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.HYST.getTableName());
                result.put("total", total);
                Integer totInsCap = projectMapper.countTotInsCap();
                result.put("totInsCap", totInsCap);
                return result;
            }
            case "闸站": {
                //闸站总数、温瑞平总数
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.GATE.getTableName());
                result.put("total", total);
                return result;
            }
            case "灌区": {
                //灌区总数、有效灌溉总面积
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.IRR.getTableName());
                result.put("total", total);
                Integer effIrrArea = projectMapper.countEffIrrArea();
                result.put("effIrrArea", effIrrArea);
                return result;
            }
            case "避灾点": {
                //避灾点总数、避灾点总人数
                Integer total = projectMapper.countProjectNumByDilapProjAndType(null, ProjectTypeTableEnum.BZD.getTableName());
                result.put("total", total);
                Integer countPerson = projectMapper.countPerson();
                result.put("countPerson", countPerson);
                return result;
            }
            default:

        }
        return result;
    }

    @Override
    public Object getHikvisionTree() {
        //查询鹿城区Adcd
        List<JSONObject> lcAdcd = projectMapper.getLcAdcd();
        //查询鹿城区摄像头数据
        List<JSONObject> hikvision = projectMapper.getHikvision();
        //获取摄像头所属工程编码
        List<String> projectCodes = hikvision.stream().map(o -> o.getString("project_code")).collect(Collectors.toList());
        String join = String.join(",", projectCodes);
        //通过所属工程编码获取工程相关信息
        List<JSONObject> projects = projectMapper.selectProjectByCodes(join);
        Map<String, List<JSONObject>> areaCode = hikvision.stream().collect(Collectors.groupingBy(jsonObject -> jsonObject.getString("area_code")));

        Map<String,List<JSONObject>> map = new HashMap<>();
        List<JSONObject> bind = new ArrayList<>();
        List<JSONObject> projectByadcd = new ArrayList<>();
        for (JSONObject project : projects) {
            String adcd = project.getString("adcd");
            List<JSONObject> list = new ArrayList<>();
            for (JSONObject object : hikvision) {
                if (adcd.equals(object.getString("project_code"))){
                    list.add(object);
                    bind.add(object);
                }
            }
            JSONObject object = new JSONObject();
            project.put("level",5);
            project.put("subset",list);
            if (list.size() > 0) {
                if (map.containsKey(list.get(0).getString("area_code"))){
                    List<JSONObject> list1 = map.get(list.get(0).getString("area_code"));
                    list1.add(project);
                }else{
                    List<JSONObject> list1 = new ArrayList<>();
                    list1.add(project);
                    map.put(list.get(0).getString("area_code"),list1);
                }

            }

        }
        hikvision.removeAll(bind);
        for (JSONObject object : hikvision) {
            String adcd = object.getString("area_code");
            map.forEach((k,v)->{
                if (adcd.equals(k)) {
                    v.add(object);
                }
            });
        }
        for (JSONObject jsonObject : lcAdcd) {
            String adcd = jsonObject.getString("adcd");
            map.forEach((k,v)->{
                if (adcd.equals(k)) {
                    jsonObject.put("subset", v);
                }
            });
        }


        List<JSONObject> collect1 = lcAdcd.stream().filter(jsonObject -> "3".equals(jsonObject.getString("level"))).collect(Collectors.toList());
        List<JSONObject> collect2 = lcAdcd.stream().filter(jsonObject -> jsonObject.get("subset") != null && "4".equals(jsonObject.getString("level"))).collect(Collectors.toList());
        JSONObject jsonObject = collect1.get(0);
        jsonObject.put("subset", collect2);
        return jsonObject;
    }
}
