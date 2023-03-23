<!--
 * @Author: your name
 * @Date: 2021-06-30 18:17:27
 * @LastEditTime: 2021-12-31 14:54:47
 * @LastEditors: dtb
 * @Description: 批后监管
 * @FilePath: \lucheng\src\views\water_disaster_custom\cards\ApprovalSupervisionCard.vue
-->
<template>
  <div class="water-card">
    <panel-item
      title="批后监管"
      has-switch
      tc
      @tclick="tclick"
      :action-tabs="['流域分布', '河道分布', '行政区划', '项目阶段']"
      @change="patrolTabChange"
    >
      <template #title-switch>
        <i
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('批后监管')"
        />
      </template>
      <div class="area-box">
        <div class="pie-chart-panel">
          <div id="pieChartApproval" style="width: 50%; height: 100%"></div>
          <div class="legend-panel">
            <div
              class="legend-item"
              v-for="(item, index) in pieDataApproval.rows"
              :key="index"
            >
              <div>
                <i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
                <span class="text">{{ item.name }}</span>
              </div>
              <span class="num">{{ item.value }}</span>
              <span>个</span>
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
  name: "ApprovalSupervisionCard",
  components: {
    PanelItem,
  },
  data() {
    return {
      timer: null,
      interval: 5000,
      activeTab: 0,

      pieDataApproval: {
        totalLength: 0,
        rows: [],
      },
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
    this.projectStatisByBasin();
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.projectStatisByBasin();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterUseCount"]),

    // 流域
    async projectStatisByBasin() {
      await waterResourceApi.projectStatisByBasin().then((res) => {
        this.pieDataApproval.rows = [];
        this.pieDataApproval.totalLength = 0;
        res.data.forEach((item) => {
          let obj = {
            name: item.basin,
            value: item.num,
          };
          this.pieDataApproval.totalLength =
            this.pieDataApproval.totalLength + item.num;
          this.pieDataApproval.rows.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpiton();
        });
      });
    },

    // 河道
    async projectStatisByRiverLevel() {
      await waterResourceApi.projectStatisByRiverLevel().then((res) => {
        this.pieDataApproval.rows = [];
        this.pieDataApproval.totalLength = 0;
        res.data.forEach((item) => {
          let obj = {
            name: item.riverLevel + "级",
            value: item.num,
          };
          this.pieDataApproval.totalLength =
            this.pieDataApproval.totalLength + item.num;
          this.pieDataApproval.rows.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpiton();
        });
      });
    },

    // 行政区划
    async projectStatisByAdcd() {
      await waterResourceApi.projectStatisByAdcd().then((res) => {
        this.pieDataApproval.rows = [];
        this.pieDataApproval.totalLength = 0;
        res.data.forEach((item) => {
          let obj = {
            name: item.adnm,
            value: item.num,
          };
          this.pieDataApproval.totalLength =
            this.pieDataApproval.totalLength + item.num;
          this.pieDataApproval.rows.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpiton();
        });
      });
    },
    // 项目阶段
    async projectStatisByProjectStage() {
      await waterResourceApi.projectStatisByProjectStage().then((res) => {
        console.log(111111, res);
        this.pieDataApproval.rows = [];
        this.pieDataApproval.totalLength = 0;
        res.data.forEach((item) => {
          let obj = {
            name: item.projectStage,
            value: item.num,
          };
          this.pieDataApproval.totalLength =
            this.pieDataApproval.totalLength + item.num;
          this.pieDataApproval.rows.push(obj);
        });
        this.$nextTick(() => {
          this.pieChartOpiton();
        });
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
    patrolTabChange(val) {
      this.activeTab = val;
      switch (val) {
        case 0:
          this.projectStatisByBasin();
          break;
        case 1:
          this.projectStatisByRiverLevel();
          break;
        case 2:
          this.projectStatisByAdcd();
          break;
        case 3:
          this.projectStatisByProjectStage();
          break;
      }
    },
    /**
     * 图表 行业类别
     */
    pieChartOpiton() {
      var chartDom = document.getElementById("pieChartApproval");
      var myChart = echarts.init(chartDom);
      var option;
      let colorList = [
        "#19af47",
        "#008fe8",
        "#e89100",
        "#6267ff",
        "#A000C8",
        "#ff551f",
        "#eec80b",
        "#98d89a",
        "#004fe3",
      ];
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
          trigger: "item",
          formatter: function (data) {
            return (
              data.name +
              "<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
              data.value +
              "</span> <span  style='font-size: 14px;'> 个</span>"
            );
          },
          confine: true,
          // extraCssText: "width:160px;height:50px;font-size:18px;",
        },
        series: [
          {
            type: "pie",
            radius: ["50%", "80%"],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: "center",
              normal: {
                // normal 是图形在默认状态下的样式
                show: true,
                position: "center",
                color: "#fff",
                fontSize: this.$commonSetting.chartFontSize,
                formatter: this.pieDataApproval.totalLength + "个\n" + "总量", // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
              },
            },
            labelLine: {
              show: false,
            },
            data: this.pieDataApproval.rows,
            color: color,
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
.water-card {
  height: 100%;

  .icon-sty {
    margin-right: 10px;
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
    background: #6267ff;
  }
  .icon-color-5 {
    @include chart-icon();
    background: #a000c8;
  }
  .icon-color-6 {
    @include chart-icon();
    background: #ff551f;
  }
  .icon-color-7 {
    @include chart-icon();
    background: #eec80b;
  }
  .icon-color-8 {
    @include chart-icon();
    background: #98d89a;
  }
  .icon-color-9 {
    @include chart-icon();
    background: #004fe3;
  }

  // .area-box {
  //   width: 100%;
  //   height: calc(100% - 34px);
  // }
  .pie-chart-panel {
    height: 200px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    .legend-panel {
      width: 50%;
      margin-left: 30px;
      .legend-item {
        float: left;
        line-height: 25px;
        display: flex;
        justify-content: space-between;
        .text {
          display: inline-block;
          width: 80px;
          font-size: 14px;
          margin-right: 10px;
        }
        .num {
          color: #1cfff5;
          font-size: 1rem;
          margin-right: 5px;
        }
      }
    }
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
