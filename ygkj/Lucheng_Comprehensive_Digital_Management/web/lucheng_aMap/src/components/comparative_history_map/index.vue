<template>
  <div class="select-map">
    <div class="map" id="selectMap" />
  </div>
</template>

<script>
  import "ol/ol.css";
  import { Map, View } from "ol";
  import mapConfig from "../../config/mapConfig";
  import XYZ from "ol/source/XYZ";
  import { Tile as TileLayer } from "ol/layer";
  import { defaults } from "ol/interaction";
  // import Vector from "ol/layer/Vector";
  // import VectorSource from "ol/source/Vector";
  // import GeoJSON from "ol/format/GeoJSON";
  // import { queryMapserver } from "@/api/loadMapServe";
  import {
    // drawPoint,
    // drawFeature,
    addWKTDataLayer,
    // drawPolygon,
    // drawLine,
    // drawLines,
    // addLayerClick,
    // Typhoon,
  } from "../../utils/mapUtil";
  // import Measure from "../../utils/measure";
  export default {
    name: "comparative_history_map",
    props: {
      mapShowsList: Array
    },
    data() {
      return {
        pointLayer: null,
        pointLayers: {},
        lineLayer:{}
      }
    },
    watch:{
      mapShowsList(val) {
        console.log(890,val)
        val.forEach(item => {
          console.log(3334,item);
          this.map.addLayer(addWKTDataLayer(item));
          this.map.getView().fit(addWKTDataLayer(item).getSource().getFeatures()[0].getGeometry(), { padding: [100, 300, 0, 300] });
        })
      }
    },
    components: {},
    mounted() {
      this.init();
    },
    methods: {
      init() {
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
                    "http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023",
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
                    "http://t0.tianditu.gov.cn/DataServer?T=cia_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023",
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
          })
        })
      },
      //展示线方法
      // _drawLines(arr, lineType) {
      //   const tempLayer = drawLines(arr);
      //   if (lineType == "河道") {
      //     this.riverLayer = tempLayer;
      //     this.map.addLayer(this.riverLayer);
      //   } else {
      //     if (!this.lineLayer[lineType]) {
      //       this.lineLayer[lineType] = tempLayer;
      //       this.map.addLayer(tempLayer);
      //     }
      //   }
      // },
      //展示点方法(用这个)
      // _drawPoints(list, style, layerName, noDialog) {
      //   const styleObj = {
      //     anchor: [0, 0],
      //     anchorXUnits: "pixels", //anchorX单位
      //     anchorYUnits: "pixels", //anchorY单位
      //     src: require("@/assets/images/reservoir.png"), //图片路径
      //     title: "",
      //     scale: 1,
      //   };
      //   if (!this.pointLayers[layerName]) {
      //     const pointLayer = drawPoint(list, styleObj);
      //     this.pointLayers[layerName] = pointLayer;
      //     this.map.addLayer(pointLayer);
      //     const selectClick = addLayerClick(pointLayer);
      //     const _this = this;
      //     selectClick.on("select", function (e) {
      //       var features = e.target.getFeatures().getArray();
      //       if (features.length > 0 && _this.clickFlag) {
      //         _this.clickFlag = false;
      //         console.log(features[0].values_, 5555);
      //         // if (noDialog) {
      //         //   _this.$emit("showDetail", features[0].values_.data);
      //         // } else {
      //         //   _this.$emit("showDialog", features[0].values_.data);
      //         // }
      //       }
      //       setTimeout(() => { _this.clickFlag = true }, 200);
      //     });
      //     this.map.addInteraction(selectClick);
      //   }
      // },
    }
  }
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