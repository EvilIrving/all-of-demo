package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;

import java.util.Collection;

public interface PeripheralAnalysisService {

    JSONArray analysis(ProjectRangeQueryVo queryVo);

    Collection candidateRecords(String codes, String type);
}
