package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.model.WatersReport;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.mapper.WatersReportMapper;
import com.ygkj.water.project.service.WatersReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/23 11:36
 * @Description:
 */
@Service
public class WatersReportServiceImpl extends ServiceImpl<WatersReportMapper, WatersReport> implements WatersReportService {

    @Autowired
    private WatersReportMapper watersReportMapper;

    @Override
    public PageVO<WatersReport> listPage(WatersReport watersReport) {
        watersReport.page();
        return PageUtils.getPageVO(watersReportMapper.listPage(watersReport));
    }
}
