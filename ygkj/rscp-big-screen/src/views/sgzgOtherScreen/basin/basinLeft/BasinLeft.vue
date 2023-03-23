<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-15 14:15:30
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/rscpDriveScreen/basin/basinLeft/BasinLeft.vue
-->
<template>
  <div class="map_left">
    <section class="basin_box" ref="basinHeight">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        流域水系（座/条）
      </div>
      <div class="basin_list" v-loading="basinLoading">
        <div :class="['basin_item', basinIndex === index ? 'active' : '']" v-for="(item, index) in basinList" :key="index" @click="clickBasin(index)">
          <div class="basin_item_left">
            <img v-if="basinIndex === index" src="@/assets/images/basin1.png" alt="">
            <img v-else src="@/assets/images/basin0.png" alt="">
            <p>{{item.basinName}}</p>
          </div>
          <div class="basin_item_right">
            {{item.count}}
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <section class="adcd_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        行政区划（座/条）
        <div class="chart_clean" v-if="leftSelectAdcd != ''" @click="cleanAdcdClick">清除</div>
      </div>
      <basin-multiple-bar-chart ref="basinAdcdChart" v-loading="adcdChartLoading" class="leftChart" :chartData="adcdChartData" @barClick="adcdChartClick"></basin-multiple-bar-chart>
    </section>
    <div class="chart_line"></div>
    <section class="type_box" :style="{ height: barHeight }">
      <div class="chart_title">
        <img src="@/assets/images/chart_item_icon.png" alt="" />
        工程类别（座/条）
      </div>
      <basin-multiple-bar-chart v-loading="typeChartLoading" class="leftChart" :chartData="typeChartData"></basin-multiple-bar-chart>
    </section>
  </div>
</template>

