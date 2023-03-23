package com.ygkj.digitization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;

import java.util.Date;

/**
 * @author xq
 * @Date 2021/2/6
 */
@Deprecated
@Api(tags = "降雨迁移")
public interface RainfallMigrateControllerApi {

    CommonResult getByHour(Date hourDate);
}
