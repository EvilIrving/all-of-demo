package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.WatersReport;
import com.ygkj.vo.PageVO;

/**
 * @author wgf
 * @date 2022/5/23 11:35
 * @Description:
 */
public interface WatersReportService extends IService<WatersReport> {

    PageVO<WatersReport> listPage(WatersReport watersReport);

}
