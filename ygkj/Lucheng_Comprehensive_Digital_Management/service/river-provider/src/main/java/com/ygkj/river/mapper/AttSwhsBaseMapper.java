package com.ygkj.river.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.river.model.AttSwhsBase;
import com.ygkj.river.model.AttSwhsSignageBase;
import com.ygkj.river.vo.request.AttSwhsBaseReqVo;
import com.ygkj.river.vo.response.AttSwhsBaseRespVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hucong
 * @description 水源地基础信息表
 * @date 2021-08-17
 */
@DS("baisc")
public interface AttSwhsBaseMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/08/17
     **/
    int insert(AttSwhsBase attSwhsBase);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/08/17
     **/
    int delete(@Param("swhsCode") String swhsCode);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/08/17
     **/
    int update(AttSwhsBase attSwhsBase);

    List<AttSwhsBaseRespVo> loadPage(AttSwhsBaseReqVo vo);

    /**
     * 组件-水源地数量按规模统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByScale();

    /**
     * 组件-水源地人口按规模统计
     *
     * @return
     */
    List<JSONObject> statisSwhsDesAnnWainByScale();

    /**
     * 组件-水源地数量按类型统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByType();

    /**
     * 组件-水源地人口按类型统计
     *
     * @return
     */
    List<JSONObject> statisSwhsDesAnnWainByType();

    /**
     * 组件-水源地数量按行政区划统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByArea();

    /**
     * 组件-水源地人口按行政区划统计
     *
     * @return
     */
    List<JSONObject> statisSwhsDesAnnWainByArea();

    /**
     *
     * @return
     */
    List<JSONObject> statisSwarnGradeNum();

    List<JSONObject> statisSwarnGradeByAdcd();

    /**
     * 可供水量计算总和
     * @return
     */
    BigDecimal calDesAnnWasuPop();

    BigDecimal totaldesannwasupop();

    List<JSONObject> waterSuantityStatisticNum(AttSwhsBaseReqVo vo);

    List<JSONObject> waterSuantityStatisticWarn(AttSwhsBaseReqVo vo);

    List<AttSwhsSignageBase> listSwhsSinageByCondition(@Param("swhsCodeList") List<String> swhsCodeList);
}
