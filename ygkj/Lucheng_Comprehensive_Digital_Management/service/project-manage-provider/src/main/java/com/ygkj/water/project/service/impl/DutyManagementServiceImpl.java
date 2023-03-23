package com.ygkj.water.project.service.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.digitization.model.WarnUser;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.project.model.DutyManagement;
import com.ygkj.project.vo.request.DutyReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.ProjectManageApplication;
import com.ygkj.water.project.feign.SendFeignClient;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.DutyManagementMapper;
import com.ygkj.water.project.service.DutyManagementService;
import joptsimple.internal.Rows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.TileObserver;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DutyManagementServiceImpl extends ServiceImpl<DutyManagementMapper, DutyManagement> implements DutyManagementService {
    @Autowired
    private DutyManagementMapper dutyManagementMapper;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    SendFeignClient sendFeignClient;


    @Override
    public void importTemplate(MultipartFile file) throws IOException {
        InputStream inputStream = null;
        Workbook workbook;
        List<DutyManagement> dutyManagements = null;

        inputStream = file.getInputStream();
        workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        // 获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        dutyManagements = new ArrayList<>(lastRowNum - 2);
        Row row = null;
        DutyManagement dutyManagement = null;
        row = sheet.getRow(0);
        Cell cell = row.getCell(0);
        String title = cell.getStringCellValue();//标题
        String year = title.substring(12, 16);//获取年份
        String month = title.substring(17,19);//获取月份
        for (int i = 2; i < lastRowNum + 1; i++) {
            row = sheet.getRow(i);
            Cell cellOne = ObjectUtils.isEmpty(row.getCell(0))? row.createCell(0): row.getCell(0);
            cellOne.setCellType(CellType.STRING);
            String dutyTime = cellOne.getStringCellValue();//日期
            Cell cellTwo = ObjectUtils.isEmpty(row.getCell(1))? row.createCell(1): row.getCell(1);
            cellTwo.setCellType(CellType.STRING);
            String week = cellTwo.getStringCellValue();//星期
            Cell cellThree = ObjectUtils.isEmpty(row.getCell(2))? row.createCell(2): row.getCell(2);
            cellThree.setCellType(CellType.STRING);
            String leaderName = cellThree.getStringCellValue();//带班领导
            Cell cellFour = ObjectUtils.isEmpty(row.getCell(3))? row.createCell(3): row.getCell(3);
            cellFour.setCellType(CellType.STRING);
            String managerName = cellFour.getStringCellValue();//白班值班人员
            Cell cellFive = ObjectUtils.isEmpty(row.getCell(4))? row.createCell(4): row.getCell(4);
            cellFive.setCellType(CellType.STRING);
            String nightManagerName = cellFive.getStringCellValue();//晚班值班人员
            Cell cellSix = ObjectUtils.isEmpty(row.getCell(5))? row.createCell(5): row.getCell(5);
            cellSix.setCellType(CellType.STRING);
            String dutyChiefName = cellSix.getStringCellValue();//当班主任

            dutyManagement = new DutyManagement();
            String leader = "";
            String manager = "";
            String nightManager = "";
            String dutyChief = "";
            if (StringUtils.isNotBlank(leaderName)){
                leader = getUserIdByUserName(leaderName);
                if (StringUtils.isBlank(leader)){
                    throw new RuntimeException(dutyTime + " 值班领导 "+ leaderName + "不存在。");
                }
            }
            if (StringUtils.isNotBlank(managerName)){
                manager = getUserIdByUserName(managerName);
                if (StringUtils.isBlank(manager)){
                    throw new RuntimeException(dutyTime + " 日间值班人员 " + managerName +"不存在。");
                }
            }
            if (StringUtils.isNotBlank(nightManagerName)){
                nightManager = getUserIdByUserName(nightManagerName);
                if (StringUtils.isBlank(nightManager)){
                    throw new RuntimeException(dutyTime + " 夜间值班人员 " + nightManagerName + "不存在。");
                }
            }
            if (StringUtils.isNotBlank(dutyChiefName)){
                dutyChief = getUserIdByUserName(dutyChiefName);
                if (StringUtils.isBlank(dutyChief)){
                    throw new RuntimeException(dutyTime + " 当班主任 " + dutyChiefName + "不存在。");
                }
            }

            String monthAndDay = dutyTime.replace("月","-");
            monthAndDay = monthAndDay.replace("日","");
            dutyManagement.setDutyDate(year + "-" + monthAndDay);
            dutyManagement.setDutyTime(dutyTime);
            dutyManagement.setWeek(week);
            dutyManagement.setLeader(leader);
            dutyManagement.setLeaderName(leaderName);
            dutyManagement.setManager(manager);
            dutyManagement.setManagerName(managerName);
            dutyManagement.setNgihtManager(nightManager);
            dutyManagement.setNgihtManagerName(nightManagerName);
            dutyManagement.setDutyChief(dutyChief);
            dutyManagement.setDutyChiefName(dutyChiefName);

            dutyManagement.setId(UUID.fastUUID().toString(true));
            dutyManagements.add(dutyManagement);
        }
        inputStream.close();

        //保存前先删除原有日期的记录
        dutyManagementMapper.deleteByYearAndMonth(year + "-" + month);
        // 批量保存
        if (!dutyManagements.isEmpty()) {
            if (dutyManagements.size() > 1000) {
                ListUtils.partition(dutyManagements,1000).stream().forEach(k->dutyManagementMapper.batchInsert(k));
            } else {
                dutyManagementMapper.batchInsert(dutyManagements);
            }
        }
    }

    private String getUserIdByUserName(String userName){
        CommonResult result = userFeignClient.userList(userName.replaceAll(" ", ""));
        if (result.getCode() == 0){
            LinkedHashMap page = (LinkedHashMap) result.getData();
            List<LinkedHashMap> list = (List<LinkedHashMap>) page.get("list");
            if (CollectionUtils.isNotBlank(list)){
                LinkedHashMap sysUser = list.get(0);
                return sysUser.get("id").toString();
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    private List<LinkedHashMap> getUserPhoneByUserId(String userIds){

        CommonResult result = userFeignClient.users(userIds);
        if (result.getCode() == 0){
            List<LinkedHashMap> list = (List<LinkedHashMap>) result.getData();
            return list;
        }else {
            return null;
        }
    }


    private static String[] week = new String[]{"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};


    @Override
    public void downloadTemplate(String yearAndMonth, HttpServletResponse response) throws Exception{

        Integer year = Integer.parseInt(yearAndMonth.substring(0,4));
        Integer month = Integer.parseInt(yearAndMonth.substring(5,7));
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row titleRow = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress( 0, 0,0,5));
        titleRow.createCell(0).setCellValue("鹿城区农业农村局值班表（" +  year + "年" + (month >= 10? month : "0"+ month) + "月" +"）");
        Row head = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress( 1, 1,0,1));

        CellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        Font font1 = wb.createFont();
        font1.setBold(true);
        headStyle.setFont(font1);

        head.createCell(0).setCellValue("日期");
        head.getCell(0).setCellStyle(headStyle);
        head.createCell(2).setCellValue("值班领导");
        head.getCell(2).setCellStyle(headStyle);
        head.createCell(3).setCellValue("日间值班人员");
        head.getCell(3).setCellStyle(headStyle);
        head.createCell(4).setCellValue("夜间值班人员");
        head.getCell(4).setCellStyle(headStyle);
        head.createCell(5).setCellValue("当班主任");
        head.getCell(5).setCellStyle(headStyle);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1,1);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(month == 12? year+1 : year, month == 12? 0 : month ,1);
//        endCalendar.add(Calendar.DAY_OF_MONTH,-1);

        int rowNum = 2;//从第二行开始填入日期
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        for (; calendar.before(endCalendar); calendar.add(Calendar.DAY_OF_MONTH,1)){
            log.info(calendar.getTime());
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue( sdf.format(calendar.getTime()) );
            row.createCell(1).setCellValue(week[calendar.get(Calendar.DAY_OF_WEEK)-1]);
            rowNum ++;
        }

        //设置样式
        CellStyle blackStyle = wb.createCellStyle();


        //自动换行*重要*
        blackStyle.setWrapText(true);
        sheet.setColumnWidth(0,20);
        titleRow.setHeightInPoints(20);//设置行高
        blackStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中
        Font font2 = wb.createFont();
        font2.setBold(true);
        font2.setFontHeight((short) 320);
        blackStyle.setFont(font2);
        titleRow.getCell(0).setCellStyle(blackStyle);
        for (int i=0 ; i < 6 ; i++){
            sheet.setColumnWidth(i,3500);
        }
//        sheet.setColumnWidth(0,3000);


        //导出到客户端
        try {
            String fileName="模板表格";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" +new String(fileName.getBytes(),"iso-8859-1")+".xlsx");//添加对中文名字支持
            response.setContentType("*/*");
            response.setCharacterEncoding("utf-8");
            wb.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public PageVO<DutyManagement> loadPage(DutyReqVo reqVo) {
        //分页
        reqVo.page();
        List<DutyManagement> list = dutyManagementMapper.loadPage(reqVo);
        return PageUtils.getPageVO(list);
    }


    @Scheduled(cron = "0 0 10 * * ?")
    public void sendDingMsgByTime(){

        /**
         * 通知内容：
         * 温馨提示，你将参与明天（X月X日，星期X）的值班工作，值班领导：XXX，日间值班人员：XXX，夜间值班人员：XXX，当班主任：XXX，请提前做好值班准备。
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,1);
        DutyReqVo reqVo = new DutyReqVo();
        reqVo.setYearAndMonth(sdf.format(instance.getTime()));
        List<DutyManagement> list = dutyManagementMapper.loadPage(reqVo);

        String content = "温馨提示，你将参与明天（";
        if (CollectionUtils.isNotBlank(list)){
            DutyManagement duty = list.get(0);
            content = content + duty.getDutyTime() + "," + duty.getWeek() + ") 的值班工作 " + (StringUtils.isNotBlank(duty.getLeaderName())? ",值班领导：":"") + duty.getLeaderName() +
                    (StringUtils.isNotBlank(duty.getManagerName())?",日间值班人员：": "" )  + duty.getManagerName() +
                    (StringUtils.isNotBlank(duty.getNgihtManagerName())?", 夜间值班人员：": "" ) + duty.getNgihtManagerName() +
                    (StringUtils.isNotBlank(duty.getDutyChief())?"，当班主任：" :"" ) + duty.getDutyChiefName() +
            "，请提前做好值班准备。";

            List<String> userIds = new ArrayList<>();
            if (StringUtils.isNotBlank(duty.getLeaderName()))userIds.add(duty.getLeader());
            if (StringUtils.isNotBlank(duty.getManager()))userIds.add(duty.getManager());
            if (StringUtils.isNotBlank(duty.getNgihtManager()))userIds.add(duty.getNgihtManager());
            if (StringUtils.isNotBlank(duty.getDutyChief()))userIds.add(duty.getDutyChief());
            if (CollectionUtils.isNotBlank(userIds)){
                //获取需要接收的手机号码
                String userIdStr = userIds.stream().collect(Collectors.joining(","));
                List<LinkedHashMap> users = getUserPhoneByUserId(userIdStr);
                for (LinkedHashMap user : users) {
                    MsgSendRecord msgSendRecord = new MsgSendRecord();
//                    TODO 测试用，提交或者发布需要更改对应代码为以下注释内容
                    msgSendRecord.setReceivePhone(user.get("phone").toString());
//                    msgSendRecord.setReceivePhone("18030383659");
                    msgSendRecord.setReceiveName(user.get("username").toString());
                    msgSendRecord.setContext(content);
                    sendFeignClient.sendDingDing(msgSendRecord);
                }
            }

        }
    }




}










