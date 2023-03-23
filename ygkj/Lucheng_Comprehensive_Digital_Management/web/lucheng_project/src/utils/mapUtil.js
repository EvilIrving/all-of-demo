/*
 * @Author: wupengfei
 * @LastEditTime: 2022-04-28 20:14:13
 * @LastEditors: Please set LastEditors
 * @Descripttion:
 * @FilePath: \lucheng_project\src\utils\mapUtil.js
 */
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";
import Draw from "ol/interaction/Draw";
import { Feature, Overlay } from "ol";
import { Style, Fill, Stroke, Icon, Text, Circle as CircleStyle, } from "ol/style";
import { Point, Polygon, LineString, MultiPolygon } from "ol/geom";
import { Select } from "ol/interaction";
import { singleClick } from "ol/events/condition";

import { isEmpty } from "./utils";

// import $ from 'jquery';
// export function addVectorLayer(coordinates, styleObj) {

// }

/*
  desc:画点方法
  params:
    coordinates:[[]] //点经纬度数组
    styleObj:{
      anchor:[],
      anchorXUnits:"",//anchorX单位
      anchorYUnits:"",//anchorY单位
      src: "",//图片路径
      title:""//标题
    }
*/
// export function drawPoint(coordinates, styleObj) {
//     let vectorSource = new VectorSource({});
//     coordinates.forEach(element => {
//         const iconFeature = new Feature({
//             geometry: new Point(element)
//         });
//         const iconStyle = new Style({
//             image: new Icon({
//                 anchor: styleObj ? styleObj.anchor : [0.5, 46],
//                 anchorXUnits: styleObj ? styleObj.anchorXUnits : "fraction",
//                 anchorYUnits: styleObj ? styleObj.anchorYUnits : "pixels",
//                 src: styleObj ? styleObj.src : require("@/assets/images/map/seawall.png"),
//                 scale: styleObj ? styleObj.scale : 1,
//             })
//         });
//         iconFeature.setStyle(iconStyle);
//         vectorSource.addFeature(iconFeature);
//     });
//     let vectorLayer = new Vector({
//         title: styleObj ? styleObj.title : "默认信息",
//         source: vectorSource
//     });
//     return vectorLayer;
// }

export function drawPoint(coordinates, styleObj, showName, type) {
  let vectorSource = new VectorSource({});
  coordinates.forEach((element) => {
    let point = [element.lng, element.lat];
    let src = styleObj.src;
    const iconFeature = new Feature({
      geometry: new Point(point),
      data: element,
    });
    let style = {
      image: new Icon({
        anchor: styleObj.anchor || [0.5, 46],
        anchorXUnits: styleObj.anchorXUnits || "fraction",
        anchorYUnits: styleObj.anchorYUnits || "pixels",
        src: src,
        scale: styleObj.scale || 0.4,
      }),
    };
    if (showName && showName != "false") {
      style.text = new Text({
        textAlign: "center",
        textBaseline: "middle",
        font: "normal 12px 微软雅黑",
        text: element.stnm,
        fill: new Fill({
          color: "#333",
        }),
        offsetX: -18,
        offsetY: 46,
        scale: 1,
      });
    }
    const iconStyle = new Style(style);
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  });
  let vectorLayer = new Vector({
    title: styleObj.title,
    source: vectorSource,
  });
  return vectorLayer;
}

export function drawPointByType(coordinates) {
  let vectorSource = new VectorSource({});
  coordinates.forEach((element) => {
    const lat = element.lat || element.latitude
    const lng = element.lng || element.longitude
    let point = [lng, lat];
    const iconFeature = new Feature({
      geometry: new Point(point),
      data: element
    });
    let style = {
      image: new Icon({
        anchor: element.styleObj.anchor || [0.5, 0.96],
        anchorXUnits: element.styleObj.anchorXUnits || "fraction",
        anchorYUnits: element.styleObj.anchorYUnits || "pixels",
        src: element.styleObj.src,
        scale: element.styleObj.scale || 1,
      }),
    };

    if (element.styleObj.title && element.styleObj.title !== '') {
      style.text = new Text({
        textAlign: "center",
        textBaseline: "middle",
        font: "normal 12px 微软雅黑",
        text: element.title,
        fill: new Fill({
          color: "#333",
        }),
        offsetX: -18,
        offsetY: 46,
        scale: 1,
      });
    }
    const iconStyle = new Style(style);
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  });

  let vectorLayer = new Vector({

    title: '',

    source: vectorSource,

  });

  return vectorLayer;
}

/*
  desc:画线方法
  params:
    coordinates:[] //点经纬度数组
    styleObj:{
      fillColor:"rgba()",
      lineColor:"",
      lineWidth:2,
      title:""//标题
    }
*/
export function drawLine(coordinates, styleObj) {
  let vectorSource = new VectorSource();
  const vectorLayer = new Vector({
    source: vectorSource,
    // style: new Style({
    //   fill: new Fill({
    //     color: styleObj.fillColor || "rgba(255, 255, 255, 0.1)",
    //   }),
    //   stroke: new Stroke({
    //     color: styleObj.lineColor || "blue",
    //     width: styleObj.lineWidth || 2,
    //   }),
    // }),
  });
  const plygon = new LineString(coordinates);
  let feature = new Feature({
    geometry: plygon,
  });
  vectorSource.addFeature(feature);
  return vectorLayer;
}

/*
  desc:画多条线方法
  params:
    arr:[] 线条信息数组 
    arr.coordinates:[] //点经纬度数组
    arr.styleObj:{
      fillColor:"rgba()",
      lineColor:"",
      lineWidth:2,
      title:""//标题
    }
*/
export function drawLines(arr) {
  let vectorSource = new VectorSource({});
  arr.forEach((element) => {
    console.log(element);
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
    vectorSource.addFeature(lineFeature);
  });
  let vectorLayer = new Vector({
    source: vectorSource,
  });
  return vectorLayer;
}

