package com.ygkj.supervision;

import com.ygkj.supervision.model.AttSupervisionDescription;
import com.ygkj.supervision.vo.request.DescriptionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lxl
 * @date 2021/5/11
 */
@Api(tags = "问题描述管理", value = "问题描述管理")
public interface AttSupervisionDescriptionControllerApi {

    /**
     * 新增问题描述
     *
     * @param attSupervisionDescription
     * @return
     */
    @ApiOperation("新增问题描述")
    CommonResult insertDescription(AttSupervisionDescription attSupervisionDescription);

    /**
     * 更新问题描述
     *
     * @param attSupervisionDescription
     * @return
     */
    @ApiOperation("更新问题描述")
    CommonResult updateDescription(AttSupervisionDescription attSupervisionDescription);


    /**
     * 根据条件获取问题描述列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("根据条件获取问题描述列表")
    CommonResult getListByCondition(DescriptionReqVo vo);

    /**
     * 查询所有问题描述
     *
     * @param vo
     * @return
     */
    @ApiModelProperty("查询所有问题描述")
    CommonResult getListAll(DescriptionReqVo vo);


    /**
     * 查询所有无子集（最底层）问题描述
     *
     * @param vo
     * @return
     */
    @ApiModelProperty("查询所有无子集（最底层）问题描述")
    CommonResult getNullChildList(DescriptionReqVo vo);


    /**
     * 级联删除问题描述
     *
     * @param descriptCode
     * @return
     */
    @ApiModelProperty("/级联删除问题描述")
    CommonResult deleteDescription(String descriptCode);


//    @ApiOperation("工具接口 导入问题描述 ")
//    CommonResult updateDescription(String fileCode) throws IOException;

    /**
     * 工具接口 导入问题描述
     *
     * @param file
     * @param fileCode
     * @return
     */
    @ApiOperation("工具接口 导入问题描述")
    CommonResult fileImportToExcel(MultipartFile file, String fileCode);

}
