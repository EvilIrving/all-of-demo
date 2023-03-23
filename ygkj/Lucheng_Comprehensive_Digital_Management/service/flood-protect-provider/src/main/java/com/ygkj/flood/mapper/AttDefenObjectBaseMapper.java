package com.ygkj.flood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.flood.model.AttDefenObjectBase;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-17 16:19
 * @description
 */
public interface AttDefenObjectBaseMapper extends BaseMapper<AttDefenObjectBase> {

    @Select("select id,admin_adnm,natural_adnm,town,defen_object,contact_tel from att_defen_object_base where del_flag = 0")
    List<AttDefenObjectBase> defenObjectTree();
}
