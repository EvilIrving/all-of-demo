package com.ygkj.water.project.service;


import com.ygkj.project.vo.request.BmSeawallStructinfoReqVo;
import com.ygkj.project.vo.response.BmSeawallStructinfoRespVo;
import com.ygkj.vo.PageVO;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 9:21
 */
public interface BmSeawallStructinfoService {
    PageVO<BmSeawallStructinfoRespVo> loadPage(BmSeawallStructinfoReqVo reqVo);
}
