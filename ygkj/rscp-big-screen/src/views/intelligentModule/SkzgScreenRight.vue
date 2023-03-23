
<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="right_top">
      <p class="second_title2 last_title">
        <span :class="[
            'active',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in topTitle" :key="index" @click="chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div ref="first_box" class="right_box first_box">
        <ul class="xsl_ul cursor">
          <li :class="numberSelected == '蓄水量' ? 'numberActive' : ''" @click="chooseMapType('蓄水量', '蓄水量')">
            <p class="num_title">当前蓄水总量</p>
            <p class="num_text">
              <countTo class="ba_num" :startVal="0" :endVal="nxData.capable" :duration="1500" :autoplay="true" :decimals="2"></countTo>
              <span class="ba_unit">亿m³</span>
            </p>
          </li>
          <li :class="numberSelected == '蓄水率' ? 'numberActive' : ''" @click="chooseMapType('蓄水率', '蓄水率')">
            <p class="num_title">当前蓄水率</p>
            <p class="num_text">
              <countTo class="ba_num" :startVal="0" :endVal="nxData.storagePerc" :duration="1500" :autoplay="true" :decimals="2"></countTo>
              <span class="ba_unit">%</span>
            </p>
          </li>
        </ul>
        <div id="histogram3d"></div>
        <ul class="lenged">
          <li>
            <span>可纳雨量</span>
            <span v-permission="'jlzs:deduction'" class="cursor deducing" @click="openDeduction(deducing)">{{ deducing }}</span>
          </li>
          <!-- 1.以下，  2.以上 -->
          <li class="cursor" @click="chooseNxType('纳蓄', '200,2', 0)">
            <span class="green"></span><span :class="nxSelectIndex === 0 ? 'active' : ''">200mm以上</span>
            <img v-if="nxSelectIndex === 0" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '100,200', 1)">
            <span class="blue"></span><span :class="nxSelectIndex === 1 ? 'active' : ''">100-200mm</span>
            <img v-if="nxSelectIndex === 1" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '50,100', 2)">
            <span class="orange"></span><span :class="nxSelectIndex === 2 ? 'active' : ''">50-100mm</span>
            <img v-if="nxSelectIndex === 2" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
          <li class="cursor" @click="chooseNxType('纳蓄', '50,1', 3)">
            <span class="red"></span><span :class="nxSelectIndex === 3 ? 'active' : ''">50mm以下</span>
            <img v-if="nxSelectIndex === 3" class="selected" :src="$config.imgUrl + 'arr.png'" alt="" />
          </li>
        </ul>
      </div>
    </div>

    <div class="right_middle">
      <p class="second_title2 last_title">
        <span :class="[
           'active',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in firstTitle" :key="index" @click="chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div class="third_box">
        <ul class="tab_box">
          <li>
            <p class="type_title">
              <img :src="$config.zhImgUrl + 'currentYear001.png'" alt="">
              <span>总数</span>
            </p>
            <div class="cursor tab_box_item" :class="numberSelected == '总数,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '总数,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ safeData.two }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '总数,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '总数,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ safeData.three }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>
          <li>
            <p class="type_title">
              <img :src="$config.zhImgUrl + 'notStart001.png'" alt="">
              <span>未开工</span>
            </p>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.twowkg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '未开工,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '未开工,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.threewkg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>
          <li>
            <p class="type_title">
              <img :src="$config.zhImgUrl + 'started001.png'" alt="">
              <span>已开工</span>
            </p>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,二类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,二类坝')">
              <span class="ba_text">二类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.twoykg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
            <div class="cursor tab_box_item" :class="numberSelected == '已开工,三类坝' ? 'numberActive' : ''" @click="chooseMapType('除险加固', '已开工,三类坝')">
              <span class="ba_text">三类坝</span>
              <span class="ba_num">
                {{ cxjgStaticData.threeykg }}
                <span class="ba_unit">座</span>
              </span>
            </div>
          </li>

        </ul>
      </div>
    </div>

    <div class="right_bottom">
      <p class="second_title2 last_title">
        <span :class="[
           'active',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in lastTitle" :key="index" @click="chooseMapType(item.name)">{{ item.name }}</span>
      </p>

      <div class="aqjd_top">
        <img :src="$config.zhImgUrl + 'TotalNumberOfStocks.png'" alt="">
        <!-- 第一行：总数   二行：前期准备、加固中、已完工、已竣工  -->
        <span>存量总数</span>
        <span :class="[ numberSelected == '已竣工,加固中,已完工,项目前期'
              ? 'numberActive'
              : '','cursor','num']" @click.self="
                chooseMapType('存量清零', '已竣工,加固中,已完工,项目前期')
              ">{{ bxskNum.totalNum || 0 }}</span><span>座</span>
      </div>

      <ul class="gs_list">
        <li class="gs_item" v-for="(item, index) in problemList" :key="index">
          <img :src="item.src" alt="" />
          <div class="cursor" :class="numberSelected == item.status ? 'numberActive' : ''" @click="chooseMapType('存量清零', item.status)">
            <h3>{{ item.status }}</h3>
            <p>
              <span>{{ item.num }}</span>{{ item.unit }}
            </p>
          </div>
        </li>
      </ul>
    </div>

    <!-- 推演弹窗 -->
    <primary-dialog :showFooter="showFooter" top="4vh" mainTitle="推演" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" :visible="dialogVisible" @handleClose="handleClose" width="1000px">
      <div class="dialog-content" :class="{ 'fullscreen-box': fullscreen }">
        <deduction v-if="dialogVisible" :fullscreen="fullscreen" :sgzgSelectBasin="sgzgSelectBasin" :sgzgSelectAdcd="sgzgSelectAdcd" @startDeducing="startDeducing" @handleClose="handleClose" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import countTo from "vue-count-to";
