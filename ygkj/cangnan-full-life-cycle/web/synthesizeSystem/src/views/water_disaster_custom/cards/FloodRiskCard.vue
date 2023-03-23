<!--
 * @Author: dyn
 * @Date: 2021-07-21 15:51:37
 * @LastEditTime: 2021-07-28 11:04:56
 * @LastEditors: Please set LastEditors
 * @Description: 洪水风险图小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\FloodRiskCard.vue
-->
<template>
  <div class="flood-risk-card">
    <panel-item
      title="洪水风险图"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['淹没面积', '影响人口', '淹没区洪水损失']"
      @change="handleTabChange"
    >
      <template slot="title-switch">
        <el-dropdown
          @visible-change="yearDropdownShow"
          placement="bottom-start"
        >
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span>{{ checkYear }}年一遇</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in yearOption" :key="index">
              <el-radio v-model="checkYear" :label="item" @change="handleYear"
                >{{ item }}年一遇</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <el-popover
          placement="bottom-start"
          width="80"
          trigger="hover"
          @show="basinDropdownShow"
          @hide="basinDropdownHide"
          popper-class="basin-popper"
        >
          <el-checkbox-group v-model="checkBasin">
            <el-checkbox label="瓯江流域"></el-checkbox>
            <el-checkbox label="飞云江流域"></el-checkbox>
            <el-checkbox label="鳌江流域"></el-checkbox>
          </el-checkbox-group>
          <span
            class="dropdown-wrap"
            :class="basinDropdownVisible ? 'dropdown-active' : ''"
            slot="reference"
          >
            <i
              style="vertical-align: text-bottom; margin-left: 6px"
              :class="
                basinDropdownVisible
                  ? 'icon-yinzhang-active'
                  : 'icon-yinzhang-1'
              "
            ></i>
            <i class="el-icon-caret-bottom"></i>
          </span>
        </el-popover>
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('洪水风险图')"
          style="margin-left: 10px"
        />
      </template>
      <div class="rainfall-style">
        <p v-if="activeTab == 0">
          全市淹没总面积
          <span>{{ floodTotal.floodedAreaTotal }}</span> km²
        </p>
        <p v-else-if="activeTab == 1">
          全市影响总人口
          <span> {{ floodTotal.submergedPopulationTotal }}</span> 万人
        </p>
        <p v-else>
          全市淹没区洪水损失
          <span> {{ floodTotal.floodLossTotal }}</span> 万元
        </p>
      </div>
      <el-row class="card-container" style="margin-top: 6px">
        <el-col :span="10" class="hydrops-sty">
          <div id="chart" class="chart" style="height: 100%; width: 100%"></div>
          <!-- <echartPie :seriesData="pieData" :extraOption="pieOption"></echartPie> -->
        </el-col>
        <el-col
          :span="14"
          class="hydrops-sty"
          style="padding-top: 15px; overflow-y: auto"
        >
          <div class="legend-wrap">
            <div v-for="(item, index) in pieData" :key="index">
              <p class="legend-label">
                <i
                  :style="{
                    'vertical-align': 'baseline',
                    background: item.color,
                  }"
                  class="icon-sty risk-legend"
                />{{ item.name }}
              </p>
              <p class="legend-value">
                <span> {{ item.value }} </span>{{ item.unit }}
              </p>
            </div>
          </div>
        </el-col>
      </el-row>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import * as echarts from "echarts"; // echart插件
