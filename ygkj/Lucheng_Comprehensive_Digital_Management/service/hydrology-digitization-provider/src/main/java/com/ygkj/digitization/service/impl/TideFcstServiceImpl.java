package com.ygkj.digitization.service.impl;

import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.AttTideFcstBaseMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.AttTideFcstBase;
import com.ygkj.digitization.service.TideFcstService;
import com.ygkj.digitization.vo.request.FcstValReqVo;
import com.ygkj.digitization.vo.request.TideFcstQueryVo;
import com.ygkj.digitization.vo.response.*;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Service
public class TideFcstServiceImpl implements TideFcstService, InitializingBean {

    @Resource
    AttTideFcstBaseMapper tideFcstBaseMapper;

    @Resource
    GenerateDistributionIDService idService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    AttStBaseMapper stBaseMapper;
    LinkedHashMap<String, String> stationMap;

    /**
     * 查询风暴潮预报列表
     *
     * @param queryVo
     * @return
     */
    @Override
    public PageVO<TideFcstResVo> pageTideFcst(TideFcstQueryVo queryVo) {
        if (queryVo.getReleaseDate() != null) {
            queryVo.setReleaseStartTm(DateUtils.clearDateTimePrecision(queryVo.getReleaseDate(), Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND));
            queryVo.setReleaseEndTm(new Date(queryVo.getReleaseStartTm().getTime() + 24 * 3600 * 1000));
        }
        queryVo.page();
        PageVO<TideFcstResVo> result = PageUtils.getPageVO(this.tideFcstBaseMapper.selectByCondition(queryVo));
        return result;
    }

    /**
     * 新增或更新风暴潮预报
     *
     * @param resVo
     * @return
     */
    @Override
    public CommonResult addOrUpdateTideFcst(TideFcstResVo resVo) {
        resVo.setReleaseType(1);
        if (StringUtils.isNotBlank(resVo.getId())) {
            updateFcst(resVo);
        } else {
            addFcst(resVo);
        }
        return CommonResult.success();
    }


    public void addFcst(TideFcstResVo resVo) {
        String fcstId = idService.nextID();
        List<TideFcstValResVo> manualValueList = resVo.getManualValueList();
        if (CollectionUtils.isNotBlank(manualValueList)) {
            Date first = manualValueList.get(0).getTm();
            Date startDate = first;
            Date endDate = first;
            for (TideFcstValResVo tideFcstValResVo : manualValueList) {
                Date date = tideFcstValResVo.getTm();
                if (date.after(endDate)) {
                    endDate = date;
                }
                if (date.before(startDate)) {
                    startDate = date;
                }
            }
            resVo.setFcstStart(sdf.format(startDate));
            resVo.setFcstEnd(sdf.format(endDate));
        }

        resVo.setId(fcstId);
        tideFcstBaseMapper.insert(resVo);
        batchSaveVal(resVo.getManualValueList(), fcstId);
    }

    public void updateFcst(TideFcstResVo resVo) {
        String fcstId = resVo.getId();
        tideFcstBaseMapper.update(resVo);
        List<TideFcstValResVo> manualValList = resVo.getManualValueList();
        this.tideFcstBaseMapper.deleteValByByFcstId(fcstId);
        batchSaveVal(manualValList, fcstId);
    }

    private void batchSaveVal(List<TideFcstValResVo> manualValList, String fcstId) {
        if (CollectionUtils.isNotBlank(manualValList)) {
            manualValList.forEach(tideFcstValResVo -> {
                tideFcstValResVo.setFcstId(fcstId);
                tideFcstValResVo.setStCode("06896");
            });
            tideFcstBaseMapper.batchInsertVal(manualValList);
        }
    }

    /**
     * 根据id风暴潮预报
     *
     * @param id
     * @return
     */
    @Override
    public TideFcstResVo getTideFcst(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        AttTideFcstBase temp = this.tideFcstBaseMapper.load(id);
        if (temp == null) {
            return null;
        }
        TideFcstResVo result = new TideFcstResVo();
        BeanUtils.copyProperties(temp, result);
        List<TideFcstValResVo> hydrFcstValList = this.tideFcstBaseMapper.selectManualValByFcstId(id);
        result.setManualValueList(hydrFcstValList);
        return result;
    }

