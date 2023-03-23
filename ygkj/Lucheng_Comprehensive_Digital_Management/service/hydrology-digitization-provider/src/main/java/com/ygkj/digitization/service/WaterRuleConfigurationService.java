package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.digitization.vo.response.WaterRuleConfigurationRespVo;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.vo.PageVO;

import java.util.Date;
import java.util.List;

public interface WaterRuleConfigurationService {

    /**
     * 新增
     */
    int insert(WaterRuleConfigurationReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(WaterRuleConfigurationReqVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<WaterRuleConfigurationRespVo> loadPage(WaterRuleConfigurationReqVo vo);

    List<StPptnR> selectHourRainfall(List<String> tableNames, String startTime, String endTime, String codes);

    /**
     * 查询河道水位最新数据
     * @param table
     * @param stcds
     * @param dataTime
     * @return
     */
    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询潮位最新数据
     * @param table
     * @param stcds
     * @param dataTime
     * @return
     */
    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询水库水位最新数据
     * @param table
     * @param stcds
     * @param dataTime
     * @return
     */
    List<StRiverR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime);
}
