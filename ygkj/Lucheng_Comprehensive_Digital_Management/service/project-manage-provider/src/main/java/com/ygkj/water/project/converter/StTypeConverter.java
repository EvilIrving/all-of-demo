package com.ygkj.water.project.converter;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class StTypeConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(value == null){
            return new CellData("");
        }
        switch (value) {
            case "RR":
                return new CellData("水库水位");
            case "ZZ":
                return new CellData("河道水位");
            case "DD":
                return new CellData("堰闸水位");
            case "TT":
                return new CellData("潮位");
            case "PP":
                return new CellData("雨量");
            case "SC":
                return new CellData("视频");
        }
        return new CellData("");
    }
}
