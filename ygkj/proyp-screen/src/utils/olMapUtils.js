import 'ol/ol.css';
import { Overlay, Feature } from 'ol';
import { Vector as VectorLayer, Group as LayerGroup } from 'ol/layer';
import { Vector as VectorSource, Cluster } from 'ol/source';
import { Point, Circle as GeoCircle, LineString, Polygon } from 'ol/geom';
import { Style, Fill, Stroke, Icon, Text } from 'ol/style';
import { getDistance, getArea } from 'ol/sphere';
import { boundingExtent } from 'ol/extent';
import { singleClick } from "ol/events/condition";
import { Select } from "ol/interaction";
import GeoJSON from 'ol/format/GeoJSON';
import * as turf from '@turf/turf';

const mapLayers = {};

/**
 * @description 地图打点
 * @param map 地图对象
 * @param pointsInfo 点位信息
 * @param layerName 图层名称
 */
function drawPoint (map, pointsInfo, layerName) {
  removeLayer(map, layerName)
  const vectorSource = new VectorSource({});
  pointsInfo.forEach((e) => {
    const point = [Number(e.longitude), Number(e.latitude)];
    let pointLayer = new Point(point);
    if (e.styleObj.geometricType && e.styleObj.geometricType === "circle") {
      pointLayer = new GeoCircle(point, (e.styleObj.radius || 3000) / map.getView().getProjection().getMetersPerUnit())
    }
    const iconFeature = new Feature({
      geometry: pointLayer,
      data: e || null,
      layerName
    });
    let style = {};
    if (e.styleObj.src && e.styleObj.src !== '') {
      style = {
        image: new Icon({
          anchor: e.styleObj.anchor || [0.5, 0.5],
          anchorXUnits: e.styleObj.anchorXUnits || 'fraction',
          anchorYUnits: e.styleObj.anchorYUnits || 'fraction',
          src: e.styleObj.src,
          scale: e.styleObj.scale || 1
        })
      };
    }
    if (e.styleObj.geometricType && e.styleObj.geometricType === "circle") {
      style = {
        fill: new Fill({
          color: e.styleObj.fillColor || e.styleObj.strokeColor || "red",
        }),
        stroke: new Stroke({
          color: e.styleObj.strokeColor || e.styleObj.fillColor || "red"
        })
      };
    }
    if (e.styleObj.title && e.styleObj.title !== '') {
      style.text = new Text({
        textAlign: 'center',
        textBaseline: 'middle',
        font: 'normal 12px 微软雅黑',
        text: e.title,
        fill: new Fill({
          color: '#333'
        }),
        offsetX: -18,
        offsetY: 46,
        scale: 1
      });
    }
    const iconStyle = new Style(style);
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  });
  const vectorLayer = new VectorLayer({
    source: vectorSource
  });
  mapLayers[layerName] = vectorLayer;
  map.addLayer(mapLayers[layerName]);
}


/**
 * @description 地图打点聚合
 */
function drawClusterPoint (map, pointsInfo, layerName) {
  removeLayer(map, layerName)
  mapLayers[layerName] = new VectorLayer({
    source: new Cluster({
      distance: pointsInfo?.styleObj?.distance || 100,
      source: new VectorSource({
        features: pointsInfo.filter(item => item.longitude && item.latitude).map(point => {
          return new Feature({
            geometry: new Point([Number(point.longitude), Number(point.latitude)]),
            data: point,
            layerName
          });
        })
      })
    }),
    style: feature => {
      const size = feature.get("features").length;
      const featureData = feature.getProperties().features[0].values_.data;
      let textOption = {
        text: ''
      };
      if (size > 1) {
        textOption = {
          font: featureData?.styleObj?.font || "500 14px 微软雅黑",
          text: `${size.toString()}`,
          textAlign: 'center',
          textBaseline: "middle",
          fill: new Fill({ color: featureData?.styleObj?.color || "#333" }),
          // backgroundFill: new Fill({ "transparent" }),
          offsetX: featureData?.styleObj?.textOffsetX || 6,
          offsetY: featureData?.styleObj?.textOffsetY || 2,
          scale: 1
        };
      }
      return new Style({
        image: new Icon({
          radius: 10,
          anchor: pointsInfo?.styleObj?.anchor || [0.5, 0.5],
          anchorXUnits: pointsInfo?.styleObj?.anchorXUnits || "fraction",
          anchorYUnits: pointsInfo?.styleObj?.anchorYUnits || "fraction",
          src: size == 1 ? featureData?.styleObj?.src || pointsInfo?.styleObj?.src || "" : featureData?.styleObj?.srcs || pointsInfo?.styleObj?.srcs || "",
          scale: pointsInfo?.styleObj?.scale || 0.4
        }),
        text: new Text(textOption)
      });
    }
  });
  map.addLayer(mapLayers[layerName]);
  const selectClick = new Select({
    condition: singleClick,
    style: null,
    filter: (feature, layer) => layer === mapLayers[layerName]

  });
  selectClick.on('select', (e) => {
    let features = e.target.getFeatures().getArray()[0].values_.features;
    if (features.length > 1) {
      const extent = boundingExtent(features.map((r) => r.getGeometry().getCoordinates()));
      map.getView().fit(extent, { duration: 1000, padding: [250, 550, 50, 550] });
    }
  });
  map.addInteraction(selectClick);
}

