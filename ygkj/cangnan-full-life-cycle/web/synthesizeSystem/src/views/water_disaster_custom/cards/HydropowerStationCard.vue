<!--
 * @Author: wpf
 * @Date: 2021-07-15 09:47:17
 * @LastEditTime: 2021-07-15 16:34:05
 * @LastEditors: Please set LastEditors
 * @Description: 水电站 卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="water-quality-card">
    <panel-item
      title="水电站"
      has-switch
      tc
      @tclick="tclick('水电站')"
      :action-tabs="['按装机容量', '按行政区划']"
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
          @click.stop="openDetailsDialog('水电站')"
          style="margin-left: 10px"
        />
        <i 
          @click="goToAmap('水电站')" 
          title="一张图" 
          class="icon-yizhangtu icon-sty" 
          style="margin-left: 10px"
        />
      </template>

      <div style="display:flex;flex-direction:column">
        <!-- 分类统计 -->
        <div class="content-box">
          <div v-if="activeTab == 0">
            <div class="count-box">
              水电站总数 <span>{{ stationCount }}</span> 座
            </div>
            <div class="count-list">
              <div
                class="count-list-item"
                v-for="(item, i) in stationList"
                :key="i"
              >
                <div class="name">
                  <i class="icon-diamonds"></i>{{ item.name }}
                </div>
                <div class="value">
                  <span>{{ item.value }}</span> 座
                </div>
              </div>
            </div>
          </div>
          <div v-if="activeTab == 1">
            <ve-histogram
              ref="chart"
              height="200px"
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
        </div>

        <!-- 按达标率排行 -->
        <sub-title-click-card title="按达标率排行" type="radio">
          <div slot="title-operate">
            <el-date-picker
              v-model="checkDate"
              type="month"
              placeholder="选择月"
              :clearable="false"
              value-format="yyyy-MM"
              @change="rateOrderByMoon(1)"
            >
            </el-date-picker>
          </div>
          <div class="content-box rank-box">
            <div class="chart-panel">
              <echartInstrument
                :seriesData="gaugeData"
                :extraOption="instrumentOption"
              ></echartInstrument>
              <span class="text">平均达标率</span>
            </div>
            <div class="rank-list">
              <p class="title">
                <i class="icon icon-left"></i>
                达标率排行
                <i class="icon icon-right"></i>
              </p>
              <div class="list-panel">
                <p class="list-item" v-for="(item, i) in rankList" :key="i">
                  <span class="name">{{ item.key }}</span>
                  <i class="line"></i>
                  <span class="value">{{ item.value * 100 }}%</span>
                </p>
              </div>
            </div>
          </div>
        </sub-title-click-card>

        <!-- 按在线率排行 -->
        <sub-title-click-card title="按在线率排行" type="radio">
          <div slot="title-operate">
            <el-date-picker
              v-model="checkDate1"
              type="month"
              placeholder="选择月"
              :clearable="false"
              value-format="yyyy-MM"
              @change="rateOrderByMoon(2)"
            >
            </el-date-picker>
          </div>
          <div class="content-box">
            <div
              class="online-rank-item"
              v-for="(item, i) in onlineRankList"
              :key="i"
            >
              <i class="rank-icon" :class="'rank-' + (i + 1)"></i>
              <span class="name">{{ item.key }}</span>
              <el-progress
                :percentage="item.value * 100"
                :show-text="false"
                color="#1EF8F2"
                :stroke-width="12"
              ></el-progress>
              <p class="value">
                <span>{{ item.value * 100 }}</span
                >%
              </p>
            </div>
          </div>
        </sub-title-click-card>

        <!-- 按类型统计 -->
        <sub-title-click-card title="按类型统计" type="radio">
          <div class="content-box pie-panel">
            <div id="pieChart" style="width: 50%; height: 100%"></div>
            <div class="legend-panel">
              <div
                class="legend-item"
                v-for="(item, index) in pieChartData"
                :key="index"
              >
                <div>
                  <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                  <span class="text">{{ item.name }}</span>
                </div>
                <span class="num">{{ item.value }}</span>
                <span>座</span>
              </div>
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
import EchartInstrument from "../charts/echart_instrument/EchartInstrument.vue"; // 仪表盘图
import * as echarts from "echarts"; // echart插件
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { generateDataZoom } from "./utils/chart.js";
import { goAmap } from "./utils/common";

