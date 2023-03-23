package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 17:55
 */
@Data
public class SysArchiveLogReqVo {

    @ApiModelProperty(value = "第几页")
    private Integer pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private Integer pageSize=10;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;

    @ApiModelProperty(value = "查询时间 格式:yyyy-MM-dd")
    private String queryTime;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "操作类型 1:查看 2:下载 3:删除 4:更新")
    private String status;

    @ApiModelProperty(value = "文件id")
    private String id;

}
