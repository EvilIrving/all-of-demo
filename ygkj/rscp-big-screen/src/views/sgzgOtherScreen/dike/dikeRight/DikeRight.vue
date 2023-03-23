<!--
 * @Author: hanyu
 * @LastEditTime: 2022-11-29 10:03:08
 * @Description: 流域大屏右侧-堤防
 * @FilePath: \rscp-big-screen\src\views\sgzgOtherScreen\dike\dikeRight\DikeRight.vue
-->
<template>
  <div class="wrap" ref="wrap">
    <section class="scale_box" :style="{ height: barHeight }">
      <div class="chart_title">
        工程规模
        <div class="chart_clean" v-if="scale != 'all'" @click="cleanScaleClick">清除</div>
      </div>
      <basin-single-bar-chart ref="scaleChart" v-loading="scaleLoading"  element-loading-background="rgba(0, 0, 0, 0.2)" v-if="showScaleChart" class="scaleChart" :chartData="scaleData" @barClick="scaleChartClick"></basin-single-bar-chart>
    </section>
    <section class="cxjg_box" ref="cxjgHeight">
      <div class="chart_title">
        除险加固
      </div>
      <div class="status_list" v-loading="loading_status"  element-loading-background="rgba(0, 0, 0, 0.2)">
        <div :class="['status_item', index === statusIndex ? 'active' : '']" v-for="(item, index) in statusList" :key="index" @click="clickStatus(index)">
          <img :src="item.src" alt="" />
          <div class="status_right">
            <p>{{ item.name }}</p>
            <span>{{ item.value ? item.value : 0 }}<i>条</i></span>
          </div>
        </div>
      </div>
    </section>
    <section class="reservoirList_box" ref="reservoirListHeight">
      <div class="chart_title">
        堤防列表
      </div>
      <!-- <div class="table_list" :style="{height: safeStatus == 'firstCnt' || safeStatus == 'totalCnt' ? 'calc(100% - 500px)' : 'calc(100% - 430px)'}" ref="tableContainer"> -->
      <div class="table_list" ref="tableContainer">
        <el-table v-loading="tableLoading" :height="tableHeight" :data="tableData" stripe style="width: 100%" @row-click="clickReservoir"  element-loading-background="rgba(0, 0, 0, 0.2)">
          <template slot="empty">
            <div class="table_nodata">
              <img :src="$config.swImgUrl + 'empty_data.png'" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="堤防名称"></el-table-column>
          <el-table-column label="规模" align="center">
            <template slot-scope="scope">
              <div>{{scope.row.project_scale}}级</div>
            </template>
          </el-table-column>
          <el-table-column label="行政区划" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.cityADNM ? scope.row.cityADNM : "-" }}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </section>
  </div>
</template>

