<!--
 * @Date: 2022-06-01 18:02:59
 * @Author: 
 * @Description: 进入单个工程详情页顶部的地图
 * @LastEditors: dtb
 * @LastEditTime: 2022-06-02 17:33:34
 * @FilePath: \lucheng-app\src\views\appView\reservoir\ProjectMapSingle.vue
-->
<template>
  <div class="map-wrap">
    <div class="map" id="project-map-single"></div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Fill, Stroke, Style } from 'ol/style';
import XYZ from 'ol/source/XYZ';
import WKT from 'ol/format/WKT';
import TileLayer from 'ol/layer/Tile';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import { Map, View, Overlay, Feature } from 'ol';
import { Vector as VectorSource, TileArcGISRest } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import GeoJSON from 'ol/format/GeoJSON';
import Text from 'ol/style/Text';
import mapConfig from "@/config/mapConfig";
import { defaults as defaultControls } from "ol/control";
import {
  drawPoint,
  drawPolygon,
  drawLine,
  addLayerClick,
} from "@/utils/mapUtil";
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
    this.registeredEvent(); //注册事件
    this.registeredEvent(); //注册事件
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
      this.map.on("click", function (e) {
      });
    },
    cleanPointLayer(layer) {
      this.map.removeLayer(layer);
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
    setView(lng, lat, zoom) {
      const to = [lng, lat];
      const view = this.map.getView();
      view.animate({
        center: to,
        duration: 200,
        zoom: zoom ? zoom : 12,
      });
    },
    registeredEvent() {
      this.map.on('moveend', () => {
        /** @global */
        window.mapZoom = this.map.getView().getZoom();
      });
      this.map.on('singleclick', (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          let properties = feature.getProperties();
          if (properties.features && properties.features.length > 1) {
            let props = {};
            for (let item of properties.features) {
              props[item.values_.data.name] = item.values_.data.stationCode;
            }
          }
          return feature;
        });
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