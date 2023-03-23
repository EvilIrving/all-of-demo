<template>
	<div class="map-wrap">
		<!-- :class="[isBigScrren && 'is-big-screen']" -->
		<div class="map" id="MainView" />

		<!-- 影像底图切换时间栏 -->
		<div :style="timelineStyle" class="playable-timeline-bar is-big-screen" v-if="timelinebarShow">
			<playable-timeline-bar
				dark
				ref="playTimeLine"
				:timeline-list="timelineList"
				:before-play-next="playNext"
				@change="handleTimelineChange"
				:defaultIndex="defaultIndex"
				:hidePlay="true"
			/>
		</div>

		<screen-footer :map-bind="map" />
		<!-- 点位悬浮框 -->
		<div v-show="popupShow || overlayEnterFlag" id="popup" class="ol-popup">
			<!-- , isBigScrren && 'is-big-screen' -->
			<div :class="['ol-popup__wrapper']">
				<div class="ol-popup__title">{{ featureInfo.name }}</div>
				<div class="ol-popup__content">
					<div class="ol-popup__content--props">
						<div :key="key" v-for="(prop, key) of featureInfo.props" class="props-item">
							<span class="item-label">{{ key }}</span>
							<span class="item-data" :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }">
								{{ prop == "null" ? "-" : prop == "null m" ? "- m" : prop }}
							</span>
						</div>
						<div v-if="featureInfo.label" class="props-label">
							<span> {{ featureInfo.label }}</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 天气实况悬浮框 -->
		<div
			v-for="(item, index) in weatherInfoList"
			v-show="item.show"
			:key="index"
			:id="`weather-popup-${index}`"
			class="ol-weather-popup"
			@click="handleWeatherPopupClick(item)"
		>
			<div>
				<div class="ol-weather-popup__content">
					<div class="weather-icon">
						<i :class="item.icon" />
					</div>
					<div class="weather-label">
						<div class="label-name">
							<span>{{ item.areaName }}</span>
							<span>{{ item.temperature }}℃</span>
						</div>
						<span class="weather">{{ item.weather }}</span>
					</div>
				</div>
			</div>
			<div class="weather-details">
				<!-- <p style="margin-bottom: 8px">温州国家气象站 (58659)</p> -->
				<div>
					<p>
						<i class="icon-waether-pre-pro" style="vertical-align: middle; margin-right: 4px" />
						<span>{{ item.humidity || "-" }}%</span>
					</p>
					<p>
						<i class="icon-weather-pre" style="vertical-align: middle; margin-right: 4px" />
						<span>{{ item.rain || "-" }}mm</span>
					</p>
				</div>
				<div>
					<p>
						<i class="icon-weather-air-pre" style="vertical-align: middle; margin-right: 4px" />
						<span>{{ item.airPressure || "-" }}hPa</span>
					</p>
					<p>
						<i class="icon-weather-visibility" style="vertical-align: middle; margin-right: 4px" />
						<span>{{ item.visibility || "-" }}m</span>
					</p>
				</div>
				<p style="vertical-align: middle; margin-top: 7px">
					<i class="icon-footer-wind" style="vertical-align: middle; margin-right: 4px" />
					<span style="margin-right: 4px">{{ item.winds }}</span>
					<span>{{ item.currentWindSpeed }}m/s</span>
				</p>
			</div>
		</div>
	</div>
</template>

<script>
import esri2geo from "esri2geo";
import { Fill, Stroke, Style, Circle } from "ol/style";
import "ol/ol.css";
import XYZ from "ol/source/XYZ";
import WKT from "ol/format/WKT";
import TileLayer from "ol/layer/Tile";
import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";
import { Map, View, Overlay, Feature } from "ol";
import { defaults as defaultControls } from "ol/control";
import { Vector as VectorSource, TileArcGISRest } from "ol/source";
import { Vector as VectorLayer } from "ol/layer";
import { LineString } from "ol/geom";
import GeoJSON from "ol/format/GeoJSON";
import mapConfig from "@/config/mapConfig";
import { fromLonLat } from "ol/proj";
import { ScreenFooter, PlayableTimelineBar } from "@/components";
import {
	drawClusterPoint,
	drawPoint,
	addLayerClick,
	Typhoon,
	showTestPoint,
	showTestClusterPoint,
	drawLineEx,
	drawPolygon,
	drawHighLines,
	drawHighLightLine
} from "@/utils/mapUtil.js";
import { queryMapserver } from "@/api/loadMapServe";
import { mapGetters, mapActions } from "vuex";

