<!--
 * @Author: wqn
 * @Date: 2022-09-05 09:48:02
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-23 14:33:08
 * @Description: 防洪调度图
-->
<template>
  <div class='control_flood_dispatch'>
    <div v-show="baseMapIsShow" class="map_container" id="controlFloodDispatchMap">
      <div class="map_legend">
        <i v-for="i in 4" :key="'map_legend_corner_' + i"></i>
        <div class="title" @click="mapLegendIsShow = !mapLegendIsShow">
          <span>图例</span>
          <span class="legend_arrow" :class="{'rotate_arrow': mapLegendIsShow}"></span>
        </div>
        <div class="content" :class="{hidden: !mapLegendIsShow}">
          <div class="subtitle">断面</div>
          <div class="legend_box">
            <div class="legend_item"><i class="green"></i>正常</div>
            <div class="legend_item"><i class="yellow"></i>超保证</div>
            <div class="legend_item"><i class="red"></i>超警戒</div>
          </div>
        </div>
      </div>
      <div class="rehearsal_button_box">
        <img v-show="isSimulationRehearsal || isHistoricalRehearsal" src="@/assets/images/ourPujiang/rehearsal-quit.png" @click="rehearsalQuit" />
        <img v-show="isSimulationRehearsal || isHistoricalRehearsal" src="@/assets/images/ourPujiang/rehearsal-save.png" @click="rehearsalSave" />
        <img v-show="isHistoricalRehearsal" src="@/assets/images/ourPujiang/rehearsal-edit.png" @click="rehearsalEdit" />
        <img v-show="isHistoricalRehearsal" src="@/assets/images/ourPujiang/rehearsal-delete.png" @click="rehearsalDelete" />
      </div>
      <div class="rehearsal_plan_box">
        <el-select v-show="isSimulationRehearsal || isHistoricalRehearsal" v-model="rehearsalPlan" placeholder="请选择">
          <el-option v-for="item in rehearsalPlanOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
    </div>
    <div v-show="!baseMapIsShow" class="map_container">
      <div class="map_legend">
        <i v-for="i in 4" :key="'map_legend_corner_' + i"></i>
        <div class="title" @click="mapLegendIsShow = !mapLegendIsShow">
          <span>图例</span>
          <span class="legend_arrow" :class="{'rotate_arrow': mapLegendIsShow}"></span>
        </div>
        <div class="content" :class="{hidden: !mapLegendIsShow}">
          <div class="subtitle">断面</div>
          <div class="legend_box">
            <div class="legend_item"><i class="green"></i>正常</div>
            <div class="legend_item"><i class="yellow"></i>超保证</div>
            <div class="legend_item"><i class="red"></i>超警戒</div>
          </div>
        </div>
      </div>
      <div class="rehearsal_button_box">
        <img src="@/assets/images/ourPujiang/rehearsal-contrast.png" @click="quitContrast" />
      </div>
      <div class="map_box" id="controlFloodDispatchLeftMap">
        <div class="rehearsal_plan_box">
          <el-select v-model="rehearsalPlan1" placeholder="请选择">
            <el-option v-for="item in rehearsalPlanOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
      </div>
      <div class="map_box" id="controlFloodDispatchRightMap">
        <div class="rehearsal_plan_box">
          <el-select v-model="rehearsalPlan2" placeholder="请选择">
            <el-option v-for="item in rehearsalPlanOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
      </div>
    </div>
    <div id="controlFloodDispatchMapOverlys"></div>
    <div v-if="chartIsShow" class="chart_box">
      <div class="chart_item">
        <chartTitle>预测</chartTitle>
        <chartForecast class="chart"></chartForecast>
      </div>
      <div class="chart_item">
        <chartTitle>预警</chartTitle>
        <chartEarlyWarn class="chart"></chartEarlyWarn>
      </div>
      <div class="chart_item">
        <chartTitle>预演</chartTitle>
        <chartRehearsal class="chart" @simulationRehearsal="simulationRehearsal" @schemeComparison="schemeComparison" @historicalRehearsal="historicalRehearsal"></chartRehearsal>
      </div>
      <div class="chart_item">
        <chartTitle>预案</chartTitle>
        <chartReservePlan class="chart"></chartReservePlan>
      </div>
    </div>
    <img v-show="baseMapIsShow && !isSimulationRehearsal && !isHistoricalRehearsal" class="fold_btn" :class="{unfold: chartIsShow}" src="@/assets/new_images/control_flood_dispatch_fold_btn.png" alt="" @click="chartDisplayToggle" />
    <template v-if="!baseMapIsShow || isSimulationRehearsal || isHistoricalRehearsal">
      <div class="timeline-box">
        <div class="start date">{{ startDateTime }}</div>
        <div class="timeline-slider">
          <el-slider v-model="dateValue" :min="dateMin" :max="dateMax" :format-tooltip="formatTooltip"></el-slider>
        </div>
        <div class="end date">{{ endDateTime }}</div>
      </div>
      <div class="control-box">
        <div class="play-speed">
          播放速度：
          <span class="speed-item" :style="{ backgroundColor: speed === 0.5 ? '#026FE0' : 'transparent' }" @click="speed = 0.5">×0.5</span>
          <span class="speed-item" :style="{ backgroundColor: speed === 1 ? '#026FE0' : 'transparent' }" @click="speed = 1">×1</span>
          <span class="speed-item" :style="{ backgroundColor: speed === 2 ? '#026FE0' : 'transparent' }" @click="speed = 2">×2</span>
          <span class="speed-item" :style="{ backgroundColor: speed === 4 ? '#026FE0' : 'transparent' }" @click="speed = 4">×4</span>
        </div>
        <div class="start-pause-btn" @click="isStart = !isStart">
          <i v-show="!isStart" class="start-icon"></i>
          <i v-show="isStart" class="pause-icon"></i>
        </div>
      </div>
    </template>
    <PrimaryMapDialog :mainTitle="mainTitle" :visible="dialogVisible" @handleClose="dialogVisible=false" :appendToBody="true" :modal="true" top="6vh" width="1296px">
      <div style="height: 719px;">
        <sectionDetails :id="sectionId" />
      </div>
    </PrimaryMapDialog>
    <PrimaryMapDialog :mainTitle="saveRehearsalTitle" :visible="saveRehearsalDialogVisible" @handleClose="saveRehearsalDialogVisible=false" :appendToBody="true" :modal="true" top="30vh" width="596px">
      <div class="dialog-content">
        <el-form ref="form" class="form-box-super" :rules="rules" :model="form" label-width="93px">
          <el-form-item label="演练名称：" prop="name">
            <el-input v-model="form.name" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="异常断面：" prop="warnType">
            <el-select style="width: 100%" v-model="form.warnType" placeholder="请选择">
              <el-option v-for="item in warnTypeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button style="color: #00BAFF;border-color: #00BAFF;" type="primary" size="small" plain @click="saveCancel">取消</el-button>
          <el-button style="background: linear-gradient(180deg, #36C0E4 0%, #0639FF 100%)" type="primary" size="small" plain @click="saveConfirm">确定</el-button>
        </div>
      </div>
    </PrimaryMapDialog>
  </div>
