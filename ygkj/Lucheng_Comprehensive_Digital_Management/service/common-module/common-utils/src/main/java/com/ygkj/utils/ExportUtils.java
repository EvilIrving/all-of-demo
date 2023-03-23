package com.ygkj.utils;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ExportUtils {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    /**
     * 导出Excel
     *
     * @param list     需要导出的数据
     * @param fileName 文件名称
     * @param heads    列头
     * @param cols     列名 顺序与列头保持一致
     */
    public static <T> void exportExcel(List<T> list, String fileName, String[] heads, String[] cols, HttpServletResponse response) {
        exportExcel(list, fileName, heads, cols, response, null, null);
    }

    /**
     * 导出Excel
     *
     * @param list     需要导出的数据
     * @param fileName 文件名称
     * @param heads    列头
     * @param cols     列名 顺序与列头保持一致
     */
    public static <T> void exportExcel(List<T> list, String fileName, String[] heads, String[] cols, HttpServletResponse response, Class clazz) {
        exportExcel(list, fileName, heads, cols, response, clazz, null);
    }

    /**
     * 导出Excel
     *
     * @param list       需要导出的数据
     * @param fileName   文件名称
     * @param heads      列头
     * @param cols       列名 顺序与列头保持一致
     * @param clazz      数据类
     * @param mappingMap 属性转换规则
     */
    public static <T> void exportExcel(List<T> list, String fileName,
                                       String[] heads, String[] cols,
                                       HttpServletResponse response, Class clazz,
                                       Map<String, Function<Object, Object>> mappingMap) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            fillData2Sheet(sheet, list, heads, cols, clazz, mappingMap);
            //弹出一个"文件下载"的对话框
            response.setContentType("application/octet-stream");
            //文件名为中文时会乱码，需要转一下
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx"+ ";" + "filename*=utf-8''" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportExcel(List<ExcelExportEntity> exportEntityList,
                                   String fileName,
                                   HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            if (com.ygkj.utils.CollectionUtils.isNotBlank(exportEntityList)) {
                Set<String> sheetNameSet = new HashSet<>(exportEntityList.size());
                for (int i = 0; i < exportEntityList.size(); i++) {
                    ExcelExportEntity entity = exportEntityList.get(i);
                    String sheetName = entity.getSheetName();
                    if (StringUtils.isBlank(sheetName)) {
                        sheetName = "Sheet" + i;
                    }
                    if (sheetNameSet.contains(sheetName)) {
                        sheetName = sheetName.concat("_").concat(String.valueOf(i));
                    }
                    Sheet sheet = workbook.createSheet(sheetName);
                    fillData2Sheet(sheet, entity.getList(), entity.getHeads(), entity.getCols(), null, entity.getMappingMap());
                    sheetNameSet.add(sheetName);
                }
            } else {
                // 空也要创建一个，否则可能会导致excel文档错误
                workbook.createSheet("Sheet1");
            }

            //弹出一个"文件下载"的对话框
            response.setContentType("application/octet-stream");
            //文件名为中文时会乱码，需要转一下
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void fillData2Sheet(Sheet sheet, List<T> list,
                                          String[] heads, String[] cols, Class clazz,
                                          Map<String, Function<Object, Object>> mappingMap) {
        int len = heads.length;
        //需要设置表格样式的可以自己添加，比如宽度自适应、字体颜色等等
        //处理表头
        Row row = sheet.createRow(0);
        IntStream.range(0, len).forEach(i -> row.createCell(i).setCellValue(heads[i]));
        if (!CollectionUtils.isEmpty(list)) {
            //获取可以访问私有属性的一堆field
            if (clazz == null) {
                clazz = list.get(0).getClass();
            }
            Map<Field, Function<Object, Object>> realMappingMap;
            if (mappingMap == null) {
                mappingMap = Collections.emptyMap();
                realMappingMap = Collections.emptyMap();
            } else {
                realMappingMap = new HashMap<>(mappingMap.size());
            }
            Field[] fields = new Field[len];
            for (int i = 0; i < len; i++) {
                Field field = getField(clazz, cols[i]);
                if (field != null) {
                    field.setAccessible(true);
                    Function<Object, Object> mapping = mappingMap.get(cols[i]);
                    if (mapping != null) {
                        realMappingMap.put(field, mapping);
                    }
                }
                fields[i] = field;
            }

            //处理需要导出的数据
            IntStream.range(0, list.size()).forEach(i -> {
                Row r = sheet.createRow(i + 1);
                T t = list.get(i);
                IntStream.range(0, len).forEach(j -> {
//                    StopWatch stopWatch=new StopWatch();
//                    stopWatch.start("task1");
                    try {
                        if (fields[j] != null) {
                            Object value = fields[j].get(t);
                            Function<Object, Object> mapping = realMappingMap.get(fields[j]);
                            if (value != null) {
                                Object finalValue = mapping == null ? value : mapping.apply(value);
                                //一些特殊类型的值可以在这里处理，这里只简单的写了一个日期类型的格式化
                                if (finalValue instanceof Date) {
                                    r.createCell(j).setCellValue(format.format(finalValue));
                                } else {
                                    r.createCell(j).setCellValue(finalValue.toString());
                                }
                            } else {
                                r.createCell(j).setCellValue("");
                            }
                        } else {
                            r.createCell(j).setCellValue("");
                        }
//                        sheet.autoSizeColumn(j);
//                        stopWatch.stop();
//                        System.out.println(stopWatch.prettyPrint());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        }
    }

    /**
     * 查找类中的字段，包括父类
     *
     * @param theClass
     * @param property
     * @return
     */
    private static Field getField(Class theClass, String property) {
        Field field = null;
        try {
            field = theClass.getDeclaredField(property);
        } catch (NoSuchFieldException ignore) {
            // 循环去找父类的属性，直到找到或父类为Object为止
            boolean loop = true;
            Class currentClass = theClass;
            do {
                try {
                    currentClass = currentClass.getSuperclass();
                    field = currentClass.getDeclaredField(property);
                    loop = false;
                } catch (NoSuchFieldException ignore2) {

                }
            } while (loop && currentClass != Object.class);
        }
        return field;
    }

    public static <T> void exportExcel2(List<T> list, String fileName, String[] heads, String[] cols, HttpServletResponse response, Class clazz) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            int len = heads.length;
            //需要设置表格样式的可以自己添加，比如宽度自适应、字体颜色等等
            //处理表头
            Row row = sheet.createRow(0);
            IntStream.range(0, len).forEach(i -> row.createCell(i).setCellValue(heads[i]));
            if (!CollectionUtils.isEmpty(list)) {


                if (clazz == null) {
                    clazz = list.get(0).getClass();
                }

                //获取方法
                Method[] methods = new Method[len];
                for (int i = 0; i < len; i++) {
                    Method method = clazz.getMethod("get" + cols[i].substring(0, 1).toUpperCase() + cols[i].substring(1));
                    methods[i] = method;
                }

                //处理需要导出的数据
                IntStream.range(0, list.size()).forEach(i -> {
                    Row r = sheet.createRow(i + 1);
                    T t = list.get(i);
                    IntStream.range(0, len).forEach(j -> {
                        try {
                            Object value = methods[j].invoke(t);
                            //一些特殊类型的值可以在这里处理，这里只简单的写了一个日期类型的格式化
                            if (value instanceof Date) {
                                r.createCell(j).setCellValue(value == null ? "" : format.format(value));
                            } else {
                                r.createCell(j).setCellValue(value == null ? "" : value.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            //弹出一个"文件下载"的对话框
            response.setContentType("application/octet-stream");
            //文件名为中文时会乱码，需要转一下
            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            //response.setCharacterEncoding("utf-8");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

