package com.ygkj.auth.api.archiveLabel;

import com.ygkj.auth.VO.request.ArchiveLabelPageQueryVO;
import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.auth.api.archiveLabel.model.SysArchiveLabel;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/1/19 18:46
 */
@Api(tags = "档案标签服务")
public interface SysArchiveLabelApi {

    @ApiOperation(value = "档案标签详情")
    SysArchiveLabel detailInfo(String id);

    @ApiOperation(value = "档案收藏分页查询")
    PageVO<SysArchiveLabel> pageInfo(ArchiveLabelPageQueryVO vo);

    @ApiOperation(value = "新增档案收藏")
    CommonResult addSysArchiveLabel(SysArchiveLabel vo);

    @ApiOperation(value = "删除档案收藏")
    CommonResult deleteSysArchiveLabel(String id);

    @ApiOperation(value = "更新档案收藏")
    CommonResult updateSysArchiveLabel(SysArchiveLabel vo);

}
