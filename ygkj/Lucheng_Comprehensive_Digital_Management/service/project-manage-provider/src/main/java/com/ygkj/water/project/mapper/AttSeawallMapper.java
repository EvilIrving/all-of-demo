package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.digitization.model.AttTideFcstValBase;
import com.ygkj.entity.SingletonMap;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.project.model.SeawallData;
import com.ygkj.project.model.SeawallWarnRecord;
import com.ygkj.project.vo.response.AttStResVo;
import com.ygkj.project.vo.response.SeawallPointResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@DS("basic")
public interface AttSeawallMapper extends BaseMapper<AttSeawallBase> {

    @DS("business")
    List<SingletonMap<String, Long>> warnTimes(Date dataTime);

    @DS("business")
    int insertWarnRecord(SeawallWarnRecord warnRecord);

    @DS("business")
    List<SeawallWarnRecord> candidateSeawallWarnRecord(List<String> seawallCodeList, Date dataTime, int pastMinutes);

    /**
     * 查询未来N分钟最高潮位
     *
     * @param stcdList      测站编码
     * @param futureMinutes 未来N分钟
     * @return
     */
    List<AttTideFcstValBase> selectForecastHighestTdz(List<String> stcdList, Date dataTime, int futureMinutes);

    /**
     * 海塘关联测站
     *
     * @param seawallCode
     * @return
     */
    List<AttStResVo> listSt(String seawallCode);

    /**
     * 根据工程编码查询海塘
     *
     * @param seawallCode
     * @return
     */
    List<AttSeawallBase> selectByCode(String seawallCode);

    @Select("SELECT COUNT(1) FROM bh_duty_record a LEFT JOIN project_code_mapping b ON b.foreign_prcd = a.PID " +
            "WHERE DATE_FORMAT(a.DUTY_DATE,'%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d') AND b.type = 1 AND local_prcd = #{seawallCode}")
    @DS("exchange")
    int isScheduling(@Param("seawallCode") String seawallCode);

    @DS("business")
    @Select("SELECT * FROM seawall_data WHERE prcd = #{seawallCode}")
    SeawallData getSeawallGcyg(@Param("seawallCode") String seawallCode);

    @Select("SELECT count(1) from sl_proj_st_r where project_code = #{seawallCode}")
    int isBindSt(@Param("seawallCode") String seawallCode);


    List<SeawallPointResVo> listSeawallPoint(String seawallCode);
}
