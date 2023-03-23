<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-16 17:24:38
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\views\appView\rainfallForecast\components\MainMap.vue
-->
<template>
  <div class="map-wrap">
    <div class="mainMap" id="mainView"></div>
    <!-- 点位悬浮框 -->
    <!-- <div class="popup_map" id="popup" ref="popup" v-show="currentCoordinate">
      <div class="popup_content">
        <div
          class="popup_title"
          style="cursor: pointer; font-weight: 600"
          @click="goProjectDetail(popupData)"
        >
          {{ popupData.name }}
        </div>
        <div class="popup_item">
          <div>
            <span>预报高潮位：</span><span style="color:#276AF0">{{ popupData.h2 }}米</span>
          </div>
        </div>
      </div>
    </div> -->
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View, Feature } from "ol";
import XYZ from "ol/source/XYZ";
import Tile from "ol/layer/Tile";
import { defaults as defaultControls } from "ol/control";
import Point from "ol/geom/Point";
import Overlay from "ol/Overlay";
import { toStringHDMS } from "ol/coordinate";
import { toLonLat } from "ol/proj";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import ImageLayer from "ol/layer/Image";
import ImageStatic from "ol/source/ImageStatic";
import { Vector as VectorSource, Cluster as ClusterSource } from "ol/source";
import {
  drawPoint,
  drawPolygon,
  drawLine,
  addLayerClick,
  showTestClusterPoint,
} from "@/utils/mapUtil";
import axios from "axios";
export default {
  data() {
    return {
      map: null,
      defaultCenter: {
        // lnglat: [119.555, 30.676],
        lnglat: [120.557, 28.056],
        zoom: 11,
      },
      pointLayer: null,
      isohyetLayer: null,
      textLayer: null,
      featureInfo: {},
      currentCoordinate: null,
      popupData: {},
      currentSelectArea: "鹿城区",
    };
  },
  mounted() {
    this.mapInit();
    this.registeredEvent(); //注册事件
    this.addStationOverlay(); //注册事件
  },
  methods: {
    mapInit() {
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
      // 地图
      this.map = new Map({
        target: "mainView",
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              url: "https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              //   url: "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          }),

          //绿色水利地图
          new Tile({
            source: new XYZ({
              url: "http://223.4.72.162:16080/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
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
      layer = drawPoint(pointArr, styleObj);
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
          // console.log(pointData);
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
    // async goProjectDetail(item) {
    //   console.log(item, 'popupData')
    //   // this.$store.commit('SET_ENFINEERINGINFO', item)
    //   // this.$localData('set', 'flashFloodRiskInfo', item)
    //   // this.$router.push('/flashFloodRiskDetail?naturalAdnm=' + item.nvi)
    //   this.$localData('set', 'tidalLevelForecastInfo', item)
    //   this.$router.push('/tidalLevelForecastDetail')
    // },
    showPredictRain(data, param) {
      // console.log(data,param,"ooooooooo");
      const arr = data.filter((item) => item.rainfall > 0);
      // console.log(arr,'arr');
      if (arr.length > 0) {
        // this.changeScreenLoading(true);
        let params = this.formatRainfallParam(arr, "rainfall");
        // console.log(params,'params');
        this._showTestPoint(params.features);
        this.getForecastRainfallLayer({
          opt: params,
          type: false,
          area: this.currentSelectArea,
          hour: param.hour,
        }).then((result) => {
          let mapImage = result.mapImage;
          // console.log(mapImage,'mapImage');
          this.getIsohyetOpacity(
            mapImage.xmin,
            mapImage.ymin,
            mapImage.xmax,
            mapImage.ymax,
            result.jobId,
            "forecast"
          );
        });
      } else {
        // this.changeScreenLoading(false);
        // this.ScreenMap.removeLayerByName('rainfall');
        this.clearTextLayer();
      }
    },
    async getIsohyetOpacity(xmin, ymin, xmax, ymax, jobId, type) {
      //type用来区分预报和实时等值面服务 forecast为预报 预报请求112 实时请求117
      let bbox = xmin + "," + ymin + "," + xmax + "," + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let url = `https://slgh.lucheng.gov.cn/arcgis/rest/services/gps/dengzhiPIDWReal0605/MapServer/jobs/${jobId}/export`;
      if (type == "forecast") {
        url = `https://slgh.lucheng.gov.cn/arcgis/rest/services/gps/PointTRaster/MapServer/jobs/${jobId}/export`;
      }
      await axios({
        url: url,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then((res) => {
        // this.changeScreenLoading(false);
        this.showIsohyetLayer(res.data);
      });
    },
    /** 雨量数据转换成等雨量面参数格式 */
    formatRainfallParam(list, paramName) {
      // console.log(list,'ppppppppp');
      let geoJson = {
        features: [],
      };
      let featuresArr = [];
      list.forEach((item) => {
        // let drp = item.drp > 0 ? item.drp : 0;
        // if (item[paramName] != "-" && item[paramName] > 0) {
        let obj = {
          attributes: {
            x: Number(item.longitude),
            y: Number(item.latitude),
            value: item[paramName],
          },
          geometry: {
            x: Number(item.longitude),
            y: Number(item.latitude),
          },
        };
        featuresArr.push(obj);
        // }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    getForecastRainfallLayer({ opt, type, area, hour = 1 }) {
      // console.log('test1111');
      // console.log(opt,'opt');
      // console.log(area);
      console.log(type, "dsadas");
      return new Promise((resolve, reject) => {
        let url =
          "https://slgh.lucheng.gov.cn/arcgis/rest/services/gps/PointTRaster/GPServer/PointTRaster/";
        let typeUrl =
          "https://slgh.lucheng.gov.cn/arcgis/rest/services/gps/dengzhiPIDWReal0522/GPServer/dengzhiPIDWReal/";
        let areaUrl = type ? typeUrl + "submitJob" : url + "submitJob";
        let params = new URLSearchParams();
        if (type) {
          params.append("station", JSON.stringify(opt));
        } else {
          params.append("points", JSON.stringify(opt));
          params.append("FiledValue", "value");
          params.append("pixSize", "0.01");
          params.append("pixAllocationType", "MAXIMUM");
        }
        params.append("f", "pjson");
        let pollingTimer = null;
        const options = {
          method: "post",
          url: areaUrl,
          headers: { "content-type": "application/x-www-form-urlencoded" },
          data: params,
        };
        axios(options).then((res) => {
          let startTimer = () => {
            pollingTimer = setTimeout(() => {
              let jobUrl = type
                ? `${typeUrl}jobs/${res.data.jobId}/results/Idwres`
                : `${url}jobs/${res.data.jobId}/results/outRaster`;
              axios({
                method: "get",
                url: `${jobUrl}?f=pjson`,
              }).then((result) => {
                if (result.data.value.mapImage.error) {
                  startTimer();
                } else {
                  clearTimeout(pollingTimer);
                  // commit("SAVE_RAINFALLLAYER", { hour, area, jobId: res.data.jobId });
                  resolve({
                    mapImage: result.data.value.mapImage.extent,
                    jobId: res.data.jobId,
                    type,
                  });
                }
              });
            }, 1000);
          };
          startTimer();
        });
      });
    },
    _showTestPoint(data) {
      this.clearTextLayer();
      this.textLayer = showTestClusterPoint(data);
      this.map.addLayer(this.textLayer);
    },
    //加载等雨量面
    showIsohyetLayer(mapImage) {
      // this.removeLayerByName('rainfall');
      this.map.removeLayer(this.isohyetLayer);
      this.isohyetLayer = new ImageLayer({
        name: "rainfall",
        source: new ImageStatic({
          // url: this.replaceUrl(mapImage.href), //这里添加静态图片的地址
          url: mapImage.href, //这里添加静态图片的地址
          projection: "EPSG:4326",
          // imageExtent: [119.53839579772948, 26.979173080444387, 121.33689579772948, 28.77767308044439] //WGS-84 经纬度数据范围
          imageExtent: [120.380091, 27.972081, 120.801921, 28.159652], //WGS-84 经纬度数据范围
        }),
        opacity: 0.8,
        zIndex: 10,
      });
      this.map.addLayer(this.isohyetLayer);
    },
    clearTextLayer() {
      if (this.textLayer != null) {
        this.map.removeLayer(this.textLayer);
        this.textLayer = null;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.map-wrap {
  width: 100%;
  height: 100%;
  .mainMap {
    width: 100%;
    height: 100%;
    // background: aqua;
  }
  .popup_map {
    min-width: 160px;
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
        // @include flexbox;
        // @include flexJC(flex-start);
        // @include flexAI(center);
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