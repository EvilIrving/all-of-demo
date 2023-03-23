package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.WatersReportInfo;
import com.ygkj.vo.PageVO;

/**
 * @author wgf
 * @date 2022/5/27 10:16
 * @Description:
 */
public interface WatersReportInfoService extends IService<WatersReportInfo> {

    PageVO<WatersReportInfo> listPage(WatersReportInfo watersReportInfo);
}
