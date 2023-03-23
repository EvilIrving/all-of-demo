<!--
 * @Author: dyn
 * @Date: 2021-07-01 13:43:37
 * @LastEditTime: 2021-07-02 11:44:16
 * @LastEditors: Please set LastEditors
 * @Description: 数字珊溪
 * @FilePath: -
-->
<template>
  <div class="water-card">
    <panel-item title="数字珊溪" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <!-- <i
          title="数字珊溪系统"
          class="icon-diannano icon-sty"
          @click="toPath"
        /> -->
      </template>

      <div class="charts-panel">
        <!-- 总览 -->
        <sub-title-click-card title="总览" type="radio">
          <div slot="title-operate">
            <i
              title="地图落点"
              :class="[
                zlbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                'icon-sty',
              ]"
              @click="drawPointZL('zlbtn')"
            />
            <i
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('珊溪水质监测')"
            />
          </div>
          <div class="chart-panel">
            <div class="top-box">
              <div class="chart-tab">
                <span
                  :class="['chart-tab-item', overviewType == 0 ? 'active' : '']"
                  @click="overviewTypeChange(0)"
                  >珊溪水库</span
                >
                <span
                  :class="['chart-tab-item', overviewType == 1 ? 'active' : '']"
                  @click="overviewTypeChange(1)"
                  >赵山渡水库</span
                >
              </div>
              <div class="right">
                <i class="icon-shuizhi"></i>
                水质 I 类
              </div>
            </div>
            <div class="count-panel">
              <div class="count-panel-item">
                <div class="count-item">
                  <i class="icon-zl-count icon-zl-count-1"></i>
                  <div class="text">
                    <p>水位</p>
                    <p class="num">
                      {{ zlData.waterLevel || "-" }} <span>m</span>
                    </p>
                  </div>
                </div>
                <div class="count-item">
                  <i class="icon-zl-count icon-zl-count-3"></i>
                  <div class="text">
                    <p>库容</p>
                    <p class="num">
                      {{ zlData.waterLevelCapacity || "-" }} <span>亿m³</span>
                    </p>
                  </div>
                </div>
              </div>
              <div class="line"></div>
              <div class="count-panel-item">
                <div class="count-item">
                  <i class="icon-zl-count icon-zl-count-2"></i>
                  <div class="text">
                    <p>实时入库流量</p>
                    <p class="num">
                      {{ zlData.synFlow || "-" }} <span>万m³</span>
                    </p>
                  </div>
                </div>
                <div class="count-item">
                  <i class="icon-zl-count icon-zl-count-4"></i>
                  <div class="text">
                    <p>日面雨量</p>
                    <p class="num">
                      {{ zlData.surfaceWaterLevel || "-" }} <span>mm</span>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </sub-title-click-card>
        <!-- 面源污染/水土流失 -->
        <sub-title-click-card
          :title="['面源污染', '水土流失']"
          @tabChange="tabChange"
        >
          <div slot="title-operate">
            <i
              title="地图落点"
              :class="[
                mywybtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                'icon-sty',
              ]"
              @click="drawAreaMYWR('mywybtn')"
            />
          </div>
          <div>
            <div class="chart-tab" v-if="tabType == '面源污染'">
              <span
                :class="['chart-tab-item', pollutionType == 0 ? 'active' : '']"
                @click="pollutionTypeChange(0)"
                >种植总面积</span
              >
              <span
                :class="['chart-tab-item', pollutionType == 1 ? 'active' : '']"
                @click="pollutionTypeChange(1)"
                >种植施肥量</span
              >
              <span
                :class="['chart-tab-item', pollutionType == 2 ? 'active' : '']"
                @click="pollutionTypeChange(2)"
                >化肥流失量</span
              >
            </div>
            <div class="chart-panel">
              <div class="pie-box">
                <div id="pieChart" style="width: 50%; height: 100%"></div>
                <div class="legend-panel">
                  <div
                    class="legend-item"
                    v-for="(item, index) in pieChartData"
                    :key="index"
                  >
                    <div>
                      <i
                        :class="['icon-circle', 'icon-color-' + (index + 1)]"
                      />
                      <span class="text">{{ item.name }}</span>
                    </div>
                    <span class="num">{{ item.value }}</span>
                    <span v-if="tabType == '面源污染'">{{
                      pollutionType == 0 ? "k㎡" : "t"
                    }}</span>
                    <span v-if="tabType == '水土流失'">%</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </sub-title-click-card>
        <!-- 巡查 -->
        <sub-title-click-card title="巡查" type="radio">
          <div slot="title-operate">
            <i
              v-if="checkType != 0"
              title="地图落点"
              :class="[
                xcbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
                'icon-sty',
              ]"
              @click="drawPointXc('xcbtn')"
            />
            <i
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('巡查')"
            />
          </div>
          <div>
            <div class="chart-tab">
              <span
                :class="['chart-tab-item', checkType == 0 ? 'active' : '']"
                @click="checkTypeChange(0)"
                >行政区划</span
              >
              <span
                :class="['chart-tab-item', checkType == 1 ? 'active' : '']"
                @click="checkTypeChange(1)"
                >乱点分析</span
              >
              <span
                :class="['chart-tab-item', checkType == 2 ? 'active' : '']"
                @click="checkTypeChange(2)"
                >处置状态</span
              >
            </div>
            <div class="chart-panel">
              <div class="line-box" v-if="checkType == 0">
                <ve-histogram
                  ref="bar"
                  height="100%"
                  width="100%"
                  :data="barData"
                  :settings="barOption.setting"
                  :extend="barOption.extend"
                  :grid="barOption.grid"
                  :tooltip-visible="false"
                  :legend-visible="false"
                  :after-config="barConfig"
                />
              </div>
              <div class="pie-box" v-if="checkType == 1">
                <div id="pieChart2" style="width: 50%; height: 100%"></div>
                <div class="legend-panel">
                  <div
                    class="legend-item"
                    v-for="(item, index) in pieChartData2"
                    :key="index"
                  >
                    <div>
                      <i
                        :class="['icon-circle', 'icon-color-' + (index + 1)]"
                      />
                      <span class="text">{{ item.name }}</span>
                    </div>
                    <span class="num">{{ item.value }}</span>
                    <span>个</span>
                  </div>
                </div>
              </div>
              <div class="pie-box" v-if="checkType == 2">
                <div id="pieChart3" style="width: 100%; height: 100%"></div>
              </div>
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
  name: "ShanXiCard",
  // layerName: ["areaLayer", "econoOverlay"],
  // legendName: "水源供水",
  components: {
    PanelItem,
    SubTitleClickCard,
  },
  data() {
    return {
      timer: null,

      zlData: {}, // 总览数据

      overviewType: 0, // 总览 统计类型
      pollutionType: 0, // 面源污染 统计类型切换
      checkType: 0, // 巡查 统计类型切换

      tabType: "面源污染", // 面源污染 水土流失统计切换

      zlbtn: false, // 总览
      mywybtn: false, // 面源污染/水土流失
      xcbtn: false, // 巡查

      totalLength: 0,
      pieChartData: [
        {
          name: "果园",
          value: 1,
        },
        {
          name: "茶园",
          value: 1,
        },
        {
          name: "耕地",
          value: 1,
        },
      ],
      totalLength2: 0, // 巡查 乱点分析 总数
      pieChartData2: [
        {
          name: "果园",
          value: 1,
        },
        {
          name: "茶园",
          value: 1,
        },
        {
          name: "耕地",
          value: 1,
        },
      ],
      pieChartData3: [
        {
          name: "整改中",
          value: 1,
        },
        {
          name: "已完成",
          value: 1,
        },
      ],

      barData: {
        columns: ["areaName", "num"],
        rows: [{ areaName: "yongjia", num: 1 }],
      },
      barOption: {
        grid: {
          top: 32,
          bottom: "5%",
          right: "0%",
          left: "0%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["个"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#fff",
          },
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 15,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
            },
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize,
            },
            axisLine: {
              show: false,
            },
            axisLabel: {
              color: "#fff",
              fontSize: this.$commonSetting.chartFontSize,
            },
            splitLine: {
              lineStyle: {
                color: "#fff",
                opacity: 0.5,
              },
            },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
          },
        },
      },

      warningCount: 0,

      waterPointList: [
        {
          name: "测试1",
          type: "水库",
          a: "ii类",
          b: "橙色预警",
          lowLeftLong: 120.253212,
          lowLeftLat: 27.789873,
          c: 1,
          d: 5,
        },
        {
          name: "测试2",
          type: "水库",
          a: "ii类",
          b: "橙色预警",
          lowLeftLong: 120.518045,
          lowLeftLat: 27.460252,
          c: 2,
          d: 6,
        },
        {
          name: "测试3",
          type: "水库",
          a: "ii类",
          b: "橙色预警",
          lowLeftLong: 120.263212,
          lowLeftLat: 27.889873,
          c: 3,
          d: 7,
        },
        {
          name: "测试4",
          type: "水库",
          a: "ii类",
          b: "橙色预警",
          lowLeftLong: 120.259212,
          lowLeftLat: 27.799873,
          c: 4,
          d: 8,
        },
        {
          name: "测试4",
          type: "水库",
          a: "ii类",
          b: "橙色预警",
          lowLeftLong: 120.359212,
          lowLeftLat: 27.799873,
          c: 5,
          d: 8,
        },
      ],

      pieChart1: null,
      pieChart2: null,
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
      "AffairId",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  watch: {},
  mounted() {
    window.addEventListener("resize", this.handleWindowResize);
    this.digitalShanXiOverview();
    this.patrolChaosHappStaticByAreaCode();
    this.timer = setInterval(() => {
      this.digitalShanXiOverview();
      this.patrolChaosHappStaticByAreaCode();
    }, 60000);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),

    // 获取总览数据
    async digitalShanXiOverview() {
      let opt = {
        reservoirName: this.overviewType == 0 ? "珊溪水库" : "赵山渡水库",
      };
      await waterResourceApi.digitalShanXiOverview(opt).then((res) => {
        this.zlData = res.data;
      });
    },
    // 获取巡查数据 行政区划
    async patrolChaosHappStaticByAreaCode() {
      await waterResourceApi.patrolChaosHappStaticByAreaCode().then((res) => {
        this.barData.rows = res.data;
      });
    },
    // 获取巡查数据 乱点分析
    async patrolChaosHappStaticByChaosType() {
      this.pieChartData2 = [];
      this.totalLength2 = 0;
      await waterResourceApi.patrolChaosHappStaticByChaosType().then((res) => {
        res.data.forEach((item) => {
          let obj = {
            name: item.chaosType,
            value: item.num,
          };
          this.pieChartData2.push(obj);
          this.totalLength2 = this.totalLength2 + item.num;
        });
      });
      this.$nextTick(() => {
        this.pieChartOpt2();
      });
    },
    // 获取巡查数据 处置状态
    async patrolChaosHappStaticByChaosStatus() {
      this.pieChartData2 = [];
      this.totalLength2 = 0;
      await waterResourceApi
        .patrolChaosHappStaticByChaosStatus()
        .then((res) => {
          res.data.forEach((item) => {
            let obj = {
              name: item.chaosStatus,
              value: item.num,
            };
            this.pieChartData2.push(obj);
            this.totalLength2 = this.totalLength2 + item.num;
          });
        });
      this.$nextTick(() => {
        this.pieChartOpt3();
      });
    },

    // 饼图 面源污染
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      var myChart = echarts.init(chartDom);
      var option;
      let colorList = [
        "#19af47",
        "#008fe8",
        "#e89100",
        "#6267ff",
        "#A000C8",
        "#ff551f",
        "#eec80b",
        "#98d89a",
        "#004fe3",
      ];
      let color = [];
      colorList.forEach((item) => {
        let obj = {
          type: "linear",
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item, // 0% 处的颜色
            },
            {
              offset: 1,
              color: "rgba(0,0,0)", // 100% 处的颜色
            },
          ],
          global: false, // 缺省为 false
        };
        color.push(obj);
      });
      let unit = this.pollutionType == 0 ? "k㎡" : "t";
      let total = this.pollutionType == 0 ? "总面积" : "总量";
      option = {
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: this.tabType == "面源污染",
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: this.tabType == "面源污染",
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.totalLength + unit + "\n" + total,
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieChartData,
            color: color,
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 饼图 巡查-乱点分析
    pieChartOpt2() {
      const _this = this;
      var chartDom = document.getElementById("pieChart2");
      if (this.pieChart1) echarts.dispose(this.pieChart1);
      if (this.pieChart2) echarts.dispose(this.pieChart2);
      this.pieChart1 = echarts.init(chartDom);
      var option;
      let colorList = [
        "#19af47",
        "#008fe8",
        "#e89100",
        "#6267ff",
        "#A000C8",
        "#ff551f",
        "#eec80b",
        "#98d89a",
        "#004fe3",
      ];
      let color = [];
      colorList.forEach((item) => {
        let obj = {
          type: "linear",
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item, // 0% 处的颜色
            },
            {
              offset: 1,
              color: "rgba(0,0,0)", // 100% 处的颜色
            },
          ],
          global: false, // 缺省为 false
        };
        color.push(obj);
      });
      option = {
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 14,
                formatter: this.totalLength2 + "个\n总数",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieChartData2,
            color: color,
          },
        ],
      };
      option && this.pieChart1.setOption(option);
    },
    // 饼图 巡查-处置状态
    pieChartOpt3() {
      const _this = this;
      var chartDom = document.getElementById("pieChart3");
      if (this.pieChart1) echarts.dispose(this.pieChart1);
      if (this.pieChart2) echarts.dispose(this.pieChart2);
      this.pieChart2 = echarts.init(chartDom);
      var myChart = echarts.init(chartDom);
      var option;
      let colorList = ["#19AF47", "#E89100"];
      let color = [];
      colorList.forEach((item) => {
        let obj = {
          type: "linear",
          x: 0,
          y: 0,
          x2: 1,
          y2: 1,
          colorStops: [
            {
              offset: 0,
              color: item, // 0% 处的颜色
            },
            {
              offset: 1,
              color: "rgba(0,0,0)", // 100% 处的颜色
            },
          ],
          global: false, // 缺省为 false
        };
        color.push(obj);
      });

      option = {
        tooltip: {
          show: false,
          trigger: "item",
          formatter: function (data) {
            return (
              "<span style='font-size: 14px;'>" +
              data.name +
              "</span><br/><span style='font-size: 14px;color: #1CFFF5;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'>个</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["35%", "60%"],
            centen: [50, 50],
            avoidLabelOverlap: false,
            label: {
              show: true,
              lineHeight: 25,
              color: "#fff",
              position: "outside",
              alignTo: "edge",
              // edgeDistance: 90,
              formatter: (data) => {
                if (data.name == "已完成") {
                  return (
                    "{name|" +
                    data.name +
                    "}" +
                    "\n" +
                    "{num2|" +
                    data.value +
                    "} 个"
                  );
                } else {
                  return (
                    "{name|" +
                    data.name +
                    "}" +
                    "\n" +
                    "{num1|" +
                    data.value +
                    "} 个"
                  );
                }
              },
              rich: {
                name: {
                  color: "#fff",
                  fontSize: 16,
                },
                num1: {
                  color: "#E89100",
                  fontSize: 18,
                  fontWeight: "bold",
                },
                num2: {
                  color: "#19AF47",
                  fontSize: 18,
                  fontWeight: "bold",
                },
              },
            },
            labelLine: {
              show: true,
              length: 20,
              length2: 65,
              maxSurfaceAngle: 0,
              lineStyle: {
                color: "#fff",
              },
            },
            data: this.pieChartData2,
            color: color,
          },
        ],
      };
      option && this.pieChart2.setOption(option);
    },
    // 柱状图
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
      let color = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[0],
          },
          {
            offset: 0.5,
            color: colorArr[1],
          },
          {
            offset: 1,
            color: colorArr[1],
          },
        ],
      };
      let barWidth = 15;
      let constData = [];
      let showData = [];
      data.filter(function (item) {
        if (item) {
          constData.push(1);
          showData.push(item);
        } else {
          constData.push(0);
          showData.push({
            value: 1,
            itemStyle: {
              normal: {
                borderColor: "rgba(0,0,0,0)",
                borderWidth: 2,
                color: "rgba(0,0,0,0)",
              },
            },
          });
        }
      });

      options.series = [
        {
          z: 1,
          name: "数据",
          type: "bar",
          barWidth: barWidth,
          barGap: "0%",
          data: data,
          itemStyle: {
            normal: {
              color: color,
            },
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#15E2DB",
          },
        },
        {
          z: 2,
          name: "数据",
          type: "pictorialBar",
          data: constData,
          symbol: "diamond",
          symbolOffset: ["0%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "数据",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData,
          symbol: "diamond",
          symbolOffset: ["0%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr[2],
              borderWidth: 2,
              color: colorArr[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      options.dataZoom = generateDataZoom(5, this.barData.rows.length);
      return options;
    },
    // tab切换  水面污染/水土流失
    tabChange(val) {
      this.tabType = val;
      (this.pieChartData = [
        {
          name: "果园1",
          value: 11,
        },
        {
          name: "茶园1",
          value: 13,
        },
        {
          name: "耕地1",
          value: 14,
        },
      ]),
        this.pieChartOpt();
      this.mywybtn = false;
    },
    // 总览 统计类型切换
    overviewTypeChange(val) {
      this.overviewType = val;
      this.digitalShanXiOverview();
    },
    // 面源污染 统计类型切换
    pollutionTypeChange(val) {
      this.pollutionType = val;
    },
    // 巡查 统计类型切换
    checkTypeChange(val) {
      this.checkType = val;
      if (val == 0) {
        this.$nextTick(() => {
          this.$refs.bar.echarts.resize();
        });
      } else if (val == 1) {
        this.$nextTick(() => {
          this.patrolChaosHappStaticByChaosType();
        });
      } else if (val == 2) {
        this.$nextTick(() => {
          this.patrolChaosHappStaticByChaosStatus();
        });
      }
      this.xcbtn = false;
      this.map.drawClusterPoint([], {}, "ldPointLayer1");
      this.map.drawClusterPoint([], {}, "ldPointLayer2");
      this.map.drawClusterPoint([], {}, "ldPointLayer3");
      this.map.drawClusterPoint([], {}, "ldPointLayer4");
      this.map.drawClusterPoint([], {}, "ldPointLayer5");
      this.map.drawClusterPoint([], {}, "ldPointLayer6");
      this.map.drawClusterPoint([], {}, "ldPointLayer7");
      this.$emit("legend-change", "乱点分析", {});
      this.map.drawClusterPoint([], {}, "CzPointLayer1");
      this.map.drawClusterPoint([], {}, "CzPointLayer2");
      this.$emit("legend-change", "处置状态", {});
    },
    // 总览 落点
    drawPointZL(btn) {
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-jh-zl-1.png"),
        src: require("@/assets/images/sx-ld-zl-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-jh-zl-2.png"),
        src: require("@/assets/images/sx-ld-zl-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-jh-zl-3.png"),
        src: require("@/assets/images/sx-ld-zl-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-jh-zl-4.png"),
        src: require("@/assets/images/sx-ld-zl-4.png"),
      };
      let style5 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-jh-zl-5.png"),
        src: require("@/assets/images/sx-ld-zl-5.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      let pointList5 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.name,
          type: "珊溪总览",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          props: {
            水质类别: `${point.tdz || "-"}m`,
            COD: `${point.tdz || "-"}m`,
            氨氮: `${point.tdz || "-"}m`,
            总磷: `${point.tdz || "-"}m`,
          },
        };
        if (point.c == 1) {
          pointList1.push(obj);
        } else if (point.c == 2) {
          pointList2.push(obj);
        } else if (point.c == 3) {
          pointList3.push(obj);
        } else if (point.c == 4) {
          pointList4.push(obj);
        } else if (point.c == 5) {
          pointList5.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "areaPointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "areaPointLayer2");
        this.map.drawClusterPoint(pointList3, style3, "areaPointLayer3");
        this.map.drawClusterPoint(pointList4, style4, "areaPointLayer4");
        this.map.drawClusterPoint(pointList5, style5, "areaPointLayer5");
        this.$emit("legend-change", "总览", {
          label: "总览",
          children: [
            {
              label: "取水口",
              icon: "legend-sx-zl-1",
            },
            {
              label: "珊溪水库大坝",
              icon: "legend-sx-zl-2",
            },
            {
              label: "赵山渡水库大坝",
              icon: "legend-sx-zl-3",
            },
            {
              label: "人工监测断面",
              icon: "legend-sx-zl-4",
            },
            {
              label: "水质自动监测站",
              icon: "legend-sx-zl-4",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "areaPointLayer1");
        this.map.drawClusterPoint([], {}, "areaPointLayer2");
        this.map.drawClusterPoint([], {}, "areaPointLayer3");
        this.map.drawClusterPoint([], {}, "areaPointLayer4");
        this.map.drawClusterPoint([], {}, "areaPointLayer5");
        this.$emit("legend-change", "总览", {});
      }
    },
    // 面源污染 水土流失
    drawAreaMYWR(btn) {
      this[btn] = !this[btn];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.name,
          type: "水源供水",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          props: {},
          scale: "千吨万人",
          _type: "水库",
          area: "台州市",
          isP: "是",
          target: "II类",
          warn: "暂无风险",
        };
      }
      if (this[btn]) {
        if (this.tabType == "面源污染") {
          this.$emit("legend-change", "面源污染", {
            label: "面源污染",
            children: [
              {
                label: "果园",
                icon: "legend-sx-ld-mywr-1",
              },
              {
                label: "茶园",
                icon: "legend-sx-ld-mywr-2",
              },
              {
                label: "耕地",
                icon: "legend-sx-ld-mywr-3",
              },
            ],
          });
        } else {
          this.$emit("legend-change", "水土流失", {
            label: "水土流失",
            children: [
              {
                label: "轻度",
                icon: "legend-sx-ld-stls-1",
              },
              {
                label: "中度",
                icon: "legend-sx-ld-stls-2",
              },
              {
                label: "强烈",
                icon: "legend-sx-ld-stls-3",
              },
              {
                label: "极强烈",
                icon: "legend-sx-ld-stls-4",
              },
              {
                label: "剧烈",
                icon: "legend-sx-ld-stls-5",
              },
            ],
          });
        }
      } else {
        // this.map.drawClusterPoint([], {}, "warnPointList1");
        // this.map.drawClusterPoint([], {}, "warnPointList2");
        // this.map.drawClusterPoint([], {}, "warnPointList3");
        // this.map.drawClusterPoint([], {}, "warnPointList4");
        if (this.tabType == "面源污染") {
          this.$emit("legend-change", "面源污染", {});
        } else {
          this.$emit("legend-change", "水土流失", {});
        }
      }
    },
    // 巡查落点
    drawPointXc(btn) {
      if (this.checkType == 1) {
        this.drawPointLd(btn);
      } else if (this.checkType == 2) {
        this.drawPointCz(btn);
      }
    },
    // 乱点分析落点
    drawPointLd(btn) {
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-1.png"),
        src: require("@/assets/images/sx-ld-ldfx-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-2.png"),
        src: require("@/assets/images/sx-ld-ldfx-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-3.png"),
        src: require("@/assets/images/sx-ld-ldfx-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-4.png"),
        src: require("@/assets/images/sx-ld-ldfx-4.png"),
      };
      let style5 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-5.png"),
        src: require("@/assets/images/sx-ld-ldfx-5.png"),
      };
      let style6 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-6.png"),
        src: require("@/assets/images/sx-ld-ldfx-6.png"),
      };
      let style7 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-ldfx-ju-7.png"),
        src: require("@/assets/images/sx-ld-ldfx-7.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      let pointList5 = [];
      let pointList6 = [];
      let pointList7 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.name,
          type: "珊溪巡查",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          props: {
            乱点类型: `${point.tdz || "-"}m`,
            乱点状态: `${point.tdz || "-"}m`,
          },
        };
        if (point.c == 1) {
          pointList1.push(obj);
        } else if (point.c == 2) {
          pointList2.push(obj);
        } else if (point.c == 3) {
          pointList3.push(obj);
        } else if (point.c == 4) {
          pointList4.push(obj);
        } else if (point.c == 5) {
          pointList5.push(obj);
        } else if (point.c == 6) {
          pointList6.push(obj);
        } else if (point.c == 7) {
          pointList7.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "ldPointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "ldPointLayer2");
        this.map.drawClusterPoint(pointList3, style3, "ldPointLayer3");
        this.map.drawClusterPoint(pointList4, style4, "ldPointLayer4");
        this.map.drawClusterPoint(pointList5, style5, "ldPointLayer5");
        this.map.drawClusterPoint(pointList5, style6, "ldPointLayer6");
        this.map.drawClusterPoint(pointList5, style7, "ldPointLayer7");
        this.$emit("legend-change", "乱点分析", {
          label: "乱点分析",
          children: [
            {
              label: "水土流失",
              icon: "legend-sx-ld-1",
            },
            {
              label: "非法采砂",
              icon: "legend-sx-ld-2",
            },
            {
              label: "倾倒垃圾",
              icon: "legend-sx-ld-3",
            },
            {
              label: "违章建筑",
              icon: "legend-sx-ld-4",
            },
            {
              label: "占用水域",
              icon: "legend-sx-ld-5",
            },
            {
              label: "非法排污",
              icon: "legend-sx-ld-6",
            },
            {
              label: "畜禽养殖",
              icon: "legend-sx-ld-7",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "ldPointLayer1");
        this.map.drawClusterPoint([], {}, "ldPointLayer2");
        this.map.drawClusterPoint([], {}, "ldPointLayer3");
        this.map.drawClusterPoint([], {}, "ldPointLayer4");
        this.map.drawClusterPoint([], {}, "ldPointLayer5");
        this.map.drawClusterPoint([], {}, "ldPointLayer6");
        this.map.drawClusterPoint([], {}, "ldPointLayer7");
        this.$emit("legend-change", "乱点分析", {});
      }
    },
    // 处置状态落点
    drawPointCz(btn) {
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-czzt-ju-1.png"),
        src: require("@/assets/images/sx-ld-czzt-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sx-ld-czzt-ju-2.png"),
        src: require("@/assets/images/sx-ld-czzt-2.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.name,
          type: "珊溪巡查",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          props: {
            乱点类型: `${point.tdz || "-"}m`,
            乱点状态: `${point.tdz || "-"}m`,
          },
        };
        if (point.c == 1) {
          pointList1.push(obj);
        } else if (point.c == 2) {
          pointList2.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "CzPointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "CzPointLayer2");
        this.$emit("legend-change", "处置状态", {
          label: "处置状态",
          children: [
            {
              label: "已完成",
              icon: "legend-sx-cz-1",
            },
            {
              label: "整改中",
              icon: "legend-sx-cz-2",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "CzPointLayer1");
        this.map.drawClusterPoint([], {}, "CzPointLayer2");
        this.$emit("legend-change", "处置状态", {});
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
        analyseDialogShow: true,
      });
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      this.$refs.bar.echarts.resize();
      this.pieChartOpt();
    },
  },
};
</script>

<style lang="scss" scoped>
.top-box {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  .right {
    color: #00b334;
    // font-size: 14px;
  }
}
.chart-tab {
  margin-bottom: 10px;
  &-item {
    margin-right: 10px;
    font-size: 14px;
    padding: 4px 10px;
    cursor: pointer;
    &.active {
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
      border-radius: 15px;
    }
  }
}

.count-panel {
  display: flex;
  justify-content: space-between;
  &-item {
    .count-item {
      display: flex;
      align-items: center;
      margin: 10px 0;
      .text {
        font-size: 14px;
        margin-left: 10px;
        line-height: 25px;
        .num {
          font-size: 20px;
          color: #56fefe;
        }
        span {
          color: #fff;
          font-size: 12px;
        }
      }
    }
  }
  .line {
    border: 1px solid rgba(255, 255, 255, 0.4);
  }
}
.pie-box {
  height: 200px;
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
      .text {
        display: inline-block;
        width: 80px;
        font-size: 14px;
        margin-right: 10px;
      }
      .num {
        color: #1cfff5;
        font-size: 1rem;
        margin-right: 5px;
      }
    }
  }
}
.line-box {
  width: 100%;
  height: 200px;
  margin-top: 10px;
  margin-bottom: 30px;
}
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-diannano {
  background-image: url(~assets/images/diannao.png);
  background-size: 100% 100%;
  display: inline-block;
  width: 20px;
  height: 18px;
}
.icon-shuizhi {
  display: inline-block;
  width: 16px;
  height: 16px;
  background-image: url(~assets/images/shuizhi.png);
  background-size: 100% 100%;
  vertical-align: middle;
}
.icon-zl-count {
  display: inline-block;
  width: 48px;
  height: 48px;
  background-size: 100% 100%;
}
.icon-zl-count-1 {
  background-image: url(~assets/images/sx-zl-count-1.png);
}
.icon-zl-count-2 {
  background-image: url(~assets/images/sx-zl-count-2.png);
}
.icon-zl-count-3 {
  background-image: url(~assets/images/sx-zl-count-3.png);
}
.icon-zl-count-4 {
  background-image: url(~assets/images/sx-zl-count-4.png);
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #19af47;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.icon-color-3 {
  @include chart-icon();
  background: #e89100;
}
.icon-color-4 {
  @include chart-icon();
  background: #6267ff;
}
.icon-color-5 {
  @include chart-icon();
  background: #a000c8;
}
.icon-color-6 {
  @include chart-icon();
  background: #ff551f;
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

