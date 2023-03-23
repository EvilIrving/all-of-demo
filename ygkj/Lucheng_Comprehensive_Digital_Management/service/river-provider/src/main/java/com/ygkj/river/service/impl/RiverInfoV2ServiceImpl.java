package com.ygkj.river.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.enums.RiverHealthEnum;
import com.ygkj.river.enums.RiverLevelEnum;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.AttRivLkChiefBaseMapper;
import com.ygkj.river.mapper.AttRivSegBaseMapper;
import com.ygkj.river.mapper.AttRivShapeBaseMapper;
import com.ygkj.river.model.AttAdcdBase;
import com.ygkj.river.model.AttRivSegBase;
import com.ygkj.river.model.AttRivShapeBase;
import com.ygkj.river.service.RiverInfoV2Service;
import com.ygkj.river.utils.CommomUtil;
import com.ygkj.river.utils.GpsChange;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.request.RiverQueryReqVo;
import com.ygkj.river.vo.response.NearestTheRiverRespVo;
import com.ygkj.river.vo.response.RivLkChiefResVo;
import com.ygkj.river.vo.response.RiverBaseInfoResVo;
import com.ygkj.river.vo.response.RiverCoordinateResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
@Service
public class RiverInfoV2ServiceImpl implements RiverInfoV2Service {

    @Resource
    AttRivSegBaseMapper rivSegBaseMapper;

    @Resource
    AttRivShapeBaseMapper rivShapeBaseMapper;

    @Resource
    AttAdcdBaseMaper adcdBaseMapper;

    @Resource
    AttRivLkChiefBaseMapper rivLkChiefBaseMapper;

    Map<Integer, String> levelMap = Builder.of(HashMap<Integer, String>::new)
            .with(HashMap::put, 1, "国控")
            .with(HashMap::put, 2, "省级")
            .with(HashMap::put, 3, "市级")
            .with(HashMap::put, 4, "县级")
            .with(HashMap::put, 5, "镇级")
            .with(HashMap::put, 6, "村级")
            .build();

    Map<Integer, String> healthMap = Builder.of(HashMap<Integer, String>::new)
            .with(HashMap::put, 1, "绿码")
            .with(HashMap::put, 2, "黄码")
            .with(HashMap::put, 3, "红码")
            .build();

