package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.GmGeneralService;
import com.ygkj.digitization.mapper.GmGeneralMapper;
import com.ygkj.digitization.model.GmGeneral;
import com.ygkj.digitization.vo.request.GenManAddUpdateVo;
import com.ygkj.digitization.vo.request.GenManUpperQueryVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xq
 * @Description 日常管理服务层实现
 * @Date 2021/2/1
 */

@Service
public class GmGeneralServiceImpl implements GmGeneralService {

    @Resource
    SnowFlake snowFlake;

    @Resource
    GmGeneralMapper gmGeneralMapper;

    @Override
    public  PageVO<GmGeneral> upperQuery(GenManUpperQueryVo queryVo) {
        String adcd=queryVo.getAdcd();
        String taskName=queryVo.getTaskName();
//        Date rtStart=queryVo.getStart();
//        Date rtEnd=queryVo.getEnd();
        if (StringUtils.isBlank(adcd)){
            adcd=null;
        }
        if (StringUtils.isBlank(taskName)){
            taskName=null;
        }
        Date start = null;
        Date end = null;
        if (queryVo.getYear() != null) {
            Date[] startEnd = getStartEndDateTimeOfYear(queryVo.getYear());
            start = startEnd[0];
            end = startEnd[1];
        }
        PageHelper.startPage(queryVo.getPageNum(),queryVo.getPageSize());
        return PageUtils.getPageVO(gmGeneralMapper.selectByAdcdFillTimeTaskName(adcd,start,end,taskName));

    }

    @Override
    public PageVO<GmGeneral> subQuery(GenManUpperQueryVo queryVo) {
        // todo 这里的adcd 需要根据各区县的用户来判断行政区划，而不是要求前端来传参数
        String adcd=queryVo.getAdcd();
        String taskName=queryVo.getTaskName();

        if (StringUtils.isBlank(adcd)){
            adcd=null;
        }
        if (StringUtils.isBlank(taskName)){
            taskName=null;
        }
        Date start = null;
        Date end = null;
        if (queryVo.getYear() != null) {
            Date[] startEnd = getStartEndDateTimeOfYear(queryVo.getYear());
            start = startEnd[0];
            end = startEnd[1];
        }
        PageHelper.startPage(queryVo.getPageNum(),queryVo.getPageSize());
        return PageUtils.getPageVO(gmGeneralMapper.selectByAdcdFillTimeTaskName(adcd,start,end,taskName));
    }

    @Override
    public int subAdd(GenManAddUpdateVo addUpdateVo) {
        GmGeneral gmGeneral =new GmGeneral();
        gmGeneral.setId(String.valueOf(snowFlake.nextId()));
        gmGeneral.setAdcd(addUpdateVo.getAdcd());
        gmGeneral.setAdnm(null);
        gmGeneral.setTaskName(addUpdateVo.getTaskName());
        gmGeneral.setTaskTarget(addUpdateVo.getTaskTarget());
        gmGeneral.setExistProblem(addUpdateVo.getExistProblem());

        gmGeneral.setFillDeptId(null);
        gmGeneral.setFillDeptName(null);

        gmGeneral.setReportTime(new Date());
        gmGeneral.setTaskState(addUpdateVo.getTaskState());
        gmGeneral.setTaskName(addUpdateVo.getTaskName());

        gmGeneral.setAttachId(null);
        return gmGeneralMapper.insert(gmGeneral);
    }

    @Override
    public int subUpdate(GenManAddUpdateVo addUpdateVo) {
        GmGeneral gmGeneral =new GmGeneral();
        gmGeneral.setId(String.valueOf(snowFlake.nextId()));
        gmGeneral.setAdcd(addUpdateVo.getAdcd());
        gmGeneral.setAdnm(null);
        gmGeneral.setTaskName(addUpdateVo.getTaskName());
        gmGeneral.setTaskTarget(addUpdateVo.getTaskTarget());
        gmGeneral.setExistProblem(addUpdateVo.getExistProblem());

        gmGeneral.setFillDeptId(null);
        gmGeneral.setFillDeptName(null);

        gmGeneral.setReportTime(new Date());
        gmGeneral.setTaskState(addUpdateVo.getTaskState());
        gmGeneral.setTaskName(addUpdateVo.getTaskName());

        gmGeneral.setAttachId(null);
        return gmGeneralMapper.update(gmGeneral);
    }

    @Override
    public int subDelete(String genManId) {
        return gmGeneralMapper.virtualDeleteById(genManId);
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
}
