<!--
 * @Author: ly
 * @Date: 2021-09-01 13:43:37
 * @LastEditTime: 2021-09-02 11:44:16
 * @LastEditors: Please set LastEditors
 * @Description: 水厂供水
 * @FilePath: -
-->
<template>
  <div class="government-assets-card">
    <panel-item title="水厂供水" tc @tclick="tclick">
      <div class="charts-panel">
        <!-- 水厂 -->
        <sub-title-click-card
          title="水厂"
          type="radio"
          :radioList="['数量', '人口']"
          @radioChange="areaChange"
        >
          <div slot="title-operate">
            <i
              title="地图落点"
              :class="[
                sydbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                'icon-sty',
              ]"
              @click="drawPointArea('sydbtn')"
            />
            <i
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('水厂')"
            />
          </div>
          <div class="chart-panel">
            <div class="chart-tab">
              <span
                :class="['chart-tab-item', areaType == 0 ? 'active' : '']"
                @click="areaTypeChange(0)"
                >按规模</span
              >
              <span
                :class="['chart-tab-item', areaType == 1 ? 'active' : '']"
                @click="areaTypeChange(1)"
                >按类型</span
              >
            </div>
            <div class="pie-box">
              <div id="pieChart" style="width: 40%; height: 100%"></div>
              <div class="legend-panel">
                <div
                  class="legend-item"
                  v-for="(item, index) in pieChartData"
                  :key="index"
                >
                  <div>
                    <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                    <span
                      class="text"
                      :style="{ width: areaType == 0 ? '100px' : '60px' }"
                    >
                      {{ item.name }}
                    </span>
                  </div>
                  <div>
                    <span class="num">{{ item.value }}</span>
                    {{ areaTabType == "数量" ? "个" : "万人" }}
                  </div>
                </div>
              </div>
            </div>
            <!-- 水厂行政区划统计图 -->
            <div class="line-box">
              <span style="margin-left: 30px">{{
                areaTabType == "数量" ? "个" : "万人"
              }}</span>
              <div id="stackedColumn" style="width: 100%; height: 80%" />
            </div>
          </div>
        </sub-title-click-card>
        <!-- 水厂运行状态 -->
        <sub-title-click-card
          title="水厂运行状态"
          type="radio"
          :radioList="['数量', '人口']"
          @radioChange="warnChange"
        >
          <div slot="title-operate">
            <i
              title="地图落点"
              :class="[
                sldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                'icon-sty',
              ]"
              @click="drawPointWarn('sldbtn')"
            />
            <i
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('水厂运行状态')"
            />
          </div>
          <div class="chart-panel">
            <div class="count-box">
              <div class="all-count">
                总数
                <span>{{
                  statisticsData.total.num == null
                    ? "-"
                    : statisticsData.total.num
                }}</span>
                {{ warnTabType == "数量" ? "个" : "万人" }}
              </div>
              <div class="count4-box">
                <div class="count4-box-item green">
                  <p>正常供水</p>
                  <p class="num">
                    {{
                      warnTabType == "数量"
                        ? statisticsData.norm.num
                        : statisticsData.norm.pop
                    }}
                  </p>
                </div>
                <div class="count4-box-item yellow">
                  <p>供水预警</p>
                  <p class="num">
                    {{
                      warnTabType == "数量"
                        ? statisticsData.volQuaWarn.num
                        : statisticsData.volQuaWarn.pop
                    }}
                  </p>
                </div>
                <div class="count4-box-item orange">
                  <p>水质预警</p>
                  <p class="num">
                    {{
                      warnTabType == "数量"
                        ? statisticsData.volumeWarn.num
                        : statisticsData.volumeWarn.pop
                    }}
                  </p>
                </div>
                <div class="count4-box-item red">
                  <p>水质供水预警</p>
                  <p class="num">
                    {{
                      warnTabType == "数量"
                        ? statisticsData.qualityWarn.num
                        : statisticsData.qualityWarn.pop
                    }}
                  </p>
                </div>
              </div>
            </div>
            <!-- 水厂运行状态行政区划统计 -->
            <div class="line-box">
              <span style="margin-left: 30px">{{
                warnTabType == "数量" ? "个" : "万人"
              }}</span>
              <div id="runningStatus" style="width: 100%; height: 80%" />
            </div>
          </div>
        </sub-title-click-card>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import SubTitleClickCard from "../components/SubTitleClickCard.vue";

