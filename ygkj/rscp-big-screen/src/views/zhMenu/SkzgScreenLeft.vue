<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title" @click="typeToGo('zhMenu')"></div>
    <section class="slide_section">
      <section class="left_top">
        <p class="second_title2">水库总览</p>
        <div class="item_wrap">
          <ul class="gs_list">
            <li
              class="gs_item"
              v-for="(item, index) in skItemList"
              :key="index"
              @click="getReservoirList(index)"
            >
              <img :src="item.src" alt="" />
              <div>
                <h3>{{ item.name }}({{ item.unit }})</h3>
                <p>
                  <span>{{ item.num }}</span>
                </p>
              </div>
            </li>
          </ul>
        </div>
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span class="active">规模</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
        </p>
        <!-- <p class="lines_title">
        <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
        <span class="active">规模</span>
        <i></i>
        <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
      </p> -->
        <div class="sk_bottom">
          <div class="sk_item" v-for="item in skNumList" :key="item.name">
            <h3>{{ item.name }}</h3>
            <p>{{ item.value || 0 }}<span>座</span></p>
          </div>
        </div>
      </section>

      <section class="left_bottom">
        <section class="left_top">
          <p class="second_title2">
            水库研判
            <!-- <img class="threeColorBtn" @click="coderuleDialogVisible = true" :src="$config.imgUrl + 'threeColorBtn.png'" title="三色码规则" /> -->
          </p>
          <!-- <div class="all_num" v-if="showZjsNubmer">
        <p>
          总数：<span>4296 (</span> 注销:<span class="cursor" @click="DegradedWorkVisible = true">19 ) </span>座
        </p>
        <div></div>
      </div> -->
          <ul ref="first_box" class="left_box first_box">
            <li
              @click="chooseMapType('风险研判')"
              :class="
                sgzgMapType == '风险研判' && colorCode == 'all'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">在册</span>
              <span class="total_num"
                >{{ colorsNum.TotalCount
                }}<span class="total_unit">座</span></span
              >
            </li>
            <li
              @click="showOneColor('red')"
              :class="
                sgzgMapType == '风险研判' && colorCode == 'red'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">红码</span>
              <span class="total_num red"
                >{{ colorsNum.RedCount
                }}<span class="total_unit">座</span></span
              >
            </li>
            <li
              @click="showOneColor('yellow')"
              :class="
                sgzgMapType == '风险研判' && colorCode == 'yellow'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">黄码</span>
              <span class="total_num yellow"
                >{{ colorsNum.yellowCount
                }}<span class="total_unit">座</span></span
              >
            </li>
            <li
              @click="showOneColor('green')"
              :class="
                sgzgMapType == '风险研判' && colorCode == 'green'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">绿码</span>
              <span class="total_num green"
                >{{ colorsNum.GreenCount
                }}<span class="total_unit">座</span></span
              >
            </li>
          </ul>
          <bar-chart-colors-code
            :class="['securityChart', showZjsNubmer ? 'hasNum' : 'noNum']"
            :chartData="barData"
            ref="threeColorchart"
          />
        </section>
        <p class="second_title2">问题处置</p>
        <div class="problem_box">
          <ul class="gs_list">
            <li
              class="gs_item"
              v-for="(item, index) in problemList"
              :key="index"
            >
              <img :src="item.src" alt="" />
              <div>
                <h3>{{ item.status }}</h3>
                <p>
                  <span>{{ item.num }}</span
                  >{{ item.unit }}
                </p>
              </div>
            </li>
          </ul>
        </div>
      </section>
    </section>

    <!-- 三色码 规则展示 -->
    <primary-dialog
      mainTitle="三色码规则"
      :visible="coderuleDialogVisible"
      @handleClose="handleCloseCode"
      @changeFullscreen="changeFullscreen"
      :fullscreen="fullscreen"
      width="850px"
      top="5vh"
      append-to-body
    >
      <div
        class="dialog-img-content threeColor"
        :class="{ 'fullscreen-box': fullscreen }"
        v-viewer
      >
        <img
          class="huimin threeColor-huimin"
          :src="$config.imgUrl + 'threeColorImg.png'"
          alt="三色码规则"
        />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import BarChartColorsCode from "./components/BarChartColorsCode";