export default {
  name: "FloodRiskCard",
  layerName: "designFloodLayer",
  legendName: "淹没深度",
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
      spbtn: false, // 落点
      activeTab: 0,
      videoTotal: 80,
      yearDropdownVisible: false,
      basinDropdownVisible: false,
      checkYear: "5",
      checkBasin: [],
      yearOption: ["5", "10", "20", "50"],
      pieData: [],
      designFloodName: {
        鳌江流域: "SJ",
        飞云江流域: "FYJ_SJ",
        瓯江流域: "OJ_SJ",
      },
      areaPieData: [],
      peoplePieData: [],
      moneyPieData: [],
      floodTotal: {},
      legendFlag: false,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.floodRiskList();
    });
  },
  watch: {
    checkBasin: function (newVal, oldVal) {
      if (newVal.length > oldVal.length) {
        this.map.drawDesignFlood(
          this.designFloodName[newVal[newVal.length - 1]],
          this.checkYear
        );
      } else {
        for (let item of oldVal) {
          if (newVal.findIndex((nitem) => nitem == item) == -1) {
            this.map.clearDesignFlood(
              `${this.designFloodName[item]}${this.checkYear}`
            );
          }
        }
      }
      if (newVal.length == 0) {
        this.legendFlag = false;
        this.$emit("legend-change", "淹没深度", {});
      } else {
        if (!this.legendFlag) {
          this.$emit("legend-change", "淹没深度", {
            label: "淹没水深彩色",
            children: [
              {
                label: "0.2~0.5",
                icon: "icon-flood02",
              },
              {
                label: "0.5~1",
                icon: "icon-flood05",
              },
              {
                label: "1~1.5",
                icon: "icon-flood1",
              },
              {
                label: "1.5~2",
                icon: "icon-flood15",
              },
              {
                label: "2~3",
                icon: "icon-flood2",
              },
              {
                label: ">3",
                icon: "icon-flood3",
              },
            ],
          });
          this.legendFlag = true;
        }
      }
    },
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetFloodRiskTotal"]),
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
    // 流域下拉
    yearDropdownShow(val) {
      this.yearDropdownVisible = val;
    },
    handleYear() {
      this.map.clearDesignFlood();
      if (this.checkBasin.length > 0) {
        for (let i of this.checkBasin) {
          this.map.drawDesignFlood(this.designFloodName[i], this.checkYear);
        }
      }
      this.floodRiskList();
    },
    basinDropdownShow() {
      this.basinDropdownVisible = true;
    },
    basinDropdownHide() {
      this.basinDropdownVisible = false;
    },
    async floodRiskList() {
      this.peoplePieData = [];
      this.areaPieData = [];
      this.moneyPieData = [];
      this.pieData = [];
      let obj = {
        designStandard: `${this.checkYear}年一遇`,
      };
      await disasterPreventionApi.floodRiskList(obj).then((res) => {
        if (res.code == 0) {
          let arr = [];
          if (res.data.list[0].name == '鳌江') {
            arr = res.data.list;
          }else {
            arr = [res.data.list[2],res.data.list[0],res.data.list[1]];
          }
          let obj = {
            鳌江: "#00EBC7",
            瓯江: "#FF7144",
            飞云江: "#EEC80B",
          };
          for (let item of arr) {
            this.peoplePieData.push({
              name: item.name,
              value: item.submergedPopulation,
              unit: "万人",
              color: obj[item.name],
              total: item.submergedPopulationTotal,
            });
            this.areaPieData.push({
              name: item.name,
              value: item.floodedArea,
              unit: "km²",
              color: obj[item.name],
              total: item.floodedAreaTotal,
            });
            this.moneyPieData.push({
              name: item.name,
              value: item.floodLoss,
              unit: "万元",
              color: obj[item.name],
              total: item.floodLossTotal,
            });
          }
          let totalObj = {
            floodedAreaTotal: arr[0].floodedAreaTotal,
            submergedPopulationTotal: arr[0].submergedPopulationTotal,
            floodLossTotal: arr[0].floodLossTotal,
          };
          this.floodTotal = totalObj;
          this.SetFloodRiskTotal(totalObj);
          this.handleTabChange();
        }
      });
    },
    handleTabChange(val) {
      this.pieData = [];
      if (val || val == 0) this.activeTab = val;
      if (this.activeTab == 0) {
        this.pieData = this.areaPieData;
      } else if (this.activeTab == 1) {
        this.pieData = this.peoplePieData;
      } else {
        this.pieData = this.moneyPieData;
      }
      this.drawChart();
    },
    drawChart() {
      var chartDom = document.getElementById("chart");
      this.myChart = echarts.init(chartDom);
      let option = {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return (
              data.data.name + ":" + "  " + data.data.value + data.data.unit
            );
          },
          confine: true,
          extraCssText: "width:160px;font-size:18px;",
        },
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
                color: "#00EBC7", // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.7)", // 100% 处的颜色
              },
            ],
            global: false, // 缺省为 false
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
                color: "#FF7144", // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.7)", // 100% 处的颜色
              },
            ],
            global: false, // 缺省为 false
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
                color: "#EEC80B", // 0% 处的颜色
              },
              {
                offset: 1,
                color: "rgba(0,0,0,0.7)", // 100% 处的颜色
              },
            ],
            global: false, // 缺省为 false
          },
        ],
        series: [
          {
            name: "洪水风险图",
            type: "pie",
            radius: ["60%", "90%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: false,
                position: "center",
                color: "#fff",
                fontSize: 12,
                // formatter: function (data) {
                //   return data.data.total;
                // },
              },
            },
            emphasis: {
              label: {
                show: true,
                fontSize: "20",
                fontWeight: "bold",
                color: "#fff",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieData,
          },
        ],
      };
      this.myChart.setOption(option);
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.flood-risk-card {
  width: 100%;
  height: 100%;
  .el-dropdown {
    cursor: pointer;

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
  .dropdown-wrap {
    cursor: pointer;
    &.dropdown-active {
      i {
        color: #1cfff5;
      }
    }
  }
  .dropdown-wrap .el-icon-caret-bottom {
    vertical-align: middle;
  }
  .rainfall-style {
    margin-top: 10px;
  }
  .card-container {
    width: 100%;
    height: calc(100% - 100px);
  }
  .hydrops-sty {
    height: 100%;
    color: #fff;
    font-size: 16px;
    font-weight: 400;
    @include flexbox;
    @include flexAC;
    &::-webkit-scrollbar {
      display: none;
    }
    > div {
      //   padding: 10px;
      > div {
        display: flex;
        padding: 5px;
        justify-content: space-between;
        > p:nth-child(2) {
          > span {
            color: #1cfff5;
          }
        }
      }
    }
    .icon-sty {
      margin-right: 10px;
      &.risk-legend {
        display: inline-block;
        height: 12px;
        width: 12px;
        border-radius: 50%;
      }
    }
    .legend-wrap {
      width: 100%;
    }
    .legend-label {
      width: 90px;
    }
    .legend-value {
      width: calc(100% - 90px);
      text-align: left;
    }
  }
}
</style>

<style lang="scss">
.el-dropdown-menu.year-dropdown {
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
.el-popper[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: #1cfff5;
}
.el-popover.basin-popper {
  background: rgba(2, 10, 28, 0.7);
  border: 1px solid #1cfff5;
  .el-checkbox,
  .el-checkbox__label {
    line-height: 26px;
    font-size: 14px;
  }
  .el-checkbox__inner {
    background: transparent;
    border: 1px solid #1cfff5;
  }
  .el-checkbox__inner::after {
    border: 1px solid #1cfff5;
    border-left: 0;
    border-top: 0;
  }
}
.basin-popper.el-popper[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: #1cfff5 !important;
  &::after {
    border-bottom-color: rgba(2, 10, 28, 0.7);
  }
}
</style>
<style lang="scss">
.flood-risk-card .panel-item .panel-item__content--tabs .tab-button,
.flood-risk-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.flood-risk-card .panel-item .panel-item__content--tabs .tab-button.active,
.flood-risk-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>