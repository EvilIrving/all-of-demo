package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 18:20
 */
@Data
public class SysArchiveCollectionReqVO {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;

}
