<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-03 09:55:39
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/bigScreen/dike/dikeLeft/DikeLeft.vue
-->
<template>
  <div class="map_left" ref="mapLeft">
    <section class="basin_box" ref="basinHeight">
      <div class="chart_title">
        流域水系(条)
      </div>
      <div class="basin_list" v-loading="basinLoading"  element-loading-background="rgba(0, 0, 0, 0.2)">
        <div :class="['basin_item', basinIndex === index ? 'active' : '']" v-for="(item, index) in basinList" :key="index" @click="clickBasin(index)">
          <div class="basin_item_left">
            <img :src="$config.swImgUrl + 'basin-item-icon.png'" alt="">
            <p>{{item.basinName}}</p>
          </div>
          <div class="basin_item_right">
            {{item.count}}
          </div>
        </div>
      </div>
    </section>
    <section class="adcd_box" :style="{ height: barHeight }">
      <div class="chart_title">
        行政区划(条)
        <div class="chart_clean" v-if="leftSelectAdcd != ''" @click="cleanAdcdClick">清除</div>
      </div>
      <basin-single-bar-chart v-loading="adcdChartLoading"  element-loading-background="rgba(0, 0, 0, 0.2)"  class="leftChart" :chartData="adcdChartData" @barClick="adcdChartClick"></basin-single-bar-chart>
    </section>
    <section class="type_box" :style="{ height: barHeight }">
      <div class="chart_title">
        堤防长度 {{dikeLength}}(km)
      </div>
      <basin-single-bar-chart v-loading="lengthChartLoading" element-loading-background="rgba(0, 0, 0, 0.2)" class="leftChart" :chartData="lengthChartData"></basin-single-bar-chart>
    </section>
  </div>
</template>

<script>
import BasinSingleBarChart from "../components/BasinSingleBarChart";
import { basinStatisticApi } from "@/api/api_basin";
import { dikeStatisticApi } from "@/api/api_dike";
export default {
  name: "",
  props: {},
  components: {
    BasinSingleBarChart
  },
  data() {
    return {
      basinIndex: "",
      basinList: [],
      basinCode: "",
      basinLoading: true,
      adcdChartData: {
        id: "leftAdcdChart",
        // yInterval: 500,
        xShupai: true,
        unit: "条",
        xData: [],
        yData: [],
        adcdList: []
      },
      adcdChartLoading: true,
      lengthChartData: {
        id: "leftLengthChart",
        // yInterval: 500,
        xShupai: false,
        unit: "km",
        xData: [],
        yData: [],
        adcdList: []
      },
      lengthChartLoading: true,
      barHeight: "",
      superviseLevel: "",
      clickAdcd: "",
      dikeLength: 0
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    }
    // leftSelectBasin() {
    //   return this.$store.state.leftSelectBasin;
    // },
  },
  watch: {
    leftSelectAdcd() {
      this.getLengthChartData();
    }
  },
  mounted() {
    this.getBarHeight();
    window.addEventListener("resize", this.getBarHeight);
    this.getBasinData();
    this.getAdcdChartData();
    this.getLengthChartData();
  },
  destroyed() {
    window.removeEventListener("resize", this.getBarHeight);
  },
  methods: {
    getBarHeight() {
      this.barHeight =
        (this.$refs.mapLeft.clientHeight -
          this.$refs.basinHeight.clientHeight) /
          2 +
        "px";
    },
    async getBasinData() {
      let res = await basinStatisticApi({
        projectCategory: "堤防",
        statistic: "basin",
        adcd: this.$localData("get", "userInfo").adcd
      });
      if (res.success) {
        this.basinList = res.data;
        this.$store.commit("SET_BASIN_LIST", this.basinList);
      }
      this.basinLoading = false;
    },
    async getAdcdChartData() {
      this.adcdChartLoading = true;
      let res = await basinStatisticApi({
        projectCategory: "堤防",
        adcd: this.$localData("get", "userInfo").adcd,
        statistic: "adcd",
        basin: this.basinCode
      });
      if (res.success) {
        this.adcdChartData.xData = [];
        this.adcdChartData.yData = [];
        this.adcdChartData.adcdList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            this.adcdChartData.xData.push(res.data[i].adnm.replace(/市/, ""));
            this.adcdChartData.adcdList.push(res.data[i].adcd);
            for (let j = 0; j < res.data[i].list.length; j++) {
              if (res.data[i].list[j].projectCategory == "堤防") {
                this.adcdChartData.yData.push(res.data[i].list[j].num);
              }
            }
          }
        }
      }
      this.adcdChartLoading = false;
    },
    async getLengthChartData() {
      this.lengthChartLoading = true;
      let res = await dikeStatisticApi({
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        statistic: "length",
        basin: this.basinCode,
        superviseLevel: this.superviseLevel
      });
      if (res.success) {
        this.lengthChartData.xData = [];
        this.lengthChartData.yData = [];
        if (res.data.length) {
          let num = 0;
          for (let i = 0; i < res.data.length; i++) {
            let name = res.data[i].SCALE_NAME;
            switch (name) {
              case "1级堤防":
                name = "一级堤防";
                break;
              case "2级堤防":
                name = "二级堤防";
                break;
              case "3级堤防":
                name = "三级堤防";
                break;
              case "4级堤防":
                name = "四级堤防";
                break;
              case "5级堤防":
                name = "五级堤防";
                break;
            }
            this.lengthChartData.xData.push(name);
            this.lengthChartData.yData.push(
              (res.data[i].DRAW_LENGTH / 1000).toFixed(2)
            );
            num += res.data[i].DRAW_LENGTH;
          }
          this.dikeLength = (num / 1000).toFixed(2);
        }
      }
      this.lengthChartLoading = false;
    },
    // 流域点击事件
    clickBasin(index) {
      if (this.basinIndex === index) {
        this.basinIndex = "";
        this.basinCode = "";
        this.$store.commit("SET_LEFT_SELECT_BASIN", "");
      } else {
        this.basinIndex = index;
        this.basinCode = this.basinList[this.basinIndex].basinCode;
        this.$store.commit("SET_LEFT_SELECT_BASIN", this.basinCode);
      }
      this.getAdcdChartData();
      this.getLengthChartData();
    },
    // 行政区划点击事件
    adcdChartClick(index) {
      if (this.adcdChartData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        this.superviseLevel = "1";
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.adcdChartData.adcdList[index]
        );
        this.superviseLevel = "";
      }
    },
    //清除行政区划点击
    cleanAdcdClick() {
      this.$store.commit("SET_LEFT_SELECT_ADCD", "");
    },
    randomNum(arr) {
      //在数组中找到最大的值并处理
      let maxNum = arr[0];
      arr.forEach(item => {
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
      return maxNum;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../style/common.scss";
.map_left {
  z-index: 1002;
  height: 100%;
  .chart_title {
    @include chartTitle();
    .chart_clean {
      @include chartClean();
    }
  }
  .basin_box {
    height: 303px;
    padding-bottom: 13px;
    .basin_list {
      @include basinList();
      height: 260px;
      .basin_item {
        @include basinItem();
        .basin_item_left {
          @include basinItemLeft();
        }
        .basin_item_right {
          @include basinItemRight();
        }
        &.active {
          @include basinItemActive();
          .basin_item_left {
            @include basinItemLeftActive();
          }
          .basin_item_right {
            @include basinItemRightActive();
          }
        }
      }
    }
  }
}
.leftChart {
  height: calc(100% - 42px);
}
</style>
