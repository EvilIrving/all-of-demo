<template>
  <div class='wrap'>
    <section class="page_top"  >
      <div>
        <h3>当前库容总量</h3>
        <p v-loading='loading'>{{currenty}}万m³</p>
      </div>
      <div>
        <h3>蓄水率</h3>
        <p v-loading='loading'>{{xsl}}%</p>
      </div>
      <div>
        <h3>至防洪高水位可纳</h3>
        <p v-loading='loading'>{{fhlvCurrenty}}万m³</p>
      </div>
      <div>
        <h3>至发电死水位可供</h3>
        <p v-loading='loading'>{{gdwlCurrenty}}万m³</p>
      </div>
    </section>
    <section class="page_bottom">
      <div class="body_top">
        <p class="small_title">基本信息</p>
        <div class="top_msg" v-loading='loading'>
          <p>水库名称：<span>{{name}}</span></p>
          <p>规模：
            <span>{{scale}}</span>
          </p>
          <p>当前水位：
            <!-- <span>{{ popupData.waterLevel ? popupData.waterLevel.toFixed(2) + "m" : "-" }} </span> -->
            <span>{{currentWaterLevel}}m</span>
          </p>
          <p>当前时间：
            <span>{{currenTime}}</span>
          </p>
        </div>
      </div>
      <div class="body_chart">
        <div v-if="showChart" id="sknxChart"></div>
      </div>
    </section>
  </div>
</template>

<script>
import { sknxTable } from "@/api/api_reservoir";
export default {
  name: 'ReservoirStorage',
  props: {
    reservoirDetail: {
      type: Object,

    }
  },
  components: {

  },
  data() {
    return {
      showChart: false,
      loading:false,
      LineData: { rzlist: [], tmlist: [] },
      maxValue: 0,
      minValue: 0,
      currentWaterLevel: null,//当前水位
      normalWaterLevel: 0,//正常水位
      fhlv: 0,
      gdwl: 0,//至发电死水位
      currenTime: '-',//当前时间
      currenty: null,//当前库容总量
      fhlvCurrenty: null,//至防洪高可纳水量
      gdwlCurrenty: null,//至发电死水位可纳水量
      xsl: '-',//蓄水率
      name: '',//名称
      scale: '',
    };
  },
  async mounted() {
    await this.getData()
    this.initChart()
  },
  methods: {
    async getData() {
      this.loading = true
      let opt = {
        prcd: this.reservoirDetail.prcd,
      }
      let res = await sknxTable(opt)
      if (res.success && res.data.rows && res.data.rows.length !== 0) {
        this.loading = false
        this.showChart = true
        let chartData = res.data
        this.currenTime = chartData.rows[chartData.rows.length - 1].tm ? chartData.rows[chartData.rows.length - 1].tm :"-" // 当前
        this.currentWaterLevel = + chartData.rows[chartData.rows.length - 1].rz  // 当前
        this.currenty = + chartData.rows[chartData.rows.length - 1].currenty.toFixed(2)  // 当前
        this.fhlvCurrenty = + chartData.rows[chartData.rows.length - 1].fhlvCurrenty.toFixed(2)  // 当前
        this.gdwlCurrenty = + chartData.rows[chartData.rows.length - 1].gdwlCurrenty.toFixed(2)  // 当前
        this.xsl = chartData.xsl ? chartData.xsl : '-'
        this.fhlv = chartData.fhlv
        this.normalWaterLevel = chartData.nwl
        this.gdwl = chartData.gdwl
        this.scale = chartData.scale
        this.name = chartData.name
        let rzlist = []
        let tmlist = []
        let compareArr = []
        chartData.rows.forEach(element => {
          if (element.rz) {
            let time = element.tm.substring(6, 13) + '时'
            rzlist.push({ value: element.rz, 
            time: element.tm, 
            currenty: element.currenty.toFixed(2), 
            fhlvCurrenty: element.fhlvCurrenty.toFixed(2) , 
            gdwlCurrenty: element.gdwlCurrenty.toFixed(2)  })
            compareArr.push(element.rz)
            tmlist.push(time)
          }
        })
        compareArr.push(this.fhlv)
        compareArr.push(this.gdwl)
        compareArr.push(this.normalWaterLevel)
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        this.LineData.rzlist = rzlist
        this.LineData.tmlist = tmlist
      }
    },
    async initChart() {
      let myChart = this.$echarts.init(
        document.getElementById('sknxChart')
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
            // console.log(params,'params');
            let tooltipObj = [{
              name: '当前水位',
              value: params[0].value,
              class: 'realtime'
            }, {
              name: '当前库容',
              value: +params[0].data.currenty,
              class: 'normal'
            }, {
              name: '至防洪高水位可纳水量',
              value: +params[0].data.fhlvCurrenty,
              class: 'fangLimit'
            }, {
              name: '至发电死水位可供水量',
              value: +params[0].data.gdwlCurrenty,
              class: 'realtime'
            }]
            return `
                     <div class='tooltip_box'>
                        <h4>${params[0].data.time}</h4>
                        <p class="">${tooltipObj[0].name}：${tooltipObj[0].value.toFixed(1)}m</p>
                        <p class="">${tooltipObj[1].name}：${tooltipObj[1].value.toFixed(1)}万m³</p>
                        <p class="${tooltipObj[1].class}">${tooltipObj[2].name}：${tooltipObj[2].value.toFixed(1)}万m³</p>
                        <p class="${tooltipObj[0].class}">${tooltipObj[3].name}：${tooltipObj[3].value.toFixed(1)}万m³</p>
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
                  yAxis: this.fhlv,
                  label: {
                    position: 'insideEndBottom',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "防洪高水位";
                    }
                  },
                  lineStyle: {
                    color: "#EF645B",
                    width: 2,
                  }
                },
                {
                  yAxis: this.normalWaterLevel,
                  label: {
                    position: 'insideEndTop',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "正常水位";
                    }
                  },
                  lineStyle: {
                    color: "#ED995F",
                    width: 2,
                  }
                },
                {
                  yAxis: this.gdwl,
                  label: {
                    position: 'middle',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "发电死水位";
                    }
                  },
                  lineStyle: {
                    color: "#D3312A",
                    width: 2,
                  }
                },
              ],
            },
            data: this.LineData.rzlist
          },
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
  .page_top {
    @include flexbox();
    @include flexJC(space-between);
    & > div {
      h3 {
        font-size: 24px;
        font-family: "youshe";
        color: #69e3ee;
        line-height: 24px;
        padding: 10px 0;
      }
      p {
        font-size: 32px;
        font-weight: bold;
        color: #ffffff;
        line-height: 39px;
      }
    }
  }
  .page_bottom {
    .body_top {
      padding: 20px 0 10px 0;
      .small_title {
        font-size: 20px;
        font-weight: 500;
        color: #ffffff;
        background: url($imageUrl + "small_title.png")
          no-repeat left 10px;
        background-size: 8px 8px;
        padding: 0 0 0 20px;
        position: relative;
        &:before {
          top: 8px;
          left: 0px;
          position: absolute;
          display: block;
          width: 10px;
          height: 10px;
          background: #03338d;
          content: "";
        }
      }
      .top_msg {
        background: #03338d;
        border: 1px solid #0051b5;
        font-size: 14px;
        font-weight: 400;
        color: #ffffff;
        height: 40px;
        box-sizing: border-box;
        width: 100%;
        margin-top: 16px;
        padding: 0 21px;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
      }
    }
    .body_chart {
      width: 100%;
      height: 300px;
      #sknxChart {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>