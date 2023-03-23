<template>
  <div class="big-card">
    <panel-item title="闸站" hasSwitch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          @click.stop="handleReservoirChange('skbtn')"
          :class="[
            skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
        />
        <i
          title="查看详表"
          @click.stop="openDetailsDialog()"
          class="icon-liebiao icon-sty"
        />
        <i
          @click="goToAmap('闸站')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
      </template>
      <!-- 闸站类型统计-->
      <div class="reservoir-sty">
        <p @click="getList('中型')">
          <span>中型</span>
          <span>
            <span
              style="
                color: #1cfff5;
                font-size: 24px;
                line-height: 24px;
                margin: 0 5px;
              "
            >
              {{ statistic.midSum }}
            </span>
            座
          </span>
        </p>
        <p @click="getList('小（Ⅰ）型')">
          <span>小(1)型</span>
          <span>
            <span
              style="
                color: #1cfff5;
                font-size: 24px;
                line-height: 24px;
                margin: 0 5px;
              "
            >
              {{ statistic.smallSum }}</span
            >
            座
          </span>
        </p>
      </div>
      <div class="area-box">
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
    <SluiceStationDetail
      v-if="detailVisible"
      type="闸站"
      :visible="detailVisible"
      @close="closeDetail"
    ></SluiceStationDetail>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { hydraulicApi } from "@/api/hydraulic_amap";
import { mapGetters, mapActions } from "vuex";
import SluiceStationDetail from "./dialog/SluiceStationDetail.vue";
import { goAmap } from './utils/common';
export default {
  name: "SluiceStationCard",
  layerName: "sluiceStationLayer",
  legendName: "闸站",
  components: {
    PanelItem,
    SluiceStationDetail
  },
  mounted() {
    this.$nextTick(() => {
      this.rsvrProjDrainageStats();
      this.getStats();
      this.getList("");
    });
  },
  data() {
    return {
      skbtn: false, //水库点位展示标记
      //大中型水库统计数据
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
          left: 10,
          containLabel: true,
        },
        setting: {
          yAxisName: ["m³/s"],
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
      resData: [],
      detailVisible: false,
      engScal:"",
      statistic:{
        midSum:0,
        smallSum:0
      }
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
  methods: {
    ...mapActions(["SetDetailDialogParam"]),
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val, data) {
      this.detailVisible = true;
      // this.SetDetailDialogParam({
      //   mediumSized: data,
      //   codeTpye: val,
      //   analyseDialogShow: true,
      // });
    },
    /**
     * 跳转一张图
     */
    goToAmap(type) {
      goAmap(type);
      // this.$router.push(`/hydraulicAmap?selectType=闸站`);
    },

    /**
     * 大中型水库
     */
    async rsvrProjDrainageStats() {
      const opt = {
        type: "闸站"
      }
      await hydraulicApi.projectsDetailList(opt).then((res) => {
        if (res.code == 0) {
          this.resData = res.data;
        }
      });
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
    async getStats() {
      const opt = {
        type: 1
      }
      await hydraulicApi.gateStats4BigScreen(opt).then(res => {
        if (res.code == 0) {
          this.statistic.midSum = res.data[1].value;
          this.statistic.smallSum = res.data[2].value;
        }
      })
    },
    async getList(level) {
      const opt = {
        type: "闸站",
        level: level
      }
      await hydraulicApi.projectsDetailList(opt).then((res) => {
        if (res.code == 0) {
          let rows = [];
          for (let item of res.data) {
            rows.push({ name: item.gateName, data: item.maxFlow });
          }
          this.barData.rows = rows;
          this.$refs["chart"].echarts.resize();
        }
      });
    },

    handleReservoirChange(btnType) {
      this[btnType] = !this[btnType];
      let style = {
        scale: 0.7,
        anchor: [24, 16],
        anchorXUnits: "pixels",
        src: require("@/assets/images/MapPoint/sluicestation.png"),
      };
      let pointList = this.resData.map((point) => {
        let imgSrc = require("@/assets/images/MapPoint/sluicestation.png");
        return {
          name: point.gateName,
          type: "闸站",
          lng: point.longitude,
          lat: point.latitude,
          code: point.gateCode,
          src: imgSrc,
          props: {
            行政区划: `${point.locCounty || '-'}`,
            闸站规模: `${point.engScal || '-'}`,
            管理层级: `${point.managementLevel || '-'}`,
            过闸流量: `${point.maxFlow || '-'} m³/s`,
            装机功率: `${point.singleInsPow || '-'} kW`,
          },
        };
      });
      if (this[btnType]) {
        this.ScreenMap.drawPoint(pointList, style, "sluiceStationLayer");
        this.$emit("legend-change", "闸站", {
          label: "闸站",
          children: [
            {
              label: "正常",
              icon: "icon-legend-sluicestation",
            },
          ],
        });
      } else {
        this.ScreenMap.drawPoint([], {}, "sluiceStationLayer");
        this.$emit("legend-change", "闸站", {});
      }
    },
    closeDetail() {
      this.detailVisible = false;
    }
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.big-card {
  height: 100%;
  // padding: 0 16px;
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  .reservoir-sty {
    cursor: pointer;
    > p {
      line-height: 24px;
      span {
        color: #fff;
        font-size: 15px;
      }
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    // > p:nth-child(1) {
    //   margin-right: 16px;
    //   > span {
    //     color: #1cfff5;
    //   }
    // }
    > p:nth-child(2) {
      background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
  }
  .area-box {
    width: 100%;
    height: calc(100% - 80px);
  }
}
</style>