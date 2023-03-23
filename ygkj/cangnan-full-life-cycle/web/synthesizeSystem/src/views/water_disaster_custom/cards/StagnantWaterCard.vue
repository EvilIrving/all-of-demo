<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 积水卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="stagnant-water-card">
    <panel-item
      title="积水"
      has-switch
      tc
      @tclick="tclick('积水')"
      :action-tabs="['按行政区划', '按是否有积水']"
      @change="handleTabChange"
    >
      <template slot="title-switch">
        <i
          title="地图落点"
          @click="handleStagnantWater('jsbtn')"
          :class="[
            jsbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('积水')"
          style="margin-left: 10px"
        />
        <i
          v-show="!previewModule"
          @click="goToAmap('积水')"
          style="margin-left: 10px"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>

      <div class="area-box" v-if="activeTab == 0">
        <ve-histogram
          ref="chart"
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
      <div class="is-water-box" v-if="activeTab == 1">
        <div class="pie-box">
          <EchartPie
            ref="pie"
            :seriesData="pieData"
            :extraOption="pieOption"
          ></EchartPie>
        </div>
        <div class="pie-legend">
          <div v-for="(item, index) in pieData" :key="index">
            <template v-if="item.name">
              <span>
                <i
                  class="legend-circle"
                  :style="{ background: item.color }"
                ></i>
                {{ item.name }}
              </span>
              <span>
                <span class="legend-num">{{ item.value }}</span>
                个
              </span>
            </template>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import { goAmap } from "./utils/common";
export default {
  name: "StagnantWaterCard",
  layerName: ["pondingLayer","nopondingLayer"],
  legendName: "积水",
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
      jsbtn: false, // 落点
      activeTab: 0, // 0按行政区划 1按是否有积水
      pointList: [],
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
      pieLegend: [
        {
          label: "无积水",
          num: 96,
        },
        {
          label: "积水",
          num: 3,
        },
      ],
      pieOption: {
        tooltip: {
          show: true,
          trigger: "item",
          formatter: function (data) {
            return data.data.name + ":" + "  " + data.data.value + "个";
          },
          confine: true,
          extraCssText: "font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["60%", "90%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: 18,
              },
            },
            labelLine: {
              show: false,
            },
          },
        ],
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
                color: "#1CFFF5", // 0% 处的颜色
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
        ],
      },
      timer:null,
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.getWaterLoggingList();
      this.getWaterLoggingStats({ statsType: 1 }); // 行政区划
      this.getWaterLoggingStats({ statsType: 2 }); // 是否有积水
      this.timer = setInterval(()=>{
        this.getWaterLoggingList();
        this.getWaterLoggingStats({ statsType: 1 });
        this.getWaterLoggingStats({ statsType: 2 });
      },300000);
    });
  },
  beforeDestroy(){
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
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
    // 地图点位
    handleStagnantWater(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/noponding-group.png"),
        src: require("@/assets/images/MapPoint/noponding-point.png"),
      };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/MapPoint/ponding-group.png"),
        src: require("@/assets/images/MapPoint/ponding-point.png"),
      };
      let pondingList = [];
      let nopondingList = [];
      for (let point of this.pointList) {
        let obj = {
          name: point.name,
          type: "积水",
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.id,
          stationCode: point.id || "",
          props:{
            '水位':`${point.waterLevel}m`
          }
        };
        if (point.ponding) {
          pondingList.push(obj);
        } else {
          nopondingList.push(obj);
        }
      }
      if (this[btnType]) {
        this.map.drawClusterPoint(pondingList, style1, "pondingLayer");
        this.map.drawClusterPoint(nopondingList, style, "nopondingLayer");
        this.$emit("legend-change", "积水", {
          label: "积水",
          clusterLayer:['pondingLayer','nopondingLayer'],
          children: [
            {
              label: "无积水",
              icon: "icon-legend-noponding",
            },
            {
              label: "积水",
              icon: "icon-legend-ponding",
            },
          ],
        });
      } else {
        this.map.drawClusterPoint([], {}, "pondingLayer");
        this.map.drawClusterPoint([], {}, "nopondingLayer");
        this.$emit("legend-change", "积水", {});
      }
    },
    goToAmap(type){
      goAmap(type);
    },
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 获取点位
    async getWaterLoggingList() {
      let res = await disasterPreventionApi.waterLoggingList();
      this.pointList = res.data.list;
    },
    // 获取积水统计数据
    async getWaterLoggingStats(opt) {
      let res = await disasterPreventionApi.waterLoggingStats(opt);
      if (opt.statsType == 1) {
        // 按行政区划统计
        if (res.code == 0) {
          let rows = [];
          for (let item of res.data.data) {
            rows.push({ name: item.key, data: item.value });
          }
          this.barData.rows = rows;
          this.$refs["chart"].echarts.resize();
        }
      } else {
        // 按是否有积水统计
        if (res.code == 0) {
          let info = res.data.data;
          this.pieData = [
            {
              value: info.noWater ? info.noWater : 0,
              name: "无积水",
              total: info.total,
              color: "#1CFFF5",
            },
            {
              name: "积水",
              value: info.hasWater ? info.hasWater : 0,
              total: info.total,
              color: "#FF7144",
            },
            {
              value: 0,
              name: "",
            },
          ];
        }
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
      return options;
    },
  },
};
</script>
<style lang="scss" scoped>
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