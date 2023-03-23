package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.ConAcceptanceApplicationReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.MessageReminderEnum;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.ConAcceptanceApplicationMapper;
import com.ygkj.water.project.mapper.ConAcceptanceApplicationNodeMapper;
import com.ygkj.water.project.mapper.MessageReminderMapper;
import com.ygkj.water.project.mapper.ProfessionalCompanyMapper;
import com.ygkj.water.project.service.ConAcceptanceApplicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConAcceptanceApplicationServiceImpl implements ConAcceptanceApplicationService {
    @Resource
    private ConAcceptanceApplicationMapper applicationMapper;
    @Resource
    private ConAcceptanceApplicationNodeMapper applicationNodeMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserFeignClient userFeignClient;
    @Autowired
    private ProfessionalCompanyMapper companyMapper;
    @Autowired
    private MessageReminderMapper reminderMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ConAcceptanceApplicationReqVo vo) {
        //新增验收申请，同时获取到申请id
        ConAcceptanceApplication application = new ConAcceptanceApplication();
        BeanUtils.copyProperties(vo,application);
        application.setId(idService.nextID());
        application.setYear(DateUtils.format(new Date(),"yyyy"));
        application.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        application.setStatus("1");
        applicationMapper.insert(application);
        int sort = 1;
        //新增申请节点
        if (StringUtils.isNotBlank(application.getPropertyUnitId())){
            ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
            node.setSort(sort);
            node.setStatus("3");
            node.setApplicationid(application.getId());
            if (StringUtils.isBlank(application.getPropertyUnitReviewerId())){
                throw new RuntimeException("请选择物业单位责任人");
            }
            node.setUnitId(application.getPropertyUnitId());
            node.setUnitReviewerId(application.getPropertyUnitReviewerId());
            node.setComments(application.getProblem());
            applicationNodeMapper.insert(node);
            sort++;
        }
        if (StringUtils.isNotBlank(application.getRunUnitId())){
            ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
            node.setSort(sort);
            node.setStatus("1");
            node.setApplicationid(application.getId());
            if (StringUtils.isBlank(application.getRunUnitReviewerId())){
                throw new RuntimeException("请选择运行管理单位审批人");
            }
            node.setUnitId(application.getRunUnitId());
            node.setUnitReviewerId(application.getRunUnitReviewerId());
            applicationNodeMapper.insert(node);
            sort++;
            //更新验收申请当前节点
            application.setCurrentnode(node.getId());
            applicationMapper.updateById(application);
        }
        if (StringUtils.isNotBlank(application.getWaterAdministrationUnitId())){
            ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
            node.setSort(sort);
            node.setStatus("1");
            node.setApplicationid(application.getId());
            if (StringUtils.isBlank(application.getWaterAdministrationUnitReviewerId())){
                throw new RuntimeException("请选择水行政主管部门审批人");
            }
            node.setUnitId(application.getWaterAdministrationUnitId());
            node.setUnitReviewerId(application.getWaterAdministrationUnitReviewerId());
            applicationNodeMapper.insert(node);
            sort++;
            //如果只有水行政审批人，没有运行单位审批人，更新验收申请当前节点
            if (ObjectUtils.isEmpty(application.getCurrentnode())){
                application.setCurrentnode(node.getId());
                applicationMapper.updateById(application);
            }
        }
    }

    @Override
    public void update(ConAcceptanceApplicationReqVo vo) {
        ConAcceptanceApplication application = new ConAcceptanceApplication();
        BeanUtils.copyProperties(vo,application);
        application.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        applicationMapper.updateById(application);
    }

    @Override
    public Object loadPage(ConAcceptanceApplicationReqVo vo) {
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("");
        vo.setCurrentUserId(currentUserId);
        if ("admin".equals(vo.getCurrentUserId())){
            vo.setIsAdmin(true);
        }else {
            vo.setIsAdmin(false);
        }
        SysUser sysUser = companyMapper.selectWyLeader(currentUserId);
        if (ObjectUtils.isNotEmpty(sysUser)){
            vo.setPropertyUnitId(sysUser.getDeptId());
            vo.setIsWyLeader(true);
        }else {
            vo.setIsWyLeader(false);
        }
        String companyCodes = "";
        if (StringUtils.isNotBlank(vo.getPropertyUnitName())){
            QueryWrapper<ProfessionalCompany> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("company_name",vo.getPropertyUnitName());
            List<ProfessionalCompany> companies = companyMapper.selectList(queryWrapper);
            if (companies.size() > 0){
                List<String> collect = companies.stream().map(ProfessionalCompany::getId).collect(Collectors.toList());
                companyCodes = String.join(",", collect);
            }
        }
        vo.setPropertyUnitReviewerId(companyCodes);
        List<ConAcceptanceApplication> loadPage = new ArrayList<>();
        if (StringUtils.isBlank(vo.getType())){
            List<ConAcceptanceApplication> applications1 = applicationMapper.loadPage1(vo);
            loadPage.addAll(applications1);
            List<ConAcceptanceApplication> applications2 = applicationMapper.loadPage2(vo);
            loadPage.addAll(applications2);
        }
        vo.page();
        if ("1".equals(vo.getType())){
            loadPage = applicationMapper.loadPage1(vo);
        }else if ("2".equals(vo.getType())){
            loadPage = applicationMapper.loadPage2(vo);
        }
        PageVO<ConAcceptanceApplication> pageVO = PageUtils.getPageVO(loadPage);
        List<ConAcceptanceApplication> list = pageVO.getList();
        list.forEach(m -> {
            List<SysFile> contractFiles = getFileList(m.getContractFile());
            m.setContractFiles(contractFiles);
            List<SysFile> acceptanceFiles = getFileList(m.getAcceptanceFile());
            m.setAcceptanceFiles(acceptanceFiles);
            //获取当前节点的审核人名称和id
            ConAcceptanceApplicationNode currentNode = applicationNodeMapper.getCurrentNode(m.getCurrentnode());
            m.setCurrentPeople(currentNode.getUnitReviewerName());
            m.setCurrentPeopleId(currentNode.getUnitReviewerId());
        });
        list = list.stream().sorted(Comparator.comparing(ConAcceptanceApplication::getCreateTime).reversed()).collect(Collectors.toList());
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        ConAcceptanceApplication application = new ConAcceptanceApplication();
        application.setId(id);
        application.setDelFlag(1);
        applicationMapper.updateById(application);
    }

    @Override
    public Object count(ConAcceptanceApplicationReqVo vo) {
        JSONObject object = new JSONObject();
        vo.setCurrentUserId(JwtTokenUtil.getCurrentUserId().orElse(""));
        List<ConAcceptanceApplication> loadPage = new ArrayList<>();
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("");
        vo.setCurrentUserId(currentUserId);
        if ("admin".equals(vo.getCurrentUserId())){
            vo.setIsAdmin(true);
        }else {
            vo.setIsAdmin(false);
        }
        SysUser sysUser = companyMapper.selectWyLeader(currentUserId);
        if (ObjectUtils.isNotEmpty(sysUser)){
            vo.setPropertyUnitId(sysUser.getDeptId());
            vo.setIsWyLeader(true);
        }else {
            vo.setIsWyLeader(false);
        }
        if (StringUtils.isBlank(vo.getType())){
            List<ConAcceptanceApplication> applications1 = applicationMapper.loadPage1(vo);
            loadPage.addAll(applications1);
            List<ConAcceptanceApplication> applications2 = applicationMapper.loadPage2(vo);
            loadPage.addAll(applications2);
        }
        if ("1".equals(vo.getType())){
            loadPage = applicationMapper.loadPage1(vo);
        }else if ("2".equals(vo.getType())){
            loadPage = applicationMapper.loadPage2(vo);
        }
        if (loadPage.size() > 0){
            //验收申请总数
            object.put("totalNum",loadPage.size());
            //验收申请审批中
            List<ConAcceptanceApplication> collect1 = loadPage.stream().filter(o -> "1".equals(o.getStatus())).collect(Collectors.toList());
            object.put("approveNum",collect1.size());
            //验收申请已驳回
            List<ConAcceptanceApplication> collect2 = loadPage.stream().filter(o -> "2".equals(o.getStatus())).collect(Collectors.toList());
            object.put("rejectNum",collect2.size());
            //验收申请已通过
            List<ConAcceptanceApplication> collect3 = loadPage.stream().filter(o -> "3".equals(o.getStatus())).collect(Collectors.toList());
            object.put("passNum",collect3.size());
        }else {
            object.put("totalNum",0);
            object.put("approveNum",0);
            object.put("rejectNum",0);
            object.put("passNum",0);
        }

        return object;
    }

    @Override
    public Object appCount() {

        JSONObject object = new JSONObject();
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        //我的申请
        ConAcceptanceApplicationReqVo vo1 = new ConAcceptanceApplicationReqVo();
        vo1.setType("1");
        vo1.setCurrentUserId(userId);
        if ("admin".equals(vo1.getCurrentUserId())){
            vo1.setIsAdmin(true);
        }else {
            vo1.setIsAdmin(false);
        }
        SysUser sysUser = companyMapper.selectWyLeader(userId);
        if (ObjectUtils.isNotEmpty(sysUser)){
            vo1.setPropertyUnitId(sysUser.getDeptId());
            vo1.setIsWyLeader(true);
        }else {
            vo1.setIsWyLeader(false);
        }
        List<ConAcceptanceApplication> loadPage1 = applicationMapper.loadPage1(vo1);
        //我的审批
        ConAcceptanceApplicationReqVo vo2 = new ConAcceptanceApplicationReqVo();
        vo2.setType("2");
        vo2.setCurrentUserId(userId);
        if ("admin".equals(vo2.getCurrentUserId())){
            vo2.setIsAdmin(true);
        }else {
            vo2.setIsAdmin(false);
        }
        if (ObjectUtils.isNotEmpty(sysUser)){
            vo2.setPropertyUnitId(sysUser.getDeptId());
            vo2.setIsWyLeader(true);
        }else {
            vo2.setIsWyLeader(false);
        }
        List<ConAcceptanceApplication> loadPage2 = applicationMapper.loadPage2(vo2);
        //消息提醒
        Integer messageNum = reminderMapper.findMsgTotal(userId, MessageReminderEnum.CONTRACT.getValue(),MessageReminderEnum.UNREAD.getValue());
        object.put("applicationNum",loadPage1.size());
        object.put("approvalNum",loadPage2.size());
        object.put("progressNum",loadPage1.size() + loadPage2.size());
        object.put("messageNum",messageNum);
        return object;
    }

    private List<SysFile> getFileList(String fileIds){

        CommonResult load = userFeignClient.load(fileIds);
        if (CommonResult.success().getCode() == load.getCode()){
            List<SysFile>  files = (List<SysFile>) load.getData();
            return files;
        }else {
            return Arrays.asList();
        }
    }
}
