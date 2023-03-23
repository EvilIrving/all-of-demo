<template>
  <div class="project-schedule-map">
    <!-- 地图挂载元素 -->
    <div class="map" id="watershedScheduleMap" />
    <!-- 点位悬浮框 -->
    <div v-show="popupShow" id="popup" class="ol-popup">
      <!-- , isBigScrren && 'is-big-screen' -->
      <div :class="['ol-popup__wrapper']">
        <div class="ol-popup__title">{{ featureInfo.name }}</div>
        <div class="ol-popup__content">
          <div class="ol-popup__content--props">
            <div v-if="featureInfo.label" class="props-label">
              <span> {{ featureInfo.label }}</span>
            </div>
            <template v-else>
              <div v-for="(prop, key) of featureInfo.props" :key="key" class="props-item">
                <span class="item-label">{{ key }}</span>
                <span class="item-data" :style="{ color: key == '汛限水位' ? '#eec80b' : '#1cfff5' }">
                  {{ prop == 'null' ? '-' : prop == 'null m' ? '- m' : prop }}
                </span>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
    <!-- 左侧边栏 -->
    <screen-silde-panel position="left" :height="260">
      <panelInfo
        :count="countData"
        v-model="checkInfo"
        @change="onChangeSetting"
        @changeChecked="onChangeChecked"
        @handle-active="onHandleActive"
        @showNumber="showNumber"
      />
    </screen-silde-panel>
    <!-- 名称、水位、流量  流域选择 -->
    <!-- <div class="action-widget">
			<el-select v-model="basin" @change="onChangeSetting">
				<el-option label="瓯江" value="瓯江" />
				<el-option label="飞云江" value="飞云江" />
				<el-option label="鳌江" value="鳌江" />
			</el-select>
			<CheckedInfo v-model="checkInfo" @change="onChangeSetting" />
		</div> -->
    <slot></slot>
  </div>
</template>
<script>
import 'ol/ol.css';
import { Map, Overlay } from 'ol';
import {
  scheduleBgLayer,
  ViewSetting,
  getFeatureStyle,
  getIconFeature,
  GeoJSONBasMap,
  MergeData,
  getFeaturesFromSuorce,
  addLayerClick
} from '../utils/map.js';
import { defaults as defaultControls } from 'ol/control';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';

import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index';

import CheckedInfo from './checkedInfo.vue';
import ScreenSildePanel from './ScreenSildePanel/index.vue';
import panelInfo from './panelInfo.vue';
import M from 'minimatch';

