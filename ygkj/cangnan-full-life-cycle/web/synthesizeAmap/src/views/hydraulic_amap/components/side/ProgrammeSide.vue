<template>
  <div class="project-side" ref="stationSide">
    <!-- 条件查询及搜索 -->
    <div class="statistics-chart" ref="statisticsChart">
      <el-row :gutter="20" class="split-row">
        <el-col :span="12">
          <el-select
            v-model="value1"
            placeholder="规划类型"
            @change="programme"
          >
            <el-option
              v-for="item in tabList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-col>
        <el-col :span="12">
          <el-select
            v-model="value2"
            placeholder="规划名称"
            @change="planningName"
          >
            <el-option
              v-for="item in tableData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-col>
      </el-row>
      <el-row
        :gutter="20"
        class="split-row"
        style="margin: 15px 0; color: #c0c4cc"
      >
        <el-col :span="24" style="margin-bottom: 20px">
          <p>
            项目总数(个)：<span style="color: #fff">{{
              inventoryList.length
            }}</span>
          </p>
        </el-col>
        <el-col :span="24">
          <p>
            总投资金额(万元)：<span style="color: #fff">{{
              amountInvestTotal
            }}</span>
          </p>
        </el-col>
      </el-row>
    </div>
    <!-- 统计列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="title-left">
          <i class="icon-judge-icon"></i>
          <span>{{ listTitle }}</span>
        </div>
      </div>
      <div class="search-area" ref="searchArea">
        <div class="left-area">
          <el-input
            @input="planningName"
            v-model="projName"
            placeholder="请输入项目名称进行查询"
          >
            <template slot="prepend">
              <el-button slot="append" icon="el-icon-search"></el-button>
            </template>
          </el-input>
        </div>
        <!-- <div class="right-area">
          <span @click="lookDetail">查看详表</span>
        </div> -->
      </div>
      <div class="table-list" :style="tableListStyle">
        <screen-table
          :columnList="tableColumns"
          :dataList="inventoryList"
          :loading="tbLoading"
          @rowClick="rowClick"
          :tableHeight="tableHeight"
        />
      </div>
    </div>
    <!-- 详情弹框 -->
    <primary-dialog
      mainTitle="详表"
      :visible="detailDialog"
      @handleClose="handleDetailClose"
      width="50%"
      :hasAnalysis="false"
    >
      <div class="dialog-content scope-content">
        <detail-table v-if="detailDialog" :projType="projectType" />
      </div>
    </primary-dialog>
  </div>
</template>

<script>
import DetailTable from "../dialog/DetailTable";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import { ScreenTable, PrimaryMapTable, PrimaryDialog } from "@/components/";

