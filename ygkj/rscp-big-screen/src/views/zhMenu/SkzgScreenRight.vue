
<template>
  <div ref="slide_wrap" class="slide_wrap">
    <div class="slide_title" @click="typeToGo('zhSeawallScreen')"></div>
    <section class="slide_section">
      <div class="right_top">
        <p class="second_title2">海塘总览</p>
        <div class="item_wrap">
          <ul class="gs_list">
            <li
              class="gs_item"
              v-for="(item, index) in htItemList"
              :key="index"
              @click="getSeawallList(index)"
            >
              <img :src="item.src" alt />
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
          <img class="point" :src="$config.imgUrl + 'point.png'" alt />
          <span class="active">等级</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt />
        </p>
        <div class="sk_bottom">
          <div class="sk_item" v-for="item in htNumList" :key="item.name">
            <h3>{{ item.name }}</h3>
            <p>
              {{ item.value || 0 }}
              <span>条</span>
            </p>
          </div>
        </div>
      </div>
      <div class="right_bottom">
        <section class="left_top">
          <p class="second_title2">
            海塘研判
            <!-- <img class="threeColorBtn" @click="coderuleDialogVisible = true" :src="$config.imgUrl + 'threeColorBtn.png'" title="三色码规则" /> -->
          </p>
          <ul ref="first_box" class="left_box first_box">
            <li
              @click="chooseMapType('风险研判')"
              :class="
                sgzgMapType == '风险研判' && htColorCode == 'all'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">总数</span>
              <span class="total_num">
                {{ colorsNum.TotalCount
                }}
                <span class="total_unit">条</span>
              </span>
            </li>
            <li
              @click="showOneColor('red')"
              :class="
                sgzgMapType == '风险研判' && htColorCode == 'red'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">红码</span>
              <span class="total_num red">
                {{ colorsNum.RedCount
                }}
                <span class="total_unit">条</span>
              </span>
            </li>
            <li
              @click="showOneColor('yellow')"
              :class="
                sgzgMapType == '风险研判' && htColorCode == 'yellow'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">黄码</span>
              <span class="total_num yellow">
                {{ colorsNum.yellowCount
                }}
                <span class="total_unit">条</span>
              </span>
            </li>
            <li
              @click="showOneColor('green')"
              :class="
                sgzgMapType == '风险研判' && htColorCode == 'green'
                  ? 'numberActive'
                  : ''
              "
            >
              <span class="total_text">绿码</span>
              <span class="total_num green">
                {{ colorsNum.GreenCount
                }}
                <span class="total_unit">条</span>
              </span>
            </li>
          </ul>
          <bar-chart-colors-code
            :class="['securityChart', showZjsNubmer ? 'hasNum' : 'noNum']"
            :chartData="barData"
            ref="seawallColorchart"
          />
        </section>
      </div>

   
      <section class="left_four">
          <p class="second_title2">
            <span>问题处置</span>
          </p>
          <div class="problem_box">
            <ul class="gs_list">
              <li class="gs_item" v-for="(item, index) in problemList" :key="index">
                <img :src="item.src" alt />
                <div>
                  <h3>{{ item.status }}</h3>
                  <p>
                    <span>{{ item.num }}</span>
                    {{ item.unit }}
                  </p>
                </div>
              </li>
            </ul>
          </div>
        </section>
    </section>
  </div>
</template>