/**
 * @description 绘制雨量等值面
 * @param map 地图对象
 * @param geoJsonPogyLon 面数据
 * @param geoJsonPolyLine 线数据
 * @param transparency 等值面透明度
 * @param layerName 图层名称
 */
function drawIsosurface (map, geoJsonPogyLon, geoJsonPolyLine, transparency, layerName) {
  removeLayer(map, layerName);
  const SURFACE_FILL_COLOR = [
    `rgba(136, 250, 136, ${transparency})`,
    `rgba(0, 200, 37, ${transparency})`,
    `rgba(0, 195, 255, ${transparency})`,
    `rgba(5, 0, 255, ${transparency})`,
    `rgba(255, 0, 252, ${transparency})`,
    `rgba(147, 0, 80, ${transparency})`
  ];
  const inRange = (value) => {
    return [10, 25, 50, 100, 250, 100000].findIndex((item) => {
      return value < item;
    });
  };
  const geojson = new GeoJSON();
  const featuresPolygon = geojson.readFeatures(geoJsonPogyLon);
  const featuresPolyline = geoJsonPolyLine ? geojson.readFeatures(geoJsonPolyLine) : undefined;
  const source = new VectorSource({ features: featuresPolygon });
  featuresPolygon.forEach((feature) => {
    const { lvalue } = feature.getProperties();
    if (lvalue < 0) {
      source.removeFeature(feature);
    }
  });
  const polygonLayer = new VectorLayer({
    source,
    style: (f) => {
      const { lvalue } = f.getProperties();
      return new Style({
        fill: new Fill({ color: SURFACE_FILL_COLOR[inRange(lvalue)] })
      });
    },
    zIndex: 10
  });
  const polylineLayer = new VectorLayer({
    source: new VectorSource({ features: featuresPolyline }),
    style: (f) => {
      const { value } = f.getProperties();
      return new Style({
        stroke: new Stroke({
          color: 'red',
          width: 2
        }),
        text: new Text({
          text: `${value}`,
          font: '12px bold Calibri,sans-serif',
          fill: new Fill({ color: '#000' })
        })
      });
    },
    zIndex: 10
  });
  mapLayers[layerName] = new LayerGroup({
    layers: [polygonLayer, polylineLayer]
  });
  map.addLayer(mapLayers[layerName]);
}

/**
 * @description 画线方法
 * @param map 地图对象
 * @param coordinates 坐标数组
 * @param styleObj  线条样式
 * @param layerName 图层名称
 */
function drawLine (map, coordinates, styleObj, layerName) {
  removeLayer(map, layerName)
  const lineSource = new VectorSource({ wrapX: false });
  const feature = new Feature({
    geometry: new LineString(coordinates)
  });
  const lineStyle = new Style({
    stroke: new Stroke({
      color: styleObj.lineColor || "blue",
      width: styleObj.lineWidth || 2
    }),
  });
  feature.setStyle(lineStyle);
  lineSource.addFeature(feature);
  mapLayers[layerName] = new VectorLayer({
    source: lineSource
  });
  map.addLayer(mapLayers[layerName]);
}

/**
 * @description 画多条线方法
 * @param map 地图对象
 * @param arr 坐标数组
 * @param layerName 图层名称
 */
function drawLines (map, arr, layerName) {
  removeLayer(map, layerName)
  const LinesSource = new VectorSource({});
  arr.forEach((element) => {
    const lineFeature = new Feature({
      geometry: new LineString(element.coordinates),
    });
    const lineStyle = new Style({
      fill: new Fill({
        color: element.fillColor || "rgba(255, 255, 255, 0.1)",
      }),
      stroke: new Stroke({
        color: element.lineColor || "blue",
        width: element.lineWidth || 3,
      }),
      text: new Text({
        text: element.text || "", // 路线标签文字
        font: "normal 12px 微软雅黑", //字体样式
        fill: new Fill({
          //文字填充样式
          color: [16, 168, 218, 1],
        }),
        backgroundFill: new Fill({
          color: [255, 255, 255, 0.9],
        }),
        padding: [1, 2, 1, 2],
      }),
    });
    lineFeature.setStyle(lineStyle);
    LinesSource.addFeature(lineFeature);
  });
  mapLayers[layerName] = new VectorLayer({
    source: LinesSource
  })
  map.addLayer(mapLayers[layerName])
  // return vectorLayer;
}

