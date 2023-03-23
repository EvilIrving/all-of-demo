package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UserPageReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 16:45
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 16:45
 * @Version: 0.0.1
 */
@Data
public class UserPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户电话号码")
    private String phone;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "组织id")
    private String deptId;
    @ApiModelProperty(value = "账户状态(1.正常 2.锁定 ")
    private Integer status;
    @ApiModelProperty(value = "用户类型")
    private Integer userType;
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "是否查询全部记录，true：是，false：否")
    private boolean fetchAll;
}
