package com.ygkj.river.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.river.enums.RegulatoryStatusEnum;
import com.ygkj.river.feign.AuthenticationClient;
import com.ygkj.river.feign.SmsFeignClient;
import com.ygkj.river.mapper.*;
import com.ygkj.river.model.*;
import com.ygkj.river.service.AttBlueInspectionService;
import com.ygkj.river.utils.CommomUtil;
import com.ygkj.river.utils.WordUtil;
import com.ygkj.river.vo.request.AttBlueInspectionReqVo;
import com.ygkj.river.vo.response.AttBlueInspectionResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.supervision.model.SysUser;
import com.ygkj.supervision.vo.response.DeptRespNodeVO;
import com.ygkj.supervision.vo.response.SysUserResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2021-08-06 14:06
 * @description 蓝线巡查
 */
@Service
public class AttBlueInspectionServiceImpl implements AttBlueInspectionService {

    private AttBlueInspectionMapper inspectionMapper;
    private AttBlueInspectionProblemMapper problemMapper;
    private GenerateDistributionIDService generateDistributionIDService;
    private SmsFeignClient smsClient;
    private AttBlueMsgInfoMapper msgInfoMapper;
    private AttBlueProjectMapper projectMapper;
    private SysUserMapper sysUserMapper;
    private AttAdcdBaseMaper attAdcdBaseMaper;
    private AuthenticationClient authenticationClient;
    private AttBlueNoticeRecordMapper attBlueNoticeRecordMapper;

