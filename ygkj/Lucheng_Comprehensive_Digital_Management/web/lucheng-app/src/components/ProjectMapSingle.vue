<!--
 * @Date: 2022-06-01 18:02:59
 * @Author: 
 * @Description: 进入单个工程详情页顶部的地图
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-06-06 10:42:39
 * @FilePath: \lucheng-app\src\components\ProjectMapSingle.vue
-->
<template>
  <div class="map-wrap">
    <div class="map" id="project-map-single"></div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
import mapConfig from "@/config/mapConfig";
import { defaults as defaultControls } from "ol/control";
import { drawPoint } from "@/utils/mapUtil";
export default {
  name: "ProMap",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      imgLayer: mapConfig.defaultLayer,
      vecLayer: mapConfig.vecLayer,
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      let center = [mapConfig.lng, mapConfig.lat];
      this.layers = [];
      this.map = new Map({
        target: "project-map-single", //地图容器
        layers: [...this.imgLayer, ...this.vecLayer],
        view: new View({
          center: center,
          zoom: mapConfig.zoom,
          maxZoom: mapConfig.maxZoom,
          minZoom: mapConfig.minZoom,
          projection: "EPSG:4326",
        }),
        controls: defaultControls({ zoom: false }).extend([]),
        logo: false,
      });
      this.imgLayer.map((item) => {
        item.setVisible(false);
      });
    },
    _drawPoint(pointArr, configObj) {
      if (this.pointLayer != null) {
        this.map.removeLayer(this.pointLayer);
      }
      const styleObj = {
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src:
          configObj && configObj.src
            ? configObj.src
            : require("@/assets/images/my-position.png"), //图片路径
        title: "",
        scale: 0.4,
        ...configObj,
      };
      if(pointArr[0].name){
        delete pointArr[0].name
      }
      this.pointLayer = drawPoint(pointArr, styleObj);
      this.map.addLayer(this.pointLayer);
      this.setView(pointArr[0].point);
    },
    setView(pointArr, zoom) {
      const to = pointArr;
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: zoom ? zoom : 12,
      });
    },
  },
};
</script>

<style lang='scss' scoped>
.map-wrap {
  height: 100%;
  width: 100%;
  position: relative;
  .map {
    height: 100%;
    width: 100%;
    position: relative;
  }
}
</style>