import * as echarts from "echarts"; // echart插件

import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterworksSupplyCard",
  layerName: [
    "warnPointList1",
    "warnPointList2",
    "warnPointList3",
    "warnPointList4",
    "areaPointLayer1",
    "areaPointLayer2",
    "areaPointLayer3",
    "areaPointLayer4"
  ],
  legendName: ["水量预警", "水厂"],
  components: {
    PanelItem,
    SubTitleClickCard
  },
  data() {
    return {
      timer: null,
      cityList: [],
      singleList: [],
      townList: [],
      unionList: [],
      xAxisData: [],
      cityData: [],
      singleData: [],
      townData: [],
      unionData: [],
      statisticsData: {
        total: { num: 0 },
        norm: { num: 0 },
        volQuaWarn: { num: 0 },
        volumeWarn: { num: 0 },
        qualityWarn: { num: 0 }
      },
      areaTabType: "数量", // 数量 人口 水源地
      warnTabType: "数量", // 数量 人口 水量预警
      areaType: 0, // 按规模 按类型
      sydbtn: false,
      sldbtn: false,
      totalLength: 4,
      pieChartData: [],
      warningCount: 0,
      waterPointList: [],
      pieChart: null,
      stackedChart: null,
      runningChart: null,
      StackedData: [],
      xAxisList: []
    };
  },
  computed: {
    ...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime", "AffairId"]),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.$nextTick(() => {
      window.addEventListener("resize", this.handleWindowResize);
      this.statisSwhsNumByScale();
      this.statisSwhsNumByArea();
      this.runningByArea();
      this.AttSwhsBaseLoadPage();
      this.warnChange("数量");
      this.timer = setInterval(() => { }, 60000);
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),

    // 水厂统计 按规模
    async statisSwhsNumByScale() {
      let opt = {
        type: this.areaType == 0 ? "2" : "3"
      };
      this.pieChartData = [];
      await waterResourceApi.waterWorksStats(opt).then(res => {
        res.data.forEach(item => {
          this.pieChartData.push({
            name:
              this.areaTabType == "数量" && this.areaType == 0
                ? item.wasuRang
                : this.areaTabType == "数量" && this.areaType == 1
                  ? item.engType
                  : this.areaTabType == "人口" && this.areaType == 0
                    ? item.wasuRang
                    : item.engType,
            value:
              this.areaTabType == "数量" && this.areaType == 0
                ? item.num
                : this.areaTabType == "数量" && this.areaType == 1
                  ? item.num
                  : this.areaTabType == "人口" && this.areaType == 0
                    ? item.pop
                    : item.pop
          });
        });
        const addDuration = arr => {
          let res = 0;
          for (let i = 0; i < arr.length; i++) {
            res += arr[i].value;
          }
          return res;
        };
        this.totalLength = addDuration(this.pieChartData);
        this.$nextTick(() => {
          this.pieChartOpt();
        });
      });
    },
    // 水厂 行政区划
    async statisSwhsNumByArea() {
      this.xAxisList = [];
      this.cityList = [];
      this.singleList = [];
      this.townList = [];
      this.unionList = [];
      await waterResourceApi.waterWorksStats({ type: "7" }).then(res => {
        if (this.areaTabType == "数量") {
          res.data.city.forEach(val => {
            this.xAxisList.push(val.areaName);
            this.cityList.push(val.num);
          });
          res.data.single.forEach(val => {
            this.singleList.push(val.num);
          });
          res.data.town.forEach(val => {
            this.townList.push(val.num);
          });
          res.data.union.forEach(val => {
            this.unionList.push(val.num);
          });
          this.$nextTick(() => {
            this.StackedOpt();
          });
        } else {
          res.data.city.forEach(val => {
            this.xAxisList.push(val.areaName);
            this.cityList.push(val.pop);
          });
          res.data.single.forEach(val => {
            this.singleList.push(val.pop);
          });
          res.data.town.forEach(val => {
            this.townList.push(val.pop);
          });
          res.data.union.forEach(val => {
            this.unionList.push(val.pop);
          });
          this.$nextTick(() => {
            this.StackedOpt();
          });
        }
      });
    },
    // 水厂 叠加柱状图
    StackedOpt() {
      var chartDom = document.getElementById("stackedColumn");
      if (this.stackedChart) echarts.dispose(this.stackedChart);
      this.stackedChart = echarts.init(chartDom);
      var option;
      option = {
        dataZoom: generateDataZoom(5, this.xAxisList.length),
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: "5%",
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: [
          {
            type: "category",
            data: this.xAxisList,
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
              color: "#fff"
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            }
          }
        ],
        yAxis: [
          {
            type: "value",
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5
              }
            }
          }
        ],
        series: [
          {
            name: "城市水厂",
            type: "bar",
            stack: "单村水站",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#26ccf2",
            data: this.cityList
          },
          {
            name: "乡镇水厂",
            type: "bar",
            stack: "单村水站",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#008fe8",
            data: this.townList
          },
          {
            name: "联村水厂",
            type: "bar",
            stack: "单村水站",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#6267ff",
            data: this.unionList
          },
          {
            name: "单村水站",
            type: "bar",
            stack: "单村水站",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#a000c8",
            data: this.singleList
          }
        ]
      };
      option && this.stackedChart.setOption(option);
    },
    // 水厂 饼图
    pieChartOpt() {
      var chartDom = document.getElementById("pieChart");
      if (this.pieChart) echarts.dispose(this.pieChart);
      this.pieChart = echarts.init(chartDom);
      var option;
      let colorList = ["#26ccf2", "#008fe8", "#6267ff", "#a000c8"];
      let color = [];
      colorList.forEach(item => {
        let obj = {
          type: "linear",
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item // 0% 处的颜色
            },
            {
              offset: 1,
              color: "rgba(0,0,0)" // 100% 处的颜色
            }
          ],
          global: false // 缺省为 false
        };
        color.push(obj);
      });
      let unit = this.areaTabType == "数量" ? "个" : "万人";
      option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              "<span style='font-size: 14px;'>" +
              data.name +
              "</span><br/><span style='font-size: 14px;color: #1CFFF5;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'>" +
              unit +
              "</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;font-size:18px;"
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.totalLength + unit + "\n总数"
              }
            },
            labelLine: {
              show: false
            },
            data: this.pieChartData,
            color: color
          }
        ]
      };
      option && this.pieChart.setOption(option);
    },
    // 水厂运行状态 行政区划
    async runningByArea() {
      this.xAxisData = [];
      this.cityData = [];
      this.singleData = [];
      this.townData = [];
      this.unionData = [];
      await waterResourceApi.waterWorksStats({ type: "4" }).then(res => {
        if (this.warnTabType == "数量") {
          res.data.norm.forEach(val => {
            this.xAxisData.push(val.areaName);
            this.cityData.push(val.num);
          });
          res.data.volumeWarn.forEach(val => {
            this.singleData.push(val.num);
          });
          res.data.qualityWarn.forEach(val => {
            this.townData.push(val.num);
          });
          res.data.volQuaWarn.forEach(val => {
            this.unionData.push(val.num);
          });
          this.$nextTick(() => {
            this.runningOpt();
          });
        } else {
          res.data.norm.forEach(val => {
            this.xAxisData.push(val.areaName);
            this.cityData.push(val.pop);
          });
          res.data.volumeWarn.forEach(val => {
            this.singleData.push(val.pop);
          });
          res.data.qualityWarn.forEach(val => {
            this.townData.push(val.pop);
          });
          res.data.volQuaWarn.forEach(val => {
            this.unionData.push(val.pop);
          });
          this.$nextTick(() => {
            this.runningOpt();
          });
        }
      });
    },
    // 水厂运行状态 叠加柱状图
    runningOpt() {
      var chartDom = document.getElementById("runningStatus");
      if (this.runningChart) echarts.dispose(this.runningChart);
      this.runningChart = echarts.init(chartDom);
      var option;
      option = {
        dataZoom: generateDataZoom(5, this.xAxisList.length),
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow" // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: "5%",
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: [
          {
            type: "category",
            data: this.xAxisList,
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
              color: "#fff"
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            }
          }
        ],
        yAxis: [
          {
            type: "value",
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5
              }
            }
          }
        ],
        series: [
          {
            name: "正常供水",
            type: "bar",
            stack: "水质供水预警",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#26ccf2",
            data: this.cityData
          },
          {
            name: "供水预警",
            type: "bar",
            stack: "水质供水预警",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#008fe8",
            data: this.singleData
          },
          {
            name: "水质预警",
            type: "bar",
            stack: "水质供水预警",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#6267ff",
            data: this.townData
          },
          {
            name: "水质供水预警",
            type: "bar",
            stack: "水质供水预警",
            barWidth: 20,
            emphasis: {
              focus: "series"
            },
            color: "#a000c8",
            data: this.unionData
          }
        ]
      };
      option && this.runningChart.setOption(option);
    },
    // 数量 人口 tab切换  水源地
    areaChange(val) {
      this.areaTabType = val;
      this.statisSwhsNumByScale();
      this.statisSwhsNumByArea();
    },
    // 数量 人口 tab切换 水厂运行状态
    async warnChange(val) {
      this.warnTabType = val;
      // 行政区划统计
      this.runningByArea();
      // 展示统计
      let res = await waterResourceApi.waterWorksStats({ type: 5 });
      if (res.code == 0) {
        this.statisticsData = res.data;
      }
    },
    // 点位信息查询
    async AttSwhsBaseLoadPage(val) {
      this.waterPointList = [];
      let opt = {
        engType: "",
        wasuRang: "",
        withRuntimeInfo: true
      };
      let res = await waterResourceApi.listWaterWorks(opt);
      if (res.code == 0) {
        this.waterPointList = res.data;
      }
    },
    // 水源地 按类型 按规模 切换
    areaTypeChange(val) {
      this.areaType = val;
      this.statisSwhsNumByScale();
    },
    // 水厂 落点
    drawPointArea(btn) {
      this.map.drawClusterPoint([], {}, "warnPointList1");
      this.map.drawClusterPoint([], {}, "warnPointList2");
      this.map.drawClusterPoint([], {}, "warnPointList3");
      this.map.drawClusterPoint([], {}, "warnPointList4");
      this.sldbtn = false;
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/city-icon-aggregation.png"),
        src: require("@/assets/images/city-icon.png")
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/town-icon-aggregation.png"),
        src: require("@/assets/images/town-icon.png")
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/union-icon-aggregation.png"),
        src: require("@/assets/images/union-icon.png")
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/single-icon-aggregation.png"),
        src: require("@/assets/images/single-icon.png")
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.adnm,
          type: "水厂",
          lng: point.cwsLong,
          lat: point.cwsLat,
          ...point,
          props: {
            "覆盖人口(万人)": point.desWasuPop,
            "设计供水规模(吨/天)": point.desWasuScal
          }
        };
        if (this.areaType == 0) {
          if (point.wasuRang == "两百吨千人以下") {
            pointList1.push(obj);
          } else if (point.wasuRang == "两百吨千人以上") {
            pointList2.push(obj);
          } else if (point.wasuRang == "千吨万人") {
            pointList3.push(obj);
          } else if (point.wasuRang == "城市水厂") {
            pointList4.push(obj);
          }
        } else if (this.areaType == 1) {
          if (point.engType == "城市") {
            pointList1.push(obj);
          } else if (point.engType == "乡镇") {
            pointList2.push(obj);
          } else if (point.engType == "联村") {
            pointList3.push(obj);
          } else if (point.engType == "单村") {
            pointList4.push(obj);
          }
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "areaPointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "areaPointLayer2");
        this.map.drawClusterPoint(pointList3, style3, "areaPointLayer3");
        this.map.drawClusterPoint(pointList4, style4, "areaPointLayer4");
        this.$emit("legend-change", "水厂", {
          label: "水厂",
          clusterLayer: ['areaPointLayer1', 'areaPointLayer2', 'areaPointLayer3', 'areaPointLayer4'],
          children: [
            {
              label: "县级以上",
              icon: "legend-sygs-1"
            },
            {
              label: "千吨万人",
              icon: "legend-sygs-2"
            },
            {
              label: "200吨~1000吨",
              icon: "legend-sygs-3"
            },
            {
              label: "200吨以下",
              icon: "legend-sygs-4"
            }
          ]
        });
      } else {
        this.map.drawClusterPoint([], {}, "areaPointLayer1");
        this.map.drawClusterPoint([], {}, "areaPointLayer2");
        this.map.drawClusterPoint([], {}, "areaPointLayer3");
        this.map.drawClusterPoint([], {}, "areaPointLayer4");
        this.$emit("legend-change", "水厂", {});
      }
    },
    // 水厂运行状态 落点
    drawPointWarn(btn) {
      this.map.drawClusterPoint([], {}, "areaPointLayer1");
      this.map.drawClusterPoint([], {}, "areaPointLayer2");
      this.map.drawClusterPoint([], {}, "areaPointLayer3");
      this.map.drawClusterPoint([], {}, "areaPointLayer4");
      this.sydbtn = false;
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/norm-icon-aggregation.png"),
        src: require("@/assets/images/norm-icon.png")
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/volumeWarn-icon-aggregation.png"),
        src: require("@/assets/images/volumeWarn-icon.png")
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/qualityWarn-icon-aggregation.png"),
        src: require("@/assets/images/qualityWarn-icon.png")
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/volQuaWarn-icon-aggregation.png"),
        src: require("@/assets/images/volQuaWarn-icon.png")
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.waterPointList) {
        let obj = {
          ...point,
          name: point.cwsName,
          type: "水厂运行状态",
          lng: point.cwsLong,
          lat: point.cwsLat,
          props: {
            所在县市: point.adnm,
            水厂规模: point.wasuRang,
            "供水服务人口(万人)": point.desWasuPop,
            出厂水水质: point.qualityNormal == "0" ? "异常" : "正常",
            供水状态: point.volumeSupplyStatus == "0" ? "异常" : "正常"
          }
        };
        if (point.runtimeState == 0) {
          pointList1.push(obj);
        } else if (point.runtimeState == 1) {
          pointList2.push(obj);
        } else if (point.runtimeState == 2) {
          pointList3.push(obj);
        } else if (point.runtimeState == 3) {
          pointList4.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "warnPointList1");
        this.map.drawClusterPoint(pointList2, style2, "warnPointList2");
        this.map.drawClusterPoint(pointList3, style3, "warnPointList3");
        this.map.drawClusterPoint(pointList4, style4, "warnPointList4");
        this.$emit("legend-change", "水量预警", {
          label: "水量预警",
          clusterLayer: ['warnPointList1', 'warnPointList2', 'warnPointList3', 'warnPointList4'],
          children: [
            {
              label: "暂无风险",
              icon: "legend-sygs-5"
            },
            {
              label: "黄色预警",
              icon: "legend-sygs-6"
            },
            {
              label: "橙色预警",
              icon: "legend-sygs-7"
            },
            {
              label: "红色预警",
              icon: "legend-sygs-8"
            }
          ]
        });
      } else {
        this.map.drawClusterPoint([], {}, "warnPointList1");
        this.map.drawClusterPoint([], {}, "warnPointList2");
        this.map.drawClusterPoint([], {}, "warnPointList3");
        this.map.drawClusterPoint([], {}, "warnPointList4");
        this.$emit("legend-change", "水量预警", {});
      }
    },
    // 跳转
    toPath() {
      window.open("https://www.baidu.com");
    },
    // 打开详情弹框点击事件
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true
      });
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      if (!this.chart) return;
      this.waterUseConfig();
    }
  }
};
</script>