export default {
	name: "ScreenMap",
	components: { ScreenFooter, PlayableTimelineBar },
	props: {},
	data() {
		return {
			//地图
			map: null,
			//点位信息
			featureInfo: {},
			popupShow: false,
			isohyetLayer: null,
			typhoonLayers: [],
			typhoonNames: [],
			typhoonTimes: [],
			textLayer: null,
			lineLayer: [],
			weatherInfoList: [],
			weatherOverlay: [],
			areaLayer: null,
			basinLayer: null,
			timelineList: [
				{ label: "2010", value: 2010 },
				{ label: "2012", value: 2012 },
				{ label: "2014", value: 2014 },
				{ label: "2016", value: 2016 },
				{ label: "2017", value: 2017 },
				{ label: "2018", value: 2018 },
				{ label: "2019", value: 2019 }
			],
			defaultIndex: 6,
			areaWKTLayer: null,
			lineFocusLayer: [],
			dynamicPointMatain: {},
			overlayEnterFlag: false
		};
	},
	created() {},
	mounted() {
		this.mapInit();
	},
	computed: {
		...mapGetters(["weatherPopover", "currentScene", "timelinebarShow", "currentScene"]),
		isBigScrren() {
			return this.$route.query.large == "true";
		},
		timelineStyle() {
			return this.currentScene == "防御复盘" ? { bottom: "200px" } : { bottom: "100px" };
		}
	},
	watch: {
		currentScene: {
			immediate: true,
			handler(val) {
				this.clearTyphoonLayer();
			}
		},
		timelinebarShow(val) {
			const layer = new TileLayer({
				id: "wz2019",
				zIndex: 99,
				source: new TileArcGISRest(
					{
						url:
							"http://10.36.161.208:16080/wzgt22/754677ad50484012ba9a9ccb0946aa18/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2019/MapServer"
					},
					{ zoomOffset: 1 }
				)
			});
			if (val) {
				this.map.addLayer(layer);
			} else {
				this.map.removeLayer(layer);
			}
		},
		"$store.state.analyseData": function(val) {
			// console.log(val,333)
			if (val == "custom") {
				this.clearTyphoonLayer();
				this.$store.commit("typhoonDataList", {});
			}
		}
	},
	methods: {
		...mapActions(["setWeatherPopover", "changeArea", "changeTimelinebarShow"]),
		/** 天气卡片点击 */
		handleWeatherPopupClick(item) {
			setTimeout(() => {
				this.setWeatherPopover(true);
			}, 300);
			this.changeArea(item.areaName);
		},
		/**
		 * 初始化地图
		 */
		mapInit() {
			//地图填充图层
			let SLTCLayer = new TileLayer({
				id: "SLTCLayer",
				source: new XYZ({
					url: "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wenzhouTC/MapServer/tile/{z}/{y}/{x}",
					projection: "EPSG:4326"
				}),
				visible: true
			});
			//地图主图层
			let mainLayer = new TileLayer(
				{
					id: "zhejiangmainSLLayer",
					source: new XYZ({
						url: "https://sldtptgis.zjwater.com/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
						projection: "EPSG:4326"
					})
				},
				{ zoomOffset: 1 }
			);
			let SLMCLayer = new TileLayer({
				id: "zhejiangmainSLMCLayer",
				zIndex: 2,
				source: new XYZ({
					/* "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wzmengc/MapServer/tile/{z}/{y}/{x}" */
					url: "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/newwzmcls/MapServer/tile/{z}/{y}/{x}",
					projection: "EPSG:4326"
				})
			});

			// view设置
			let viewSetting = new View({
				projection: "EPSG:4326",
				center: [120.527, 27.86],
				zoom: 9.5,
				maxZoom: 18,
				minZoom: 8,
				extent: [118.245, 26.51, 122.752, 29.45]
			});
			this.map = new Map({
				target: "MainView",
				controls: defaultControls({ zoom: false }).extend([]),
				layers: [SLTCLayer, mainLayer, SLMCLayer],
				view: viewSetting
			});
			// this.drawPoint();
			this.registeredEvent(); //注册事件
			this.addStationOverlay();
		},
		/**
		 * 设置摄像机位置
		 */
		setView(lng, lat) {
			const view = this.map.getView();
			view.animate({ center: [lng, lat], duration: 200, zoom: 14 });
		},
		addStationOverlay() {
			this.overlay = new Overlay({
				//设置弹出框的容器
				element: document.querySelector("#popup"),
				autoPan: true,
				autoPanAnimation: {
					duration: 250
				}
			});
			this.map.addOverlay(this.overlay);
		},
		addWeatherOverlay(coodinateList) {
			this.weatherInfoList = [];
			for (let item of coodinateList) {
				this.weatherInfoList.push({ ...item, show: true });
			}
			// console.log(999, this.weatherInfoList);
			this.$nextTick(() => {
				this.weatherInfoList.forEach((item, index) => {
					this.weatherOverlay[index] = new Overlay({
						//设置弹出框的容器
						element: document.querySelector(`#weather-popup-${index}`),
						positioning: "bottom-center",
						autoPan: true,
						autoPanAnimation: {
							duration: 250
						}
					});
					this.map.addOverlay(this.weatherOverlay[index]);
					this.weatherOverlay[index].setPosition(item.coodinate);
				});
			});
		},
		/**
		 * 移除所有图层
		 */
		removeAllLayer() {
			// 忽略的图层列表：按name属性
			const list = ["MainMaplayer", "wenzhouRiverLayer"];
			this.map.getLayers().forEach(element => {
				if (list.includes(element.get("name"))) {
					this.map.removeLayer(element);
				}
			});
		},
		//加载等雨量面
		showIsohyetLayer(mapImage) {
			this.removeLayerByName("rainfall");
			this.isohyetLayer = new ImageLayer({
				name: "rainfall",
				source: new ImageStatic({
					url: mapImage.href, //这里添加静态图片的地址
					projection: "EPSG:4326",
					imageExtent: [119.53839579772948, 26.979173080444387, 121.33689579772948, 28.77767308044439] //WGS-84 经纬度数据范围
				}),
				opacity: 0.5,
				zIndex: 10
			});
			this.map.addLayer(this.isohyetLayer);
		},
		/**
		 * 移除指定图层
		 * @param {String} layerName 指定的图层名
		 */
		removeLayerByName(layerName) {
			this.map.getLayers().forEach(element => {
				if (element && element.get("name") == layerName) {
					this.map.removeLayer(element);
				}
			});
		},
		/**
		 * 检验指定图层是否存在
		 */
		isLayerExist(layerName) {
			let flag = false;
			this.map.getLayers().forEach(element => {
				if (element && element.get("name") == layerName) {
					flag = true;
				}
			});
			return flag;
		},
		/**
		 * 注册事件
		 */
		registeredEvent() {
			this.map.on("singleclick", event => {
				// console.log(2222, event);
			});
			this.map.on("pointermove", event => {
				// console.log(this.map.getTargetElement());
				// console.log(event);
				let pixel = this.map.getEventPixel(event.originalEvent);
				let flag = false;
				this.map.getTargetElement().style.cursor = "auto";
				this.map.forEachFeatureAtPixel(pixel, feature => {
					let coodinate = event.coordinate;
					let properties = feature.getProperties();
					if (feature && properties) {
						this.map.getTargetElement().style.cursor = "pointer";
						// if(properties.data){
						//   this.featureInfo = {
						//     name: properties.data.name,
						//     props: properties.data.props,
						//     label: properties.data.label || false,
						//   };
						//   if (this.featureInfo) {
						//     this.overlay.setPosition(coodinate);
						//     flag = true;
						//   }
						// }
						//聚合时 只展示一个点信息
						if (properties.features && properties.features.length == 1) {
							this.featureInfo = {
								name: properties.features[0].values_.data.name,
								props: properties.features[0].values_.data.props,
								label: properties.features[0].values_.data.label || false
							};
							if (this.featureInfo) {
								this.overlay.setPosition(coodinate);
								flag = true;
							}
						} else if (properties.data) {
							//非聚合点位时显示信息
							this.featureInfo = {
								name: properties.data.name,
								props: properties.data.props,
								label: properties.data.label || false
							};
							if (this.featureInfo) {
								this.overlay.setPosition(coodinate);
								flag = true;
							}
							if (properties.data.no_pointermove) {
								flag = false;
							}
						}
					}
				});
				if (flag) {
					this.popupShow = true;
				} else {
					this.popupShow = false;
				}
			});
		},
		/**
		 * 画点并将图层点位绑定事件
		 * @param {Array} coordinate 点位列表
		 * @param {Object} styleObj 样式对象
		 * @param {String} layerName 点位图层名称，用于分辨
		 */
		drawPoint(coordinates, styleObj, layerName = "pointLayer") {
			if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);

			let pointLayer = drawPoint(coordinates, styleObj);
			pointLayer.set("name", layerName);
			pointLayer.setZIndex(10);
			this.map.addLayer(pointLayer);
			let selectClick = addLayerClick(pointLayer);
			selectClick.on("select", e => {
				let features = e.target.getFeatures().getArray();
				if (features.length > 0) {
					this.$emit("point-click", features[0].values_.data);
				}
			});
			this.map.addInteraction(selectClick);
		},
		drawDynamicPoint(coordinates, typeName) {
			// console.log(coordinates);
			if (this.dynamicPointMatain[typeName]) {
				this.clearDynamicPoint(typeName);
			}
			this.dynamicPointMatain[typeName] = [];
			let mapDom = document.getElementById("MainView");
			coordinates.map((item, index) => {
				let pointDom = document.createElement("div");
				pointDom.id = `${typeName + index}`;
				pointDom.className = typeName;
				let childDom = document.createElement("div");
				childDom.className = "dynamic-child";
				pointDom.appendChild(childDom);
				mapDom.appendChild(pointDom);

				pointDom.addEventListener("pointermove", () => {
					this.featureInfo = {
						name: item.name,
						props: item.props,
						label: item.label || false,
						...item
					};
					if (this.featureInfo) {
						this.overlay.setPosition([item.lng, item.lat]);
						this.overlayEnterFlag = true;
					}
				});

				pointDom.addEventListener("click", () => {
					const data = {
						...item
					};
					// console.log(data)
					this.$emit("point-click", data);
				});

				pointDom.addEventListener("mouseout", () => {
					this.overlayEnterFlag = false;
				});

				let marker = new Overlay({
					position: [item.lng, item.lat],
					stopEvent: false,
					positioning: "center-bottom",
					element: document.getElementById(`${typeName + index}`),
					offset: [-25, -25] //根据图标大小设置偏移位置
				});
				this.dynamicPointMatain[typeName].push(marker);
				this.map.addOverlay(marker);
				marker.on("pointermove", event => {
					// console.log(event);
				});
			});
		},
		clearDynamicPoint(typeName) {
			if (typeName) {
				if (this.dynamicPointMatain[typeName]) {
					this.dynamicPointMatain[typeName].forEach(item => {
						this.map.removeOverlay(item);
					});
					// this.dynamicPointMatain[typeName];
					delete this.dynamicPointMatain[typeName];
					let dom = document.getElementsByClassName(typeName);
					if (dom.length > 0) {
						dom.forEach(item => {
							dom.parentNode.removeChild(item);
						});
					}
				}
			} else {
				//清除全部动态点位
				for (let key in this.dynamicPointMatain) {
					this.dynamicPointMatain[key].forEach(child => {
						this.map.removeOverlay(child);
					});
					let dom = document.getElementsByClassName(key);
					if (dom.length > 0) {
						dom.forEach(item => {
							dom.parentNode.removeChild(item);
						});
					}
				}
				this.dynamicPointMatain = {};
			}
		},
		clearAreaWKTLayer() {
			if (this.areaWKTLayer != null) {
				this.map.removeLayer(this.areaWKTLayer);
				this.areaWKTLayer = null;
			}
		},
		drawAreaWKT(arr) {
			// console.log(arr,333);
			this.clearAreaWKTLayer();
			var format = new WKT();
			var source = new VectorSource({ wrapX: false });
			for (let i = 0; i < arr.length; i++) {
				if (arr[i].coordinates) {
					var feature = format.readFeature(arr[i].coordinates);
					feature.data = arr[i];
					source.addFeature(feature);
				}
			}
			let styleFunction = function(feature) {
				let styles = [];
				let type = feature.data.lvl;
				let color = "#E8B005";
				let rgba = "rgba(232,176,5,0.5)";
				if (type == "1") {
					color = "#F39100";
					rgba = "rgba(243,145,0,0.5)";
				} else {
					color = "#F10000";
					rgba = "rgba(241,0,0,0.5)";
				}
				styles.push(
					new Style({
						stroke: new Stroke({
							color: color,
							width: 2
						}),
						fill: new Fill({
							color: rgba
						})
					})
				);
				return styles;
			};
			this.areaWKTLayer = new VectorLayer({
				source: source,
				style: styleFunction,
				zIndex: 1001
			});
			this.map.addLayer(this.areaWKTLayer);
		},
		/**
		 * 画聚合点位并将图层点位绑定事件
		 * @param {Array} coordinate 点位列表
		 * @param {Object} styleObj 样式对象
		 * @param {String} layerName 点位图层名称，用于分辨
		 */
		drawClusterPoint(coordinates, styleObj, layerName = "pointLayer") {
			if (this.isLayerExist(layerName)) this.removeLayerByName(layerName);
			let pointLayer = drawClusterPoint(coordinates, styleObj);
			pointLayer.set("name", layerName);
			pointLayer.setZIndex(10);
			this.map.addLayer(pointLayer);
			let selectClick = addLayerClick(pointLayer);
			selectClick.on("select", e => {
				let features = e.target.getFeatures().getArray()[0].values_.features;
				if (features.length == 1) {
					this.$emit("point-click", features[0].values_.data);
				}
			});
			this.map.addInteraction(selectClick);
		},
		/**
		 * 通用使用数据中的 coorpot 属性画线
		 * @param {{coorpot:string}[]} dataList
		 * @param {string} type
		 */
		drawCoorpotLine(dataList, type, style = { lineWidth: 4, lineColor: "#e80000" }) {
			console.log(dataList);
			for (let data of dataList) {
				const converter = document.createElement("div");
				converter.innerHTML = data.coorpot ? data.coorpot : "[]";
				const lineString = decodeURIComponent(converter.innerText)
					.replaceAll(/\\/g, "")
					.replaceAll(/^"(.+)"$/g, "$1");
				let lineList = eval(lineString);

				lineList = lineList.map(line => {
					return {
						lineList: line.map(item => [item.lng, item.lat]),
						data: {
							type
						}
					};
				});
				if (typeof style == "object") {
					this.drawLineEx(lineList, style);
				} else if (typeof style == "function") {
					this.drawLineEx(lineList, style(data));
				}
			}
		},
		/**
		 * drawLine
		 */
		drawLineEx(coordinates, styleObj = {}) {
			if (!coordinates.length > 0) return;
			let layer = drawLineEx(coordinates, styleObj);
			layer.set("name", layerName);
			this.lineLayer.push(layer);
			layer.set("name", "lineLayer");
			layer.setZIndex(10);
			this.map.addLayer(layer);
			let selectClick = addLayerClick(layer);
			selectClick.on("select", e => {
				let features = e.target.getFeatures().array_;
				// console.log(e.target.getFeatures(),555)
				if (e.target.getFeatures().array_.length == 1) {
					this.$emit("point-click", features[0].values_.data);
				}
			});
			this.map.addInteraction(selectClick);
		},

		removeLineLayer() {
			this.lineLayer.forEach(item => {
				this.map.removeLayer(item);
			});
		},
		drawLineFocus(coordinates, styleObj = {}) {
			if (!coordinates.length > 0) return;
			let layer = drawLineEx(coordinates, styleObj);
			this.lineFocusLayer.push(layer);
			layer.set("name", "focuLineLayer");
			layer.setZIndex(10);
			this.map.addLayer(layer);
		},
		removeFocusLineLayer() {
			this.lineFocusLayer.forEach(item => {
				this.map.removeLayer(item);
			});
		},
		showTyphoon(data) {
			// this.clearTyphoonLayer();
			// this.toggleShadeLayer("remove", this.shadeLayer);
			// console.log(data, 44444);
			// if()
			// if (this.shadeFlag) {
			//   this.shadeLayer = false;
			//   this.toggleShadeLayer("remove", this.shadeLayer);
			//   this.toggleShadeLayer("add", this.textLayer);
			// }
			const typhoon = new Typhoon(this.map, data, this.typhoonLayers, this.typhoonNames, this.typhoonTimes, this);
			typhoon.init();
		},
		clearTyphoonLayer() {
			if (this.typhoonLayers.length > 0) {
				this.typhoonLayers.map(item => {
					this.map.removeLayer(item);
				});
				this.typhoonLayers = [];
			}
			if (this.typhoonNames.length > 0) {
				this.typhoonNames.map(item => {
					this.map.removeOverlay(item);
				});
				this.typhoonNames = [];
			}
			if (this.typhoonTimes.length > 0) {
				this.typhoonTimes.map(item => {
					clearInterval(item);
				});
				this.typhoonTimes = [];
			}
		},
		_showTestPoint(data) {
			this.clearTextLayer();
			this.textLayer = showTestClusterPoint(data);
			this.map.addLayer(this.textLayer);
		},
		clearTextLayer() {
			if (this.textLayer != null) {
				this.map.removeLayer(this.textLayer);
				this.textLayer = null;
			}
		},
		//根据行政区划名称查询行政区划边界 lineFlag为true时加载边界线 不传加载面
		showAreaLine(areaName, lineFlag) {
			this.clearAreaLayer();
			const searchAreaList = [
				"鹿城区",
				"龙湾区",
				"瓯海区",
				"洞头区",
				"永嘉县",
				"平阳县",
				"苍南县",
				"文成县",
				"泰顺县",
				"瑞安市",
				"乐清市",
				"龙港市"
			];
			//对应searchAreaList 每个行政区划的定位中心点
			const centerPoint = [
				[120.55389798474596, 28.068232997940203],
				[120.81894315076158, 27.89931820301833],
				[120.54703152966783, 27.963862880752703],
				[121.03317654919908, 27.919917568252703],
				[120.66376126599596, 28.337398037002703],
				[120.36143482403382, 27.64214415802697],
				[120.44959860797451, 27.440954047283753],
				[120.00699559080968, 27.82237370619345],
				[119.85989517628137, 27.524267556397135],
				[120.43658087217555, 27.839296762732104],
				[121.01717188880941, 28.276692685577352],
				[120.57336116457707, 27.55135084435864]
			];
			const areaIndex = searchAreaList.indexOf(areaName);
			if (areaIndex != -1) {
				const searchUrl = "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/wenzhou_fangxunend/MapServer/3/query";
				const opt = {
					where: "NAME='" + areaName + "'",
					outFields: "*",
					f: "pjson"
				};
				queryMapserver(searchUrl, opt).then(res => {
					let response = res.features;
					const coordinates = response[0].geometry.rings;
					this.areaLayer = drawPolygon(coordinates, {
						fillColor: "rgba(0,0,0,0)",
						lineColor: "#FF551F",
						lineDash: [6],
						lineWidth: 4
					});
					//this.areaLayer = drawPolygon(coordinates, {});
					// this.areaLayer = drawPolygon(coordinates, {});
					this.map.addLayer(this.areaLayer);
					// if(lineFlag){
					//   let highlightLayer = drawHighLightLine(this.areaLayer,this.map);
					//   highlightLayer.setStyle(() => {
					//       return new Style({
					//           fill: new Fill({ color: (response[0].style && response[0].style.getFill) ? response[0].style.getFill().getColor() : '#aaa' }),
					//           stroke: new Stroke({ color: 'rgba(255, 255, 255, 0.2)', width: 3 })
					//       });
					//   });
					//   // console.log(response[0],2233)
					//   const plygon = new LineString(coordinates);
					//   const feature = new Feature({
					//       geometry: plygon,
					//   });
					//   highlightLayer.getSource().addFeature(feature);
					// }
				});
				this.flyPoint(centerPoint[areaIndex], 10);
			} else {
				const searchUrl =
					"http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_fangxun/MapServer/2/query";
				const opt = {
					where: "NAME='" + areaName + "'",
					outFields: "*",
					f: "pjson"
				};
				queryMapserver(searchUrl, opt).then(res => {
					let response = res.features;
					const coordinates = response[0].geometry.rings;
					this.areaLayer = drawPolygon(coordinates, {
						fillColor: "rgba(91,169,252, 0.6)",
						lineColor: "rgba(91,169,252, 0.6)"
					});
					this.map.addLayer(this.areaLayer);
				});
				this.flyPoint(centerPoint[areaIndex], 9.5);
			}
		},
		//定位到对应位置和层级
		flyPoint(center, zoom) {
			const view = this.map.getView();
			view.animate({
				center: center,
				duration: 300,
				zoom: zoom
			});
		},
		clearAreaLayer() {
			if (this.areaLayer != null) {
				this.map.removeLayer(this.areaLayer);
				this.areaLayer = null;
			}
		},
		/** 流域切换 */
		basinCommand(command) {
			if (command == "全流域") {
				if (this.basinLayer != null) {
					this.map.removeLayer(this.basinLayer);
					this.basinLayer = null;
					this.fullMap();
				}
				return;
			}
			queryMapserver(
				"/温州流域/MapServer/0/query",
				{
					where: "WRNM='" + command + "'",
					f: "json"
				},
				true
			).then(res => {
				if (res.features && res.features.length > 0) {
					esri2geo(res, (err, data) => {
						//加载温州流域图层
						let vectorSource = new VectorSource({
							features: new GeoJSON().readFeatures(data)
						});
						if (this.basinLayer != null) {
							this.map.removeLayer(this.basinLayer);
							this.basinLayer = null;
						}
						this.basinLayer = new VectorLayer({
							source: vectorSource,
							style: new Style({
								stroke: new Stroke({
									color: "#FF00E7",
									lineDash: [6],
									width: 4
								}),
								fill: new Fill({
									color: "rgba(0, 0, 255, 0)"
								})
							}),
							zIndex: 1000
						});
						this.map.addLayer(this.basinLayer);
						this.map.getView().fit(
							this.basinLayer
								.getSource()
								.getFeatures()[0]
								.getGeometry(),
							{
								padding: [100, 300, 0, 200]
							}
						);
					});
				}
			});
		},
		fullMap() {
			let view = this.map.getView();
			view.animate({
				center: [mapConfig.lng, mapConfig.lat],
				duration: 300,
				zoom: mapConfig.zoom
			});
		},
		playNext() {
			return new Promise(resolve => {
				resolve();
			});
		},
		removeYxLayers() {
			const yxArr = ["wz2010", "wz2012", "wz2014", "wz2016", "wz2017", "wz2018", "wz2019"];
			this.map.getLayers().forEach(element => {
				let layerId = element.get("id");

				if (yxArr.includes(layerId)) {
					this.map.removeLayer(element);
				}
			});
		},
		handleTimelineChange(idx) {
			//温州历年影像图层
			const yxLayers = {
				2010: new TileLayer({
					id: "wz2010",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/1fec7dfbdc744c2d9bc25e7f2b2043e8/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2010/MapServer"
						},
						{ zoomOffset: 1 }
					)
				}),
				2012: new TileLayer({
					id: "wz2012",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/57d3b2406d5643e6a3646d0f3942a0a3/arcgis/rest/services/HISIMG/WZ2000_SPOT5_2012/MapServer"
						},
						{ zoomOffset: 1 }
					)
				}),
				2014: new TileLayer({
					id: "wz2014",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/4c687988428d4261aeb49caa87a8bcd5/arcgis/rest/services/HISIMG/WZ2000_DOM_2014/MapServer"
						},
						{ zoomOffset: 1 }
					)
				}),
				2016: new TileLayer({
					id: "wz2016",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/a2511b47fcb94458995e546e64f301af/arcgis/rest/services/HISIMG/WZ2000_GF_2016_2M/MapServe"
						},
						{ zoomOffset: 1 }
					)
				}),
				2017: new TileLayer({
					id: "wz2017",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/fd7dc8e1cf7f4756b4059419ed492200/arcgis/rest/services/HISIMG/WZ2000_DOM_2017/MapServer"
						},
						{ zoomOffset: 1 }
					)
				}),
				2018: new TileLayer({
					id: "wz2018",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/563382047626443aba7e546b86c0cf4f/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2018/MapServer"
						},
						{ zoomOffset: 1 }
					)
				}),
				2019: new TileLayer({
					id: "wz2019",
					zIndex: 1,
					source: new TileArcGISRest(
						{
							url:
								"http://10.36.161.208:16080/wzgt22/754677ad50484012ba9a9ccb0946aa18/arcgis/rest/services/HISIMG/WZ2000_WORLDVIEW_2019/MapServer"
						},
						{ zoomOffset: 1 }
					)
				})
			};
			this.removeYxLayers();
			// console.log(yxLayers[this.timelineList[idx].label])
			this.map.addLayer(yxLayers[this.timelineList[idx].label]);
		}
	}
};
</script>
<style lang="scss" scoped>
.map-wrap {
	width: 100%;
	height: 100%;
	.map {
		width: 100%;
		height: 100%;
	}
	.playable-timeline-bar {
		position: absolute;
		width: 800px;
		bottom: 100px;
		left: 50%;
		height: 100px;
		transform: translateX(-50%) scaleX(0.5);
		@include flexbox;
		@include flexJC(center);
		@include flexAC;
		z-index: 100;
	}
	.ol-popup {
		position: absolute;
		bottom: 15px;
		left: -50%;
		transform: translateX(-50%);
		border-radius: 3px;
		min-width: 150px;
		.ol-popup__wrapper {
			background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
			border: 2px solid #00c1ff;
			box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
			border-radius: 3px;
			height: 100%;
			width: 100%;
			padding: 16px;
			position: relative;
			opacity: 0.9;
			&:before {
				content: "";
				position: absolute;
				display: block;
				box-sizing: content-box;
				width: 0px;
				height: 0px;
				border: 7px solid transparent;
				border-top: 7px solid #103567;
				border-bottom: 0;
				bottom: 0;
				left: 50%;
				transform: translateX(-50%) translateY(100%);
				z-index: 100;
			}
			&:after {
				content: "";
				position: absolute;
				display: block;
				box-sizing: content-box;
				width: 0px;
				height: 0px;
				border: 10px solid transparent;
				border-top: 10px solid #00c1ff;
				border-bottom: 0;
				bottom: 0;
				left: 50%;
				transform: translateX(-50%) translateY(100%);
			}
			.ol-popup__title {
				font-size: 18px;
				color: #fff;
			}
			.ol-popup__content {
				color: #fff;
				.ol-popup__content--props {
					display: flex;
					flex-direction: column;
					.props-item {
						// padding: 0 12px;
						position: relative;
						display: flex;
						align-items: center;
						min-width: 150px;
						width: max-content;
						/* &:not(:only-child) {
              flex-direction: column;
              min-width: 100px;
              &:after {
                content: "";
                position: absolute;
                height: 80%;
                right: 0;
                top: 10%;
                border-right: 1px solid #00c1ff4d;
              }
              &:first-child {
                padding-left: 0;
              }
              &:last-child {
                padding-right: 0;
                &:after {
                  border-right: 0;
                }
              }
              .item-label {
                font-size: 16px;
              }
              .item-data {
                font-size: 18px;
                color: #33ffff;
                line-height: 25px;
              }
            } */
						.item-label {
							font-size: 16px;
						}
						.item-data {
							margin-left: 8px;
							font-size: 18px;
							color: #33ffff;
							line-height: 25px;
						}
						&:first-child {
							&:not(:only-child) {
								margin-top: 8px;
							}
						}
					}
					.props-label {
						line-height: 20px;
						width: 350px;
					}
				}
			}
		}
	}
	.icon-footer-wind {
		width: 1rem;
		height: 1rem;
		margin: 0;
	}
	.ol-weather-popup {
		user-select: none;
		cursor: pointer;
		width: 200px;
		height: 160px;
		background-image: linear-gradient(180deg, #0096c3 2%, #103567 100%);
		border: 2px solid #00c1ff;
		box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
		border-radius: 3px;
		position: relative;
		opacity: 0.9;
		&:before {
			content: "";
			position: absolute;
			display: block;
			box-sizing: content-box;

			width: 0px;
			height: 0px;
			border: 7px solid transparent;
			border-top: 7px solid #103567;
			bottom: 0;
			left: 50%;
			transform: translateX(-50%) translateY(100%);
			z-index: 100;
		}
		&:after {
			content: "";
			position: absolute;
			display: block;
			box-sizing: content-box;

			width: 0px;
			height: 0px;
			border: 10px solid transparent;
			border-top: 10px solid #00c1ff;
			bottom: 0;
			left: 50%;
			transform: translateX(-50%) translateY(100%);
		}
		.weather-details {
			border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
			margin: 0 8px;
			padding-top: 8px;
			color: #fff;
			> div {
				padding: 7px 0;
				display: flex;
				justify-content: space-between;
			}
		}
		.ol-weather-popup__content {
			height: 100%;
			width: 100%;
			color: #fff;
			display: flex;
			justify-content: space-between;
			align-items: center;

			.weather-icon {
				margin-left: 16px;
				width: 48px;
				i {
					width: 32px;
					height: 32px;
				}
			}
			.weather-label {
				flex: 2;
				text-align: left;
				display: flex;
				flex-direction: column;
				line-height: 24px;
				font-size: 16px;
				margin-right: 8px;
				.label-name {
					display: flex;
					justify-content: space-between;
				}
				.weather {
					font-size: 18px;
				}
			}
		}
	}
}
</style>
<style lang="scss">
.rainrisk-dynamic {
	width: 50px;
	height: 50px;
	background: url(~assets/images/MapPoint/risk-rain.png) no-repeat center center;
	background-size: 60% 65%;
}
.flood-dynamic {
	width: 50px;
	height: 50px;
	background: url(~assets/images/MapPoint/torrents_higher.png) no-repeat center center;
	background-size: 60% 65%;
}
.reservoir-dynamic {
	width: 50px;
	height: 50px;
	background: url(~assets/images/MapPoint/reservoir-fault.png) no-repeat center center;
	background-size: 60% 65%;
}
.dynamic-child {
	background-color: red;
	border-radius: 100%;
	-webkit-animation: scaleout 1s infinite ease-in-out;
	animation: scaleout 1s infinite ease-in-out;
	width: 50px;
	height: 50px;
}
// background: url(~assets/images/MapPoint/risk-rain.gif) no-repeat center center;
// background-size:100% 100%;
@-webkit-keyframes scaleout {
	0% {
		-webkit-transform: scale(0);
	}
	100% {
		-webkit-transform: scale(1);
		opacity: 0;
	}
}
@keyframes scaleout {
	0% {
		transform: scale(0);
		-webkit-transform: scale(0);
	}
	100% {
		transform: scale(1);
		-webkit-transform: scale(1);
		opacity: 0;
	}
}
</style>
