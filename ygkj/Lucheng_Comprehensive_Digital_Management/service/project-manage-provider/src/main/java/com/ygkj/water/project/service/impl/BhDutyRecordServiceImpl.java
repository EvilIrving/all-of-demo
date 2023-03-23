package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.digitization.model.WarnUser;
import com.ygkj.project.model.BhDutyRecord;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.mapper.BhDutyRecordMapper;
import com.ygkj.water.project.mapper.BhMapper;
import com.ygkj.water.project.mapper.ProjectRelMapper;
import com.ygkj.water.project.service.BhDutyRecordService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/4/26 14:03
 * @Description:
 */
@Service
public class BhDutyRecordServiceImpl extends ServiceImpl<BhDutyRecordMapper, BhDutyRecord> implements BhDutyRecordService {

    @Autowired
    private BhDutyRecordMapper bhDutyRecordMapper;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private BhMapper bhMapper;
    @Override
    public PageVO<BhDutyRecord> getList(BhDutyRecord bhDutyRecord) {

        String pid = selectForeignCode(
                ProjectCodeMapping.builder().type(1)
                        .projectType(null)
                        .localPrcd(bhDutyRecord.getPid())
                        .build());
        pid = pid == null?bhDutyRecord.getPid():pid;
        bhDutyRecord.setPid(pid);

        bhDutyRecord.page();
        List<BhDutyRecord> list = bhDutyRecordMapper.getList(bhDutyRecord);
        PageVO<BhDutyRecord> pageVO = PageUtils.getPageVO(list);
        list = pageVO.getList();
        list.forEach(m->{
            List<String> types = new ArrayList<>();
            if(m.getPatrolType().contains("1")){
                types.add("日常巡查");
            }else if (m.getPatrolType().contains("2")){
                types.add("汛前检查");
            }else if (m.getPatrolType().contains("3")){
                types.add("年度检查");
            }else if (m.getPatrolType().contains("4")){
                types.add("特别检查");
            }
            m.setPatrolType(String.join(",",types));
        });
        pageVO.setList(list);
        return pageVO;
    }

    @Override
    public String selectForeignCode(ProjectCodeMapping projectCodeMapping) {
        return projectRelMapper.selectForeignCode(projectCodeMapping);
    }

    @Override
    public void exportDuty(HttpServletResponse response, BhDutyRecord bhDutyRecord) {
        PageVO<BhDutyRecord> pageVO = getList(bhDutyRecord);
        List<BhDutyRecord> list = pageVO.getList();
        ExportUtils.exportExcel(list,"值班值守",
                new String[]{"值班日期","值班负责人","值班员","水工巡查人员","联系电话","设备巡查人员","联系电话","值班电话","巡查类型"},
                new String[]{"dutyDate","leaderName","mgtDayName","mgtNightName","mgtNightPhone","devicePatrolName",
                        "devicePatrolPhone","dutyPhone","patrolType"},response);
    }

    @Override
    @DS("exchange")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public JSONObject importDuty(MultipartFile file, String pid) throws Exception{
        JSONObject result = new JSONObject();
        result.put("fileName",file.getOriginalFilename());

        BigDecimal size = BigDecimal.valueOf(file.getSize()).divide(BigDecimal.valueOf(1024),2,BigDecimal.ROUND_HALF_UP);
        result.put("size", size.toString() + " kb");
        result.put("suffix",getFileSuffix(file.getOriginalFilename()));
        String bhPid = selectForeignCode(
                ProjectCodeMapping.builder().type(1)
                        .projectType(null)
                        .localPrcd(pid)
                        .build());
        pid = bhPid == null ? pid : bhPid;

        InputStream inputStream = null;
        Workbook workbook;

        inputStream = file.getInputStream();
        workbook = WorkbookFactory.create(inputStream);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Sheet sheet = workbook.getSheetAt(0);
        // 获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        Row row = null;
        BhDutyRecord bhDutyRecord = new BhDutyRecord();
        for (int i = 1; i < lastRowNum + 1; i++) {
            row = sheet.getRow(i);
            Date dutyDate;
            try {
                dutyDate = sdf.parse(row.getCell(0).getStringCellValue());
            }catch (Exception r){
                throw new RuntimeException("日期格式化异常，请使用 yyyy-MM-dd 格式填写日期");
            }
            bhDutyRecord.setDutyDate(dutyDate);
            //先判断该工程下的该时间的数据是否存在。若已经存在不进行导入新增
            QueryWrapper<BhDutyRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PID", pid);
            queryWrapper.eq("LEFT(DUTY_DATE,10)", DateUtils.format(dutyDate,"yyyy-MM-dd"));
            BhDutyRecord dutyRecord = bhDutyRecordMapper.selectOne(queryWrapper);
            if (dutyRecord != null){
                continue;
            }
            String leaderIds = bhMapper.findIdsByName(row.getCell(1).getStringCellValue());
            bhDutyRecord.setLeader(leaderIds);
            String mgtDayIds = bhMapper.findIdsByName(row.getCell(2).getStringCellValue());
            bhDutyRecord.setMgtDay(mgtDayIds);
            String mgtNightIds = bhMapper.findIdsByName(row.getCell(3).getStringCellValue());
            bhDutyRecord.setMgtNightId(mgtNightIds);
            bhDutyRecord.setMgtNightPhone(row.getCell(4).getStringCellValue());
            String devicePatrolIds = bhMapper.findIdsByName(row.getCell(5).getStringCellValue());
            bhDutyRecord.setDevicePatrolUid(devicePatrolIds);
            bhDutyRecord.setDevicePatrolPhone(row.getCell(6).getStringCellValue());
            bhDutyRecord.setDutyPhone(row.getCell(7).getStringCellValue());
            String patrolType = row.getCell(8).getStringCellValue();
            List<String> types = new ArrayList<>();
            if(patrolType.contains("日常巡查")){
                types.add("1");
            }else if (patrolType.contains("汛前检查")){
                types.add("2");
            }else if (patrolType.contains("年度检查")){
                types.add("3");
            }else if (patrolType.contains("特别检查")){
                types.add("4");
            }
            bhDutyRecord.setPid(pid);
            bhDutyRecord.setPatrolType(types.stream().collect(Collectors.joining(",")));
            bhDutyRecord.setId(IdWorker.get32UUID());
            bhDutyRecordMapper.insert(bhDutyRecord);
        }

        inputStream.close();

        return result;
    }


    public String getFileSuffix(String fileName){
        //没有后缀名情况
        if (fileName.lastIndexOf(".") == -1){
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));

    }


}
