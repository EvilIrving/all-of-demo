package com.ygkj.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author xq
 * 
 * @Date 2021/1/28
 */
public class ExcelUtils {

    final static String EMPTY_STR="";

    public static Object getValueByRightTypeCell(Cell cell) {
        Object object = null;
        switch (cell.getCellType()) {
            case STRING: {
                object = cell.getStringCellValue();
                break;
            }
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    object = cell.getDateCellValue();
                } else {
                    object = cell.getNumericCellValue();
                }
                break;
            }
            case FORMULA: {
                object = cell.getCellFormula();
                break;
            }
            case BLANK: {
                object = cell.getStringCellValue();
                break;
            }
            case BOOLEAN:
                object = cell.getBooleanCellValue();
                break;
            case ERROR:
                object = cell.getErrorCellValue();
                break;
            case _NONE:
                object = cell.getErrorCellValue();
                break;
            default:
                break;
        }
        return object;
    }

    public static void setSuitableValue(Cell cell, Object value) {
        if (cell == null) {
            return;
        }
        if (value instanceof Number) {
            if (value instanceof Float) {
                cell.setCellValue(BigDecimal.valueOf((Float) value).doubleValue());
//                CellStyle cellStyle= cell.getCellStyle();
//                DataFormat dataFormat= cell.getSheet().getWorkbook().createDataFormat();
//                cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
            } else if (value instanceof Double) {
                cell.setCellValue(BigDecimal.valueOf((Double) value).doubleValue());
//                CellStyle cellStyle= cell.getCellStyle();
//                DataFormat dataFormat= cell.getSheet().getWorkbook().createDataFormat();
//                cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
//                cell.setCellStyle(cellStyle);
            } else if (value instanceof Byte) {
                cell.setCellValue(BigDecimal.valueOf((Byte) value).doubleValue());
            } else if (value instanceof Short) {
                cell.setCellValue(BigDecimal.valueOf((Short) value).doubleValue());
            } else if (value instanceof Integer) {
                cell.setCellValue(BigDecimal.valueOf((Integer) value).doubleValue());
            } else if (value instanceof Long) {
                cell.setCellValue(BigDecimal.valueOf((Long) value).doubleValue());
            }
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        }
    }

    /**
     * index start with 1,you should plus 1 before you use function.
     *
     * @param index
     * @return
     */
    public static String numIndexToStr(int index) {
        List<Integer> queue = new ArrayList<>();
        int mod = index;
        while (true) {
            if (mod >= 26) {
                queue.add(mod / 26);
                mod = mod % 26;
            } else {
                queue.add(mod);
                break;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int temp : queue) {
            builder.append(Character.toChars(64 + temp));
        }
        return builder.toString();
    }

    /**
     * 1,2,3,4---A,B,C,D
     *
     * @param indexStr
     * @return
     */
    public static int strIndexToNum(String indexStr) {

        if (indexStr!=null&&!EMPTY_STR.equals(indexStr)){

        }else{
            return -1;
        }
        char cha = indexStr.charAt(0);
        if (cha > 47 && cha < 58) {
            return new BigDecimal(indexStr).intValue();
        }
        indexStr = indexStr.toUpperCase();
        Stack<Character> stack = new Stack<>();
        int i = 0;
        while (i < indexStr.length()) {
            char temp = indexStr.charAt(i);
            if (temp > 64 && temp < 91) {
                stack.push(temp);
            }
            i++;
        }
        i = 0;
        int result = 0;
        while (!stack.empty()) {
            Character character = stack.pop();
            result = result + (int) ((character - 64) * Math.pow(26, i));
            i++;
        }
        return result;
    }


    /**
     * 删除列
     *
     * @param sheet
     * @param columnIndex
     */
    public static void removeColumn(Sheet sheet, int columnIndex) {
        if (sheet == null) {
            return;
        }
        if (columnIndex<0){
            return;
        }
        for (Iterator<Row> rowIterator = sheet.rowIterator();
             rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                continue;
            }
            row.removeCell(cell);
        }
    }

    public static void hideColumn(Sheet sheet, int columnIndex) {
        if (sheet == null) {
            return;
        }
        if (columnIndex<0){
            return;
        }
        sheet.setColumnHidden(columnIndex, true);
    }

    public static void clearColumn(Sheet sheet, int columnIndex) {
        if (sheet == null) {
            return;
        }
        if (columnIndex<0){
            return;
        }
        for (Iterator<Row> rowIterator = sheet.rowIterator();
             rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                continue;
            }
            cell.setBlank();
        }
    }

    public static void removeRow(Sheet sheet, int rowIndex) {
        if (sheet == null) {
            return;
        }
        if (rowIndex<0){
            return;
        }
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            sheet.removeRow(row);
        }
    }
    public static void removeRow(Sheet sheet, Row row) {
        if (sheet == null) {
            return;
        }
        if (row != null) {
            sheet.removeRow(row);
        }
    }

    public static void removeRowAutoShift(Sheet sheet, int rowIndex) {
        if (sheet == null) {
            return;
        }

        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);//将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
        }
        if(rowIndex==lastRowNum){
            Row removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    public static void hideRow(Sheet sheet, int rowIndex) {
        if (sheet == null) {
            return;
        }
        if (rowIndex<0){
            return;
        }
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            row.setZeroHeight(true);
        }

    }

    public static void hideRow(Row row) {
        if (row == null) {
            return;
        }
        row.setZeroHeight(true);
    }

    public static void clearRow(Row row) {
        if (row == null) {
            return;
        }
        for (Iterator<Cell> cellIterator = row.cellIterator();
             cellIterator.hasNext(); ) {
            Cell cell = cellIterator.next();
            if (cell == null) {
                continue;
            }
            cell.setBlank();
        }
    }

    public static void clearRow(Sheet sheet, int rowIndex) {
        if (sheet == null) {
            return;
        }
        if (rowIndex<0){
            return;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return;
        }
        for (Iterator<Cell> cellIterator = row.cellIterator();
             cellIterator.hasNext(); ) {
            Cell cell = cellIterator.next();
            if (cell == null) {
                continue;
            }
            cell.setBlank();
        }
    }

    private void cloneCell(Cell cNew, Cell cOld) {
        cNew.setCellComment(cOld.getCellComment());
        cNew.setCellStyle(cOld.getCellStyle());

        if (CellType.BOOLEAN == cNew.getCellTypeEnum()) {
            cNew.setCellValue(cOld.getBooleanCellValue());
        } else if (CellType.NUMERIC == cNew.getCellTypeEnum()) {
            cNew.setCellValue(cOld.getNumericCellValue());
        } else if (CellType.STRING == cNew.getCellTypeEnum()) {
            cNew.setCellValue(cOld.getStringCellValue());
        } else if (CellType.ERROR == cNew.getCellTypeEnum()) {
            cNew.setCellValue(cOld.getErrorCellValue());
        } else if (CellType.FORMULA == cNew.getCellTypeEnum()) {
            cNew.setCellValue(cOld.getCellFormula());
        }
    }


    public static void main(String[] args) {
        System.out.println(strIndexToNum(""));
    }

    public static String getCleanStrValFromCell(Cell cell) {
        if (cell == null) {
            return EMPTY_STR;
        }
        Object tempVal = getValueByRightTypeCell(cell);
        if (tempVal == null) {
            return EMPTY_STR;
        }
        if (tempVal instanceof String) {
            return (String) tempVal;
        } else {
            return String.valueOf(tempVal);
        }
    }

    public static Cell getCell(Sheet sheet, int rowIndex, int cellIndex){
        if (sheet==null){
            return null;
        }
        if (rowIndex<0){
            return null;
        }
        if (cellIndex<0){
            return null;
        }
        Row row=sheet.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        return row.getCell(cellIndex);
    }
}
