package com.ygkj.big.screen.vo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 20:34
 */
@Data
public class ResultsWarnVo implements Serializable {

    private String lng;
    private String lat;
    private List<Villages> villages;

    private String promptlyMoveVillage;
    private String effectPeople;
    private String adcd;
    private String warningMoveVillage;
    private String countyName;
    private String adnm;
}
