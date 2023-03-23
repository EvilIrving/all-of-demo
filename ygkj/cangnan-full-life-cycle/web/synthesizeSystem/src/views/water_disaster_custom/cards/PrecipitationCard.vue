<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-09-18 11:21:08
 * @LastEditors: ysl
 * @Description: 降水量（公报）
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/PrecipitationCard.vue
-->
<template>
  <div class="stagnant-water-card">
    <panel-item title="降水量" has-switch tc @tclick="tclick('降水量')">
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
          @click.stop="clickRealtimeRainfull('realdzmbtn')"
          :class="[
            realdzmbtn == true ? 'icon-diagram-1' : 'icon-diagram',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('降水量（公报）')"
          style="margin-left: 10px"
        />
      </template>

      <ve-histogram
        ref="chart"
        height="100%"
        width="100%"
        :data="barData1"
        :settings="barOption1.setting"
        :extend="barOption1.extend"
        :grid="barOption1.grid"
        :data-zoom="barOption1.dataZoom"
        :tooltip-visible="false"
        :legend-visible="false"
        :after-config="barConfig1"
      />
    </panel-item>
  </div>
</template>
<script>
import { mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import mapConfig from "@/config/mapConfig";
import { queryMapserver } from "@/api/loadMapServe";

import * as echarts from "echarts"; // echart插件

import { generateDataZoom } from "./utils/chart.js";

export default {
  name: "PrecipitationCard",
  layerName: ["preWaterAreaLayer"], // 有点位加
  legendName: "降水量公报", // 有点位加 图例名称
  components: {
    PanelItem,
  },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },

  data() {
    return {
      jsbtn: false, // 落点
      pointList: [],
      timer: null,

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      realdzmbtn: false, //实时等值面开关标记

      barData1: {
        columns: ["name", "data", "avg", "precent"],
        rows: [],
      },

      barOption1: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["亿m³", "%"],
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
      areaGeometryArr: [],
    };
  },
  created() {
    this.getYearOption();
    this.getAreaGeometry();
  },
  mounted() {
    this.$nextTick(() => {
      this.stats4WrbRainfall();
      this.getCenterData();
      this.timer = setInterval(() => {
        this.stats4WrbRainfall();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetPrecipitationCount"]),

    // 获取降水量统计数据
    async stats4WrbRainfall() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.stats4WrbRainfall(opt).then((res) => {
        let list = res.data.statsList;
        let data = [];
        let all = 0;
        list.forEach((item) => {
          all = all + item.rainfall;
        });
        list.forEach((item) => {
          let obj = {
            name: item.areaName,
            data: item.rainfall,
            avg: res.data.average,
            precent: all ? ((item.rainfall / all) * 100).toFixed(2) : 0,
          };
          data.push(obj);
        });
        this.barData1.rows = data.sort((a, b) => b.data - a.data);
        this.checkYear = res.data.year + " 年";
        this.$nextTick(() => {
          this.$refs["chart"].echarts.resize();
        });
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.statsCwindow4WrbRainfall(opt).then((res) => {
        this.SetPrecipitationCount(res.data);
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
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    barConfig1(options) {
      let data = options.series[0].data;
      let pre = options.series[2].data;
      let avg = options.series[1].data;
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
          yAxisIndex: 0,
          z: 1,
          name: "数据",
          type: "pictorialBar",
          symbol: "rect",
          symbolOffset: ["-25%", "0"],
          barWidth: barWidth,
          barGap: "-100%",
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
          yAxisIndex: 0,
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
          yAxisIndex: 0,
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
        {
          yAxisIndex: 1,
          z: 0,
          name: "占比",
          type: "bar",
          data: pre,
          barGap: "-100%",
          itemStyle: {
            normal: {
              opacity: 0,
            },
          },
        },
        {
          yAxisIndex: 0,
          name: "平均值",
          type: "line",
          barGap: "-100%",
          data: avg,
          lineStyle: {
            normal: {
              type: "solid",
              width: "2",
              color: "#00e065",
            },
          },
        },
      ];
      options.tooltip = {
        show: true,
        confine: true,
        formatter: (val) => {
          let htmlText =
            `<p class="rivertooltip__item rivertooltip__item-error"><span>${val.name}</span></p>` +
            `<p class="rivertooltip__item rivertooltip__item-error"><span>降水量: </span><span>${val.value}mm</span></p>` +
            `<p class="rivertooltip__item rivertooltip__item-warn"><span>占比: </span><span>${pre[val.dataIndex]
            }%</span></p>` +
            `<p class="rivertooltip__item rivertooltip__item-warn"><span>平均值: </span><span>${avg[
              val.dataIndex
            ].toFixed(2)}mm</span></p>`;
          return htmlText;
        },
      };
      options.dataZoom = generateDataZoom(4, this.barData1.rows.length);
      return options;
    }, // 50150,49850
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
    handleYear(val) {
      // console.log(111, val);
      this.stats4WrbRainfall().then(()=>{
        if(this.realdzmbtn){
          this.showAreaPolygon();
        }
      });
      this.getCenterData();
    },
    // 点击等值面
    clickRealtimeRainfull() {
      this.realdzmbtn = !this.realdzmbtn;
      if (this.realdzmbtn) {
        this.showAreaPolygon();
      } else {
        this.map.drawAreaPolygon([], 'preWaterAreaLayer');
        this.$emit("legend-change", "降水量公报", {});
      }
    },
    showAreaPolygon() {
      let rows = this.barData1.rows;
      let arr = this.barData1.rows.map(item => { return item.data });
      const max = Math.max(...arr);
      const min = Math.min(...arr);
      const onePercent = (max - min) / 5;
      rows.map(item => {
        item.geometry = this.getGeometryByName(item.name);
        item.attr = item;
        item.style = this.getColor(item.data, max, min)
      });
      this.map.drawAreaPolygon(rows, 'preWaterAreaLayer');
      this.$emit("legend-change", "降水量公报", {
        label: "降水量公报",
        children: [
          { label: `${parseFloat(onePercent * 4).toFixed(0)} - ${parseFloat(onePercent * 5).toFixed(0)}`, icon: "evaluate-type5" },
          { label: `${parseFloat(onePercent * 3).toFixed(0)} - ${parseFloat(onePercent * 4).toFixed(0)}`, icon: "evaluate-type4" },
          { label: `${parseFloat(onePercent * 2).toFixed(0)} - ${parseFloat(onePercent * 3).toFixed(0)}`, icon: "evaluate-type3" },
          { label: `${parseFloat(onePercent).toFixed(0)} - ${parseFloat(onePercent * 2).toFixed(0)}`, icon: "evaluate-type2" },
          { label: `${parseFloat(min).toFixed(0)}-${parseFloat(onePercent).toFixed(0)}`, icon: "evaluate-type1" },
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
    handleWindowResize() {
      if (!this.barChart) return;
      this.barChart.resize();
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.stagnant-water-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
  }
  .is-water-box {
    display: flex;
  }
  .pie-box {
    width: 40%;
    height: 100%;
  }
  .pie-legend {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 60%;
    height: 100%;
    font-size: 16px;
    padding-left: 30px;
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
#barChart {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss">
.el-dropdown-menu.year-dropdown {
  max-height: 200px;
  overflow: auto;
  background: rgba(2, 10, 28, 0.7);
  //   background: #061639;
  border: 1px solid #1cfff5;
  .el-dropdown-menu__item {
    line-height: 26px;
  }
  .el-radio {
    color: #fff;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1cfff5;
    background: transparent;
    &::after {
      width: 8px;
      height: 8px;
      background: #1cfff5;
    }
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #1cfff5;
  }
  .el-radio__inner {
    background: transparent;
    border: 1px solid #fff;
  }
}
.el-dropdown-menu.year-dropdown.el-popper[x-placement^="bottom"]
  .popper__arrow::after {
  border-bottom-color: rgba(2, 10, 28, 0.7);
}
</style>

<style lang="scss">
.stagnant-water-card .panel-item .panel-item__content--tabs .tab-button,
.stagnant-water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.stagnant-water-card .panel-item .panel-item__content--tabs .tab-button.active,
.stagnant-water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>