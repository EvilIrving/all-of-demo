package com.ygkj.river.mapper;

import com.ygkj.entity.SingletonMap;
import com.ygkj.river.model.AttRivInletBase;
import com.ygkj.river.vo.request.RivInletReqVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @description att_riv_inlet_base
 * @date 2021-08-06
 */
public interface AttRivInletBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/06
     **/
    int insert(AttRivInletBase attRivInletBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/06
     **/
    int delete(String expNo);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/06
     **/
    int update(AttRivInletBase attRivInletBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/06
     **/
    AttRivInletBase load(String expNo);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/06
     **/
    List<AttRivInletBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/06
     **/
    int pageListCount(int offset, int pagesize);


    List<AttRivInletBase> selectByCondition(RivInletReqVo reqVo);

    @Select("select area_code as `key`,count(1) as `value` from att_riv_inlet_base where del_flag=0 and record_type=#{recordType} group by area_code")
    List<SingletonMap<String, Long>> countByAreaCode(Integer recordType);

}