export default {
  name: "HydropowerStationCard",
  legendName: "水电站",
  layerName: ["sdzLayer1", "sdzLayer2"],
  components: {
    PanelItem,
    SubTitleClickCard,
    EchartInstrument,
  },
  data() {
    return {
      ldbtn: false, // 落点
      activeTab: 0, //
      pointList: [],

      stationCount: 0, // 水电站总数
      stationList: [
        { name: "10000KW-50000KW", value: 0 },
        { name: "1000KW-10000KW", value: 0 },
        { name: "1000KW以下", value: 0 },
      ],

      barData: {
        columns: ["name", "data"],
        rows: [],
      },
      barOption: {
        dataZoom: {
          type: "inside",
          startValue: 0,
          endValue: 4,
        },
        grid: {
          top: 40,
          bottom: "1%",
          containLabel: true,
        },
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
      },
      pieData: [],

      checkDate: "",
      checkDate1: "",
      //水库图表数据
      gaugeData: [],
      instrumentOption: {},
      rankList: [
        { key: "水电站", value: 1 },
        { key: "水电站", value: 1 },
        { key: "水电站", value: 1 },
      ],
      onlineRankList: [
        { key: "水电站", value: 1 },
        { key: "水电站", value: 1 },
        { key: "水电站", value: 1 },
      ],
      total: 123,
      pieChartData: [
        { name: "混合式", value: 1 },
        { name: "饮水式", value: 1 },
        { name: "坝式(河床)", value: 1 },
        { name: "抽水蓄能", value: 1 },
        { name: "坝式(坝后)", value: 1 },
      ],

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
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
      "AffairId",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  mounted() {
    this.getDate();
    this.pieChartOpt();
    this.countByType(1);
    this.countByType(3);
    this.mainTotalNum();
    this.rateOrderByMoon(1);
    this.rateOrderByMoon(2);
    this.hystDropPoint();
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),

    goToAmap(type){
      goAmap(type);
    },

    // 水电站分类统计
    async countByType(type = 1) {
      let opt = {
        type: type,
      };
      await waterResourceApi.countByType(opt).then((res) => {
        if (type == 1) {
          this.stationList = [
            { name: "10000KW-50000KW", value: res.data.moreTenThousand },
            { name: "1000KW-10000KW", value: res.data.moreThousand },
            { name: "1000KW以下", value: res.data.lessThousand },
          ];
        } else if (type == 2) {
          this.barData.rows = [];
          for (let i in res.data) {
            let obj = {
              name: i,
              data: res.data[i],
            };
            this.barData.rows.push(obj);
          }
          this.$nextTick(() => {
            this.$refs["chart"].echarts.resize();
          });
        } else if (type == 3) {
          this.pieChartData = [
            { name: "混合式", value: res.data[0] || 0 },
            { name: "饮水式", value: res.data[2] || 0 },
            { name: "坝式(河床)", value: res.data[1] || 0 },
            { name: "抽水蓄能", value: res.data[4] || 0 },
            { name: "坝式(坝后)", value: res.data[3] || 0 },
          ];
          this.$nextTick(() => {
            this.pieChartOpt();
          });
        }
      });
    },
    // 获取总数
    async mainTotalNum() {
      await waterResourceApi.mainTotalNum().then((res) => {
        this.stationCount = res.data.totalHyst;
      });
    },
    // 达标率 在线率 排行  1-达标率 2-在线率
    async rateOrderByMoon(type = 1) {
      let year =
        type == 1
          ? Number(this.checkDate.substring(0, 4))
          : Number(this.checkDate1.substring(0, 4));
      let month =
        type == 1
          ? Number(this.checkDate.substring(5, 7))
          : Number(this.checkDate1.substring(5, 7));
      let opt = {
        year: year,
        moon: month,
        type: type,
      };
      await waterResourceApi.rateOrderByMoon(opt).then((res) => {
        if (type == 1) {
          let standardArg = (res.data.standardArg * 100).toFixed(2);
          this.gaugeData = [standardArg];
          this.rankList = res.data.standardTopThree;
          this.$nextTick(() => {
            this.gaugeChartConfig();
          });
        } else if (type == 2) {
          this.onlineRankList = res.data.onlineTopThree;
        }
      });
    },
    // 落点数据
    async hystDropPoint() {
      await waterResourceApi.hystDropPoint().then((res) => {
        this.pointList = res.data;
      });
    },

    // 柱状图
    barConfig(options) {
      let data = options.series[0].data;
      let colorArr = [];
      colorArr = ["#00C1FF", "#72C2FF", "#99E6FF"];
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

      options.grid = {
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
            fontSize: 14,
            position: "top",
            color: "#00C1FF",
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
      options.dataZoom = generateDataZoom(5, this.barData.rows.length);
      return options;
    },
    // 仪表盘
    gaugeChartConfig() {
      const _this = this;
      this.instrumentOption = {
        tooltip: {
          show: true,
          formatter: function () {
            return (
              '<span style="font-size:14px">平均达标率' +
              _this.gaugeData +
              "%</span>"
            );
          },
          confine: true,
        },
      };
    },
    // 饼图
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChart");
      var myChart = echarts.init(chartDom);
      var option;
      let colorList = ["#008fe8", "#00ebc7", "#19af47", "#eec80b", "#e89100"];
      let color = [];
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
                formatter: this.total + "座\n总数",
              },
            },
            labelLine: {
              show: false,
              trigger: "item",
              confine: true,
            },
            data: this.pieChartData,
            color: color,
          },
        ],
      };
      option && myChart.setOption(option);
    },
    // 落点
    drawPoint(btn) {
      this[btn] = !this[btn];
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sdz-ld-jh-1.png"),
        src: require("@/assets/images/sdz-ld-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/sdz-ld-jh-2.png"),
        src: require("@/assets/images/sdz-ld-2.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.hystName,
          type: "水电站",
          lng: point.hystLong,
          lat: point.hystLat,
          props: {
            行政区划: `${point.hystCode || "-"}`,
            水电站类型: `${point.hystType || "-"}`,
            装机流量: `${point.insCap || "-"}m³/s`,
          },
          hystCode: point.hystCode,
        };
        if (point.equipmentOnline == "1") {
          pointList1.push(obj);
        } else if (point.equipmentOnline == "0") {
          pointList2.push(obj);
        }
      }
      if (this[btn]) {
        this.map.drawClusterPoint(pointList1, style1, "sdzLayer1");
        this.map.drawClusterPoint(pointList2, style2, "sdzLayer2");
        this.$emit("legend-change", "水电站", {
          label: "水电站",
          children: [
            {
              label: "在线",
              icon: "legend-sdz-1",
            },
            {
              label: "离线",
              icon: "legend-sdz-2",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "sdzLayer1");
        this.map.drawClusterPoint([], {}, "sdzLayer2");
        this.$emit("legend-change", "水电站", {});
      }
    },
    // 获取上一月
    getDate() {
      const date = new Date();
      const y = date.getFullYear();
      const m =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      this.checkDate = `${y}-${m - 1}`;
      this.checkDate1 = `${y}-${m - 1}`;
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
    handleTabChange(val) {
      this.activeTab = val;
      if (val == 0) this.countByType(1);
      if (val == 1) this.countByType(2);
    },
  },
};
</script>
<style lang="scss" scoped>
.content-box {
  margin-bottom: 20px;
}
.count-box {
  line-height: 40px;
  text-align: center;
  margin-top: 15px;
  font-size: 16px;
  background-image: url(~assets/images/sygs-count-bg.png);
  background-size: 100% 100%;
  span {
    color: #1ef8f2;
    font-size: 20px;
  }
}
.count-list {
  &-item {
    margin-top: 10px;
    padding: 10px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    background-image: linear-gradient(
      90deg,
      #274e61 0%,
      rgba(249, 249, 249, 0) 100%
    );
    i {
      margin-right: 8px;
      vertical-align: middle;
    }
    .value span {
      color: #1ef8f2;
      font-size: 18px;
    }
  }
}

.rank-box {
  height: 150px;
  display: flex;
  justify-content: space-between;
  .chart-panel,
  .rank-list {
    flex: 1;
  }
  .chart-panel {
    text-align: center;
    .text {
      font-size: 15px;
      position: relative;
      bottom: 20px;
    }
  }
  .rank-list {
    color: #1ef8f2;
    font-size: 16px;
    border-left: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
    .title {
      text-align: center;
      .icon {
        width: 35px;
        height: 2px;
        display: inline-block;
        vertical-align: middle;
      }
      .icon-left {
        background-image: linear-gradient(
          64deg,
          rgba(0, 193, 255, 0) 5%,
          #00c1ff 100%
        );
      }
      .icon-right {
        background-image: linear-gradient(
          64deg,
          #00c1ff 5%,
          rgba(0, 193, 255, 0) 100%
        );
      }
    }
    .list-panel {
      margin: 20px 15px;
      .list-item {
        margin-bottom: 24px;
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .name {
        width: 70px;
      }
      .line {
        display: inline-block;
        height: 1px;
        width: 45px;
        border: 1px dotted rgba(30, 248, 242, 0.2);
        margin: 0 5px;
      }
      .value {
        color: #fff;
      }
    }
  }
}

.online-rank-item {
  display: flex;
  justify-content: space-between;
  // line-height: 45px;
  margin-bottom: 24px;
  align-items: center;
  .rank-icon {
    background-repeat: no-repeat;
    display: inline-block;
    width: 24px;
    height: 24px;
    background-size: 100% 100%;
  }
  .el-progress {
    flex: 1;
    ::v-deep.el-progress-bar__outer {
      background-color: rgba($color: #000000, $alpha: 0.3);
    }
  }
  .name {
    width: 70px;
    margin: 0 12px 0 8px;
    font-size: 15px;
    line-height: 22px;
  }
  .value {
    color: #1ef8f2;
    font-size: 16px;
    margin-left: 8px;
    span {
      font-size: 28px;
    }
  }
}

.pie-panel {
  height: 150px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-top: 20px;
  .legend-panel {
    width: 60%;
    margin-left: 30px;
    .legend-item {
      float: left;
      line-height: 25px;
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      .text {
        display: inline-block;
        width: 60px;
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

.rank-1 {
  background-image: url(~assets/images/rank-1.png);
}
.rank-2 {
  background-image: url(~assets/images/rank-2.png);
}
.rank-3 {
  background-image: url(~assets/images/rank-3.png);
}

.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #008fe8;
}
.icon-color-2 {
  @include chart-icon();
  background: #00ebc7;
}
.icon-color-3 {
  @include chart-icon();
  background: #19af47;
}
.icon-color-4 {
  @include chart-icon();
  background: #eec80b;
}
.icon-color-5 {
  @include chart-icon();
  background: #e89100;
}

::v-deep .el-date-editor {
  width: 115px;
  height: 32px;
}
::v-deep .el-input__inner {
  height: 32px;
}
::v-deep .el-input__icon {
  line-height: 32px;
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