package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.model.WatersReportInfo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.mapper.WatersReportInfoMapper;
import com.ygkj.water.project.service.WatersReportInfoService;
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
public class WatersReportInfoServiceImpl extends ServiceImpl<WatersReportInfoMapper, WatersReportInfo> implements WatersReportInfoService {

    @Autowired
    private WatersReportInfoMapper watersReportInfoMapper;
    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    public PageVO<WatersReportInfo> listPage(WatersReportInfo watersReportInfo) {
        watersReportInfo.page();
        List<WatersReportInfo> watersReportInfos = watersReportInfoMapper.listPage(watersReportInfo);
        for (WatersReportInfo reportInfo : watersReportInfos) {
            String picturesIds = reportInfo.getPicturesIds();
            if (StringUtils.isNotBlank(picturesIds)){
                List<SysFile> sysFiles = sysFileMapper.loadByIds(picturesIds);
                reportInfo.setFiles(sysFiles);
            }
        }
        return PageUtils.getPageVO(watersReportInfos);
    }
}
