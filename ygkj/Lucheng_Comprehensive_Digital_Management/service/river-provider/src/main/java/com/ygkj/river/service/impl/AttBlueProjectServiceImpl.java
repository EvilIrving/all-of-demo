package com.ygkj.river.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.enums.InspectionProblemRectifyEnum;
import com.ygkj.river.enums.ProjectStatusEnum;
import com.ygkj.river.enums.RegulatoryStatusEnum;
import com.ygkj.river.enums.YesOrNoEnum;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.AttBlueInspectionMapper;
import com.ygkj.river.mapper.AttBlueInspectionProblemMapper;
import com.ygkj.river.mapper.AttBlueProjectMapper;
import com.ygkj.river.model.AttAdcdBase;
import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.model.AttBlueInspectionProblem;
import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.service.AttBlueProjectService;
import com.ygkj.river.service.RiverInfoV2Service;
import com.ygkj.river.utils.UserUtils;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttBlueProjectReqVo;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.response.AttBlueInspectionResVo;
import com.ygkj.river.vo.response.AttBlueProjectResVo;
import com.ygkj.river.vo.response.AttBlueResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2021-08-06 9:55
 * @description
 */
@Service
public class AttBlueProjectServiceImpl implements AttBlueProjectService {

    private AttBlueProjectMapper attBlueProjectMapper;
    private GenerateDistributionIDService generateDistributionIDService;
    private AttBlueInspectionMapper inspectionMapper;
    private AttBlueInspectionProblemMapper problemMapper;

    private AttBlueInspectionMapper attBlueInspectionMapper;

    @Resource
    private AttAdcdBaseMaper attAdcdBaseMaper;

    @Autowired
    private RiverInfoV2Service riverInfoV2Service;

    public AttBlueProjectServiceImpl(@Autowired AttBlueProjectMapper attBlueProjectMapper,
                                     @Autowired GenerateDistributionIDService generateDistributionIDService,
                                     @Autowired AttBlueInspectionMapper inspectionMapper,
                                     @Autowired AttBlueInspectionProblemMapper problemMapper,
                                     @Autowired AttBlueInspectionMapper attBlueInspectionMapper) {
        this.attBlueProjectMapper = attBlueProjectMapper;
        this.generateDistributionIDService = generateDistributionIDService;
        this.inspectionMapper = inspectionMapper;
        this.problemMapper = problemMapper;
        this.attBlueInspectionMapper = attBlueInspectionMapper;
    }

