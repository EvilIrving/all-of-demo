<!--
 * @Author: wpf
 * @Date: 2021-07-15 09:47:17
 * @LastEditTime: 2021-07-15 16:34:05
 * @LastEditors: Please set LastEditors
 * @Description: 河长巡查卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="water-quality-card">
    <panel-item
      title="河长巡查"
      has-switch
      tc
      @tclick="tclick('河长巡查')"
      :action-tabs="['行政区划', '处置状态', '问题程度', '变化曲线']"
      @change="handleTabChange"
    >
      <template slot="title-switch">
        <i
          title="地图落点"
          @click="drawPoint('ldbtn')"
          :class="[
            ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('河长巡查')"
          style="margin-left: 10px"
        />
      </template>

      <div id="barChart" class="area-box" v-if="activeTab == 0">
        <ve-histogram
          ref="bar"
          height="100%"
          width="100%"
          :data="barData"
          :settings="barOption.setting"
          :extend="barOption.extend"
          :grid="barOption.grid"
          :data-zoom="barOption.dataZoom"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />
      </div>
      <div class="area-box pie-box" v-if="activeTab == 1 || activeTab == 2">
        <div id="pieChart" style="width: 50%; height: 100%"></div>
        <div class="legend-panel">
          <div
            class="legend-item"
            v-for="(item, index) in pieDataPatrol"
            :key="index"
          >
            <div>
              <i
                v-if="activeTab == 1"
                :class="['icon-circle', 'icon-color-' + (index + 1)]"
              />
              <i
                v-if="activeTab == 2"
                :class="['icon-circle', 'icon-color-1-' + (index + 1)]"
              />
              <span class="text">{{ item.name }}</span>
            </div>
            <div>
              <span class="num">{{ item.value }}</span>
              <span>个</span>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 3">
        <div id="lineChart" style="width: 100%; height: 100%"></div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { generateDataZoom, generateStartDataZoom } from "./utils/chart.js";
import * as echarts from "echarts"; // echart插件

