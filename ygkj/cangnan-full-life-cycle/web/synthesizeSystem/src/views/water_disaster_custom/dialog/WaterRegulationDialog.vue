<!--
 * @Autor: ysl
 * @Date: 2021-08-12 19:19:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-14 14:21:29
 * @Description: 取水监管落点弹窗
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/dialog/WaterRegulationDialog.vue
-->
<template>
  <div class="detail-table">
    <el-dialog
      width="619px"
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
        <p class="primary-dialog__title--label">水源地概况</p>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <div class="water-supply-select-panel">
          <div
            :class="[
              tabSwitch == item ? 'active' : '',
              'water-supply-select-btn',
            ]"
            @click="changeActiveTab(item)"
            v-for="(item, index) in tabList"
            :key="index"
          >
            {{ item }}
          </div>
        </div>
        <div class="info-panel">
          <!-- 图表 -->
          <div id="histogramChart" style="width: 100%; height: 100%" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

import * as echarts from "echarts"; // echart插件

export default {
  name: "WaterRegulationDialog",
  data() {
    return {
      obj: {},
      img: "",
      name: "",
      scale: "",
      type: "",
      area: "",
      isP: "",
      target: "",
      warn: "",
      tabSwitch: "日取水量",
      tabList: ["日取水量", "月取水量"],
      yearData: [],
      overviewData: [],
      legendName: ["取水量", "取水累积量"],
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
  mounted() {},
  computed: {},
  methods: {
    init(val) {
      this.obj = val;
      this.$nextTick(() => {
        this.intakeCurve()
      });
    },
    handleClose() {
      this.$emit("closeDialog");
    },
    changeActiveTab(val) {
      this.tabSwitch = val;
      this.intakeCurve();
    },
    // 取水口曲线接口数据
    async intakeCurve() {
      let obj = {
        wainCode: this.obj.wainCode, //取水口编码
        type: this.tabSwitch == "日取水量" ? 1 : 2, //1，日取水量；2，月取水量
      };
      await waterResourceApi.intakeCurve(obj).then((res) => {
        if (res.code == 0) {
          this.overviewData = [];
          this.yearData = [];
          this.dataList = res.data;
          let arr = [];
          let str = [];
          res.data.forEach((val) => {
            arr.push(val.intake);
            str.push(val.intakeSum);
            this.yearData.push(val.tm2Str);
          });
          this.overviewData.push(
            {
              name: "取水量",
              type: "bar",
              barWidth: "25",
              color: "#56FEFE",
              data: arr,
            },
            {
              name: "取水累积量",
              type: "line",
              color: "#56FEFE",
              yAxisIndex: 1,
              data: str,
            }
          );
          this.histogram();
        }
      });
    },
    // 柱状，折线图
    histogram() {
      var chartDom = document.getElementById("histogramChart");
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999",
            },
          },
        },
        legend: {
          top: "0",
          right: "20",
          data: this.legendName,
          textStyle: {
            color: "#fff", //这里用参数代替了
          },
        },
        grid: {
          top: "60",
          left: "10",
          right: "30",
          bottom: "10",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: this.yearData,
            axisPointer: {
              type: "shadow",
            },
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLabel: {
              show: true,
              textStyle: {
                color: "#fff", //这里用参数代替了
              },
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "取水量（m³）",
            nameTextStyle: {
              color: "#fff",
              fontSize: 12,
            },
            axisLabel: {
              formatter: "{value}",
              show: true,
              textStyle: {
                color: "#fff", //这里用参数代替了
              },
            },
          },
          {
            type: "value",
            name: "取水累积量（m³）",
            nameTextStyle: {
              color: "#fff",
              fontSize: 12,
            },
            axisLabel: {
              formatter: "{value}",
              show: true,
              textStyle: {
                color: "#fff", //这里用参数代替了
              },
            },
          },
        ],
        series: this.overviewData,
      };
      option && myChart.setOption(option, true);
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-dialog {
  .img-panel {
    height: 300px;
    margin-bottom: 20px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .info-panel {
    color: #fff;
    font-size: 16px;
    height: calc(100% - 44px);
    span.name {
      font-size: 15px;
    }
    span.value {
      margin-left: 10px;
      color: #33ffff;
    }
  }

  ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #1ef8f2;
    background-color: #1ef8f2;
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
      height: 500px;
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
<style lang="scss" scoped>
.water-supply-select-panel {
  display: flex;
  margin-bottom: 20px;
}
.water-supply-select-btn {
  padding: 10px 20px;
  margin-right: 20px;
  color: #c9c8c8;
  border: 1px solid rgba(255, 255, 255, 0.6);
  cursor: pointer;
  &.active {
    color: #56fefe;
    background: rgba(72, 152, 251, 0.1);
    border: 1px solid #56fefe;
    box-shadow: inset 0 0 5px 0 #56fefe;
  }
}
</style>