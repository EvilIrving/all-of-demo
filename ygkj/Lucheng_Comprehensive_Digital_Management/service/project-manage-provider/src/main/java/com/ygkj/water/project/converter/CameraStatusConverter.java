package com.ygkj.water.project.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.NumberUtils;

import java.text.ParseException;

public class CameraStatusConverter implements Converter<Integer> {
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    public Integer convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws ParseException {
        return NumberUtils.parseInteger(cellData.getStringValue(), contentProperty);
    }

    public CellData convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if(value == null){
            return new CellData("");
        }else if(value == 1){
            return new CellData("在线");
        }else {
            return new CellData("离线");
        }
    }
}

