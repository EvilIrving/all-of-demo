<!--
 * @Author: hanyu
 * @LastEditTime: 2022-02-13 16:51:50
 * @Description: OperationPlan
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenWorkbench\projectDetail\OperationPlan.vue
-->
<template>
  <div class='wrap'>
    <section class="page_top">
      <table class="top_table">
        <tr class="head">
          <td>年度</td>
          <td>控运计划</td>
          <td>计划申请文件</td>
          <td>计划批准文件</td>
        </tr>
        <tr class="body">
          <td class="year">
            <el-select popper-class="query_select" v-model="yearValue" placeholder="年份">
              <el-option v-for="(item,index) in yearOption" :key="index" :label="item.annual" :value="index">
              </el-option>
            </el-select>
          </td>
          <td class="lookfile" @click="lookUpFile('控运计划')">查看附件</td>
          <td class="lookfile" @click="lookUpFile('计划申请文件')">查看附件</td>
          <td class="lookfile" @click="lookUpFile('计划批准文件')">查看附件</td>
        </tr>
      </table>

    </section>
    <section class="page_bottom">
      <div class="level_msg">
        <div>
          <p>当前水位：<span>{{currentWaterLevel}}m</span></p>
          <p>超限制：<span>{{limitWaterLevel}}m</span></p>
          <p>较前1个小时：<span>
              <i class="el-icon-caret-bottom"></i>
              <!-- <i class="el-icon-caret-top"></i> -->
              {{oneHourBefore > 0  ? oneHourBefore : 0 }}m
            </span>
          </p>
        </div>
        <span>当前时间：{{currenTime}}</span>
      </div>
      <div class="level_chart">
        <div v-if="showChart" id="kyjhChart"></div>
      </div>
    </section>
  </div>
</template>

