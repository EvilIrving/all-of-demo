package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/20 17:03
 */
@Data
public class SysFileReqVo {

    @ApiModelProperty(value = "第几页")
    private Integer pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private Integer pageSize=10;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;

    @ApiModelProperty(value = "标签ids")
    private String labelIds;

    @ApiModelProperty(value = "时间 格式：yyyy")
    private String year;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件夹id")
    private String achievementId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "文件id")
    private String id;

    @ApiModelProperty(value = "是否其他")
    private boolean otherType;
}
