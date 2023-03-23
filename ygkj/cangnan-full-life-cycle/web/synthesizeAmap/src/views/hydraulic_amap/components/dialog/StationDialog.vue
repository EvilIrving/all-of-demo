<template>
  <div
    class="water-dialog map-dialog"
    v-loading="loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  >
    <div class="chart-box" v-if="dialogType != 'camera'">
      <primary-subhead :text="subhead">
        <slot>
          <el-select
            v-if="dialogType != '水文站水质'"
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
          <div v-if="dialogType == '水文站水质'">
            <span
              :class="tabSwitch == '高锰酸盐指数' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('高锰酸盐指数')"
              >高锰酸盐指数</span
            >
            <span
              :class="tabSwitch == '氨氮' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('氨氮')"
              >氨氮</span
            >
            <span
              :class="tabSwitch == '总磷' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('总磷')"
              >总磷</span
            >
            <span
              :class="tabSwitch == '溶解氧' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('溶解氧')"
              >溶解氧</span
            >
          </div>
          <span  v-if="dialogType != '水文站水质'" style="color: #fff; white-space: nowrap">{{
            dialogType == "PP" ? "mm" : dialogType == "SS" ? "cm" : "m"
          }}</span>
          <ve-line
            v-if="
              dialogType == 'RR' ||
              dialogType == 'ZZ' ||
              dialogType == 'DD' ||
              dialogType == 'TT'||
              dialogType == 'SS'||
              dialogType == '水文站水质' ||
              dialogType == 'flow' ||
              dialogType == 'FP'
            "
            :extend="chartWaterExtend"
            :colors="waterColors"
            :data="chartWaterData"
            :settings="chartWaterSettings"
            height="100%"
            width="100%"
            :grid="waterGrid"
            :mark-line="waterMarkLine"
          />
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
        </div>
      </div>
    </div>
    <div class="info-box" v-if="dialogType != 'camera'">
      <primary-map-tab
        ref="listTab"
        :tabArr="listTabArr"
        :tabName="listTab"
        @tabChange="listTabChange"
      ></primary-map-tab>
      <div class="table-box">
        <primary-map-table
          :columnList="columnList"
          :dataList="dataList"
        ></primary-map-table>
      </div>
    </div>
    <div class="video-content" v-if="dialogType == 'camera'">
      <primary-video
        ref="videoDialog"
        :code="stationData.cameraIndexCode"
      ></primary-video>
    </div>
  </div>
</template>

