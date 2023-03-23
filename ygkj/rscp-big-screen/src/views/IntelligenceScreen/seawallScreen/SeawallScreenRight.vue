
<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title">控制运用</div>
    <div class="right_top">
      <p class="second_title2 last_title">
        <span :class="[
            'cursor',
            index === topActive ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in topTitle" :key="index" @click="chooseMapType(item.name)">{{ item.name }}</span>
      </p>
      <div v-if="topActive == 0" ref="first_box" class="right_box first_box">
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span :class="[
              tabIndex == index ? 'active' : '',
              sgzgMapType == item ? 'mapTypeActive' : '',
            ]" v-for="(item, index) in tabList" :key="item">{{ item }}
            <!-- <b class="scheme" @click.stop="schemeDialogVisible = true">方案管理</b> -->
          </span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
        </p>
        <div class="scheme-list">
          <div class="scheme-item" @click="checkScheme(item.id, '防潮分析')" :class="{ 'active-scheme-item': planId === item.id }" v-for="(item, index) in schemeList" :key="index">
            {{ item.planName }}
          </div>
          <!-- <div class="scheme-item" >20220914.10【梅花】海洋</div>
          <div class="scheme-item" >20220914.08【梅花】研究</div>
          <div class="scheme-item" >20220914.06【梅花】水文</div> -->
        </div>
      </div>
    </div>
    <div class="right_middle">
      <p class="second_title2">
        <span :class="[
            'cursor',
            index === titleActive0 ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in firstTitle" :key="index">{{ item.name }}</span>
      </p>
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
            'cursor',
            tabIndex1 == index ? 'active' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList1" :key="item" @click="(tabIndex1 = index), chooseMapType(item)">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="right_box gs_box" v-show="tabIndex1 == 0">
        <div class="gs_list">
          <div class="gs_item">
            <img :src="$config.imgUrl + 'gs1.png'" alt="" />
            <div class="cursor" :class="numberSelected == '大中型闸站' ? 'numberActive' : ''" @click="chooseMapType('附属建筑物', '大中型闸站')">
              <h3>大中型闸站</h3>
              <p>
                <span>{{ affiliateStatistic.dzxGateNum }}</span>个
              </p>
            </div>
          </div>
          <div class="gs_item pl">
            <img :src="$config.imgUrl + 'gs2.png'" alt="" />
            <div class="cursor" :class="numberSelected == '水闸' ? 'numberActive' : ''" @click="chooseMapType('附属建筑物', '水闸')">
              <h3>水闸</h3>
              <p>
                <span>{{ affiliateStatistic.sluiceNum }}</span>个
              </p>
            </div>
          </div>
          <div class="gs_item">
            <img :src="$config.imgUrl + 'gs3.png'" alt="" />
            <div class="cursor" @click="chooseMapType('附属建筑物', '泵站')" :class="numberSelected == '泵站' ? 'numberActive' : ''">
              <h3>泵站</h3>
              <p>
                <span>{{ affiliateStatistic.pumpNum }}</span>个
              </p>
            </div>
          </div>
          <div class="gs_item pl">
            <img :src="$config.imgUrl + 'gs4.png'" alt="" />
            <div class="cursor" @click="chooseMapType('附属建筑物', '闸站')" :class="numberSelected == '闸站' ? 'numberActive' : ''">
              <h3>闸站</h3>
              <p>
                <span>{{ affiliateStatistic.xxGateNum }}</span>个
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="facilities_box" v-show="tabIndex1 == 1">
        <div class="facilities_item">
          <p @click="chooseMapType('口门管控', '旱闸')" :class="numberSelected == '旱闸' ? 'numberActive' : ''">旱闸</p>
          <div>
            总数<span>{{ facility.dryGate }}</span>个
          </div>
          <div>
            关闭<span>{{ facility.dryCloseNum }}</span>个
          </div>
        </div>
        <div class="facilities_item">
          <p @click="chooseMapType('口门管控', '道口')" :class="numberSelected == '道口' ? 'numberActive' : ''">道口</p>
          <div>
            总数<span>{{ facility.gapNum }}</span>个
          </div>
          <div>
            关闭<span>{{ facility.gapCloseNum }}</span>个
          </div>
        </div>
        <div class="facilities_item">
          <p @click="chooseMapType('口门管控', '缺口')" :class="numberSelected == '缺口' ? 'numberActive' : ''">缺口</p>
          <div>
            总数<span>{{ facility.crossNum }}</span>个
          </div>
          <div>
            关闭<span>{{ facility.crossCloseNum }}</span>个
          </div>
        </div>
         <div class="facilities_item">
          <p @click="chooseMapType('口门管控', '船坞口')" :class="numberSelected == '船坞口' ? 'numberActive' : ''">船坞口</p>
          <div>
            总数<span>{{ facility.dockNum || 0 }}</span>个
          </div>
          <div>
            关闭<span>{{ facility.dockCloseNum || 0}}</span>个
          </div>
        </div>
      </div>
      <p class="third_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span :class="[
            tabIndex2 == index ? 'active' : '',
            sgzgMapType == item ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in tabList2" :key="item">{{ item }}</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p>
      <div class="right_box gs_box">
        <div class="gs_list">
          <div class="gs_item">
            <img class="patrol-img" src="../../../assets/images/patrol-01.png" alt="" />
            <div class="">
              <h3>巡查轮次</h3>
              <p><span>-</span>轮</p>
            </div>
          </div>
          <div class="gs_item pl">
            <img class="patrol-img" src="../../../assets/images/patrol-02.png" alt="" />
            <div class="">
              <h3>巡查人次</h3>
              <p><span>-</span>人次</p>
            </div>
          </div>
          <div class="gs_item">
            <img class="patrol-img" src="../../../assets/images/patrol-03.png" alt="" />
            <div class="">
              <h3>上报信息</h3>
              <p><span>-</span>次</p>
            </div>
          </div>
          <div class="gs_item pl">
            <img class="patrol-img" src="../../../assets/images/patrol-04.png" alt="" />
            <div>
              <h3>应急值班</h3>
              <p><span>-</span>人</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="right_bottom">
      <p class="second_title2 last_title">
        <span :class="[
            index === titleActive ? 'active' : '',
            sgzgMapType == item.name ? 'mapTypeActive' : '',
          ]" v-for="(item, index) in lastTitle" :key="index">{{ item.name }}</span>
      </p>
      <div class="third_box" v-show="titleActive == 0">
        <ul class="tab_box">
          <li>
            <img src="../../../assets/images/cultural-01.png" alt="" />
            <div class="tab_box_item">
              <p><span>-</span>km</p>
              <p>海塘岸线长度</p>
            </div>
          </li>
          <li>
            <img src="../../../assets/images/cultural-02.png" alt="" />
            <div class="tab_box_item">
              <p><span>-</span>条</p>
              <p>生态海塘</p>
            </div>
          </li>
          <li>
            <img src="../../../assets/images/cultural-03.png" alt="" />
            <div class="tab_box_item">
              <p><span>-</span>条</p>
              <p>沿塘公路</p>
            </div>
          </li>
          <li>
            <img src="../../../assets/images/cultural-04.png" alt="" />
            <div class="tab_box_item">
              <p><span>-</span>条</p>
              <p>文化长廊</p>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <!-- 顶部标题点击弹窗 -->
    <primary-dialog mainTitle="" :visible="huiminDialogVisible" @handleClose="handleCloseHuimin" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="1200px" append-to-body top="10vh">
      <div class="dialog-img-content" :class="{ 'fullscreen-box': fullscreen }" v-viewer>
        <img class="huimin fullscreen-huimin" :src="$config.imgUrl + 'newhuimin.png'" alt="" />
      </div>
      <!-- <p style="text-align: center;color: #fff;padding-top:5px;font-size: 20px;">点击可放大图片</p> -->
    </primary-dialog>
    <primary-dialog mainTitle="沿海潮位预报" :visible="schemeDialogVisible" @handleClose="schemeDialogVisible = false" @changeFullscreen="changeFullscreen" :fullscreen="fullscreen" width="1200px" append-to-body top="5vh">
      <iframe class="iframe-box" src="https://sk.zjwater.com/mgt/coastalTideAlert/forecastList" frameborder="0"></iframe>
    </primary-dialog>
  </div>
