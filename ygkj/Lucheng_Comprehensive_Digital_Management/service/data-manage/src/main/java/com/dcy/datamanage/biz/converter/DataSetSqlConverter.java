package com.dcy.datamanage.biz.converter;

import com.dcy.datamanage.biz.constant.DataSetFieldType;
import com.dcy.datamanage.biz.util.DateUtil;
import com.dcy.datamanage.biz.util.NumberUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DataSetSqlConverter {

    public static BiFunction getSqlConverter(Integer fieldType) {
        return map.get(fieldType);
    }


    private static BiFunction<Date, String, String> DateConverter = ((date, format) ->
            format == null ? DateUtil.dateToStr(date, DateUtil.SDF_DATE) : DateUtil.dateToStr(date, format)
    );

    private static BiFunction<Date, String, String> TimeConverter = ((date, format) ->
            format == null ? DateUtil.dateToStr(date, DateUtil.SDF_TIME) : DateUtil.dateToStr(date, format)
    );

    private static BiFunction<Object, String, String> DateTimeConverter = ((object, format) -> {
        Class<?> aClass = object.getClass();
        if (format == null) {
            return aClass == LocalDateTime.class ?
                    DateUtil.dateToStr((LocalDateTime) object, DateUtil.DTF_DATETIME) :
                    DateUtil.dateToStr((Date) object, DateUtil.SDF_DATETIME);
        } else {
            return aClass == LocalDateTime.class ?
                    DateUtil.dateToStr((LocalDateTime) object, format) :
                    DateUtil.dateToStr((Date) object, format);
        }
    });

    private static BiFunction<Number, String, BigDecimal> DecimalConverter = ((number, format) ->
            format == null ? NumberUtil.format(number, NumberUtil.NORMAL) : NumberUtil.format(number, format)
    );

    private static Map<Integer, BiFunction> map = new HashMap<Integer, BiFunction>() {{
        put(DataSetFieldType.DATE, DateConverter);
        put(DataSetFieldType.TIME, TimeConverter);
        put(DataSetFieldType.DATETIME, DateTimeConverter);
        put(DataSetFieldType.DECIMAL, DecimalConverter);
    }};
}
