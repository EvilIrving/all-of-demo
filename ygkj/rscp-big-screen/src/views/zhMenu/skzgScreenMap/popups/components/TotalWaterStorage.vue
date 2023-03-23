<!--
 * @Date: 2022-09-21 18:04:47
 * @Author: 
 * @Description: 蓄水总量弹窗
 * @LastEditors: dtb
 * @LastEditTime: 2022-11-11 17:29:27
 * @FilePath: \rscp-big-screen\src\views\skzgScreen\skzgScreenMap\popups\components\TotalWaterStorage.vue
-->

<template>
  <div class="popup_map">
    <div class="dialog_title">
      <i class="el-dialog__close" @click="closePopup()"></i>
    </div>
    <div class="dialog_body">
      <div class="body_content">
        <div class="map_switch">
          <div class="">
            <h2 :title="cascaderCheckedName " class="adnm_switch">{{cascaderCheckedName}}
              <i v-show="showDropDir" @click.stop="showDropDir = false,showList=false" class="dropdown cursor" key="dropdown"></i>
              <i v-show="!showDropDir" @click.stop="showDropDir = true,showList=true" class="dropup cursor" key="dropup"></i>
            </h2>
            <div class="false_box">
              <el-cascader-panel class='adcd_box' v-model="cascaderChecked" v-show="showList" ref="cascaderXZ" :options="adcdList" :props="{checkStrictly: true,expandTrigger:'click',emitPath:false }" clearable :show-all-levels="false" @change="changeCascader">
              </el-cascader-panel>
            </div>
          </div>
        </div>
        <div class="small_tlt">
          <span>蓄水总量</span> <span class="blue">{{lastestXsQuantity}}亿m³</span>
          <span>蓄水率 </span> <span class="blue"> {{lastestXsPerc}}%</span>
          <span class="blue" style="float:right"> {{lastestTime}}</span> <span style="float:right">当前时间：</span>
        </div>
      </div>
      <div class="lines"></div>
      <div class="right_wrapper">
        <ul class="timesBtn">
          <li @click="activeIndex = index" :class="['time',{active:activeIndex===index}]" v-for="(item,index) in times" :key="index">{{item}}</li>
          <!-- <li class="time ">日</li>
          <li class="time">月</li>
          <li class="time">年</li> -->
        </ul>
        <div v-if="showChangeCurl" id="adcdCurveChart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { statislistJson } from "@/api/api_reservoir";
