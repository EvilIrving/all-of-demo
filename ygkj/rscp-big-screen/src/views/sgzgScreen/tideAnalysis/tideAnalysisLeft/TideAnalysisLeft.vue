<!--
 * @Author: hanyu
 * @LastEditTime: 2021-12-19 17:33:38
 * @Description: 地图左侧-统计
 * @FilePath: \rscp-big-screen\src\views\sgzgScreen\tideAnalysis\tideAnalysisLeft\TideAnalysisLeft.vue
-->
<template>
  <div>
    <pie-chart v-loading="htjbtjLoading" :style="{ height: barHeight }" class="htfbChart" :chartData="htjbtjData" @pieClick="pieClick2"></pie-chart>
    <div class="chart_line"></div>
    <section class="cxjg_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程检查
      </div>
      <div class="status_list">
        <div :class="['status_item', index === statusIndex ? 'active' : '']" v-for="(item, index) in checkProList" :key="index" @click="clickCheckStatus(index)">
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <div class="scale_box" v-loading="skgmLoading" ref="reservoirHeight">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        海塘安全鉴定
      </div>
      <div class="scale_list">
        <div :class="[
            'scale_item',
            index == 0 || index == 1? 'active' : '',
            index == 0 || index == 1? 'width50' : 'width33',
          ]" v-for="(item, index) in skgmList" :key="index" @click="clickScale(index)">
          <div class="scale_item_name">
            <img :src="item.img" alt="" />
            <p>{{ item.name }}</p>
          </div>
          <div class="scale_item_num">{{ item.value }}<span>座</span></div>
        </div>
      </div>
    </div>
    <div class="chart_line"></div>
    <bar-chart v-loading="htfbLoading" :style="{ height: barHeight }" class="htfbChart" :chartData="htfbData" @barClick="barClickAdcd1"></bar-chart>
    <div class="border"></div>
  </div>
</template>

<script>
import BarChart from '@/components/BarChart'
import PieChart from '@/components/PieChart'
import { seawallLeftStatisticApi } from '@/api/api_seawall'
import { reservoirLeftStatisticApi, statisticCheckBigScreenApi, statisticSeawallSafeBigScreen } from "@/api/api_reservoir";
export default {
  name: '',
  props: {

  },
  components: {
    BarChart, PieChart
  },
  data() {
    return {
      htfbData: {
        title: "海塘分布",
        id: 'htfbChart',
        xData: [],
        yData: [],
        adcdList: [],
        unit: '条'
      },
      htfbLoading: true,
      checkProList: [],
      statusIndex: "",
      skgmLoading: true,
      scaleIndex: "",
      skgmList: [],
      loading_inspect: true,
      htjbtjData: {
        title: "海塘等级",
        id: 'htjbtjChart',
        unit: '条',
        legend: {
          orient: 'vertical',
          left: '65%',
          height: '80%'
        },
        color: ['#E02020', '#EB7E65', '#F7C739', '#6F9DF2', '#DAB6A0', '#8BBC89'],
        series: [],
      },
      htjbtjLoading: true,
      barHeight: '',
      pieHeight: '',
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
      this.getInspectData()
      this.getSkjbtjData()
    },
    leftSelectProjectScale(val) {
      this.getInspectData()
      this.getSkjbtjData()
    },
  },
  mounted() {
    this.barHeight = window.innerHeight / 3.5 + 'px'
    this.getHtfbData();
    this.getHtjbtjData();
    this.getInspectData()
    this.getSkjbtjData()

  },
  methods: {
    // 水库规模
    async getSkjbtjData() {
      let res = await statisticSeawallSafeBigScreen();
      if (res.success) {
        if (res.data) {
          let otherArr = [];
          otherArr.push({
            name: "一类塘",
            value: res.data.firstPond,
            img: require("../../../../assets/images/sk-scale1.png"),
          });
          otherArr.push({
            name: "二类塘",
            value: res.data.secondPond,
            img: require("../../../../assets/images/sk-scale1.png"),
          });
          otherArr.push({
            name: "三类塘",
            value: res.data.thirdPond,
            img: require("../../../../assets/images/sk-scale1.png"),
          }); otherArr.push({
            name: "待鉴定",
            value: res.data.readyConstruction,
            img: require("../../../../assets/images/sk-scale1.png"),
          }); otherArr.push({
            name: "安澜建设",
            value: res.data.constructionCnt,
            img: require("../../../../assets/images/sk-scale1.png"),
          });
          this.skgmList = otherArr;
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skgmLoading = false;
    },
    //  工程检查情况
    async getInspectData() {
      this.loading_inspect = true;
      let otherArr = [];
      let opt = {
        projectCategory: "海塘",
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
    // 海塘分布
    async getHtfbData() {
      let res = await seawallLeftStatisticApi({
        statistic: 'adcd',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              this.htfbData.xData.push(res.data[i].adnm.substring(0, res.data[i].adnm.length - 1))
            } else {
              this.htfbData.xData.push(res.data[i].adnm)
            }
            this.htfbData.yData.push(res.data[i].num)
            this.htfbData.adcdList.push(res.data[i].adcd)
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htfbLoading = false
    },
    // 海塘级别统计
    async getHtjbtjData() {
      let res = await seawallLeftStatisticApi({
        statistic: 'scale',
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if (res.success) {
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            let htName = res.data[i].project_scale + '级海塘'
            if (res.data[i].project_scale != 6) {
              this.htjbtjData.series.push({
                name: htName,
                value: res.data[i].num,
                project_scale: res.data[i].project_scale
              })
            }
          }
        }
      } else {
        this.$message.error(res.msg ? res.msg : '请求失败')
      }
      this.htjbtjLoading = false
    },
    // 海塘分布柱状图点击事件
    barClickAdcd1(index) {
      if (this.htfbData.xData[index] === '省级') {
        this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
      } else {
        this.$store.commit('SET_LEFT_SELECT_ADCD', this.htfbData.adcdList[index])
      }
    },
    // 海塘级别统计点击事件
    pieClick2(data) {
      this.$store.commit('SET_LEFT_SELECT_PROJECT_SCALE', data.project_scale)
    }
  }
}
</script>

<style lang='scss' scoped>
.htfbChart {
  width: 100%;
  // height: var (--barChartHeight);
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
.scale_box {
  height: 230px;
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
      height: 70px;
      margin-bottom: 10px;
      // cursor: pointer;
      padding: 14px 0;
      box-sizing: border-box;
      &.width50 {
        width: 50%;
        height: 90px;
        padding: none;
        border-radius: 0px;
        &:nth-child(1) {
          border-top-left-radius: 4px;
          border-bottom-left-radius: 4px;
        }
        &:nth-child(2) {
          border-top-right-radius: 4px;
          border-bottom-right-radius: 4px;
        }
        .scale_item_name {
          font-size: 16px;
          img {
            width: 16px;
            height: 16px;
          }
        }
        .scale_item_num {
          font-size: 24px;
        }
      }
      &.width33 {
        width: 32%;
        .scale_item_name {
          font-size: 16px;
          img {
            width: 16px;
            height: 16px;
          }
        }
        .scale_item_num {
          font-size: 24px;
          margin-top: 10px;
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
        background: url("../../../../assets/images/scale_select.png") no-repeat
          center;
        background-size: cover;
      }
    }
  }
}
</style>