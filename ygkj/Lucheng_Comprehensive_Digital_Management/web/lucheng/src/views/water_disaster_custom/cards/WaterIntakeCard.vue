<!--
 * @Author: 
 * @Date: 2021-07-01 13:43:37
 * @LastEditTime: 2022-11-30 10:50:13
 * @LastEditors: Please set LastEditors
 * @Description: 取水户
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\WaterIntakeCard.vue
-->
<template>
  <div class="water-card">
    <panel-item title="抗旱监管" has-switch tc @tclick="tclick('抗旱监管')">
      <template #title-switch>
        <i
          title="地图落点"
          :class="[ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
          @click="btnClick('ldbtn')"
        />
      </template>

      <div class="water-content-box">
        <div class="water-content-item">
          <div class="water-content-title">
            <span>风险监测</span>
          </div>
          <div class="histogram-box risk-box">
            <div class="risk-icon">
              <div class="risk-icon-a sche-risk-normal-04"></div>
              <div class="risk-icon-b" :class="`sche-risk-${rainfall.wrState}-02`"></div>
              <div class="risk-icon-c" :class="`sche-risk-${noRain.wrState}-03`"></div>
              <div class="risk-icon-d sche-risk-normal-01"></div>
            </div>
            <div class="risk-list">
              <div>
                <span :class="`sche-risk-normal-01`"></span>
                水源预警
              </div>
              <div>
                <span :class="`sche-risk-${rainfall.wrState}-02`"></span>
                近30日降水量
              </div>
              <div>
                <span :class="`sche-risk-${noRain.wrState}-03`"></span>
                连续无雨日数
              </div>
              <div>
                <span class="sche-risk-normal-04"></span>
                饮水困难人数
              </div>
            </div>
          </div>
        </div>
        <div class="water-content-item">
          <div class="water-content-title">
            <span>数据监管</span>
          </div>
          <div class="police-statistics">
            <div class="supervise-box" v-if="showDetail">
              <div class="supervise-icon-detail supervise-icon-01" @click="showDetail = false">
                <div>
                  <span
                    :class="
                      rainfall.analyzeResult && rainfall.analyzeResult.indexOf('-') != '-1' ? 'down-icon' : 'up-icon'
                    "
                  ></span
                  ><span>{{
                    rainfall.analyzeResult && rainfall.analyzeResult.indexOf('-') != '-1'
                      ? rainfall.analyzeResult.substring(1, rainfall.analyzeResult.length)
                      : rainfall.analyzeResult
                  }}</span
                  >%
                </div>
                <div>近30日降雨量</div>
              </div>
              <div class="supervise-icon-bg">
                <div class="supervise-icon"></div>
              </div>
              <div class="supervise-icon-box supervise-icon-box-01">
                <div class="supervise-icon-detail supervise-icon-04">
                  <div><span>48</span></div>
                  <div>水库水位</div>
                </div>
                <div class="supervise-icon-detail supervise-icon-05">
                  <div>
                    <span>{{ noRain.analyzeResult }}</span>
                  </div>
                  <div>饮水困难人数</div>
                </div>
              </div>
              <div class="supervise-icon-box">
                <div class="supervise-icon-detail supervise-icon-02">
                  <div><span>48</span></div>
                  <div>可供水天数</div>
                </div>
                <div class="supervise-icon-detail supervise-icon-03">
                  <div>
                    <span>{{ noRain.analyzeResult }}</span>
                  </div>
                  <div>连续无雨天数</div>
                </div>
              </div>
            </div>
            <div class="primary-map-table" v-else>
              <CapsuleTabs :tabs="['返回']" @change="showDetail = true" />
              <div class="is-water-box">
                <vchart-bar
                  v-if="!showDetail"
                  height="100%"
                  width="100%"
                  ref="ranksChart"
                  :barData="barData"
                  :barWidth="16"
                  barName=""
                ></vchart-bar>
              </div>
            </div>
          </div>
        </div>
        <div class="water-content-item">
          <div class="water-content-title">
            <span>降雨预报</span>
          </div>
          <div class="histogram-box">
            <div class="rainfall-no" v-if="weatherList.length == 0"></div>
            <div v-else class="rainfall-list">
              <div class="weather-box">
                <div v-for="item in weatherList" :key="item.tm">
                  <div>{{ item.tm }}</div>
                  <div>
                    <span :class="item.icon"></span>
                  </div>
                  <div>{{ item.wd }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from '../components/PanelItem.vue';
import { mapGetters } from 'vuex';
import { riskMonitoring } from '@/api/waterResourcesGuarantee';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import VchartBar from '../charts/vchart_3dbar/VchartBar.vue';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import { waterAndRain } from '@/api/bigScreenPart';
export default {
  name: 'WaterIntakeCard',
  components: {
    PanelItem,
    CapsuleTabs,
    VchartBar
  },
  data() {
    return {
      rainfall: {},
      noRain: {},
      sourceWater: {},
      weatherList: [],
      ldbtn: false,
      showDetail: true,
      barData: {
        columns: ['name', 'data'],
        rows: []
      },
      hydropowerStation: [], //水电站列表数据
      quality: [], //水质站列表数据
      waterPointList: [], //水厂列表数据
      swhWaterPointList: [], //水源地列表数据
      reservoirList: [], //水库列表数据
      sluiceList: [], //水闸列表数据
      rankingSealevelData: [], //潮位数据
      rankingReservoirData: [], //水库水位数据
      rankingCheckgateData: [], //堰闸水位数据
      rankingRiverwayData: [] //河道水位数据
    };
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {},
  created() {},
  mounted() {
    this.droughtWraning();
    this.listJsonForWeather();
    this.listDayRainfall();
    this.hysDropPoint();
    this.qualityList();
    this.listWaterWorks();
    this.AttSwhsBaseLoadPage();
    this.getProjectPoint('水库');
    this.getProjectPoint('水闸');
    this.getOverView('RR');
    this.getOverView('ZZ');
    this.getOverView('DD');
    this.getOverView('TT');
  },
  methods: {
    async droughtWraning() {
      let res = await riskMonitoring.droughtWraning({
        areaCode: '330302000000'
      });
      if (res.code == 0) {
        res.data.forEach((element) => {
          switch (element.index) {
            case '近30日降水量':
              this.rainfall = element;
              break;
            case '连续无雨日数':
              this.noRain = element;
              break;
            default:
              this.sourceWater = element;
              break;
          }
        });
      }
    },
    async listJsonForWeather() {
      let res = await riskMonitoring.listJsonForWeather({type: 'futureWeather'});
      if (res.code == 0) {
        this.weatherList = res.data.map((item) => {
          let icon = this.getWeatherIcon(item.type);
          return {
            icon,
            ...item
          };
        });
      }
    },
    async listDayRainfall() {
      let res = await riskMonitoring.listDayRainfall();
      if (res.code == 0) {
        this.barData.rows = res.data.map((item) => {
          let date = item.date.split('-');
          date.splice(0, 1);
          return {
            name: date.join('-'),
            data: item.drp
          };
        });
      }
    },
    getWeatherIcon(weatherName) {
      let icon = '';
      let qingRegex = /^晴$|转晴/;
      let yinRegex = /^多云$|^阴$|转多云|转阴/;
      let yuRegex = /雨$|/;
      if (qingRegex.exec(weatherName)) {
        icon = 'icon-weather-sunny';
      } else if (yinRegex.exec(weatherName)) {
        icon = 'icon-weather-cloudy';
      } else if (yuRegex.exec(weatherName)) {
        icon = 'icon-weather-rain';
      }
      return icon;
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit('tclick', title);
    },
    //水电站落点数据
    async hysDropPoint() {
      let res = await riskMonitoring.hysDropPoint();
      if (res.code == 0) {
        this.hydropowerStation = res.data;
      }
    },
    //水质站落点数据
    async qualityList() {
      let res = await riskMonitoring.qualityList();
      if (res.code == 0) {
        this.quality = res.data;
      }
    },
    //水厂数据
    async listWaterWorks() {
      this.waterPointList = [];
      let res = await waterResourceApi.listWaterWorks();
      if (res.code == 0) {
        this.waterPointList = res.data;
      }
    },
    // 水源地点位信息查询
    async AttSwhsBaseLoadPage() {
      this.waterPointList = [];
      let res = await waterResourceApi.AttSwhsBaseLoadPage({
        withSignage: true
      });
      if (res.code == 0) {
        this.swhWaterPointList = res.data.list;
      }
    },
    //获取工程点位
    async getProjectPoint(type) {
      let res = await hydraulicApi.getProjects({ type });
      if (res.code == 0) {
        if (type == '水库') {
          this.reservoirList = res.data;
        } else {
          this.sluiceList = res.data;
        }
      }
    },
    //'水库','河道', '堰闸','潮位' 数据
    async getOverView(stationType) {
      // RR ZZ  DD TT
      // ['水库','河道', '堰闸','潮位',],
      const opt = { stationType };
      await waterAndRain.waterStationsList(opt).then((res) => {
        if (res.code == 0) {
          if (stationType == 'RR') {
            this.rankingReservoirData = res.data;
          } else if (stationType == 'ZZ') {
            this.rankingRiverwayData = res.data;
          } else if (stationType == 'DD') {
            this.rankingCheckgateData = res.data;
          } else {
            this.rankingSealevelData = res.data;
          }
        }
      });
    },
    btnClick(btn) {
      console.log(this.map.drawClusterPoint, 'dasdaasd');
      this[btn] = !this[btn];
      if (this[btn]) {
        this.drawHydropower();
        this.drawQuality();
        this.drawPointArea();
        this.drawSwhPointArea();
        this.drawSluice();
        this.drawReservoir();
        this.drawWaterRegimen('regimenRR');
        this.drawWaterRegimen('regimenZZ');
        this.drawWaterRegimen('regimenDD');
        this.drawWaterRegimen('regimenTT');
      } else {
        //清除水电站
        this.map.drawClusterPoint([], {}, `hydropowerStationLayer`);
        //清除水质站
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal1');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal2');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal3');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal4');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal5');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal6');
        this.map.drawClusterPoint([], {}, 'waterQualityAbnormal7');
        this.$emit('legend-change', '水质监控', {});
        //清除水厂
        this.map.drawClusterPoint([], {}, 'areaPointLayer1');
        this.map.drawClusterPoint([], {}, 'areaPointLayer2');
        this.map.drawClusterPoint([], {}, 'areaPointLayer3');
        this.map.drawClusterPoint([], {}, 'areaPointLayer4');
        this.$emit('legend-change', '水厂', {});
        //清除水源地
        this.map.drawClusterPoint([], {}, 'areaPointLayer5');
        this.map.drawClusterPoint([], {}, 'areaPointLayer6');
        this.map.drawClusterPoint([], {}, 'areaPointLayer7');
        this.map.drawClusterPoint([], {}, 'areaPointLayer8');
        this.$emit('legend-change', '水源地', {});
        //水闸
        this.map.drawClusterPoint([], {}, `sluiceLayer`);
        //水库
        this.map.drawClusterPoint([], {}, `reservoirLayer`);
        //水情
        this.map.drawClusterPoint([], {}, `regimenRRLayer`);
        this.map.drawClusterPoint([], {}, `regimenZZLayer`);
        this.map.drawClusterPoint([], {}, `regimenDDLayer`);
        this.map.drawClusterPoint([], {}, `regimenTTLayer`);
      }
    },
    //水电站落点
    drawHydropower() {
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/hydropower-station-icon.png`),
        srcs: require(`@/assets/new_images/point/hydropower-station-juhe-icon.png`)
      };
      let list = this.hydropowerStation.map((item) => {
        return {
          ...item,
          type: '抗旱监管',
          name: item.hystName,
          lat: parseFloat(item.hystLat),
          lng: parseFloat(item.hystLong),
          projectType: 'hydropowerStationLayer',
          props: {
            流量: `${item.insCap}m³/s`
          }
        };
      });
      this.map.drawClusterPoint(list, style, `hydropowerStationLayer`);
    },
    //水质站落点
    drawQuality() {
      let abnormalArr1 = [];
      let abnormalArr2 = [];
      let abnormalArr3 = [];
      let abnormalArr4 = [];
      let abnormalArr5 = [];
      let abnormalArr6 = [];
      let abnormalArr7 = [];
      let abnormalStyle1 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/one-class.png`),
        srcs: require(`@/assets/new_images/point/one-class-cluster.png`)
      };
      let abnormalStyle2 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/two-class.png`),
        srcs: require(`@/assets/new_images/point/two-class-cluster.png`)
      };
      let abnormalStyle3 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/three-class.png`),
        srcs: require(`@/assets/new_images/point/three-class-cluster.png`)
      };
      let abnormalStyle4 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/four-class.png`),
        srcs: require(`@/assets/new_images/point/four-class-cluster.png`)
      };
      let abnormalStyle5 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/five-class.png`),
        srcs: require(`@/assets/new_images/point/five-class-cluster.png`)
      };
      let abnormalStyle6 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/six-class.png`),
        srcs: require(`@/assets/new_images/point/six-class-cluster.png`)
      };
      let abnormalStyle7 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/seven-class.png`),
        srcs: require(`@/assets/new_images/point/seven-class-cluster.png`)
      };
      this.quality.forEach((item) => {
        let obj = {
          ...item,
          type: '抗旱监管',
          name: item.siteName,
          lat: parseFloat(item.latitude),
          lng: parseFloat(item.longitude),
          projectType: 'waterQualityAbnormal',
          props: {
            站点名称: item.siteName,
            水质等级:
              item.qualityLevel == 1 && item.qualityWorse == false
                ? 'Ⅰ类'
                : item.qualityLevel == 2 && item.qualityWorse == false
                ? 'Ⅱ类'
                : item.qualityLevel == 3 && item.qualityWorse == false
                ? 'Ⅲ类'
                : item.qualityLevel == 4 && item.qualityWorse == false
                ? 'Ⅳ类'
                : item.qualityLevel == 5 && item.qualityWorse == false
                ? 'Ⅴ类'
                : item.qualityLevel == 6 && item.qualityWorse == false
                ? '劣Ⅴ类'
                : '恶化',
            更新时间: item.tm
          },
          text: `${item.siteName || '-'} 水质等级:${
            item.qualityLevel == 1 && item.qualityWorse == false
              ? 'Ⅰ类'
              : item.qualityLevel == 2 && item.qualityWorse == false
              ? 'Ⅱ类'
              : item.qualityLevel == 3 && item.qualityWorse == false
              ? 'Ⅲ类'
              : item.qualityLevel == 4 && item.qualityWorse == false
              ? 'Ⅳ类'
              : item.qualityLevel == 5 && item.qualityWorse == false
              ? 'Ⅴ类'
              : item.qualityLevel == 6 && item.qualityWorse == false
              ? '劣Ⅴ类'
              : '恶化'
          }`
        };
        if (item.qualityLevel == 1 && item.qualityWorse == false) {
          abnormalArr1.push(obj);
        } else if (item.qualityLevel == 2 && item.qualityWorse == false) {
          abnormalArr2.push(obj);
        } else if (item.qualityLevel == 3 && item.qualityWorse == false) {
          abnormalArr3.push(obj);
        } else if (item.qualityLevel == 4 && item.qualityWorse == false) {
          abnormalArr4.push(obj);
        } else if (item.qualityLevel == 5 && item.qualityWorse == false) {
          abnormalArr5.push(obj);
        } else if (item.qualityLevel == 6 && item.qualityWorse == false) {
          abnormalArr6.push(obj);
        } else {
          abnormalArr7.push(obj);
        }
      });
      this.map.drawClusterPoint(abnormalArr1, abnormalStyle1, 'waterQualityAbnormal1');
      this.map.drawClusterPoint(abnormalArr2, abnormalStyle2, 'waterQualityAbnormal2');
      this.map.drawClusterPoint(abnormalArr3, abnormalStyle3, 'waterQualityAbnormal3');
      this.map.drawClusterPoint(abnormalArr4, abnormalStyle4, 'waterQualityAbnormal4');
      this.map.drawClusterPoint(abnormalArr5, abnormalStyle5, 'waterQualityAbnormal5');
      this.map.drawClusterPoint(abnormalArr6, abnormalStyle6, 'waterQualityAbnormal6');
      this.map.drawClusterPoint(abnormalArr7, abnormalStyle7, 'waterQualityAbnormal7');
      this.$emit('legend-change', '水质监控', {
        label: '水质监控',
        clusterLayer: [
          'waterQualityAbnormal1',
          'waterQualityAbnormal2',
          'waterQualityAbnormal3',
          'waterQualityAbnormal4',
          'waterQualityAbnormal5',
          'waterQualityAbnormal6',
          'waterQualityAbnormal7'
        ],
        children: [
          { label: 'I类', icon: 'icon-one-icon' },
          { label: 'II类', icon: 'icon-two-icon' },
          { label: 'III类', icon: 'icon-three-icon' },
          { label: 'IV类', icon: 'icon-four-icon' },
          { label: 'V类', icon: 'icon-five-icon' },
          { label: '劣V类', icon: 'icon-six-icon' },
          { label: '恶化', icon: 'icon-error-icon' }
        ]
      });
    },
    // 水厂 落点
    drawPointArea() {
      let style1 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/city-icon-aggregation.png'),
        src: require('@/assets/new_images/point/city-icon.png')
      };
      let style2 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/town-icon-aggregation.png'),
        src: require('@/assets/new_images/point/town-icon.png')
      };
      let style3 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/union-icon-aggregation.png'),
        src: require('@/assets/new_images/point/union-icon.png')
      };
      let style4 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/single-icon-aggregation.png'),
        src: require('@/assets/new_images/point/single-icon.png')
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.cwsName,
          type: '抗旱监管',
          lng: point.cwsLong,
          lat: point.cwsLat,
          projectType: 'waterWorks',
          ...point,
          props: {
            '覆盖人口(万人)': point.desWasuPop,
            '设计供水规模(吨/天)': point.desWasuScal
          }
        };
        if (point.wasuRang == '两百吨千人以下') {
          pointList1.push(obj);
        } else if (point.wasuRang == '两百吨千人以上') {
          pointList2.push(obj);
        } else if (point.wasuRang == '千吨万人') {
          pointList3.push(obj);
        } else if (point.wasuRang == '城市水厂') {
          pointList4.push(obj);
        }
      }
      this.map.drawClusterPoint(pointList1, style1, 'areaPointLayer1');
      this.map.drawClusterPoint(pointList2, style2, 'areaPointLayer2');
      this.map.drawClusterPoint(pointList3, style3, 'areaPointLayer3');
      this.map.drawClusterPoint(pointList4, style4, 'areaPointLayer4');
      this.$emit('legend-change', '水厂', {
        label: '水厂',
        clusterLayer: ['areaPointLayer1', 'areaPointLayer2', 'areaPointLayer3', 'areaPointLayer4'],
        children: [
          {
            label: '两百吨千人以下',
            icon: 'water-works-sygs-5'
          },
          {
            label: '两百吨千人以上',
            icon: 'water-works-sygs-1'
          },
          {
            label: '千吨万人',
            icon: 'water-works-sygs-3'
          },
          {
            label: '城市水厂',
            icon: 'water-works-sygs-4'
          }
        ]
      });
    },
    // 水源地 落点
    drawSwhPointArea(btn) {
      let style1 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/shuiyuandi-ld-juhe-1.png'),
        src: require('@/assets/new_images/point/shuiyuandi-ld-1.png')
      };
      let style2 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/shuiyuandi-ld-juhe-2.png'),
        src: require('@/assets/new_images/point/shuiyuandi-ld-2.png')
      };
      let style3 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/shuiyuandi-ld-juhe-3.png'),
        src: require('@/assets/new_images/point/shuiyuandi-ld-3.png')
      };
      let style4 = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/new_images/point/shuiyuandi-ld-juhe-4.png'),
        src: require('@/assets/new_images/point/shuiyuandi-ld-4.png')
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.swhWaterPointList) {
        let obj = {
          ...point,
          name: point.swhsName,
          type: '抗旱监管',
          lng: point.swhsLong,
          lat: point.swhsLat,
          projectType: 'sourceOfWater',
          props: {
            规模:
              point.supplyScale == '1'
                ? '县级以上'
                : point.supplyScale == '2'
                ? '千吨万人'
                : point.supplyScale == '3'
                ? '200吨-1000吨'
                : '200吨以下',
            水源类型:
              point.wainWasoType == '1'
                ? '水库'
                : point.wainWasoType == '2'
                ? '山塘'
                : point.wainWasoType == '3'
                ? '河流湖泊'
                : point.wainWasoType == '4'
                ? '溪沟堰坝'
                : point.wainWasoType == '5'
                ? '蓄水池'
                : point.wainWasoType == '6'
                ? '地下水'
                : point.wainWasoType == '7'
                ? '海水淡化'
                : point.wainWasoType == '8'
                ? '山泉水'
                : '其他',
            // area: point.wasuTownName,
            // isP: '是',
            目标水质: 'I类',
            水量预警:
              point.warnType == '1'
                ? '暂无风险'
                : point.warnType == '2'
                ? '黄色预警'
                : point.warnType == '3'
                ? '橙色预警'
                : '红色预警'
          }
        };
        if (point.supplyScale == 1) {
          pointList1.push(obj);
        } else if (point.supplyScale == 2) {
          pointList2.push(obj);
        } else if (point.supplyScale == 3) {
          pointList3.push(obj);
        } else if (point.supplyScale == 4) {
          pointList4.push(obj);
        }
      }
      this.map.drawClusterPoint(pointList1, style4, 'areaPointLayer5');
      this.map.drawClusterPoint(pointList2, style3, 'areaPointLayer6');
      this.map.drawClusterPoint(pointList3, style2, 'areaPointLayer7');
      this.map.drawClusterPoint(pointList4, style1, 'areaPointLayer8');
      this.$emit('legend-change', '水源地', {
        label: '水源地',
        clusterLayer: ['areaPointLayer5', 'areaPointLayer6', 'areaPointLayer7', 'areaPointLayer8'],
        children: [
          {
            label: '200吨以下',
            icon: 'legend-sygs-5'
          },
          {
            label: '200吨~1000吨',
            icon: 'legend-sygs-1'
          },
          {
            label: '千吨万人',
            icon: 'legend-sygs-3'
          },
          {
            label: '县级以上',
            icon: 'legend-sygs-4'
          }
        ]
      });
    },
    //水闸落点
    drawSluice() {
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/point-sluice-normal.png`),
        srcs: require(`@/assets/new_images/point/group-sluice-normal.png`)
      };
      let list = this.sluiceList.map((item) => {
        return {
          ...item,
          type: '抗旱监管',
          name: item.wagaName,
          lat: item.stLat || item.startLat,
          lng: item.stLong || item.startLong,
          code: item.stCode ? item.stCode : item.wagaCode,
          stCode: item.stCode,
          isOpened: item.gateOpened,
          stType: 'DD',
          projectType: 'sluice',
          projectCode: item.wagaCode,
          props: {
            水闸名称: item.wagaName,
            闸上水位: item.upz,
            闸下水位: item.dwz,
            过闸流量: item.ll,
            闸门开度: item.ovs
          }
        };
      });
      this.map.drawClusterPoint(list, style, `sluiceLayer`);
    },
    //水库落点
    drawReservoir() {
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/point-reservoir-normal.png`),
        srcs: require(`@/assets/new_images/point/group-reservoir-normal.png`)
      };
      let list = this.reservoirList.map((item) => {
        return {
          ...item,
          type: '抗旱监管',
          name: item.resName,
          lng: item.lowLeftLong,
          lat: item.lowLeftLat,
          isOpened: item.rsvrOpened,
          code: item.mainStcd,
          stType: 'RR',
          projectType: 'reservoir',
          projectCode: item.resCode,
          props: {
            水库名称: item.resName,
            水位: item.waterLevel,
            下泄流量: item.xxll
          }
        };
      });
      this.map.drawClusterPoint(list, style, `reservoirLayer`);
    },
    //水位落点
    drawWaterRegimen(projectType) {
      let style = {
        scale: 1,
        anchor: [24, 24],
        anchorXUnits: 'pixels',
        src: require(`@/assets/new_images/point/water-regimen-icon.png`),
        srcs: require(`@/assets/new_images/point/water-regimen-icon-juhe.png`)
      };
      let arr = [];
      switch (projectType) {
        case 'regimenRR':
          arr = this.rankingReservoirData;
          break;
        case 'regimenZZ':
          arr = this.rankingRiverwayData;
          break;
        case 'regimenDD':
          arr = this.rankingCheckgateData;
          break;
        default:
          arr = this.rankingSealevelData;
          break;
      }
      let list = arr.map((item) => {
        return {
          ...item,
          type: '抗旱监管',
          name: item.stName,
          lat: item.stLat,
          lng: item.stLong,
          code: item.stCode,
          stType: item.stType,
          warning: item.warning,
          projectType: 'regimenLayer',
          props: {
            名称: item.stName,
            水位: item.waterLevel,
            更新时间: item.time
          }
        };
      });
      this.map.drawClusterPoint(list, style, `${projectType}Layer`);
    }
  },
  beforeDestroy() {
    //清除水电站
    this.map.drawClusterPoint([], {}, `hydropowerStationLayer`);
    //清除水质站
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal1');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal2');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal3');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal4');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal5');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal6');
    this.map.drawClusterPoint([], {}, 'waterQualityAbnormal7');
    this.$emit('legend-change', '水质监控', {});
    //清除水厂
    this.map.drawClusterPoint([], {}, 'areaPointLayer1');
    this.map.drawClusterPoint([], {}, 'areaPointLayer2');
    this.map.drawClusterPoint([], {}, 'areaPointLayer3');
    this.map.drawClusterPoint([], {}, 'areaPointLayer4');
    this.$emit('legend-change', '水厂', {});
    //清除水源地
    this.map.drawClusterPoint([], {}, 'areaPointLayer5');
    this.map.drawClusterPoint([], {}, 'areaPointLayer6');
    this.map.drawClusterPoint([], {}, 'areaPointLayer7');
    this.map.drawClusterPoint([], {}, 'areaPointLayer8');
    this.$emit('legend-change', '水源地', {});
    //水闸
    this.map.drawClusterPoint([], {}, `sluiceLayer`);
    //水库
    this.map.drawClusterPoint([], {}, `reservoirLayer`);
    //水情
    this.map.drawClusterPoint([], {}, `regimenRRLayer`);
    this.map.drawClusterPoint([], {}, `regimenZZLayer`);
    this.map.drawClusterPoint([], {}, `regimenDDLayer`);
    this.map.drawClusterPoint([], {}, `regimenTTLayer`);
  }
};
</script>

