package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.ResultsWarnIndicator;
import com.ygkj.big.screen.vo.response.FlashFloodDetailsResVo;
import com.ygkj.big.screen.vo.response.MountainTorrentDetailResVo;
import com.ygkj.big.screen.vo.response.MountainTorrentIndicatorResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 14:01
 */
public interface ResultsWarnIndicatorMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/06/18
     **/
    int insert(ResultsWarnIndicator resultsWarnIndicator);

    List<ResultsWarnIndicator> selectList(ResultsWarnIndicator entity);

    List<FlashFloodDetailsResVo> findFlashFloodDetails(@Param("nvi") String nvi, @Param("avi") String avi);

    @DS("business")
    Double findDataRainNewData(@Param("stationCode") String stationCode, @Param("tableName") String tableName);

    List<MountainTorrentIndicatorResVo> selectByCondition(ResultsWarnIndicator entity);
}