<script>
import {
  PrimarySubhead,
  PrimaryMapTab,
  PrimaryMapTable,
  PrimaryVideo,
} from "@/components";
import { mapGetters } from "vuex";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
export default {
  name: "WaterDialog",
  props: {
    dialogType: {
      type: String,
      default: '水质'
    },
    stationData: Object,
  },
  data() {
    this.chartRainSettings = {
      showLine: ["累计雨量"],
      itemStyle: {
        //配置一些样式
        color: {
          type: "linear",
          x: 0, //X轴起始坐标
          y: 0, //Y轴起始坐标
          x2: 1, //X轴终点坐标
          y2: 1, //Y轴终点坐标
          colorStops: [
            {
              offset: 0,
              color: "#9DE16C", // 0% 处的颜色
            },
            {
              offset: 1,
              color: "#07BBB9", // 100% 处的颜色
            },
          ],
          globalCoord: false, // 缺省为 false
        },
      },
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
        icon: "rect",
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: "#fff",
          fontSize: 12,
        },
      },
      "xAxis.0.axisLabel.color": "#fff",
      series(v) {
        v && v.forEach((i) => (i.barGap = "0%"));
        return v;
      },
    };
    this.chartWaterExtend = {
      legend: {
        icon: "rect",
        itemWidth: 8,
        itemHeight: 4,
        right: 0,
        bottom: 0,
        textStyle: {
          //图例文字的样式
          color: "#fff",
          fontSize: 12,
        },
      },
      "xAxis.0.axisLabel.color": "#fff",
      "yAxis.0.axisLabel.color": "#fff",
    };
    this.rainGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0,
    };
    this.waterGrid = {
      top: 20,
      bottom: 30,
      left: 0,
      right: 0,
    };
    this.waterMarkLine = {
      symbol: "none", //去掉警戒线最后面的箭头
      data: [
        {
          yAxis: 30,
        },
      ],
      lineStyle: {
        normal: {
          color: "red", // 这儿设置安全基线颜色
        },
      },
      label: {
        normal: {
          position: "middle",
          formatter: "超汛限水位", // 这儿设置安全基线
        },
      },
    };
    this.yAxis = {
      inverse: false,
      max: 20,
      textStyle: {
        color: "#fff", //这里是改变字体颜色
      },
      axisLine: {
        show: false,
        lineStyle: {
          color: "#fff",
        },
      },
    };
    return {
      tabSwitch: '高锰酸盐指数',
      subhead: "水位曲线",
      timeOptions: [
        {
          value: 1,
          label: "前一天",
        },
        {
          value: 2,
          label: "前二天",
        },
        {
          value: 3,
          label: "前三天",
        },
        {
          value: 4,
          label: "前四天",
        },
        {
          value: 7,
          label: "前一周",
        },
        {
          value: 14,
          label: "前二周",
        },
      ],
      time: 14,
      charRaintData: {
        columns: ["时间", "雨量"],
        rows: [],
      },
      listTabArr: [
        {
          value: 0,
          label: "数据列表",
        },
      ],
      listTab: "",
      columnList: [
        {
          width: 160,
          prop: "tm",
          label: "时间",
        },
      ],
      dataList: [],
      waterColors: ["#32BCB4", "#19af47", "red"],
      chartWaterData: {
        columns: ["时间", "水位"],
        rows: [],
      },
      activeTab: 0,
      cameraCode: "",
      loading: false,
      specialType: "",
    };
  },
  computed: {
    ...mapGetters([
      "formatterEndTime",
      "formatterStartTime",
      "formatterCurrentTime",
    ]),
  },
  components: {
    PrimarySubhead,
    PrimaryMapTab,
    PrimaryMapTable,
    PrimaryVideo,
  },
  watch: {},
  mounted() {
    this.$nextTick(()=> {
      if (this.dialogType == '水文站水质') {
        this.qualityCurve(this.stationData.siteNo);
      }
    });
  },
  methods: {
    // tab切换
    tabSwitchClick(val) {
      this.tabSwitch = val;
      this.qualityCurve(this.stationData.siteNo);
    },
    // 水文水质数据接口
    async qualityCurve(val) {
      this.dataList = [];
      this.columnList = [];
      this.subhead = "水质曲线";
      let opt = {
        month: '12',
        siteNo: val
      }
      await hydraulicApi.qualityCurve(opt).then((res) => {
        if (res.code == 0) {
          this.waterMarkLine = {};
          this.loading = false;
          this.dataList = res.data.reverse();
          this.waterColors = ["#32BCB4", "#FFFF00","#f06058"];
          if (this.tabSwitch == '高锰酸盐指数') {
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "codmn",
                label: "高锰酸盐指数",
              }
            ]
            let info = JSON.parse(JSON.stringify(res.data));
            info.map((item) => {
              item["时间"] = item.tm;
              item['高锰酸盐指数'] = item.codmn;
              return item;
            });
            this.chartWaterData.columns = ['时间','高锰酸盐指数'];
            this.chartWaterData.rows = info;
          }else if (this.tabSwitch == '氨氮') {
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "nh3N",
                label: "氨氮",
              }
            ]
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item["时间"] = item.tm;
              item['氨氮'] = item.nh3N;
              return item;
            });
            this.chartWaterData.columns = ['时间', '氨氮'];
            this.chartWaterData.rows = info;
          }else if (this.tabSwitch == '总磷') {
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "tp",
                label: "总磷",
              }
            ]
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item["时间"] = item.tm;
              item['总磷'] = item.tp;
              return item;
            });
            this.chartWaterData.columns = ['时间','总磷'];
            this.chartWaterData.rows = info;
          }else if (this.tabSwitch == '溶解氧') {
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "do",
                label: "溶解氧",
              }
            ]
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            info.map((item) => {
              item["时间"] = item.tm;
              item['溶解氧'] = item.do;
              return item;
            });
            this.chartWaterData.columns = ['时间',"溶解氧"];
            this.chartWaterData.rows = info;
          }
        }
      })
    },
    // 获取水位测站信息
    getWaterStationInfo() {
      this.loading = true;
      this.subhead = "水位曲线";
      const opt = {
        stationCode: this.stationData.stCode || this.stationData.stcd,
        stationType: this.stationData.stType,
        days: this.time,
      };
      if (this.dialogType == 'SS') {
        hydraulicApi.moistureStationInfo(opt).then((res) => {
          if (res.code == 0) {
            this.waterMarkLine = {};
            this.loading = false;
            this.subhead = "墒情曲线";
            this.waterColors = ["#32BCB4", "#FFFF00","#f06058"];
            this.dataList = res.data.reverse();
            let info = JSON.parse(JSON.stringify(res.data)).reverse();
            this.columnList = [
              {
                prop: "tm",
                label: "时间",
              },
              {
                prop: "slm10",
                label: "10cm土壤含水率",
                sortable: true,
              },
              {
                prop: "slm20",
                label: "20cm土壤含水率",
                sortable: true,
              },
              {
                prop: "slm30",
                label: "30cm土壤含水率",
                sortable: true,
              },
            ];
            this.chartWaterData.columns = [ "时间", "10cm土壤含水率", "20cm土壤含水率", "30cm土壤含水率"];
            info.map((item) => {
              item["时间"] = item.tm;
              item["10cm土壤含水率"] = item.slm10;
              item["20cm土壤含水率"] = item.slm20;
              item["30cm土壤含水率"] = item.slm30;
              return item;
            });
            this.chartWaterData.rows = info;
          }
        });
      }else {
        hydraulicApi.waterStationInfo(opt).then((res) => {
          this.loading = false;
          if (res.code == 0) {
            this.dataList = res.data.data.map((item) => {
              item.rz = item.rz || item.z || item.tdz;
              return item;
            });
            let info = [];
            let arrs = JSON.parse(JSON.stringify(res.data.data)).reverse();
            if (res.data.astroTdz) {
              if (res.data.astroTdz.length > 0) {
                let astroTdz = JSON.parse(JSON.stringify(res.data.astroTdz)).reverse();
                const arr = arrs.map(item => {
                  const str = astroTdz.find(i => {
                    if (item.tm == i.tm) { return i }
                  })
                  item.twc = str.tdz;
                  return item;
                });
                info = arr;
              }else {
                info = JSON.parse(JSON.stringify(res.data.data)).reverse();
              }
            }else {
              info = JSON.parse(JSON.stringify(res.data.data)).reverse();
            }
            if (this.dialogType == "RR") {
              // 水库
              this.waterMarkLine.label.normal.formatter = "汛限水位";
              this.waterMarkLine.data[0].yAxis = res.data.limitLevel;
              this.waterColors = ["#32BCB4", "red"];
            } else if (this.dialogType == "DD") {
              // 闸坝
              this.waterMarkLine.label.normal.formatter = "警戒水位";
              this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
              this.waterColors = ["#32BCB4", "#32BCB4", "#32BCB4", "red"];
            } else if (this.dialogType == "ZZ") {
              // 河道
              this.waterMarkLine.label.normal.formatter = "警戒水位";
              this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
              this.waterColors = ["#32BCB4", "red"];
            } else if (this.dialogType == "TT") {
              // 潮位
              this.subhead = "潮位曲线";
              this.waterMarkLine.label.normal.formatter = "警戒水位";
              this.waterMarkLine.data[0].yAxis = res.data.warningLevel; // 警戒水位
            }
            if (this.dialogType == "DD") {
              this.columnList = [
                {
                  width: 160,
                  prop: "tm",
                  label: "时间",
                },
                {
                  prop: "upz",
                  label: "闸上水位(m)",
                },
                {
                  prop: "dwz",
                  label: "闸下水位(m)",
                },
                {
                  prop: "ll",
                  label: "过闸流量(m³/s)",
                },
              ];
              this.chartWaterData.columns = ["时间", "闸上水位", "闸下水位", "过闸流量", "警戒水位"];
            } else if (this.dialogType == "TT"){
              this.columnList = [
                {
                  width: 160,
                  prop: "tm",
                  label: "时间",
                },
                {
                  prop: "rz",
                  label: "水位(m)",
                },
              ];
              this.chartWaterData.columns = ["时间", "水位","天文潮","警戒水位"];
            } else if (this.dialogType == "RR") {
              // 水库
              this.columnList = [
                {
                  width: 160,
                  prop: "tm",
                  label: "时间",
                },
                {
                  prop: "rz",
                  label: "水位(m)",
                },
              ];
              this.chartWaterData.columns = ["时间", "水位", "汛限水位"];
            }else {
              this.columnList = [
                {
                  width: 160,
                  prop: "tm",
                  label: "时间",
                },
                {
                  prop: "rz",
                  label: "水位(m)",
                },
              ];
              this.chartWaterData.columns = ["时间", "水位", "警戒水位"];
            }
            info.map((item) => {
              item["时间"] = item.tm;
              item["水位"] = item.rz || item.z || item.tdz;
              item["天文潮"] = item.twc;
              item["闸上水位"] = item.upz;
              item["闸下水位"] = item.dwz;
              item["过闸流量"] = item.ll;
              return item;
            });
            this.chartWaterData.rows = info;
          }
        });
      }
    },
    //获取演练水库水位信息
    getPreWaterStationInfo() {
      this.loading = true;
      this.subhead = "水位曲线";
      const opt = {
        resCode: this.stationData.code || this.stationData.stcd,
        start: this.stationData.start,
        end: this.stationData.end,
        timeForward: true,
      };
      hydraulicApi.lmRsvrDrillWlCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data.map((item) => {
            item.rz = item.waterLevel || item.z || item.tdz;
            item.tm = item.dataTime;
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data)).reverse();
          if (this.dialogType == "RR") {
            // 水库
            this.waterMarkLine.label.normal.formatter = "汛限水位";
            this.waterMarkLine.data[0].yAxis = res.data[0].currentLimitLevel;
          }
          this.columnList = [
            {
              width: 160,
              prop: "tm",
              label: "时间",
            },
            {
              prop: "rz",
              label: "水位(m)",
            },
          ];
          this.chartWaterData.columns = ["时间", "水位"];
          info.map((item) => {
            item["时间"] = item.dataTime;
            item["水位"] = item.waterLevel || item.z || item.tdz;
            return item;
          });
          this.chartWaterData.rows = info;
        }
      });
    },
    // 获取雨量测站信息
    getRainStationInfo(type) {
      this.specialType = type; //复盘雨量弹窗特殊标记
      this.loading = true;
      this.columnList = [
        {
          width: 160,
          prop: "tm",
          label: "时间",
        },
        {
          prop: "drp",
          label: "降雨量(mm)",
        },
      ];
      this.subhead = "降雨分析";
      const opt = {
        stationCode:
          this.stationData.stcd ||
          this.stationData.stCode ||
          this.stationData.code,
        days: this.time,
      };
      if (type == "复盘雨量") {
        const start = this.formatterStartTime("yyyy-MM-DD hh:mm:ss");
        const end = this.formatterEndTime("yyyy-MM-DD hh:mm:ss");
        opt.start = start;
        opt.end = end;
      }
      hydraulicApi.rainStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          let info = JSON.parse(JSON.stringify(res.data)).reverse();
          let max = 0;
          info.map((item) => {
            item["时间"] = item.tm;
            item["雨量"] = item.drp;
            max = item.drp > max ? item.drp : max;
            return item;
          });
          this.yAxis.max = max * 1.5;
          this.charRaintData.rows = info;
        }
      });
    },
    // 获取演练雨量信息
    getPreviewRainStationInfo() {
      this.loading = true;
      this.columnList = [
        {
          width: 160,
          prop: "tm",
          label: "时间",
        },
        {
          prop: "drp",
          label: "降雨量(mm)",
        },
      ];
      this.subhead = "降雨分析";
      let opt = {
        start: new Date(this.formatterStartTime()).format(
          "yyyy-MM-dd hh:mm:ss"
        ),
        end: new Date(this.formatterCurrentTime()).format(
          "yyyy-MM-dd hh:mm:ss"
        ),
        timeForward: true,
        stationCode:
          this.stationData.mgstcd ||
          this.stationData.stcd ||
          this.stationData.stCode ||
          this.stationData.code,
      };
      hydraulicApi.rainStationInfo(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          let info = JSON.parse(JSON.stringify(res.data)).reverse();
          let max = 0;
          info.map((item) => {
            item["时间"] = item.tm;
            item["雨量"] = item.drp;
            max = item.drp > max ? item.drp : max;
            return item;
          });
          this.yAxis.max = max * 1.5;
          this.charRaintData.rows = info;
        }
      });
    },
    //获取积水测站信息
    getLogStaitonInfo() {
      this.loading = true;
      this.subhead = "水位曲线";
      // console.log(this.stationData);
      const opt = {
        name: this.stationData.name,
        hours: this.time * 24,
      };
      hydraulicApi.floodPointHistoricCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data;
          this.columnList = [
            {
              prop: "key",
              label: "时间",
              width: 200,
            },
            {
              prop: "value",
              label: "水深(cm)",
            },
          ];
          this.chartWaterData.columns = ["时间", "水深"];
          let info = [];
          res.data.map((item) => {
            info.push({ 时间: item.key, 水深: item.value });
            return item;
          });
          this.chartWaterData.rows = info;
        }
      });
    },

    // getCameraStationInfo() {
    //   // console.log(this.stationData);
    // },
    // // 获取水闸工情测站信息
    // getStationInfo() {
    //   // console.log(this.stationData);
    // },
    timeHandle(val) {
      this.time = val;
      this.getInfo();
    },
    listTabChange(val) {
      this.activeTab = val;
    },
    // 根据不同类型 获取数据
    getInfo() {
      switch (this.dialogType) {
        case "PP":
          this.getRainStationInfo();
          break;
        case "flow":
          this.getFlowDetail();
          break;
        case "FP":
          this.getLogStaitonInfo();
          break;
        default:
          this.getWaterStationInfo();
      }
    },
    // 第一次设置选中第一个tab
    setFirstTab() {
      this.$refs.listTab.tabChange(0);
    },
    //流量
    async getFlowDetail() {
      this.loading = true;
      this.columnList = [
        {
          prop: "tm",
          label: "时间",
          width: 160,
        },
        {
          prop: "z",
          label: "水位(m)",
        },
        {
          prop: "flow",
          label: "流量(m³/s)",
        },
      ];
      this.subhead = "流量列表";
      const opt = {
        // dataTime: new Date().format("yyyy-MM-dd hh:mm:ss"),
        hours: this.time * 24,
        stCode: this.stationData.stCode,
      };
      this.chartWaterData.columns = ["时间", "流量", "水位"];
      hydraulicApi.getMrwlCurve(opt).then((res) => {
        this.loading = false;
        if (res.code == 0) {
          this.dataList = res.data.map((item) => {
            item.tm = new Date(item.tm).format("yyyy-MM-dd hh:mm:ss");
            return item;
          });
          let info = JSON.parse(JSON.stringify(res.data)).reverse();
          info.map((item) => {
            item["时间"] = new Date(item.tm).format("yyyy-MM-dd hh:mm:ss");
            item["流量"] = item.flow;
            item["水位"] = item.z;
            return item;
          });
          // console.log(info, "dasd");
          this.chartWaterData.rows = info;
        }
        console.log(this.dataList, 666);
      });
    },
  },
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
}
</style>
