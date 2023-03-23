package com.ygkj.river.vo.response;

import com.ygkj.river.model.AttRivLkChiefBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/10
 */
@Data
@ApiModel("河长信息")
public class RivLkChiefResVo extends AttRivLkChiefBase {
    private static final long serialVersionUID = 1098986648124482560L;

    @ApiModelProperty("河段id")
    Integer rivSegId;

    @ApiModelProperty("河长等级")
    String levelName;

    @ApiModelProperty("河长身份")
    String rpostName;

    public void doTranslate() {
        if (super.getLevel() != null) {
            switch (super.getLevel()) {
                case 2:
                    levelName = "省级";
                    break;
                case 3:
                    levelName = "市级";
                    break;
                case 4:
                    levelName = "县级";
                    break;
                case 5:
                    levelName = "镇级";
                    break;
                case 6:
                    levelName = "村级";
                    break;
            }
        }
        if (super.getRpost() != null) {
            switch (super.getRpost()) {
                case 1:
                    if (super.getType() == 1) {
                        rpostName = "河长";
                    } else {
                        rpostName = "库长";
                    }
                    break;
                case 2:
                    if (super.getType() == 1) {
                        rpostName = "督察";
                    } else {
                        rpostName = "湖长";
                    }
                    break;
                case 3:
                    if (super.getType() == 1) {
                        rpostName = "警长";
                    } else {
                        rpostName = "督察长";
                    }
                    break;
            }
        }


    }
}
