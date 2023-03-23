<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-03 14:08:33
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/bigScreen/gate/gateLeft/GateLeft.vue
-->
<template>
  <div class="map_left">
    <section class="basin_box" ref="basinHeight">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        流域水系（座）
      </div>
      <div class="basin_list" v-loading="basinLoading">
        <div :class="['basin_item', basinIndex === index ? 'active' : '']" v-for="(item, index) in basinList" :key="index" @click="clickBasin(index)">
          <div class="basin_item_left">
            <img v-if="basinIndex === index" src="@/assets/images/basin1.png" alt="">
            <img v-else src="@/assets/images/basin0.png" alt="">
            <p>{{item.basinName}}</p>
          </div>
          <div class="basin_item_right">
            {{item.count}}<span>座</span>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <section class="adcd_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        行政区划（座）
        <div class="chart_clean" v-if="leftSelectAdcd != ''" @click="cleanAdcdClick">清除</div>
      </div>
      <basin-single-bar-chart v-loading="adcdChartLoading" class="leftChart" :chartData="adcdChartData" @barClick="adcdChartClick"></basin-single-bar-chart>
    </section>
    <div class="chart_line"></div>
    <section class="type_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        <!-- 过闸流量（千m³/s）  /  过闸功率（万kW） -->
        规模及数量（座）
        <div class="chart_clean" v-if="projectLeftScale != ''" @click="cleanScaleClick">清除</div>
      </div>
      <!-- <basin-multiple-bar-chart v-loading="flowChartLoading" class="leftChart" :chartData="flowChartData"></basin-multiple-bar-chart> -->
      <basin-single-bar-chart v-loading="scaleLoading" class="leftChart" :chartData="scaleData" @barClick="scaleChartClick"></basin-single-bar-chart>
    </section>
  </div>
</template>

