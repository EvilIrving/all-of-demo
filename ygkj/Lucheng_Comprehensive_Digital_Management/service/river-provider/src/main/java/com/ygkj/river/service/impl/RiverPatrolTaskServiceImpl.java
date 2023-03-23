package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.*;
import com.ygkj.river.service.RiverPatrolTaskService;
import com.ygkj.river.mapper.*;
import com.ygkj.river.model.*;
import com.ygkj.river.vo.request.*;
import com.ygkj.river.vo.response.*;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@Service
public class RiverPatrolTaskServiceImpl implements RiverPatrolTaskService {
    @Resource
    RiverPatrolTaskMapper taskMapper;

    @Resource
    GenerateDistributionIDService idService;

    @Resource
    RiverInfoMapper riverMapper;

    @Resource
    RiverPatrolTaskEventMapper eventMapper;

    @Resource
    RiverPatrolTaskEventProbMapper probMapper;

    @Resource
    AttAdcdBaseMaper adcdMapper;

    @Resource
    HikvisionCameraRecordMapper cameraMapper;


    @Override
    public List<HikvisionCameraRecord> cameraOfRiver(String riverCode) {
        if (StringUtils.isBlank(riverCode)) {
            return Collections.emptyList();
        }
        return cameraMapper.cameraOfRiver(riverCode);
    }

    @Override
    public List<HikvisionCameraRecord> cameraByCode(String cameraCode) {
        if (StringUtils.isBlank(cameraCode)) {
            return Collections.emptyList();
        }
        List<HikvisionCameraRecord> tempResult = cameraMapper.loadMany(cameraCode);
        if (tempResult == null) {
            tempResult = Collections.emptyList();
        }
        String[] cameraCodeArray = cameraCode.split(",");
        List<HikvisionCameraRecord> result = new ArrayList<>(cameraCodeArray.length);
        Map<String, HikvisionCameraRecord> cameraMap = tempResult.stream().collect(Collectors.toMap(HikvisionCameraRecord::getCameraIndexCode, o -> o));
        for (String cc : cameraCodeArray) {
            result.add(cameraMap.getOrDefault(cc, null));
        }
        return result;
    }

    @Override
    public PageVO<RptTaskResVo> taskPage(RptTaskQueryReqVo reqVo) {
        PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        PageVO<RptTaskResVo> result = PageUtils.getPageVO(taskMapper.selectByCondition(reqVo));
        List<RptTaskResVo> tempList = result.getList();
        if (CollectionUtils.isNotBlank(tempList)) {
            for (RptTaskResVo temp : tempList) {
                if (StringUtils.isNotBlank(temp.getCameraCode())) {
                    temp.setCameraNum(StringUtils.countMatches(temp.getCameraCode(), ",") + 1);
                } else {
                    temp.setCameraNum(0);
                }
            }
        }
        return result;
    }

    @Override
    public int addTask(RptTaskEditReqVo reqVo) {
        RiverInfoResVo riverInfo = riverMapper.load(reqVo.getRiverCode());
        if (riverInfo == null) {
            return 0;
        }
        RiverPatrolTask record = new RiverPatrolTask();
        BeanUtils.copyProperties(reqVo, record);
        record.setId(idService.nextID());
        record.setRiverName(riverInfo.getName());
        record.setRiverLevel(riverInfo.getRiverGrade());
        record.setRiverBas(riverInfo.getBasin());
        record.setAdcd(riverInfo.getDistrinctAdcd());
        if (StringUtils.isBlank(record.getRiverManageName())) {
            record.setRiverManageName(riverInfo.getRiverManagerName());
        }
        if (StringUtils.isBlank(record.getRiverManagePhone())) {
            record.setRiverManagePhone(riverInfo.getRiverManagerPhone());
        }
        record.setRiverManagerGrade(riverInfo.getRiverManagerGrade());
        if (StringUtils.isNotBlank(record.getAdcd())) {
            record.setAdnm(this.adcdMapper.selectNameOfAdcd(record.getAdcd()));
        }
        return taskMapper.insert(record);
    }

