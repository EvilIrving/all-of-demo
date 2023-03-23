<template>
  <div class="water-dialog map-dialog" v-loading="loading" element-loading-background="rgba(0, 0, 0, 0.8)">
    <div class="chart-box" v-if="dialogType != 'camera'">
      <div class="tb-top-panel" v-if="dialogType == 'PP' && carouselData.length > 0">
        <i class="icon-liebiao icon-sty laba" />
        <p>
          <marquee direction="left">
            <span v-for="(item, i) in carouselData" :key="i" style="margin-right: 100px">
              温州市历史最大{{ item.during_unit }}降雨：{{ item.happen_date }}，{{ item.area_name }}，{{
                item.station_name
              }}，{{ item.rainfall }}mm
            </span>
          </marquee>
        </p>
      </div>
      <primary-subhead :text="subhead">
        <slot>
          <el-select
            v-if="dialogType == 'FP'"
            v-model="siteInformationData"
            class="primary-select"
            @change="siteInformation"
            placeholder="请选择河道水位站"
            style="width: 170px"
          >
            <el-option
              v-for="item in siteInformationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-date-picker
            v-if="dialogType == 'FP'"
            v-model="value"
            type="datetimerange"
            range-separator="至"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            @change="startDate"
            end-placeholder="结束日期"
          >
          </el-date-picker>
          <el-select
            v-if="dialogType == 'PP'"
            v-model="year"
            class="primary-select"
            @change="yearHandle"
            placeholder="请选择年份"
            style="width: 126px"
          >
            <el-option
              v-for="item in yearOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-select
            v-if="dialogType != '水质监控' && dialogType != 'FP'"
            v-model="time"
            class="primary-select"
            @change="timeHandle"
            placeholder="请选择时间"
            style="width: 126px"
          >
            <el-option
              v-for="item in timeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <el-date-picker
            v-if="dialogType == 'TT'"
            v-model="dateTime"
            type="datetimerange"
            range-separator="至"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始日期"
            :disabled="time ? true : false"
            @change="getWaterStationInfo"
            :clearable="false"
            end-placeholder="结束日期"
          >
          </el-date-picker>
          <div v-if="dialogType == '水质监控'">
            <span :class="tabSwitch == 'codmn' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('codmn')"
              >高锰酸盐指数</span
            >
            <span :class="tabSwitch == 'nh3N' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('nh3N')"
              >氨氮</span
            >
            <span :class="tabSwitch == 'tp' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('tp')">总磷</span>
            <span :class="tabSwitch == 'do' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('do')"
              >溶解氧</span
            >
          </div>
        </slot>
      </primary-subhead>
      <div class="chart-wrap">
        <div class="chart-content">
          <div class="quality—target" v-if="dialogType == '水质监控'">
            <span v-for="(item, index) in targetList[tabSwitch]" :key="index">
              <span>{{ item.key }}</span> {{ item.value }}
            </span>
          </div>
          <span v-if="dialogType != 'TT'" style="color: #fff; white-space: nowrap">{{
            dialogType == 'PP' ? 'mm' : dialogType == '断面' ? 'm³/s' : 'm'
          }}</span>
          <div v-if="dialogType == 'FP'" style="width: 100%; height: 100%">
            <div id="main" style="width: 100%; height: 100%" />
          </div>
          <ve-line
            v-if="
              dialogType == 'RR' ||
              dialogType == 'ZZ' ||
              dialogType == 'DD' ||
              dialogType == '断面' ||
              dialogType == 'flow' ||
              dialogType == '水质监控'
            "
            :extend="chartWaterExtend"
            :colors="waterColors"
            :data="chartWaterData"
            :settings="chartWaterSettings"
            :height="dialogType == '水质监控' ? '96%' : '100%'"
            width="100%"
            :grid="waterGrid"
            :mark-line="waterMarkLine"
          />

          <echartLine v-if="dialogType == 'TT'" ref="lineChart" :extraOption="extraOption"></echartLine>
          <ve-histogram
            v-if="dialogType == 'PP'"
            :data="charRaintData"
            :extend="chartRainExtend"
            height="100%"
            width="100%"
            :grid="rainGrid"
            :settings="chartRainSettings"
            :yAxis="yAxis"
          />
          <!--
              :settings="vchartsConfig.setting"
              :extend="vchartsConfig.extend" -->
          <!-- :mark-line="waterMarkLine" -->
        </div>
      </div>
    </div>
    <div class="info-box" v-if="dialogType != 'camera'">
      <div style="display: flex; justify-content: space-between; align-items: center">
        <primary-map-tab
          ref="listTab"
          :tabArr="listTabArr"
          :tabName="listTab"
          @tabChange="listTabChange"
        ></primary-map-tab>
        <download-excel
          v-if="dialogType == 'PP' || dialogType == 'ZZ'"
          class="export-excel-wrapper"
          :data="dataList"
          :fields="json_fields"
          :name="downLoadName"
        >
          <el-button type="primary" size="small">导出</el-button>
        </download-excel>
      </div>
      <div class="table-box">
        <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
      </div>
    </div>
    <div class="video-content" v-if="dialogType == 'camera'">
      <!-- rtmp视频播放 -->
      <PrimaryVideoPlayer :code="stationData.cameraIndexCode"></PrimaryVideoPlayer>
      <!-- <video-player ref="videoPlayer" :options="playerOptions" /> -->
      <!-- 正常视频播放 -->
      <!-- <primary-video v-else ref="videoDialog" :code="stationData.cameraIndexCode"/> -->
    </div>
  </div>
</template>

