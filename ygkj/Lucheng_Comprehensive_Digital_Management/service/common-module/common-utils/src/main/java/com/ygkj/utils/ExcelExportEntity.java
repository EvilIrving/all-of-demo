package com.ygkj.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author xq
 * @Description
 * @Date 2021/9/15
 */
@Data
public class ExcelExportEntity {
    String sheetName;
    List<?> list;
    String[] heads;
    String[] cols;
    Map<String, Function<Object, Object>> mappingMap;

    public ExcelExportEntity() {
    }

    public ExcelExportEntity(String sheetName,
                             List<?> list,
                             String[] heads, String[] cols,
                             Map<String, Function<Object, Object>> mappingMap) {
        this.sheetName = sheetName;
        this.list = list;
        this.heads = heads;
        this.cols = cols;
        this.mappingMap = mappingMap;
    }
}
