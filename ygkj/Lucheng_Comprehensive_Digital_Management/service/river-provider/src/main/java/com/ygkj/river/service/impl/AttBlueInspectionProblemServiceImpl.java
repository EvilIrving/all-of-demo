package com.ygkj.river.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.river.enums.InspectionProblemRectifyEnum;
import com.ygkj.river.enums.InspectionProblemStatusEnum;
import com.ygkj.river.enums.ProjectStatusEnum;
import com.ygkj.river.enums.RegulatoryStatusEnum;
import com.ygkj.river.mapper.AttBlueInspectionMapper;
import com.ygkj.river.mapper.AttBlueInspectionProblemMapper;
import com.ygkj.river.mapper.AttBlueProjectMapper;
import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.model.AttBlueInspectionProblem;
import com.ygkj.river.service.AttBlueInspectionProblemService;
import com.ygkj.river.service.RiverInfoV2Service;
import com.ygkj.river.service.RiverService;
import com.ygkj.river.utils.UserUtils;
import com.ygkj.river.vo.request.AttBlueInspectionProblemReqVo;
import com.ygkj.river.vo.request.AttBlueInspectionReqVo;
import com.ygkj.river.vo.request.AttPatrolFeedbackReqVo;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.response.AttBlueInspectionProblemResVo;
import com.ygkj.river.vo.response.AttBlueInspectionResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2021-08-06 20:36
 * @description
 */
@Service
public class AttBlueInspectionProblemServiceImpl implements AttBlueInspectionProblemService, DisposableBean {

    private AttBlueInspectionProblemMapper problemMapper;
    private GenerateDistributionIDService generateDistributionIDService;
    private AttBlueInspectionMapper inspectionMapper;
    ExecutorService executorService;
    private AttBlueProjectMapper projectMapper;

    @Autowired
    RiverInfoV2Service riverInfoV2Service;

    @Autowired
    RiverService riverService;

    public AttBlueInspectionProblemServiceImpl(@Autowired AttBlueInspectionProblemMapper problemMapper,
                                               @Autowired GenerateDistributionIDService generateDistributionIDService,
                                               @Autowired AttBlueInspectionMapper inspectionMapper,
                                               @Autowired AttBlueProjectMapper projectMapper) {
        this.problemMapper = problemMapper;
        this.generateDistributionIDService = generateDistributionIDService;
        this.inspectionMapper = inspectionMapper;
        this.projectMapper = projectMapper;
        executorService = new ThreadPoolExecutor(4, 16, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(16));
    }

    @Override
    public Boolean addInspectionProblem(AttBlueInspectionProblem attBlueInspectionProblem) {

        attBlueInspectionProblem.setId(generateDistributionIDService.nextID());
        attBlueInspectionProblem.setCreateId(UserUtils.getCurrentUserId());
        attBlueInspectionProblem.setCreateTime(new Date());
        if (problemMapper.insert(attBlueInspectionProblem) > 0) {
//            submitHealthTask(attBlueInspectionProblem.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean setProblemNormal(String problemIds) {
        String[] split = problemIds.split(",");
        //设置对应问题为无需整改
        String projectId = problemMapper.getProjectIdByProblemId(split[0]);
        projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.NO_PROBLEM.getValue(),projectId);
        return problemMapper.setProblemNormal(Arrays.asList(split)) > 0 ? true : false;
    }

