package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.response.SectFlowQueryReqVo;
import com.ygkj.digitization.vo.response.SectFlowResVo;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
public interface SectFlowService {


    /**
     * 查询断面流量列表
     * @param reqVo
     * @return
     */
    List<SectFlowResVo> getSectFlowList(SectFlowQueryReqVo reqVo);

    /**
     * 断面流量列表-表头统计
     * @param reqVo
     * @return
     */
    JSONObject stats4SectFlowList(SectFlowQueryReqVo reqVo);

    /**
     * 导出断面流量列表
     * @param reqVo
     * @param response
     */
    void exportSectFlowList(SectFlowQueryReqVo reqVo, HttpServletResponse response);

    /**
     * 断面流量统计
     * @return
     */
    JSONObject sectFlowStats();

    /**
     * 断面流量曲线
     * @param sectName 断面名称
     * @param hours 往前推的小时数
     * @return
     */
    List<StPrrwtResVo> sectFlowCurve(String sectName, Integer hours);
}