import PrimaryDialog from "./components/PrimaryDialog.vue";
import {
  xzyyStatisticApi,
  threeMalCount,
  threeYardBarChart,
  generateProject,
  reservoirLeftStatisticApi,
  clickLog,
} from "@/api/api_reservoir";
import { resProstatistical } from "@/api/api_basin";
export default {
  name: "SgzgScreenLeft",
  props: {},
  components: {
    BarChartColorsCode,
    PrimaryDialog,
  },
  computed: {
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
    colorCode() {
      return this.$store.state.colorCode;
    },
    numberSelected() {
      return this.$store.state.numberSelected;
    },
  },

  watch: {
    selectData: {
      handler(val) {
        this.getCXJGStatic(5);
        this.getColorsNum();
        this.getColorsBarData();
        ////////
        this.getSkStatistic();
        // this.getProStatistical();
        this.getReservoirList(0);
        if (val.sgzgSelectAdcd == "" && val.sgzgSelectBasin == "") {
          this.showZjsNubmer = true;
        } else if (val.sgzgSelectAdcd.indexOf("0000000000") != -1) {
          this.showZjsNubmer = true;
        } else {
          this.showZjsNubmer = false;
        }
      },
      deep: true,
      immediate: false,
    },
  },
  data() {
    return {
      skItemList: [
        {
          name: "数量",
          num: 4271,
          unit: "座",
          src: this.$config.swImgUrl + "skzsl.png",
        },
        {
          name: "总库容",
          num: 448,
          unit: "亿方",
          src: this.$config.swImgUrl + "skzkr.png",
        },
      ],
      skNumList: [
        {
          name: "大(1)型",
          value: 5,
        },
        {
          name: "大(2)型",
          value: 29,
        },
        {
          name: "中型",
          value: 159,
        },
        {
          name: "小(1)型",
          value: 728,
        },
        {
          name: "小(2)型",
          value: 3356,
        },
      ],
      coderuleDialogVisible: false,
      DegradedWorkVisible: false,
      barLoading: true,
      barHeight: 150,
      barData: {
        id: "colorsCodeChart",
        xData: [],
        rData: [],
        yData: [],
        gData: [],
        adcdList: [],
        unit: "座",
        yInterval: 0,
        noWords: false,
      },
      problemList: [
        {
          status: "检查总量",
          num: 0,
          unit: "座次",
          src: this.$config.imgUrl + "totalInspection.png",
        },
        {
          status: "发现问题",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "problemFound.png",
        },
        {
          status: "处置中",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "problemFound.png",
        },
        {
          status: "已处置",
          num: 0,
          unit: "项",
          src: this.$config.imgUrl + "hadDispose.png",
        },
      ],
      timer: null,
      fullscreen: false,
      colorsNum: {},
      inXq: false,
      showColor: "all",
      chart: {},
      showZjsNubmer: true,
    };
  },
  mounted() {
    // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.four_box.offsetHeight + this.$refs.third_box.offsetHeight + this.$refs.third_box.offsetHeight + 100 + 320 + 150)
    this.getCXJGStatic(5);
    this.getColorsNum();
    this.getColorsBarData();
    //////
    this.getSkStatistic();
    // this.getProStatistical();
    this.getReservoirList(0);
    let userAdcd = this.$localData("get", "userInfo").adcd;
    if (userAdcd.indexOf("0000000000") != -1) {
      this.showZjsNubmer = true;
    } else {
      this.showZjsNubmer = false;
    }

    let nowTime = new Date(new Date().format("MM-dd"));
    let startTime = new Date("04-15");
    let endTime = new Date("10-14");
    if (nowTime >= startTime && nowTime <= endTime) {
      this.inXq = true;
    }
  },
  methods: {
    typeToGo(path) {
      window.open("/screen/" + path);
      // this.$router.push('/' + path)
    },
    async getReservoirList() {
      if (index != 0) return false;
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        gpType: 1,
        projectScale: "1,2,3,4",
        screen: "17,18,19",
      };
      this.$parent.$refs.skzgMap.removeHtLayer();
      let res = await generateProject(opt);
      if (res.success) {
        if (res.data.length) {
          res.data.map((v) => {
            v.lat = v.LTTD;
            v.lng = v.LGTD;
            v.mapType = "reservoirPopup";
            v.styleObj = {
              scale: 0.5,
              src: this.$config.swImgUrl + "sk_jh.png",
            };
          });
        }
        this.$parent.showMapPointsJh(res.data, "PointLayer", "sk");
      }
    },
    // 获取 库容 流量 长度等统计数据
    async getProStatistical() {
      let res = await resProstatistical({
        adcd: this.$localData("get", "userInfo").adcd,
      });
      if (res.success && res.rows.length) {
        this.skItemList[1].num = res.rows[0].resTcp.toFixed(0);
      }
    },
    // 水库级别统计
    async getSkStatistic() {
      this.squareList = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        superviseLevel: this.superviseLevel,
      });
      if (res.success) {
        let arr = [];
        for (let i = 0; i < res.data.length; i++) {
          let obj = {};
          obj.value = res.data[i].num;
          switch (res.data[i].project_scale) {
            case "1":
              obj.name = "大(1)型";
              break;
            case "2":
              obj.name = "大(2)型";
              break;
            case "3":
              obj.name = "中型";
              break;
            case "4":
              obj.name = "小(1)型";
              break;
            case "5":
              obj.name = "小(2)型";
              break;
          }
          arr.push(obj);
        }
        this.skNumList = arr;
      }
      this.loading_square = false;
    },
    /** 水库不聚合红黄绿码落点 start */
    // async getReservoirList(index) {
    //   if (index != 0) {
    //     return false;
    //   }
    //   this.apiOnce = true;
    //   this.pointArr = {
    //     绿码: [],
    //     红码: [],
    //     黄码: [],
    //   };
    //   let opt = {
    //     adcd: this.sgzgSelectAdcd
    //       ? this.sgzgSelectAdcd
    //       : this.$localData("get", "userInfo").adcd,
    //     basinCode: this.sgzgSelectBasin,
    //     gpType: 1,
    //     projectScale: this.projectType,
    //     screen: this.projectStatus,
    //   };
    //   this.$parent.$refs.skzgMap.removeHtLayer();
    //   let res = await generateProject(opt);
    //   if (res.success) {
    //     this.apiOnce = false;
    //     if (res.data.length) {
    //       res.data.map((v) => {
    //         v.lat = v.LTTD;
    //         v.lng = v.LGTD;
    //         v.mapType = "reservoirPopup";
    //         v.styleObj = {
    //           scale: 0.5,
    //         };
    //         switch (v.CODE) {
    //           case "绿色":
    //             v.styleObj.src = this.$config.imgUrl + "greencode.png";
    //             this.pointArr["绿码"].push(v);
    //             break;
    //           case "红色":
    //             v.styleObj.src = this.$config.imgUrl + "redcode.png";
    //             this.pointArr["红码"].push(v);
    //             break;
    //           case "黄色":
    //             v.styleObj.src = this.$config.imgUrl + "yellowcode.png";
    //             this.pointArr["黄码"].push(v);
    //             break;
    //         }
    //       });
    //     }
    //     this.showPoints();
    //   }
    // },
    // showPoints() {
    //   let pointArr = [];
    //   let tempArr = [];
    //   let color = this.$store.state.colorCode;
    //   switch (color) {
    //     case "green":
    //       tempArr = ["绿码"];
    //       break;
    //     case "yellow":
    //       tempArr = ["黄码"];
    //       break;
    //     case "red":
    //       tempArr = ["红码"];
    //       break;
    //     case "all":
    //     case "":
    //       tempArr = ["红码", "黄码", "绿码"];
    //       break;
    //   }
    //   for (let i = 0; i < tempArr.length; i++) {
    //     pointArr.push(...this.pointArr[tempArr[i]]);
    //   }
    //   this.$parent.$refs.skzgMap.drawMapPoints(pointArr, "reservoirPopup");
    // },
    /** 水库不聚合红黄绿码落点 end */
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    async getColorsNum() {
      let res = await threeMalCount({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        this.colorsNum = res.data;
      }
    },
    async getColorsBarData() {
      this.barLoading = true;
      let res = await threeYardBarChart({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        let xData = [];
        let rData = [];
        let yData = [];
        let gData = [];
        let adcdList = [];
        if (res.data.length > 0) {
          res.data.forEach((element) => {
            if (element.red + element.yellow + element.green != 0) {
              if (element.adnm.length > 2) {
                xData.push(element.adnm.replace(/市/, ""));
              } else {
                xData.push(element.adnm);
              }
              rData.push(element.red);
              yData.push(element.yellow);
              gData.push(element.green);
              adcdList.push(element.adcd);
            }
          });
        }
        this.barData.xData = xData;
        this.barData.rData = rData;
        this.barData.yData = yData;
        this.barData.gData = gData;
        this.barData.adcdList = adcdList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.barLoading = false;
    },
    async getCXJGStatic(type) {
      let res = await xzyyStatisticApi({
        gpType: type,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd,
      });
      if (res.success) {
        this.problemList[0].num = res.data[0].checkNum;
        this.problemList[1].num = res.data[0].problemNum;
        this.problemList[2].num = res.data[0].czzNum;
        this.problemList[3].num = res.data[0].yczNum;
      }
    },
    handleCloseCode() {
      this.fullscreen = false;
      this.coderuleDialogVisible = false;
    },
    chooseMapType(type, numberSelected) {
      clickLog({
        title: "智慧水库-" + type,
      });
      const noSelectValue = ["检查总量", "发现问题"];
      if (numberSelected) {
        if (!noSelectValue.includes(numberSelected)) {
          this.$store.commit("SET_NUMBER_SELECT", numberSelected);
        } else {
          return;
        }
      } else {
        if (type == "存量清零") {
          this.$store.commit(
            "SET_NUMBER_SELECT",
            "已竣工,加固中,已完工,项目前期"
          );
        } else {
          this.$store.commit("SET_NUMBER_SELECT", "");
        }
      }
      //清除红黄码选中状态
      this.$store.commit("SET_COLORCODE", "all");
      this.$refs.threeColorchart.initChart("all");

      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);

      // 通用
      // this.$parent.$refs.skzgMap.closeAllOverly(); // 关闭弹窗
      // this.$store.commit("SET_SKZG_SWITCH_LEGEND", true); // 打开图例
      // this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
    },
    showOneColor(color) {
      this.chooseMapType("风险研判");
      this.$refs.threeColorchart.initChart(color);
      this.$store.commit("SET_COLORCODE", color);
    },
  },
  beforeDestroy() {
    this.timer = null;
  },
};
</script>

