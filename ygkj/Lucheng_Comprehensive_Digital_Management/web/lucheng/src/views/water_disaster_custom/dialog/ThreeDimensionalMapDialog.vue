<template>
  <div class="detail-table">
    <DataDetailDialog :visible.sync="visible" :before-close="handleClose" class="detail-table" :title="title">
      <!-- 三维地图 潮位 降雨 视频   // tide rain camera -->
      <div v-if="titleContain == '潮位'" class="new_box">
        <div class="left_box">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">24小时水位曲线</span>
          </div>
          <img style="width: 100%" src="../../../assets/images/long_line.png" alt="" />
          <div class="curve_line">
            <ve-line
              :extend="chartWaterExtend"
              :colors="waterColors"
              :data="chartWaterData"
              :settings="chartWaterSettings"
              height="100%"
              width="100%"
              :grid="waterGrid"
              :mark-line="waterMarkLine"
            />
          </div>
        </div>
        <div class="right_box">
          <div>
            <img src="../../../assets/images/diamonds.png" alt="" />
            <span style="color: #fff">列表</span>
          </div>
          <img style="width: 100%" src="../../../assets/images/short_line.png" alt="" />
          <div class="table-box">
            <primary-map-table :columnList="watercolumnList" :dataList="dataList"></primary-map-table>
          </div>
        </div>
      </div>
      <!-- 降雨 -->
      <div v-if="titleContain == '降雨'" class="new_box" v-loading="loading">
        <div class="chart-box">
          <div class="tb-top-panel" v-if="carouselData.length > 0">
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
            </slot>
          </primary-subhead>
          <div class="chart-wrap">
            <div class="chart-content">
              <span style="color: #fff; white-space: nowrap">mm</span>
              <ve-histogram
                :data="chartRainData"
                :extend="chartRainExtend"
                height="100%"
                width="100%"
                :grid="rainGrid"
                :settings="chartRainSettings"
                :yAxis="yAxis"
              />
            </div>
          </div>
        </div>
        <div class="info-box">
          <div>
            <span style="color: #fff" class="info-text">列表</span>
          </div>
          <div class="table-box">
            <primary-map-table :columnList="rainColumnList" :dataList="dataList"></primary-map-table>
          </div>
        </div>
      </div>
      <!-- 视频 -->
      <div v-if="titleContain == '视频'" class="video-content new_box">
        <!-- rtmp视频播放 -->
        <video-player v-if="pointData.cameraSource == 2" ref="videoPlayer" :options="playerOptions" />
        <!-- 正常视频播放 -->
        <primary-video v-else ref="videoDialog" :code="pointData.cameraIndexCode" />
      </div>
    </DataDetailDialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { PrimarySubhead, PrimaryMapTab, PrimaryMapTable, PrimaryVideo } from '@/components';
