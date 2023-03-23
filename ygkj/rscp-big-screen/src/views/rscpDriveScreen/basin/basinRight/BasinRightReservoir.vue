<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-21 15:36:20
 * @Description: 流域大屏右侧-水库
 * @FilePath: /rscp-big-screen/src/views/rscpDriveScreen/basin/basinRight/BasinRightReservoir.vue
-->
<template>
  <div class="wrap">
    <section class="scale_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        工程规模
      </div>
      <basin-single-bar-chart v-loading="scaleLoading" v-if="showScaleChart" class="scaleChart" :chartData="scaleData"></basin-single-bar-chart>
    </section>
    <section class="fbcyj_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        运行情况
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
          <div class="square_img_box">
            <div class="square_img">
              <img
                v-if="index === 2 || index === squareIndex"
                class="square_img_circle"
                src="../../../../assets/images/blue_circle.png"
                alt=""
              />
              <img
                v-if="index === 2 || index === squareIndex"
                class="square_img_center"
                src="../../../../assets/images/blue_center.png"
                alt=""
              />
              <img
                v-if="index == 3 && index !== squareIndex"
                class="square_img_circle"
                src="../../../../assets/images/green_circle.png"
                alt=""
              />
              <img
                v-if="index == 3 && index !== squareIndex"
                class="square_img_center"
                src="../../../../assets/images/green_center.png"
                alt=""
              />
              <img
                v-if="index == 1 && index !== squareIndex"
                class="square_img_circle"
                src="../../../../assets/images/yellow_circle.png"
                alt=""
              />
              <img
                v-if="index == 1 && index !== squareIndex"
                class="square_img_center"
                src="../../../../assets/images/yellow_center.png"
                alt=""
              />
              <img
                v-if="index == 0 && index !== squareIndex"
                class="square_img_circle"
                src="../../../../assets/images/orange_circle.png"
                alt=""
              />
              <img
                v-if="index == 0 && index !== squareIndex"
                class="square_img_center"
                src="../../../../assets/images/orange_center.png"
                alt=""
              />
            </div>
          </div>
          <div class="square_bottom">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <section class="reservoirList_box">
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
          <el-table-column prop="NAME" label="水库名称"></el-table-column>
          <!-- <el-table-column label="安全等级" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.status ? scope.row.status : '-'}}</div>
            </template>
          </el-table-column> -->
          <el-table-column label="规模" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == '1'">大(1)型</div>
              <div v-if="scope.row.project_scale == '2'">大(2)型</div>
              <div v-if="scope.row.project_scale == '3'">中型</div>
              <div v-if="scope.row.project_scale == '4'">小(1)型</div>
              <div v-if="scope.row.project_scale == '5'">小(2)型</div>
            </template>
          </el-table-column>
          <el-table-column label="运行情况" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.yxStatus ? scope.row.yxStatus : "-" }}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import BasinSingleBarChart from "../components/BasinSingleBarChart";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
