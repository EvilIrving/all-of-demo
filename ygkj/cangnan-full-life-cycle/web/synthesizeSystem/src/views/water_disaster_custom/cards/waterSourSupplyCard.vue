<!--
 * @Author: dyn
 * @Date: 2021-07-01 13:43:37
 * @LastEditTime: 2021-07-02 11:44:16
 * @LastEditors: Please set LastEditors
 * @Description: 水源供水
 * @FilePath: -
-->
<template>
  <div class="water-card">
    <panel-item title="水源供水" tc @tclick="tclick">
      <div class="charts-panel">
        <!-- 水源地 -->
        <sub-title-click-card
          title="水源地"
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
              @click.stop="openDetailsDialog('水源地')"
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
                      >{{ item.name }}</span
                    >
                  </div>
                  <div v-if="areaType == 0">
                    <span class="num">{{ item.value }}</span
                    >{{ areaTabType == "数量" ? "个" : "万人" }}
                  </div>
                </div>
              </div>
            </div>
            <div class="line-box">
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
          </div>
        </sub-title-click-card>
        <!-- 水量预警 -->
        <sub-title-click-card
          title="水量预警"
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
              @click.stop="openDetailsDialog('水量预警')"
            />
          </div>
          <div class="chart-panel">
            <div class="count-box">
              <div class="all-count">
                <!-- 总数 <span>{{ statisticsData[4].num == null ? '-' : statisticsData[4].num }}</span> -->
                {{ warnTabType == "数量" ? "个" : "万人" }}
              </div>
              <div class="count4-box">
                <div class="count4-box-item green">
                  <p>{{statisticsData[0].warnType}}</p>
                  <p class="num">{{statisticsData[0].num}}</p>
                </div>
                <div class="count4-box-item yellow">
                  <p>{{statisticsData[1].warnType}}</p>
                  <p class="num">{{statisticsData[1].num}}</p>
                </div>
                <div class="count4-box-item orange">
                  <p>{{statisticsData[2].warnType}}</p>
                  <p class="num">{{statisticsData[2].num}}</p>
                </div>
                <div class="count4-box-item red">
                  <p>{{statisticsData[3].warnType}}</p>
                  <p class="num">{{statisticsData[3].num}}</p>
                </div>
              </div>
            </div>
            <div class="line-box">
              <ve-histogram
                ref="bar2"
                height="100%"
                width="100%"
                :data="barData2"
                :settings="barOption.setting"
                :extend="barOption.extend"
                :grid="barOption.grid"
                :tooltip-visible="false"
                :legend-visible="false"
                :after-config="barConfig"
              />
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
  name: "waterSourSupplyCard",
  layerName: ["areaLayer", "econoOverlay"],
  legendName: "水源供水",
  components: {
    PanelItem,
    SubTitleClickCard,
  },
  data() {
    return {
      timer: null,
      statisticsData: [],
      areaTabType: "数量", // 数量 人口 水源地
      warnTabType: "数量", // 数量 人口 水量预警
      areaType: 0, // 按规模 按类型
      sydbtn: false,
      sldbtn: false,
      totalLength: 4,
      pieChartData: [],

      barData: {
        columns: ["adnm", "num"],
        rows: [],
      },
      barData2: {
        columns: ["adnm", "num"],
        rows: [],
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
          yAxisName: ["条"],
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

      waterPointList: [],

      pieChart: null,
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
    this.statisSwhsNumByScale();
    this.statisSwhsNumByArea();
    this.warnChange('数量');
    this.AttSwhsBaseLoadPage();
    this.timer = setInterval(() => {}, 60000);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),

    // 水源地统计 按规模
    async statisSwhsNumByScale() {
      let opt = {
        type: this.areaTabType == "数量" ? "1" : "2",
      };
      this.pieChartData = [];
      await waterResourceApi.statisSwhsNumByScale(opt).then((res) => {
        res.data.forEach((item) => {
          let obj = {};
          switch (item.supplyScale) {
            case "1":
              obj = {
                name: "县级以上",
                value: item.num,
              };
              break;
            case "2":
              obj = {
                name: "千吨万人",
                value: item.num,
              };
              break;
            case "3":
              obj = {
                name: "200吨-1000吨",
                value: item.num,
              };
              break;
            case "4":
              obj = {
                name: "200吨以下",
                value: item.num,
              };
              break;
          }
          this.pieChartData.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpt();
        });
      });
    },
    // 水源地统计 按类型
    async statisSwhsNumByType() {
      let opt = {
        type: this.areaTabType == "数量" ? "1" : "2",
      };
      this.pieChartData = [];
      await waterResourceApi.statisSwhsNumByType(opt).then((res) => {
        res.data.forEach((item) => {
          let obj = {};
          switch (item.wainWasoType) {
            case "0":
              obj = {
                name: "水库",
                value: item.num,
              };
              break;
            case "1":
              obj = {
                name: "山塘",
                value: item.num,
              };
              break;
            case "2":
              obj = {
                name: "河流湖泊",
                value: item.num,
              };
              break;
            case "3":
              obj = {
                name: "溪沟堰坝",
                value: item.num,
              };
              break;
            case "4":
              obj = {
                name: "蓄水池",
                value: item.num,
              };
              break;
            case "5":
              obj = {
                name: "地下水",
                value: item.num,
              };
              break;
            case "6":
              obj = {
                name: "海水淡化",
                value: item.num,
              };
              break;
            case "7":
              obj = {
                name: "山泉水",
                value: item.num,
              };
              break;
            case "8":
              obj = {
                name: "其他",
                value: item.num,
              };
              break;
          }
          this.pieChartData.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpt();
        });
      });
    },

    // 水源地 行政区划
    async statisSwhsNumByArea() {
      let opt = {
        type: this.areaTabType == "数量" ? "1" : "2",
      };
      await waterResourceApi.statisSwhsNumByArea(opt).then((res) => {
        this.barData.rows = res.data;
      });
    },

    // 水源地 饼图
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      if (this.pieChart) echarts.dispose(this.pieChart);
      this.pieChart = echarts.init(chartDom);
      var option;
      let colorList = [
        "#26ccf2",
        "#008fe8",
        "#6267ff",
        "#a000c8",
        "#ff551f",
        "#e89100",
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
              "</span><br/><span style='font-size: 14px;color: #1CFFF5;'>" +
              (data.value / _this.totalLength) * 100 +
              "</span> <span style='font-size: 14px;'> %</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;font-size:18px;",
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
                formatter: this.totalLength + unit + "\n总数",
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
      option && this.pieChart.setOption(option);
    },
    // 水源地 柱状图
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
      options.yAxis[0].name = this.areaTabType == "数量" ? "个" : "万人";
      return options;
    },
    // 数量 人口 tab切换  水源地
    areaChange(val) {
      this.areaTabType = val;
      this.areaType == 0
        ? this.statisSwhsNumByScale()
        : this.statisSwhsNumByType();
      this.statisSwhsNumByArea();
    },
    // 数量 人口 tab切换  水量预警
    async warnChange(val) {
      this.warnTabType = val;
      let opt = val == '数量' ? '1' : '2';
      this.statisSwarnGradeByAdcd(opt);
      let res =  await waterResourceApi.statisSwarnGradeNum({type: opt});
      if(res.code == 0) {
        this.statisticsData = res.data;
      } 
    },
    // 点位信息查询
    async AttSwhsBaseLoadPage() {
      this.waterPointList = [];
      let res =  await waterResourceApi.AttSwhsBaseLoadPage();
      if(res.code == 0) {
        this.waterPointList = res.data.list;
      } 
    },
    // 水量预警按行政区划统计
    async statisSwarnGradeByAdcd(opt) {
      let res =  await waterResourceApi.statisSwarnGradeByAdcd({type: opt});
      if(res.code == 0) {
        this.barData2.rows = res.data;
        this.$nextTick(() => {
          this.$refs["bar2"].echarts.resize();
        })
      } 
    },
    // 水源地 按类型 按规模 切换
    areaTypeChange(val) {
      this.areaType = val;
      val == 0 ? this.statisSwhsNumByScale() : this.statisSwhsNumByType();
    },
    // 水源地 落点
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
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-1.png"),
        src: require("@/assets/images/shuiyuandi-ld-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-2.png"),
        src: require("@/assets/images/shuiyuandi-ld-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-3.png"),
        src: require("@/assets/images/shuiyuandi-ld-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-4.png"),
        src: require("@/assets/images/shuiyuandi-ld-4.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.adnm,
          type: "水源供水",
          lng: point.swhsLong,
          lat: point.swhsLat,
          scale: point.supplyScale == '1' ? '县级以上' : point.supplyScale == '2' ? '千吨万人' : point.supplyScale == '3' ? '200吨-1000吨' : '200吨以下' ,
          _type: point.wainWasoType == '1' ? '水库' : point.wainWasoType == '2' ? '山塘' : point.wainWasoType == '3' ? '河流湖泊' : point.wainWasoType == '4' ? '溪沟堰坝' : point.wainWasoType == '5' ? '蓄水池' : point.wainWasoType == '6' ? '地下水' : point.wainWasoType == '7' ? '海水淡化' : point.wainWasoType == '8' ? '山泉水' : '其他',
          area: point.wasuTownName,
          isP: "是",
          target: "I类",
          warn: point.warnType == '1' ? '暂无风险' : point.warnType == '2' ? '黄色预警' : point.warnType == '3' ? '橙色预警' : '红色预警',
          props: {
            scale: point.supplyScale == '1' ? '县级以上' : point.supplyScale == '2' ? '千吨万人' : point.supplyScale == '3' ? '200吨-1000吨' : '200吨以下' ,
            _type: point.wainWasoType == '1' ? '水库' : point.wainWasoType == '2' ? '山塘' : point.wainWasoType == '3' ? '河流湖泊' : point.wainWasoType == '4' ? '溪沟堰坝' : point.wainWasoType == '5' ? '蓄水池' : point.wainWasoType == '6' ? '地下水' : point.wainWasoType == '7' ? '海水淡化' : point.wainWasoType == '8' ? '山泉水' : '其他',
            area: point.wasuTownName,
            isP: "是",
            target: "I类",
            warn: point.warnType == '1' ? '暂无风险' : point.warnType == '2' ? '黄色预警' : point.warnType == '3' ? '橙色预警' : '红色预警',
          },
        };
        if (point.warnType == 1) {
          pointList1.push(obj);
        } else if (point.warnType == 2) {
          pointList2.push(obj);
        } else if (point.warnType == 3) {
          pointList3.push(obj);
        } else if (point.warnType == 4) {
          pointList4.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "areaPointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "areaPointLayer2");
        this.map.drawClusterPoint(pointList3, style3, "areaPointLayer3");
        this.map.drawClusterPoint(pointList4, style4, "areaPointLayer4");
        this.$emit("legend-change", "水源地", {
          label: "水源地",
          children: [
            {
              label: "县级以上",
              icon: "legend-sygs-1",
            },
            {
              label: "千吨万人",
              icon: "legend-sygs-2",
            },
            {
              label: "200吨~1000吨",
              icon: "legend-sygs-3",
            },
            {
              label: "200吨以下",
              icon: "legend-sygs-4",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "areaPointLayer1");
        this.map.drawClusterPoint([], {}, "areaPointLayer2");
        this.map.drawClusterPoint([], {}, "areaPointLayer3");
        this.map.drawClusterPoint([], {}, "areaPointLayer4");
        this.$emit("legend-change", "水源地", {});
      }
    },
    // 水量预警 落点
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
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-5.png"),
        src: require("@/assets/images/shuiyuandi-ld-5.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-6.png"),
        src: require("@/assets/images/shuiyuandi-ld-6.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-7.png"),
        src: require("@/assets/images/shuiyuandi-ld-7.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-8.png"),
        src: require("@/assets/images/shuiyuandi-ld-8.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.waterPointList) {
        let obj = {
          name: point.adnm,
          type: "水源供水",
          lng: point.swhsLong,
          lat: point.swhsLat,
          props: {
            scale: point.supplyScale == '1' ? '县级以上' : point.supplyScale == '2' ? '千吨万人' : point.supplyScale == '3' ? '200吨-1000吨' : '200吨以下' ,
            _type: point.wainWasoType == '1' ? '水库' : point.wainWasoType == '2' ? '山塘' : point.wainWasoType == '3' ? '河流湖泊' : point.wainWasoType == '4' ? '溪沟堰坝' : point.wainWasoType == '5' ? '蓄水池' : point.wainWasoType == '6' ? '地下水' : point.wainWasoType == '7' ? '海水淡化' : point.wainWasoType == '8' ? '山泉水' : '其他',
            area: point.wasuTownName,
            isP: "是",
            target: "I类",
            warn: point.warnType == '1' ? '暂无风险' : point.warnType == '2' ? '黄色预警' : point.warnType == '3' ? '橙色预警' : '红色预警',
          },
          scale: point.supplyScale == '1' ? '县级以上' : point.supplyScale == '2' ? '千吨万人' : point.supplyScale == '3' ? '200吨-1000吨' : '200吨以下' ,
          _type: point.wainWasoType == '1' ? '水库' : point.wainWasoType == '2' ? '山塘' : point.wainWasoType == '3' ? '河流湖泊' : point.wainWasoType == '4' ? '溪沟堰坝' : point.wainWasoType == '5' ? '蓄水池' : point.wainWasoType == '6' ? '地下水' : point.wainWasoType == '7' ? '海水淡化' : point.wainWasoType == '8' ? '山泉水' : '其他',
          area: point.wasuTownName,
          isP: "是",
          target: "II类",
          warn: point.warnType == '1' ? '暂无风险' : point.warnType == '2' ? '黄色预警' : point.warnType == '3' ? '橙色预警' : '红色预警',
        };
        if (point.warnType == 1) {
          pointList1.push(obj);
        } else if (point.warnType == 2) {
          pointList2.push(obj);
        } else if (point.warnType == 3) {
          pointList3.push(obj);
        } else if (point.warnType == 4) {
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
          children: [
            {
              label: "暂无风险",
              icon: "legend-sygs-5",
            },
            {
              label: "黄色预警",
              icon: "legend-sygs-6",
            },
            {
              label: "橙色预警",
              icon: "legend-sygs-7",
            },
            {
              label: "红色预警",
              icon: "legend-sygs-8",
            },
          ],
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
        analyseDialogShow: true,
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
    },
  },
};
</script>

<style lang="scss" scoped>
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
  height: 200px;
  margin-top: 10px;
  margin-bottom: 30px;
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