<script>
import BasinSingleBarChart from "../components/BasinSingleBarChart";
import { basinStatisticApi, dikeLineApi } from "@/api/api_basin";
export default {
  name: "",
  props: {
    safeStatus: String
  },
  components: {
    BasinSingleBarChart
  },
  data() {
    return {
      scaleData: {
        id: "scaleChart",
        xData: [],
        yData: [],
        scaleList: [],
        unit: "条"
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
        projectCategory: "堤防",
        adcd: this.leftSelectAdcd
          ? this.leftSelectAdcd
          : this.$localData("get", "userInfo").adcd
      },
      listFlag: true,
      adcd: this.leftSelectAdcd
        ? this.leftSelectAdcd
        : this.$localData("get", "userInfo").adcd,
      superviseLevel: "",

      scale: "all",

      loading_status: true,
      statusList: [],
      statusIndex: "",
      statusValue: "",
      barHeight: ""
    };
  },
  computed: {
    leftSelectAdcd() {
      return this.$store.state.leftSelectAdcd;
    },
    leftSelectBasin() {
      return this.$store.state.leftSelectBasin;
    }
  },
  watch: {
    safeStatus() {
      this.setOptions("safeStatus", this.safeStatus);
      this.getScaleData();
      this.getStatusData();
      this.getReservoirList();
    },
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
      this.getStatusData();
      this.getReservoirList();
    },
    leftSelectBasin() {
      this.getScaleData();
      this.getStatusData();
      this.getReservoirList();
    }
  },
  mounted() {
    this.getBarHeight();
    window.addEventListener("resize", this.getBarHeight);
    if (this.leftSelectAdcd == "33") {
      this.opt.superviseLevel = 1;
      this.superviseLevel = 1;
    }
    if (this.leftSelectAdcd) {
      this.opt.adcd = this.leftSelectAdcd;
      this.adcd = this.leftSelectAdcd;
    }
    this.setOptions("safeStatus", this.safeStatus);
    this.getScaleData();
    this.getStatusData();
    this.getReservoirList();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.getBarHeight);
  },
  methods: {
    getBarHeight() {
      this.barHeight =
        this.$refs.wrap.clientHeight -
        this.$refs.cxjgHeight.clientHeight -
        this.$refs.reservoirListHeight.clientHeight +
        "px";
    },
    resizeTable() {
      this.$nextTick(() => {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height;
        window.onresize = () => {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height;
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
    async getScaleData() {
      this.showScaleChart = false;
      this.$nextTick(() => {
        this.showScaleChart = true;
      });
      let res = await basinStatisticApi({
        statistic: "typeScale",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        projectCategory: "堤防",
        itemCategory: "safeStatus",
        itemValue: this.safeStatus,
        superviseLevel: this.superviseLevel
      });
      if (res.success) {
        let xData = [];
        let yData = [];
        let scaleList = [];
        if (res.data.length) {
          for (let i = 0; i < res.data.length; i++) {
            let text = "";
            switch (Number(res.data[i].projectScale)) {
              case 1:
                text = "一级堤防";
                break;
              case 2:
                text = "二级堤防";
                break;
              case 3:
                text = "三级堤防";
                break;
              case 4:
                text = "四级堤防";
                break;
              case 5:
                text = "五级堤防";
                break;
            }
            xData.push(text);
            yData.push(res.data[i].num);
            scaleList.push(res.data[i].projectScale);
          }
        }
        this.scaleData.xData = xData;
        this.scaleData.yData = yData;
        this.scaleData.scaleList = scaleList;
      }
      this.scaleLoading = false;
    },

    // 除险加固情况
    async getStatusData() {
      this.loading_status = true;
      let res = await basinStatisticApi({
        statistic: "safeStatus",
        adcd: this.adcd,
        basin: this.leftSelectBasin,
        projectCategory: "堤防",
        itemCategory: "safeStatus",
        itemValue: this.safeStatus,
        superviseLevel: this.superviseLevel
      });
      if (res.success) {
        switch (this.safeStatus) {
          case "thirdCnt":
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: this.$config.swImgUrl + "status_wjg.png"
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].thirddegraded : 0,
                src: this.$config.swImgUrl + "status_njdbf.png"
              }
            ];
            break;
          case "secondCnt":
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: this.$config.swImgUrl + "status_wjg.png"
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].seconddegraded : 0,
                src: this.$config.swImgUrl + "status_njdbf.png"
              }
            ];
            break;
          case "processCnt":
            this.statusList = [
              {
                name: "三类堤加固中",
                value: res.data[0].thirdprocess ? res.data[0].thirdprocess : 0,
                src: this.$config.swImgUrl + "status_jgz.png"
              },
              {
                name: "二类堤加固中",
                value: res.data[0].secondprocess
                  ? res.data[0].secondprocess
                  : 0,
                src: this.$config.swImgUrl + "status_jgz.png"
              }
            ];
            break;
          case "firstCnt":
            this.statusList = [
              {
                name: "一类堤",
                value: res.data[0].firstClass ? res.data[0].firstClass : 0,
                src: this.$config.swImgUrl + "status_wjg.png"
              },
              {
                name: "二类堤已完工",
                value: res.data[0].secondfinish ? res.data[0].secondfinish : 0,
                src: this.$config.swImgUrl + "status_ywg.png"
              },
              {
                name: "三类堤已完工",
                value: res.data[0].thirdfinish ? res.data[0].thirdfinish : 0,
                src: this.$config.swImgUrl + "status_ywg.png"
              }
            ];
            break;
          case "totalCnt":
            this.statusList = [
              {
                name: "未加固",
                value: res.data[0].status1 ? res.data[0].status1 : 0,
                src: this.$config.swImgUrl + "status_wjg.png"
              },
              {
                name: "加固中",
                value: res.data[0].status2 ? res.data[0].status2 : 0,
                src: this.$config.swImgUrl + "status_jgz.png"
              },
              {
                name: "已完工",
                value: res.data[0].status3 ? res.data[0].status3 : 0,
                src: this.$config.swImgUrl + "status_ywg.png"
              },
              {
                name: "拟降等报废",
                value: res.data[0].status4 ? res.data[0].status4 : 0,
                src: this.$config.swImgUrl + "status_njdbf.png"
              }
            ];
            break;
        }
      } else {
        this.$message.error(res.msg ? res.msg : "请求失败");
      }
      this.resizeTable();
      this.loading_status = false;
    },

    // 堤防列表
    async getReservoirList() {
      if (this.listFlag) {
        this.opt.basin = this.leftSelectBasin;
        this.$parent.$refs.mainMap.removeAllLayer();
        this.$parent.$refs.mainMap.closePopup();
        this.$parent.$refs.mainMap.initView();
        this.tableLoading = true;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", false);
        this.tableData = [];
        this.listFlag = false;
        let res = await basinStatisticApi(this.opt);
        if (res.success) {
          let pointArr = [];
          // let pointObj = {
          //   green: [],
          //   blue: [],
          //   yellow: [],
          //   orange: []
          // };
          let lineArr = [];
          if (res.data.length) {
            this.tableData = res.data;
            res.data.map(v => {
              v.lat = v.LTTD;
              v.lng = v.LGTD;
              v.rightType = "2";
              v.src = require("@/assets/images/map/reservoir.png");
              pointArr.push(v);
              lineArr.push({
                line: v.COORPOT
                  ? JSON.parse(
                      v.COORPOT.replace(new RegExp("&quot;", "gm"), '"')
                    )[0]
                  : "",
                level: v.project_scale
              });
            });
          }
          this.$parent.showMapPoints(pointArr, 'dike');
          this.$parent.showMapLines(lineArr);
        }
        this.listFlag = true;
        this.tableLoading = false;
        this.$store.commit("SET_BASIN_RIGTH_SELECT_FLAG", true);
      }
    },

    clickReservoir(row) {
      this.$parent.setView(Number(row.LGTD) + 0.022, Number(row.LTTD) + 0.01);
      row.lat = row.LTTD;
      row.lng = row.LGTD;
      row.rightType = "2";
      this.getOneDikeLine(row.PRCD, row.project_scale);
      // this.$parent.showOneSelectPoint([row]);
      setTimeout(() => {
        this.$parent.setPopup(row);
      }, 500);
    },

    async getOneDikeLine(prcd, level) {
      let res = await dikeLineApi({
        prcd: prcd
      });
      let lineArr = [];
      if (res.success) {
        lineArr.push({
          line: res.rows[0].coorpot
            ? JSON.parse(
                res.rows[0].coorpot.replace(new RegExp("&quot;", "gm"), '"')
              )[0]
            : "",
          level: level
        });
        this.$parent.showMapLines(lineArr);
      }
    },

    scaleChartClick(index) {
      this.scale = this.scaleData.scaleList[index];
      this.setOptions("scale", this.scale);
      this.getReservoirList();
    },
    cleanScaleClick() {
      this.scale = "all";
      this.setOptions("scale", this.scale);
      this.$refs.scaleChart.initChart();
    }
  }
};
</script>

<style lang="scss" scoped>
@import "../../style/common.scss";
.wrap {
  height: 100%;
  .chart_title {
    @include chartTitle();
    .chart_clean {
      @include chartClean();
    }
  }
  .scaleChart {
    height: calc(100% - 42px);
  }
  // .scale_box {
  //   height: 260px;
  // }
  .cxjg_box {
    .status_list {
      @include statusList();
      .status_item {
        @include statusItem();
        .status_right {
          @include statusRight();
        }
      }
    }
  }
  .reservoirList_box {
    height: 310px;
    .table_list {
      height: calc(100% - 50px);
      box-sizing: border-box;
      padding: 0 0 0 16px;
      margin-bottom: 8px;
      overflow: auto;
      @include changeElTable();
    }
  }
}
</style>
