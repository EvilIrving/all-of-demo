<!--
 * @Author: wqn
 * @Date: 2022-11-10 17:30:33
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-21 18:29:15
 * @Description: 地图
-->
<template>
  <div style="width: 100%;height: 100%;">
    <div ref="olMap" class="map-container"></div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View } from "ol";
import { defaults as defaultControls } from "ol/control";
import { Vector as VectorLayer } from "ol/layer";
import { Vector as VectorSource } from "ol/source";
import TileLayer from "ol/layer/Tile";
import XYZ from "ol/source/XYZ";
import { Style, Fill } from "ol/style";
import GeoJSON from "ol/format/GeoJSON";
import { defaults as defaultInteractions } from "ol/interaction";
import { getVectorContext } from "ol/render";
import admnGeoJson from "@/assets/geoJson/zjsGeoJson.json";
import borderGeoJson from "@/assets/geoJson/cutZjsGeoJson.json";

export default {
  name: "Map",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      defaultCenter: {},
      overlaysArr: [],
      mapInitViewData: {
        浙江省: {
          center: [120.0051796875, 28.6],
          zoom: 8.3,
        },
        杭州市: {
          center: [119.49926673638585, 29.95238969771562],
          zoom: 9.1,
        },
        嘉兴市: {
          center: [120.82295965448235, 30.62365529556139],
          zoom: 10,
        },
        舟山市: {
          center: [122.31747879977257, 30.15719831907083],
          zoom: 9.5,
        },
        绍兴市: {
          center: [120.65203143078425, 29.779204859669218],
          zoom: 9.5,
        },
        湖州市: {
          center: [119.86013581555973, 30.774957488035955],
          zoom: 10,
        },
        丽水市: {
          center: [119.52993030133263, 28.058645185047814],
          zoom: 9.3,
        },
        台州市: {
          center: [121.1019486426036, 28.620996798490223],
          zoom: 9.5,
        },
        温州市: {
          center: [120.48230685780125, 27.850262573033703],
          zoom: 9.3,
        },
        衢州市: {
          center: [118.6963592735759, 28.903052563843694],
          zoom: 9.6,
        },
        金华市: {
          center: [119.96357972413958, 29.14920448445481],
          zoom: 9.5,
        },
        宁波市: {
          center: [121.55592775905025, 29.707711898447568],
          zoom: 9.5,
        },
      },
      //图层集合
      pointLayers: {
        reservoirPopup: null,
      },
      pointData: {},
    };
  },
  computed: {},
  watch: {},
  created() { },
  mounted() {
    // 初始化地图
    this.initMap();
  },
  destroyed() { },
  methods: {
    /**
     * @description 初始化地图
     */
    initMap() {
      const tiandituMap = new TileLayer({
        source: new XYZ({
          url:
            process.env.NODE_ENV === "production"
              ? "https://sldtpt.zjwater.com:6443/zjsslgcyxglpt2/arcgis/rest/services/basemap/ZJRasterMap/MapServer/tile/{z}/{y}/{x}"
              : "http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=bea4826cd9f20c39d86d6226c45593e9",
          projection: "EPSG:4326",
        }),
      });
      const clipLayer = new VectorLayer({
        projection: "EPSG:4326",
        source: new VectorSource({
          features: new GeoJSON().readFeatures(borderGeoJson),
          featureProjection: "EPSG:4326",
        }),
      });
      tiandituMap.on("postrender", function (e) {
        e.context.globalCompositeOperation = "destination-in";
        clipLayer.getSource().forEachFeature(function (feature) {
          getVectorContext(e).drawFeature(
            feature,
            new Style({ fill: new Fill({ color: "red" }) })
          );
        });
        e.context.globalCompositeOperation = "source-over";
      });
      this.map = new Map({
        target: this.$refs.olMap,
        layers: [tiandituMap],
        view: new View({
          projection: "EPSG:4326",
          center: [120.6, 28.8],
          zoom: 7.8,
          maxZoom: 18,
          minZoom: 8,
        }),
        controls: defaultControls({ zoom: false }).extend([]),
        interactions: defaultInteractions({ doubleClickZoom: false }),
      });

      this.$olMapUtil.drawAdmnBoundary(this.map, admnGeoJson);
      this.$olMapUtil.drawMapBoundary(this.map, borderGeoJson);
      this.map.on("singleclick", (evt) => {
        const pixel = this.map.getEventPixel(evt.originalEvent);
        const feature = this.map.forEachFeatureAtPixel(pixel, (feat) => feat);

        console.log(feature, "feature");
        this.$store.commit("setClickLngLat", evt.coordinate);
        if (
          feature &&
          feature.values_ &&
          feature.values_.features &&
          feature.values_.features.length === 1 &&
          feature.values_.features[0].values_ &&
          feature.values_.features[0].values_.layerName
        ) {
          this.$store.commit(
            "setClickFeatVal",
            feature.values_.features[0].values_
          );
        } else if (feature && feature.values_ && feature.values_.layerName) {
          this.$store.commit("setClickFeatVal", feature.values_);
        } else {
          this.$store.commit("setClickFeatVal", null);
        }
        if (feature && feature.values_ && feature.values_.admnLayerName) {
          this.$store.commit("setClickAdmnFeat", feature);
        } else {
          this.$store.commit("setClickAdmnFeat", null);
        }
      });
      this.map.on("dblclick", (evt) => {
        this.$store.commit("setDbClickLngLat", evt.coordinate);
      });
      this.map.on("pointermove", (evt) => {
        const pixel = this.map.getEventPixel(evt.originalEvent);
        const feature = this.map.forEachFeatureAtPixel(pixel, (feat) => feat);
        if (this.map.hasFeatureAtPixel(pixel)) {
          this.map.getTargetElement().style.cursor = "pointer";
        }
        this.$store.commit("setHoverLngLat", evt.coordinate);
        if (feature && feature.values_ && feature.values_.layerName) {
          this.$store.commit("setHoverFeatVal", feature.values_);
        } else {
          this.$store.commit("setHoverFeatVal", null);
        }
        if (feature && feature.values_ && feature.values_.admnLayerName) {
          this.$store.commit("setHoverAdmnVal", feature);
        } else {
          this.$store.commit("setHoverAdmnVal", null);
        }
      });
      this.map.on("moveend", () => {
        this.$store.commit("setMapZoom", this.map.getView().getZoom());
      });
      this.$nextTick(() => {
        this.$emit(
          "initMapFinished",
          this.map,
          new GeoJSON().readFeatures(borderGeoJson)[0].getGeometry()
        );
      });
      // // 为不同角色设置地图视图
      // for (let key in this.mapInitViewData) {
      //   if (key == this.userAdnm) {
      //     this.$olMapUtil.flyPoint(
      //       this.map,
      //       this.mapInitViewData[key].center,
      //       this.mapInitViewData[key].zoom
      //     );
      //     break;
      //   } else {
      //     this.$olMapUtil.flyPoint(
      //       this.map,
      //       this.mapInitViewData["浙江省"].center,
      //       this.mapInitViewData["浙江省"].zoom
      //     );
      //   }
      // }
    },
  },
};
</script>
<style lang='scss' scoped>
.map-container {
  width: 100%;
  height: 100%;
}
</style>
