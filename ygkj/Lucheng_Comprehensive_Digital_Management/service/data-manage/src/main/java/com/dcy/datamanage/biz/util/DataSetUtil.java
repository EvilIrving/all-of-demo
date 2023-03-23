package com.dcy.datamanage.biz.util;

import com.dcy.datamanage.api.model.BusDataSetResField;
import com.dcy.datamanage.biz.constant.DataSetFieldType;
import com.dcy.datamanage.biz.dto.input.BusDataSetPreviewInputDTO;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataSetUtil {

    public static Map<String, BusDataSetResField> getOldFieldMap(BusDataSetPreviewInputDTO dto) {
        List<BusDataSetResField> oldList = dto.getFieldList();
        Map<String, BusDataSetResField> oldMap = CollectionUtils.isEmpty(oldList) ? Collections.emptyMap() :
                oldList.stream().collect(Collectors.toMap(BusDataSetResField::getFieldName, Function.identity()));
        return oldMap;
    }

    public static BusDataSetResField busDataSetResField(String key, Object value, Map<String, BusDataSetResField> oldMap,
                                                        List<BusDataSetResField> busDataSetResFieldList, Map<String, BusDataSetResField> completeMap,
                                                        Map<String, BusDataSetResField> pendingMap, Function<Object, Integer> function) {
        //如果已完成的缓存有就直接从已完成的缓存返回
        if (completeMap.containsKey(key)) {
            return completeMap.get(key);
        }

        BusDataSetResField busDataSetResField;
        //查询是否有待完成的
        if (pendingMap.containsKey(key)) {
            busDataSetResField = pendingMap.get(key);
            //值不为空，判断类型，删除待完成，存入已完成
            if (value != null) {
                Integer type = function.apply(value);
                busDataSetResField.setFieldType(type);
                pendingMap.remove(key);
                completeMap.put(key, busDataSetResField);
            }
        } else {
            //之前有相同的字段用之前的
            if (oldMap.containsKey(key)) {
                busDataSetResField = oldMap.get(key);
            } else {
                //完全新的初始化一个
                busDataSetResField = new BusDataSetResField();
                busDataSetResField.setFieldName(key);
            }
            if (value == null) {
                //值为空默认类型,放到待处理的map中
                busDataSetResField.setFieldType(DataSetFieldType.UNKNOWN);
                pendingMap.put(key, busDataSetResField);
            } else {
                //值不为空判断类型,放到完成的map中
                busDataSetResField.setFieldType(function.apply(value));
                completeMap.put(key, busDataSetResField);
            }

            busDataSetResFieldList.add(busDataSetResField);
        }
        return busDataSetResField;
    }
}
