/*
 * @Author: wupengfei
 * @LastEditTime: 2021-10-12 09:42:53
 * @LastEditors: Please set LastEditors
 * @Descripttion:
 * @FilePath: \big_screen\src\utils\mapUtil.js
 */
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Draw from "ol/interaction/Draw";
import { Feature, Overlay } from "ol";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import { Point, Polygon, LineString, MultiPolygon } from "ol/geom";
import { Select } from "ol/interaction";
import { singleClick } from "ol/events/condition";

import { isEmpty } from "./utils";

// import $ from 'jquery';
// export function addVectorLayer(coordinates, styleObj) {

// }
/*
  desc:画单个点方法
  params:
    coordinates:[ //点经纬度数组
    styleObj:{
      anchor:[],
      anchorXUnits:"",//anchorX单位
      anchorYUnits:"",//anchorY单位
      src: "",//图片路径
      title:""//标题
    }
*/
export function drawOnePoint(coordinates, styleObj) {
  let vectorSource = new VectorSource({});
  const iconFeature = new Feature({
    geometry: new Point(coordinates),
  });
  const iconStyle = new Style({
    image: new Icon({
      anchor: styleObj ? styleObj.anchor : [0.5, 46],
      anchorXUnits: styleObj ? styleObj.anchorXUnits : "fraction",
      anchorYUnits: styleObj ? styleObj.anchorYUnits : "pixels",
      // src: styleObj ? styleObj.src : require("@/assets/images/map/seawall.png"),
      scale: styleObj ? styleObj.scale : 1,
    }),
  });
  iconFeature.setStyle(iconStyle);
  vectorSource.addFeature(iconFeature);
  let vectorLayer = new Vector({
    title: styleObj ? styleObj.title : "默认信息",
    source: vectorSource,
  });
  return vectorLayer;
}
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
    // if (showName && showName != "false" && type != 'reservoir') {
    //   switch (element.colorType) {
    //     case "blue":
    //       src = require("@/assets/images/map/seawall.png");
    //       break;
    //     case "yellow":
    //       src = require("@/assets/images/map/seawall-yellow.png");
    //       break;
    //     case "orange":
    //       src = require("@/assets/images/map/seawall-orange.png");
    //       break;
    //     case "red":
    //       src = require("@/assets/images/map/seawall-red.png");
    //       break;
    //   }
    // }
    // if(type == 'reservoir'){
    //   switch (element.colorType) {
    //     case "green":
    //       src = require("@/assets/images/map/reservoir-green4.png");
    //       break;
    //     case "blue":
    //       src = require("@/assets/images/map/reservoir-blue4.png");
    //       break;
    //     case "yellow":
    //       src = require("@/assets/images/map/reservoir-yellow4.png");
    //       break;
    //     case "orange":
    //       src = require("@/assets/images/map/reservoir-orange4.png");
    //       break;
    //     case "red":
    //       src = require("@/assets/images/map/reservoir-red4.png");
    //       break;
    //   }
    // }
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
    style: new Style({
      fill: new Fill({
        color: styleObj.fillColor || "rgba(255, 255, 255, 0.1)",
      }),
      stroke: new Stroke({
        color: styleObj.lineColor || "blue",
        width: styleObj.lineWidth || 2,
      }),
    }),
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
    style: new Style({
      fill: new Fill({
        color: styleObj ? styleObj.fillColor : "rgba(91,169,252, 0.2)",
      }),
      stroke: new Stroke({
        color: styleObj ? styleObj.lineColor : "blue",
        width: styleObj ? styleObj.lineWidth : 2,
      }),
    }),
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
    draw.on("drawend", function() {
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

export function newTyphoon(map, typhoonData, typhoonLayerMap, vueComponent) {
  this.map = map;
  this.typhoonData = typhoonData;
  this.typhoonLayers = typhoonLayerMap; //台风图层
  this.typhoonPlayIndex = {}; //台风当前选中路径点位
  this.nameOverlays = {}; //台风名称图层
  this.forecastFeatures = {}; //预报暂存容器
  //预测线颜色对象
  this.forcColorDict = {
    中国: "#ec5d72",
    中国香港: "#ec7cfe",
    中国台湾: "#ecaa65",
    日本: "#56f66e",
    美国: "#53dbfe",
    韩国: "#72a4ac",
    欧洲: "#4c54a6",
  };

  this.init = function() {
    // 添加警戒线
    // this.addWarnLine();
    // 添加地图事件
    // this.addMapEvent();
    // 展示台风数据
    this.showTyphoon(this.typhoonData);

    let imgElement = document.createElement("img");
    let picOverlay = this.map.getOverlayById("overlayWind"+typhoonData.code);
    if (picOverlay) {
      this.map.removeOverlay(picOverlay);
    }
    let lyr = new Overlay({
      id: "overlayWind"+typhoonData.code,
      position: [0, 0], //默认空
      positioning: "center-bottom",
      element: imgElement, //绑定上面添加的元素
      //stopEvent: false,
      offset: [-20, -20], //图片偏移量
    });
    this.map.addOverlay(lyr);
    // imgElement.src = require("../assets/images/typhoon.gif");
    lyr.setPosition([this.typhoonData.points[this.typhoonData.points.length-1].lng, this.typhoonData.points[this.typhoonData.points.length-1].lat]);
  };

  // 添加警戒线
  this.addWarnLine = function() {
    // 警戒线样式设置
    const warnLineStyle = function(feat) {
      const attr = feat.get("attr");
      return new Style({
        stroke: new Stroke({
          color: attr.color,
          width: attr.weight,
          lineDash: attr.dashArray,
        }),
      });
    };

    //警戒线数据
    const warnLineData = [
      {
        color: "blue",
        weight: 1,
        opacity: 0.8,
        dashArray: [0, 0],
        points: [
          [105, 0],
          [113, 4.5],
          [119, 11],
          [119, 18],
          [127, 22],
          [127, 34],
        ],
      },
      {
        color: "green",
        weight: 1,
        opacity: 0.8,
        dashArray: [10, 5],
        points: [
          [105, 0],
          [120, 0],
          [132, 15],
          [132, 34],
        ],
      },
    ];

    const features = [];
    for (var i = 0; i < warnLineData.length; i++) {
      const d = warnLineData[i];
      const geometry = new LineString(d.points);
      geometry.transform("EPSG:4326", this.map.getView().getProjection());
      const feature = new Feature({
        geometry: geometry,
        attr: d,
      });
      features.push(feature);
    }

    const source = new VectorSource({
      features: features,
    });

    const warnLines = new Vector({
      source: source,
      style: warnLineStyle,
      opacity: 0.8,
      zIndex: 100,
    });
    warnLines.set("name", "warnLines");
    this.map.addLayer(warnLines);
  };

  // 添加地图事件
  this.addMapEvent = function() {
    //鼠标移动事件
    this.map.on("pointermove", (evt) => {
      // const pixel = evt.pixel;
      // const dom = this.map.getTargetElement();
      // if (this.map.hasFeatureAtPixel(pixel)) {
      //   dom.style.cursor = "pointer";
      //   const features = this.map.getFeaturesAtPixel(pixel);
      //   let feature = features[0];
      //   for (let i = 0; i < features.length; i++) {
      //     if (features[i].values_.pointFlag) {
      //       feature = features[i];
      //       break;
      //     }
      //   }
      //   const properties = feature.getProperties();
      //   const attr = feature.get("attr");
      //   if (properties.pointFlag) {
      //     //台风点位展示详情弹框
      //     setPointData(attr);
      //     vueComponent.typhoonName = this.typhoonData.name;
      //     vueComponent.typhoonPoint = true;
      //     const coordinates = [properties.attr.lng, properties.attr.lat];
      //     vueComponent.pointOverlay.setPosition(coordinates);
      //   } else {
      //     vueComponent.typhoonPoint = false;
      //     vueComponent.pointOverlay.setPosition(null);
      //   }
      // } else {
      //   dom.style.cursor = "default";
      //   vueComponent.typhoonPoint = false;
      //   vueComponent.pointOverlay.setPosition(null);
      // }
    });

    //台风点位点击事件
    this.map.on("click", (evt) => {
      const pixel = evt.pixel;
      if (this.map.hasFeatureAtPixel(pixel)) {
        const features = this.map.getFeaturesAtPixel(pixel);
        const feature = features[0];
        const attr = feature.get("attr");
        if (attr) {
          this.typhoonPlayIndex[attr.tfbh] = attr.index;
          this.showForecast(attr.tfbh, attr);
          if (attr.radius10_quad) {
            const {
              lng,
              lat,
              move_speed,
              power,
              pressure,
              speed,
              time,
              tfbh,
              typhoonInfo,
            } = attr;
            const { ne, nw, se, sw } = attr.radius10_quad;
            const point = { lng, lat, ne, nw, se, sw };
            const param = {
              lng,
              lat,
              move_speed,
              power,
              pressure,
              speed,
              point,
              time,
              code: tfbh,
              typhoonInfo,
            };
            vueComponent.SetTyphoonPointData(param);
          }
          //展示台风转圈圈
          let imgElement = document.createElement("img");
          let picOverlay = this.map.getOverlayById("overlayWind");
          if (picOverlay) {
            this.map.removeOverlay(picOverlay);
          }
          let lyr = new Overlay({
            id: "overlayWind",
            position: [0, 0], //默认空
            positioning: "center-bottom",
            element: imgElement, //绑定上面添加的元素
            //stopEvent: false,
            offset: [-15, -12], //图片偏移量
          });
          this.map.addOverlay(lyr);
          const src = getPointUrl(attr.speed);
          imgElement.src = src;
          lyr.setPosition([attr.lng, attr.lat]);
        }
      }
    });
  };

  //根据风速获取点的url
  function getPointUrl(_speed) {
    // let _url = require("../assets/images/map-tf-level1.gif");
    // if (typeof _speed === "number") {
    //   if (_speed >= 10.8 && _speed < 17.2) {
    //     _url = require("../assets/images/map-tf-level1.gif");
    //   } else if (_speed >= 17.2 && _speed < 24.5) {
    //     _url = require("../assets/images/map-tf-level3.gif");
    //   } else if (_speed >= 24.5 && _speed < 32.7) {
    //     _url = require("../assets/images/map-tf-level2.gif");
    //   } else if (_speed >= 32.7 && _speed < 41.5) {
    //     _url = require("../assets/images/map-tf-level4.gif");
    //   } else if (_speed >= 41.5 && _speed < 50.1) {
    //     _url = require("../assets/images/map-tf-level5.gif");
    //   } else {
    //     _url = require("../assets/images/map-tf-level6.gif");
    //   }
    // }
    // return _url;
  }

  //设置台风点位弹窗数据
  const setPointData = (data) => {
    vueComponent.typhoonPointData = {
      时间: new Date(data.time).format("yyyy-MM-dd hh:mm"),
      中心气压: isEmpty(data.pressure) + "百帕",
      最大风速: isEmpty(data.speed) + "米/秒",
      风力: isEmpty(data.power) + "级",
      等级: isEmpty(data.strong),
      移动速度: isEmpty(data.move_speed) + "公里/时",
      移动方向: isEmpty(data.move_dir),
      七级半径: isEmpty(data.radius7) + "公里",
      十级半径: isEmpty(data.radius10) + "公里",
      十二级半径: isEmpty(data.radius12) + "公里",
    };
  };

  //展示台风数据
  this.showTyphoon = function(typhoonData) {
    const tfbh = typhoonData.code;

    //1.添加台风名称
    this.addNameOverlay(typhoonData);

    //2.创建展示图层
    this.addTyphoonLayer(tfbh, typhoonData);

    // 3.开始播放
    this.playTyphoon(tfbh);
  };

  // 添加台风名称
  this.addNameOverlay = function(typhoonData) {
    const nameDom = document.createElement("div");
    nameDom.setAttribute("class", "typhoon-name");
    nameDom.innerHTML = typhoonData.name;
    const pointStart = typhoonData.points[typhoonData.points.length-1];
    const position = [pointStart.lng, pointStart.lat];
    const nameOverlay = new Overlay({
      element: nameDom,
      position: position,
      positioning: "center-left",
      offset: [20, 0],
    });
    this.map.addOverlay(nameOverlay);
    this.nameOverlays[typhoonData.code] = nameOverlay;
    this.typhoonLayers.set(typhoonData.code + "name", nameOverlay);
  };

  //创建展示图层
  this.addTyphoonLayer = function(tfbh, typhoonData) {
    //台风路径点
    let features = [];
    typhoonData.points.map((item) => {
      const feature = new Feature({
        geometry: new Point([item.lng, item.lat]),
        attr: item,
        pointFlag: true,
      });
      features.push(feature);
    });
    const source = new VectorSource({
      features: features,
    });
    const layer = new Vector({
      source: source,
      style: this.typhoonStyle,
      zIndex: 2000,
    });
    this.map.addLayer(layer);
    //
    this.typhoonLayers.set(tfbh + "layer", layer);
  };

  //播放台风
  this.playTyphoon = function(tfbh) {
    let index = 0; //设置默认初始点index
    let view = this.map.getView();
    //视图以台风路径中间点位中心
    // const centerPos = parseInt(this.typhoonData.points.length / 2);
    // const posPoint = this.typhoonData.points[centerPos];
    // view.setCenter([posPoint.lng, posPoint.lat]);
    // view.setZoom(6);

    //开始播放第一个点
    this.play(index, tfbh);
    //定时播放
    const interval = setInterval(() => {
      index++;
      //播放点index等于台风点数量时 清除定时器
      if (index === this.typhoonData.points.length) {
        clearInterval(interval);
        this.typhoonLayers.set(tfbh + "interval", null);
      } else {
        this.play(index, tfbh);
      }
    }, 20);
    //将定时器存入typhoonMap中 方便外部清除定时器用
    this.typhoonLayers.set(tfbh + "interval", interval);
  };

  this.play = (index, tfbh) => {
    this.typhoonPlayIndex[tfbh] = index;
    const points = this.typhoonData.points;
    let point = points[index];
    point = { ...point, type: "live", index, tfbh };
    if (index > 0) {
      const prePoint = points[index - 1];
      point.start = [prePoint.lng, prePoint.lat];
    }
    point.end = [point.lng, point.lat];
    // const coords = fromLonLat(point.end);
    const feature = new Feature({
      geometry: new Point(point.end),
      attr: point,
    });
    //
    this.typhoonLayers
      .get(tfbh + "layer")
      .getSource()
      .addFeature(feature);

    this.showForecast(tfbh, point);
  };

  //台风展示样式
  this.typhoonStyle = (feature) => {
    const attr = feature.get("attr");
    const { speed, type, index } = attr;
    //
    const color = getPointColor(speed);
    let styles = [];
    //当前点半径
    const radius = type === "live" ? 4 : 3;
    const pointStyle = new Style({
      image: new Circle({
        radius: radius,
        fill: new Fill({
          color: color,
        }),
        stroke: new Stroke({
          color: "rgba(0, 0, 0, 0.6)",
          width: 1,
        }),
      }),
    });
    styles.push(pointStyle);

    if (type === "live" && index === this.typhoonPlayIndex[attr.tfbh]) {
      const center = feature.get("geometry").getCoordinates();
      //7级风圈样式
      if (attr.radius7 > 0) {
        const geometry = getCircleGeometry(center, attr.radius7_quad);
        const style = new Style({
          geometry: geometry,
          stroke: new Stroke({
            color: "#C2CE69",
            width: 1,
          }),
          fill: new Fill({
            color: "rgba(194,206,105,0.2)",
          }),
        });
        styles.push(style);
      }
      //10级风圈样式
      if (attr.radius10 > 0) {
        const geometry = getCircleGeometry(center, attr.radius10_quad);
        const style = new Style({
          geometry: geometry,
          stroke: new Stroke({
            color: "#ffff00",
            width: 1,
          }),
          fill: new Fill({
            color: "rgba(255,255,0,0.2)",
          }),
        });
        styles.push(style);
      }
      //12级风圈样式
      if (attr.radius12 > 0) {
        const geometry = getCircleGeometry(center, attr.radius12_quad);
        const style = new Style({
          geometry: geometry,
          stroke: new Stroke({
            color: "#da7341",
            width: 1,
          }),
          fill: new Fill({
            color: "rgba(218,115,65,0.2)",
          }),
        });
        styles.push(style);
      }
    }

    //台风线的样式
    const { start, end } = attr;
    const lineColor = getLineColor(type, attr.sets);
    const lineDash = type === "live" ? [0] : [8];
    const lineWidth = type === "live" ? 2 : 1;
    if (start && start.length > 0) {
      const coords = [start, end];
      const geometry = new LineString(coords);
      const lineStyle = new Style({
        geometry: geometry,
        stroke: new Stroke({
          color: lineColor,
          width: lineWidth,
          lineDash: lineDash,
        }),
      });
      styles.push(lineStyle);
    }
    return styles;
  };

  //根据风速获取点的颜色
  function getPointColor(_speed) {
    _speed = Number(_speed);
    let _color = "";
    if (typeof _speed === "number") {
      if (_speed >= 10.8 && _speed < 17.2) {
        _color = "rgba(153, 255, 153, .9)";
      } else if (_speed >= 17.2 && _speed < 24.5) {
        _color = "rgba(102, 204, 255, .9)";
      } else if (_speed >= 24.5 && _speed < 32.7) {
        _color = "rgba(255, 255, 102, .9)";
      } else if (_speed >= 32.7 && _speed < 41.5) {
        _color = "rgba(253, 139, 0, .9)";
      } else if (_speed >= 41.5 && _speed < 50.1) {
        _color = "rgba(255, 51, 0, .9)";
      } else {
        _color = "rgba(255, 0, 255, .9)";
      }
    } else {
      _color = "rgba(255, 0, 255, .9)";
    }
    return _color;
  }

  //生成风圈要素
  function getCircleGeometry(center, radiusData) {
    // if (typeof radiusData === "number") {
    // 	return new Circle(center, radiusData * 1000);
    // } else {
    // 	if (radiusData["ne"]) {
    // 		let _coords = [];
    // 		let _angInterval = 6;
    // 		let _pointNums = 360 / (_angInterval * 4);
    // 		let quadrant = {
    // 			// 逆时针算角度
    // 			"0": "ne",
    // 			"1": "nw",
    // 			"2": "sw",
    // 			"3": "se",
    // 		};
    // 		for (let i = 0; i < 4; i++) {
    // 			let _r = parseFloat(radiusData[quadrant[i]]) * 0.01; // 单位是km
    // 			if (!_r) _r = 0;
    // 			for (let j = i * _pointNums; j <= (i + 1) * _pointNums; j++) {
    // 				let _ang = _angInterval * j;
    // 				let x = center[0] + _r * Math.cos((_ang * Math.PI) / 180);
    // 				let y = center[1] + _r * Math.sin((_ang * Math.PI) / 180);
    // 				_coords.push([x, y]);
    // 			}
    // 		}
    // 		return new Polygon([_coords]);
    // 	} else {
    // 		return null;
    // 	}
    // }
  }

  //获取线的颜色
  let getLineColor = (type, sets) => {
    if (type === "live") {
      return "rgba(0, 0, 0, .6)";
    } else {
      let color = this.forcColorDict[sets];
      if (!color) color = "rgba(0, 0, 0, .3)";
      return color;
    }
  };

  //展示预报数据
  this.showForecast = (tfbh, livePoint) => {
    const source = this.typhoonLayers.get(tfbh + "layer").getSource();
    // 1. 删除预报数据
    // this.removeForecast(tfbh);
    // 2. 添加预报
    const forecast = livePoint.forecast ? livePoint.forecast : [];
    const features = [];
    for (var i = 0; i < forecast.length; i++) {
      const f = forecast[i];
      for (var j = 0; j < f.points.length; j++) {
        const point = f.points[j];
        const prePoint = f.points[j - 1];
        point.sets = f.sets;
        point.type = "forc";
        point.index = j;
        point.start =
          j === 0
            ? [livePoint.lng, livePoint.lat]
            : [prePoint.lng, prePoint.lat];
        point.end = [point.lng, point.lat];
        const coords = point.end;
        const feature = new Feature({
          geometry: new Point(coords),
          attr: point,
        });
        features.push(feature);
      }
    }
    source.addFeatures(features);
    this.forecastFeatures[tfbh] = features;
  };

  // 删除预报数据
  this.removeForecast = (tfbh) => {
    if (this.forecastFeatures[tfbh]) {
      const source = this.typhoonLayers.get(tfbh + "layer").getSource();
      for (var i = 0; i < this.forecastFeatures[tfbh].length; i++) {
        const f = this.forecastFeatures[tfbh][i];
        source.removeFeature(f);
      }
      this.forecastFeatures[tfbh] = [];
    }
  };
}
