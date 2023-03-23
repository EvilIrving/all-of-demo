<!--
 * @Author: wupengfei
 * @LastEditTime: 2020-10-09 10:29:01
 * @LastEditors: wupengfei
 * @Descripttion: 
 * @FilePath: \ruiAnWaterPlatform\src\components\primary_map\index.vue
-->
<template>
  <div class="wrap">
    <div class="map" id="map">
      <slot></slot>
      <el-button class="clear-button" @click="clearMap" v-if="isDraw"
        >清除</el-button
      >
      <div class="map-operate">
        <primary-operate
          @measure="measure"
          @zoomChange="zoomChange"
        ></primary-operate>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
// import { fromLonLat } from "ol/proj.js";// 格式化地理坐标
import mapConfig from "../../config/mapConfig";
import XYZ from "ol/source/XYZ";
import { Tile as TileLayer } from "ol/layer";
import { defaults } from "ol/interaction";
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";
import { queryMapserver } from "@/api/loadMapServe";
// import waterArea1 from "../../assets/water_data/waterArea1.json";
// import waterArea2 from "../../assets/water_data/waterArea2.json";
// import waterArea3 from "../../assets/water_data/waterArea3.json";
// import waterArea4 from "../../assets/water_data/waterArea4.json";
// import waterArea5 from "../../assets/water_data/waterArea5.json";
// import waterArea6 from "../../assets/water_data/waterArea6.json";
import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
  Typhoon,
} from "../../utils/mapUtil";
import PrimaryOperate from "../primary_map_operate/";
import Measure from "../../utils/measure";
export default {
  name: "PrimaryMap",
  props: {
    isDraw: {
      type: Boolean,
      value: false,
    },
    pointArr: String,
    zoom: Number,
    minZoom: Number,
  },
  data() {
    return {
      map: null,
      drawSource: null,
      hasPolygon: false,
      polygonLayer: null,
      polygonLayerList: [],
      riverLayerList: [],
      pointLayer: null,
      shadeLayer: new TileLayer(
        {
          zIndex: 3,
          source: new XYZ({
            url:
              "http://117.149.227.112:6080/arcgis/rest/services/mapservice/wenzhou_bs/MapServer/tile/{z}/{y}/{x}",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      ),
      textLayer: new TileLayer(
        {
          zIndex: 2,
          source: new XYZ({
            url:
              "http://t0.tianditu.gov.cn/DataServer?T=cia_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      ),
      mapLayers: {},
      pointLayers: {},
      clickFlag: true,
      lineLayer: {},
      riverLayer: null,
      typhoonLayers: [], //台风路径图层数组
      typhoonNames: [], //台风名称数组
      typhoonTimes: [], //定时器数组 台风还在播放时切换台风清除定时器
      areaLayer: null,
      geoLayer: null,
      shadeFlag: true,
      riverwayLayer: null, // 河道图层
    };
  },
  components: {
    PrimaryOperate,
  },
  mounted() {
    this.$nextTick(() => {
      this.init();
      if (this.isDraw && !this.pointArr) {
        const { source, vector } = drawFeature(window["mapObj"], "LineString");
        this.drawSource = source;
        this.polygonLayer = vector;
        // console.log(this.drawSource.);
        // console.log(this.polygonLayer,123)
      }
      const polygonArr = eval(this.pointArr);
      if (polygonArr && polygonArr.length > 0) {
        this.polygonLayer = drawLine(polygonArr, {});
        window["mapObj"].addLayer(this.polygonLayer);
      }
      Measure.setMeatureFeature();
    });
  },
  methods: {
    //初始化地图
    init() {
      let center = [mapConfig.lng, mapConfig.lat];
      this.layers = [];
      window["mapObj"] = new Map({
        target: "map", //地图容器
        layers: [
          new TileLayer(
            {
              // zIndex: 1,
              source: new XYZ({
                url:
                  "http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
          new TileLayer(
            {
              source: new XYZ({
                url:
                  "http://112.17.127.75:8008/arcgis/rest/services/2017%E6%B8%A9%E5%B7%9E%E5%8E%BF%E7%95%8C/MapServer/tile/{z}/{y}/{x}",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
          
          // this.shadeLayer,
          // new TileLayer(
          //   {
          //     source: new XYZ({
          //       url:
          //         "http://112.17.127.75:8008/arcgis/rest/services/wenzhou/2017wenzhou_border2/MapServer/tile/{z}/{y}/{x}",
          //       projection: "EPSG:4326",
          //     }),
          //   },
          //   { zoomOffset: 1 }
          // ),
          // new TileLayer(
          //   {
          //     source: new XYZ({
          //       url:
          //         "http://112.17.127.75:8008/arcgis/rest/services/%E6%B8%A9%E5%B7%9E%E5%B8%82%E7%95%8C/MapServer/tile/{z}/{y}/{x}",
          //       projection: "EPSG:4326",
          //     }),
          //   },
          //   { zoomOffset: 1 }
          // ),
          // this.shadeLayer,
          // mapConfig.maskLayer[0],
          // mapConfig.boundaryLayer[0],
          // mapConfig.xjBoundary[0],
        ],
        view: new View({
          center: center,
          zoom: mapConfig.zoom,
          maxZoom: mapConfig.maxZoom,
          minZoom: this.minZoom ? this.minZoom : mapConfig.minZoom,
          projection: "EPSG:4326",
        }),
        logo: false,
        //屏蔽双击放大事件
        interactions: new defaults({
          doubleClickZoom: false,
        }),
      });
      // let _this = this;
      //地图点击事件
      window["mapObj"].on("click", function (e) {
        console.log(e);
      });
      this.$nextTick(() => {
        let view = window["mapObj"].getView();
        view.setCenter(center);
        view.setZoom(mapConfig.zoom);
        // this.toggleShadeLayer("remove");
      });
    },
    //展示点位方法
    _drawPoint(list, style) {
      // console.log(list, 555);
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/map-intake-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      this.pointLayer = drawPoint(list, styleObj);
      console.log(this.pointLayer, 888);
      // pointLayer.setZIndex(1000)
      window["mapObj"].addLayer(this.pointLayer);
      const selectClick = addLayerClick(this.pointLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.data);
        }
      });
      window["mapObj"].addInteraction(selectClick);
    },
    removeLayers(type) {
      switch (type) {
        case "pointLayer":
          {
            window["mapObj"].removeLayer(this.pointLayer);
            this.pointLayer = null;
          }
          break;
        case "polygonLayer": {
          if (this.polygonLayerList.length > 0) {
            this.polygonLayerList.map((item) => {
              window["mapObj"].removeLayer(item);
            });
            this.polygonLayerList = [];
          }
        }
      }
    },
    //展示线方法
    _drawLines(arr, lineType) {
      const tempLayer = drawLines(arr);
      if (lineType == "河道") {
        this.riverLayer = tempLayer;
        window["mapObj"].addLayer(this.riverLayer);
      } else {
        if (!this.lineLayer[lineType]) {
          this.lineLayer[lineType] = tempLayer;
          window["mapObj"].addLayer(tempLayer);
        }
      }
    },
    //展示点方法(用这个)
    _drawPoints(list, style, layerName, noDialog) {
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/map-intake-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      // console.log(this.pointLayers[layerName], 9999);
      if (!this.pointLayers[layerName]) {
        // console.log(list, 6666);
        const pointLayer = drawPoint(list, styleObj);
        this.pointLayers[layerName] = pointLayer;
        // pointLayer.setZIndex(1000)
        // console.log(pointLayer);
        window["mapObj"].addLayer(pointLayer);
        const selectClick = addLayerClick(pointLayer);
        const _this = this;
        selectClick.on("select", function (e) {
          var features = e.target.getFeatures().getArray();
          if (features.length > 0 && _this.clickFlag) {
            _this.clickFlag = false;
            console.log(features[0].values_, 5555);
            if (noDialog) {
              _this.$emit("showDetail", features[0].values_.data);
            } else {
              _this.$emit("showDialog", features[0].values_.data);
            }
          }
          setTimeout(() => {
            _this.clickFlag = true;
          }, 200);
          // console.log(e.stioProp, 222);
        });
        window["mapObj"].addInteraction(selectClick);
      }
    },
    _drawPolygon(coordinates, styleObj) {
      const polygonLayer = drawPolygon(coordinates, styleObj);
      this.polygonLayerList.push(polygonLayer);
      const selectClick = addLayerClick(polygonLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.data);
        }
      });
      window["mapObj"].addInteraction(selectClick);
      window["mapObj"].addLayer(polygonLayer);
    },
    _drawRiverPolygon(coordinates, styleObj) {
      const polygonLayer = drawPolygon(coordinates, styleObj);
      this.riverLayerList.push(polygonLayer);
      const selectClick = addLayerClick(polygonLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          // console.log(features);
          _this.$emit("showDialog", features[0].values_.data);
        }
      });
      window["mapObj"].addInteraction(selectClick);
      window["mapObj"].addLayer(polygonLayer);
    },
    clearRiverPolygonLayer() {
      if (this.riverPolyLayerList.length > 0) {
        this.riverPolyLayerList.map((item) => {
          window["mapObj"].removeLayer(item);
        });
        this.riverPolyLayerList = [];
      }
    },
    clearMap() {
      window["mapObj"].removeLayer(this.polygonLayer);
      const { source, vector } = drawFeature(window["mapObj"], "LineString");
      this.drawSource = source;
      this.polygonLayer = vector;
    },
    clearMapOnly() {
      window["mapObj"].removeLayer(this.polygonLayer);
    },
    setCenterView(lng, lat, zoom) {
      let view = window["mapObj"].getView();
      view.animate({
        center: [lng, lat],
        duration: 300,
        zoom: zoom,
      });
      // view.setCenter([lng, lat]);
      // view.setZoom(zoom);
    },
    //传入数组中没有的layer 清除
    clearLayers(layerNameArr) {
      if (layerNameArr.length == 0) {
        for (let key in this.pointLayers) {
          window["mapObj"].removeLayer(this.pointLayers[key]);
          delete this.pointLayers[key];
        }
      } else {
        for (let key in this.pointLayers) {
          if (layerNameArr.indexOf(key) == -1) {
            window["mapObj"].removeLayer(this.pointLayers[key]);
            delete this.pointLayers[key];
          }
        }
      }
    },
    //根据图层名称清除layer
    clearLayersByLayer(layerName) {
      for (let key in this.pointLayers) {
        if (layerName == key) {
          window["mapObj"].removeLayer(this.pointLayers[key]);
          delete this.pointLayers[key];
        }
      }
    },
    //清除pointLayer中所有的图层
    clearPointLayers() {
      for (let key in this.pointLayers) {
        window["mapObj"].removeLayer(this.pointLayers[key]);
        delete this.pointLayers[key];
      }
    },
    //根据行政区划名称查区域边界
    showAreaLine(areaName) {
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
      ];
      const areaIndex = searchAreaList.indexOf(areaName);
      if (areaIndex != -1) {
        const searchUrl =
          "http://112.17.127.75:8008/arcgis/rest/services/%e6%b8%a9%e5%b7%9e%e5%8e%bf%e7%95%8c/MapServer/0/query";
        const opt = {
          where: "name='" + areaName + "'",
          inSR: 4490,
          outFields: "*",
          outSR: 4490,
          f: "pjson",
        };
        let geojson = {};
        queryMapserver(searchUrl, opt).then((res) => {
          let response = res.features;
          const coordinates = response[0].geometry.rings;
          this.areaLayer = drawPolygon(coordinates, {});
          window["mapObj"].addLayer(this.areaLayer);
        });
        this.flyPoint(centerPoint[areaIndex], 11);
      }
    },
    //定位到对应位置和层级
    flyPoint(center, zoom) {
      const view = window["mapObj"].getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom,
      });
    },
    //查询河道
    showRiverLayer(param) {
      this.riverwayLayer = new TileLayer(
        {
          source: new XYZ({
            url:
              "http://10.33.184.44:6080/arcgis/rest/services/waterSearch/RV_RVAA/MapServer/tile/{z}/{y}/{x}",
            projection: "EPSG:4326",
          }),
        },
        { zoomOffset: 1 }
      );
      window["mapObj"].addLayer(this.riverwayLayer);
    },
    setCenter() {
      // const view = window["mapObj"].getView();
      // window["mapObj"].
    },
    _addGeoJson() {
      this.clearGeoLayer();
      // console.log(new GeoJSON().readFeatures(waterArea2), 666);
      // const json1 = new GeoJSON().readFeatures(waterArea1);
      // const json2 = new GeoJSON().readFeatures(waterArea2);
      // const json3 = new GeoJSON().readFeatures(waterArea3);
      // const json4 = new GeoJSON().readFeatures(waterArea4);
      // const json5 = new GeoJSON().readFeatures(waterArea5);
      // const json6 = new GeoJSON().readFeatures(waterArea6);
      const features = [
        // ...json1,
        // ...json2,
        // ...json3,
        // ...json4,
        // ...json5,
        // ...json6,
      ];
      const source = new VectorSource({
        features: features,
        // dataProjection: "EPSG:3857",
        featureProjection: "EPSG:4326",
      });
      // console.log(new GeoJSON().readFeatures(waterArea1), 123);
      this.geoLayer = new Vector({
        projection: "EPSG:4326",
        source: source,
      });
      // const areaLayer = showLayerByGeoJson(
      //   require("../../assets/water_data/waterArea1.json")
      // );
      // console.log(areaLayer, 222);
      window["mapObj"].addLayer(this.geoLayer);
    },
    clearGeoLayer() {
      if (this.geoLayer) {
        window["mapObj"].removeLayer(this.geoLayer);
        this.geoLayer = null;
      }
    },
    clearLineLayers(layerNameArr) {
      if (layerNameArr.length == 0) {
        for (let key in this.lineLayer) {
          window["mapObj"].removeLayer(this.lineLayer[key]);
          delete this.lineLayer[key];
        }
      } else {
        for (let key in this.lineLayer) {
          if (layerNameArr.indexOf(key) > -1) {
            window["mapObj"].removeLayer(this.lineLayer[key]);
            delete this.lineLayer[key];
          }
        }
      }
    },
    // 清除指定图层
    clearOneLayer(layerName) {
      for (let key in this.pointLayers) {
        if (layerName == key) {
          window["mapObj"].removeLayer(this.pointLayers[key]);
          delete this.pointLayers[key];
        }
      }
    },
    clearRiverLayer() {
      if (this.riverLayer) {
        window["mapObj"].removeLayer(this.riverLayer);
        this.riverLayer = null;
      }
    },
    clearRiverwayLayer() {
      if (this.riverwayLayer) {
        window["mapObj"].removeLayer(this.riverwayLayer);
        this.riverwayLayer = null;
      }
    },
    clearAreaLayer() {
      if (this.areaLayer != null) {
        window["mapObj"].removeLayer(this.areaLayer);
        this.areaLayer = null;
      }
    },
    clearTyphoonLayer() {
      if (this.typhoonLayers.length > 0) {
        this.typhoonLayers.map((item) => {
          window["mapObj"].removeLayer(item);
        });
        this.typhoonLayers = [];
      }
      if (this.typhoonNames.length > 0) {
        this.typhoonNames.map((item) => {
          window["mapObj"].removeOverlay(item);
        });
        this.typhoonNames = [];
      }
      if (this.typhoonTimes.length > 0) {
        this.typhoonTimes.map((item) => {
          clearInterval(item);
        });
        this.typhoonTimes = [];
      }
    },
    toggleShadeLayer(type) {
      if (type == "add") {
        window["mapObj"].addLayer(this.shadeLayer);
      } else {
        // console.log("remove");
        window["mapObj"].removeLayer(this.shadeLayer);
        // window["mapObj"].addLayer(this.textLayer);
      }
    },
    measure(command) {
      if (command == "none") {
        Measure.meatureFeaturesClear();
      } else {
        Measure.measure(window["mapObj"], command);
      }
    },
    zoomChange(type) {
      let valueZoom = window["mapObj"].getView().getZoom();
      if (type == "add") {
        valueZoom++;
      } else {
        valueZoom--;
      }
      window["mapObj"].getView().setZoom(valueZoom);
    },
  },
};
</script>

<style lang='scss' scoped>
.map {
  height: 100%;
  position: relative;
  overflow: hidden;
  .clear-button {
    position: absolute;
    bottom: 10px;
    right: 10px;
    z-index: 1000;
  }
  ::v-deep .tooltip {
    color: #fff;
  }
  .map-operate {
    width: auto;
    height: 40px;
    line-height: 40px;
    position: absolute;
    left: 36px;
    bottom: 40px;
    color: #fff;
    z-index: 1900;
  }
}
</style>