<style lang="scss" scoped>
.water-content-box {
  margin-bottom: 20px;
  .water-content-item {
    padding-top: 14px;
    height: 230px;
    box-sizing: border-box;
    &:nth-child(2) {
      height: 280px;
    }
    &:last-child {
      height: 195px;
    }
    .water-content-title {
      width: 100%;
      height: 50px;
      background: url(~assets/images/WaterDisastersImage/flood-title-icon.png) no-repeat;
      background-size: 100% 100%;
      box-sizing: border-box;
      padding: 7px 0 0 40px;
      span {
        font-size: 16px;
      }
    }
    .police-statistics {
      height: calc(100% - 50px);
      overflow: auto;
      &::-webkit-scrollbar {
        display: none;
      }
      .supervise-box {
        position: relative;
        padding: 70px 0 10px 0;
        .supervise-icon-bg {
          width: 280px;
          height: 168px;
          background: url(~assets/images/scheduling/supervise-icon-bg.png) no-repeat;
          background-size: 100% 100%;
          margin: 0 auto;
          .supervise-icon {
            width: 100%;
            height: 100%;
            background: url(~assets/images/scheduling/supervise-icon.png) no-repeat;
            background-size: 100% 100%;
          }
        }

        .supervise-icon-detail {
          width: 110px;
          height: 100px;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: center;
          > div:nth-child(1) {
            font-size: 24px;
            font-family: ziticqtezhanti;
            color: #00d9ff;
            background: linear-gradient(180deg, #ffffff 0%, #39e6ff 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            > span {
              font-size: 28px;
            }
          }
          > div:nth-child(2) {
            position: relative;
            top: 2px;
          }
        }
        .supervise-icon-01 {
          background: url(~assets/images/scheduling/supervise-icon-01.png) no-repeat;
          background-size: 100% 100%;
          margin: 0 auto;
          position: absolute;
          left: 50%;
          margin-left: -60px;
          top: 0;
          > div:nth-child(1) {
            display: flex;
            > span:nth-child(1) {
              width: 14px;
              height: 18px;

              &.up-icon {
                background: url(~assets/images/scheduling/up-icon.png) no-repeat;
                background-size: 100% 100%;
                margin: 4px 5px 0 0;
              }
              &.down-icon {
                background: url(~assets/images/scheduling/down-icon.png) no-repeat;
                background-size: 100% 100%;
                margin: 6px 5px 0 0;
              }
            }
          }
        }
        .supervise-icon-box {
          display: flex;
          justify-content: space-between;
          position: absolute;
          bottom: -30px;
          width: 100%;
          .supervise-icon-02 {
            background: url(~assets/images/scheduling/supervise-icon-02.png) no-repeat;
            background-size: 100% 100%;
          }
          .supervise-icon-03 {
            background: url(~assets/images/scheduling/supervise-icon-03.png) no-repeat;
            background-size: 100% 100%;
          }
          .supervise-icon-04 {
            background: url(~assets/images/scheduling/supervise-icon-04.png) no-repeat;
            background-size: 100% 100%;
          }
          .supervise-icon-05 {
            background: url(~assets/images/scheduling/supervise-icon-05.png) no-repeat;
            background-size: 100% 100%;
          }
          &.supervise-icon-box-01 {
            bottom: 105px;
          }
        }
      }
    }
    .histogram-box {
      height: calc(100% - 50px);
      width: 100%;
      &.risk-box {
        display: flex;
        padding: 10px 10px 0;
        box-sizing: border-box;
        .risk-icon {
          width: 150px;
          height: 150px;
          background: url(~assets/images/scheduling/risk-icon.png) no-repeat;
          background-size: 100% 100%;
          position: relative;
          > div {
            position: absolute;
            width: 24px;
            height: 24px;
            animation-name: example;
            animation-duration: 2s;
            animation-timing-function: linear;
            animation-iteration-count: infinite;
            animation-direction: alternate;
            &.risk-icon-a {
              left: 40px;
              top: 25px;
              animation-delay: 0s;
            }
            &.risk-icon-b {
              left: 104px;
              top: 45px;
              animation-delay: 1.5s;
            }
            &.risk-icon-c {
              left: 88px;
              top: 95px;
              animation-delay: 2.5s;
            }
            &.risk-icon-d {
              left: 25px;
              top: 90px;
              animation-delay: 3.5s;
            }
          }
        }
        .risk-list {
          padding: 15px 0 0 36px;
          > div {
            display: flex;
            align-items: center;
            padding-bottom: 20px;
            > span:first-child {
              width: 15px;
              height: 15px;
              margin-right: 8px;
            }
            &:last-child {
              padding-bottom: 0;
            }
          }
        }
      }
      .rainfall-no {
        width: 100%;
        height: 100%;
        background: url(~assets/images/scheduling/rainfall-no-icon.png) no-repeat;
        background-size: 100% 100%;
      }
      .rainfall-list {
        width: 100%;
        overflow-x: auto;
        height: 100%;
        &::-webkit-scrollbar {
          height: 4px;
        }
        &::-webkit-scrollbar-thumb {
          /*滚动条里面小方块*/
          background: #0fdbf6;
          border-radius: 2px;
        }
        &::-webkit-scrollbar-track {
          /*滚动条里面轨道*/
          background: #3a6081;
          border-radius: 2px;
        }
        .weather-box {
          width: 1800px;
          height: 100%;
          > div {
            display: inline-block;
            vertical-align: top;
            padding-top: 20px;
            box-sizing: border-box;
            > div {
              font-size: 16px;
              line-height: 22px;
              text-align: center;
              width: 120px;
              &:nth-child(2) {
                margin: 5px 0;
              }
            }
          }
        }
      }
    }
    .primary-map-table {
      width: 100%;
      height: 100%;
      .is-water-box {
        width: 100%;
        height: calc(100% - 45px);
      }
      .flex-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .capsule-tabs {
          width: 50%;
        }
      }
    }
  }
}
@keyframes example {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.2;
  }
  100% {
    opacity: 1;
  }
}

