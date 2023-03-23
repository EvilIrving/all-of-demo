package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzh
 * @Date: 2020/10/23 16:07
 * @Description:
 */
@Data
public class ProjectTagUpdateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标签id")
    private String tagId;

    @ApiModelProperty("标签名")
    private String tag;
}
