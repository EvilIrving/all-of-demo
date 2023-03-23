package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.mapper.AttPrevTfBaseMapper;
import com.ygkj.big.screen.mapper.ResultsWarnIndicatorMapper;
import com.ygkj.big.screen.model.AttPrevTfBase;
import com.ygkj.big.screen.model.ResultsWarnIndicator;
import com.ygkj.service.GenerateDistributionIDService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 10:20
 */
@Component
@Slf4j
@RequestMapping("prevTfBase")
public class PrevTfBaseTaskController {


    @Resource
    private AttPrevTfBaseMapper attPrevTfBaseMapper;

    @Resource
    private GenerateDistributionIDService generateDistributionIDService;

    @Resource
    private ResultsWarnIndicatorMapper resultsWarnIndicatorMapper;

    //村落同步
    @GetMapping("execute")
    public void execute() throws Exception {
        List<AttPrevTfBase> attPrevTfBaseList1 = new ArrayList<>();
        List<AttPrevTfBase> attPrevTfBaseList2 = new ArrayList<>();
        List<AttPrevTfBase> attPrevTfBaseList3 = new ArrayList<>();


        try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\山洪灾害防治对象名录表20210618104710.xls"); XSSFWorkbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();


            AttPrevTfBase attPrevTfBase = null;
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);

                if (!"已通过".equals(getValue(row, 11))) {
                    continue;
                }

                attPrevTfBase = new AttPrevTfBase();
                attPrevTfBase.setId(generateDistributionIDService.nextID());


                attPrevTfBase.setLat(new BigDecimal(row.getCell(7).getNumericCellValue()));
                attPrevTfBase.setLon(new BigDecimal(row.getCell(6).getNumericCellValue()));
                String value = getValue(row, 0);

                attPrevTfBase.setCity(value.substring(0, 3));
                attPrevTfBase.setCounty(value.substring(3, 6));
                attPrevTfBase.setTown(value.substring(6));
                attPrevTfBase.setAvi(getValue(row, 1));
                attPrevTfBase.setNvi(getValue(row, 2));

                attPrevTfBase.setArea(new BigDecimal(getValue(row, 3)));

                attPrevTfBase.setTho(Integer.parseInt(new java.text.DecimalFormat("0").format(row.getCell(4).getNumericCellValue())));
                attPrevTfBase.setTpo(Integer.parseInt(new java.text.DecimalFormat("0").format(row.getCell(5).getNumericCellValue())));
                attPrevTfBase.setRecType(getValue(row, 8));

                attPrevTfBaseList1.add(attPrevTfBase);
            }
        } catch (Exception e) {
            log.error("错误信息1:{}", e.getMessage());
        }


        try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\山洪预警责任人表20210618104814.xls"); XSSFWorkbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();

            AttPrevTfBase attPrevTfBase = null;
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);

                if (!"已通过".equals(getValue(row, 9))) {
                    continue;
                }

                attPrevTfBase = new AttPrevTfBase();
//                attPrevTfBase.setId(generateDistributionIDService.nextID());

                if ("村级防汛责任人".equals(getValue(row, 3)) && "村级".equals(getValue(row, 4))) {
                    String value2 = getValue(row, 2);//自然村
                    attPrevTfBase.setNvi(value2);
                    String value5 = getValue(row, 5);//责任人姓名
                    attPrevTfBase.setChargerName(value5);
                    String value6 = getValue(row, 6);//手机号码
                    attPrevTfBase.setChargerPhone(value6);

                    attPrevTfBaseList2.add(attPrevTfBase);
                }
            }
        } catch (Exception e) {
            log.error("错误信息:{}", e.getMessage());
        }


        try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\村落站点关联关系表.xls"); XSSFWorkbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();

            AttPrevTfBase attPrevTfBase = null;
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);

                if (!"已通过".equals(getValue(row, 9))) {
                    continue;
                }

                attPrevTfBase = new AttPrevTfBase();
