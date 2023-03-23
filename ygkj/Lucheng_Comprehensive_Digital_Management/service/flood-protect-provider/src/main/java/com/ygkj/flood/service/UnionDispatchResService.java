package com.ygkj.flood.service;

import com.ygkj.flood.model.FloodPoint;
import com.ygkj.flood.vo.response.ProResInfo;
import com.ygkj.flood.vo.response.WlaOverWarnNumRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

public interface UnionDispatchResService {

    CommonResult<WlaOverWarnNumRespVo> warnNum();

    CommonResult<List<ProResInfo>> resProList(String projectCode);

    PageVO<FloodPoint> newestFloodPoint(Integer pageNum, Integer pageSize);


    /**
     * @return
     */
    @ApiOperation("一键研判-雨情")
    Map<String, Object> rainfallWarn();

    @ApiOperation("一键研判-江河水情")
    Map<String, Object> riverWagaWarn();

    @ApiOperation("一键研判-大中型水库水情")
    Map<String, Object> largeMidRsvrWarn();


    @ApiOperation("一键研判-积水")
    Map<String, Object> floodPointWarn();
}
