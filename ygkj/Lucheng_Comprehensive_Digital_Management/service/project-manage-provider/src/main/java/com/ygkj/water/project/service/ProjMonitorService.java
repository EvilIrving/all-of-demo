package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.project.vo.request.ProjMonitorReqVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-28 14:36
 * @description
 */
public interface ProjMonitorService {

    /**
     * 列表查询
     * @param reqVo
     * @return
     */
    PageVO<MoRegularMonitorVo> regularMonitorList(ProjMonitorReqVo reqVo);

    /**
     * 插入定时监测
     * @param vo
     * @return
     */
    Integer insertRegularMonitor(MoRegularMonitorVo vo);

    /**
     * 更新定时监测
     * @param vo
     * @return
     */
    Integer updateRegularMonitor(MoRegularMonitorVo vo);

    /**
     * 删除定时监测
     * @param id
     * @return
     */
    Integer regularMonitorDelete(String id);

    /**
     * 导出列表
     * @param reqVo
     */
    void regularMonitorExport(HttpServletResponse response, ProjMonitorReqVo reqVo);
}
