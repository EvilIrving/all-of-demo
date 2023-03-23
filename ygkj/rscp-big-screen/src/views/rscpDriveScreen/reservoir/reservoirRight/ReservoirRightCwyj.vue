<!--
 * @Author: hanyu
 * @LastEditTime: 2021-10-12 15:22:03
 * @Description: 地图右侧-潮位预警
 * @FilePath: /rscp-big-screen/src/views/bigScreen/reservoir/reservoirRight/ReservoirRightCwyj.vue
-->
<template>
  <div class="wrap">
    <div class="btn_title">
      <div class="title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        <p>降雨预报</p>
      </div>
      <!-- <el-button plain size="small" @click="sendMsg()">短信预警</el-button> -->
    </div>

    <section class="fangan_box">
      <div class="chart_line"></div>
      <div class="fangan_title">
        <p>方案选择</p>
        <!-- <el-button plain size="small" @click="fanganManage()">方案管理</el-button> -->
      </div>
      <div id="fangan_select">
        <span class="select_title">气象预报：</span>
        <el-button
          class="planItem"
          v-for="(item, index) in fanganList0"
          :key="index"
          :type="index === fanganIndex0 ? 'primary' : ''"
          @click="fanganClick0(index)"
          >{{ item.name }}</el-button
        >
      </div>
      <div id="fangan_select">
        <span class="select_title">未来估报：</span>
        <el-button
          class="planItem"
          v-for="(item, index) in fanganList"
          :key="index"
          :type="index === (fanganIndex0 - 3) ? 'primary' : ''"
          @click="fanganClick(index)"
          >{{ item.name }}</el-button
        >
      </div>
    </section>
    <div class="chart_line"></div>
    <div class="seawallList_wrap hei2">
      <section class="fbcyj_box">
        <div class="select_title">
          <div>
            <img src="../../../../assets/images/chart_item_icon.png" alt="" />
            预估水位
          </div>
        </div>
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
            <span>{{ item.value }}<i>座</i></span>
          </div>
        </div>
      </section>
      <section class="htfb_box">
        <bar-chart
          v-if="showYjhtfbChart"
          v-loading="htfbLoading"
          class="htfbChart"
          :chartData="yjhtfbData"
          @barClick="barClickAdcd"
        ></bar-chart>
      </section>
      <div class="chart_line"></div>
      <section class="seawallList_box">
        <div class="chart_title" style="height:25px">
          <img src="../../../../assets/images/chart_item_icon.png" alt="" />
          水库列表
          <el-button
            v-if="canExport"
            style="float:right"
            plain
            size="small"
            @click="exportList()"
            >导出</el-button
          >
        </div>
        <div class="table_list" ref="tableContainer">
          <el-table
            :height="tableHeight"
            v-if="showTable"
            v-loading="tableLoading"
            :data="tableData"
            stripe
            style="width: 100%"
            @row-click="clickReservoir"
          >
            <template slot="empty">
              <div class="table_nodata">
                <img src="../../../../assets/images/noData.png" alt="" />
              </div>
            </template>
            <el-table-column width="90px" label="水库名称">
              <template slot-scope="scope">
                <div>{{ scope.row.NAME ? scope.row.NAME : "-" }}</div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === 2"
              width="58px"
              label="汛限水位"
              align="center"
            >
              <template slot-scope="scope">
                <div>
                  {{
                    scope.row.limitWaterLevel
                      ? scope.row.limitWaterLevel.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === 1"
              width="58px"
              label="正常水位"
              align="center"
            >
              <template slot-scope="scope">
                <div>
                  {{ scope.row.NWL ? scope.row.NWL.toFixed(2) + "m" : "-" }}
                </div>
              </template>
            </el-table-column>

            <el-table-column
              v-if="squareIndex === 0"
              width="58px"
              label="设计水位"
              align="center"
            >
              <template slot-scope="scope">
                <div>
                  {{ scope.row.DSFLZ ? scope.row.DSFLZ.toFixed(2) + "m" : "-" }}
                </div>
              </template>
            </el-table-column>

            <!-- <el-table-column v-if="squareIndex === 3" width="58px" label="校核水位" align="center">
              <template slot-scope="scope">
                <div>{{scope.row.CKFLV ? scope.row.CKFLV.toFixed(2) + 'm' : '-'}}</div>
              </template>
            </el-table-column> -->

            <el-table-column width="58px" label="实时水位" align="center">
              <template slot-scope="scope">
                <div>
                  {{
                    scope.row.waterLevel
                      ? scope.row.waterLevel.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>

            <el-table-column width="58px" label="预报水位" align="center">
              <template slot-scope="scope">
                <div v-if="fanganIndex0 == 0">
                  {{
                    scope.row.waterLevel060
                      ? scope.row.waterLevel060.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex0 == 1">
                  {{
                    scope.row.waterLevel180
                      ? scope.row.waterLevel180.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex0 == 2">
                  {{
                    scope.row.waterLevel360
                      ? scope.row.waterLevel360.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex0 == 3">
                  {{
                    scope.row.waterLevel50
                      ? scope.row.waterLevel50.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex0 == 4">
                  {{
                    scope.row.waterLevel100
                      ? scope.row.waterLevel100.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex0 == 5">
                  {{
                    scope.row.waterLevel200
                      ? scope.row.waterLevel200.toFixed(2) + "m"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>
            <!-- <el-table-column width="58px" label="预报库容" align="center">
              <template slot-scope="scope">
                <div v-if="fanganIndex == 0">
                  {{
                    scope.row.currenty50
                      ? scope.row.currenty50.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex == 1">
                  {{
                    scope.row.currenty100
                      ? scope.row.currenty100.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex == 2">
                  {{
                    scope.row.currenty150
                      ? scope.row.currenty150.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex == 3">
                  {{
                    scope.row.currenty200
                      ? scope.row.currenty200.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex == 4">
                  {{
                    scope.row.currenty250
                      ? scope.row.currenty250.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
                <div v-if="fanganIndex == 5">
                  {{
                    scope.row.currenty300
                      ? scope.row.currenty300.toFixed(0) + "万m³"
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column> -->

            <el-table-column width="90px" label="时间" align="center">
              <template slot-scope="scope">
                <div>
                  {{
                    scope.row.waterleveltm
                      ? scope.row.waterleveltm.slice(0, 13)
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import BarChart from "@/components/BarChart";
import { drawPoint } from "@/utils/mapUtil";

export default {
  name: "",
  props: {
    rightSelectList2: {
      type: Array,
      required: true,
    },
  },
  components: {
    BarChart,
  },
  data() {
    return {
      yjhtfbData: {
        title: "预警分布",
        // yInterval: 2,
        id: "bxskfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "条",
      },
      htfbLoading: false,
      showYjhtfbChart: false,

      tableData: [],
      tableLoading: false,
      squareList: [],
      squareIndex: 0,
      squareValue: "DSFLZ",
      loading_square: true,
      listFlag: true,
      tableHeight: 0,
      cwzfbTableLoading: true,
      cwzfbTableData: [],

      fanganIndex0: 0,
      fanganList0: [
        {
          name: "1h预报",
          value: "waterlevel060",
        },
        {
          name: "3h预报",
          value: "waterlevel180",
        },
        {
          name: "6h预报",
          value: "waterlevel360",
        },
      ],
      fanganIndex: "",
      fanganList: [
        {
          name: "50mm",
          value: "waterlevel50",
        },
        {
          name: "100mm",
          value: "waterlevel100",
        },
        // {
        //   name: '150mm',
        //   value: 'waterlevel150'
        // },
        {
          name: "200mm",
          value: "waterlevel200",
        },
        // {
        //   name: '250mm',
        //   value: 'waterlevel250'
        // },
        // {
        //   name: '300mm',
        //   value: 'waterlevel300'
        // }
      ],
      planId: "waterlevel060",
      showTable: true,
      canExport: false,

      opt: {
        statistic: "mapList",
        basin: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "scale,forecast,DSFLZ",
        itemValue: "all,waterlevel060,1",
        superviseLevel: "",
      },
      superviseLevel: "",
      scale: "all",
      adcd: this.$localData("get", "userInfo").adcd,
    };
  },

  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      this.adcd = val;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      this.getYbData();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.scale = val;
      this.getYbData();
    },
    leftSelectBasin(val) {
      this.getYbData();
    }
  },

  mounted() {
    this.resizeTable();
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
      this.superviseLevel = 1;
    }
    if (this.leftSelectAdcd) {
      this.opt.adcd = this.leftSelectAdcd;
      this.adcd = this.leftSelectAdcd;
    }
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
      this.scale = this.leftSelectProjectScale;
    }
    this.getYbData();
  },

  methods: {
    setOptions(type, val) {
      if (val) {
        // val不为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) == -1) {
            // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + "," + type;
            this.opt.itemValue = this.opt.itemValue + "," + val;
          } else {
            // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(",");
            let index = itemArr.indexValue(type);
            let valueArr = this.opt.itemValue.toString().split(",");
            valueArr[index] = val;
            this.opt.itemValue = valueArr.join(",");
          }
        } else {
          // 没有参数
          this.opt.itemCategory = type;
          this.opt.itemValue = val;
        }
      } else {
        // 为空
        if (this.opt.itemCategory) {
          if (this.opt.itemCategory.indexOf(type) != -1) {
            // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(",");
            let valueArr = this.opt.itemValue.toString().split(",");
            let index = itemArr.indexValue(type);
            itemArr.splice(index, 1);
            valueArr.splice(index, 1);
            this.opt.itemCategory = itemArr.join(",");
            this.opt.itemValue = valueArr.join(",");
          }
        }
      }
    },

    getYbData() {
      this.getTideAlertData();
      this.getLengthData();
      this.getReservoirList();
    },

    fanganClick(index) {
      if (this.listFlag) {
        // this.squareIndex = ''
        // if(this.squareIndex !== ''){
        this.squareValue = this.squareList[this.squareIndex].type;
        // }else{
        //   this.squareValue = ''
        // }
        // this.fanganIndex0 = "";
        this.fanganIndex0 = index + 3;
        this.planId = this.fanganList[index].value;
        this.setOptions("forecast", this.planId);
        this.getYbData();
      }
    },

    fanganClick0(index) {
      if (this.listFlag) {
        // if(this.squareIndex !== ''){
        this.squareValue = this.squareList[this.squareIndex].type;
        // }else{
        //   this.squareValue = ''
        // }
        this.fanganIndex = "";
        this.fanganIndex0 = index;
        this.planId = this.fanganList0[index].value;
        this.setOptions("forecast", this.planId);
        this.getYbData();
      }
    },

    clickSquare(index) {
      if (this.listFlag) {
        this.showTable = false;
        this.$nextTick(() => {
          this.showTable = true;
        });
        // if(this.squareIndex === index){
        //   this.squareIndex = ''
        //   this.squareValue = ''
        //   this.opt.itemCategory = 'scale,forecast'
        //   this.opt.itemValue = this.scale + ',' + this.planId
        // }else{
        this.squareIndex = index;
        this.squareValue = this.squareList[index].type;
        this.opt.itemCategory = "scale,forecast," + this.squareValue;
        this.opt.itemValue = this.scale + "," + this.planId + ",1";

        // }
        this.getReservoirList();
        this.getLengthData();
      }
    },

    // 水库分布柱状图点击事件
    // barClickAdcd(index){
    //   if(this.yjhtfbData.xData[index] == '省级'){
    //     this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
    //   }else{
    //     this.$store.commit('SET_LEFT_SELECT_ADCD', this.yjhtfbData.adcdList[index])
    //   }
    // },

    barClickAdcd(index) {
      if (this.yjhtfbData.xData[index] == "省级") {
        this.opt.superviseLevel = 1;
        // this.$store.commit('SET_LEFT_SELECT_ADCD', '33')
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        this.opt.adcd = this.yjhtfbData.adcdList[index];
        this.$store.commit(
          "SET_LEFT_SELECT_ADNM",
          this.yjhtfbData.adnmList[index]
        );
        // this.$store.commit('SET_LEFT_SELECT_ADCD', this.skfbData.adcdList[index])
      }
      this.getReservoirList();
    },

    async getTideAlertData() {
      this.loading_square = true;
      this.squareList = [];
      let res = await reservoirLeftStatisticApi({
        statistic: "forecast",
        basin: this.leftSelectBasin,
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
        itemCategory: "scale,forecast",
        itemValue: this.scale + "," + this.planId,
      });
      if (res.success) {
        this.squareList.push(
          {
            type: "DSFLZ",
            name: "超设计",
            value: res.data[0]["DSFLZ"],
          },
          {
            type: "NWL",
            name: "超正常",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超汛限",
            value: res.data[0]["limitWaterLevel"],
          }
        );
        // this.squareValue = this.squareList[0].type
      }
      this.loading_square = false;
    },

    // 预警水库分布
    async getLengthData() {
      this.htfbLoading = true;
      this.showYjhtfbChart = false;
      this.$nextTick(() => {
        this.showYjhtfbChart = true;
      });
      let opt = {};
      if (this.squareValue) {
        opt = {
          statistic: "adcd",
          basin: this.leftSelectBasin,
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "scale,forecast," + this.squareValue,
          itemValue: this.scale + "," + this.planId + ",1",
          superviseLevel: this.superviseLevel,
        };
      } else {
        opt = {
          statistic: "adcd",
          adcd: this.leftSelectAdcd
            ? this.leftSelectAdcd
            : this.$localData("get", "userInfo").adcd,
          itemCategory: "scale,forecast",
          itemValue: this.scale + "," + this.planId,
          superviseLevel: this.superviseLevel,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        let xData = [];
        let yData = [];
        let adcdList = [];
        let adnmList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            if (res.data[i].adnm.length > 2) {
              xData.push(res.data[i].adnm.replace(/市/, ""));
            } else {
              xData.push(res.data[i].adnm);
            }
            yData.push(res.data[i].num);
            adcdList.push(res.data[i].adcd);
            adnmList.push(res.data[i].adnm);
          }
        }
        this.yjhtfbData.xData = xData;
        this.yjhtfbData.yData = yData;
        this.yjhtfbData.adcdList = adcdList;
        this.yjhtfbData.adnmList = adnmList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }

      this.htfbLoading = false;
    },

    async getReservoirList() {
      if (this.listFlag) {
        this.opt.basin = this.leftSelectBasin,
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
        this.listFlag = false;
        this.tableData = [];
        this.tableLoading = true;
        this.canExport = false;
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          if (res.data.length) {
            res.data.map((v) => {
              switch(this.fanganIndex0){
                case 0:
                  v.ybWater = v.waterLevel060;
                  break;
                case 1:
                  v.ybWater = v.waterLevel180;
                  break;
                case 2:
                  v.ybWater = v.waterLevel360;
                  break;
                case 3:
                  v.ybWater = v.waterLevel50;
                  break;
                case 4:
                  v.ybWater = v.waterLevel100;
                  break;
                case 5:
                  v.ybWater = v.waterLevel200;
                  break;
              }
              switch(this.squareIndex){
                case 0:
                  v.overName = '设计水位'
                  v.overValue = v.DSFLZ
                  break;
                case 1:
                  v.overName = '正常水位'
                  v.overValue = v.NWL
                  break;
                case 2:
                  v.overName = '汛限水位'
                  v.overValue = v.limitWaterLevel
                  break;
              }
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.isYcyj = true;
              v.colorType = "red";
              pointArr.push(v);
            });
            this.tableData = res.data;
            this.canExport = true;
          }
          this.$parent.showMapPoints(pointArr);
          // this.$parent.showMapPointJh(pointArr, 'cwyj')
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.004, Number(row.LTTD) + 0.013);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "1";
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 80;
        let _this = this;
        // window.onresize = function() {
        //   const height = this.$refs.tableContainer.clientHeight;
        //   this.tableHeight = height - 80
        // }
      });
    },

    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/bm/reservoirei/toExcel` +
          `?adcd=${adcd}` +
          `&itemCategory=forecast,${this.squareValue}` +
          `&itemValue=${this.planId}` +
          `&token=${token}`
      );
    },

    sendMsg() {
      this.$emit("openMassageDialog");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  .btn_title {
    padding: 8px 16px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    .title {
      @include flexbox();
      @include flexAC();
      & > p {
        font-size: 14px;
        line-height: 32px;
        color: #000000;
        padding-left: 5px;
      }
    }
  }
  .select_title {
    padding: 16px 16px 10px 16px;
    @include flexbox();
    @include flexAC();
    @include flexJC(space-between);
    & > div {
      @include flexbox();
      @include flexAC();
      color: #000;
      font-weight: 500;
      font-size: 16px;
      img {
        margin-right: 5px;
      }
    }
  }
  .fangan_box {
    height: 154px;
    .fangan_title {
      padding: 8px 16px;
      @include flexbox();
      @include flexJC(space-between);
      & > p {
        font-size: 16px;
        line-height: 32px;
        color: #000000;
        font-weight: 500;
      }
    }
  }
  .fbcyj_box {
    .select_square {
      height: 80px;
      box-sizing: border-box;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 8px 16px 16px 16px;
      .square_item {
        box-sizing: border-box;
        width: 32%;
        height: 70px;
        background: #f8f8f8;
        border: 1px solid #d6d6d6;
        text-align: center;
        cursor: pointer;
        border-radius: 2px;
        margin-bottom: 8px;
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
          padding: 8px 0 4px 0;
        }
        i {
          font-size: 12px;
          padding-left: 4px;
        }
        & > span,
        & > h3 {
          line-height: 24px;
          font-size: 20px;
          color: #000;
          font-weight: 500;
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
  .seawallList_wrap {
    &.hei2 {
      height: calc(100% - 160px);
    }
  }
  .seawallList_box {
    height: calc(100% - 320px);
  }
  .cwzfblist_wrap {
    height: calc(100% - 105px);
  }
  .table_list {
    height: calc(100% - 42px);
    box-sizing: border-box;
    padding-bottom: 16px;
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
.htfbChart {
  width: 100%;
  height: 210px;
}
</style>
<style lang="scss">
.select_title {
  .el-radio {
    color: #000;
    font-size: 16px;
    margin-right: 8px;
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #000;
  }
  .el-radio__label {
    padding-left: 4px;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1341c5;
    background: #1341c5;
  }
  .el-select .el-input__inner {
    height: 32px;
  }
  .el-input__icon {
    line-height: unset;
  }
}
#fangan_select {
  padding: 0 16px 12px 16px;
  @include flexbox();
  overflow-x: auto;
  overflow-y: hidden;
  .select_title{
    padding: 0;
    text-align: center;
    line-height: 32.88px;
  }
  button{
    width: 70px;
  }
  .el-select {
    width: 100%;
    // height: 32px;
  }
  .el-button {
    padding-left: 8px;
    padding-right: 8px;
  }
}
#fangan_select::-webkit-scrollbar {
  // display: none;
}
#fangan_select::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 10px;
  background-color: rgb(168, 168, 168);
}

// #fangan_select::-webkit-scrollbar-track {
//   /*滚动条里面轨道*/
//   // -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
//   background: #fff;
//   // background-color: #fff;
//   // display: none

// }
.noFangan {
  text-align: center;
}
</style>
