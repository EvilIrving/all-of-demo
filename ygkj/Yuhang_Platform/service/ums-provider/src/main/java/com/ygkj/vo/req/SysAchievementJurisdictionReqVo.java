package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/20 15:01
 */
@Data
public class SysAchievementJurisdictionReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;

    @ApiModelProperty(value = "权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限")
    private String type;

    @ApiModelProperty(value = "文档id")
    private String achievementId;
}
