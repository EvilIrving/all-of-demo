package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-17 16:14
 * @description
 */
@Data
public class DefenObjectReqVo extends PageEntity {

    @ApiModelProperty("id")
    private String id;
    //TODO 筛选参数未定
    private String key;
}