    /**
     * 删除风暴潮预报
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult deleteTideFcst(String id) {
        if (StringUtils.isBlank(id)) {
            return CommonResult.failed("参数为空");
        }
        this.tideFcstBaseMapper.deleteValByByFcstId(id);
        this.tideFcstBaseMapper.delete(id);
        return CommonResult.success();
    }

    /**
     * 手动报警潮位列表
     *
     * @return
     */
    @Override
    public List<AttStBase> manualFcstTideList() {
        List<AttStBase> stationList = this.stBaseMapper.selectByStCodeList(new ArrayList<>(stationMap.keySet()));
        List<AttStBase> result = new ArrayList<>(stationList.size());
        if (CollectionUtils.isNotBlank(stationList)) {
            Map<String, AttStBase> attStBaseMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
            stationMap.forEach((stCode, stName) -> {
                AttStBase temp = attStBaseMap.get(stCode);
                if (temp != null) {
                    temp.setStName(stName);
                    result.add(temp);
                }
            });
        }
        return result;
    }

    /**
     * 根据预报id查询风暴潮列表
     *
     * @param fcstId
     * @return
     */
    @Override
    public List<AttTideFcstValResVo> listTileFcst(String fcstId) {
        return tideFcstBaseMapper.listFcstVal(fcstId);
    }

    /**
     * 查询最新一项风暴潮手动预报
     *
     * @param releaseType 发布类型1：手动预报，2自动预报
     * @return
     */
    @Override
    public TideFcstResVo newestTideFcst(Integer releaseType) {
        if (releaseType == null) {
            releaseType = 1;
        }
        // todo 尚未接入风暴潮，先这么做
        if (!releaseType.equals(1)) {
            return null;
        }
        TideFcstResVo result = this.tideFcstBaseMapper.selectNewestFcst();
        if (result != null) {
            List<TideFcstValResVo> fcstValList = this.tideFcstBaseMapper.selectManualValByFcstId(result.getId());
            fillStationInfo(fcstValList);
            result.setManualValueList(fcstValList);
            for (TideFcstValResVo tideFcstValResVo : fcstValList) {
                tideFcstValResVo.buildWarnState();
            }
        }
        return result;
    }

    private void fillStationInfo(List<TideFcstValResVo> fcstValList) {
        if (CollectionUtils.isBlank(fcstValList)) {
            return;
        }
        List<AttStBase> stationList = this.stBaseMapper.selectByStCodeList(fcstValList.stream()
                .map(TideFcstValResVo::getStCode)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
        fillStationInfo(stationList, fcstValList);
    }

    private void fillStationInfo(List<AttStBase> stationList, List<TideFcstValResVo> fcstValList) {
        if (CollectionUtils.isBlank(fcstValList) || CollectionUtils.isBlank(stationList)) {
            return;
        }
        Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
        for (TideFcstValResVo hydrFcstResVo : fcstValList) {
            AttStBase station = stationMap.get(hydrFcstResVo.getStCode());
            if (station == null) {
                continue;
            }
            hydrFcstResVo.setStLat(station.getStLat());
            hydrFcstResVo.setStName(this.stationMap.getOrDefault(hydrFcstResVo.getStCode(), station.getStName()));
            hydrFcstResVo.setStLong(station.getStLong());
            hydrFcstResVo.setBlueLevel(station.getBlueLevel());
            hydrFcstResVo.setYellowLevel(station.getYellowLevel());
            hydrFcstResVo.setOrangeLevel(station.getOrangeLevel());
            hydrFcstResVo.setRedLevel(station.getRedLevel());
        }
    }

    /**
     * 风暴潮站点列表
     *
     * @param fcstId
     * @return
     */
    @Override
    public List<FcstStResVo> listTileFcstSt(String fcstId) {
        return tideFcstBaseMapper.listTileFcstSt(fcstId);
    }

    /**
     * 风暴潮列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<FcstValResVo> listTileFcstVal(FcstValReqVo reqVo) {
        return tideFcstBaseMapper.listTileFcstVal(reqVo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        stationMap = new LinkedHashMap<>();
        stationMap.put("06896", "温州");
        stationMap.put("10303", "灵昆");
        stationMap.put("01252", "瑞安");
        stationMap.put("01664", "鳌江");
    }
}
