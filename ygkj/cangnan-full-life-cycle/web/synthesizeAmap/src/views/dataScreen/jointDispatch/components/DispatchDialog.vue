<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="1200px"
    :before-close="handleClose"
    top="10vh"
    :title="name"
    class="dispatch-dialog"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="dialog-content">
      <div class="configuration-header">
        <div class="first-line">
          <div class="left-side">
            <span>时间选择 </span>
            <el-date-picker
              v-model="startTime"
              type="datetime"
              value-format="yyyy-MM-dd hh:mm:ss"
              placeholder="选择日期">
            </el-date-picker>
            <b>至</b>
            <el-date-picker
              v-model="endTime"
              type="datetime"
              value-format="yyyy-MM-dd hh:mm:ss"
              placeholder="选择日期">
            </el-date-picker>
          </div>
          <div class="right-side">
            <span class="active">导出WORD</span>
            <span>人工计算</span>
            <span>人工雨量调度</span>
            <span class="active" @click="doCalc">重新计算</span>
          </div>
        </div>
        <div class="second-line">
          <div class="parameter-area">
            <div class="param-item">
              <span>入库流量放大系数:</span>
              <el-input v-model="inMagnifyNum"></el-input>
            </div>
            <div class="param-item">
              <span>出库流量放大系数:</span>
              <el-input v-model="outMagnifyNum"></el-input>
            </div>
            <div class="param-item">
              <span>当前汛限:</span>
              <el-input v-model="floodControl"></el-input>
              <span style="padding-left:4px">米</span>
            </div>
          </div>
        </div>
      </div>
      <!-- <ve-histogram
        :data="chartData"
        :data-zoom="dataZoom"
        :settings="chartSettings"
        :grid="grid"
        height="100%"
        :extend="extend"
      ></ve-histogram> -->
      <!-- <div id="myChart" ref="myChart"></div> -->
      <div class="content-body">
        <div id="chart"></div>
        <div class="configuration-body">
          <model-config @updateChart="updateChart" ref="modelConfig"></model-config>
        </div>
      </div>
      
    </div>
  </el-dialog>
