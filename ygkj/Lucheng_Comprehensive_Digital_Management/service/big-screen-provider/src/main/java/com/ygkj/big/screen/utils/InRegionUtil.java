package com.ygkj.big.screen.utils;

import java.util.List;

/**
 * @author wgf
 * @date 2022/9/7 10:06
 * @Description: 计算经纬度是否在这个范围内
 */
public class InRegionUtil {

    /**
     * @param longitudeCur
     * @param latitudeCur
     * @param pathList  {[longitude,latitude],[longitude,latitude],[longitude,latitude]}
     * @return
     */
    public static boolean IsInRegion(double longitudeCur, double latitudeCur, List<Double[]> pathList) {
        //点小于3无法构成多边形
        if (pathList.size() < 3)
        {
            return false;
        }
        int iSum = 0;
        int pathCount = pathList.size();
        double longStart = 0, latiStart = 0, longEnd = 0, latiEnd = 0;
        double dLong = 0;
        for (int i = 0; i < pathCount; i++) {
            int nextIndex = i + 1;
            if (i == pathCount - 1) {
                nextIndex = 0;
            }
            longStart = pathList.get(i)[0];
            latiStart = pathList.get(i)[1];
            longEnd = pathList.get(nextIndex)[0];
            latiEnd = pathList.get(nextIndex)[1];

            //判断纬度即Y坐标是否在2点的Y坐标内，只有在其内水平线才会相交
            if ((latitudeCur >= latiStart && latitudeCur < latiEnd) ||
                    (latitudeCur >= latiEnd && latitudeCur < latiStart)) {
                if (Math.abs(latiStart - latiEnd) > 0) {
                    dLong = longStart - ((longStart - longEnd) * (latiStart - latitudeCur)) / (latiStart - latiEnd);
                    if (dLong < longitudeCur) {
                        iSum++;
                    }
                }
            }
        }

        if ((iSum % 2) != 0) {
            return true;
        }
        return false;
    }
}
