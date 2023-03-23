package com.ygkj.visualization.vo.response;

import com.ygkj.visualization.model.RealTyphoonData;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/23
 */
@Data
public class TyphoonStatisticResVo {
    Integer typhoonNum;
    RealTyphoonData maxTyphoonInfo;
    List<Map<String, Object>> typhoonNumGroupByYear;
    // lowerEight,lowerTen,lowerTwelve,lowerFourteen,biggerFourteen
    List<Map<String, Object>> typhoonNumGroupByPower;

    public final static String lowerEight = "<=8";
    public final static String lowerTen = "8~10";
    public final static String lowerTwelve = "10~12";
    public final static String lowerFourteen = "12~14";
    public final static String biggerFourteen = ">14";

}