<style lang="scss" scoped>
.charts-panel {
  display: flex;
  flex-direction: column;
}
.chart-tab {
  margin-bottom: 10px;
}
.chart-tab-item {
  margin-right: 10px;
  font-size: 14px;
  padding: 4px 10px;
  cursor: pointer;
  &.active {
    background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    border-radius: 15px;
  }
}
.pie-box {
  height: 130px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  .legend-panel {
    width: 60%;
    margin-left: 30px;
    .legend-item {
      float: left;
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      font-size: 14px;
      .text {
        display: inline-block;
        // width: 80px;
        margin-right: 10px;
      }
      .num {
        color: #1cfff5;
      }
    }
  }
}
.line-box {
  width: 100%;
  height: 170px;
  margin-top: 10px;
  // margin-bottom: 30px;
}
.all-count {
  width: 100%;
  line-height: 46px;
  text-align: center;
  background: url(~assets/images/sygs-count-bg.png) no-repeat;
  background-size: 100% 100%;
  margin-bottom: 10px;
  span {
    margin: 0 10px;
    font-size: 20px;
    color: #1cfff5;
  }
}
.count4-box {
  display: flex;
  justify-content: space-between;
  text-align: center;
  font-size: 14px;
  &-item {
    border-radius: 2px;
    padding: 10px 10px 8px 10px;
    margin-right: 5px;
    &:last-child {
      margin-right: 0;
    }
    .num {
      font-size: 16px;
      margin-top: 5px;
    }
  }
  .green {
    background: rgba(25, 175, 71, 0.1);
    border: 1px solid rgba(25, 175, 71, 0.5);
    .num {
      color: #19af47;
    }
  }
  .yellow {
    background: rgba(232, 145, 0, 0.1);
    border: 1px solid rgba(232, 145, 0, 0.5);
    .num {
      color: #e89100;
    }
  }
  .orange {
    background: rgba(255, 85, 31, 0.1);
    border: 1px solid rgba(255, 85, 31, 0.5);
    .num {
      color: #ff551f;
    }
  }
  .red {
    background: rgba(232, 0, 0, 0.1);
    border: 1px solid rgba(232, 0, 0, 0.5);
    .num {
      color: #e80000;
    }
  }
}

.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-diannano {
  background: url(~assets/images/diannao.png) no-repeat;
  background-size: 100% 100%;
  display: inline-block;
  width: 20px;
  height: 18px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #26ccf2;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.icon-color-3 {
  @include chart-icon();
  background: #6267ff;
}
.icon-color-4 {
  @include chart-icon();
  background: #a000c8;
}
.icon-color-5 {
  @include chart-icon();
  background: #ff551f;
}
.icon-color-6 {
  @include chart-icon();
  background: #e89100;
}
.icon-color-7 {
  @include chart-icon();
  background: #eec80b;
}
.icon-color-8 {
  @include chart-icon();
  background: #98d89a;
}
.icon-color-9 {
  @include chart-icon();
  background: #004fe3;
}
</style>
