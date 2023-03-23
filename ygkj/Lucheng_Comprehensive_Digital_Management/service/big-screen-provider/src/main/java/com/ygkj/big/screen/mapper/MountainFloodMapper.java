package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.MountainTorrentMonitorWarnRecord;
import com.ygkj.big.screen.model.MountainTorrentMonitorWarnValue;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/10/11
 */
public interface MountainFloodMapper {

    /**
     * 新增 报警值
     *
     * @author xq
     * @date 2021/10/11
     **/
    @DS("business")
    int insertMonitorWarnValue(MountainTorrentMonitorWarnValue mountainTorrentMonitorWarnValue);

    /**
     * 新增 报警记录
     *
     * @author xq
     * @date 2021/10/11
     **/
    @DS("business")
    int insertMonitorWarnRecord(MountainTorrentMonitorWarnRecord mountainTorrentMonitorWarnRecord);

    @DS("business")
    List<MountainTorrentMonitorWarnRecord> selectPastMinutesNewestWarnRecord(Date dataTime, int pastMinutes);

    /**
     * 查询距离dataTime 最近的山洪预报值
     *
     * @param dataTime
     * @param futureMinutes 未来
     * @return
     */
    @DS("business")
    String selectLowestPredictInfo(Date dataTime, int futureMinutes);
}
