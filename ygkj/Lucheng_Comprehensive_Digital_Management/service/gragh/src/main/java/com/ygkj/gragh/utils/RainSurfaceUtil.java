package com.ygkj.gragh.utils;

import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.Geometry;
import com.ygkj.gragh.vo.response.RainSurfaceResVo;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import wContour.Contour;
import wContour.Global.Border;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Global.Polygon;
import wContour.Interpolate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huang.zh
 * @version 1.0
 * @description: TODO
 * @date 2022/3/24 9:56
 */
public class RainSurfaceUtil {

    /**
     * 生成等值面
     *
     * @param trainData    训练数据
     * @param dataInterval 数据间隔
     * @param size         大小，宽，高
     * @param fileUrl  四至
     * @param isclip       是否裁剪
     * @return
     */
    public static RainSurfaceResVo calEquiSurface(double[][] trainData,
                                                  double[] dataInterval,
                                                  int[] size,
                                                  URL fileUrl,
                                                  boolean isclip) {
        String geojsonpogylon = StringUtils.EMPTY;
        String geojsonpolyline = StringUtils.EMPTY;
        try {
            double _undefData = -9999.0;
            SimpleFeatureCollection polygonCollection = null;
            SimpleFeatureCollection polylineCollection = null;
            List<PolyLine> cPolylineList = new ArrayList<PolyLine>();
            List<Polygon> cPolygonList = new ArrayList<Polygon>();

            int width = size[0],
                    height = size[1];
            double[] _X = new double[width];
            double[] _Y = new double[height];

            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(fileUrl);
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection fc = featureSource.getFeatures();

            double minX = fc.getBounds().getMinX();
            double minY = fc.getBounds().getMinY();
            double maxX = fc.getBounds().getMaxX();
            double maxY = fc.getBounds().getMaxY();


            Interpolate.createGridXY_Num(minX, minY, maxX, maxY, _X, _Y);
            double[][] _gridData = new double[width][height];

            int nc = dataInterval.length;

            _gridData = Interpolate.interpolation_IDW_Neighbor(trainData,
                    _X, _Y, 12, _undefData);// IDW插值


            int[][] S1 = new int[_gridData.length][_gridData[0].length];
            List<Border> _borders = Contour.tracingBorders(_gridData, _X, _Y,
                    S1, _undefData);

            cPolylineList = Contour.tracingContourLines(_gridData, _X, _Y, nc,
                    dataInterval, _undefData, _borders, S1);// 生成等值线

            cPolylineList = Contour.smoothLines(cPolylineList);// 平滑

            cPolylineList = deleteShortLines(cPolylineList,0.1);// 过滤短线

            cPolygonList = Contour.tracingPolygons(_gridData, cPolylineList,
                    _borders, dataInterval);

            geojsonpogylon = getPolygonGeoJson(cPolygonList);

            geojsonpolyline = getPolylineGeoJson(cPolylineList);

            if (isclip) {
                polygonCollection = GeoJSONUtil.readGeoJsonByString(geojsonpogylon);
                FeatureSource dc = clipFeatureCollection(fc, polygonCollection);
                geojsonpogylon = getPolygonGeoJson(dc.getFeatures());

                polylineCollection= GeoJSONUtil.readGeoJsonByString(geojsonpolyline);
                FeatureSource dl = clipPolylineCollection(fc, polylineCollection);
                geojsonpolyline = getPolylineGeoJson(dl.getFeatures());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject object = JSONObject.parseObject(geojsonpogylon);
        for (Object o : object.getJSONArray("features")) {
            JSONObject feature = (JSONObject) o;
            feature.put("area",getAreaByGeoJson(feature.toJSONString()));
        }
        geojsonpogylon = object.toJSONString();
        return new RainSurfaceResVo(geojsonpogylon,geojsonpolyline);
    }

    public static double[] transferInterval(String dataInterval){
        String[] strings = dataInterval.split(",");
        double[] result = new double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Double.parseDouble(strings[i]);
        }
        return result;
    }

    private static FeatureSource clipFeatureCollection(FeatureCollection fc,
                                                       SimpleFeatureCollection gs) {
        FeatureSource cs = null;
        try {
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            FeatureIterator contourFeatureIterator = gs.features();
            FeatureIterator dataFeatureIterator = fc.features();
            while (dataFeatureIterator.hasNext()) {
                Feature dataFeature = dataFeatureIterator.next();
                Geometry dataGeometry = (Geometry) dataFeature.getProperty(
                        "the_geom").getValue();
                while (contourFeatureIterator.hasNext()) {
                    Feature contourFeature = contourFeatureIterator.next();
                    Geometry contourGeometry = (Geometry) contourFeature
                            .getProperty("geometry").getValue();
                    double lv = (Double) contourFeature.getProperty("lvalue")
                            .getValue();
                    double hv = (Double) contourFeature.getProperty("hvalue")
                            .getValue();
                    if (dataGeometry.intersects(contourGeometry)) {
                        Geometry geo = dataGeometry
                                .intersection(contourGeometry);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("the_geom", geo);
                        map.put("lvalue", lv);
                        map.put("hvalue", hv);
                        values.add(map);
                    }

                }

            }

            contourFeatureIterator.close();
            dataFeatureIterator.close();

            SimpleFeatureCollection sc = FeaureUtil
                    .creatSimpleFeatureByFeilds(
                            "polygons",
                            "crs:4326,the_geom:MultiPolygon,lvalue:double,hvalue:double",
                            values);
            cs = FeaureUtil.creatFeatureSourceByCollection(sc);

        } catch (Exception e) {
            e.printStackTrace();
            return cs;
        }

        return cs;
    }

    private static String getPolygonGeoJson(FeatureCollection fc) {
        FeatureJSON fjson = new FeatureJSON();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
            FeatureIterator itertor = fc.features();
            List<String> list = new ArrayList<String>();
            while (itertor.hasNext()) {
                SimpleFeature feature = (SimpleFeature) itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                list.add(writer.toString());
            }
            itertor.close();
            sb.append(list.toString());
            sb.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String getPolygonGeoJson(List<Polygon> cPolygonList) {
        String geo = null;
        String geometry = " { \"type\":\"Feature\",\"geometry\":";
        String properties = ",\"properties\":{ \"hvalue\":";

        String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
        String end = "  ] }";
        if (cPolygonList == null || cPolygonList.size() == 0) {
            return null;
        }
        try {
            for (Polygon pPolygon : cPolygonList) {

                List<Object> ptsTotal = new ArrayList<Object>();
                List<Object> pts = new ArrayList<Object>();

                PolyLine pline = pPolygon.OutLine;

                for (PointD ptD : pline.PointList) {
                    List<Double> pt = new ArrayList<Double>();
                    pt.add(ptD.X);
                    pt.add(ptD.Y);
                    pts.add(pt);
                }

                ptsTotal.add(pts);

                if (pPolygon.HasHoles()) {
                    for (PolyLine cptLine : pPolygon.HoleLines) {
                        List<Object> cpts = new ArrayList<Object>();
                        for (PointD ccptD : cptLine.PointList) {
                            List<Double> pt = new ArrayList<Double>();
                            pt.add(ccptD.X);
                            pt.add(ccptD.Y);
                            cpts.add(pt);
                        }
                        if (cpts.size() > 0) {
                            ptsTotal.add(cpts);
                        }
                    }
                }

                JSONObject js = new JSONObject();
                js.put("type", "Polygon");
                js.put("coordinates", ptsTotal);
                double hv = pPolygon.HighValue;
                double lv = pPolygon.LowValue;

                if (hv == lv) {
                    if (pPolygon.IsClockWise) {
                        if (!pPolygon.IsHighCenter) {
                                                hv = hv - 0.1;
                                                lv = lv - 0.1;
                        }

                    } else {
                        if (!pPolygon.IsHighCenter) {
                                               hv = hv - 0.1;
                                               lv = lv - 0.1;
                        }
                    }
                } else {
                    if (!pPolygon.IsClockWise) {
                                        lv = lv + 0.1;
                    } else {
                        if (pPolygon.IsHighCenter) {
                                                hv = hv - 0.1;
                        }
                    }

                }

                geo = geometry + js.toString() + properties + hv
                        + ", \"lvalue\":" + lv + "} }" + "," + geo;

            }
            if (geo.contains(",")) {
                geo = geo.substring(0, geo.lastIndexOf(","));
            }

            geo = head + geo + end;
        } catch (Exception e) {
            e.printStackTrace();
            return geo;
        }
        return geo;
    }

    private static String getPolylineGeoJson(List<PolyLine> cPolylineList) {
        String geo=null;
        String geometry=" { \"type\":\"Feature\",\"geometry\":";
        String properties = ",\"properties\":{ \"hvalue\":";
        String head = "{\"type\": \"FeatureCollection\"," + "\"features\": [";
        String end = "  ] }";
        if (cPolylineList == null || cPolylineList.size() == 0) {
            return null;
        }
        try {
            for (PolyLine pPolyline : cPolylineList) {

                List<Object> ptsTotal = new ArrayList<Object>();
                List<Object> pts = new ArrayList<Object>();

                for (PointD ptD : pPolyline.PointList) {
                    List<Double> pt = new ArrayList<Double>();
                    pt.add(ptD.X);
                    pt.add(ptD.Y);
                    pts.add(pt);
                }

                ptsTotal.add(pts);


                JSONObject js = new JSONObject();
                js.put("type", "MultiLineString");
                js.put("coordinates", ptsTotal);
                double hv = pPolyline.Value;


                geo = geometry + js.toString() + properties + hv
                        + ", \"value\":" + hv + "} }" + "," + geo;

            }
            if (geo.contains(",")) {
                geo = geo.substring(0, geo.lastIndexOf(","));
            }

            geo = head + geo + end;
        } catch (Exception e) {
            e.printStackTrace();
            return geo;
        }
        return geo;

    }


    private static FeatureSource clipPolylineCollection(FeatureCollection fc,
                                                 SimpleFeatureCollection gs) {
        FeatureSource cs = null;
        try {
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            FeatureIterator contourFeatureIterator = gs.features();
            FeatureIterator dataFeatureIterator = fc.features();
            while (dataFeatureIterator.hasNext()) {
                Feature dataFeature = dataFeatureIterator.next();
                Geometry dataGeometry = (Geometry) dataFeature.getProperty(
                        "the_geom").getValue();
                while (contourFeatureIterator.hasNext()) {
                    Feature contourFeature = contourFeatureIterator.next();
                    Geometry contourGeometry = (Geometry) contourFeature
                            .getProperty("geometry").getValue();
                    double lv = (Double) contourFeature.getProperty("value")
                            .getValue();
                    if (dataGeometry.intersects(contourGeometry)) {
                        Geometry geo = dataGeometry

                                .intersection(contourGeometry);
                        if(geo.getGeometryType()=="MultiLineString")
                        {
                            for(int MultiLineIndex=0;MultiLineIndex<geo.getNumGeometries();MultiLineIndex++)
                            {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("the_geom", geo.getGeometryN(MultiLineIndex));
                                map.put("value", lv);
                                values.add(map);
                            }
                        }
                        else
                        {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("the_geom", geo);
                            map.put("value", lv);
                            values.add(map);
                        }

                    }

                }

            }

            contourFeatureIterator.close();
            dataFeatureIterator.close();



            SimpleFeatureCollection sc = FeaureUtil
                    .creatSimpleFeatureByFeilds(
                            "polylines",
                            "crs:4326,the_geom:LineString,value:double",
                            values);
            cs = FeaureUtil.creatFeatureSourceByCollection(sc);

        } catch (Exception e) {
            e.printStackTrace();
            return cs;
        }

        return cs;
    }


    private static double getPolylineLength(PolyLine pl)
    {
        PointD startPt=pl.PointList.get(0);
        double totalDistance=0.0;
        try {
            for(int ptindex=0;ptindex<pl.PointList.size();ptindex++)
            {
                PointD curPt=pl.PointList.get(ptindex);
                double curDistance=Math.pow(Math.pow(curPt.X-startPt.X,2)+Math.pow(curPt.Y-startPt.Y,2),0.5);
                totalDistance=totalDistance+curDistance;
                startPt=curPt;
            }
        } catch (Exception e) {
            return -1.0;
        }
        return totalDistance;
    }


    private static List<PolyLine> deleteShortLines(List<PolyLine> aLineList,double limitL) {
        List<PolyLine> resultLine= new ArrayList<PolyLine>();
        try {
            for(PolyLine curPl : aLineList)
            {
                double lineLength=getPolylineLength(curPl);
                if (lineLength<=limitL&&lineLength>=0.0)
                {
                    //如果是很短的就不放入结果
                }
                else
                {
                    resultLine.add(curPl);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return resultLine;
    }

    private static String getPolylineGeoJson(FeatureCollection fc) {
        FeatureJSON fjson = new FeatureJSON();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");
            FeatureIterator itertor = fc.features();
            List<String> list = new ArrayList<String>();
            while (itertor.hasNext()) {
                SimpleFeature feature = (SimpleFeature) itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                list.add(writer.toString());
            }
            itertor.close();
            sb.append(list.toString());
            sb.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAreaByGeoJson(String geojson) {
        String area = StringUtils.EMPTY;
        try {
            GeometryJSON gjson = new GeometryJSON(15);
            Reader reader = new StringReader(geojson);
            Geometry geom = gjson.read(reader);
            geom = lonlat2WebMactor(geom);
            if(geom != null) {
                area = new BigDecimal(geom.getArea())
                        .divide(new BigDecimal(Math.pow(10,6)))
                        .setScale(BigDecimal.ROUND_DOWN,2)
                        .toPlainString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return area;
    }

    private static Geometry lonlat2WebMactor(Geometry geom) {
        Geometry res = null;
        try {
            CoordinateReferenceSystem crsTarget = CRS.decode("EPSG:3857");
            // 投影转换
            MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crsTarget);
            res = JTS.transform(geom, transform);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
