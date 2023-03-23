<template>
  <div class="water-dialog map-dialog" :class="{ 'disaster-dialog': dialogType == 'disaster' }" element-loading-background="rgba(0, 0, 0, 0.8)">
    <template v-if="dialogType != 'disaster'" v-loading="loading">
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
            <!-- <el-select v-if="dialogType == 'PP'" v-model="year" class="primary-select" @change="yearHandle" placeholder="请选择年份" style="width: 126px">
              <el-option v-for="item in yearOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select> -->
            <el-select v-if="dialogType != '水质监控'" v-model="time" class="primary-select" @change="timeHandle" placeholder="请选择时间" style="width: 126px">
              <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </slot>
        </primary-subhead>
        <div class="chart-wrap">
          <div class="chart-content">
            <span style="color: #fff; white-space: nowrap">{{ dialogType == 'PP' ? 'mm' : 'm' }}</span>
            <div v-if="dialogType == '水质监控'">
              <span :class="tabSwitch == '高锰酸盐指数' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('高锰酸盐指数')">高锰酸盐指数</span>
              <span :class="tabSwitch == '氨氮' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('氨氮')">氨氮</span>
              <span :class="tabSwitch == '总磷' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('总磷')">总磷</span>
              <span :class="tabSwitch == '溶解氧' ? 'tab-switch' : 'tab-switch-sty'" @click="tabSwitchClick('溶解氧')">溶解氧</span>
            </div>
            <ve-line v-if="
                dialogType == 'RR' ||
                  dialogType == 'ZZ' ||
                  dialogType == 'DD' ||
                  dialogType == 'TT' ||
                  dialogType == 'FP' ||
                  dialogType == '断面' ||
                  dialogType == 'flow' ||
                  dialogType == '水质监控'
              " :extend="chartWaterExtend" :colors="waterColors" :data="chartWaterData" :settings="chartWaterSettings" :height="dialogType == '水质监控' ? '98%' : '100%'" width="100%" :grid="waterGrid" :mark-line="waterMarkLine" />
            <ve-histogram v-if="dialogType == 'PP'" :data="charRaintData" :extend="chartRainExtend" height="100%" width="100%" :grid="rainGrid" :settings="chartRainSettings" :yAxis="yAxis" />
            <!-- 
              :settings="vchartsConfig.setting"
              :extend="vchartsConfig.extend" -->
            <!-- :mark-line="waterMarkLine" -->
          </div>
        </div>
      </div>
      <div class="info-box" v-if="dialogType != 'camera'">
        <primary-map-tab ref="listTab" :tabArr="listTabArr" :tabName="listTab" @tabChange="listTabChange"></primary-map-tab>
        <div class="table-box">
          <primary-map-table :columnList="columnList" :dataList="dataList"></primary-map-table>
        </div>
      </div>
      <div class="video-content" v-if="dialogType == 'camera'">
        <video v-if="stationData.cameraSource == 2" id="video" class="video-player video-js vjs-default-skin vjs-big-play-centered" preload="auto">
          <source :src="stationData.previewUrl" type="rtmp">
        </video>
        <!-- rtmp视频播放 -->
        <!-- <video-player :playsinline="true" class="video-player" v-if="stationData.cameraSource == 2" ref="videoPlayer" :options="playerOptions" /> -->
        <!-- 正常视频播放 -->
        <primary-video v-else ref="videoDialog" :code="stationData.cameraIndexCode" />
      </div>
    </template>
    <template v-if="dialogType == 'disaster'">
      <disaster :disasterData="stationData" v-if="stationData.type == '避灾点'" />
      <warehouse :disasterData="stationData" v-if="stationData.type == '救援仓库'" />
      <stormTide :disasterData="stationData" v-if="stationData.type == '风暴潮预警'" />
    </template>
  </div>
</template>

