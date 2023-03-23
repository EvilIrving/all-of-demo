package com.ygkj.big.screen.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.ResultsWarnIndicator;
import com.ygkj.big.screen.vo.response.FlashFloodDetailsResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 17:22
 */
public interface ResultsWarnIndicatorService {

    List<ResultsWarnIndicator> selectList(ResultsWarnIndicator entity);

    List<FlashFloodDetailsResVo> findFlashFloodDetails(String nvi, String avi);

    Double findDataRainNewData(String stationCode, String tableName);

}