    @Override
    public int updateTask(RptTaskEditReqVo reqVo) {
        RiverInfoResVo riverInfo = riverMapper.load(reqVo.getRiverCode());
        if (riverInfo == null) {
            return 0;
        }
        RiverPatrolTask record = new RiverPatrolTask();
        BeanUtils.copyProperties(reqVo, record);
        record.setRiverName(riverInfo.getName());
        record.setRiverLevel(riverInfo.getRiverGrade());
        record.setRiverBas(riverInfo.getBasin());
        record.setAdcd(riverInfo.getDistrinctAdcd());
        if (StringUtils.isBlank(record.getRiverManageName())) {
            record.setRiverManageName(riverInfo.getRiverManagerName());
        }
        if (StringUtils.isBlank(record.getRiverManagePhone())) {
            record.setRiverManagePhone(riverInfo.getRiverManagerPhone());
        }
        record.setRiverManagerGrade(riverInfo.getRiverManagerGrade());
        if (StringUtils.isNotBlank(record.getAdcd())) {
            record.setAdnm(this.adcdMapper.selectNameOfAdcd(record.getAdcd()));
        }
        return taskMapper.update(record);
    }

    @Override
    public int delTask(String id) {
        RiverPatrolTask reqVo = new RiverPatrolTask();
        reqVo.setDelFlag(1);
        reqVo.setId(id);
        return taskMapper.update(reqVo);
    }

    @Override
    public RptTaskResVo getOneTask(String id) {
        RiverPatrolTask tempResult = taskMapper.load(id);
        RptTaskResVo result = null;
        if (tempResult != null) {
            result = new RptTaskResVo();
            BeanUtils.copyProperties(tempResult, result);
            result.setRiverInfo(riverMapper.load(result.getRiverCode()));
            RptEventQueryReqVo eventReqVo = new RptEventQueryReqVo();
            eventReqVo.setTaskId(id);
            result.setEventList(eventMapper.selectByCondition(eventReqVo));
            if (StringUtils.isNotBlank(result.getCameraCode())) {
                result.setCameraNum(StringUtils.countMatches(result.getCameraCode(), ",") + 1);
            } else {
                result.setCameraNum(0);
            }
        }
        return result;
    }


    /**
     * ------------------------任务巡河事件-----------------------------------
     */

    @Override
    public PageVO<RptEventResVo> eventPage(RptEventQueryReqVo reqVo) {
        PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        PageVO<RptEventResVo> result = PageUtils.getPageVO(eventMapper.selectByCondition(reqVo));
        List<RptEventResVo> tempList = result.getList();
        if (CollectionUtils.isNotBlank(tempList)) {
            List<ComKeyValue<String, Integer>> countNumList = probMapper.countByEventIds(tempList.stream().map(RptEventResVo::getId).collect(Collectors.joining()));
            if (countNumList == null) {
                countNumList = Collections.emptyList();
            }
            Map<String, Integer> countNumMap = countNumList.stream().collect(Collectors.toMap(ComKeyValue::getKey, ComKeyValue::getValue));
            for (RptEventResVo temp : tempList) {
                temp.setProblemNum(Integer.parseInt(String.valueOf(countNumMap.getOrDefault(temp.getId(), 0))));
            }
        }
        return result;
    }

    @Override
    public String addEvent(RptEventEditReqVo reqVo) {
        RptTaskResVo taskInfo = getOneTask(reqVo.getTaskId());
        if (taskInfo == null) {
            return null;
        }
        RiverPatrolTaskEvent record = new RiverPatrolTaskEvent();
        BeanUtils.copyProperties(reqVo, record);
        record.setId(idService.nextID());
        record.setTaskName(taskInfo.getTaskName());
        record.setRiverCode(taskInfo.getRiverCode());
        record.setRiverName(taskInfo.getRiverName());
        record.setRiverName(taskInfo.getRiverName());
        record.setRiverBas(taskInfo.getRiverInfo() == null ? null : taskInfo.getRiverInfo().getBasin());
        record.setPatrolFreeq(taskInfo.getPatrolFreeq());
        record.setAdcd(taskInfo.getAdcd());
        record.setAdnm(taskInfo.getAdnm());
        if (eventMapper.insert(record) > 0) {
            return record.getId();
        } else {
            return null;
        }
    }