/**
 * @description 两点之间飞线
 * @param map 地图对象
 * @param start 起点经纬度坐标
 * @param styleObj 终点经纬度坐标
 * @param layerName 图层名称
 */
function flyLine (map, start, end, layerName) {
  const flightSource = new VectorSource()
  const turfFormat = new GeoJSON();
  const line = turf.lineString([start, [start[0] + (end[0] - start[0]) * 0.5, start[1] + (end[1] - start[1]) * 1], end])
  const curved = turf.bezierSpline(line)
  const bezierFeature = turfFormat.readFeature(curved)
  const arcStyle = new Style({
    stroke: new Stroke({
      color: [255, 214, 8, 1],
      width: 2
    })
  })
  bezierFeature.setStyle(arcStyle)
  flightSource.addFeature(bezierFeature)
  mapLayers[layerName] = new VectorLayer({
    source: flightSource
  })
  map.addLayer(mapLayers[layerName])
}

/**
 * @description 画面方法
 * @param map 地图对象
 * @param coordinates 坐标数组
 * @param styleObj 多边形样式
 * @param layerName 图层名称
 */
function drawPoly (map, coordinates, styleObj, layerName) {
  removeLayer(map, layerName)
  const polySource = new VectorSource({})
  const firstCoordinate = coordinates[0]
  const coordinates_ = coordinates.concat([firstCoordinate])
  const feature = new Feature({
    geometry: new Polygon([coordinates_])
  })
  const polyStyle = new Style({
    fill: new Fill({
      color: styleObj.fillColor || [255, 255, 255, 0.33],
    }),
    stroke: new Stroke({
      color: styleObj.lineColor || "blue",
      width: styleObj.lineWidth || 2,
      lineDash: styleObj.lineDash
    }),
  })
  feature.setStyle(polyStyle)
  polySource.addFeature(feature)
  mapLayers[layerName] = new VectorLayer({
    source: polySource
  })
  map.addLayer(mapLayers[layerName])
}

/**
 * @description 移除地图图层
 * @param map 地图对象
 * @param layerName 指定移除图层的名称，不传时移除所有图层
 */
function removeLayer (map, layerName) {
  if (layerName) {
    if (mapLayers[layerName] != null) {
      map.removeLayer(mapLayers[layerName]);
      mapLayers[layerName] = null;
    }
  } else {
    const layerNames = Object.keys(mapLayers);
    layerNames.forEach((e) => {
      if (mapLayers[e] != null) {
        map.removeLayer(mapLayers[e]);
        mapLayers[e] = null;
      }
    });
  }

}

/**
 * @description 添加悬浮窗-位置相对落点
 * @param map 地图对象
 * @param pointInfo 点位信息
 * @param ele DOM元素
 * @param iconHeight 落点图标高度
 * @param hoverName 图层名称
 */
function drawHoverFrame (map, pointInfo, ele, iconHeight, hoverName) {
  const scale = pointInfo.styleObj.scale || 1;
  const anchorYValue = pointInfo.styleObj.anchor ? pointInfo.styleObj.anchor[1] : 0.5;
  const anchorYUnits = pointInfo.styleObj.anchorYUnits || 'fraction';
  mapLayers[hoverName] = new Overlay({
    element: ele,
    offset: [0, anchorYUnits === 'fraction' ? -iconHeight * scale * anchorYValue : anchorYValue - iconHeight * scale],
    position: [Number(pointInfo.longitude), Number(pointInfo.latitude)],
    positioning: "bottom-center",
    autoPan: false,
    autoPanAnimation: { duration: 250 },
  });
  map.addOverlay(mapLayers[hoverName])
}

/**
 * @description 添加悬浮窗-位置相对鼠标
 * @param {*} map 地图对象
 * @param {*} coordinate 经纬度坐标
 * @param {*} ele DOM元素
 * @param {*} hoverName 图层名称
 */
