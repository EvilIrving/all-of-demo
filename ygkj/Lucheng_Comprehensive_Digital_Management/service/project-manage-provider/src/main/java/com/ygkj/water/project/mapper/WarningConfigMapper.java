package com.ygkj.water.project.mapper;

import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报警配置表
 *
 * @author huangzh
 * @date 2020/12/30
 */
public interface WarningConfigMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int insert(WarningConfig warningConfig);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int delete(int id);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int update(WarningConfig warningConfig);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    WarningConfig load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    List<WarningConfig> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int pageListCount(int offset, int pagesize);

    List<WarningConfig> selectByCondition(WarningConfigPageQueryVo vo);

    Integer batchDelete(@Param("list") List<String> list);

    String getContent(@Param("id") String id);

    List<RainWarningConfig> getRainWarnConfigs(WarningConfigQueryVo vo);

    List<WarnUserInfo> getWarnUserInfo(String id);

    List<WarnConfigStaInfo> getStaInfos(StaInfoQueryVo vo);

    int udpWarnThreshold(@Param("list") List<WarningThreshold> list, @Param("threshold") Double threshold, @Param("configId") String configId);

    int addWarnThreshold(@Param("list") List<WarningThreshold> list);

    List<WarningThreshold> queryExistWarnT(@Param("list") List<String> list, @Param("rainfallPeriod") String rainfallPeriod);

    int saveWarnUsers(@Param("list") List<WarnUserInfo> list);

    int saveWarningConfig(WarningConfigAddReqVo vo);

    List<WaterStaInfoRespVo> getWaterStaInfos(StaInfoQueryVo vo);

    List<WarningThreshold> queryExistWaterWarnT(@Param("list") List<String> list, @Param("stType") String stType);

    int udpWaterWarnThreshold(@Param("list") List<WarningThreshold> list, @Param("thresholdType") String thresholdType, @Param("configId") String configId);

    int addWaterWarnThreshold(List<WarningThreshold> list);

    List<FlowStaInfoRespVo> getFlowStaInfos(StaInfoQueryVo vo);

    List<WarningThreshold> queryExistFlowWarnT(@Param("list") List<String> list, @Param("warnCondition") String warnCondition);

    int udpFlowWarnThreshold(@Param("list") List<WarningThreshold> list, @Param("flowThreshold") Double flowThreshold, @Param("configId") String configId);

    int addFlowWarnThreshold(List<WarningThreshold> list);

    List<CapacityStaInfoRespVo> getCapacityStaInfos(StaInfoQueryVo vo);

    List<WarningThreshold> queryExistCapacityWarnT(List<String> list);

    int udpCapacityWarnThreshold(@Param("list") List<WarningThreshold> list, @Param("avgCapacity") Double avgCapacity, @Param("configId") String configId);

    int addCapacityWarnThreshold(List<WarningThreshold> list);

    int delWarnSta(@Param("configIds") List<String> configIds, @Param("stCodes") List<String> stCodes);

    int updWarnThreshold(WarnConfigUdpReqVo vo);

    List<WarnAnalyseRespVo> getWarnAnalyes(WarningAnalyseQueryVo vo);

    int saveWarnForword(ForwordWarnAnalyseReqVo vo);

    int saveWarnForwordUsers(List<WarningForwordUser> list);

    int updateWarnForwordStat(String recordId);
}
