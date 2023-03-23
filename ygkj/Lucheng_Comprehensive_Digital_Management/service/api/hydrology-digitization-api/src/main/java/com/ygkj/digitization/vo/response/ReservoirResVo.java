package com.ygkj.digitization.vo.response;

import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.math.BigDecimal;

@Data
public class ReservoirResVo {

    private String resCode;

    private String resName;

    private BigDecimal meiFloodLimitWaterLevel;

    private BigDecimal yphoonFloodLimitWaterLevel;

    private BigDecimal uppLevFlco;

    private BigDecimal normWatLev;

    private String engScal;

    private String mainStcd;
}
