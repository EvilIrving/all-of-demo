<template>
  <div class="project-side" ref="stationSide">
    <div class="statistics-chart" ref="statisticsChart">
      <div class="statistics-list" ref="statisticsList" :style="listStyle">
        <div class="catalog-title" ref="catalogTitle">
          <div class="title-left">
            <i class="icon-judge-icon"></i>
            <span>{{ listTitle1 }}</span>
          </div>
        </div>
      </div>

      <div class="chart-box">
        <pie-chart :chartData="chartData"></pie-chart>
      </div>
      <el-row :gutter="8" class="split-row">
        <div class="rain-station">
          <primary-select
            @selectHandle="selectHandle"
            style="margin-bottom: 16px"
          ></primary-select>
        </div>
      </el-row>

      <el-row :gutter="20" style="margin-top: 10px">
        <el-col :span="8">
          <el-select
            v-model="proType"
            placeholder="四乱类型"
            @change="getList"
            clearable
          >
            <el-option
              v-for="item in fourOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-select
            v-model="status"
            placeholder="审批情况"
            @change="getList"
            clearable
          >
            <el-option
              v-for="item in checkOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-date-picker
            class="year-sel"
            v-model="year"
            type="year"
            placeholder="监测年份"
            @change="getList"
            value-format="yyyy"
            format="yyyy"
          >
          </el-date-picker>
        </el-col>
      </el-row>
      <el-row
        :gutter="20"
        class="split-row"
        style="margin: 15px 0; color: #c0c4cc"
      >
      </el-row>
    </div>
    <!-- 四乱问题列表 -->
    <div class="statistics-list" ref="statisticsList" :style="listStyle">
      <div class="catalog-title" ref="catalogTitle">
        <div class="title-left">
          <i class="icon-judge-icon"></i>
          <span>{{ listTitle2 }}</span>
        </div>
      </div>
      <div class="search-area" ref="searchArea">
        <div class="left-area">
          <el-input
            v-model="name"
            placeholder="输入四乱问题名称进行搜索"
            @input="searchName"
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
          :columnList="columnList"
          :dataList="dataList"
          :tableHeight="tableHeight"
          :tableLoading="tableLoading"
          :loading="tbLoading"
        />
      </div>
    </div>
  </div>
