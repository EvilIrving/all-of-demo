<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-25 10:08:37
 * @Description: 纳蓄弹窗 
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\NxypPopup.vue
-->
<template>
  <div class="popup_map">
    <div class="dialog_title">
      <span style="cursor:pointer;" @click="lookDetail(popupData)">{{ popupData.NAME }}</span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '1'" v-html="'&nbsp[大(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '2'" v-html="'&nbsp[大(2)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '3'" v-html="'&nbsp[中型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '4'" v-html="'&nbsp[小(1)型]'"></span>
      <span style="font-size: 16px;" @click="lookDetail(popupData)" v-if="popupData.project_scale == '5'" v-html="'&nbsp[小(2)型]'"></span>
      <i class="dialog__close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_content">
        <p class="small_title">基本信息</p>
        <div class="left_item bg">
          <p>行政区划：</p>
          <span>{{popupData.adcdctiy}} - {{popupData.adcdcountry}}</span>
        </div>
        <div class="left_item ">
          <p>集雨面积：</p>
          <span>{{ popupData.RCA ? popupData.RCA + "km²" : "-" }}</span>
        </div>
        <div class="left_item bg">
          <p>水库功能：</p>
          <span class="ellipsis">{{ popupData.ACTFUNCMC ? popupData.ACTFUNCMC : "-" }}</span>
        </div>
        <!-- <div class="left_item">
          <p>水库规模：</p>
          <span>{{popupData.scale}}</span>
        </div> -->
        <p class="small_title">水库纳蓄</p>
        <div class="left_item bg">
          <p>时间：</p>
          <span>{{popupData.waterLevelTM}}</span>
        </div>
        <div class="left_item">
          <p>蓄水量(万m³)：</p>
          <span>{{popupData.capable}}</span>
        </div>
        <div class="left_item bg">
          <p>蓄水率(%)：</p>
          <span>{{popupData.storagePerc}}</span>
        </div>
        <div class="left_item">
          <p>死库容(万m³)：</p>
          <span>{{popupData.DDCP}}</span>
        </div>
        <div class="left_item bg">
          <p>正常库容(万m³)：</p>
          <span>{{popupData.CSCNWL}}</span>
        </div>
        <div class="left_item">
          <p>总库容(万m³)：</p>
          <span>{{popupData.TATOL_STORAGE}}</span>
        </div>
      </div>
      <div class="lines"></div>
      <div class="right_wrapper">
        <p class="small_title">蓄水变化过程曲线</p>
        <ul class="timesBtn">
          <li @click="activeIndex = index" :class="['time',{active:activeIndex===index}]" v-for="(item,index) in times" :key="index">{{item}}</li>
          <!-- <li class="time ">日</li>
          <li class="time">月</li>
          <li class="time">年</li> -->
        </ul>
        <div v-if="showChangeCurl" id="curveChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { statislistJson } from "@/api/api_reservoir";
export default {
  name: 'NxypPopup',
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
      activeIndex: 2,
      showChangeCurl: false,
      times: ['年', '月', '日'],
      maxValue: 0,
      minValue: 0,
      myChart: null,
      LineData: { capablelist: [], tmlist: [], capablePerclist: [] },
      option: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
          },
          padding: 5,
          textStyle: {
            fontSize: 16,
            lineHeight: 22
          },
        },
        legend: {
          show: true,
          type: 'plain',
          right: '90',
        },
        grid: {
          top: "32",
          left: "20",
          right: "20",
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
            data: [],
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
          {
            type: 'category',
            data: [],
            axisLabel: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            axisLine: {
              show: false,
            },
          },
        ],
        yAxis: [{
          type: 'value',
          name: '蓄水量（万m³）',
          nameLocation: 'end',
          min: 0,
          max: 0,
          splitNumber: 10,
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
            show: false,
            lineStyle: {
              type: 'dashed'
            }
          },
          splitArea: {
            show: false
          },
        }, {
          type: 'value',
          name: '蓄水率（%）',
          nameLocation: 'end',
          splitNumber: 10,
          min: 20,
          // max: 100,
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
            show: true,
          },
          axisLabel: {
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
        }],
        series: [
          {
            name: '蓄水量（万m³）',
            type: 'bar',
            color: '#2952D0',
            barMaxWidth: '16',
            yAxisIndex: 0,
            data: []
          }, {
            name: '蓄水率（%）',
            type: 'line',
            symbol: 'emptyCircle',
            showAllSymbol: true, //动画效果
            smooth: true, //光滑的曲线
            color: '#19B787FF',
            yAxisIndex: 1,
            data: []
          },
        ],
      }
    };
  },
  computed: {
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'nxypPopup') {
        this.showChangeCurl = false
        setTimeout(() => {
          this.showChangeCurl = true
          this.$nextTick(() => {
            this.initChart()
          })
        }, 1000)
      }
    },
    activeIndex(oldVal, newVal) {
      if (oldVal !== newVal) {
        this.updateChartView()
      }
    }
  },
  mounted() {
  },
  methods: {
    closePopup() {
      this.$parent.$parent.closeAllOverly()
    },
    // 弹窗内打开查看工程详情
    lookDetail(row) {
      this.$parent.$parent.$parent.openDetailPopup(row);
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
    async getData() {
      this.LineData.capablelist = []
      this.LineData.tmlist = []
      this.LineData.capablePerclist = []
      let opt = {
        prcd: this.popupData.PRCD,
        // 1 年 2 月 3 日
        type: this.activeIndex + 1,
      }
      let res = await statislistJson(opt)
      if (res.success) {
        let capablelist = []
        let tmlist = []
        let capablePerclist = []
        let compareArr = []
        res.rows.forEach(element => {
          if (element.capable) {
            let time = element.tm
            capablelist.push({ value: element.capable, time: element.tm })
            capablePerclist.push({ value: element.capablePerc, time: element.tm })
            compareArr.push(element.capable)
            tmlist.push(time)
          }
        })
        console.log(compareArr, 'compareArr');
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        this.LineData.capablelist = capablelist
        this.LineData.capablePerclist = capablePerclist
        this.LineData.tmlist = tmlist
      }
    },
    async updateChartView() {
      if (!this.myChart) return;
      await this.getData()
      console.log(this.minValue, 'compareArr', this.maxValue);
      this.option.xAxis[0].data = this.option.xAxis[1].data = this.LineData.tmlist
      this.option.series[0].data = this.LineData.capablelist
      this.option.series[1].data = this.LineData.capablePerclist
      this.option.yAxis[0].min = (this.minValue - (this.maxValue - this.minValue) - 6).toFixed(0)
      this.option.yAxis[0].max = (this.maxValue + (this.maxValue - this.minValue) + 6).toFixed(0)
      this.myChart.setOption(this.option, true);
    },
    async initChart() {
      await this.getData()
      this.myChart = this.$echarts.init(
        document.getElementById('curveChart')
      );
      this.option.xAxis[0].data = this.option.xAxis[1].data = this.LineData.tmlist
      this.option.series[0].data = this.LineData.capablelist
      this.option.series[1].data = this.LineData.capablePerclist
      this.option.yAxis[0].min = (this.minValue - (this.maxValue - this.minValue) - 6).toFixed(0)
      this.option.yAxis[0].max = (this.maxValue + (this.maxValue - this.minValue) + 6).toFixed(0)
      // this.option.yAxis[1].min = (this.minValue - (this.maxValue - this.minValue) * 1).toFixed(0)
      // this.option.yAxis[1].max = (this.maxValue + (this.maxValue - this.minValue) * 1).toFixed(0)
      this.myChart.setOption(this.option);
    },

  }
}
</script>