<script>
import BasinMultipleBarChart from "../components/BasinMultipleBarChart";
import BasinSingleBarChart from "../components/BasinSingleBarChart";
import { basinStatisticApi } from "@/api/api_basin";
import { gateStatisticApi } from "@/api/api_gate";
export default {
  name: "",
  props: {},
  components: {
    BasinMultipleBarChart,
    BasinSingleBarChart
  },
  data() {
    return {
      basinIndex: '',
      basinList: [],
      basinCode: '',
      basinLoading: true,

      adcdChartData: {
        id: 'leftAdcdChart',
        // yInterval: 500,
        unit: '座',
        xData: [],
        yData: [],
        adcdList: [],
        xShupai: true
      },
      adcdChartLoading: true,

      scaleData: {
        id: 'leftScaleChart',
        // yInterval: 500,
        unit: '座',
        xData: [],
        yData: [],
        scaleList: []
      },
      scaleLoading: true,

      // flowChartData: {
      //   id: 'leftLengthChart',
      //   // yInterval: 500,
      //   unit: '',
      //   xData: [],
      //   barSeries: [],
      // },
      // flowChartLoading: true,

      barHeight: "",
      superviseLevel: '',
      clickAdcd: ''
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    projectLeftScale() {
      return this.$store.state.projectLeftScale;
    },
    // leftSelectBasin() {
    //   return this.$store.state.leftSelectBasin;
    // },
  },
  watch: {
    leftSelectAdcd(val){
      // this.getFlowChartData();
      this.getScaleChartData();
    }
  },
  mounted() {
    this.barHeight = (this.$parent.$refs.drawLeft.clientHeight - this.$refs.basinHeight.clientHeight) / 2 + "px";
    this.getBasinData();
    this.getAdcdChartData();
    // this.getFlowChartData();
    this.getScaleChartData();
  },
  methods: {
    async getBasinData(){
      let res = await basinStatisticApi({
        projectCategory: '闸站',
        statistic: "basin",
        adcd: this.$localData("get", "userInfo").adcd
      })
      if(res.success){
        this.basinList = res.data
        this.$store.commit('SET_BASIN_LIST',this.basinList)
      }
      this.basinLoading = false
    },
    async getAdcdChartData(){
      this.adcdChartLoading = true
      let res = await basinStatisticApi({
        projectCategory: '闸站',
        adcd: this.$localData("get", "userInfo").adcd,
        statistic: "adcd",
        basin: this.basinCode,
      })
      if(res.success){
        this.adcdChartData.xData = []
        this.adcdChartData.yData = []
        this.adcdChartData.adcdList = []
        if(res.data.length){
          for(let i = 0; i < res.data.length; i++){
            this.adcdChartData.xData.push(res.data[i].adnm.replace(/市/, ""))
            this.adcdChartData.adcdList.push(res.data[i].adcd)
            for(let j = 0; j < res.data[i].list.length; j++){
              if(res.data[i].list[j].projectCategory == '闸站'){
                this.adcdChartData.yData.push(res.data[i].list[j].num)
              }
            }
          }
        }
      }
      this.adcdChartLoading = false
    },

    async getScaleChartData(){
      this.scaleLoading = true;
      let res = await basinStatisticApi({
        statistic: "typeScale",
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
        basin: this.basinCode,
        projectCategory: '闸站',
        superviseLevel: this.superviseLevel
      })
      if (res.success) {
        let xData = [];
        let yData = [];
        let scaleList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            xData.push(res.data[i].projectScale.toString().searchSacle());
            yData.push(res.data[i].num);
            scaleList.push(res.data[i].projectScale);
          }
        }
        this.scaleData.xData = xData;
        this.scaleData.yData = yData;
        this.scaleData.scaleList = scaleList;
      }
      this.scaleLoading = false;
    },

    // async getFlowChartData(){
    //   this.flowChartLoading = true
    //   let res = await gateStatisticApi({
    //     adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
    //     statistic: "power",
    //     basin: this.basinCode,
    //     superviseLevel: this.superviseLevel
    //   })
    //   if(res.success){
    //     console.log(res,'---');
    //     this.flowChartData.xData = []
    //     let yData1 = []
    //     let yData2 = []
    //     if(res.data.length){
    //       for(let i = 0; i < res.data.length; i++){
    //         this.flowChartData.xData.push(res.data[i].SCALE_NAME.replace(/（/, "(").replace(/）/, ")"))
    //         // yData1.push(res.data[i].MAX_FLOW)
    //         yData1.push((res.data[i].MAX_FLOW / 1000).toFixed(2))
    //         // yData2.push(res.data[i].MOTOR_POWER)
    //         yData2.push((res.data[i].MOTOR_POWER / 10000).toFixed(2))
    //       }
    //     }
    //     this.flowChartData.barSeries[0] = {
    //       name: '过闸流量(千m³/s)',
    //       type: "bar",
    //       barWidth: 15,
    //       color: '#6e9ff2',
    //       stack: 'zjll',
    //       data: yData1,
    //       label: {
    //         show: false,
    //         position: 'center',
    //         color: '#333',
    //         fontWeight: 500
    //       },
    //     }
    //     this.flowChartData.barSeries[1] = {
    //       name: '过闸功率(万kW)',
    //       type: "bar",
    //       barWidth: 15,
    //       color: '#7A5AE6',
    //       stack: 'zjgl',
    //       data: yData2,
    //       label: {
    //         show: false,
    //         position: 'center',
    //         color: '#333',
    //         fontWeight: 500
    //       },
    //     }
    //   }
    //   this.flowChartLoading = false
    // },

    // 流域点击事件
    clickBasin(index) {
      if (this.basinIndex === index) {
        this.basinIndex = "";
        this.basinCode = ""
        this.$store.commit("SET_LEFT_SELECT_BASIN", "");
      } else {
        this.basinIndex = index;
        this.basinCode = this.basinList[this.basinIndex].basinCode
        this.$store.commit("SET_LEFT_SELECT_BASIN", this.basinCode);
      }
      this.getAdcdChartData();
      // this.getFlowChartData();
      this.getScaleChartData();
    },
    // 行政区划点击事件
    adcdChartClick(index) {
      if (this.adcdChartData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        this.superviseLevel = '1'
      } else {
        this.$store.commit("SET_LEFT_SELECT_ADCD", this.adcdChartData.adcdList[index]);
        this.superviseLevel = ''
      }
    },
    //清除行政区划点击
    cleanAdcdClick(){
      this.$store.commit("SET_LEFT_SELECT_ADCD", '');
    },

    // 工程规模点击事件
    scaleChartClick(index){
      this.$store.commit("SET_PROJECR_LEFT_SCALE", this.scaleData.scaleList[index]);
    },
    // 工程规模点击事件
    cleanScaleClick(){
      this.$store.commit("SET_PROJECR_LEFT_SCALE", '');
    },
    
    randomNum(arr) {
      //在数组中找到最大的值并处理
      let maxNum = arr[0];
      arr.forEach((item, index) => {
        if (item > maxNum) {
          maxNum = item;
        }
      });
      let maxNumLength = maxNum.toString().split("");
      let flag = true;
      let numCount = "";
      for (let i = 0; i < maxNumLength.length; i++) {
        if (i > 0) {
          numCount += "0";
          if (maxNumLength[i] != 0) {
            flag = false;
          }
        }
      }
      if (!flag) {
        maxNumLength[0] = Number(maxNumLength[0]) + 1;
        maxNum = maxNumLength[0] + numCount;
      }
      return maxNum
    },
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  z-index: 1002;
  .basin_box {
    height: 300px;
    .basin_list {
      @include flexbox();
      @include flexAC();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 0 16px;
      .basin_item {
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
        border-radius: 4px;
        // height: 96px;
        margin-bottom: 10px;
        cursor: pointer;
        padding: 8px 4px;
        box-sizing: border-box;
        color: #2F3C4F;
        width: 48.5%;
        @include flexbox();
        @include flexAC();
        @include flexJC(space-between);
        .basin_item_left {
          @include flexbox();
          @include flexAC();
          @include flexJC(center);
          // padding-left: 32px;
          font-size: 14px;
          img {
            margin-right: 5px;
            width: 20px;
            height: 20px;
          }
        }
        .basin_item_right {
          font-size: 20px;
          line-height: 20px;
          font-weight: 700;
          text-align: center;
          span {
            font-size: 14px;
            padding-left: 3px;
          }
        }
        &.active {
          color: #193BC4;
          border: 1px solid #193BC4;
          background: #EDF0FB;
        }
      }
    }
  }
}
.leftChart {
  height: calc(100% - 40px);
  // height: var (--barChartHeight);
}
</style>