<script>
import { PrimarySubhead, PrimaryMapTab, PrimaryMapTable, PrimaryVideoPlayer } from '@/components';
import { mapGetters } from 'vuex';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import * as echarts from 'echarts';
import { none } from 'ol/centerconstraint';
import EchartLine from '@/views/water_disaster_custom/charts/echart_line/EchartLine.vue'; // 折线图
import moment from 'moment'
let day1 = new Date();
day1.setDate(day1.getDate() - 4);
let s1 = day1.format('yyyy-MM-dd hh:mm:ss');
let s2 = new Date().format('yyyy-MM-dd hh:mm:ss');
export default {
  name: 'WaterDialog',
  props: {
    dialogType: String,
    dialogTitle: String,
    stationData: Object
  },
  data() {
    this.chartRainSettings = {
      showLine: ['累计雨量'],
      itemStyle: {
        //配置一些样式
        color: {
          type: 'linear',
          x: 0, //X轴起始坐标
          y: 0, //Y轴起始坐标
          x2: 1, //X轴终点坐标
          y2: 1, //Y轴终点坐标
          colorStops: [
            {
              offset: 0,
              color: '#9DE16C' // 0% 处的颜色
            },
            {
              offset: 1,
              color: '#07BBB9' // 100% 处的颜色
            }
          ],
          globalCoord: false // 缺省为 false
        }
      }
      //   labelMap: {
      //       tm: '时间',
      //       level: '水位'
      //   }
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线'],
      lineStyle: {
        type: 'dotted'
      }
    };
    this.chartRainExtend = {
      legend: {
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: '#fff',
          fontSize: 12
        }
      },
      'xAxis.0.axisLabel.color': '#0b2045',
      series(v) {
        v && v.forEach((i) => (i.barGap = '0%'));
        return v;
      }
    };
    this.chartWaterExtend = {
      legend: {
        icon: 'rect',
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: '#FFF',
          fontSize: 12
        }
      },
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff'
    };
    this.rainGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0
    };
    this.waterMarkLine = {
      data: [
        {
          yAxis: 30
        }
      ],
      lineStyle: {
        normal: {
          color: 'red' // 这儿设置安全基线颜色
        }
      },
      label: {
        normal: {
          position: 'middle',
          formatter: '超汛限水位' // 这儿设置安全基线
        }
      }
    };
    this.yAxis = {
      inverse: false,
      max: 20,
      textStyle: {
        color: '#fff' //这里是改变字体颜色
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: '#fff'
        }
      }
    };
    return {
      json_fields: {
        时间: 'tm',
        '降雨量(mm)': 'drp'
      },
      downLoadName: '',
      siteInformationData: null,
      siteInformationOptions: [],
      times: [],
      times1: [],
      value: [s1, s2],
      start: s1,
      end: s2,
      markPoint: {},
      riverWaterLevelData: [],
      seriesList: [],
      legendData: [],
      colorList: ['#56FEFE', '#19AF47', '#FF5151', '#000DFF', '#008FE8', '#6D70FF', '#EEC80B', '#A000C8', '#E87400'],

      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度、
        techOrder: ['flash'],
        autoplay: true,
        controls: true,
        preload: 'auto', //视频预加载
        muted: false, //默认情况下将会消除任何音频。
        loop: false, //导致视频一结束就重新开始
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [
          {
            type: 'rtmp/mp4',
            //   src: 'rtmp://122.228.154.149:1935/service/PuId-ChannelNo=122000000109597567-01&PlayMethod=0&StreamingType=1'
            src: 'rtmp://58.200.131.2:1935/livetv/hunantv'
          }
        ],
        width: document.documentElement.clientWidth,
        notSupportedMessage: '此视频暂无法播放，请稍后再试' //允许覆盖Video.js无法播放媒体源时显示的默认信息。
      },
      subhead: '水位曲线',
      year: null,
      yearOptions: [
        {
          value: 2020,
          label: '2020'
        },
        {
          value: 2019,
          label: '2019'
        },
        {
          value: 2018,
          label: '2018'
        },
        {
          value: 2017,
          label: '2017'
        },
        {
          value: 2016,
          label: '2016'
        }
      ],
      dateTime: [],
      timeOptions: [
        {
          value: 1,
          label: '前一天'
        },
        {
          value: 2,
          label: '前二天'
        },
        {
          value: 3,
          label: '前三天'
        },
        {
          value: 4,
          label: '前四天'
        },
        {
          value: 7,
          label: '前一周'
        },
        {
          value: 14,
          label: '前二周'
        }
      ],
      time: 1,
      historyList: [],
      charRaintData: {
        columns: ['当前时间', '当前雨量', '历史同期最大雨量'],
        rows: []
      },
      listTabArr: [
        {
          value: 0,
          label: '数据列表'
        }
      ],
      listTab: '',
      columnList: [
        {
          width: 160,
          prop: 'tm',
          label: '时间'
        }
      ],
      dataList: [],
      waterColors: ['#32BCB4', '#19af47', 'red'],
      chartWaterData: {
        columns: ['时间', '水位'],
        rows: []
      },
      activeTab: 0,
      cameraCode: '',
      loading: false,
      specialType: '',
      sectObj: {},
      carouselData: [],
      siteNo: null,
      tabSwitch: 'codmn',
      //潮位图表X轴数据
      extraOption: {},
      targetList: {
        codmn: [
          { key: 'I类', value: '≤ 2' },
          { key: 'Ⅱ类', value: '≤ 4' },
          { key: 'Ⅲ类', value: '≤ 6' },
          { key: 'Ⅳ类', value: '≤ 10' },
          { key: 'Ⅴ类', value: '≤ 15' }
        ],
        nh3N: [
          { key: 'I类', value: '≤ 0.15' },
          { key: 'Ⅱ类', value: '≤ 0.5' },
          { key: 'Ⅲ类', value: '≤ 1.0' },
          { key: 'Ⅳ类', value: '≤ 1.5' },
          { key: 'Ⅴ类', value: '≤ 2.0' }
        ],
        tp: [
          { key: 'I类', value: '≤ 0.02' },
          { key: 'Ⅱ类', value: '≤ 0.1' },
          { key: 'Ⅲ类', value: '≤ 0.2' },
          { key: 'Ⅳ类', value: '≤ 0.3' },
          { key: 'Ⅴ类', value: '≤ 0.4' }
        ],
        do: [
          { key: 'I类', value: '≥ 7.5' },
          { key: 'Ⅱ类', value: '≥ 6' },
          { key: 'Ⅲ类', value: '≥ 5' },
          { key: 'Ⅳ类', value: '≥ 3' },
          { key: 'Ⅴ类', value: '≥ 2' }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(['formatterEndTime', 'formatterStartTime', 'formatterCurrentTime', 'AffairId', 'previewModule'])
  },
  components: {
    PrimarySubhead,
    PrimaryMapTab,
    PrimaryMapTable,
    PrimaryVideoPlayer,
    EchartLine
  },
  watch: {},
  mounted() {
    if (this.dialogType == 'camera' && this.stationData.cameraSource == 2) {
      let source = {
        type: 'application/x-mpegURL',
        src: this.stationData.previewUrl
      };
      this.playerOptions.sources.push(source);
    }
    if (this.dialogType == 'FP') {
      this.analysisByStrategy();
      this.getLogStaitonInfo();
    }
    if (this.dialogType == 'TT') {
      let startDate = moment().subtract(1, 'days').format('YYYY-MM-DD 00:00:00');
      let endDate = moment().add(2, 'days').format('YYYY-MM-DD 00:00:00');
      this.dateTime = [startDate, endDate];
    }
  },
  methods: {
    startDate(val) {
      this.start = val[0];
      this.end = val[1];
      this.getInfo();
    },
    // 测试积水曲线图
    waterRetentionCurve() {
      var chartDom = document.getElementById('main');
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        color: ['#56FEFE', '#19AF47', '#FF5151', '#000DFF', '#008FE8', '#6D70FF', '#EEC80B', '#A000C8', '#E87400'],
        grid: {
          x: 20,
          y: 20,
          x2: 10,
          y2: 60
        },
        legend: {
          x: 'right',
          y: 'bottom',
          data: this.legendData
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line'
          }
        },
        xAxis: {
          type: 'time',
          splitLine: {
            show: false
          },
          axisLabel: {
            show: true,
            textStyle: {
              color: '#fff'
            }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            show: true,
            textStyle: {
              color: '#fff'
            }
          }
        },
        series: this.seriesList
      };
      option && myChart.setOption(option);
    },
    // 河道水位河积水点关联数据处理
    dataProcessing() {
      this.times = [];
      this.times1 = [];
      if (this.riverWaterLevelData.length > 0) {
        this.riverWaterLevelData.forEach((val) => {
          this.times1.push([val['tm'], val['z']]);
        });
        this.chartWaterData.rows.forEach((val) => {
          this.times.push([val['时间'], val['水深']]);
        });
        this.seriesList = [
          {
            name: '水深',
            data: this.times,
            type: 'bar',
            barWidth: 20,
            smooth: true,
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.colorList[0]
                }
              }
            }
          },
          {
            name: '河道水位',
            data: this.times1,
            type: 'line',
            smooth: true,
            symbol: none,
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.colorList[1]
                }
              }
            }
          }
        ];
        this.legendData = [
          {
            name: '水深',
            textStyle: {
              color: this.colorList[0]
            }
          },
          {
            name: '河道水位',
            textStyle: {
              color: this.colorList[1]
            }
          }
        ];
        this.$nextTick(() => {
          this.waterRetentionCurve();
        });
      } else {
        this.chartWaterData.rows.forEach((val) => {
          this.times.push([val['时间'], val['水深']]);
        });
        this.seriesList = [
          {
            name: '水深',
            data: this.times,
            type: 'bar',
            barWidth: 20,
            smooth: true,
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.colorList[0]
                }
              }
            }
          }
        ];
        this.legendData = [
          {
            name: '水深',
            textStyle: {
              color: this.colorList[0]
            }
          }
        ];
        this.$nextTick(() => {
          this.waterRetentionCurve();
        });
      }
    },
    // 河道信息站点信息选择
    siteInformation() {
      this.waterLevels();
    },
    // 根据积水点查询河道站点
    async analysisByStrategy() {
      this.siteInformationOptions = [];
      let opt = {
        distance: 3,
        type: '河道水位',
        latitude: this.stationData.lat,
        longitude: this.stationData.lng
      };
      let res = await hydraulicApi.analysisByStrategy(opt);
      if (res.code == 0) {
        res.data.forEach((val) => {
          this.siteInformationOptions.push({
            value: val.stCode,
            label: val.stName
          });
        });
      }
    },
    // 根据河道站点信息获取河道水位线
    async waterLevels() {
      let opt = {
        begin: this.start,
        end: this.end,
        stationType: 'ZZ',
        stationCode: this.siteInformationData
      };
      let res = await hydraulicApi.waterLevels(opt);
      if (res.code == 0) {
        this.riverWaterLevelData = res.data.data;
        this.$nextTick(() => {
          this.dataProcessing();
        });
      }
    },
    // 展示历史降雨
    rainfallHistoryMax() {
      let opt = {
        stationCode:
          this.stationData.stcd || this.stationData.stCode || this.stationData.code || this.stationData.mgstcd
      };
      hydraulicApi.rainfallHistoryMax(opt).then((res) => {
        this.carouselData = res.data;
      });
    },
    tabSwitchClick(type) {
      this.tabSwitch = type;
      this.qualityCurve(this.siteNo);
    },
    // 水文水质数据接口
    async qualityCurve(val) {
      this.dataList = [];
      this.columnList = [];
      this.subhead = '水质曲线';
      this.siteNo = val;
      let opt = {
        month: '12',
        siteNo: val
      };
      await hydraulicApi.qualityCurve(opt).then((res) => {
        if (res.code == 0) {
          this.waterMarkLine = {};
          this.loading = false;
          this.dataList = res.data.reverse();
          this.waterColors = ['#32BCB4', '#FFFF00', '#ff8700'];
          if (this.tabSwitch == 'codmn') {
            this.columnList = [
              {
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'codmn',
                label: '高锰酸盐指数'
              }
            ];
            let info = JSON.parse(JSON.stringify(res.data));
            info.map((item) => {
              item['时间'] = item.tm;
              item['高锰酸盐指数'] = item.codmn;
              return item;
            });
            this.chartWaterData.columns = ['时间', '高锰酸盐指数'];
            this.chartWaterData.rows = info;
          } else if (this.tabSwitch == 'nh3N') {
            this.columnList = [
              {
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'nh3N',
                label: '氨氮'
              }
            ];
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item['时间'] = item.tm;
              item['氨氮'] = item.nh3N;
              return item;
            });
            this.chartWaterData.columns = ['时间', '氨氮'];
            this.chartWaterData.rows = info;
          } else if (this.tabSwitch == 'tp') {
            this.columnList = [
              {
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'tp',
                label: '总磷'
              }
            ];
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item['时间'] = item.tm;
              item['总磷'] = item.tp;
              return item;
            });
            this.chartWaterData.columns = ['时间', '总磷'];
            this.chartWaterData.rows = info;
          } else if (this.tabSwitch == 'do') {
            this.columnList = [
              {
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'do',
                label: '溶解氧'
              }
            ];
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item['时间'] = item.tm;
              item['溶解氧'] = item.do;
              return item;
            });
            this.chartWaterData.columns = ['时间', '溶解氧'];
            this.chartWaterData.rows = info;
          }
        }
      });
    },
    // 获取水位测站信息
    getWaterStationInfo() {
      this.loading = true;
      this.subhead = '水位曲线';
      this.json_fields = this.dialogType == 'PP' ? { 时间: 'tm', '降雨量(mm)': 'drp' } : { 时间: 'tm', '水位(mm)': 'z' }
      this.downLoadName = this.dialogTitle + `${this.dialogType == 'PP' ? '-降雨列表.xls' : '-水位列表.xls'}`
      const opt = {
        stationCode: this.stationData.stCode || this.stationData.stcd,
        stationType: this.stationData.stType,
      };
      if(this.dialogType == 'TT'){
        opt.begin = this.dateTime[0];
        opt.end = this.dateTime[1];
      } else {
        opt.days = this.time
      }
      hydraulicApi.waterStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          let arr = [];
          this.dataList = res.data.data && res.data.data.map((item) => {
            item.rz = item.rz || item.z || item.tdz;
            return item;
          }).reverse();
          let info = res.data.data ? JSON.parse(JSON.stringify(res.data.data)) : [];
          let arrs = res.data.data ? JSON.parse(JSON.stringify(res.data.data)).reverse() : [];
          if (res.data.astroTdz) {
            if (res.data.astroTdz.length > 0) {
              this.dataList = res.data.astroTdz.map((item) => {
                item.rz = item.rz || item.z || item.tdz;
                return item;
              }).reverse();
              let astroTdz = JSON.parse(JSON.stringify(res.data.astroTdz)).reverse();
              const arr = arrs.map((item) => {
                const str = astroTdz.find((i) => {
                  if (item.tm == i.tm) {
                    return i;
                  }
                });
                item.twc = str.tdz;
                item.tdz = item.tdz ? item.tdz : str.tdz;
                return item;
              });
              info = arr;
            } else {
              info = res.data.data ? JSON.parse(JSON.stringify(res.data.data)).reverse() : [];
            }
          } else {
            info = res.data.data ? JSON.parse(JSON.stringify(res.data.data)).reverse() : [];
          }
          if (this.dialogType == 'RR') {
            this.chartWaterExtend = {
              legend: {
                icon: 'rect',
                itemWidth: 8,
                itemHeight: 4,
                right: 0,
                bottom: 0,
                textStyle: {
                  //图例文字的样式
                  color: '#1b3047',
                  fontSize: 12
                }
              },
              'xAxis.0.axisLabel.color': '#fff',
              'yAxis.0.axisLabel.color': '#fff'
            };
            // 水库
            this.waterMarkLine.label.normal.formatter = '汛限水位';
            this.waterMarkLine.data[0].yAxis = res.data.limitLevel;
          } else if (this.dialogType == 'DD') {
            // 闸坝
            this.waterMarkLine.label.normal.formatter = '警戒水位';
            this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
          } else if (this.dialogType == 'ZZ') {
            // 河道
            this.subhead = '水位过程线';
            this.waterMarkLine.label.normal.formatter = '警戒水位';
            this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
            this.waterMarkLine.lineStyle.normal = 'rgba(255, 255, 255, 0)';
            this.waterMarkLine.symbol = 'none';
          } else if (this.dialogType == 'TT') {
            // 潮位
            this.subhead = '潮位过程线';
            this.columnList = [
              {
                prop: 'tm',
                width: 160,
                label: '时间'
              },
              {
                prop: 'tdz',
                label: '水位(m)'
              }
            ];
            if (res.data.warningLevel > 0) {
              this.waterMarkLine.label.normal.formatter = '警戒水位';
              this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
            }
          }
          if(this.dialogType == 'TT') {
            this.timeOptions.findIndex(item => item.label == '自定义') == -1 ?this.timeOptions.push({
              value: null,
              label: '自定义'
            }) : '';
          } else {
            this.timeOptions = this.timeOptions.filter(item => item.label != '自定义');
          }
          if (
            this.dialogType == 'RR' ||
            this.dialogType == 'DD' ||
            this.dialogType == 'ZZ' ||
            this.dialogType == 'TT'
          ) {
            if (res.data.highestLevel) {
              let highRemark = {
                yAxis: res.data.highestLevel,
                lineStyle: {
                  color: 'orange'
                },
                label: {
                  position: 'middle',
                  formatter: '历史最高水位'
                }
              };
              this.waterMarkLine.data[1] = highRemark;
            }
          }
          if (this.dialogType == 'DD') {
            this.columnList = [
              {
                width: 160,
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'upz',
                label: '闸上水位(m)'
              },
              {
                prop: 'dwz',
                label: '闸下水位(m)'
              },
              {
                prop: 'll',
                label: '过闸流量(m³/s)'
              }
            ];
            this.chartWaterData.columns = ['时间', '闸上水位', '闸下水位', '过闸流量'];
          } else if (this.dialogType == 'TT') {
            let obj = [];
            let arr = [];
            let xAxisArr = [];
            let warnTide = [];
            info.forEach((item) => {
              if (item.tdz && item.tm) {
                obj.push(item.twc);
                arr.push(item.tdz);
                xAxisArr.push(item.tm.slice(5, 16));
              }
              warnTide.push(res.data.warningLevel);
            });
            let xAxis = [
              {
                axisLabel: {
                  show: true,
                  textStyle: {
                    color: '#fff',
                    fontSize: this.$commonSetting.chartFontSize
                  },
                  formatter: function (value) {
                    var ret = ''; //拼接加\n返回的类目项
                    var maxLength = 6; //每项显示文字个数
                    var valLength = value.length; //X轴类目项的文字个数
                    var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                    if (rowN > 1) {
                      //如果类目项的文字大于3,
                      for (var i = 0; i < rowN; i++) {
                        var temp = ''; //每次截取的字符串
                        var start = i * maxLength; //开始截取的位置
                        var end = start + maxLength; //结束截取的位置
                        //这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                        temp = value.substring(start, end) + '\n';
                        ret += temp; //凭借最终的字符串
                      }
                      return ret;
                    } else {
                      return value;
                    }
                  }
                },
                data: xAxisArr.reverse()
              }
            ];
            this.extraOption = {
              yAxis: {
                axisLabel: {
                  fontSize: this.$commonSetting.chartFontSize
                }
              },
              legend: {
                textStyle: {
                  fontSize: this.$commonSetting.chartFontSize
                }
              },
              series: [
                {
                  data: arr.reverse(),
                  type: 'line',
                  name: '水位',
                  smooth: false, //关键点，为true是不支持虚线的，实线就用true
                  itemStyle: {
                    normal: {
                      // lineStyle: {
                      //   width: 2,
                      //   type: 'dashed',
                      //   color: '#1579FF',
                      // },
                      color: '#1579FF'
                    }
                  },
                  lineStyle: {
                    color: '#1579FF'
                  }
                },
                {
                  data: obj.reverse(),
                  type: 'line',
                  smooth: true,
                  name: '天文潮',
                  lineStyle: {
                    color: '#7bf9f1'
                  },
                  itemStyle: {
                    normal: {
                      lineStyle: {
                        width: 2,
                        type: 'dashed',
                        color: '#7bf9f1'
                      },
                      color: '#7bf9f1'
                    }
                  }
                }
              ]
            };
            this.extraOption.xAxis = xAxis;

            this.waterColors = ['#1579FF', '#FF3B3B', '#ff8700'];
            if (res.data.warningLevel > 0) {
              this.chartWaterData.columns = ['时间', '水位', '天文潮', '警戒水位'];
              this.extraOption.series.push({
                data: warnTide.reverse(),
                type: 'line',
                smooth: true,
                name: '警戒水位',
                lineStyle: {
                  color: '#ff8700',
                  type: 'dashed'
                },
                itemStyle: {
                  normal: {
                    color: '#ff8700'
                  }
                }
              });
            } else {
              this.chartWaterData.columns = ['时间', '水位', '天文潮'];
            }
          } else if (this.dialogType == 'ZZ') {
            this.waterColors = ['#1579FF', '#FF3B3B', '#ff8700'];
            this.columnList = [
              {
                width: 160,
                prop: 'tm',
                label: '时间'
              },
              {
                width: 100,
                prop: 'z',
                label: '水位(m)'
              }
            ];
            if (res.data.warningLevel > 0) {
              this.chartWaterData.columns = ['时间', '水位', '保证水位', '警戒水位'];
            } else {
              this.chartWaterData.columns = ['时间', '水位', '保证水位'];
            }
          } else {
            this.columnList = [
              {
                width: 160,
                prop: 'tm',
                label: '时间'
              },
              {
                prop: 'rz',
                label: '水位(m)'
              }
            ];
            this.chartWaterData.columns = ['时间', '水位'];
          }
          info.map((item) => {
            item['时间'] = item.tm;
            item['水位'] = item.rz || item.z || item.tdz;
            item['天文潮'] = item.twc;
            item['闸上水位'] = item.upz;
            item['保证水位'] = res.data.guaranteeLevel;
            item['警戒水位'] = res.data.warningLevel;
            item['闸下水位'] = item.dwz;
            item['过闸流量'] = item.ll;
            return item;
          });
          this.chartWaterData.rows = info.reverse();
        }
      });
    },
    //获取积水测站信息
    getLogStaitonInfo() {
      this.loading = true;
      this.subhead = '水位曲线';
      const opt = {
        name: this.stationData.name,
        start: this.start,
        end: this.end
      };
      hydraulicApi.floodPointCurveHistory(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          this.columnList = [
            {
              prop: 'key',
              label: '时间',
              width: 200
            },
            {
              prop: 'value',
              label: '水深(cm)'
            }
          ];
          this.chartWaterData.columns = ['时间', '水深'];
          let info = [];
          res.data.map((item) => {
            info.push({ 时间: item.key, 水深: item.value });
            return item;
          });
          this.chartWaterData.rows = info;
          this.$nextTick(() => {
            this.dataProcessing();
          });
        }
      });
    },
    //获取演练水库水位信息
    getPreWaterStationInfo() {
      this.loading = true;
      this.subhead = '水位曲线';
      const opt = {
        resCode: this.stationData.code || this.stationData.stcd,
        start: this.stationData.start,
        end: this.stationData.end,
        timeForward: true,
        drillAffairId: this.AffairId
      };
      hydraulicApi.lmRsvrDrillWlCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          let arr = [];
          this.dataList = res.data.map((item) => {
            item.rz = item.waterLevel || item.z || item.tdz;
            item.tm = item.dataTime;
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data));
          if (this.dialogType == 'RR') {
            // 水库
            this.waterMarkLine.label.normal.formatter = '汛限水位';
            this.waterMarkLine.data[0].yAxis = res.data[0].currentLimitLevel;
          }
          this.columnList = [
            {
              width: 160,
              prop: 'tm',
              label: '时间'
            },
            {
              prop: 'rz',
              label: '水位(m)'
            }
          ];
          this.chartWaterData.columns = ['时间', '水位'];
          info.map((item) => {
            item['时间'] = item.dataTime;
            item['水位'] = item.waterLevel || item.z || item.tdz;
            return item;
          });
          this.chartWaterData.rows = info;
        }
      });
    },
    // 历史同期降雨分析
    getYearRainStationInfo() {
      const opt = {
        year: this.year,
        stationCode:
          this.stationData.stcd || this.stationData.stCode || this.stationData.code || this.stationData.mgstcd,
        days: this.time
      };
      hydraulicApi.rainStationInfo(opt).then((res) => {
        if (res.code == 0) {
          this.historyList = res.data;
          let arr = res.data;
          arr.map((item) => {
            item['history_tm'] = item.tm;
            item['history_drp'] = item.drp;
            return item;
          });
          let obj = this.dataList.map((item, index) => {
            item['now_tm'] = item.tm;
            item['now_drp'] = item.drp;
            return { ...item, ...arr[index] };
          });
          let info = JSON.parse(JSON.stringify(obj)).reverse();
          let max = 0;
          info.map((item) => {
            item['当前时间'] = item.now_tm + '/' + (item.history_tm || '-');
            item['当前雨量'] = item.now_drp;
            // item["历史时间"] = item.history_tm;
            item['历史同期最大雨量'] = item.history_drp;
            max = item.now_drp > max ? item.now_drp : max;
            return item;
          });
          this.yAxis.max = max * 1.5;
          this.charRaintData.rows = info;
        }
      });
    },
    // 获取雨量测站信息
    getRainStationInfo(type) {
      this.rainfallHistoryMax();
      this.specialType = type; //复盘雨量弹窗特殊标记
      this.loading = true;
      this.columnList = [
        {
          width: 160,
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '降雨量(mm)'
        }
      ];
      this.subhead = '降雨分析';
      const opt = {
        stationCode:
          this.stationData.stcd || this.stationData.stCode || this.stationData.code || this.stationData.mgstcd,
        days: this.time
      };
      if (type == '复盘雨量') {
        const start = this.formatterStartTime('yyyy-MM-DD hh:mm:ss');
        const end = this.formatterEndTime('yyyy-MM-DD hh:mm:ss');
        opt.start = start;
        opt.end = end;
      }
      hydraulicApi.rainStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          if (this.historyList.length == 0) {
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            let max = 0;
            info.map((item) => {
              item['当前时间'] = item.tm;
              item['当前雨量'] = item.drp;
              max = item.drp > max ? item.drp : max;
              return item;
            });
            this.yAxis.max = max * 1.5;
            this.charRaintData.rows = info;
          } else {
            let arr = res.data;
            arr.map((item) => {
              item['now_tm'] = item.tm;
              item['now_drp'] = item.drp;
              return item;
            });
            let obj = this.historyList.map((item, index) => {
              item['history_tm'] = item.tm;
              item['history_drp'] = item.drp;
              return { ...item, ...arr[index] };
            });
            let info = JSON.parse(JSON.stringify(obj)).reverse();
            let max = 0;
            info.map((item) => {
              item['当前时间'] = item.now_tm + '/' + (item.history_tm || '-');
              item['当前雨量'] = item.now_drp;
              // item["历史时间"] = item.history_tm;
              item['历史同期最大雨量'] = item.history_drp;
              max = item.now_drp > max ? item.now_drp : max;
              return item;
            });
            this.yAxis.max = max * 1.5;
            this.charRaintData.rows = info;
          }
        }
      });
    },
    // 获取演练雨量信息
    getPreviewRainStationInfo() {
      this.loading = true;
      this.columnList = [
        {
          width: 160,
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '降雨量(mm)'
        }
      ];
      this.subhead = '降雨分析';
      let opt = {
        start: new Date(this.formatterStartTime()).format('yyyy-MM-dd hh:mm:ss'),
        end: new Date(this.formatterCurrentTime()).format('yyyy-MM-dd hh:mm:ss'),
        timeForward: true,
        stationCode:
          this.stationData.mgstcd || this.stationData.stcd || this.stationData.stCode || this.stationData.code
      };
      hydraulicApi.rainStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          if (this.historyList.length == 0) {
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            let max = 0;
            info.map((item) => {
              item['当前时间'] = item.tm;
              item['当前雨量'] = item.drp;
              max = item.drp > max ? item.drp : max;
              return item;
            });
            this.yAxis.max = max * 1.5;
            this.charRaintData.rows = info;
          } else {
            let arr = res.data;
            arr.map((item) => {
              item['now_tm'] = item.tm;
              item['now_drp'] = item.drp;
              return item;
            });
            let obj = this.historyList.map((item, index) => {
              item['history_tm'] = item.tm;
              item['history_drp'] = item.drp;
              return { ...item, ...arr[index] };
            });
            let info = JSON.parse(JSON.stringify(obj)).reverse();
            let max = 0;
            info.map((item) => {
              item['当前时间'] = item.now_tm + '/' + (item.history_tm || '-');
              item['当前雨量'] = item.now_drp;
              // item["历史时间"] = item.history_tm;
              item['历史同期最大雨量'] = item.history_drp;
              max = item.now_drp > max ? item.now_drp : max;
              return item;
            });
            this.yAxis.max = max * 1.5;
            this.charRaintData.rows = info;
          }
        }
      });
    },
    yearHandle(val) {
      this.year = val;
      this.getYearRainStationInfo();
    },
    timeHandle(val) {
      this.time = val;
      if(val) {
        this.getInfo();
      }
      if (this.dialogType == 'TT') {
        let startDate = moment().subtract(val, 'days').format('YYYY-MM-DD 00:00:00');
        let endDate = moment().add(1, 'days').format('YYYY-MM-DD 00:00:00');
        this.dateTime = [startDate, endDate];
        this.getWaterStationInfo()
      }
    },
    listTabChange(val) {
      this.activeTab = val;
    },
    // 根据不同类型 获取数据
    getInfo() {
      switch (this.dialogType) {
        case 'PP': {
          if (this.previewModule) {
            this.getPreviewRainStationInfo();
          } else {
            this.getRainStationInfo();
          }
          break;
        }
        case 'flow':
          this.getFlowDetail();
          break;
        case 'FP':
          this.getLogStaitonInfo();
          if (this.siteInformationData) {
            this.waterLevels();
          }
          break;
        case '断面':
          this.sectFlowCurve();
          break;
        default:
          this.getWaterStationInfo();
      }
    },
    // 第一次设置选中第一个tab
    setFirstTab() {
      this.$refs.listTab.tabChange(0);
    },
    //获取流域生态流量断面流量曲线
    async sectFlowCurve(data) {
      if (data) {
        this.sectObj = data.data;
      }
      this.loading = true;
      this.subhead = '生态流量曲线';
      let opt = {
        hours: this.time * 24,
        sectName: this.sectObj.sectName
      };
      this.chartWaterData.rows = [];
      let res = await waterResourceApi.getSectFlowCurve(opt);
      this.loading = false;
      if (res.code == 0) {
        this.dataList = res.data;
        this.columnList = [
          {
            prop: 'tm',
            label: '时间',
            width: 180
          },
          {
            prop: 'flow',
            label: '流量(m³/s)'
          }
        ];
        this.waterMarkLine = {
          data: [
            {
              yAxis: Number(this.sectObj.redFlow),
              lineStyle: {
                color: 'red'
              },
              label: {
                position: 'middle',
                formatter: '红色预警指标'
              }
            },
            {
              yAxis: Number(this.sectObj.orangeFlow),
              lineStyle: {
                color: '#FF551F'
              },
              label: {
                position: 'middle',
                formatter: '橙色预警指标'
              }
            }
          ],
          label: {
            normal: {
              position: 'middle',
              formatter: '超汛限水位' // 这儿设置安全基线
            }
          }
        };
        this.chartWaterData.columns = ['时间', '流量'];
        let info = [];
        res.data.map((item) => {
          info.push({ 时间: item.tm, 流量: item.flow });
          return item;
        });
        this.chartWaterData.rows = info;
      }
    },
    //流量
    async getFlowDetail() {
      this.loading = true;
      this.columnList = [
        {
          prop: 'tm',
          label: '时间',
          width: 160
        },
        {
          prop: 'z',
          label: '水位(m)'
        },
        {
          prop: 'flow',
          label: '流量(m³/s)'
        }
      ];
      this.subhead = '流量列表';
      const opt = {
        // dataTime: new Date().format("yyyy-MM-dd hh:mm:ss"),
        hours: this.time * 24,
        stCode: this.stationData.code
      };
      this.chartWaterData.columns = ['时间', '流量', '水位'];
      hydraulicApi.getMrwlCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data.map((item) => {
            item.tm = new Date(item.tm).format('yyyy-MM-dd hh:mm:ss');
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data)).reverse();
          info.map((item) => {
            item['时间'] = new Date(item.tm).format('yyyy-MM-dd hh:mm:ss');
            item['流量'] = item.flow;
            item['水位'] = item.z;
            return item;
          });
          this.chartWaterData.rows = info;
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.water-dialog {
  @include flexbox;
  height: 100%;
  width: 100%;
  height: 572px;
  padding: 0 16px 16px 16px;
  .primary-map-tab .tab-box {
    margin-top: 0;
  }
  .chart-box {
    @include flexbox;
    @include flexflow(column, wrap);
    width: 67%;
    padding-right: 20px;
    .chart-wrap {
      @include flex(1);
      position: relative;
      height: 100%;
    }
    .chart-content {
      width: 100%;
      height: 100%;
      position: absolute;
      top: 0;
      .quality—target {
        display: flex;
        justify-content: space-between;
        padding: 13px 30px;
        margin-bottom: 16px;
        background-image: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 3%, rgba(41, 147, 180, 0.1) 100%);
        span {
          span {
            color: #33ffff;
            margin-right: 8px;
          }
          color: #fff;
        }
      }
    }
  }
  .info-box {
    @include flexbox;
    @include flexflow(column, wrap);
    width: 33%;
    height: 100%;
    .table-box {
      width: 100%;
      @include flex(1);
      @include flexbox;
      @include flexflow(column, wrap);
      position: relative;
      overflow-y: auto;
      ::v-deep .table-wrap {
        position: absolute;
        top: 0;
      }
      .info-wrap {
        width: 100%;
        position: absolute;
        top: 0;
      }
    }

    .mt16 {
      margin-bottom: 16px;
      color: #fff;
    }
    .info-text {
      @include flexbox;
      @include flexJC(space-between);
      @include flexAC;
      width: 100%;
      height: 44px;
      background: rgba(0, 0, 0, 0.4);
      border-radius: 4px;
      border-radius: 4px;
      color: #fff;
      border: 0;
      padding: 0 16px;
    }
  }

  .video-content {
    height: 100%;
    width: 100%;
  }

  // 工程特性
  .engineering-characteristics {
    height: 203px;
    width: 100%;
    // background: url(../../../../assets/images/engineering-characteristics.png)
    // no-repeat center;
    background-size: 100% 100%;
    margin-bottom: 16px;
  }

  // 信息模板
  .msg-template {
    width: 100%;
    text-indent: 28px;
    padding: 10px 24px 20px 24px;
    background: rgba(0, 0, 0, 0.2);
    line-height: 22px;
    color: rgba(255, 255, 255, 0.88);
  }

  // 开度信息
  .open-info {
    height: 58px;
    width: 45px;
    // background: url(../../../../assets/images/open-info.png) no-repeat center;
    background-size: 100% 100%;
  }

  // 水位流量曲线
  .flow-chart {
    height: 220px;
    width: 100%;
  }

  .transformation-wrap {
    @include flexbox;
    > div:first-child {
      width: 40%;
    }
    > div:last-child {
      width: 40%;
    }
    i {
      margin: 42px 16px 0 16px;
    }
  }
}
</style>
<style lang="scss" scoped>
.water-dialog {
  .primary-map-tab .tab-box {
    margin-top: 0;
  }
  .primary-map-tab .tab-item {
    margin-right: 0;
  }
  .transformation-wrap .el-input__inner {
    border-radius: 4px 0 0 4px;
  }
  .primary-select {
    border: 0.0625rem solid rgba(255, 255, 255, 0.6);
    border-radius: 2px;
  }
  ::v-deep .el-input-group__append,
  ::v-deep .el-input-group__prepend {
    background-color: rgba(0, 0, 0, 0.4);
    color: #909399;
    vertical-align: middle;
    display: table-cell;
    position: relative;
    border: 0;
    border-radius: 0 4px 4px 0;
    width: 1px;
    white-space: nowrap;
    padding: 10px;
  }
  .tb-top-panel {
    margin: 5px 0 10px 0;
    color: #fff;
    display: flex;
    align-items: center;
    i {
      float: left;
    }
    p {
      flex: 1;
      font-size: 15px;
      margin-left: 10px;
      background-color: rgba(255, 255, 255, 0.1);
      padding: 8px 10px;
      border-radius: 4px;
    }
    span {
      color: #7ccfff;
      font-size: 20px;
    }
    .laba {
      width: 24px;
      height: 24px;
      background: url(~assets/images/laba.png) no-repeat;
      background-size: 100%;
    }
  }
}

.tab-switch {
  padding: 5px 10px;
  margin-right: 15px;
  color: #56fefe;
  width: 150px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #56fefe;
}
.tab-switch-sty {
  padding: 5px 10px;
  margin-right: 15px;
  color: #fff;
  width: 160px;
  height: 30px;
  line-height: 30px;
  border: 1px solid #fff;
}
    ::v-deep .el-date-editor--datetimerange.el-input__inner {
      width: 22rem;
    }
    ::v-deep .el-range__close-icon {
      display: none;
    }
    ::v-deep .el-range-input {
      width: 47%;
    }
    ::v-deep .el-input__inner {
      background-color: transparent;
      height: 32px;
    }
    .el-date-editor {
      border: 1px solid #aaa;
    }
    ::v-deep .el-range-editor.is-disabled input,
    ::v-deep .el-range-editor input,
    ::v-deep .el-range-separator {
      background-color: transparent;
      color: #fff;
      line-height: 26px;
    }
    ::v-deep .el-input__icon {
      color: #fff;
      line-height: 26px;
    }
</style>