import { getAdcdTree } from '@/api/api_utils'
export default {
  name: 'XsQuantity',
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
      showList: false,
      times: ['年', '月', '日'],
      maxValue: 0,
      adcdList: [],
      showDropDir: false,//显示上下拉箭头
      lastestXsQuantity: "",
      lastestXsPerc: "",
      lastestTime: "",
      userAdcd: this.$localData("get", "userInfo").adcd,
      userAdnm: this.$localData("get", "userInfo").adnm,
      minValue: 0,
      cascaderChecked: '',
      cascaderCheckedName: '',
      LineData: { capablelist: [], tmlist: [], capablePerclist: [] },
      myChart: null,
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
          left: "5",
          right: "5",
          bottom: "30",
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
          name: '蓄水量（亿m³）',
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
            name: '蓄水量（亿m³）',
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
    // cascaderChecked() {
    //   return this.popupData.adcd || '';
    // },
    // cascaderCheckedName() {
    //   return this.popupData.adnm || '';
    // },
  },
  watch: {
    popupData(val) {
      if (val.mapType == 'XsQuantity') {
        this.cascaderChecked = val.adcd || '',
          this.cascaderCheckedName = val.adnm || '',
          this.showChangeCurl = false
        this.getAdcdOptions()
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
    showSwitchBtn(flag) {
      this.showDropDir = !flag
    },
    // 改变行政区划事件
    changeCascader(itemVlaue) {
      this.showList = false;
      this.showDropDir = false;
      let node = this.$refs.cascaderXZ.getCheckedNodes()
      this.cascaderCheckedName = node[0].label
      this.cascaderChecked = itemVlaue
      this.updateChartView()
    },
    // 地图筛选
    async getAdcdOptions() {
      let res = await getAdcdTree({
        adcd: this.userAdcd,
        fetchAll: true
      })
      if (res.success) {
        let adcdAllName = ''
        if (this.userAdcd.endsWith("0000000000")) {
          adcdAllName = "浙江省"
        } else if (this.userAdcd.endsWith("00000000")) {
          adcdAllName = "全市"
        } else {
          adcdAllName = "全区/县"
        }
        let temp = [{
          label: adcdAllName,
          value: this.userAdcd,
          checked: true,
        }]
        for (let i = 0; i < res.adcdTree.children.length; i++) {
          if (res.adcdTree.children[i].label == '江干区') {
            res.adcdTree.children.splice(i, 1)
          }
          // for (let j = 0; j < res.adcdTree.children[i].children.length; j++) {
          //   if (res.adcdTree.children[i].children[j].label == '江干区') {
          //     res.adcdTree.children[i].children.splice(j, 1)
          //   }
          // }
        }
        this.adcdList = [...temp, ...res.adcdTree.children]
      }
    },
    closePopup() {
      this.showList = false;
      this.showDropDir = false;
      this.showChangeCurl = false
      this.$parent.$parent.closeAllOverly()
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
        adcd: this.cascaderChecked,
        type: this.activeIndex + 1,
      }
      let res = await statislistJson(opt)
      if (res.success) {
        let capablelist = []
        let tmlist = []
        let capablePerclist = []
        let compareArr = []
        let percCompareArr = []
        res.rows.forEach((element, index) => {
          if (element.capable) {
            let time = element.tm
            capablelist.push({ value: (element.capable / 10000).toFixed(2), time: element.tm })
            capablePerclist.push({ value: element.capablePerc, time: element.tm })
            compareArr.push(element.capable / 10000)
            percCompareArr.push(element.capablePerc)
            tmlist.push(time)
            if (res.rows.length - 1 === index) {
              this.lastestXsQuantity = (element.capable / 10000).toFixed(2)
              this.lastestXsPerc = element.capablePerc
              this.lastestTime = element.tm
            }
          }
        })
        this.minValue = this.arrayMin(compareArr)
        this.maxValue = this.arrayMax(compareArr)
        // this.maxValue = this.arrayMin(percCompareArr)
        // this.maxValue = this.arrayMax(percCompareArr)
        this.LineData.capablelist = capablelist
        this.LineData.capablePerclist = capablePerclist

        this.LineData.tmlist = tmlist
      }
    },
    // 更新视图
    async updateChartView() {
      if (!this.myChart) return;
      await this.getData()
      this.option.xAxis[0].data = this.option.xAxis[1].data = this.LineData.tmlist
      this.option.series[0].data = this.LineData.capablelist
      this.option.series[1].data = this.LineData.capablePerclist
      this.option.yAxis[0].min = (this.minValue - (this.maxValue - this.minValue) - 6).toFixed(0)
      this.option.yAxis[0].max = (this.maxValue + (this.maxValue - this.minValue) + 6).toFixed(0)
      this.myChart.setOption(this.option, true);
    },

    // 初始化
    async initChart() {
      await this.getData()
      this.myChart = this.$echarts.init(
        document.getElementById('adcdCurveChart')
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
  width: 760px;
  height: 500px;
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
    // @include flexbox();

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
      width: 100%;
      .map_switch {
        margin-left: 20px;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 100;
        .adnm_switch {
          font-size: 26px;
          font-family: youshe;
          color: #000;
          margin-top: 16px;
          line-height: 46px;
          position: relative;
          width: auto;
          i {
            width: 20px;
            height: 20px;
          }
          .dropdown {
            display: inline-block;
            background: center/100% url($imageUrl + "todown.png") no-repeat;
          }
          .dropup {
            display: inline-block;
            background: center/100% url($imageUrl + "todown.png") no-repeat;
            transform: rotate(180deg);
          }
        }
        .false_box {
          position: absolute;
          top: 0px;
          left: 100px;
          .adcd_box {
            margin-left: 20px;
            height: 100%;
          }
        }
      }
      .small_tlt {
        width: 100%;
        height: 30px;
        color: #000;
        span {
          font-size: 16px;
          font-weight: 600;
          &.blue {
            color: #2952d0;
            padding-right: 20px;
          }
        }
      }
    }
    .lines {
      width: 100%;
      height: 1px;
      margin: 6px 0px;
      background: #e0e3eb;
    }
    .right_wrapper {
      color: #000;
      width: 100%;
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
      #adcdCurveChart {
        width: 96%;
        height: 85%;
      }
    }
  }
}
</style>