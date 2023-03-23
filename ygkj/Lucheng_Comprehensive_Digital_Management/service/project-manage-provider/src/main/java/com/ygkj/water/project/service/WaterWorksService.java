package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.AttCwsBaseWcp;
import com.ygkj.project.vo.request.WaterWorksReqVo;
import com.ygkj.project.vo.response.CwsWcpResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description 水厂服务
 * @Date 2021/8/16
 */
public interface WaterWorksService {

    /**
     * 查询水厂列表
     *
     * @param reqVo
     * @return
     */
    List<CwsWcpResVo> listWaterWorks(WaterWorksReqVo reqVo);

    /**
     * 查询水厂列表表头统计
     *
     * @param reqVo
     * @return
     */
    Map<String, Object> stats4listWaterWorks(WaterWorksReqVo reqVo);

    /**
     * 查询水厂运行状态列表表头统计
     *
     * @param reqVo
     * @return
     */
    Map<String, Object> stats4listWaterWorksState(WaterWorksReqVo reqVo);

    /**
     * 水厂统计
     *
     * @param type 1. 根据行政区划统计：城市、乡镇、联村、单村;
     *             2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；
     *             3. 根据水厂类型统计：城市、乡镇、联村、单村；
     *             4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警
     *             5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警
     *             6. 统计日累计供水量，异常供水次数
     */
    Object waterWorksStats(int type);

    /**
     * 水厂水量水质曲线
     *
     * @param type    1. 水量，2水质
     * @param days    时间区间类型：过去24小时=1，近7天=7；近1个月=30
     * @param cwsCode 水厂编码
     * @return
     */
    JSONObject waterWorksStateCurve(int type, int days, String cwsCode);
}
