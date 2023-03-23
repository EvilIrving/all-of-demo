<!--
 * @Date: 2022-10-18 16:52:17
 * @Author: 
 * @Description: 闭环管控
 * @LastEditors: dtb
 * @LastEditTime: 2023-02-23 15:12:23
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallRight\SeawallRightAljs.vue
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
        <span v-show="!checkDataType">202212梅花(Muifa)</span>
      </div>
      <div class="check-btn" @click="checkData()">
        <!-- <div class="check-btn"> -->
        <div>
          <img v-show="checkDataType" src="../../../../assets/images/check-btn-a.png" alt="" />
          <img v-show="!checkDataType" class="back-icon" src="../../../../assets/images/back-icon-a.png" alt="" />
        </div>
        <span>{{ checkDataType ? "复盘" : "返回" }}</span>
      </div>
    </div>
    <div class="flow-box">
      <div class="flow-item">
        <img src="../../../../assets/images/flow-icon-a.png" alt="" />
        <p>
          风险海塘清单<span>{{ checkDataType ? 0 : 104 }}</span>条次
        </p>
      </div>
      <div class="flow-arrow"></div>
      <div class="flow-item">
        <img src="../../../../assets/images/flow-icon-a.png" alt="" />
        <p>
          发送责任单位<span>{{ checkDataType ? 0 : 24 }}</span>个次
        </p>
      </div>
      <div class="flow-arrow"></div>
      <div class="flow-practicable">
        <div class="practicable-title">责任单位落实</div>
        <div class="practicable-list">
          <div class="practicable-item">
            <p>未处置</p>
            <p><span>0</span></p>
          </div>
          <div class="practicable-item">
            <p>处置中</p>
            <p><span>0</span></p>
          </div>
          <div class="practicable-item">
            <p>已处置</p>
            <p>
              <span>{{ checkDataType ? 0 : 104 }}</span>
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

    <!-- 以下数字要求可以点击显示悬浮窗 统计为0不可点击 -->
    <div class="measure-box">
      <div class="measure-title">措施落实</div>
      <div class="patrol-box">
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span>加密巡查</span>
          <i></i>
          <!-- <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" /> -->
        </p>
        <div class="patrol-list">
          <div class="patrol-detail">
            <img src="../../../../assets/images/patrol-detail-a.png" alt="" />
            巡查人次<span :class="['cursor',{checked:clickType==='巡查人次', xunchaActive:clickType==='巡查人次'}]" @click='showThePopup("巡查人次", checkDataType ? 0 : 6743 )'>{{ checkDataType ? 0 : 6743 }}</span>次
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
        <!-- <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            口门封堵
          </div>
        </div> -->
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span>口门封堵</span>
          <i></i>
          <!-- <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" /> -->
        </p>
        <div class="control-two-list">
          <div class="control-two-item" v-for="(item, index) in twoList" :key="index">
            <div>{{ item.name }}</div>
            <div :class="{fengduActive:clickType===item.name + '总数'}">
              总数<span :class="['cursor',{checked:clickType===item.name + '总数'}]" @click='showThePopup(item.name + "总数",item.total)'>{{ item.total }}</span>处
            </div>
            <div :class="{fengduActive:clickType===item.name}">
              关闭<span :class="['cursor',{checked:clickType===item.name}]" @click='showThePopup(item.name,checkDataType ? item.close :item.fupan)'>{{ checkDataType ? item.close :item.fupan  }}</span>处
            </div>
          </div>
        </div>
      </div>
      <div class="select_title-list">
        <div class="select_title select_title-item">
          <div>
            <img :src="$config.imgUrl + 'point.png'" alt="" />
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
            <img :src="$config.imgUrl + 'point.png'" alt="" />
            工程停工
          </div>
          <div>
            <p>
              <span>{{ checkDataType ? 0 : 96 }}</span>项
            </p>
            <div class="select_title-line"></div>
            <p class="last-p">
              <span>{{ checkDataType ? 0 : 31 }}</span>个 县(市、区)
            </p>
          </div>
        </div>
        <div class="select_title select_title-item">
          <div>
            <img :src="$config.imgUrl + 'point.png'" alt="" />
            人员转移
          </div>
          <div>
            <p :class="{xunchaActive:clickType==='人员转移'}">
              <span :class="['cursor',{checked:clickType==='人员转移'}]" @click='showThePopup("人员转移" ,checkDataType ? 0 : 10565 )'>{{ checkDataType ? 0 : 10565 }}</span>人
            </p>
            <div class="select_title-line"></div>
            <p class="last-p">
              <span>{{ checkDataType ? 0 : 31 }}</span>个 县(市、区)
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
    <div class="control-measures-box">
      <div class="control-measures" @click="showDialog(3)">
        <img src="../../../../assets/images/control-measures-icon-b.png" alt="" />
        <span>一键生成</span>
      </div>
      <div class="control-measures" @click="showDialog(1)">
        <img src="../../../../assets/images/control-measures-icon-a.png" alt="" />
        <span>落实清单</span>
      </div>
      <div class="control-measures" @click="showDialog(2)">
        <img src="../../../../assets/images/history-icon.png" alt="" />
        <span>历史清单</span>
      </div>
    </div>
    <PrimaryDialog :mainTitle="dialogTitleType === 1 ? '落实清单' : '历史清单'" class="fangan_dialog" :visible="showFanganDialog" width="85%" :showFooter="false" top="8vh" :fullscreen="fullscreen" @changeFullscreen="changeFullscreen" @handleClose="showFanganDialog = false">
      <div class="img-control" :class="fullscreen ? 'fullscreen' : ''">
        <iframe :src="iframeSrc" class="iframe-box" frameborder="0"></iframe>
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import { seawallLeftStatisticApi, zhSeawall } from "@/api/api_seawall";
import PrimaryDialog from "../components/PrimaryDialog.vue";
export default {
  name: "SeawallRightThree",
  components: {
    PrimaryDialog,
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      // this.getSeawallList();
      this.getKouMenData()
    },
  },
  data() {
    return {
      checkDataType: true,
      twoList: [
        {
          name: "旱闸",
          total: 0,
          close: 0,
          fupan: 450,
        },
          {
            name: "道口",
            total: 0,
            close: 0,
            fupan: 462,
          },
        {
          name: "缺口",
          total: 0,
          close: 0,
          fupan: 258,
        },
        {
          name: "船坞口",
          total: 0,
          close: 0,
          fupan: 462,
        },
      ],
      currentTyphoon: {},
      pointArr: [],
      lineArr: [],
      // pdf弹框
      showFanganDialog: false,
      dialogTitleType: 1,
      iframeSrc: "",
      clickType: "",
      fullscreen: false,
    };
  },
  mounted() {
    this.getSeawallList();
    this.getKouMenData()
  },
  methods: {
    showThePopup(type, flag) {
      // 数字为0 不显示悬浮窗
      if (!flag) return;
      this.clickType = type
      if (type.indexOf('总数') === -1) {
        let name = type
        name = this.checkDataType ? `${name}正常` : `${name}复盘`
        console.log(name, 'name');

        this.$parent.$refs.seawallMap._drawBlockOverlays(name)
      } else {
        console.log(type, 'type');
        this.$parent.$refs.seawallMap._drawBlockOverlays(type)
      }
    },
    async getKouMenData() {
      let opt = {
        adcd: this.leftSelectAdcd
      };
      let res = await zhSeawall.seawallDoor(opt);
      if (res.success) {
        this.twoList[0].total = res.data[0].dryGate;
        this.twoList[0].close = res.data[0].dryCloseNum || 0;
        this.twoList[1].total = res.data[0].gapNum;
        this.twoList[1].close = res.data[0].gapCloseNum || 0;
        this.twoList[2].total = res.data[0].crossNum;
        this.twoList[2].close = res.data[0].crossCloseNum || 0;
        this.twoList[3].total = res.data[0].dockNum || 0;
        this.twoList[3].close = res.data[0].dockCloseNum || 0;
      }
    },
    // 查看pdf
    showDialog(index) {
      if (index === 3) {
        this.$parent.openGernaretion('闭环管控', this.checkDataType)
        return;
      }
      this.dialogTitleType = index;
      if (this.dialogTitleType == 1) {
        this.iframeSrc = this.$config.zhImgUrl + "38条海塘风险管控03(有).pdf";
      } else {
        this.iframeSrc = this.$config.zhImgUrl + "66条海塘风险管控清单03(有).et.pdf";
      }
      this.showFanganDialog = true;
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    checkData() {
      this.clickType = ''
      this.checkDataType = !this.checkDataType;
      if (this.checkDataType) {
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
      } else {
        this.$parent.$refs.seawallMap.clearCityLayer()
        this.$parent.showMapPoints(this.pointArr, "cwyj");
        this.$parent.showMapLines(this.lineArr);
      }
    },
    async getSeawallList() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      if (this.leftSelectAdcd === '330000000000') {
        this.$parent.$refs.seawallMap.initView();
      } else {

      }

      let opt = {
        statistic: "mapList",
        itemCategory: "forecast,planId",
        itemValue: "overCnt,F2DB010A1E87451685EAC761D76FEBFD",
        adcd: this.leftSelectAdcd,
      };
      let res = await seawallLeftStatisticApi(opt);
      if (res.success) {
        this.pointArr = [];
        this.lineArr = [];
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
            this.pointArr.push({
              lat: v.LTTD,
              lng: v.LGTD,
              NAME: v.NAME,
              PRCD: v.PRCD,
              rightType: "3",
              forecast_time: v.forecast_time,
              colorType: colorType,
              status: v.status,
              DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
              DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
              forecast: v.forecast,
              countryadnm: v.countryadnm,
              cityadnm: v.cityadnm,
              LOWEST_ELEVATION: v.LOWEST_ELEVATION,
              src: require("@/assets/images/map/gao_risk.png"),
            });
            this.lineArr.push({
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
        // this.$parent.showMapPoints(pointArr, "cwyj");
        // this.$parent.showMapLines(lineArr);
      }
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
  .third_title:before {
    content: none;
  }
  .third_title {
    padding-bottom: 8px;
    span {
      color: #ffffff;
      font-size: 20px;
      font-family: pangmen3;
    }
    .tip-icon {
      width: 19px;
      height: 20px;
      margin-left: 8px;
      cursor: pointer;
    }
  }
  .select_title {
    padding: 16px 0px 10px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-size: 18px;
      font-family: pangmen3;
      img {
        margin-right: 5px;
      }
    }
  }
  .check-btn-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 10px;
    .typhoon-name {
      font-family: "pangmen3";
      font-size: 22px;
      color: #46fdff;
      font-weight: bold;
    }
    .check-btn {
      font-size: 16px;
      color: #ffffff;
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
    height: 35%;
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
      background: url("../../../../assets/images/flow-arrow-b.png") no-repeat;
      background-size: 100% 100%;
      border: none;
      margin: 0 auto;
    }
    .flow-item {
      height: 48px;
      display: flex;
      justify-content: center;
      align-items: center;
      background: rgba(70, 253, 255, 0.1);
      border-radius: 4px;
      border: 1px solid #00cfff;
      > img {
        width: 24px;
        height: 24px;
      }
      > p {
        display: flex;
        justify-content: center;
        align-items: center;
        color: #ffffff;
        font-size: 16px;
        padding-left: 8px;
        span {
          font-size: 28px;
          padding: 0 4px 0 12px;
          color: #46fdff;
          font-family: "pangmen3";
        }
      }
      &.active-flow {
        P {
          color: rgba(25, 59, 196, 1);
        }
      }
    }
    .flow-practicable {
      height: 157px;
      background: rgba(70, 253, 255, 0.1);
      border-radius: 4px;
      border: 1px solid #00cfff;
      .practicable-title {
        font-size: 24px;
        text-align: center;
        padding-top: 16px;
        color: #ffffff;
        font-family: "pangmen3";
      }
      .practicable-list {
        padding: 16px 14px 0 12px;
        display: flex;
        justify-content: space-between;
        .practicable-item {
          width: 106px;
          height: 76px;
          background: url("../../../../assets/images/practicable-item.png")
            no-repeat;
          background-size: 100% 100%;
          P {
            text-align: center;
            color: #ffffff;
            line-height: 24px;
            &:nth-child(1) {
              padding-top: 10px;
              font-size: 18px;
            }
            &:nth-child(2) {
              padding-top: 6px;
              font-size: 12px;
              font-weight: 600;
              span {
                font-size: 28px;
                color: #46fdff;
                padding-right: 5px;
                font-family: "pangmen3";
              }
            }
          }
        }
      }
    }
  }
  .measure-box {
    height: calc(54% - 20px);
    margin-top: 16px;
    border-radius: 4px;
    padding: 0 15px;
    background: rgba(70, 253, 255, 0.1);
    border-radius: 4px;
    border: 1px solid #00cfff;
    .measure-title {
      text-align: center;
      padding: 16px 0;
      font-size: 24px;
      color: #ffffff;
      line-height: 24px;
      font-family: "pangmen3";
    }
    .select_title-list {
      height: 22%;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .select_title-item {
        padding-bottom: 0;
        padding-top: 0;
        &:nth-child(3) {
          padding-bottom: 7px;
        }
        img {
          width: 20px;
        }
        > div {
          &:first-child {
            font-size: 20px;
            font-family: "pangmen3";
            color: #ffffff;
          }
          &:nth-child(2) {
            display: flex;
            align-items: center;
            p {
              font-size: 14px;
              color: #ffffff;
              span {
                color: #46fdff;
                font-size: 28px;
                padding-right: 4px;
                font-family: "pangmen3";
              }
            }
            .select_title-line {
              width: 1px;
              height: 14px;
              background-color: #546f9f;
              margin: 0 15px;
            }
            .last-p {
              min-width: 65px;
            }
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
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        color: #ffffff;
        background: rgba(70, 253, 255, 0.1);
        border-radius: 4px;
        border: 1px solid #00cfff;
        span {
          font-size: 28px;
          padding: 0 4px 0 12px;
          color: #46fdff;
          font-family: "pangmen3";
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
    height: 45%;
    .control-two-list {
      padding-top: 8px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: calc(100% - 50px);
      .control-two-item {
        background: #154aad;
        border: 1px solid #0051b5;
        padding: 2px 0 2px 16px;
        @include flexbox();
        @include flexAI(center);
        @include flexJC(space-around);
        > div {
          display: inline-block;
          color: #ffffff;
          font-size: 14px;
          text-align: center;
          span {
            font-size: 28px;
            color: #46fdff;
            padding: 0 8px 0 13px;
            font-family: "pangmen3";
          }
          &:nth-child(1) {
            flex: 1;
            font-family: "pangmen3";
            line-height: 24px;
            padding-right: 20px;
            border-right: 1px solid #546f9f;
            font-size: 18px;
          }
          &:nth-child(2) {
            flex: 2.3;
            text-align: center;
          }
          &:nth-child(3) {
            flex: 2;
            text-align: center;
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
.control-measures-box {
  width: 100%;
  margin-top: 16px;
  // padding: 2px 16px 0;
  display: flex;
  justify-content: space-between;
  .control-measures {
    width: 145px;
    height: 48px;
    background: url("../../../../assets/images/control-measures-bg.png")
      no-repeat;
    background-size: 100% 90%;
    font-size: 18px;
    font-weight: bold;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    img {
      width: 24px;
      margin-right: 15px;
    }
  }
}
.checked {
  color: #ffa01e !important;
  font-weight: 600;
  letter-spacing: 2px;
}

.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 25px;
  box-sizing: border-box;
  &.fullscreen .iframe-box {
    height: calc(100% -15px);
  }
  .iframe-box {
    width: 100%;
    height: calc(100% - 15px);
  }
}
</style>