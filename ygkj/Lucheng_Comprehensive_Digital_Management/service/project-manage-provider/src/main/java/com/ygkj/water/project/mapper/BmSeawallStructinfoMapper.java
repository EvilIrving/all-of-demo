package com.ygkj.water.project.mapper;

import com.ygkj.project.vo.request.BmSeawallStructinfoReqVo;
import com.ygkj.project.vo.response.BmSeawallStructinfoRespVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 9:26
 */
@Repository
public interface BmSeawallStructinfoMapper {
    List<BmSeawallStructinfoRespVo> loadPage(BmSeawallStructinfoReqVo reqVo);
}
