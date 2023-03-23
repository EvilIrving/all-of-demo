package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.ProjMicroImage;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.ProjMicroImageInsVo;
import com.ygkj.project.vo.request.ProjMicroImageReqVo;
import com.ygkj.project.vo.response.ProjMicroImageRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
public interface ProjMicroImageService {

    /**
     * 新增
     */
    int insert(ProjMicroImageInsVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjMicroImageInsVo vo);


    List<JSONObject> loadPage(ProjMicroImageReqVo vo);

    /**
     * 查找时间轴年份
     *
     * @param type
     * @return
     */
    List<Integer> yearList(String type);

    PageVO<ProjMicroImageRespVo> listData(ProjMicroImageReqVo vo);

    void saveUserChoosepData(String data);

    JSONObject getUserChoosepData();

}
