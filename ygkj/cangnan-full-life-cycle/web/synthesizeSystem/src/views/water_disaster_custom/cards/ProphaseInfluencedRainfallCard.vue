<!--
 * @Autor: ysl
 * @Date: 2021-09-02 10:31:43
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-03 16:49:06
 * @Description: 前期影响雨量卡片
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/ProphaseInfluencedRainfallCard.vue
-->
<template>
  <div class="prophase-influenced-rainfall-card">
    <panel-item
      :title="'前期影响雨量'"
      has-switch
      tc
      @tclick="tclick('前期影响雨量')"
    >
      <template slot="title-switch">
        <i
          title="地图落点"
          @click="situationDrawMapHandle('sldbtn')"
          :class="[
            sldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog"
          style="margin-left: 10px"
        />
      </template>

      <div class="charts-panel">
        <div style="display: flex; justify-content: center;margin:15px 0;">
          <p class="storage-capacity"></p>
          <p style=" padding: 0 6px; color: #1cfff5; font-size: 16px; font-weight: 500;" > 近一个月影响雨量 </p>
          <p class="storage-capacity-sty"></p>
        </div>
        <div id="savingLineChart" style="width: 100%; height: 75%" ></div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { generateDataZoom } from "./utils/chart.js";
import mapConfig from "@/config/mapConfig";
import { queryMapserver } from "@/api/loadMapServe";

import * as echarts from "echarts"; // echart插件

