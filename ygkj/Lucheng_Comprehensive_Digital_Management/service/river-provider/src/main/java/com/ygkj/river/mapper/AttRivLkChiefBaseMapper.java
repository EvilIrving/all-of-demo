package com.ygkj.river.mapper;

import com.ygkj.river.model.AttRivLkChiefBase;
import com.ygkj.river.vo.response.RivLkChiefResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xq
 * @description 河长/湖长基础信息表
 * @date 2021-08-04
 */
public interface AttRivLkChiefBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/04
     **/
    int insert(AttRivLkChiefBase attRivLkChiefBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/04
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/04
     **/
    int update(AttRivLkChiefBase attRivLkChiefBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    AttRivLkChiefBase load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/04
     **/
    List<AttRivLkChiefBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/04
     **/
    int pageListCount(int offset, int pagesize);


    @Select("select * from att_riv_lk_chief_base where del_flag=0 and type=1 and rpost=1")
    List<AttRivLkChiefBase> selectByCondition();

    @Select("select a.id,a.real_name,a.phone,a.post,a.level,a.rpost,a.type,b.riv_seg_id from att_riv_lk_chief_base as a " +
            " inner join rel_riv_seg_chief as b on a.id=b.chief_id" +
            " where a.del_flag=0 and find_in_set(b.riv_seg_id,#{rivSegIds}) and b.type=#{type}")
    List<RivLkChiefResVo> selectByRiverId(String rivSegIds, Integer type);
}