.sche-risk-normal-01 {
  background: url(~assets/images/scheduling/sche-risk-normal-01.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-normal-02 {
  background: url(~assets/images/scheduling/sche-risk-normal-02.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-normal-03 {
  background: url(~assets/images/scheduling/sche-risk-normal-03.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-normal-04 {
  background: url(~assets/images/scheduling/sche-risk-normal-04.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-blue-01 {
  background: url(~assets/images/scheduling/sche-risk-blue-01.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-blue-02 {
  background: url(~assets/images/scheduling/sche-risk-blue-02.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-blue-03 {
  background: url(~assets/images/scheduling/sche-risk-blue-03.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-blue-04 {
  background: url(~assets/images/scheduling/sche-risk-blue-04.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-yellow-01 {
  background: url(~assets/images/scheduling/sche-risk-yellow-01.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-yellow-02 {
  background: url(~assets/images/scheduling/sche-risk-yellow-02.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-yellow-03 {
  background: url(~assets/images/scheduling/sche-risk-yellow-03.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-yellow-04 {
  background: url(~assets/images/scheduling/sche-risk-yellow-04.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-orange-01 {
  background: url(~assets/images/scheduling/sche-risk-orange-01.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-orange-02 {
  background: url(~assets/images/scheduling/sche-risk-orange-02.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-orange-03 {
  background: url(~assets/images/scheduling/sche-risk-orange-03.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-orange-04 {
  background: url(~assets/images/scheduling/sche-risk-orange-04.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-red-01 {
  background: url(~assets/images/scheduling/sche-risk-red-01.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-red-02 {
  background: url(~assets/images/scheduling/sche-risk-red-02.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-red-03 {
  background: url(~assets/images/scheduling/sche-risk-red-03.png) no-repeat;
  background-size: 100% 100%;
}
.sche-risk-red-04 {
  background: url(~assets/images/scheduling/sche-risk-red-04.png) no-repeat;
  background-size: 100% 100%;
}
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
</style>
