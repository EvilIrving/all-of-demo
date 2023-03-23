<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-21 15:36:28
 * @Description: 地图右侧-病险水库
 * @FilePath: /rscp-big-screen/src/views/sgzgScreen/healthyOnline/healthRight/healthRightAll.vue
-->
<template>
  <div class="wrap">
    <div class="chart_line"></div>
    <section class="fbcyj_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        非常运行
      </div>
      <div class="select_square" v-loading="loading_square">
        <div :class="[
            'square_item',
            item.type,
            index === squareIndex ? 'active' : '',
          ]" v-for="(item, index) in squareList" :key="index" @click="clickSquare(index)">
          <div class="square_img_box">
            <div class="square_img">
              <img v-if="index === 2 || index === squareIndex" class="square_img_circle" src="../../../../assets/images/blue_circle.png" alt="" />
              <img v-if="index === 2 || index === squareIndex" class="square_img_center" src="../../../../assets/images/blue_center.png" alt="" />
              <img v-if="index == 3 && index !== squareIndex" class="square_img_circle" src="../../../../assets/images/green_circle.png" alt="" />
              <img v-if="index == 3 && index !== squareIndex" class="square_img_center" src="../../../../assets/images/green_center.png" alt="" />
              <img v-if="index == 1 && index !== squareIndex" class="square_img_circle" src="../../../../assets/images/yellow_circle.png" alt="" />
              <img v-if="index == 1 && index !== squareIndex" class="square_img_center" src="../../../../assets/images/yellow_center.png" alt="" />
              <img v-if="index == 0 && index !== squareIndex" class="square_img_circle" src="../../../../assets/images/orange_circle.png" alt="" />
              <img v-if="index == 0 && index !== squareIndex" class="square_img_center" src="../../../../assets/images/orange_center.png" alt="" />
            </div>
          </div>
          <div class="square_bottom">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <!-- <div class="chart_line"></div>
    <section class="cxjg_box">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        除险加固
      </div>
      <div class="status_list">
        <div :class="['status_item', index === statusIndex ? 'active' : '']" v-for="(item, index) in statusList" :key="index" @click="clickStatus(index)">
          <img src="../../../../assets/images/status_njdbf.png" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>座</i></span>
          </div>
        </div>
      </div>
    </section>
    <div class="chart_line"></div>
    <bar-chart
      v-loading="sghtfbLoading"
      v-if="showSkfbChart"
      class="htfbChart"
      :chartData="skfbData"
      @barClick="barClickAdcd"
    ></bar-chart> -->

    <div class="chart_line"></div>
    <section class="reservoirList_box">
      <div class="chart_title" style="height:25px">
        <img src="../../../../assets/images/chart_item_icon.png" alt="" />
        水库列表
        <!-- <el-button v-if="canExport" style="float:right" plain size="small" @click="exportList()">导出</el-button> -->
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickReservoir">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="水库名称"></el-table-column>
          <el-table-column label="行政区划" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.cityADNM ? scope.row.cityADNM : "-" }}</div>
            </template>
          </el-table-column>
          <el-table-column label="规模" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == '1'">大(1)型</div>
              <div v-if="scope.row.project_scale == '2'">大(2)型</div>
              <div v-if="scope.row.project_scale == '3'">中型</div>
              <div v-if="scope.row.project_scale == '4'">小(1)型</div>
              <div v-if="scope.row.project_scale == '5'">小(2)型</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import BarChart from "@/components/BarChart";