<script>
import PrimaryDialog from "./components/PrimaryDialog.vue";
import BarChartColorsCode from "./components/BarChartColorsCode";
import { clickLog } from "@/api/api_reservoir";
import {
  zhSeawall,
  seawallLeftStatisticApi,
  seawallProIndo
} from "@/api/api_seawall";
import { resProstatistical } from "@/api/api_basin";
export default {
  name: "SkzgScreenRight",
  props: {},
  components: {
    PrimaryDialog,
    BarChartColorsCode
  },
  computed: {
    htColorCode() {
      return this.$store.state.htColorCode;
    },
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
    }
  },
  watch: {
    selectData: {
      handler(val) {
        this.getProStatistical();
        this.getHtStatistic();
        this.getColorsBarData();
        this.getColorsNum();
        this.getCXJGStatic();
        if (val.sgzgSelectAdcd == "" && val.sgzgSelectBasin == "") {
          this.showZjsNubmer = true;
        } else if (val.sgzgSelectAdcd.indexOf("0000000000") != -1) {
          this.showZjsNubmer = true;
        } else {
          this.showZjsNubmer = false;
        }
      },
      deep: true,
      immediate: true
    }
  },
  data() {
    return {
      clickType: "",
      problemList: [
        {
          status: "工程检查",
          num: 0,
          unit: "条次",
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
      htNumList: [
        {
          name: "1级",
          value: "1"
        },
        {
          name: "2级",
          value: "1"
        },
        {
          name: "3级",
          value: "1"
        },
        {
          name: "4级",
          value: "1"
        },
        {
          name: "5级",
          value: "1"
        }
      ],
      htItemList: [
        {
          name: "数量",
          num: 0,
          unit: "条",
          src: this.$config.swImgUrl + "htsl.png"
        },
        {
          name: "总长度",
          num: 0,
          unit: "km",
          src: this.$config.swImgUrl + "htcd.png"
        }
      ],
      colorsNum: {},
      showZjsNubmer: true,
      barLoading: true,
      barHeight: 150,
      barData: {
        id: "seawallCodeChart",
        xData: [],
        rData: [],
        yData: [],
        gData: [],
        adcdList: [],
        unit: "座",
        yInterval: 0,
        noWords: false
      },
      safeData: {},
      userAdcd: ""
    };
  },
  mounted() {
    this.userAdcd = this.$localData("get", "userInfo").adcd;
    this.getColorsBarData();
    this.getColorsNum();
    this.getProStatistical();
    this.getHtStatistic();
    this.getCXJGStatic();
    let userAdcd = this.$localData("get", "userInfo").adcd;
    if (userAdcd.indexOf("0000000000") != -1) {
      this.showZjsNubmer = true;
    } else {
      this.showZjsNubmer = false;
    }
    // this.barHeight = this.$refs.slide_wrap.offsetHeight - (this.$refs.first_box.offsetHeight + this.$refs.third_box.offsetHeight + 400)
  },
  methods: {
    /**start copy */
    typeToGo(path) {
      window.open("/screen/" + path);
      // this.$router.push('/' + path)
    },
    async getSeawallList(index) {
      if (index != 0) {
        return false;
      }
      this.apiOnce = true;
      this.pointArr = {
        绿码: [],
        红码: [],
        黄码: []
      };
      let opt = {
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        basinCode: this.sgzgSelectBasin,
        projectScale: this.projectType,
        extractPram: this.projectStatus
      };
      this.$parent.$refs.skzgMap.removeAllLayer();
      let res = await seawallProIndo(opt);
      if (res.success) {
        this.apiOnce = false;
        let lineArr = [];
        if (res.data.length) {
          res.data.map(v => {
            v.lat = v.lttd;
            v.lng = v.lgtd;
            v.styleObj = {
              scale: 1
            };
            v.mapType = "seawallPopup";
            switch (v.threeYards) {
              case "绿色":
                v.styleObj.src = this.$config.imgUrl + "greencode.png";
                this.pointArr["绿码"].push(v);
                break;
              case "红色":
                v.styleObj.src = this.$config.imgUrl + "redcode.png";
                this.pointArr["红码"].push(v);
                break;
              case "黄色":
                v.styleObj.src = this.$config.imgUrl + "yellowcode.png";
                this.pointArr["黄码"].push(v);
                break;
            }
            lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                    v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                : "",
              level: v.project_scale,
              type: "seawallPopup"
            });
            v.lineArr = [];
            v.lineArr.push({
              line: v.coorpot
                ? JSON.parse(
                    v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                : "",
              level: v.seawallPopup,
              type: "seawallPopup"
            });
          });
        }
        this.showPoints();
        this.$parent.$refs.skzgMap._drawLines(lineArr);
      }
    },
    showPoints() {
      let pointArr = [];
      let tempArr = [];
      let color = this.$store.state.htColorCode;
      switch (color) {
        case "green":
          tempArr = ["绿码"];
          break;
        case "yellow":
          tempArr = ["黄码"];
          break;
        case "red":
          tempArr = ["红码"];
          break;
        case "all":
        case "":
          tempArr = ["红码", "黄码", "绿码"];
          break;
      }
      for (let i = 0; i < tempArr.length; i++) {
        pointArr.push(...this.pointArr[tempArr[i]]);
      }
      this.$parent.$refs.skzgMap.newMapJh(pointArr, "seawallPopup");
    },

    // 获取 库容 流量 长度等统计数据
    async getProStatistical() {
      let res = await resProstatistical({
        adcd: this.$localData("get", "userInfo").adcd
      });
      if (res.success && res.rows.length) {
        this.htItemList[1].num = res.rows[0].seawallLen.toFixed(0);
      }
    },
    // 海塘级别统计
    async getHtStatistic() {
      let res = await seawallLeftStatisticApi({
        statistic: "scale",
        adcd: this.$localData("get", "userInfo").adcd
      });
      if (res.success && res.data.length) {
        let totalNum = 0;
        for (let i = 0; i < res.data.length; i++) {
          totalNum += res.data[i].num;
          if (res.data[i].project_scale != 6) {
            switch (res.data[i].project_scale) {
              case "1":
                this.htNumList[0].value = res.data[i].num;
                break;
              case "2":
                this.htNumList[1].value = res.data[i].num;
                break;
              case "3":
                this.htNumList[2].value = res.data[i].num;
                break;
              case "4":
                this.htNumList[3].value = res.data[i].num;
                break;
              default:
                this.htNumList[4].value = res.data[i].num;
                break;
            }
          }
        }
        this.htItemList[0].num = totalNum;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.htjbtjLoading = false;
    },
    // 海塘 三色码 统计
    async getColorsNum() {
      let res = await zhSeawall.seawallThreeCodeSta({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd
      });
      if (res.success) {
        this.colorsNum = res.data;
      }
    },
      // 问题处置
      async getCXJGStatic(type) {
      let res = await zhSeawall.problemDealSta({
        gpType: type,
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      });
      if (res.success) {
        this.problemList[1].num = res.data.problemNum;
        this.problemList[2].num = res.data.dealingNum;
        this.problemList[3].num = res.data.dealedNum;
        this.problemList[0].num =
          res.data.problemNum + res.data.dealingNum + res.data.dealedNum;
      }
    },
    //  三色码 柱状图
    async getColorsBarData() {
      this.barLoading = true;
      let res = await zhSeawall.seawallthreeYardBarChart({
        basinCode: this.sgzgSelectBasin,
        adcd: this.sgzgSelectAdcd
          ? this.sgzgSelectAdcd
          : this.$localData("get", "userInfo").adcd
      });
      if (res.success) {
        let xData = [];
        let rData = [];
        let yData = [];
        let gData = [];
        let adcdList = [];
        if (res.rows.length > 0) {
          res.rows.forEach(element => {
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
    chooseMapType(type, numberSelected) {
      clickLog({
        title: "智慧水库-" + type
      });
      const noSelectValue = ["检查总量", "发现问题"];
      if (numberSelected) {
        if (!noSelectValue.includes(numberSelected)) {
          this.$store.commit("SET_NUMBER_SELECT", numberSelected);
        } else {
          return;
        }
      } else {
        this.$store.commit("SET_NUMBER_SELECT", "");
      }
      //清除红黄码选中状态
      this.$store.commit("SET_HTCOLORCODE", "all");
      this.$refs.seawallColorchart.initChart("all");

      // 关闭蓄水量浮窗开关
      this.$store.commit("SET_SKZG_SWITCH_XSL", false);

      // 通用
      // this.$parent.$refs.seawallMap.closeAllOverly(); // 关闭弹窗
      // this.$store.commit("SET_SKZG_SWITCH_LEGEND", true); // 打开图例
      // this.$store.commit("SET_SKZG_MAP_TYPE", type); //切换图例
    },
    showOneColor(color) {
      this.chooseMapType("风险研判");
      this.$refs.seawallColorchart.initChart(color);
      this.$store.commit("SET_HTCOLORCODE", color);
    }
    /**end copy */
  }
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
      background: url($swImgUrl+"splitLine.png") no-repeat center;
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
    background: url("../../assets/images/seawall-bg.png") no-repeat center;

    // background: url($imageUrl+"title_bg.png") no-repeat center;
    background-size: cover;
    cursor: pointer;
  }
  .slide_section {
    height: calc(100% - 48px);
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
    background: url($imageUrl+"second_title.png") no-repeat bottom;
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
        cursor: pointer;
        &.active {
          font-size: 30px;
          color: rgb(238, 246, 255);
        }
      }
    }
  }
  .right_top {
    height: calc(39% - 24px);
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
  }
  .left_four {
    height: calc(33% - 24px);
    overflow: hidden;
    .problem_box {
      height: calc(100% - 5px);
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
  .right_bottom {
    height: 35%;
    overflow: hidden;
    .left_top {
      width: 100%;
      height: 100%;
      overflow: hidden;
      .threeColorBtn {
        margin-left: 10px;
        width: 20px;
      }
      .all_num {
        width: 100%;
        @include flexbox();
        @include flexAI(center);
        margin-bottom: 3px;
        padding-left: 5px;
        p {
          color: #46fdff;
          font-size: 21px;
          font-family: youshe;
          margin-right: 15px;
          span {
            font-size: 22px;
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
        margin: 3px 0px;
        &.left_box {
          li {
            flex: 1;
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
          height: calc(100% - 105px);
        }
        &.noNum {
          height: calc(100% - 105px);
        }
      }
    }
  }
}
</style>