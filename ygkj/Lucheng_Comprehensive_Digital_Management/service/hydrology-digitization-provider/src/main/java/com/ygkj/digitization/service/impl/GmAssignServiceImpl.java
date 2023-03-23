package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.GmAssignService;
import com.ygkj.digitization.enums.AssManTaskStateEnum;
import com.ygkj.digitization.mapper.GmAssignMapper;
import com.ygkj.digitization.mapper.AttAdcdBaseMapper;
import com.ygkj.digitization.mapper.SysDeptMapper;
import com.ygkj.digitization.model.GmAssign;
import com.ygkj.digitization.vo.request.AssManAddUpdateVo;
import com.ygkj.digitization.vo.request.AssManQueryVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xq
 * @Description 任务书下达服务层实现
 * @Date 2021/2/1
 */
@Service
public class GmAssignServiceImpl implements GmAssignService {

    @Resource
    GmAssignMapper assManMapper;

    @Resource
    SnowFlake snowFlake;

    @Resource
    SysDeptMapper sysDeptMapper;

    @Resource
    AttAdcdBaseMapper attAdcdBaseMapper;

    @Override
    public PageVO<GmAssign> upperQuery(AssManQueryVo queryVo) {
        String adcd = queryVo.getAdcd();
        String itemName = queryVo.getItemName();
        Date start = null;
        Date end = null;
        if (StringUtils.isBlank(adcd)) {
            adcd = null;
        }
        if (StringUtils.isBlank(itemName)) {
            itemName = null;
        }
        if (queryVo.getYear() != null) {
            Date[] startEnd = getStartEndDateTimeOfYear(queryVo.getYear());
            start = startEnd[0];
            end = startEnd[1];
        }
        Integer paramTaskState = queryVo.getTaskState();
        List<Integer> selectParamTaskState = null;
        if (AssManTaskStateEnum.UN_SEND.getTaskStateNum().equals(paramTaskState)) {
            selectParamTaskState = Arrays.asList(AssManTaskStateEnum.UN_SEND.getTaskStateNum());
        } else if (AssManTaskStateEnum.SENDED.getTaskStateNum().equals(paramTaskState)) {
            // 已下达包括状态：已提醒、已超时、已结束
            selectParamTaskState = Arrays.asList(AssManTaskStateEnum.SENDED.getTaskStateNum(),
                    AssManTaskStateEnum.ALERTED.getTaskStateNum(),
                    AssManTaskStateEnum.OUT_OF_DATE.getTaskStateNum(),
                    AssManTaskStateEnum.FINISHED.getTaskStateNum());
        }
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        return PageUtils.getPageVO(assManMapper.selectByAdcdItemNameExetime(adcd, itemName, start, end, selectParamTaskState,queryVo.getFileName()));
    }

