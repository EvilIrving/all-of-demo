package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgLmrDisDomSs;
import org.springframework.stereotype.Repository;

@Repository
@DS("exchange")
public interface BhLgLmrDisDomSsMapper extends BaseMapper<BhLgLmrDisDomSs> {
}
