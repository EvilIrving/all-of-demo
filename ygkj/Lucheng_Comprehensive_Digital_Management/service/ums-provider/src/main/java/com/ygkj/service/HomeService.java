package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.vo.resp.HomeRespVO;

/**
 * @ClassName: HomeService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/25 21:27
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/25 21:27
 * @Version: 0.0.1
 */
public interface HomeService {

    HomeRespVO getHomeInfo(String userId, String system);

    JSONObject myTrajectory();
}
