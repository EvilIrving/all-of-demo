package com.ygkj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SysLog;
import com.ygkj.gragh.vo.response.AqjcExportResVo;
import com.ygkj.mapper.SysLogMapper;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.platform.management.vo.request.SysLoginLogPage;
import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.service.LogService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.SysLogPageReqVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.SysLogRespVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: LogServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/23 16:18
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/23 16:18
 * @Version: 0.0.1
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageVO<SysLog> pageInfo(SysLogPageReqVO vo) {

        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysLog> sysLogs = sysLogMapper.selectAll(vo);
        return PageUtils.getPageVO(sysLogs);
    }

    @Override
    public void deleted(List<String> logIds) {
        sysLogMapper.batchDeletedLog(logIds);
    }

    @Override
    public PageVO<SysLoginLog> loadLoginLogs(SysLoginLogPage pageVo){
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<SysLoginLog> sysLoginLogs = sysLogMapper.loadLoginLogs(pageVo);
        for (SysLoginLog sysLoginLog : sysLoginLogs) {
            sysLoginLog.setOperateInfo("登陆系统成功");
        }
        return PageUtils.getPageVO(sysLoginLogs);
    }

    @Override
    public void export(HttpServletResponse response, SysLogPageReqVO vo) throws ParseException {
        Date nowTime = new Date();
        if (StringUtils.isBlank(vo.getEndTime())){
            vo.setEndTime(DateUtils.format(nowTime,DateUtils.DATE_FORMAT_19));
        }
        if (StringUtils.isBlank(vo.getStartTime())){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(vo.getEndTime(),DateUtils.DATE_FORMAT_19));
            calendar.add(Calendar.MONTH,-3);
            vo.setStartTime(DateUtils.format(calendar.getTime(),DateUtils.DATE_FORMAT_19));
        }
        List<SysLog> sysLogs = sysLogMapper.selectAll(vo);
        List<SysLogRespVo> result = new ArrayList<>();
        sysLogs.forEach(sysLog -> {
            SysLogRespVo sysLogRespVo = new SysLogRespVo();
            BeanUtils.copyProperties(sysLog, sysLogRespVo);
            result.add(sysLogRespVo);
        });
        try {
            String fileName = URLEncoder.encode("操作日志.xlsx", "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ";" + "filename*=utf-8''" + fileName);
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream(), SysLogRespVo.class).sheet("操作日志").doWrite(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
