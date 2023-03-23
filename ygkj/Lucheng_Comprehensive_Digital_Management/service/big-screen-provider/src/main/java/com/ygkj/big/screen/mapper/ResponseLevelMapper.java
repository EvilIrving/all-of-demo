package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.ResponseLevel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/9 14:07
 * @Description:
 */
@Repository
public interface ResponseLevelMapper extends BaseMapper<ResponseLevel> {

    @Select("SELECT * FROM flood_control_and_presentation WHERE LEFT(tm,10) = #{date} AND type = '1' ORDER BY tm DESC")
    List<JSONObject> getAllHoursByDate(@Param("date") String date);

    @Select("SELECT * FROM flood_control_and_presentation WHERE LEFT(tm,7) = #{date} AND type = '2' ORDER BY tm DESC")
    List<JSONObject> getAllMonthByDate(@Param("date") String date);
}
