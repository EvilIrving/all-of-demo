package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgMfd;
import org.springframework.stereotype.Repository;

@Repository
@DS("exchange")
public interface BhLgMfdServiceMapper extends BaseMapper<BhLgMfd> {
}