//                attPrevTfBase.setId(generateDistributionIDService.nextID());


                String value2 = getValue(row, 2);//自然村
                attPrevTfBase.setNvi(value2);
                String value4 = getValue(row, 4);//关联雨量站名称
                attPrevTfBase.setStationName(value4);
                String value5 = getValue(row, 5);//测站代码
                attPrevTfBase.setStationCode(value5);

                attPrevTfBaseList3.add(attPrevTfBase);
            }
        } catch (Exception e) {
            log.error("错误信息:{}", e.getMessage());
        }

        for (AttPrevTfBase attPrevTfBase : attPrevTfBaseList1) {
            String nvi = attPrevTfBase.getNvi();
            List<AttPrevTfBase> collect2 = attPrevTfBaseList2.stream().filter(e -> e.getNvi().equals(nvi)).collect(Collectors.toList());
            if (!collect2.isEmpty()) {
                AttPrevTfBase attPrevTfBase2 = collect2.get(0);
                attPrevTfBase.setChargerPhone(attPrevTfBase2.getChargerPhone());
                attPrevTfBase.setChargerName(attPrevTfBase2.getChargerName());
            }


            List<AttPrevTfBase> collect3 = attPrevTfBaseList3.stream().filter(e -> e.getNvi().equals(nvi)).collect(Collectors.toList());
            if (!collect3.isEmpty()) {
                AttPrevTfBase attPrevTfBase3 = collect3.get(0);
                attPrevTfBase.setStationCode(attPrevTfBase3.getStationCode());
                attPrevTfBase.setStationName(attPrevTfBase3.getStationName());
            }
            attPrevTfBaseMapper.insert(attPrevTfBase);
        }
        log.info("同步完成");
    }


    private static String getValue(Row row, int i) {
        Cell cell = row.getCell(i);
        CellType cellType = cell.getCellTypeEnum();
        if (CellType.STRING.equals(cellType)) {
            return cell.getStringCellValue();
        } else if (CellType.NUMERIC.equals(cellType)) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    //山洪灾害防治区重要村落预警指标成果表同步
    @GetMapping("execute2")
    public void execute2() throws Exception {
        List<ResultsWarnIndicator> resultsWarnIndicators = new ArrayList<>();

        try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\山洪灾害防治区重要村落预警指标成果表20210618134430.xls"); XSSFWorkbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();


            ResultsWarnIndicator resultsWarnIndicator = null;
            for (int i = 1; i <= rowNum; i++) {
                Row row = sheet.getRow(i);

                if (!"已通过".equals(getValue(row, 10)) || !"0.90Wm".equals(getValue(row, 3))) {
                    continue;
                }
                resultsWarnIndicator = new ResultsWarnIndicator();
                resultsWarnIndicator.setId(generateDistributionIDService.nextID());
                resultsWarnIndicator.setTown(getValue(row, 0));
                resultsWarnIndicator.setAvi(getValue(row, 1));
                resultsWarnIndicator.setNvi(getValue(row, 2));
                resultsWarnIndicator.setSoilMoisture(getValue(row, 3));
                resultsWarnIndicator.setIndexCategory(getValue(row, 4));
                resultsWarnIndicator.setPeriod(new BigDecimal(row.getCell(5).getNumericCellValue()));
                resultsWarnIndicator.setReadyMove(new BigDecimal(row.getCell(6).getNumericCellValue()));
                resultsWarnIndicator.setImmediateTransfer(new BigDecimal(row.getCell(7).getNumericCellValue()));
                resultsWarnIndicator.setCriticalLevel(new BigDecimal(row.getCell(8).getNumericCellValue()));
                resultsWarnIndicator.setMethod(getValue(row, 9));

                resultsWarnIndicators.add(resultsWarnIndicator);
            }
            for (ResultsWarnIndicator warnIndicator : resultsWarnIndicators) {
                try {
                    resultsWarnIndicatorMapper.insert(warnIndicator);
                } catch (Exception e) {
                    log.error("新增错误信息:{}", e.getMessage());
                }
            }
            log.info("同步完成execute2");
        } catch (Exception e) {
            log.error("错误信息2:{}", e.getMessage());
        }

    }
}
