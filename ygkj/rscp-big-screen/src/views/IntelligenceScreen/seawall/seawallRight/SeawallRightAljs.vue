<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-17 20:08:22
 * @Description: 地图右侧-安澜建设
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallRight\SeawallRightAljs.vue
-->
<template>
  <div class="wrap">
    <div class="check-btn-box">
      <div class="typhoon-name">
        <!-- <span v-if="currentTyphoon.name"
          >{{ currentTyphoon.tfid + currentTyphoon.name }}({{
            currentTyphoon.enname
          }})</span
        > -->
        <span v-show='!checkDataType'>202212梅花(Muifa)</span>
      </div>
      <div class="check-btn" @click="checkDataType = !checkDataType">
        <!-- <div class="check-btn"> -->
        <div>
          <img v-show="checkDataType" src="../../../../assets/images/check-btn.png" alt="" />
          <img v-show="!checkDataType" class="back-icon" src="../../../../assets/images/back-icon.png" alt="" />
        </div>
        <span>{{ checkDataType ? "复盘" : "返回" }}</span>
      </div>
    </div>
    <div class="flow-box">
      <div class="flow-item">
        <img src="../../../../assets/images/flow-icon.png" alt="" />
        <p>
          风险海塘清单<span>{{ checkDataType ? 0 : 106 }}</span>条次
        </p>
      </div>
      <div class="flow-arrow"></div>
      <div class="flow-item">
        <img src="../../../../assets/images/flow-icon.png" alt="" />
        <p>
          发送责任单位<span>{{ checkDataType ? 0 : 25 }}</span>个
        </p>
      </div>
      <div class="flow-arrow"></div>
      <div class="flow-practicable">
        <div class="practicable-title">责任单位落实</div>
        <div class="practicable-list">
          <div class="practicable-item">
            <p>未处置</p>
            <p><span>0</span>件</p>
          </div>
          <div class="practicable-item">
            <p>处置中</p>
            <p><span>0</span>件</p>
          </div>
          <div class="practicable-item">
            <p>已处置</p>
            <p>
              <span>{{ checkDataType ? 0 : 106 }}</span>件
            </p>
          </div>
        </div>
      </div>
      <!-- <div class="flow-arrow"></div>
      <div class="flow-item">
        <img src="../../../../assets/images/flow-icon.png" alt="" />
        <p>落实反馈清单<span>280</span>条</p>
      </div> -->
    </div>
    <div class="measure-box">
      <div class="measure-title">措施落实</div>
      <div class="patrol-box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            加密巡查
          </div>
        </div>
        <div class="patrol-list">
          <div class="patrol-detail">
            <img src="../../../../assets/images/patrol-detail.png" alt="" />
            巡查人次<span>{{ checkDataType ? 0 : 7210 }}</span>次
          </div>
          <!-- <div class="patrol-item">
            <img src="../../../../assets/images/patrol-num.png" alt="" />
            <div>
              <p>巡查轮次</p>
              <p><span>161</span>轮</p>
            </div>
          </div>
          <div class="patrol-item">
            <img src="../../../../assets/images/patrol-person.png" alt="" />
            <div>
              <p>巡查人次</p>
              <p><span>320</span>人次</p>
            </div>
          </div> -->
        </div>
      </div>
      <div class="control-two-box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            口门封堵
          </div>
        </div>
        <div class="control-two-list">
          <div class="control-two-item" v-for="(item, index) in twoList" :key="index">
            <div>{{ item.name }}</div>
            <div>
              总数<span>{{ item.total }}</span>处
            </div>
            <div>
              关闭<span>{{ checkDataType ? 0 : item.close }}</span>处
            </div>
          </div>
        </div>
      </div>
      <div class="select_title-list">
        <div class="select_title select_title-item">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            应急加固
          </div>
          <div>
            <p>
              <span>{{ checkDataType ? 0 : 6 }}</span>条
            </p>
            <div class="select_title-line"></div>
            <p class="last-p">
              <span>{{ checkDataType ? 0 : 10 }}</span>处
            </p>
          </div>
        </div>
        <div class="select_title select_title-item">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            工程停工
          </div>
          <div>
            <p>
              <span>{{ checkDataType ? 0 : 90 }}</span>项
            </p>
            <div class="select_title-line"></div>
            <p class="last-p">
              <span>{{ checkDataType ? 0 : 22 }}</span>个 县(市、区)
            </p>
          </div>
        </div>
        <div class="select_title select_title-item">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            人员转移
          </div>
          <div>
            <p>
              <span>{{ checkDataType ? 0 : 10565 }}</span>人
            </p>
            <div class="select_title-line"></div>
            <p class="last-p">
              <span>{{ checkDataType ? 0 : 22 }}</span>个 县(市、区)
            </p>
          </div>
        </div>
      </div>
      <!-- <div class="reinforce-box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            措施落实
          </div>
        </div>
        <div class="reinforce-list">
          <div class="reinforce-item reinforce-item-line">
            <img src="../../../../assets/images/reinforce-a.png" alt="" />
            <p>应急加固</p>
            <div>
              <p><span>134</span>条</p>
              <i class="line"></i>
              <p><span>155</span>处</p>
            </div>
          </div>
          <div class="reinforce-item reinforce-item-line">
            <img src="../../../../assets/images/reinforce-b.png" alt="" />
            <p>工程停工</p>
            <div>
              <p><span>26</span>项</p>
            </div>
          </div>
          <div class="reinforce-item">
            <img src="../../../../assets/images/reinforce-c.png" alt="" />
            <p>人员转移</p>
            <div>
              <p><span>6300</span>人</p>
            </div>
          </div>
        </div>
      </div> -->
    </div>
  </div>
