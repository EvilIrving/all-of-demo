<!--
 * @Author: wpf
 * @Date: 2021-07-15 09:47:17
 * @LastEditTime: 2021-07-15 16:34:05
 * @LastEditors: Please set LastEditors
 * @Description: 水质监控卡片
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="water-quality-card">
    <panel-item
      title="水质监控"
      has-switch
      tc
      @tclick="tclick('水质监控')"
      :action-tabs="['水文站点','国控断面','省控断面']"
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
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水质')"
          style="margin-left: 10px"
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
      <div class="iarea-box" v-if="activeTab == 1">
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
      <div class="iarea-box" v-if="activeTab == 2">
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
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "StagnantWaterCard",
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
      jsbtn: false, // 落点
      activeTab: 0, // 0按行政区划 1按是否有积水
      pointList: [],
      barData: {
        columns: ["name", "data"],
        rows: [{name:'1月',data:62},{name:'2月',data:52}],
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
    };
  },
  mounted() {
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
    handleTabChange(val) {
      this.activeTab = val;
    },
    // 获取点位
    async getWaterLoggingList() {
      let res = await disasterPreventionApi.waterLoggingList();
      this.pointList = res.data;
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
.water-quality-card{
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