/*
  desc:3857坐标系画线方法
  params:
    coordinates:[] //点经纬度数组
    styleObj:{
      fillColor:"rgba()",
      lineColor:"",
      lineWidth:2,
      title:""//标题
    }
*/
export function drawLineWith3857(coordinates, styleObj) {
  let vectorSource = new VectorSource();
  const vectorLayer = new Vector({
    source: vectorSource,
    style: new Style({
      fill: new Fill({
        color: styleObj.fillColor || "rgba(255, 255, 255, 0.1)",
      }),
      stroke: new Stroke({
        color: styleObj.lineColor,
        width: styleObj.lineWidth || 2,
      }),
    }),
  });
  const plygon = new LineString(coordinates);
  plygon.transform("EPSG:4326", "EPSG:3857");
  let feature = new Feature({
    geometry: plygon,
  });
  vectorSource.addFeature(feature);
  return vectorLayer;
}

/*
  desc:3857坐标系加载面方法
  params:
    coordinates:[] //点经纬度数组
    styleObj:{
      PolygonFillColor:"rgba()",
      PolygonLineColor:"",
      PolygonLineWidth:2,
      title:""//标题
    }
*/
export function drawPolygonWith3857(coordinates, styleObj, polygonType) {
  let vectorPolygonSource = new VectorSource();
  const vectorPolygonLayer = new Vector({
    source: vectorPolygonSource,
    style: new Style({
      fill: new Fill({
        color: styleObj.fillColor || "rgba(91,169,252, 0.6)",
      }),
      stroke: new Stroke({
        color: styleObj.lineColor || "blue",
        width: styleObj.lineWidth || 2,
      }),
    }),
  });
  let polygon;
  if (polygonType == "MultiPolygon") {
    polygon = new MultiPolygon(coordinates, styleObj.title);
    plygon.transform("EPSG:4326", "EPSG:3857");
  } else {
    polygon = new Polygon(coordinates, styleObj.title);
    plygon.transform("EPSG:4326", "EPSG:3857");
  }
  let feature = new Feature({
    geometry: polygon,
  });
  vectorPolygonSource.addFeature(feature);
  return vectorPolygonLayer;
}
/*
  desc:加载面方法
  params:
    coordinates:[] //点经纬度数组
    styleObj:{
      PolygonFillColor:"rgba()",
      PolygonLineColor:"",
      PolygonLineWidth:2,
      title:""//标题
    }
*/
export function drawPolygon(coordinates, styleObj, polygonType) {
  let vectorPolygonSource = new VectorSource();
  const vectorPolygonLayer = new Vector({
    source: vectorPolygonSource,
    // style: new Style({
    //   fill: new Fill({
    //     color: styleObj ? styleObj.fillColor : "rgba(91,169,252, 0.2)",
    //   }),
    //   stroke: new Stroke({
    //     color: styleObj ? styleObj.lineColor : "blue",
    //     width: styleObj ? styleObj.lineWidth : 2,
    //   }),
    // }),
  });
  let polygon;
  if (polygonType == "MultiPolygon") {
    polygon = new MultiPolygon(coordinates, styleObj ? styleObj.title : "");
  } else {
    polygon = new Polygon(coordinates, styleObj ? styleObj.title : "");
  }
  let feature = new Feature({
    geometry: polygon,
  });
  vectorPolygonSource.addFeature(feature);
  return vectorPolygonLayer;
}

export function drawGeoJson(data) {
  var vectorPolygonSource = new VectorSource({
    features: new GeoJSON().readFeatures(data)
  });
  const vectorPolygonLayer = new Vector({
    source: vectorPolygonSource,
    style: new Style({
      fill: new Fill({
        color: "rgba(91,169,252, 0.2)",
      }),
      stroke: new Stroke({
        color: "blue",
        width: 2,
      }),
    }),
  });
  return vectorPolygonLayer;
}

//画点线面 结束绘制 featureType 的值为： "None"
//画面时 featureType的值为 "Polygon"，
var draw;
export function drawFeature(mapObj, featureType) {
  const source = new VectorSource({ wrapX: false });
  const vector = new Vector({
    source: source,
  });
  mapObj.addLayer(vector);
  if (featureType !== "None") {
    mapObj.removeInteraction(draw);
    draw = new Draw({
      source: source,
      type: featureType,
    });
    draw.on("drawend", function () {
      mapObj.removeInteraction(draw);
    });
    mapObj.addInteraction(draw);
    const resultObj = {
      source,
      vector,
    };
    return resultObj;
  }
}

export function addLayerClick(clickLayer) {
  let selectClick = new Select({
    condition: singleClick,
    style: null,
    filter: (feature, layer) => {
      return layer === clickLayer; // 需要添加事件的layer
    },
  });
  return selectClick;
}

// //arcgis rest接口服务图层查询封装
// export function queryLayer(url, condition) {
//     // return axios.post(url, {
//     //     "where": condition,
//     //     "inSR": 4490,
//     //     "outFields": "*",
//     //     "outSR": 4490,
//     //     "f": "pjson"
//     // });
//     return $.ajax({
//         url: url,
//         type: "POST",
//         dataType: "json",
//         data: {
//             "where": condition,
//             // "where": "NAME='徐泾港'",
//             "inSR": 4490,
//             "outFields": "*",
//             "outSR": 4490,
//             "f": "pjson"
//         },
//         success: function(data) {
//
//         }
//     })
// }


