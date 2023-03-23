/*
 * @Author: wupengfei
 * @LastEditTime: 2021-07-16 13:56:59
 * @LastEditors: Please set LastEditors
 * @Descripttion:
 * @FilePath: \FloodDisasterDispatch\src\utils\mapUtil.js
 */
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import Draw from "ol/interaction/Draw";
import { Feature, Overlay } from "ol";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import { Point, Polygon, LineString, MultiPolygon, Circle as GeoCircle } from "ol/geom";
import { Select } from "ol/interaction";
import { singleClick } from "ol/events/condition";
import GeoJSON from "ol/format/GeoJSON";
import WKT from 'ol/format/WKT';

import "ol/ol.css";
import Cluster from "ol/source/Cluster";
import Map from "ol/Map";
import Stamen from "ol/source/Stamen";
import View from "ol/View";
// import { Heatmap as HeatmapLayer, Tile as TileLayer, Vector as LayerVec } from 'ol/layer'

import olsourceOSM from "ol/source/OSM";
import { get as getProjection, transform, fromLonLat } from "ol/proj";
import { isEmpty } from "./utils";

// import { Vector as SourceVec, Cluster, XYZ } from 'ol/source'

// import { WindLayer } from 'ol-wind'
// export function addVectorLayer(coordinates, styleObj) {

