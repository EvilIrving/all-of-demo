<template>
  <div class="map_wrap">
    <div class="map" id="mainView"></div>
    <div class="popup_map" ref="popup" v-show="popupData.type == 'project'">
      <div class="popup_content">
        <div
          class="popup_title"
          style="color: #003ec7; cursor: pointer"
          @click="goProjectDetail(popupData)"
        >
          {{ popupData.projectName }}
        </div>
        <div class="popup_item">
          <p>项目类型：</p>
          <span>{{ popupData.projectType || "-" }}</span>
        </div>
        <div class="popup_item">
          <p>编制时间：</p>
          <span>{{ popupData.applyDate || "-" }}</span>
        </div>
        <div class="popup_item">
          <p>联系人：</p>
          <span>{{
            popupData.principalName || "-"
          }}</span>
        </div>
        <div class="popup_item">
          <p>联系电话：</p>
          <span>{{
            popupData.principalTel || "-"
          }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Overlay from "ol/Overlay";
import "ol/ol.css";
import { Map, View, Feature } from "ol";
import XYZ from "ol/source/XYZ";
import Tile from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import { DoubleClickZoom } from "ol/interaction";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import { drawPoint, drawPointByType } from "@/utils/mapUtil";
import { getOutsideToken } from "@/api";

export default {
  name: "MapModel",
  props: {},
  components: {},
  data() {
    return {
      map: null,
      pointLayer: null,
      overlay: null,
      currentCoordinate: null,
      defaultCenter: {
        lnglat: [120.827, 28],
        zoom: 11,
      },
      type: "",
      popupData: {},
    };
  },
  mounted() {
    this.mapInit();
  },
  methods: {
    mapInit() {
      //弹窗
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
      //地图
      this.map = new Map({
        target: "mainView",
        controls: defaultControls({ zoom: true }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              url: "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),
          new Tile({
            source: new XYZ({
              url: "https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),
          new Tile({
            source: new XYZ({
              // url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              url: "https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_shuiyumian/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),
        ],
        overlays: [this.overlay],
        view: new View({
          projection: "EPSG:4326", //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
        }),
      });

      //禁用地图缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find((interaction) => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);
      //监听事件
      this.mapClick();
      // 鼠标移入显示手指
      this.map.on("pointermove", (e) => {
        let pixel = this.map.getEventPixel(e.originalEvent);
        let hit = this.map.hasFeatureAtPixel(pixel);
        if (hit) {
          let feature = this.map.forEachFeatureAtPixel(
            pixel,
            function (feature) {
              return feature;
            }
          );
          if (feature && feature.values_.data) {
            let pointData = feature.values_.data;
            if (pointData.type == "project") {
              this.popupData = pointData;
              this.$nextTick(() => {
                this.overlay.setPosition([pointData.lng, pointData.lat]);
              });
            }
          }
          this.map.getTargetElement().style.cursor = "pointer";
        } else {
          this.closePopup();
          this.map.getTargetElement().style.cursor = "";
        }
      });
    },

    //地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        if (
          feature &&
          feature.values_.features &&
          feature.values_.features.length > 0
        ) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            let pointData = feature.values_.features[0].values_.data;
            const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData;
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
            });
          } else {
            //点击聚合图标时
            this.map.getView().fit(feature.get("features")[0].getGeometry(), {
              maxZoom: this.map.getView().getZoom() + 1,
              duration: 250,
            });
          }
        } else {
          let pointData = feature.values_.data;
          if (pointData.type == "project") {
            this.$emit("mapPointClick", pointData);
          } else {
            this.closePopup();
            this.popupData = pointData;
            const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng, pointData.lat]);
            });
          }
        }
      });
    },

    //打点不聚合
    _drawPoints(coordinates) {
      this.map.removeLayer(this.pointLayer);
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/project.png"), //图片路径
        title: "",
        scale: 0.7,
      };
      this.pointLayer = drawPoint(coordinates, styleObj);
      this.pointLayer.setZIndex(99);
      this.map.addLayer(this.pointLayer);
    },

    drawMapPoints(coordinates) {
      this.map.removeLayer(this.pointLayer);
      this.pointLayer = drawPointByType(coordinates);
      this.pointLayer.setZIndex(99);
      this.map.addLayer(this.pointLayer);
    },

    removeAllLayer() {
      this.closePopup();
      this.map.removeLayer(this.pointLayer);
    },
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    async goProjectDetail(item) {
      console.log(item, 5656565656);
    },
  },
};
</script>

<style scoped lang="scss">
.map_wrap {
  background: #fdfdf1;
  position: relative;
  width: 100%;
  height: 100%;
  .map {
    width: 100%;
    height: 100%;
  }
  .popup_map {
    background: #fff;
    padding: 24px;
    @include shadow(#ccc, 0px, 0px, 2px);
    border-radius: 4px;
    .popup_content {
      color: #333;
      .popup_title {
        color: #000;
        font-size: 18px;
        font-weight: 500;
        padding-bottom: 4px;
      }
      .popup_item {
        padding-top: 16px;
        @include flexbox;
        @include flexAC;
        font-size: 16px;
        p {
          color: #666;
        }
        span {
          color: #000;
        }
      }
    }
  }
}
</style>