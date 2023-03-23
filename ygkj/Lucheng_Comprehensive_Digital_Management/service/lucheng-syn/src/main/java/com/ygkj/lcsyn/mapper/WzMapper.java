package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.RsvrReportFlood;

import java.util.List;

@DS("wz")
public interface WzMapper {

    List<RsvrReportFlood> list(List<String> list);
}
