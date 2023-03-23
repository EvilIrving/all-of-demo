<!--
 * @Author: hanyu
 * @LastEditTime: 2022-10-18 16:47:03
 * @Description: 地图右侧-潮位预警
 * @FilePath: \rscp-big-screen\src\views\IntelligenceScreen\seawall\seawallRight\SeawallRightSscw.vue
-->
<template>
  <div class="wrap">
    <div class="seawallList_wrap hei1">
      <section class="fbcyj_box">
        <!-- <div class="chart_title">
          <img src="../../../../assets/images/chart_item_icon.png" alt="">
          风险清单
        </div> -->
        <div class="select_square" v-loading="loading_square">
          <div
            :class="[
              'square_item',
              item.type,
              index === squareIndex ? 'active' : '',
            ]"
            v-for="(item, index) in squareList"
            :key="index"
            @click="clickSquare(index)"
          >
            <p>{{ item.name }}</p>
            <span>{{ item.value }}<i>条</i></span>
            <h3>{{ item.length }}<i>公里</i></h3>
          </div>
        </div>
      </section>
      <section class="htfb_box">
        <bar-chart
          v-if="showChart"
          v-loading="htfbLoading"
          class="htfbChart"
          :chartData="yjhtfbData"
          :textType="true"
          @barClick="barClickAdcd"
        ></bar-chart>
      </section>
      <section class="seawallList_box">
        <!-- <div class="chart_title">
          <img src="../../../../assets/images/chart_item_icon.png" alt="">
          风险清单
        </div> -->
        <div class="table_list" ref="tableContainer">
          <el-table
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
            <!-- <el-table-column :width=" squareIndex === 0 || squareIndex === 1 || squareIndex === 2 || squareIndex === 3 ? '60px' : ''" label="时间" align="center"> -->
            <el-table-column label="时间" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.tm ? scope.row.tm.slice(0, 16) : "-" }}</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>
      <div class="control-measures-box">
        <div class="control-measures" @click="showFanganDialog = true">
          <span>管控措施</span>
          <img
            src="../../../../assets/images/control-measures-icon.png"
            alt=""
          />
        </div>
      </div>
    </div>
    <PrimaryDialog
      mainTitle="管控措施"
      custom-class="fangan_dialog"
      :visible="showFanganDialog"
      width="1200px"
      :showFooter="false"
      top="3.5vh"
      :fullscreen="fullscreen"
      @changeFullscreen="changeFullscreen"
      @handleClose="showFanganDialog = false"
    >
      <div class="img-control">
        <img src="../../../../assets/images/control-measures.png" alt="" />
      </div>
    </PrimaryDialog>
  </div>
</template>

