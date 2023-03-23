<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 水库蓄水量（公报）
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterSupplyCard.vue
-->
<template>
  <div class="water-card">
    <panel-item title="水库蓄水量" has-switch tc @tclick="tclick">
      <!-- <template slot="label-description">（公报）</template> -->
      <template slot="title-switch">
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
          title="地图落点"
          @click="handleMapNet('ldbtn')"
          :class="[
            ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('水库蓄水量（公报）')"
        />
        <i
          @click="goToAmap('水库')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>

      <div class="area-box">
        <ve-histogram
          ref="chart"
          height="100%"
          width="100%"
          :data="reserStorageData"
          :settings="reserStorageOption.setting"
          :extend="reserStorageOption.extend"
          :grid="reserStorageOption.grid"
          :data-zoom="reserStorageOption.dataZoom"
          :tooltip-visible="false"
          :legend-visible="false"
          :after-config="barConfig2"
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
import { disasterPreventionApi } from "@/api/RightSildePanel";

export default {
  name: "ReservoirStorageCard",
  layerName: "digReservoir1Layer",
  legendName: "水库蓄水量",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,

      hwbtn: false,
      reserStorageData: {
        columns: ["name", "thisYear", "lastYear"],
        rows: [],
      },
      reserStorageOption: {
        grid: {
          top: "24%",
          bottom: "5%",
          containLabel: true,
        },
        setting: {
          yAxisName: ["亿m³"],
          labelMap: { thisYear: "今年", lastYear: "去年" },
          label: {
            show: true,
            fontSize: this.$commonSetting.chartFontSize,
            position: "top",
            color: "#00C1FF",
            formatter: ({ value }) => Number(value).toFixed(1),
          },
        },
        extend: {
          legend: {
            right: "0%",
            icon: "circle",
            itemWidth: 10,
            textStyle: { color: "#FFF" },
          },
          "textStyle.color": "#fff",
          barWidth: 15,
          yAxis: {
            nameTextStyle: { color: "#ffffff99" },
            max: (value) => {
              return value.max > 1 ? null : 1;
            },
            axisLabel: {
              color: "#fff",
            },
            splitLine: {
              lineStyle: {
                color: "#00C1FF",
                opacity: 0.5,
              },
            },
          },
          "series.0.itemStyle": { color: "#00C1FF" },
          "series.1.itemStyle": { color: "#EEC80B" },
        },
      },

      ldbtn: false, //水库点位展示标记
      reservoiData: {},
      reservoiList: [],

      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
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
    this.getYearOption();
  },
  mounted() {
    this.stats4WrbWaterConsume();
    this.getCenterData();
    this.rsvrProjDrainageStats();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.stats4WrbWaterConsume();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetReserStorageCount"]),

    // 水库蓄水量统计接口
    async stats4WrbWaterConsume() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      let data = [];
      await waterResourceApi.stats4WrbRsvrStore(opt).then((res) => {
        this.checkYear = res.data.year + " 年";
        res.data.selectYearList.forEach((thisY) => {
          let lastObj = res.data.lastYearList.find((lastY) => {
            return thisY.name == lastY.name;
          });
          if (lastObj) {
            let obj = {
              name: thisY.resName,
              thisYear: Number(thisY.waterStore.toFixed(2)),
              lastYear: Number(lastObj.waterStore.toFixed(2)),
            };
            data.push(obj);
          }
        });
        this.reserStorageData.rows = data;
        this.$refs["chart"].echarts.resize();
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {
        year: this.checkYear ? Number(this.checkYear.substring(0, 4)) : "",
      };
      await waterResourceApi.statsCwindow4WrbRsvrStore(opt).then((res) => {
        this.SetReserStorageCount(res.data);
      });
    },

    //获取大中型水库
    async rsvrProjDrainageStats() {
      await disasterPreventionApi.rsvrProjDrainageStats().then((res) => {
        if (res.code == 0) {
          this.reservoiData = res.data;
          this.reservoiList = this.reservoiData.list || [];
        }
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
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    // 地图落点
    handleMapNet(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        src: require("@/assets/images/legend/shuiku-juhe.png"),
        srcs: require("@/assets/images/dianwei-juhe.png"),
      };
      let pointList = this.reservoiList.map((point) => {
        let imgSrc = require("@/assets/images/legend/shuiku-juhe.png");
        if (point.overLimitVal != null && point.overLimitVal > 0) {
          imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
        }
        return {
          name: point.resName,
          type: "水库蓄水量",
          lng: point.lowLeftLong,
          lat: point.lowLeftLat,
          code: point.resCode,
          src: imgSrc,
          props: {},
        };
      });
      if (this[btnType]) {
        this.ScreenMap.drawClusterPoint(pointList, style, "digReservoir1Layer");
        this.$emit("legend-change", "水库", {
          label: "水库",
          clusterLayer:['digReservoir1Layer'],
          children: [
            {
              label: "正常",
              icon: "icon-legend-water-general",
            },
            {
              label: "超警",
              icon: "icon-legend-water-flood",
            },
          ],
        });
      } else {
        this.ScreenMap.drawClusterPoint([], {}, "digReservoir1Layer");
        this.$emit("legend-change", "水库", {});
      }
    },

    // 跳转一张图
    goToAmap(type) {
      this.$router.push(`/hydraulicAmap?selectType=${type}`);
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
      this.stats4WrbWaterConsume();
      this.getCenterData();
    },
    barConfig2(options) {
      let thisYear = options.series[0].data;
      let lastYear = options.series[1].data;
      let colorArr1 = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
      let colorArr2 = ["#06a2d4", "#62d9ff", "#64d9ff"];
      let color1 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr1[0],
          },
          {
            offset: 0.5,
            color: colorArr1[0],
          },
          {
            offset: 0.5,
            color: colorArr1[1],
          },
          {
            offset: 1,
            color: colorArr1[1],
          },
        ],
      };
      let color2 = {
        type: "linear",
        x: 0,
        x2: 1,
        y: 0,
        y2: 0,
        colorStops: [
          {
            offset: 0,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[0],
          },
          {
            offset: 0.5,
            color: colorArr2[1],
          },
          {
            offset: 1,
            color: colorArr2[1],
          },
        ],
      };
      let barWidth = 15;
      let constData1 = [];
      let showData1 = [];
      let constData2 = [];
      let showData2 = [];
      thisYear.filter(function (item) {
        if (item) {
          constData1.push(1);
          showData1.push(item);
        } else {
          constData1.push(0);
          showData1.push({
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
      lastYear.filter(function (item) {
        if (item) {
          constData2.push(1);
          showData2.push(item);
        } else {
          constData2.push(0);
          showData2.push({
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

      options.tooltip = {
        show: true,
        formatter: (data) => {
          return (
            data.name +
            "<br/><span style='font-size: 14px;'>今年：" +
            thisYear[data.dataIndex] +
            "亿m³</span> <br/><span style='font-size: 14px;'>去年：" +
            lastYear[data.dataIndex] +
            "亿m³</span>"
          );
        },
      };

      options.series = [
        {
          z: 1,
          name: "今年",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: thisYear,
          itemStyle: {
            normal: {
              color: color1,
            },
          },
          label: {
            show: true,
            fontSize: 12,
            position: "top",
            color: "#1EF8F2",
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: constData1,
          symbol: "diamond",
          symbolOffset: ["-60%", "55%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color1,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData1,
          symbol: "diamond",
          symbolOffset: ["-92%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr1[2],
              borderWidth: 2,
              color: colorArr1[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 1,
          name: "去年",
          type: "bar",
          barWidth: barWidth,
          barGap: "30%",
          data: lastYear,
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          label: {
            show: true,
            fontSize: 12,
            position: "top",
            color: "#06a2d4",
          },
        },
        {
          z: 2,
          name: "",
          type: "pictorialBar",
          data: constData2,
          symbol: "diamond",
          symbolOffset: ["65%", "50%"],
          symbolSize: [barWidth, 10],
          itemStyle: {
            normal: {
              color: color2,
            },
          },
          tooltip: {
            show: false,
          },
        },
        {
          z: 3,
          name: "",
          type: "pictorialBar",
          symbolPosition: "end",
          data: showData2,
          symbol: "diamond",
          symbolOffset: ["90%", "-50%"],
          symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
          itemStyle: {
            normal: {
              borderColor: colorArr2[2],
              borderWidth: 2,
              color: colorArr2[2],
            },
          },
          tooltip: {
            show: false,
          },
        },
      ];
      options.dataZoom = generateDataZoom(4, this.reserStorageData.rows.length);
      options.legend.selectedMode = false;
      return options;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-card {
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
.water-card .panel-item .panel-item__content--tabs .tab-button,
.water-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>