</template>

<script>
import countTo from "vue-count-to";
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
  ecologyApi,
  clickLog,
} from "@/api/api_reservoir";
import { getPlanApi, zhhtSeawallAssPrcd, zhSeawall } from "@/api/api_seawall";
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
    planId() {
      return this.$store.state.planId;
    },
    sgzgMapType() {
      return this.$store.state.sgzgMapType;
    },
    sgzgSelectAdcd() {
      return (
        this.$store.state.sgzgSelectAdcd ||
        this.$localData("get", "userInfo").adcd
      );
    },
    sgzgSelectBasin() {
      return this.$store.state.sgzgSelectBasin;
    },
    selectData() {
      let { sgzgSelectAdcd, sgzgSelectBasin } = this.$store.state;
      return { sgzgSelectAdcd, sgzgSelectBasin };
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
  },
  watch: {
    selectData: {
      handler(val) {
        this.tabIndex1 == 1 ? this.getfDData() : this.getAffiliateData();
      },
      deep: true,
      immediate: true,
    },
    titleActive: {
      handler(newVal, oldVal) { },
      deep: true,
    },
    titleActive0: {
      handler(newVal, oldVal) { },
      deep: true,
    },
  },
  data() {
    return {
      huiminDialogVisible: false,
      loading: false,
      fullscreen: false,
      titleActive: 0,
      titleActive0: 0,
      topActive: 0,
      topTitle: [{ name: "防潮分析" }],
      firstTitle: [{ name: "沿塘管控" }],
      lastTitle: [{ name: "人文生态" }],
      tabList: ["风暴潮预报方案"],
      tabList1: ["附属建筑物", "口门管控"],
      tabList2: ["巡查统计"],
      tabIndex: 0,
      tabIndex1: 0,
      tabIndex2: 0,
      adcdList: [],
      selectAdcd: "",
      inputName: "",
      tableHeight: 0,
      siteList: [],
      facility: {},
      userAdcd: "",
      schemeList: [
        "20220914.13【梅花】水文",
        "20220914.10【梅花】海洋",
        "20220914.08【梅花】研究",
        "20220914.06【梅花】水文",
      ],
      activeSchemeItemIndex: "",
      schemeDialogVisible: false,
      affiliateStatistic: {
        dzxGateNum: 0,
        sluiceNum: 0,
        pumpNum: 0,
        xxGateNum: 0,
      },
    };
  },
  mounted() {
    this.userAdcd = this.$localData("get", "userInfo").adcd;
    this.getAffiliateData();
    this.getfDData();
    this.getPlanList();

    // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.first_box.offsetHeight + this.$refs.third_box.offsetHeight + 400)
  },
  methods: {
    async getPlanList() {
      let res = await getPlanApi({
        isMatch: 1,
        fetchAll: true,
        planType: "综合",
      });
      if (res.success) {
        if (res.rows.length > 4) {
          this.schemeList = res.rows.slice(0, 4);
        } else {
          this.schemeList = res.rows;
        }

        // if (this.fanganList.length > 0) {
        //   this.planId = this.fanganList[0].id;
        //   this.getTideAlertData("forecast");
        // } else {
        //   this.planId = "";
        // }
        // this.getTideAlertData("forecast");
        // this.getSeawallList();
      }
    },
    checkScheme(id, type) {
      // this.schemeDialogVisible = true;
      //设置风暴潮选中id
      this.$store.commit("SET_PLAN_ID", id);
      //清除红黄码选中状态
      this.$store.commit("SET_COLORCODE", "all");

      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);

      // 通用
      this.$parent.$refs.seawallMap.closeAllOverly(); // 关闭弹窗
      this.$store.commit("SET_SKZG_SWITCH_LEGEND", true); // 打开图例
      this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
      clickLog({
        title: "智慧水库-" + type,
      });
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      // this.$nextTick(() => {
      // 	this.$refs.generation.$refs.tb_wrappers.doLayout();
      // });
    },

    // 附属建筑物 统计
    async getAffiliateData() {
      let opt = {
        adcd: this.sgzgSelectAdcd,
        basinCode: this.sgzgSelectBasin,
      };

      let res = await zhhtSeawallAssPrcd(opt);
      if (res.success && res.data.length !== 0) {
        this.affiliateStatistic = res.data[0];
      } else {
        this.affiliateStatistic = {
          dzxGateNum: 0,
          sluiceNum: 0,
          pumpNum: 0,
          xxGateNum: 0,
        };
      }
    },
    // 口门管控 统计
    async getfDData() {
      let res = await zhSeawall.seawallDoor({
        adcd: this.sgzgSelectAdcd,
        basinCode: this.sgzgSelectBasin,
      });
      if (res.success) {
        this.facility = res.data[0];
      }
    },

    chooseMapType(type, numberSelected) {
      clickLog({
        title: "智慧海塘-" + type,
      });
      this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
      this.$store.commit("SET_NUMBER_SELECT", "");
      if (type === '口门管控' && !numberSelected) numberSelected = '旱闸'
      if (numberSelected) {
        this.$store.commit("SET_NUMBER_SELECT", numberSelected);
      }
      if (type === "防潮分析") {
        let planId = "";
        if (this.schemeList.length > 0) {
          planId = this.schemeList[0].id;
        }
        this.checkScheme(planId, "防潮分析");
      }
      this.$parent.$refs.seawallMap.closeAllOverly();
      this.$parent.$refs.seawallMap.removeAllLayer();
    },

    handleCloseHuimin() {
      this.huiminDialogVisible = false;
    },
  },
};
</script>

