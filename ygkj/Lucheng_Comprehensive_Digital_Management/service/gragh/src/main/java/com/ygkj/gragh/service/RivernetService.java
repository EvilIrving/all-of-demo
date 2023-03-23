package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.vo.request.RivernetReplayReqVo;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import com.ygkj.gragh.vo.response.RivernetWaterLevelResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/27
 */
public interface RivernetService {
    List<RivernetWaterLevelResVo> listRivernet(RivernetWaterLevelReqVo reqVo);


    /**
     * 河网复盘统计
     *
     * @return
     */
    JSONObject rivernetReplayStats(RivernetReplayReqVo reqVo);

    /**
     * 导出河网
     *
     * @param reqVo
     * @param response
     */
    void exportRivernet(RivernetWaterLevelReqVo reqVo, HttpServletResponse response);
}