function drawHoverFrame_followMouse (map, coordinate, ele, hoverName, positioning, offset) {
  mapLayers[hoverName] = new Overlay({
    element: ele,
    offset: offset || [0, 0],
    position: coordinate,
    positioning: positioning || "bottom-center",
    autoPan: false,
    autoPanAnimation: { duration: 250 },
  });
  map.addOverlay(mapLayers[hoverName]);
}

/**
 * @description 移除悬浮窗
 * @param map 地图对象
 * @param hoverName 图层名称
 */
function removeHoverFrame (map, hoverName) {
  map.removeOverlay(mapLayers[hoverName])
}

/**
 * @description 绘制GeoJson数据
 * @param geoJson 数据源
 * @param styleObj 自定义样式
 */
function drawGeoJson (map, geoJson, styleObj, layerName) {
  removeLayer(map, layerName)
  const vectorPolygonSource = new VectorSource({
    features: new GeoJSON().readFeatures(geoJson),
  });
  mapLayers[layerName] = new VectorLayer({
    source: vectorPolygonSource,
    style: (feature) =>
      new Style({
        fill: new Fill({
          color: styleObj ? styleObj.fillColor : "transparent",
        }),
        stroke: new Stroke({
          color: styleObj ? styleObj.lineColor : "#00D5FF",
          width: styleObj ? styleObj.lineWidth : 1,
          lineDash: styleObj ? styleObj.lineDash : '',
        }),
        text: new Text({
          text: feature.values_['市'],
          font: "normal 14px 微软雅黑",
          fill: new Fill({
            color: styleObj ? styleObj.textColor : [146, 199, 208, 1],
          }),
          padding: [1, 2, 1, 2],
        }),
      }),
  });
  map.addLayer(mapLayers[layerName])
}

/**
 * @description 绘制地图边界
 * @param map 地图对象
 * @param geoJson 数据源
 */
function drawMapBoundary (map, geoJson) {
  const vectorPolygonSource = new VectorSource({
    features: new GeoJSON().readFeatures(geoJson),
  });
  mapLayers['mapBoundary_layer'] = new LayerGroup({
    layers: [
      new VectorLayer({
        source: vectorPolygonSource,
        style: new Style({
          fill: new Fill({
            color: "transparent",
          }),
          stroke: new Stroke({
            color: "rgba(0, 73, 255, 0.2)",
            width: 7,
          })
        }),
      }),
      new VectorLayer({
        source: vectorPolygonSource,
        style: new Style({
          fill: new Fill({
            color: "transparent",
          }),
          stroke: new Stroke({
            color: "rgba(0, 126, 255, 0.4)",
            width: 3,
          })
        }),
      }),
      new VectorLayer({
        source: vectorPolygonSource,
        style: new Style({
          fill: new Fill({
            color: "transparent",
          }),
          stroke: new Stroke({
            color: "rgba(0, 213, 255, 1)",
            width: 2,
            lineDash: [4, 8, 0, 8]
          })
        }),
      })
    ]
  });
  map.addLayer(mapLayers['mapBoundary_layer'])
}

/**
 * @description 绘制行政边界
 * @param map 地图对象
 * @param geoJson 数据源
 */
function drawAdmnBoundary (map, geoJson) {
  mapLayers['admnBoundary_layer'] = new VectorLayer({
    source: new VectorSource({
      features: new GeoJSON().readFeatures(geoJson),
    }),
    style: (feature) => {
      feature.values_.admnLayerName = "admnBoundary_layer"
      return new Style({
        fill: new Fill({
          color: "transparent",
        }),
        stroke: new Stroke({
          color: "#00BAFF",
          width: 1,
        }),
        text: new Text({
          text: feature.values_['市'],
          font: "normal 14px 微软雅黑",
          fill: new Fill({
            color: "#94E8FF",
          }),
          padding: [1, 2, 1, 2],
        }),
      })
    }
  });
  mapLayers['admnBoundary_layer'].on('prerender', evt => {
    evt.context.shadowBlur = 1
    evt.context.shadowOffsetY = 2
    evt.context.shadowColor = 'rgba(0, 4, 114, 1)'
  })
  mapLayers['admnBoundary_layer'].on('postrender', evt => {
    evt.context.shadowBlur = 0
    evt.context.shadowOffsetY = 0
    evt.context.shadowColor = 'rgba(0, 4, 114, 1)'
  })
  map.addLayer(mapLayers['admnBoundary_layer'])
}

/**
 * @description 改变单个行政区域的样式
 * @param feature 区域要素
 * @param styleObj 指定样式，不传时为恢复默认样式
 */