    @Override
    public Boolean addProject(AttBlueProjectRecVo attBlueProject) throws Exception {

        AttBlueProject project = new AttBlueProject();
        BeanUtils.copyProperties(attBlueProject, project);

        project.setId(generateDistributionIDService.nextID());
        project.setCreateId(UserUtils.getCurrentUserId());
        project.setCreateTime(new Date());
        //更新时间顺带一起填入，方便进行排序
        project.setUpdateTime(project.getCreateTime());
        try {
            if (attBlueProjectMapper.insert(project) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public PageVO<AttBlueProjectResVo> projectList(AttBlueProjectReqVo reqVo) {
        if (ObjectUtils.isNotEmpty(reqVo.getPageNum()) && ObjectUtils.isNotEmpty(reqVo.getPageSize())) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        List<AttBlueProjectResVo> projectList = attBlueProjectMapper.selectByCondition(reqVo);
        for (AttBlueProjectResVo attBlueProjectResVo : projectList) {
            attBlueProjectResVo.setBas(attBlueProjectResVo.getBasin());
            if (StringUtils.isBlank(attBlueProjectResVo.getAdnm()) && StringUtils.isNotBlank(attBlueProjectResVo.getAdcd())) {
                attBlueProjectResVo.setAdnm(attAdcdBaseMaper.selectNameOfAdcd(attBlueProjectResVo.getAdcd()));
            }
            //翻译监管状态 和项目阶段
            ProjectStatusEnum proEnum = ProjectStatusEnum.getStatusEnumByCode(attBlueProjectResVo.getProjectStage());
            attBlueProjectResVo.setProState(proEnum.getName());
            RegulatoryStatusEnum reEnum = RegulatoryStatusEnum.getStatusEnumByCode(attBlueProjectResVo.getRegulatoryStatus());
            attBlueProjectResVo.setRegulaState(reEnum.getName());
            //翻译
            if (ObjectUtils.isNotEmpty(attBlueProjectResVo.getIsNarrow())){
                attBlueProjectResVo.setNarrow(YesOrNoEnum.getStatusEnumByCode(attBlueProjectResVo.getIsNarrow()).getKey());
            }
            if (ObjectUtils.isNotEmpty(attBlueProjectResVo.getIsAffectSafety())){
                attBlueProjectResVo.setAffectSafety(YesOrNoEnum.getStatusEnumByCode(attBlueProjectResVo.getIsAffectSafety()).getKey());
            }
        }
        if (reqVo.isWithInspection()){
            projectList.forEach( p -> {
                List<AttBlueInspection> inspections = inspectionMapper.selectList(Wrappers.lambdaQuery(AttBlueInspection.class)
                        .eq(AttBlueInspection::getProjectId, p.getId())
                        .eq(AttBlueInspection::getDelFlag, 0)
                        .orderByAsc(AttBlueInspection::getCreateTime));
                if (CollectionUtils.isNotBlank(inspections)){
                    AttBlueInspection inspection = inspections.get(inspections.size()-1);
                    AttBlueInspectionResVo resVo = new AttBlueInspectionResVo();
                    BeanUtils.copyProperties(inspection,resVo);
                    List<AttBlueInspectionProblem> problems = problemMapper.selectList(Wrappers.lambdaQuery(AttBlueInspectionProblem.class)
                            .eq(AttBlueInspectionProblem::getDelFlag, 0)
                            .eq(AttBlueInspectionProblem::getStatus, 2)
                            .eq(AttBlueInspectionProblem::getInspectionId, resVo.getId()));
                    resVo.setProblems(problems);
                    p.setInspections(resVo);
                }

            });
        }
        return PageUtils.getPageVO(projectList);
    }

    @Override
    public Boolean updateProject(AttBlueProject attBlueProject) throws Exception {
        if (StringUtils.isBlank(attBlueProject.getId())) {
            throw new Exception("id不能为空");
        }
        try {
            if (attBlueProjectMapper.updateById(attBlueProject) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Object projectDetail(String projectId) {
        //包含了基本信息，以及对应的巡查列表  巡查列表下还拥有 对应的问题列表
        //监管总次数  发现问题数   未整改问题数   逾期问题数  移交执法大队数量
        AttBlueProject attBlueProject = attBlueProjectMapper.selectById(projectId);
        if (attBlueProject != null) {
            attBlueProject.setAdnm(attAdcdBaseMaper.selectNameOfAdcd(attBlueProject.getAdcd()));
        }
        List<AttBlueInspection> inspections = inspectionMapper.selectList(Wrappers.lambdaQuery(AttBlueInspection.class)
                .eq(AttBlueInspection::getProjectId, projectId)
                .eq(AttBlueInspection::getDelFlag, 0)
                .orderByAsc(AttBlueInspection::getCreateTime));

        Map<String, Object> result = new HashMap<>();
        result.put("project", attBlueProject);
        result.put("inspection", inspections);

        //统计问题数量
        Integer problemNum = problemMapper.countTotalProblemNum(projectId);
        //未整改问题数量
        Integer unRectifyNum = problemMapper.countTotalAbNormalNum(projectId,InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
        Integer outTimeNum = problemMapper.countTotalAbNormalNum(projectId, InspectionProblemRectifyEnum.OUT_TIME.getValue());
        result.put("unDealProblem", unRectifyNum); //未整改问题数量
        result.put("outTimeProblem",outTimeNum);    //逾期问题数量
        result.put("findProblem", problemNum);   //全部问题数量

        //移交执法部门数量
        long transferNum = inspections.stream().filter(a -> ObjectUtils.isNotEmpty(a.getHiddenDanger()) && a.getHiddenDanger() == 1).count();
        result.put("hiddenDangerNum", transferNum);

        return result;
    }


    @Override
    public Object homeOverView() {


        // C位总览 修改为总监管多少次；参与多少人；移交多少次；本月监管多少次
        Integer total = inspectionMapper.countNumByAll("");
        Map<String, Object> result = new HashMap<>(4);
        result.put("inspectionNum", total);//监管次数
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(calendar.getTime());
        Integer moonTotal = inspectionMapper.selectCount(Wrappers.lambdaQuery(AttBlueInspection.class)
                .like(AttBlueInspection::getCreateTime,date));
        //本月监管次数
        result.put("moonTotal",moonTotal);
        //参与多少人
        Integer participantNum = inspectionMapper.findCountPeopleNum("");
        result.put("participantNum",participantNum);
        Integer unDealProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
        Integer outTimeDealProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.OUT_TIME.getValue());
        Integer findProblem = problemMapper.countHaveNumByAll();
        result.put("findProblem", findProblem);//发现问题数
        result.put("unDealProblem", unDealProblem + outTimeDealProblem);//未处理问题数
        Integer hiddenDangerNum = inspectionMapper.countToLawNum();
        result.put("hiddenDangerNum", hiddenDangerNum);//移交执法大队数
        Integer projectNum = attBlueProjectMapper.countProjectNum();
        result.put("projectNum", projectNum);//项目总数
        return result;
    }


    @Override
    public Object projectStaticsForHome() {

        //监管总计xx次、发现问题xx个、未整改问题xx个、逾期未整改xx个、移交执法部门xx次
        Integer total = inspectionMapper.countNumByAll("");
        Map<String, Object> result = new HashMap<>(4);
        result.put("inspectionNum", total);//监管次数
        Integer findProblem = problemMapper.countHaveNumByAll();
        Integer unDealProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
        Integer outTimeProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.OUT_TIME.getValue());
        result.put("findProblem", findProblem);//发现问题数
        result.put("unDealProblem", unDealProblem);//未整改问题数
        result.put("outTimeProblem",outTimeProblem); //逾期未整改问题数量
        Integer hiddenDangerNum = inspectionMapper.countToLawNum();
        result.put("hiddenDangerNum", hiddenDangerNum);//移交执法大队数

        return result;
    }

    @Override
    public Object homeOverViewByAdcd() {

        String adcd = "";

        // C位总览 修改为总监管多少次；参与多少人；移交多少次；本月监管多少次
        Integer total = inspectionMapper.countNumByAll("");
        Map<String, Object> result = new HashMap<>(4);
        result.put("inspectionNum", total);//监管次数
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(calendar.getTime());
        Integer moonTotal = inspectionMapper.selectCount(Wrappers.lambdaQuery(AttBlueInspection.class)
                .like(AttBlueInspection::getCreateTime,date));
        //本月监管次数
        result.put("moonTotal",moonTotal);
        //参与多少人
        Integer participantNum = inspectionMapper.findCountPeopleNum("");
        result.put("participantNum",participantNum);
        Integer unDealProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
        Integer outTimeDealProblem = problemMapper.countUnRectifyNumByAll(InspectionProblemRectifyEnum.OUT_TIME.getValue());
        Integer findProblem = problemMapper.countHaveNumByAll();
        result.put("findProblem", findProblem);//发现问题数
        result.put("unDealProblem", unDealProblem + outTimeDealProblem);//未处理问题数
        Integer hiddenDangerNum = inspectionMapper.countToLawNumByAdcd(adcd);
        result.put("hiddenDangerNum", hiddenDangerNum);//移交执法大队数
        Integer projectNum = attBlueProjectMapper.countProjectNum();
        result.put("projectNum", projectNum);//项目总数
        return result;
    }

    @Override
    public Boolean deleteProjectById(String id) {

        AttBlueProject project = new AttBlueProject();
        project.setId(id);
        project.setDelFlag(1);
        project.setUpdateTime(new Date());
        project.setUpdateId(UserUtils.getCurrentUserId());

        if (attBlueProjectMapper.updateById(project) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public AttBlueProject findById(String id) {
        return attBlueProjectMapper.findById(id);
    }

    @Override
    public List<JSONObject> projectStatisByBasin() {
        return attBlueProjectMapper.projectStatisByBasin();
    }

    @Override
    public List<JSONObject> projectStatisByRiverLevel() {
        return attBlueProjectMapper.projectStatisByRiverLevel();
    }

    @Override
    public List<JSONObject> projectStatisByAdcd() {
        return attBlueProjectMapper.projectStatisByAdcd();
    }

    @Override
    public List<JSONObject> projectStatisByProjectStage() {
        return attBlueProjectMapper.projectStatisByProjectStage();
    }



    // 河流健康码-水域岸线占补平衡 计算
    public void calculateHealthState(String riverId) {
        try {
            List<String> riverIdLIst = this.attBlueProjectMapper.selectRiverIdOfWaterAreaOccupyFillNotBalance(riverId);
            if (CollectionUtils.isBlank(riverIdLIst)) {
                return;
            }
            List<RiverHealthStatReqVo> reqVoList = new ArrayList<>(riverIdLIst.size());
            for (String riverIdKey : riverIdLIst) {
                try {
                    RiverHealthStatReqVo reqVo = new RiverHealthStatReqVo();
                    reqVo.setId(Integer.valueOf(riverIdKey));
                    reqVo.setShorelineOccupyBalance(0);
                    reqVoList.add(reqVo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (CollectionUtils.isNotBlank(reqVoList)) {
                riverInfoV2Service.updateRivSegHealthState(reqVoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exportDetailTable(AttBlueProjectReqVo reqVo, HttpServletResponse response) {
        PageVO<AttBlueProjectResVo> pageVO = projectList(reqVo);
        List<AttBlueProjectResVo> list = pageVO.getList();
        ExportUtils.exportExcel(list,"批后监管详表",
                new String[]{"项目名称","项目编号","行政区划","年份","所属河道","地点","监管次数","监管状态","最后一次监管时间","未整改问题数","单位名称","法人代表","联系电话" },
                new String[]{"projectName","projectCode","adnm","year","riverName","address","inspectionNum","regulaState","lastInspectionTime","unRectifyProblemNum","unitName","legalRepresent","phone"},
                response);
    }


    @Override
    public Object projectInspectionNum(Integer year) {
        JSONObject result = new JSONObject();
        Integer totalNum = inspectionMapper.countNumByAll("");
        result.set("totalNum",totalNum);

        Calendar calendar = Calendar.getInstance();
        List<JSONObject> jsonList = new ArrayList<>();
        if (ObjectUtils.isEmpty(year)){
            year = calendar.get(Calendar.YEAR);
        }
        Integer month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0 ; i < month ; i++){
            Integer monthNum = 0;
            monthNum = inspectionMapper.selectCount(Wrappers.lambdaQuery(AttBlueInspection.class)
                    .like(AttBlueInspection::getCreateTime, year + "-" + (10 <= (month - i) ? (month - i) : "0" + (month - i)))
                    .eq(AttBlueInspection::getDelFlag, 0));
            JSONObject json = new JSONObject();
            json.set("month",month-i);
            json.set("monthNum",monthNum);
            jsonList.add(json);
        }
        result.set("list",jsonList);
        return result;
    }

    @Override
    public Object projectProblemNum() {
        //按行政区划 统计 参与人数 + 问题数量
        JSONObject result = new JSONObject();

        Integer totalNum = 0;
        List<JSONObject> jsonList = new ArrayList<>();
        List<AttAdcdBase> attAdcdBases = attAdcdBaseMaper.countyOfWenZhou();
        for (AttAdcdBase adcd :attAdcdBases){
            JSONObject json = new JSONObject();
            json.set("areaName",adcd.getAdnm());
            Integer peopleNum = inspectionMapper.statisticsPeopleByArea(adcd.getAdcd());
            json.set("peopleNum", peopleNum);
            Integer problemNum = problemMapper.countTotalByAdcd(adcd.getAdcd());
            totalNum += problemNum;
            json.set("problemNum",problemNum);
            jsonList.add(json);
        }
        jsonList.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInt("problemNum")).reversed());
        result.set("list",jsonList);
        result.set("totalNum",totalNum);
        return result;
    }

    @Override
    public Object inspectionFinishRateAndNumber() {

        List<AttAdcdBase> wenZhouAdcdList = attAdcdBaseMaper.getWenZhouAdcdList();

        //监察完成率 根据行政区划找到对应的工程，再根据工程获取对应监察列
        List<AttBlueProject> projectList = attBlueProjectMapper.selectList(Wrappers.lambdaQuery(AttBlueProject.class)
                .eq(AttBlueProject::getDelFlag, 0).isNotNull(AttBlueProject::getAdcd));
        Map<String, List<AttBlueProject>> adcdMap = projectList.stream().collect(Collectors.groupingBy(AttBlueProject::getAdcd));

        List<AttBlueResVo> resVos = new ArrayList<>();
        List<AttBlueResVo> zeroResVos = new ArrayList<>();
        for (AttAdcdBase adcdBase: wenZhouAdcdList){

            AttBlueResVo resVo = new AttBlueResVo();
            resVo.setAreaName(adcdBase.getAdnm());
            resVo.setOrderFlag(adcdBase.getOrderFlag());
            if (adcdMap.containsKey(adcdBase.getAdcd())){
                List<AttBlueProject> projects = adcdMap.get(adcdBase.getAdcd());
                List<String> ids = projects.stream().map(AttBlueProject::getId).collect(Collectors.toList());
                List<AttBlueInspection> inspections = attBlueInspectionMapper.selectList(Wrappers.lambdaQuery(AttBlueInspection.class)
                        .eq(AttBlueInspection::getDelFlag, 0)
                        .in(AttBlueInspection::getProjectId, ids));
                resVo.setInspectionNumber(inspections.size());
                if (inspections.size()!= 0){
                    long count = inspections.stream().filter(a -> a.getResultStatus().equals(1)).count();
                    // 创建一个数值格式化对象
                    BigDecimal rate = BigDecimal.valueOf(count).divide(BigDecimal.valueOf(inspections.size()),4,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                    resVo.setFinishedRate(rate);
                }else {
                    resVo.setFinishedRate(BigDecimal.ZERO);
                }
                resVos.add(resVo);
            }else {
                resVo.setInspectionNumber(0);
                resVo.setFinishedRate(BigDecimal.ZERO);
                zeroResVos.add(resVo);
            }

        }
        Map<String,Object> result = new HashMap<>(2);

        //完成率排序
        List<AttBlueResVo> rateList = resVos.stream().filter(a -> a.getOrderFlag()!=null).sorted(Comparator.comparing(AttBlueResVo::getFinishedRate).reversed()).collect(Collectors.toList());
        zeroResVos = zeroResVos.stream().filter(a -> a.getOrderFlag() != null).sorted(Comparator.comparing(AttBlueResVo::getOrderFlag)).collect(Collectors.toList());
        rateList.addAll(zeroResVos);
        result.put("rate",rateList);

        //次数排序
        List<AttBlueResVo> numberList = resVos.stream().filter(a -> a.getOrderFlag()!=null).sorted(Comparator.comparing(AttBlueResVo::getInspectionNumber).reversed()).collect(Collectors.toList());
        numberList.addAll(zeroResVos);
        result.put("number",numberList);

        return result;
    }

}
