package com.ygkj.warning;

import com.ygkj.warning.vo.request.GroupWarnLogQueryVo;
import com.ygkj.warning.vo.request.RelWarnGroupAddVo;
import com.ygkj.warning.vo.request.RelWarnGroupQueryVo;
import com.ygkj.warning.vo.request.SysWarnLogCommentAddVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2021/4/9 11:21
 * @Description:
 */
@Api(tags = "预警系统", value = "预警系统")
public interface SysWarnLogControllerApi {

    @ApiOperation("预警消息")
    CommonResult logs(GroupWarnLogQueryVo queryVo);

    @ApiOperation("预警信息评论框")
    CommonResult warnLogComments(Long id);

    @ApiOperation("当前用户未读的站内预警信息")
    CommonResult currentUserUnReadWarnLogs();

    @ApiOperation("更新站内消息为已读")
    CommonResult readLog(Long id);

    @ApiOperation("添加一条评论")
    CommonResult comment(SysWarnLogCommentAddVo addVo);

    @ApiOperation("查找当前对象绑定的预警组")
    CommonResult groups(RelWarnGroupQueryVo queryVo);

    @ApiOperation("关联报警配置")
    CommonResult bindRelation(RelWarnGroupAddVo addVo);
}
