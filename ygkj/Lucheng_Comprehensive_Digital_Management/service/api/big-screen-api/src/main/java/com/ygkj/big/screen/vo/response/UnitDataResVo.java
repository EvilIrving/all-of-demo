package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.BsUnitData;
import com.ygkj.big.screen.model.BsUnitDataIndex;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/7/9
 */
@Data
public class UnitDataResVo extends BsUnitDataIndex {

    private static final long serialVersionUID = 4789812300569954679L;

    String dataValue;

    Object originData;

    Class originDataType;

    public BsUnitData buildUnitDataObj() {
        BsUnitData result = new BsUnitData();
        result.setDataId(this.getDataId());
        result.setDataValue(this.getDataValue());
        return result;
    }
}