function changeAdmnStyle (feature, styleObj) {
  if (feature) {
    feature.setStyle(
      new Style({
        fill: new Fill({
          color: styleObj && styleObj.fillColor ? styleObj.fillColor : "transparent",
        }),
        stroke: new Stroke({
          color: styleObj && styleObj.strokeColor ? styleObj.strokeColor : "#00BAFF",
          width: styleObj && styleObj.strokeWidth ? styleObj.strokeWidth : 1,
        }),
        text: new Text({
          text: feature.values_['市'],
          font: "normal 14px 微软雅黑",
          fill: new Fill({
            color: styleObj && styleObj.textColor ? styleObj.textColor : "#94E8FF",
          }),
          padding: [1, 2, 1, 2],
        }),
      })
    );
  }
}

/**
 * @description 绘制地图暗色图层
 * @param map 地图对象
 * @param geoJson 数据源
 * @param color 颜色
 */
function drawDarkMask (map, geoJson, color) {
  mapLayers['darkMask_layer'] = new VectorLayer({
    source: new VectorSource({
      features: new GeoJSON().readFeatures(geoJson),
    }),
    style: new Style({
      fill: new Fill({
        color: color ? color : [0, 29, 105, 0.6],
      }),
      stroke: new Stroke({
        color: "transparent",
        width: 0,
      }),
    }),
  });
  map.addLayer(mapLayers['darkMask_layer'])
}

/**
 * @description 改变地图鼠标图标
 * @param map 地图对象
 * @param icon cursor的属性值或引入的图标，为空时恢复默认图标
 */
function changeMapPointCursor (map, icon) {
  const attributes = ['default', 'auto', 'crosshair', 'pointer', 'move', 'e-resize', 'ne-resize', 'nw-resize', 'n-resize', 'se-resize', 'sw-resize', 's-resize', 'w-resize', 'text', 'wait', 'help']
  map.on("pointermove", (e) => {
    if (icon) {
      map.getTargetElement().style.cursor = attributes.includes(icon) ? icon : "url(" + icon + "), default";
    } else {
      const pixel = map.getEventPixel(e.originalEvent);
      if (map.hasFeatureAtPixel(pixel)) {
        map.getTargetElement().style.cursor = "pointer";
      } else {
        map.getTargetElement().style.cursor = "auto";
      }
    }
  });
}

/**
 * @description 定位到对应位置和层级
 * @param map 地图对象
 * @param center 中心点
 * @param zoom 地图层级
 */
function flyPoint (map, center, zoom) {
  const view = map.getView();
  view.animate({
    center: center,
    duration: 300,
    zoom: zoom
  });
}

/**
 * @description 根据给定边界进行视口的改变
 * @param map 地图对象
 * @param polygon geometry对象
 * @param padding 视口内边距填充
 */
function changeViewByPolygon (map, polygon, padding) {
  const view = map.getView()
  view.fit(polygon, {
    padding: padding || [0, 0, 0, 0],
    duration: 300
  })
}

/**
 * @description 计算多个经纬度坐标之间连线的总距离
 * @param {array} linePoints 连线点位的集合
 */
function computedDistance (linePoints) {
  if (linePoints.length <= 1) {
    return '0'
  } else {
    let sumLength = 0
    for (let i = 1; i < linePoints.length; i++) {
      sumLength += getDistance(linePoints[i - 1], linePoints[i])
    }
    return sumLength < 1000 ? sumLength.toFixed(2) + '米' : (sumLength / 1000).toFixed(2) + '公里'
  }
}

/**
 * @description 计算多个经纬度坐标形成的多边形总面积
 * @param {array} polyPoints 多边形点位的集合
 */
function computedArea (polyPoints) {
  if (polyPoints.length <= 2) {
    return '0'
  } else {
    const firstCoordinate = polyPoints[0]
    const coordinates_ = polyPoints.concat([firstCoordinate])
    const polygon = new Polygon([coordinates_])
    const sumArea = getArea(polygon, {
      projection: 'EPSG:4326'
    })
    if (sumArea > 10000) {
      return Math.round((sumArea / 1000000) * 100) / 100 + '平方千米';
    } else {
      return Math.round(sumArea * 100) / 100 + '平方米';
    }
  }
}

export default {
  drawPoint,
  drawClusterPoint,
  drawIsosurface,
  drawLine,
  drawLines,
  flyLine,
  drawPoly,
  removeLayer,
  drawHoverFrame,
  drawHoverFrame_followMouse,
  removeHoverFrame,
  drawGeoJson,
  drawMapBoundary,
  drawAdmnBoundary,
  changeAdmnStyle,
  drawDarkMask,
  changeMapPointCursor,
  flyPoint,
  changeViewByPolygon,
  computedDistance,
  computedArea
}