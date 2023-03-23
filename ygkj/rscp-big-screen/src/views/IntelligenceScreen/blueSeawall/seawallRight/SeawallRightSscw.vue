<!--
 * @Date: 2022-10-18 16:52:17
 * @Author: 
 * @Description: 风险研判
 * @LastEditors: dtb
 * @LastEditTime: 2022-10-29 15:57:57
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\blueSeawall\seawallRight\SeawallRightSscw.vue
-->

<template>
  <div class="wrap">
    <div class="seawallList_wrap hei1">
      <div class="second_title2">高潮位风险</div>
      <section class="fbcyj_box">
        <p class="third_title">
          <img class="point" :src="$config.imgUrl + 'point.png'" alt="" />
          <span :class="[planTypeIndex == index ? 'active' : '']" v-for="(item, index) in planTypeList" :key="index" @click="checkPlanClick(index)">{{ item }}预报</span>
          <i></i>
          <img class="arr" :src="$config.imgUrl + 'arr.gif'" alt="" />
        </p>

        <div class="query_wrapper" v-if="fanganList.length">
          <el-select popper-class="query_select" v-model="planId" @change="fanganClick" placeholder="请选择" size="small">
            <el-option v-for="item in fanganList" :key="item.id" :label="item.planName" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div v-else class="noFangan">暂无方案</div>
        <div class="select_square">
          <div :class="[
              'square_item',
              item.type,
              index === squareIndex ? 'active' : '',
            ]" v-for="(item, index) in squareList" :key="index" @click="clickSquare(index)">
            <p>{{ item.name }}</p>
            <span>{{ item.value }}<i>条</i></span>
            <h3>{{ item.length }}<i>公里</i></h3>
          </div>
        </div>
      </section>
      <section class="htfb_box">
        <!-- <bar-chartclass="htfbChart" :chartData="yjhtfbData" :textType="true" @barClick="barClickAdcd">
        </bar-chartclass=> -->
        <ul v-if="showChart">
          <li v-for="(item, index) in yjhtfbData.xData" :key="index" :class="{'active':fanganBlockIndex === index}" @click="barClickAdcd(index)">
            <i class="warn_icon"></i> <span>{{yjhtfbData.xData[index]}}</span><span class="num">{{ yjhtfbData.yData[index]}}</span> 条
          </li>
        </ul>
        <div class="table_nodata" v-else>
          <img src="../../../../assets/images/noData-a.png" alt="" />
        </div>

      </section>
      <section class="seawallList_box">
        <div class="table_list" ref="tableContainer">
          <div class="scroll_item_title">
            <div class="scroll_item_name">海塘名称</div>
            <div class="scroll_item_height" v-if="squareIndex === 2">
              塘顶最低高程
            </div>
            <!-- <div class="scroll_item_type" v-if="squareIndex !== 2">
              安全状态
            </div> -->
            <div class="scroll_item_type" v-if="squareIndex !== 2">
              设计潮位
            </div>
            <div class="scroll_item_type scroll_item_type_long">预报最高潮位</div>
            <div class="scroll_item_tm">出现时间</div>
          </div>
          <div class="scroll_item_box" ref="scroll" @click="viewRecords($event)">
            <seamless-scroll className="scroll_item_ervice" :list="tableData" v-if="tableData.length > 5">
              <div class="scroll_item">
                <div v-for="(item, index) in tableData" :key="index" :data-index="index" class="scroll_item_ervice">
                  <div class="scroll_item_name">
                    {{ item.NAME ? item.NAME : "-" }}
                  </div>
                  <div class="scroll_item_height" v-if="squareIndex === 2">
                    {{
                      item.LOWEST_ELEVATION ? item.LOWEST_ELEVATION + "m" : "-"
                    }}
                  </div>
                  <!-- <div class="scroll_item_type" v-if="squareIndex !== 2">
                    {{ item.status ? item.status : "-" }}
                  </div> -->
                  <div class="scroll_item_type" v-if="squareIndex !== 2">
                    {{
                      item.DESIGN_TIDE_LEVEL
                        ? item.DESIGN_TIDE_LEVEL.toFixed(2)  + "m"
                        : "-"
                    }}
                  </div>
                  <div class="scroll_item_type red scroll_item_type_long">
                    {{ item.forecast ? item.forecast.toFixed(2)  + "m" : "-" }}
                  </div>
                  <div class="scroll_item_tm">
                    {{
                      item.forecast_time ? item.forecast_time.slice(5, 16) : "-"
                    }}
                  </div>
                </div>
              </div>
            </seamless-scroll>
            <div class="scroll_item" v-else-if="tableData.length && tableData.length <= 5 ">
              <div v-for="(item, index) in tableData" :key="index" :data-index="index" class="scroll_item_ervice">
                <div class="scroll_item_name">
                  {{ item.NAME ? item.NAME : "-" }}
                </div>
                <div class="scroll_item_height" v-if="squareIndex === 2">
                  {{
                      item.LOWEST_ELEVATION ? item.LOWEST_ELEVATION + "m" : "-"
                    }}
                </div>
                <!-- <div class="scroll_item_type" v-if="squareIndex !== 2">
                  {{ item.status ? item.status : "-" }}
                </div> -->
                <div class="scroll_item_type" v-if="squareIndex !== 2">
                  {{
                      item.DESIGN_TIDE_LEVEL
                        ? item.DESIGN_TIDE_LEVEL.toFixed(2)  + "m"
                        : "-"
                    }}
                </div>
                <div class="scroll_item_type red scroll_item_type_long">
                  {{ item.forecast ? item.forecast.toFixed(2)  + "m" : "-" }}
                </div>
                <div class="scroll_item_tm">
                  {{
                      item.forecast_time ? item.forecast_time.slice(5, 16) : "-"
                    }}
                </div>
              </div>
            </div>
            <div class="table_nodata" v-else>
              <img src="../../../../assets/images/noData-a.png" alt="" />
            </div>
          </div>
          <!-- <el-table
            :height="tableHeight"
            v-if="showTable"
            v-loading="tableLoading"
            :data="tableData"
            stripe
            style="width: 100%"
            @row-click="clickSeawall"
          >
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column
              :width="squareIndex === 1 ? '70px' : ''"
              prop="NAME"
              label="海塘名称"
            >
              <template slot-scope="scope">
                <div :class="scope.row.colorType">
                  {{ scope.row.NAME ? scope.row.NAME : "-" }}
                </div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === 0"
              label="塘顶最低高程"
              align="center"
              width="100px"
            >
              <template slot-scope="scope">
                <div>
                  {{
                    scope.row.LOWEST_ELEVATION
                      ? scope.row.LOWEST_ELEVATION + "m"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === 1"
              label="安全状态"
              align="center"
              width="70px"
            >
              <template slot-scope="scope">
                <div>{{ scope.row.status ? scope.row.status : "-" }}</div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === '' || squareIndex === 1"
              label="设计潮位"
              align="center"
              width="70px"
            >
              <template slot-scope="scope">
                <div>
                  {{
                    scope.row.DESIGN_TIDE_LEVEL
                      ? scope.row.DESIGN_TIDE_LEVEL + "m"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              :width="squareIndex === 1 ? '70px' : ''"
              label="实时潮位"
              align="center"
            >
              <template slot-scope="scope">
                <div>{{ scope.row.tide ? scope.row.tide + "m" : "-" }}</div>
              </template>
            </el-table-column>
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.tm ? scope.row.tm.slice(0, 16) : "-" }}</div>
              </template>
            </el-table-column>
          </el-table> -->
        </div>
      </section>
      <div class="control-measures-box">
        <div class="control-measures" @click="showDialog(1)">
          <img src="../../../../assets/images/control-measures-icon-a.png" alt="" />
          <span>措施清单</span>
        </div>
        <div class="control-measures" @click="showDialog(2)">
          <img src="../../../../assets/images/history-icon.png" alt="" />
          <span>历史清单</span>
        </div>
      </div>
    </div>
    <PrimaryDialog :mainTitle="dialogTitleType === 1 ? '措施清单' : '历史清单'" class="fangan_dialog" :visible="showFanganDialog" width="85%" :showFooter="false" top="10vh" :fullscreen="fullscreen" @changeFullscreen="changeFullscreen" @handleClose="showFanganDialog = false">
      <div class="img-control" :class="fullscreen ? 'fullscreen' : ''">
        <iframe :src="iframeSrc" class="iframe-box" frameborder="0"></iframe>
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import {
  seawallLeftStatisticApi,
  cwzListApi,
  getPlanApi,
} from "@/api/api_seawall";
import BarChart from "../components/BarChart";

import PrimaryDialog from "../components/PrimaryDialog.vue";
import SeamlessScroll from "@/components/SeamlessScroll";

export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PrimaryDialog,
    SeamlessScroll,
  },
  data() {
    return {
      showFanganDialog: false,
      dialogTitleType: 1,
      iframeSrc: "",
      fullscreen: false,
      cwFlag: "1",
      seawallList: [],
      adOptions: [],
      yjhtfbData: {
        title: "预警分布",
        yInterval: 1,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      },
      htfbLoading: true,
      tableData: [],
      tableLoading: true,
      squareList: [],
      squareIndex: 0,
      squareValue: "overCnt",
      loading_square: true,
      seawallFangan: "",
      planTypeIndex: 0,
      fanganBlockIndex: '',
      planTypeList: ["综合", "海洋", "水文", "研究院"],
      listFlag: true,
      tableHeight: 0,
      cwztableHeight: 0,
      showChart: true,
      cwzfbTableLoading: true,
      cwzfbTableData: [],
      spanArr: [],
      yjNum: 0,
      planId: "",
      fanganList: [],
      fanganIndex: 0,
      showTable: true,
      timerId: null,
      timeout: null,
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd || this.$localData("get", "userInfo").adcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.getSeawallList();
      // this.getLengthData();
    },
  },

  async mounted() {
    await this.checkPlanClick(0);
    this.resizeTable();
    let _this = this;
    // window.addEventListener("resize", function () {
    //   _this.resizeTable();
    // });
  },

  methods: {
    fanganClick(index) {
      this.squareIndex = 0;
      this.squareValue = "overCnt";
      this.fanganIndex = index;
      this.getTideAlertData("forecast");
    },
    checkPlanClick(index) {
      // if (this.planTypeIndex === index) {
      //   return false;
      // }
      this.planTypeIndex = index;
      this.getPlanList();
    },
    showDialog(index) {
      this.dialogTitleType = index;
      if (this.dialogTitleType == 1) {
        this.iframeSrc = this.$config.zhImgUrl + "38条海塘风险管控02（无）.pdf";
      } else {
        this.iframeSrc =
          this.$config.zhImgUrl + "66条海塘风险管控清单03(无).et.pdf";
      }
      this.showFanganDialog = true;
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
    },
    async getPlanList() {
      let res = await getPlanApi({
        isMatch: 1,
        fetchAll: true,
        planType: this.planTypeList[this.planTypeIndex],
      });
      if (res.success) {
        this.fanganList = res.rows;
        if (this.fanganList.length > 0) {
          this.planId = this.fanganList[0].id;
          this.getTideAlertData("forecast");
        } else {
          this.planId = "";
        }
        // this.getTideAlertData("forecast");
        // this.getSeawallList();
      }
    },

    clickSquare(index) {
      this.showTable = false;
      this.$nextTick(() => {
        this.showTable = true;
      });
      this.fanganBlockIndex = ''
      this.$store.commit("SET_LEFT_SELECT_ADCD", "");
      this.squareIndex = index;
      this.squareValue = this.squareList[index].type;
      this.getLengthData();
    },


    // 获取 潮位方块 统计信息
    async getTideAlertData(type) {
      this.loading_square = true;
      this.squareList = [];
      let res = await seawallLeftStatisticApi({
        statistic: type,
        adcd: this.$localData("get", "userInfo").adcd,
        itemCategory: "planId",
        itemValue: this.planId,
      });
      if (res.success) {
        this.yjNum = res.data[0]["total"];
        this.squareList.push(
          {
            type: "overCnt",
            name: "超设计潮位",
            value: res.data[0]["overCnt"],
            length: res.data[0]["overLength"]
              ? (res.data[0]["overLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "sickCnt",
            name: "超病险塘能力",
            value: res.data[0]["sickCnt"],
            length: res.data[0]["sickLength"]
              ? (res.data[0]["sickLength"] / 1000).toFixed(1)
              : 0,
          },
          {
            type: "overTop",
            name: "超塘顶高程",
            value: res.data[0]["overTop"],
            length: res.data[0]["overTopLength"]
              ? (res.data[0]["overTopLength"] / 1000).toFixed(1)
              : 0,
          }
        );
        this.getLengthData();
      }
      this.loading_square = false;
    },

    // 预警海塘分布图信息 改为了 长条块的列表
    async getLengthData() {
      this.htfbLoading = true;
      this.yjhtfbData = {
        title: "预警分布",
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      };
      let opt = {};
      if (this.planId != "") {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "forecast,planId",
          itemValue: this.squareValue + "," + this.planId,
        };
      } else {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "forecast",
          itemValue: this.squareValue,
        };
      }
      let res = await seawallLeftStatisticApi(opt);
      if (res.success) {
        this.showChart = true;
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              this.yjhtfbData.xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
            } else {
              this.yjhtfbData.xData.push(res.data[i].adnm);
            }
            this.yjhtfbData.yData.push(res.data[i].num);
            this.yjhtfbData.adcdList.push(res.data[i].adcd);
          }
          // this.$store.commit("SET_LEFT_SELECT_ADCD", res.data[0].adcd);

        } else {
          this.showChart = false;
        }
        this.getSeawallList();
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

      this.htfbLoading = false;
    },
    // 落点
    async getSeawallList() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.tableData = [];
      this.tableLoading = true;
      let opt = (opt = {
        statistic: "mapList",
        itemCategory: "forecast,planId",
        itemValue: this.squareValue + "," + this.planId,
        adcd: this.leftSelectAdcd
      });
      let res = await seawallLeftStatisticApi(opt);
      this.tableLoading = false;
      if (res.success) {
        let pointArr = [];
        let lineArr = [];
        if (res.data.length) {
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
              cityadnm: v.cityadnm,
              countryadnm: v.countryadnm,
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

        } else {
          console.log('------0000000------');
          this.tableData = []
        }
        this.$parent.showMapPoints(pointArr, "cwyj");
        this.$parent.showMapLines(lineArr);
      }
    },

    viewRecords(e) {
      let parentNode = e.target.parentNode;
      let index = parentNode.getAttribute("data-index");
      this.clickSeawall(this.tableData[index])
    },
    clickSeawall(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "3";
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },


    // 海塘分布柱状图点击事件
    barClickAdcd(index) {
      this.fanganBlockIndex = index
      if (this.yjhtfbData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.yjhtfbData.adcdList[index]
        );
      }
    },

    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height;
      });
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  height: 100%;
  .seawallList_wrap {
    height: 100%;
    .second_title2 {
      padding: 0 0 0 37px;
      margin-bottom: 10px;
      transition: all 0.3s linear;
      font-family: "hanyi";
      width: 100%;
      height: 40px;
      font-size: 30px;
      @include flexbox();
      @include flexJC(flex-start);
      @include flexAI(center);
      background: url($imageUrl + "second_title.png") no-repeat bottom;
      background-size: 100% auto;
      cursor: pointer;
      box-sizing: border-box;
      z-index: 2;
      color: #ffffff;
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
  }
  .fbcyj_box {
    height: 22%;
    .query_wrapper {
      // 下拉样式
      width: 91%;
      margin: 0 auto;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      .el-input {
        width: 100%;
        margin: 0 1%;
      }
      .el-select {
        width: 96%;
        border-color: #448ef7;
        background: #03338d;
        margin: 10px;
        & > .el-input {
          width: 100%;
        }
      }
    }
    .third_title {
      &::before {
        content: none;
      }
      span {
        padding-right: 16px;
        font-size: 20px;
        color: rgba(255, 255, 255, 0.5);
        transition: all 0.2s linear 0s;
        font-family: pangmen3;
        &.active {
          font-size: 24px;
          color: #ffffff;
        }
      }
    }
    .select_square {
      // height: calc(100% - 80px);
      height: 60%;
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      align-items: center;
      padding: 8px 16px;
      box-sizing: border-box;
      .square_item {
        box-sizing: border-box;
        width: 23.5%;
        height: 116px;
        text-align: center;
        cursor: pointer;
        border-radius: 2px;
        background: none !important;
        border: none !important;
        &.active {
          background: url("../../../../assets/images/square_item_active.png")
            no-repeat !important;
          background-size: 100% 100% !important;
        }
        & > p {
          line-height: 24px;
          font-size: 18px;
          color: #ffffff;
          padding: 12px 0 6px 0;
        }
        i {
          font-size: 12px;
          padding-left: 4px;
          color: #ffffff;
          font-family: "normal";
        }
        & > span,
        & > h3 {
          font-size: 28px;
          padding-left: 4px;
          font-family: "pangmen3";
          color: #46fdff;
        }
        & > h3 {
          padding-top: 8px;
        }
        &.overCnt,
        &.nearCnt,
        &.overTop,
        &.closeCnt,
        &.overDesign,
        &.sickCnt {
          width: 32%;
          background: #f8f8f8;
          border: 1px solid #d6d6d6;
        }
        &.blue {
          background: #eef5fe;
          border: 1px solid #2478f0;
        }
        &.yellow {
          background: #fffbf0;
          border: 1px solid #f7c739;
        }
        &.orange {
          background: #fef5f3;
          border: 1px solid #eb7e65;
        }
        &.red {
          background: #fdeeee;
          border: 1px solid #e02020;
        }
      }
    }
  }
  .htfb_box {
    height: 26%;
    position: relative;
    .htfbChart {
      width: 100%;
      height: 100%;
    }
    ul {
      color: #fff;
      font-size: 25px;
      width: 100%;
      height: 100%;
      margin: 5px 0px;
      @include flexbox();
      @include flexAI(center);
      @include flexJC(flex-start);
      @include flexflow(column wrap);
      li {
        @include flexbox();
        @include flexAI(center);
        @include flexJC(center);
        width: 93%;
        height: 50px;
        margin: 10px auto;
        background: url("../../../../assets/images/tiderisk_block.png")
          no-repeat center;
        background-size: 100% 100%;
        cursor: pointer;
        &.active {
          background: url("../../../../assets/images/tiderisk_block_active.png")
            no-repeat center;
          background-size: 100% 100%;
        }
        i.warn_icon {
          width: 32px;
          height: 32px;
          background: url("../../../../assets/images/tiderisk_warn.png")
            no-repeat;
          background-size: 100% 100%;
          margin-right: 16px;
        }
        span {
          font-size: 25px;
          font-weight: 400;
          color: #ffffff;
          padding-right: 10px;
          &.num {
            font-size: 36px;
            font-family: "pangmen3";
            color: #ffcd19;
            padding-right: 6px;
          }
        }
      }
    }

    .nodata {
      position: absolute;
      left: 36%;
      top: 26%;
      width: 170px;
    }
  }
  .control-measures-box {
    margin-top: 4.5%;
    padding: 2px 16px 0;
    display: flex;
    justify-content: space-between;
    .control-measures {
      width: 225px;
      height: 48px;
      background: url("../../../../assets/images/control-measures-bg.png")
        no-repeat;
      background-size: 100% 100%;
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
  .seawallList_box {
    height: 37%;
    .table_list {
      // height: calc(100% - 42px);
      height: 100%;
      box-sizing: border-box;
      overflow: hidden;
      .el-table td div {
        & > .bold {
          font-weight: bold;
        }
        & > .blue {
          color: #2478f0;
        }
        & > .yellow {
          color: #f7c739;
        }
        & > .orange {
          color: #eb7e65;
        }
        & > .red {
          color: #e02020;
        }
      }
      .scroll_item_title {
        height: 48px;
        background: rgba(0, 186, 255, 0.2);
        padding: 0 16px;
        align-items: center;
        display: flex;
        line-height: 48px;
        > div {
          color: #46fdff;
          font-weight: 600;
          font-size: 18px;
          font-family: PingFangSC-Semibold, PingFang SC;
          text-align: center;
          &.scroll_item_name {
            width: 145px;
            overflow: hidden; //超出的文本隐藏
            text-overflow: ellipsis; //溢出用省略号显示
            white-space: nowrap; //溢出不换行
          }
          &.scroll_item_height {
            width: 116px;
          }
          &.scroll_item_type {
            width: 90px;
            &.red {
              color: #e02020;
            }
            &.scroll_item_type_long {
              width: 120px;
            }
          }
          &.scroll_item_tm {
            width: 105px;
          }
          &.real-time {
            width: 90px;
          }
        }
      }
      .scroll_item_box {
        height: calc(100% - 40px);
        width: 100%;
        overflow: hidden;
        position: relative;
        .scroll_item {
          width: 100%;
          // position: absolute;
          // top: 0;
          .scroll_item_ervice {
            height: 48px;
            padding: 0 16px;
            align-items: center;
            display: flex;
            // border-bottom: 1px solid rgba(51, 144, 225, 0.4);
            // &:nth-child(odd) {
            //   background: rgba(0, 186, 255, 0.2);
            // }
            > div {
              color: #ffffff;
              font-size: 18px;
              text-align: center;
              cursor: pointer;
              &.scroll_item_name {
                width: 145px;
                overflow: hidden; //超出的文本隐藏
                text-overflow: ellipsis; //溢出用省略号显示
                white-space: nowrap; //溢出不换行
              }
              &.scroll_item_height {
                width: 116px;
              }
              &.scroll_item_type {
                width: 90px;
                // &.red {
                //   color: #e02020;
                // }
                &.scroll_item_type_long {
                  width: 120px;
                }
              }
              &.scroll_item_tm {
                width: 105px;
              }
              &.real-time {
                width: 90px;
              }
            }
          }
        }
      }
    }
  }
}

.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 25px;
  box-sizing: border-box;
  .iframe-box {
    width: 100%;
    height: calc(100% - 15px);
  }
  &.fullscreen .iframe-box {
    height: calc(100% - 15px);
  }
}
.table_nodata {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>

<style lang="scss">
.seawallList_wrap .el-loading-mask {
  background-color: transparent;
  color: #ffffff;
  font-size: 16px;
}
</style>