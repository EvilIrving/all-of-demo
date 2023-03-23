package com.ygkj.water.project.utils;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wgf
 * easyExcel  重新编写根据指定列合并单元格
 * @date 2021/7/14 16:43
 * @Description:
 */
public class EasyExcelUtils implements CellWriteHandler {

    private int[]mergeColumnIndex;

    private int mergeRowIndex;
    // 设置第n列为标杆，列此列上下行数据相同的情况之下 其他列如果相同才合并
    private int standardColumnIndex;
    //标杆列对应的数据
    private ArrayList<String> list;
    //不需要对应标杆的列， 相同就合并
    private int[]Index;
    //表头的行数
    private int ks;

    private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {

        Object curData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();

        Cell preCell = cell.getSheet().getRow(curRowIndex -1).getCell(curColIndex);

        Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();

        // 获取上一行第0列单元格和本行第0列单元格
        Cell preRowFirstCell = cell.getSheet().getRow(curRowIndex -1).getCell(standardColumnIndex);
        Object preRowFirstData = preRowFirstCell.getCellTypeEnum() == CellType.STRING ? preRowFirstCell.getStringCellValue() : preRowFirstCell.getNumericCellValue();
        Object curRowFirstData = null;
        Cell curRowFirstCell = cell.getSheet().getRow(curRowIndex).getCell(standardColumnIndex);
        curRowFirstData = curRowFirstCell.getCellTypeEnum() == CellType.STRING ? curRowFirstCell.getStringCellValue() : curRowFirstCell.getNumericCellValue();
        // 当前列不为标杆列，根据标杆列上下两个单元格值是否一致来合并
        // 当前列为标杆列并且上下两个单元格一致则合并
        if ((curColIndex==standardColumnIndex && preData.equals(curData)) || (curColIndex!=standardColumnIndex && preRowFirstData.equals(curRowFirstData) && preData.equals(curData))) {


            Sheet sheet = writeSheetHolder.getSheet();

            List mergeRegions = sheet.getMergedRegions();

            boolean isMerged =false;

            for (int i =0; i < mergeRegions.size() && !isMerged; i++) {

                CellRangeAddress cellRangeAddr = (CellRangeAddress) mergeRegions.get(i);

                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元

                if (cellRangeAddr.isInRange(curRowIndex -1, curColIndex)) {

                    sheet.removeMergedRegion(i);

                    cellRangeAddr.setLastRow(curRowIndex);

                    sheet.addMergedRegion(cellRangeAddr);

                    isMerged =true;

                }

            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {

                CellRangeAddress cellRangeAddress =new CellRangeAddress(curRowIndex -1, curRowIndex, curColIndex, curColIndex);

                sheet.addMergedRegion(cellRangeAddress);
            }
        }
    }

    private void mergeWithPrevRow2(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {

        Object curData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();

        Cell preCell = cell.getSheet().getRow(curRowIndex -1).getCell(curColIndex);

        Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();

        Cell preRowFirstCell = cell.getSheet().getRow(curRowIndex -1).getCell(standardColumnIndex);
        Object preRowFirstData = preRowFirstCell.getCellTypeEnum() == CellType.STRING ? preRowFirstCell.getStringCellValue() : preRowFirstCell.getNumericCellValue();
        Object curRowFirstData = list.get(curRowIndex - ks);
        boolean flag = false;
        for (int index : Index) {
            if (curColIndex == index){
                flag = true;
            }
        }
        if (( flag && preData.equals(curData)) || (preRowFirstData.equals(curRowFirstData) && preData.equals(curData))) {


            Sheet sheet = writeSheetHolder.getSheet();

            List mergeRegions = sheet.getMergedRegions();

            boolean isMerged =false;

            for (int i =0; i < mergeRegions.size() && !isMerged; i++) {

                CellRangeAddress cellRangeAddr = (CellRangeAddress) mergeRegions.get(i);

                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元

                if (cellRangeAddr.isInRange(curRowIndex -1, curColIndex)) {

                    sheet.removeMergedRegion(i);

                    cellRangeAddr.setLastRow(curRowIndex);

                    sheet.addMergedRegion(cellRangeAddr);

                    isMerged =true;

                }

            }
            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {

                CellRangeAddress cellRangeAddress =new CellRangeAddress(curRowIndex -1, curRowIndex, curColIndex, curColIndex);

                sheet.addMergedRegion(cellRangeAddress);
            }
        }
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        int curRowIndex = cell.getRowIndex();
        int curColIndex = cell.getColumnIndex();
        if (curRowIndex > mergeRowIndex) {
            for (int i = 0; i < mergeColumnIndex.length; i++) {
                if (curColIndex == mergeColumnIndex[i]) {
                    if (list != null && list.size() > 0){
                        mergeWithPrevRow2(writeSheetHolder, cell, curRowIndex, curColIndex);
                        break;
                    }else {
                        mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex);
                        break;
                    }

                }
            }
        }
    }

    public EasyExcelUtils(int[] mergeColumnIndex, int mergeRowIndex, int standardColumnIndex) {
        this.mergeColumnIndex = mergeColumnIndex;
        this.mergeRowIndex = mergeRowIndex;
        this.standardColumnIndex = standardColumnIndex;
    }

    public EasyExcelUtils(int[] mergeColumnIndex, int mergeRowIndex, int standardColumnIndex, ArrayList list, int[]Index, int sk) {
        this.mergeColumnIndex = mergeColumnIndex;
        this.mergeRowIndex = mergeRowIndex;
        this.standardColumnIndex = standardColumnIndex;
        this.list = list;
        this.Index = Index;
        this.ks = sk;
    }
}
