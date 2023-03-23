package com.ygkj.project.vo.request;

import com.github.pagehelper.Page;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/25
 */
@Data
@ApiModel("海塘列表查询入参")
public class SeawallQueryReqVo extends PageEntity {

    private static final long serialVersionUID = -6721767844659360726L;

    @ApiModelProperty("海塘名称")
    String seawallName;

    @ApiModelProperty("海塘编码")
    String seawallCode;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("工程等别：1级、2级、3级、4级、5级")
    String grade;

    @ApiModelProperty("安全鉴定：一类塘、二类塘、三类塘、待鉴定")
    String safetyConclusion;

    @ApiModelProperty("100年以上、100年一遇、50年一遇、20年一遇、10年一遇、10年以下")
    String designTideStandard;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("导出策略：0：海塘组件列表导出")
    private Integer exportStrategy = 0;

    //海塘编码集合
    private List<String> codes;
}
