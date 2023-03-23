package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.water.model.CommonResult;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-18 15:39
 */
public interface WorkingConditionsService {
    JSONObject workingConditionsTotal();

    CommonResult workingConditionsList(String code,String eng_scal, String pjnm, String pjtp,String openStatus,Integer pageNum,Integer pageSize);
}
