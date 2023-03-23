<!--
 * @Author: wanxin
 * @LastEditTime: 2022-06-13 16:37:02
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\views\appView\flashFloodRisk\components\MainMap.vue
-->
<template>
  <div class="map-wrap">
    <div class="mainMap" id="mainView"></div>
    <!-- 点位悬浮框 -->
    <div class="popup_map" id="popup" ref="popup" v-show="currentCoordinate">
      <div class="popup_content">
        <div
          class="popup_title"
          style="cursor: pointer; font-weight: 600"
          @click="goProjectDetail(popupData)"
        >
          {{ popupData.nvi }}
        </div>
        <div class="popup_item">
          <div>
            <span>总人口：</span><span>{{ popupData.tpo }}人</span>
          </div>
          <div>
            <span>转移责任人：</span>{{ popupData.chargerName }}<span></span>
          </div>
          <div>
            <span>电话：</span><span>{{ popupData.chargerPhone }}</span>
          </div>
        </div>
        <!-- <div
          class="popup_title"
          style="cursor: pointer; font-weight: 600"
          @click="goProjectDetail(popupData)"
          v-if="type === '山塘'"
        >
          {{ popupData.name }}
        </div>
        <div class="popup_item" v-if="type === '山塘'">
          <span>总容积：</span>
          <span>{{ popupData.jhhsRj }}万m3</span>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import 'ol/ol.css'
import { Map, View, Feature } from 'ol'
import XYZ from 'ol/source/XYZ'
import Tile from 'ol/layer/Tile'
import { defaults as defaultControls } from 'ol/control'
import Point from 'ol/geom/Point'
import Overlay from 'ol/Overlay'
import { toStringHDMS } from 'ol/coordinate'
import { toLonLat } from 'ol/proj'
import { Style, Fill, Stroke, Icon, Text, Circle } from 'ol/style'
import { Tile as TileLayer, Vector as VectorLayer } from 'ol/layer'
import { Vector as VectorSource, Cluster as ClusterSource } from 'ol/source'
import {
  drawPoint,
  drawPolygon,
  drawLine,
  addLayerClick,
} from '@/utils/mapUtil'
export default {
  data() {
    return {
      map: null,
      defaultCenter: {
        // lnglat: [119.555, 30.676],
        lnglat: [120.657, 28.016],
        zoom: 11,
      },
      pointLayer: null,
      featureInfo: {},
      currentCoordinate: null,
      popupData: {},
    }
  },
  mounted() {
    this.mapInit()
    this.registeredEvent() //注册事件
    this.addStationOverlay() //注册事件
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
        positioning: 'bottom-center',
        offset: [-16, -10],
      })
      // 地图
      this.map = new Map({
        target: 'mainView',
        //地图控件
        // controls: defaultControls({ attribution: true, zoom: true }),
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new Tile({
            source: new XYZ({
              url: 'https://t0.tianditu.gov.cn/DataServer?T=vec_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021',
              //   url: "https://t0.tianditu.gov.cn/DataServer?T=ter_c&X={x}&Y={y}&L={z}&tk=fa28e99f08e5a48b15bff03e7104a021",
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous',
            }),
            preload: 1,
            visible: true,
          }),

          //绿色水利地图
          new Tile({
            source: new XYZ({
              url: 'http://223.4.72.162:16080/arcgis/rest/services/basemap/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous',
            }),
            preload: 1,
            visible: true,
          }),
        ],
        overlays: [this.overlay],
        view: new View({
          projection: 'EPSG:4326', //使用这个坐标系
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          maxZoom: 18,
          minZoom: 0,
        }),
      })
    },
    cleanPointLayer(layer) {
      this.map.removeLayer(layer)
    },
    setView(lng, lat, zoom) {
      const to = [lng, lat]
      const view = this.map.getView()
      view.animate({
        center: to,
        duration: 200,
        zoom: zoom ? zoom : 12,
      })
    },
    _drawPoint(pointArr, configObj, layer) {
      if (layer != null) {
        layer.getSource().clear()
      }
      const styleObj = {
        anchorXUnits: 'pixels', //anchorX单位
        anchorYUnits: 'pixels', //anchorY单位
        src: configObj.src || require('@/assets/images/my-position.png'), //图片路径
        title: '',
        ...configObj,
      }
      layer = drawPoint(pointArr, styleObj)
      this.map.addLayer(layer)
      return layer
    },
    registeredEvent() {
      // 鼠标移入显示手指
      this.map.on('pointermove', (e) => {
        var pixel = this.map.getEventPixel(e.originalEvent)
        var hit = this.map.hasFeatureAtPixel(pixel)
        if (hit) {
          this.map.getTargetElement().style.cursor = 'pointer'
        } else {
          this.map.getTargetElement().style.cursor = ''
        }
      })
      this.map.on('moveend', () => {
        /** @global */
        window.mapZoom = this.map.getView().getZoom()
      })
      this.map.on('singleclick', (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent)
        let feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          let properties = feature.getProperties()
          if (properties.features && properties.features.length > 1) {
            let props = {}
            for (let item of properties.features) {
              props[item.values_.data.name] = item.values_.data.code
            }
          }
          return feature
        })
        //是否存在坐标
        if (feature == undefined) {
          this.closePopup()
        }
        if (feature) {
          let pointData = feature.values_.data
          const coordinate = evt.coordinate // 获取坐标
          const hdms = toStringHDMS(toLonLat([pointData.lng, pointData.lat])) // 转换坐标格式
          this.currentCoordinate = hdms // 保存坐标点
          this.popupData = pointData
          setTimeout(() => {
            this.overlay.setPosition([pointData.lng, pointData.lat])
          }, 0)
        }
        //点击单个图标时
      })
    },
    // 关闭弹窗
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined)
      this.currentCoordinate = null
    },
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#popup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250,
        },
      })
      this.map.addOverlay(this.overlay)
    },
    async goProjectDetail(item) {
      // this.$store.commit('SET_ENFINEERINGINFO', item)
      this.$localData('set', 'flashFloodRiskInfo', item)
      this.$router.push('/flashFloodRiskDetail?naturalAdnm=' + item.nvi)
    },
  },
}
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