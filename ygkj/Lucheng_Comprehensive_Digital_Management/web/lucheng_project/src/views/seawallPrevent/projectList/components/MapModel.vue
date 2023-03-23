<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-02 19:34:47
 * @Description: 地图模式
 * @FilePath: /lucheng_project/src/views/seawallPrevent/projectList/components/MapModel.vue
-->
<template>
  <div class='map_wrap'>
    <div class="map" id="mainView"></div>
    <!-- 弹窗元素 -->
    <div class="popup_map" ref="popup" v-show="currentCoordinate">
      <div class="popup_content">
        <div class="popup_title" style="color: #003ec7; cursor: pointer" @click="goProjectDetail(popupData)">{{popupData.seawallName}}
        </div>
        <div class="popup_item">
          <p>所在地：</p>
          <span>{{popupData.areaName ? popupData.areaName : '-'}}</span>
        </div>
        <div class="popup_item">
          <p>海塘等级：</p>
          <span>{{popupData.seawallLength ? popupData.seawallLevel + '级' : '-'}}</span>
        </div>
        <div class="popup_item">
          <p>海塘长度：</p>
          <span>{{popupData.seawallLength ? popupData.seawallLength + '米' : '-'}}</span>
        </div>
        <div class="popup_item">
          <p>安全鉴定：</p>
          <span>{{popupData.safetyConclusion ? popupData.safetyConclusion : '-'}}</span>
        </div>
      </div>
    </div>
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
import { Vector as VectorSource, Cluster as ClusterSource } from "ol/source";
import { Draw, DoubleClickZoom } from "ol/interaction";
import {
  drawPoint,
  drawLines
} from "@/utils/mapUtil";

import { getOutsideToken } from '@/api/index'
export default {
  name: '',
  props: {
    
  },
  components: {
    
  },
  data () {
    return {
      map: null,
      pointLayer: null,
      linesLayer: null,
      overlay: null,
      popupData: {},
      currentCoordinate: null,
      defaultCenter: {
        lnglat: [120.827, 28],
        zoom: 11,
      },
    };
  },
  mounted() {
    this.mapInit()
  },
  methods: {
    //初始化地图
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

          //绿色水利地图
          new Tile({
            source: new XYZ({
              url: "https://sldtpt.zjwater.com:6443/lcqslqyszhghpt/PBS/rest/services/ZLSLVectorMap/MapServer/tile/{z}/{y}/{x}",
              projection: "EPSG:4326",
              crossOrigin: "anonymous",
            }),
            preload: 1,
            visible: true,
          })
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

      // 禁用地图双击缩放
      const dblClickInteraction = this.map
        .getInteractions()
        .getArray()
        .find((interaction) => {
          return interaction instanceof DoubleClickZoom;
        });
      this.map.removeInteraction(dblClickInteraction);

      // 监听事件
      this.mapClick(); // 初始化地图成功后，给地图添加点击事件

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
    },

    // 地图点击事件
    mapClick() {
      this.map.on("singleclick", (evt) => {
        var pixel = this.map.getEventPixel(evt.originalEvent);
        var feature = this.map.forEachFeatureAtPixel(pixel, function (feature) {
          return feature;
        });
        this.closePopup()
        if (feature && feature.values_.features && feature.values_.features.length > 0) {
          if (feature.values_.features.length == 1) {
            //点击单个图标时
            let pointData = feature.values_.features[0].values_.data;
            const coordinate = evt.coordinate; // 获取坐标
            const hdms = toStringHDMS(toLonLat([pointData.lng,pointData.lat])); // 转换坐标格式
            this.currentCoordinate = hdms; // 保存坐标点
            this.popupData = pointData
            setTimeout(() => {
              this.overlay.setPosition([pointData.lng,pointData.lat]);
            });
          } else {
            //点击聚合图标时
            this.map.getView().fit(
              feature.get('features')[0].getGeometry(), 
              {
                maxZoom: this.map.getView().getZoom() + 1,
                duration: 250
              }
            )
          }
        }else{
          let pointData = feature.values_.data;
          this.popupData = pointData
          const hdms = toStringHDMS(toLonLat([pointData.lng,pointData.lat])); // 转换坐标格式
          this.currentCoordinate = hdms; // 保存坐标点
          setTimeout(() => {
            this.overlay.setPosition([pointData.lng,pointData.lat]);
          });
        }
      });
    },

    //画线
    _drawLines(list){
      this.map.removeLayer(this.linesLayer);
      for (let i = 0; i < list.length; i++) {
        list[i].coordinates = []
        if(list[i].line && list[i].line.length){
          for(let j = 0; j < list[i].line.length; j++){
            list[i].coordinates.push([list[i].line[j].lng, list[i].line[j].lat])
          }
          switch(list[i].level){
            case '1':
              list[i].lineColor = '#E02020'
              break;
            case '2':
              list[i].lineColor = '#EB7E65'
              break;
            case '3':
              list[i].lineColor = '#F7C739'
              break;
            case '4':
              list[i].lineColor = '#193CC5'
              break;
            case '5':
              list[i].lineColor = '#1BC57E'
              break;
            default:
              list[i].lineColor = '#C2C2C2'
              break;
          }
        }
      }
      this.linesLayer = drawLines(list);
      this.map.addLayer(this.linesLayer);
    },

    //打点不聚合
    _drawPoints(coordinates) {
      this.map.removeLayer(this.pointLayer);
      // 点
      const styleObj = {
        anchor: [1, 1],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        src: require("@/assets/images/reservoir.png"), //图片路径
        title: "",
        scale: .7,
      };
      this.pointLayer = drawPoint(coordinates, styleObj);
      this.pointLayer.setZIndex(99);
      this.map.addLayer(this.pointLayer);
    },

    removeAllLayer() {
      this.closePopup()
      this.map.removeLayer(this.pointLayer);
    },

    // 关闭弹窗
    closePopup() {
      // 把弹窗位置设置为undefined，并清空坐标数据
      this.overlay.setPosition(undefined);
      this.currentCoordinate = null;
    },

    async goProjectDetail(item){
      let seawallDetail = item
      seawallDetail.prcd = item.seawallCode
      let res = await getOutsideToken({
        prcd: item.seawallCode
      })
      if(res.code === 0){
        seawallDetail.outsideToken = res.data
        this.$store.commit('SET_SEAWALL_DETAIL', seawallDetail)
        this.$router.push('/projectMenu')
      }
    }
  }
}
</script>

<style lang='scss' scoped>
  .map_wrap{
    background: #fdfdf1;
    position: relative;
    width: 100%;
    height: 100%;
    .map {
      width: 100%;
      height: 100%;
    }
    .popup_map{
      background: #fff;
      padding: 24px;
      @include shadow(#ccc, 0px, 0px, 2px);
      border-radius: 4px;
      .popup_content{
        color: #333;
        .popup_title{
          color: #000;
          font-size: 18px;
          font-weight: 500;
          padding-bottom: 4px;
        }
        .popup_item{
          padding-top: 16px;
          @include flexbox;
          @include flexAC;
          font-size: 16px;
          p{
            color: #666;
          }
          span{
            color: #000;
          }
        }
      }
    }
  }
</style>