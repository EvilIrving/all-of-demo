<template>
  <div class="map-select-point">
    <div class="map" id="selectPointMap">
      <div class="search-box">
        <slot></slot>
      </div>
      <el-button class="save-button" @click="saveData">保存</el-button>
      <!-- <el-button class="clear-button" @click="clearMap" v-if="isDraw"
        >清除</el-button
      > -->
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
      searchName: "",
    };
  },
  components: {},
  mounted() {
    this.init();
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
        target: "selectPointMap", //地图容器
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
      this.$emit("saveData");
    },
  },
};
</script>
<style scoped lang="scss">
.map-select-point {
  height: 100%;
  .map {
    height: 100%;
    position: relative;
    overflow: hidden;
    .search-box {
      width: 400px;
      height: 100%;
      z-index: 1000;
      position: absolute;
      right: 0;
      border: 1px solid #fff;
      background: #fff;
    }
    .clear-button {
      position: absolute;
      bottom: 10px;
      right: 10px;
      z-index: 1000;
    }
    .save-button {
      position: absolute;
      bottom: 10px;
      right: 20px;
      z-index: 1000;
    }
  }
}
</style>