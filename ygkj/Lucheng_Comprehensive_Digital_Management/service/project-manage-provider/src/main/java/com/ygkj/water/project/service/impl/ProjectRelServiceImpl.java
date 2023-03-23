package com.ygkj.water.project.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.ProjectTypeTableEnum;
import com.ygkj.water.project.feign.BigScreenClient;
import com.ygkj.water.project.mapper.AttSeawallMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.ProjectRelService;
import com.ygkj.water.project.vo.MonitoringStationResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("projectRelService")
public class ProjectRelServiceImpl implements ProjectRelService {

    @Autowired
    private ProjectRelMapper projectRelMapper;

    @Autowired
    private AttSeawallMapper seawallMapper;

    private DateTimeFormatter ym = DateTimeFormatter.ofPattern("yyyyMM");

    @Autowired
    BigScreenClient bigScreenClient;

    /**
     * 关联测站
     *
     * @param vo
     * @return
     */
    @Override
    public List<SlProjStResVo> listSt(ProRelStVo vo) {
        Integer type = vo.getStType();
        switch (type) {
            case 1:
                vo.getStTypeList().add("TT");
//                vo.getStTypeList().add("ZZ");
//                vo.getStTypeList().add("DD");
                break;
            case 2:
                vo.getStTypeList().add("PP");
                break;
            case 3:
                vo.getStTypeList().add("ZZ");
                vo.getStTypeList().add("ZQ");
                break;
            case 4:
                return projectRelMapper.listRelQua(vo.getProjectCode());
            case 5:
                vo.getStTypeList().add("SM");
                break;
            case 6:
                vo.getStTypeList().add("DD");
                break;
            case 7:
                vo.getStTypeList().add("RR");
                vo.getStTypeList().add("R2");
                break;
        }
        return projectRelMapper.listRelSt(vo);
    }

    /**
     * 分页查询巡查列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ProjectPatrolResVo> pagePatrol(ProjectPatrolReqVo reqVo) {
        //首先将工程编码替换成外部的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(1)
                        .localPrcd(reqVo.getProjectCode())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return null;
        }
//        if (reqVo.getStartTime() == null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            reqVo.setStartTime(calendar.getTime());
//        }
        reqVo.setProjectCode(foreignCode);
        reqVo.page();
//        return PageUtils.getPageVO(Collections.emptyList());
        return PageUtils.getPageVO(projectRelMapper.listPatrol(reqVo));
    }

    /**
     * 导出巡查人员
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportPagePatrol(ProjectPatrolReqVo reqVo, HttpServletResponse response) {
        PageVO<ProjectPatrolResVo> page = pagePatrol(reqVo);
        List<ProjectPatrolResVo> list = new ArrayList<>();
        if (page != null) {
            list = page.getList();
        }
        export("工程巡查", response, list, ProjectPatrolResVo.class, reqVo.getSeawallName());
    }

    private <T> void export(String fileName, HttpServletResponse response, List<T> list, Class<T> clazz, String sheetName) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 字典列表
     *
     * @param bhDict
     * @return
     */
    @Override
    public List<BhDict> listDict(BhDict bhDict) {
        return projectRelMapper.listBhDict(bhDict);
    }