    public AttBlueInspectionServiceImpl(@Autowired AttBlueInspectionMapper inspectionMapper,
                                        @Autowired GenerateDistributionIDService generateDistributionIDService,
                                        @Autowired AttBlueInspectionProblemMapper problemMapper,
                                        @Autowired SmsFeignClient smsClient,
                                        @Autowired AttBlueMsgInfoMapper msgInfoMapper,
                                        @Autowired AttBlueProjectMapper projectMapper,
                                        @Autowired SysUserMapper sysUserMapper,
                                        @Autowired AttAdcdBaseMaper attAdcdBaseMaper,
                                        @Autowired AuthenticationClient authenticationClient,
                                        @Autowired AttBlueNoticeRecordMapper attBlueNoticeRecordMapper) {
        this.inspectionMapper = inspectionMapper;
        this.generateDistributionIDService = generateDistributionIDService;
        this.problemMapper = problemMapper;
        this.smsClient = smsClient;
        this.msgInfoMapper = msgInfoMapper;
        this.projectMapper = projectMapper;
        this.sysUserMapper = sysUserMapper;
        this.attAdcdBaseMaper = attAdcdBaseMaper;
        this.authenticationClient = authenticationClient;
        this.attBlueNoticeRecordMapper = attBlueNoticeRecordMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addInspection(AttBlueInspectionReqVo reqVo) throws Exception{
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("-");
        String inspectionId = generateDistributionIDService.nextID();
        AttBlueInspection inspection = new AttBlueInspection();
        inspection.setStage(reqVo.getStage());
        inspection.setInspector(reqVo.getInspector());
        inspection.setInspectorId(reqVo.getInspectorId());
        inspection.setDistributor(JwtTokenUtil.getCurrentUserName().orElse("-"));
        inspection.setProjectId(reqVo.getProjectId());
        inspection.setProjectName(reqVo.getProjectName());
        inspection.setId(inspectionId);
        inspection.setCreateId(currentUserId);
        Date date = new Date();
        inspection.setPatrolBeginTime(date);
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
            projectMapper.updateProjectStatus(reqVo.getStage(),reqVo.getProjectId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateInspection(AttBlueInspection attBlueInspection) {

        attBlueInspection.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        attBlueInspection.setUpdateTime(new Date());

        if (inspectionMapper.updateById(attBlueInspection)>0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean reviewDistribute(AttBlueInspectionReqVo reqVo) {

        AttBlueInspection attBlueInspection = inspectionMapper.selectById(reqVo.getInspectionId());
        attBlueInspection.setRectifyName(reqVo.getRectifyName());
        attBlueInspection.setRectifyBeginTime(reqVo.getRectifyBeginTime());
        attBlueInspection.setRectifyEndTime(reqVo.getRectifyEndTime());
        attBlueInspection.setReviewer(reqVo.getReviewer());
        attBlueInspection.setReviewerId(reqVo.getReviewerId());
        AttBlueInspection inspection = inspectionMapper.selectById(reqVo.getInspectionId());
        //复核派发有通知记录的时间
        if (StringUtils.isNotBlank(reqVo.getRectifyNoticeTime())){
            List<String> strs = Arrays.asList(reqVo.getRectifyNoticeTime().split(","));
            List<Integer> times = strs.stream().map(Integer::parseInt).collect(Collectors.toList());
            saveNoticeMsgDate(times,reqVo.getReviewerId(),reqVo.getInspectionId(),inspection.getStage(),inspection.getProjectId(),reqVo.getRectifyBeginTime(),reqVo.getRectifyEndTime());
        }

        attBlueInspection.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        attBlueInspection.setUpdateTime(new Date());
        attBlueInspection.setRectifyPeople(reqVo.getInspector());
        if (inspectionMapper.updateById(attBlueInspection)>0){
            projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.NEW.getValue(), inspection.getProjectId());
            return true;
        }
        return false;
    }


    private void saveNoticeMsgDate(List<Integer> times,String noticeUserId,String inspectionId,Integer stage,String projectId,Date rectifyBeginTime,Date rectifyEndTime){

        SysUser noticeUser = sysUserMapper.selectById(noticeUserId);
        if (ObjectUtils.isNotEmpty(noticeUser)){
            //发送浙政钉通知
            String state = stage == 1 ?"前期项目":"项目";
            AttBlueProject project = projectMapper.selectById(projectId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

            String content = "通知：关于" + state  +
                    "的整改通知" + project.getProjectName() +
                    "项目于" + sdf.format(rectifyBeginTime) + "下达整改任务，截止" + sdf.format(rectifyEndTime)
                    + "需整改完成。" + "项目地点：" + project.getAddress()
                    + ",项目负责人" + project.getLegalRepresent() + ",项目负责人电话：" + project.getPhone();

            for (Integer time : times) {
                //查询当前的巡查员信息
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rectifyEndTime);
                calendar.add(Calendar.DAY_OF_MONTH, time * -1);
                Date noticeTime = calendar.getTime();
                //插入通知时间记录
                attBlueNoticeRecordMapper.insert(Builder.of(AttBlueNoticeRecord::new)
                        .with(AttBlueNoticeRecord::setId,generateDistributionIDService.nextID())
                        .with(AttBlueNoticeRecord::setInspectionId,inspectionId)
                        .with(AttBlueNoticeRecord::setContent,content)
                        .with(AttBlueNoticeRecord::setPhone,noticeUser.getPhone())
                        .with(AttBlueNoticeRecord::setUserId,noticeUser.getId())
                        .with(AttBlueNoticeRecord::setTm,noticeTime)
                        .with(AttBlueNoticeRecord::setCreateTime,new Date())
                        .build());
            }
        }

    }


    /**
     * 统一发送方法
     * @param logs
     */
    private void sendMsgByLogs(List<SysSendMessageLog> logs){
        //TODO 未正式上线 进行注释 避免发送

        for (SysSendMessageLog log : logs) {
            log.setPhone("18030383659");
        }
        smsClient.sendMessage(logs);
    }



    @Override
    public Boolean hiddenDanger(String appraisalResult,String inspectionId, boolean flag) {
        String userId =  JwtTokenUtil.getCurrentUserId().orElse("-");
        AttBlueInspection attBlueInspection = inspectionMapper.selectById(inspectionId);
        attBlueInspection.setAppraisalResult(StringUtils.isBlank(appraisalResult)?"-":appraisalResult);
        if (flag){
            //设置 是有隐患   异常的问题已经设置为需要整改了
            attBlueInspection.setHiddenDanger(1);
            attBlueInspection.setUpdateTime(new Date());
            attBlueInspection.setUpdateId(userId);
            //阶段进入移交执法大队
            projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.TRANSFER.getValue(),attBlueInspection.getProjectId());
        }else {
            //设置 成无隐患
            attBlueInspection.setHiddenDanger(0);
            attBlueInspection.setUpdateTime(new Date());
            //已结束
            attBlueInspection.setResultStatus(1);
            attBlueInspection.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse("-"));
            //查询当前巡查下的问题列表，并且将其更改为无需整改了
            List<AttBlueInspectionProblem> problems = problemMapper.selectList(Wrappers.lambdaQuery(AttBlueInspectionProblem.class)
                    .eq(AttBlueInspectionProblem::getInspectionId,inspectionId)
                    .eq(AttBlueInspectionProblem::getDelFlag,0));
            problems.forEach(p ->{
                //设置问题为无需整改
                p.setRectify(0);
                p.setUpdateId(userId);
                p.setUpdateTime(new Date());
                problemMapper.updateById(p);
            });
            //变成无问题状态，等待重新发起新监管
            projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.NO_PROBLEM.getValue(), attBlueInspection.getProjectId());
        }
        if (inspectionMapper.updateById(attBlueInspection)>0){
            return true;
        }
        return false;
    }


    @Override
    public Object inspectionList(String stage, String type,Integer pageSize,Integer pageNum,Integer result) {

        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        if (ObjectUtils.isNotEmpty(pageSize)&&ObjectUtils.isNotEmpty(pageNum)){
            PageHelper.startPage(pageNum,pageSize);
        }
        List<AttBlueInspectionResVo> attBlueInspections = inspectionMapper.selectListByCondition(userId, stage, type,result);

        return attBlueInspections;
    }


    @Override
    public Object inspectionDetail(String inspectionId) {
        //查询本次巡查信息
        AttBlueInspection attBlueInspection = inspectionMapper.selectById(inspectionId);
        //查询本次巡查问题列表信息
        List<AttBlueInspectionProblem> problems = problemMapper.selectList(Wrappers.lambdaQuery(AttBlueInspectionProblem.class)
                .eq(AttBlueInspectionProblem::getInspectionId,inspectionId)
                .eq(AttBlueInspectionProblem::getDelFlag,0));
        Map<String,Object> result = new HashMap<>(2);
        result.put("list",problems);
        result.put("inspection",attBlueInspection);

        List<AttBlueInspectionProblem> collect = problems.stream().filter(p -> p.getFileIds() != null).collect(Collectors.toList());

        AttBlueProject project = projectMapper.selectById(attBlueInspection.getProjectId());
        //文件列表
        List<SysFile> files = new ArrayList<>();
        CommonResult load = authenticationClient.load(project.getFileIds());
        if (load.getCode() == CommonResult.success().getCode()){
            files  = (List<SysFile>) load.getData();
        }

        for (AttBlueInspectionProblem problem : collect) {
            files.add(Builder.of(SysFile::new)
                    .with(SysFile::setFileUrl,problem.getFileIds())
                    .with(SysFile::setFileName,problem.getProblemDescrip())
                    .build());
        }
        result.put("fileList",files);

        return result;
    }

    @Override
    public Object caseClosed(AttBlueInspectionReqVo reqVo) {

        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("-");
        String currentUserName = JwtTokenUtil.getCurrentUserName().orElse("-");

        AttBlueInspection inspection = inspectionMapper.selectById(reqVo.getInspectionId());
        inspection.setCoordinator(currentUserName);
        inspection.setCoordinatorId(currentUserId);
        inspection.setCoordinatorFile(reqVo.getCoordinatorFile());
        inspection.setCoordinatorProduce(reqVo.getCoordinatorProduce());
        inspection.setCoordinatorTime(reqVo.getCoordinatorDate());
        inspection.setCoordinatorFileIds(reqVo.getCoordinatorFileIds());

        Integer aLong = Math.abs(Integer.parseInt(CommomUtil.betweenDays(reqVo.getCoordinatorDate(),inspection.getPatrolBeginTime()).toString()));
        //持续时间  结办时间 - 首次巡查时间
        inspection.setPendingTime(aLong);
        inspection.setCoordinatorNotify(reqVo.getCoordinatorNotify());
        //本次巡检已完成
        inspection.setResultStatus(1);

        inspection.setUpdateId(currentUserId);
        inspection.setUpdateTime(new Date());

        //需要进行通知的人员列表
//        List<SysUserResVo> coordinatorPeoples = reqVo.getCoordinatorPeoples();
//        if (CollectionUtils.isNotBlank(coordinatorPeoples)){
//            List<String> p = coordinatorPeoples.stream().filter(o->o.getUserName()!=null)
//                    .map(SysUserResVo::getUserName).collect(Collectors.toList());
//            inspection.setRectifyPeople(p.toString());
//            //发送浙政钉通知
//            String state = inspection.getStage().equals(1)?"前期项目":"项目";
//            SysUser sysUser = null;
//            if (StringUtils.isNotBlank(inspection.getCoordinatorId())){
//                 sysUser = sysUserMapper.selectById(inspection.getCoordinatorId());
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//            String phone = ObjectUtils.isNotEmpty(sysUser)? sysUser.getPhone() : "";
//            String content = "通知：关于" +  state +
//                    "的办结通知" + inspection.getProjectName() +
//                    "项目于" + sdf.format(inspection.getCoordinatorTime()) + "已完成结办。" +
//                    "结办人员：" + inspection.getCoordinator() + "。结办人电话：" + phone;
//            saveInspectionMsgInfo(coordinatorPeoples,reqVo.getInspectionId(),1,content);
//        }

        if (inspectionMapper.updateById(inspection)>0){
            //项目变为已完工阶段
            projectMapper.updateRegulatoryStatusById(RegulatoryStatusEnum.OVER.getValue(),inspection.getProjectId());
            return true;
        }
        return false;
    }


    @Override
    public Object getInspectDeptUser(String key) {

        List<DeptRespNodeVO> deptRespNodeVOS = sysUserMapper.selectDeptUserByRelations();
        List<DeptRespNodeVO> removeList = new ArrayList<>();
        for (DeptRespNodeVO deptRespNodeVO : deptRespNodeVOS) {
            if (StringUtils.isEmpty(key)){
                key = "";
            }
            List<SysUserResVo> sysUserResVos =  sysUserMapper.selectListByDeptVo(deptRespNodeVO.getId(),key);
            if (CollectionUtils.isBlank(sysUserResVos)){
                removeList.add(deptRespNodeVO);
            }else {
                deptRespNodeVO.setSysUsers(sysUserResVos);
            }
        }
        deptRespNodeVOS.removeAll(removeList);

        List<DeptRespNodeVO> result = new ArrayList<>();
        //行政区划分组
        Map<String, List<DeptRespNodeVO>> listMap = deptRespNodeVOS.stream()
                .collect(Collectors.groupingBy(DeptRespNodeVO::getAdcd, Collectors.toList()));
        Set<String> keySet = listMap.keySet();
        String areaCode = null;
        for (String adcd : keySet) {
            //不够补全 “0”
            if (12 > adcd.length()){
                char[] chars = new char[12];
                Arrays.fill(chars,'0');
                for (int i = 0; i < adcd.length(); i++) {
                    chars[i] = adcd.charAt(i);
                }
                areaCode = new String(chars);
            }else {
                areaCode = adcd;
            }
            String adnm = attAdcdBaseMaper.selectNameOfAdcd(areaCode);
            DeptRespNodeVO nodeVO = new DeptRespNodeVO();
            nodeVO.setChildren(listMap.get(adcd));
            if (StringUtils.isNotBlank(adnm)){
                nodeVO.setTitle(adnm);
            }else {
                nodeVO.setTitle("其他");
            }
            nodeVO.setAdcd(adcd);
            nodeVO.setSysUsers(new ArrayList<>());
            result.add(nodeVO);
        }
        return result;
    }


    @Override
    public void generateRectificationOrder(HttpServletResponse response, Map<String,String> inspectionId) throws IOException {

        Map<String, Object> param = new HashMap<>();
        AttBlueProject project = new AttBlueProject();
        AttBlueInspection inspection = inspectionMapper.selectById(inspectionId.get("inspectionId"));
        if (ObjectUtils.isNotEmpty(inspection)){
            project = projectMapper.selectById(inspection.getProjectId());
        }else {
            inspection = new AttBlueInspection();
        }

        param.put("projName", StringUtils.isNotBlank(project.getProjectName())? project.getProjectName(): "");
        param.put("address", StringUtils.isNotBlank(project.getAddress())?project.getAddress():"");
        param.put("riverName", StringUtils.isNotBlank(project.getRiverName())?project.getRiverName():"");
        param.put("unitName", StringUtils.isNotBlank(project.getUnitName())?project.getUnitName():"");
        param.put("inspector", StringUtils.isNotBlank(inspection.getInspector())?inspection.getInspector():"");
        SysUser sysUser = sysUserMapper.selectById(inspection.getInspectorId());
        if (ObjectUtils.isNotEmpty(sysUser)){
            param.put("inspectorPhone", StringUtils.isNotBlank(sysUser.getPhone())?sysUser.getPhone():"");
        }else {
            param.put("inspectorPhone", "");
        }

        String path = WordUtil.class.getClassLoader().getResource("./").getPath();
        XWPFDocument doc = WordUtil.generateWord(param, path + "../../src/main/resources/doc/1.docx");

        //弹出一个"文件下载"的对话框
        response.setContentType("application/octet-stream");
        //文件名为中文时会乱码，需要转一下
        response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode("项目调查表", "UTF-8") + ".docx");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        response.flushBuffer();
        doc.write(response.getOutputStream());
    }
}
