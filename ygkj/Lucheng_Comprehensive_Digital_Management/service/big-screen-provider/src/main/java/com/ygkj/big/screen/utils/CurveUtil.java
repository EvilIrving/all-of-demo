package com.ygkj.big.screen.utils;

import com.ygkj.big.screen.model.ReservoirCapacityCurve;

import java.util.List;

/**
 * @author: fml
 * @Date: 2020/9/23 01:01
 * @Description: 库容曲线计算
 */
public class CurveUtil {

    /**
     * 根据水位获取库容、流量...
     *
     * @param rz             水位
     * @param capacityCurves 水位（库容、流量...） 对应关系
     * @return Double
     */
    public static Double getYByRz(double rz, List<ReservoirCapacityCurve> capacityCurves) {
        double result = 0.0;

        ReservoirCapacityCurve before = null;
        ReservoirCapacityCurve after = null;
        for (ReservoirCapacityCurve curve : capacityCurves) {
            if (curve.getWaterLevel().doubleValue() == rz) {
                result = curve.getCapacity().doubleValue();
                break;
            }
            if (curve.getWaterLevel().doubleValue() < rz) {
                before = curve;
            }
            if (curve.getWaterLevel().doubleValue() > rz) {
                after = curve;
                break;
            }
        }
        if (before != null && after != null) {
            Double bY = before.getWaterLevel().doubleValue();
            Double aY = after.getWaterLevel().doubleValue();
            Double bZ = before.getCapacity().doubleValue();
            Double aZ = after.getCapacity().doubleValue();

            Double zm = DoubleUtils.sub(aZ, bZ);
            Double yb = DoubleUtils.sub(aY, bY);
            Double yb2 = DoubleUtils.sub(rz, bY);
            Double mul = DoubleUtils.mul(zm, yb2);

            result = DoubleUtils.sum(DoubleUtils.div(mul, yb, 2), bZ);
        }

        return result;
    }

}

