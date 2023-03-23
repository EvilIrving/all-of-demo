package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.MessageReminder;
import com.ygkj.project.model.MtDailyTask;
import com.ygkj.project.model.MtTaskNode;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.project.vo.request.MtTaskNodeReqVo;
import com.ygkj.project.vo.response.ThreeChargeResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.enums.HiddenDangerStatusEnum;
import com.ygkj.water.project.enums.MessageReminderEnum;
import com.ygkj.water.project.enums.TaskNodeEnum;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.MessageReminderMapper;
import com.ygkj.water.project.mapper.MtDailyTaskMapper;
import com.ygkj.water.project.mapper.MtTaskNodeMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.EngineeringSupervisionService;
import com.ygkj.water.project.service.HiddenDangerService;
import com.ygkj.water.project.service.MtDailyTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.annotation.processing.Messager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-04-21 17:43
 * @description
 */
@Service
public class HiddenDangerServiceImpl implements HiddenDangerService {

    @Resource
    private MtTaskNodeMapper mtTaskNodeMapper;
    @Resource
    private MtDailyTaskMapper mtDailyTaskMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;
    @Resource
    private EngineeringSupervisionService engineeringSupervisionService;
    @Resource
    private MessageReminderMapper messageReminderMapper;
    @Resource
    private MtDailyTaskService mtDailyTaskService;


    @Override
    public Object getHiddenDangerDetail(String id) {

        MtDailyTask mtDailyTask = mtDailyTaskMapper.selectById(id);

        MtTaskNode node = new MtTaskNode();
        node.setSort(0);
        node.setInfo(mtDailyTask);
        node.setOperator(mtDailyTask.getCreator());
        node.setOpinion(mtDailyTask.getTaskDescribe());
        node.setOperator(mtDailyTask.getCreator());
        List<MtTaskNode> mtTaskNodes =  mtTaskNodeMapper.selectList(Wrappers.lambdaQuery(MtTaskNode.class)
                .eq(MtTaskNode::getTaskId, id)
                .eq(MtTaskNode::getDelFlag, 0));

        mtTaskNodes.add(node);
        mtTaskNodes = mtTaskNodes.stream().sorted(Comparator.comparing(MtTaskNode::getSort)).collect(Collectors.toList());
        setFile(mtTaskNodes);
        return mtTaskNodes;
    }