    @Override
    public int updateEvent(RptEventEditReqVo reqVo) {
        RptTaskResVo taskInfo = getOneTask(reqVo.getTaskId());
        if (taskInfo == null) {
            return 0;
        }
        RiverPatrolTaskEvent record = new RiverPatrolTaskEvent();
        BeanUtils.copyProperties(reqVo, record);
        record.setTaskName(taskInfo.getTaskName());
        record.setRiverCode(taskInfo.getRiverCode());
        record.setRiverName(taskInfo.getRiverName());
        record.setRiverName(taskInfo.getRiverName());
        record.setRiverBas(taskInfo.getRiverInfo() == null ? null : taskInfo.getRiverInfo().getBasin());
        record.setPatrolFreeq(taskInfo.getPatrolFreeq());
        record.setAdcd(taskInfo.getAdcd());
        record.setAdnm(taskInfo.getAdnm());
        return eventMapper.update(record);
    }

    @Override
    public int delEvent(String id) {
        RiverPatrolTaskEvent reqVo = new RiverPatrolTaskEvent();
        reqVo.setDelFlag(1);
        reqVo.setId(id);
        return eventMapper.update(reqVo);
    }

    @Override
    public RptEventResVo getOneEvent(String id) {
        RiverPatrolTaskEvent tempResult = eventMapper.load(id);
        RptEventResVo result = null;
        if (tempResult != null) {
            result = new RptEventResVo();
            BeanUtils.copyProperties(tempResult, result);
//            List<ComKeyValue<String, Integer>> countNumList = probMapper.countByEventIds(result.getId());
//            if (CollectionUtils.isNotBlank(countNumList)) {
//                result.setProblemNum(countNumList.get(0).getValue());
//            }else {
//                result.setProblemNum(0);
//            }
            List<String> cameraCodes = probMapper.cameraCodesOfProbByEventIds(result.getId());
            if (CollectionUtils.isNotBlank(cameraCodes)) {
                result.setProblemNum(cameraCodes.size());
                result.setCameraCodeList(cameraCodes);
            } else {
                result.setProblemNum(0);
                result.setCameraCodeList(Collections.emptyList());
            }

            RptProbQueryReqVo reqVo = new RptProbQueryReqVo();
            reqVo.setEventId(result.getId());
            List<RiverPatrolTaskEventProb> probs = probMapper.selectByCondition(reqVo);
            probs = probs == null ? Collections.emptyList() : probs;
            result.setProbResList(probs);


//            result.setTaskInfo(getOneTask(result.getTaskId()));
            result.setTaskInfo(taskMapper.loadTaskResVo(result.getTaskId()));
        }
        return result;
    }

    @Override
    public List<RiverPatrolTaskEvent> getAllEvent(RptEventQueryReqVo reqVo) {
        List<RiverPatrolTaskEvent> riverPatrolTaskEvents = eventMapper.selectForExport(reqVo);
        return riverPatrolTaskEvents;
    }

    @Override
    public int addProblem(RptProbEditReqVo reqVo) {
        RptEventResVo eventInfo = getOneEvent(reqVo.getEventId());
        if (eventInfo == null) {
            return 0;
        }
        RiverPatrolTaskEventProb record = new RiverPatrolTaskEventProb();
        BeanUtils.copyProperties(reqVo, record);
        record.setId(idService.nextID());
        record.setTaskId(eventInfo.getTaskId());
        record.setTaskName(eventInfo.getTaskName());
        record.setRiverCode(eventInfo.getRiverCode());
        record.setRiverName(eventInfo.getRiverName());
        record.setCameraName(cameraMapper.cameraNameByCode(reqVo.getCameraCode()));
        record.setAdcd(eventInfo.getAdcd());
        record.setAdnm(eventInfo.getAdnm());
        return probMapper.insert(record);
    }

