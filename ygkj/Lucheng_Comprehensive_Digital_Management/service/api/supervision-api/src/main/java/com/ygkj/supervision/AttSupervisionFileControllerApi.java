package com.ygkj.supervision;


import com.ygkj.supervision.model.AttSupervisionFile;
import com.ygkj.supervision.vo.request.FileReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @date 2021/5/10
 */
@Api(tags = "文件表管理", value = "文件表管理")
public interface AttSupervisionFileControllerApi {

    /**
     * 新增文件
     *
     * @param attSupervisionFile
     * @return
     */
    @ApiOperation("新增文件")
    CommonResult insertFile(AttSupervisionFile attSupervisionFile);

    /**
     * 条件筛选获取文件列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("条件筛选获取文件列表")
    CommonResult getFileListByCondition(FileReqVo vo);


    /**
     * 更新文件信息
     *
     * @param attSupervisionFile
     * @return
     */
    @ApiOperation("更新文件信息")
    CommonResult updateFile(AttSupervisionFile attSupervisionFile);

    /**
     * 逻辑删除文件记录
     *
     * @param fileCode
     * @return
     */
    @ApiOperation("逻辑删除文件记录")
    CommonResult deleteFile(String fileCode);

}
