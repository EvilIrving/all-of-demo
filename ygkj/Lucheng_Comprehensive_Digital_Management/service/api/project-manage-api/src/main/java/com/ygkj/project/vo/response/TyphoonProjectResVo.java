package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.*;
import com.ygkj.project.model.AttAllRealProjectBase;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/7/16
 */
@Data
public class TyphoonProjectResVo extends AttAllRealProjectBase {

    //    水库、山塘、堤防、海塘、泵站、水闸、闸站、灌区、农饮水、水电站
    AttResBase rsvrInfo;
    AttMnpdBase mnpdInfo;
    AttDikeBase dikeInfo;
    AttSeawallBase seawallInfo;
    AttPustBase pustInfo;
    AttWagaBase wagaInfo;
    AttGateBase gateInfo;
    AttIrrBase irrInfo;
    AttCwsBase cwsInfo;
    AttHystBase hystInfo;
}
