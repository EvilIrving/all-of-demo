package com.ygkj.digitization.service;

import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.HydrFcstQueryVo;
import com.ygkj.digitization.vo.response.HydrFcstResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
public interface HydrFcstService {

    /**
     * 查询水文预报列表
     * @param queryVo
     * @return
     */
    PageVO<HydrFcstResVo> pageHydrFcst(HydrFcstQueryVo queryVo);

    /**
     * 新增或更新水文预报
     * @param resVo
     * @return
     */
    CommonResult addOrUpdateHydrFcst(HydrFcstResVo resVo);

    /**
     * 根据id水文预报
     * @param id
     * @return
     */
    HydrFcstResVo getHydrFcst(String id);

    /**
     * 新增或更新水文预报
     * @param id
     * @return
     */
    CommonResult deleteHydrFcst(String id);

    /**
     * 最新水文预报
     * @return
     */
    List<HydrFcstResVo> newestHydrFcst();


    /**
     * 列出水文测站
     * @param stType 水库：RR,河道：ZZ
     * @param stName
     * @return
     */
    List<AttStBase> listHydrStation(String stType,String stName);
}