import { DataDetailDialog } from './DetailsCommonDialog/components';
import { waterAndRain } from '@/api/bigScreenPart';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
export default {
  name: 'ThreeDimensionalMapDialog',
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
        show: false,
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
      xAxis: {
        boundaryGap: false
      },
      series: [
        {
          type: 'line',
          symbol: 'circle',
          symbolSize: 1
        }
      ],
      'xAxis.0.axisLabel.color': '#fff',
      'yAxis.0.axisLabel.color': '#fff'
    };
    this.chartWaterSettings = {
      showLine: ['水位曲线']
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
      resize: false,
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
      year: 2021,
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
      subhead: '降雨分析',
      historyList: [],
      chartRainData: {
        columns: ['当前时间', '当前雨量', '历史同期最大雨量'],
        rows: []
      },
      rainColumnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'drp',
          label: '降雨量(mm)'
        }
      ],
      carouselData: [],
      waterColors: ['#00BAFF', '#FCEE27', '#1CFFF5'],
      watercolumnList: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'tdz',
          label: '潮位(m)'
        }
      ],
      chartWaterData: {
        columns: ['时间', '实时水位'],
        rows: []
      },
      dataList: [],
      waterMarkLine: {
        data: [
          {
            yAxis: 0
          }
        ],
        lineStyle: {
          normal: {
            type: 'solid',
            color: '#FF4A1A ' // 这儿设置安全基线颜色
          }
        },
        label: {
          normal: {
            position: 'middle',
            formatter: '警戒线水位' // 这儿设置安全基线
          }
        }
      },
      loading: false
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    pointData: {
      type: [Object, Array]
    },
    title: {
      type: String,
      default: '潮位'
    }
  },
  components: {
    DataDetailDialog,
    PrimaryVideo,
    PrimarySubhead,
    PrimaryMapTable
  },
  created() {},
  mounted() {
    if (this.title.search('视频') !== -1 && this.pointData.cameraSource == 2) {
      let source = {
        type: 'application/x-mpegURL',
        src: this.pointData.previewUrl
      };
      this.playerOptions.sources.push(source);
    }
    // this.chartWaterData = [];
    // this.chartRainData = [];
    // this.dataList = [];
    this.$nextTick(() => {
      this.title.indexOf('潮位') !== -1
        ? this.getRainlist()
        : this.title.indexOf('降雨') !== -1
        ? this.getRainStationInfo()
        : '';
    });
    // 测试用请求数据
  },
  computed: {
    ...mapGetters(['formatterEndTime', 'formatterStartTime', 'formatterCurrentTime', 'AffairId']),
    titleContain() {
      if (this.title.search('潮位') !== -1) {
        return '潮位';
      } else if (this.title.search('降雨') !== -1) {
        return '降雨';
      } else {
        return '视频';
      }
    }
  },
  methods: {
    // 展示历史降雨
    rainfallHistoryMax() {
      let opt = {
        stationCode: this.pointData.stcd || this.pointData.stCode || this.pointData.code || this.pointData.mgstcd
      };
      hydraulicApi.rainfallHistoryMax(opt).then((res) => {
        this.carouselData = res.data;
      });
    },
    yearHandle(val) {
      this.year = val;
      this.getYearRainStationInfo();
    },
    timeHandle(val) {
      this.time = val;
      this.getRainStationInfo();
    },
    // 历史同期降雨分析
    getYearRainStationInfo() {
      const opt = {
        year: this.year,
        stationCode: this.pointData.stcd || this.pointData.stCode || this.pointData.code || this.pointData.mgstcd,
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
          this.chartRainData.rows = info;
        }
      });
    },
    // 获取降雨信息
    getRainStationInfo() {
      this.rainfallHistoryMax();
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
        stationCode: this.pointData.stcd,
        days: this.time
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
            this.chartRainData.rows = info;
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
            this.chartRainData.rows = info;
          }
        }
      });
    },
    // 获取潮位曲线数据和列表数据
    getRainlist() {
      this.loading = true;
      let opt = {
        stationCode: this.pointData.stCode,
        stationType: 'TT',
        days: 1
      };
      waterAndRain.waterLevels(opt).then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.dataList = res.data.data;
          let info = JSON.parse(JSON.stringify(res.data.data));
          let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
          if (res.data.astroTdz && res.data.astroTdz.length > 0) {
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
          this.chartWaterData.columns = ['时间', '潮位'];
          info.map((item) => {
            item['时间'] = item.tm;
            item['潮位'] = item.tdz;
            item['保证水位'] = res.data.guaranteeLevel;
            item['警戒水位'] = res.data.warningLevel;
            return item;
          });
          this.chartWaterData.rows = info;
          this.waterMarkLine[0].data.yAxis = res.data.warningLevel;
        }
      });
    },
    handleClose() {
      this.$emit('closeDialog');
      // this.chartWaterData = [];
    }
  }
};
</script>
<style lang="scss" scoped>
.new_box {
  width: 100%;
  height: 100%;
  display: flex;
  & > img {
    width: 90%;
  }
  justify-content: flex-start;
  .left_box > div:nth-child(1),
  .right_box > div:nth-child(1) {
    margin-top: 20px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .rain_box {
    margin-top: 20px;
    align-items: center;
    width: 67%;
    padding: 10px;
    .curve_line {
      width: 100%;
      height: calc(100% - 100px);
      .nodata {
        width: 100%;
        height: 100%;
      }
    }
  }
  .left_box {
    width: 67%;
    height: 100%;
    padding: 10px;
    .curve_line {
      width: 100%;
      height: calc(100% - 100px);
      .nodata {
        width: 100%;
        height: 100%;
      }
    }
  }
  .right_box {
    width: 33%;
    height: 100%;
    padding: 10px;
    > img {
      width: 90%;
    }
    .table-box {
      margin-top: 10px;
      width: 100%;
      height: calc(100% - 100px);
    }
  }
}
.video-content {
  height: 100%;
  width: 100%;
}
.subhead-wrap {
  width: 100%;
  @include flexbox;
  align-items: center;
  margin: 16px 0;
  font-size: 16px;
  .subhead {
    height: 16px;
    padding-left: 8px;
    border-left: 4px solid #32bcb4;
    color: #fff;
  }
  .subhead-operate {
    > div {
      display: inline-block;
      padding: 8px 12px;
      background: rgba(0, 0, 0, 0.4);
      color: #fff;
      cursor: pointer;
      &:first-child {
        border-radius: 6px 0 0 6px;
      }
      &:last-child {
        border-radius: 0 6px 6px 0;
      }
      &.active {
        background-color: #328173;
      }
    }
  }
}
.chart-box {
  @include flexbox;
  @include flexflow(column, wrap);
  width: 67%;
  padding-right: 20px;
  .chart-wrap {
    @include flex(1);
    position: relative;
    height: calc(100% - 50px);
  }
  .chart-content {
    width: 100%;
    height: calc(100% - 50px);
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
  .info-text {
    color: #fff;
    font-size: 16px;
    height: 40px;
    line-height: 40px;
    padding: 10px 10px;
  }
}
</style>