<script>
import videojs from "video.js"
// import 'videojs-flash'; //引入才能播放rtmp视屏
import "video.js/dist/video-js.min.css"
import SWF_URL from "videojs-swf/dist/video-js.swf"
videojs.options.flash.swf = SWF_URL
import { PrimarySubhead, PrimaryMapTab, PrimaryMapTable, PrimaryVideo } from '@/components';
import { mapGetters } from 'vuex';
import disaster from './conponents/disaster.vue';
import warehouse from './conponents/warehouse.vue';
import stormTide from './conponents/stormTide.vue';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
export default {
  name: 'WaterDialog',
  props: {
    dialogType: String,
    stationData: Object,
    dialogTypeName: {
      type: String,
      default: ''
    }
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
      showLine: ["水位曲线"],
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
      videoOptions: {
        autoplay: true, // 是否自动播放
        muted: false, // 是否静音
        fluid: false, // 宽高自适应
        loop: false,//是否循环播放
        techOrder: ["flash"]
      },
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度、
        live: true,
        autoplay: true,
        fluid: true,
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        },
        techOrder: ['flash'],
        flash: {
          hls: { withCredentials: false },
          swf: SWF_URL // 引入静态文件swf
        },
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
            src: 'rtmp://media3.sinovision.net:1935/live/livestream'
          }
        ],
        width: document.documentElement.clientWidth,
        notSupportedMessage: '此视频暂无法播放，请稍后再试' //允许覆盖Video.js无法播放媒体源时显示的默认信息。
      },
      subhead: '水位曲线',
      year: null,
      yearOptions: [
        {
          value: 2021,
          label: '2021'
        },
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
        }
      ],
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
      time: 2,
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
      tabSwitch: '高锰酸盐指数'
    };
  },
  computed: {
    ...mapGetters(['formatterEndTime', 'formatterStartTime', 'formatterCurrentTime', 'AffairId'])
  },
  components: {
    PrimarySubhead,
    PrimaryMapTab,
    PrimaryMapTable,
    PrimaryVideo,
    disaster,
    warehouse,
    stormTide
  },
  watch: {},
  mounted() {
    if (this.dialogType == 'camera' && this.stationData.cameraSource == 2) {
      let nowPlayVideoUrl = this.stationData.previewUrl
      this.initVideoPlayer(nowPlayVideoUrl)
      // let source = [{
      //   type: 'rtmp/mp4',
      //   // type: 'application/x-mpegURL',
      //   src: this.stationData.previewUrl
      // }];
      // // this.playerOptions.sources.push(source);
      // this.playerOptions.sources = source;

    }
  },
  methods: {
    initVideoPlayer(nowPlayVideoUrl) {
      {
        // 第一个选中的要播放的video标签, 记得是video标签, 
        const currentInstance = videojs(document.querySelector('#video'), this.videoOptions)
        currentInstance.src({
          src: nowPlayVideoUrl,
          type: 'rtmp/flv', // 这个type值必写, 告诉videojs这是一个rtmp流视频
        })
        currentInstance.play()
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
          this.waterColors = ['#32BCB4', '#FFFF00', '#f06058'];
          if (this.tabSwitch == '高锰酸盐指数') {
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
          } else if (this.tabSwitch == '氨氮') {
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
          } else if (this.tabSwitch == '总磷') {
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
          } else if (this.tabSwitch == '溶解氧') {
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
      const opt = {
        stationCode: this.stationData.stCode || this.stationData.stcd,
        stationType: this.stationData.stType,
        days: this.time
      };
      hydraulicApi.waterStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          let arr = [];
          this.dataList = res.data.data.map((item) => {
            item.rz = item.rz || item.z || item.tdz;
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data.data));
          let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
          if (res.data.astroTdz) {
            if (res.data.astroTdz.length > 0) {
              let astroTdz = JSON.parse(JSON.stringify(res.data.astroTdz)).reverse();
              const arr = arrs.map((item) => {
                const str = astroTdz.find((i) => {
                  if (item.tm == i.tm) {
                    return i;
                  }
                });
                item.twc = str.tdz;
                return item;
              });
              info = arr;
            } else {
              info = JSON.parse(JSON.stringify(res.data.data)).reverse();
            }
          } else {
            info = JSON.parse(JSON.stringify(res.data.data)).reverse();
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
            this.waterMarkLine.label.normal.formatter = '警戒水位';
            this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
          } else if (this.dialogType == 'TT') {
            // 潮位
            this.subhead = '潮位曲线';
            if (res.data.warningLevel > 0) {
              this.waterMarkLine.label.normal.formatter = '警戒水位';
              this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
            }
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
            if (res.data.warningLevel > 0) {
              this.chartWaterData.columns = ['时间', '水位', '天文潮', '警戒水位'];
            } else {
              this.chartWaterData.columns = ['时间', '水位', '天文潮'];
            }
          } else if (this.dialogType == 'ZZ') {
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
          this.chartWaterData.rows = info;
        }
      });
    },
    //获取积水测站信息
    getLogStaitonInfo() {
      this.loading = true;
      this.subhead = '水位曲线';
      // console.log(this.stationData);
      const opt = {
        name: this.stationData.name,
        hours: this.time * 24
      };
      hydraulicApi.floodPointHistoricCurve(opt).then((res) => {
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
          if (res.data[0].remark) {
            this.waterMarkLine = {
              data: [
                {
                  yAxis: Number(res.data[0].remark),
                  lineStyle: {
                    color: 'orange'
                  },
                  label: {
                    position: 'middle',
                    formatter: '历史最高水位'
                  }
                }
              ]
            };
          }

          // console.log(this.chartWaterData);
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
            item['当前时间'] = item.now_tm + '/' + item.history_tm || '';
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
              item['当前时间'] = item.now_tm + '/' + item.history_tm || '';
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
              item['当前时间'] = item.now_tm + '/' + item.history_tm || '';
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

    getCameraStationInfo() {
      // console.log(this.stationData);
    },
    // 获取水闸工情测站信息
    getStationInfo() {
      // console.log(this.stationData);
    },
    yearHandle(val) {
      this.year = val;
      this.getYearRainStationInfo();
    },
    timeHandle(val) {
      this.time = val;
      this.getInfo();
    },
    listTabChange(val) {
      this.activeTab = val;
    },
    // 根据不同类型 获取数据
    getInfo() {
      // this.dialogType == "PP"
      //   ? this.getRainStationInfo()
      //   : this.getWaterStationInfo();
      // console.log(111, this.dialogType);
      console.log(109, this.dialogType);
      switch (this.dialogType) {
        case 'PP':
          this.getRainStationInfo();
          break;
        case 'flow':
          this.getFlowDetail();
          break;
        case 'FP':
          this.getLogStaitonInfo();
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
            width: 200
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
          // console.log(info, "dasd");
          this.chartWaterData.rows = info;
        }
        console.log(this.dataList, 666);
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
.disaster-dialog {
  padding: 0;
  height: 650px;
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
.video-player {
  width: 100%;
  height: 100%;
}
</style>