export default {
  name: "project-side",
  data() {
    return {
      // 规划字段
      value1: "",
      value2: "",
      projName: "",
      tabList: [],
      tableData: [],
      inventoryList: [],
      amountInvestTotal: 0,
      //结束

      statisticData: [], //统计图数据
      extraOption: {},
      projectType: "发展规划", //工程类型
      searchVal: "",
      tableHeight: 0,
      tableColumns: [
        {
          prop: "projName",
          label: "名称",
        },
        {
          prop: "constructProperty",
          label: "建设性质",
        },
        {
          prop: "amountInvestTotal",
          label: "总投资（万元）",
        },
      ],
      dataList: [],
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      typeTab: "行政区划",
      basinName: "", //选中流域字段
      areaCode: "", //选中行政区划
      detailDialog: false,
      //条件选择
      radioVal: "",
      radioVal1: "",

      tbLoading: false,
    };
  },
  computed: {
    chartStyle() {
      let obj = {
        height: "280px",
      };
      if (this.typeTab == "行政区划") {
        obj = {
          height: "280px",
        };
      } else if (this.typeTab == "流域") {
        obj = {
          height: "180px",
        };
      } else if (this.typeTab == "条件选择") {
        obj = {
          height: "180px",
        };
      }
      return obj;
    },
    listTitle() {
      let text = "统计列表(" + this.projectTotal + ")";
      if (this.areaCode) {
        text = text + "(" + this.areaCode + ")";
      } else {
        text = "统计列表(" + this.projectTotal + ")";
      }
      return text;
    },
    mapObj() {
      return this.$store.state.hydraulicAmap.mapComponent;
    },
    projectTotal() {
      return this.inventoryList.length;
    },
  },
  components: {
    ScreenTable,
    PrimaryMapTable,
    EchartCapsule,
    PrimaryDialog,
    DetailTable,
  },
  watch: {
    $route: {
      handler: function (val, oldVal) {
        this.projectType = val.query.type ? val.query.type : "";

        this.getPlanTypeByClassify({ classify: this.projectType });

        this.calcDomHeight();
        this.calcTableHeight();

        this.getProjectList(this.projectType);
        this.projectCount("行政区划", this.projectType);
        this.typeTab = "行政区划";
        this.$nextTick(() => {
          this.resetParam();
        });
      },
      // 深度观察监听
      deep: true,
    },
  },
  mounted() {
    this.projectType = this.$route.query.type ? this.$route.query.type : "";
    this.getPlanTypeByClassify({ classify: this.projectType });

    this.calcDomHeight();
    this.calcTableHeight();
    this.projectType = this.$route.query.type ? this.$route.query.type : "";
    // console.log(3334,this.projectType);
    this.getProjectList(this.projectType);
    this.projectCount("行政区划", this.projectType);
    window.addEventListener("resize", this.handleWindowResize);
    this.$nextTick(() => {
      this.resetParam();
    });
    this.projectsDetailList();
    this.projectsShanList();
    this.projectsDykeLevelList();
    this.projectsSeawallList();
    this.projectsTypeList();
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleWindowResize);
  },
  methods: {
    // 查询规划名称
    programme(val) {
      let str = this.tabList.find((item) => item.id == val);
      this.projectName = str.name;
      this.pagePlanRec({ ptId: val });
    },
    // 查询项目清单
    planningName() {
      this.getAllProjByPrId({ prId: this.value2, projName: this.projName });
    },
    // 项目清单
    async getAllProjByPrId(val) {
      this.tbLoading = true;
      this.amountInvestTotal = 0;
      this.inventoryList = [];
      await hydraulicApi.getAllProjByPrId(val).then((res) => {
        if (res.code == 0) {
          res.data.forEach((item) => {
            this.inventoryList.push(item);
            this.amountInvestTotal =
              this.amountInvestTotal + item.amountInvestTotal;
          });
          this.tbLoading = false;
        }
      });
    },
    //规划名称列表
    async pagePlanRec(val) {
      this.tableData = [];
      await hydraulicApi.pagePlanRec(val).then((res) => {
        if (res.code == 0) {
          this.value2 = res.data.list[0].id;
          this.planningName();
          res.data.list.forEach((item) => {
            this.tableData.push({
              name: item.planName,
              id: item.id,
            });
          });
        }
      });
    },
    // 规划接口
    async getPlanTypeByClassify(val) {
      this.tabList = [];
      await hydraulicApi.getPlanTypeByClassify(val).then((res) => {
        if (res.code == 0) {
          this.value1 = res.data[1].ptName;
          this.pagePlanRec({ ptId: res.data[1].id });
          res.data.forEach((item) => {
            this.tabList.push({
              name: item.ptName,
              id: item.id,
            });
          });
        }
      });
    },

    //计算表格父容器高度
    calcDomHeight() {
      this.$nextTick(() => {
        const height =
          this.$refs.stationSide.offsetHeight -
          this.$refs.statisticsChart.offsetHeight +
          "px";
        this.listStyle = {
          height,
        };
      });
    },
    //计算表格高度
    calcTableHeight() {
      this.$nextTick(() => {
        setTimeout(() => {
          this.tableHeight =
            this.$refs.statisticsList.offsetHeight -
            this.$refs.catalogTitle.offsetHeight -
            this.$refs.searchArea.offsetHeight;
          this.tableListStyle = {
            height: this.tableHeight + "px",
          };
        }, 100);
      });
    },
    handleWindowResize() {
      this.calcDomHeight();
      this.calcTableHeight();
    },
    rightShowDialog(data) {
      let pointList = [];
      let lineList = [];
      if (data.type == "line") {
        if (data.pointList.length > 0) {
          data.pointList.forEach((item) => {
            lineList.push({
              name: item.projectName,
              coordinates: item.coodinate,
              fillColor: "red",
              lineColor: "red",
              lineWidth: 5,
              id: data.id,
            });
          });
          this.mapObj._drawLine(lineList[0], [], "line");
        }
      } else {
        data.pointList.forEach((item) => {
          let imgSrc = require("@/assets/images/reservoir.png");
          if (item.projectType == "河道") {
            imgSrc = require("@/assets/images/agricultural-water.png");
          } else if (item.projectType == "灌区") {
            imgSrc = require("@/assets/images/flood-control-areas.png");
          } else if (item.projectType == "分洪工程") {
            imgSrc = require("@/assets/images/irrigated-area.png");
          } else if (item.projectType == "防洪区") {
            imgSrc = require("@/assets/images/pump-station.png");
          } else if (item.projectType == "堤防") {
            imgSrc = require("@/assets/images/water-gate.png");
          } else if (item.projectType == "水库") {
            imgSrc = require("@/assets/images/reservoir.png");
          } else if (item.projectType == "保护区") {
            imgSrc = require("@/assets/images/protection-zone.png");
          }
          pointList.push({
            lng: item.lat,
            lat: item.lng,
            name: item.projectName,
            id: data.id,
            src: imgSrc,
          });
        });
        this.mapObj._drawPoints(pointList, [], "pointLayer");
      }
    },
    rowClick(data) {
      console.log(101, data, data.mapCoordinate);
      let arr = [];
      if (data.mapCoordinate) {
        arr = JSON.parse(data.mapCoordinate);
      }
      let str = {
        id: data.id,
        name: data.projName,
        projType: data.projType,
        pointList: arr.point.length > 0 ? arr.point : arr.line,
        type: arr.point.length > 0 ? "point" : "line",
      };
      console.log(102, str);
      // this.mapObj.setView(arr.line[0]);
      // this.$parent.showDialog(data);
      this.rightShowDialog(str);
      // this.$parent.$emit("programmeShowDialog", str);

      // this.$parent.programmeShowDialog({
      //   id: data.id,
      //   name: data.projName,
      //   projType: data.projType,
      //   pointList: arr.point.length > 0 ? arr.point : arr.line,
      //   type: arr.point.length > 0 ? 'point' : 'line' ,
      // });
      // let lng =
      //   data.longitude ||
      //   data.lowLeftLong ||
      //   data.dtMidLong ||
      //   data.startLong ||
      //   data.pustLong ||
      //   data.startLong ||
      //   data.hystLong;
      // let lat =
      //   data.latitude ||
      //   data.lowLeftLat ||
      //   data.dtMidLat ||
      //   data.startLat ||
      //   data.pustLat ||
      //   data.startLat ||
      //   data.hystLat;
      // if(lng && lat)
      //   this.mapObj.setView(lng, lat);
      //   // this.mapObj.setView(120.61000290936578, 27.58024579310141);

      // // 有问题
      // this.$parent.showDialog(data);
    },
    //重置筛选条件
    resetParam() {
      this.basinName = "";
      this.areaCode = "";
      this.searchVal = "";
      this.mapObj.resetMap();
      this.mapObj.clearAreaLayer();
      this.mapObj.clearBasinLayer();
      this.getProjectList(this.projectType);
    },
    //统计分析tab改变
    typeTabChange(type) {
      this.resetParam();
      this.typeTab = type;
      this.projectCount(type, this.projectType);
    },
    async getProjectList(type, unloadPoint) {
      // console.log(this.$route.query.type);
      const opt = {
        type: this.$route.query.type || "水库",
        projectName: this.searchVal,
        areaCode: this.areaCode,
        bas: this.basinName,
      };
      if (type == "水库" || type == "水闸") {
        opt.withWaterLevel = true;
      }
      this.tableColumns = [
        {
          prop: "projName",
          label: "名称",
        },
        {
          prop: "constructProperty",
          label: "建设性质",
        },
        {
          prop: "amountInvestTotal",
          label: "总投资（万元）",
        },
      ];
      // let arr = [
      //   {
      //     prop: "startYear",
      //     label: "项目起止年限",
      //     sortable: true,
      //   },
      //   {
      //     prop: "constructProperty",
      //     label: "建设性质",
      //     sortable: true,
      //   },
      //   {
      //     prop: "amountInvestTotal",
      //     label: "总投资（万元）",
      //     sortable: true,
      //   }
      // ];
      switch (type) {
        case "水库":
          {
            arr = [
              {
                prop: "engScal",
                label: "工程规模",
                sortable: true,
              },
              {
                prop: "totCap",
                label: "总库容(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "山塘":
          {
            arr = [
              {
                prop: "mnpdCate",
                label: "山塘类型",
                sortable: true,
              },
              {
                prop: "totalVol",
                label: "总容积(万m³)",
                sortable: true,
              },
            ];
          }
          break;
        case "堤防":
          {
            arr = [
              {
                prop: "dikeType",
                label: "堤防类别",
              },
              {
                prop: "dikeLen",
                label: "堤防长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "海塘":
          {
            arr = [
              {
                prop: "seawallLevel",
                label: "海塘级别",
                sortable: true,
              },
              {
                prop: "reachStandardLength",
                label: "海塘长度(m)",
                sortable: true,
              },
            ];
          }
          break;
        case "泵站":
          {
            arr = [
              {
                prop: "engScal",
                label: "泵站规模",
                sortable: true,
              },
              {
                prop: "insFlow",
                label: "装机流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "水闸":
          {
            arr = [
              {
                prop: "engScal",
                label: "水闸规模",
                sortable: true,
              },
              {
                prop: "desLockDisc",
                label: "最大过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "闸站":
          {
            arr = [
              {
                prop: "engScal",
                label: "闸站规模",
                sortable: true,
              },
              {
                prop: "maxFlow",
                label: "过闸流量(m³/s)",
                sortable: true,
              },
            ];
          }
          break;
        case "灌区":
          {
            arr = [
              {
                prop: "engScal",
                label: "灌区等级",
                sortable: true,
              },
            ];
          }
          break;
        case "农饮水":
          {
            arr = [];
          }
          break;
        case "水电站":
          {
            arr = [
              {
                prop: "engScal",
                label: "水电站规模",
                sortable: true,
              },
              {
                prop: "totInsCap",
                label: "总装机容量(kW)",
                sortable: true,
              },
            ];
          }
          break;
      }
      arr.forEach((item) => {
        this.tableColumns.push(item);
      });

      let res = await hydraulicApi.getProjects(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      this.dataList = res.data;
      // console.log( this.$parent.showProjectPoint)
      if (!unloadPoint) {
        // console.log(type,111)
        if (type == "海塘") {
          this.mapObj.handleSeawallChange(this.dataList);
        } else {
          this.mapObj.showProjectPoint(this.dataList, this.projectType);
        }
      }
    },
    async getCountryData(data) {
      if (this.typeTab == "行政区划") {
        this.areaCode = data.name;
        this.mapObj.showAreaLine(this.areaCode);
      } else if (this.typeTab == "流域") {
        this.basinName = data.name;
        const bsName = this.basinName + "流域";
        // console.log(this.basinName);
        this.mapObj.basinCommand(bsName);
      }
      this.getProjectList(this.projectType, true);
    },
    //工程分类 统计
    async projectCount(type, stType) {
      // console.log(stType);
      let opt = {
        type,
        stType,
      };
      let res = await hydraulicApi.projectCount(opt);
      if (res.code == 0) {
        this.extraOption = {};
        this.statisticData = [];
        let xArr = [];
        // console.log(res.data,999);
        res.data.forEach((item) => {
          const x = item.areaName || item.key;
          xArr.push(x);
          const y = item.number || item.value;
          this.statisticData.unshift(y);
        });
        xArr = xArr.reverse();
        // console.log(this.statisticData,123);
        let obj = {
          yAxis: {
            data: xArr,
          },
        };
        this.extraOption = { ...obj };

        this.$refs.chartCapsule.updateChartView();
        this.$nextTick(() => {
          this.calcTableHeight();
        });
      }
    },
    //详表弹窗关闭
    handleDetailClose() {
      this.detailDialog = false;
    },
    //查看详表
    lookDetail() {
      this.detailDialog = true;
    },
    //工程规模筛选
    async projectsDetailList() {
      let opt = {
        type: this.projectType,
        level: this.radioVal,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
      console.log(this.dataList);
    },
    //山塘类型筛选
    async projectsShanList() {
      let opt = {
        type: this.projectType,
        mnpdCate: this.radioVal,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
      console.log(this.dataList);
    },
    //堤防级别筛选
    async projectsDykeLevelList() {
      let opt = {
        type: this.projectType,
        dikeGrad: this.radioVal,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
      console.log(this.dataList);
    },
    //海塘筛选
    async projectsSeawallList() {
      let opt = {
        type: this.projectType,
        dikeGrad: this.radioVal1,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
      console.log(this.dataList);
    },
    //类型筛选
    async projectsTypeList() {
      let opt = {
        type: this.projectType,
        engType: this.radioVal,
      };
      let res = await hydraulicApi.projectsDetailList(opt);
      res.data.map((item) => {
        item.resName =
          item.resName ||
          item.mnpdName ||
          item.dikeName ||
          item.seawallName ||
          item.pustName ||
          item.wagaName ||
          item.gateName ||
          item.irrName ||
          item.cwsName ||
          item.hystName;
        item.totalVol = item.totalVol || "-";
      });
      if (res.code == 0) {
        this.dataList = res.data;
      } else {
        this.dataList = [];
      }
      console.log(this.dataList);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.project-side {
  width: 100%;
  height: 100%;
  @include flexbox;
  @include flexflow(column, nowrap);
  .statistics-chart {
    position: relative;
    flex: 1;
    .title-right {
      position: absolute;
      right: 0;
      .icon-btn-reset {
        cursor: pointer;
        height: 30px;
      }
    }
    .chart-tabs {
      margin-top: 10px;
      @include flexbox;
      @include flexAC;
      span {
        display: inline-block;
        padding: 4px 8px;
        width: 80px;
        // height: 28px;
        color: #4898fb;
        border: 1px solid #4898fb;
        text-align: center;
        line-height: 16px;
        font-size: 12px;
        margin-right: 16px;
        cursor: pointer;
        &.active {
          color: #a6e9fb;
          background: rgba(72, 152, 251, 0.1);
          border: 1px solid #23e3fb;
        }
      }
    }
    .chart-box {
      padding-top: 8px;
      .split-line {
        display: inline-block;
        width: 100%;
        height: 4px;
      }
      ::v-deep .dv-capsule-chart {
        height: 100%;
      }
      .type-chart {
        padding: 16px 0;
        .type-chart-item {
          @include flexbox;
          @include flexflow(row, nowrap);
          color: #00c1ff;
          margin-bottom: 20px;
          .chart-item-title {
            width: 25px;
            letter-spacing: 10px;
            border-right: 1px solid #00c1ff;
          }
          .chart-item-content {
            @include flex(1);
            @include flexbox;
            @include flexflow(row, nowrap);
            padding-left: 18px;
            .param-item {
              width: 49%;
              @include flexbox;
              @include flexAC;
              @include flexflow(row, nowrap);
              cursor: pointer;
              .param-item-icon {
                width: 43px;
                height: 43px;
              }
              .param-item-data {
                margin-left: 10px;
                p {
                  line-height: 24px;
                  span {
                    font-weight: bold;
                    color: #1cfef4;
                    margin-right: 6px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  .statistics-list {
    @include flex(2);
    .table-list {
      height: 100%;
      width: 100%;
    }
  }
}
</style>
<style lang="scss">
.custom-filter {
  .type-name {
    font-family: PingFangSC-Regular;
    font-size: 14px;
    color: #ffffff;
    font-weight: 400;
  }
  .filter-time {
    height: 48px;
    display: flex;
    align-items: center;
    > * {
      margin: 0 3px;
    }
    .el-button {
      margin-left: 16px;
    }
  }
  .el-radio {
    margin: 8px 0;
    width: 90px;
    .el-radio__inner {
      background-color: transparent;
      border: 1px solid #4694f6;
    }
    .el-radio__input.is-checked {
      .el-radio__inner {
        background: transparent;
        &:after {
          background-color: #1ef8f2;
          border-color: #1ef8f2;
          transform: translate(-50%, -50%) scale(1.8);
        }
      }
    }
    .el-radio__label {
      color: #fff;
    }
  }
  .el-date-editor {
    width: 139px;
    .el-input__prefix {
      color: #3f9bfc;
      left: unset;
      right: 8px;
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__suffix {
      .el-input__icon {
        line-height: 1;
      }
    }
    .el-input__inner {
      padding-left: 8px;
      width: 139px;
      height: 32px;
      border: 1px solid #3f9bfc;
      border-radius: 2px;
    }
    &.el-date-editor--time-select {
      width: 88px;
      .el-input__inner {
        width: 88px;
      }
    }
  }
  .rainfall-range {
    display: flex;
    color: #fff;
    justify-content: left;
    // align-items: center;
    margin-top: 8px;
    margin-bottom: 24px;
    .btn-sty {
      padding: 5px 10px;
      font-size: 14px;
      margin-left: 10px;
      line-height: 20px;
      color: #c0c4cc;
      background: rgba(0, 0, 0, 0.4);
      border: 1px solid #3f9bfc;
    }
    .el-select {
      width: auto;
      .el-input__inner {
        // width: 88px;
        height: 32px;
        border: 1px solid #3f9bfc;
        border-radius: 2px;
      }
      .el-input__suffix {
        .el-input__icon {
          line-height: 1;
        }
      }
    }
    > .el-input {
      width: auto;
      .el-input__inner {
        width: 98px;
        height: 32px;
        border: 1px solid #3f9bfc;
        border-radius: 2px;
      }
    }
  }
  .screen-button {
    width: 72px;
    height: 32px;
    border: 1px solid #4898fb;
    border-radius: 2px;
    background: #0000;
    color: #4898fb;
    padding: 0;
  }
  .split-row {
    margin-top: 10px;
    margin-bottom: 10px;
  }
}
</style>
