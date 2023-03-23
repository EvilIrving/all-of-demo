<template>
  <div class="pump-Station-card" style="">
    <panel-item
      title="泵站"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['按规模统计', '按行政区划']"
      @change="handleTabChange"
    >
      <template #label-description></template>

      <template #title-switch>
        <i
          title="地图落点"
          @click="handleStagnantWater('jsbtn')"
          :class="[
            jsbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('泵站')"
          style="margin-left: 10px"
        />
        <i
          v-show="!previewModule"
          @click="goToAMap('泵站')"
          style="margin-left: 10px"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>
      <!-- 第一个tab -->
      <div class="is-water-box" v-if="activeTab == 0">
        <!-- 泵闸 -->
        <div class="rain-forecast-card" style="width: 100%; height: 15%">
          <div class="rainfall-style" style="padding: 0.2rem">
            <p>
              泵站总数 <span> {{ allpumpnum }}</span> 条
            </p>
          </div>
        </div>
        <div class="pie_box">
          <!-- <div class="pie-box">
            <EchartPie
              ref="pie"
              :seriesData="pieData"
              :extraOption="pieOption"
            ></EchartPie>
          </div> -->
          <div
            id="pumpPie"
            style="width: 50%; height: 100%"
            ref="pumpPie"
          ></div>
          <div class="pie-legend">
            <div v-for="(item, index) in pieData" :key="index">
              <template v-if="item.name">
                <span>
                  <i
                    class="legend-circle"
                    :style="{ background: item.color }"
                  ></i>
                  {{ item.name }}
                </span>
                <span>
                  <span class="legend-num">{{ item.value }}</span>
                  个
                </span>
              </template>
            </div>
          </div>
        </div>
      </div>
      <!-- 第二个tab -->
      <div class="is-water-box" v-if="activeTab == 1">
        <ve-histogram
          ref="chart"
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
        <!-- 
          :data-zoom="barOption.dataZoom" -->
      </div>

      <!-- </div> -->
    </panel-item>
  </div>
</template>
<script>
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import CardTabss from "../../WaterDisasterPreventionScreen/components/CardTabss.vue";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import { generateDataZoom } from "./utils/chart.js";

