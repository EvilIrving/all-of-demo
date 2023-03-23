package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttPustBase;
import com.ygkj.flood.model.AttResBase;
import com.ygkj.flood.model.AttWagaBase;
import com.ygkj.flood.vo.request.ProjectListReqVo;
import com.ygkj.flood.vo.response.AllStorageResVo;
import com.ygkj.flood.vo.response.PustListResVo;
import com.ygkj.flood.vo.response.StorageResVo;
import com.ygkj.flood.vo.response.WagaListResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;

public interface FloodProjectService {

    /**
     * 可蓄水量/纳蓄能力统计
     *
     * @param type 1可蓄水量 2纳蓄能力
     * @return
     */
    List<StorageResVo> countStorage(Integer type, Integer resScal);

    /**
     * 大中型水库蓄水率
     *
     * @return
     */
    AllStorageResVo countAllStorageRate();

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    List<WagaListResVo> listWaga(ProjectListReqVo reqVo);

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    List<PustListResVo> listPust(ProjectListReqVo reqVo);


    /**
     * 水闸工程列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageVO<AttWagaBase> listWagaProj(Integer pageSize, Integer pageNum);

    /**
     * 泵站工程列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageVO<AttPustBase> listPustProj(Integer pageSize, Integer pageNum, String pustCode);

    List<AttResBase> listRsvrStorageWaterLevel();
}
