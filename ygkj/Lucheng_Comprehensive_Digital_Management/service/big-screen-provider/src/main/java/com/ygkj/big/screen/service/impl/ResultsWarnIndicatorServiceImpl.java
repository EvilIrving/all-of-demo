package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.ResultsWarnIndicatorService;
import com.ygkj.big.screen.mapper.ResultsWarnIndicatorMapper;
import com.ygkj.big.screen.model.ResultsWarnIndicator;
import com.ygkj.big.screen.vo.response.FlashFloodDetailsResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 17:22
 */
@Service
public class ResultsWarnIndicatorServiceImpl implements ResultsWarnIndicatorService {

    @Autowired
    private ResultsWarnIndicatorMapper resultsWarnIndicatorMapper;

    @Override
    public List<ResultsWarnIndicator> selectList(ResultsWarnIndicator entity) {
        return resultsWarnIndicatorMapper.selectList(entity);
    }

    @Override
    public List<FlashFloodDetailsResVo> findFlashFloodDetails(String nvi, String avi) {
        return resultsWarnIndicatorMapper.findFlashFloodDetails(nvi, avi);
    }

    @Override
    public Double findDataRainNewData(String stationCode, String tableName) {
        return resultsWarnIndicatorMapper.findDataRainNewData(stationCode, tableName);
    }


}