<style lang='scss' scoped>
.item_wrap {
  height: calc(50% - 60px);
  .gs_list {
    @include flexbox();
    @include flexAI(center);
    @include flexflow(row wrap);
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    // padding-left: 16px;
    .gs_item {
      width: 48%;
      height: 50%;
      @include flexbox();
      @include flexAI(center);
      box-sizing: border-box;
      &.pl {
        width: 52%;
      }
      img {
        width: 90px;
        height: 60px;
      }
      &:nth-child(1) {
        cursor: pointer;
      }
      &:nth-child(2n + 1) {
        div {
          padding: 5px;
        }
      }
      div {
        h3 {
          font-size: 16px;
          padding-top: 4px;
          padding-bottom: 8px;
          color: rgba(230, 247, 255, 0.8);
        }
        p {
          color: rgba(230, 247, 255, 0.8);
          font-size: 16px;
          span {
            vertical-align: text-bottom;
            font-family: youshe;
            color: #fff;
            text-shadow: 0 0 10px rgba(255, 255, 255, 0.3),
              0 0 20px rgba(255, 255, 255, 0.3), 0 0 30px rgba(0, 232, 255, 0.4),
              0 0 40px rgba(0, 232, 255, 0.4);
            font-size: 28px;
            margin-right: 4px;
          }
        }
      }
    }
  }
}
.sk_bottom {
  @include flexbox();
  @include flexAI(center);
  @include flexJC(flex-start);
  @include flexflow(row wrap);
  padding: 0 0 0 20px;
  height: 120px;
  .sk_item {
    width: 33%;
    // @include flexbox();
    // @include flexAI(center);
    // @include flexJC(center);
    // // flex-direction: column;
    padding: 10px 0px 10px 0px;
    position: relative;
    &:nth-child(1)::after,
    &:nth-child(2)::after,
    &:nth-child(4)::after {
      content: "";
      width: 2px;
      height: 36px;
      position: absolute;
      top: 20px;
      left: 100px;
      background: url($swImgUrl + "splitLine.png") no-repeat center;
      background-size: 100% auto;
    }
    h3 {
      font-size: 18px;
    }
    p {
      font-size: 22px;
      font-family: "manrope";
      padding-top: 5px;
      span {
        font-size: 18px;
        font-family: none;
        margin-left: 5px;
        color: #fff;
      }
    }
  }
}
.third_title:before {
  content: none;
}
.dialog-img-content {
  overflow: auto;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE 10+ */
  &::-webkit-scrollbar {
    display: none;
  }

  width: 100%;
  height: 620px;
  text-align: center;
  img.huimin {
    width: 100%;
    &.threeColor-huimin {
      width: 750px;
    }
  }
  &.degradedWork {
    height: 650px;
  }
  &.fullscreen-box .siwei-huimin {
    width: 70%;
  }
}
.slide_wrap {
  width: 100%;
  height: 100%;
  .slide_title {
    width: 92%;
    height: 48px;
    font-size: 36px;
    margin: 0px auto;
    font-family: "pangmen";
    @include flexbox();
    @include flexJC(center);
    @include flexAI(center);
    // background: url($imageUrl + "title_bg.png") no-repeat center;
    background: url("../../assets/images/reservoir-bg.png") no-repeat center;
    background-size: cover;
    cursor: pointer;
  }
  .slide_section {
    height: calc(100% - 48px);
  }
  .left_top {
    width: 100%;
    height: calc(39% - 24px);
    overflow: hidden;
    .threeColorBtn {
      margin-left: 10px;
      width: 20px;
    }
    .all_num {
      width: 100%;
      @include flexbox();
      @include flexAI(center);
      margin-bottom: 5px;
      padding-left: 5px;
      p {
        color: #46fdff;
        font-size: 22px;
        font-family: youshe;
        margin-right: 15px;
        span {
          font-size: 24px;
        }
      }
      div {
        @include flex(1);
        border-bottom: 1px solid #526aaf;
      }
    }
    .first_box {
      width: 100%;
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      margin: 4px 0px;
      &.left_box {
        // height: 100px;
        // margin-top: 3px;
        li {
          flex: 1;
          // height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: flex-start;
          padding: 0 5px;
          cursor: pointer;
          span {
            margin: 0px 0px -4px;
            white-space: nowrap;
          }
          .total_text {
            line-height: 30px;
            font-size: 18px;
            padding-left: 5px;
            color: rgba(216, 235, 255, 0.75);
            @include flexbox();
            @include flexJC(center);
            @include flexAI(center);
          }
          .total_num {
            align-items: baseline;
            padding: 5px 5px 0 5px;
            font-size: 32px;
            // font-family: "youshe";
            font-weight: bold;
            margin: 0px;
            color: #46fdff;
            &.red {
              color: #e4443d;
            }
            &.yellow {
              color: #ded052;
            }
            &.green {
              color: #6eec61;
            }
          }
          .total_unit {
            font-size: 18px;
            color: rgb(255, 255, 255);
            transform: translate3d(10px, -2px, 0px);
          }
        }
        .circle_box {
          flex: 0.9;
          position: relative;
          img.index-out {
            position: absolute;
            width: 100%;
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
            top: 25%;
            left: 25%;
            width: 50%;
          }
        }
      }
    }
    .securityChart {
      width: 100%;
      &.hasNum {
        height: calc(100% - 130px);
      }
      &.noNum {
        height: calc(100% - 105px);
      }
    }
  }
  .left_bottom {
    height: calc(65% - 24px);
    overflow: hidden;
    .left_top {
      height: calc(62% - 24px);
    }
    .threeColorBtn {
      margin-left: 10px;
      width: 20px;
    }
    .problem_box {
      height: calc(50% - 30px);
      .gs_list {
        @include flexbox();
        @include flexAI(center);
        @include flexflow(row wrap);
        width: 100%;
        height: calc(100% - 50px);
        box-sizing: border-box;
        padding: 10px 0;
        .gs_item {
          width: 48%;
          height: 50%;
          @include flexbox();
          @include flexAI(center);
          box-sizing: border-box;
          // padding-left: 15px;
          // margin-top: 50px;
          &.pl {
            width: 52%;
            // padding-left: 20px;
          }
          img {
            width: 90px;
            height: 60px;
            // margin-right: 10px;
          }
          &:nth-child(2n + 1) {
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
                margin-right: 4px;
              }
            }
          }
        }
      }
    }
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
    box-sizing: border-box;
    z-index: 2;
    &.last_title {
      display: flex;
      flex-direction: row;
      align-items: center;
      span {
        transition: all 0.2s linear;
        margin-right: 10px;
        font-size: 24px;
        color: rgba(226, 237, 255, 0.5);
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
    .codeRules {
      display: inline-block;
      margin-left: 10px;
      width: 24px;
      height: 24px;
      background: center/100% url($imageUrl + "coderules.png");
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
}
</style>