import Highcharts from "highcharts";
import FunnelChart from "./components/FunnelChart.vue";
import ConcentricChart from "./components/ConcentricChart.vue";
import BarChartNoClick from "./components/BarChartNoClick";
import PrimaryDialog from "./components/PrimaryDialog.vue";
import Deduction from "./components/Deduction.vue";
import {
  floodCountStorage,
  nxStatisticApi,
  reservoirBigScreen,
  wateringApi,
  electricityApi,
  safeAppraisalApi,
  ecologyApi,
  xzyyStatisticApi,
  cyjgNumApi,
  clickLog,
} from "@/api/api_reservoir";
export default {
  name: "SkzgScreenRight",
  props: {},
  components: {
    FunnelChart,
    BarChartNoClick,
    PrimaryDialog,
    Deduction,
    ConcentricChart,
    countTo,
  },
  computed: {
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    },
    sgzgSelectAdcd() {
      return this.$store.state.sgzgSelectAdcd;
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
    selectData() {
      let { sgzgSelectAdcd, sgzgSelectBasin } = this.$store.state;
      return { sgzgSelectAdcd, sgzgSelectBasin };
    },
    waterStorageQuantity() {
      // 纳蓄的图例变化更新到vuex
      return this.$store.state.waterStorageQuantity;
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
    waterStorageRate() {
      // 纳蓄的图例变化更新到vuex
      return this.$store.state.waterStorageRate;
    },
  },
  watch: {
    selectData: {
      handler(val) {
        this.getSafeData();
        this.getClqlData();
        this.getCXJGStatic(3);
        this.getNxData()
      },
      deep: true,
      immediate: true,
    },
    waterStorageQuantity(val) {
      // 纳蓄 蓄水量的图例 更新
      this.getNxData();
    },
    waterStorageRate(val) {
      // 纳蓄 蓄水率的图例 更新
      this.getNxData();
    },
  },
  data() {
    return {
      playlistDialogVisible: false,
      funnelLoading: false,
      barLoading: true,
      dialogVisible: false,
      loading: false,
      showFooter: false,
      fullscreen: false,
      deducing: "推演",
      safeData: {},
      // topTitle: [{ name: "纳蓄" }, { name: "防洪" }],
      // firstTitle: [{ name: "供水" }, { name: "灌溉" }],
      // lastTitle: [{ name: "生态" }, { name: "发电" }],
      topTitle: [{ name: "纳蓄" }],
      firstTitle: [{ name: "除险加固" }],
      lastTitle: [{ name: "存量清零" }],
      bxskNum: {},
      problemList: [
        {
          status: "项目前期",
          num: 0,
          unit: "座",
          src: this.$config.zhImgUrl + "earlyStage01.png",
        },
        {
          status: "加固中",
          num: 0,
          unit: "座",
          src: this.$config.zhImgUrl + "reinforcing01.png",
        },
        {
          status: "已完工",
          num: 0,
          unit: "座",
          src: this.$config.zhImgUrl + "completed01.png",
        },
        {
          status: "已竣工",
          num: 0,
          unit: "座",
          src: this.$config.zhImgUrl + "completedAgain01.png",
        },
      ],
      cxjgStaticData: [],
      funnelHeight: 200,
      funnelData: {
        id: "funnelData",
        data: [],
        unit: "座",
      },
      barHeight: 150,

      stData: {
        skNum: 0,
        scenicNum: 0,
      },
      nxData: {
        capable: 0,
        storagePerc: 0,
      },
      adcdList: [],
      selectAdcd: "",
      inputName: "",
      tableHeight: 0,
      siteList: [],
      ggData: {},
      funnel3dData: [],
      rateList: [
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
        {
          width: 0,
        },
      ],
      nxSelectIndex: "",
      nxInterval: null,
      powerGeStatistic: 0,
      suppplyStatistic: 0,
      irrigationStatistic: 0,
      carbon: 0,
      ggImgWidth: 30,
      ggImgHeight: 30,
      supply_total: 0, // 可供水量
      installedTotal: 0, //装机容量
      capacityTotal: 0, //当年灌溉水量
      gsTotal: 0, //供水水量
      ggTotal: 0, //灌溉水量
      generatingTotal: 0, //发电量
      carbonCount: 0, // 碳排量
      userAdcd: "",
    };
  },
  mounted() {
    this.userAdcd = this.$localData("get", "userInfo").adcd;
    this.getFunnelData();
    this.getNxData();
    this.getSafeData();
    this.getClqlData();
    this.getCXJGStatic(3);
    // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.first_box.offsetHeight + this.$refs.third_box.offsetHeight + 400)
  },
  methods: {
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },



    // 纳蓄 两个 头部统计
    async getNxData() {
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
      };
      if (this.numberSelected == "蓄水率") {
        // opt.projectScale = this.waterStorageQuantity
        opt.projectScale = this.waterStorageRate;
      } else {
        opt.projectScale = this.waterStorageQuantity;
      }
      let res = await nxStatisticApi(opt);
      if (res.success && res.data.length !== 0) {
        this.nxData.capable = 0;
        this.nxData.storagePerc = 0;
        setTimeout(() => {
          this.nxData.capable = +res.data[0].capable;
          this.nxData.storagePerc = res.data[0].storagePerc;
        });
        if (this.nxInterval) clearInterval(this.nxInterval);
        this.nxInterval = setInterval(() => {
          this.getNxData();
        }, 60000);
      } else {
        this.nxData.capable = 0;
        this.nxData.storagePerc = 0;
      }
    },

    // 除险加固统计数据
    async getCXJGStatic(type) {
      let res = await xzyyStatisticApi({
        gpType: type,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        this.cxjgStaticData = res.data[0];
      }
    },
    // 除险加固统计数据
    async getSafeData() {
      let res = await safeAppraisalApi({
        interType: 1,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.safeData = res.data;
      }
    },
    // 存量清零
    async getClqlData() {
      let res = await cyjgNumApi({
        interType: 1, //统计
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        this.bxskNum = res.data;
        this.problemList[0].num = res.data.xmqq
        this.problemList[1].num = res.data.jgz
        this.problemList[2].num = res.data.ywg
        this.problemList[3].num = res.data.yjg
      }
    },
    // 纳蓄图例点击
    chooseNxType(type, rainScope, index) {
      this.$parent.$refs.skzgMap.closeAllOverly();
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);
      this.$store.commit("SET_SKZG_MAP_TYPE", type);
      this.$store.commit("SET_RAIN", rainScope);
      this.nxSelectIndex = index;
    },

    chooseMapType(type, numberSelected) {
      clickLog({
        title: "智慧水库-" + type
      });
      this.$store.commit("SET_NUMBER_SELECT", "");
      if (numberSelected) {
        this.$store.commit("SET_NUMBER_SELECT", numberSelected);
      }
      if (type === "蓄水量") {
        this.$store.commit("SET_WATER_STORAGE_QUANTITY", "1,2,3");
      }
      if (type === "蓄水率") {
        this.$store.commit("SET_WATER_STORAGE_RATE", "1,2,3");
      }

      this.$parent.$refs.skzgMap.closeAllOverly();
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true);
      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);
      this.$store.commit("SET_SKZG_MAP_TYPE", type);
    },

    // 推演
    startDeducing(flag) {
      this.showFooter = false;
      this.$nextTick(() => {
        this.showFooter = flag;
      });
    },
    openDeduction(deducing) {
      if (deducing === "推演") {
        this.dialogVisible = true;
        clickLog({
          title: "智慧水库-推演"
        });
      } else {
        this.deducing = "推演";
        this.$store.commit("SET_IFUNDER", "");
        this.$store.commit("SET_RAIN", "");
        this.dialogVisible = false;
      }
    },
    // 水库库容
    async getFunnelData() {
      this.funnelLoading = true;
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basin: this.sgzgSelectBasin,
        gpType: 6,
      };
      let res = await reservoirBigScreen(opt);
      if (res.success) {
        if (res.data.length > 0) {
          let arr = [];
          let sumprice = res.data.reduce(function (
            totalValue,
            currentValue,
            currentIndex,
            arr
          ) {
            totalValue += currentValue.num;
            currentValue.accumulateNum = totalValue;
            return totalValue;
          },
            0);
          // res.data.sort((a, b) => b.num - a.num )
          res.data.reverse();

          res.data.map((i, index) => {
            i.name = i.num + "座";
            i.value = ((i.accumulateNum / sumprice) * 100 * 0.6).toFixed(2);
            arr.push(i);
          });
          this.funnelData.data = arr;
          this.funnel3dData = [];
          arr.forEach((i) => {
            this.funnel3dData.push([i.fz, +i.num]);
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.initHistogram3D();
      this.funnelLoading = false;
    },
    // 3D柱状图
    initHistogram3D() {
      var chart = new Highcharts.chart("histogram3d", {
        chart: {
          type: "funnel3d",
          options3d: {
            enabled: true,
            alpha: 6,
            viewDistance: 50,
          },
          backgroundColor: "rgba(0,0,0,0)",
        },
        title: false,
        plotOptions: {
          series: {
            dataLabels: {
              enabled: true,
              format: "<b>{point.y:,.0f}",
              color: (Highcharts.theme && Highcharts.theme.colors) || "black",
              allowOverlap: false,
              y: 12,
              color: "#fff",
              style: {
                fontSize: 14,
              },
              align: "center",
            },
            cursor: "pointer",
            neckWidth: "20%",
            neckHeight: "0%",
            width: "75%",
            height: "100%",
            minSize: "100",
            events: {
              click: (event) => {
                console.log(event, "3D-event");
                // event.point.name
                // event.point.y
                // event.point.color = 'red'
                switch (event.point.name) {
                  case "50mm以下":
                    this.chooseNxType("纳蓄", "50,1", 3);
                    break;
                  case "50-100mm":
                    this.chooseNxType("纳蓄", "50,100", 2);
                    break;
                  case "100-200mm":
                    this.chooseNxType("纳蓄", "100,200", 1);
                    break;
                  default:
                    this.chooseNxType("纳蓄", "200,2", 0);
                    break;
                }
              },
            },
          },
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: "",
            data: this.funnel3dData,
            colorByPoint: true,
            allowPointSelect: true,
            colors: [
              "rgba(91, 190, 88, 0.7)",
              "rgba(56, 134, 255, 0.7)",
              "rgba(247, 181, 0, 0.7)",
              "rgba(250, 100, 0, 0.8)",
            ],
          },
        ],
      });
      // document.getElementById('histogram3d').addEventListener('click', () => {
      //   var selectedPoints = chart.getSelectedPoints();
      //   console.log(selectedPoints[0], 'selectedPoints')
      //   if (chart.lbl) {
      //     chart.lbl.destroy();
      //   }
      // });
    },
    handleClose() {
      this.dialogVisible = false;
      this.deducing = "推演";
    },
    handlePlaylistClose() {
      this.playlistDialogVisible = false;
    },
  },
};
</script>

<style lang='scss' scoped>
.third_title:before {
  content: none;
}
.slide_wrap {
  width: 100%;
  height: 100%;
  .slide_title {
    width: 100%;
    height: 48px;
    font-size: 36px;
    margin-bottom: 4px;
    font-family: "pangmen";
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    background: url($imageUrl + "title_bg.png") no-repeat center;
    background-size: cover;
    cursor: pointer;
  }
  .second_title2 {
    padding: 0 0 0 37px;
    margin-bottom: 10px;
    transition: all 0.3s linear;
    font-family: "youshe";
    width: 100%;
    height: 40px;
    font-size: 30px;
    @include flexbox();
    @include flexJC(flex-start);
    @include flexAI(center);
    background: url($imageUrl + "second_title.png") no-repeat bottom;
    background-size: 100% auto;
    cursor: pointer;
    z-index: 2;
    box-sizing: border-box;
    span {
      cursor: pointer;
    }
    &.last_title {
      display: flex;
      flex-direction: row;
      align-items: center;
      span {
        transition: all 0.2s linear;
        margin-right: 25px;
        font-size: 24px;
        color: rgba(226, 237, 255, 0.5);
        cursor: pointer;
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
  }
  .right_top {
    height: calc(35% - 15px);
    overflow: hidden;
    .first_box {
      height: 100%;
      .xsl_ul {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        li {
          width: 40%;
          &:first-child {
            margin-right: 30px;
          }
        }
      }
      .funnel_chart {
        width: 100%;
        height: calc(100% - 120px);
      }
    }
    .circle_wrapper {
      width: 100%;
      // height: 100px;
      height: calc(50% - 50px);
      // margin: 2% 0px;
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      li {
        width: 50%;
        // height: 100%;
        display: flex;
        justify-content: center;
        // align-items: flex-start;
        @include flexAI(center);
        margin: 0px 5px;
        .circle_box {
          @include flexbox();
          @include flexJC(center);
          @include flexAI(center);
          width: 50%;
          // height: 100%;
          position: relative;
          img.index-out {
            @include flexbox();
            @include flexJC(center);
            @include flexAI(center);
            width: 60%;
            animation: rotate 3s linear 2s infinite;
            @keyframes rotate {
              0% {
                transform: rotate(0deg);
              }
              100% {
                transform: rotate(360deg);
              }
            }
          }
          img.index-in {
            position: absolute;
            top: 20%;
            left: 35%;
            width: 30%;
          }
          img.top_ring {
            position: absolute;
            top: 25%;
            left: 30%;
            width: 100%;
            animation: opacityc 1.5s linear 2s infinite;
            @keyframes opacityc {
              0% {
                opacity: 0;
              }
              50% {
                opacity: 0.8;
              }
              100% {
                opacity: 0;
              }
            }
          }
          img.middle_ring {
            position: absolute;
            top: 35%;
            left: 40%;
            width: 80%;
            animation: rotateZdeg 5s linear 2s infinite;
            @keyframes rotateZdeg {
              0% {
                transform: translate3d(0px, 0px, 0px) scaleX(1) scaleY(1)
                  rotateX(65deg) rotateY(0deg) rotateZ(0deg) skewX(0deg)
                  skewY(0deg);
              }
              100% {
                transform: translate3d(0px, 0px, 0px) scaleX(1) scaleY(1)
                  rotateX(65deg) rotateY(0deg) rotateZ(360deg) skewX(0deg)
                  skewY(0deg);
              }
            }
          }
          img.bottom_ring {
            position: absolute;
            top: 58%;
            left: 40%;
            width: 80%;
            height: 50%;
          }
        }
        .text_box {
          @include flexbox();
          flex-direction: column;
          @include flexJC(center);
          width: calc(50% - 5px);
          height: 100%;
          position: relative;
          .num_title {
            font-size: 18px;
            color: rgba(230, 247, 255, 0.8);
            padding: 5px 0 5px 5px;
          }
          .num_text {
            text-align: left;
            padding-left: 5px;
          }
          .ba_num {
            font-size: 26px;
            font-family: "youshe";
            color: rgb(255, 255, 255);
            letter-spacing: 3px;
          }
          .ba_unit {
            font-size: 12px;
            font-weight: 500;
            color: rgb(255, 255, 255);
          }
        }
        .lanxu_box {
          @include flexbox();
          @include flexJC(center);
          flex-direction: column;
          width: 50%;
          height: 100%;
          position: relative;
          .num_title {
            position: absolute;
            width: 130px;
            top: -5px;
            left: -75px;
            font-size: 18px;
            color: rgba(230, 247, 255, 0.8);
            text-align: center;
          }
          .num_text {
            position: absolute;
            width: 130px;
            top: 26px;
            left: -75px;
            text-align: center;
          }
          .ba_num {
            font-size: 26px;
            font-family: "youshe";
            color: rgb(255, 255, 255);
            letter-spacing: 3px;
          }
          .ba_unit {
            font-size: 12px;
            font-weight: 500;
            color: rgb(255, 255, 255);
          }
        }
        span {
          margin: 0px 0px -4px;
          white-space: nowrap;
        }
        .total_text {
          line-height: 30px;
          font-size: 18px;
          color: rgba(216, 235, 255, 0.75);
        }
        .total_num {
          align-items: baseline;
          padding-top: 5px;
          font-size: 32px;
          // font-family: "youshe";
          font-weight: bold;
          margin: 0px;
          color: rgb(0, 230, 241);
        }
        .total_unit {
          font-size: 18px;
          color: rgb(255, 255, 255);
          transform: translate3d(10px, -2px, 0px);
        }
      }
    }
    .fhxy_wrapper {
      @include flexbox();
      @include flexAI(center);
      @include flexflow(row wrap);
      width: 100%;
      height: calc(50% - 50px);
      box-sizing: border-box;
      .fhxy_item {
        width: 50%;
        height: 100%;
        @include flexbox();
        @include flexAI(center);
        box-sizing: border-box;
        // padding-left: 15px;
        // margin-top: 50px;
        &:first-child {
          width: 45%;
        }
        &:last-child {
          width: 55%;
        }
        img {
          width: 75px;
          height: 75px;
          margin: 0 0 0 15px;
        }
        &:first-child {
          div {
            padding: 5px;
          }
        }
        div {
          h3 {
            font-size: 20px;
            padding-top: 4px;
            padding-bottom: 8px;
            color: rgba(230, 247, 255, 0.8);
          }
          p {
            // color: rgb(15, 146, 255);
            color: rgba(230, 247, 255, 0.8);
            font-size: 16px;
            span {
              vertical-align: text-bottom;
              font-family: youshe;
              color: #fff;
              font-size: 28px;
              margin-right: 3px;
            }
          }
        }
      }
    }
  }
  .right_middle {
    height: calc(30% - 15px);
    overflow: hidden;
    .third_box {
      margin: 8px 0;
      .tab_box {
        width: calc(100% - 20px);
        height: 100%;
        display: grid;
        grid-template-columns: 1fr 1fr 1fr;
        gap: 10px 4px;
        margin: 10px auto;
        font-size: 26px;
        text-align: left;
        li {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          align-items: center;
          p.type_title {
            display: flex;
            justify-content: center;
            align-items: center;
            img {
              width: 30px;
              height: 30px;
              display: inline-block;
            }
            span {
              perspective: 138px;
              font-family: "youshe";
              font-size: 22px;
              text-align: center;
              color: rgb(20, 245, 241, 0.9);
              margin: 22px 5px;
            }
          }

          .tab_box_item {
            @include flexbox;
            @include flexAI(center);
          }
          div {
            margin: 18px 0px;
            span {
              font-size: 18px;
              color: rgba(230, 247, 255, 0.8);
              margin: 0 2px;
            }
            .ba_num {
              font-family: "manrope";
              display: flex;
              font-size: 22px;
              flex: 0 1 0%;
              align-items: center;
              // margin: 0 6px;
              color: #fff;
              .ba_unit {
                color: rgba(230, 247, 255, 0.8);
                font-size: 18px;
                margin: 0 6px;
              }
            }
          }
        }
      }
    }
  }
  .right_bottom {
    height: 37%;
    overflow: hidden;
    .aqjd_top {
      width: 70%;
      height: 60px;
      box-sizing: border-box;
      margin: 10px auto;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      img {
        width: 80px;
        height: 60px;
        display: inline-block;
      }
      span {
        font-size: 20px;
        line-height: 40px;
        padding: 0 5px 0 10px;
        color: rgba(230, 247, 255, 0.8);
        &.num {
          padding-left: 16px;
          font-size: 28px;
          font-weight: bold;
          color: #69e3ee;
        }
      }
    }
    .gs_list {
      @include flexbox();
      @include flexAI(center);
      @include flexflow(row wrap);
      width: 82%;
      height: calc(96% - 100px);
      box-sizing: border-box;
      margin: 10px auto;
      .gs_item {
        width: 44%;
        height: 30%;
        @include flexbox();
        @include flexAI(center);
        box-sizing: border-box;
        // padding-left: 15px;
        // margin-top: 50px;
        &:nth-child(2n + 1) {
          width: 56%;
          // padding-left: 20px;
        }
        img {
          width: 65px;
          height: 55px;
        }

        div {
          padding: 5px 16px;
          h3 {
            font-size: 20px;
            padding-top: 4px;
            padding-bottom: 8px;
            color: rgba(230, 247, 255, 0.8);
          }
          p {
            // color: rgb(15, 146, 255);
            color: rgba(230, 247, 255, 0.8);
            font-size: 16px;
            span {
              vertical-align: text-bottom;
              font-family: youshe;
              color: #fff;
              font-size: 28px;
              margin-right: 4px;
            }
          }
        }
      }
    }
  }

  .right_box {
    ul {
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      li {
        width: 100%;
        .num_title {
          font-size: 20px;
          color: rgba(230, 247, 255, 0.8);
          text-align: left;
          padding: 8px 0 15px 30px;
        }
        .num_text {
          font-size: 26px;
          padding-left: 30px;
          font-family: "manrope";
        }
        .ba_num {
          font-size: 26px;
          font-family: "manrope";
          color: rgb(255, 255, 255);
          margin-right: 5px;
        }
        .ba_unit {
          font-size: 18px;
          font-weight: 600;
          color: #00cfff;
        }
        .th_num {
          margin-top: 10px;
          font-size: 28px;
          font-family: "youshe";
          color: rgb(255, 255, 255);
          margin-right: 5px;
        }
        .th_unit {
          color: #fff;
          font-size: 18px;
          font-weight: 600;
        }
        .pic_box {
          position: relative;
          width: 100%;
          height: 120px;
          img.index-out {
            position: absolute;
            width: 100%;
          }
          img.index-in {
            position: absolute;
            top: 0%;
            left: 25%;
            width: 50%;
            animation: imageAnimation 3s linear infinite;
            @keyframes imageAnimation {
              0% {
                opacity: 1;
                transform: none;
              }
              50% {
                opacity: 1;
                transform: rotateX(0deg) rotateY(0deg) rotateZ(0deg) scaleX(1)
                  scaleY(1) translate3d(0px, 10px, 0px);
              }
              100% {
                opacity: 1;
                transform: none;
              }
              100% {
                opacity: 1;
                transform: none;
              }
            }
          }
        }
        .pic_title {
          font-size: 20px;
          color: rgba(230, 247, 255, 0.8);
          text-align: center;
          padding: 5px 0 10px 0;
        }
        .pic_text {
          text-align: center;
        }
      }
    }
    .lenged {
      display: flex;
      flex-direction: column;
      width: 10%;
      position: absolute;
      top: 25%;
      right: 1%;
      .deducing {
        font-family: youshe;
        color: #69e3ee;
        line-height: 18px;
        width: 46px;
        font-size: 18px;
        margin-left: 5px;
        text-align: center;
        border-radius: 18px;
        border: 2px solid #69e3ee;
      }
      li {
        user-select: none;
        -webkit-user-select: none;
        width: 100%;
        font-size: 18px;
        line-height: 28px;
        @include flexbox();
        @include flexAC();
        @include flexJC(flex-start);
        span.active {
          font-weight: bold;
          color: #69e3ee;
        }
        span.green {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(91, 190, 88, 0.7);
        }
        span.blue {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(56, 134, 255, 0.7);
        }
        span.orange {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(247, 181, 0, 0.7);
        }
        span.red {
          width: 24px;
          height: 14px;
          margin-right: 5px;
          border-radius: 2px;
          background: rgba(250, 100, 0, 0.8);
        }
        .selected {
          width: 20px;
          margin-left: 5px;
        }
      }
    }
    #histogram3d {
      width: 65%;
      height: 60%;
    }
  }
  .dialog-playing {
    height: 600px;
    ::v-deep .primary-dialog {
      .el-dialog__body {
        padding: 0;
      }
    }
  }
  .dialog-content {
    height: 650px;
    // overflow: auto;
    img.huimin {
      width: 100%;
    }
  }
}
.dialog-img-content {
  height: 600px;
  width: 100%;
  overflow: auto;
  text-align: center;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }
  img.huimin {
    width: 100%;
  }
  &.fullscreen-box .fullscreen-huimin {
    width: 80%;
  }
}
</style>
<style lang="scss">
.el-loading-mask {
  background-color: #4f5fd052;
}
.viewer-container {
  z-index: 9999 !important;
}
.highcharts-data-label-hidden {
  opacity: 1 !important;
}
</style>