package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.GmBusinessService;
import com.ygkj.digitization.enums.BusManTaskStateEnum;
import com.ygkj.digitization.mapper.AttAdcdBaseMapper;
import com.ygkj.digitization.mapper.GmBusinessMapper;
import com.ygkj.digitization.mapper.SysDeptMapper;
import com.ygkj.digitization.model.GmBusiness;
import com.ygkj.digitization.vo.request.BusManAddUpdateVo;
import com.ygkj.digitization.vo.request.BusManQueryVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xq
 * @Description 业务管理服务层实现
 * @Date 2021/2/1
 */
@Service
public class GmBusinessServiceImpl implements GmBusinessService {

    @Resource
    GmBusinessMapper busManMapper;

    @Resource
    SnowFlake snowFlake;

    @Resource
    SysDeptMapper sysDeptMapper;

    @Resource
    AttAdcdBaseMapper attAdcdBaseMapper;

    @Override
    public PageVO<GmBusiness> upperQuery(BusManQueryVo queryVo) {
        String adcd = queryVo.getAdcd();
        String taskName = queryVo.getTaskName();
        Date start = null;
        Date end = null;
        if (StringUtils.isBlank(adcd)) {
            adcd = null;
        }
        if (StringUtils.isBlank(taskName)) {
            taskName = null;
        }
        if (queryVo.getYear() != null) {
            Date[] startEnd = getStartEndDateTimeOfYear(queryVo.getYear());
            start = startEnd[0];
            end = startEnd[1];
        }
        Integer paramTaskState = queryVo.getTaskState();
        List<Integer> selectParamTaskState = null;
        if (BusManTaskStateEnum.UN_SEND.getTaskStateNum().equals(paramTaskState)) {
            selectParamTaskState = Arrays.asList(BusManTaskStateEnum.UN_SEND.getTaskStateNum());
        } else if (BusManTaskStateEnum.SENDED.getTaskStateNum().equals(paramTaskState)) {
            // 已下达包括状态：已提醒、已超时、已结束
            selectParamTaskState = Arrays.asList(BusManTaskStateEnum.SENDED.getTaskStateNum(),
                    BusManTaskStateEnum.ALERTED.getTaskStateNum(),
                    BusManTaskStateEnum.OUT_OF_DATE.getTaskStateNum(),
                    BusManTaskStateEnum.FINISHED.getTaskStateNum());
        }
        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        return PageUtils.getPageVO(busManMapper.selectByAdcdTaskNameExetime(adcd, taskName, start, end, selectParamTaskState));
    }

    @Override
    public GmBusiness upperQueryOneById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return busManMapper.selectById(id);
        } else {
            return null;
        }
    }

    @Override
    public int upperAdd(BusManAddUpdateVo addUpdateVo) {
        GmBusiness busMan = new GmBusiness();
        busMan.setId(String.valueOf(snowFlake.nextId()));
        busMan.setTaskName(addUpdateVo.getTaskName());
//        busMan.setExeDeptId(addUpdateVo.getExeDeptId());
//        busMan.setExeStartTime(addUpdateVo.getExeStartTime());
//        busMan.setExeEndTime(addUpdateVo.getExeEndTime());
//        busMan.setTaskState(BusManTaskStateEnum.UN_SEND.getTaskStateNum());
//        String[] deptNameAdcd=getDeptNameAdcdByDeptId(addUpdateVo.getExeDeptId());
//        busMan.setExeDeptName(deptNameAdcd[0]);
        busMan.setAdcd(addUpdateVo.getAdcd());
        busMan.setAdnm(attAdcdBaseMapper.selectAdnmByAdcd(busMan.getAdcd()));
        busMan.setDelFlag(0);
        busMan.setCategory(addUpdateVo.getCategory());
        busMan.setCompletion(addUpdateVo.getCompletion());
        busMan.setProgress(addUpdateVo.getProgress());
        busMan.setRespPerson(addUpdateVo.getRespPerson());
        return busManMapper.insertOne(busMan);
    }


    @Override
    public int upperUpdate(BusManAddUpdateVo addUpdateVo) {
        GmBusiness busMan = busManMapper.selectById(addUpdateVo.getId());
        busMan.setTaskName(addUpdateVo.getTaskName());
//        busMan.setExeDeptId(addUpdateVo.getExeDeptId());
//        busMan.setExeStartTime(addUpdateVo.getExeStartTime());
//        busMan.setExeEndTime(addUpdateVo.getExeEndTime());
//        String[] deptNameAdcd=getDeptNameAdcdByDeptId(addUpdateVo.getExeDeptId());
//        busMan.setExeDeptName(deptNameAdcd[0]);
        busMan.setAdcd(addUpdateVo.getAdcd());
        busMan.setAdnm(attAdcdBaseMapper.selectAdnmByAdcd(addUpdateVo.getAdcd()));
        busMan.setCategory(addUpdateVo.getCategory());
        busMan.setCompletion(addUpdateVo.getCompletion());
        busMan.setProgress(addUpdateVo.getProgress());
        busMan.setRespPerson(addUpdateVo.getRespPerson());
        busMan.setUpdateTime(new Date());
        return busManMapper.update(busMan);
    }

    @Override
    public int upperDeleteById(String id) {
        if (StringUtils.isNotBlank(id)) {
            return busManMapper.virtualDeleteById(id);
        } else {
            return 0;
        }
    }

    @Override
    public int sendOrder(String id) {
        if (StringUtils.isNotBlank(id)) {
            return busManMapper.updateTaskStateFromState(BusManTaskStateEnum.UN_SEND.getTaskStateNum(),
                    BusManTaskStateEnum.SENDED.getTaskStateNum(), id);
        } else {
            return busManMapper.updateTaskStateFromState(BusManTaskStateEnum.UN_SEND.getTaskStateNum(),
                    BusManTaskStateEnum.SENDED.getTaskStateNum(), id);
        }
    }

    @Override
    public int syncTask(Integer year) {
        Date[] startEnd = getStartEndDateTimeOfYear(year);
        Date start = startEnd[0];
        Date end = startEnd[1];
        List<GmBusiness> tempVal = busManMapper.selectByAdcdTaskNameExetime(null, null, start, end, null);
        if (CollectionUtils.isBlank(tempVal)) {
            return 0;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int thisYear=calendar.get(Calendar.YEAR);
        for (GmBusiness temp : tempVal) {
            temp.setId(String.valueOf(snowFlake.nextId()));
            calendar.setTime(temp.getExeStartTime());
            calendar.set(Calendar.YEAR,thisYear);
            temp.setExeStartTime(calendar.getTime());
            calendar.setTime(temp.getExeEndTime());
            calendar.set(Calendar.YEAR,thisYear);
            temp.setExeEndTime(calendar.getTime());
            temp.setTaskState(BusManTaskStateEnum.UN_SEND.getTaskStateNum());
            temp.setFinishTime(null);
            temp.setExeUserId(null);
            temp.setExeUserName(null);
        }
        return busManMapper.insertMany(tempVal);
    }

    @Override
    public int alertTask(String id) {
        if (StringUtils.isNotBlank(id)) {
            // TODO 这里要提示
            return busManMapper.updateTaskStateFromState(BusManTaskStateEnum.SENDED.getTaskStateNum(),
                    BusManTaskStateEnum.ALERTED.getTaskStateNum(), id);
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
