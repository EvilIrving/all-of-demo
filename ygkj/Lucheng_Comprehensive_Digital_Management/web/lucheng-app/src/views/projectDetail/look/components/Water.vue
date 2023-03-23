<template>
  <div class="water">
    <div class="title_box" style="margin-left: 0.6rem">
      <div class="title_flex">
        <img src="@/assets/decorate.png" alt="" />
        <span>雨量曲线</span>
      </div>
      <el-select v-model="value" size="small" @change="getDateList">
        <el-option v-for="(item, index) in options" :key="index" :label="item.stnm" :value="item.stnm" />
      </el-select>
    </div>
    <div>
      <!-- 图表 -->
      <div class="chart-box">
        <EchartLine :extraOption="extraOption"></EchartLine>
      </div>
    </div>

    <div>
      <div class="title_box" style="margin-left: 0.6rem">
        <div class="title_flex">
          <img src="@/assets/decorate.png" alt="" />
          <span>雨量列表</span>
        </div>
      </div>
      <el-table :data="tableData" border style="width: 9rem; height: 200px; margin-left: 0.6rem" :cell-style="rowClass" height="200" stripe class="primary-table">
        <el-table-column align="center" label="序号" type="index" width="60">
        </el-table-column>
        <el-table-column prop="drp" label="雨量(mm)" width="100" align="center">
        </el-table-column>
        <el-table-column prop="tm" label="时间" align="center">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import Bar from "@/echarts/Bar.vue";
import EchartLine from "@/echarts/echart_line/EchartLine.vue";
import { realTimeMonitoring } from "@/api/projectDetail.js";
export default {
  name: "Water",
  components: {
    Bar,
    EchartLine,
  },
  props: {
    type: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      tableData: [],
      menuIndex: 0,
      value: "站点名称",
      options: [],
      timeList: [],
      dataList: [],
      // 统计图
      extraOption: {},
    };
  },
  mounted() {
    this.getProList();
    setTimeout(() => {
      this.getDateList();
    }, 100);
  },
  methods: {
    rowClass() {
      //表格数据居中显示
      return "text-align:center";
    },
    //获取工程点位列表
    async getProList() {
      let opt = {
        stType: 2,
        projectType: 1,
        projectCode: this.$localData("get", "projectCode"),
      };
      let res = await realTimeMonitoring.stationList(opt);
      if (res.code === 0) {
        this.options = res.data;
        this.value = this.options[0].stnm;
      }
    },
    //获取点位间接数据列表
    async getDateList() {
      let data = {};
      this.timeList = [];
      this.dataList = [];
      for (let val of this.options) {
        if (this.value === val.stnm) data = val;
      }
      let opt = {
        stationCode: data.stcd,
        stationType: "TT",
        days: 1,
      };
      let res = await realTimeMonitoring.hourRainfall(opt);
      if (res.code === 0) {
        res.data.map((i) => (i.drp = Math.abs(i.drp)));
        this.tableData = res.data;
        for (let item of this.tableData) {
          this.timeList.push(item.tm);
          this.dataList.push(parseFloat(item.drp));
        }
        this.extraOption = {
          xAxis: {
            data: this.timeList,
            axisTick: {
              show: false,
            },
            axisLabel: {
              interval: "auto",
              color: "#AEB4B9",
            },
            axisLine: {
              lineStyle: {
                color: "#EDEDED",
              },
            },
          },

          yAxis: {
            type: "value",
            splitLine: {
              show: true,
              lineStyle: {
                color: "#EDEDED",
              },
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false, //不显示坐标轴线
            },
            axisLabel: {
              color: "#AEB4B9",
            },
          },
          tooltip: {
            trigger: "item",
            backgroundColor: "#40CD96",
            formatter: "{a}:{c}" + "mm",
          },
          dataZoom: [
            {
              type: "inside", //slider表示有滑动块的，inside表示内置的
              show: true,
              realtime: true, //拖动时，是否实时更新系列的视图。
              xAxisIndex: [0],
              start: 0,
              end: 100,
            },
          ],
          grid: {
            top: 10,
            left: 28,
            right: 10,
            bottom: 20,
          },
          series: [
            {
              name: "雨量",
              data: this.dataList,
              type: "bar",
              itemStyle: {
                normal: {
                  //颜色渐变函数 前四个参数分别表示四个位置依次为左、下、右、上
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 1,
                      color: "#036EFF", // 100% 处的颜色
                    },
                    {
                      offset: 0,
                      color: "#66C9FF", // 0% 处的颜色
                    },
                  ]), //背景渐变色
                },
              },
            },
          ],
        };
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.chart-box {
  width: 9rem;
  height: 4rem;
  margin-left: 0.6rem;
  margin-bottom: 0.5rem;
}
::v-deep .el-input__inner {
  border: none;
  padding-left: 0;
}
</style>
<style lang="scss">
@import "@/style/tableCommon";
</style>