    @Override
    public int updateProblem(RptProbEditReqVo reqVo) {
        RptEventResVo eventInfo = getOneEvent(reqVo.getEventId());
        if (eventInfo == null) {
            return 0;
        }
        RiverPatrolTaskEventProb record = new RiverPatrolTaskEventProb();
        BeanUtils.copyProperties(reqVo, record);
        record.setTaskId(eventInfo.getTaskId());
        record.setTaskName(eventInfo.getTaskName());
        record.setRiverCode(eventInfo.getRiverCode());
        record.setRiverName(eventInfo.getRiverName());
        record.setCameraName(cameraMapper.cameraNameByCode(reqVo.getCameraCode()));
        record.setAdcd(eventInfo.getAdcd());
        record.setAdnm(eventInfo.getAdnm());
        return probMapper.update(record);
    }

    @Override
    public int delProblem(String id) {
        RiverPatrolTaskEventProb reqVo = new RiverPatrolTaskEventProb();
        reqVo.setDelFlag(1);
        reqVo.setId(id);
        return probMapper.update(reqVo);
    }

    @Override
    public List<RptProbResVo> problemByEventIdCameraCode(String eventId, String cameraCode) {
        if (StringUtils.isBlank(eventId) || StringUtils.isBlank(cameraCode)) {
            return Collections.emptyList();
        }
        List<RiverPatrolTaskEventProb> records = probMapper.selectByCondition(Builder.of(RptProbQueryReqVo::new)
                .with(RptProbQueryReqVo::setCameraCode, cameraCode)
                .with(RptProbQueryReqVo::setEventId, eventId)
                .build());
        if (CollectionUtils.isBlank(records)) {
            return Collections.emptyList();
        }
        List<RptTaskResVo> taskInfos = taskMapper.selectByTaskIds(records.stream().map(RiverPatrolTaskEventProb::getTaskId).distinct().collect(Collectors.joining()));
        taskInfos = taskInfos == null ? Collections.emptyList() : taskInfos;
        Map<String, RptTaskResVo> riverInfoMap = taskInfos.stream().collect(Collectors.toMap(RptTaskResVo::getId, o -> o));
        List<RptProbResVo> result = new ArrayList<>(taskInfos.size());
        for (RiverPatrolTaskEventProb temp : records) {
            RptTaskResVo taskInfo = riverInfoMap.get(temp.getRiverCode());
            RptProbResVo tempResult = new RptProbResVo();
            BeanUtils.copyProperties(temp, tempResult);
            if (taskInfo != null) {
                tempResult.setRiverManagerGrade(taskInfo.getRiverManagerGrade());
                tempResult.setRiverManagerName(taskInfo.getRiverManageName());
                tempResult.setRiverManagerPhone(taskInfo.getRiverManagePhone());
            }
            result.add(tempResult);
        }
        return result;
    }

    @Override
    public RptProbResVo problemById(String probId) {
        if (StringUtils.isBlank(probId)) {
            return null;
        }
        RiverPatrolTaskEventProb prob = probMapper.load(probId);
        if (prob == null) {
            return null;
        }
        List<RptTaskResVo> taskInfos = taskMapper.selectByTaskIds(prob.getTaskId());
        taskInfos = taskInfos == null ? Collections.emptyList() : taskInfos;
        RptTaskResVo taskInfo = taskInfos.get(0);
        RptProbResVo result = new RptProbResVo();
        BeanUtils.copyProperties(prob, result);
        if (taskInfo != null) {
            result.setRiverManagerGrade(taskInfo.getRiverManagerGrade());
            result.setRiverManagerName(taskInfo.getRiverManageName());
            result.setRiverManagerPhone(taskInfo.getRiverManagePhone());
        }
        return result;
    }
}
