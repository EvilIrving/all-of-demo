package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgLmrDisDom;
import org.springframework.stereotype.Repository;

@Repository
@DS("exchange")
public interface BhLgLmrDisDomMapper extends BaseMapper<BhLgLmrDisDom> {
}