export default {
  name: 'WatershedScheduleMap',
  components: {
    CheckedInfo,
    ScreenSildePanel,
    panelInfo
  },
  data() {
    return {
      map: null,
      // 图层
      baseLineLayer: null,
      generalPointLayer: null,
      overlay: null,
      // 点位信息
      featureInfo: {},
      popupShow: false,
      //feature数据
      featurePoint: null,
      checkInfo: ['名称'],
      basin: '瓯江',
      projectDialogVisible: false,
      highlightArr: [],
      countData: {
        skWarn: [],
        szWarn: []
      },
      pointData: {}
    };
  },
  mounted() {
    this.initMap();
    this.getHydrProjStaDiagram('瓯江');
  },
  methods: {
    showNumber(type, boolenType) {
      const geoJSON = GeoJSONBasMap['瓯江'];
      console.log(boolenType);
      if (boolenType) {
        if (type == 1) {
          if (this.countData.skWarn.length > 0) {
            let data = MergeData(this.countData.skWarn, geoJSON, type);
            this.drawGeneralPoint(getFeaturesFromSuorce(data.points));
          }
        } else {
          console.log(this.countData.szWarn, 'this.countData.skWarnthis.countData.skWarnthis.countData.skWarn');
          if (this.countData.szWarn.length > 0) {
            let data = MergeData(this.countData.szWarn, geoJSON, type);
            console.log(data, 'dasdsadsa');
            this.drawGeneralPoint(getFeaturesFromSuorce(data.points));
          }
        }
      } else {
        let data = MergeData(this.pointData, geoJSON);
        this.drawGeneralPoint(getFeaturesFromSuorce(data.points));
      }
    },
    onHandleActive(arr) {
      this.$emit('handle-active', arr);
    },
    onChangeChecked(val) {
      this.checkInfo = val;
      this.onChangeSetting();
    },
    onChangeSetting(val) {
      // let { basin } = this;
      this.basin = val || this.basin;
      this.$emit('set-basin', this.basin);
      this.getHydrProjStaDiagram(this.basin);
    },
    /** 初始化地图，挂载至元素 */
    initMap() {
      this.map = new Map({
        target: 'watershedScheduleMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [scheduleBgLayer],
        view: ViewSetting
      });
      this.registeredEvent();
      this.addStationOverlay();
    },
    /**
     * 添加overlay
     */
    addStationOverlay() {
      this.overlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#popup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.overlay);
    },
    /**
     * 注册事件
     */
    registeredEvent() {
      this.map.on('click', (event) => {
        //console.log("pnMapClick", event);
      });
      this.map.on('pointermove', (event) => {
        let pixel = this.map.getEventPixel(event.originalEvent);
        let flag = false;
        this.map.getTargetElement().style.cursor = 'auto';
        this.map.forEachFeatureAtPixel(pixel, (feature) => {
          let coodinate = event.coordinate;
          let properties = feature.getProperties();
          if (properties.type == 'generalPoint') {
            let data = properties.data.values_;
            this.map.getTargetElement().style.cursor = 'pointer';
            this.featureInfo = { name: data.name, props: data.props, label: data.label };
            if (this.featureInfo) {
              this.overlay.setPosition(coodinate);
              flag = true;
            }
          }
        });
        if (flag) {
          this.popupShow = true;
        } else {
          this.popupShow = false;
        }
      });
    },
    /**
     * 获取接口数据并与GeoJSON合并
     */
    async getHydrProjStaDiagram(bas) {
      const geoJSON = GeoJSONBasMap[bas];
      // let data = MergeData(point, geoJSON);
      // console.log(data.line)
      // let features = data.line.features[1]
      // data.line.features = features
      // this.drawFlowLine(getFeaturesFromSuorce(data.line));
      // this.drawGeneralPoint(getFeaturesFromSuorce(data.points));

      let res = await disasterPreventionApi.getHydrProjStaDiagram();
      if (res.code == 0) {
        let skWarn = [];
        let szWarn = [];
        for (let key in res.data) {
          if (res.data[key].codeType == '水库工程' && res.data[key].overWarnLimit) {
            skWarn.push(res.data[key]);
          }
          if (res.data[key].codeType == '水闸工程' && res.data[key].ovs) {
            szWarn.push(res.data[key]);
          }
        }
        this.countData = {
          skWarn,
          szWarn
        };
        this.pointData = res.data;
        let data = MergeData(res.data, geoJSON);
        this.drawFlowLine(getFeaturesFromSuorce(data.line));
        this.drawGeneralPoint(getFeaturesFromSuorce(data.points));
      }
    },
    /**
     * @param {Number[]} center 中心经纬度
     * @param {Number} zoom 缩放级别
     */
    flyPoint(center, zoom) {
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom
      });
    },
    /**
     * 设置摄像机位置
     * @param {Number} lng
     * @param {Number} lat
     */
    setView(lng, lat) {
      const view = this.map.getView();
      view.animate({ center: [lng, lat], duration: 200, zoom: 14 });
    },

    /**
     * 绘制动效线段
     * @param {Array} features geoJson转化的Feature对象数组
     */
    drawFlowLine(features) {
      if (this.baseLineLayer != null) this.map.removeLayer(this.baseLineLayer);
      let source = new VectorSource({ wrapX: false });
      this.baseLineLayer = new VectorLayer({ source });
      for (let feature of features) {
        feature.setStyle(getFeatureStyle);
        feature.set('dashOffset', 0);
        setInterval(() => {
          let offset = feature.get('dashOffset');
          let offset_step = offset - 1;
          if (feature.values_.Id == 1) {
            offset_step = offset + 1;
          }
          offset = offset == 32 ? 0 : offset_step;
          feature.set('dashOffset', offset);
        }, 100);
        source.addFeature(feature);
      }
      this.map.addLayer(this.baseLineLayer);
    },
    /**
     * 绘制点位
     */
    drawPointByCount(val) {
      this.highlightArr = val;
      this.getHydrProjStaDiagram(this.basin);
    },
    /**
     * 绘制点位
     */
    drawGeneralPoint(features) {
      if (this.generalPointLayer != null) this.map.removeLayer(this.generalPointLayer);
      let source = new VectorSource();
      features.forEach((feature, index) => {
        let text = '';
        let num = 1.1;
        if (feature.get('props')) {
          if (this.checkInfo.includes('名称')) {
            text += `${feature.get('name')}`;
          }
          if (this.checkInfo.includes('水位')) {
            let level =
              feature.get('props')['水位'] || feature.get('props')['实时水位'] || feature.get('props')['闸上水位'];
            if (level) text += `\n水位:${level}`;
          }
          if (this.checkInfo.includes('流量') && feature.get('type').indexOf('水库') !== -1) {
            let flow = feature.values_.detail.flow;
            if (flow) text += `\n流量:${flow} `;
          }
        } else text = feature.get('name');
        feature.set('text', text);
        num = feature.get('type') == '水闸' ? 1.1 : num;
        const iconFeature = getIconFeature(feature, num);
        iconFeature.set('id', index);
        source.addFeature(iconFeature);
      });
      this.generalPointLayer = new VectorLayer({ source });
      this.map.addLayer(this.generalPointLayer);
      let selectClick = addLayerClick(this.generalPointLayer);
      selectClick.on('select', (e) => {
        let features = e.target.getFeatures().array_;
        if (features.length == 1) {
          let feature = features[0].values_.data;
          this.$emit('point-click', feature.values_, { info: 'ok' });

          selectClick.getFeatures().clear();
        }
      });
      this.map.addInteraction(selectClick);
    }
  }
};
</script>
<style lang="scss">
.ol-popup {
  position: absolute;
  bottom: 15px;
  left: -50%;
  transform: translateX(-50%);
  border-radius: 3px;
  min-width: 150px;
  .ol-popup__wrapper {
    background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
    border: 2px solid #00c1ff;
    box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
    border-radius: 3px;
    height: 100%;
    width: 100%;
    padding: 16px;
    position: relative;
    opacity: 0.9;
    &:before {
      content: '';
      position: absolute;
      display: block;
      box-sizing: content-box;
      width: 0px;
      height: 0px;
      border: 7px solid transparent;
      border-top: 7px solid #103567;
      border-bottom: 0;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
      z-index: 100;
    }
    &:after {
      content: '';
      position: absolute;
      display: block;
      box-sizing: content-box;
      width: 0px;
      height: 0px;
      border: 10px solid transparent;
      border-top: 10px solid #00c1ff;
      border-bottom: 0;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
    }
    .ol-popup__title {
      font-size: 18px;
      color: #fff;
    }
    .ol-popup__content {
      color: #fff;
      .ol-popup__content--props {
        display: flex;
        flex-direction: column;
        .props-item {
          // padding: 0 12px;
          position: relative;
          display: flex;
          align-items: center;
          min-width: 150px;
          width: max-content;

          .item-label {
            font-size: 16px;
          }
          .item-data {
            margin-left: 8px;
            font-size: 18px;
            color: #33ffff;
            line-height: 25px;
          }
          &:first-child {
            &:not(:only-child) {
              margin-top: 8px;
            }
          }
        }
        .props-label {
          font-size: 16px;
          // font-weight: bold;
          padding-top: 16px;
          width: auto;
        }
      }
    }
  }
}
.action-widget {
  position: absolute;
  right: 2%;
  top: 10%;
  z-index: 11;
  .checked-info {
    width: 56px;
    margin-left: auto;
  }
}
</style>
