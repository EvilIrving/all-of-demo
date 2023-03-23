<!--
 * @Autor: ysl
 * @Date: 2021-08-23 09:50:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-13 10:56:06
 * @Description: 资产管理
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/governmentAssetsCard.vue
-->
<template>
  <div class="government-assets-card">
    <panel-item title="资产管理" has-switch tc @tclick="tclick('资产管理')">
      <template slot="title-switch"><span style="visibility: hidden">123123</span></template>
      
      <div class="charts-panel">
        <!-- 资产总览 -->
        <sub-title-click-card :title="['资产总览']">
          <div slot="title-operate">
            <el-radio v-model="checkRadio" label="1">数量</el-radio>
            <el-radio v-model="checkRadio" label="2">金额</el-radio>
            <i
              v-show="!previewModule"
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('资产总览')"
              style="margin-left: 10px"
            />
          </div>
          <div class="chart-panel more-panel">
            <div class="count-panel">
              <div class="count-item">
                <i class="icon-count icon-sybh-count-1"></i>
                <div class="text">
                  <p>资产总数</p>
                  <p class="num">{{ assetsObj.totalNum || 0 }} <span>件</span></p>
                </div>
              </div>
              <div class="count-item">
                <i class="icon-count icon-sybh-count-2"></i>
                <div class="text">
                  <p>到期件数</p>
                  <p class="num outNum">{{ assetsObj.expireNum || 0 }} <span>件</span></p>
                </div>
              </div>
              <div class="count-item">
                <i class="icon-count icon-sybh-count-3"></i>
                <div class="text">
                  <p>资产总额</p>
                  <p class="num">{{ assetsObj.totalAmount || 0 }} <span>万元</span></p>
                </div>
              </div>
            </div>
            <div class="pie-box">
              <div id="deptTypePie" style="width:50%;height:100%" ref="deptTypePie"></div>
              <div class="legend-panel">
                <div
                  class="legend-item"
                  v-for="(item, index) in deptType"
                  :key="index"
                >
                  <div>
                    <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                    <span class="text">{{ item.label }}</span>
                  </div>
                  <span>
                    <span class="num">{{ item.value || 0 }}</span>
                    {{unit}}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </sub-title-click-card>
        <!-- 报废 -->
        <sub-title-click-card :title="[`报废 ${302} 件`]">
          <div slot="title-operate">
            <i
              v-show="!previewModule"
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('报废')"
              style="margin-left: 10px"
            />
          </div>

          <div class="chart-panel">
            <div class="bar_box">
              <el-row v-for="(item, index) in barDataList" :key="index" :gutter="14" class="bar_line">
                <el-col :span="7" class="text">{{ item.deptName }}</el-col>
                <el-col :span="14">
                  <el-progress 
                    :show-text="false"
                    :stroke-width="8" 
                    :percentage="(item.value / maxValue) * 100" 
                    :color="'#E89100'"></el-progress>
                </el-col>
                <el-col :span="3"><span :style="{'color': '#E89100'}">{{ item.value }}</span>/{{ maxValue }}</el-col>
              </el-row>
            </div>
          </div>
        </sub-title-click-card>
        <!-- 领用 -->
        <sub-title-click-card :title="[`领用 ${302} 件`]">
          <div slot="title-operate">
            <i
              v-show="!previewModule"
              title="查看详表"
              class="icon-liebiao icon-sty"
              @click.stop="openDetailsDialog('领用')"
              style="margin-left: 10px"
            />
          </div>

          <div class="chart-panel">
            <div class="bar_box">
              <el-row v-for="(item, index) in barDataList" :key="index" :gutter="14" class="bar_line">
                <el-col :span="7" class="text">{{ item.deptName }}</el-col>
                <el-col :span="14">
                  <el-progress 
                    :show-text="false"
                    :stroke-width="8" 
                    :percentage="(item.value / maxValue) * 100" 
                    :color="'#1df8f3'"></el-progress>
                </el-col>
                <el-col :span="3"><span :style="{'color': '#1df8f3'}">{{ item.value }}</span>/{{ maxValue }}</el-col>
              </el-row>
            </div>
          </div>
        </sub-title-click-card>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import SubTitleClickCard from "../components/SubTitleClickCard.vue";
