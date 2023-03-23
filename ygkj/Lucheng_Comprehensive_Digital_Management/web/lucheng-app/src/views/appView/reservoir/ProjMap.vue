<template>
  <div class="map-wrap">
    <div class="map" id="proj-map"></div>
    <!-- 点位悬浮框 -->
    <div class="popup_map" id="popup" ref="popup" v-show="currentCoordinate">
      <div class="popup_content">
        <div class="popup_title" style="cursor: pointer; font-weight: 600" @click="goProjectDetail(popupData)" v-if="type !== '山塘'">
          {{ popupData.name }}
          <span style="color: #036eff">{{
              popupData.engScal || popupData.dikeGrad || popupData.seawallLevel
            }}{{ type }}</span>
        </div>
        <div class="popup_item" v-if="type !== '山塘'">
          <span>{{ popupData.safetyConclusion || popupData.wagaType ||popupData.dikeType  }}</span>|
          <span>{{
            popupData.areaName || popupData.village || popupData.startLocation || popupData.startLoc || "温州市鹿城区"
          }}</span>
        </div>
        <div class="popup_title" style="cursor: pointer; font-weight: 600" @click="goProjectDetail(popupData)" v-if="type === '山塘'">
          {{ popupData.name }}
        </div>
        <div class="popup_item" v-if="type === '山塘'">
          <span>总容积：</span>
          <span>{{ popupData.jhhsRj }}万m3</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View, Overlay } from "ol";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import mapConfig from "@/config/mapConfig";
import { defaults as defaultControls } from "ol/control";
import { drawPoint, drawSinglePoint } from "@/utils/mapUtil";
export default {
  name: "ProjMap",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      imgLayer: mapConfig.defaultLayer,
      vecLayer: mapConfig.vecLayer,
      featureInfo: {},
      currentCoordinate: null,
      popupData: {},
      type: this.$route.query.type,
    };
  },
  computed: {
  },
  mounted() {
    this.init();
    this.registeredEvent(); //注册事件
    this.addStationOverlay(); //注册事件
  },
  methods: {
    init() {
      let center = [mapConfig.lng, mapConfig.lat];
      this.layers = [];
      // 弹窗
      this.overlay = new Overlay({
        element: this.$refs.popup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-16, -10],
      });
      this.map = new Map({
        target: "proj-map", //地图容器
        layers: [...this.imgLayer, ...this.vecLayer],
        overlays: [this.overlay],
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
    cleanPointLayer(layer) {
      this.map.removeLayer(layer);
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
      layer = drawSinglePoint(pointArr, styleObj);
      this.map.addLayer(layer);
      return layer;
    },
    registeredEvent() {
      // 鼠标移入显示手指
      this.map.on("pointermove", (e) => {
        var pixel = this.map.getEventPixel(e.originalEvent);
        var hit = this.map.hasFeatureAtPixel(pixel);
        if (hit) {
          this.map.getTargetElement().style.cursor = "pointer";
        } else {
          this.map.getTargetElement().style.cursor = "";
        }
      });
      this.map.on("moveend", () => {
        /** @global */
        window.mapZoom = this.map.getView().getZoom();
      });
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          let properties = feature.getProperties();
          if (properties.features && properties.features.length > 1) {
            let props = {};
            for (let item of properties.features) {
              props[item.values_.data.name] = item.values_.data.code;
            }
          }
          return feature;
        });
        //是否存在坐标
        if (feature == undefined) {
          this.closePopup();
        }
        if (feature) {
          let pointData = feature.values_.data;
          const coordinate = evt.coordinate; // 获取坐标
          const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          this.popupData = pointData;
          setTimeout(() => {
            this.overlay.setPosition([pointData.lng, pointData.lat]);
          }, 0);
        }
        //点击单个图标时
      });
    },
    // 关闭弹窗
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector("#popup"),
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
      });
      this.map.addOverlay(this.overlay);
    },
    async goProjectDetail(item) {
      this.$store.commit('SET_ENFINEERINGINFO', item)
      this.$store.commit('SET_PROJECTCODE', item.code)
      this.$localData("set", "engineeringInfo", item);
      this.$localData("set", "projectCode", item.code)
      this.$router.push("/reservoirDetail?code=" + item.code + '&name=' + item.name + '&type=' + this.type);
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
  .popup_map {
    min-width: 220px;
    background: #fff;
    padding: 14px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    position: absolute;
    bottom: 15px;
    left: -50%;
    transform: translateX(-50%);
    .popup_content {
      color: #333;
      .popup_title {
        @include flexbox;
        @include flexJC(space-between);
        @include flexAI(center);
        color: #000;
        font-size: 16px;
      }
      .popup_item {
        padding-top: 16px;
        @include flexbox;
        @include flexJC(flex-start);
        @include flexAI(center);
        line-height: 14px;
        font-size: 14px;
        p {
          color: #999999;
        }
        span {
          color: #999999;
          vertical-align: middle;
        }
      }
    }
  }
}
</style>