    public void setFile(List<MtTaskNode> mtTaskNodes){

        for (MtTaskNode mtTaskNode : mtTaskNodes) {
            List<SysFile> afterLoad = new ArrayList<>();
            if (StringUtils.isNotBlank(mtTaskNode.getImagesAfter())){
                afterLoad = sysFileMapper.loadByIds(mtTaskNode.getImagesAfter());
            }
            mtTaskNode.setImageAfterList(afterLoad);
            List<SysFile> beforeLoad = new ArrayList<>();
            if (StringUtils.isNotBlank(mtTaskNode.getImagesBefore())){
                beforeLoad = sysFileMapper.loadByIds(mtTaskNode.getImagesBefore());
            }
            mtTaskNode.setImageBeforeList(beforeLoad);
            List<SysFile> fileLoad = new ArrayList<>();
            if (StringUtils.isNotBlank(mtTaskNode.getFileIds())){
                fileLoad = sysFileMapper.loadByIds(mtTaskNode.getFileIds());
            }
            mtTaskNode.setFileList(fileLoad);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object implementSubmit(MtTaskNodeReqVo reqVo) {

        if (ObjectUtils.isEmpty(reqVo.getSort())){
            throw new RuntimeException("参数缺失");
        }
        Date date = new Date();
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("");
        MtDailyTask mtDailyTask = mtDailyTaskMapper.selectById(reqVo.getTaskId());
        if (!currentUserId.equals(mtDailyTask.getImplementUserId())){
            throw new RuntimeException("非处理人员不可进行处理");
        }

        reqVo.setOperator(JwtTokenUtil.getCurrentUserName().orElse(""));
        MtTaskNode node = new MtTaskNode();
        BeanUtils.copyProperties(reqVo, node);
        node.setUpdateTime(date);
        node.setUpdateId(currentUserId);
        node.setOperatorId(currentUserId);
        node.setStatus(TaskNodeEnum.AUDIT.getCode());
        mtTaskNodeMapper.updateById(node);

        //同时如果不是归档（审批通过）添加下一个节点的信息。
        MtTaskNode nextNode = new MtTaskNode();
        nextNode.setId(idService.nextID());
        nextNode.setTaskId(reqVo.getTaskId());
        nextNode.setSort(reqVo.getSort() + 1);
        nextNode.setStatus(TaskNodeEnum.REVIEW.getCode());
        nextNode.setCreateId(currentUserId);
        nextNode.setCreateTime(date);
        nextNode.setOperator(mtDailyTask.getCreator());
        nextNode.setOperatorId(mtDailyTask.getCreateId());
        mtTaskNodeMapper.insert(nextNode);
        //更新任务状态
        mtDailyTaskMapper.updateStatus(reqVo.getTaskId(), HiddenDangerStatusEnum.EXECUTION.getCode());
        return true;
    }

    @Override
    public Object submitProgressInfo(MtTaskNodeReqVo reqVo) {

        if (ObjectUtils.isEmpty(reqVo.getSort())){
            throw new RuntimeException("参数缺失");
        }
        if (! (TaskNodeEnum.PASS.getCode().equals(reqVo.getStatus()) || TaskNodeEnum.REJECT.getCode().equals(reqVo.getStatus()) ) ){
            throw new RuntimeException("参数异常");
        }
        Date date = new Date();
        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("");

        if (!judgeWaterManage(currentUserId)){
            throw new RuntimeException("非水管单位用户无法审批");
        }
        MtTaskNode node = new MtTaskNode();
        BeanUtils.copyProperties(reqVo, node);
        node.setUpdateTime(date);
        node.setUpdateId(currentUserId);
        mtTaskNodeMapper.updateById(node);
        //如果是已通过，直接进行归档
        MtTaskNode nextNode = new MtTaskNode();
        nextNode.setId(idService.nextID());
        nextNode.setTaskId(reqVo.getTaskId());
        nextNode.setSort(reqVo.getSort() + 1);
        nextNode.setCreateId(currentUserId);
        nextNode.setCreateTime(date);
        if (TaskNodeEnum.PASS.getCode().equals(reqVo.getStatus())){
            nextNode.setStatus(TaskNodeEnum.ARCHIVE.getCode());
            //结办抄送人员名单  发起人、处理人、审核人
            List<MtTaskNode> mtTaskNodes = mtTaskNodeMapper.selectList(Wrappers.lambdaQuery(MtTaskNode.class)
                    .eq(MtTaskNode::getTaskId, node.getTaskId()).groupBy(MtTaskNode::getOperator));
            String cc = mtTaskNodes.stream().map(MtTaskNode::getOperator).collect(Collectors.joining(","));
            nextNode.setOperator(cc);
            //抄送消息
            for (MtTaskNode mtTaskNode : mtTaskNodes) {
                addMessageReminder(node.getTaskId(),"您有一条维修养护的流程已归档",mtTaskNode.getOperatorId());
            }
            mtDailyTaskMapper.updateStatus(reqVo.getTaskId(), HiddenDangerStatusEnum.ALREADY.getCode());
        }else if (TaskNodeEnum.REJECT.getCode().equals(reqVo.getStatus())){
            nextNode.setStatus(TaskNodeEnum.PEND.getCode());
            MtDailyTask mtDailyTask = mtDailyTaskMapper.selectById(reqVo.getTaskId());
            nextNode.setOperator(mtDailyTask.getImplementUser());
            nextNode.setOperatorId(mtDailyTask.getImplementUserId());
        }
        mtTaskNodeMapper.insert(nextNode);

        return true;
    }


    public boolean judgeWaterManage(String userId){

        String id = mtTaskNodeMapper.judgeWaterManageUser(userId);
        if (StringUtils.isNotBlank(id)){
            return true;
        }
        return false;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendNoticeMsg(String id) {

        List<MtTaskNode> mtTaskNodes = mtTaskNodeMapper.selectList(Wrappers.lambdaQuery(MtTaskNode.class)
                .eq(MtTaskNode::getTaskId, id)
                .and( mt -> mt.eq(MtTaskNode::getStatus,TaskNodeEnum.PEND.getCode())
                        .or().eq(MtTaskNode::getStatus,TaskNodeEnum.REVIEW.getCode())));
        if (CollectionUtils.isEmpty(mtTaskNodes) || mtTaskNodes.size() > 1){
            throw new RuntimeException("待办节点为空或者超过一个");
        }
        //获取待提醒节点
        MtTaskNode node = mtTaskNodes.get(0);
        //获取提醒人  | 待处理查询处理人，待审核获取 六个责任人中的 技术责任人 和 水行政部门负责人
        MtDailyTask mtDailyTask = mtDailyTaskMapper.selectById(id);

        if (node.getSort().equals(TaskNodeEnum.PEND.getCode())){
            addMessageReminder(mtDailyTask.getId(),"你有一条维修养护的待办提醒",mtDailyTask.getImplementUserId());
        }else {
            mtDailyTask.getPrcd();
            List<ThreeChargeResVo> threeChargeResVos = engineeringSupervisionService.threeCharge(mtDailyTask.getPrcd());
            //获取手机号码，根据手机号码进行匹配用户
            List<String> phones = threeChargeResVos.stream().filter(t -> "水行政部门负责人".equals(t.getType()) || "技术责任人".equals(t.getType())).map(t -> t.getPhone()).collect(Collectors.toList());
            for (String phone : phones) {
                String userId = mtDailyTaskMapper.findUserIdByPhone(phone);
                if (StringUtils.isNotBlank(userId)){
                    addMessageReminder(mtDailyTask.getId(),"你有一条维修养护的待办提醒",userId);
                }
            }
        }
        //更新节点提醒状态
        node.setNotice(1);
        node.setUpdateTime(new Date());
        if (mtTaskNodeMapper.updateById(node) > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 维修养护 消息提醒
     * @param associationId
     * @param content
     * @param reminderUserId
     */
    public void addMessageReminder(String associationId,String content,String reminderUserId){

        MessageReminder messageReminder = new MessageReminder();
        messageReminder.setType(MessageReminderEnum.HIDDEN_DANGER.getValue());
        messageReminder.setAssociationId(associationId);
        //消息提醒内容
        messageReminder.setMessage(content);
        messageReminder.setReminderTime(new Date());
        messageReminder.setReminderUser(reminderUserId);
        messageReminderMapper.insert(messageReminder);

    }



    @Override
    public List<MessageReminder> msgList() {

        String currentUserId = JwtTokenUtil.getCurrentUserId().orElse("");
        List<MessageReminder> messageReminders = messageReminderMapper.selectList(Wrappers.lambdaQuery(MessageReminder.class)
                .eq(MessageReminder::getReminderUser, currentUserId)
                .eq(MessageReminder::getType,MessageReminderEnum.HIDDEN_DANGER.getValue())
                .orderByDesc(MessageReminder::getReminderTime));

        return messageReminders;
    }

    @Override
    public boolean msgRead(String id) {

        MessageReminder reminder = new MessageReminder();
        reminder.setStatus(MessageReminderEnum.READ.getValue());
        int update = messageReminderMapper.update(Builder.of(MessageReminder::new).with(MessageReminder::setStatus, MessageReminderEnum.READ.getValue()).build(), Wrappers.lambdaQuery(MessageReminder.class).eq(MessageReminder::getAssociationId, id));

        return update > 0 ;
    }


    @Override
    public JSONObject hiddenAppTotal() {

        JSONObject result = new JSONObject();
        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        //总数量
        MtDmDailyTaskReqVo vo = new MtDmDailyTaskReqVo();
//        vo.setCreateId(userId);
        PageVO<MtDailyTask> approveTotal = mtDailyTaskService.loadPage(vo);
        result.put("approveTotal",approveTotal.getTotalRows());
        //消息数量
        Integer msgTotal = messageReminderMapper.findMsgTotal(userId, MessageReminderEnum.HIDDEN_DANGER.getValue(),MessageReminderEnum.UNREAD.getValue());
        result.put("msgTotal",msgTotal);
        //待办数量
        if (judgeWaterManage(userId)){
            PageVO<MtDailyTask> mtDailyTaskPageVO = mtDailyTaskService.loadPage(vo);
            Integer pendTotal = mtDailyTaskPageVO.getTotalRows().intValue();
            result.put("pendTotal",pendTotal);
        }else {
            result.put("pendTotal",0);
        }
        result.put("progess",approveTotal.getTotalRows());
        return result;
    }
}
