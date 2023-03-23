package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BsDrillStdata;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 测站数据表
 * @date 2021-07-08
 */
@DS("meta")
public interface BsDrillStdataMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/07/08
     **/
    int insert(BsDrillStdata bsDrillStdata);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/07/08
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/07/08
     **/
    int update(BsDrillStdata bsDrillStdata);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    BsDrillStdata load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/07/08
     **/
    List<BsDrillStdata> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/07/08
     **/
    int pageListCount(int offset, int pagesize);

    BsDrillStdata selectNoDataByUniqueIndex(@Param("stcaseId") String stcaseId,
                                            @Param("caseId") String caseId,
                                            @Param("timeStamp") Date timeStamp);

    List<BsDrillStdata> selectDataByTimeInterval(@Param("stcaseId") String stcaseId,
                                                 @Param("caseId") String caseId,
                                                 @Param("start") Date start,
                                                 @Param("end") Date end);

}
