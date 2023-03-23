<template>
  <div class="flow">
    <div style="margin-left: 0.6rem; width: 3rem">
      <el-select v-model="value" size="small" @change="getDateList">
        <el-option
          v-for="item in options"
          :key="item.stnm"
          :label="item.stnm"
          :value="item.stnm"
        />
      </el-select>
    </div>
    <div>
      <p
        style="
          margin-top: 0.6rem;
          margin-bottom: 0.2rem;
          margin-left: 0.6rem;
          font-size: 20px;
          color: #1f1f1f;
          font-weight: 500;
        "
      >
        流量曲线
      </p>
      <!-- 图表 -->
      <div class="chart-box">
        <EchartLine :extraOption="extraOption"></EchartLine>
      </div>
    </div>

    <div>
      <p
        style="
          margin-bottom: 0.2rem;
          margin-left: 0.6rem;
          font-size: 20px;
          color: #1f1f1f;
          font-weight: 500;
        "
      >
        流量列表
      </p>
      <el-table
        :data="tableData"
        border
        style="width: 9rem; height: 200px; margin-left: 0.6rem"
        :cell-style="rowClass"
        height="200"
        stripe
        class="primary-table"
      >
        <el-table-column align="center" label="序号" type="index" width="60">
        </el-table-column>
        <el-table-column prop="data" label="流量(m)" align="center">
        </el-table-column>
        <el-table-column prop="tm" label="时间" align="center" width="220">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import EchartLine from "../../../../echarts/echart_line/EchartLine.vue";
import { seeProApi } from "../../../../api/projectDetail.js";
export default {
  name: "Flow",
  components: {
    EchartLine,
  },
  props: {
    type: {
      type: Number,
      default: 3,
    },
  },
  data() {
    return {
      tableData: [],
      menuIndex: 0,
      value: "测试站点",
      options: [],
      timeList: [],
      dataList: [],
      // 统计图
      extraOption: {},
      // 列表
      tableData: [],
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
    handleCommand(command) {
      this.value = command;
    },
    //获取工程点位列表
    async getProList() {
      let opt = {
        type: this.type,
        pjcd: this.$localData("get", "seawallCode"),
      };
      let res = await seeProApi.getListProSt(opt);
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
        stcd: data.stcd,
        sttp: data.sttp,
      };
      let res = await seeProApi.getListProStData(opt);
      if (res.code === 0) {
        this.tableData = res.data;
        for (let item of this.tableData) {
          this.timeList.push(item.tm);
          this.dataList.push(parseFloat(item.data));
        }
        this.tableData.sort(function (a, b) {
          return b.time < a.time ? -1 : 1;
        });
        this.extraOption = {
          xAxis: {
            data: this.timeList,
            axisTick: {
              show: false,
            },
            axisLabel: {
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
            backgroundColor: "#036EFF",
            formatter: "{a}:{c}" + "m",
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
              name: "最高流量",
              data: this.dataList,
              type: "line",
              smooth: true,
              symbol: "emptyCircle",
              color: "#036EFF",
              itemStyle: {
                normal: {
                  lineStyle: {
                    color: "#036EFF",
                  },
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