package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.BsScene;
import com.ygkj.big.screen.model.BsUnit;
import com.ygkj.big.screen.vo.request.BsUnitQueryReqVo;
import com.ygkj.big.screen.vo.request.SceneUnitReqVo;
import com.ygkj.big.screen.vo.response.ChartResVo;
import com.ygkj.big.screen.vo.response.Unit4SceneResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
public interface UnitSceneService {
    /**
     * 各个大屏组件
     *
     * @return
     */
    List<ChartResVo<String, Long>> unitNumStatistic();

    /**
     * 查询大屏组件信息
     *
     * @param reqVo
     * @return
     */
    PageVO<BsUnit> listUnit(BsUnitQueryReqVo reqVo);

    /**
     * 添加或更新一项大屏组件信息
     *
     * @param record
     * @return
     */
    CommonResult addOrUpdateUnit(BsUnit record);

    /**
     * 删除一项大屏组件信息
     *
     * @param id
     * @return
     */
    CommonResult deleteUnit(String id);

    /**
     * 查询大屏场景信息
     *
     * @param bsName
     * @return
     */
    List<BsScene> listScene(String bsName, Integer mode);

    /**
     * 添加或更新一项大屏场景信息
     *
     * @param record
     * @return
     */
    CommonResult addOrUpdateScene(SceneUnitReqVo record);

    /**
     * 删除一项大屏场景信息
     *
     * @param id
     * @return
     */
    CommonResult deleteScene(String id);

    /**
     * 设置场景为默认场景
     *
     * @return
     */
    CommonResult setSceneAsDefault(String sceneId, String bsName);

    /**
     * 查询一项完整的大屏
     *
     * @param bsName
     * @param sceneName
     * @return
     */
    List<Unit4SceneResVo> listFullBigScreen(String id, String bsName, String sceneName, Integer mode);

}