    @Override
    public GmAssign upperQueryOneById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return assManMapper.selectById(id);
        } else {
            return null;
        }
    }

    @Override
    public int upperAdd(AssManAddUpdateVo addUpdateVo) {
        GmAssign assMan = new GmAssign();
        assMan.setId(String.valueOf(snowFlake.nextId()));
//        assMan.setItemName(addUpdateVo.getItemName());
//        assMan.setTaskRequest(addUpdateVo.getTaskRequest());
//        assMan.setPlanFinishTimes(addUpdateVo.getPlanFinishTimes());
//        assMan.setExeStartTime(addUpdateVo.getExeStartTime());
//        assMan.setExeEndTime(addUpdateVo.getExeEndTime());
//        assMan.setExeDeptId(addUpdateVo.getExeDeptId());
//        assMan.setTaskState(AssManTaskStateEnum.UN_SEND.getTaskStateNum());
//        String[] deptNameAdcd=getDeptNameAdcdByDeptId(addUpdateVo.getExeDeptId());
//        assMan.setExeDeptName(deptNameAdcd[0]);
        assMan.setAdcd(addUpdateVo.getAdcd());
        assMan.setAdnm(attAdcdBaseMapper.selectAdnmByAdcd(addUpdateVo.getAdcd()));
        assMan.setDelFlag(0);
        assMan.setFileUrl(addUpdateVo.getFileUrl());
        assMan.setFileName(addUpdateVo.getFileName());
        assMan.setUploadName(getCurrentUserName());
        assMan.setCreateTime(new Date());
        return assManMapper.insertOne(assMan);
    }

    private String getCurrentUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userName = null;
        if (StringUtils.isNotBlank(token)) {
            userName = JwtTokenUtil.getUserName(token);
        }
        return userName;
    }


    @Override
    public int upperUpdate(AssManAddUpdateVo addUpdateVo) {
        GmAssign assMan = assManMapper.selectById(addUpdateVo.getId());
//        assMan.setItemName(addUpdateVo.getItemName());
//        assMan.setTaskRequest(addUpdateVo.getTaskRequest());
//        assMan.setPlanFinishTimes(addUpdateVo.getPlanFinishTimes());
//        assMan.setExeStartTime(addUpdateVo.getExeStartTime());
//        assMan.setExeEndTime(addUpdateVo.getExeEndTime());
//        assMan.setExeDeptId(addUpdateVo.getExeDeptId());
//        String[] deptNameAdcd=getDeptNameAdcdByDeptId(addUpdateVo.getExeDeptId());
//        assMan.setExeDeptName(deptNameAdcd[0]);
        assMan.setAdcd(addUpdateVo.getAdcd());
        assMan.setAdnm(attAdcdBaseMapper.selectAdnmByAdcd(assMan.getAdcd()));
        assMan.setFileUrl(addUpdateVo.getFileUrl());
        assMan.setFileName(addUpdateVo.getFileName());
        assMan.setUploadName(getCurrentUserName());
        return assManMapper.update(assMan);
    }

    @Override
    public int upperDeleteById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return assManMapper.virtualDeleteById(id);
        } else {
            return 0;
        }
    }

    @Override
    public int sendOrder(String id) {
        if (StringUtils.isNotBlank(id)) {
            return assManMapper.updateTaskStateFromState(AssManTaskStateEnum.UN_SEND.getTaskStateNum(),
                    AssManTaskStateEnum.SENDED.getTaskStateNum(), id);
        } else {
            return assManMapper.updateTaskStateFromState(AssManTaskStateEnum.UN_SEND.getTaskStateNum(),
                    AssManTaskStateEnum.SENDED.getTaskStateNum(), id);
        }
    }

    @Override
    public int syncTask(Integer year) {
        Date[] startEnd = getStartEndDateTimeOfYear(year);
        Date start = startEnd[0];
        Date end = startEnd[1];
        List<GmAssign> tempVal = assManMapper.selectByAdcdItemNameExetime(null, null, start, end, null,null);
        if (CollectionUtils.isBlank(tempVal)) {
            return 0;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int thisYear=calendar.get(Calendar.YEAR);
        for (GmAssign temp : tempVal) {
            temp.setId(String.valueOf(snowFlake.nextId()));
            calendar.setTime(temp.getExeStartTime());
            calendar.set(Calendar.YEAR,thisYear);
            temp.setExeStartTime(calendar.getTime());
            calendar.setTime(temp.getExeEndTime());
            calendar.set(Calendar.YEAR,thisYear);
            temp.setExeEndTime(calendar.getTime());
            temp.setTaskState(AssManTaskStateEnum.UN_SEND.getTaskStateNum());
            temp.setFinishTime(null);
            temp.setRealFinishTimes(null);
        }
        return assManMapper.insertMany(tempVal);
    }

    @Override
    public int alertTask(String id) {
        if (StringUtils.isNotBlank(id)) {
            // TODO 这里要提示
            return assManMapper.updateTaskStateFromState(AssManTaskStateEnum.SENDED.getTaskStateNum(),
                    AssManTaskStateEnum.ALERTED.getTaskStateNum(), id);
        }
        return 0;
    }

    private Date[] getStartEndDateTimeOfYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date end = calendar.getTime();
        return new Date[]{start, end};
    }

    private String[] getDeptNameAdcdByDeptId(String deptId) {
        if (StringUtils.isNotBlank(deptId)) {
            Map resultMap = sysDeptMapper.selectDeptNameAdcdByDeptId(deptId);
            if (CollectionUtils.isNotBlank(resultMap)){
                String adcd=(String)resultMap.getOrDefault("adcd",StringUtils.EMPTY);
                String deptName=(String)resultMap.getOrDefault("name",StringUtils.EMPTY);
                return new String[]{deptName, adcd};
            }
        }
        return new String[]{StringUtils.EMPTY, StringUtils.EMPTY};
    }
}
