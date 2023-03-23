<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-07-06 22:04:35
 * @LastEditors: Please set LastEditors
 * @Description: 美丽河湖
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterSupplyCard.vue
-->
<template>
  <div class="water-card">
    <panel-item title="美丽河湖" has-switch tc @tclick="tclick">
      <template slot="title-switch">
        <i
          title="地图落点"
          :class="[
            ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1',
            'icon-sty',
          ]"
          @click="drawPoint()"
        />
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('美丽河湖')"
        />
      </template>

      <div class="area-box">
        <div class="chart-panel">
          <div class="count-panel">
            <div
              :class="['count-item', active == '1' ? 'active' : '']"
              @click="tabChange('1')"
            >
              <p>工程总数</p>
              <p class="num">{{ count.projectTotalNum }}</p>
            </div>
            <div class="count-line"></div>
            <div
              :class="['count-item', active == '2' ? 'active' : '']"
              @click="tabChange('2')"
            >
              <p>项目在建</p>
              <p class="num">{{ count.underConstructionNum }}</p>
            </div>
            <div class="count-line"></div>
            <div
              :class="['count-item', active == '3' ? 'active' : '']"
              @click="tabChange('3')"
            >
              <p>已完工</p>
              <p class="num">{{ count.completeNum }}</p>
            </div>
          </div>
          <div class="pie-box">
            <div id="pieChartBeauty" style="width: 50%; height: 100%"></div>
            <div class="legend-panel">
              <div
                class="legend-item"
                v-for="(item, index) in pieDataBeau"
                :key="index"
              >
                <div>
                  <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                  <span class="text">{{ item.name }}</span>
                </div>
                <span class="num">{{ item.value }} 个</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { mapGetters, mapActions } from "vuex";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

