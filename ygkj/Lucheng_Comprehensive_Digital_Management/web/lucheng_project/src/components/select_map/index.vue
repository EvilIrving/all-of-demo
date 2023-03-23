<template>
  <div class="select-map">
    <div class="map" id="selectMap">
      <el-button class="save-button" @click="saveData" v-if="isDraw"
        >保存</el-button
      >
      <el-button class="clear-button" @click="clearMap" v-if="isDraw"
        >清除</el-button
      >
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
import mapConfig from "../../config/mapConfig";
import XYZ from "ol/source/XYZ";
import { Tile as TileLayer } from "ol/layer";
import { defaults } from "ol/interaction";
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import GeoJSON from "ol/format/GeoJSON";
import { queryMapserver } from "@/api/loadMapServe";
import {
  drawPoint,
  drawFeature,
  drawPolygon,
  drawLine,
  drawLines,
  addLayerClick,
  Typhoon,
} from "../../utils/mapUtil";
import Measure from "../../utils/measure";
export default {
  name: "SelectMap",
  props: {
    isDraw: {
      type: Boolean,
      value: false,
    },
    pointArr: Array,
    drawType: {
      pointArr: String,
      default: "point",
    },
    isLook: Boolean,
  },
  data() {
    return {
      map: null,
      polygonLayer: null,
      drawSource: null,
    };
  },
  components: {},
  mounted() {
    this.init();
    //加载已存在的点位
    if (this.pointArr.length > 0) {
      let coordinates = [];
      let pointObj = {
        lat: this.pointArr[1],
        lng: this.pointArr[0],
      };
      coordinates.push(pointObj);
      let styleObj = {
        src: require("@/assets/images/position-active-icon.png"),
      };
      this.polygonLayer = drawPoint(coordinates, styleObj);
      this.map.addLayer(this.polygonLayer);
    } else {
      this.clearMap();
    }
    //判断是否能够画点或画线
    if (this.isDraw && !this.pointArr && !isLook) {
      //   console.log(123);
      if (this.drawType == "point") {
        var { source, vector } = drawFeature(this.map, "Point");
      } else {
        var { source, vector } = drawFeature(this.map, "LineString");
      }
      this.drawSource = source;
      this.polygonLayer = vector;
      //   console.log(this.polygonLayer, 555);
      // console.log(this.polygonLayer,123)
    }
  },
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    init() {
      //   console.log("init");
      let center = [mapConfig.lng, mapConfig.lat];
      this.layers = [];
      this.map = new Map({
        target: "selectMap", //地图容器
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
              // zIndex: 1,
              source: new XYZ({
                url:
                  "http://t0.tianditu.gov.cn/DataServer?T=cia_c&X={x}&Y={y}&L={z}&tk=2eeb2384fab6fcc8f96762c31bd0563b",
                projection: "EPSG:4326",
              }),
            },
            { zoomOffset: 1 }
          ),
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
      this.map.on("click", function (e) {
        console.log(e);
      });
      this.$nextTick(() => {
        let view = this.map.getView();
        view.setCenter(center);
        view.setZoom(mapConfig.zoom);
      });
    },
    clearMap() {
      this.map.removeLayer(this.polygonLayer);
      if (this.drawType == "point") {
        var { source, vector } = drawFeature(this.map, "Point");
      } else {
        var { source, vector } = drawFeature(this.map, "LineString");
      }
      this.drawSource = source;
      this.polygonLayer = vector;
    },
    saveData() {
      this.$emit("saveData", this.map.drawPointData);
    },
  },
};
</script>

<style scoped lang="scss">
.select-map {
  height: 100%;
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
    .save-button {
      position: absolute;
      bottom: 10px;
      right: 110px;
      z-index: 1000;
    }
  }
}
</style>