export default {
  name: "",
  props: {},
  components: {
    BasinSingleBarChart,
  },
  data() {
    return {
      scaleData: {
        id: "scaleChart",
        xData: [],
        yData: [],
        scaleList: [],
        unit: "座",
      },
      scaleLoading: true,
      showScaleChart: false,

      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
        basin: "",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd,
      },
      canExport: false,
      squareList: [],
      squareIndex: "",
      loading_square: true,
      squareValue: "",
      listFlag: true,
      adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",

      progressLoading: true,
      progressData: [],
      scale: "all",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    },
  },
  watch: {
    leftSelectAdcd(val) {
      this.opt.adcd = val ? val : this.$localData("get", "userInfo").adcd;
      if (val == "33") {
        this.opt.superviseLevel = 1;
        this.superviseLevel = 1;
      } else {
        this.opt.superviseLevel = "";
        this.superviseLevel = "";
      }
      this.adcd = val;
      this.getScaleData();
      this.getSquareData();
      this.getReservoirList();
    },
    leftSelectBasin(val) {
      this.getScaleData();
      this.getSquareData();
      this.getReservoirList();
    },
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
    this.getScaleData();
    this.getSquareData();
    this.getReservoirList();
  },
  methods: {
    resizeTable() {
      this.$nextTick(function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
        let _this = this;
        window.onresize = function() {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height - 20;
        };
      });
    },
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
          if (this.opt.itemCategory.indexOf(type) !== -1) {
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

    // 工程规模
    async getScaleData(){
      this.showScaleChart = false
      this.$nextTick(()=>{
        this.showScaleChart = true
      })
      let res = await reservoirLeftStatisticApi({
        statistic: "scale",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        itemCategory: "",
        itemValue: "",
      })
      if (res.success) {
        let xData = [];
        let yData = [];
        let scaleList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            xData.push(res.data[i].project_scale.toString().searchSacle());
            yData.push(res.data[i].num);
            scaleList.push(res.data[i].project_scale);
          }
        }
        this.scaleData.xData = xData;
        this.scaleData.yData = yData;
        this.scaleData.scaleList = scaleList;
        this.$store.commit("SET_RESERVOIR_LEFT_KR_DATA", res.data);
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.scaleLoading = false;
      
    },

    // 运行情况
    async getSquareData() {
      this.loading_square = true;
      this.squareList = [];
      let opt = {
        statistic: "overCnt",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        itemCategory: "",
        itemValue: "",
        superviseLevel: this.superviseLevel,
      };
      
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        this.squareList.push(
          {
            type: "DSFLZ",
            name: "超设计水位",
            value: res.data[0]["DSFLZ"],
          },
          {
            type: "NWL",
            name: "超正常水位",
            value: res.data[0]["NWL"],
          },
          {
            type: "limitWaterLevel",
            name: "超限制水位",
            value: res.data[0]["limitWaterLevel"],
          }
          // {
          //   type: "belowLimit",
          //   name: "低于限制水位",
          //   value: res.data[0]["belowLimit"],
          // }
        );
      }
      this.loading_square = false;
    },

    // 病险水库列表
    async getReservoirList(colorNum) {
      if (this.listFlag) {
        this.opt.basin = this.leftSelectBasin
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.canExport = false;
        this.listFlag = false;
        let res = await reservoirLeftStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          let pointObj = {
            green: [],
            blue: [],
            yellow: [],
            orange: [],
          };
          if (res.data.length) {
            this.canExport = true;
            this.tableData = res.data;
            let colorType = "";
            let yxStatus = "";
            if (this.squareIndex === 3) {
              colorType = "green";
              yxStatus = "低于汛限水位"
            } else if (this.squareIndex === 2) {
              colorType = "blue";
              yxStatus = "超限制水位"
            } else if (this.squareIndex === 1) {
              colorType = "yellow";
              yxStatus = "超正常水位"
            } else if (this.squareIndex === 0) {
              colorType = "orange";
              yxStatus = "超设计水位"
            }
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.src = require("@/assets/images/map/reservoir.png");
              v.colorType = colorType;
              v.yxStatus = yxStatus;

              if (this.squareIndex === "") {
                v.waterLevel !== null &&
                v.limitWaterLevel !== null &&
                v.waterLevel <= v.limitWaterLevel
                  ? (v.colorType = "green", v.yxStatus = "低于汛限水位")
                  : "";

                v.limitWaterLevel !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.limitWaterLevel < v.waterLevel &&
                v.waterLevel <= v.NWL
                  ? (v.colorType = "blue", v.yxStatus = "超限制水位")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.NWL < v.waterLevel &&
                v.waterLevel <= v.DSFLZ
                  ? (v.colorType = "yellow", v.yxStatus = "超正常水位")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.waterLevel > v.DSFLZ
                  ? (v.colorType = "orange", v.yxStatus = "超设计水位")
                  : "";

              }
              if (v.colorType) {
                pointObj[v.colorType].push(v);
              }
            });
            if (colorNum === "green") {
              // 红、黄、蓝、绿
              pointArr = [
                ...pointObj["green"],
                ...pointObj["blue"],
                ...pointObj["yellow"],
                ...pointObj["orange"],
              ];
            } else {
              pointArr = [
                ...pointObj["blue"],
                ...pointObj["yellow"],
                ...pointObj["orange"],
              ];
            }
          }
          // let jhArr = [...pointObj['green']]
          // let noJhArr = [...pointObj['blue'], ...pointObj['yellow'], ...pointObj['orange']]
          // this.$parent.showMapPointJh(jhArr, 'all')
          this.$parent.showMapPoints(pointArr, 'reservoir');
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.004, Number(row.LTTD) + 0.013);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "1";
      // this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    clickSquare(index) {
      if (this.listFlag) {
        if (this.squareIndex === index) {
          this.squareIndex = "";
          this.squareValue = "";
          this.opt.itemCategory = "scale";
          this.opt.itemValue = this.scale;
        } else {
          this.squareIndex = index;
          this.squareValue = this.squareList[index].type;
          this.opt.itemCategory = this.squareValue + ",scale";
          this.opt.itemValue = "1," + this.scale;
        }
        this.opt.adcd = this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd;
        this.$parent.$refs.reservoirMap.removePolygon();
        this.getReservoirList();
      }
    },

    exportList() {
      let adcd = this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd;
      let token = this.$localData("get", "token");
      window.open(
        this.$config.host +
          `/mgt/bm/reservoirei/toExcel` +
          `?adcd=${this.opt.adcd}` +
          `&statistic=mapList` +
          `&superviseLevel=${this.opt.superviseLevel}` +
          `&itemCategory=${this.opt.itemCategory}` +
          `&itemValue=${this.opt.itemValue}` +
          `&token=${token}`
      );
    },
    
    pieClick(index) {
      console.log(index, "pieClick");
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  .scaleChart {
    width: 100%;
    height: 220px;
  }
  .scale_box{
    height: 250px;
  }
  .fbcyj_box {
    height: 188px;
    .select_square {
      // height: 180px;
      box-sizing: border-box;
      padding: 0 16px 16px 16px;
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      .square_item {
        box-sizing: border-box;
        width: 25%;
        height: 130px;
        text-align: center;
        // width: 32%;
        // background: #F8F8F8;
        // border: 1px solid #D6D6D6;
        cursor: pointer;
        border-radius: 2px;
        // margin-bottom: 8px;
        &.active {
          background: #193cc5 !important;
          border: 1px solid #193cc5 !important;
          .square_bottom {
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
        }
        .square_img_box {
          padding-top: 5px;
          .square_img {
            margin: 0 auto;
            width: 55px;
            height: 55px;
            position: relative;
            .square_img_circle {
              width: 100%;
              height: 100%;
            }
            .square_img_center {
              width: 36px;
              height: 36px;
              position: absolute;
              top: 9px;
              left: 9px;
            }
          }
        }
        .square_bottom {
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
  }
  .reservoirList_box {
    height: calc(100% - 440px);
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}
</style>
