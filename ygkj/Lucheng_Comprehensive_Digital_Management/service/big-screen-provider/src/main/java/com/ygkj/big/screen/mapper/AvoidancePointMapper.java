package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.vo.request.AvoidancePointReqVo;
import com.ygkj.big.screen.vo.response.AvoidancePointRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-20 18:50
 */
@DS("basic")
public interface AvoidancePointMapper {

    List<AvoidancePointRespVo> pageList(AvoidancePointReqVo vo);

    List<AvoidancePointRespVo> load(AvoidancePointReqVo vo);

    int insert(AvoidancePointReqVo vo);
    int update(AvoidancePointReqVo vo);

    int delete(AvoidancePointReqVo vo);
}
