<!--
 * @Autor: ysl
 * @Date: 2021-08-11 09:55:26
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-14 13:44:50
 * @Description: 水源保障
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/waterSecurityCard.vue
-->
<template>
  <div class="water-security-card">
    <panel-item title="水源保障" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click="handleMapNet('sldbtn')"
          :class="[
            sldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水源保障')"
        />
      </template>

      <div class="area-box">
        <ve-histogram
          ref="chart1"
          height="100%"
          width="100%"
          :data="barData"
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
import PanelItem from "../components/PanelItem.vue";
import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "WaterSecurityCard",
  layerName: "focuLineLayer",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,

      hwbtn: false,
      barData: {
        columns: ["name", "supplyAbility" ],
        rows: [],
      },
      barOption: {
        grid: {
          top: 40,
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["天"],
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

      sldbtn: false, //水源地点位展示标记
      reservoiData: {},
      reservoiList: [],
    };
  },
  computed: {
    ...mapGetters([
      "ScreenMap",
      "currentSelectArea",
      "previewModule",
      "formatterCurrentTime",
    ]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },
  created() {
  },
  mounted() {
    this.$nextTick(() => {
      this.waterGuaranteeRsvrWatSrcTable();
      this.getCenterData();
      this.timer = setInterval(() => {
        this.waterGuaranteeRsvrWatSrcTable();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterSecurityCount"]),

    // 获取水源保障统计数据
    async waterGuaranteeRsvrWatSrcTable() {
      let opt = {};
      await waterResourceApi.waterGuaranteeRsvrWatSrcTable(opt).then((res) => {
          this.barData.rows = res.data.map(item => {
            item.supplyAbility = item.supplyAbility || 0
            return item
          });
          this.reservoiList = res.data
      });
      this.$refs["chart1"].echarts.resize();
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {};
      await waterResourceApi.rsvrWatSrc4CWindow(opt).then((res) => {
        this.SetWaterSecurityCount(res.data);
      });
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
			this.$emit("openDialog", "水源保障")
    },
    // 地图落点
    handleMapNet(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require("@/assets/images/shuiyuandi-ld-5.png"),
        srcs: require("@/assets/images/shuiyuandi-ld-juhe-5.png"),
      };
        let normalSrc = require("@/assets/images/shuiku-linshi.png");
        let pointList = this.reservoiList.map((point) => {
        let imgSrc = require("@/assets/images/legend/shuiku-juhe.png");
        if (point.overLimitVal != null && point.overLimitVal > 0) {
          imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
        }
        return {
          name: point.name,
          type: "水源保障",
          lng: Number(point.longitude),
          lat: Number(point.latitude),
          code: point.code,
          supplyAbility: point.supplyAbility,
          props: {},
          src: normalSrc,
          scale: point.engType,
          _type: "水库",
          area: point.areaName,
          isP: point.protectArea,
          target: point.targetQuality,
          warn: point.volumeWarn,
        };
      });
      if (this[btnType]) {
        this.ScreenMap.drawClusterPoint(pointList, style, "areaPointLayer5");
      } else {
        this.ScreenMap.drawClusterPoint([], {}, "areaPointLayer5");
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
          markPoint: { // 设置最大值和最小值
            symbol: "circle",
            symbolSize: 1,
            data: [
              {
                type: "max",
                // name: "最大",
              },
              {
                type: "min",
                // name: "最小",
              },
            ],
            label: {
              show: true,
              formatter: '{b}\n{c}',
              color: "#1EF8F2",
              fontSize: 18,
              position: "top"
            }
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
      options.dataZoom = generateDataZoom(4, this.barData.rows.length);
      return options;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-security-card {
  height: 100%;

  .icon-sty {
    margin-right: 10px;
  }

  .area-box {
    width: 100%;
    height: 100%;
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
</style>

<style lang="scss">
.water-security-card .panel-item .panel-item__content--tabs .tab-button,
.water-security-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-security-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-security-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