</template>

<script>
  import { seawallLeftStatisticApi } from "@/api/api_seawall";
  export default {
    name: "SeawallRightThree",
    computed: {
      leftSelectAdcd() {
        return this.$store.state.leftSelectAdcd;
      },
    },
    watch: {
      leftSelectAdcd(val) {
        this.getSeawallList();
      },
    },
    data() {
      return {
        checkDataType: true,
        twoList: [
          {
            name: "旱闸",
            total: 451,
            close: 451,
          },
          {
            name: "缺口",
            total: 251,
            close: 251,
          },
          {
            name: "道口",
            total: 462,
            close: 462,
          },
          // {
          //   name: "涵闸",
          //   total: 229,
          //   close: 75,
          // },
        ],
        currentTyphoon: {},
      };
    },
    mounted() {
      this.getSeawallList();
      this.getCurrentTyphoon();
    },
    methods: {

      async getSeawallList() {
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
        let opt = {
          statistic: "mapList",
          itemCategory: "forecast,planId",
          itemValue: "overCnt,F2DB010A1E87451685EAC761D76FEBFD",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
        }
        let res = await seawallLeftStatisticApi(opt);
        if (res.success) {
          let pointArr = [];
          let lineArr = [];
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            res.data.map((v, i) => {
              let colorType = "";
              if (v.forecast > v.TIDE_ALERT_BLUE) {
                if (
                  v.forecast < v.TIDE_ALERT_YELLOW ||
                  v.forecast == v.TIDE_ALERT_YELLOW
                ) {
                  colorType = "blue";
                } else if (
                  v.forecast < v.TIDE_ALERT_ORANGE ||
                  v.forecast == v.TIDE_ALERT_ORANGE
                ) {
                  colorType = "yellow";
                } else if (
                  v.forecast < v.TIDE_ALERT_RED ||
                  v.forecast == v.TIDE_ALERT_RED
                ) {
                  colorType = "orange";
                } else {
                  colorType = "red";
                }
              } else {
                colorType = "default";
              }
              this.tableData[i].colorType = colorType;
              pointArr.push({
                lat: v.LTTD,
                lng: v.LGTD,
                NAME: v.NAME,
                PRCD: v.PRCD,
                rightType: "3",
                forecast_time: v.forecast_time,
                colorType: colorType,
                forecast: v.forecast,
                status: v.status,
                DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
                DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
                LOWEST_ELEVATION: v.LOWEST_ELEVATION,
                src: require("@/assets/images/map/gao_risk.png"),
              });
              lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                level: v.project_scale,
              });
              v.lineArr = [];
              v.lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                    v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                  )[0]
                  : "",
                level: v.project_scale,
              });
            });
          }
          this.$parent.showMapPoints(pointArr, "cwyj");
          this.$parent.showMapLines(lineArr);
        }
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
      },
    },
  };
</script>

