<!--
 * @Author: dyn
 * @Date: 2021-07-12 09:47:17
 * @LastEditTime: 2021-07-15 18:12:26
 * @LastEditors: Please set LastEditors
 * @Description: 珊溪总览落点弹窗
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\countCards\StagnantWaterCard.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      width="60%"
      destroy-on-close
      :visible.sync="visible"
      :before-close="handleClose"
      append-to-body
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">{{ name }}</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="content-panel">
          <div class="left-content">
            <div class="top-panel">
              <div class="title">
                <i class="icon-diamonds"></i>
                <span>监测分析</span>
              </div>
              <el-select
                v-model="time"
                class="primary-select"
                @change="meteorologicalPastTimeData"
                placeholder="请选择时间"
                style="width: 126px"
              >
                <el-option
                  v-for="item in timeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>

            <div class="chart-panel">
              <div id="lineChart" style="width: 100%; height: 100%"></div>
            </div>
          </div>
          <div class="right-content">
            <el-table
              ref="multipleTable"
              :data="tbList"
              height="100%"
              width="100%"
              stripe
              class="abc"
              v-loading="loading"
              element-loading-text="拼命加载中"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
            >
              <el-table-column prop="index" label="序号" width="55">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                :show-overflow-tooltip="true"
                prop="tm"
                label="时间"
                width="160"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="windVelocity"
                label="COD"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="direct"
                label="氨氮"
              />
              <el-table-column
                :show-overflow-tooltip="true"
                prop="relhumidity"
                label="总磷"
              />
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { generateDataZoom } from "../cards/utils/chart.js";
import * as echarts from "echarts"; // echart插件
import { weatherApi } from "@/api/weatherApi";

export default {
  name: "ShanXiZLDialog",
  data() {
    return {
      name: "",
      stationCode: "",
      activeTab: "风力风向",
      time: 14,
      chartData: {},
      tbList: [],
      loading: false,

      timeOptions: [
        {
          value: 1,
          label: "前一天",
        },
        {
          value: 2,
          label: "前二天",
        },
        {
          value: 3,
          label: "前三天",
        },
        {
          value: 4,
          label: "前四天",
        },
        {
          value: 7,
          label: "前一周",
        },
        {
          value: 14,
          label: "前二周",
        },
      ],
    };
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  components: {},
  created() {},
  mounted() {
    window.addEventListener("resize", this.handleWindowResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  computed: {},
  methods: {
    init(val) {
      this.name = val.name;
      this.stationCode = val.stationCode;
      this.$nextTick(() => {
        this.meteorologicalPastTimeData();
      });
    },
    async meteorologicalPastTimeData() {
      this.chartData = {
        name: [
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
          "2021-08-01 00:00:00",
        ],
        cod: [1, 2, 3, 2, 4, 1, 3],
        andan: [6, 4, 8, 3, 7, 2, 6],
        zonglin: [8, 4, 6, 2, 4, 5, 3],
      };
      let opt = {
        days: this.time,
        stationCode: this.stationCode,
      };
      // await weatherApi.meteorologicalPastTimeData(opt).then((res) => {
      //   this.tbList = res.data;
      //   res.data.forEach((item) => {
      //     this.chartData.name.push(item.tm);
      //     this.chartData.windVelocity.push(item.windVelocity);
      //     this.chartData.direction.push(item.direct);
      //     this.chartData.relhumidity.push(item.relhumidity);
      //     this.chartData.precipition.push(item.precipition);
      //     this.chartData.stationPress.push(item.stationPress);
      //   });
      this.$nextTick(() => {
        this.wetChartOpt();
      });
      // });
    },
    changeTab(val) {
      this.activeTab = val;
      this.$nextTick(() => {
        this.wetChartOpt();
      });
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    wetChartOpt() {
      const _this = this;
      var chartDom = document.getElementById("lineChart");
      var myChart = echarts.init(chartDom);

      var value = [];
      let options = {
        dataZoom: generateDataZoom(6, this.chartData.name.length),
        grid: {
          top: "10%",
          bottom: "5%",
          left: "0%",
          right: "0%",
          containLabel: true,
        },
        xAxis: {
          type: "category",
          data: this.chartData.name,
          axisLabel: {
            show: true,
            textStyle: {
              color: "#fff",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            lineStyle: {
              color: "#fff",
              fontSize: "14",
            },
          },
        },
        yAxis: {
          type: "value",
          name: "",
          nameTextStyle: {
            color: "#ffffff99",
            fontSize: "14",
          },
          axisLine: {
            show: false,
          },
          axisLabel: {
            color: "#fff",
            fontSize: "14",
          },
          splitLine: {
            lineStyle: {
              show: false,
              color: "#fff",
              opacity: 0.2,
            },
          },
        },
        legend: {
          show: true,
          selectedMode: false,
          textStyle: {
            color: "#fff",
          },
          // itemStyle: {
          //   color: "none",
          //   borderColor: "#1CFFF5",
          //   borderWidth: 1,
          //   opacity: 1,
          // },
          itemGap: 20,
        },
        tooltip: {
          trigger: "axis",
          formatter: function (data) {
            console.log(9800, data);
            return (
              data[0].name +
              "<br/><span>COD：</span>" +
              data[0].value +
              "<br/><span>氨氮：</span>" +
              data[1].value +
              "<br/><span>总磷：</span>" +
              data[2].value
            );
          },
          confine: true,
        },
        series: [
          {
            name: "COD",
            type: "line",
            data: this.chartData.cod,
            smooth: true,
            symbol: "none",
          },
          {
            name: "氨氮",
            type: "line",
            data: this.chartData.andan,
            smooth: true,
            symbol: "none",
          },
          {
            name: "总磷",
            type: "line",
            data: this.chartData.zonglin,
            smooth: true,
            symbol: "none",
          },
        ],
        color: ["#1CFFF5", "#008FE8", "#E89100"],
      };

      options && myChart.setOption(options);
    },
    // 当窗口缩放时，echart动态调整自身大小
    handleWindowResize() {
      this.$nextTick(() => {
        if (this.activeTab == "风力风向") {
          this.windChartOpt();
        } else if (this.activeTab == "相对湿度") {
          this.wetChartOpt();
        } else if (this.activeTab == "降水量") {
          this.wetChartOpt();
        } else if (this.activeTab == "气压") {
          this.wetChartOpt();
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .top-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content-panel {
    height: 500px;
    display: flex;
    justify-content: space-between;
    color: #fff;
    .left-content {
      width: 60%;
      margin-right: 20px;
      .title {
        margin-bottom: 15px;
        span {
          margin-left: 10px;
          font-size: 18px;
          vertical-align: top;
        }
      }
    }
    .chart-panel {
      width: 100%;
      height: calc(100% - 40px);
    }
    .right-content {
      width: 40%;
    }
  }
  .abc ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  ::v-deep .el-dialog {
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
      }
    }
    .el-dialog__body {
      // height: 300px;
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      position: relative;
      .primary-dialog__content {
        height: 100%;
        padding: 20px;
      }
    }
  }
}
</style>