</template>
<script>
import echarts from "echarts";
import ModelConfig from "./sidebar/ModelConfig";
export default {
  name: "RescueSupport",
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    name: {
      type: String,
      default: "",
    },
    info: {
      type: Array,
    },
  },
  components: {
    ModelConfig
  },
  mounted() {
    this.floodControl = this.$store.state.modelInfo.limitLevel;
    this.chartData = this.info;
    this.$nextTick(() => {
      // this.drawChart();
      this.dealData();
    });
  },
  data() {
    return {
      chartData:[],
      startTime:"",
      endTime:"",
      inMagnifyNum:1.0,//入库放大系数
      outMagnifyNum:1.0,//出库放大系数
      floodControl:"",//当前汛限
    };
  },
  methods: {
    dealData() {
      let xArr = [],
        rainArr = [],
        flowArr = [],
        outFlowArr = [],
        waterArr = [],
        actualWaterArr = [],
        capacityArr = [];
      for (let i in this.chartData) {
        xArr.push(this.chartData[i].forecastTime);
        rainArr.push(this.chartData[i].rain);
        flowArr.push(this.chartData[i].flow);
        outFlowArr.push(this.chartData[i].outFlow);
        waterArr.push(this.chartData[i].water);
        actualWaterArr.push(this.chartData[i].actualWater);
        capacityArr.push(this.chartData[i].capacity);
      }
      this.drawChart(
        xArr,
        rainArr,
        flowArr,
        outFlowArr,
        waterArr,
        actualWaterArr,
        capacityArr
      );
    },
    drawChart(
      xArr,
      rainArr,
      flowArr,
      outFlowArr,
      waterArr,
      actualWaterArr,
      capacityArr
    ) {
      // 基于准备好的dom，初始化echarts实例
      this.$echarts.dispose(document.getElementById("chart"));
      let myChart = this.$echarts.init(document.getElementById("chart"));

      let colors = [
        "#51FEBF",
        "#01C9FE",
        "#301FFF",
        "#9F1CFF",
        "#FFC31C",
        "#FF4A1A",
      ];
      let months = xArr;
      // 指定图表的配置项和数据
      let option = {
        color: colors,
        title: {
          //标题，可以自定义标题的位置和样式
        },
        grid: {
          // 间距是 根据x、y轴计算的；假如都是0，x、y轴的label汉字就隐藏掉了。
          left: 240, // 默认10%，给24就挺合适的。
          right: 240, // 默认10%
        },
        legend: {
          //图例，每一个系列独有一个图例，注意：图例的名字必须跟下面series数组里面的name一致。
          data: [
            "雨量",
            "入库流量",
            "出库流量",
            "预测水位",
            "实测水位",
            "库容",
          ],
          textStyle: { color: "#4898fb" },
        },
        tooltip: {
          //鼠标悬浮时的样式，可自定义
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "cross", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        xAxis: {
          //x轴的配置
          data: months,
          axisLine: {
            lineStyle: {
              color: "#4898fb",
            },
          },
          axisLabel: {
            formatter: function(value){
              return new Date(value).format("MM-dd hh:mm:ss");
            },
            textStyle: {
              color: "#4898fb",
            },
          },
        },

        yAxis: [
          {
            position: "left",
            type: "value",
            name: "雨量",
            inverse:true,
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            axisLine: {
              lineStyle: {
                color: colors[0],
              },
            },
            axisLabel: {
              formatter: "{value} m",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
          {
            position: "left",
            offset: 60,
            type: "value",
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            name: "入库流量",
            axisLine: {
              lineStyle: {
                color: colors[1],
              },
            },
            axisLabel: {
              formatter: "{value} m³/s",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
          {
            position: "left",
            offset: 140,
            type: "value",
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            name: "出库流量",
            axisLine: {
              lineStyle: {
                color: colors[2],
              },
            },
            axisLabel: {
              formatter: "{value} m³/s",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
          {
            position: "right",
            type: "value",
            name: "预测水位",
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            axisLine: {
              lineStyle: {
                color: colors[3],
              },
            },
            axisLabel: {
              formatter: "{value} m",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
          {
            position: "right",
            offset: 60,
            type: "value",
            name: "实测水位",
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            axisLine: {
              lineStyle: {
                color: colors[4],
              },
            },
            axisLabel: {
              formatter: "{value} m",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
          {
            position: "right",
            offset: 120,
            type: "value",
            name: "库容",
            splitLine: {
              show: true,
              lineStyle: {
                color: "rgba(0,0,0,.6)",
              },
            },
            axisLine: {
              lineStyle: {
                color: colors[5],
              },
            },
            axisLabel: {
              formatter: "{value} 万m³",
              textStyle: {
                color: "#4898fb",
              },
            },
          },
        ],
        toolbox: {
          show: false,
        },
        series: [
          {
            name: "雨量",
            type: "bar",
            barMaxWidth: "20%",
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "0", //使用第一个y轴，序号从0开始
            data: rainArr,
            // itemStyle: {
            //   normal: {
            //     color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            //       { offset: 0, color: "#51F1F6" }, //柱图渐变色
            //       { offset: 0.5, color: "#3ED4F7" }, //柱图渐变色
            //       { offset: 1, color: "#23ABF7" }, //柱图渐变色
            //     ]),
            //   },
            //   emphasis: {
            //     color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            //       { offset: 0, color: "#51F1F6" }, //柱图高亮渐变色
            //       { offset: 0.7, color: "#3ED4F7" }, //柱图高亮渐变色
            //       { offset: 1, color: "#23ABF7" }, //柱图高亮渐变色
            //     ]),
            //   },
            // },
          },
          {
            name: "入库流量",
            type: "line",
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "1", //使用第二个y轴
            data: flowArr,
          },
          {
            name: "出库流量",
            type: "line",
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "2", //使用第三个y轴
            data: flowArr,
          },
          {
            name: "出库流量",
            type: "line",
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "3", //使用第三个y轴
            data: outFlowArr,
          },
          {
            name: "预测水位",
            type: "line",
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "4", //使用第三个y轴
            data: waterArr,
          },
          {
            name: "实测水位",
            type: "line",
            showSymbol: true,
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "5", //使用第三个y轴
            data: actualWaterArr,
          },
          {
            name: "库容",
            type: "line",
            showSymbol: true,
            symbol: "circle", //设定为实心点
            symbolSize: 6, //设定实心点的大小
            barMaxWidth: "20%",
            smooth: true,
            label: {
              normal: {
                show: false,
                position: "top",
              },
            },
            yAxisIndex: "5", //使用第三个y轴
            data: capacityArr,
          },
        ],
      };

      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    },
    //重新计算
    doCalc(){
      if(!this.startTime && !this.endTime){
        this.$message({
          type: 'warning',
          message:"请选择起止时间"
        })
      }else{
        this.$refs.modelConfig.getDetail(this.startTime,this.endTime,this.inMagnifyNum,this.floodControl,this.outMagnifyNum);
      }
    },
    //更新统计图
    updateChart(data){
      console.log(data);
      this.chartData = data;
      this.dealData();
    },
    handleClose() {
      this.$emit("handleClose");
    },
    
  },
};
</script>

<style lang="scss">
@import "@/style/blueDialog.scss";
.dispatch-dialog {
  .el-dialog {
    background: rgba(0, 0, 0, 0)
      url(../../../../assets/images/blue-dialog-bg.png) no-repeat center center;
    background-size: 100% 100%;
    .el-dialog__header {
      background: rgba(0, 0, 0, 0);
      padding: 20px 20px 12px;
      .el-dialog__title {
        color: #4db6e4;
      }
      .el-dialog__headerbtn {
        top: 26px;
        right: 28px;
      }
    }
    .dialog-content {
      width: 100%;
      height: 650px;
      overflow: auto;
      padding-top: 20px;
      .configuration-header{
        width: 100%;
        height: 100px;
        padding: 0 32px;
        .first-line{
          @include flexbox;
          @include flexflow(row,nowrap);
          @include flexJC(space-between);
          @include flexAC;
          .left-side{
            color: #4898fb;
            .el-input{
              border: 1px solid #4898fb;
              .el-input__inner{
                height: 28px;
                color: #4898fb;
                background: rgba(0,0,0,0);
              }
            }
            b{
              padding: 0 12px;
              font-weight: normal;
            }
          }
          .right-side{
            span{
              display: inline-block;
              padding: 8px 12px;
              border-radius: 4px;
              background: #205AB0;
              color: #ccc;
              font-size: 12px;
              margin-right: 16px;
              cursor: pointer;
              &.active{
                color: #fff;
                background: #3283FF;
              }
            }
          }
        }
        .second-line{
          padding-top: 16px;
          .parameter-area{
            width: calc(100% - 450px);
            @include flexbox;
            @include flexJC(space-around);
            @include flexflow(row,nowrap);
            .param-item{
              width: 250px;
              @include flexbox;
              @include flexAC;
              span{
                color: #4898fb;
                padding-right: 8px;
              }
              .el-input{
                width: 70px;
                .el-input__inner{
                  background: rgba(0,0,0,0);
                  border: 1px solid #4898fb;
                  height: 28px;
                }
              }
              
            }
          }
        }
      }
      .content-body{
        height: calc(100% - 100px);
        width: 100%;
        padding: 16px;
        @include flexbox;
        @include flexflow(row,nowrap);
        #chart {
          height: 100%;
          width: calc(100% - 450px);
        }
        .configuration-body{
          width: 450px;
          height: 100%;
          padding-right: 26px;
        }
      }
    }
  }
}
</style>