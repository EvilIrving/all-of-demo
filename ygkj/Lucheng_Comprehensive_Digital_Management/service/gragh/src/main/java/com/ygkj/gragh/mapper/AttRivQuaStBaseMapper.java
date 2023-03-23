package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttRivQuaStBase;
import com.ygkj.gragh.vo.request.RivQuaReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.RivQuaResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河道水质测站表
 * @date 2021-08-04
 */
public interface AttRivQuaStBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/04
     **/
    int insert(AttRivQuaStBase attRivQuaStBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/04
     **/
    int delete(String siteNo);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/04
     **/
    int update(AttRivQuaStBase attRivQuaStBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    AttRivQuaStBase load(String siteNo);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    List<AttRivQuaStBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/04
     **/
    int pageListCount(int offset, int pagesize);


    /**
     * @param reqVo
     * @return
     */
    List<AttRivQuaStBase> selectByCondition(RivQuaReqVo reqVo);

    // todo 因为河长通那边更新频率低，所以目前选的是两个月的时间过滤

    /**
     * @param dataTime  数据时刻，如果为空就查最新的，否则，查询截止dataTime 最新的
     * @param siteNames 编码，以半角逗号隔开
     * @return
     */
    @DS("business")
    List<RivQuaResVo> selectNewestValueBySiteName(Date dataTime, String siteNames);


    /**
     * 查询最近几个月的水质数据
     *
     * @param siteNames 编码，以半角逗号隔开
     * @return
     */
    @DS("business")
    List<RivQuaResVo> selectRecentMonthValueBySiteName(String siteNames, int month);

    @DS("business")
    List<RivQuaResVo> selectValueByTimeInterval(@Param("siteName") String siteName, @Param("start") Date start, @Param("end") Date end);

    @Select("select area_code as `key`,count(1) as `value` from att_riv_qua_st_base where del_flag=0 group by area_code")
    List<ChartResVo<String, Long>> countByAreaCode();

    @Select("select concat(level,'') as `key`,count(1) as `value` from att_riv_qua_st_base where del_flag=0 group by level")
    List<ChartResVo<String, Long>> countByLevel();
}