<script>
import { spcpListJson, getFile, kyjhTable } from "@/api/api_reservoir";
export default {
  name: 'OperationPlan',
  props: {
    reservoirDetail: {
      type: Object,

    }
  },
  components: {

  },
  data() {
    return {
      year: new Date().format('yyyy'),
      yearValue: 0,
      yearOption: [],
      fileIndex: 0,
      loading: false,
      showChart: false,
      LineData: { rzlist: [], tmlist: [],characteristiclist:[] },
      maxValue: 0,
      minValue: 0,
      currentWaterLevel: 0,//当前水位
      ExceedingFloodLimit: 0,//超汛限
      oneHourBefore: 0,//较前1个小时
      currenTime: 0,//当前时间
      FloodControlHighWaterLevel: 0,//防洪高水位
      normalWaterLevel: 0,//正常水位
      realtimeWaterLevel: 0,//实时水位
      limitWaterLevel: 0,//限制水位
      characteristicWaterLevel: 0,//特征水位
    };
  },
  async mounted() {
    this.getspcpListJson(this.reservoirDetail.prcd)
    await this.getData()
    this.initChart()
  },
  methods: {
    async getspcpListJson(prcd) {
      let res = await spcpListJson({ prcd: prcd })
      if (res.success) {
        this.fileList = res.rows
        this.yearOption = res.rows
      }
    },
    async lookUpFile(fileType) {
      this.loading = true
      let res = ''
      if (fileType === '控运计划') {
        res = await getFile({ dataId: this.fileList[this.yearValue].file })
      } else if (fileType === '计划申请文件') {
        res = await getFile({ dataId: this.fileList[this.yearValue].appfl })
      } else {
        res = await getFile({ dataId: this.fileList[this.yearValue].apvfl })
      }
      if (res.result) {
        this.loading = false
        window.open(this.$config.host + res.list[0].filurl)
      }
    },
    async getData() {
      let opt = {
        prcd: this.reservoirDetail.prcd,
      }
      let res = await kyjhTable(opt)
      if (res.success) {
        this.showChart = true
        this.currenTime = res.data.tm // 当前时间
        this.oneHourBefore = (res.data.waterLevel - res.data.oldWaterLevel).toFixed(0) // 一小时前时间
        this.currentWaterLevel = +res.data.waterLevel // 当前水位
        this.normalWaterLevel = +res.data.data[0].NWL // 正常水位
        this.limitWaterLevel = +res.data.data[0].limitWaterLevel // 限制水位
        this.characteristicWaterLevel = + res.data.newTzsw // 特征水位
        this.FloodControlHighWaterLevel = +res.data.data[0].FHLV // 防洪高水位
        let rzlist = []
        let tmlist = []
        let characteristiclist = []
        let compareArr = []
        let today = this.currenTime.substring(0,10)
        res.data.rows.forEach(element => {
          if (element.rz) {
            let time = element.tm.substring(6, 13) + '时'
            rzlist.push({ value: element.rz, time: element.tm })
            compareArr.push(element.rz)
            tmlist.push(time)
          }
          if (element.tm.substring(0,10) == today) {
            characteristiclist.push({ value: + res.data.newTzsw, time: element.tm })
          }else{
            characteristiclist.push({ value: + res.data.oldTzsw, time: element.tm })
          }
        })
        compareArr.push(this.currentWaterLevel)
        compareArr.push(this.normalWaterLevel)
        compareArr.push(this.limitWaterLevel)
        compareArr.push(this.characteristicWaterLevel)
        compareArr.push(this.FloodControlHighWaterLevel)
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        this.LineData.rzlist = rzlist
        this.LineData.tmlist = tmlist
        this.LineData.characteristiclist = characteristiclist
      }
    },
    async initChart() {
      let myChart = this.$echarts.init(
        document.getElementById('kyjhChart')
      );
      let option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
          padding: 5,
          borderWidth: 4,
          borderColor: '#000000',
          backgroundColor: '#0000007e',
          textStyle: {
            fontSize: 16,
            lineHeight: 22
          },
          formatter: (params) => {
            let tooltipObj = [{
              name: '实时水位',
              value: params[0].value,
              class: 'realtime'
            }, {
              name: '正常蓄水位',
              value: this.normalWaterLevel,
              class: 'normal'
            }, {
              name: '限制水位',
              value: this.limitWaterLevel,
              class: 'limit'
            }, 
             {
              name: '防洪高水位',
              value: this.FloodControlHighWaterLevel,
              class: 'fangLimit'
            }
            // {
            //   name: '特征水位',
            //   value: this.characteristicWaterLevel,
            //   class: 'characteristic'
            // }, 
           ]
            tooltipObj.sort((a, b) => b.value - a.value)
            return `
                     <div class='tooltip_box'>
                        <h4>${params[0].data.time}</h4>
                        <p class="${tooltipObj[0].class}">${tooltipObj[0].name}：${tooltipObj[0].value}m</p>
                        <p class="${tooltipObj[1].class}">${tooltipObj[1].name}：${tooltipObj[1].value}m</p>
                        <p class="${tooltipObj[2].class}">${tooltipObj[2].name}：${tooltipObj[2].value}m</p>
                        <p class="${tooltipObj[3].class}">${tooltipObj[3].name}：${tooltipObj[3].value}m</p>
                      </div>
                    `
          }
        },
        grid: {
          top: "28",
          left: "0",
          right: "0",
          bottom: "16",
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: true,
            axisTick: {
              alignWithLabel: true,
            },
            data: this.LineData.tmlist,
            axisLabel: {
              textStyle: {
                color: "#fff",
              }
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
          },
        ],
        yAxis: {
          type: 'value',
          name: '水位m',
          min: (this.minValue - (this.maxValue - this.minValue) * 1).toFixed(0),
          max: (this.maxValue + (this.maxValue - this.minValue) * 1).toFixed(0),
          // splitNumber : 20,
          nameTextStyle: {
            color: "#fff",
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
            },
          },
          splitLine: {
            lineStyle: {
              color: "#334688",
            },
          },
        },
        series: [
          {
            name: '实时水位(m)',
            type: 'line',
            color: '#1CFFF5',
            yAxisIndex: 0,
            markLine: {
              symbol: ['none', 'none'],
              silent: true,
              data: [
                {
                  yAxis: this.limitWaterLevel,
                  label: {
                    position: 'insideEndBottom',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "限制水位";
                    }
                  },
                  lineStyle: {
                    color: "#ED995F",
                    width: 2,
                  }
                },
                {
                  yAxis: this.normalWaterLevel,
                  label: {
                    position: 'insideEndTop',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "正常蓄水位";
                    }
                  },
                  lineStyle: {
                    color: "#EF645B",
                    width: 2,
                  }
                },
                {
                  yAxis: this.FloodControlHighWaterLevel,
                  label: {
                    position: 'middle',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "防洪高水位";
                    }
                  },
                  lineStyle: {
                    color: "#D3312A",
                    width: 2,
                  }
                },
                // {
                //   yAxis: this.characteristicWaterLevel,
                //   label: {
                //     position: 'middle',
                //     distance: [0, 3],
                //     formatter: function (data) {
                //       return "特征水位";
                //     }
                //   },
                //   lineStyle: {
                //     color: "#5282E2",
                //     width: 2,
                //   }
                // },
              ],
            },
            data: this.LineData.rzlist
          },
          // {
          //   name: '特征水位(m)',
          //   type: 'line',
          //   color: '#5282E2',
          //   yAxisIndex: 0,
          //   data: this.LineData.characteristiclist
          // },
        ],
      }
      myChart.setOption(option);
    },
    // 查找数组中最小值 he 最大值
    arrayMin(arrs) {
      var min = arrs[0];
      for (var i = 1, ilen = arrs.length; i < ilen; i += 1) {
        if (arrs[i] < min) {
          min = arrs[i];
        }
      }
      return min;
    },
    arrayMax(arrs) {
      var max = arrs[0];
      for (var i = 1, ilen = arrs.length; i < ilen; i++) {
        if (arrs[i] > max) {
          max = arrs[i];
        }
      }
      return max;
    },
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  height: calc(100% - 60px);
  width: 100%;
  .page_top {
    width: 100%;
    .top_table {
      width: 100%;
      border-bottom: 1px solid #3390e1;
      .head {
        height: 48px;
        width: 100%;
        background: #03338d;
        border: 1px solid #0051b5;
        td {
          text-align: center;
          font-size: 16px;
          line-height: 48px;
          border-right: 1px solid #0053b8;
          color: #03e6f0;
          &:last-child {
            border-right: none;
          }
        }
      }
      .body {
        height: 48px;
        td {
          text-align: center;
          font-size: 16px;
          line-height: 48px;
          &.year {
            position: relative;
            .el-icon-arrow-down {
              position: absolute;
              font-weight: bold;
              font-size: 20px;
              right: 18px;
              top: 13px;
              z-index: 1;
            }
          }
          &.lookfile {
            cursor: pointer;
            color: #69e3ee;
          }
        }
      }
    }
  }
  .page_bottom {
    .level_msg {
      @include flexbox();
      @include flexAI(center);
      padding: 20px 0;
      & > div {
        width: 70%;
        @include flexbox();
        p {
          margin-right: 10px;
          font-size: 20px;
          color: #ffffff;
          span {
            color: #69e3ee;
          }
        }
      }
      & > span {
        display: block;
        width: 30%;
        font-size: 14px;
      }
    }
    .level_chart {
      width: 100%;
      height: 300px;
      #kyjhChart {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>

<style lang="scss">
.year {
  width: 100px;
  .el-input__inner {
    background: #052176;
    border: 0px;
    color: #fff;
    font-size: 16px;
  }
  .el-date-editor {
    z-index: 2;
    width: 100%;
    .el-input__inner {
      width: 100%;
      padding: 0 0 0 20px;
      text-align: left;
      background: #052176;
      border: 1px solid #1990ff;
      color: #fff;
      font-size: 16px;
    }
    .el-input__prefix {
      display: none;
    }
  }
}
</style>