    /**
     * 分页查询隐患列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ProjectDangerResVo> pageDanger(ProjectDangerReqVo reqVo) {
        //首先将工程编码替换成外部的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(1)
                        .localPrcd(reqVo.getProjectCode())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return null;
        }
        reqVo.setProjectCode(foreignCode);
        reqVo.page();
        List<ProjectDangerResVo> list = projectRelMapper.listDanger(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            BhDict bhDict = new BhDict();
            bhDict.setTableName("bh_lg_prj_hide_danger");
            List<BhDict> dictList = projectRelMapper.listBhDict(bhDict);
            Map<String, String> fMap = new HashMap<>();
            Map<String, String> dMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(dictList)) {
                for (BhDict dict : dictList) {
                    if ("FOUNDMETHOD".equals(dict.getColName())) {
                        fMap.put(dict.getCode(), dict.getVal());
                    } else {
                        dMap.put(dict.getCode(), dict.getVal());
                    }
                }
            }

            //编码转换
            list.forEach(projectDangerResVo -> {
                projectDangerResVo.setFoundmethod(trans(fMap, projectDangerResVo.getFoundmethod()));
                projectDangerResVo.setDisposalmethod(trans(dMap, projectDangerResVo.getDisposalmethod()));
            });
        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 导出工程隐患
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportPageDanger(ProjectDangerReqVo reqVo, HttpServletResponse response) {
        PageVO<ProjectDangerResVo> page = pageDanger(reqVo);
        List<ProjectDangerResVo> list = new ArrayList<>();
        if (page != null) {
            list = page.getList();
        }
        export("工程隐患", response, list, ProjectDangerResVo.class, reqVo.getSeawallName());
    }

    private String trans(Map<String, String> map, String code) {
        if (StringUtils.isEmpty(code)) {
            return code;
        }
        return map.containsKey(code) ? map.get(code) : "其他";
    }

    /**
     * 分页查询历史重大险情
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<ProjectGreatDangerResVo> pageGreatDanger(ProjectGreatDangerReqVo reqVo) {
        //首先将工程编码替换成外部的
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(1)
                        .localPrcd(reqVo.getProjectCode())
                        .build()
        );
        if (StringUtils.isBlank(foreignCode)) {
            return null;
        }
        reqVo.setProjectCode(foreignCode);
        reqVo.page();
        return PageUtils.getPageVO(Collections.emptyList());
//        return PageUtils.getPageVO(projectRelMapper.listGreatDanger(reqVo));
    }

    /**
     * 分页或者列表查询监测站点
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<MonitoringStationResVo> listMs(MonitoringStationReqVo reqVo) {
        reqVo.page();
        return PageUtils.getPageVO(projectRelMapper.listMs(reqVo));
    }


    /**
     * 值班人员查询
     *
     * @param projectCode
     * @return
     */
    @Override
    public TwoDutyResVo selectDuty(String projectCode) {
        TwoDutyResVo twoDutyResVo = new TwoDutyResVo();
        String date = DateTimeFormatter.ISO_DATE.format(LocalDate.now());
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(1)
                        .localPrcd(projectCode)
                        .build()
        );
        //首先查询负责人
        twoDutyResVo.setLeader(projectRelMapper.selectLeader(foreignCode, date));

        //然后查询干活的
        twoDutyResVo.setWorker(projectRelMapper.listWorker(foreignCode, date));