// }
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
export function drawPoint(coordinates, styleObj) {
	let vectorSource = new VectorSource({});
	coordinates.forEach((element) => {
		let point = [element.lng, element.lat];
		const iconFeature = new Feature({
			geometry: new Point(point),
			data: element,
		});
		// const iconStyle = new Style({
		// 	image: new Icon({
		// 		anchor: styleObj.anchor || [0.5, 46],
		// 		anchorXUnits: styleObj.anchorXUnits || "fraction",
		// 		anchorYUnits: styleObj.anchorYUnits || "pixels",
		// 		src: element.src || styleObj.src,
		// 		scale: styleObj.scale || 1,
		// 		rotation: element.rotation || 0
		// 	}),
		// });
		// iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	});
	let vectorLayer = new Vector({
		title: styleObj.title,
		source: vectorSource,
		zIndex: 100,
		style: (feature) => {
			const mapZoom = window.mapZoom;
			let style;
			// console.log(size, 666)
			let titleText;
			// console.log(feature.getProperties(),666)
			const featureData = feature.getProperties().data;
			if (mapZoom > 11) {
				// console.log(featureData,666);
				const propkey = featureData.props ? Object.keys(featureData.props) : [];
				let titleData = `${featureData.name || ''} ${propkey.length > 0 ? (propkey[0] + ':' + featureData.props[propkey[0]]):''}`;
				titleText = new Text({
					text: titleData, // 路线标签文字
					font: "normal 14px 微软雅黑", //字体样式
					fill: new Fill({
						//文字填充样式
						color: "#333",
					}),
					backgroundFill: new Fill({
						color: "#fff",
					}),
					offsetY: styleObj.textOffsetY || -36,
					padding: [1, 2, 1, 2],
				});
			}
			style = new Style({
				image: new Icon({
					anchor: styleObj.anchor || [0.5, 46],
					anchorXUnits: styleObj.anchorXUnits || "fraction",
					anchorYUnits: styleObj.anchorYUnits || "pixels",
					src: featureData.src || styleObj.src,
					scale: styleObj.scale || 1,
					rotation: featureData.rotation || 0
				}),
				text: titleText
			});
			return style;
		},
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

export function drawHighLightLine(layer, mapObj) {
	let vectorSource = new VectorSource();
	layer = new Vector({
		source: vectorSource,
		style: () => {
			const style = new Style({
				fill: new Fill({
					color: "raba(0,0,0,1)",
				}),
				stroke: new Stroke({
					color: "rgba(255, 255, 255, 1)",
					width: 1,
				}),
			});
			return style;
		},
	});
	mapObj.addLayer(layer);
	onBindLayerClick(layer);
	return layer;
}

function onBindLayerClick(layer) {
	layer.on("precompose", (evt) => {
		evt.context.shadowBlur = 25;
		evt.context.shadowColor = "black";
	});
	layer.on("postcompose", (evt) => {
		evt.context.shadowBlur = 0;
		evt.context.shadowColor = "black";
	});
}

/**
 * 绘制带两端点位的线段
 * @param {Array} coordinatesList [[[lng,lat]],[lng,lat]...]
 * @param {Object} styleObj {}
 * @returns
 */
export function drawLineEx(coordinatesList, styleObj) {
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
	console.log(coordinatesList,9888)

	for (let coordinates of coordinatesList) {
		const plygon = new LineString(coordinates.lineList);
		let feature = new Feature({ geometry: plygon, data: coordinates.data });
		vectorSource.addFeature(feature);
	}

	return vectorLayer;
}
//绘制带箭头的线
export function drawArrowLine(coordinates) {
	// 用于设置线串所在的矢量图层样式的函数
	let styleFunction = function (feature) {
		let geometry = feature.getGeometry();
		let styles = [
			new Style({
				// 线串的样式
				stroke: new Stroke({
					color: "#FC3",
					width: 2,
				}),
			}),
		];
		// 对线段的每一个子线段都设置箭头样式
		geometry.forEachSegment(function (start, end) {
			var dx = end[0] - start[0];
			var dy = end[1] - start[1];
			var rotation = Math.atan2(dy, dx); // 获取子线段的角度（弧度）
			//arrows
			styles.push(
				new Style({
					// 与线串的各个子线段对应的样式
					geometry: new Point(end),
					image: new Icon({
						src: require("../assets/images/map-right-arrow.png"),
						anchor: [0.75, 0.5], // 图标锚点
						rotateWithView: true, // 与地图视图一起旋转
						// 设置子线段箭头图标样式的角度
						rotation: rotation, // 因为角度以顺时针旋转为正值，所以前面添加负号
					}),
				})
			);
		});
		return styles;
	};
	let vectorSource = new VectorSource();
	const plygon = new LineString(coordinates);
	let feature = new Feature({
		geometry: plygon,
	});
	vectorSource.addFeature(feature);
	const vectorLayer = new Vector({
		source: vectorSource,
		style: styleFunction,
	});
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
			data: element,
		});
		const lineStyle = new Style({
			fill: new Fill({
				color: element.fillColor || "rgba(255, 255, 255, 1)",
			}),
			stroke: new Stroke({
				color: element.lineColor || "#00C1FF",
				width: element.lineWidth || 2,
				lineDash: [10, 10],
			}),
			text: new Text({
				text: element.text, // 路线标签文字
				font: "normal 12px 微软雅黑", //字体样式
				fill: new Fill({
					//文字填充样式
					color: element.fontColor || [16, 168, 218, 1],
				}),
				backgroundFill: new Fill({
					color: [255, 255, 255, 0],
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

export function drawHighLines(arr) {
	let vectorSource = new VectorSource({});
	const lineFeature = new Feature({
		geometry: new LineString(arr),
	});
	const lineStyle = new Style({
		fill: new Fill({
			color: "rgba(255, 255, 255, 1)",
		}),
		stroke: new Stroke({
			color: "#1CFFF5",
			width: 2,
			lineDash: [10, 10],
		}),
	});
	lineFeature.setStyle(lineStyle);
	vectorSource.addFeature(lineFeature);
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
		zIndex: 10,
		style: new Style({
			fill: new Fill({
				color: styleObj.fillColor || "rgba(124,222,0, 0.48)",
			}),
			stroke: new Stroke({
				color: styleObj.lineColor || "rgba(124,222,0,1)",
				width: styleObj.lineWidth || 2,
				lineDash: styleObj.lineDash || [],
			}),
			text: new Text({
				text: styleObj.title,
				textAlign: "center",
				textBaseline: "middle",
				scale: "1.5",
				fill: new Fill({
					color: "red",
				}),
			}),
		}),
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
		data: styleObj.data,
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
		draw.on("drawend", function (e) {
			if (featureType == "Point") {
				mapObj.drawPointData = e.feature.values_.geometry.extent_;
			} else {
				mapObj.drawPointData = e.feature.values_.geometry.flatCoordinates;
			}
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
	// console.log(clickLayer,222)
	let selectClick = new Select({
		condition: singleClick,
		style: null,
		// style: areaStyles,
		filter: (feature, layer) => {
			return layer === clickLayer; // 需要添加事件的layer
		},
	});
	return selectClick;
}

/**
 * 画点方法（聚合）
 * @param {Array} coordinates 点经纬度数组
 * @param {*} styleObj {anchor:[],anchorXUnits:"",//anchorX单位anchorYUnits:"",//anchorY单位src: "",//图片路径title:""//标题}
 * @returns
 */
export function drawClusterPoint(coordinates, styleObj) {
	let vectorSource = new VectorSource({});
	let features = []; // 存放点要素的数组
	coordinates.forEach((element) => {
		if (element.lng & element.lat) {
			let point = [element.lng, element.lat];
			const iconFeature = new Feature({
				geometry: new Point(point),
				data: element,
				source: clusterSource,
			});
			const iconStyle = new Style({
				image: new Icon({
					anchor: styleObj.anchor || [0.5, 1],
					anchorXUnits: styleObj.anchorXUnits || "fraction",
					anchorYUnits: styleObj.anchorYUnits || "fraction",
					src: element.src || styleObj.src,
					scale: styleObj.scale || 1,
				}),
				// text: new Text({
				// 	text: styleObj.title,
				// 	textAlign: "center",
				// 	textBaseline: "middle",
				// 	scale: "1.5",
				// 	fill: new Fill({
				// 		color: "red",
				// 	}),
				// })
			});
			// iconFeature.setStyle(iconStyle);
			vectorSource.addFeature(iconFeature);
			features.push(iconFeature);
			let styleCache = {};//用于保存特定数量的聚合群的要素样式
			let vectorLayer = new Vector({
				title: styleObj.title || "",
				source: vectorSource,
				zIndex: 100,
				style: function (feature) {
					let size = feature.get('features').length;
					let style = styleCache[size];
					if (!style) {
						style = [
							new Style({
								image: new Icon({
									anchor: styleObj.anchor || [0.5, 1],
									anchorXUnits: styleObj.anchorXUnits || "fraction",
									anchorYUnits: styleObj.anchorYUnits || "fraction",
									src: element.src || styleObj.src,
									scale: styleObj.scale || 1,
								}),
							})
						];
						styleCache[size] = style;
					}
					return style;
				}
			});
			return vectorLayer;
		}
	});

	//创建聚合源
	let clusterSource = new Cluster({
		distance: styleObj.distance || parseInt(30, 20), // 两个点聚合的间距
		source: new VectorSource({ features }), // 点状要素生成的矢量数据源
	});

	// 点的样式
	let styleCache = {};
	let clusters = new Vector({
		source: clusterSource,
		zIndex: 100,
		style: (feature) => {
			const mapZoom = window.mapZoom;
			let size = feature.get("features").length;
			let style;
			// console.log(size, 666)
			let titleText = null;
			if (size == 1 && mapZoom > 12) {
				const featureData = feature.getProperties().features[0].values_.data;
				const propkey = featureData.props ? Object.keys(featureData.props) : [];
				let titleData = `${featureData.name}` ;
				if(propkey.length > 0){
					titleData = `${featureData.name} ${propkey[0]}:${featureData.props[propkey[0]]}` ;
				}
				titleText = new Text({
					text: titleData, // 路线标签文字
					font: "normal 14px 微软雅黑", //字体样式
					fill: new Fill({
						//文字填充样式
						color: "#333",
					}),
					backgroundFill: new Fill({
						color: "#fff",
					}),
					offsetY: styleObj.textOffsetY || -36,
					padding: [1, 2, 1, 2],
				});
			}else if(size > 1){
				titleText = new Text({
					text: `${size}`, // 路线标签文字
					font: "normal 12px 微软雅黑", //字体样式
					fill: new Fill({
						//文字填充样式
						color: "#fff",
					}),
					backgroundFill: new Fill({
						color: [255, 255, 255, 0],
					}),
					offsetX: styleObj.textOffsetX || 15,
					offsetY: styleObj.textOffsetY || -10,
					padding: [1, 2, 1, 2],
				});
			}
			style = new Style({
				image: new Icon({
					radius: 10,
					anchor: styleObj.anchor || [0.5, 46],
					anchorXUnits: styleObj.anchorXUnits || "fraction",
					anchorYUnits: styleObj.anchorYUnits || "pixels",
					src: size == 1 ? styleObj.src : styleObj.srcs || "",
					scale: styleObj.scale || 1,
				}),
				text: titleText
			});
			styleCache[size] = style;
			return style;
		},
	});
	return clusters;
}

export function Typhoon(map, typhoonData, typhoonLayers, typhoonNames, typhoonTimes, vueTemp) {
	{
		const that = this;
		that.vueTemp = vueTemp;
		that._map = map;
		that._speed = 50;
		that._typhoonList = typhoonData;
		that.typhoonLayers = typhoonLayers;
		that.typhoonNames = typhoonNames;
		that.typhoonTimes = typhoonTimes;
		that._typhoonList.points = eval(typhoonData.points);
		that._typhoonData = {};
		that._nameOverlays = {};
		that._typhoonPlayFlag = {};
		that._typhoonPlayIndex = {};
		that._warnLines = null;
		that._typhoonLayers = {};
		that._forecastFeatures = {};
		that._forcColorDict = {
			中国: "#ec5d72",
			中国香港: "#ec7cfe",
			中国台湾: "#ecaa65",
			日本: "#56f66e",
			美国: "#53dbfe",
			韩国: "#72a4ac",
			欧洲: "#4c54a6",
		};

		that.init = function () {
			// 添加警戒线
			let warnLines = that.addWarnLine();
			// 添加地图事件
			that.addMapEvent();
			// console.log(that._typhoonList.points)
			// 获取台风数据
			that.showTyphoon(that._typhoonList);
		};

		that.addWarnLine = function () {
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
				}, {
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
				},];
			const features = [];
			for (var i = 0; i < warnLineData.length; i++) {
				const d = warnLineData[i];
				const geometry = new LineString(d.points);
				geometry.transform("EPSG:4326", that._map.getView().getProjection());
				const feature = new Feature({
					geometry: geometry,
					attr: d,
				});
				features.push(feature);
			}
			const source = new VectorSource({
				features: features,
			});
			that._warnLines = new Vector({
				source: source,
				style: that.warnLineStyle,
				opacity: 0.8,
				zIndex: 100,
			});
			that._map.addLayer(that._warnLines);
		};
		// 警戒线样式设置
		that.warnLineStyle = function (feat) {
			const attr = feat.get("attr");
			return new Style({
				stroke: new Stroke({
					color: attr.color,
					width: attr.weight,
					lineDash: attr.dashArray,
				}),
			});
		};

		// 展示台风
		that.showTyphoon = function (typhoonData) {
			const tfbh = typhoonData.code;
			that._typhoonData[tfbh] = typhoonData;

			// 1. 添加台风名称
			that.addNameOverlay(typhoonData);

			// 2. 创建展示图层
			that.addTyphoonLayer(tfbh, typhoonData);

			// 3.开始播放
			that.playTyphoon(tfbh);
		};

		// 移除台风
		that.removeTyphoon = function (tfbh) {
			// 删除台风名称
			that._map.removeOverlay(that._nameOverlays[tfbh]);
			// 删除展示图层
			that._map.removeLayer(that._typhoonLayers[tfbh].layer);
			// 消除定时器
			clearInterval(that._typhoonPlayFlag[tfbh]);
		};

		// 添加台风名称
		that.addNameOverlay = function (typhoonData) {
			const nameDom = document.createElement("div");
			nameDom.setAttribute("class", "typhoon-name");
			nameDom.innerHTML = typhoonData.name;
			const pointStart = typhoonData.points[0];
			const position = [pointStart.lng, pointStart.lat];
			const nameOverlay = new Overlay({
				element: nameDom,
				position: position,
				positioning: "center-left",
				offset: [15, 0],
			});
			that._map.addOverlay(nameOverlay);
			typhoonNames.push(nameOverlay);
			that._nameOverlays[typhoonData.tfbh] = nameOverlay;
		};

		// 根据编号添加台风图层
		that.addTyphoonLayer = function (tfbh, typhoonData) {
			let features = [];
			typhoonData.points.map((item) => {
				const feature = new Feature({
					geometry: new Point([item.lng, item.lat]),
					attr: item,
				});
				features.push(feature);
			});
			// const geometry = new LineString(tempPoints);
			// new Point(coords)
			// geometry.transform('EPSG:4326', that._map.getView().getProjection());
			// features.push(feature);
			const source = new VectorSource({
				features: features,
			});
			const layer = new Vector({
				source: source,
				style: that.typhoonStyle,
				zIndex: 100,
			});
			that.typhoonLayers.push(layer);
			that._map.addLayer(layer);
			// that._typhoonLayers[tfbh] = {
			// 	source: source,
			// 	layer: layer,
			// };
			// that._forecastFeatures[tfbh] = [];
		};
		// 添加地图事件
		that.addMapEvent = function () {
			// 鼠标移动事件
			that._map.on("pointermove", function (evt) {
				const pixel = evt.pixel;
				const dom = that._map.getTargetElement();
				if (that._map.hasFeatureAtPixel(pixel)) {
					dom.style.cursor = "pointer";
					const features = that._map.getFeaturesAtPixel(pixel);
					const feature = features[0];
				} else {
					dom.style.cursor = "default";
				}
			});

			that._map.on("click", function (evt) {
				const pixel = evt.pixel;

				if (that._map.hasFeatureAtPixel(pixel)) {
					const features = that._map.getFeaturesAtPixel(pixel);
					const feature = features[0];
					const attr = feature.get("attr");
					clearInterval(that._typhoonPlayFlag[attr.tfbh]);
					that.vueTemp.$emit("typhoonPointClick", attr);
					if (attr) {
						that._typhoonPlayIndex[attr.tfbh] = attr.index;
						that.showForecast(attr.tfbh, attr);
					}
				}
			});
		};

		// 播放台风
		that.playTyphoon = function (tfbh) {
			let index = 0;
			const typhoonData = that._typhoonData[tfbh];
			let view = that._map.getView();
			const centerPos = parseInt(typhoonData.points.length / 2);
			const posPoint = typhoonData.points[centerPos];
			// view.setZoom(7);
			view.setCenter([posPoint.lng, posPoint.lat]);
			that.play(index, tfbh);
			that._typhoonPlayFlag[tfbh] = setInterval(function () {
				index++;
				// const curPoint = typhoonData.points[index];
				// if (curPoint) {
				//   view.setCenter([curPoint.lng, curPoint.lat]);
				// }
				if (index === typhoonData.points.length) {
					clearInterval(that._typhoonPlayFlag[tfbh]);
				} else {
					that.play(index, tfbh);
				}
			}, that._speed);
			that.typhoonTimes.push(that._typhoonPlayFlag[tfbh]);
		};

		// 播放单个点
		that.play = function (index, tfbh) {
			// 删除预报
			// that.removeForecast(tfbh);
			that._typhoonPlayIndex[tfbh] = index;

			const points = that._typhoonData[tfbh].points;
			const point = points[index];
			point.type = "live";
			point.index = index;
			point.tfbh = tfbh;
			if (index > 0) {
				const prePoint = points[index - 1];
				point.start = [prePoint.lng, prePoint.lat];
			}
			point.end = [point.lng, point.lat];
			const coords = fromLonLat(point.end);
			const feature = new Feature({
				geometry: new Point(coords),
				attr: point,
			});
			that._typhoonLayers[tfbh].source.addFeature(feature);

			// 最后一个实况点，展示预报路径
			// if (index === that._typhoonData[tfbh].points.length - 1) {
			//   that.showForecast(tfbh, point);
			// }
		};

		// 删除预报数据
		that.removeForecast = function (tfbh) {
			const source = that._typhoonLayers[tfbh].source;
			for (var i = 0; i < that._forecastFeatures[tfbh].length; i++) {
				const f = that._forecastFeatures[tfbh][i];
				source.removeFeature(f);
			}
			that._forecastFeatures[tfbh] = [];
		};

		// 展示预报数据
		that.showForecast = function (tfbh, livePoint) {
			const source = that._typhoonLayers[tfbh].source;
			// 1. 删除预报数据
			that.removeForecast(tfbh);
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
					point.start = j === 0 ? [livePoint.lng, livePoint.lat] : [prePoint.lng, prePoint.lat];
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
			that._forecastFeatures[tfbh] = features;
		};

		// 台风展示样式
		that.typhoonStyle = function (feat) {
			const attr = feat.get("attr");
			const speed = attr.speed;
			const type = attr.type;
			const index = attr.index;
			const color = that.getPointColor(speed);
			let styles = [];
			// 点的样式
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

			if (type === "live" && index === that._typhoonPlayIndex[attr.tfbh]) {
				const center = feat.get("geometry").getCoordinates();
				const fillStyle = new Fill({
					color: "rgba(0, 0, 0, 0.2)",
				});
				// 7级风圈的样式
				if (attr.radius7 > 0) {
					const geometry = that.getCircleGeometry(center, attr.radius7_quad);
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
				// 10级风圈的样式
				if (attr.radius10 > 0) {
					const geometry = that.getCircleGeometry(center, attr.radius10_quad);
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
				// 12级风圈的样式
				if (attr.radius12 > 0) {
					const geometry = that.getCircleGeometry(center, attr.radius12_quad);
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

			// 线的样式
			const start = attr.start;
			const end = attr.end;
			const lineColor = that.getLineColor(type, attr.sets);
			const lineDash = type === "live" ? [0] : [8];
			const lineWidth = type === "live" ? 2 : 1;
			if (start && start.length > 0) {
				const coords = [start, end];
				const geometry = new LineString(coords);
				geometry.transform("EPSG:4326", that._map.getView().getProjection());
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

		// 获取线的颜色
		that.getLineColor = function (type, sets) {
			if (type === "live") {
				return "rgba(0, 0, 0, .6)";
			} else {
				let color = that._forcColorDict[sets];
				if (!color) color = "rgba(0, 0, 0, .3)";
				return color;
			}
		};

		// 根据风速获取点的颜色
		that.getPointColor = function (_speed) {
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
		};

		that.getCircleGeometry = function (center, radiusData) {
			if (typeof radiusData === "number") {
				return new Circle(center, radiusData * 1000);
			} else {
				if (radiusData["ne"]) {
					let _coords = [];
					let _angInterval = 6;
					let _pointNums = 360 / (_angInterval * 4);
					let quadrant = {
						// 逆时针算角度
						"0": "ne",
						"1": "nw",
						"2": "sw",
						"3": "se",
					};
					for (let i = 0; i < 4; i++) {
						let _r = parseFloat(radiusData[quadrant[i]]) * 0.01; // 单位是km
						if (!_r) _r = 0;
						for (let j = i * _pointNums; j <= (i + 1) * _pointNums; j++) {
							let _ang = _angInterval * j;
							let x = center[0] + _r * Math.cos((_ang * Math.PI) / 180);
							let y = center[1] + _r * Math.sin((_ang * Math.PI) / 180);
							_coords.push([x, y]);
						}
					}
					return new Polygon([_coords]);
				} else {
					return null;
				}
			}
		};
	}
}

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

	this.init = function () {
		// 添加警戒线
		this.addWarnLine();
		// 添加地图事件
		this.addMapEvent();
		// 展示台风数据
		this.showTyphoon(this.typhoonData);
	};

	// 添加警戒线
	this.addWarnLine = function () {
		// 警戒线样式设置
		const warnLineStyle = function (feat) {
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
	this.addMapEvent = function () {
		//鼠标移动事件
		this.map.on("pointermove", (evt) => {
			const pixel = evt.pixel;
			const dom = this.map.getTargetElement();
			if (this.map.hasFeatureAtPixel(pixel)) {
				dom.style.cursor = "pointer";
				const features = this.map.getFeaturesAtPixel(pixel);
				let feature = features[0];
				for (let i = 0; i < features.length; i++) {
					if (features[i].values_.pointFlag) {
						feature = features[i];
						break;
					}
				}
				const properties = feature.getProperties();
				const attr = feature.get("attr");
				if (properties.pointFlag) {
					//台风点位展示详情弹框
					setPointData(attr);
					vueComponent.typhoonName = this.typhoonData.name;
					vueComponent.typhoonPoint = true;
					const coordinates = [properties.attr.lng, properties.attr.lat];
					vueComponent.pointOverlay.setPosition(coordinates);
				} else {
					vueComponent.typhoonPoint = false;
					vueComponent.pointOverlay.setPosition(null);
				}
			} else {
				dom.style.cursor = "default";
				vueComponent.typhoonPoint = false;
				vueComponent.pointOverlay.setPosition(null);
			}
		});

		//台风点位点击事件
		this.map.on("click", (evt) => {
			const pixel = evt.pixel;
			if (this.map.hasFeatureAtPixel(pixel)) {
				const features = this.map.getFeaturesAtPixel(pixel);
				const feature = features[0];
				const attr = feature.get("attr");
				// console.log(attr,88899);
				if (attr) {
					this.typhoonPlayIndex[attr.tfbh] = attr.index;
					this.showForecast(attr.tfbh, attr);
					if (attr.radius10_quad) {
						const { lng, lat, move_speed, power, pressure, speed, time, tfbh, typhoonInfo } = attr;
						const { ne, nw, se, sw } = attr.radius10_quad;
						const point = { lng, lat, ne, nw, se, sw };
						const param = { lng, lat, move_speed, power, pressure, speed, point, time, code: tfbh, typhoonInfo };
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
						position: [0, 0],//默认空
						positioning: 'center-bottom',
						element: imgElement,//绑定上面添加的元素
						//stopEvent: false,
						offset: [-15, -12]//图片偏移量
					});
					this.map.addOverlay(lyr);
					const src = getPointUrl(attr.speed);
					imgElement.src = src;
					lyr.setPosition([attr.lng, attr.lat])
				}
			}
		});
	};

	//根据风速获取点的url
	function getPointUrl(_speed) {
		let _url = require('../assets/images/map-tf-level1.gif');
		if (typeof _speed === "number") {
			if (_speed >= 10.8 && _speed < 17.2) {
				_url = require('../assets/images/map-tf-level1.gif');
			} else if (_speed >= 17.2 && _speed < 24.5) {
				_url = require('../assets/images/map-tf-level3.gif');
			} else if (_speed >= 24.5 && _speed < 32.7) {
				_url = require('../assets/images/map-tf-level2.gif');
			} else if (_speed >= 32.7 && _speed < 41.5) {
				_url = require('../assets/images/map-tf-level4.gif');
			} else if (_speed >= 41.5 && _speed < 50.1) {
				_url = require('../assets/images/map-tf-level5.gif');
			} else {
				_url = require('../assets/images/map-tf-level6.gif');
			}
		}
		return _url;
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
	this.showTyphoon = function (typhoonData) {
		const tfbh = typhoonData.code;

		//1.添加台风名称
		this.addNameOverlay(typhoonData);

		//2.创建展示图层
		// this.addTyphoonLayer(tfbh, typhoonData);

		// 3.开始播放
		this.playTyphoon(tfbh);
	};

	// 添加台风名称
	this.addNameOverlay = function (typhoonData) {
		const nameDom = document.createElement("div");
		nameDom.setAttribute("class", "typhoon-name");
		nameDom.innerHTML = typhoonData.name;
		const pointStart = typhoonData.points[0];
		const position = [pointStart.lng, pointStart.lat];
		const nameOverlay = new Overlay({
			element: nameDom,
			position: position,
			positioning: "center-left",
			offset: [15, 0],
		});
		this.map.addOverlay(nameOverlay);
		this.nameOverlays[typhoonData.code] = nameOverlay;
		this.typhoonLayers.set(typhoonData.code + 'name', nameOverlay);
	};

	//创建展示图层
	this.addTyphoonLayer = function (tfbh, typhoonData) {
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
		// console.log(666,layer.getSource());
		this.typhoonLayers.set(tfbh + 'layer', layer);
	};

	//播放台风
	this.playTyphoon = function (tfbh) {
		let index = 0; //设置默认初始点index
		let view = this.map.getView();
		//视图以台风路径中间点位中心
		const centerPos = parseInt(this.typhoonData.points.length / 2);
		const posPoint = this.typhoonData.points[centerPos];
		view.setCenter([posPoint.lng, posPoint.lat]);
		view.setZoom(6);

		let features = [];
    const source = new VectorSource({
      features: features,
    });
    const layer = new Vector({
      source: source,
      style: this.typhoonStyle,
      zIndex: 2000,
    });
    this.map.addLayer(layer);
    this.typhoonLayers.set(tfbh + 'layer', layer);

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
		// console.log(feature);
		this.typhoonLayers
			.get(tfbh + 'layer')
			.getSource()
			.addFeature(feature);

		this.showForecast(tfbh, point);
	};

	//台风展示样式
	this.typhoonStyle = (feature) => {
		const attr = feature.get("attr");
		const { speed, type, index } = attr;
		// console.log(attr);
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
		if (typeof radiusData === "number") {
			return new Circle(center, radiusData * 1000);
		} else {
			if (radiusData && radiusData["ne"]) {
				let _coords = [];
				let _angInterval = 6;
				let _pointNums = 360 / (_angInterval * 4);
				let quadrant = {
					// 逆时针算角度
					"0": "ne",
					"1": "nw",
					"2": "sw",
					"3": "se",
				};
				for (let i = 0; i < 4; i++) {
					let _r = parseFloat(radiusData[quadrant[i]]) * 0.01; // 单位是km
					if (!_r) _r = 0;
					for (let j = i * _pointNums; j <= (i + 1) * _pointNums; j++) {
						let _ang = _angInterval * j;
						let x = center[0] + _r * Math.cos((_ang * Math.PI) / 180);
						let y = center[1] + _r * Math.sin((_ang * Math.PI) / 180);
						_coords.push([x, y]);
					}
				}
				return new Polygon([_coords]);
			} else {
				return null;
			}
		}
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
		const source = this.typhoonLayers.get(tfbh + 'layer').getSource();
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
				point.start = j === 0 ? [livePoint.lng, livePoint.lat] : [prePoint.lng, prePoint.lat];
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
			const source = this.typhoonLayers.get(tfbh + 'layer').getSource();
			for (var i = 0; i < this.forecastFeatures[tfbh].length; i++) {
				const f = this.forecastFeatures[tfbh][i];
				source.removeFeature(f);
			}
			this.forecastFeatures[tfbh] = [];
		}
	};
}

export function drawAreaPolygon(coordinates) {
	const features = [];
	coordinates.map(item => {
		const feature = new Feature({
			geometry: new Polygon(item.geometry),
			attr: item.attr,
			style: item.style
		});
		features.push(feature)
	})
	const source = new VectorSource({
		features: features,
	});
	const areaStyle = function (feat) {
		const data = feat.get("style");
		const fillColor = data.fillColor;
		const lineColor = data.lineColor;
		const style = new Style({
			stroke: new Stroke({
				color: lineColor || "rgba(124,222,0,1)",
				width: 1,
			}),
			fill: new Fill({
				color: fillColor || "rgba(124,222,0, 0.48)",
			})
		})
		return style;
	}
	const layer = new Vector({
		source: source,
		style: areaStyle,
		zIndex: 100,
	});
	return layer;
}

export function showLayerByGeoJson(jsonUrl) {
	const source = new VectorSource({
		projection: "EPSG:4326",
		url: jsonUrl,
		format: new GeoJSON(),
	});
	const layer = new Vector({
		source: source,
	});
	return layer;
}

//大屏地图测试
export function drawMapPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc;
		if (coordinates[i].qxType == 1) {
			imgSrc = require("@/assets/images/map_fxck.png");
		} else if (coordinates[i].qxType == 2) {
			imgSrc = require("@/assets/images/map_qxdu.png");
		}
		switch (coordinates[i].type) {
			case 1:
				imgSrc = require("@/assets/images/legend-rain0.png");
				break;
			case 2:
				imgSrc = require("@/assets/images/legend-rain9.png");
				break;
			case 3:
				imgSrc = require("@/assets/images/legend-rain10.png");
				break;
			case 4:
				imgSrc = require("@/assets/images/legend-rain25.png");
				break;
			case 5:
				imgSrc = require("@/assets/images/legend-rain50.png");
				break;
			case 6:
				imgSrc = require("@/assets/images/legend-rain100.png");
				break;
			case 7:
				imgSrc = require("@/assets/images/legend-rain250.png");
				break;
			default:
				imgSrc = require("@/assets/images/legend-rain0.png");
				break;
		}
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].lng, coordinates[i].lat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].lng,
				lat: coordinates[i].lat,
				name: coordinates[i].name,
				data: coordinates[i],
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
			text: new Text({
				//对齐方式
				textAlign: "center",
				//文本基线
				textBaseline: "middle",
				//字体样式
				font: "normal 14px 微软雅黑",
				//文本内容
				text: iconFeature.getProperties("featureProperties").featureProperties.name,
				//填充样式
				fill: new Fill({
					color: "#00C0FF",
				}),
				radius: 10,
				offsetX: 5,
				offsetY: -15,
				backgroundFill: new Fill({
					color: "rgba(0,0,0,0)",
				}),
				padding: [3, 6, 1, 6],
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//水利综合一张图点位

//水情
export function drawSkWaterPoint(coordinates, type) {
	// console.log(coordinates,666)
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = coordinates[i].src || require("@/assets/images/legend-pond-normal.png");
		// if (coordinates[i].warnStatus == 1) {
		//   imgSrc = require("@/assets/images/legend/map_unnormal.png");
		// } else {
		//   imgSrc = require("@/assets/images/legend/map_normal.png");
		// }
		coordinates[i].flag = "water";
		const latitude = coordinates[i].latitude || coordinates[i].stLat;
		const longitude = coordinates[i].longitude || coordinates[i].stLong;
		const iconFeature = new Feature({
			geometry: new Point([longitude, latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: longitude,
				lat: latitude,
				data: coordinates[i],
				type: type,
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [6, 6],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		zIndex: 10,
		source: vectorSource,
	});
	return vectorLayer;
}
//雨情
export function drawSkRainPoint(coordinates, type) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = coordinates[i].src;
		coordinates[i].flag = "water";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				zIndex: 10,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				data: coordinates[i],
				type: type,
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [6, 6],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
		zIndex: 10,
	});
	return vectorLayer;
}

//加载视频点位
export function drawVideoPoint(coordinates, type) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = coordinates[i].src;
		coordinates[i].flag = "video";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				zIndex: 10,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				data: coordinates[i],
				type: type,
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [27, 30],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 0.5,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
		zIndex: 10,
	});
	return vectorLayer;
}

//工程点位
export function drawProjectPoint(coordinates, type) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let longitude, latitude;
		let imgSrc = require("@/assets/images/legend/map_unnormal.png");
		switch (type) {
			case "水库":
				{
					latitude = coordinates[i].lowLeftLat || coordinates[i].lat;
					longitude = coordinates[i].lowLeftLong || coordinates[i].lng;
					imgSrc = require("@/assets/images/legend-large-normal.png");
					if (coordinates[i].overLimitVal != null && coordinates[i].overLimitVal > 0) {
						imgSrc = require("@/assets/images/legend-large-abnormal.png");
					}
				}
				break;
			case "山塘":
				{
					latitude = coordinates[i].dtMidLat || coordinates[i].latitude;
					longitude = coordinates[i].dtMidLong || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-pond-normal.png");
				}
				break;
			case "堤防":
				{
					latitude = coordinates[i].startLat || coordinates[i].latitude;
					longitude = coordinates[i].startLong || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-dike-normal.png");
				}
				break;
			case "海塘":
				{
					latitude = coordinates[i].latitude || coordinates[i].latitude;
					longitude = coordinates[i].longitude || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-seawall-normal.png");
				}
				break;
			case "泵站":
				{
					latitude = coordinates[i].pustLat || coordinates[i].latitude;
					longitude = coordinates[i].pustLong || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-pump-normal.png");
				}
				break;
			case "水闸":
				{
					latitude = coordinates[i].startLat || coordinates[i].latitude;
					longitude = coordinates[i].startLong || coordinates[i].longitude;
					imgSrc = require("@/assets/images/map-sluice-normal.png");
					if (coordinates[i].gateOpened == true) {
						imgSrc = require("@/assets/images/map-sluice-warn.png");
					}
				}
				break;
			case "闸站":
				{
					latitude = coordinates[i].latitude || coordinates[i].latitude;
					longitude = coordinates[i].longitude || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-sluice-normal.png");
				}
				break;
			case "水电站":
				{
					latitude = coordinates[i].hystLat || coordinates[i].latitude;
					longitude = coordinates[i].hystLong || coordinates[i].longitude;
					imgSrc = require("@/assets/images/legend-hydropower-normal.png");
				}
				break;
			default: {
				latitude = coordinates[i].latitude || coordinates[i].cwsLat;
				longitude = coordinates[i].longitude || coordinates[i].cwsLong;
				imgSrc = require("@/assets/images/legend-large-normal.png");
			}
		}
		const iconFeature = new Feature({
			geometry: new Point([longitude, latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: longitude,
				lat: latitude,
				data: coordinates[i],
				type: type,
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [12, 12],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
		zIndex: 10,
	});
	return vectorLayer;
}

//大屏-防汛研判-降雨分析点位
export function drawJgRainPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = require("@/assets/images/legend-rain0.png");
		if (coordinates[i].drp <= 0) {
			imgSrc = require("@/assets/images/legend/rain_0.png");
		} else if (coordinates[i].drp > 0 && coordinates[i].drp < 10) {
			imgSrc = require("@/assets/images/legend/rain_10.png");
		} else if (coordinates[i].drp >= 10 && coordinates[i].drp < 25) {
			imgSrc = require("@/assets/images/legend/rain_10_25.png");
		} else if (coordinates[i].drp >= 25 && coordinates[i].drp < 50) {
			imgSrc = require("@/assets/images/legend/rain_25_50.png");
		} else if (coordinates[i].drp >= 50 && coordinates[i].drp < 100) {
			imgSrc = require("@/assets/images/legend/rain_50_100.png");
		} else if (coordinates[i].drp >= 100 && coordinates[i].drp < 250) {
			imgSrc = require("@/assets/images/legend/rain_100_250.png");
		} else if (coordinates[i].drp >= 250) {
			imgSrc = require("@/assets/images/legend/rain_250.png");
		}
		coordinates[i].flag = "rain";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				name: coordinates[i].name,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-防汛研判-降雨分析点位
export function drawJgWaterPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc;
		if (coordinates[i].warnStatus == 1) {
			imgSrc = require("@/assets/images/legend/map_unnormal.png");
		} else {
			imgSrc = require("@/assets/images/legend/map_normal.png");
		}
		coordinates[i].flag = "water";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].stLong, coordinates[i].stLat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].stLong,
				lat: coordinates[i].stLat,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-防汛研判-水情/工程分析点位
export function drawIsDangerPoint(coordinates, flag) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc;
		if (coordinates[i].warnStatus == 1) {
			imgSrc = require("@/assets/images/legend/map_unnormal.png");
		} else {
			imgSrc = require("@/assets/images/legend/map_normal.png");
		}
		coordinates[i].flag = flag;
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].stLong, coordinates[i].stLat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].stLong,
				lat: coordinates[i].stLat,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

export function setReservoirPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc;
		if (coordinates[i].currentWaterLevel > coordinates[i].limitLevel) {
			imgSrc = require("@/assets/images/legend/chaojinshuiku.png");
		} else {
			imgSrc = require("@/assets/images/legend/zhengchangshuiku.png");
		}
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-抢险支持-防汛仓库、抢险队伍点位
export function drawQxWaterPoint(coordinates, type) {
	let vectorSource = new VectorSource({});
	let imgSrc;
	if (type == 1) {
		imgSrc = require("@/assets/images/map_fxck_long.png");
	} else if (type == 2) {
		imgSrc = require("@/assets/images/map_qxdu_long.png");
	}
	for (let i = 0; i < coordinates.length; i++) {
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				data: coordinates[i],
				type: type,
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [12, 12],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-抢险支持-调度路线起点终点点位
export function drawRoutePoint(coordinates, type) {
	let anchor = [27, 30];
	let vectorSource = new VectorSource({});
	let imgSrc;
	if (type == 1) {
		//队伍起点
		imgSrc = require("@/assets/images/route-ranks.png");
	} else if (type == 2) {
		//仓库起点
		imgSrc = require("@/assets/images/route-warehouse.png");
	} else if (type == 3) {
		anchor = [24, 24];
		//队伍终点
		imgSrc = require("@/assets/images/end-point.png");
	}
	const iconFeature = new Feature({
		geometry: new Point([coordinates[0], coordinates[1]]),
	});
	iconFeature.setProperties({
		featureProperties: {
			index: 0,
			lng: coordinates[0],
			lat: coordinates[1],
		},
	});
	iconFeature.setId(0); //设置feature id方便获取对应的feature
	const iconStyle = new Style({
		image: new Icon({
			anchor: anchor,
			anchorXUnits: "pixels", //anchorX单位
			anchorYUnits: "pixels", //anchorY单位
			src: imgSrc, //图片路径
			scale: 1,
		}),
	});
	iconFeature.setStyle(iconStyle);
	vectorSource.addFeature(iconFeature);
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-联合调度-水库地图点位
export function drawLhddPoint(coordinates, flag) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc;
		if (coordinates[i].warnStatus == 1) {
			imgSrc = require("@/assets/images/legend/map_unnormal.png");
		} else {
			imgSrc = require("@/assets/images/legend/map_normal.png");
		}
		coordinates[i].flag = flag;
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].lng, coordinates[i].lat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].lng,
				lat: coordinates[i].lat,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-洪涝分析-积水点点位
export function drawStagnantPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = require("@/assets/images/legend/legend-stagnant-normal.png");
		if (coordinates[i].data > 0) {
			imgSrc = require("@/assets/images/legend/legend-stagnant-warn.png");
		}
		coordinates[i].flag = "stagnant";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				name: coordinates[i].name,
				data: coordinates[i],
				type: "stagnant",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
			// text: new Text({
			//   text: coordinates[i].name,              // 路线标签文字
			//   font: 'normal 12px 微软雅黑',           //字体样式
			//   fill: new Fill({           //文字填充样式
			//     color: [16, 168, 218, 1]
			//   }),
			//   backgroundFill: new Fill({
			//     color: [255, 255, 255, 0],
			//   }),
			//   padding: [1, 2, 1, 2],
			// })
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

export function drawRiskPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = require("@/assets/images/legend/risk-legend-danger.png");
		if (coordinates[i].riskType == 1) {
			imgSrc = require("@/assets/images/legend/risk-legend-warn.png");
		} else if (coordinates[i].riskType == 2) {
			imgSrc = require("@/assets/images/legend/risk-legend-normal.png");
		}
		coordinates[i].flag = "risk";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				name: coordinates[i].name,
				data: coordinates[i],
				type: "risk",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-洪涝分析-测站点位
export function drawFloodStationPoint(coordinates) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < coordinates.length; i++) {
		let imgSrc = require("@/assets/images/legend/rain_10_25.png");
		if (coordinates[i].data > 0.5 && coordinates[i].data < 0.6) {
			imgSrc = require("@/assets/images/legend/rain_50_100.png");
		} else if (coordinates[i].data >= 0.6) {
			imgSrc = require("@/assets/images/legend/rain_250.png");
		}
		coordinates[i].flag = "floodStation";
		const iconFeature = new Feature({
			geometry: new Point([coordinates[i].longitude, coordinates[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: coordinates[i].longitude,
				lat: coordinates[i].latitude,
				name: coordinates[i].name,
				data: coordinates[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
			// text: new Text({
			//   text: coordinates[i].name,              // 路线标签文字
			//   font: 'normal 12px 微软雅黑',           //字体样式
			//   fill: new Fill({           //文字填充样式
			//     color: [16, 168, 218, 1]
			//   }),
			//   // backgroundFill: new Fill({
			//   //   color: [255, 255, 255, 0],
			//   // }),
			//   padding: [1, 2, 1, 2],
			// })
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-工情内涝-积水点
export function liftWaterloggingStationPoint(waterPointsData) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < waterPointsData.length; i++) {
		let imgSrc = require("@/assets/images/legend/wujishui.png");
		if (waterPointsData[i].ponding == true) {
			imgSrc = require("@/assets/images/legend/jishuidian.png");
		} else {
			imgSrc = require("@/assets/images/legend/wujishui.png");
		}
		waterPointsData[i].flag = "floodStation";
		const iconFeature = new Feature({
			geometry: new Point([waterPointsData[i].longitude, waterPointsData[i].latitude]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: waterPointsData[i].longitude,
				lat: waterPointsData[i].latitude,
				name: waterPointsData[i].name,
				data: waterPointsData[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({ title: "", source: vectorSource });
	return vectorLayer;
}

//大屏-工情内涝-河网水位
export function riverStationPoint(riverNetworkData, val) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < riverNetworkData.length; i++) {
		let imgSrc = require("@/assets/images/legend/shuiwei1.png");
		if (riverNetworkData[i].warning == true) {
			imgSrc = require("@/assets/images/legend/shuiwei2.png");
		} else {
			imgSrc = require("@/assets/images/legend/shuiwei1.png");
		}
		riverNetworkData[i].flag = "floodStation";
		const iconFeature = new Feature({
			geometry: new Point([riverNetworkData[i].stLong, riverNetworkData[i].stLat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: riverNetworkData[i].stLong,
				lat: riverNetworkData[i].stLat,
				name: riverNetworkData[i].stName,
				data: riverNetworkData[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-工情内涝-潮位
export function seaLevelStationPoint(waterStationsData) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < waterStationsData.length; i++) {
		let imgSrc = require("@/assets/images/legend/choawei.png");
		waterStationsData[i].flag = "floodStation";
		const iconFeature = new Feature({
			geometry: new Point([waterStationsData[i].stLong, waterStationsData[i].stLat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: waterStationsData[i].stLong,
				lat: waterStationsData[i].stLat,
				name: waterStationsData[i].stName,
				data: waterStationsData[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

//大屏-工情内涝-闸门
export function sluiceStationPoint(waterLoggingData) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < waterLoggingData.length; i++) {
		let imgSrc = require("@/assets/images/legend/zhamen1.png");
		if (waterLoggingData[i].warning == true) {
			imgSrc = require("@/assets/images/legend/zhamen2.png");
		} else {
			imgSrc = require("@/assets/images/legend/zhamen1.png");
		}
		waterLoggingData[i].flag = "floodStation";
		const iconFeature = new Feature({
			geometry: new Point([waterLoggingData[i].stLong, waterLoggingData[i].stLat]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: waterLoggingData[i].stLong,
				lat: waterLoggingData[i].stLat,
				name: waterLoggingData[i].stName,
				data: waterLoggingData[i],
				type: "stationPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [0, 0],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 1,
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

export function drawCircleInMeter(center, rds, mapObj) {
	let vectorSource = new VectorSource({});
	const radius =
		rds /
		mapObj
			.getView()
			.getProjection()
			.getMetersPerUnit();
	const circle = new GeoCircle(center, radius);
	const circleFeature = new Feature({
		geometry: circle,
	});
	vectorSource.addFeature(circleFeature);
	let vectorLayer = new Vector({
		source: vectorSource,
		style: new Style({
			fill: new Fill({
				color: "rgba(124,222,0, 0.48)",
			}),
			stroke: new Stroke({
				color: "rgba(124,222,0,1)",
				width: 2,
			}),
		}),
	});
	return vectorLayer;
}

//
export function showTestPoint(data) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < data.length; i += 50) {
		// console.log(data[i].geometry.x,data[i].geometry.y,data[i].attributes.value);
		const iconFeature = new Feature({
			geometry: new Point([data[i].geometry.x, data[i].geometry.y]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: data[i].geometry.x,
				lat: data[i].geometry.y,
				data: data[i],
				type: "textPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		let iconText = data[i].attributes.value ? data[i].attributes.value.toFixed(1) : 0;
		const iconStyle = new Style({
			image: new Circle({
				radius: 0,
			}),
			text: new Text({
				text: iconText, // 路线标签文字
				font: "normal 12px 微软雅黑", //字体样式
				fill: new Fill({
					//文字填充样式
					color: "#888",
				}),
				backgroundFill: new Fill({
					color: [255, 255, 255, 0],
				}),
				padding: [1, 2, 1, 2],
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
		zIndex: 1000,
	});
	return vectorLayer;
}

export function showPosPoint(data) {
	let vectorSource = new VectorSource({});
}

export function showTestClusterPoint(data) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < data.length; i++) {
		const iconFeature = new Feature({
			geometry: new Point([data[i].geometry.x, data[i].geometry.y]),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: data[i].geometry.x,
				lat: data[i].geometry.y,
				data: data[i],
				type: "textPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		let iconText = data[i].attributes.value ? data[i].attributes.value.toFixed(1) : 0;
		const iconStyle = new Style({
			image: new Circle({
				radius: 0,
			}),
			text: new Text({
				text: iconText, // 路线标签文字
				font: "normal 12px 微软雅黑", //字体样式
				fill: new Fill({
					//文字填充样式
					color: "#888",
				}),
				backgroundFill: new Fill({
					color: [255, 255, 255, 0],
				}),
				padding: [1, 2, 1, 2],
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}

	//创建聚合源
	let clusterSource = new Cluster({
		distance: parseInt(20, 20), // 两个点聚合的间距
		source: vectorSource, // 点状要素生成的矢量数据源
	});

	let clusters = new Vector({
		source: clusterSource,
		style: (feature) => {
			let featureItems = feature.get("features");
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
					radius: 0,
				}),
				text: new Text({
					text: max + "", // 路线标签文字
					font: "normal 12px 微软雅黑", //字体样式
					fill: new Fill({
						//文字填充样式
						color: "#888",
					}),
					backgroundFill: new Fill({
						color: [255, 255, 255, 0],
					}),
					padding: [1, 2, 1, 2],
				}),
			});
			return style;
		},
		zIndex: 100,
	});
	return clusters;
}

//概化图点位
export function showGeneralizaPoint(pointData) {
	let vectorSource = new VectorSource({});
	for (let i = 0; i < pointData.length; i++) {
		let imgSrc = "";
		switch (pointData[i].type) {
			case "潮位":
				{
					imgSrc = require("@/assets/images/generalPoints/general-chaowei.png");
					// pointData[i].props = {
					//     '水位': '-m',
					// }
				}
				break;
			case "小型水库":
				{
					imgSrc = require("@/assets/images/generalPoints/general-sk.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					//     '入库流量': '-m³/s',
					//     '出库流量': '-m³/s',
					// }
				}
				break;
			case "中型水库":
				{
					imgSrc = require("@/assets/images/generalPoints/general-sk.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					//     '入库流量': '-m³/s',
					//     '出库流量': '-m³/s',
					// }
				}
				break;
			case "大型水库":
				{
					imgSrc = require("@/assets/images/generalPoints/general-skbig.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					//     '入库流量': '-m³/s',
					//     '出库流量': '-m³/s',
					// }
				}
				break;
			case "水库":
				{
					imgSrc = require("@/assets/images/generalPoints/general-sk.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					//     '入库流量': '-m³/s',
					//     '出库流量': '-m³/s',
					// }
				}
				break;

			case "水闸":
				{
					imgSrc = require("@/assets/images/generalPoints/general-gate.png");
					// pointData[i].props = {
					//     '闸上水位': '-m',
					//     '闸下水位': '-m',
					//     '过闸流量': '-m³/s',
					// }
				}
				break;
			case "水文":
				{
					imgSrc = require("@/assets/images/generalPoints/general-hydrology.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					// }
				}
				break;
			case "排涝工程":
				{
					imgSrc = require("@/assets/images/generalPoints/general-drain.png");
					// pointData[i].props = {
					//     '水位': '-m',
					//     '流量': '-m³/s',
					// }
				}
				break;
		}
		const iconFeature = new Feature({
			geometry: new Point(pointData[i].coordinates),
		});
		iconFeature.setProperties({
			featureProperties: {
				index: i,
				lng: pointData[i].coordinates[0],
				lat: pointData[i].coordinates[1],
				name: pointData[i].text,
				data: pointData[i],
				type: "generalPoint",
			},
		});
		iconFeature.setId(i); //设置feature id方便获取对应的feature
		const iconStyle = new Style({
			image: new Icon({
				anchor: [16, 16],
				anchorXUnits: "pixels", //anchorX单位
				anchorYUnits: "pixels", //anchorY单位
				src: imgSrc, //图片路径
				scale: 0.7,
			}),
			text: new Text({
				//对齐方式
				textAlign: "center",
				//文本基线
				textBaseline: "middle",
				//字体样式
				font: "normal 14px 微软雅黑",
				//文本内容
				text: pointData[i].text,
				//填充样式
				fill: new Fill({
					color: "#fff",
				}),
				radius: 10,
				offsetY: -25,
				backgroundFill: new Fill({
					color: "rgba(0,0,0,0)",
				}),
				padding: [3, 6, 1, 6],
			}),
		});
		iconFeature.setStyle(iconStyle);
		vectorSource.addFeature(iconFeature);
	}
	let vectorLayer = new Vector({
		title: "",
		source: vectorSource,
	});
	return vectorLayer;
}

/*
	CAD智能对比地暂时方法
*/
export function addWKTDataLayer(WKTString) {
	// addWKTDataLayer(WKTString) {
	//创建WKT对象
	let format = new WKT();
	//读取数据
	let feature = format.readFeature(WKTString, {
		dataProjection: 'EPSG:4326',
		featureProjection: 'EPSG:4326'
	});
	//将图层数据添加进图层
	let vectorWKTLayer = new Vector({
		source: new VectorSource({
			features: [feature]
		})
	});
	//返回图层
	return vectorWKTLayer;
}