</template>

<script>
import { PrimaryMapDialog } from '@/components';
import chartTitle from './chartTitle.vue';
import chartForecast from './chartForecast.vue';
import chartEarlyWarn from './chartEarlyWarn.vue';
import chartRehearsal from './chartRehearsal.vue';
import chartReservePlan from './chartReservePlan.vue';
import sectionDetails from './sectionDetails.vue';
import 'ol/ol.css';
import { Map, View, Feature } from 'ol';
import { defaults as defaultControls } from 'ol/control';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import Overlay from 'ol/Overlay';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import Point from 'ol/geom/Point';
import { Style, Icon, RegularShape, Fill, Stroke } from 'ol/style';
// import mapConfig from '@/config/mapConfig';
import { HeavyRainsArchive } from '@/api/bigScreenPart';
export default {
  name: 'controlFloodDispatch',
  props: {
    pointData: {
      type: Array,
      defaults: () => []
    },
    overlayData: {
      type: Array,
      defaults: () => []
    }
  },
  components: {
    PrimaryMapDialog,
    chartTitle,
    chartForecast,
    chartEarlyWarn,
    chartRehearsal,
    chartReservePlan,
    sectionDetails
  },
  data() {
    return {
      chartIsShow: false,
      baseMapIsShow: true,
      map: null,
      leftMap: null,
      rightMap: null,
      view: new View({
        projection: 'EPSG:4326',
        center: [120.57, 28.05],
        zoom: 12,
        maxZoom: 16,
        minZoom: 3
      }),
      markersLayer: {},
      mapLegendIsShow: true,
      dialogVisible: false,
      mainTitle: '',
      sectionId: '',
      isSimulationRehearsal: false, // 是否为模拟预演
      isHistoricalRehearsal: false, // 是否为历史预演
      rehearsalInfo: {}, // 历史预演信息
      rehearsalPlan: '',
      rehearsalPlan1: '',
      rehearsalPlan2: '0',
      rehearsalPlanOptions: [
        { value: '', label: '不考虑未来' },
        { value: '0', label: '预报降雨' },
        { value: '1', label: '典型暴雨-1' },
        { value: '2', label: '典型暴雨-2' },
      ],
      saveRehearsalDialogVisible: false,
      saveRehearsalTitle: '',
      form: {
        name: '',
        warnType: '异常断面1'
      },
      warnTypeOptions: [
        { value: '异常断面1', label: '根据演练结果生成'}
      ],
      rules: {
        name: [{ required: true, message: '请填写名称', trigger: 'blur' }],
      },
      dateMin: 0,
      dateValue: 0,
      dateMax: 24,
      startDateTime: '2023.03.23 08:00',
      endDateTime: '2023.03.24 07:00',
      speed: 1,
      isStart: false,
      timer: null
    };
  },
  computed: {},
  watch: {
    baseMapIsShow(value) {
      if (!value) {
        this.$nextTick(() => {
          this.leftMapInit();
          this.rightMapInit();
        });
      }
    },
    pointData(value) {
      this.drawPoint(value);
    },
    overlayData(value) {
      this.drawOverlays(value);
    },
    isStart(val) {
      clearInterval(this.timer);
      if (val) {
        this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
        this.timer = setInterval(() => {
          this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
        }, 3000 / this.speed);
      }
    },
    speed(val) {
      clearInterval(this.timer);
      this.timer = setInterval(() => {
        this.dateValue === this.dateMax ? (this.dateValue = 0) : this.dateValue++;
      }, 3000 / val);
    },
    dateValue(val) {
      // 获取新的地图数据
    }
  },
  created() {},
  mounted() {
    this.mapInit();
  },
  destroyed() {},
  methods: {
    /**
     * @description 初始化地图
     */
    mapInit() {
      this.map = new Map({
        target: 'controlFloodDispatchMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023',
                projection: 'EPSG:4326'
              })
            },
            { zoomOffset: 1 }
          ),
          new TileLayer({
            source: new XYZ({
              url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_border/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous'
            }),
            preload: 1,
            visible: true
          }),
          new TileLayer({
            source: new XYZ({
              url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_river/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous'
            }),
            preload: 1,
            visible: true
          })
        ],
        view: this.view
      });
      this.map.on('singleclick', (evt) => {
        // console.log([evt.coordinate[0], evt.coordinate[1]]);
        const pixel = this.map.getEventPixel(evt.originalEvent);
        const feature = this.map.forEachFeatureAtPixel(pixel, (features) => features);
        if (feature) {
          if (feature.values_.type && feature.values_.type === '断面测站') {
            this.mainTitle = feature?.values_?.name ?? '-';
            this.sectionId = feature?.values_?.id ?? '';
            this.dialogVisible = true;
          }
        }
      });
    },
    /**
     * @description 初始化方案对比时左边地图
     */
    leftMapInit() {
      this.leftMap = new Map({
        target: 'controlFloodDispatchLeftMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023',
                projection: 'EPSG:4326'
              })
            },
            { zoomOffset: 1 }
          ),
          new TileLayer({
            source: new XYZ({
              url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_border/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous'
            }),
            preload: 1,
            visible: true
          })
        ],
        view: this.view
      });
    },
    /**
     * @description 初始化方案对比时右边地图
     */
    rightMapInit() {
      this.leftMap = new Map({
        target: 'controlFloodDispatchRightMap',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [
          new TileLayer(
            {
              source: new XYZ({
                url:
                  'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023',
                projection: 'EPSG:4326'
              })
            },
            { zoomOffset: 1 }
          ),
          new TileLayer({
            source: new XYZ({
              url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_border/MapServer/tile/{z}/{y}/{x}',
              projection: 'EPSG:4326',
              crossOrigin: 'anonymous'
            }),
            preload: 1,
            visible: true
          })
        ],
        view: this.view
      });
    },
    /**
     * @description 切换图表的展示与否
     */
    chartDisplayToggle() {
      this.chartIsShow = !this.chartIsShow;
    },
    /**
     * @description 模拟预演
     */
    simulationRehearsal() {
      this.isSimulationRehearsal = true
      this.chartIsShow = false
    },
    /**
     * @description 历史预演
     */
    historicalRehearsal(info) {
      this.isHistoricalRehearsal = true
      this.chartIsShow = false
      this.rehearsalInfo = info
    },
    /**
     * @description 退出预演
     */
    rehearsalQuit() {
      this.isSimulationRehearsal = false
      this.isHistoricalRehearsal = false
      this.rehearsalInfo = {}
    },
    /**
     * @description 保存预演
     */
    rehearsalSave() {
      this.form = {
        name: '',
        warnType: '异常断面1'
      }
      this.saveRehearsalTitle = '保存'
      this.saveRehearsalDialogVisible = true
    },
    /**
     * @description 编辑预演
     */
    rehearsalEdit() {
      this.form = {
        name: this.rehearsalInfo?.name ?? '',
        warnType: '异常断面1'
      }
      this.saveRehearsalTitle = '编辑'
      this.saveRehearsalDialogVisible = true
    },
    /**
     * @description 取消保存/编辑预演
     */
    saveCancel() {
      this.$refs['form'].clearValidate()
      this.saveRehearsalDialogVisible = false
    },
    /**
     * @description 确定保存/编辑预演
     */
    saveConfirm() {
      this.$refs['form'].validate((valid) => {
        console.log(valid)
        if (valid) {
          this.saveRehearsalDialogVisible = false
        } else {
          return false
        }
      })
    },
    /**
     * @description 删除预演
     */
    rehearsalDelete() {
      this.$confirm("此操作将删除该预演, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          console.log(this.rehearsalInfo)
          if (true) {
            this.$message({
              message: "删除成功",
              type: "success",
            });
            this.rehearsalQuit()
          }
        })
        .catch(() => { });
    },
    /**
     * @description 方案对比
     */
    schemeComparison() {
      this.chartIsShow = false
      this.baseMapIsShow = false
    },
    /**
     * @description 退出对比
     */
    quitContrast() {
      this.baseMapIsShow = true
    },
    /**
     * @description 绘制多浮窗
     */
    drawOverlays(overlayData) {
      document.getElementById('controlFloodDispatchMapOverlys').innerHTML = '';
      Object.keys(this.markersLayer).forEach((e) => {
        if (this.markersLayer[e] != null) {
          this.map.removeOverlay(this.markersLayer[e]);
        }
      });
      this.markersLayer = [];
      overlayData.forEach((item, i) => {
        let contentHtml = '';
        if (item.type === '潮位测站') {
          contentHtml = `
            <div class="overly_title">${item.name || '--'}</div>
            <div class="overly_item">实时潮位<span class="value">${item.waterLevel || '--'}</span>m</div>
            <div class="overly_item">更新时间<span class="value">${item.updateTime ? item.updateTime.substring(5, 16) : '--'}</span></div>
            <div class="overly_item">预报高潮位<span class="value">${item.ybTdz || '--'}</span>m</div>
            <div class="overly_item">预报高潮位时间<span class="value">${item.ybTime ? item.ybTime.substring(5, 16) : '--'}</span></div>
          `;
        } else if (item.type === '水库测站') {
          contentHtml = `
            <div class="overly_title">${item.name || '--'}</div>
            <div class="overly_item">水位<span class="value">${item.waterLevel || '--'}</span>m</div>
            <div class="overly_item">放水状态<span class="value">--</span></div>
            <div class="overly_item">更新时间<span class="value">${item.updateTime ? item.updateTime.substring(5, 16) : '--'}</span></div>
          `;
        } else if (item.type === '水闸测站') {
          contentHtml = `
            <div class="overly_title">${item.name || '--'}</div>
            <div class="overly_item">闸上水位<span class="value">${item.upz || '--'}</span>m</div>
            <div class="overly_item">闸下水位<span class="value">${item.dwz || '--'}</span>m</div>
            <div class="overly_item">闸门状态<span class="value">${
              item.openGate ? (Number(item.openGate) > 0 ? '开启中' : '关闭中') : '--'
            }</span></div>
            <div class="overly_item">更新时间<span class="value">${item.updateTime ? item.updateTime.substring(5, 16) : '--'}</span></div>
          `;
        } else if (item.type === '断面测站') {
          contentHtml = `
            <div class="overly_title">${item.name || '--'}</div>
            <div class="overly_item">实时水位<span class="value">${item.waterLevel || '--'}</span>m</div>
            <div class="overly_item">实时流量<span class="value">--</span></div>
            <div class="overly_item">更新时间<span class="value">${item.updateTime ? item.updateTime.substring(5, 16) : '--'}</span></div>
          `;
        }
        document.getElementById(
          'controlFloodDispatchMapOverlys'
        ).innerHTML += `<div id='overly${i}' class='overlyStyle'>${contentHtml}</div>`;
        this.markersLayer['overly' + i] = new Overlay({
          offset: [0, -12],
          positioning: 'bottom-center',
          position: [item.lgtd, item.lttd],
          element: document.getElementById('overly' + i)
        });
        this.map.addOverlay(this.markersLayer['overly' + i]);
      });
    },
    /**
     * @description 地图打点
     */
    drawPoint(pointData) {
      const vectorSource = new VectorSource({});
      pointData.forEach((e) => {
        const iconFeature = new Feature({
          geometry: new Point([Number(e.lgtd), Number(e.lttd)]),
          type: e.type,
          id: e.dmId || e.stCode,
          name: e.name
        });
        if (e.type === '断面测站') {
          iconFeature.setStyle(
            new Style({
              image: new RegularShape({
                points: 3,
                radius: 10,
                fill: new Fill({
                  color: e.status == 1 ? '#FCEE27' : e.status == 2 ? '#FF0000' : '#19AF47'
                }),
                stroke: new Stroke({
                  color: '#FFFFFF',
                  size: 2
                })
              })
            })
          );
        } else {
          iconFeature.setStyle(
            new Style({
              image: new Icon({
                anchor: [0.5, 0.5],
                anchorXUnits: 'fraction',
                anchorYUnits: 'fraction',
                src:
                  e.type === '潮位测站'
                    ? require('@/assets/new_images/control_flood_dispatch_map_tideLevel_icon.png')
                    : e.type === '水库测站'
                    ? require('@/assets/new_images/control_flood_dispatch_map_reservoir_icon.png')
                    : require('@/assets/new_images/control_flood_dispatch_map_sluice_icon.png'),
                scale: 1
              })
            })
          );
        }
        vectorSource.addFeature(iconFeature);
      });
      const vectorLayer = new VectorLayer({
        source: vectorSource
      });
      this.map.addLayer(vectorLayer);
    },
    /**
     * @description 格式化滑块的Tooltip
     */
    formatTooltip(val) {
      return this.getDateTimeStr(new Date(Date.parse(this.startDateTime) + val * 60 * 60 * 1000));
    },
    /**
     * @description 获取MM-dd HH:00格式的时间字符串
     */
    getDateTimeStr(d) {
      return `${d.getFullYear()}.${handleSingleDigit(d.getMonth() + 1)}.${handleSingleDigit(
        d.getDate()
      )} ${handleSingleDigit(d.getHours())}:00`;
      function handleSingleDigit(value) {
        return value < 10 ? '0' + value : value;
      }
    },
  }
};
</script>
<style lang='scss' scoped>
.control_flood_dispatch {
  position: relative;
  width: 100%;
  height: 100%;
  .map_container {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: space-between;
    .map_box {
      position: relative;
      width: calc(50% - 7px);
      height: 100%;
      .back_btn {
        position: absolute;
        left: 20px;
        top: 15px;
        width: 77px;
        height: 28px;
        z-index: 1;
        cursor: pointer;
      }
    }
    .map_legend {
      position: absolute;
      left: 0;
      bottom: 0;
      background: linear-gradient(180deg, rgba(6, 29, 54, 0.7) 0%, #051832 100%);
      box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
      border: 1px solid #00c1ff;
      z-index: 3;
      > i {
        position: absolute;
        width: 18px;
        height: 16px;
        border-image: linear-gradient(180deg, rgba(53, 185, 227, 1), rgba(96, 138, 255, 1)) 3 3;
        &:nth-child(1) {
          left: -3px;
          top: -3px;
          border-left: 3px solid;
          border-top: 3px solid;
        }
        &:nth-child(2) {
          top: -3px;
          right: -3px;
          border-top: 3px solid;
          border-right: 3px solid;
        }
        &:nth-child(3) {
          right: -3px;
          bottom: -3px;
          border-right: 3px solid;
          border-bottom: 3px solid;
        }
        &:nth-child(4) {
          bottom: -3px;
          left: -3px;
          border-bottom: 3px solid;
          border-left: 3px solid;
        }
      }
      .title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 15px;
        height: 40px;
        background: linear-gradient(270deg, rgba(12, 61, 54, 0.02) 0%, rgba(7, 157, 254, 0.4) 100%);
        font-size: 16px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        line-height: 40px;
        cursor: pointer;
        .legend_arrow {
          width: 13px;
          height: 9px;
          background-image: url('~@/assets/new_images/projectScheduling/legend-arrow.png');
          background-size: 100% 100%;
          background-repeat: no-repeat;
          transform: rotate(180deg);
          &.rotate_arrow {
            transform: rotate(0);
          }
        }
      }
      .content {
        overflow: hidden;
        box-sizing: border-box;
        height: 74px;
        width: 280px;
        padding: 7px 21px 21px 15px;
        &.hidden {
          height: 0;
          padding: 0 21px 0 15px;
        }
        .subtitle {
          height: 24px;
          font-size: 14px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #00baff;
          line-height: 24px;
          margin-bottom: 3px;
          cursor: pointer;
        }
        .legend_box {
          display: flex;
          justify-content: space-between;
          .legend_item {
            display: flex;
            align-items: center;
            height: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 20px;
            > i {
              display: inline-block;
              width: 12px;
              height: 5px;
              margin-right: 6px;
              &.green {
                background-color: #00ffa4;
              }
              &.yellow {
                background-color: #fdf9c4;
              }
              &.red {
                background-color: #ff0200;
              }
            }
          }
        }
      }
    }
    .rehearsal_button_box {
      position: absolute;
      left: 16px;
      top: 12px;
      z-index: 3;
      img {
        display: block;
        width: 187px;
        height: 58px;
        cursor: pointer;
        + img {
          margin-top: 24px;
        }
      }
    }
    .rehearsal_plan_box {
      position: absolute;
      top: 15px;
      right: 30px;
      z-index: 3;
      ::v-deep .el-select {
        .el-input__inner {
          width: 150px;
          height: 32px;
          background-color: rgba(0, 186, 255, 0.1);
          border-radius: 2px;
          border: 1px solid rgba(0, 186, 255, 0.54);
          line-height: 30px;
          font-size: 14px;
          font-weight: 400;
          color: #00BAFF;
        }
        .el-input__icon {
          line-height: 30px;
        }
      }
    }
  }
  .chart_box {
    display: flex;
    justify-content: space-between;
    position: absolute;
    bottom: 78px;
    width: 100%;
    .chart_item {
      z-index: 4;
      overflow: hidden;
      position: relative;
      box-sizing: border-box;
      width: 460px;
      height: 296px;
      background: url(~@/assets/new_images/control_flood_dispatch_chart_bgi.png);
      background-size: 100% 100%;
      box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
      padding: 15px;
      .chart {
        height: calc(100% - 40px);
        margin-top: 10px;
      }
    }
  }
  .fold_btn {
    position: absolute;
    width: 119px;
    height: 119px;
    left: 0;
    right: 0;
    margin: auto;
    bottom: 57px;
    cursor: pointer;
    &.unfold {
      bottom: 364px;
      transform: rotateX(180deg);
    }
  }
  .timeline-box {
    position: absolute;
    display: flex;
    box-sizing: border-box;
    width: 100%;
    height: 22px;
    bottom: 125px;
    padding: 0 50px;
    .date {
      width: 150px;
      height: 22px;
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 22px;
      &.end {
        text-align: right;
      }
    }
    .timeline-slider {
      width: calc(100% - 300px);
      ::v-deep .el-slider__runway {
        margin: 8.5px 0;
        background-color: rgba(252, 238, 39, 0.33);
        .el-slider__bar {
          background-color: transparent;
          background-image: linear-gradient(270deg, #ffda00 0%, rgba(255, 218, 0, 0) 100%);
        }
        .el-slider__button-wrapper {
          .el-slider__button {
            border: none;
            background-color: #ffda00;
            box-shadow: 0px 0px 10px 0px #ffdb00;
          }
        }
      }
    }
  }
  .control-box {
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    width: calc(100% - 28px);
    height: 38px;
    bottom: 80px;
    .play-speed {
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 38px;
      margin-right: 35px;
      .speed-item {
        display: inline-block;
        box-sizing: border-box;
        border: 1px solid #026fe0;
        padding: 0 16px;
        height: 30px;
        line-height: 30px;
        cursor: pointer;
        &:first-child {
          border-radius: 4px 0px 0px 4px;
        }
        &:last-child {
          border-radius: 0px 4px 4px 0px;
        }
        &:not(:first-child) {
          border-left: none;
        }
      }
    }
    .start-pause-btn {
      position: relative;
      width: 38px;
      height: 38px;
      background-color: #026fe0;
      border-radius: 50%;
      cursor: pointer;
      i {
        position: absolute;
        display: inline-block;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
      }
      .start-icon {
        width: 0;
        height: 0;
        border-top: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 12px solid #ffffff;
        transform: translateX(2px);
      }
      .pause-icon {
        width: 12px;
        height: 14px;
        border-top: none;
        border-bottom: none;
        border-left: 4px solid #ffffff;
        border-right: 4px solid #ffffff;
      }
    }
  }
}
.dialog-footer {
  position: absolute;
  right: 20px;
  bottom: 20px;
  .el-button--primary.is-plain {
    color: #fff;
    border-color: #0bbbff;
    background-color: rgba($color: #0bbbff, $alpha: 0.1);
  }
}
</style>
<style>
.overlyStyle {
  padding: 10px;
  border: 1px solid #00baff;
  background-image: linear-gradient(to bottom, rgba(0, 148, 195, 0.8), rgba(0, 30, 70, 0.8));
  border-radius: 2px;
}
.overlyStyle > .overly_title {
  height: 22px;
  font-size: 16px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #ffffff;
  line-height: 22px;
}
.overlyStyle > .overly_item {
  height: 20px;
  font-size: 14px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #ffffff;
  line-height: 20px;
}
.overlyStyle > .overly_item > span.value {
  height: 23px;
  font-size: 16px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  color: #23e3fb;
  line-height: 22px;
  margin: 0 10px;
}
</style>