package com.ygkj.water.project.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.MtDailyTask;
import com.ygkj.project.model.MtTaskNode;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.TaskNodeEnum;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.MtDailyTaskMapper;
import com.ygkj.water.project.mapper.MtTaskNodeMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.MtDailyTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-19 9:16
 * @description
 */
@Service
public class MtDailyTaskServiceImpl implements MtDailyTaskService {

    @Resource
    private MtDailyTaskMapper mtDailyTaskMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;
    @Resource
    private MtTaskNodeMapper mtTaskNodeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MtDmDailyTaskReqVo vo) {
        MtDailyTask mtDailyTask = new MtDailyTask();
        BeanUtils.copyProperties(vo, mtDailyTask);
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        String userName = JwtTokenUtil.getCurrentUserName().orElse("");
        String taskId = idService.nextID();
        mtDailyTask.setId(taskId);
        mtDailyTask.setCreateId(userId);
        mtDailyTask.setCreator(userName);

        //创建日常任务的同时，添加隐患节点
        MtTaskNode node = new MtTaskNode();
        node.setId(idService.nextID());
        node.setTaskId(taskId);
        node.setStatus(TaskNodeEnum.PEND.getCode());
        node.setOperator(vo.getImplementUser());
        node.setSort(1);
        mtTaskNodeMapper.insert(node);

        return mtDailyTaskMapper.insert(mtDailyTask);
    }

    @Override
    public int update(MtDmDailyTaskReqVo vo) {
        MtDailyTask mtDailyTask = new MtDailyTask();
        BeanUtils.copyProperties(vo,mtDailyTask);
        mtDailyTask.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return mtDailyTaskMapper.updateById(mtDailyTask);
    }

    @Override
    public int deleteByTaskId(String id) {

        return mtDailyTaskMapper.deleteByTaskId(id);
    }

    @Override
    public PageVO<MtDailyTask> loadPage(MtDmDailyTaskReqVo vo) {

        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<MtDailyTask> list = mtDailyTaskMapper.selectList(Wrappers.lambdaQuery(MtDailyTask.class)
                .like(StringUtils.isNotBlank(vo.getProjectName()), MtDailyTask::getProjectName, vo.getProjectName())
                .likeRight(StringUtils.isNotBlank(vo.getYear()), MtDailyTask::getTm, vo.getYear())
                .eq(StringUtils.isNotBlank(vo.getProjectType()), MtDailyTask::getProjectType, vo.getProjectType())
                .eq(StringUtils.isNotBlank(vo.getId()), MtDailyTask::getId, vo.getId())
                .eq(StringUtils.isNotBlank(vo.getPrcd()), MtDailyTask::getPrcd, vo.getPrcd())
                .eq(StringUtils.isNotBlank(vo.getProcessWay()), MtDailyTask::getProcessWay, vo.getProcessWay())
                .eq(StringUtils.isNotBlank(vo.getProblemSource()), MtDailyTask::getProblemSource, vo.getProblemSource())
                .eq(ObjectUtils.isNotEmpty(vo.getHiddenDangerStatus()), MtDailyTask::getHiddenDangerStatus, vo.getHiddenDangerStatus())
                .likeRight(StringUtils.isNotBlank(vo.getDate()),MtDailyTask::getTm,vo.getDate())
                .eq(StringUtils.isNotBlank(vo.getProblemType()),MtDailyTask::getProblemType,vo.getProblemType())
                .eq(StringUtils.isNotBlank(vo.getCreateId()),MtDailyTask::getCreateId,vo.getCreateId())
                .eq(MtDailyTask::getDelFlag, 0).orderByDesc(MtDailyTask::getCreateTime));
        Calendar calendar = Calendar.getInstance();
        list.forEach( l -> {
            if (ObjectUtils.isNotNull(l.getTm())){
                calendar.setTime(l.getTm());
                l.setYear(calendar.get(Calendar.YEAR));
            }
            List<SysFile> fileList = sysFileMapper.loadByIds(l.getImgIds());
            l.setImages(fileList);
        });
        return PageUtils.getPageVO(list);
    }


}