    @Override
    public Boolean setProblemAbNormal(String problemId, Byte status) {

        AttBlueInspectionProblem problem = new AttBlueInspectionProblem();
        problem.setId(problemId);
        problem.setStatus(status.intValue());
        //判断状态为正常的话  设置为无需整改   否则设置为未整改
        problem.setRectify(status.equals(InspectionProblemStatusEnum.NORMAL.getValue()) ?
                InspectionProblemRectifyEnum.NO_RECTIFY.getValue() :
                InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
//        未确定整改需求
//        String projectId = problemMapper.getProjectIdByProblemId(problemId);
//        projectMapper.updateRegulatoryStatusById(status.equals(InspectionProblemStatusEnum.NORMAL.getValue()) ?
//                RegulatoryStatusEnum.NO_PROBLEM.getValue():RegulatoryStatusEnum.CONDUCT.getValue(),projectId);
        if (problemMapper.updateById(problem) > 0) {
//            submitHealthTask(problem.getId());
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Object getProblemListByInspectionId(AttBlueInspectionProblemReqVo reqVo) {
        Map<String, Object> result = new HashMap<>(4);
        boolean flag = false;
        if (StringUtils.isNotBlank(reqVo.getRectify()) &&reqVo.getRectify().equals("4")){
            flag = true;
            reqVo.setRectify(null);
        }
        List<AttBlueInspectionProblem> problems = problemMapper.selectList(Wrappers.lambdaQuery(AttBlueInspectionProblem.class)
                .eq(AttBlueInspectionProblem::getInspectionId, reqVo.getInspectionId())
                .eq(AttBlueInspectionProblem::getDelFlag, 0)
                .eq(StringUtils.isNotBlank(reqVo.getStatus()),AttBlueInspectionProblem::getStatus,reqVo.getStatus())
                .eq(StringUtils.isNotBlank(reqVo.getRectify()),AttBlueInspectionProblem::getRectify,reqVo.getRectify())
                .orderByDesc(AttBlueInspectionProblem::getCreateTime));
        List<AttBlueInspectionProblemResVo> resultList = new ArrayList<>();
        if (flag){
            problems = problems.stream().filter(a -> a.getRectify()!=0).collect(Collectors.toList());
        }
        for (AttBlueInspectionProblem problem : problems) {
            AttBlueInspectionProblemResVo resVo = new AttBlueInspectionProblemResVo();
            BeanUtils.copyProperties(problem, resVo);
            if (StringUtils.isNotBlank(problem.getFileIds())) {
                String[] sp = problem.getFileIds().split(",");
                resVo.setUrls(sp);
            }
            resultList.add(resVo);
        }

        result.put("list", resultList);
        Long normalNum = problems.stream().filter(p -> p.getStatus() == 1).count();
        Long unNormalNum = problems.stream().filter(p -> p.getStatus() == 2).count();
        result.put("normal", normalNum);
        result.put("unNormal", unNormalNum);
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean patrolFeedback(AttPatrolFeedbackReqVo reqVo) throws Exception {
        String userId = UserUtils.getCurrentUserId();
        Date date = new Date();
        String inspectionId = null;
        AttBlueInspection attBlueInspection = null;
//        List<AttBlueInspectionProblem> problemList = JSON.parseArray(problems, AttBlueInspectionProblem.class);
        List<AttBlueInspectionProblem> problems = reqVo.getProblems();
        String type = reqVo.getType();
        if (CollectionUtils.isNotBlank(problems)) {
            inspectionId = problems.get(0).getInspectionId();
            attBlueInspection = inspectionMapper.selectById(inspectionId);
        }
        //判断本次巡查是否异常标识
        boolean flag = false;
        //批量更新对应问题
        for (AttBlueInspectionProblem problem : problems) {
            problem.setUpdateId(userId);
            problem.setUpdateTime(date);
            //如果状态为异常，整改同步更新为 未整改
            if (problem.getStatus().equals(InspectionProblemStatusEnum.AB_NORMAL.getValue())) {
                problem.setRectify(InspectionProblemRectifyEnum.NOT_RECTIFY.getValue());
                flag = true;
            }else if ("2".equals(type)){
                //如果是复核的话，并且状态为正常情况，同步更新问题为已整改状态
                problem.setRectify(InspectionProblemRectifyEnum.RECTIFY.getValue());
            }
            if (!problem.getRectify().equals(InspectionProblemRectifyEnum.NO_RECTIFY)){
                problemMapper.updateById(problem);
            }
        }
        if (ObjectUtils.isNotEmpty(attBlueInspection)) {
            attBlueInspection.setUpdateId(userId);
            attBlueInspection.setUpdateTime(date);
            //如果为 1 表示为 首次巡检  否则为复核巡检
            if (type.equals("1")) {
                attBlueInspection.setPatrolEndTime(date);
                attBlueInspection.setPatrolResult(flag ? 1 : 0);
                //结果正常直接完成
                attBlueInspection.setResultStatus(flag ? 0 : 1);
            } else {
                attBlueInspection.setReviewerEndTime(date);
                attBlueInspection.setReviewResult(flag ? 1 : 0);
                //结果正常直接完成
                attBlueInspection.setResultStatus(flag ? 0 : 1);
            }
            if (inspectionMapper.updateById(attBlueInspection) > 0) {
                //结果如果正常，项目进入完成状态，否则进入待整改状态   ps-- 添加逻辑，如果状态都正常，可设定下次巡查时间，自动创建下次巡查派发。
                if (!flag){
                    //都是正常  且下次巡查时间不为空
                    if (ObjectUtils.isNotEmpty(reqVo.getNextDate())){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        //发起新的巡查
                        AttBlueInspectionReqVo addInspectionReqVo = new AttBlueInspectionReqVo();
                        addInspectionReqVo.setInspector(attBlueInspection.getInspector());
                        addInspectionReqVo.setStage(attBlueInspection.getStage());
                        addInspectionReqVo.setInspectorId(attBlueInspection.getInspectorId());
                        addInspectionReqVo.setProjectId(attBlueInspection.getProjectId());
                        addInspectionReqVo.setProjectName(attBlueInspection.getProjectName());
                        addInspectionReqVo.setPatrolBeginTime(sdf.parse(reqVo.getNextDate()));
                        addInspectionReqVo.setProblems(problems.stream().map(AttBlueInspectionProblem::getEntryName).collect(Collectors.toList()));
                        addInspection(addInspectionReqVo);

                        projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.NEW.getValue(),attBlueInspection.getProjectId());
                    }else {
                        //如果下次巡查为空即结束巡查
                        projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.OVER.getValue() ,attBlueInspection.getProjectId());
                    }
                }else {
                    projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.CONDUCT.getValue() ,attBlueInspection.getProjectId());
                }

                return true;
            }
        }
        return false;
    }


    /**
     * 河长通-河流健康码-河湖管护到位 计算
     */
    public void calculateHztHealthState() {
        try {
            List<String> riverIdLIst = this.problemMapper.selectRiverIdWithProblem();
            if (CollectionUtils.isBlank(riverIdLIst)) {
                return;
            }
            List<RiverHealthStatReqVo> reqVoList = new ArrayList<>(riverIdLIst.size());
            for (String riverIdKey : riverIdLIst) {
                try {
                    RiverHealthStatReqVo reqVo = new RiverHealthStatReqVo();
                    reqVo.setId(Integer.valueOf(riverIdKey));
                    reqVo.setRiverManageWell(0);
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


    public Boolean addInspection(AttBlueInspectionReqVo reqVo) throws Exception{
        String currentUserId = UserUtils.getCurrentUserId();
        String inspectionId = generateDistributionIDService.nextID();
        AttBlueInspection inspection = new AttBlueInspection();
        inspection.setStage(reqVo.getStage());
        inspection.setInspector(reqVo.getInspector());
        inspection.setInspectorId(reqVo.getInspectorId());
        inspection.setDistributor(UserUtils.getCurrentUserName());
        inspection.setProjectId(reqVo.getProjectId());
        inspection.setProjectName(reqVo.getProjectName());
        inspection.setId(inspectionId);
        inspection.setCreateId(currentUserId);
        Date date = new Date();
        inspection.setPatrolBeginTime(ObjectUtils.isNotEmpty(reqVo.getPatrolBeginTime())? reqVo.getPatrolBeginTime():date);
        inspection.setCreateTime(date);
        if (inspectionMapper.insert(inspection)>0){
            //进行对应 异常配置的插入
            if (CollectionUtils.isNotBlank(reqVo.getProblems())){
                for (String problemName:reqVo.getProblems()){
                    AttBlueInspectionProblem problem = new AttBlueInspectionProblem();
                    problem.setCreateId(currentUserId);
                    problem.setCreateTime(new Date());
                    problem.setInspectionId(inspectionId);
                    problem.setId(generateDistributionIDService.nextID());
                    problem.setEntryName(problemName);
                    problemMapper.insert(problem);
                }
            }else {
                throw new Exception("未选择巡查问题");
            }
            projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.NEW.getValue(),reqVo.getProjectId());
            return true;
        }
        return false;
    }


    /**
     * 定时更新 逾期未整改任务
     */
    public void setOutTimeProblem(){

        problemMapper.updateOutTimeProblem();

    }
    @Override
    public Map<String,Object> statusCount() {

        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        List<AttBlueInspectionResVo> resVos = inspectionMapper.statusListCount(userId);
        Map<String,Object> result = new HashMap<>(4);
        //进行不同状态的数量筛选
        result.put("全部",resVos.size()); //全部
        long earlyCount = resVos.stream().filter(a -> a.getStage().equals(ProjectStatusEnum.EARLY.getValue())).count();
        result.put(ProjectStatusEnum.EARLY.getName(), earlyCount);
        long constructionCount = resVos.stream().filter(a -> a.getStage().equals(ProjectStatusEnum.CONSTRUCTION.getValue())).count();
        result.put(ProjectStatusEnum.CONSTRUCTION.getName(),constructionCount);
        long finishedCount = resVos.stream().filter(a -> a.getStage().equals(ProjectStatusEnum.FINISHED.getValue())).count();
        result.put(ProjectStatusEnum.FINISHED.getName(), finishedCount);
        return result;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }
}
