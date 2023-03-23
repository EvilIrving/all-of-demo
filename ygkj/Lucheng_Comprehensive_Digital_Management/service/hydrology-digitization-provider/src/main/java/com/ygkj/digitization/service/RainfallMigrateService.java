package com.ygkj.digitization.service;

import com.ygkj.digitization.model.RainfallMigrate;
import com.ygkj.digitization.vo.response.RainfallMigrateResVo;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * 
 * @Date 2021/2/6
 */
@Deprecated
public interface RainfallMigrateService {

  List<RainfallMigrateResVo> getByHour(Date hourDate);
}