</template>
<script>
import { PrimaryMapTab, PrimarySubhead, PrimarySelect } from "@/components";
import DetailTable from "../dialog/DetailTable";
import EchartCapsule from "../charts/echart_capsule/EchartCapsule";
import { ScreenTable, PrimaryMapTable, PrimaryDialog } from "@/components/";
import PieChart from "./PieChart";
import { WaterRegulationApi } from "@/api/waterSupervision/index.js";
export default {
  name: "ClearFour",
  components: {
    PieChart,
    PrimarySubhead,
    PrimaryMapTab,
    PrimarySelect,
    ScreenTable,
    PrimaryMapTable,
    EchartCapsule,
    PrimaryDialog,
    DetailTable,
  },
  data() {
    return {
      // 规划字段
      quanbu: "",
      year: "",
      tableHeight: "",
      adnm: "",
      areaCode: "",
      proType: "",
      state: "",
      fourOptions: [
        {
          label: "乱占",
          value: 1,
        },
        {
          label: "乱采",
          value: 2,
        },
        {
          label: "乱堆",
          value: 3,
        },
        {
          label: "乱建",
          value: 4,
        },
      ],
      checkOptions: [
        {
          label: "未处理",
          value: 1,
        },
        {
          label: "待审核",
          value: 2,
        },
        {
          label: "已处理",
          value: 3,
        },
        {
          label: "逾期未处理",
          value: 4,
        },
      ],
      status: "",
      subhead: "取水户列表",
      activeHeadtab: 0,
      name: "",
      columnList: [
        {
          prop: "proType",
          label: "四乱类型",
        },
        {
          prop: "adnm",
          label: "行政区划",
        },
        {
          prop: "reportYear",
          label: "监测年份",
        },
        {
          prop: "status",
          label: "审批情况",
        },
      ],
      dataList: [],
      tabList: [],
      tableData: [],
      inventoryList: [],
      tableLoading: false,
      activeHeadtab: 0,
      tableListStyle: {}, //表格样式
      listStyle: {}, //表格父容器样式
      //结束
      chartData: {
        columns: ["key", "value"],
        rows: [],
      },
      tbLoading: false,
    };
  },
  computed: {
    listTitle1() {
      let text = "四乱统计";
      return text;
    },
    listTitle2() {
      let text = "四乱问题列表";
      // if (this.areaCode) {
      //   text = text + "(" + this.areaCode + ")";
      // } else {
      //   text = "统计列表(" + this.projectTotal + ")";
      // }
      return text;
    },
  },

  mounted() {
    this.$nextTick(() => {
      this.getList();
      this.getCount();
    });
  },
  methods: {
    //    按名字搜索
    searchName() {
      this.getList();
    },
    // 获取统计饼图
    async getCount() {
      await WaterRegulationApi.proTypeStatistics().then((res) => {
        if (res.code == 0) {
          let info = res.data;
          let arr = [];
          let obj = {
            1: "乱占",
            2: "乱采",
            3: "乱堆",
            4: "乱建",
          };
          for (let i in info) {
            if (obj[info[i].proType]) {
              arr.push({
                key: obj[info[i].proType],
                value: info[i].num,
              });
            }
          }
          this.chartData.rows = arr;
        }
      });
    },
    // 计算表格高度
    // 获取列表
    async getList() {
      //   this.$parent.clearOneLayer("清四乱");
      this.tbLoading = true;
      this.tableLoading = true;
      this.dataList = [];
      const opt = {
        name: this.name,
        adcd: this.areaCode,
        proType: this.proType,
        year: this.year,
        status: this.status,
        fetchAll: true,
      };
      await WaterRegulationApi.queryWaterProblem(opt).then((res) => {
        this.tableLoading = false;
        if (res.code == 0) {
          let list = res.data.list;
          this.dataList = list;
          let obj = {
            1: "乱占",
            2: "乱采",
            3: "乱堆",
            4: "乱建",
          };
          let obj1 = {
            1: "未处理",
            2: "待审核",
            3: "已处理",
            4: "逾期未处理",
          };
          list.map((item) => {
            let src = "";
            if (item.proType == 1) {
              src = require("@/assets/images/legend-rain10.png");
            } else if (item.proType == 2) {
              src = require("@/assets/images/legend-rain9.png");
            } else if (item.proType == 3) {
              src = require("@/assets/images/legend-rain25.png");
            } else if (item.proType == 4) {
              src = require("@/assets/images/legend-rain50.png");
            }
            item.proType = obj[item.proType];
            item.status = obj1[item.status];
            item.lng = item.lng;
            item.lat = item.lat;
            item.type = "four";
            item.src = src;
            return item;
          });
          // this.$parent.showMapPoint(list, "清四乱");
        }
        this.tbLoading = false;
      });
    },
    selectHandle(val, type, label) {
      if (type == "area") {
        this.areaCode = val;
        this.getList();
        // this.$parent.showAreaBorder(label);
      }
    },
    rowClick(data) {
      let opt = data;
      opt.type = "four";
      if (!data.lng || !data.lat) {
        this.$message.warning("该点缺失经纬度");
        return;
      } else {
        // this.$parent.showDialog(opt);
        console.log(this);
        // console.log(this.$parent);
      }
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
    //计算表格父容器高度
    // calcDomHeight() {
    //   this.$nextTick(() => {
    //     const height =
    //       this.$refs.stationSide.offsetHeight -
    //       this.$refs.statisticsChart.offsetHeight +
    //       "px";
    //     this.listStyle = {
    //       height,
    //     };
    //   });
    // },
    // 查询项目清单
    // planningName() {
    //   this.getAllProjByPrId({ prId: this.value2, projName: this.projName });
    // },
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
      width: 100%;
      height: 240px;
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
<style lang="scss" scoped>
@import "@/style/theme.scss";
.rain-station {
  @include flexbox;
  @include flexflow(column, wrap);
  width: 100%;
  height: calc(100% - 16px);
  color: #fff;
  font-size: 14px;
  > div {
    width: 100%;
  }
  .rain-range {
    margin-top: 16px;
    margin-bottom: 16px;
    > div {
      text-align: center;
    }
    .wave {
      line-height: 44px;
    }
  }
  .table-box {
    width: 100%;
    @include flex(1);
    @include flexbox;
    @include flexflow(column, wrap);
    position: relative;
    ::v-deep .table-wrap {
      position: absolute;
      top: 0;
    }
  }
  .chart-box {
    width: 100%;
    height: 240px;
  }
}
</style>
<style lang="scss">
.year-sel.el-date-editor.el-input,
.year-sel.el-date-editor.el-input__inner {
  width: 100%;
}
.el-input-group__append,
.el-input-group__prepend {
  background: rgba(0, 0, 0, 0.4);
  border: 0;
  color: #fff;
}
</style>