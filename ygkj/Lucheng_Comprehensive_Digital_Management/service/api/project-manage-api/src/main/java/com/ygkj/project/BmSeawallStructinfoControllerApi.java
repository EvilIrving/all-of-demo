package com.ygkj.project;

import com.ygkj.project.vo.request.BmSeawallStructinfoReqVo;
import com.ygkj.water.model.CommonResult;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 9:16
 */

public interface BmSeawallStructinfoControllerApi {

    CommonResult loadPage(BmSeawallStructinfoReqVo reqVo);
}
