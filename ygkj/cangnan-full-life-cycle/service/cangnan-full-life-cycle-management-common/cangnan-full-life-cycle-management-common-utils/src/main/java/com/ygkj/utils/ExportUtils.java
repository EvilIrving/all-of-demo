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
import java.util.Date;
import java.util.List;
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
        exportExcel(list, fileName, heads, cols, response, null);
    }

    /**
     * 导出Excel
     *
     * @param list     需要导出的数据
     * @param fileName 文件名称
     * @param heads    列头
     * @param cols     列名 顺序与列头保持一致
     * @param clazz    数据类
     */
    public static <T> void exportExcel(List<T> list, String fileName, String[] heads, String[] cols, HttpServletResponse response, Class clazz) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
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

                Field[] fields = new Field[len];
                for (int i = 0; i < len; i++) {
                    try {
                        Field field = clazz.getDeclaredField(cols[i]);
                        field.setAccessible(true);
                        fields[i] = field;
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }

                //处理需要导出的数据
                IntStream.range(0, list.size()).forEach(i -> {
                    Row r = sheet.createRow(i + 1);
                    T t = list.get(i);
                    IntStream.range(0, len).forEach(j -> {
                        try {
                            Object value = fields[j].get(t);
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
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

