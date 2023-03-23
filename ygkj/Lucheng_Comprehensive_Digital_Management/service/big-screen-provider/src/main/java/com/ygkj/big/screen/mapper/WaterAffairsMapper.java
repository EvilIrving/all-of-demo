package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.HikvisionCameraRecord;
import com.ygkj.big.screen.model.ReservoirCapacityCurve;
import com.ygkj.big.screen.model.WtSoilErosionProjBase;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.gragh.model.AttCwsBase;
import com.ygkj.gragh.vo.request.WaterOperateReqVo;
import com.ygkj.gragh.vo.response.WaterOperateResVo;
import com.ygkj.project.model.AttCwsBaseWcp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fml
 * @date 2021-03-30 11:32
 * @description 水事务监管专题
 */
public interface WaterAffairsMapper {

    @DS("business")
    List<WaSoilErosionRespVo> getSoilEros(@Param("year") String year);

    @DS("business")
    List<WtSoilErosionProjBase> getSoilEroProjs(@Param("year") String year, @Param("id") String id);

    List<Map<String, Object>> getCameraAdcdStats(@Param("warnStatus") String warnStatus);

    List<WzAdcdInfo> getProjTypeCameraStats(@Param("warnStatus") String warnStatus);

    List<HikvisionCameraRecord> getCameras(@Param("status") String status);

    List<RpWaterLevelsRespVo> getStBases(@Param("stType") String stType);

    List<Map<String, Object>> getResWaterLevels(@Param("tableName") String tableName);

    List<WaResStaWaterLevelInfo> getResStas();

    List<ReservoirCapacityCurve> getCapacityCurveByStCode(@Param("stCode") String stCode);

    List<WzAdcdInfo> yearSoilErosionStats();

    List<WzAdcdInfo> adcdSoilErosionStats(@Param("year") String year);

    List<WzAdcdInfo> yearSoilErosionGovernStats();

    List<WzAdcdInfo> adcdSoilErosionGovernStats(@Param("year") String year);

    List<WzAdcdInfo> yearSoilErosionProjStats();

    List<WzAdcdInfo> adcdSoilErosionProjStats(@Param("year") String year);

    /**
     * 获取所有农饮水工程 方便进行统计
     *
     * @return
     */
    List<AttCwsBaseWcp> getAllCwsBase();

    /**
     * 查询水厂运行状态列表
     *
     * @param reqVo
     * @return
     */
    List<WaterOperateResVo> getWaterOperateNum(WaterOperateReqVo reqVo);
}