<script>
import {
  seawallLeftStatisticApi,
  adListApi,
  cwzListApi,
  getPlanApi,
} from "@/api/api_seawall";
import BarChart from "@/components/BarChart";
import { drawPoint } from "@/utils/mapUtil";
import PrimaryDialog from "../../../skzgScreen/components/PrimaryDialog.vue";
let planId = "F2DB010A1E87451685EAC761D76FEBFD";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PrimaryDialog,
  },
  data() {
    return {
      showFanganDialog: false,
      fullscreen: false,
      cwFlag: "1",
      seawallList: [],
      seawallAd: "",
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
      fanganOptions: [
        {
          label: "暂无方案",
          value: "",
        },
      ],
      listFlag: true,
      tableHeight: 0,
      cwztableHeight: 0,
      showChart: true,
      cwzfbTableLoading: true,
      cwzfbTableData: [],
      spanArr: [],
      yjNum: 0,
      fanganList: [],
      fanganIndex: 0,
      showTable: true,
    };
  },

  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.getSeawallList();
    },
    cwFlag(val) {
      this.squareIndex = 0;
      this.squareValue = "overTop";
      if (val == "1") {
        this.resizeTable();
        this.getSscwData();
      } else {
        this.resizeTable2();
        this.getCwzList();
      }
    },
  },

  mounted() {
    // this.resizeTable2()
    // this.getCwzList()
    this.resizeTable();
    this.getSscwData();
    let _this = this;
    window.addEventListener("resize", function () {
      _this.resizeTable();
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize");
  },
  methods: {
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
        } else {
          this.planId = "";
        }
        this.getYbcwData();
      }
    },
    getSscwData() {
      this.getTideAlertData();
      this.getSeawallList();
      this.getLengthData();
    },
    // 预警海塘分布
    // async getLengthData(){
    //   this.htfbLoading = true
    //   this.yjhtfbData = {
    //     title: "预警分布",
    //     yInterval: 1,
    //     id: 'yjhtfbChart',
    //     xData: [],
    //     yData: [],
    //     adcdList: [],
    //     unit: '条'
    //   }
    //   this.showChart = false
    //   this.$nextTick(()=>{
    //     this.showChart = true
    //   })
    //   this.htfbLoading = false
    // },
    // 预警海塘分布
    async getLengthData() {
      this.htfbLoading = true;
      this.yjhtfbData = {
        title: "预警分布",
        // yInterval: 2,
        id: "yjhtfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
        noTitle: true,
      };
      let res = await seawallLeftStatisticApi({
        statistic: "adcd",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "forecast,planId",
        itemValue: this.squareValue + "," + planId,
      });
      if (res.success) {
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
          // this.showChart = false
          // this.$nextTick(()=>{
          //   this.showChart = true
          // })
        } else {
          this.showChart = false;
          this.$nextTick(() => {
            this.showChart = true;
          });
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

      this.htfbLoading = false;
    },
    // adChange(val){
    //   this.getSeawallList()
    // },
    clickSeawall(row) {
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "8";
      this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },
    clickCwzfb(row) {
      // this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.setView(row.stLgtd, row.stLttd);
      row.lat = row.stLttd;
      row.lng = row.stLgtd;
      row.LGTD = row.stLgtd;
      row.LTTD = row.stLttd;
      row.rightType = "7";
      this.$parent.showOneSelectPoint([row], true);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },
    clickSquare(index) {
      this.showTable = false;
      this.$nextTick(() => {
        this.showTable = true;
      });
      if (this.listFlag) {
        // if(this.squareIndex === index){
        //   this.squareIndex = ''
        // }else{
        this.squareIndex = index;
        // }
        this.squareValue = this.squareList[index].type;
        this.getSeawallList();
        this.getLengthData();
      }
    },

    // 海塘分布柱状图点击事件
    barClickAdcd(index) {
      if (this.yjhtfbData.xData[index] == "省级") {
        this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
      } else {
        this.$store.commit(
          "SET_LEFT_SELECT_ADCD",
          this.yjhtfbData.adcdList[index]
        );
      }
    },
    // async getAdOptions(){
    //   this.adOptions = []
    //   let res = await adListApi()
    //   if(res.success){
    //     if(res.rows.length){
    //       for (let i = 0; i < res.rows.length; i++) {
    //         this.adOptions.push({
    //           label: res.rows[i].coastalName,
    //           value: res.rows[i].id
    //         })
    //       }
    //     }
    //     this.adOptions.unshift({
    //       label: '全部岸段',
    //       value: ''
    //     })
    //   }
    // },
    async getSeawallList() {
      if (this.listFlag) {
        this.$parent.$refs.seawallMap.removeAllLayer();
        this.$parent.$refs.seawallMap.closePopup();
        this.$parent.$refs.seawallMap.initView();
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
        this.listFlag = false;
        this.tableData = [];
        this.tableLoading = true;
        let opt = {};
        if (this.seawallAd) {
          opt = {
            statistic: "mapList",
            itemCategory: "costalId,tideAlert",
            itemValue: this.seawallAd + "," + this.squareValue,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
          };
        } else {
          // if(this.squareIndex !== ''){
          opt = {
            statistic: "mapList",
            itemCategory: "forecast,planId",
            itemValue: this.squareValue + "," + planId,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
          };
          // }else{
          //   opt = {
          //     statistic: 'mapList' ,
          //     adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
          //   }
          // }
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
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
      }
    },

    async getTideAlertData() {
      this.loading_square = true;
      this.squareList = [];
      let res = await seawallLeftStatisticApi({
        statistic: "forecast",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "planId",
        itemValue: planId,
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
      }
      this.loading_square = false;
    },
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        // this.tableHeight = height - 80;
        this.tableHeight = height;
        let _this = this;
      });
    },
    resizeTable2() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer2.clientHeight;
        console.log(height, "height");
        this.cwztableHeight = height - 60;
        let _this = this;
        window.onresize = function () {
          const height = this.$refs.tableContainer2.clientHeight;
          this.cwztableHeight = height - 60;
        };
      });
    },
    async getCwzList() {
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", false);
      this.cwzfbTableData = [];
      this.cwzfbTableLoading = true;
      let res = await cwzListApi();
      let pointArr = [];
      if (res.success) {
        this.cwzfbTableData = res.rows;
        res.rows.map((v, i) => {
          let colorType = "";
          if (v.tide > v.tideAlertBlue) {
            if (v.tide < v.tideAlertYellow || v.tide == v.tideAlertYellow) {
              colorType = "blue";
            } else if (
              v.tide < v.tideAlertOrange ||
              v.tide == v.tideAlertOrange
            ) {
              colorType = "yellow";
            } else if (v.tide < v.tideAlertRed || v.tide == v.tideAlertRed) {
              colorType = "orange";
            } else {
              colorType = "red";
            }
          } else {
            colorType = "default";
          }
          this.cwzfbTableData[i].colorType = colorType;
          pointArr.push({
            lat: v.stLttd,
            lng: v.stLgtd,
            coastalName: v.coastalName,
            tm: v.tm,
            tide: v.tide,
            colorType: colorType,
            rightType: "7",
            stnm: v.stnm,
            src: require("@/assets/images/map/seawall.png"),
          });
        });
        this.$parent.showMapPoints(pointArr);
        // this.$parent.showOverlays(pointArr)
        this.spanArr = [];
        let contactDot = 0;
        this.cwzfbTableData.forEach((item, index) => {
          item.index = index;
          if (index === 0) {
            this.spanArr.push(1);
          } else {
            if (item.stnm === this.cwzfbTableData[index - 1].stnm) {
              this.spanArr[contactDot] += 1;
              this.spanArr.push(0);
            } else {
              this.spanArr.push(1);
              contactDot = index;
            }
          }
        });
      }
      this.cwzfbTableLoading = false;
      this.$store.commit("SET_SEAWALL_RIGTH_SELECT_FLAG", true);
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        const _row = this.spanArr[rowIndex];
        const _col = _row > 0 ? 1 : 0;
        return {
          rowspan: _row,
          colspan: _col,
        };
      }
    },
    fanganManage() {
      let url =
        "https://sk.zjwater.com/mgt/coastalTideAlert/forecastList" +
        // let url = 'http://192.168.2.97:8082/mgt/coastalTideAlert/forecastList' +
        "?from=bigscreen&token=" +
        this.$localData("get", "token");
      this.$parent.openFanganManage(url);
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  height: 100%;
  .seawallList_wrap {
    height: 100%;
  }
  .fbcyj_box {
    height: 15%;
    // background-color: red;
    .select_square {
      // height: calc(100% - 80px);
      height: 100%;
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
        &.active {
          background: #193cc5 !important;
          border: 1px solid #193cc5 !important;
          & > p {
            color: #fff !important;
          }
          & > span {
            color: #fff !important;
          }
          & > h3 {
            color: #fff !important;
          }
        }
        & > p {
          line-height: 24px;
          font-size: 14px;
          color: #000;
          padding: 12px 0 6px 0;
        }
        i {
          font-size: 14px;
          padding-left: 4px;
        }

        & > span,
        & > h3 {
          line-height: 24px;
          font-size: 24px;
          color: #000;
          font-weight: 500;
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
    height: 32%;
    .htfbChart {
      margin-top: 3px;
      width: 100%;
      // height: calc(100% - 55px);
      height: 100%;
    }
  }
  .control-measures-box {
    margin-top: 4.5%;
    padding: 2px 16px 0;
    .control-measures {
      width: 100%;
      height: 48px;
      background: #f8f8f8;
      border-radius: 4px;
      border: 1px solid #d6d6d6;
      font-size: 16px;
      font-weight: bold;
      color: #2e3d53;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      img {
        width: 24px;
        margin-left: 15px;
      }
    }
  }
  .seawallList_box {
    height: 43%;
    .table_list {
      // height: calc(100% - 42px);
      height: 100%;
      box-sizing: border-box;
      overflow: auto;
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
    }
  }
}

.img-control {
  text-align: center;
  height: 100%;
  overflow: auto;
  padding-bottom: 25px;
  box-sizing: border-box;
  img {
    width: 65%;
    vertical-align: bottom;
  }
}
</style>