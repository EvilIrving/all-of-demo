package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.WaterRuleConfiguration;
import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.digitization.vo.response.WaterRuleConfigurationRespVo;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description 水位规则配置表
 * @author hucong
 * @date 2021-07-13
 */
public interface WaterRuleConfigurationMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/07/13
     **/
    int insert(WaterRuleConfiguration entity);

    /**
     * 刪除
     * @author hucong
     * @date 2021/07/13
     **/
    int delete(String id);

    /**
     * 更新
     * @author hucong
     * @date 2021/07/13
     **/
    int update(WaterRuleConfiguration entity);

    List<WaterRuleConfigurationRespVo> loadPage(WaterRuleConfigurationReqVo vo);

    Integer countPeopleNum(@Param("peopleIds") String peopleIds);

    @DS("business")
    List<StPptnR> selectHourRainfall(@Param("list") List<String> tableNames,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("codes") String codes);

    @DS("business")
    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    @DS("business")
    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    @DS("business")
    List<StRiverR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime);
}
