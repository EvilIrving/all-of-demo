package com.ygkj.supervision.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MatterQueryReqVo extends PageEntity {

    @ApiModelProperty("事项类型")
    private String matterType;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("人员id --前端不需要传递（根据token获取填入）")
    private String userId;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("开始时间")
    private String start;

    @ApiModelProperty("结束时间")
    private String end;


}
