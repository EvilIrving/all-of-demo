package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.AssessmentInspectionReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.AssessmentInspectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentInspectionServiceImpl extends ServiceImpl<AssessmentInspectionMapper,AssessmentInspection> implements AssessmentInspectionService {
    @Resource
    private AssessmentInspectionMapper inspectionMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private DeptCustomMapper deptCustomMapper;
    @Resource
    private ProfessionalCompanyMapper companyMapper;
    @Resource
    private MessageReminderMapper messageReminderMapper;
    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(AssessmentInspectionReqVo vo) {
        AssessmentInspection inspection = new AssessmentInspection();
        BeanUtils.copyProperties(vo,inspection);
        inspection.setId(idService.nextID());
        if (StringUtils.isBlank(inspection.getSupervisionStatus())){
            inspection.setSupervisionStatus("1");
        }
        inspection.setHandlingStatus("1");
        inspection.setArchivingStatus("1");
        inspection.setEvaluationStatus("1");
        inspection.setYear(DateUtils.format(new Date(),"yyyy"));
        inspection.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        inspectionMapper.insert(inspection);
        //已督促就向责任人发消息
        if ("2".equals(vo.getSupervisionStatus())){
            if (StringUtils.isNotBlank(vo.getResponsibleUnit())){
                AssessmentInspectionReqVo reqVo = new AssessmentInspectionReqVo();
                reqVo.setId(inspection.getId());
                List<AssessmentInspection> load = inspectionMapper.load(reqVo);
                AssessmentInspection inspection1 = load.get(0);
                MessageReminder reminder = new MessageReminder();
                reminder.setAssociationId(inspection.getId());
                reminder.setMessage(inspection1.getProjectName()+"："+inspection1.getProblems());
                reminder.setType("督办检查");
                reminder.setReminderUser(vo.getResponsibleUnit());
                reminder.setReminderTime(new Date());
                messageReminderMapper.insert(reminder);
            }else {
                throw new RuntimeException("请选择责任人!");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AssessmentInspectionReqVo vo) {
        AssessmentInspection inspection = new AssessmentInspection();
        BeanUtils.copyProperties(vo,inspection);
        inspection.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        inspectionMapper.updateById(inspection);
    }

    @Override
    public Object load(AssessmentInspectionReqVo vo) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        if (!"admin".equals(userId)){
            vo.setCurrentUserId(userId);
        }
        vo.page();
        if("2".equals(vo.getType())){
            vo.setArchivingStatus("1");
        }else if("3".equals(vo.getType())){
            vo.setArchivingStatus("1");
        }else if("4".equals(vo.getType())){
            vo.setArchivingStatus("2");
        }
        List<AssessmentInspection>  inspections = inspectionMapper.load(vo);
        PageVO<AssessmentInspection> pageVO = PageUtils.getPageVO(inspections);
        List<AssessmentInspection> list = pageVO.getList();
        list.forEach(m->{
            List<SysFile> fileList1 = sysFileMapper.loadByIds(m.getProblemsPicture());
            m.setProblemsPictureList(fileList1);
            List<SysFile> fileList2 = sysFileMapper.loadByIds(m.getHandlingPicture());
            m.setHandlingPictureList(fileList2);
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        AssessmentInspection inspection = new AssessmentInspection();
        inspection.setId(id);
        inspection.setDelFlag(1);
        inspectionMapper.updateById(inspection);
    }

    @Override
    public void export(AssessmentInspectionReqVo vo, HttpServletResponse response) {
        List<AssessmentInspection> list = inspectionMapper.load(vo);

        ExportUtils.exportExcel(list,"督察检查  ",
                new String[]{"工程名称","检查时间","存在问题","上报人","责任单位","责任人","处理说明"},
                new String[]{"projectName","inspectionTime","problems","createName","responsibleUnitName","responsiblePeopleName","handlingInstruction"},response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handing(AssessmentInspectionReqVo vo,String handType) {
        //督办
        if("1".equals(handType)){
            AssessmentInspection inspection = new AssessmentInspection();
            inspection.setId(vo.getId());
            inspection.setSupervisionStatus("2");
            inspectionMapper.updateById(inspection);
            //已督促就向责任人发消息
            if ("2".equals(inspection.getSupervisionStatus())){
                AssessmentInspection inspection1 = inspectionMapper.getById(vo.getId());
                if (StringUtils.isNotBlank(inspection1.getResponsibleUnit())){
                    MessageReminder reminder = new MessageReminder();
                    reminder.setAssociationId(inspection.getId());
                    reminder.setMessage(inspection1.getProjectName()+"："+inspection1.getProblems());
                    reminder.setType("督办检查");
                    reminder.setReminderUser(vo.getResponsibleUnit());
                    reminder.setReminderTime(new Date());
                    messageReminderMapper.insert(reminder);
                }else {
                    throw new RuntimeException("未选择责任人,消息无法发送!");
                }
            }
            return;
        }
        AssessmentInspection selectById = inspectionMapper.selectById(vo.getId());
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        if (!userId.equals(selectById.getResponsiblePeopleName())){
            throw new RuntimeException("当前用户不是责任人,没有权限操作!");
        }
        //处理和归档
        if ("2".equals(handType)){
            if ("2".equals(selectById.getSupervisionStatus())){
                AssessmentInspection inspection = new AssessmentInspection();
                BeanUtils.copyProperties(vo,inspection);
                inspection.setHandlingStatus("2");
                inspectionMapper.updateById(inspection);
                return;
            }else {
                throw new RuntimeException("该任务尚未督促,无法更新为已处理！");
            }
        }else if ("3".equals(handType)){
            if ("2".equals(selectById.getHandlingStatus())){
                AssessmentInspection inspection = new AssessmentInspection();
                inspection.setId(vo.getId());
                inspection.setArchivingStatus("2");
                inspectionMapper.updateById(inspection);
                return;
            }else {
                throw new RuntimeException("该任务尚未处理，无法归档！");
            }
        }
    }

    @Override
    public Object getResponsibleUnits() {
        List<String> result = new ArrayList<>();
        List<SysDept> deptList = deptCustomMapper.getDeptList("67011533,67038494");
        if (CollectionUtils.isNotBlank(deptList)){
            List<String> collect = deptList.stream().map(SysDept::getName).collect(Collectors.toList());
            result.addAll(collect);
        }
        List<ProfessionalCompany> companies = companyMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isNotBlank(companies)){
            List<String> collect = companies.stream().map(ProfessionalCompany::getCompanyName).collect(Collectors.toList());
            result.addAll(collect);
        }
        result = result.stream().distinct().collect(Collectors.toList());
        return result;
    }

    @Override
    public Object appCount(AssessmentInspectionReqVo vo) {
        JSONObject object = new JSONObject();
        vo.setCurrentUserId(JwtTokenUtil.getCurrentUserId().orElse(""));
        List<AssessmentInspection> load = inspectionMapper.load(vo);

        List<AssessmentInspection> load1 = load.stream().filter(o->"1".equals(o.getArchivingStatus())).collect(Collectors.toList());

        List<AssessmentInspection> load2 = load.stream().filter(o->"2".equals(o.getArchivingStatus())).collect(Collectors.toList());


        //督察检查总数
        object.put("inspectNum",load.size());
        //督察督办总数
        object.put("superviseNum",load1.size());
        //进度查询总数
        object.put("progressNum",load1.size());
        //归档成果
        object.put("filedNum",load2.size());

        return object;
    }
}