import * as echarts from "echarts"; // echart插件
import { goAMap } from "./utils/common";
export default {
  name: "pumpStationCard",
  layerName: ["pumpLayers1", "pumpLayers2", "pumpLayers3"],
  legendName: "泵站",
  components: {
    CardTabss,
    PanelItem,
    EchartPie
  },
  computed: {
    ...mapGetters(["previewModule", "SetPumpStationCount"]),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },

  data() {
    return {
      pumpStList: [],
      pointList: [], //地图落点
      jsbtn: false, // 落点
      allpumpnum: "",
      jsbtn: false, // 落点
      activeTab: 0, // 0按行政区划 1按是否有积水
      pointList: [],

      barData: {
        columns: ["name", "data"],
        rows: []
      },
      barOption: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true
        },
        setting: {
          yAxisName: ["人"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
            position: "top",
            color: "#fff"
          }
        },
        extend: {
          "textStyle.color": "#fff",
          barWidth: 10,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
              formatter: function (value) {
                var ret = "";
                var maxLength = 5; //每项显示文字个数
                var valLength = value.length; //X轴类目项的文字个数
                var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
                if (rowN > 1) {
                  for (var i = 0; i < rowN; i++) {
                    var temp = ""; //每次截取的字符串
                    var start = i * maxLength; //开始截取的位置
                    var end = start + maxLength; //结束截取的位置
                    temp = value.substring(start, end) + "\n";
                    ret += temp; //凭借最终的字符串
                  }
                  return ret;
                } else {
                  return value;
                }
              }
            }
          },
          yAxis: {
            nameTextStyle: {
              color: "#ffffff99",
              fontSize: this.$commonSetting.chartFontSize
            },
            axisLine: {
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
            },
            max: value => {
              return value.max > 1 ? null : 1;
            }
          }
        }
      },
      pieData: [],
      pieLegend: [
        {
          label: "无积水",
          num: 96
        },
        {
          label: "积水",
          num: 3
        }
      ],
      //饼图
      pieOption: {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return data.data.name + ":" + "  " + data.data.value + "个";
          },
          confine: true,
          extraCssText: "font-size:18px;"
        },
        series: [
          {
            type: "pie",
            radius: ["60%", "90%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 18
              }
            },
            labelLine: {
              show: false
            }
          }
        ],
        color: [
          {
            type: "linear",
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: "#1CFFF5" // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.7)" // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          },
          {
            type: "linear",
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: "#FF7144" // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.7)" // 100% 处的颜色
              }
            ],
            global: false // 缺省为 false
          }
        ]
      },
      timer: null
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.getDikeLoggingList(); //地图点位获取
      this.getstatisticalProjectNum(); //tab1泵站数量统计
      // this.getWaterLoggingList();
      this.pustProject(); //tab1饼图统计
      this.getstatisticalDiskNumByArea(); //tab2泵站行政区划数量统计
      this.timer = setInterval(() => {
        this.getDikeLoggingList(); //地图点位获取
        // console.log("走通了1");
        this.pustProject(); //tab1饼图统计
        this.getstatisticalProjectNum(); //tab1泵站数量统计
        // this.getWaterLoggingList();
        this.getstatisticalDiskNumByArea(); //tab2行政泵站区划数量统计
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.$emit("openDialog");
    },
    goToAMap(type) {
      goAMap(type);
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.$nextTick(() => {
        val == 0 ? this.pumpPie() : "";
      });
    },
    // 获取点位
    // async getWaterLoggingList() {
    //   let res = await disasterPreventionApi.waterLoggingList();
    //   this.pointList = res.data.list;
    // },
    pumpPie() {
      var chartDom = document.getElementById("pumpPie");
      var myChart = echarts.init(chartDom);
      var option;
      const color0 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#1CFFF5" // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)" // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      const color1 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#FF7144" // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)" // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      const color2 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#eec80b" // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)" // 100% 处的颜色
          }
        ],
        global: false // 缺省为 false
      };
      option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            if (data.name) {
              return (
                data.name +
                "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
                data.value +
                "</span> <span  style='font-size: 14px;'> 个</span>"
              );
            }
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;"
        },
        series: [
          {
            type: "pie",
            radius: ["45%", "80%"],
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
                formatter: `{b|${this.allpumpnum}}` + "个" + "\n" + "总数",
                rich: {
                  b: {
                    fontSize: 20,
                    color: "#fff"
                  }
                }
              }
            },
            labelLine: {
              show: false
            },
            data: this.pieData,
            color: [color0, color1, color2]
          }
        ]
      };
      option && myChart.setOption(option);
    },
    // 饼图统计数据
    async pustProject() {
      let info1 = "";
      let info2 = "";
      let info3 = "";
      await disasterPreventionApi.pustProject({ engScal: "3" }).then(res => {
        if (res.code == 0) {
          info1 = res.data.list.length;
          //  console.log(info1,11111)
        }
      });
      await disasterPreventionApi.pustProject({ engScal: "4" }).then(res => {
        if (res.code == 0) {
          info2 = res.data.list.length;
          //  console.log(info2,22222)
        }
      });
      await disasterPreventionApi.pustProject({ engScal: "5" }).then(res => {
        if (res.code == 0) {
          info3 = res.data.list.length;
          //  console.log(info3,33333)
        }
      });

      this.pieData = [
        {
          value: info1 | 0,
          name: "中型",
          total: info1 + info2 + info3 + "\n" + "个总数",
          color: "#1CFFF5"
        },
        {
          name: "小（1）型",
          value: info2 | 0,
          total: info1 + info2 + info3 + "\n" + "个总数",
          color: "#FF7144"
        },
        {
          name: "小（2）型",
          value: info3 | 0,
          total: info1 + info2 + info3 + "\n" + "个总数",
          color: "#eec80b"
        },
        {
          value: 0,
          name: ""
        }
      ];
      this.pumpPie();
    },
    // 获取点位
    async getDikeLoggingList() {
      let res = await disasterPreventionApi.pustProject();
      this.pumpStList = res.data.list;
      // console.log(this.pumpStList,987987)
    },
    // 地图点位
    handleStagnantWater(btnType) {
      // this.dikeType
      this[btnType] = !this[btnType];
      // let style = {
      //   scale: 0.7,
      //   anchor: [24, 16],
      //   anchorXUnits: "pixels",
      //   src: require("@/assets/images/pump1big.png"),
      //   ssrc: require("@/assets/images/shuiku-linshi.png"),
      //   // srcs:require("@/assets/images/MapPoint/sluice.png")
      // };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require("@/assets/images/pump1big.png"),
        srcs: require("@/assets/images/pump1big-juhe.png")
        // srcs:require("@/assets/images/MapPoint/sluice.png")
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require("@/assets/images/pump2big.png"),
        srcs: require("@/assets/images/pump2big-juhe.png")
        // srcs:require("@/assets/images/MapPoint/sluice.png")
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require("@/assets/images/pump3big.png"),
        srcs: require("@/assets/images/pump3big-juhe.png")
        // srcs:require("@/assets/images/MapPoint/sluice.png")
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];

      // let pointList = [];
      for (let point of this.pumpStList) {
        //   let imgSrc = require("@/assets/images/pump1big.png");
        //   if (point.engScal == "4") {
        //     imgSrc = require("@/assets/images/pump2big.png");
        //   }
        //   if (point.engScal == "5") {
        //     imgSrc = require("@/assets/images/pump3big.png");
        //   }

        let obj = {
          lng: point.pustLong,
          lat: point.pustLat,
          type: "泵站",
          src: style1.src,
          opt: style1.srcs,
          code: point.pustCode,
          name: point.pustName,
          stationCode: point.stCode || "",
          props: {
            行政区划: `${point.countryAdnm || "-"}`,
            "装机流量(m³/s)": `${point.insFlow || "-"}`,
            "装机功率(kW)": `${point.insPow || "-"}`
          },
          obj: point
        };
        if (point.engScal == "3") {
          pointList1.push(obj);
        } else if (point.engScal == "4") {
          pointList2.push(obj);
        } else if (point.engScal == "5") {
          pointList3.push(obj);
        }
      }

      if (this[btnType]) {
        console.log(pointList1, 212)
        // this.map.drawPoint(pointList, style, "digReservoirLayers");
        this.map.drawClusterPoint(pointList1, style1, "pumpLayers1");
        this.map.drawClusterPoint(pointList2, style2, "pumpLayers2");
        this.map.drawClusterPoint(pointList3, style3, "pumpLayers3");
        this.$emit("legend-change", "泵站", {
          label: "泵站",
          clusterLayer: ['pumpLayers1', 'pumpLayers2', 'pumpLayers3'],
          children: [
            {
              label: "中型",
              icon: "icon-pump1small",
            },
            {
              label: "小（1）型",
              icon: "icon-pump2small",
            },
            {
              label: "小（2）型",
              icon: "icon-pump3small",
            },
          ],
        });
      } else {
        // this.map.drawPoint([], {}, "digReservoirLayers");
        this.map.drawClusterPoint([], {}, "pumpLayers1");
        this.map.drawClusterPoint([], {}, "pumpLayers2");
        this.map.drawClusterPoint([], {}, "pumpLayers3");
        this.$emit("legend-change", "泵站", {});
      }
    },
    // 获取泵站tab2数据

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
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[0]
          },
          {
            offset: 0.5,
            color: colorArr[1]
          },
          {
            offset: 1,
            color: colorArr[1]
          }
        ]
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
                color: "rgba(0,0,0,0)"
              }
            }
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
              color: color
            }
          },
          label: {
            show: true,
            fontSize: 14,
            position: "top",
            color: "#1EF8F2"
          }
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
              color: color
            }
          },
          tooltip: {
            show: false
          }
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
              color: colorArr[2]
            }
          },
          tooltip: {
            show: false
          }
        }
      ];
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      return options;
    },
    //工程总数量统计
    async getstatisticalProjectNum() {
      await disasterPreventionApi.getstatisticalProjectNum({ type: "泵站" }).then(res => {
        if (res.code == 0) {
          this.allpumpnum = res.data;
        }
      });
    },
    //泵站规模数量统计// 按行政区划统计
    async getstatisticalDiskNumByArea() {
      await disasterPreventionApi.getstatisticalDiskNumByArea({ type: "泵站" }).then(res => {
        if (res.code == 0) {
          let rows = [];
          for (let item of res.data) {
            rows.push({ name: item.adnm, data: item.num });
          }
          // console.log(rows,"走通了！！!!！");
          this.barData.rows = rows;
          this.activeTab == 1 ? this.$refs["chart"].echarts.resize() : "";
          // console.log(this.barData.rows, "通了吗？？？");
        }
        // else {
        //   console.log("走不通！！！");
        // }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
// ::v-deep
.pump-Station-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    display: flex;
    height: 100%;
    flex-direction: column;
  }
  .is-water-box {
    margin-top: 6px;
  }
  .pie_box {
    // margin-top: 20px;
    width: 100%;
    height: calc(100% - 25px);
    display: flex;
    align-items: center;
  }
  .pie-box {
    width: 50%;
    height: 100%;
  }
  .pie-legend {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 50%;
    height: 50%;
    font-size: 16px;
    padding-left: 16px;
    // padding-top: -80px;
    > div {
      display: flex;
      justify-content: space-between;
      line-height: 30px;

      &:last-child .legend-circle {
        background: #ff7144;
      }
      .legend-circle {
        display: inline-block;
        height: 14px;
        width: 14px;
        border-radius: 50%;
        background: #1cfff5;
        vertical-align: middle;
        margin-right: 10px;
      }
      .legend-num {
        font-size: 20px;
        color: #1cfff5;
        margin-right: 7px;
      }
    }
  }
}
</style>

<style lang="scss">
.pump-Station-card .panel-item .panel-item__content--tabs .tab-button,
.pump-Station-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.pump-Station-card .panel-item .panel-item__content--tabs .tab-button.active,
.pump-Station-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
  .flood-discharge {
    user-select: none;
    color: #fff;
    width: 100%;
    overflow-y: scroll;
    &::-webkit-scrollbar {
      display: none;
    }
    .flood-discharge-item {
      padding: 10px 0;
      border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
      position: relative;
      &.white-bg {
        &:after {
          content: "";
          position: absolute;
          height: 100%;
          width: 200%;
          left: 0;
          top: 0;
          opacity: 0.5;
          background-image: linear-gradient(
            90deg,
            #ffffff33 0%,
            #f9f9f900 100%
          );
        }
      }
      &:nth-child(even) {
        border-right: 0;
      }
      .flood-discharge-item__content {
        .item-title {
          font-size: 16px;
          text-align: center;
          padding-top: 5px;
        }
        .item-text {
          display: flex;
          padding-top: 5px;
          font-size: 18px;
          justify-content: center;
          > i {
            margin: 5px 10px 0 0;
          }
          .item-label {
            font-size: 14px;
            .number {
              margin: 0 5px;
              font-size: 24px;
              color: #1cfff5;
            }
          }
        }
      }
    }
  }
}
::v-deep .rivertooltip__item {
  padding: 4px 8px;
  font-size: 18px;
  &.rivertooltip__item-error {
    color: #ff7144;
  }
  &.rivertooltip__item-warn {
    color: #eec80b;
  }
}
::v-deep .panel-item__title--switch {
  i {
    cursor: pointer;
  }
}
.reservoir-sty {
  > p {
    @include flexbox;
    @include flexflow(row, nowrap);
    line-height: 32px;
    padding: 4px 0;
    span {
      color: #fff;
      font-size: 24px;
    }
    display: flex;
    // flex-direction: column;
    align-items: center;
  }
  // > p:nth-child(1) {
  //   margin-right: 16px;
  //   > span {
  //     color: #1cfff5;
  //   }
  // }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat
      top center;
    background-size: 100% 100%;
  }
}
.reservoir-style {
  $color: #1cfff485;
  // position: absolute;
  content: "";
  width: 100%;
  // height: 2.5rem;
  left: 0;
  background: linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) left top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) right top no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) left bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat,
    linear-gradient(to bottom, $color, $color) right bottom no-repeat;
  /*设置大小*/
  background-size: 12px 2px, 2px 12px;
  border-radius: 3px;
}
::v-deep .el-carousel {
  .el-carousel__container {
    height: 100%;
  }
}

