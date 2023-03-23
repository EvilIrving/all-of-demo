<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.NAME }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <span class="adcd">{{popupData.cityADNM}} - {{popupData.countryADNM}}</span>
      <i class="el-icon-close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_top">
        <div class="top_title">
          <!-- <p class="small_title">基本信息</p> -->
          <p class="status">当前状态：<span>溢洪中</span></p>
          <p class="continue">持续时间：<span>{{ detailData.ltHours ? detailData.ltHours : '-' }}小时</span></p>
        </div>
        <div class="top_msg">
          <p>当前水位：
            <span>{{ detailData.waterLevel ? detailData.waterLevel.toFixed(2) + "m" : "-" }} </span>
          </p>
          <p>当前时间：
            <span>{{ detailData.waterLevelTM ? detailData.waterLevelTM.slice(0,16):"-" }} </span>
          </p>
          <p>工程限制水位：
            <span>{{ detailData.limitWaterLevel ? detailData.limitWaterLevel.toFixed(2) + "m" : "-" }} </span>
          </p>
          <p>设计洪水位：
            <span>{{ detailData.DSFLZ ? detailData.DSFLZ.toFixed(2) + "m" : "-" }} </span>
          </p>
        </div>
      </div>
      <div class="body_chart">
        <!-- <WaterLevelChart v-if="showChart"/> -->
        <div v-if="showChart" id="waterLevelChart"></div>
      </div>

    </div>
  </div>
</template>

<script>
import { floodListYihong } from "@/api/api_reservoir";
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
      maxValue: 0,
      minValue: 0,
      detailData: {}
    };
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'floodControlYihongPopup') {
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
        prcd: this.popupData.PRCD,
      }
      let res = await floodListYihong(opt)
      if (res.success) {
        this.detailData = res.date
        this.wtele = Number(res.date.wtele)
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
        compareArr.push(this.wtele)
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        this.LineData.rzlist = rzlist
        this.LineData.tmlist = tmlist
        console.log(compareArr, 'compareArr');
      }
    },
    async initChart() {
      await this.getData()
      let myChart = this.$echarts.init(
        document.getElementById('waterLevelChart')
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
            let tooltipObj = [
              {
                name: '实时水位',
                value: params[0].value,
                class: 'realtime'
              },
              {
                name: '溢洪道堰顶高程',
                value: this.wtele,
                class: 'limit'
              }
            ]
            tooltipObj.sort((a, b) => b.value - a.value)
            return `
                     <div class='tooltip_box'>
                        <h4>${params[0].data.time}</h4>
                        <p class="${tooltipObj[0].class}">${tooltipObj[0].name}：${tooltipObj[0].value}m</p>
                        <p class="${tooltipObj[1].class}">${tooltipObj[1].name}：${tooltipObj[1].value}m</p>
                      </div>
                    `
          }
        },
        grid: {
          top: "32",
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
              show: false,
            },
          },
        ],
        yAxis: {
          type: 'value',
          name: '水位m',
          min: (this.minValue - (this.maxValue - this.minValue) * 1).toFixed(2),
          max: (this.maxValue + (this.maxValue - this.minValue) * 1).toFixed(2),
          // splitNumber : 20,
          nameTextStyle: {
            color: "#fff",
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
                color: "#333",
              }
          },
          splitLine: {
            show: true,
            lineStyle: {
              type: 'dashed'
            }
          },
          splitArea: {
            show: false
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
                  yAxis: this.wtele,
                  label: {
                    position: 'insideEndBottom',
                    distance: [0, 3],
                    formatter: function (data) {
                      return "溢洪道堰顶高程";
                    }
                  },
                  lineStyle: {
                    color: "#ED995F",
                    width: 2,
                  }
                }
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
      &.adcd {
        font-size: 16px;
        margin-left: 15px;
        color: #18dde2;
      }
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
          font-weight: 500;
          color: #333;
          // background: url($imageUrl + "small_title.png") no-repeat left 10px;
          // background-size: 8px 8px;
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
        .status {
          font-size: 20px;
          font-weight: 500;
          color: #333;
          span {
            font-weight: 500;
            color: #ed995f;
          }
        }
        .continue {
          font-size: 20px;
          font-weight: 500;
          color: #333;
        }
      }
      .top_msg {
        // background: #03338d;
        border: 1px solid #ccc;
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
      #waterLevelChart {
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