export default {
  name: "RiverPatrolCard",
  layerName: ["pointLayer1", "pointLayer2", "pointLayer3", "pointLayer4"],
  legendName: "河长巡查",
  components: {
    PanelItem,
  },
  computed: {
    ...mapGetters(["previewModule"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    getDate() {
      let date = new Date();
      let lastMonth = date.getTime() - 1000 * 60 * 60 * 24 * 30;

      let d = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      let m =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let y = date.getFullYear();

      let date1 = new Date(lastMonth);
      let d1 = date1.getDate() < 10 ? "0" + date1.getDate() : date1.getDate();
      let m1 =
        date1.getMonth() + 1 < 10
          ? "0" + (date1.getMonth() + 1)
          : date1.getMonth() + 1;
      let y1 = date1.getFullYear();

      return [`${y1}-${m1}-${d1} 00:00:00`, `${y}-${m}-${d} 00:00:00`];
    },
  },
  data() {
    return {
      ldbtn: false, // 落点
      activeTab: 0,
      pointList: [
        {
          name: "ceshi",
          lng: 120.5538,
          lat: 28.06823,
          a: 1,
        },
        {
          name: "ceshi2",
          lng: 120.8189,
          lat: 27.8993,
          a: 2,
        },
      ],
      barData: {
        columns: ["key", "value"],
        rows: [],
      },
      barOption: {
        setting: {
          yAxisName: ["个"],
          itemStyle: { color: "#fff" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            fontWeight: "bold",
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
        dataZoom: {
          type: "slider",
          startValue: 0, //默认为0
          endValue: 6,
          show: true,
          xAxisIndex: [0],
          handleSize: 0, //滑动条的 左右2个滑动条的大小
          height: 5, //组件高度
          bottom: 0,
          fillerColor: "#1EF8F2",
          backgroundColor: "#0000",
          borderColor: "#0000",
          showDetail: false,
          showDataShadow: false, //是否显示数据阴影 默认auto
          realtime: true, //是否实时更新
          filterMode: "filter",
        }
      },
      total: 123,
      pieDataPatrol: [],
      lineData: {
        name: [],
        value: [],
      },
      myChart: null,
      pieChart: null,
      timeFlag: null,//interval标记
      lineOptions: {}
    };
  },
  mounted() {
    this.chiefPatrolProblemStats("1");
    this.queryPatrolProblem();
  },
  beforeDestroy() {
    this.stopMove();
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
    //柱状图自动滚动
    autoMove() {
      this.goMove();
      document.getElementById("barChart").addEventListener('mouseover', this.stopMove);
      document.getElementById("barChart").addEventListener('mouseleave', this.goMove);
    },
    goMove() {
      if (this.timeFlag) {
        clearInterval(this.timeFlag);
      }
      this.timeFlag = setInterval(() => {
        // 每次向后滚动一个，最后一个从头开始。
        if (Number(this.barOption.dataZoom.endValue) === this.barData.rows.length - 1) {
          this.barOption.dataZoom.endValue = 6;
          this.barOption.dataZoom.startValue = 0;
        } else {
          this.barOption.dataZoom.endValue += 1;
          this.barOption.dataZoom.startValue += 1
        }
        // this.$refs["bar"].echarts.__Prototype__.setOption();
      }, 1000);
    },
    stopMove() {
      // console.log(123);
      clearInterval(this.timeFlag);
      this.timeFlag = null;
    },
    //折线图自动滚动
    lineAutoMove() {
      this.lineGoMove();
      this.myChart.on('mouseover', this.stopMove);
      this.myChart.on('mouseleave', this.lineGoMove);
    },
    lineGoMove() {
      this.timeFlag = setInterval(() => {
        // 每次向后滚动一个，最后一个从头开始。
        if (Number(this.lineOptions.dataZoom.endValue) === this.lineData.value.length - 1) {
          this.lineOptions.dataZoom.endValue = 4;
          this.lineOptions.dataZoom.startValue = 0;
        } else {
          this.lineOptions.dataZoom.endValue += 1;
          this.lineOptions.dataZoom.startValue += 1
        }
        this.myChart.setOption(this.lineOptions);
      }, 1000);
    },

    // 饼图统计数据
    async chiefPatrolProblemStats(type) {
      this.pieDataPatrol = [];
      this.total = 0;
      await waterResourceApi
        .chiefPatrolProblemStats({ type: type })
        .then((res) => {
          switch (type) {
            case "1":
              this.barData.rows = res.data;
              this.$nextTick(() => {
                this.$refs["bar"].echarts.resize();
                this.autoMove();
              });
              break;
            case "2":
              res.data.forEach((item) => {
                let obj = {
                  name: item.key,
                  value: item.value,
                };
                this.total = this.total + item.value;
                this.pieDataPatrol.push(obj);
              });
              this.$nextTick(() => {
                this.pieChartOptPatrol();
              });
              break;
            case "3":
              res.data.forEach((item) => {
                let obj = {
                  name: item.key,
                  value: item.value,
                };
                this.total = this.total + item.value;
                this.pieDataPatrol.push(obj);
              });
              this.$nextTick(() => {
                this.pieChartOptPatrol();
              });
              break;
            case "4":
              res.data.forEach((item) => {
                this.lineData.name.push(item.key);
                this.lineData.value.push(item.value);
              });
              this.$nextTick(() => {
                this.lineChartOpt();
                this.lineAutoMove();
              });
              break;
          }
        });
    },

    // 落点数据
    async queryPatrolProblem() {
      let opt = {
        startTime: this.getDate[0],
        endTime: this.getDate[1],
      };
      await waterResourceApi.queryPatrolProblem(opt).then((res) => {
        this.pointList = res.data.list;
      });
    },

    // 地图点位
    drawPoint(btnType) {
      this[btnType] = !this[btnType];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/hzxc-ld-jh-1.png"),
        src: require("@/assets/images/hzxc-ld-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/hzxc-ld-jh-2.png"),
        src: require("@/assets/images/hzxc-ld-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/hzxc-ld-jh-3.png"),
        src: require("@/assets/images/hzxc-ld-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/hzxc-ld-jh-4.png"),
        src: require("@/assets/images/hzxc-ld-4.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.riverName,
          type: "河长巡查",
          lng: Number(point.location && point.location.split(",")[0]),
          lat: Number(point.location && point.location.split(",")[1]),
          props: {
            发现地址: `${point.address || "-"}`,
            处置状态: `${this.getProblemStatu(point.state) || "-"}`,
            问题程度: `${this.getProblemType(point.problemType) || "-"}`,
            问题详情: `${point.content || "-"}`,
          },
          obj: point,
        };
        if (point.state == 1) {
          pointList1.push(obj);
        } else if (point.state == 2) {
          pointList2.push(obj);
        } else if (point.state == 3) {
          pointList3.push(obj);
        } else if (point.state == 4) {
          pointList4.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pointList1, style1, "pointLayer1");
        this.map.drawClusterPoint(pointList2, style2, "pointLayer2");
        this.map.drawClusterPoint(pointList3, style3, "pointLayer3");
        this.map.drawClusterPoint(pointList4, style4, "pointLayer4");
        this.$emit("legend-change", "河长巡查", {
          label: "河长巡查",
          children: [
            {
              label: "未受理",
              icon: "legend-hzxc-1",
            },
            {
              label: "处理中",
              icon: "legend-hzxc-2",
            },
            {
              label: "已处理待评价",
              icon: "legend-hzxc-3",
            },
            {
              label: "已处理已评价",
              icon: "legend-hzxc-4",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "pointLayer1");
        this.map.drawClusterPoint([], {}, "pointLayer2");
        this.map.drawClusterPoint([], {}, "pointLayer3");
        this.map.drawClusterPoint([], {}, "pointLayer4");
        this.$emit("legend-change", "河长巡查", {});
      }
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
      options.grid = {
        top: "20%",
        right: "0",
        left: "0%",
        bottom: "5%",
      };
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
            fontSize: 18,
            position: "top",
            color: "#1EF8F2",
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
      // options.dataZoom = generateDataZoom(5, this.barData.rows.length);
      return options;
    },
    // 饼图
    pieChartOptPatrol() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      if (this.myChart) echarts.dispose(this.myChart);
      if (this.pieChart) echarts.dispose(this.pieChart);
      this.pieChart = echarts.init(chartDom);
      var option;
      let colorList1 = ["#a80bee", "#e89100", "#008fe8", "#19af47"];
      let colorList2 = ["#e89100", "#ff551f", "#ef1313"];
      let color = [];
      let colorList = this.activeTab == 1 ? colorList1 : colorList2;
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
          show: true,
          confine: true,
        },
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
                formatter: this.total + "个\n总数",
              },
            },
            labelLine: {
              show: false,
              trigger: "item",
              confine: true,
            },
            data: this.pieDataPatrol,
            color: color,
          },
        ],
      };
      option && this.pieChart.setOption(option);
    },
    // 线图
    lineChartOpt() {
      var chartDom = document.getElementById("lineChart");
      if (this.myChart) echarts.dispose(this.myChart);
      if (this.pieChart) echarts.dispose(this.pieChart);
      this.myChart = echarts.init(chartDom);
      this.lineOptions = {
        dataZoom: generateStartDataZoom(0,5),
        grid: {
          top: "20%",
          bottom: "5%",
          left: "0%",
          right: "0%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.lineData.name,
          axisLabel: {
            show: true,
            textStyle: {
              color: "#fff",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            lineStyle: {
              color: "#fff",
              fontSize: "14",
            },
          },
        },
        yAxis: {
          type: "value",
          name: "个",
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
              show: false,
              color: "#fff",
              opacity: 0.2,
            },
          },
        },
        tooltip: {
          trigger: "axis",
          confine: true,
        },
        legend: {
          show: true,
          left: "right",
          textStyle: {
            color: "#fff",
          },
        },
        series: [
          {
            name: "问题数量",
            type: "line",
            data: this.lineData.value,
            smooth: true,
            symbol: "none",
          },
        ],
        color: "#E89100",
      };
      this.myChart.setOption(this.lineOptions);
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.stopMove();
      switch (val) {
        case 0: // 行政区划
          this.chiefPatrolProblemStats("1");
          break;
        case 1: // 处置状态
          this.chiefPatrolProblemStats("2");
          break;
        case 2: // 问题程度
          this.chiefPatrolProblemStats("3");
          break;
        case 3: // 变化曲线
          this.chiefPatrolProblemStats("4");
          break;
      }
    },
    // 河道问题程度转换
    getProblemType(val) {
      switch (val) {
        case 0:
          return "轻微";
        case 1:
          return "一般问题";
        case 2:
          return "重大问题";
      }
    },
    // 河道问题状态转换
    getProblemStatu(val) {
      switch (val) {
        case 1:
          return "未受理";
        case 2:
          return "处理中";
        case 3:
          return "已处理待评价";
        case 4:
          return "已处理已评价";
      }
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
  },
};
</script>
<style lang="scss" scoped>
.water-quality-card {
  width: 100%;
  height: 100%;
  .area-box {
    width: 100%;
    height: calc(100% - 44px);
    margin-top: 10px;
  }
  .pie-box {
    height: 150px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 20px;
    .legend-panel {
      width: 60%;
      margin-left: 30px;
      .legend-item {
        width: 95%;
        float: left;
        line-height: 25px;
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        .text {
          display: inline-block;
          font-size: 12px;
          margin-right: 10px;
        }
        .num {
          color: #1cfff5;
          font-size: 12px;
          margin-right: 5px;
        }
      }
    }
  }

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
    border-radius: 50%;
  }
  .icon-color-1 {
    @include chart-icon();
    background: #a80bee;
  }
  .icon-color-2 {
    @include chart-icon();
    background: #e89100;
  }
  .icon-color-3 {
    @include chart-icon();
    background: #008fe8;
  }
  .icon-color-4 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-color-1-1 {
    @include chart-icon();
    background: #e89100;
  }
  .icon-color-1-2 {
    @include chart-icon();
    background: #ff551f;
  }
  .icon-color-1-3 {
    @include chart-icon();
    background: #ef1313;
  }
}
</style>

<style lang="scss">
.water-quality-card .panel-item .panel-item__content--tabs .tab-button,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-quality-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-quality-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>