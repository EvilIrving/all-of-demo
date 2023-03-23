package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.CollectPoint;
import com.ygkj.visualization.vo.request.CollectPointQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xq
 * 
 * @Date 2021/2/26
 */
@DS("business")
public interface CollectPointMapper {
    /**
     * [新增]
     * @author xq
     * @date 2021/02/26
     **/
    int insert(CollectPoint collectPoint);

    int deleteByIdList(@Param("idList")List<String> idList, @Param("userId") String userId);

    List<CollectPoint> selectAll(@Param("userId")String userId,@Param("name") String name);

    List<CollectPoint> selectByCondition(CollectPointQueryVo queryVo);

}
