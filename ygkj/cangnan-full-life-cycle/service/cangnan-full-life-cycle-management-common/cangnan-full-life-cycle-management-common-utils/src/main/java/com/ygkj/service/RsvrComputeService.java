package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.RsvrFeatureConfig;
import com.ygkj.enums.EngScalEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author xq
 * @Description 水库公用计算服务
 * @Date 2021/8/2
 */
public class RsvrComputeService {

    @Autowired
    RsvrFeatureConfig limitRangeConfig;


    public BigDecimal getCurrentLimitLevel(JSONObject rsvr) {
        return getCurrentLimitLevel(rsvr, LocalDate.now());
    }


    public BigDecimal getCurrentLimitLevel(JSONObject rsvr, LocalDate now) {
        EngScalEnum engScalEnum = EngScalEnum.getByLevel(rsvr.getString("engScal"));
        // 0 为正常蓄水水位 1为梅汛限水位  2为台汛限水位
        int limitStatus = 0;
        if (!now.isBefore(limitRangeConfig.getMeiLimitStart().withYear(now.getYear())) && !now.isAfter(limitRangeConfig.getMeiLimitEnd().withYear(now.getYear()))) {
            limitStatus = 1;
        } else if (!now.isBefore(limitRangeConfig.getTyphoonLimitStart().withYear(now.getYear())) && !now.isAfter(limitRangeConfig.getTyphoonLimitEnd().withYear(now.getYear()))) {
            limitStatus = 2;
        }
        if (engScalEnum != null) {
            if (Integer.parseInt(engScalEnum.getEngScal()) < 4) {
                switch (limitStatus) {
                    case 1:
                        return rsvr.getBigDecimal("meiFloodLimitWaterLevel");
                    case 2:
                        return rsvr.getBigDecimal("yphoonFloodLlimitWaterLevel");
                    default:
                        return rsvr.getBigDecimal("normWatLev");
                }
            } else {
                return rsvr.getBigDecimal("normWatLev");
            }
        }
        return null;
    }

    public BigDecimal getCurrentLimitLevel(Object rsvr, LocalDate now) {
        try {
            Field[] fields = rsvr.getClass().getDeclaredFields();
            Field engScalField = null;
            Field meiFloodLimitWaterLevelField = null;
            Field yphoonFloodLlimitWaterLevelField = null;
            Field normWatLevField = null;
            for (Field field : fields) {
                String name = field.getName();
                if (name.equals("engScal")) {
                    engScalField = field;
                    field.setAccessible(true);
                } else if (name.equals("meiFloodLimitWaterLevel")) {
                    meiFloodLimitWaterLevelField = field;
                    field.setAccessible(true);
                } else if (name.equals("yphoonFloodLlimitWaterLevel")) {
                    yphoonFloodLlimitWaterLevelField = field;
                    field.setAccessible(true);
                } else if (name.equals("normWatLev")) {
                    normWatLevField = field;
                    field.setAccessible(true);
                }
            }
            EngScalEnum engScalEnum = EngScalEnum.getByLevel((String) engScalField.get(rsvr));
            // 0 为正常蓄水水位 1为梅汛限水位  2为台汛限水位
            int limitStatus = 0;
            if (!now.isBefore(limitRangeConfig.getMeiLimitStart().withYear(now.getYear())) && !now.isAfter(limitRangeConfig.getMeiLimitEnd().withYear(now.getYear()))) {
                limitStatus = 1;
            } else if (!now.isBefore(limitRangeConfig.getTyphoonLimitStart().withYear(now.getYear())) && !now.isAfter(limitRangeConfig.getTyphoonLimitEnd().withYear(now.getYear()))) {
                limitStatus = 2;
            }
            if (engScalEnum != null) {
                if (Integer.parseInt(engScalEnum.getEngScal()) < 4) {
                    switch (limitStatus) {
                        case 1:
                            return (BigDecimal) meiFloodLimitWaterLevelField.get(rsvr);
                        case 2:
                            return (BigDecimal) yphoonFloodLlimitWaterLevelField.get(rsvr);
                        default:
                            return (BigDecimal) normWatLevField.get(rsvr);
                    }
                } else {
                    return (BigDecimal) normWatLevField.get(rsvr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
