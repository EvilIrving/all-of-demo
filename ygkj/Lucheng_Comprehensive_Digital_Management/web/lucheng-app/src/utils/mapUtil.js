/*
 * @Author: wupengfei
 * @LastEditTime: 2022-06-08 20:41:50
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 */
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import { Feature, Overlay } from "ol";
// import { Style, Fill, Stroke, Icon, Text } from "ol/style";
// import { Point, Polygon, LineString, MultiPolygon } from "ol/geom";
import { Style, Fill, Stroke, Icon, Text, Circle } from 'ol/style';
import { Point, Polygon, LineString, MultiPolygon, Circle as GeoCircle } from 'ol/geom';
import { Select } from 'ol/interaction'
// import singleClick from "ol/events/condition/singleClick"
import { singleClick } from "ol/events/condition"
import Cluster from 'ol/source/Cluster';


/*
  desc:加载线方法
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
        color: styleObj.fillColor || "red"
      }),
      stroke: new Stroke({
        color: styleObj.lineColor || "red",
        width: styleObj.lineWidth || 2
      }),
      text: new Text({
        text: styleObj.title,
        // textAlign: 'center',
        // textBaseline: 'middle',
        scale: "1.5",
        fill: new Fill({
          color: "red"
        })
      })
    })
  });
  const plygon = new LineString(coordinates);
  let feature = new Feature({
    geometry: plygon,
    name: styleObj.title,
    projectId: styleObj.projectId
  });
  vectorSource.addFeature(feature);
  return vectorLayer;
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
        color: styleObj.fillColor || "rgba(91,169,252, 0.6)"
      }),
      stroke: new Stroke({
        color: styleObj.lineColor || "blue",
        width: styleObj.lineWidth || 2
      }),
      text: new Text({
        text: styleObj.title,
        textAlign: 'center',
        textBaseline: 'middle',
        scale: "1.5",
        fill: new Fill({
          color: "red"
        })
      })
    })
  });
  let polygon;
  if (polygonType == "MultiPolygon") {
    polygon = new MultiPolygon(coordinates);
  } else {
    polygon = new Polygon(coordinates);
  }
  let feature = new Feature({
    geometry: polygon,
    name: styleObj.title,
    projectId: styleObj.projectId
  });
  vectorPolygonSource.addFeature(feature);
  return vectorPolygonLayer;
}

export function addLayerClick(clickLayer) {
  let selectClick = new Select({
    condition: singleClick,
    style: null,
    filter: (feature, layer) => {
      return layer === clickLayer // 需要添加事件的layer
    }
  });
  return selectClick;
}



/*
  desc:画点方法
  params:
    coordinates:[] //点经纬度数组
    styleObj:{
      anchor:[],
      anchorXUnits:"",//anchorX单位
      anchorYUnits:"",//anchorY单位
      src: "",//图片路径
      title:""//标题
    }
*/
export function drawPoint(coordinates, styleObj, type) {
  let vectorSource = new VectorSource({});
  let textOption = { font: "normal 12px 微软雅黑", padding: [1, 2, 1, 2] };
  coordinates.forEach(element => {
    const iconFeature = new Feature({
      geometry: new Point(element.point),
      data: element
    });
    if (type) {
      styleObj.src = element.imgSrc
    }
    const iconStyle = new Style({
      image: new Icon({
        anchorXUnits: styleObj.anchorXUnits || "fraction",
        anchorYUnits: styleObj.anchorYUnits || "pixels",
        src: styleObj.src,
        size: [200, 200],
        scale: styleObj.scale || 1.5
      }),
      // text: new Text({
      //   text: element.name,
      //   textAlign: 'center',
      //   scale: "1.5",
      //   offsetX: 20,
      //   offsetY: 50,
      //   fill: new Fill({
      //     color: "#1B89FA"
      //   })
      // })
    });
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  });
  let vectorLayer = new Vector({
    title: styleObj.title,
    source: vectorSource
  });
  return vectorLayer;
}

