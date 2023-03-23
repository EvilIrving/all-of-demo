<!--
 * @Autor: ysl
 * @Date: 2021-08-09 15:42:07
 * @LastEditors: ysl
 * @LastEditTime: 2021-10-09 11:31:04
 * @Description: 公务用车
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/governmentOfficialCarCard.vue
-->
<template>
  <div class="government-official-car-card">
    <panel-item
      title="公务用车"
      has-switch
      tc
      @tclick="tclick('公务用车')"
      :action-tabs="['总览', '报销次数', '报销金额', '出行方式']"
      @change="handleTabChange"
    >
      <template #label-description></template>

      <template #title-switch>
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
          @click.stop="openDetailsDialog('公务用车')"
          style="margin-left: 10px"
        />
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <!-- 未知bug，饼图无法消失，默认第一个div下出现... -->
        <div class="rain-forecast-card" style="display:none"></div>
        <div class="rain-forecast-card">
          <div class="rainfall-style">
            <div v-for="(item, index) in threeYearsData" :key="index">
              {{ item.year }}年用车总次数 
              <p > <span>{{ item.num || 0 }}</span> 次</p>
            </div>
          </div>
          <div class="government-ranking-box">
            <div class="ranking-box" v-for="(item, index) in rankingList" :key="index">
              <div class="table-title">
                <div :class="[{'table-title__label': !item.index}, 'title-label'] ">
                  <div>
                    <span :class="item.index ? 'rank-icon top'+item.index : ''"></span>
                    {{item.label}}
                  </div>
                  <span style="font-size: 14px"><span style="color: #1CFFF5;font-size:16px;margin-right:3px">{{item.total}}</span>人<span :class="['rank-icon', item.type]"></span></span>
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
						v-for="(item, index) in barData.type"
						:key="index"
					>
						<div>
							<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
							<span class="text">{{ item.label }}</span>
						</div>
						<span class="num">{{ item.value }}辆</span>
					</div>
      	</div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { oaApi } from "@/api/governmentOaApi";
import { generateDataZoom } from "./utils/chart.js";

import * as echarts from "echarts"; // echart插件

export default {
  name: "governmentOfficialCarCard",
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
      activeTab: 0,

      monthOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      checkMonth: "",
      totalCount: 0,
      rankingList: [],
      barData1: {
        columns: ["deptName", "num"],
        rows: [],
      },
      barData2: {
        columns: ["deptName", "totalCost"],
        rows: [],
      },
      barData: {
        type: [],
      },
			totalLength: 0,
      barOption: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["次"],
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
      threeYearsData: [],
    };
  },
  created() {
    this.getmonthOption();
  },
  mounted() {
		this.getOaOverView()
    this.getCarApplyOverViewComponent()
    this.officialCarCStatis()
    this.carApplyThreeYearsData()
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetofficialVehicleManagementCount"]),// 获取近3年数据
    carApplyThreeYearsData(){
      oaApi.carApplyThreeYearsData({}).then(res => {
        this.threeYearsData = res.data
      })
    },
    // 设置C位数据
    async officialCarCStatis() {
      await oaApi.officialCarCStatis().then((res) => {
        // 获取c位数据
          this.SetofficialVehicleManagementCount({
            carNumber: res.data.carNumber,
            expenseMoney: res.data.expenseMoney,
            expenseNumber: res.data.expenseNumber
          });
      })
    },
    // 获取公务用车数据
    async getOaOverView() {
      let opt = {
        moon: Number(this.checkMonth.split(' ')[0]),
      };
      let str = this.activeTab == 1 ? "statisExpenseAccount" : this.activeTab == 2 ? "statisExpenseMoney" : "officialCarStatisway"
      await oaApi[str](opt).then((res) => {
				this.activeTab == 1 || this.activeTab == 2 ? this[`barData${this.activeTab}`].rows = res.data
				: ""
				if( this.activeTab == 3 ) { 
          this.totalLength = 0
          this.barData.type = []
					this.barData.type = res.data.map( item => {
            item.label = item.useModel
            item.value = item.num
            this.totalLength += item.num
            return item
          })
				} 
      });
			this.activeTab == 3 ? await this.areaTypePie() : ""
			switch(this.activeTab) {
				case 1 : this.$refs["chart1"].echarts.resize();
					break;
				case 2 : this.$refs["chart2"].echarts.resize();
					break;
			}
    },
    async getCarApplyOverViewComponent() {
      let opt = {
        month: Number(this.checkMonth.split(' ')[0]),
      };
      this.rankingList = []
      await oaApi.carApplyOverViewComponent(opt).then((res) => {
        let data = res.data
        this.totalCount = data.officeCarTotalNum
        data.list = data.list.sort((a,b) => b.moon - a.moon)
        for(let item of data.list){
          this.rankingList.push({
            moon: item.moon,
            label: `${item.moon}月报销总数`, 
            total: item.total, 
            list: item.list.splice(0,3)
          })
        }
        let arr = data.list.sort((a,b) => b.total - a.total).splice(0,3)
        arr.forEach((item, index) => {
          this.rankingList.map(sItem => {
            item.moon == sItem.moon ? sItem.index = index + 1 : ""
            return sItem
          })
        })
        if(this.rankingList[0].total > this.rankingList[1].total) {
          this.rankingList[0].type = "rank-icon-up"
        } else {
          this.rankingList[0].type = "rank-icon-down"
        }
      });
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
			this.$emit("openDialog", "公务用车")
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", "公务用车");
    },
    handleTabChange(val) {
      this.activeTab = val;
			this.barOption.setting.yAxisName[0] = this.activeTab == 1 ? "次" : "万元"
      this.activeTab == 0 ? this.getCarApplyOverViewComponent() : this.getOaOverView();
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
      var chartDom = document.getElementById("oaAreaPie");
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
            color: "#1CFFF5", // 0% 处的颜色
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
            color: "#19AF47", // 0% 处的颜色
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
            color: "#009AFF", // 0% 处的颜色
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
            color: "#F8E61A", // 0% 处的颜色
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
            color: "#FF7144", // 0% 处的颜色
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
            data: this.barData.type,
            color: [color0, color1, color2, color3, color4 ],
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
.government-official-car-card {
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
.government-official-car-card .panel-item .panel-item__content--tabs .tab-button,
.government-official-car-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.government-official-car-card .panel-item .panel-item__content--tabs .tab-button.active,
.government-official-car-card .panel-item .panel-item__title--tabs .tab-button.active {
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
  background: #19AF47;
}
.icon-color-3 {
  @include chart-icon();
  background: #009AFF;
}
.icon-color-4 {
  @include chart-icon();
  background: #F8E61A;
}
.icon-color-5 {
  @include chart-icon();
  background: #FF7144;
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
  padding: 4px 18px 7px;
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
  .title-label {
    position: relative;
    margin-left: 1.55rem;
    line-height: 2rem;
    font-size: 16px;
    display: flex;
    justify-content: space-between;
    align-items:center;
    &::before {
      left: -1.5875rem;
    }
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
          height: 28px;
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
    justify-content: space-around;
    padding: 5px 0;
    font-size: 14px;;
    span {
      font-size: 14px;
      font-weight: bold;
      color: #1CFFF5;
    }
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