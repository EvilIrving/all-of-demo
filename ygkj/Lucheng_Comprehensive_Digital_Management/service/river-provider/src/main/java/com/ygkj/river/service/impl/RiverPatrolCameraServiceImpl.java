package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.HikvisionCameraRecordMapper;
import com.ygkj.river.mapper.RiverInfoMapper;
import com.ygkj.river.mapper.RiverPatrolCameraEventMapper;
import com.ygkj.river.service.RiverPatrolCameraService;
import com.ygkj.river.model.HikvisionCameraRecord;
import com.ygkj.river.model.RiverPatrolCameraEvent;
import com.ygkj.river.vo.request.RpcEventEditReqVo;
import com.ygkj.river.vo.request.RpcEventQueryReqVo;
import com.ygkj.river.vo.response.RiverInfoResVo;
import com.ygkj.river.vo.response.RpcEventResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@Service
public class RiverPatrolCameraServiceImpl implements RiverPatrolCameraService {


    @Resource
    RiverPatrolCameraEventMapper eventMapper;
    @Resource
    HikvisionCameraRecordMapper cameraMapper;

    @Resource
    RiverInfoMapper riverMapper;

    @Resource
    AttAdcdBaseMaper adcdMapper;

    @Resource
    GenerateDistributionIDService idService;


    @Override
    public PageVO<RpcEventResVo> cameraEventPage(RpcEventQueryReqVo reqVo) {
        PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        PageVO<RpcEventResVo> result = PageUtils.getPageVO(eventMapper.selectByCondition(reqVo));
        return result;
    }

    @Override
    public int addCameraEvent(RpcEventEditReqVo reqVo) {
        HikvisionCameraRecord cameraRecord = cameraMapper.load(reqVo.getCameraCode());
        RiverInfoResVo riverInfo = riverMapper.load(reqVo.getRiverCode());
        if (cameraRecord == null || riverInfo == null) {
            return 0;
        }
        RiverPatrolCameraEvent record = new RiverPatrolCameraEvent();
        BeanUtils.copyProperties(reqVo, record);
        record.setCameraName(cameraRecord.getCameraName());
        record.setRiverName(riverInfo.getName());
        record.setRiverBas(riverInfo.getBasin());
        record.setId(idService.nextID());
        if (StringUtils.isNotBlank(reqVo.getAdcd())) {
            record.setAdnm(adcdMapper.selectNameOfAdcd(reqVo.getAdcd()));
        }
        return eventMapper.insert(record);
    }

    @Override
    public int updateCameraEvent(RpcEventEditReqVo reqVo) {
        HikvisionCameraRecord cameraRecord = cameraMapper.load(reqVo.getCameraCode());
        RiverInfoResVo riverInfo = riverMapper.load(reqVo.getRiverCode());
        if (cameraRecord == null || riverInfo == null) {
            return 0;
        }
        RiverPatrolCameraEvent record = new RiverPatrolCameraEvent();
        BeanUtils.copyProperties(reqVo, record);
        record.setCameraName(cameraRecord.getCameraName());
        record.setRiverName(riverInfo.getName());
        record.setRiverBas(riverInfo.getBasin());
        if (StringUtils.isNotBlank(reqVo.getAdcd())) {
            record.setAdnm(adcdMapper.selectNameOfAdcd(reqVo.getAdcd()));
        }
        return eventMapper.update(record);
    }

    @Override
    public int delCameraEvent(String id) {
        RiverPatrolCameraEvent record = new RiverPatrolCameraEvent();
        record.setId(id);
        record.setDelFlag(1);
        return eventMapper.update(record);
    }

    @Override
    public RpcEventResVo getOneCameraEvent(String id) {
        RiverPatrolCameraEvent temp = this.eventMapper.load(id);
        if (temp == null) {
            return null;
        }
        RpcEventResVo result = new RpcEventResVo();
        BeanUtils.copyProperties(temp, result);
        RiverInfoResVo riverInfo = riverMapper.load(result.getRiverCode());
        if (riverInfo != null) {
            result.setRiverManagerGrade(riverInfo.getRiverManagerGrade());
            result.setRiverManagerName(riverInfo.getRiverManagerName());
            result.setRiverManagerPhone(riverInfo.getRiverManagerPhone());
        }
        return result;
    }

    @Override
    public List<RiverPatrolCameraEvent> selectForExport(RpcEventQueryReqVo reqVo) {
        List<RiverPatrolCameraEvent> list = eventMapper.selectForExport(reqVo);
        return list;
    }
}
