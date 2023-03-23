<!--
 * @Date: 2021-12-23 15:28:33
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-27 09:06:29
 * @FilePath: \rscp-big-screen\src\views\sgzgScreenNew\zhekuHealthy\zhekuHealthyLeft\zhekuHealthyLeft.vue
-->
<template>
  <div class="map_left">

    <section class="cxjg_box" ref="reservoirHeight">
      <div class="left_title">
        <span>水库安全状态</span>
      </div>
      <div class="status_list" v-loading="skgmLoading">
        <div :class="['status_item']" v-for="(item, index) in statusList" :key="index">
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }} <i>{{item.type}}</i> </p>
            <div> <span style="font-size:22px">{{ item.value ? item.value : 0 }} <i>座</i></span>
              <i v-if="index !==0">|</i> <span style="font-size:16px" v-if="index !==0">{{ item.typeValue ? item.typeValue : 0 }} <i>座</i></span>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="cxjg_box" ref="reservoirHeight">
      <div class="left_title">
        <span>工程检查</span>
      </div>
      <div class="check_list" v-loading="skgmLoading">
        <div class="status_list">
          <!-- <div class="first_box"> -->

          <div class="status_item danger_item">
            <img src="../../../../assets/images/discoverQS.png" alt="">
            <div class="status_right">
              <p>发现问题</p>
              <span>4116<i>项</i></span>
            </div>
          </div>
          <div class="status_item danger_item">
            <img src="../../../../assets/images/NeedRectificationProject.png" alt="">
            <div class="status_right">
              <p>整改问题</p>
              <span>3787<i>项</i></span>
            </div>
          </div>
          <!-- <div class="left_box">
            <div class="top">
              <img src="../../../../assets/images/NeedRectificationProject.png" alt="">
              <span>整改问题</span>
            </div>
            <div class="bottom">
              <span class="num">1631</span>
              <span class="text">座</span>
            </div>
          </div>
          <div class="center_box">
          </div>
          <div class="right_box">
            <div class="top">
              <img src="../../../../assets/images/discoverQS.png" alt="">
              <span>发现问题</span>
            </div>
            <div class="bottom">
              <span class="num">4116</span>
              <span class="text">座</span>
            </div>
          </div> -->
        </div>
        <div class="second_box">
          <div :class="['status_box']" v-for="(item, index) in checkProList" :key="index">
            <img :src="item.src" alt="" />
            <div class="status_right">
              <div class="sec_left">
                <span>{{ item.name }}</span>
              </div>
              <div class="sec_right">
                <div>
                  <span class="num">{{ item.value}} </span>
                  <span class="text">座</span>
                </div>
                <div>
                  <span class="num">{{ item.personTimes}} </span>
                  <span class="text">人次</span>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </section>
    <section class="cxjg_box">
      <div class="left_title">
        <span>安全鉴定</span>
      </div>
      <div class="safe_list" v-loading="skgmLoading">
        <div :class="['status_item',item.className]" v-for="(item, index) in statusList" :key="index">
          <img :src="item.srcs" alt="" />
          <div class="status_right">
            <div class="top">
              <span>{{ item.names }}</span>
            </div>
            <div class="bottom">
              <span :class="['num',item.className]">{{ item.plan}} </span>
              <span class="text">座</span>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="cxjg_box" ref="reservoirHeight">
      <div class="left_title">
        <span>库区隐患</span>
      </div>
      <div class="status_list" v-loading="skgmLoading">
        <div class="status_item danger_item" v-for="(item, index) in statusList.slice(0,2)" :key="index">
          <img :src="item.dangersrcs" alt="" />
          <div class="status_right">
            <p>{{ item.dangerName }}</p>
            <span>{{ item.dangerNumber ? item.dangerNumber : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi, statisticCheckBigScreenApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {},
  data() {
    return {
      checkProList: [],
      statusList: [],
      loading_status: true,
      loading_inspect: true,
      skfbLoading: true,
      skgmLoading: true,
      skgmList: [],
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        adcd: this.$localData("get", "userInfo").adcd,
      },
      adcd: this.$localData("get", "userInfo").adcd,
    };
  },
  async mounted() {
    this.barHeight = (window.innerHeight - this.$refs.reservoirHeight.clientHeight) / 3 + "px";
    this.getSkjbtjData();
    this.getSkfbData();
    this.getStatusData();
    this.getInspectData();
  },
  methods: {
    //  工程检查情况
    async getInspectData() {
      this.loading_inspect = true;
      let otherArr = [];
      let opt = {
        projectCategory: "水库",
        projectScale: 'all',
      };
      let res = await statisticCheckBigScreenApi(opt);
      if (res.success) {
        otherArr = [{
          name: "本年度检查",
          value: 4296,
          personTimes: 23800,
          src: require("@/assets/images/bndjc.png"),
        },
        {
          name: "水利部督查",
          personTimes: 1387,
          value: 129,
          src: require("@/assets/images/slbdc.png"),
        },
        {
          name: "水利厅督查",
          value: 255,
          personTimes: 1933,
          src: require("@/assets/images/slbdc.png"),
        },
        {
          name: "地方自查",
          value: 5397,
          personTimes: 21488,
          src: require("@/assets/images/dfzc.png"),
        },
        ]
        this.checkProList = otherArr
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_inspect = false;
    },
    // 水库安全鉴定情况
    async getStatusData() {
      this.loading_status = true;
      let safeData = [];
      let opt = {
        statistic: "problemCnt",
        adcd: this.adcd,
        itemCategory: this.squareValue,
        itemValue: "1",
        superviseLevel: this.superviseLevel,
      };
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        safeData = [
          {
            name: "正常坝",
            type: '(一类坝)',
            value: 3478,
            names: "本年计划",
            plan: 764,
            dangerName: '森林防火',
            dangerNumber: 15,
            className: 'one',
            src: require("@/assets/images/threeTypesDams.png"),
            srcs: require("@/assets/images/bnjh.png"),
            dangersrcs: require("@/assets/images/fireGuard.png"),
          },
          {
            name: "病害坝",
            type: '(二类坝) ｜加固中',
            typeValue: 117,
            names: "本年到期",
            className: 'two',
            value: 601,
            plan: 108,
            dangerName: '蚁类防治',
            dangerNumber: 58,
            src: require("@/assets/images/unopenedDam.png"),
            srcs: require("@/assets/images/DueThisYear.png"),
            dangersrcs: require("@/assets/images/antGuard.png"),
          },
          {
            name: "危险坝",
            type: '(三类坝) ｜加固中',
            typeValue: 79,
            names: "本年完成",
            className: 'three',
            value: 217,
            plan: 1017,
            src: require("@/assets/images/reinforcement.png"),
            srcs: require("@/assets/images/bnwc.png"),
          },
        ];
        this.statusList = safeData;
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
        itemCategory: "scale",
        itemValue: '',
      };
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
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
          this.skgmList = otherArr;
          this.$store.commit(
            "SET_RESERVOIR_LEFT_SCALE_DATA",
            otherArr
          );
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.skgmLoading = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.map_left {
  z-index: 1002;
  .left_title {
    width: 100%;
    height: 35px;
    margin: 3% 0px 3% 0px;
    span {
      font: bold 20px italic;
      vertical-align: center;
      margin-left: 30px;
      line-height: 35px;
    }
    background: left/96% no-repeat url("../../../../assets/images/menu-bg.png");
  }
}
.cxjg_box {
  @include flexbox();
  @include flexJC(space-between);
  @include flexflow(column nowrap);
  .status_list {
    flex: 1;
    @include flexbox();
    @include flexJC(space-between);
    @include flexflow(row wrap);
    padding: 5px 5px;
    box-sizing: border-box;
    .status_item {
      flex: 1;
      @include flexbox();
      @include flexAC();
      @include flexflow(column nowrap);
      // cursor: pointer;
      border: 1px solid #fff;
      box-sizing: border-box;
      padding: 2px 0;

      &.active {
        background: rgba(25, 59, 196, 0.15);
        border: 1px solid #193bc4;
      }
      img {
        margin-right: 5px;
        margin-left: 5px;
        width: 36%;
      }
      .status_right {
        @include flexbox();
        @include flexAC();
        @include flexflow(column nowrap);
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
          font-size: 18px;
          color: #000;
          font-weight: bold;
        }
        div {
          & > span {
            line-height: 24px;
            font-size: 18px;
            color: #000;
            font-weight: bold;
          }
        }
      }
      &.danger_item {
        @include flexJC(center);
        @include flexAI(center);
        @include flexflow(row nowrap);
        img {
          width: 30%;
        }
        .status_right {
          @include flexJC(center);
          @include flexAI(center);
          @include flexflow(row nowrap);
          & > p {
            color: #000;
            font-weight: normal;
            margin-right: 5px;
          }
          & > span {
            padding: 8px 0 4px 0;
          }
        }
      }
    }
  }

  .check_list {
    flex: 1.5;
    padding: 0px 5px;
    .first_box {
      @include flexbox();
      @include flexJC(space-around);
      @include flexflow(row nowrap);
      width: calc(100% - 32px);
      height: 90px;
      margin: 0px 16px;
      background: center/100% url("../../../../assets/images/check_box.png")
        no-repeat;
      .center_box {
        display: flex;
        @include flexJC(center);
        @include flexAI(center);
        // width: 1px;
        // height: 50px;
        // background: #fff;
      }
      .left_box,
      .right_box {
        @include flexbox();
        @include flexJC(space-around);
        @include flexflow(column nowrap);
        margin: 10px 0px;
        color: #ffffff;
        .top,
        .bottom {
          @include flexbox();
          @include flexJC(center);
          @include flexAI(center);
          @include flexflow(row nowrap);
        }
        .top {
          img {
            margin-right: 10px;
            flex: 1;
          }
          span {
            flex: 3;
          }
        }
        .bottom {
          .num {
            font-size: 32px;
          }
          .text {
            margin-left: 5px;
            font-size: 16px;
          }
        }
      }

      .left_box .top,
      .right_box .bottom {
        @include flexbox();
        @include flexJC(center);
        @include flexAI(center);
        @include flexflow(row nowrap);
      }
    }
    .second_box {
      width: calc(100% - 32px);
      @include flexbox();
      @include flexJC(center);
      @include flexAI(center);
      @include flexflow(column wrap);
      margin: 0px 16px;
      .status_box {
        @include flexbox();
        @include flexJC(flex-start);
        @include flexAI(center);
        width: 90%;
        height: 60px;
        img {
          width: 15%;
          margin-right: 10px;
        }
        .status_right {
          width: 85%;
          @include flexbox();
          @include flexJC(space-between);
          @include flexflow(row nowrap);
          .sec_left {
            flex: 1;
            margin-left: 5px;
            font-size: 16px;
            color: #333333;
            img {
              flex: 1;
            }
            span {
              flex: 3;
            }
          }
          .sec_right {
            flex: 1.5;
            @include flexbox();
            @include flexJC(space-between);
            @include flexflow(row nowrap);
            .num {
              font-size: 26px;
              font-weight: 600;
            }
            .text {
              margin-left: 2px;
              font-size: 14px;
            }
          }
        }
      }
    }
  }
  .safe_list {
    width: 100%;
    height: 100%;
    @include flexbox();
    @include flexJC(space-around);
    padding: 10px 5px;
    box-sizing: border-box;
    .status_item {
      flex: 1;
      height: 110px;
      padding: 2% 0px;
      border-radius: 4px;
      &.one {
        border: 1px solid #3b58cd7e;
        background: 100% 100%/40% no-repeat
          url("../../../../assets/images/gray-shield.png");
      }
      &.two {
        border: 1px solid #ff9d09;
        background: 100% 100%/40% no-repeat
          url("../../../../assets/images/yellow-shield.png");
      }
      &.three {
        border: 1px solid #63b225;
        background: 100% 100%/40% no-repeat
          url("../../../../assets/images/green-shield.png");
      }
      background-position: 100% 100%;
      border: 1px solid #3b58cd7e;
      margin: 0px 10px;
      @include flexbox();
      @include flexJC(space-around);
      @include flexAI(center);
      @include flexflow(column nowrap);
      img {
        width: 30%;
      }
      .status_right {
        .top {
          text-align: center;
          margin-bottom: 10px;
          font-size: 18px;
          color: #000000;
        }
        .bottom {
          text-align: center;
          .num {
            font-size: 20px;
            font-weight: 600;
            &.one {
              color: #4a6bee;
            }
            &.two {
              color: #ff9d09;
            }
            &.three {
              color: #63b225;
            }
          }
          .text {
            margin-left: 5px;
            font-size: 16px;
          }
        }
      }
    }
  }
}
</style>
