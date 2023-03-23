<!--
 * @Author: hanyu
 * @LastEditTime: 2021-11-02 19:33:55
 * @Description: 地图选点组件
 * @FilePath: /lucheng_project/src/components/mapSelectPoint.vue
-->
<template>
  <div class='mapSelect'>
    <div id="mapTarget"></div>
    <div class="point_msg">
      <span>经度：{{selectPointLng}}</span>
      <span>纬度：{{selectPointLat}}</span>
    </div>
    <div class="footer_btn">
      <el-button type="primary" class="btn_save" @click="surePoint()">保存</el-button>
      <el-button @click="closeMapSelectDialog()">取消</el-button>
    </div>
  </div>
</template>

<script>
import "ol/ol.css";
import { Map, View, Feature } from "ol";
import XYZ from "ol/source/XYZ";
import { defaults as defaultsControl } from 'ol/control' //ol.control
import Point from "ol/geom/Point";
import { Style, Fill, Stroke, Icon, Text, Circle } from "ol/style";
import Tile from 'ol/layer/Tile'
import Vector from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
export default {
  name: '',
  props: {
    pointLng: {
      type: [String, Number],
      default: ''
    },
    pointLat: {
      type: [String, Number],
      default: ''
    }
  },
  components: {
    
  },
  data () {
    return {
      projection: 'EPSG:4326',
      mapObj: null,
      selectPointLng: '',
      selectPointLat: '',
      defaultCenter: {
        lnglat: [120.827, 28],
        zoom: 11,
      },
    };
  },
  mounted() {
    this.selectPointLng = this.pointLng
    this.selectPointLat = this.pointLat
    this.mapInit()
  },
  methods: {
    mapInit() {
      let that = this
      this.projection = 'EPSG:3857'
      this.mapObj = new Map({
        target: 'mapTarget',
        controls: defaultsControl({ attribution: false, zoom: false }),
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
        view: new View({
          projection: "EPSG:4326",
          // center: [this.pointLng, this.pointLat],
          center: this.defaultCenter.lnglat,
          zoom: this.defaultCenter.zoom,
          minZoom: 6,
          maxZoom: 17,
        }),
      })
      if(this.pointLng && this.pointLat){
        this.drawOnePoint(this.pointLng, this.pointLat)
      }
      this.mapObj.on('click', function(evt) {
        var coordinate = evt.coordinate;
        that.drawOnePoint(coordinate[0], coordinate[1])
        that.selectPointLng = coordinate[0]
        that.selectPointLat = coordinate[1]
      })
    },
    drawOnePoint(lng,lat){
      this.mapObj.removeLayer(this.pointLayer)
      const iconStyle = new Style({
        image: new Icon({
          anchor: [13, 23],
          anchorXUnits: "pixels", //anchorX单位
          anchorYUnits: "pixels", //anchorY单位
          src: require("@/assets/images/mapsearch.png"), //图片路径
          title: "",
          scale: 1,
        })
      });
      let vectorSource = new VectorSource({});
      const iconFeature = new Feature(new Point([lng,lat]));
      iconFeature.setStyle(iconStyle);
      vectorSource.addFeature(iconFeature);
      this.pointLayer = new Vector({
        title: iconStyle.title,
        source: vectorSource
      });
      this.mapObj.addLayer(this.pointLayer);
    },
    closeMapSelectDialog(){
      this.$emit('closeMapSelectDialog')
    },
    surePoint(){
      this.$emit('selectPoint', this.selectPointLng, this.selectPointLat)
    }
  }
}
</script>

<style lang='scss' scoped>
  .mapSelect {
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    .point_msg{
      padding: 24px 0 16px 0;
      font-size: 16px;
      color: #333;
      span{
        margin-right: 40px;
      }
    }
    #mapTarget{
      width: 100%;
      height: 500px;
    }
  }
</style>