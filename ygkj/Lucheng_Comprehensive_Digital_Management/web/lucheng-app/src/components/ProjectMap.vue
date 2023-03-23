<template>
  <div class="map-wrap">
    <div class="map" id="map-box-wrap"></div>
    <!-- 点位悬浮框 -->
    <div class="popup_map" ref="popup_rainfall" v-show="rainfallType">
      <div
        class="popup_content"
        :class="{ 'old-wrap-popup': addRouteType === 'older' }"
        @click="golink(popupData)"
      >
        <div class="popup_title">
          <p>{{ popupData.name }}</p>
          <p v-if="popupData.type == '雨情'">
            雨量：<span style="color: #036eff">{{ popupData.drp }}mm</span>
          </p>
          <p v-if="popupData.type == '水情'">
            实时水位：<span style="color: #036eff"
              >{{ popupData | popupDataType }}m</span
            >
          </p>
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
import { drawPoint } from "@/utils/mapUtil";
import { mapGetters } from "vuex";
export default {
  name: "ProMap",
  props: {},
  computed: {
    ...mapGetters(["addRouteType"]),
  },
  data() {
    return {
      map: null,
      pointLayer: null,
      imgLayer: mapConfig.defaultLayer,
      vecLayer: mapConfig.vecLayer,
      featureInfo: {},
      currentCoordinate: null,
      popupData: {},
      rainfallType: false,
      overlayRainfall: null,
    };
  },
  filters: {
    popupDataType: (data) => {
      switch (data.stType) {
        case "RR":
          return data.waterLevel;
        case "ZZ":
          return data.waterLevel;
        case "DD":
          return data.upz;
        default:
          return data.tdz;
      }
    },
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
      this.overlayRainfall = new Overlay({
        element: this.$refs.popup_rainfall, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250,
        },
        positioning: "bottom-center",
        offset: [-16, -10],
      });
      this.map = new Map({
        target: "map-box-wrap", //地图容器
        layers: [...this.imgLayer, ...this.vecLayer],
        overlays: [this.overlayRainfall],
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
    _drawPoint(pointArr, configObj, type) {
      if (this.pointLayer != null) {
        this.cleanPointLayer(this.pointLayer);
      }
      const styleObj = {
        anchorXUnits: "pixels", //anchorX单位
        anchorYUnits: "pixels", //anchorY单位
        src: configObj.src || require("@/assets/images/my-position.png"), //图片路径
        title: "",
        scale: configObj.scale || 0.8,
        ...configObj,
      };
      this.pointLayer = drawPoint(pointArr, styleObj, type);
      this.map.addLayer(this.pointLayer);
    },
    registeredEvent() {
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
          const hdms = toStringHDMS(toLonLat(pointData.point)); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          if (pointData.type == "雨情" || pointData.type == "水情") {
            pointData["name"] = pointData.stationName || pointData.stName;
            this.popupData = pointData;
            this.rainfallType = true;
          }
          setTimeout(() => {
            this.overlayRainfall.setPosition(pointData.point);
          }, 0);
        }
        //点击单个图标时
      });
    },
    // 关闭弹窗
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlayRainfall.setPosition(undefined);
      this.currentCoordinate = null;
    },
    golink(data) {
      this.$emit("golink", data);
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
    width: 180px;
    background: #fff;
    padding: 12px;
    box-sizing: border-box;
    border-radius: 4px;
    position: absolute;
    bottom: 0px;
    left: -31px;
    // margin-left: 62px;
    transform: translateX(-15%);
    .popup_content {
      display: flex;
      color: #333;
      .popup_title {
        color: #000;
        font-size: 16px;
        > p {
          line-height: 22px;
        }
      }
      &.old-wrap-popup {
        .popup_title {
          font-size: 20px;
          > p {
            color: #666666;
            &:first-child {
              color: #1e1e1f;
              font-size: 24px;
              padding-bottom: 10px;
            }
          }
        }
      }
    }
  }
}
</style>