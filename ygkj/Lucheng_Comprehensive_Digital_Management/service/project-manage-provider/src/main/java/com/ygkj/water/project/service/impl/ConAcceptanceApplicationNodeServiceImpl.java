package com.ygkj.water.project.service.impl;

import com.ygkj.project.model.ConAcceptanceApplication;
import com.ygkj.project.model.ConAcceptanceApplicationNode;
import com.ygkj.project.model.MessageReminder;
import com.ygkj.project.vo.request.ConAcceptanceApplicationNodeReqVo;
import com.ygkj.water.project.mapper.ConAcceptanceApplicationMapper;
import com.ygkj.water.project.mapper.ConAcceptanceApplicationNodeMapper;
import com.ygkj.water.project.mapper.MessageReminderMapper;
import com.ygkj.water.project.service.ConAcceptanceApplicationNodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ConAcceptanceApplicationNodeServiceImpl implements ConAcceptanceApplicationNodeService {
    @Resource
    private ConAcceptanceApplicationNodeMapper applicationNodeMapper;
    @Resource
    private ConAcceptanceApplicationMapper applicationMapper;
    @Resource
    private MessageReminderMapper messageReminderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(ConAcceptanceApplicationNodeReqVo vo) {
        Integer maxNodeId = applicationNodeMapper.getMaxNodeId(vo.getApplicationid());
        if (maxNodeId != null && maxNodeId.equals(vo.getNodeId())){
            ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
            BeanUtils.copyProperties(vo,node);
            node.setId(vo.getNodeId());
            //当前节点为通过
            node.setStatus("3");
            applicationNodeMapper.updateById(node);
            //将验收申请更新为完成
            ConAcceptanceApplication application = new ConAcceptanceApplication();
            application.setId(vo.getApplicationid());
            application.setStatus("3");
            applicationMapper.updateById(application);
            //往消息提醒表新增提醒消息
            ConAcceptanceApplication application1 = applicationMapper.selectById(vo.getApplicationid());
            List<ConAcceptanceApplicationNode> nodes = applicationNodeMapper.getNodesByAssociationId(vo.getApplicationid());
            for (ConAcceptanceApplicationNode applicationNode : nodes) {
                MessageReminder reminder = new MessageReminder();
                reminder.setAssociationId(applicationNode.getApplicationid());
                reminder.setMessage(application1.getContractName());
                reminder.setType("合同管理");
                reminder.setReminderUser(applicationNode.getUnitReviewerId());
                reminder.setReminderTime(new Date());
                messageReminderMapper.insert(reminder);
            }
        }else {
            ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
            BeanUtils.copyProperties(vo,node);
            //当前节点为通过
            node.setStatus("3");
            node.setId(vo.getNodeId());
            applicationNodeMapper.updateById(node);
            //下一个节点
            ConAcceptanceApplicationNode afterNode = applicationNodeMapper.getAfterNode(vo.getApplicationid(),vo.getNodeId());
            //将验收申请当前节点改为下一个节点
            ConAcceptanceApplication application = applicationMapper.selectById(vo.getApplicationid());
            application.setCurrentnode(afterNode.getId());
            application.setId(vo.getApplicationid());
            //判断原来申请是否为已驳回，是，更新为审批中
            if ("2".equals(application.getStatus())){
                application.setStatus("1");
                //同时之后的审核节点不管原来是通过还是驳回，同一恢复为审批中
                applicationNodeMapper.recover(vo.getApplicationid());
            }
            applicationMapper.updateById(application);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(ConAcceptanceApplicationNodeReqVo vo) {
        ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
        BeanUtils.copyProperties(vo,node);
        node.setStatus("2");
        node.setId(vo.getNodeId());
        applicationNodeMapper.updateById(node);
        //初始节点
        ConAcceptanceApplicationNode firstNode = applicationNodeMapper.getFirstNode(vo.getApplicationid());
        firstNode.setStatus("1");
        //初始节点重置为审批中
        applicationNodeMapper.updateById(firstNode);
        //将验收申请当前节点改为初始节点
        ConAcceptanceApplication application = new ConAcceptanceApplication();
        application.setCurrentnode(firstNode.getId());
        application.setId(vo.getApplicationid());
        application.setStatus("2");
        applicationMapper.updateById(application);
    }

    @Override
    public Object loadNodes(ConAcceptanceApplicationNodeReqVo vo) {
        List<ConAcceptanceApplicationNode> applicationNodes = applicationNodeMapper.loadNodes(vo);
        return applicationNodes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reminder(ConAcceptanceApplicationNodeReqVo vo) {
        //节点提醒状态改为已提醒
        ConAcceptanceApplicationNode node = new ConAcceptanceApplicationNode();
        BeanUtils.copyProperties(vo,node);
        node.setId(vo.getNodeId());
        node.setReminderStatus("2");
        applicationNodeMapper.updateById(node);
        //往消息提醒表新增提醒消息
        ConAcceptanceApplicationNode currentNode = applicationNodeMapper.getCurrentNode(vo.getNodeId());
        ConAcceptanceApplication application = applicationMapper.selectById(currentNode.getApplicationid());
        MessageReminder reminder = new MessageReminder();
        reminder.setAssociationId(currentNode.getApplicationid());
        reminder.setMessage(application.getContractName());
        reminder.setType("合同管理");
        reminder.setReminderUser(currentNode.getUnitReviewerId());
        reminder.setReminderTime(new Date());
        messageReminderMapper.insert(reminder);
    }
}
