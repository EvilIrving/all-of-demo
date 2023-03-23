<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-20 10:44:58
 * @Description: 地图左侧-统计
 * @FilePath: /rscp-big-screen/src/views/sgzgScreen/healthyOnline/healthLeft/healthLeft.vue
-->
<template>
  <div class="map_left">
    <div class="scale_box" v-loading="skgmLoading" ref="reservoirHeight">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库规模
      </div>
      <div class="scale_list">
        <div :class="[
            'scale_item',
            index === scaleIndex ? 'active' : '',
            index === 0 || index === 1 ? 'width50' : 'width33',
          ]" v-for="(item, index) in skgmList" :key="index" @click="clickScale(index)">
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>座</span></div>
        </div>
      </div>
    </div>
    <section class="cxjg_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程检查
      </div>
      <div class="status_list">
        <div :class="['status_item', index === statusIndex ? 'active' : '']" v-for="(item, index) in checkProList" :key="index">
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <section class="cxjg_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库安全鉴定
      </div>
      <div class="status_list">
        <div :class="['status_item', index === statusIndex ? 'active' : '']" v-for="(item, index) in statusList" :key="index">
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>

    <div class="chart_line"></div>
    <bar-chart v-loading="skfbLoading" :style="{ height: barHeight }" class="skLeftChart" :chartData="skfbData" @barClick="barClickAdcd1"></bar-chart>
    <div class="chart_line"></div>
    <!-- <bar-chart-double
      v-loading="skkrLoading"
      :style="{ height: barHeight }"
      class="skLeftChart"
      :chartData="skkrData"
    ></bar-chart-double>
    <div class="border"></div> -->
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import BarChartDouble from "@/components/BarChartDouble";
import PieChart from "@/components/PieChart";
import { reservoirLeftStatisticApi, statisticCheckBigScreenApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
    BarChartDouble,
  },
  data() {
    return {
      statusList: [],
      checkProList: [],
      statusValue: "",
      statusIndex: "",
      loading_status: true,
      loading_inspect: true,
      statusData: {
        title: "水库安全鉴定",
        id: "statusData",
        unit: "座",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: ["#5B8FF9", "#5AD8A6", "#F7C63A", "#779fec", "#8BBC89"],
        series: [],
      },
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
      scaleIndex: 0,
      statusList: [],
      barHeight: "",
      pieHeight: "",
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      adcd: this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",
      listFlag: true,
      squareValue: "",
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
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      this.adcd = val;
      this.getStatusData();
      this.getInspectData()
    },
    leftSelectProjectScale(val) {
      this.getSkfbData();
      this.getStatusData();
      this.getInspectData()
    },
  },
  async mounted() {
    this.barHeight = (window.innerHeight - this.$refs.reservoirHeight.clientHeight) / 2 + "px";
    await this.clickScale(this.scaleIndex)
    this.getSkjbtjData();
    this.getSkfbData();
    this.getStatusData();
    this.getInspectData();
  },
  methods: {
    //  工程检查情况
    async getInspectData() {
      this.loading_inspect = true;
      // let opt = {};
      // if (this.leftSelectProjectScale) {
      //   opt = {
      //     statistic: "problemCnt",
      //     adcd: this.adcd,
      //     itemCategory: "scale",
      //     itemValue: this.leftSelectProjectScale,
      //     superviseLevel: this.superviseLevel,
      //   };
      // } else {
      //   // projectCategory
      //   // projectScale
      //   opt = {
      //     statistic: "problemCnt",
      //     adcd: this.adcd,
      //     itemCategory: this.squareValue,
      //     itemValue: "1",
      //     superviseLevel: this.superviseLevel,
      //   };
      // }
      let otherArr = [];
      let opt = {
        projectCategory: "水库",
        projectScale: this.leftSelectProjectScale !== '' ? this.leftSelectProjectScale : 'all',
      };
      let res = await statisticCheckBigScreenApi(opt);
      if (res.success) {
        otherArr = [{
          name: "发现问题",
          value: res.data.totalProblems ? res.data.totalProblems : 0,
          src: require("@/assets/images/discoverQS.png"),
        },
        {
          name: "需整改工程",
          value: res.data.totalProblemProject ? res.data.totalProblemProject : 0,
          src: require("@/assets/images/NeedRectificationProject.png"),
        },
        {
          name: "已完成整改",
          value: res.data.totalFixedProject ? res.data.totalFixedProject : 0,
          src: require("@/assets/images/Rectificated.png"),
        },
        {
          name: "正在开展整改",
          value: res.data.totalFixingProject ? res.data.totalFixingProject : 0,
          src: require("@/assets/images/Rectifying.png"),
        },]
        this.checkProList = otherArr
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_inspect = false;
    },
    // 水库安全鉴定情况
    async getStatusData() {
      this.loading_status = true;
      this.statusData.series = [];
      let opt = {};
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "problemCnt",
          adcd: this.adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
          superviseLevel: this.superviseLevel,
        };
      } else {
        opt = {
          statistic: "problemCnt",
          adcd: this.adcd,
          itemCategory: this.squareValue,
          itemValue: "1",
          superviseLevel: this.superviseLevel,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        this.statusData.series = [
          {
            name: "未加固三类坝",
            value: res.data[0].unsteadythird ? res.data[0].unsteadythird : 0,
            src: require("@/assets/images/threeTypesDams.png"),
          },
          {
            name: "未加固二类坝",
            value: res.data[0].unsteadysecond ? res.data[0].unsteadysecond : 0,
            src: require("@/assets/images/unopenedDam.png"),
          },
          {
            name: "加固中水库",
            value: res.data[0].status2 ? res.data[0].status2 : 0,
            src: require("@/assets/images/reinforcement.png"),
          },
          {
            name: "一类坝水库",
            value: res.data[0].firstClass ? res.data[0].firstClass : 0,
            src: require("@/assets/images/oneTypesDams.png"),
          },
        ];
        this.statusList = this.statusData.series;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_status = false;
    },
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
        this.scaleIndex = '';
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
      padding: 0 16px;
      .scale_item {
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
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
          color: #fff;
          border: none;
          background: url("../../../../assets/images/scale_select.png")
            no-repeat center;
          background-size: cover;
        }
      }
    }
  }
}
.cxjg_box {
  .status_list {
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(row wrap);
    padding: 0 5px;
    box-sizing: border-box;
    .status_item {
      width: 50%;
      @include flexbox();
      @include flexAC();
      // cursor: pointer;
      border: 1px solid #fff;
      box-sizing: border-box;
      padding: 2px 0;
      // &:hover{
      //   background: rgba(25,59,196,0.15);
      //   @include transition(all, .5s, ease);
      // }
      &.active {
        background: rgba(25, 59, 196, 0.15);
        border: 1px solid #193bc4;
      }
      img {
        width: 35%;
        margin-right: 5px;
        margin-left: 5px;
      }
      .status_right {
        & > p {
          line-height: 24px;
          font-size: 14px;
          color: #000;
          font-weight: bold;
          padding: 8px 0 4px 0;
        }
        i {
          font-size: 12px;
          padding-left: 4px;
        }
        & > span {
          line-height: 24px;
          font-size: 16px;
          color: #000;
          font-weight: bold;
        }
      }
    }
  }
}
</style>
