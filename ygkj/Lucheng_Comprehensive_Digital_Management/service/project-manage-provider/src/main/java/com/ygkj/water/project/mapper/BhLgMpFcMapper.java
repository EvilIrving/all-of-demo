package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgMpFc;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.project.vo.response.BhLgMpFcResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2022-04-26 15:32
 * @description 汛前检查报告 mapper
 */
@DS("exchange")
public interface BhLgMpFcMapper extends BaseMapper<BhLgMpFc> {


    List<BhLgMpFcResVo> selectBhLgMpFcList(@Param("reqVo") ProjCheckReqVo reqVo);

}
