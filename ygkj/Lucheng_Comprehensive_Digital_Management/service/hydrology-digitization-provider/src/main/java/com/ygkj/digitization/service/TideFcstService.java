package com.ygkj.digitization.service;

import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.FcstValReqVo;
import com.ygkj.digitization.vo.request.TideFcstQueryVo;
import com.ygkj.digitization.vo.response.AttTideFcstValResVo;
import com.ygkj.digitization.vo.response.FcstStResVo;
import com.ygkj.digitization.vo.response.FcstValResVo;
import com.ygkj.digitization.vo.response.TideFcstResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
public interface TideFcstService {
    /**
     * 查询风暴潮预报列表
     *
     * @param queryVo
     * @return
     */
    PageVO<TideFcstResVo> pageTideFcst(TideFcstQueryVo queryVo);

    /**
     * 新增或更新风暴潮预报
     *
     * @param resVo
     * @return
     */
    CommonResult addOrUpdateTideFcst(TideFcstResVo resVo);

    /**
     * 根据id风暴潮预报
     *
     * @param id
     * @return
     */
    TideFcstResVo getTideFcst(String id);

    /**
     * 删除风暴潮预报
     *
     * @param id
     * @return
     */
    CommonResult deleteTideFcst(String id);

    /**
     * 查询最新一项风暴潮手动预报
     *
     * @param releaseType 发布类型1：手动预报，2自动预报
     * @return
     */
    TideFcstResVo newestTideFcst(Integer releaseType);

    /**
     * 手动报警潮位列表
     *
     * @return
     */
    List<AttStBase> manualFcstTideList();

    /**
     * 根据预报id查询风暴潮列表
     *
     * @param fcstId
     * @return
     */
    List<AttTideFcstValResVo> listTileFcst(String fcstId);

    /**
     * 风暴潮站点列表
     *
     * @param fcstId
     * @return
     */
    List<FcstStResVo> listTileFcstSt(String fcstId);

    /**
     * 风暴潮列表
     *
     * @param reqVo
     * @return
     */
    List<FcstValResVo> listTileFcstVal(FcstValReqVo reqVo);
}
