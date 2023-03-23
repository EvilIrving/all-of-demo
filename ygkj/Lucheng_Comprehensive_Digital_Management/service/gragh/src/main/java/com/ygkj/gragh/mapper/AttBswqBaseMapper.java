package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttBswqBase;
import com.ygkj.gragh.model.BswqRatio;
import com.ygkj.gragh.vo.request.BswqReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 交界断面水质测站表
 * @date 2021-04-21
 */

public interface AttBswqBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/21
     **/
    int insert(AttBswqBase attBswqBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/04/21
     **/
    int delete(String code);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/04/21
     **/
    int update(AttBswqBase attBswqBase);

    /**
     * 查询 根据主键 code 查询
     *
     * @author xq
     * @date 2021/04/21
     **/
    AttBswqBase load(String code);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/04/21
     **/
    List<AttBswqBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/04/21
     **/
    int pageListCount(int offset, int pagesize);

    List<AttBswqBase> selectByCondition(BswqReqVo reqVo);

    // todo 因为大数据局那边更新频率低，所以目前选的是两个月的时间过滤

    /**
     * @param codes 编码，以半角逗号隔开
     * @return
     */
    @DS("business")
    List<BswqResVo> selectNewestValueByCode(String codes);

    @DS("business")
    List<BswqResVo> selectValueByCodeAndTime(@Param("code") String code, @Param("start") Date start, @Param("end") Date end);

    @DS("business")
    List<BswqRatio> bswqRatio(@Param("type") int type, @Param("start") Date start, @Param("end") Date end);

    @Select("select adcd as `key`,count(1) as `value` from att_bswq_base where del_flag=0 group by adcd")
    List<ChartResVo<String, Long>> countByAdcd();

}