export function drawSinglePoint(coordinates, styleObj) {
  let vectorSource = new VectorSource({});
  let textOption = { font: "normal 12px 微软雅黑", padding: [1, 2, 1, 2] };
  coordinates.forEach(element => {
    (element, 'element');
    const iconFeature = new Feature({
      geometry: new Point(element.point),
      data: element
    });
    const iconStyle = new Style({
      image: new Icon({
        anchorXUnits: styleObj.anchorXUnits || "fraction",
        anchorYUnits: styleObj.anchorYUnits || "pixels",
        src: styleObj.src,
        size: [200, 200],
        scale: styleObj.scale || 1
      }),
      text: new Text({
        ...textOption,
        text: element.name,
        textAlign: 'center',
        scale: "1",
        fill: new Fill({ color: "#333" }),
        backgroundFill: new Fill({ color: "#fff" }),
        offsetX: styleObj.textOffsetX || 15,
        offsetY: styleObj.textOffsetY || -10
      })
    });
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  });
  let vectorLayer = new Vector({
    title: styleObj.title,
    source: vectorSource
  });
  return vectorLayer;
}

export function showPointTile(pointArr, elem) {
  let pointOverlayArr = [];
  pointArr.map(item => {
    let coordinates = [item.LGTD, item.LTTD];
    let pointOverlay = new Overlay({
      position: coordinates,					//经纬度坐标需要转
      positioning: "center-center",						//设置该坐标点在标记图形中心上
      element: document.getElementById(elem),
      stopEvent: false
    });
    pointOverlayArr.push(pointOverlay);
  })
  return pointOverlayArr;
}
//展示当前点位
export function drawMyPoint(coordinates) {
  let vectorSource = new VectorSource({});
  let imgSrc = require("@/assets/images/my-position.png");
  const iconFeature = new Feature({
    geometry: new Point([coordinates.longitude, coordinates.latitude])
  });
  iconFeature.setProperties({
    featureProperties: {
      index: 0,
      lng: coordinates.longitude,
      lat: coordinates.latitude,
      data: coordinates,
    }
  });
  iconFeature.setId(0); //设置feature id方便获取对应的feature
  const iconStyle = new Style({
    image: new Icon({
      anchor: [11, 12],
      anchorXUnits: "pixels", //anchorX单位
      anchorYUnits: "pixels", //anchorY单位
      src: imgSrc, //图片路径
      scale: 1
    }),
  });
  iconFeature.setStyle(iconStyle);
  vectorSource.addFeature(iconFeature);
}

export function showTestClusterPoint(data) {
  let vectorSource = new VectorSource({});
  for (let i = 0; i < data.length; i++) {
    const iconFeature = new Feature({
      geometry: new Point([data[i].geometry.x, data[i].geometry.y])
    });
    iconFeature.setProperties({
      featureProperties: {
        index: i,
        lng: data[i].geometry.x,
        lat: data[i].geometry.y,
        data: data[i],
        type: 'textPoint'
      }
    });
    iconFeature.setId(i); //设置feature id方便获取对应的feature
    let iconText = data[i].attributes.value ? data[i].attributes.value.toFixed(1) : 0;
    const iconStyle = new Style({
      image: new Circle({
        radius: 0
      }),
      text: new Text({
        text: iconText, // 路线标签文字
        font: 'normal 12px 微软雅黑', //字体样式
        fill: new Fill({
          //文字填充样式
          color: '#888'
        }),
        backgroundFill: new Fill({
          color: [255, 255, 255, 0]
        }),
        padding: [1, 2, 1, 2]
      })
    });
    iconFeature.setStyle(iconStyle);
    vectorSource.addFeature(iconFeature);
  }

  //创建聚合源
  let clusterSource = new Cluster({
    distance: parseInt(20, 20), // 两个点聚合的间距
    source: vectorSource // 点状要素生成的矢量数据源
  });

  let clusters = new Vector({
    source: clusterSource,
    style: (feature) => {
      let featureItems = feature.get('features');
      let max = 0;
      for (let i = 0; i < featureItems.length; i++) {
        const item = featureItems[i].values_.featureProperties.data;
        const textValue = item.attributes.value.toFixed(1);
        if (textValue > max) {
          max = textValue;
        }
      }
      let style = new Style({
        image: new Circle({
          radius: 0
        }),
        text: new Text({
          text: max + '', // 路线标签文字
          font: 'normal 12px 微软雅黑', //字体样式
          fill: new Fill({
            //文字填充样式
            color: '#888'
          }),
          backgroundFill: new Fill({
            color: [255, 255, 255, 0]
          }),
          padding: [1, 2, 1, 2]
        })
      });
      return style;
    },
    zIndex: 100
  });
  return clusters;
}



