package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhDepPersontrain;
import org.springframework.stereotype.Repository;

@Repository
@DS("exchange")
public interface BhDepPersontrainMapper extends BaseMapper<BhDepPersontrain> {
}