export default {
  name: "ProphaseInfluencedRainfallCard",
  components: {
    PanelItem,
  },
  computed: {
    ...mapGetters(["previewModule"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },

  data() {
    return {
      activeTab: "节水指标",
      sldbtn: false,
      actionTabs: ["按区域", "按取水许可"],
      activeActionTab: 0,
      waterUseData: {},
      checkType: 0,
      filterTypeOptions: { label: "雨量", value: 0, unit: "mm", type: "totalWatCsm" },
      totalLength: 0,
      // 等值面
      barData1: {
        columns: ["areaName", "totalWra"],
        rows: [],
      },
      areaGeometryArr: []
    };
  },
  created() {
    this.getAreaGeometry()
  },
  mounted() {
    window.addEventListener("resize", this.handleWindowResize);
    this.waterUseDataList();
    this.precedeAffectRainfall()
    this.getCenterData();
    
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
    this.map.removeOverlayByName("targetOverlay");
    this.map.drawAreaPolygon([], 'waterResourceAreaLayer');
    this.$emit("legend-change", "影响雨量", {});
  },
  methods: {
    ...mapActions([
      "SetDetailDialogParam",
      "SetProphaseInfluencedRainfallCount",
    ]),
    // 获取C位数据
    async getCenterData() {
      let opt = {};
      await waterResourceApi.precedeAffectRainfall4CWindow(opt).then((res) => {
        this.SetProphaseInfluencedRainfallCount(res.data);
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog() {
      this.$emit("openDialog", "前期影响雨量");
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 数据
    async waterUseDataList() {
      this.waterUseData = {
        name: [],
        now: [],
        // watConControlNorm: 0,
      };
      let opt = {};
      let type = this.filterTypeOptions.type;
      await waterResourceApi.precedeAffectRainfallCurve(opt).then((res) => {
        // this.waterUseData.watConControlNorm = res.data.norm[type] || 0;
        res.data.forEach((item) => {
          item.date = (item.date && item.date.substring(5, 10)) || 0
          item.drp = (item.drp && item.drp.toFixed(1)) || 0
          this.waterUseData.name.push(item.date);
          this.waterUseData.now.push(item.drp);
        });
        if(this.waterUseData.name.length > 0){
          this.waterUseData.name = this.waterUseData.name.reverse();
          this.waterUseData.now = this.waterUseData.now.reverse();
        }
      });
      this.$nextTick(() => {
        this.waterUseConfig();
      });
    },
    // 线图
    waterUseConfig() {
      const _this = this;
      var chartDom = document.getElementById("savingLineChart");
      var myChart = echarts.init(chartDom);
      let iconPath1 = require("@/assets/images/map-realigning-3.png");
      let options = {
        dataZoom: generateDataZoom(6, this.waterUseData.name.length),
        grid: {
          top: "20%",
          bottom: "5%",
          left: "0%",
          right: "0%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.waterUseData.name,
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
              fontSize: "18",
            },
          },
        },
        yAxis: {
          type: "value",
          name: this.filterTypeOptions.unit,
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
          // max: _this.waterUseData.watConControlNorm + 5,
        },
        legend: {
          right: "10",
          selectedMode: false,
          textStyle: {
            color: "#fff",
            fontSize: 12,
          },
          itemHeight: 10,
          data: [
            {
              name: this.filterTypeOptions.label,
              icon: "image://" + iconPath1,
            },
          ],
        },
        tooltip: {
          trigger: "axis",
          formatter: function (data) {
            return (
              data[0].name +
              "<br/><span>" +
              _this.filterTypeOptions.label +
              "：</span>" +
              data[0].value +
              "<span>" +
              _this.filterTypeOptions.unit +
              "</span>"
            );
          },
          confine: true,
        },
        series: [
          {
            name: this.filterTypeOptions.label,
            type: "line",
            smooth: true,
            data: this.waterUseData.now,
            symbol: "none",
            lineStyle: {
              color: "#1EF8F2",
            },
          },
        ],
      };

      options && myChart.setOption(options);
    },

    // 点位数据
    async precedeAffectRainfall() {
      let result = [];
      await waterResourceApi.precedeAffectRainfall({}).then((res) => {
          result = res.data.map( item => {
            item.drp = item.drp && Number(item.drp).toFixed(2)
            item.value = item.drp
            item.adnm = item.areaName
            item.type = "前期影响雨量"
            item.unit = "mm"
            return item
          })
          this.barData1.rows = result
      })
      return result;
    },
    // 落点按钮
    situationDrawMapHandle(btnType) {
      this[btnType] = !this[btnType];
      // legend-change
      this.$emit("legend-change", "影响雨量", {});
      if (this[btnType]) {
          this.setMapOverLay();
          this.showAreaPolygon();
      } else {
        this.map.removeOverlayByName("targetOverlay");
        this.map.drawAreaPolygon([], 'waterResourceAreaLayer');
        this.$emit("legend-change", "影响雨量", {});
      }
    },
    // 落点
    setMapOverLay() {
      if (this.sldbtn) {
        // 非实时获取点位数据
        // this.precedeAffectRainfall().then((res) => {
          this.map.addSavingActionFixedOverlay(
            this.barData1.rows,
            "targetOverlay",
            "前期影响雨量"
          );
        // });
      }
    },
    // 区域等值面
    showAreaPolygon() {
      let rows = this.barData1.rows;
      rows.map(item => {
        item.geometry = this.getGeometryByName(item.areaName);
        item.attr = item;
        item.style = this.getColor(item.drp);
      });
      this.map.drawAreaPolygon(rows, 'waterResourceAreaLayer');
      this.$emit("legend-change", "影响雨量", {
        label: "影响雨量",
        children: [
          { label: `90 - 100`, icon: "evaluate-type1" },
          { label: `80 - 90`, icon: "evaluate-type3" },
          { label: `70 - 80`, icon: "evaluate-type6" },
          { label: `60 - 70`, icon: "evaluate-type4" },
          { label: `< 60`, icon: "evaluate-type5" },
        ],
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
    getGeometryByName(name) {
      let data = this.areaGeometryArr.filter(item => {
        return item.attributes.NAME == name;
      })
      return data[0].geometry.rings;
    },
    getColor(data) {
      const lineColors = ['#6D6DF0', '#42CC71', '#fcff24', '#FEB722', '#F7413F'];
      const fillColors = ['rgba(109,109,240,0.5)', 'rgba(66,204,113,0.5)', 'rgba(255,255,36,0.5)', 'rgba(254,183,34,0.5)', 'rgba(247,65,63,0.5)'];
      let colorObj = {
        fillColor: "",
        lineColor: ""
      }
      if (90 < data && data <= 100) {
        colorObj.fillColor = fillColors[0];
        colorObj.lineColor = lineColors[0];
      } else if (80 < data && data <= 90) {
        colorObj.fillColor = fillColors[1];
        colorObj.lineColor = lineColors[1];
      } else if (70 < data && data <= 80) {
        colorObj.fillColor = fillColors[2];
        colorObj.lineColor = lineColors[2];
      } else if (60 < data && data <= 70) {
        colorObj.fillColor = fillColors[3];
        colorObj.lineColor = lineColors[3];
      } else {
        colorObj.fillColor = fillColors[4];
        colorObj.lineColor = lineColors[4];
      }
      return colorObj;
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      this.waterUseConfig();
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.prophase-influenced-rainfall-card {
  width: 100%;
  height: 100%;
  .panel-item__content {
    overflow: hidden;
  }
  .area-box,
  .is-water-box {
    width: 100%;
    height: 100%;
  }
  .is-water-box {
    display: flex;
    height: calc(100% - 10px);
  }
  .pie-box {
    width: 100%;
    height: calc(100% - 24px);
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
.charts-panel {
  height: 100%;
}
.panel-item__content--tabs,
.panel-item__title--tabs {
  .tab-button {
    cursor: pointer;
    user-select: none;
    height: 24px;
    line-height: 24px;
    padding: 0 5px;
    border-radius: 13px;
    color: #fff;
    font-size: 20px;
    &.active {
      padding: 2px 7px;
      // background: #00c1ff;
      background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
    }
  }
}
.panel-item__content--tabs {
  margin-top: 6px;
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
.prophase-influenced-rainfall-card
  .panel-item
  .panel-item__content--tabs
  .tab-button,
.prophase-influenced-rainfall-card
  .panel-item
  .panel-item__title--tabs
  .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.prophase-influenced-rainfall-card
  .panel-item
  .panel-item__content--tabs
  .tab-button.active,
.prophase-influenced-rainfall-card
  .panel-item
  .panel-item__title--tabs
  .tab-button.active {
  padding: 0 12px;
}
</style>

<style lang="scss" scoped>
.storage-capacity {
  margin-top: 9px;
  background-image: linear-gradient(
    64deg,
    rgba(0, 193, 255, 0) 5%,
    #00c1ff 100%
  );
  width: 25%;
  height: 2px;
}
.storage-capacity-sty {
  background-image: linear-gradient(
    64deg,
    #00c1ff 5%,
    rgba(0, 193, 255, 0) 100%
  );
  margin-top: 9px;
  width: 25%;
  height: 2px;
}
</style>