package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.CamBindObjReqVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.CameraOpsQueryReqVo;
import com.ygkj.project.model.SysWarnLog;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.factory.SysSendMessageLogCreator;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.CameraWarnRuleEnum;
import com.ygkj.water.project.feign.SmsFeignClient;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.CameraOpsService;
import com.ygkj.water.project.task.CameraWarnNotifyManager;
import com.ygkj.water.project.task.CameraWarnNotifyTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@Service
public class CameraOpsServiceImpl implements CameraOpsService {

    Logger log = LoggerFactory.getLogger(CameraOpsServiceImpl.class);
    @Resource
    AttCameraOpsMapper opsMapper;
    @Resource
    AttCameraRegionMapper regionMapper;

    @Resource
    HikCameraMapper cameraMapper;

    @Resource
    AttAdcdBaseMapper adcdMapper;

    @Resource
    ProjectMapper projectMapper;

    @Resource
    SmsFeignClient smsClient;

    @Resource
    AttDictBaseMapper dictBaseMapper;

    @Resource
    WarningMapper warningMapper;

    @Resource
    UserMapper userMapper;

    @Autowired
    CameraWarnNotifyManager cameraWarnNotifyManager;

    DateTimeFormatter smsDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH：mm：ss");

//    final static String nullVal="Null";

    final static String BASIN_DICT_CODE = "basin";

    final static Integer onlineStatus = 1;

    final static Integer offlineStatus = 0;

    final static Integer doWarnNotify = 1;

    @Override
    public List<RegionResVo> regionAsTree() {
        /**
         * 1. 先查出所有的region
         * 2. 再根据region组合成树
         */
        QueryWrapper<AttCameraRegion> query = new QueryWrapper<>();
        List<AttCameraRegion> regions = regionMapper.selectList(query);
        List<RegionResVo> tempResult = regions.stream().map(o -> {
            RegionResVo temp = new RegionResVo();
            org.springframework.beans.BeanUtils.copyProperties(o, temp);
            temp.setChildren(new ArrayList<>());
            return temp;
        }).collect(Collectors.toList());
        List<RegionResVo> result = Collections.emptyList();
        if (CollectionUtils.isNotBlank(tempResult)) {
            Map<String, RegionResVo> regionMap = tempResult.stream().collect(Collectors.toMap(RegionResVo::getIndexCode, o -> o));
            result = new ArrayList<>(tempResult.size());
            for (RegionResVo region : tempResult) {
                RegionResVo parentObject = regionMap.get(region.getParentIndexCode());
                if (parentObject != null) {
                    if (parentObject.getChildren() == null) {
                        parentObject.setChildren(new ArrayList<>());
                    }
                    parentObject.getChildren().add(region);
                } else {
                    result.add(region);
                }
            }
        }
        return result;
    }

