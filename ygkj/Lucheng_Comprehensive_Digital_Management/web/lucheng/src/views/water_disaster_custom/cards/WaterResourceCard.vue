<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 水资源量
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="stagnant-water-card">
    <panel-item
      title="水资源量"
      has-switch
      tc
      @tclick="tclick('水资源量')"
      :action-tabs="['水资源总量', '人均水资源量']"
      @change="handleTabChange"
    >
      <template #label-description></template>

      <template #title-switch>
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
          @click.stop="clickRealtimeRainfull()"
          :class="[
            realdzmbtn == true ? 'icon-diagram-1' : 'icon-diagram',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水资源量')"
          style="margin-left: 10px"
        />
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <ve-histogram
          ref="chart1"
          height="100%"
          width="100%"
          :data="barData1"
          :settings="barOption.setting"
          :extend="barOption.extend"
          :grid="barOption.grid"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig"
        />
      </div>
      <div class="is-water-box" v-if="activeTab == 1">
        <ve-histogram
          ref="chart2"
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
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { generateDataZoom } from "./utils/chart.js";
import mapConfig from "@/config/mapConfig";
import { queryMapserver } from "@/api/loadMapServe";

import * as echarts from "echarts"; // echart插件

export default {
  name: "WaterResourceCard",
  layerName: "waterResourceAreaLayer",
  legendName: "水资源量", // 有点位加 图例名称
  components: {
    PanelItem,
    EchartPie,
  },
  computed: {
    ...mapGetters(["previewModule"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },

  data() {
    return {
      activeTab: 0,
      timer: null,
      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      barData1: {
        columns: ["adnm", "totalWaterResource"],
        rows: [],
      },
      barData2: {
        columns: ["adnm", "watResPerCapita"],
        rows: [],
      },
      barOption: {
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
      realdzmbtn: false,
      areaGeometryArr: [],
    };
  },
  created() {
    this.getYearOption();
    this.getAreaGeometry();
  },
  mounted() {
    this.$nextTick(() => {
      this.stats4WrbWra();
      this.getCenterData();
      this.timer = setInterval(() => {
        this.stats4WrbWra();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterResourceCount"]),
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
    // 获取水资源统计数据
    async stats4WrbWra() {
      let opt = {
        type: this.activeTab == 0 ? "1" : "2",
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.stats4WrbWra(opt).then((res) => {
        this.checkYear = res.data[0].year + " 年";
        if (this.activeTab == 0) {
          this.barData1.rows = res.data;
          this.barOption.setting.yAxisName =  ["亿m³"]; 
        } else {
          this.barData2.rows = res.data;
          this.barOption.setting.yAxisName =  ["m³"]; 
        }
      });
      if (this.activeTab == 0) {
        this.$refs["chart1"].echarts.resize();
      } else {
        this.$refs["chart2"].echarts.resize();
      }
    },
    clickRealtimeRainfull() {
      this.realdzmbtn = !this.realdzmbtn;
      if (this.realdzmbtn) {
        this.showAreaPolygon();
      } else {
        this.map.drawAreaPolygon([], 'waterResourceAreaLayer');
        this.$emit("legend-change", "水资源量", {});
      }
    },
    showAreaPolygon() {
      // console.log(this.barData1,1233)
      let rows = this.barData1.rows;
      let arr = this.barData1.rows.map(item => { return item.totalWaterResource });
      if (this.activeTab == 1) {
        rows = this.barData2.rows;
        arr = this.barData2.rows.map(item => { return item.watResPerCapita });
      }
      const max = Math.max(...arr);
      const min = Math.min(...arr);
      const onePercent = (max - min) / 5;
      rows.map(item => {
        // console.log(item, 222)
        item.geometry = this.getGeometryByName(item.adnm);
        item.attr = item;
        item.type = '水资源量';
        item.active = this.activeTab;
        if (this.activeTab == 0) {
          item.style = this.getColor(item.totalWaterResource, max, min);
        } else {
          item.style = this.getColor(item.watResPerCapita, max, min);
        }
      });
      this.map.drawAreaPolygon(rows, 'waterResourceAreaLayer');
      this.$emit("legend-change", "水资源量", {
        label: "水资源量",
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
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.statsCwindow4WrbWra(opt).then((res) => {
        this.SetWaterResourceCount(res.data);
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
    handleTabChange(val) {
      this.activeTab = val;
      this.stats4WrbWra();
      this.getCenterData();
      if (this.realdzmbtn) {
        this.showAreaPolygon();
      }
    },
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
      options.tooltip = {
        show: true,
        trigger: 'item',
        confine: true,
        formatter: (val) => {
          let str = this.barData1.rows.find((item)=>{
            if(item.adnm == val.name) {
              return item;
            }
          });
          if (this.activeTab == 0) {
            let htmlText =
              `<p class="rivertooltip__item rivertooltip__item-error"><span>${str.adnm}</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>水资源总量: </span><span>${str.totalWaterResource}亿m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>较上一年: </span><span>${str.twrCompareLastYearRate}%</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>占全市水资源总量: </span><span>${str.twrOfCityRate}%</span></p>`;
            return htmlText;
          }else {
            let htmlText =
              `<p class="rivertooltip__item rivertooltip__item-error"><span>${str.adnm}</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>人均水资源量: </span><span>${str.watResPerCapita}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>较上一年: </span><span>${str.wrpcCompareLastYearRate}%</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于温州人均: </span><span>${str.wzWrpc}m³ </span> ${str.wrpcSubWzPosNeg == true ? '多' : '少'} <span>${str.wrpcSubWzAbsVal}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于浙江省人均: </span><span>${str.zjWrpc}m³</span> ${str.wrpcSubZjPosNeg == true ? '多' : '少'} <span>${str.wrpcSubZjAbsVal}m³</span></p>` +
              `<p class="rivertooltip__item rivertooltip__item-error"><span>相对于全国人均: </span><span>${str.cnWrpc}m³</span> ${str.wrpcSubCnPosNeg == true ? '多' : '少'} <span>${str.wrpcSubCnAbsVal}m³</span></p>`;
            return htmlText;
          }
        }
      };
      options.dataZoom = generateDataZoom(4, this.barData1.rows.length);
      return options;
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
      this.stats4WrbWra().then(() => {
        this.showAreaPolygon();
      });
      this.getCenterData();
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