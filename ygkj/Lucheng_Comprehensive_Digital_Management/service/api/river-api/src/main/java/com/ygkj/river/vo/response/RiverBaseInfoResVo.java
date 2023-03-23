package com.ygkj.river.vo.response;

import com.ygkj.river.model.AttRivInfoBase;
import com.ygkj.river.model.AttRivLkChiefBase;
import com.ygkj.river.model.AttRivSegBase;
import com.ygkj.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
@Data
@ApiModel("河段基础信息表")
public class RiverBaseInfoResVo extends AttRivSegBase {

    private static final long serialVersionUID = 6865497230715742307L;

    @ApiModelProperty("河流路径经纬度")
    String riverPath;

    @ApiModelProperty("河道等级名称")
    String levelName;

    @ApiModelProperty("河长列表")
    List<RivLkChiefResVo> riverChiefList = Collections.emptyList();

    public void doTranslate() {
        if (CollectionUtils.isNotBlank(riverChiefList)) {
            for (RivLkChiefResVo rivLkChiefResVo : riverChiefList) {
                rivLkChiefResVo.doTranslate();
            }
        }
    }
}
