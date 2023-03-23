<!--
 * @Date: 2022-05-11 10:46:51
 * @Author: 
 * @Description: 风险图
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-10-14 14:09:55
 * @FilePath: \lucheng\src\views\new_screen\projectScheduling\controlFloodDispatch\floodRiskMap.vue
-->
<template>
  <div class="floodRiskMap">
    <div class="tab-box">
      <el-select
        class="selectStyle drainage-basin-select"
        v-model="drainageBasinType"
        placeholder="请选择"
        @change="selectChange"
      >
        <el-option
          v-for="item in drainageBasinOptions"
          :key="item.type"
          :label="item.name"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-select class="selectStyle" v-model="floodLevel" placeholder="请选择" @change="selectChange">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"> </el-option>
      </el-select>
    </div>
    <div class="map-box" id="riskMap" ref="riskMap"></div>
    <div class="map-legend">
      <div class="legend-title" @click="handleActive">
        <span>图例</span
        ><i :class="active ? 'el-icon-d-arrow-left' : 'el-icon-d-arrow-right'" style="transform: rotate(-90deg)" />
      </div>
      <div v-show="active" class="legend-content">
        <div style="color: #00baff">淹没深度</div>
        <div v-for="item in dotArr" :key="item.id">
          <span class="dot" :class="'dot' + item.id"></span>{{ item.value }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import esri2geo from 'esri2geo';
import { Fill, Stroke, Style } from 'ol/style';
import 'ol/ol.css';
import XYZ from 'ol/source/XYZ';
import WKT from 'ol/format/WKT';
import TileLayer from 'ol/layer/Tile';
import ImageLayer from 'ol/layer/Image';
import ImageStatic from 'ol/source/ImageStatic';
import { Map, View, Overlay } from 'ol';
import { defaults as defaultControls } from 'ol/control';
import { Vector as VectorSource, TileArcGISRest } from 'ol/source';
import { Vector as VectorLayer } from 'ol/layer';
import GeoJSON from 'ol/format/GeoJSON';
import mapConfig from '@/config/mapConfig';
import { log } from 'video.js';
import { exportFloodRiskDisasterData } from '@/api/micro-service/project-manage/export';
import luchengGeoJson from '@/assets/geojson/lucheng_FeaturesToJSON.json';
export default {
  name: 'controlFloodDispatchRiskMap',
  props: {},
  components: {},
  data() {
    return {
      //地图
      map: null,
      options: [
        {
          value: '5',
          label: '5年一遇'
        },
        {
          value: '10',
          label: '10年一遇'
        },
        {
          value: '20',
          label: '20年一遇'
        },
        {
          value: '50',
          label: '50年一遇'
        }
      ],
      drainageBasinOptions: [
        {
          value: 0,
          type: 'Oujiang-river',
          name: '瓯江干流鹿城片',
          point: [120.41351584325, 28.11985355935, 120.50188318175, 28.15819003655]
        },
        {
          value: 1,
          type: 'Our-pujiang',
          name: '戍浦江片',
          point: [120.4754881641, 28.029496499, 120.5680176969, 28.1113564442]
        },
        {
          value: 2,
          type: 'WenRui-plain',
          name: '温瑞平原片',
          point: [120.5189384189, 27.7085746335, 120.8823263449, 28.0968656905]
        }
      ],
      floodLevel: '5',
      drainageBasinType: 0,
      riskLayer: null,
      active: true,
      dotArr: [
        { id: 1, value: '>3' },
        { id: 2, value: '2 ~ 3' },
        { id: 3, value: '1.5 ~ 2' },
        { id: 4, value: '1 ~ 1.5' },
        { id: 5, value: '0.5 ~ 1' },
        { id: 6, value: '0.2 ~ 0.5' }
      ],
      windWaveBaseLayers: null
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.mapInit();
    this.selectChange(this.floodLevel);
  },
  methods: {
    mapInit() {
      let mainLayer = new TileLayer(
        {
          id: 'zhejiangluchengmainSLLayer',
          source: new XYZ({
            url:
              mapConfig.mapServerTwo +
              'wzgt25/147a2cb528474bc78ba28632c16d037c/arcgis/rest/services/CGCS2000/2020yx_cgcs2000/MapServer/tile/{z}/{y}/{x}',
            projection: 'EPSG:4326'
          })
        },
        { zoomOffset: 1 }
      );
      // view设置
      let viewSetting = new View({
        projection: 'EPSG:4326',
        center: [120.53062753412031, 28.054533108165375],
        zoom: 12,
        maxZoom: 20,
        minZoom: 3
        // extent: [118.245, 26.51, 122.752, 29.45],
      });
      this.map = new Map({
        target: 'riskMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [mainLayer],
        // layers: [tiandituVec, tiandituCva, mainLayer],
        view: viewSetting
      });

      console.log(this.map.getViewport());
      //画布样式
      setTimeout(() => {
        const img = new Image();
        img.src = require('@/assets/images/mengceng.png');
        console.log(this.map.getViewport().querySelector('canvas'));
        const canvas = this.map.getViewport().querySelector('canvas');
        const ctx = canvas.getContext('2d');
        this.map.on(
          'postcompose',
          function () {
            // 可以实现动画效果
            ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
            this.render();
          },
          this.map
        );
      }, 100);
      //   this.registeredEvent(); //注册事件
      //   this.addStationOverlay();
      this.addLuchengGeoJsonLayer();
    },
    selectChange() {
      if (this.riskLayer) {
        this.map.removeLayer(this.riskLayer);
        this.riskLayer = null;
      }
      let imgName = `${this.drainageBasinOptions[this.drainageBasinType].type}${this.floodLevel}`;
      console.log(`@/assets/images/custom_card/${imgName}.png`);
      let imgSrc = require(`@/assets/images/custom_card/${imgName}.png`);
      let point = this.drainageBasinOptions[this.drainageBasinType].point;
      this.riskLayer = new ImageLayer({
        id: 'riskLayer',
        source: new ImageStatic({
          url: imgSrc, //这里添加静态图片的地址
          projection: 'EPSG:4326',
          imageExtent: point
        }),
        opacity: 0.5,
        zIndex: 10
      });
      this.map.addLayer(this.riskLayer);
    },
    handleActive() {
      this.active = !this.active;
    },
    //加载鹿城流域图
    addLuchengGeoJsonLayer() {
      let vectorSource = new VectorSource({
        features: new GeoJSON().readFeatures(luchengGeoJson)
      });
      if (this.windWaveBaseLayers != null) {
        this.map.removeLayer(this.windWaveBaseLayers);
        this.windWaveBaseLayers = null;
      }
      this.windWaveBaseLayers = new VectorLayer({
        source: vectorSource,
        opacity: 0.85,
        style: new Style({
          stroke: new Stroke({
            color: '#FF3F00',
            width: 2
          }),
          fill: new Fill({
            color: 'rgba(0, 196, 255, 0)'
          })
        }),
        zIndex: 1
      });
      this.map.addLayer(this.windWaveBaseLayers);
    }
  }
};
</script>

<style scoped lang="scss">
.floodRiskMap {
  position: relative;
  height: 100%;
  .tab-box {
    position: absolute;
    right: 2%;
    top: 23px;
    z-index: 100;
  }
  .map-box {
    height: 100%;
  }
  .map-legend {
    position: absolute;
    width: 130px;
    right: 2%;
    bottom: 35px;
    z-index: 100;
    color: #fff;
    padding: 11px;
    box-sizing: border-box;
    background: url(~assets/images/ScreenAsset/drag-card-icon.png) no-repeat;
    background-size: 100% 100%;
    background-color: rgba($color: #031a2b, $alpha: 0.9);
    .legend-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 7px;
      //   background: linear-gradient(180deg, #026fe0 0%, #0250e0 100%);
      //   opacity: 0.49;
    }
    .legend-content {
      > div {
        margin-bottom: 8px;
      }
      .dot {
        position: relative;
        top: 2px;
        display: inline-block;
        width: 16px;
        height: 16px;
        border-radius: 50%;
        background: #f40005;
        margin-right: 5px;
      }
      .dot2 {
        background: #f8a501;
      }
      .dot3 {
        background: #ffda18;
      }
      .dot4 {
        background: #4fff01;
      }
      .dot5 {
        background: #56ffff;
      }
      .dot6 {
        background: #1c27ff;
      }
    }
  }
}
.selectStyle {
  ::v-deep .el-input__inner {
    width: 114px;
    height: 24px;
    background: rgba(0, 186, 255, 0.1);
    border-radius: 2px;
    border: 1px solid rgba(0, 186, 255, 0.54);
    line-height: 24px;
  }
  ::v-deep .el-input__icon {
    line-height: 24px;
  }
  &.drainage-basin-select {
    margin-right: 16px;
    ::v-deep .el-input__inner {
      width: 150px;
    }
    ::v-deep .el-input__icon {
      line-height: 24px;
    }
  }
}
</style>
