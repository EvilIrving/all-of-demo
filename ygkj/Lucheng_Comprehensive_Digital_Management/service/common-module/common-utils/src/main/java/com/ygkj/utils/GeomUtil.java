package com.ygkj.utils;

import com.alibaba.fastjson.JSON;
import com.ygkj.entity.PointEntity;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GeomUtil {

    private static final String LOCATE_URL_PREFIX = "https://caiyunapp.com/fcgi-bin/v1/coord2text.py?lang=zh_CN&latlng=";

    public static String locate(String longitude, String latitude) {
        String index = latitude.concat(",").concat(longitude);
        String address = HttpClientUtil.httpGet(LOCATE_URL_PREFIX.concat(index));
        return JSON.parseObject(address).getString("address");
    }

    public static boolean inScope(List<PointEntity> list, Double longitude, Double latitude) {
        Path2D.Double path = create(list);
        return path.contains(new Point2D.Double(longitude.doubleValue(), latitude.doubleValue()));
    }

    private static Path2D.Double create(List<PointEntity> list) {
        List<Point2D> polygon = new ArrayList<>(list.size());
        list.forEach(pointEntity ->
                polygon.add(new Point2D.Double(pointEntity.getLongitude().doubleValue(), pointEntity.getLatitude().doubleValue())));
        Path2D.Double generalPath = new Path2D.Double();
        Point2D first = polygon.get(0);
        generalPath.moveTo(first.getX(), first.getY());
        polygon.remove(0);
        for (Point2D d : polygon) {
            generalPath.lineTo(d.getX(), d.getY());
        }
        generalPath.lineTo(first.getX(), first.getY());
        generalPath.closePath();
        return generalPath;
    }

    /**
     * 判断是否在范围内
     *
     * @param radius    圆的半径 单位m
     * @param centerLat 圆心纬度
     * @param centerLng 圆心经度
     * @param targetLat 目标点纬度
     * @param targetLng 目标点经度
     * @return
     */
    public static boolean isInCircle(double radius, double centerLat, double centerLng, double targetLat, double targetLng) {
        double R = 6378137.0;
        double dLat = (targetLat - centerLat) * Math.PI / 180;
        double dLng = (targetLng - centerLng) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(centerLat * Math.PI / 180) * Math.cos(targetLat * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        double dis = Math.round(d);
        if (dis <= radius) {  //点在圆内
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(locate("120.71", "28.02"));
    }
}