<style lang='scss' scoped>
.cursor {
  cursor: pointer;
}
.third_title:before {
  content: none;
}
.iframe-box {
  width: 100%;
  height: 600px;
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
    // cursor: pointer;
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
    z-index: 2;
    box-sizing: border-box;

    &.last_title {
      display: flex;
      flex-direction: row;
      align-items: center;
      span {
        transition: all 0.2s linear;
        margin-right: 25px;
        font-size: 24px;
        color: rgba(226, 237, 255, 0.5);
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
  }

  .third_title {
    @include flexbox();
    @include flexAI(center);
    // @include flexJC();
    font-weight: bold;
    color: #fff;
    margin-bottom: 4px;
    ::before {
      content: none;
    }
    .point {
      width: 20px;
      margin-right: 5px;
    }
    span {
      font-family: "youshe";
      color: rgba(226, 237, 255, 0.5);
      font-size: 20px;
      user-select: none;
      margin-right: 10px;
      // transition: all 0.2s linear 0s;
      &.active {
        font-size: 24px;
        color: #fff;
      }
    }
    i {
      display: block;
      height: 1px;
      @include flex(1);
      border-bottom: 1px solid #4c70a3;
      margin: 0 10px;
    }
    .arr {
      width: 30px;
    }
  }
  .right_top {
    height: calc(28% - 15px);
    overflow: hidden;
    .first_box {
      height: 100%;
      .third_title {
        .active {
          display: flex;
          align-items: center;
        }
        .scheme {
          display: inline-block;
          width: 80px;
          height: 30px;
          border-radius: 2px;
          line-height: 30px;
          text-align: center;
          border: 1px solid rgba(43, 178, 249, 0.39);
          font-size: 16px;
          font-family: none;
          font-weight: 500;
          color: #69e3ee;
          margin-left: 8px;
        }
      }
      .scheme-list {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        align-items: center;
        padding: 2% 24px 0 0;
        height: calc(100% - 90px);
        .scheme-item {
          width: 194px;
          height: 45px;
          background: url("../../../assets/images/scheme-icon.png") no-repeat;
          background-size: 100% 100%;
          font-size: 16px;
          line-height: 45px;
          text-align: center;
          color: rgba(255, 255, 255, 0.6);
          margin: 2% 0;
          cursor: pointer;
          &.active-scheme-item {
            color: #46fdff;
          }
        }
      }
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
            .ba_unit {
              font-size: 12px;
              font-weight: 500;
            }
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
    height: calc(50% - 10px);
    overflow: hidden;
    .facilities_box {
      padding: 10px;
      height: 36%;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .facilities_item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20px 0 15px;
        P {
          font-family: "youshe";
          color: #46fdff;
          font-size: 26px;
          cursor: pointer;
        }
        div {
          font-size: 16px;
          color: rgba(255, 255, 255, 0.7);
          width: 30%;
          span {
            font-weight: bold;
            color: rgba(255, 255, 255, 1);
            font-size: 22px;
            padding: 0 8px 0 16px;
          }
        }
      }
    }
    .gs_box {
      width: 100%;
      height: 36%;
      box-sizing: border-box;
      overflow: hidden;
      // padding-left: 10px;
      ul {
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
        li {
          width: 100%;
          .num_title {
            font-size: 18px;
            color: rgba(230, 247, 255, 0.8);
            text-align: left;
            padding: 6px 0 10px 0px;
          }
          .num_text {
            padding-left: 0px;
          }
        }
      }
      .securityChart {
        width: 100%;
        height: calc(100% - 110px);
      }
      .gs_list {
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        padding-top: 14px;
        .gs_item {
          width: 48%;
          height: 50%;
          @include flexbox();
          @include flexAI(center);
          box-sizing: border-box;
          // padding-left: 15px;
          // margin-top: 50px;
          // margin-bottom: 10px;
          &.pl {
            width: 50%;
            // padding-left: 20px;
          }
          img {
            width: 90px;
            &.patrol-img {
              width: 58px;
              margin: 0 20px;
            }
          }
          &:nth-child(2n + 1) {
            div {
              padding: 5px;
            }
          }
          div {
            h3 {
              font-size: 18px;
              padding: 4px 0;
              color: rgba(230, 247, 255, 0.8);
            }
            p {
              @include flexbox();
              @include flexAI(center);
              color: rgba(230, 247, 255, 0.8);
              font-size: 16px;
              span {
                font-family: youshe;
                color: #fff;
                font-size: 24px;
                margin-right: 4px;
              }
            }
          }
        }
      }
    }
    .gg_box {
      width: calc(100% - 20px);
      // height: 100%;
      height: calc(100% - 60px);
      box-sizing: border-box;
      overflow: auto;
      margin-left: 20px;
      .gg_item {
        @include flexbox();
        @include flexAI(center);
        margin-top: 10px;
        img {
          // width: 61px;
          // height: 61px;
          margin-right: 15px;
          // @media screen and (min-height: 1000px) {
          //   width: 75px;
          //   height: 75px;
          // }
          // @media screen and (min-height: 1600px) and (max-height: 1900px){
          //   width: 50px;
          //   height: 50px;
          // }
          // @media screen and (min-height: 1900px) and (max-height: 2000px){
          //   width: 61px;
          //   height: 61px;
          // }
        }

        div {
          @include flex(1);
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          & > span {
            font-size: 20px;
            color: rgba(230, 247, 255, 0.8);
            text-align: center;
          }
          & > p {
            margin-top: 15px;
            font-size: 26px;
            font-family: "manrope";
            color: rgb(255, 255, 255);
            margin-right: 5px;
            letter-spacing: 3px;
            & > span {
              font-size: 18px;
              font-weight: 600;
              color: #00cfff;
              padding-left: 5px;
            }
          }
        }
      }
      .gg_rate {
        // margin-top: 15px;
        .rate_top {
          @include flexbox();
          @include flexAI(center);
          @include flexJC(space-between);
          p {
            font-size: 18px;
          }
          div {
            font-size: 20px;
            width: 75px;
            height: 30px;
            line-height: 29px;
            text-align: center;
            box-sizing: border-box;
            border: 2px solid #6be2e5;
            font-weight: bold;
            margin-right: 10px;
          }
        }
        .rate_body {
          // 图片进度条
          // margin-top: 15px;
          // background: url($imageUrl + 'rate.png') no-repeat center;
          // background-size: 100% auto;
          // height: 20px;
          // box-sizing: border-box;

          // diy 进度条
          .rate_box {
            margin-top: 10px;
            width: 100%;
            @include flexbox();
            @include flexAI(center);
            @include flexJC(space-between);
            .rate_item {
              width: 8%;
              height: 20px;
              background: #1843a9;
              border-radius: 4px;
              overflow: hidden;
              .rate_item_color {
                width: 0;
                height: 20px;
                background: #4ba6f4;
              }
            }
          }
        }
      }
    }
  }
  .right_bottom {
    height: calc(21% - 18px);
    overflow: hidden;
    .third_box {
      height: calc(100% - 50px);
      .tab_box {
        height: 100%;
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        justify-content: space-between;
        font-size: 26px;
        text-align: left;
        li {
          width: 50%;
          height: 50%;
          position: relative;
          img {
            width: 196px;
          }
          div.tab_box_item {
            position: absolute;
            top: 10px;
            left: 91px;
            p {
              font-size: 14px;
              color: #ffffff;
              &:nth-child(1) {
                color: #c7f1ff;
                padding-bottom: 3px;
                span {
                  color: #5ce1ff;
                  font-size: 20px;
                  padding-right: 5px;
                }
              }
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