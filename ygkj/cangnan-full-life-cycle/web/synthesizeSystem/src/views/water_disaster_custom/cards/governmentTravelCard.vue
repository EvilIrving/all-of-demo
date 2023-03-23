<!--
 * @Autor: ysl
 * @Date: 2021-08-09 15:42:07
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-14 09:59:11
 * @Description: 出差管理
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/governmentTravelCard.vue
-->
<template>
  <div class="government-travel-card">
    <panel-item
      title="出差管理"
      has-switch
      tc
      @tclick="tclick('出差管理')"
      :action-tabs="['总览', '人次', '金额', '地点', '类型']"
      @change="handleTabChange"
    >
      <template slot="label-description"></template>

      <template slot="title-switch">
        <el-dropdown placement="bottom-start" trigger="click" v-show="activeTab != 0">
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span style="font-size: 1rem">{{ checkMonth }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in monthOption" :key="index">
              <el-radio
                v-model="checkMonth"
                :label="item"
                @change="handleMonth"
                >{{ item }}</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('出差管理')"
          style="margin-left: 10px"
        />
      </template>
      <div class="area-box" v-if="activeTab == 0">
        <!-- 未知bug，饼图无法消失，默认第一个div下出现... -->
        <div class="rain-forecast-card" style="display:none"></div>
        <div class="rain-forecast-card">
          <div class="rainfall-style">
            <p>
              出差总人次 <span > {{ totalCount || 0 }}</span> 人
            </p>
          </div>
          <div class="government-ranking-box">
            <div class="ranking-box" v-for="(item, index) in rankingList" :key="index">
              <div class="table-title">
                <div class="table-title__label" style="align-items:center">
                  <span>{{item.label}}</span>
                  <span style="font-size: 14px"><span style="color: #1CFFF5;font-size:16px;margin-right:3px">{{item.total}}</span>人</span>
                </div>
              </div>
              <div class="content-box">
                <div class="ranking" v-for="(items, indexs) in item.list" :key="indexs">
                  <div :class="['ranking-icon', 'government-icon-top' + (indexs + 1) ]"></div>
                  <div>
                    <div>{{items.key}}</div>
                    <span style="font-size: 14px"><span style="color: #1CFFF5;font-size:16px;margin-right:3px">{{items.value}}</span>人</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="area-box" v-if="activeTab == 1">
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
      <div class="is-water-box" v-if="activeTab == 2">
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
			<div class="chart-panel pie-box" v-if="activeTab == 3">
        <div id="oaAreaPie" style="width:50%;height:100%" ref="oaAreaPie"></div>
					<div class="legend-panel">
					<div
						class="legend-item"
						v-for="(item, index) in barData.area"
						:key="index"
					>
						<div>
							<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
							<span class="text">{{ item.label }}</span>
						</div>
						<span class="num">{{ item.value }}次</span>
					</div>
      	</div>
      </div>
			<div class="chart-panel pie-box" v-if="activeTab == 4">
        <div id="oaTypePie" style="width:50%;height:100%" ref="oaTypePie"></div>
				<div class="legend-panel">
					<div
						class="legend-item"
						v-for="(item, index) in barData.type"
						:key="index"
					>
						<div>
							<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
							<span class="text">{{ item.label }}</span>
						</div>
						<span class="num">{{ item.value }}次</span>
					</div>
      	</div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import { oaApi } from "@/api/governmentOaApi";
import { generateDataZoom } from "./utils/chart.js";

import * as echarts from "echarts"; // echart插件

export default {
  name: "governmentTravelCard",
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

      monthOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      checkMonth: "",
      totalCount: 0,
      rankingList: [],
      barData1: {
        columns: ["deptName", "peopleNum"],
        rows: [],
      },
      barData2: {
        columns: ["deptName", "totalCost"],
        rows: [],
      },
      barData: {
        area: [
					{ label: "市内", name: "areaInNumTotal", value: 0 },
					{ label: "市外", name: "areaOutNumTotal", value: 0 }
				],
        type: [
					{ label: "会议出差", name: "meetTrainCount", value: 0 },
					{ label: "其他出差", name: "otherCount", value: 0 }
				],
      },
			totalLength: 0,
      barOption: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["人"],
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
          barWidth: 10,
          xAxis: {
            axisLabel: {
              interval: 0,
              fontSize: this.$commonSetting.chartFontSize,
							formatter: function (value) {
                var ret = "";
          	    var maxLength = 5;//每项显示文字个数  
                var valLength = value.length;//X轴类目项的文字个数  
                var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数  
                if ( rowN > 1 ) {
                  for (var i = 0; i < rowN; i++) {
                    var temp = "";//每次截取的字符串  
                    var start = i * maxLength;//开始截取的位置  
                    var end = start + maxLength;//结束截取的位置  
                    temp = value.substring(start, end) + "\n";
                    ret += temp; //凭借最终的字符串  
                	}
                	return ret;
                } else {
                  return value;
                }
              },
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
      travelType: [
        { label: "出差人次", value: 5},
        { label: "报销金额", value: 5},
        { label: "出差地点", value: 3},
        { label: "出差类型", value: 2},
      ],
    };
  },
  created() {
    this.getmonthOption();
  },
  beforeDestroy() {
      this.$store.commit('warnData', true);
    },
  mounted() {
    this.$nextTick(()=>{
      this.$store.commit('warnData', false);
    })
		this.getOaOverView()
    this.getOaOverView("1")
    this.getBusinessApplyOverViewComponent()
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetGovernmentTravelCount"]),
    // 获取出差管理数据
    async getOaOverView(cStation) {
      let type = this.activeTab != 0 ? this.travelType[this.activeTab - 1].value : 5
      let opt = {
        type: cStation || type,
        moon: Number(this.checkMonth.split(' ')[0]),
      };
      await oaApi.getOaOverView(opt).then((res) => {
        if( cStation ) {
          // 获取c位数据
          this.SetGovernmentTravelCount({
            peopleTotal: res.data.peopleTotal,
            costTotal: res.data.costTotal
          });
        }
				this.activeTab == 1 || this.activeTab == 2 ? this[`barData${this.activeTab}`].rows = res.data.list
				: ""
				if( this.activeTab == 3 ) { 
					this.barData.area[0].value = res.data.areaInNumTotal
					this.barData.area[1].value = res.data.areaOutNumTotal
					this.totalLength = res.data.areaInNumTotal + res.data.areaOutNumTotal
				} 
				if( this.activeTab == 4 ) { 
					this.barData.type[0].value = res.data.meetTrainCount
					this.barData.type[1].value = res.data.otherCount
					this.totalLength = res.data.meetTrainCount + res.data.otherCount
				} 
      });
			this.activeTab == 3 || this.activeTab == 4 ? await this.areaTypePie() : ""
			switch(this.activeTab) {
				case 1 : this.$refs["chart1"].echarts.resize();
					break;
				case 2 : this.$refs["chart2"].echarts.resize();
					break;
			}
    },
    async getBusinessApplyOverViewComponent() {
      let opt = {
        type: 4,
        moon: Number(this.checkMonth.split(' ')[0]),
      };
      this.rankingList = []
      await oaApi.businessApplyOverViewComponent(opt).then((res) => {
        let data = res.data
        this.totalCount = data.peopleTotal
        for(let item of data.nearThreeMouth){
          this.rankingList.push({
            label: `${item.moon}月出差总人次`, 
            total: item.total, 
            list: item.list
          })
        }
      })
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
			this.$emit("openDialog", "出差管理")
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
			this.barOption.setting.yAxisName[0] = this.activeTab == 1 ? "人" : "元"
      this.activeTab == 0 ? this.getBusinessApplyOverViewComponent() : this.getOaOverView();
      
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
      let length = this.activeTab == 1 ? this.barData1.rows.length : this.barData2.rows.length
      options.dataZoom = generateDataZoom(4, length);
      return options;
    },
		
    // 出差地点/出差类型 饼图
    areaTypePie() {
			let str = this.activeTab == 3 ? "oaAreaPie" : "oaTypePie"
      var chartDom = document.getElementById(str);
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
            color: "#15E2DB", // 0% 处的颜色
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
            color: "#008fe8", // 0% 处的颜色
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
            data: this.activeTab == 3 ? this.barData.area : this.barData.type,
            color: [color0, color1 ],
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 获取月份选择下拉内容
    getmonthOption() {
      let date = new Date();
      let m = date.getMonth();
      for (let i = 1; i <= 12; i++) {
        this.monthOption.push(i + " 月");
      }
      this.checkMonth = m + " 月";
    },
    // 下拉月份选择后
    handleMonth() {
      this.getOaOverView();
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.government-travel-card {
  width: 100%;
  height: 100%;
  .area-box,
  .is-water-box {
    width: 100%;
    height: calc(100% - 34px);
    overflow: hidden;
  }
  .is-water-box {
    display: flex;
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
.government-travel-card .panel-item .panel-item__content--tabs .tab-button,
.government-travel-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.government-travel-card .panel-item .panel-item__content--tabs .tab-button.active,
.government-travel-card .panel-item .panel-item__title--tabs .tab-button.active {
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
}
.icon-color-1 {
  @include chart-icon();
  background: #15E2DB;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.chart-panel {
  height: 200px;
}
.pie-box {
  display: flex;
  justify-content: space-around;
  align-items: center;
	width: 100%;
  .legend-panel {
    width: 40%;
    .legend-item {
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      .text {
        font-size: 14px;
        margin-right: 10px;
      }
      .num {
        color: #1cfff5;
        font-size: 1rem;
      }
    }
  }
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.government-ranking-box {
  height: calc(100% - 2.3rem);
  overflow: auto;
  margin-top: 6px;
	&::-webkit-scrollbar-thumb:vertical {
		background-color: #1ef8f2;
	}
  &::-webkit-scrollbar {
    width: 3px;
    border-radius: 2px;
  }
}
.ranking-box {
  background-color: rgba(28, 255, 245, 0.08);
  padding: 8px 12px 7px;
  margin: 6px 0;
  &:first-child {
    margin-top: 0;
  }
  &:last-child {
    margin-bottom: 0;
  }
  .table-title__label {
   font-size: 16px; 
  }
  .content-box {
    display: flex;
    justify-content: space-between;
    margin-top: 4px;
  }
  .ranking {
    display: flex;
width: 33%;
    & > div {
      font-size: 14px;
      &:last-child {
        margin-left: 10px;
        div {
          margin-bottom: 4px;
        }
      }
    }
  }
}
.rain-forecast-card {
  height: 100%;
  padding: 5px 0 0;
  .rainfall-style {
    @include flexbox;
    @include flexJC(center);
    padding: 5px 0;
    // margin-top: 5px;
    // height:18%;
  }
  ::v-deep .panel-item {
    .panel-item__content {
      @include flexbox;
      @include flexflow(column, nowrap);
      @include flexJC(space-around);
    }
  }

}
</style>