<style lang='scss' scoped>
.wrap {
  padding: 16px;
  overflow: auto;
  height: 100%;
  box-sizing: border-box;
  .select_title {
    padding: 16px 0px 10px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-weight: 500;
      font-size: 18px;
      img {
        margin-right: 5px;
      }
      &:first-child {
        font-weight: 600;
      }
    }
  }
  .check-btn-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 16px;
    .typhoon-name {
      font-family: "youshe";
      font-size: 22px;
      color: #000000;
      font-weight: bold;
    }
    .check-btn {
      font-size: 16px;
      color: #2e3d53;
      cursor: pointer;
      // cursor: not-allowed;
      width: 70px;
      display: flex;
      align-items: center;
      height: 22px;
      > div {
        width: 22px;
        margin-right: 8px;
        img {
          width: 19px;
          height: 22px;
          vertical-align: bottom;
          &.back-icon {
            width: 22px;
            height: 19px;
          }
        }
      }
    }
  }
  .flow-box {
    height: 30%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    > div {
      position: relative;
      width: 100%;
      background: #f8f8f8;
      border-radius: 4px;
      border: 1px solid #d6d6d6;
    }
    .flow-arrow {
      width: 15px;
      height: 22px;
      background: url("../../../../assets/images/flow-arrow.png") no-repeat;
      background-size: 100% 100%;
      border: none;
      margin: 0 auto;
    }
    .flow-item {
      height: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
      > img {
        width: 24px;
        height: 24px;
      }
      > p {
        color: #2e3d53;
        font-size: 16px;
        padding-left: 8px;
        span {
          font-size: 24px;
          padding: 0 4px 0 12px;
          font-weight: 600;
        }
      }
      &.active-flow {
        P {
          color: rgba(25, 59, 196, 1);
        }
      }
    }
    .flow-practicable {
      height: 116px;
      .practicable-title {
        font-size: 16px;
        text-align: center;
        padding-top: 8px;
        color: #2e3d53;
      }
      .practicable-list {
        padding: 8px 14px 0 12px;
        display: flex;
        justify-content: space-between;
        .practicable-item {
          width: 106px;
          height: 72px;
          background: #f8f8f8;
          border-radius: 2px;
          border: 1px solid #d6d6d6;
          P {
            text-align: center;
            color: #000000;
            line-height: 24px;
            &:nth-child(1) {
              padding-top: 10px;
              font-size: 14px;
            }
            &:nth-child(2) {
              padding-top: 4px;
              font-size: 16px;
              font-weight: 600;
              span {
                font-size: 24px;
                padding-right: 5px;
              }
            }
          }
        }
      }
    }
  }
  .measure-box {
    height: calc(65.5% - 18px);
    margin-top: 16px;
    background: #f8f8f8;
    border-radius: 4px;
    border: 1px solid #d6d6d6;
    padding: 0 15px;
    .measure-title {
      text-align: center;
      padding-top: 8px;
      font-size: 18px;
      color: #000000;
      line-height: 24px;
      font-weight: bold;
    }
    .select_title-list {
      height: 33%;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      padding-top: 16px;
      .select_title-item {
        > div:nth-child(2) {
          display: flex;
          align-items: center;
          p {
            font-size: 14px;
            color: rgba(0, 0, 0, 0.48);
            span {
              font-weight: 600;
              color: rgba(0, 0, 0, 0.88);
              font-size: 18px;
              padding-right: 8px;
            }
          }
          .select_title-line {
            width: 1px;
            height: 14px;
            background-color: #d6d6d6;
            margin: 0 15px;
          }
          .last-p {
            min-width: 65px;
          }
        }
      }
    }
  }
  .patrol-box {
    height: 20%;
    .patrol-list {
      display: flex;
      align-items: center;
      height: calc(100% - 42px);
      .patrol-detail {
        height: 48px;
        background: #f8f8f8;
        border-radius: 4px;
        border: 1px solid #d6d6d6;
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        font-size: 16px;
        color: #2e3d53;
        span {
          font-weight: bold;
          font-size: 24px;
          padding: 0 4px 0 12px;
        }
        > img {
          width: 25px;
          height: 24px;
          margin-right: 8px;
        }
      }
      .patrol-item {
        display: flex;
        width: 50%;
        img {
          width: 48px;
          height: 48px;
        }
        div {
          padding-left: 16px;
          p {
            color: #000000;
            font-size: 14px;
            &:nth-child(2) {
              padding-top: 4px;
              font-weight: 600;
              line-height: 32px;
              span {
                font-size: 20px;
                padding-right: 8px;
              }
            }
          }
        }
      }
    }
  }
  .control-two-box {
    height: 37%;
    .control-two-list {
      padding-top: 8px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: calc(100% - 50px);
      .control-two-item {
        background: #f0f0f0;
        padding: 10px 0 9px 16px;
        > div {
          display: inline-block;
          color: rgba(0, 0, 0, 0.48);
          font-size: 14px;
          span {
            font-size: 18px;
            color: rgba(0, 0, 0, 0.88);
            font-weight: bold;
            padding: 0 8px 0 18px;
          }
          &:nth-child(1) {
            color: #193bc4;
            line-height: 24px;
            font-weight: bold;
            padding-right: 24px;
            border-right: 1px solid #d6d6d6;
          }
          &:nth-child(2) {
            padding-left: 30px;
            width: 135px;
          }
        }
        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }
  .reinforce-box {
    height: 20%;
    .reinforce-list {
      padding: 3% 0 0 0;
      width: 100%;
      .reinforce-item {
        display: inline-block;
        width: 33.333%;
        text-align: center;
        img {
          width: 55px;
          height: 56px;
        }
        > p {
          font-size: 14px;
          padding: 14px 0 8px 0;
        }
        > div {
          > p {
            display: inline-block;
            color: #000000;
            font-weight: 600;
            span {
              font-size: 24px;
              font-weight: 600;
              padding-right: 8px;
            }
          }
          .line {
            display: inline-block;
            width: 1px;
            height: 14px;
            background-color: #d6d6d6;
            margin: 0 9px;
          }
        }
        &:first-child {
          > div {
            > p {
              span {
                font-size: 20px;
                padding-right: 0px;
              }
            }
          }
        }
        &.reinforce-item-line {
          position: relative;
          &::after {
            content: "";
            position: absolute;
            width: 1px;
            height: 80px;
            background-color: rgba(151, 151, 151, 0.2);
            right: 0;
            top: 5px;
          }
        }
      }
    }
  }
}
</style>