.carousel-item-content {
  // padding: 0 16px;
  @include flexbox;
  @include flexflow(column, nowrap);
  @include flexJC(space-around);
}
.reservoir-sty1 {
  margin: 0;
  @include flexbox;
  @include flexJC(space-between);
  p {
    margin: 0;
    line-height: 32px;
    width: 50%;
    margin: 12px 0;
    @include flexbox;
    @include flexflow(column, nowrap);
    @include flexAC;
    @include flexJC(center);
    background: url(~assets/images/WaterDisastersImage/rainfall-bg.png)
      no-repeat;
    background-size: 100% 100%;
  }
  > p:nth-child(1) {
    margin-right: 16px;
    > span {
      color: #1cfff5;
      font-size: 16px;
    }
  }
  > p:nth-child(2) {
    background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat
      top center;
    background-size: 100% 100%;
    > span {
      font-size: 16px;
      > span {
        color: #eec80b;
      }
    }
  }
}
.area-style {
  padding: 5px !important;
  // margin: 0;
  margin: 0;
  height: 40px;
  p {
    @include flexbox;
    @include flexflow(row, nowrap);
    @include flexAC;
    span {
      margin: 0;
    }
  }

  > p:nth-child(1) {
    line-height: 1.8rem;
    color: #1cfff5;
    font-weight: 500;
    @include flexbox;
    > i {
      vertical-align: text-top;
      margin-top: 2px;
      margin-right: 0;
    }
    > span {
      padding-left: 2px;
      color: #1cfff5;
    }
  }
  > p:nth-child(2) {
    line-height: 25px;
    color: #fff;
    > span {
      // font-size: 28px;
      color: #1ff1f0;
      padding: 0 6px;
    }
  }
  > p:nth-child(3) {
    line-height: 25px;
    color: #fff;
    > i {
      vertical-align: text-top;
      margin-top: 7px;
      margin-right: 0;
    }
    > span {
      font-size: 28px;
      color: #eec80b;
      padding: 0 6px;
    }
  }
}
.diagram-sty {
  // margin: 15px 0;
  color: #fff;
  height: calc(100% - 60px);
  .diagram-sty-content {
    height: 100%;
  }
  .border-L {
    height: 90%;
    border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
  }
  ::v-deep.border-R::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 0; /*高宽分别对应横竖滚动条的尺寸*/
  }
  .border-R {
    padding-left: 10px;
    height: 90%;
    overflow-y: auto;
    font-size: 16px;
    color: #1cfff5;
    .classOption-sty {
      padding: 5px 0;
      display: flex;
      justify-content: space-between;
      > p:nth-child(1) {
        // width: 100px;
        overflow-x: auto;
        overflow: hidden;
      }
    }
  }
}
.storage-capacity {
  margin-top: 9px;
  background-image: linear-gradient(
    64deg,
    rgba(0, 193, 255, 0) 5%,
    #00c1ff 100%
  );
  width: 15%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(
    64deg,
    #00c1ff 5%,
    rgba(0, 193, 255, 0) 100%
  );
  margin-top: 9px;
  width: 20%;
  height: 2px;
}

