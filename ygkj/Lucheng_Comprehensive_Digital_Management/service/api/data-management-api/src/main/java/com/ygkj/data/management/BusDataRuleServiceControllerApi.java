package com.ygkj.data.management;

import com.ygkj.data.management.dto.BusDataRuleDto;
import com.ygkj.data.management.vo.request.BusDataRuleQueryVo;
import com.ygkj.data.management.vo.request.CandidateRuleRequestVo;
import com.ygkj.data.management.vo.request.ColumnRuleBindVo;
import com.ygkj.data.management.vo.request.RowUpdateRuleBindVo;
import com.ygkj.data.management.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

@Api(tags = "数据规则维护", value = "数据规则维护")
@Validated
public interface BusDataRuleServiceControllerApi {

    @ApiOperation("条件查询（列表和单条都能用）")
    CommonResult rules(BusDataRuleQueryVo queryVo);

    @ApiOperation("新增数据规则")
    CommonResult add(BusDataRuleDto busDataRuleDto);

    @ApiOperation("修改数据规则")
    CommonResult update(BusDataRuleDto busDataRuleDto);

    @ApiOperation("删除数据规则（批量用逗号隔开）")
    CommonResult delete(String ids);

    @ApiOperation("获取绑定的规则（字段和记录通用）")
    CommonResult candidateRules(CandidateRuleRequestVo requestVo);

    @ApiOperation("绑定字段规则")
    CommonResult bindColumnRules(ColumnRuleBindVo bindVo);

    @ApiOperation("绑定记录更新规则")
    CommonResult bindRowRules(RowUpdateRuleBindVo bindVo);

    @ApiOperation("获取某张表所有字段正则校验规则绑定情况")
    CommonResult candidateColumnRules(String tableId);
}