import { oaApi } from "@/api/governmentOaApi";
import { generateDataZoom } from "./utils/chart.js";

import * as echarts from "echarts"; // echart插件

export default {
  name: "governmentAssetsCard",
  components: {
    PanelItem,
    SubTitleClickCard,
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

      monthOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      checkMonth: "",
      barData1: {
        columns: ["deptName", "peopleNum"],
        rows: [],
      },
      barData2: {
        columns: ["deptName", "totalCost"],
        rows: [],
      },
      checkRadio: "1",
      barDataList: [
				{ deptName: "局领导", value: 31 },
				{ deptName: "办公室", value: 28 },
				{ deptName: "市水情数据服务中心", value: 27 },
				{ deptName: "水政监察支队", value: 25 },
				{ deptName: "人事处", value: 10 },
			],
      maxValue: 100,
      assetsObj: {},
      unit: "件",
      deptType:[
				{ label: "局领导", value: 734 },
				{ label: "办公室", value: 512 },
				{ label: "市水情数据服务中心", value: 998 },
				{ label: "水政监察支队", value: 400 },
				{ label: "人事处", value: 355 },
			],
			totalLength: 100,
    };
  },
  created() {
  },
  mounted() {
    this.getOaOverView(1)
    this.getOaOverView(2)
    this.getOaOverView(4)
    // this.getOaOverView(5)
    // this.getOaOverView(6)
    this.deptTypePie()
  },
  watch: {
    checkRadio(val) {
      val == "1" ? this.getOaOverView(2) : this.getOaOverView(3)
    }
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetGovernmentAssetsCount"]),
    // 获取资产管理数据
    async getOaOverView(type) {
      let opt = {
        type: type, // 1，统计资产总数，到期件数，资产总额；2，按部门总计资产件数；3，按部门总计资产总额；4，大屏C位统计;5. 按部门统计报废，6. 按部门统计领用
      };
      await oaApi.stats4OaAssets(opt).then((res) => {
        if( res.code == 0 ){ 
          switch (type) {
            case 1 :
              this.assetsObj = res.data
              this.assetsObj.totalAmount = Number( this.assetsObj.totalAmount / 10000).toFixed(2)
              break;
            case 2 :
              this.unit = "件"
              // this.deptType = res.data.map(item => {
              //   item.label = item.key
              //   return item
              // })
              this.deptTypePie()
              break;
            case 3 :
              this.unit = "元"
              // this.deptType = res.data.map(item => {
              //   item.label = item.key
              //   return item
              // })
              this.deptTypePie()
              break;
            case 4 :
              // 设置c位数据
              this.SetGovernmentAssetsCount({
                totalAmount: (Number(res.data.totalAmount) / 10000).toFixed(2),
                totalNum: res.data.totalNum,
                scrapNum: res.data.scrapNum,
                collectNum: res.data.collectNum,
                expireNum: res.data.expireNum,
              });
              break;
            case 5 :
              break;
            case 6 :
              break;
          }
        }
      })
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
			this.$emit("openDialog", val)
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 资产总览 饼图
    deptTypePie() {
      var chartDom = document.getElementById("deptTypePie");
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
            color: "#00EBC7", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
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
            color: "#008FE8", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
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
            color: "#19AF47", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color3 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#FF7D7D", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
          },
        ],
        global: false, // 缺省为 false
      };
      const color4 = {
        type: "linear",
        x: 0,
        y: 0,
        x2: 1,
        y2: 1,
        colorStops: [
          {
            offset: 0,
            color: "#E89100", // 0% 处的颜色
          },
          {
            offset: 1,
            color: "rgba(0,0,0)", // 100% 处的颜色
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
              data.data.label +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'> 个</span>"
            );
          },
          confine: true,
          extraCssText: "width:160px;height:50px;font-size:18px;",
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
                fontSize: 22,
                formatter: this.totalLength + " ",
              },
            },
            labelLine: {
              show: false,
            },
            data: this.deptType,
            color: [ color0, color1, color2, color3, color4 ],
          },
        ],
      };
      option && myChart.setOption(option);
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.government-assets-card {
  width: 100%;
  height: 100%;
  .sub-title-card {
    height: auto;
  }
  ::v-deep .el-radio {
    color: #fff;
    .el-radio__inner {
      background-color: transparent;
      &:hover {
        border-color: #fff;
      }
    }
    &.is-checked {
      .el-radio__inner {
        border-color: #15E2DB;
        background: transparent;
        &::after {
          width: 0.4rem;
          height: 0.4rem;
          left: 45%;
          top: 45%;
          background-color: #15E2DB;
        }
      }
      .el-radio__label {
        color: #15E2DB;
      }
    }
  }
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
  }
	.bar_box {
		overflow: hidden auto;
  	height: 100%;
		width: 100%;
    padding: 8px;
		.bar_line {
			margin-top: 0;
			width: 98%;
      .text {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
		}
    &::-webkit-scrollbar-track-piece {
      background-color: transparent;
    }
    &::-webkit-scrollbar {
      width:3px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: #15E2DB;
    }
	}
	.bar_line {
		width: 100%;
		margin: 0.8rem;
		display: flex;
		align-items: center;
    &:last-child {
      margin-bottom: 0;
    }
	}
  .is-water-box {
    display: flex;
  }
  .pie-box {
    width: 100%;
    height: calc(100% - 116px);
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
  .count-panel {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    .count-item {
      width: 50%;
      display: flex;
      align-items: center;
      margin: 5px 0 0;
      .text {
        font-size: 14px;
        margin-left: 10px;
        line-height: 25px;
        .num {
          font-size: 20px;
          color: #56fefe;
        }
        .outNum {
          color: #E89100;
        }
        span {
          color: #fff;
          font-size: 12px;
        }
      }
    }
  }
  
  .icon-count {
    display: inline-block;
    width: 48px;
    height: 48px;
    background-size: 100% 100%;
    &.icon-sybh-count-1 {
      background-image: url(~assets/images/assets_count_1.png);
    }
    &.icon-sybh-count-2 {
      background-image: url(~assets/images/assets_count_2.png);
    }
    &.icon-sybh-count-3 {
      background-image: url(~assets/images/assets_count_3.png);
    }
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
.government-assets-card .panel-item .panel-item__content--tabs .tab-button,
.government-assets-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.government-assets-card .panel-item .panel-item__content--tabs .tab-button.active,
.government-assets-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>

<style lang="scss" scoped>
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
  &.icon-color-1 {
    @include chart-icon();
    background: #15E2DB;
  }
  &.icon-color-2 {
    @include chart-icon();
    background: #008fe8;
  }
  &.icon-color-3 {
    @include chart-icon();
    background: #19AF47;
  }
  &.icon-color-4 {
    @include chart-icon();
    background: #FF7D7D;
  }
  &.icon-color-5 {
    @include chart-icon();
    background: #E89100;
  }
}
.chart-panel {
  height: 150px;
  overflow: hidden;
  position: relative;
  &.more-panel {
    height: 300px;
  }
}
.pie-box {
  display: flex;
  justify-content: space-around;
  align-items: center;
	width: 100%;
  .legend-panel {
    width: 45%;
    height: 100%;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      > div {
        width: 70%;
        display: flex;
        align-items: center;
        overflow: hidden;
        text-overflow: ellipsis;
      }
      .text {
        font-size: 14px;
        margin-right: 8px;
        width: 80%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        // word-break: keep-all;
      }
      .num {
        color: #1cfff5;
        font-size: 1rem;
      }
    }
  }
}
</style>