    @Override
    public PageVO<HikCameraResVo> pageCamera(HikCameraQueryReqVo reqVo) {
        if (reqVo.getPageNum() > 0 && reqVo.getPageSize() > 0) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        if (StringUtils.isNotBlank(reqVo.getAreaCode()) && !StringUtils.startsWith(reqVo.getAreaCode(), "3303")) {
            reqVo.setAreaCode(adcdMapper.getWenzhouAreaCodeByAdnm(reqVo.getAreaCode()));
        }
        // 当传入其他的时候，查询非十大工程、河道、在建工程绑定的摄像头
        if (StringUtils.isNotBlank(reqVo.getProjectType()) && reqVo.getProjectType().equals("其他")) {
            reqVo.setNotBelongProjType(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, listAllCameraBindObjType()));
            reqVo.setProjectType(null);
        }
        PageVO<HikCameraResVo> result = PageUtils.getPageVO(cameraMapper.selectCameraInfoByCondition(reqVo));
        List<HikCameraResVo> tempList = result.getList();
        if (CollectionUtils.isNotBlank(tempList)) {
            String areaCodes = tempList.stream().map(HikCameraResVo::getAreaCode).filter(StringUtils::isNotBlank).map(o -> o.substring(0, 6).concat("000000")).collect(Collectors.joining(","));
            Map<String, String> adcdMap = Collections.emptyMap();
            if (StringUtils.isNotBlank(areaCodes)) {
                List<AttAdcdBase> adcdList = adcdMapper.selectByAreaCodes(areaCodes);
                if (CollectionUtils.isNotBlank(adcdList)) {
                    adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
                }
            }
            computeOnlineOfflineHours(tempList, reqVo);
            computeNewestStatusDuration(tempList);
            for (HikCameraResVo cameraResVo : tempList) {
                cameraResVo.setLocateMarked(StringUtils.isNotBlank(cameraResVo.getLatitude())
                        && StringUtils.isNotBlank(cameraResVo.getLongitude()));
                if (StringUtils.isNotBlank(cameraResVo.getAreaCode())) {
                    cameraResVo.setAdnm(adcdMap.get(cameraResVo.getAreaCode().substring(0, 6).concat("000000")));
                }
            }
        }
        return result;
    }

    public void computeOnlineOfflineHours(List<HikCameraResVo> tempList, HikCameraQueryReqVo reqVo) {
        if (CollectionUtils.isBlank(tempList)) {
            return;
        }
        Map<Integer, Double> onlineTimeMap = new HashMap<>(tempList.size());
        Map<Integer, Double> offlineTimeMap = new HashMap<>(tempList.size());
        if (reqVo.getStiStart() != null && reqVo.getStiEnd() != null) {
            List<Integer> hashList = tempList.stream().map(HikCameraResVo::getRegionCameraHash).filter(Objects::nonNull).collect(Collectors.toList());
            List<AttCameraOnline> historicOnlineList = this.cameraMapper.selectCameraOnlineByCameraHashList(hashList, reqVo.getStiStart(), reqVo.getStiEnd());
            Date end = new Date(reqVo.getStiEnd().getTime() + 24 * 3600 * 1000);
            historicOnlineList.stream()
                    .collect(Collectors.groupingBy(AttCameraOnline::getRegionCameraHash))
                    .forEach((hash, onlineTempList) -> {
                        onlineTempList.sort(Comparator.comparing(AttCameraOnline::getCreateTime));
                        long onlineTime = 0;
                        long offlineTime = 0;
                        for (int i = 0; i < onlineTempList.size(); i++) {
                            AttCameraOnline current = onlineTempList.get(i);
                            AttCameraOnline next = null;
                            if (i + 1 < onlineTempList.size()) {
                                next = onlineTempList.get(i + 1);
                            }
                            long time = 0;
                            if (next != null) {
                                if (next.getCreateTime() != null && current.getCreateTime() != null) {
                                    time = next.getCreateTime().getTime() - current.getCreateTime().getTime();
                                } else {
                                    time = 3600000;
                                }
                            } else {
                                if (current.getCollectTime() != null) {
                                    time = end.getTime() - current.getCreateTime().getTime();
                                } else {
                                    time = end.getTime() - DateUtils.clearDateTimePrecision(end, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND).getTime();
                                }

                            }
                            switch (current.getOnline()) {
                                case 1:
                                    onlineTime += time;
                                    break;
                                case 0:
                                    offlineTime += time;
                                    break;
                            }
                        }
                        onlineTimeMap.put(hash, (double) (onlineTime / 3600000));
                        offlineTimeMap.put(hash, (double) (offlineTime / 3600000));
                    });
        }

        for (HikCameraResVo cameraResVo : tempList) {
            cameraResVo.setOfflineTime(offlineTimeMap.getOrDefault(cameraResVo.getRegionCameraHash(), 0D));
            cameraResVo.setOnlineTime(onlineTimeMap.getOrDefault(cameraResVo.getRegionCameraHash(), 0D));
        }
    }

    public void computeNewestStatusDuration(List<HikCameraResVo> tempList) {
        if (CollectionUtils.isNotBlank(tempList)) {
            Date now = new Date();
            long currentMills = now.getTime();
            for (HikCameraResVo camera : tempList) {
                if (camera.getStatusFirstCollectTime() != null) {
                    long collectMills = camera.getStatusFirstCollectTime().getTime();
                    int hours = (int) ((currentMills - collectMills) / 3600000);
                    if (hours >= 0) {
                        camera.setStatusDuration(hours);
                    } else {
                        log.warn("状态持续时长错误：currentMills: {}  collectTime: {}", now, camera.getStatusFirstCollectTime());
                    }
                }
            }
        }
    }

    /**
     * 根据条件统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4PageCamera(HikCameraQueryReqVo reqVo) {
        if (StringUtils.isNotBlank(reqVo.getAreaCode()) && !StringUtils.startsWith(reqVo.getAreaCode(), "3303")) {
            reqVo.setAreaCode(adcdMapper.getWenzhouAreaCodeByAdnm(reqVo.getAreaCode()));
        }
        // 真奇葩
        if (StringUtils.isNotBlank(reqVo.getProjectType()) && reqVo.getProjectType().equals("其他")) {
            reqVo.setNotBelongProjType(String.join(",", listAllCameraBindObjType()));
            reqVo.setProjectType(null);
        }
        List<HikCameraResVo> tempList = cameraMapper.selectCamStatus(reqVo);
        long online = 0;
        long offline = 0;
        int total = 0;
        Integer onlineStatus = 1;
        if (CollectionUtils.isNotBlank(tempList)) {
            online = tempList.stream().filter(o -> o.getStatus().equals(onlineStatus)).count();
            offline = tempList.stream().filter(o -> !o.getStatus().equals(onlineStatus)).count();
            total = tempList.size();
        }
        JSONObject result = new JSONObject();
        result.put("online", online);
        result.put("offline", offline);
        result.put("total", total);
        return result;
    }

    @Override
    public PageVO<HikCameraResVo> listBindHistory(String cameraIndexCode, Integer pageSize, Integer pageNum) {
        if (StringUtils.isBlank(cameraIndexCode)) {
            return PageUtils.getPageVO(Collections.emptyList());
        }
        HikvisionCameraRecord record = cameraMapper.load(cameraIndexCode);
        if (record == null) {
            return PageUtils.getPageVO(Collections.emptyList());
        }
        if (pageSize != null && pageNum != null)
            PageHelper.startPage(pageNum, pageSize);
        return PageUtils.getPageVO(cameraMapper.selectCameraInfoByCondition(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setRegionCameraHash, record.getRegionCameraHash())
                .with(HikCameraQueryReqVo::setExcludeCameraIndexCode, record.getCameraIndexCode())
                .build()));
    }

    @Override
    public int updateCameraInfo(HikvisionCameraRecord cameraRecord) {
        return cameraMapper.update(cameraRecord);
    }

    @Override
    public int delCamera(String cameraIndexCode) {
        return cameraMapper.update(Builder.of(HikvisionCameraRecord::new)
                .with(HikvisionCameraRecord::setCameraIndexCode, cameraIndexCode)
                .with(HikvisionCameraRecord::setDelFlag, 1).build());
    }

    @Override
    public int bindProjByRegion(String projectId, String regionIndexCode) {
        List<String> regionIndexCodeList = getCascadeRegionIndexCode(regionIndexCode);
        if (CollectionUtils.isBlank(regionIndexCodeList)) {
            return 0;
        }
        List<HikCameraResVo> hikList = cameraMapper.selectByCondition(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setRegionIndexCode, String.join(",", regionIndexCodeList)).build());
        int num = 0;
        if (CollectionUtils.isNotBlank(hikList)) {
            for (HikCameraResVo record : hikList) {
                record.setProjectCode(projectId);
                num += cameraMapper.update(record);
            }
        }
        return num;
    }

    @Override
    public int bindProjByCamera(String projectId, String cameraIndexCode) {
        if (StringUtils.isBlank(projectId) || StringUtils.isBlank(cameraIndexCode)) {
            return 0;
        }
        return cameraMapper.update(Builder.of(HikvisionCameraRecord::new)
                .with(HikvisionCameraRecord::setProjectCode, projectId)
                .with(HikvisionCameraRecord::setCameraIndexCode, cameraIndexCode).build());
    }

    @Override
    public int bindOpsByCamera(String opsId, String cameraIndexCode) {
        if (StringUtils.isNotBlank(opsId) && StringUtils.isNotBlank(cameraIndexCode)) {
            return cameraMapper.update(Builder.of(HikvisionCameraRecord::new)
                    .with(HikvisionCameraRecord::setOpsId, opsId)
                    .with(HikvisionCameraRecord::setCameraIndexCode, cameraIndexCode).build());
        } else {
            return 0;
        }
    }

    @Override
    public int unbindProjCamera(String cameraIndexCode) {
        if (StringUtils.isNotBlank(cameraIndexCode)) {
            return cameraMapper.unbindProjectCamera(cameraIndexCode);
        } else {
            return 0;
        }
    }

    @Override
    public int unbindOpsCamera(String opsId, String cameraIndexCode) {
        if (StringUtils.isNotBlank(opsId) && StringUtils.isNotBlank(cameraIndexCode)) {
            return cameraMapper.unbindOpsCamera(opsId, cameraIndexCode);
        } else {
            return 0;
        }
    }

    @Override
    public PageVO<AttCameraOps> pageOpsInfo(CameraOpsQueryReqVo reqVo) {
        QueryWrapper<AttCameraOps> query = new QueryWrapper<>();
        if (StringUtils.isNotBlank(reqVo.getOpsName())) {
            query.like("ops_name", reqVo.getOpsName());
        }
        PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        return PageUtils.getPageVO(opsMapper.selectList(query));
    }

    @Override
    public int addOrUpdateOps(AttCameraOps record) {
        if (StringUtils.isNotBlank(record.getOpsPhone())) {
            record.setNotifyType(this.userMapper.checkPhoneExist(record.getOpsPhone()) == null ?
                    SysSendMessageLog.MessageSendType.SMS.type() :
                    SysSendMessageLog.MessageSendType.GOVERNMENT_DING_DING.type());
        }
        if (StringUtils.isBlank(record.getId())) {
            return opsMapper.insert(record);
        } else {
            UpdateWrapper<AttCameraOps> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", record.getId());
            return opsMapper.update(record, updateWrapper);
        }
    }

    @Override
    public int delOps(String opsId) {
        if (StringUtils.isBlank(opsId)) {
            return 0;
        }
        cameraMapper.unbindOps(opsId);
        AttCameraOps record = new AttCameraOps();
        record.setDelFlag(1);
        UpdateWrapper<AttCameraOps> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("del_flag", 1);
        updateWrapper.eq("id", opsId);
        return opsMapper.update(record, updateWrapper);
    }

    @Override
    public HikCameraStatsResVo statsCameraByBasin() {
        HikCameraStatsResVo result = new HikCameraStatsResVo();
//        List<String> basinList = Arrays.asList("飞云江", "鳌江", "瓯江", "其他");
        List<String> basinList = dictBaseMapper.selectByDictCode(BASIN_DICT_CODE).stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getDictName()))
                .map(AttDictBase::getDictName)
                .collect(Collectors.toList());
        List<HikCameraResVo> cameraList = this.cameraMapper.selectAllStatusAdcdBasin();
        cameraList = cameraList == null ? Collections.emptyList() : cameraList;
        List<ChartResVo<String, Long>> onlineNum = new ArrayList<>();
        List<ChartResVo<String, Long>> totalNum = new ArrayList<>();
        List<ChartResVo<String, Long>> offlineNum = new ArrayList<>();
        cameraList.removeAll(Collections.singleton(null));
        Map<String, List<HikCameraResVo>> cameraMap = cameraList.stream().filter(o -> StringUtils.isNotBlank(o.getBasin())).collect(Collectors.groupingBy(o -> o.getBasin()));
        Map<String, ChartResVo<String, Long>> onlineMap = new HashMap<>(basinList.size());
        Map<String, ChartResVo<String, Long>> offlineMap = new HashMap<>(basinList.size());
        for (String basin : basinList) {
            List<HikCameraResVo> tempList = cameraMap.getOrDefault(basin, Collections.emptyList());
            totalNum.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, basin)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempList.size())))
                    .build());

            Map<Integer, List<HikCameraResVo>> tempMap = tempList.stream().filter(o -> o.getStatus() != null).collect(Collectors.groupingBy(o -> o.getStatus()));

            onlineMap.put(basin, Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, basin)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(1, Collections.emptyList()).size())))
                    .build());
            offlineMap.put(basin, Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, basin)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(0, Collections.emptyList()).size())))
                    .build());
        }
        totalNum = totalNum.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
        for (ChartResVo<String, Long> temp : totalNum) {
            onlineNum.add(onlineMap.get(temp.getKey()));
            offlineNum.add(offlineMap.get(temp.getKey()));
        }
        result.setOfflineNum(offlineNum);
        result.setTotalNum(totalNum);
        result.setOnlineNum(onlineNum);
        return result;
    }

    @Override
    public HikCameraStatsResVo statsCameraByProject() {
        HikCameraStatsResVo result = new HikCameraStatsResVo();
        List<String> projectTypeList = this.projectMapper.selectProjectType();
        projectTypeList.add("其他");
        if (CollectionUtils.isBlank(projectTypeList)) {
            return result;
        }
        List<ChartResVo<String, Long>> onlineNum = new ArrayList<>();
        List<ChartResVo<String, Long>> totalNum = new ArrayList<>();
        List<ChartResVo<String, Long>> offlineNum = new ArrayList<>();
        List<HikCameraResVo> cameraList = this.cameraMapper.selectAllProjTypeStatus();
        cameraList = cameraList == null ? Collections.emptyList() : cameraList;
        cameraList.removeAll(Collections.singleton(null));
        Map<String, List<HikCameraResVo>> cameraMap = cameraList.stream().filter(o -> StringUtils.isNotBlank(o.getProjectType())).collect(Collectors.groupingBy(o -> o.getProjectType()));
        Map<String, ChartResVo<String, Long>> onlineMap = new HashMap<>(projectTypeList.size());
        Map<String, ChartResVo<String, Long>> offlineMap = new HashMap<>(projectTypeList.size());
        for (String projectType : projectTypeList) {
            List<HikCameraResVo> tempList = cameraMap.getOrDefault(projectType, Collections.emptyList());
            totalNum.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, projectType)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempList.size())))
                    .build());

            Map<Integer, List<HikCameraResVo>> tempMap = tempList.stream().filter(o -> o.getStatus() != null).collect(Collectors.groupingBy(o -> o.getStatus()));


            onlineMap.put(projectType, Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, projectType)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(1, Collections.emptyList()).size())))
                    .build());
            offlineMap.put(projectType, Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, projectType)
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(0, Collections.emptyList()).size())))
                    .build());
        }
        totalNum = totalNum.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
        for (ChartResVo<String, Long> temp : totalNum) {
            onlineNum.add(onlineMap.get(temp.getKey()));
            offlineNum.add(offlineMap.get(temp.getKey()));
        }
        result.setOfflineNum(offlineNum);
        result.setTotalNum(totalNum);
        result.setOnlineNum(onlineNum);
        return result;
    }

    @Override
    public HikCameraStatsResVo statsCameraByAdcd() {
        HikCameraStatsResVo result = new HikCameraStatsResVo();
        QueryWrapper<AttAdcdBase> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.notIn("adcd", Arrays.asList("330351000000",
                "330353000000",
                "330354000000",
                "330355000000"));
        queryWrapper = queryWrapper.eq("del_flag", 0);
        queryWrapper = queryWrapper.eq("parent_adcd", "330300000000");
        List<AttAdcdBase> adcdList = adcdMapper.selectList(queryWrapper);
        List<HikCameraResVo> cameraList = this.cameraMapper.selectAllStatusAdcdBasin();
        cameraList = cameraList == null ? Collections.emptyList() : cameraList;
        List<ChartResVo<String, Long>> onlineNum = new ArrayList<>();
        List<ChartResVo<String, Long>> totalNum = new ArrayList<>();
        List<ChartResVo<String, Long>> offlineNum = new ArrayList<>();
        cameraList.removeAll(Collections.singleton(null));
        Map<String, List<HikCameraResVo>> cameraMap = cameraList.stream()
                .filter(o -> StringUtils.isNotBlank(o.getAreaCode()))
                .collect(Collectors.groupingBy(o -> o.getAreaCode()));
        Map<String, ChartResVo<String, Long>> onlineMap = new HashMap<>(adcdList.size());
        Map<String, ChartResVo<String, Long>> offlineMap = new HashMap<>(adcdList.size());
        for (AttAdcdBase adcd : adcdList) {
            List<HikCameraResVo> tempList = cameraMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            totalNum.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, adcd.getAdnm())
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempList.size())))
                    .build());

            Map<Integer, List<HikCameraResVo>> tempMap = tempList.stream().filter(o -> o.getStatus() != null).collect(Collectors.groupingBy(o -> o.getStatus()));

            onlineMap.put(adcd.getAdnm(), Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, adcd.getAdnm())
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(1, Collections.emptyList()).size())))
                    .build());
            offlineMap.put(adcd.getAdnm(), Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, adcd.getAdnm())
                    .with(ChartResVo::setValue, Long.parseLong(String.valueOf(tempMap.getOrDefault(0, Collections.emptyList()).size())))
                    .build());
        }
        totalNum = totalNum.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
        for (ChartResVo<String, Long> temp : totalNum) {
            onlineNum.add(onlineMap.get(temp.getKey()));
            offlineNum.add(offlineMap.get(temp.getKey()));
        }
        result.setOfflineNum(offlineNum);
        result.setTotalNum(totalNum);
        result.setOnlineNum(onlineNum);
        return result;
    }

    @Override
    public PageVO<HikOpsQuaResVo> pageCameraOpsStatus(HikCameraQueryReqVo reqVo) {
        PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        PageVO<HikOpsQuaResVo> result = PageUtils.getPageVO(this.cameraMapper.selectCamsProjOpsQua(reqVo));
        List<HikOpsQuaResVo> tempList = result.getList();
        if (CollectionUtils.isBlank(tempList)) {
            return result;
        }
//        Map<String, Date> warnMap = new HashMap<>(tempList.size());
//        SysWarnGroup warnGroup = warningMapper.selectGroupByClassName(HikvisionCameraRecord.class.getName());
//        if (warnGroup != null) {
//            String gbCameraIndexCodes = tempList.stream()
//                    .map(HikOpsQuaResVo::getGbIndexCode).collect(Collectors.joining(","));
//            List<RelWarnTemplateObj> relations = warningMapper.selectRelWarnTemplateObjByObjIdsGroupId(gbCameraIndexCodes, warnGroup.getId());
//            if (CollectionUtils.isNotBlank(relations)) {
//                List<SysWarnLog> warnLogs = warningMapper.selectNewsestWarnLogByObjWarnId(relations.stream()
//                        .map(o -> o.getId().toString()).collect(Collectors.joining(",")));
//                Map<Long, Date> tempMap = warnLogs.stream().collect(Collectors.toMap(SysWarnLog::getWarnObjId, SysWarnLog::getWarningTime));
//                for (RelWarnTemplateObj relation : relations) {
//                    warnMap.put(relation.getObjId(), tempMap.get(relation.getId()));
//                }
//            }
//        }
        Map<String, Date> warnMap = Collections.emptyMap();
        List<CameraWarnRecord> cameraWarnRecords = cameraMapper.selectNewestWarnTime("camera_warn_record_" + LocalDate.now().getYear(), tempList.stream()
                .map(HikOpsQuaResVo::getCameraIndexCode).collect(Collectors.toList()));
        if (CollectionUtils.isNotBlank(cameraWarnRecords)) {
            warnMap = cameraWarnRecords.stream().collect(Collectors.toMap(CameraWarnRecord::getCameraIndexCode, CameraWarnRecord::getWarnTime));
        }

        String cameraIndexCodes = tempList.stream()
                .map(HikOpsQuaResVo::getCameraIndexCode).collect(Collectors.joining(","));
        List<AttCameraOnline> onlines = this.cameraMapper.selectCameraOnline(cameraIndexCodes, DateUtils.localDate2Date(LocalDate.now()));
        Map<String, Long> checkTimesMap = Collections.emptyMap();
        Map<String, Long> onlineTimesMap = Collections.emptyMap();
        Integer online = 1;
        if (CollectionUtils.isNotBlank(onlines)) {
            checkTimesMap = onlines.stream().collect(Collectors.groupingBy(AttCameraOnline::getIndexCode, Collectors.counting()));
            onlineTimesMap = onlines.stream().filter(o -> online.equals(o.getOnline())).collect(Collectors.groupingBy(AttCameraOnline::getIndexCode, Collectors.counting()));
        }
        for (HikOpsQuaResVo temp : tempList) {
            temp.setOnlineTimes(onlineTimesMap.getOrDefault(temp.getCameraIndexCode(), 0L).intValue());
            temp.setCheckOnlineTimes(checkTimesMap.getOrDefault(temp.getCameraIndexCode(), 0L).intValue());
            if (temp.getOnlineTimes() != null && temp.getCheckOnlineTimes() != null && temp.getCheckOnlineTimes() > 0) {
                temp.setOnlinePercent((double) (temp.getOnlineTimes() * 100 / temp.getCheckOnlineTimes()));
            }
//            temp.setLastWarnTime(warnMap.get(temp.getGbIndexCode()));
            temp.setLastWarnTime(warnMap.get(temp.getCameraIndexCode()));
        }
        return result;
    }

    /**
     * 更新摄像头预警规则
     *
     * @param cameraIndexCode 摄像头唯一编码
     * @param isWarnNotify    是否报警
     * @param warnNotifyRule  预警规则，1，每小时，2，每日，3，每周，4，每月
     * @return
     */
    @Override
    public int updateCameraWarnNotifyRule(String cameraIndexCode, Boolean isWarnNotify, Integer warnNotifyRule) {
        CameraWarnRuleEnum ruleEnum = CameraWarnRuleEnum.candidateByRuleCode(warnNotifyRule);
        if (StringUtils.isBlank(cameraIndexCode) || isWarnNotify == null || ruleEnum == null) {
            return -1;
        }
        Date warnSetTime = new Date();
        int result = this.cameraMapper.update(Builder.of(HikvisionCameraRecord::new)
                .with(HikvisionCameraRecord::setCameraIndexCode, cameraIndexCode)
                .with(HikvisionCameraRecord::setIsWarnNotify, isWarnNotify ? 1 : 0)
                .with(HikvisionCameraRecord::setWarnNotifyRule, ruleEnum.getRuleCode())
                .with(HikvisionCameraRecord::setWarnSetTime, warnSetTime)
                .build());
        if (result == 1) {
            cameraWarnNotifyManager.updateTask(cameraIndexCode, warnSetTime, ruleEnum);
        } else if (result > 1) {
            cameraWarnNotifyManager.refresh();
        }
        return result;
    }

    /**
     * 查看摄像头警告任务当前执行状态
     *
     * @param cameraIndexCode 摄像头编码
     * @return
     */
    @Override
    public CameraWarnNotifyTask getWarnTaskState(String cameraIndexCode) {
        return cameraWarnNotifyManager.getTask(cameraIndexCode);
    }

    @Override
    public CommonResult sendCameraStatusToOps(String cameraIndexCode) {
        if (StringUtils.isBlank(cameraIndexCode)) {
            return CommonResult.failed("摄像头编码为空");
        }
        List<HikCameraResVo> cameraResVos = this.cameraMapper.selectCameraInfoByCondition(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setCameraIndexCode, cameraIndexCode)
                .with(HikCameraQueryReqVo::setWithProjInfo, false)
                .with(HikCameraQueryReqVo::setWithOpsInfo, true)
                .build());
        if (CollectionUtils.isBlank(cameraResVos)) {
            return CommonResult.failed("摄像头不存在");
        }
        HikCameraResVo cameraResVo = cameraResVos.get(0);
        if (StringUtils.isBlank(cameraResVo.getOpsId()) || StringUtils.isBlank(cameraResVo.getOpsPhone()) || StringUtils.isBlank(cameraResVo.getNotifyType())) {
            return CommonResult.failed("运维人员不存在");
        }
        try {
            if (!sendMsgV2(cameraResVo, true)) {
                throw new RuntimeException("短信发送失败");
            }
        } catch (Exception e) {
            log.error("短信发送失败:{}", e.getStackTrace());
            return CommonResult.failed("短信发送失败");
        }
        return CommonResult.success("成功");
    }

    @Override
    public List<CameraWarnRecord> historySmsLog(String cameraIndexCode) {
        if (StringUtils.isBlank(cameraIndexCode)) {
            return Collections.emptyList();
        }
        return cameraMapper.selectCameraWarnRecord("camera_warn_record_" + LocalDate.now().getYear(), cameraIndexCode);
//        HikvisionCameraRecord cameraRecord = cameraMapper.load(cameraIndexCode);
//        if (cameraRecord == null) {
//            return Collections.emptyList();
//        }
//        SysWarnGroup warnGroup = warningMapper.selectGroupByClassName(HikvisionCameraRecord.class.getName());
//        if (warnGroup != null) {
//            RelWarnTemplateObj relation = warningMapper.selectRelWarnTemplateObjBy(cameraRecord.getGbIndexCode(), warnGroup.getId());
//            if (relation != null) {
//                return warningMapper.selectWarnLogByWarnObjId(relation.getId());
//            }
//        }
//        return Collections.emptyList();
    }

    @Override
    public List<HikCameraResVo> selectCameras(HikCameraQueryReqVo reqVo) {
        List<HikCameraResVo> list = cameraMapper.selectByCondition(reqVo);
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<HikCameraResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
                comparator = comparator.reversed();
            }
            list = list.stream().sorted(comparator).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<ChartResVo<String, Long>> cameraNumStats(int type) {
        switch (type) {
            case 1: {
                return statsCameraByAdcd().getTotalNum();
            }
            case 2: {
                return statsCameraByBasin().getTotalNum();
            }
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<CountCameraStatus> countCameraStatus() {
        return cameraMapper.countCameraStatus();
    }

    @Deprecated
    private boolean sendMsg(AttCameraOps ops, HikCameraResVo camera) {
        /**
         * 1. 先找到根据预警对象名找到预警组
         * 2. 在根据预警对象记录id，和预警组找到模板id
         * 3. 再根据模板id找到模板正文
         */
        SysWarnGroup warnGroup = warningMapper.selectGroupByClassName(HikvisionCameraRecord.class.getName());

        RelWarnTemplateObj relation = warningMapper.selectRelWarnTemplateObjBy(camera.getGbIndexCode(), warnGroup.getId());

        SysWarnTemplate template = warningMapper.selectWarnTemplateById(relation.getTemplateId());

//        摄像头下线预警：#{T}，#{P}-#{C}已下线，请注意及时运维。
        String msg = template.getContent();
        msg = msg.replace("#{T}", LocalDateTime.now().format(smsDtf));
        msg = msg.replace("#{P}", camera.getProjectName() == null ? "" : camera.getProjectName());
        msg = msg.replace("#{C}", camera.getCameraName());
        SysWarnLog warnLog = new SysWarnLog();
        warnLog.setReceiveUserId(ops.getId());
        warnLog.setReceivePhone(ops.getOpsPhone());
        warnLog.setChecked("-1");
        warnLog.setMsgType("100");
        warnLog.setWarnObjId(relation.getId());
        warnLog.setChecked("-1");
        warnLog.setGroupId(warnGroup.getId());
        warnLog.setMsg(msg);
        CommonResult result = smsClient.addOrUpdate(warnLog);
        log.info("短信发送结果：{}", result.toString());
        return CommonResult.success().getCode() == result.getCode();
    }

    /**
     * 发送预警短信
     *
     * @param cameraIndexCode
     * @param forceSend
     * @return
     */
    @Override
    public boolean sendMsgV2(String cameraIndexCode, boolean forceSend) {
        List<HikCameraResVo> cameraResVos = this.cameraMapper.selectCameraInfoByCondition(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setCameraIndexCode, cameraIndexCode)
                .with(HikCameraQueryReqVo::setWithProjInfo, false)
                .with(HikCameraQueryReqVo::setWithOpsInfo, true)
                .build());
        if (CollectionUtils.isBlank(cameraResVos)) {
            return false;
        }
        HikCameraResVo cameraResVo = cameraResVos.get(0);
        if (StringUtils.isBlank(cameraResVo.getOpsId()) || StringUtils.isBlank(cameraResVo.getOpsPhone()) || StringUtils.isBlank(cameraResVo.getNotifyType())) {
            return false;
        }
        return sendMsgV2(cameraResVo, forceSend);
    }

    public boolean sendMsgV2(HikCameraResVo camera, boolean forceSend) {
        if (onlineStatus.equals(camera.getStatus())) {
            if (!forceSend) {
                return true;
            }
        }
        if (!doWarnNotify.equals(camera.getIsWarnNotify())) {
            if (!forceSend) {
                return true;
            }
        }
        String content = "您好，当前设备 " + camera.getCameraName() + " 已下线，请注意及时运维！";
        SysSendMessageLog message = SysSendMessageLogCreator.createMessage(content, camera.getOpsPhone(),
                StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, camera.getOpsName(),
                Optional.ofNullable(SysSendMessageLog.MessageSendType.candidateType(camera.getNotifyType()))
                        .orElse(SysSendMessageLog.MessageSendType.SMS).type(),
                "视频监控",
                SysSendMessageLog.SystemType.PROJECT_MANAGE.type(), SysSendMessageLog.messageType.WARNING.type());

        CommonResult resp = smsClient.sendMessage(Collections.singletonList(message));
        boolean sendState = CommonCode.SUCCESS.code() == resp.getCode();
        if (sendState) {
            this.cameraMapper.insertCameraWarnRecord(Builder.of(CameraWarnRecord::new)
                    .with(CameraWarnRecord::setCameraIndexCode, camera.getCameraIndexCode())
                    .with(CameraWarnRecord::setRegionCameraHash, camera.getRegionCameraHash())
                    .with(CameraWarnRecord::setWarnTime, new Date())
                    .with(CameraWarnRecord::setContent, content)
                    .with(CameraWarnRecord::setOpsName, camera.getOpsName())
                    .with(CameraWarnRecord::setOpsPhone, camera.getOpsPhone())
                    .with(CameraWarnRecord::setNotifyType, camera.getNotifyType())
                    .build());
        }
        return sendState;
    }

    private List<String> getCascadeRegionIndexCode(String parentRegionIndexCode) {
        /**
         * 1. 第一步 查询父id的region_path
         * 2. 第二步 查询根据父id的region_path cha查询所有的index_code
         */
        if (StringUtils.isBlank(parentRegionIndexCode)) {
            return Collections.emptyList();
        }

        QueryWrapper<AttCameraRegion> oneQuery = new QueryWrapper();
        oneQuery.eq("index_code", parentRegionIndexCode);
        AttCameraRegion region = regionMapper.selectOne(oneQuery);
        if (region == null) {
            return Collections.emptyList();
        }
        QueryWrapper<AttCameraRegion> twoQuery = new QueryWrapper().select();
        twoQuery = twoQuery.select("index_code");
        twoQuery = twoQuery.like("region_path", region.getRegionPath() + "%");
        List<AttCameraRegion> tempList = regionMapper.selectList(twoQuery);
        if (tempList == null) {
            tempList = Collections.emptyList();
        }
        return tempList.stream().map(AttCameraRegion::getIndexCode).collect(Collectors.toList());
    }

    /**
     * 摄像头根据行政区划统计，用于新版一张图
     *
     * @param sortType 1. 按总数，2. 按在线数，3. 按离线数，4. 按在线率 降序
     * @return
     */
    @Override
    public JSONObject cameraStatusByAdcd4Amap(Integer sortType) {
        if (sortType == null) {
            sortType = 1;
        }
        HikCamStatsResVo tempResult = new HikCamStatsResVo();
        QueryWrapper<AttAdcdBase> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.notIn("adcd", Arrays.asList("330351000000",
                "330353000000",
                "330354000000",
                "330355000000"));
        queryWrapper = queryWrapper.eq("del_flag", 0);
        queryWrapper = queryWrapper.eq("parent_adcd", "330300000000");
        List<AttAdcdBase> adcdList = adcdMapper.selectList(queryWrapper);
        List<HikCameraResVo> cameraList = this.cameraMapper.selectAllStatusAdcdBasin();
        cameraList = cameraList == null ? Collections.emptyList() : cameraList;
        tempResult.setTotalNum(cameraList.size());
        // 一定要取出总size后 去空
        cameraList.removeAll(Collections.singleton(null));
        tempResult.setOnlineNum((int) cameraList.stream().filter(o -> onlineStatus.equals(o.getStatus())).count());
        tempResult.setOfflineNum((int) cameraList.stream().filter(o -> offlineStatus.equals(o.getStatus())).count());
        tempResult.computeOnlineRate();
        tempResult.setAreaName("温州市");
        tempResult.setAreaCode("330300000000");

        List<HikCamStatsResVo> statsList = new ArrayList<>(adcdList.size());

        Map<String, List<HikCameraResVo>> cameraMap = cameraList.stream()
                .filter(o -> StringUtils.isNotBlank(o.getAreaCode()))
                .collect(Collectors.groupingBy(HikvisionCameraRecord::getAreaCode));
        for (AttAdcdBase adcd : adcdList) {
            List<HikCameraResVo> tempList = cameraMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            HikCamStatsResVo statsTemp = new HikCamStatsResVo();
            statsTemp.setTotalNum(tempList.size());
            statsTemp.setOnlineNum((int) tempList.stream().filter(o -> onlineStatus.equals(o.getStatus())).count());
            statsTemp.setOfflineNum((int) tempList.stream().filter(o -> offlineStatus.equals(o.getStatus())).count());
            statsTemp.computeOnlineRate();
            statsTemp.setAreaName(adcd.getAdnm());
            statsTemp.setAreaCode(adcd.getAdcd());
            statsList.add(statsTemp);
        }
        JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(tempResult, SerializerFeature.WriteMapNullValue));
        Comparator<HikCamStatsResVo> comparator = null;
        switch (sortType) {
            case 1:
                comparator = Comparator.comparing(HikCamStatsResVo::getTotalNum).reversed();
                break;
            case 2:
                comparator = Comparator.comparing(HikCamStatsResVo::getOnlineNum).reversed();
                break;
            case 3:
                comparator = Comparator.comparing(HikCamStatsResVo::getOfflineNum).reversed();
                break;
            case 4:
                comparator = Comparator.comparing(HikCamStatsResVo::getOnlineRate).reversed();
                break;
            default:
                break;
        }
        if (comparator != null) {
            statsList.sort(comparator);
        }
        result.put("statsList", statsList);
        return result;
    }

    /**
     * 列出摄像头绑定对象
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<CamBindObjResVo> listCameraBindObj(CamBindObjReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null)
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        return PageUtils.getPageVO(cameraMapper.selectBindObjByCondition(reqVo));
    }

    /**
     * 列出视频绑定对象类型
     *
     * @return
     */
    @Override
    public List<String> listAllCameraBindObjType() {
        return cameraMapper.selectCameraBindObjType(null);
    }
}