<script>
import BasinMultipleBarChart from "../components/BasinMultipleBarChart";
import { basinStatisticApi } from "@/api/api_basin";
export default {
  name: "",
  props: {},
  components: {
    BasinMultipleBarChart
  },
  data() {
    return {
      basinIndex: '',
      basinList: [],
      basinCode: '',
      basinLoading: true,
      adcdChartData: {
        id: 'leftAdcdChart',
        color: ['#6e9ff1','#a3abd9','#81d8f4','#deb29c','#795ee5'],
        max: 5000,
        // yInterval: 500,
        xData: [],
        barSeries: [],
        adcdList: []
      },
      adcdChartLoading: true,
      typeChartData: {
        id: 'leftTypeChart',
        color: ['#e8efff','#d1dffa','#b7c9f9','#a0b7f6','#6da0f3'],
        max: 5000,
        xData: [],
        barSeries: []
      },
      typeChartLoading: true,
      barHeight: "",
      superviseLevel: ''
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
      this.getTypeChartData()
    },
    // leftSelectBasin(val){
    //   this.getAdcdChartData()
    //   this.getTypeChartData()
    // },
  },
  mounted() {
    this.barHeight = (window.innerHeight - this.$refs.basinHeight.clientHeight) / 2 + "px";
    this.getBasinData();
    this.getAdcdChartData();
    this.getTypeChartData();
  },
  methods: {
    async getBasinData(){
      let res = await basinStatisticApi({
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
        adcd: this.$localData("get", "userInfo").adcd,
        statistic: "adcd",
        basin: this.basinCode
      })
      if(res.success){
        this.adcdChartData.xData = []
        this.adcdChartData.barSeries = []
        this.adcdChartData.adcdList = []
        let typeList = {
          '水库': [],
          '堤防': [],
          '水闸': [],
          '泵站': [],
          '闸站': []
        }
        if(res.data.length){
          let totalData = []
          for(let i = 0; i < res.data.length; i++){
            this.adcdChartData.xData.push(res.data[i].adnm)
            this.adcdChartData.adcdList.push(res.data[i].adcd)
            totalData.push(res.data[i].total)
            for(let j = 0; j < res.data[i].list.length; j++){
              typeList[res.data[i].list[j].projectCategory].push(res.data[i].list[j].num)
            }
          }
          for(let key in typeList){
            this.adcdChartData.barSeries.push({
              name: key,
              type: "bar",
              barWidth: 15,
              // color: '#7A5AE6',
              stack: 'total',
              data: typeList[key],
              label: {
                show: false,
                position: 'center',
                color: '#333',
                fontWeight: 500
              },
            })
          }
          this.adcdChartData.barSeries.push({
            name: '总计',
            type: 'bar',
            stack: 'total',
            label: {
              normal: {
                offset: ['50', '80'],
                show: true,
                position: 'insideBottom',
                formatter: '{c}',
                textStyle: {
                    color: '#000'
                }
              }
            },
            itemStyle: {
              normal: {
                color: 'rgba(128, 128, 128, 0)'
              }
            },
            data: totalData
          })
          let _totalData = JSON.parse(JSON.stringify(totalData))
          // let up = _totalData.sort(function (a, b) {
          //   return b - a;
          // })[0]
          // up = (up / 6).toFixed(0)
          // this.adcdChartData.max = _totalData.sort(function (a, b) {
          //   return b - a;
          // })[0] + Number(up);
          this.adcdChartData.max = Number(this.randomNum(_totalData))
        }
      }
      this.adcdChartLoading = false
    },
    async getTypeChartData(){
      this.typeChartLoading = true
      let res = await basinStatisticApi({
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
        statistic: "projectScale",
        basin: this.basinCode,
        superviseLevel: this.superviseLevel
      })
      if(res.success){
        this.typeChartData.xData = []
        this.typeChartData.barSeries = []
        this.typeChartData.adcdList = []
        let typeList = {
          '1': [],
          '2': [],
          '3': [],
          '4': [],
          '5': []
        }
        if(res.data.length){
          let totalData = []
          for(let i = 0; i < res.data.length; i++){
            totalData.push(res.data[i].total)
            this.typeChartData.xData.push(res.data[i].projectCategory)
            for(let j = 0; j < res.data[i].list.length; j++){
              typeList[res.data[i].list[j].projectScale].push(res.data[i].list[j].num)
            }
          }
          for(let key in typeList){
            let keyName = ''
            switch (key){
              case '1':
                keyName = '大(1)型'
                break;
              case '2':
                keyName = '大(2)型'
                break;
              case '3':
                keyName = '中型'
                break;
              case '4':
                keyName = '小(1)型'
                break;
              case '5':
                keyName = '小(2)型'
                break;

            }
            this.typeChartData.barSeries.push({
              name: keyName,
              type: "bar",
              barWidth: 15,
              // color: '#7A5AE6',
              stack: 'total',
              data: typeList[key],
              label: {
                show: false,
                // position: 'top',
                color: '#333',
                fontWeight: 500
              },
            })
          }
          this.typeChartData.barSeries.reverse()
          this.typeChartData.barSeries.push({
            name: '总计',
            type: 'bar',
            stack: 'total',
            label: {
              normal: {
                offset: ['50', '80'],
                show: true,
                position: 'insideBottom',
                formatter: '{c}',
                textStyle: {
                    color: '#000'
                }
              }
            },
            itemStyle: {
              normal: {
                color: 'rgba(128, 128, 128, 0)'
              }
            },
            data: totalData
          })
          let _totalData = JSON.parse(JSON.stringify(totalData))
          // let up = _totalData.sort(function (a, b) {
          //   return b - a;
          // })[0]
          // up = (up / 6).toFixed(0)
          // this.typeChartData.max = _totalData.sort(function (a, b) {
          //   return b - a;
          // })[0] + Number(up);
          this.typeChartData.max = Number(this.randomNum(_totalData))
        }
      }
      this.typeChartLoading = false
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
      this.getTypeChartData()
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
      // this.$refs.basinAdcdChart.initChart()
      this.getAdcdChartData()
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
  // height: calc(100% - 40px);
  // height: var (--barChartHeight);
}
</style>
