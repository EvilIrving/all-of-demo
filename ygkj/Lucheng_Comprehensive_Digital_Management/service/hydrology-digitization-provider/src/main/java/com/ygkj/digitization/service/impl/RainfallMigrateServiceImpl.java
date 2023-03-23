package com.ygkj.digitization.service.impl;

import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.RainfallMigrateMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.RainfallMigrate;
import com.ygkj.digitization.service.RainfallMigrateService;
import com.ygkj.digitization.vo.response.RainfallMigrateResVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 降雨迁移过程 已废弃
 * @Date 2021/2/6
 */
@Deprecated
@Service
public class RainfallMigrateServiceImpl implements RainfallMigrateService {

    @Resource
    RainfallMigrateMapper rainfallMigrateServiceMapper;

    @Resource
    AttStBaseMapper attStBaseMapper;

    @Override
    public List<RainfallMigrateResVo> getByHour(Date hourDate) {
        hourDate= DateUtils.clearDateTimePrecision(hourDate, Calendar.MINUTE,Calendar.SECOND,Calendar.MILLISECOND);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(hourDate);
        calendar.set(2021,1,7);
        hourDate=calendar.getTime();
        List<AttStBase> stBases=attStBaseMapper.selectByType("PP");
        if(stBases!=null&&!stBases.isEmpty()){
            List<RainfallMigrateResVo> result=new ArrayList<>(stBases.size());
            List<String> stCodes=stBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
            List<RainfallMigrate> migrateData=rainfallMigrateServiceMapper.selectByTmStcd(hourDate,stCodes);
            if (CollectionUtils.isBlank(migrateData)){
                migrateData=Collections.emptyList();
            }
            Map<String,RainfallMigrate> migrateMap=migrateData.stream().collect(Collectors.toMap(RainfallMigrate::getStcd,o->o));
            for (AttStBase temp : stBases) {
                RainfallMigrateResVo res=new RainfallMigrateResVo();
                res.setStationName(temp.getStName());
                res.setStcd(temp.getStCode());
                res.setLatitude(temp.getStLat()==null?null:temp.getStLat().doubleValue());
                res.setLongitude(temp.getStLong()==null?null:temp.getStLong().doubleValue());
                res.setStType(temp.getStType());
                RainfallMigrate base=migrateMap.get(res.getStcd());
                if (base!=null){
                    res.setMgstcd(base.getMgstcd());
                    res.setDrp(base.getDrp());
                    res.setTm(base.getTm());
                    res.setIntv(base.getIntv());
                    res.setUploadTime(base.getUploadTime());
                }
                result.add(res);
            }
            return result;
        }else{
            return Collections.EMPTY_LIST;
        }
    }
}