    /**
     * 查询河道基础信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RiverBaseInfoResVo> pageRivSegBaseInfo(RiverQueryReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        PageVO<RiverBaseInfoResVo> result = PageUtils.getPageVO(rivSegBaseMapper.selectByCondition(reqVo));
        List<RiverBaseInfoResVo> list = result.getList();
        if (CollectionUtils.isNotBlank(list)) {
            list.forEach(o -> {
                Optional.ofNullable(RiverLevelEnum.getByType(o.getHealthState())).ifPresent(o2 -> o.setLevelName(o2.getDesc()));
            });
        }
        if (reqVo.isWithChiefInfo()) {
            List<RivLkChiefResVo> chiefList = rivLkChiefBaseMapper.selectByRiverId(list.stream().map(RiverBaseInfoResVo::getId).map(String::valueOf).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)), 1);
            if (CollectionUtils.isNotBlank(chiefList)) {
                Map<Integer, List<RivLkChiefResVo>> chiefGroupMap = chiefList.stream().collect(Collectors.groupingBy(RivLkChiefResVo::getRivSegId));
                list.forEach(o -> {
                    o.setRiverChiefList(chiefGroupMap.getOrDefault(o.getId(), Collections.emptyList()));
                });
            }
        }
        return result;
    }

    /**
     * 对分页查询河段基础信息进行统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject pageRivSegStats(RiverQueryReqVo reqVo) {
        int rivSegNum = 0;
        long rivGreenNum = 0;
        long rivYellowNum = 0;
        long rivRedNum = 0;
        JSONObject result = new JSONObject();
        PageVO<RiverBaseInfoResVo> tempPage = pageRivSegBaseInfo(new RiverQueryReqVo());
        List<RiverBaseInfoResVo> tempList = tempPage.getList();
        if (CollectionUtils.isNotBlank(tempList)) {
            rivSegNum = tempList.size();
            rivGreenNum = tempList.stream().filter(o -> RiverHealthEnum.Green.getType().equals(o.getHealthState())).count();
            rivYellowNum = tempList.stream().filter(o -> RiverHealthEnum.Yellow.getType().equals(o.getHealthState())).count();
            rivRedNum = tempList.stream().filter(o -> RiverHealthEnum.Red.getType().equals(o.getHealthState())).count();
        }
        result.put("rivSegNum", rivSegNum);
        result.put("rivGreenNum", rivGreenNum);
        result.put("rivYellowNum", rivYellowNum);
        result.put("rivRedNum", rivRedNum);
        return result;
    }

    /**
     * 河道地图落点
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<RiverCoordinateResVo> riverCoordinate(RiverQueryReqVo reqVo) {
        return rivShapeBaseMapper.selectByCondition(reqVo.getRiverNo());
    }

    /**
     * 河道地图落点
     *
     * @param statsType 1: 根据行政区划统计，2：根据河道等级统计，3：根据健康分析统计
     * @return
     */
    @Override
    public List<SingletonMap<String, Long>> riverStats(Integer statsType) {
        if (statsType == null) {
            return Collections.emptyList();
        }
        switch (statsType) {
            case 1: {
                List<AttAdcdBase> adcdList = adcdBaseMapper.countyOfWenZhou();
                List<SingletonMap<String, Long>> records = this.rivSegBaseMapper.countByAreaCode();
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, adcd.getAdnm())
                            .with(SingletonMap::setValue, recordMap.getOrDefault(adcd.getAdcd(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 2: {
                List<SingletonMap<String, Long>> records = this.rivSegBaseMapper.countByLevel();
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(SingletonMap::getKey, SingletonMap::getValue));
                levelMap.forEach((level, name) -> {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, name)
                            .with(SingletonMap::setValue, recordMap.getOrDefault(level.toString(), 0L))
                            .build());
                });
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 3: {
                List<RiverBaseInfoResVo> records = pageRivSegBaseInfo(new RiverQueryReqVo()).getList();
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<Integer, Long> recordMap = records.stream().filter(o -> o.getHealthState() != null)
                        .collect(Collectors.groupingBy(RiverBaseInfoResVo::getHealthState, Collectors.counting()));
                healthMap.forEach((healthState, name) -> {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, name)
                            .with(SingletonMap::setValue, recordMap.getOrDefault(healthState, 0L))
                            .build());
                });
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<NearestTheRiverRespVo> nearestTheRiver(String riverNo, Double latitude, Double longitude, String address) {
        try {
            AttRivShapeBase attRivShapeBase = rivShapeBaseMapper.findByRiverNo(riverNo);
            if (attRivShapeBase != null) {
                double[] toBd09 = GpsChange.gps84_To_bd09(latitude, longitude);
                double lat = toBd09[0];
                double lng = toBd09[1];
                if (StringUtils.isNotBlank(attRivShapeBase.getRiverPath())) {
                    JSONArray jsonArray = JSON.parseArray(attRivShapeBase.getRiverPath());
                    List<NearestTheRiverRespVo> riverRespVos = new ArrayList<>();
                    NearestTheRiverRespVo nearestTheRiverRespVo = null;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        nearestTheRiverRespVo = new NearestTheRiverRespVo();
                        JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                        BigDecimal jo1 = jsonArray1.getBigDecimal(0);//longitude
                        BigDecimal jo2 = jsonArray1.getBigDecimal(1);//latitude

                        double distance = CommomUtil.getDistance(jo2.doubleValue(), jo1.doubleValue(), lat, lng);
                        nearestTheRiverRespVo.setDistance(new BigDecimal(distance + ""));
                        nearestTheRiverRespVo.setLongitude(jo1.doubleValue());
                        nearestTheRiverRespVo.setLatitude(jo2.doubleValue());
                        riverRespVos.add(nearestTheRiverRespVo);
                    }
                    if (!riverRespVos.isEmpty()) {
                        for (NearestTheRiverRespVo riverRespVo : riverRespVos) {
                            CommomUtil.reverseGeocodGps(riverRespVo.getLatitude().doubleValue(), riverRespVo.getLongitude(), riverRespVo);
                        }
                        riverRespVos = riverRespVos.stream().filter(e -> e.getAddress().contains(address)).sorted(Comparator.comparing(NearestTheRiverRespVo::getDistance)).limit(3).collect(Collectors.toList());
                        return riverRespVos;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据id查询河段详情
     *
     * @param id 河段id
     * @return
     */
    @Override
    public RiverBaseInfoResVo rivSegInfoById(Integer id) {
        if (id == null) {
            return null;
        }
        AttRivSegBase temp = rivSegBaseMapper.load(id);
        if (temp == null) {
            return null;
        }
        RiverBaseInfoResVo result = new RiverBaseInfoResVo();
        BeanUtils.copyProperties(temp, result);
        List<RivLkChiefResVo> chiefList = rivLkChiefBaseMapper.selectByRiverId(String.valueOf(id), 1);
        if (CollectionUtils.isNotBlank(chiefList)) {
            Map<Integer, List<RivLkChiefResVo>> chiefGroupMap = chiefList.stream().collect(Collectors.groupingBy(RivLkChiefResVo::getRivSegId));
            result.setRiverChiefList(chiefGroupMap.getOrDefault(id, Collections.emptyList()));
            result.doTranslate();
        }
        return result;
    }

    /**
     * 更新多项河段健康评价
     *
     * @param reqVoList
     * @return
     */
    @Override
    public synchronized CommonResult updateRivSegHealthState(List<RiverHealthStatReqVo> reqVoList) {
        if (CollectionUtils.isBlank(reqVoList)) {
            return CommonResult.success(StringUtils.EMPTY);
        }
        reqVoList = reqVoList.stream().filter(o -> o != null && o.getId() != null).collect(Collectors.toList());
        if (CollectionUtils.isBlank(reqVoList)) {
            return CommonResult.success("id不能为空");
        }
        List<RiverHealthStatReqVo> existList = this.rivSegBaseMapper.selectHealthStat(reqVoList.stream().map(o -> o.getId().toString()).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        Map<Integer, RiverHealthStatReqVo> existMap = existList.stream().collect(Collectors.toMap(RiverHealthStatReqVo::getId, Function.identity()));
        for (RiverHealthStatReqVo reqVo : reqVoList) {
            RiverHealthStatReqVo exist = existMap.get(reqVo.getId());
            if (exist == null) {
                continue;
            }
            if (reqVo.getNoneFourChaos() == null) {
                reqVo.setNoneFourChaos(exist.getNoneFourChaos() == null ? 1 : exist.getNoneFourChaos());
            }
            if (reqVo.getRiverManageWell() == null) {
                reqVo.setRiverManageWell(exist.getRiverManageWell() == null ? 1 : exist.getRiverManageWell());
            }
            if (reqVo.getShorelineOccupyBalance() == null) {
                reqVo.setShorelineOccupyBalance(exist.getShorelineOccupyBalance() == null ? 1 : exist.getShorelineOccupyBalance());
            }
            if (reqVo.getNoneIllegalRiverCreate() == null) {
                reqVo.setNoneIllegalRiverCreate(exist.getNoneIllegalRiverCreate() == null ? 1 : exist.getNoneIllegalRiverCreate());
            }
            if (reqVo.getRiverProjTallied() == null) {
                reqVo.setRiverProjTallied(exist.getRiverProjTallied() == null ? 1 : exist.getRiverProjTallied());
            }
            if (reqVo.getPublicSatisfy() == null) {
                reqVo.setPublicSatisfy(exist.getPublicSatisfy() == null ? 1 : exist.getPublicSatisfy());
            }
            reqVo.buildHealthState();
            AttRivSegBase target = new AttRivSegBase();
            BeanUtils.copyProperties(reqVo, target);
            rivSegBaseMapper.update(target);
        }
        return CommonResult.success();
    }

    /**
     * 类型 1:数量 2:长度
     *
     * @param type
     * @return
     */
    @Override
    public List<JSONObject> rivSegStaticByArea(String type) {
        if ("2".equals(type)) {
            return rivSegBaseMapper.rivSegStaticLengthByArea();
        }
        return rivSegBaseMapper.rivSegStaticByArea();
    }

    /**
     * 类型 1:数量 2:长度
     *
     * @param type
     * @return
     */
    @Override
    public List<JSONObject> rivSegStaticByAreaLevel(String type) {
        if ("2".equals(type)) {
            return rivSegBaseMapper.rivSegStaticLengthByAreaLevel();
        }
        return rivSegBaseMapper.rivSegStaticByAreaLevel();
    }

    @Override
    public List<JSONObject> rivSegStaticByHealthState() {
        List<JSONObject> jsonObjects = rivSegBaseMapper.rivSegStaticByHealthState();
        if (!jsonObjects.isEmpty()) {
            Integer total = 0;
            for (JSONObject jsonObject : jsonObjects) {
                Integer num = jsonObject.getInteger("num");
                if (num != null) {
                    total += num;
                }
            }
            JSONObject jo = new JSONObject();
            jo.put("healthState", "总数");
            jo.put("num", total);
            jsonObjects.add(jo);
        }
        return jsonObjects;
    }

    @Override
    public JSONObject rivSegCStatic() {
        return rivSegBaseMapper.rivSegCStatic();
    }
}
