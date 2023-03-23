<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-17 14:27:44
 * @Description: 地图
 * @FilePath: /cangnan-app/src/views/projectIndex/components/ProjectMap.vue
-->
<template>
  <div class="project-map">
    <div class="map" id="map"></div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
import mapConfig from "@/config/mapConfig";
import { defaults as defaultControls } from "ol/control";
import {
  drawPoint,
  drawPolygon,
  drawLine,
  addLayerClick,
} from "@/utils/mapUtil";
export default {
  name: "ProjectMap",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      areaLayerList: [],
      imgLayer: mapConfig.defaultLayer,
      vecLayer: mapConfig.vecLayer,
      mapChangeType: "影像图",
      test: "",
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
        target: "map", //地图容器
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
      this.map.on("click", function (e) {
        console.log(e);
      });
    },
    getMyPos() {
      if (navigator.geolocation) {
        var n = navigator.geolocation.getCurrentPosition(function (res) {
          console.log(res);
          this.test = res.toString();
          // this.mapChangeType = res.toString();
          // alert(res); // 需要的坐标地址就在res中
        });
      } else {
        Toast("定位获取失败");
      }
    },
    cleanPointLayer(layer) {
      this.map.removeLayer(layer);
    },
    setView(lng, lat, zoom) {
      const to = [lng, lat];
      const view = this.map.getView();
      // console.log(view)
      view.animate({
        center: to,
        duration: 200,
        zoom: zoom ? zoom : 12,
      });
    },
    _drawPoint(pointArr, configObj, layer) {
      if (layer != null) {
        layer.getSource().clear();
      }
      const styleObj = {
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: configObj.src || require("@/assets/images/my-position.png"), //图片路径
        title: "",
        ...configObj,
      };
      layer = drawPoint(pointArr, styleObj);
      this.map.addLayer(layer);
      return layer;
    },
  },
};
</script>

<style lang='scss' scoped>
.project-map {
  height: 100%;
  width: 100%;
  .map {
    height: 100%;
    width: 100%;
    position: relative;
  }
}
</style>