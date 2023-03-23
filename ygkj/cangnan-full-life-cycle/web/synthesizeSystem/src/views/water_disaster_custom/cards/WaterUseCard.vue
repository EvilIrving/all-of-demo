<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 用水量（公报）
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterSupplyCard.vue
-->
<template>
  <div class="water-card">
    <panel-item
      title="用水量"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['行业类别', '行政区划']"
      @change="handleTabChange"
    >
      <template slot="label-description">（公报）</template>
      <template slot="title-switch">
        <el-dropdown placement="bottom-start" trigger="click">
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span style="font-size: 1rem">{{ checkYear }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in yearOption" :key="index">
              <el-radio
                v-model="checkYear"
                :label="item"
                @change="handleYear"
                >{{ item }}</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <i
          title="等值面"
          style="height: 18px"
          v-if="activeTab == 1"
          @click.stop="clickRealtimeRainfull()"
          :class="[
            realdzmbtn == true ? 'icon-diagram-1' : 'icon-diagram',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('用水量（公报）')"
        />
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <div class="pie-chart-panel">
          <div id="waterUseEchart" style="width: 100%; height: 100%"></div>
          <div class="legend-panel">
            <div
              class="legend-item"
              v-for="(item, index) in waterUseJobData.rows"
              :key="index"
            >
              <i
                style="vertical-align: middle"
                :class="['icon-sty', 'icon-circle', 'icon-year-' + index]"
              />
              <div class="text">
                <p>{{ item.name }}</p>
                <p>
                  <span style="color: #1cfff5; font-size: 1rem">{{
                    item.value
                  }}</span>
                  亿m³
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 1">
        <ve-histogram
          ref="chart"
          height="100%"
          width="100%"
          :data="waterUseAreaData"
          :settings="waterUseAreaOption.setting"
          :extend="waterUseAreaOption.extend"
          :grid="waterUseAreaOption.grid"
          :data-zoom="waterUseAreaOption.dataZoom"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />
      </div>
    </panel-item>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import mapConfig from "@/config/mapConfig";
import { queryMapserver } from "@/api/loadMapServe";
export default {
  name: "WaterUseCard",
  layerName: "focuLineLayer",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      activeTab: 0,

      waterUseJobData: {
        totalLength: "477.2",
        rows: [],
      },
      waterUseAreaData: {
        columns: ["areaName", "waterConsume"],
        rows: [],
      },
      waterUseAreaOption: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["亿m³"],
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
      },

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      realdzmbtn:false,
      areaGeometryArr: [],
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  created() {
    this.getYearOption();
    this.getAreaGeometry();
  },
  mounted() {
    this.stats4WrbWaterConsume();
    this.getCenterData();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.stats4WrbWaterConsume();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterUseCount"]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    //获取行政区划边界
    getAreaGeometry() {
      const searchUrl =
        mapConfig.mapServer + "wenzhou/wenzhou_fangxunend/MapServer/3/query";
      const opt = {
        where: "1=1",
        outFields: "*",
        f: "pjson",
      };
      queryMapserver(searchUrl, opt).then((res) => {
        if (res.features) {
          this.areaGeometryArr = res.features;
        }
      });
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.stats4WrbWaterConsume();
    },
    /**
     * 用水量行业类别数据接口请求
     */
    async stats4WrbWaterConsume() {
      let opt = {
        type: this.activeTab == 0 ? "1" : "2",
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      let data = [];
      let count = 0;
      await waterResourceApi.stats4WrbWaterConsume(opt).then((res) => {
        this.checkYear = res.data[0].year + " 年";
        res.data.forEach((item) => {
          let value = item.classifyValue || item.waterConsume;
          let obj = {
            name: item.classifyName,
            value: value.toFixed(2),
          };
          count = count + item.classifyValue;
          data.push(obj);
        });
        if (this.activeTab == 0) {
          this.waterUseJobData.rows = data;
          this.waterUseJobData.totalLength = count.toFixed(2);
          this.$nextTick(() => {
            this.waterUseEchart();
          });
        } else {
          this.waterUseAreaData.rows = res.data;
          this.$nextTick(() => {
            this.$refs["chart"].echarts.resize();
          });
        }
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.statsCwindow4WrbWaterConsume(opt).then((res) => {
        this.SetWaterUseCount(res.data);
      });
    },
    /**
     * 图表 行业类别
     */
    waterUseEchart() {
      var chartDom = document.getElementById("waterUseEchart");
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
            color: "#19AF47", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
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
            color: "#A000C8", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
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
            color: "#009AFF", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0,0.7)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.name +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'> 亿m³</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
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
                formatter: this.waterUseJobData.totalLength + "亿m³\n" + "总量", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.waterUseJobData.rows,
            color: [color0, color1, color2],
          },
        ],
      };
      option && myChart.setOption(option);
    },

    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push(i + " 年");
      }
      // this.checkYear = y + " 年";
    },
    // 下拉年份选择后
    handleYear() {
      this.stats4WrbWaterConsume().then(()=>{
        if(this.realdzmbtn){
          this.showAreaPolygon();
        }
      });
      this.getCenterData();
    },
    // 行政区划柱状图设置
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
      let barWidth = 30;
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
      options.dataZoom = generateDataZoom(4, this.waterUseAreaData.rows.length);
      return options;
    },
    clickRealtimeRainfull(){
      this.realdzmbtn = !this.realdzmbtn;
      if (this.realdzmbtn) {
        this.showAreaPolygon();
      } else {
        this.map.drawAreaPolygon([], 'waterUseAreaLayer');
        this.$emit("legend-change", "用水量", {});
      }
    },
    showAreaPolygon() {
      let rows = this.waterUseAreaData.rows;
      let arr = this.waterUseAreaData.rows.map(item => { return item.waterConsume });
      // console.log(rows,123)
      const max = Math.max(...arr);
      const min = Math.min(...arr);
      const onePercent = (max - min) / 5;
      rows.map(item => {
        item.geometry = this.getGeometryByName(item.areaName);
        item.attr = item;
        item.style = this.getColor(item.waterConsume, max, min)
      });
      this.map.drawAreaPolygon(rows, 'waterUseAreaLayer');
      this.$emit("legend-change", "用水量", {
        label: "用水量",
        children: [
          { label: `${parseFloat(onePercent * 4).toFixed(1)} - ${parseFloat(onePercent * 5).toFixed(1)}`, icon: "evaluate-type5" },
          { label: `${parseFloat(onePercent * 3).toFixed(1)} - ${parseFloat(onePercent * 4).toFixed(1)}`, icon: "evaluate-type4" },
          { label: `${parseFloat(onePercent * 2).toFixed(1)} - ${parseFloat(onePercent * 3).toFixed(1)}`, icon: "evaluate-type3" },
          { label: `${parseFloat(onePercent).toFixed(1)} - ${parseFloat(onePercent * 2).toFixed(1)}`, icon: "evaluate-type2" },
          { label: `${parseFloat(min).toFixed(1)}-${parseFloat(onePercent).toFixed(1)}`, icon: "evaluate-type1" },
        ],
      });
    },
    getGeometryByName(name) {
      let data = this.areaGeometryArr.filter(item => {
        return item.attributes.NAME == name;
      })
      return data[0].geometry.rings;
    },
    getColor(data, max, min) {
      const normalizationRatio = this.normalization(data, max, min);
      const lineColors = ['#6D6DF0', '#3797F7', '#42CC71', '#FEB722', '#F7413F'];
      const fillColors = ['rgba(109,109,240,0.5)', 'rgba(55,151,247,0.5)', 'rgba(66,204,113,0.5)', 'rgba(254,183,34,0.5)', 'rgba(247,65,63,0.5)'];
      let colorObj = {
        fillColor: "",
        lineColor: ""
      }
      if (normalizationRatio <= 0.2) {
        colorObj.fillColor = fillColors[0];
        colorObj.lineColor = lineColors[0];
      } else if (normalizationRatio <= 0.4) {
        colorObj.fillColor = fillColors[1];
        colorObj.lineColor = lineColors[1];
      } else if (normalizationRatio <= 0.6) {
        colorObj.fillColor = fillColors[2];
        colorObj.lineColor = lineColors[2];
      } else if (normalizationRatio <= 0.8) {
        colorObj.fillColor = fillColors[3];
        colorObj.lineColor = lineColors[3];
      } else {
        colorObj.fillColor = fillColors[4];
        colorObj.lineColor = lineColors[4];
      }
      return colorObj;
    },
    normalization(value, max, min) {
      let normalizationRatio = (value - min) / (max - min)
      return normalizationRatio;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-card {
  height: 100%;

  .icon-sty {
    margin-right: 10px;
  }

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
  }

  .icon-year-0 {
    @include chart-icon();
    background: #30c8ee;
  }
  .icon-year-1 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-year-2 {
    @include chart-icon();
    background: #088bdd;
  }
  .icon-year-3 {
    @include chart-icon();
    background: #dcf874;
  }
  .icon-year-4 {
    @include chart-icon();
    background: #fd4851;
  }
  .icon-year-5 {
    @include chart-icon();
    background: #e800d5;
  }

  .area-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .pie-chart-panel {
    height: 100%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    .legend-panel {
      width: 100%;
      .legend-item {
        .text {
          display: inline-block;
          p {
            display: inline-block;
            font-size: 14px;
            margin-bottom: 8px;
            margin-right: 10px;
            &:last-child {
              margin-bottom: 0;
            }
          }
        }
      }
    }
  }
  .el-dropdown {
    cursor: pointer;
    top: -2px;
    right: 10px;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .el-icon-caret-bottom {
    margin-left: 3px;
  }
}
</style>

<style lang="scss">
.water-card .panel-item .panel-item__content--tabs .tab-button,
.water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
