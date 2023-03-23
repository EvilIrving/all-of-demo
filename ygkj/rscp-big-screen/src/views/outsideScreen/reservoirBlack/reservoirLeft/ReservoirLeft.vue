<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-15 10:53:12
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/outsideScreen/reservoirBlack/reservoirLeft/ReservoirLeft.vue
-->
<template>
  <div class="map_left" >
    <div class="scale_box" v-loading="skgmLoading" ref="reservoirHeight">
      <div class="chart_title_black">
        水库规模
      </div>
      <div class="scale_list" >
        <div
          :class="[
            'scale_item',
            index === scaleIndex ? 'active' : '',
            index == 0 || index == 1 ? 'width50' : 'width33',
          ]"
          v-for="(item, index) in skgmList"
          :key="index"
          @click="clickScale(index)"
        >
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>座</span></div>
        </div>
      </div>
    </div>
    <!-- <div class="chart_line"></div> -->
    <bar-chart
      v-loading="skfbLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="skfbData"
      @barClick="barClickAdcd1"
    ></bar-chart>
    <bar-chart-double
      v-loading="skkrLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="skkrData"
    ></bar-chart-double>
  </div>
</template>

<script>
import BarChart from "../components/BarChart";
import BarChartDouble from "../components/BarChartDouble";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    BarChartDouble,
  },
  data() {
    return {
      skfbData: {
        title: "水库数量（座）",
        xShupai: true,
        id: "skfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        adnmList: [],
        unit: "座",
      },
      skfbLoading: true,

      skgmData: {
        title: "水库规模",
        id: "skgmChart",
        unit: "座",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        // color: ['#E02020', '#EB7E65', '#F7C739', '#6F9DF2', '#8BBC89'],
        color: ["#F7C739", "#EB7E65", "#9dc87e", "#779fec"],
        series: [],
      },
      skgmLoading: true,
      skgmList: [],
      scaleIndex: "",

      skkrData: {
        title: "可供(纳)水量（亿方）",
        id: "skkrChart",
        xData: [],
        yData: [],
        yData2: [],
        adcdList: [],
        unit: "亿m³",
        // noWords: true
      },
      skkrLoading: true,

      barHeight: "",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
  },
  watch: {
    leftSelectAdcd(val){
      this.getSkglsData()
    },
    leftSelectProjectScale(val) {
      this.getSkglsData();
      this.getSkfbData();
    },
  },
  mounted() {
    // Promise.all([

    // ]).then(res=>{

    // })
    // this.barHeight = (window.innerHeight - 405) / 2 + "px";
    console.log(this.$parent.$refs.drawLeft.clientHeight,'总和高度',this.$refs.reservoirHeight.clientHeight,'水库规模高度');
    console.log(this.$parent.$refs.drawLeft.clientHeight - this.$refs.reservoirHeight.clientHeight,'最后高度');
    this.barHeight = (this.$parent.$refs.drawLeft.clientHeight - this.$refs.reservoirHeight.clientHeight) / 2 + "px";
    // this.barHeight = window.innerHeight / 2 * 15 / (15 + 19) + 'px'
    // this.barHeight = window.innerHeight * 7 / 24 + 'px'
    this.getSkjbtjData();
    this.getSkfbData();
    this.getSkglsData();
  },
  methods: {
    // 水库分布
    async getSkfbData() {
      let opt = {
        statistic: "adcd",
        adcd: this.$localData("get", "userInfo").adcd,
      };
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "adcd",
          adcd: this.$localData("get", "userInfo").adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        let xData = [];
        let yData = [];
        let adcdList = [];
        let adnmList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            adnmList.push(res.data[i].adnm);
            if (res.data[i].adnm.length > 2) {
              // xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
              xData.push(res.data[i].adnm.replace(/市/, ""));
            } else {
              xData.push(res.data[i].adnm);
            }
            yData.push(res.data[i].num);
            adcdList.push(res.data[i].adcd);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        this.skfbData.adcdList = adcdList;
        this.skfbData.adnmList = adnmList;
        this.$store.commit("SET_RESERVOIR_LEFT_ADCD_DATA", res.data);
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skfbLoading = false;
    },

    // 水库库容
    async getSkglsData() {
      let opt = {
        statistic: "capacity",
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
      };
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "capacity",
          adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        let xData = [];
        let yData = [];
        let yData2 = [];
        let adcdList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              // xData.push(res.data[i].adnm.substring(0,res.data[i].adnm.length - 1))
              xData.push(res.data[i].adnm.replace(/市/, ""));
            } else {
              xData.push(res.data[i].adnm);
            }
            yData.push((res.data[i].supply / 10000).toFixed(1));
            yData2.push(
              res.data[i].addtion ? (res.data[i].addtion / 10000).toFixed(1) : 0
            );
            adcdList.push(res.data[i].adcd);
          }
        }
        this.skkrData.xData = xData;
        this.skkrData.yData = yData;
        this.skkrData.yData2 = yData2;
        this.skkrData.adcdList = adcdList;
        this.$store.commit("SET_RESERVOIR_LEFT_KR_DATA", res.data);
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skkrLoading = false;
    },

    // 水库规模
    async getSkjbtjData() {
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        if (res.data.length) {
          let bigArr = [];
          let otherArr = [];
          for (let i = 0; i < res.data.length; i++) {
            switch (res.data[i].project_scale) {
              case "1":
                otherArr.push({
                  name: "大(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/sk-scale1.png"),
                });
                break;
              case "2":
                otherArr.push({
                  name: "大(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/sk-scale1.png"),
                });
                break;
              case "3":
                otherArr.push({
                  name: "中型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/sk-scale2.png"),
                });
                break;
              case "4":
                otherArr.push({
                  name: "小(1)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/sk-scale3.png"),
                });
                break;
              case "5":
                otherArr.push({
                  name: "小(2)型",
                  value: res.data[i].num,
                  project_scale: res.data[i].project_scale,
                  img: require("../../../../assets/images/sk-scale3.png"),
                });
                break;
            }
          }
          // let bigVal = 0
          // for (let i = 0; i < bigArr.length; i++) {
          //   bigVal += bigArr[i]
          // }
          // this.skgmData.series = [{
          //   name: '大型',
          //   value: bigVal,
          //   project_scale: '-1',
          //   img: require('../../../../assets/images/sk-scale1.png')
          // },...otherArr]
          this.skgmData.series = otherArr;
          this.skgmList = this.skgmData.series;
          this.$store.commit(
            "SET_RESERVOIR_LEFT_SCALE_DATA",
            this.skgmData.series
          );
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skgmLoading = false;
    },

    // 水库分布柱状图点击事件
    barClickAdcd1(index) {
      if (this.skfbData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADNM",
          this.skfbData.adnmList[index]
        );
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.skfbData.adcdList[index]
        );
      }
    },

    clickScale(index) {
      if (this.scaleIndex === index) {
        this.scaleIndex = "";
        this.$store.commit("SET_LEFT_SELECT_PROJECT_SCALE", "all");
      } else {
        this.scaleIndex = index;
        this.$store.commit(
          "SET_LEFT_SELECT_PROJECT_SCALE",
          this.skgmList[index].project_scale
        );
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  z-index: 1002;
  .scale_box {
    height: 260px;
    .scale_list {
      @include flexbox();
      @include flexAC();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 8px 16px 0 16px;
      .scale_item {
        color: #fff;
        background: #17272E;
        // border: 1px solid #17272E;
        border-radius: 4px;
        height: 96px;
        margin-bottom: 10px;
        cursor: pointer;
        padding: 14px 0;
        box-sizing: border-box;
        &.width50 {
          width: 48.8%;
        }
        &.width33 {
          width: 32%;
          .scale_item_name {
            font-size: 16px;
            img {
              width: 22px;
              height: 22px;
            }
          }
          .scale_item_num {
            font-size: 26px;
            margin-top: 13px;
          }
        }
        .scale_item_name {
          @include flexbox();
          @include flexAC();
          @include flexJC(center);
          // padding-left: 32px;
          font-size: 18px;
          img {
            margin-right: 10px;
            width: 25px;
            height: 25px;
          }
        }
        .scale_item_num {
          font-size: 32px;
          font-weight: 500;
          text-align: center;
          margin-top: 7px;
          span {
            font-size: 22px;
            padding-left: 5px;
          }
        }
        &.active {
          // border: 1px solid #36979b;
          // box-sizing: border-box;
          background: url("../../../../assets/images/black-select.png") no-repeat center;
          background-size: 100% 100%;
        }
      }
    }
  }
}
.skLeftChart {
  width: 100%;
  // height: 320px;
  // height: var (--barChartHeight);
}
</style>
