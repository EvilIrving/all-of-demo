<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-03 09:55:39
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/bigScreen/dike/dikeLeft/DikeLeft.vue
-->
<template>
  <div class="map_left">
    <section class="basin_box" ref="basinHeight">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        流域水系（条）
      </div>
      <div class="basin_list" v-loading="basinLoading">
        <div :class="['basin_item', basinIndex === index ? 'active' : '']" v-for="(item, index) in basinList" :key="index" @click="clickBasin(index)">
          <div class="basin_item_left">
            <img v-if="basinIndex === index" src="@/assets/images/basin1.png" alt="">
            <img v-else src="@/assets/images/basin0.png" alt="">
            <p>{{item.basinName}}</p>
          </div>
          <div class="basin_item_right">
            {{item.count}}<span>条</span>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <section class="adcd_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        行政区划（条）
        <div class="chart_clean" v-if="leftSelectAdcd != ''" @click="cleanAdcdClick">清除</div>
      </div>
      <basin-single-bar-chart v-loading="adcdChartLoading" class="leftChart" :chartData="adcdChartData" @barClick="adcdChartClick"></basin-single-bar-chart>
    </section>
    <div class="chart_line"></div>
    <section class="type_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        堤防长度 {{dikeLength}}（km）
      </div>
      <basin-single-bar-chart v-loading="lengthChartLoading" class="leftChart" :chartData="lengthChartData"></basin-single-bar-chart>
    </section>
  </div>
</template>

<script>
import BasinMultipleBarChart from "../components/BasinMultipleBarChart";
import BasinSingleBarChart from "../components/BasinSingleBarChart";
import { basinStatisticApi } from "@/api/api_basin";
import { dikeStatisticApi } from "@/api/api_dike";
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
        xShupai: true,
        unit: '条',
        xData: [],
        yData: [],
        adcdList: []
      },
      adcdChartLoading: true,
      lengthChartData: {
        id: 'leftLengthChart',
        // yInterval: 500,
        xShupai: true,
        unit: 'km',
        xData: [],
        yData: [],
        adcdList: []
      },
      lengthChartLoading: true,
      barHeight: "",
      superviseLevel: '',
      clickAdcd: '',
      dikeLength: 0
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    // leftSelectBasin() {
    //   return this.$store.state.leftSelectBasin;
    // },
  },
  watch: {
    leftSelectAdcd(val){
      this.getLengthChartData();
    }
  },
  mounted() {
    this.barHeight = (this.$parent.$refs.drawLeft.clientHeight - this.$refs.basinHeight.clientHeight) / 2 + "px";
    this.getBasinData();
    this.getAdcdChartData();
    this.getLengthChartData();
  },
  methods: {
    async getBasinData(){
      let res = await basinStatisticApi({
        projectCategory: '堤防',
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
        projectCategory: '堤防',
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
              if(res.data[i].list[j].projectCategory == '堤防'){
                this.adcdChartData.yData.push(res.data[i].list[j].num)
              }
            }
          }
        }
      }
      this.adcdChartLoading = false
    },
    async getLengthChartData(){
      this.lengthChartLoading = true
      let res = await dikeStatisticApi({
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
        statistic: "length",
        basin: this.basinCode,
        superviseLevel: this.superviseLevel
      })
      if(res.success){
        this.lengthChartData.xData = []
        this.lengthChartData.yData = []
        if(res.data.length){
          let num = 0
          for(let i = 0; i < res.data.length; i++){
            let name = res.data[i].SCALE_NAME
            switch(name) {
              case '1级堤防':
                name = '一级堤防';
                break;
              case '2级堤防':
                name = '二级堤防';
                break;
              case '3级堤防':
                name = '三级堤防';
                break;
              case '4级堤防':
                name = '四级堤防';
                break;
              case '5级堤防':
                name = '五级堤防';
                break;
            }
            this.lengthChartData.xData.push(name)
            this.lengthChartData.yData.push((res.data[i].DRAW_LENGTH / 1000).toFixed(2))
            num += res.data[i].DRAW_LENGTH
          }
          this.dikeLength = (num / 1000).toFixed(2)
        }
      }
      this.lengthChartLoading = false
    },
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
      this.getAdcdChartData()
      this.getLengthChartData()
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
