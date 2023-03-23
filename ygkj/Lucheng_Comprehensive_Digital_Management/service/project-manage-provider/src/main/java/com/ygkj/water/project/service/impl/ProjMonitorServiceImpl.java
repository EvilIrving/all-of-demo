package com.ygkj.water.project.service.impl;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.MoRegularMonitor;
import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.project.vo.request.ProjMonitorReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.MoRegularMonitorMapper;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.ProjMonitorService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-28 14:50
 * @description
 */
@Service
public class ProjMonitorServiceImpl implements ProjMonitorService {

    @Resource
    private MoRegularMonitorMapper moRegularMonitorMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    public PageVO<MoRegularMonitorVo> regularMonitorList(ProjMonitorReqVo reqVo) {
        reqVo.page();
        List<MoRegularMonitorVo> list = moRegularMonitorMapper.selectRegularMonitorList(reqVo);
        for (MoRegularMonitorVo vo : list) {
            setRegularMonitorFile(vo);
        }
        return PageUtils.getPageVO(list);
    }

    public void setRegularMonitorFile(MoRegularMonitorVo vo){
        vo.setAnalyzeReportList(StringUtils.isNotBlank(vo.getAnalyzeReport())? sysFileMapper.loadByIds(vo.getAnalyzeReport()) : Collections.emptyList());
        vo.setReorganizationReportList(StringUtils.isNotBlank(vo.getReorganizationReport())? sysFileMapper.loadByIds(vo.getReorganizationReport()):Collections.emptyList());
    }


    @Override
    public Integer insertRegularMonitor(MoRegularMonitorVo vo) {

        MoRegularMonitor moRegularMonitor = new MoRegularMonitor();
        BeanUtils.copyProperties(vo, moRegularMonitor);
        moRegularMonitor.setId(idService.nextID());
        moRegularMonitor.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return moRegularMonitorMapper.insert(moRegularMonitor);
    }

    @Override
    public Integer updateRegularMonitor(MoRegularMonitorVo vo) {

        MoRegularMonitor moRegularMonitor = new MoRegularMonitor();
        BeanUtils.copyProperties(vo, moRegularMonitor);
        moRegularMonitor.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        return moRegularMonitorMapper.updateById(moRegularMonitor);
    }

    @Override
    public Integer regularMonitorDelete(String id) {

        return moRegularMonitorMapper.deleteByRegularId(id);
    }


    @Override
    public void regularMonitorExport(HttpServletResponse response, ProjMonitorReqVo reqVo) {
        PageVO<MoRegularMonitorVo> pageVO = regularMonitorList(reqVo);
        List<MoRegularMonitorVo> list = pageVO.getList();
        ExportUtils.exportExcel(list,"定期监测表",
                new String[]{"年份","监测日期","监测单位","监测类型","与前期对比是否异常","水库管理单位负责人"},
                new String[]{"year","monitorDate","monitorUnit","monitorType","compared","manager"},response);
    }
}
