package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.SectionModelData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionModelDataMapper extends BaseMapper<SectionModelData> {
    @DS("basic")
    void replaceAll(List<SectionModelData> list);
}
