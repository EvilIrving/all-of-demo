package com.ygkj.utils;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xq
 * @Description 电子围栏工具类，用于判断一个点是否在某个区域内
 * https://blog.csdn.net/qq_39190679/article/details/102524601
 * @Date 2021/4/29
 */
public class ElectrFenceUtils {
    /**
     * 地球半径
     */
    private static double EARTH_RADIUS = 6378138.0;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算是否在圆上（单位/千米）
     *
     * @param radius 半径
     * @param lat1   纬度
     * @param lng1   经度
     * @return boolean
     * @Title: GetDistance
     */
    public static boolean isInCircle(double radius, double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        if (s > radius) {//不在圆上
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否在矩形区域内
     *
     * @param lat    测试点经度
     * @param lng    测试点纬度
     * @param minLat 纬度范围限制1
     * @param maxLat 纬度范围限制2
     * @param minLng 经度限制范围1
     * @param maxLng 经度范围限制2
     * @return boolean
     * @Title: isInArea
     */
    public static boolean isInRectangleArea(double lat, double lng, double minLat,
                                            double maxLat, double minLng, double maxLng) {
        if (isInRange(lat, minLat, maxLat)) {//如果在纬度的范围内
            if (minLng * maxLng > 0) {
                if (isInRange(lng, minLng, maxLng)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (Math.abs(minLng) + Math.abs(maxLng) < 180) {
                    if (isInRange(lng, minLng, maxLng)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    double left = Math.max(minLng, maxLng);
                    double right = Math.min(minLng, maxLng);
                    if (isInRange(lng, left, 180) || isInRange(lng, right, -180)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否在经纬度范围内
     *
     * @param point
     * @param left
     * @param right
     * @return boolean
     * @Title: isInRange
     */
    public static boolean isInRange(double point, double left, double right) {
        return point >= Math.min(left, right) && point <= Math.max(left, right);
    }


    /**
     * 判断点是否在多边形内
     *
     * @param point 测试点
     * @param pts   多边形的点
     * @return boolean
     * @Title: IsPointInPoly
     */
    public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> pts) {

        int N = pts.size();
        boolean boundOrVertex = true;
        int intersectCount = 0;//交叉点数量
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;//临近顶点
        Point2D.Double p = point; //当前点

        p1 = pts.get(0);
        for (int i = 1; i <= N; ++i) {
            if (p.equals(p1)) {
                return boundOrVertex;
            }

            p2 = pts.get(i % N);
            if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
                p1 = p2;
                continue;
            }

            //射线穿过算法
            if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
                if (p.y <= Math.max(p1.y, p2.y)) {
                    if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
                        return boundOrVertex;
                    }

                    if (p1.y == p2.y) {
                        if (p1.y == p.y) {
                            return boundOrVertex;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
                        if (Math.abs(p.y - xinters) < precision) {
                            return boundOrVertex;
                        }

                        if (p.y < xinters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (p.x == p2.x && p.y <= p2.y) {
                    Point2D.Double p3 = pts.get((i + 1) % N);
                    if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }
        if (intersectCount % 2 == 0) {//偶数在多边形外
            return false;
        } else { //奇数在多边形内
            return true;
        }
    }

    @Data
    public static class LongLatitude{
        double longitude;
        double latitude;
    }

    public static void main(String[] args) {
        String s = "[[[120.78787153933898, 27.96668053423711], [120.7429587781593, 27.98528240329001], [120.71676178168373, 28.00495074156693], [120.66821940077799, 28.00564408247621], [120.5822196668677, 28.003061451015526], [120.5644461462979, 27.98976402047686], [120.55408001107497, 27.94990724763195], [120.55408001107497, 27.916764428615533], [120.54643251158674, 27.882494160444107], [120.54848517036336, 27.857203164458838], [120.57793648363163, 27.832282895646895], [120.61611843768837, 27.80979448473669], [120.63786697991134, 27.793483123374017], [120.67077802082453, 27.790098691899455], [120.71553647468997, 27.779677511642237], [120.76171051468005, 27.793105283385557], [120.79141390249217, 27.839832627905494], [120.81113207577957, 27.889317841904738], [120.79142006391164, 27.934626836722195], [120.7900607460443, 27.968155831765188]]]";
        JSONArray temp = JSONArray.parseArray(s).getJSONArray(0);
        List<Point2D.Double> list = new ArrayList<>();

        for (Object o : temp) {
            JSONArray ja = (JSONArray) o;
            list.add(Builder.of(Point2D.Double::new)
                    .with(Point2D.Double::setLocation, ja.getDouble(0), ja.getDouble(1))
                    .build());
        }
        /**
         *
         * 120.45708343594225 27.924788036255702
         * 120.6027429443959 27.652890287142224
         * 120.84550879181866 27.895656134564973
         * 120.69790715658561 28.02772075556295 不在
         * 120.70179141014438 27.95197781116705
         * 120.62022208541035 27.887887627447444
         * 120.69790715658561 27.84127658474228 在
         */
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.45708343594225D, 27.924788036255702D)
                .build(),list));
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.6027429443959D, 27.652890287142224D)
                .build(),list));
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.84550879181866D, 27.895656134564973D)
                .build(),list));
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.69790715658561D, 28.02772075556295D)
                .build(),list));


        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.70179141014438D, 27.95197781116705D)
                .build(),list));
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.62022208541035D, 27.887887627447444D)
                .build(),list));
        System.out.println(isInPolygon(Builder.of(Point2D.Double::new)
                .with(Point2D.Double::setLocation, 120.69790715658561D, 27.84127658474228D)
                .build(),list));

    }
}
