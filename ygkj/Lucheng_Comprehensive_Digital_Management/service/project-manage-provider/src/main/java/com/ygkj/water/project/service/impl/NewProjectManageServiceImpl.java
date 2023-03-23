package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SingletonMap;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttGateBase;
import com.ygkj.gragh.model.AttIrrBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPustR;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.AdcdBaseService;
import com.ygkj.water.project.service.NewProjectManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("newProjectManageService")
public class NewProjectManageServiceImpl implements NewProjectManageService {

    @Autowired
    private ProjectMapper projectMapper;
    @Resource
    private EngineeringSupervisionMapper engineeringSupervisionMapper;
    @Resource
    private AttAdcdBaseMapper attAdcdBaseMapper;
    @Resource
    private StationMapper stationMapper;
    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Resource
    private AdcdBaseService adcdBaseService;

    @Override
    public PageVO<ReservoirProjectResp> reservoirProject(ReservoirProjectReq vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ReservoirProjectResp> reservoirProjectResps = projectMapper.reservoirProjectList(vo);
        return PageUtils.getPageVO(reservoirProjectResps);
    }

    @Override
    public List<StatisticsReservoirTypeResp> reservoirProjectStatic(ReservoirProjectReq vo) {
        return projectMapper.reservoirProjectStatic(vo);
    }

    @Override
    public List<StatisticsSeawallTypeResp> seawallProjectStatic(SeawallProjectReq vo) {
        return projectMapper.seawallProjectStatic(vo);
    }

    @Override
    public PageVO<SeawallProjectResp> seawallProject(SeawallProjectReq vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SeawallProjectResp> seawallTypeResps = projectMapper.seawallProject(vo);
        return PageUtils.getPageVO(seawallTypeResps);
    }

