package com.ygkj.water.project.service;

import com.ygkj.project.model.BmAccBuild;
import com.ygkj.project.vo.response.BmAccBuildResVo;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-11 13:55
 */
public interface BmAccBuildService {
    List<BmAccBuildResVo> selectBmAccBuildByCondition(String type,String prcd);
}