.icon-sty {
  margin-right: 10px;
  &.icon-year-100 {
    @include chart-icon();
    background: #19af47;
  }
  &.icon-year-50 {
    @include chart-icon();
    background: #eec80b;
  }
  &.icon-year-20 {
    @include chart-icon();
    background: #e89100;
  }
  &.icon-year-10 {
    @include chart-icon();
    background: #dd00db;
  }
  &.icon-year-5 {
    @include chart-icon();
    background: #e80000;
  }
  &.risk-legend {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    display: inline-block;
    @include chart-icon();
  }
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
  height: 100%;
  .flood-discharge {
    user-select: none;
    color: #fff;
    width: 100%;
    overflow-y: scroll;
    &::-webkit-scrollbar {
      display: none;
    }
    .flood-discharge-item {
      padding: 10px 0;
      border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
      position: relative;
      &.white-bg {
        &:after {
          content: "";
          position: absolute;
          height: 100%;
          width: 200%;
          left: 0;
          top: 0;
          opacity: 0.5;
          background-image: linear-gradient(
            90deg,
            #ffffff33 0%,
            #f9f9f900 100%
          );
        }
      }
      &:nth-child(even) {
        border-right: 0;
      }
      .flood-discharge-item__content {
        .item-title {
          font-size: 16px;
          text-align: center;
          padding-top: 5px;
        }
        .item-text {
          display: flex;
          padding-top: 5px;
          font-size: 18px;
          justify-content: center;
          > i {
            margin: 5px 10px 0 0;
          }
          .item-label {
            font-size: 14px;
            .number {
              margin: 0 5px;
              font-size: 24px;
              color: #1cfff5;
            }
          }
        }
      }
    }
  }
}
</style>