    @Override
    public PageVO<DikeProjectResp> dikeProject(DikeProjectReq vo) {
        if (vo.isPaged()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<DikeProjectResp> dikeProjectResps = projectMapper.dikeProject(vo);
        return PageUtils.getPageVO(dikeProjectResps);
    }

    /**
     * 堤防工程导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportDikeProject(DikeProjectReq reqVo, HttpServletResponse response) {
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        List<DikeProjectResp> dikeList = dikeProject(reqVo).getList();
        switch (reqVo.getExportStrategy()){
            case 0:{
                ExportUtils.exportExcel(dikeList, "堤防",
                        new String[]{"工程名称", "管理层级", "行政区划", "堤防规模", "所在河流", "河道级别", "岸别","堤防类型","堤防长(m)","标绘长(m)","防洪标准"},
                        new String[]{"dikeName", "managementLevel", "countryAdnm", "dikeGrad", "bas", "riverLevel", "rvBank", "dikeType", "dikeLen", "flStaLen", "planFlSta"}, response,null, Collections.emptyMap());

            }break;
        }
    }

    @Override
    public List<StatisticsDikeTypeResp> dikeProjectStatic(DikeProjectReq vo) {
        return projectMapper.dikeProjectStatic(vo);
    }

    @Override
    public PageVO<WagaProjectResp> wagaProject(WagaProjectReq vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<WagaProjectResp> wagaProjectResps = projectMapper.wagaProject(vo);
        return PageUtils.getPageVO(wagaProjectResps);
    }

    @Override
    public List<StatisticsWagaTypeResp> wagaProjectStatic(WagaProjectReq vo) {
        return projectMapper.wagaProjectStatic(vo);
    }

    @Override
    public PageVO<IrrProjectResp> irrProject(IrrProjectReq vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<IrrProjectResp> irrProjectResps = projectMapper.irrProject(vo);
        return PageUtils.getPageVO(irrProjectResps);
    }

    @Override
    public List<StatisticsIrrTypeResp> irrProjectStatic(IrrProjectReq vo) {
        return projectMapper.irrProjectStatic(vo);
    }

    @Override
    public List<StatisticsGateTypeResp> gateProjectStatic(GateProjectReq vo) {
        return projectMapper.gateProjectStatic(vo);
    }

    @Override
    public PageVO<GateProjectResp> gateProject(GateProjectReq vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<GateProjectResp> gateProjectResps = projectMapper.gateProject(vo);
        return PageUtils.getPageVO(gateProjectResps);
    }

    @Override
    public PageVO<PustProjectResp> pustProject(PustProjectReq vo) {
        if (vo.getPageNum()>0&&vo.getPageSize()>0){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
//        List<PustProjectResp> pustProjectResps = projectMapper.pustProject(vo);
        PageVO<PustProjectResp> result= PageUtils.getPageVO(projectMapper.pustProject(vo));
        List<PustProjectResp> pustProjectResps =result.getList();
        if (CollectionUtils.isNotBlank(pustProjectResps)){
            String pustCodes=pustProjectResps.stream().map(PustProjectResp::getPustCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
            List<AttStBase> stationList=stationMapper.selectStationByProjCode(pustCodes, StationType.PUST.type());
            Map<String, StPustR> projPustMap=Collections.emptyMap();
            if (CollectionUtils.isNotBlank(stationList)){
                projPustMap=new HashMap<>(stationList.size());
                List<StPustR> stPustRList=waterAndRainMapper.selectPustByTimeCode(stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList()), null);
                Map<String, StPustR> stcdPustMap=Collections.emptyMap();
                if (CollectionUtils.isNotBlank(stationList)){
                    stcdPustMap=stPustRList.stream().collect(Collectors.toMap(StPustR::getMgstcd, Function.identity()));
                }
                for (AttStBase stBase : stationList) {
                    StPustR pust=stcdPustMap.get(stBase.getStCode());
                    if (pust!=null){
                        projPustMap.put(stBase.getProjCode(),pust);
                    }
                }
            }
            for (PustProjectResp pustProj : pustProjectResps) {
                StPustR pust= projPustMap.get(pustProj.getPustCode());
                if (pust!=null){
                    pustProj.setNum1(pust.getNum1());
                    pustProj.setNum2(pust.getNum2());
                    pustProj.setNum3(pust.getNum3());
                    pustProj.setNum4(pust.getNum4());
                    pustProj.setNum5(pust.getNum5());
                    pustProj.setJskZ(pust.getJskZ());
                    pustProj.setJscZ(pust.getJscZ());
                    pustProj.setCscZ(pust.getCscZ());
                    pustProj.setSum(pust.getSum());
                    pustProj.setPustOpened(HydrologyUtils.isPustOpened(pust.getNum1(),pust.getNum2(),pust.getNum3(),pust.getNum4(),pust.getNum5())>0?true:false);
                }
            }
        }
        result.setList(pustProjectResps);
        return result;
    }

    /**
     * 泵站工程导出
     *
     * @param reqVo                  请求入参
     * @param response 响应流
     * @return
     */
    @Override
    public void exportPustProject(PustProjectReq reqVo, HttpServletResponse response) {
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.setPageNum(-1);
        reqVo.setPageNum(-1);
        // 目前导出只需要调用mapper，而不需要实时数据
        // 日后有需要加入实时，直接调用@see com.ygkj.water.project.service.impl.NewProjectManageServiceImpl.pustProject
        List<PustProjectResp> pustProjectResps = projectMapper.pustProject(reqVo);
        switch (reqVo.getExportStrategy()){
            case 0:{
                Function<Object,Object> engScalMapping=o->{
                    EngScalEnum scalEnum= EngScalEnum.getByLevel(String.valueOf(o));
                    if (scalEnum==null){
                        return StringUtils.EMPTY;
                    }else{
                        return scalEnum.getDesc();
                    }

                };
                ExportUtils.exportExcel(pustProjectResps, "泵站",
                        new String[]{"泵站名称", "管理层级", "行政区划", "泵站规模", "所在河流", "装机流量(m³/s)", "装机功率(kW)"},
                        new String[]{"pustName", "adnm", "countryAdnm", "engScal", "bas", "insFlow", "insPow"}, response,null, Collections.singletonMap("engScal",engScalMapping));

            }break;
        }
    }

    @Override
    public PageVO<AttMnpdBaseReqVo> mnpdProject(MnpdProjectReq vo) {
        if (vo.isPaged()) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<AttMnpdBaseReqVo> attMnpdBaseReqVos = projectMapper.mnpdProject(vo);
        return PageUtils.getPageVO(attMnpdBaseReqVos);
    }

    /**
     * 导出山塘数据
     *
     * @param response
     * @param reqVo
     */
    @Override
    public void exportMnpdProject(HttpServletResponse response, MnpdProjectReq reqVo) {
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
//        StopWatch stopWatch=new StopWatch();
//        stopWatch.start("kaishi1");
        List<AttMnpdBaseReqVo> mnpdList =mnpdProject(reqVo).getList();
//       stopWatch.stop();
//        stopWatch.start("kaishi2");
        switch (reqVo.getExportStrategy()){
            case 0:{
                Function<Object,Object> engScalMapping=o->{
                    EngScalEnum scalEnum= EngScalEnum.getByLevel(String.valueOf(o));
                    if (scalEnum==null){
                        return StringUtils.EMPTY;
                    }else{
                        return scalEnum.getDesc();
                    }
                };
                ExportUtils.exportExcel(mnpdList, "山塘",
                        new String[]{"山塘名称", "山塘类别", "所在乡(镇)/村", "行政区划", "主要功能", "实时库容(万m³)", "所有权人(管理责任主体)"},
                        new String[]{"mnpdName", "mnpdCate", "strName", "countryAdnm", "zygn", "", "syqr"}, response,null, Collections.singletonMap("engScal",engScalMapping));

            }break;
        }
//        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());
    }

    @Override
    public List<StatisticsPustTypeResp> pustProjectStatic(PustProjectReq vo) {
        return projectMapper.pustProjectStatic(vo);
    }

    @Override
    public List<ProjectAreaStatic> reservoirProjectAreaStatic(String areaCode) {
        return projectMapper.reservoirProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> seawallProjectAreaStatic(String areaCode) {
        return projectMapper.seawallProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> dikeProjectAreaStatic(String areaCode) {
        return projectMapper.dikeProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> wagaProjectAreaStatic(String areaCode) {
        return projectMapper.wagaProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> irrProjectAreaStatic(String areaCode) {
        return projectMapper.irrProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> gateProjectAreaStatic(String areaCode) {
        return projectMapper.gateProjectAreaStatic(areaCode);
    }

    @Override
    public List<ProjectAreaStatic> pustProjectAreaStatic(String areaCode) {
        return projectMapper.pustProjectAreaStatic(areaCode);
    }

    @Override
    public List<DictResVo> countProject(String areaCode) {
        List<DictResVo> dictResVos = engineeringSupervisionMapper.countProjectNew("project_type",areaCode);
        String[] strings = new String[]{"水库", "泵站", "水闸", "堤防", "海塘", "闸站", "灌区"};
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
        return dictResVos;
    }

    @Override
    public CommonResult projectEngscalStatic(String type, String areaCode) {
        //水库, 泵站, 水闸, 堤防, 海塘, 闸站, 灌区
        switch (type) {
            case "水库":
                ReservoirProjectReq vo = new ReservoirProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    vo.setAreaCode(areaCode);
                }
                List<StatisticsReservoirTypeResp> data = projectMapper.reservoirProjectStatic(vo);
                StatisticsReservoirTypeResp entity = null;
                if (data.stream().filter(e->"1".equals(e.getEngScal())).count() == 0) {
                    entity = new StatisticsReservoirTypeResp();
                    entity.setNum(0);
                    entity.setEngScal("1");
                    entity.setScalName("大（Ⅰ）型");
                    data.add(entity);
                }
                if (data.stream().filter(e->"2".equals(e.getEngScal())).count() == 0) {
                    entity = new StatisticsReservoirTypeResp();
                    entity.setNum(0);
                    entity.setEngScal("2");
                    entity.setScalName("大（Ⅱ）型");
                    data.add(entity);
                }
                if (data.stream().filter(e->"3".equals(e.getEngScal())).count() == 0) {
                    entity = new StatisticsReservoirTypeResp();
                    entity.setNum(0);
                    entity.setEngScal("3");
                    entity.setScalName("中型");
                    data.add(entity);
                }
                if (data.stream().filter(e->"4".equals(e.getEngScal())).count() == 0) {
                    entity = new StatisticsReservoirTypeResp();
                    entity.setNum(0);
                    entity.setEngScal("4");
                    entity.setScalName("小（Ⅰ）型");
                    data.add(entity);
                }
                if (data.stream().filter(e->"5".equals(e.getEngScal())).count() == 0) {
                    entity = new StatisticsReservoirTypeResp();
                    entity.setNum(0);
                    entity.setEngScal("5");
                    entity.setScalName("小（Ⅱ）型");
                    data.add(entity);
                }
                data = data.stream().sorted(Comparator.comparing(StatisticsReservoirTypeResp::getEngScal)).collect(Collectors.toList());
                return CommonResult.success(data);
            case "泵站":
                PustProjectReq pustProjectReq = new PustProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    pustProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsPustTypeResp> data1 = projectMapper.pustProjectStatic(pustProjectReq);
                StatisticsPustTypeResp statisticsPustTypeResp = null;
                if (data1.stream().filter(e->"1".equals(e.getEngScal())).count() == 0) {
                    statisticsPustTypeResp = new StatisticsPustTypeResp();
                    statisticsPustTypeResp.setNum(0);
                    statisticsPustTypeResp.setEngScal("1");
                    statisticsPustTypeResp.setScalName("大（Ⅰ）型");
                    data1.add(statisticsPustTypeResp);
                }
                if (data1.stream().filter(e->"2".equals(e.getEngScal())).count() == 0) {
                    statisticsPustTypeResp = new StatisticsPustTypeResp();
                    statisticsPustTypeResp.setNum(0);
                    statisticsPustTypeResp.setEngScal("2");
                    statisticsPustTypeResp.setScalName("大（Ⅱ）型");
                    data1.add(statisticsPustTypeResp);
                }
                if (data1.stream().filter(e->"3".equals(e.getEngScal())).count() == 0) {
                    statisticsPustTypeResp = new StatisticsPustTypeResp();
                    statisticsPustTypeResp.setNum(0);
                    statisticsPustTypeResp.setEngScal("3");
                    statisticsPustTypeResp.setScalName("中型");
                    data1.add(statisticsPustTypeResp);
                }
                if (data1.stream().filter(e->"4".equals(e.getEngScal())).count() == 0) {
                    statisticsPustTypeResp = new StatisticsPustTypeResp();
                    statisticsPustTypeResp.setNum(0);
                    statisticsPustTypeResp.setEngScal("4");
                    statisticsPustTypeResp.setScalName("小（Ⅰ）型");
                    data1.add(statisticsPustTypeResp);
                }
                if (data1.stream().filter(e->"5".equals(e.getEngScal())).count() == 0) {
                    statisticsPustTypeResp = new StatisticsPustTypeResp();
                    statisticsPustTypeResp.setNum(0);
                    statisticsPustTypeResp.setEngScal("5");
                    statisticsPustTypeResp.setScalName("小（Ⅱ）型");
                    data1.add(statisticsPustTypeResp);
                }
                data1 = data1.stream().sorted(Comparator.comparing(StatisticsPustTypeResp::getEngScal)).collect(Collectors.toList());
                return CommonResult.success(data1);
            case "水闸":
                WagaProjectReq wagaProjectReq = new WagaProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    wagaProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsWagaTypeResp> data2 = projectMapper.wagaProjectStatic(wagaProjectReq);
                StatisticsWagaTypeResp statisticsWagaTypeResp = null;
                if (data2.stream().filter(e->"1".equals(e.getEngScal())).count() == 0) {
                    statisticsWagaTypeResp = new StatisticsWagaTypeResp();
                    statisticsWagaTypeResp.setNum(0);
                    statisticsWagaTypeResp.setEngScal("1");
                    statisticsWagaTypeResp.setScalName("大（Ⅰ）型");
                    data2.add(statisticsWagaTypeResp);
                }
                if (data2.stream().filter(e->"2".equals(e.getEngScal())).count() == 0) {
                    statisticsWagaTypeResp = new StatisticsWagaTypeResp();
                    statisticsWagaTypeResp.setNum(0);
                    statisticsWagaTypeResp.setEngScal("2");
                    statisticsWagaTypeResp.setScalName("大（Ⅱ）型");
                    data2.add(statisticsWagaTypeResp);
                }
                if (data2.stream().filter(e->"3".equals(e.getEngScal())).count() == 0) {
                    statisticsWagaTypeResp = new StatisticsWagaTypeResp();
                    statisticsWagaTypeResp.setNum(0);
                    statisticsWagaTypeResp.setEngScal("3");
                    statisticsWagaTypeResp.setScalName("中型");
                    data2.add(statisticsWagaTypeResp);
                }
                if (data2.stream().filter(e->"4".equals(e.getEngScal())).count() == 0) {
                    statisticsWagaTypeResp = new StatisticsWagaTypeResp();
                    statisticsWagaTypeResp.setNum(0);
                    statisticsWagaTypeResp.setEngScal("4");
                    statisticsWagaTypeResp.setScalName("小（Ⅰ）型");
                    data2.add(statisticsWagaTypeResp);
                }
                if (data2.stream().filter(e->"5".equals(e.getEngScal())).count() == 0) {
                    statisticsWagaTypeResp = new StatisticsWagaTypeResp();
                    statisticsWagaTypeResp.setNum(0);
                    statisticsWagaTypeResp.setEngScal("5");
                    statisticsWagaTypeResp.setScalName("小（Ⅱ）型");
                    data2.add(statisticsWagaTypeResp);
                }
                data2 = data2.stream().sorted(Comparator.comparing(StatisticsWagaTypeResp::getEngScal)).collect(Collectors.toList());
                return CommonResult.success(data2);
            case "堤防":
                DikeProjectReq dikeProjectReq = new DikeProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    dikeProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsDikeTypeResp> data3 = projectMapper.dikeProjectStatic(dikeProjectReq);
                StatisticsDikeTypeResp statisticsDikeTypeResp = null;
                if (data3.stream().filter(e->"1".equals(e.getEngGrad())).count() == 0) {
                    statisticsDikeTypeResp = new StatisticsDikeTypeResp();
                    statisticsDikeTypeResp.setNum(0);
                    statisticsDikeTypeResp.setEngGrad("1");
                    statisticsDikeTypeResp.setScalName("1级");
                    data3.add(statisticsDikeTypeResp);
                }
                if (data3.stream().filter(e->"2".equals(e.getEngGrad())).count() == 0) {
                    statisticsDikeTypeResp = new StatisticsDikeTypeResp();
                    statisticsDikeTypeResp.setNum(0);
                    statisticsDikeTypeResp.setEngGrad("2");
                    statisticsDikeTypeResp.setScalName("2级");
                    data3.add(statisticsDikeTypeResp);
                }
                if (data3.stream().filter(e->"3".equals(e.getEngGrad())).count() == 0) {
                    statisticsDikeTypeResp = new StatisticsDikeTypeResp();
                    statisticsDikeTypeResp.setNum(0);
                    statisticsDikeTypeResp.setEngGrad("3");
                    statisticsDikeTypeResp.setScalName("3级");
                    data3.add(statisticsDikeTypeResp);
                }
                if (data3.stream().filter(e->"4".equals(e.getEngGrad())).count() == 0) {
                    statisticsDikeTypeResp = new StatisticsDikeTypeResp();
                    statisticsDikeTypeResp.setNum(0);
                    statisticsDikeTypeResp.setEngGrad("4");
                    statisticsDikeTypeResp.setScalName("4级");
                    data3.add(statisticsDikeTypeResp);
                }
                if (data3.stream().filter(e->"5".equals(e.getEngGrad())).count() == 0) {
                    statisticsDikeTypeResp = new StatisticsDikeTypeResp();
                    statisticsDikeTypeResp.setNum(0);
                    statisticsDikeTypeResp.setEngGrad("5");
                    statisticsDikeTypeResp.setScalName("5级");
                    data3.add(statisticsDikeTypeResp);
                }
                data3 = data3.stream().sorted(Comparator.comparing(StatisticsDikeTypeResp::getEngGrad)).collect(Collectors.toList());
                return CommonResult.success(data3);
            case "海塘":
                SeawallProjectReq seawallProjectReq = new SeawallProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    seawallProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsSeawallTypeResp> data4 = projectMapper.seawallProjectStatic(seawallProjectReq);
                StatisticsSeawallTypeResp statisticsSeawallTypeResp = null;
                if (data4.stream().filter(e->"1".equals(e.getEngGrad())).count() == 0) {
                    statisticsSeawallTypeResp = new StatisticsSeawallTypeResp();
                    statisticsSeawallTypeResp.setNum(0);
                    statisticsSeawallTypeResp.setEngGrad("1");
                    statisticsSeawallTypeResp.setScalName("大（Ⅰ）型");
                    data4.add(statisticsSeawallTypeResp);
                }
                if (data4.stream().filter(e->"2".equals(e.getEngGrad())).count() == 0) {
                    statisticsSeawallTypeResp = new StatisticsSeawallTypeResp();
                    statisticsSeawallTypeResp.setNum(0);
                    statisticsSeawallTypeResp.setEngGrad("2");
                    statisticsSeawallTypeResp.setScalName("大（Ⅱ）型");
                    data4.add(statisticsSeawallTypeResp);
                }
                if (data4.stream().filter(e->"3".equals(e.getEngGrad())).count() == 0) {
                    statisticsSeawallTypeResp = new StatisticsSeawallTypeResp();
                    statisticsSeawallTypeResp.setNum(0);
                    statisticsSeawallTypeResp.setEngGrad("3");
                    statisticsSeawallTypeResp.setScalName("中型");
                    data4.add(statisticsSeawallTypeResp);
                }
                if (data4.stream().filter(e->"4".equals(e.getEngGrad())).count() == 0) {
                    statisticsSeawallTypeResp = new StatisticsSeawallTypeResp();
                    statisticsSeawallTypeResp.setNum(0);
                    statisticsSeawallTypeResp.setEngGrad("4");
                    statisticsSeawallTypeResp.setScalName("小（Ⅰ）型");
                    data4.add(statisticsSeawallTypeResp);
                }
                if (data4.stream().filter(e->"5".equals(e.getEngGrad())).count() == 0) {
                    statisticsSeawallTypeResp = new StatisticsSeawallTypeResp();
                    statisticsSeawallTypeResp.setNum(0);
                    statisticsSeawallTypeResp.setEngGrad("5");
                    statisticsSeawallTypeResp.setScalName("小（Ⅱ）型");
                    data4.add(statisticsSeawallTypeResp);
                }
                data4 = data4.stream().sorted(Comparator.comparing(StatisticsSeawallTypeResp::getEngGrad)).collect(Collectors.toList());
                return CommonResult.success(data4);
            case "闸站":
                GateProjectReq gateProjectReq = new GateProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    gateProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsGateTypeResp> data5 = projectMapper.gateProjectStatic(gateProjectReq);
                StatisticsGateTypeResp statisticsGateTypeResp = null;
                if (data5.stream().filter(e->"1".equals(e.getEngScal())).count() == 0) {
                    statisticsGateTypeResp = new StatisticsGateTypeResp();
                    statisticsGateTypeResp.setNum(0);
                    statisticsGateTypeResp.setEngScal("1");
                    statisticsGateTypeResp.setScalName("大（Ⅰ）型");
                    data5.add(statisticsGateTypeResp);
                }
                if (data5.stream().filter(e->"2".equals(e.getEngScal())).count() == 0) {
                    statisticsGateTypeResp = new StatisticsGateTypeResp();
                    statisticsGateTypeResp.setNum(0);
                    statisticsGateTypeResp.setEngScal("2");
                    statisticsGateTypeResp.setScalName("大（Ⅱ）型");
                    data5.add(statisticsGateTypeResp);
                }
                if (data5.stream().filter(e->"3".equals(e.getEngScal())).count() == 0) {
                    statisticsGateTypeResp = new StatisticsGateTypeResp();
                    statisticsGateTypeResp.setNum(0);
                    statisticsGateTypeResp.setEngScal("3");
                    statisticsGateTypeResp.setScalName("中型");
                    data5.add(statisticsGateTypeResp);
                }
                if (data5.stream().filter(e->"4".equals(e.getEngScal())).count() == 0) {
                    statisticsGateTypeResp = new StatisticsGateTypeResp();
                    statisticsGateTypeResp.setNum(0);
                    statisticsGateTypeResp.setEngScal("4");
                    statisticsGateTypeResp.setScalName("小（Ⅰ）型");
                    data5.add(statisticsGateTypeResp);
                }
                if (data5.stream().filter(e->"5".equals(e.getEngScal())).count() == 0) {
                    statisticsGateTypeResp = new StatisticsGateTypeResp();
                    statisticsGateTypeResp.setNum(0);
                    statisticsGateTypeResp.setEngScal("5");
                    statisticsGateTypeResp.setScalName("小（Ⅱ）型");
                    data5.add(statisticsGateTypeResp);
                }
                data5 = data5.stream().sorted(Comparator.comparing(StatisticsGateTypeResp::getEngScal)).collect(Collectors.toList());
                return CommonResult.success(data5);
            case "灌区":
                IrrProjectReq irrProjectReq = new IrrProjectReq();
                if (StringUtils.isNotEmpty(areaCode)) {
                    irrProjectReq.setAreaCode(areaCode);
                }
                List<StatisticsIrrTypeResp> data6 = projectMapper.irrProjectStatic(irrProjectReq);
                StatisticsIrrTypeResp statisticsIrrTypeResp = null;
                if (data6.stream().filter(e->"1".equals(e.getEngScal())).count() == 0) {
                    statisticsIrrTypeResp = new StatisticsIrrTypeResp();
                    statisticsIrrTypeResp.setNum(0);
                    statisticsIrrTypeResp.setEngScal("1");
                    statisticsIrrTypeResp.setScalName("大（Ⅰ）型");
                    data6.add(statisticsIrrTypeResp);
                }
                if (data6.stream().filter(e->"2".equals(e.getEngScal())).count() == 0) {
                    statisticsIrrTypeResp = new StatisticsIrrTypeResp();
                    statisticsIrrTypeResp.setNum(0);
                    statisticsIrrTypeResp.setEngScal("2");
                    statisticsIrrTypeResp.setScalName("大（Ⅱ）型");
                    data6.add(statisticsIrrTypeResp);
                }
                if (data6.stream().filter(e->"3".equals(e.getEngScal())).count() == 0) {
                    statisticsIrrTypeResp = new StatisticsIrrTypeResp();
                    statisticsIrrTypeResp.setNum(0);
                    statisticsIrrTypeResp.setEngScal("3");
                    statisticsIrrTypeResp.setScalName("中型");
                    data6.add(statisticsIrrTypeResp);
                }
                if (data6.stream().filter(e->"4".equals(e.getEngScal())).count() == 0) {
                    statisticsIrrTypeResp = new StatisticsIrrTypeResp();
                    statisticsIrrTypeResp.setNum(0);
                    statisticsIrrTypeResp.setEngScal("4");
                    statisticsIrrTypeResp.setScalName("小（Ⅰ）型");
                    data6.add(statisticsIrrTypeResp);
                }
                if (data6.stream().filter(e->"5".equals(e.getEngScal())).count() == 0) {
                    statisticsIrrTypeResp = new StatisticsIrrTypeResp();
                    statisticsIrrTypeResp.setNum(0);
                    statisticsIrrTypeResp.setEngScal("5");
                    statisticsIrrTypeResp.setScalName("小（Ⅱ）型");
                    data6.add(statisticsIrrTypeResp);
                }
                data6 = data6.stream().sorted(Comparator.comparing(StatisticsIrrTypeResp::getEngScal)).collect(Collectors.toList());
                return CommonResult.success(data6);
        }
        return null;
    }

    @Override
    public Integer statisticalProjectNum(String type) {
        switch (type) {
            case "堤防":
                return projectMapper.statisticalProjectDikeNum();
            case "泵站":
                return projectMapper.statisticalProjectPustNum();
            case "山塘":
                return projectMapper.statisticalProjectMnpdNum();
        }
        return null;
    }

    @Override
    public List<JSONObject> statisticalNumByArea(String type) {
        switch (type) {
            case "堤防":
                return projectMapper.statisticalProjectDikeNumByArea().stream()
                        .filter(e->StringUtils.isNotBlank(e.getString("adnm"))).collect(Collectors.toList());
            case "泵站":
                return projectMapper.statisticalProjectPustNumByArea().stream()
                        .filter(e->StringUtils.isNotBlank(e.getString("adnm"))).collect(Collectors.toList());
            case "山塘":
                return projectMapper.statisticalProjectMnpdNumByArea().stream()
                        .filter(e->StringUtils.isNotBlank(e.getString("adnm"))).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public JSONObject cAStatisticalProject(String type) {
        JSONObject jsonObject = new JSONObject();
        Integer totalNum = 0;
        switch (type) {
            case "堤防":
                totalNum = projectMapper.statisticalProjectDikeNum();
                jsonObject.put("totalNum",totalNum);//提防总数
                Integer dikeLen = projectMapper.countDikeLen();
                jsonObject.put("dikeLen",dikeLen);//提防总长度

                List<JSONObject> collect = projectMapper.statisticalProjectDikeNumByArea().stream()
                        .filter(e -> StringUtils.isNotBlank(e.getString("adnm")))
                        .sorted(Comparator.comparing(e -> e.getInteger("num"))).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    JSONObject joh = collect.get(collect.size() - 1);
                    Integer numh = joh.getInteger("num");
                    String adnmh = joh.getString("adnm");
                    BigDecimal rateh = new BigDecimal(numh + "").divide(new BigDecimal(totalNum + ""),4,BigDecimal.ROUND_HALF_UP);
                    jsonObject.put("highestNumber", adnmh + " ｜ "+numh+"条("+new BigDecimal(rateh.doubleValue() * 100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"%)");//最多数量


                    JSONObject jom = collect.get(0);
                    Integer numm = jom.getInteger("num");
                    String adnmm = jom.getString("adnm");
                    BigDecimal ratem = new BigDecimal(numm  + "").divide(new BigDecimal(totalNum + ""),4,BigDecimal.ROUND_HALF_UP);
                    jsonObject.put("miniNumber",adnmm + " ｜ "+numm+"条("+new BigDecimal(ratem.doubleValue() * 100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() +"%)");
                } else {
                    jsonObject.put("highestNumber","");
                    jsonObject.put("miniNumber","");
                }

                break;
            case "泵站":
                totalNum = projectMapper.statisticalProjectPustNum();
                jsonObject.put("totalNum",totalNum);//泵站总数

                Integer totalInstalled = projectMapper.countInsFlow();
                jsonObject.put("totalInstalled",totalInstalled);//总装机

                List<JSONObject> collect1 = projectMapper.statisticalProjectPustNumByArea().stream()
                        .filter(e -> StringUtils.isNotBlank(e.getString("adnm")))
                        .sorted(Comparator.comparing(e -> e.getInteger("num"))).collect(Collectors.toList());

                if (!collect1.isEmpty()) {
                    JSONObject joh = collect1.get(collect1.size() - 1);
                    Integer numh = joh.getInteger("num");
                    String adnmh = joh.getString("adnm");
                    BigDecimal rateh = new BigDecimal(numh * 100 + "").divide(new BigDecimal(totalNum + ""), 2, BigDecimal.ROUND_HALF_UP);
                    jsonObject.put("highestNumber", adnmh + " ｜ "+numh+"条("+rateh+"%)");//最多数量
                } else {
                    jsonObject.put("highestNumber","");
                }

                List<PustProjectResp> collect2 = projectMapper.pustProject(new PustProjectReq()).stream().filter(e->e.getInsFlow() != null)
                        .sorted(Comparator.comparing(PustProjectResp::getInsFlow).reversed()).collect(Collectors.toList());
                if (!collect2.isEmpty()) {
                    jsonObject.put("largestInstalledEntity",collect2.get(0));
                } else {
                    jsonObject.put("largestInstalledEntity",null);
                }
                break;
            case "山塘":
                totalNum = projectMapper.statisticalProjectMnpdNum();
                jsonObject.put("totalNum",totalNum);//山塘总数

                List<JSONObject> collect3 = projectMapper.statisticalProjectMnpdNumByArea().stream()
                        .filter(e -> StringUtils.isNotBlank(e.getString("adnm")))
                        .sorted(Comparator.comparing(e -> e.getInteger("num"))).collect(Collectors.toList());

                if (!collect3.isEmpty()) {
                    JSONObject joh = collect3.get(collect3.size() - 1);
                    Integer numh = joh.getInteger("num");
                    String adnmh = joh.getString("adnm");
                    BigDecimal rateh = new BigDecimal(numh * 100 + "").divide(new BigDecimal(totalNum + ""), 2, BigDecimal.ROUND_HALF_UP);
                    jsonObject.put("highestNumber", adnmh + " ｜ "+numh+"条("+rateh+"%)");//最多数量
                } else {
                    jsonObject.put("highestNumber","");
                }

                List<JSONObject> jsonObjects = projectMapper.selectMnpdGroupByZygn();
                if (!jsonObjects.isEmpty()) {
                    Integer zygnTotal = 0;
                    for (JSONObject object : jsonObjects) {
                        String zygn = object.getString("zygn");
                        Integer num = object.getInteger("num");
                        if (zygn.contains("灌溉")) {
                            zygnTotal += num;
                        }
                    }
                    BigDecimal rateh = new BigDecimal(zygnTotal * 100 + "").divide(new BigDecimal(totalNum + ""), 2, BigDecimal.ROUND_HALF_UP);
                    jsonObject.put("mainPurpose","灌溉 | " + zygnTotal + "个(" + rateh+"%)");
                } else {
                    jsonObject.put("mainPurpose","");
                }
                break;
        }
        return jsonObject;
    }

    /**
     * 大屏灌区统计接口
     * @param type 1. 统计大型，中型，小型灌区数目，2. c位统计
     */
    @Override
    public Object irrStats4BigScreen(int type) {
        switch (type){
            case 1:{
                List<SingletonMap<String,Long>> result=new ArrayList<>();
                List<AttIrrBase> irrList= this.projectMapper.selectIrrProjectByCondition(null);
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"大型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"1,2")
                        .with(SingletonMap::setValue,irrList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.L1.getEngScal().equals(o.getEngScal())
                                        || EngScalEnum.L2.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"中型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"3")
                        .with(SingletonMap::setValue,irrList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.MID.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"小型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"4,5")
                        .with(SingletonMap::setValue,irrList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.S1.getEngScal().equals(o.getEngScal())
                                        || EngScalEnum.S2.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                return result;
            }
            case 2:{
                Map<String,Object> result=new HashMap<>();
                List<AttIrrBase> tempList= this.projectMapper.selectIrrProjectByCondition(null);
                result.put("irrNum",tempList.size());
                result.put("desIrrArea",tempList.stream().filter(o->o.getDesIrrArea()!=null).mapToInt(AttIrrBase::getDesIrrArea).sum());
                result.put("effIrrArea",tempList.stream().filter(o->o.getEffIrrArea()!=null).mapToInt(AttIrrBase::getEffIrrArea).sum());
                return result;
            }
            default:
                return null;
        }
    }

    /**
     * 大屏闸站统计接口
     *
     * @param type 1. 统计大型，中型，小型灌区数目，2. c位统计
     */
    @Override
    public Object gateStats4BigScreen(int type) {
        switch (type){
            case 1:{
                List<SingletonMap<String,Long>> result=new ArrayList<>();
                List<AttGateBase> gateList= this.projectMapper.selectGateProjectByCondition(null);
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"大型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"1,2")
                        .with(SingletonMap::setValue,gateList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.L1.getEngScal().equals(o.getEngScal())
                                        || EngScalEnum.L2.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"中型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"3")
                        .with(SingletonMap::setValue,gateList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.MID.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                result.add(Builder.of(SingletonMap<String,Long>::new)
                        .with(SingletonMap::setKey,"小型")
                        // remark 给前端，方便筛选
                        .with(SingletonMap::setRemark,"4,5")
                        .with(SingletonMap::setValue,gateList.stream()
                                .filter(o->StringUtils.isNotBlank(o.getEngScal()))
                                .filter(o-> EngScalEnum.S1.getEngScal().equals(o.getEngScal())
                                        || EngScalEnum.S2.getEngScal().equals(o.getEngScal()))
                                .count())
                        .build());
                return result;
            }
            case 2:{
                Map<String,Object> result=new HashMap<>();
                List<AttGateBase> tempList= this.projectMapper.selectGateProjectByCondition(null);
                result.put("gateNum",tempList.size());
                result.put("totalInsPow",tempList.stream().map(AttGateBase::getSingleInsPow).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add));
                return result;
            }
            default:
                return null;
        }
    }

    /**
     * 导出闸站数据
     *
     * @param response
     * @param reqVo
     */
    @Override
    public void exportGateProject(HttpServletResponse response, GateQueryReqVo reqVo) {
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        // 目前导出只需要调用mapper，而不需要实时数据
        List<AttGateBase> gateList = projectMapper.selectGateProjectByCondition(reqVo);
        switch (reqVo.getExportStrategy()){
            case 0:{
                Function<Object,Object> engScalMapping=o->{
                    EngScalEnum scalEnum= EngScalEnum.getByLevel(String.valueOf(o));
                    if (scalEnum==null){
                        return StringUtils.EMPTY;
                    }else{
                        return scalEnum.getDesc();
                    }

                };
                ExportUtils.exportExcel(gateList, "闸站",
                        new String[]{"闸站名称", "管理层级", "所在市", "所在县", "闸站规模", "过闸流量(m³/s)", "装机功率(kW)"},
                        new String[]{"gateName", "managementLevel", "locCity", "locCounty", "engScal", "maxFlow", "singleInsPow"}, response,null, Collections.singletonMap("engScal",engScalMapping));

            }break;
        }
    }

    /**
     * 导出灌区数据
     *
     * @param response
     * @param reqVo
     */
    @Override
    public void exportIrrProject(HttpServletResponse response, IrrQueryReqVo reqVo) {
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        // 目前导出只需要调用mapper，而不需要实时数据
        List<AttIrrBase> irrList = projectMapper.selectIrrProjectByCondition(reqVo);
        switch (reqVo.getExportStrategy()){
            case 0:{
                List<String> adcdList = irrList.stream().map(AttIrrBase::getAdcd).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
                Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);
                Function<Object,Object> engScalMapping=o->{
                    EngScalEnum scalEnum= EngScalEnum.getByLevel(String.valueOf(o));
                    if (scalEnum==null){
                        return StringUtils.EMPTY;
                    }else{
                        return scalEnum.getDesc();
                    }
                };
                for (AttIrrBase irr : irrList) {
                    irr.setAdnm(adcdMap.get(irr.getAdcd()));
                }
                ExportUtils.exportExcel(irrList, "灌区",
                        new String[]{"灌区名称", "工程规模", "设计灌溉面积", "有效灌溉面积", "管理单位", "行政区划", "流域","管理层级"},
                        new String[]{"irrName", "engScal", "desIrrArea", "effIrrArea", "manUnit", "adnm", "bas","managementLevel"}, response,null, Collections.singletonMap("engScal",engScalMapping));

            }break;
        }
    }
}