<style lang='scss' scoped>
.popup_map {
  z-index: 99;
  background: #fff;
  padding: 16px 20px 16px 20px;
  // @include shadow(#ccc, 0px, 0px, 2px);
  // background: url($imageUrl + "sk-popup-bg.png") no-repeat center;
  // background-size: 100% 100%;
  width: 920px;
  height: 460px;
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
    @include flexbox();
    .small_title {
      font-size: 16px;
      font-weight: 500;
      color: #333;
      // background: url($imageUrl + "small_title.png") no-repeat left 25px;
      // background-size: 8px 8px;
      padding: 8px 0 8px 12px;
      position: relative;
      &:before {
        top: 12px;
        left: 0px;
        position: absolute;
        display: block;
        width: 7px;
        height: 7px;
        background: #03338d;
        content: "";
      }
    }
    .body_content {
      width: 26%;

      .left_item {
        height: 38px;
        line-height: 38px;
        @include flexbox;
        padding-left: 5px;
        box-sizing: border-box;
        p {
          min-width: 70px;
          color: #333;
          font-size: 14px;
        }
        span {
          color: #333;
          font-size: 14px;
        }
        img {
          width: 80px;
          height: 71px;
          margin-top: 13px;
        }
        &.bg {
          // background: #03338d;
          box-sizing: border-box;
          background: #f4f5f8;
        }
      }
    }
    .lines {
      width: 2px;
      height: 100%;
      margin: 0 8px;
      background: #e0e3eb;
    }
    .right_wrapper {
      color: #000;
      width: 73%;
      height: 100%;
      .timesBtn {
        width: 14%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        li {
          width: 26px;
          height: 26px;
          font-size: 16px;
          line-height: 26px;
          text-align: center;
          background: #fff;
          border-radius: 4px;
          color: #000;
          &.active {
            color: #ffffff;
            background: #2952d0;
          }
        }
      }
      #curveChart {
        width: 96%;
        height: 85%;
      }
    }
  }
}
</style>