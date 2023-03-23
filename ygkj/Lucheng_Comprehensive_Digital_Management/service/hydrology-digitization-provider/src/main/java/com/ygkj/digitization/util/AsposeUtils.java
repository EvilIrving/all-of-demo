package com.ygkj.digitization.util;

import com.aspose.words.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class AsposeUtils {

    private static InputStream inputStream = null;
    private static Logger logger = Logger.getLogger(AsposeUtils.class);

    public static boolean checkLicense(){
        return setLicense();
    }

    /***************************************  上面是公有方法 -- 超华丽的分割线 --下面是私有方法****************************************/

    /**
     * 设置License
     * @return
     */
    private static boolean setLicense(){
        if (inputStream == null){
            InputStream licenseInput = getLicenseAsStream();
            if (licenseInput != null) {
                try {
                    com.aspose.words.License aposeLic = new com.aspose.words.License();
                    aposeLic.setLicense(licenseInput);
                    return aposeLic.getIsLicensed();
                } catch (Exception e) {
                    logger.error("set words license error!", e);
                }
            }else {
                return false;
            }
        }
        return true;
    }

    /**
     * 以流的形式读出License
     * @return
     */
    private static InputStream getLicenseAsStream() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            String path = contextClassLoader.getResource("aspose/license.xml").toURI().getPath();
            inputStream = new FileInputStream(path);
        } catch (Exception e) {
            logger.error("license not found!", e);
        }
        return inputStream;
    }


    public static Cell setCell(Cell cell , Document doc,String value){
        cell.getParagraphs().clear();
        Paragraph p = new Paragraph(doc);
        //设置水平居中
        p.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
        //设置垂直的方向
        cell.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
        p.appendChild(new Run(doc,value));
        cell.appendChild(p);
        return cell;
    }

    /**
     * 创建单元格
     * @param value
     * @param doc
     * @return
     */
    public static Cell createCell(String value, Document doc){
        Cell cell = new Cell(doc);
        Paragraph p = new Paragraph(doc);
        //设置水平居中
        p.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
        //设置垂直的方向
        cell.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
        p.appendChild(new Run(doc,value));
        cell.appendChild(p);
        return cell;
    }

    /**
     * 创建行
     * @param columnCount
     * @param columnValues
     * @param doc
     * @return
     */
    public static Row createRow(int columnCount,String[] columnValues,Document doc){
        Row row = new Row(doc);
        for (int i = 0; i < columnCount; i++) {
            if (columnValues.length >i) {
                Cell cell = createCell(columnValues[i], doc);
                row.getCells().add(cell);
            } else {
                Cell cell = createCell("", doc);
                row.getCells().add(cell);
            }
        }
        return row;
    }

    /**
     * 合并单元格
     * @param startCell
     * @param endCell
     */
    public static void mergeCells(Cell startCell, Cell endCell) {
        Table parentTable = startCell.getParentRow().getParentTable();

        // Find the row and cell indices for the start and end cell.
        Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell), parentTable.indexOf(startCell.getParentRow()));
        Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell), parentTable.indexOf(endCell.getParentRow()));
        // Create the range of cells to be merged based off these indices. Inverse each index if the end cell if before the start cell.
        Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x),
                Math.min(startCellPos.y, endCellPos.y),
                Math.abs(endCellPos.x - startCellPos.x) + 1,
                Math.abs(endCellPos.y - startCellPos.y) + 1);

        for (Row row : parentTable.getRows()) {
            for (Cell cell : row.getCells()) {
                Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));

                // Check if the current cell is inside our merge range then merge it.
                if (mergeRange.contains(currentPos)) {
                    if (currentPos.getX() == mergeRange.getX())
                        cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

                    if (currentPos.getY() == mergeRange.getY())
                        cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                }
            }
        }
    }

}