export default {
  name: "beautifulRiverCard",
  layerName: ["mlhhLayer1", "mlhhLayer2", "mlhhLayer3", "mlhhLayer4"],
  legendName: "美丽河湖",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      ldbtn: false,
      active: "1",

      count: {},
      total: 0,
      pieDataBeau: [],

      pointList1: [],
      pointList2: [],
      pointList3: [],
      pointList4: [],
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
  mounted() {
    this.$nextTick(() => {
      this.getPieChartDate();
      this.projectStatic();

      this.getBeautifulRevierList();
      this.AttSmallMediumRiver();
      this.AttBeautVillage();
      this.AttRuralPond();
      this.timer = setInterval(() => {
        this.getPieChartDate();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterUseCount"]),

    // 统计
    async projectStatic() {
      await waterResourceApi.projectStatic().then((res) => {
        this.count = res.data;
      });
    },
    // 饼图 统计
    async getPieChartDate() {
      let opt = {
        type: this.active,
      };
      await waterResourceApi.projectStatic(opt).then((res) => {
        this.pieDataBeau = [
          { name: "美丽河湖建设", value: res.data.beautLakeNum },
          { name: "中小河流治理", value: res.data.smallMediumRiverNum },
          { name: "水美乡镇建设", value: res.data.beautVillageNum },
          { name: "农村池塘整治", value: res.data.ruralPondNum },
        ];
        if (this.active == "1") {
          this.total = res.data.projectTotalNum;
        } else if (this.active == "2") {
          this.total = res.data.underConstructionNum;
        } else if (this.active == "3") {
          this.total = res.data.completeNum;
        }
        this.$nextTick(() => {
          this.pieChartOpt();
        });
      });
    },
    // 落点数据 美丽河湖
    async getBeautifulRevierList() {
      await waterResourceApi.getBeautifulList().then((res) => {
        this.pointList1 = res.data.list;
      });
    },
    // 落点数据 中小河流
    async AttSmallMediumRiver() {
      await waterResourceApi.AttSmallMediumRiver().then((res) => {
        this.pointList2 = res.data.list;
      });
    },
    // 落点数据 水美乡镇
    async AttBeautVillage() {
      await waterResourceApi.AttBeautVillage().then((res) => {
        this.pointList3 = res.data.list;
      });
    },
    // 落点数据 农村池塘
    async AttRuralPond() {
      await waterResourceApi.AttRuralPond().then((res) => {
        this.pointList4 = res.data.list;
      });
    },

    // 落点
    drawPoint() {
      this.ldbtn = !this.ldbtn;
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/mlhh-ld-jh-1.png"),
        src: require("@/assets/images/mlhh-ld-1.png"),
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/mlhh-ld-jh-2.png"),
        src: require("@/assets/images/mlhh-ld-2.png"),
      };
      let style3 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/mlhh-ld-jh-3.png"),
        src: require("@/assets/images/mlhh-ld-3.png"),
      };
      let style4 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: "pixels",
        srcs: require("@/assets/images/mlhh-ld-jh-4.png"),
        src: require("@/assets/images/mlhh-ld-4.png"),
      };
      let pointList1 = [];
      let pointList2 = [];
      let pointList3 = [];
      let pointList4 = [];
      for (let point of this.pointList1) {
        let obj = {
          name: point.sectionName,
          type: "美丽河湖",
          lng: Number(point.startLng),
          lat: Number(point.startLat),
          props: {
            工程状态: `${point.projectStatus || "-"}`,
            河段名称: `${point.sectionName || "-"}`,
            起点位置: `${point.startLocation || "-"}`,
            终点位置: `${point.endLocation || "-"}`,
          },
          obj: point,
          activeTab: 1,
        };
        pointList1.push(obj);
      }
      for (let point of this.pointList2) {
        let obj = {
          name: point.sectionName,
          type: "美丽河湖",
          lng: Number(point.startLng),
          lat: Number(point.startLat),
          props: {
            工程状态: `${point.projectStatus || "-"}`,
            河段名称: `${point.sectionName || "-"}`,
            起点位置: `${point.startLocation || "-"}`,
            终点位置: `${point.endLocation || "-"}`,
          },
          obj: point,
          activeTab: 2,
        };
        pointList2.push(obj);
      }
      for (let point of this.pointList3) {
        let obj = {
          name: point.sectionName,
          type: "美丽河湖",
          lng: Number(point.startLng),
          lat: Number(point.startLat),
          props: {
            工程状态: `${point.projectStatus || "-"}`,
            河段名称: `${point.sectionName || "-"}`,
            起点位置: `${point.startLocation || "-"}`,
            终点位置: `${point.endLocation || "-"}`,
          },
          obj: point,
          activeTab: 3,
        };
        pointList3.push(obj);
      }
      for (let point of this.pointList4) {
        let obj = {
          name: point.poudName,
          type: "美丽河湖",
          lng: Number(point.lng),
          lat: Number(point.lat),
          props: {
            工程状态: `${point.projectStatus || "-"}`,
            池塘名称: `${point.poudName || "-"}`,
          },
          obj: point,
          activeTab: 4,
        };
        pointList4.push(obj);
      }
      // console.log(1111, pointList1);
      // console.log(2222, pointList2);
      // console.log(3333, pointList3);
      // console.log(4444, pointList4);
      if (this.ldbtn) {
        this.$emit("legend-change", "美丽河湖", {
          label: "美丽河湖",
          children: [
            {
              label: "美丽河湖建设",
              icon: "mlhh-legend-1",
            },
            {
              label: "中小河流治理",
              icon: "mlhh-legend-2",
            },
            {
              label: "水美乡镇建设",
              icon: "mlhh-legend-3",
            },
            {
              label: "农村池塘整治",
              icon: "mlhh-legend-4",
            },
          ],
        });
        this.map.drawClusterPoint(pointList1, style1, "mlhhLayer1");
        this.map.drawClusterPoint(pointList2, style2, "mlhhLayer2");
        this.map.drawClusterPoint(pointList3, style3, "mlhhLayer3");
        this.map.drawClusterPoint(pointList4, style4, "mlhhLayer4");
      } else {
        this.$emit("legend-change", "美丽河湖", {});
        this.map.drawClusterPoint([], {}, "mlhhLayer1");
        this.map.drawClusterPoint([], {}, "mlhhLayer2");
        this.map.drawClusterPoint([], {}, "mlhhLayer3");
        this.map.drawClusterPoint([], {}, "mlhhLayer4");
      }
    },
    // 饼图
    pieChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("pieChartBeauty");
      var myChart = echarts.init(chartDom);
      var option;
      let color = [];
      let colorList = ["#E82E00", "#E89100", "#19AF47", "#008FE8", "#724CE8"];
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
        grid: {
          top: "20%",
          left: "0%",
          right: "0%",
          bottom: "0%",
          containLabel: true,
        },

        legend: {
          show: false,
        },
        tooltip: {
          trigger: "item",
          formatter: function (data) {
            return (
              "<span style='font-size: 14px;'>" +
              data.name +
              "</span><br/><span style='font-size: 14px;color: #1CFFF5;'>" +
              data.value +
              "</span> 个"
            );
          },
          confine: true,
          // extraCssText: "width:160px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "90%"],
            centen: [0, 0],
            avoidLabelOverlap: false,
            label: {
              show: true,
              normal: {
                show: true,
                position: "center",
                formatter: this.total + "个\n总数",
                color: "#fff",
                fontSize: this.$commonSetting.chartFontSize,
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieDataBeau,
            color: color,
          },
        ],
      };
      option && myChart.setOption(option);
    },

    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    // 打开详情弹框点击事件
    openDetailsDialog(val) {
      this.SetDetailDialogParam({
        mediumSized: "",
        codeTpye: val,
        analyseDialogShow: true,
      });
    },
    tabChange(val) {
      this.active = val;
      this.getPieChartDate();
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-card {
  height: 100%;

  .area-box {
    height: calc(100% - 10px);
  }
  .chart-panel {
    height: 100%;
  }

  .count-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 10px 0;
    .count-item {
      font-size: 14px;
      color: #fff;
      padding: 10px 15px;
      cursor: pointer;
      .num {
        color: #1cfff5;
        font-size: 16px;
        margin-top: 8px;
        text-align: center;
      }
      &.active {
        background: linear-gradient(to bottom, #1cfff485, #1cfff485) left top
            no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) left top no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) right top no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) right top no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) left bottom no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) left bottom no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) right bottom
            no-repeat,
          linear-gradient(to bottom, #1cfff485, #1cfff485) right bottom
            no-repeat;
        background-size: 12px 2px, 2px 12px;
        border-radius: 3px;
        background-color: rgba(28, 255, 245, 0.1);
      }
    }
    .count-line {
      width: 1px;
      height: 40px;
      background-color: rgba(255, 255, 255, 0.4);
    }
  }

  .pie-box {
    height: calc(100% - 70px);
    display: flex;
    justify-content: space-around;
    align-items: center;
    // margin-top: 20px;

    .legend-panel {
      width: 60%;
      margin-left: 20px;
      .legend-item {
        float: left;
        line-height: 25px;
        display: flex;
        justify-content: space-between;
        .text {
          display: inline-block;
          width: 100px;
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

  .icon-circle {
    width: 12px !important;
    height: 12px !important;
    border-radius: 50%;
  }

  .icon-color-1 {
    @include chart-icon();
    background: #19af47;
  }
  .icon-color-2 {
    @include chart-icon();
    background: #008fe8;
  }
  .icon-color-3 {
    @include chart-icon();
    background: #e89100;
  }
  .icon-color-4 {
    @include chart-icon();
    background: #724ce8;
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
