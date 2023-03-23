<!--
 * @Author: wupengfei
 * @LastEditTime: 2021-11-16 10:02:32
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: \lucheng\src\components\primary_map_typhoon\index.vue
-->
<template>
  <div class="wrap">
    <div class="map" id="map">
      <slot></slot>
      <el-button class="clear-button" @click="clearMap" v-if="isDraw"
        >清除</el-button
      >
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
import waterArea1 from "../../assets/water_data/waterArea1.json";
import waterArea2 from "../../assets/water_data/waterArea2.json";
import waterArea3 from "../../assets/water_data/waterArea3.json";
import waterArea4 from "../../assets/water_data/waterArea4.json";
import sxJson from "../../assets/water_data/330600.json";
import { queryMapserver } from "@/api/loadMapServe";

import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";

import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
  Typhoon,
  showLayerByGeoJson,
} from "../../utils/mapUtil";
export default {
  name: "PrimaryMap",
  props: {
    isDraw: {
      type: Boolean,
      value: false,
    },
    pointArr: String,
  },
  data() {
    return {
      map: null,
      drawSource: null,
      hasPolygon: false,
      polygonLayer: null,
      polygonLayerList: [],
      pointLayer: null,
      mapLayers: {},
      pointLayers: {},
      clickFlag: true,
      lineLayer: {},
      riverLayer: null,
      areaLayer: null,
    };
  },
  components: {},
  mounted() {
    this.$nextTick(() => {
      this.init();
    });
  },
  methods: {
    init() {
      var center = [mapConfig.lng, mapConfig.lat];
      this.layers = [];
      this.map = new Map({
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
          // mapConfig.defaultLayer[0],
          new TileLayer(
            {
              source: new XYZ({
                url:
                  "http://112.17.127.75:8008/arcgis/rest/services/wzbap/MapServer/tile/{z}/{y}/{x}",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
          new TileLayer(
            {
              source: new XYZ({
                url:
                  "http://112.17.127.75:8008/arcgis/rest/services/%E6%B8%A9%E5%B7%9E%E5%B8%82%E7%95%8C/MapServer/tile/{z}/{y}/{x}",
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
          // mapConfig.maskLayer[0],
          // mapConfig.boundaryLayer[0],
          // mapConfig.xjBoundary[0],
        ],
        view: new View({
          center: center,
          zoom: mapConfig.zoom,
          maxZoom: mapConfig.maxZoom,
          minZoom: 1,
          projection: "EPSG:4326",
        }),
        logo: false,
      });
      // let _this = this;
      this.map.on("click", function (e) {
        //  console.log(_this.polygonLayer)
        //  if(_this.polygonLayer&&_this.pointArr){
        //    _this.map.removeLayer(_this.polygonLayer);
        //    _this.pointArr = "";
        //    _this.drawSource = drawFeature(_this.map,"Polygon").source;
        //    _this.polygonLayer = drawFeature(_this.map,"Polygon").vector;
        //  }
        // console.log(e)
      });
      // const typhoon = new Typhoon(this.map);
      // typhoon.init();

      this._addGeoJson();
      this.showAreaLine("鹿城区");
    },
    _drawPoint(list, style) {
      // this.mapLayers.layerName = null;
      // console.log(list, 2345);
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/map-intake-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      this.pointLayer = drawPoint(list, styleObj);
      // pointLayer.setZIndex(1000)
      this.map.addLayer(this.pointLayer);
      const selectClick = addLayerClick(this.pointLayer);
      const _this = this;
      selectClick.on("select", function (e) {
        var features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          _this.$emit("showDialog", features[0].values_.data);
        }
      });
      this.map.addInteraction(selectClick);
    },
    _addGeoJson() {
      // console.log(new GeoJSON().readFeatures(waterArea1), 666);

      const features = [];
      const source = new VectorSource({
        features: new GeoJSON().readFeatures(waterArea4),
        // dataProjection: "EPSG:3857",
        featureProjection: "EPSG:4326",
      });
      // console.log(new GeoJSON().readFeatures(waterArea1), 123);
      const areaLayer = new Vector({
        projection: "EPSG:4326",
        source: source,
      });
      // const areaLayer = showLayerByGeoJson(
      //   require("../../assets/water_data/waterArea1.json")
      // );
      // console.log(areaLayer, 222);
      this.map.addLayer(areaLayer);
    },
    removeLayers(type) {
      switch (type) {
        case "pointLayer":
          {
            this.map.removeLayer(this.pointLayer);
            this.pointLayer = null;
          }
          break;
        case "polygonLayer": {
          if (this.polygonLayerList.length > 0) {
            this.polygonLayerList.map((item) => {
              this.map.removeLayer(item);
            });
            this.polygonLayerList = [];
          }
        }
      }
    },
    _drawLines(arr, lineType) {
      // console.log(lineType, 6677);
      const tempLayer = drawLines(arr);
      // console.log(tempLayer, 888);
      if (lineType == "河道") {
        this.riverLayer = tempLayer;
        this.map.addLayer(this.riverLayer);
      } else {
        if (!this.lineLayer[lineType]) {
          this.lineLayer[lineType] = tempLayer;
          this.map.addLayer(tempLayer);
        }
      }
    },
    _drawPoints(list, style, layerName, noDialog) {
      // console.log(this.pointLayers[layerName], 666);
      // this.mapLayers.layerName = null;
      // console.log(list, 2345);
      const styleObj = {
        anchor: [0, 0],
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: require("@/assets/images/map-intake-normal.png"), //图片路径
        title: "",
        scale: 1,
      };
      // console.log(this.pointLayers[layerName]);
      if (!this.pointLayers[layerName]) {
        const pointLayer = drawPoint(list, styleObj);
        this.pointLayers[layerName] = pointLayer;
        // pointLayer.setZIndex(1000)
        // console.log(pointLayer);
        this.map.addLayer(pointLayer);
        const selectClick = addLayerClick(pointLayer);
        const _this = this;
        selectClick.on("select", function (e) {
          var features = e.target.getFeatures().getArray();
          if (features.length > 0 && _this.clickFlag) {
            _this.clickFlag = false;
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
        this.map.addInteraction(selectClick);
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
      this.map.addInteraction(selectClick);
      this.map.addLayer(polygonLayer);
    },
    clearMap() {
      this.map.removeLayer(this.polygonLayer);
      const { source, vector } = drawFeature(this.map, "LineString");
      this.drawSource = source;
      this.polygonLayer = vector;
    },
    clearMapOnly() {
      this.map.removeLayer(this.polygonLayer);
    },
    setCenterView(lng, lat, zoom) {
      let view = this.map.getView();
      view.setCenter([lng, lat]);
      view.setZoom(zoom);
    },
    showAreaLine(areaName) {
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
        this.map.addLayer(this.areaLayer);
      });
    },
    clearLayers(layerNameArr) {
      if (layerNameArr.length == 0) {
        for (let key in this.pointLayers) {
          this.map.removeLayer(this.pointLayers[key]);
          delete this.pointLayers[key];
        }
      } else {
        for (let key in this.pointLayers) {
          if (layerNameArr.indexOf(key) == -1) {
            this.map.removeLayer(this.pointLayers[key]);
            delete this.pointLayers[key];
          }
        }
      }
      // console.log(this.pointLayers, 777);

      // console.log(this.pointLayers, 777);
      // console.log(layerNameArr, 222);
    },
    clearLineLayers(layerNameArr) {
      // console.log(layerNameArr, 9988);
      if (layerNameArr.length == 0) {
        for (let key in this.lineLayer) {
          this.map.removeLayer(this.lineLayer[key]);
          delete this.lineLayer[key];
        }
      } else {
        // console.log();
        for (let key in this.lineLayer) {
          if (layerNameArr.indexOf(key)) {
            this.map.removeLayer(this.lineLayer[key]);
            delete this.lineLayer[key];
          }
        }
      }
    },
    // 清除指定图层
    clearOneLayer(layerName) {
      for (let key in this.pointLayers) {
        if (layerName == key) {
          this.map.removeLayer(this.pointLayers[key]);
          delete this.pointLayers[key];
        }
      }
    },
    clearRiverLayer() {
      if (this.riverLayer) {
        this.map.removeLayer(this.riverLayer);
        this.riverLayer = null;
      }
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
}
</style>