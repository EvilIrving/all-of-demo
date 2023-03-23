package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.AttHydrFcstBase;
import com.ygkj.digitization.model.AttHydrFcstValBase;
import com.ygkj.digitization.vo.request.HydrFcstQueryVo;
import com.ygkj.digitization.vo.response.HydrFcstResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @description 水文预报表
 * @author xq
 * @date 2021-09-22
 */
public interface AttHydrFcstBaseMapper {

    /**
     * 新增 预报
     * @author xq
     * @date 2021/09/22
     **/
    int insert(AttHydrFcstBase attHydrFcstBase);

    /**
     * 刪除 预报
     * @author xq
     * @date 2021/09/22
     **/
    int delete(String id);

    /**
     * 更新 预报
     * @author xq
     * @date 2021/09/22
     **/
    int update(AttHydrFcstBase attHydrFcstBase);

    /**
     * 查询 根据主键 id 查询 预报
     * @author xq
     * @date 2021/09/22
     **/
    AttHydrFcstBase load(String id);

    /**
     * 新增 预报值
     * @author xq
     * @date 2021/09/22
     **/
    int replaceVal(AttHydrFcstValBase attHydrFcstValBase);

    /**
     * 更新 预报值
     * @author xq
     * @date 2021/09/22
     **/
    int updateVal(AttHydrFcstValBase attHydrFcstValBase);

    int deleteValByByFcstId(@Param("fcstId")String fcstId,@Param("tmList") List<Date> tmList);

    @Select("select tm from att_hydr_fcst_val_base where fcst_id=#{fcstId}")
    List<Date> selectValTmByFcstId(String fcstId);

    @Select("select * from att_hydr_fcst_val_base where fcst_id=#{fcstId} order by tm asc")
    List<AttHydrFcstValBase> selectValByFcstId(String fcstId);

    List<HydrFcstResVo> selectFcstByCondition(HydrFcstQueryVo queryVo);

    List<HydrFcstResVo> selectNewestFcst();

}