import PieChart from "@/components/PieChart";
import ProgressList from "@/components/ProgressList";
import { reservoirLeftStatisticApi } from "@/api/api_reservoir";
import eventBus from "@/utils/eventBus";
export default {
  name: "",
  props: {},
  components: {
    BarChart,
    PieChart,
    ProgressList,
  },
  data() {
    return {
      sghtfbData: {
        title: "病险水库分布",
        id: "sghtfbChart",
        unit: "",
        legend: {
          orient: "vertical",
          left: "60%",
          height: "70%",
        },
        color: [
          "#00c87b",
          "#539ff5",
          "#6e86a3",
          "#ffc425",
          "#ff7663",
          "#4fd3f0",
          "#aa84d4",
          "#ffa662",
          "#269A99",
        ],
        series: [],
      },
      skfbData: {
        title: "水库数量（座）",
        id: "bxskfbChart",
        xData: [],
        yData: [],
        adcdList: [],
        unit: "座",
      },
      sghtfbLoading: true,
      showSkfbChart: false,
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: "mapList",
        itemCategory: "",
        itemValue: "",
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
      adcd: this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",
      loading_xsgs: true,
      topData: {},
      loading_status: true,
      statusData: {
        title: "除险加固情况",
        id: "statusData",
        unit: "座",
        legend: {
          orient: "vertical",
          left: "65%",
          height: "80%",
        },
        color: ["#5B8FF9", "#5AD8A6", "#F7C63A", "#779fec", "#8BBC89"],
        series: [],
      },
      statusList: [],
      statusValue: "",
      statusIndex: "",
      progressLoading: true,
      progressData: [],
      scale: "all",
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectAdnm() {
      return this.$store.state.leftSelectAdnm;
    },
    leftSelectProjectScale() {
      return this.$store.state.leftSelectProjectScale;
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
      this.getSquareData();
      this.getStatusData();
      this.getSghtfbData();
    },
    leftSelectProjectScale(val) {
      this.setOptions("scale", val);
      this.scale = val;
      this.getSquareData();
      this.getStatusData();
      this.getSghtfbData();
    },
    leftSelectAdnm(val) {
      if (val == this.$localData("get", "userInfo").adnm) {
        this.opt.superviseLevel = "";
        this.opt.adcd = this.leftSelectAdcd;
        this.getReservoirList();
      }
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
    if (this.leftSelectProjectScale) {
      this.setOptions("scale", this.leftSelectProjectScale);
      this.scale = this.leftSelectProjectScale;
    }
    // this.getTopData()
    this.getSquareData();
    this.getStatusData();
    // this.getAdcdData()
    this.getSghtfbData();
  },
  methods: {
    resizeTable() {
      this.$nextTick(function () {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20;
        let _this = this;
        window.onresize = function () {
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

    // async getTopData(){
    //   let res = await reservoirLeftStatisticApi({
    //     statistic: 'problemCnt',
    //     adcd: this.adcd,
    //     itemCategory: this.squareValue,
    //     itemValue: '1',
    //     superviseLevel: this.superviseLevel
    //   })
    //   if(res.success){
    //     this.topData = res.data[0];
    //   }
    // },

    // 运行情况
    async getSquareData() {
      this.loading_square = true;
      this.squareList = [];
      let opt = {};
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "overCnt",
          adcd: this.adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
          superviseLevel: this.superviseLevel,
        };
      } else {
        opt = {
          statistic: "overCnt",
          adcd: this.adcd,
          itemCategory: "",
          itemValue: "",
          superviseLevel: this.superviseLevel,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        this.squareList = [
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
          },
        ];
        this.getReservoirList();
      }
      this.loading_square = false;
    },

    // 除险加固情况
    async getStatusData() {
      this.loading_status = true;
      this.statusData.series = [];
      let opt = {};
      if (this.leftSelectProjectScale) {
        opt = {
          statistic: "problemCnt",
          adcd: this.adcd,
          itemCategory: "scale",
          itemValue: this.leftSelectProjectScale,
          superviseLevel: this.superviseLevel,
        };
      } else {
        opt = {
          statistic: "problemCnt",
          adcd: this.adcd,
          itemCategory: this.squareValue,
          itemValue: "1",
          superviseLevel: this.superviseLevel,
        };
      }
      let res = await reservoirLeftStatisticApi(opt);
      if (res.success) {
        this.statusData.series = [
          {
            name: "未加固",
            value: res.data[0].status1 ? res.data[0].status1 : 0,
            src: require("@/assets/images/status_wjg.png"),
          },
          {
            name: "加固中",
            value: res.data[0].status2 ? res.data[0].status2 : 0,
            src: require("@/assets/images/status_jgz.png"),
          },
          {
            name: "已完工",
            value: res.data[0].status3 ? res.data[0].status3 : 0,
            src: require("@/assets/images/status_ywg.png"),
          },
          {
            name: "拟降等报废",
            value: res.data[0].status4 ? res.data[0].status4 : 0,
            src: require("@/assets/images/status_njdbf.png"),
          },
        ];
        this.statusList = this.statusData.series;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.loading_status = false;
    },

    // 病险水库分布
    async getSghtfbData() {
      this.sghtfbLoading = true;
      this.showSkfbChart = false;
      this.$nextTick(() => {
        this.showSkfbChart = true;
      });
      let opt = {};
      if (this.statusValue) {
        if (this.squareValue) {
          opt = {
            statistic: "adcd",
            itemCategory: "status," + this.squareValue + ",scale",
            itemValue: this.statusValue + ",1," + this.scale,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
            superviseLevel: this.superviseLevel,
          };
        } else {
          opt = {
            statistic: "adcd",
            itemCategory: "status,scale",
            itemValue: this.statusValue + "," + this.scale,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
            superviseLevel: this.superviseLevel,
          };
        }
      } else {
        if (this.squareValue) {
          opt = {
            statistic: "adcd",
            itemCategory: this.squareValue + ",scale",
            itemValue: "1," + this.scale,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
            superviseLevel: this.superviseLevel,
          };
        } else {
          opt = {
            statistic: "adcd",
            itemCategory: "scale",
            itemValue: this.scale,
            adcd: this.leftSelectAdcd
              ? this.leftSelectAdcd
              : this.$localData("get", "userInfo").adcd,
            superviseLevel: this.superviseLevel,
          };
        }
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
              xData.push(
                res.data[i].adnm.substring(0, res.data[i].adnm.length - 1)
              );
            } else {
              xData.push(res.data[i].adnm);
            }
            yData.push(res.data[i].num);
            adcdList.push(res.data[i].adcd);
            adnmList.push(res.data[i].adnm);
          }
        }
        this.skfbData.xData = xData;
        this.skfbData.yData = yData;
        this.skfbData.adcdList = adcdList;
        this.skfbData.adnmList = adnmList;
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.sghtfbLoading = false;
    },

    // 病险水库列表
    async getReservoirList(colorNum) {
      if (this.listFlag) {
        this.$parent.$refs.reservoirMap.removeAllLayer();
        this.$parent.$refs.reservoirMap.closePopup();
        this.$parent.$refs.reservoirMap.initView();
        this.tableLoading = true;
        if (this.opt.adcd === '33') {
          this.opt.superviseLevel = '1'
        } else {
          this.opt.superviseLevel = ''
        }
        this.$store.commit("SET_RESERVOIR_RIGTH_SELECT_FLAG", false);
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
            if (this.squareIndex === 3) {
              colorType = "green";
            } else if (this.squareIndex === 2) {
              colorType = "blue";
            } else if (this.squareIndex === 1) {
              colorType = "yellow";
            } else if (this.squareIndex === 0) {
              colorType = "orange";
            }
            res.data.map((v) => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "1";
              v.src = require("@/assets/images/map/reservoir.png");
              v.colorType = colorType;

              if (this.squareIndex === "") {
                v.waterLevel !== null &&
                v.limitWaterLevel !== null &&
                v.waterLevel <= v.limitWaterLevel
                  ? (v.colorType = "green")
                  : "";

                v.limitWaterLevel !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.limitWaterLevel < v.waterLevel &&
                v.waterLevel <= v.NWL
                  ? (v.colorType = "blue")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.NWL != null &&
                v.NWL < v.waterLevel &&
                v.waterLevel <= v.DSFLZ
                  ? (v.colorType = "yellow")
                  : "";

                v.DSFLZ !== null &&
                v.waterLevel !== null &&
                v.waterLevel > v.DSFLZ
                  ? (v.colorType = "orange")
                  : "";

                // if (v.waterLevel < v.limitWaterLevel) {
                //   v.colorType = "green";
                // } else if (
                //   v.limitWaterLevel < v.waterLevel &&
                //   v.waterLevel < v.NWL
                // ) {
                //   v.colorType = "blue";
                // } else if (v.NWL < v.waterLevel && v.waterLevel < v.FHLV) {
                //   v.colorType = "yellow";
                //   aa++;
                // } else if (v.waterLevel > v.FHLV) {
                //   v.colorType = "orange";
                // } else {
                //   v.colorType = "green";
                // }
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
          this.$parent.showMapPoints(pointArr);
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
          this.opt.itemCategory = "status,scale";
          this.opt.itemValue = this.statusValue + "," + this.scale;
        } else {
          this.squareIndex = index;
          this.squareValue = this.squareList[index].type;
          if (this.statusValue) {
            this.opt.itemCategory = "status,scale," + this.squareValue;
            this.opt.itemValue = this.statusValue + "," + this.scale + ",1";
          } else {
            this.opt.itemCategory = this.squareValue + ",scale";
            this.opt.itemValue = "1," + this.scale;
          }
        }
        this.opt.adcd = this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd;
        this.$parent.$refs.reservoirMap.removePolygon();
        this.getSghtfbData();
        this.getStatusData();
        if (index === 3) {
          this.getReservoirList("green");
          // 添加 eventBus 监听器
          eventBus.$emit("custom-event", 20);
        } else {
          this.getReservoirList();
        }
      }
    },

    clickStatus(index) {
      if (this.listFlag) {
        if (this.statusIndex === index) {
          this.statusIndex = "";
          this.statusValue = "";
          this.opt.itemCategory = this.squareValue;
          this.opt.itemValue = "1";
        } else {
          this.statusIndex = index;
          this.statusValue = this.statusList[index].name;
          if (this.squareValue) {
            this.opt.itemCategory = "status," + this.squareValue;
            this.opt.itemValue = this.statusValue + ",1";
          } else {
            this.opt.itemCategory = "status";
            this.opt.itemValue = this.statusValue;
          }
        }
        this.getSghtfbData();
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

    barClickAdcd(index) {
      if (this.skfbData.xData[index] == "省级") {
        this.opt.superviseLevel = 1;
        // this.$store.commit("SET_LEFT_SELECT_ADCD", "33");
        this.$store.commit("SET_LEFT_SELECT_ADNM", "浙江省");
      } else {
        // this.opt.superviseLevel = "";
        this.opt.adcd = this.skfbData.adcdList[index];
        this.$store.commit(
          "SET_LEFT_SELECT_ADNM",
          this.skfbData.adnmList[index]
        );
        // this.$store.commit('SET_LEFT_SELECT_ADCD', this.skfbData.adcdList[index])
      }
    },
    pieClick(index) {
      console.log(index, "pieClick");
    },
  },
  // beforeDestroy() {
  //   // 移除 eventBus 监听器
  //   eventBus.$off("custom-event");
  // },
};
</script>

<style lang="scss" scoped>
.wrap {
  height: 100%;
  .htfbChart {
    width: 100%;
    height: 220px;
  }
  .statusChart {
    width: 100%;
    height: 250px;
  }
  .prg_box {
    height: 380px;
    position: relative;
    .export_btn {
      position: absolute;
      right: 16px;
      top: 5px;
    }
    .progress {
      height: 100%;
    }
  }

  .xsgs_box {
    padding-bottom: 16px;
    .xsgs_words {
      color: #333;
      padding: 0 16px;
      font-size: 18px;
      line-height: 26px;
      span {
        font-weight: 500;
        &.weight {
          font-weight: bold;
        }
        &.red {
          color: #e02020;
        }
        &.orange {
          color: #fa6400;
        }
        &.yellow {
          color: #f7b500;
        }
      }
    }
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
              // -webkit-animation: imgCircle 2s linear 2s 5 alternate;
              // animation: imgCircle 2s linear infinite;
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
  .cxjg_box {
    .status_list {
      @include flexbox();
      @include flexJC(space-between);
      @include flexflow(row wrap);
      padding: 0 16px;
      box-sizing: border-box;
      .status_item {
        width: 50%;
        @include flexbox();
        @include flexAC();
        // @include flexJC(center);
        // margin-bottom: 8px;
        cursor: pointer;
        border: 1px solid #fff;
        box-sizing: border-box;
        padding: 8px 0;
        // &:hover{
        //   background: rgba(25,59,196,0.15);
        //   @include transition(all, .5s, ease);
        // }
        &.active {
          background: rgba(25, 59, 196, 0.15);
          border: 1px solid #193bc4;
        }
        img {
          margin-right: 10px;
          margin-left: 20px;
        }
        .status_right {
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
            font-size: 16px;
            color: #000;
            font-weight: bold;
          }
        }
      }
    }
  }
  .reservoirList_box {
    height: calc(100% - 188px);
    .table_list {
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
    }
  }
}

@-webkit-keyframes imgCircle {
  from {
    -webkit-transform: rotate(0deg);
  }
  to {
    -webkit-transform: rotate(360deg);
  }
}

@keyframes imgCircle {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
