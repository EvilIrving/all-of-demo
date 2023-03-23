<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.name }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_top">
        <div class="top_title">
          <!-- <p class="small_title">基本信息</p> -->
          <p class="status">当前状态：<span>{{ popupData.LtstatusName }}</span></p>
          <p class="continue" v-if="LtstatusName != '空库运行'">持续时间：<span>{{ popupData.ltHours }}小时</span></p>
        </div>
        <div class="top_msg">
          <p>行政区划：</p>
          <span>{{popupData.cityADNM}} - {{popupData.countryADNM}}</span>
          <!-- <p>规模：
            <span>{{popupData.eng_scal}}</span>
          </p> -->
          <p>当前水位：
            <span>{{ waterLevel ? waterLevel.toFixed(2) + "m" : "-" }} </span>
          </p>
          <p>当前时间：
            <span>{{ waterleveltm?waterleveltm.substring(0,13) + '时':"-" }} </span>
          </p>
        </div>
      </div>
      <div class="body_chart">
        <!-- <WaterLevelChart v-if="showChart"/> -->
        <div v-if="showChart" id="cxzyxWaterLevelChart"></div>
      </div>

    </div>
  </div>
</template>

<script>
import { dailogDetail } from "@/api/api_reservoir";
export default {
  name: 'CxjgPopup',
  props: {
    popupData: {
      type: Object,
      required: true
    }
  },
  components: {
  },
  data() {
    return {
      showChart: false,
      LineData: { rzlist: [], tmlist: [] },
      limitWaterLevel: 0,
      nwl: 0,
      fhlv: 0,
      DSFLZ: 0,
      maxValue: 0,
      minValue: 0,
      waterleveltm: '',
      waterLevel: 0,
      LtstatusName: '',
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'cxzyxPopup') {
        this.showChart = false
        setTimeout(() => {
          this.showChart = true
          this.$nextTick(() => {
            this.initChart()
          })
        }, 1000)
      }
    }
  },
  mounted() { },
  methods: {
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
    },
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    async getData() {
      let opt = {
        prcd: this.popupData.prcd,
        gpType: 4,
        extractPram: '1'
      }
      let res = await dailogDetail(opt)
      if (res.success) {
        this.limitWaterLevel = res.data.limitWaterLevel
        this.nwl = res.data.NWL
        this.fhlv = res.data.FHLV
        this.DSFLZ = res.data.DSFLZ
        this.waterLevel = res.data.waterLevel
        this.waterleveltm = res.data.waterleveltm
        let rzlist = []
        let tmlist = []
        let compareArr = []
        res.rows.forEach(element => {
          if (element.rz) {
            let time = element.tm.substring(6, 13) + '时'
            rzlist.push({ value: element.rz, time: element.tm })
            compareArr.push(element.rz)
            tmlist.push(time)
          }
        })

        this.LtstatusName = res.data.LtstatusName
        compareArr.push(res.data.NWL)
        compareArr.push(res.data.FHLV)
        compareArr.push(res.data.limitWaterLevel)
        compareArr.push(res.data.DSFLZ)
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        this.LineData.rzlist = rzlist
        this.LineData.tmlist = tmlist
      }
    },
    async initChart() {
      await this.getData()
      let myChart = this.$echarts.init(
        document.getElementById('cxzyxWaterLevelChart')
      );
      let option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
          padding: 5,
          borderWidth: 0,
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
            },
            // {
            //   name: '正常蓄水位',
            //   value: this.nwl,
            //   class: 'normal'
            // }, 
            {
              name: '限制水位',
              value: this.limitWaterLevel,
              class: 'limit'
            },
            // {
            //   name: '防洪高水位',
            //   value: this.fhlv,
            //   class: 'fangLimit'
            // }, 
            {
              name: '设计洪水位',
              value: this.DSFLZ,
              class: 'fangLimit'
            }]
            tooltipObj.sort((a, b) => b.value - a.value)
            return `
                     <div class='tooltip_box'>
                        <h4>${params[0].data.time.substring(0,13)}时</h4>
                        <p class="${tooltipObj[0].class}">${tooltipObj[0].name}：${tooltipObj[0].value}m</p>
                        <p class="${tooltipObj[1].class}">${tooltipObj[1].name}：${tooltipObj[1].value}m</p>
                        <p class="${tooltipObj[2].class}">${tooltipObj[2].name}：${tooltipObj[2].value}m</p>
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
                color: "#333",
              }
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: true,
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
            color: "#757d8a",
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: "#ACB3C6FF",
            },
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            interval: 'auto',
            textStyle: {
              color: "#666666",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed'
            }
          },
        },
        series: [
          {
            name: '实时水位(m)',
            type: 'line',
            color: '#2952D0',
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
                // {
                //   yAxis: this.nwl,
                //   label: {
                //     position: 'insideEndTop',
                //     distance: [0, 3],
                //     formatter: function (data) {
                //       return "正常蓄水位";
                //     }
                //   },
                //   lineStyle: {
                //     color: "#EF645B",
                //     width: 2,
                //   }
                // },
                // {
                //   yAxis: this.fhlv,
                //   label: {
                //     position: 'middle',
                //     distance: [0, 3],
                //     formatter: function (data) {
                //       return "防洪高水位";
                //     }
                //   },
                //   lineStyle: {
                //     color: "#D3312A",
                //     width: 2,
                //   }
                // },
                {
                  yAxis: this.DSFLZ,
                  label: {
                    position: 'insideEndBottom',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "设计洪水位";
                    }
                  },
                  lineStyle: {
                    // color: "#D3312A",
                    color: "#ff0000",
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

    // 查找数组中最小值
    arrayMin(arrs) {
      var min = arrs[0];
      for (var i = 1, ilen = arrs.length; i < ilen; i += 1) {
        if (arrs[i] < min) {
          min = arrs[i];
        }
      }
      return min;
    },

    // 查找数组中最大值
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
.popup_map {
  z-index: 99;
  background: #fff;
  padding: 30px 40px 40px 40px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  // background-size: 100% 100%;
  width: 730px;
  height: 480px;
  border-radius: 4px;
  .dialog_title {
    background: transparent;
    // background: url($imageUrl + "sk-popup-title.png") no-repeat bottom;
    // background-size: 100% auto;
    height: 40px;
    line-height: 40px;
    padding-left: 16px;
    font-weight: bold;
    width: 100%;
    box-sizing: border-box;
    span {
      font-size: 24px;
      font-weight: bold;
      // font-family: youshe;
      color: #333;
    }
    & > i {
      position: absolute;
      top: 20px;
      right: 40px;
      width: 20px;
      height: 20px;
      color: #999;
      font-size: 20px;
      cursor: pointer;
      background: url($imageUrl + "closeImg.png") no-repeat center;
      background-size: 100%;
      z-index: 10;
    }
  }
  .dialog_body {
    width: 100%;
    height: calc(100% - 40px);
    .body_top {
      padding: 20px 0 10px 0;
      .top_title {
        width: 100%;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-between);
        .small_title {
          font-size: 18px;
        font-weight: 600;
        color: #333;
        // background: url($imageUrl + 'small_title.png') no-repeat left
        //   25px;
        // background-size: 8px 8px;
        padding: 5px 0 6px 16px;
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
        .status {
          font-size: 20px;
          font-weight: 500;
          color: #333;
          span {
            font-weight: 500;
            color: #e95a3e;
            // color: #efc30a;
          }
        }
        .continue {
          font-size: 20px;
          font-weight: 500;
          color: #333;
        }
      }
      .top_msg {
        background: #f4f5f8;
        font-size: 14px;
        font-weight: 400;
        color: #333;
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
      width: 730px;
      height: 350px;
      #cxzyxWaterLevelChart {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>
<style lang='scss'>
.tooltip_box {
  .realtime {
    line-height: 1.5;
    color: #18dde2;
  }
  .normal {
    line-height: 1.5;
    color: #ef645b;
  }
  .limit {
    line-height: 1.5;
    color: #ed995f;
  }
  .fangLimit {
    line-height: 1.5;
    color: #d3312a;
  }
}
</style>