        return twoDutyResVo;
    }

    /**
     * 管理信息
     *
     * @param projectCode
     * @return
     */
    @Override
    public ManageInfoResVo selectManageInfo(String projectCode) {
        ManageInfoResVo manageInfoResVo = new ManageInfoResVo();
        //安全鉴定和险情加固
        List<AttSeawallBase> attSeawallBaseList = seawallMapper.selectByCode(projectCode);
        AttSeawallBase attSeawallBase = attSeawallBaseList.get(0);
        manageInfoResVo.setSafetyConclusion(attSeawallBase.getSafetyConclusion());
        manageInfoResVo.setReinforce(attSeawallBase.getReinforce());
        //TODO 险情处置
        manageInfoResVo.setGreatDanger(0);

        //TODO 防汛预案
        manageInfoResVo.setFloodPlan(1);

        //TODO 防汛演练
        manageInfoResVo.setFloodDrill(1);

        return manageInfoResVo;
    }


    /**
     * 运行报警
     *
     * @param projectCode
     * @return
     */
    @Override
    public List<RuntimeWarnResVo> selectRuntimeWarn(String projectCode) {
        //安全鉴定
        List<AttSeawallBase> seawallBaseList = seawallMapper.selectByCode(projectCode);
        List<String> codeList = new ArrayList<>();
        Map<String, RuntimeWarnResVo> map = new HashMap<>();
        seawallBaseList.forEach(seawallBase -> {
            String code = seawallBase.getSeawallCode();
            RuntimeWarnResVo runtimeWarnResVo = new RuntimeWarnResVo();
            runtimeWarnResVo.setSeawallCode(code);
            runtimeWarnResVo.setSeawallName(seawallBase.getSeawallName());
            runtimeWarnResVo.setLatitude(seawallBase.getLatitude());
            runtimeWarnResVo.setLongitude(seawallBase.getLongitude());
            runtimeWarnResVo.setCoorpot(seawallBase.getCoorpot());
            runtimeWarnResVo.setPreventTideStandard(seawallBase.getPreventTideStandard());
            runtimeWarnResVo.setSeawallLength(seawallBase.getSeawallLength());
            runtimeWarnResVo.setSafetyConclusion(seawallBase.getSafetyConclusion());
            codeList.add(code);
            String safetyTime = seawallBase.getSafetyTime();
            //根据上次安全鉴定时间判断安全鉴定是否超期
            runtimeWarnResVo.setSafety(safeByTime(safetyTime));
            map.put(code, runtimeWarnResVo);
        });

        runtimeNoSafe(map, codeList);

        return new ArrayList<>(map.values());
    }

    private void runtimeNoSafe(Map<String, ? extends RuntimeWarnResVo> map, List<String> codeList) {
        List<ProjectCodeMapping> mappingList = projectRelMapper.listMapping(codeList);
        Map<String, String> codeMap = new HashMap<>();
        codeList.clear();
        mappingList.forEach(projectCodeMapping -> {
            codeMap.put(projectCodeMapping.getForeignPrcd(), projectCodeMapping.getLocalPrcd());
            codeList.add(projectCodeMapping.getForeignPrcd());
        });

        //汛前检查
        flood(map, codeMap, codeList);

        if (!CollectionUtils.isEmpty(codeList)) {
            //年度总结
            yearReport(map, codeMap, codeList);

            //巡查信息
            patrol(map, codeMap, codeList);

            //工程隐患
            danger(map, codeMap, codeList);
        }
    }

    /**
     * 工程绑定测站
     *
     * @param reqVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProjRel(BatchSlProjStReqVo reqVo) {

        String type = reqVo.getStType();
        String projectCode = reqVo.getProjectCode();
        SlProjStR param = new SlProjStR();
        param.setProjectCode(projectCode);

        if ("SC".equals(type)) {

            //解除旧的绑定关系
            projectRelMapper.cleanProjVideoRel(param);

            //建立新的绑定关系
            if (!CollectionUtils.isEmpty(reqVo.getList())) {
                projectRelMapper.updateProjVideoRel(reqVo);
            }
        } else {

            //解除旧的绑定关系
            Integer projectType = reqVo.getProjectType();
            String stType = reqVo.getStType();
            param.setStType(stType);
            projectRelMapper.delProjStRel(param);

            //建立新的绑定关系
            if (!CollectionUtils.isEmpty(reqVo.getList())) {
                List<SlProjStR> list = reqVo.getList();
                //给每个对象加上工程类型和工程编码
                list.forEach(slProjStR -> {
                    slProjStR.setProjectType(projectType);
                    slProjStR.setProjectCode(projectCode);
                });
                //批量保存绑定关系
                projectRelMapper.batchInsertProjStRel(list);
            }

        }
    }

    /**
     * 清除工程绑定关系
     *
     * @param slProjStR
     */
    @Override
    public void delProjRel(SlProjStR slProjStR) {
        String type = slProjStR.getStType();
        switch (type) {
            case "SC":
                projectRelMapper.cleanProjVideoRel(slProjStR);
                break;
            default:
                delProjStRel(slProjStR);
        }
    }

    /**
     * 分页查询工程(未)绑定站点
     *
     * @param reqVo
     */
    @Override
    public PageVO<SlProjStRResVo> pageProjRel(SlProjStReqVo reqVo) {
        reqVo.page();
        String type = reqVo.getStType();
        List<SlProjStRResVo> list = null;
        switch (type) {
            case "SC":
                list = projectRelMapper.listProjVideoRel(reqVo);
                break;
            case "PP":
                reqVo.setIsRainStation("1");
                reqVo.setStType(null);
                list = projectRelMapper.listProjStRel(reqVo);
                break;
            default:
                list = projectRelMapper.listProjStRel(reqVo);
        }
        return PageUtils.getPageVO(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delProjStRel(SlProjStR slProjStR) {
        projectRelMapper.delProjStRel(slProjStR);
        if (slProjStR.getIsMain() == 1) {
            //去掉工程表里的主站
            Integer projectType = slProjStR.getProjectType();
            switch (projectType) {
                case 1:
                    projectRelMapper.delSeawallMainSt(slProjStR);
                    break;
                //TODO 其他工程暂时先不做
            }
        }
    }

    /**
     * 设置(取消)主站
     *
     * @param slProjStR
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMainSt(SlProjStR slProjStR) {
        Integer isMain = slProjStR.getIsMain();
        if (isMain == 1) {
            //清除旧的主站
            projectRelMapper.cleanMainSt(slProjStR.getProjectCode());
            //更新主站
            Integer projectType = slProjStR.getProjectType();
            switch (projectType) {
                case 1:
                    projectRelMapper.updateSeawallMainSt(slProjStR);
                    break;
                //TODO 其他工程暂时先不做
            }
        } else {
            //清除主站
            Integer projectType = slProjStR.getProjectType();
            switch (projectType) {
                case 1:
                    projectRelMapper.delSeawallMainSt(slProjStR);
                    break;
                //TODO 其他工程暂时先不做
            }
        }
        projectRelMapper.updateMainSt(slProjStR);
    }

    /**
     * 各站点数量
     *
     * @param projectCode
     * @return
     */
    @Override
    public CountRelResVo countRel(String projectCode) {
        //测站数量
        CountRelResVo countRelResVo = projectRelMapper.countRelSt(projectCode);
        //摄像头数量
        countRelResVo.setScNum(projectRelMapper.countRelVideo(projectCode));

        return countRelResVo;
    }

    private void danger(Map<String, ? extends RuntimeWarnResVo> map, Map<String, String> codeMap, List<String> codeList) {
        List<BhLgPrjHideDanger> list = projectRelMapper.listDangerWarn(codeList);
        if (!CollectionUtils.isEmpty(list)) {
            for (BhLgPrjHideDanger bhLgPrjHideDanger : list) {
                String code = bhLgPrjHideDanger.getPid();
                String localCode = codeMap.get(code);
                if (map.containsKey(localCode)) {
                    RuntimeWarnResVo resVo = map.get(localCode);
                    //有未完成的就是有隐患
                    if (bhLgPrjHideDanger.getActlCompTm() == null && StringUtils.isEmpty(bhLgPrjHideDanger.getAfteraid())) {
                        resVo.setDanger(1);
                        break;
                    }
                    //有隐患就是2
                    resVo.setDanger(2);
                }
            }

        }
    }

    private void patrol(Map<String, ? extends RuntimeWarnResVo> map, Map<String, String> codeMap, List<String> codeList) {
        //查询最新的一条巡查
        List<BhSlPatrolGis> list = projectRelMapper.listPatrolWarn(codeList);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(bhSlPatrolGis -> {
                if (bhSlPatrolGis.getHiddenTroubleCount() > 0) {
                    String code = bhSlPatrolGis.getSkid();
                    String localCode = codeMap.get(code);
                    //有异常
                    if (map.containsKey(localCode)) {
                        map.get(localCode).setPatrol(0);
                    }
                }
            });
        }
    }

    private void yearReport(Map<String, ? extends RuntimeWarnResVo> map, Map<String, String> codeMap, List<String> codeList) {
        //查询上一年的年度报告
        List<BhLgMpAc> list = projectRelMapper.listBhAc(codeList);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(bhLgMpAc -> {
                String code = bhLgMpAc.getPid();
                String localCode = codeMap.get(code);
                //传了的就不算超期了
                if (map.containsKey(localCode)) {
                    map.get(localCode).setYearReport(0);
                }
            });
        }
    }

    private void flood(Map<String, ? extends RuntimeWarnResVo> map, Map<String, String> codeMap, List<String> codeList) {
        //首先判断当前时间
        LocalDate now = LocalDate.now();
        if (now.getMonthValue() < 4 || (now.getMonthValue() == 4 && now.getDayOfMonth() <= 15)) {
            //4月十五之前都不算超期
            map.forEach((k, v) -> v.setFlood(0));
        } else if (!CollectionUtils.isEmpty(codeList)) {
            //查询今年的汛前检查是否已传
            List<BhLgMpFc> list = projectRelMapper.listBhFc(codeList);
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(bhLgMpFc -> {
                    String code = bhLgMpFc.getPid();
                    String localCode = codeMap.get(code);
                    //传了的就不算超期了
                    if (map.containsKey(localCode)) {
                        map.get(localCode).setFlood(0);
                    }
                });
            }
        }
    }

    /**
     * 本地海塘编码转成标化海塘编码
     *
     * @param seawallCode
     * @return
     */
    @Override
    public String transBhSeawall(String seawallCode) {
        return projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(1)
                        .localPrcd(seawallCode)
                        .build()
        );
    }

    /**
     * 工程超警
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<WaterLevelWarnResVo> waterLevelWarn(WaterLevelWarnReqVo reqVo) {
        String projectType = reqVo.getProjectType();
        List<WaterLevelWarnResVo> result = null;

        reqVo.page();
        if (ProjectTypeTableEnum.RES.getType().equals(projectType)) {

            result = resWarn(reqVo);

        } else if (ProjectTypeTableEnum.WAGA.getType().equals(projectType)) {

            result = wagaWarn(reqVo);

        } else if (ProjectTypeTableEnum.DIKE.getType().equals(projectType)) {

            result = dikeWarn(reqVo);

        }
        //过滤不超警数据
        if (reqVo.getWarning()){
            result = result.stream().filter(r -> r.isWarning()).collect(Collectors.toList());
        }
        return PageUtils.getPageVO(result);
    }

    private List<WaterLevelWarnResVo> dikeWarn(WaterLevelWarnReqVo reqVo) {
        List<WaterLevelWarnResVo> result = projectRelMapper.listDike(reqVo);
        if (!CollectionUtils.isEmpty(result)) {
            List<String> stcdList = result.stream().filter(w -> StringUtils.isNotBlank(w.getStcd()))
                    .map(WaterLevelWarnResVo::getStcd).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stcdList)) {
                //TODO 堤防没有绑定测站数据 不知道查哪个表

            }

        }

        return result;
    }

    private List<WaterLevelWarnResVo> wagaWarn(WaterLevelWarnReqVo reqVo) {
        List<WaterLevelWarnResVo> result = projectRelMapper.listWaga(reqVo);
        if (!CollectionUtils.isEmpty(result)) {
            List<String> stcdList = result.stream().filter(w -> StringUtils.isNotBlank(w.getStcd()))
                    .map(WaterLevelWarnResVo::getStcd).collect(Collectors.toList());
            String table = StationType.GATE.table() + ym.format(LocalDate.now());
            //查询对应的水闸水位
            List<StWasR> waterLevelList = new ArrayList<>();
            if (stcdList.size() > 0){
                waterLevelList = projectRelMapper.listUpz(stcdList, table);
            }
            if (!CollectionUtils.isEmpty(waterLevelList)) {
                Map<String, StWasR> waterLevelMap = waterLevelList.stream().filter(stWasR -> stWasR.getUpz() != null)
                        .collect(Collectors.toMap(StWasR::getStcd, Function.identity()));
                result.forEach(waterLevelWarnResVo -> {
                    String stcd = waterLevelWarnResVo.getStcd();
                    if (waterLevelMap.containsKey(stcd)) {
                        StWasR stWasR = waterLevelMap.get(stcd);
                        Double waterLevel = stWasR.getUpz();
                        waterLevelWarnResVo.setDwz(stWasR.getDwz());
                        waterLevelWarnResVo.setMxgtq(stWasR.getMxgtq());
                        waterLevelWarnResVo.setOvs(stWasR.getOvs());
                        waterLevelWarnResVo.setWaterLevel(waterLevel);
                        waterLevelWarnResVo.setTm(stWasR.getTm());
                        Double controlTransportLevel = waterLevelWarnResVo.getControlTransportLevel();
                        if (isGreater(waterLevel, controlTransportLevel)) {
                            waterLevelWarnResVo.setWarning(true);
                        }
                    }
                });
            }
        }

        return result;
    }

    private boolean isGreater(Double waterLevel, Double standard) {
        return standard != null && standard != 0 && waterLevel > standard;
    }

    private List<WaterLevelWarnResVo> resWarn(WaterLevelWarnReqVo reqVo) {
        List<WaterLevelWarnResVo> result = projectRelMapper.listRes(reqVo);
        if (!CollectionUtils.isEmpty(result)) {
            List<String> stcdList = result.stream().filter(w -> StringUtils.isNotBlank(w.getStcd()))
                    .map(WaterLevelWarnResVo::getStcd).collect(Collectors.toList());
            String table = StationType.RESERVOIR.table() + ym.format(LocalDate.now());
            //查询对应的水库水位
            List<StRsvrR> waterLevelList = new ArrayList<>();
            if (stcdList.size() > 0){
                waterLevelList = projectRelMapper.listRz(stcdList, table);
            }
            if (!CollectionUtils.isEmpty(waterLevelList)) {
                Map<String, StRsvrR> waterLevelMap = waterLevelList.stream().collect(Collectors.toMap(StRsvrR::getStcd, Function.identity()));
                result.forEach(waterLevelWarnResVo -> {
                    String stcd = waterLevelWarnResVo.getStcd();
                    if (waterLevelMap.containsKey(stcd)) {
                        StRsvrR stRsvrR = waterLevelMap.get(stcd);
                        Double waterLevel = stRsvrR.getRz();
                        waterLevelWarnResVo.setWaterLevel(waterLevel);
                        waterLevelWarnResVo.setTm(stRsvrR.getTm());
                        Double limitLevel = waterLevelWarnResVo.getLimitLevel();
                        //水位超过汛限水位报警
                        if (isGreater(waterLevel, limitLevel)) {
                            waterLevelWarnResVo.setWarning(true);
                        }
                    }
                });
            }
        }

        return result;
    }

    /**
     * 工程运行报警统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<RuntimeWarnAllProjectResVo> listRuntimeWarn(RuntimeWarnReqVo reqVo) {
        //安全鉴定
        List<RuntimeWarnAllProjectResVo> proList = projectRelMapper.listRuntimeProject(reqVo);
        List<String> codeList = new ArrayList<>();
        Map<String, RuntimeWarnAllProjectResVo> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(proList)) {
            proList.forEach(runtimeWarnAllProjectResVo -> {
                String code = runtimeWarnAllProjectResVo.getProjectCode();
                codeList.add(code);
                String safetyTime = runtimeWarnAllProjectResVo.getSafetyTime();
                //根据上次安全鉴定时间判断安全鉴定是否超期
                runtimeWarnAllProjectResVo.setSafety(safeByTime(safetyTime));
                map.put(code, runtimeWarnAllProjectResVo);
            });

            runtimeNoSafe(map, codeList);
        }
        ArrayList<RuntimeWarnAllProjectResVo> result = new ArrayList<>(map.values());
        if (reqVo.getIsProjInfo()){
            setProjInfo(result);
        }
        return result;
    }


    private void setProjInfo(ArrayList<RuntimeWarnAllProjectResVo> resVos){

        for (RuntimeWarnAllProjectResVo resVo : resVos) {
            WaterLevelWarnReqVo reqVo = new WaterLevelWarnReqVo();
            reqVo.setProjectCode(resVo.getProjectCode());
            List<WaterLevelWarnResVo> result = new ArrayList<>();
            if (ProjectTypeTableEnum.RES.getType().equals(resVo.getProjectType())){
                result = resWarn(reqVo);
                WaterLevelWarnResVo vo = CollectionUtils.isEmpty(result) ? null : result.get(0);
                if (vo != null){
                    resVo.setWaterLevel(vo.getWaterLevel());
                    resVo.setLimitLevel(vo.getLimitLevel());
                    resVo.setSafetyConclusion(vo.getSafetyConclusion());
                    resVo.setTm(vo.getTm());
                }
            } else if (ProjectTypeTableEnum.WAGA.getType().equals(resVo.getProjectType())){
                result = wagaWarn(reqVo);
                WaterLevelWarnResVo vo = CollectionUtils.isEmpty(result) ? null : result.get(0);
                resVo.setWaterLevel(vo.getWaterLevel());
                resVo.setDwz(vo.getDwz());
                resVo.setMxgtq(vo.getMxgtq());
                resVo.setOvs(vo.getOvs());
            } else if (ProjectTypeTableEnum.DIKE.getType().equals(resVo.getProjectType())){
                result = dikeWarn(reqVo);
                WaterLevelWarnResVo vo = CollectionUtils.isEmpty(result) ? null : result.get(0);
                resVo.setDikeGrad(vo.getDikeGrad());
                resVo.setPlanFlSta(vo.getPlanFlSta());
                resVo.setDikeLen(vo.getDikeLen());
            }else if (ProjectTypeTableEnum.SEAWALL.getType().equals(resVo.getProjectType())){
                List<AttSeawallBase> attSeawallBases = seawallMapper.selectByCode(resVo.getProjectCode());
                AttSeawallBase base = CollectionUtils.isEmpty(attSeawallBases) ? null : attSeawallBases.get(0);
                resVo.setVillage(base.getVillage());
            }
        }
    }




    /**
     * 汛前检查列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<BeforeFloodResVo> pageBeforeFlood(CheckReqVo reqVo) {
        //本地编码转换成外部编码
        checkCodeTrans(reqVo);
        reqVo.page();
        List<BeforeFloodResVo> list = projectRelMapper.listBeforeFlood(reqVo);
        return PageUtils.getPageVO(list);
    }

    private void checkCodeTrans(CheckReqVo reqVo) {
        String foreignCode = projectRelMapper.selectForeignCode(
                ProjectCodeMapping.builder()
                        .type(1)
                        .projectType(reqVo.getProjectType())
                        .localPrcd(reqVo.getProjectCode())
                        .build()
        );
        reqVo.setProjectCode(foreignCode);
    }

    /**
     * 年度检查列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<BhLgMpAcResVo> pageYearReport(CheckReqVo reqVo) {
        //本地编码转换成外部编码
        checkCodeTrans(reqVo);
        reqVo.page();
        List<BhLgMpAcResVo> list = projectRelMapper.listYearReport(reqVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 白蚁检查
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<BhLgTermiteCheck> pageTermiteCheck(CheckReqVo reqVo) {
        //本地编码转换成外部编码
        checkCodeTrans(reqVo);
        reqVo.page();
        List<BhLgTermiteCheck> list = projectRelMapper.listTermiteCheck(reqVo);
        return PageUtils.getPageVO(list);
    }

    @Override
    public HashMap<String, Integer> waterLevelWarnCount() {
        HashMap<String, Integer> map = new HashMap<>();
        int skCount = 0,szCount = 0, dfCount = 0;
        WaterLevelWarnReqVo reqVo = new WaterLevelWarnReqVo();
        reqVo.setProjectType("水库");
        List<WaterLevelWarnResVo> sk = resWarn(reqVo);
        if (sk != null && sk.size() > 0) {
            skCount = sk.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        map.put("skCount", skCount);
        reqVo.setProjectType("水闸");
        List<WaterLevelWarnResVo> sz = wagaWarn(reqVo);
        if (sz != null && sz.size() > 0) {
            szCount = sz.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        map.put("szCount", szCount);
        reqVo.setProjectType("堤防");
        List<WaterLevelWarnResVo> df = dikeWarn(reqVo);
        if (df != null && df.size() > 0) {
            dfCount = df.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        map.put("dfCount", dfCount);
        return map;
}

    @Override
    public List<JSONObject> getProjList(List<String> codeList) {
        return projectRelMapper.getProjList(codeList);
    }

    @Override
    public FloodRiskCenter floodRiskCenter() {
        FloodRiskCenter floodRiskCenter = new FloodRiskCenter();

        //工程巡查数据
        List<RuntimeWarnAllProjectResVo> resVos = listRuntimeWarn(new RuntimeWarnReqVo());
        int skgc = 0, szgc = 0, dfgc = 0, htgc = 0;
        if (resVos != null && resVos.size() > 0) {
            for (RuntimeWarnAllProjectResVo resVo : resVos) {
                int danger = resVo.getDanger() == 1 ? 1 : 0;
                int safety = resVo.getSafety();
                int flood = resVo.getFlood();
                int patrol = resVo.getPatrol();
                int yearReport = resVo.getYearReport();
                //有问题的项目都是1,没问题都是0
                int wt = danger + safety + flood + patrol + yearReport;
                String projectType = resVo.getProjectType();
                switch (projectType) {
                    case "水库":
                        skgc += wt;
                        break;
                    case "水闸":
                        szgc += wt;
                        break;
                    case "堤防":
                        dfgc += wt;
                        break;
                    case "海塘":
                        htgc += wt;
                        break;
                    default:
                        break;
                }
            }
        }
        //查询水库风险
        WaterLevelWarnReqVo reqVo = new WaterLevelWarnReqVo();
        List<WaterLevelWarnResVo> sk = resWarn(reqVo);
        int skCount = 0;
        if (sk != null && sk.size() > 0) {
            skCount = sk.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        floodRiskCenter.setSkRisk(skCount + skgc);
        //查询水闸风险
        int szCount = 0;
        List<WaterLevelWarnResVo> sz = wagaWarn(reqVo);
        if (sz != null && sz.size() > 0) {
            szCount = sz.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        floodRiskCenter.setSzRisk(szCount + szgc);
        //查询堤防风险
        int dfCount = 0;
        List<WaterLevelWarnResVo> df = dikeWarn(reqVo);
        if (df != null && df.size() > 0) {
            dfCount = df.stream().mapToInt(m -> m.isWarning() ? 1 : 0).sum();
        }
        floodRiskCenter.setDfRisk(dfCount + dfgc);
        //海塘报警风险
        CommonResult seawallWarning = bigScreenClient.seawallWarning(null, null);
        HashMap<String, Object> data = (HashMap) seawallWarning.getData();
        List<AttSeawallBaseVo> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttSeawallBaseVo.class);
        int htBj = 0, htYj = 0, wy = 0, cj = 0;
        if (baseVo != null && baseVo.size() > 0) {
            htBj = baseVo.stream().mapToInt(m -> m.getStatus() != 1 ? 1 : 0).sum();
        }
        //海塘预警风险
        CommonResult seawallGiveAnAlarm = bigScreenClient.seawallGiveAnAlarm(null, null);
        HashMap<String, Object> seawallWarningData = (HashMap) seawallGiveAnAlarm.getData();
        List<AttSeawallBaseVo> baseVos = JSON.parseArray(JSON.toJSONString(seawallWarningData.get("list")), AttSeawallBaseVo.class);
        if (baseVos != null && baseVos.size() > 0) {
            htYj = baseVos.stream().mapToInt(m -> m.getStatus() != 1 ? 1 : 0).sum();
        }
        //沉降
        CommonResult cjList = bigScreenClient.displacement(null, 1);
        List<JSONObject> cjListData = (List<JSONObject>) cjList.getData();
        wy = cjListData.size();
        //位移
        CommonResult wyList = bigScreenClient.displacement(null, 2);
        List<JSONObject> wyListData = (List<JSONObject>) wyList.getData();
        cj = wyListData.size();

        floodRiskCenter.setHtRisk(htBj + htYj + wy + cj + htgc);
        return floodRiskCenter;
    }

    private Integer safeByTime(String safetyTime) {
        if (StringUtils.isBlank(safetyTime)) {
            return 1;
        } else {
            try {
                int dif = LocalDate.now().getYear() - Integer.valueOf(safetyTime.substring(0, 4));
                return dif > 5 ? 1 : 0;
            } catch (Exception e) {
                return 1;
            }
        }
    }
}
