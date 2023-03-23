<!--
 * @Author: dyn
 * @Date: 2021-02-27 15:25:20
 * @LastEditTime: 2021-03-01 09:06:39
 * @LastEditors: Please set LastEditors
 * @Description: 洪涝分析-水位分析-左侧
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudgeNew\components\sidebar\WaterAnalysisLeft.vue
-->
<template>
  <div class="sidebar-box">
    <div class="sidebar-top">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 平原水位</span>
      </p>
      <div class="sidebar-search">
        <el-input
          class="search-inp"
          v-model="reservoirName"
          @input="getMeasuringName"
          placeholder="请输入测站名进行搜索">
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        <el-switch
          v-model="reservoirValue"
          active-color="#106CE0"
          inactive-color="#ff4949"
          class="sidebar-switch"
        ></el-switch>
      </div>
      <div class="sidebar-list">
        <el-table
          :data="plainTableData"
          style="width: 100%"
          height="240"
          highlight-current-row
          v-loading="loading">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="stName"
            label="站点名称"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="realLevel"
            label="实时水位(m)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="guaranteeLevel"
            label="保证水位(m)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="warningLevel"
            label="警戒水位(m)"
            width="100"
          ></el-table-column>
        </el-table>
      </div>
    </div>
    <div class="sidebar-bottom">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 流域水位</span>
      </p>
      <div class="sidebar-search">
        <el-input
          class="search-inp"
          v-model="basinName"
          @input="getMeasuringNames"
          placeholder="请输入测站名进行搜索"
        >
          <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-input>
        <el-switch
          v-model="basinValue"
          active-color="#106CE0"
          inactive-color="#ff4949"
          class="sidebar-switch"
        ></el-switch>
      </div>
      <div class="sidebar-list">
        <el-table
          :data="tableData"
          style="width: 100%"
          height="240"
          highlight-current-row
          v-loading="loading"
        >
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="stName"
            label="站点名称"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="realLevel"
            label="实时水位(m)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="guaranteeLevel"
            label="保证水位(m)"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="warningLevel"
            label="警戒水位(m)"
            width="100"
          ></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { waterApi,precipitation } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      reservoirName: "",
      reservoirValue: true,
      basinName: "",
      basinValue: true,
      plainName: "",
      plainValue: true,




      timeTabArr: [
        {
          label: "1小时",
          value: 1,
        },
        {
          label: "3小时",
          value: 3,
        },
        {
          label: "24小时",
          value: 24,
        },
        {
          label: "72小时",
          value: 72,
        },
      ],
      activeTime: 1,
      loading: false,
      colId: null,
      tableData: [],
      plainTableData: [],
    };
  },
  watch: {
    $route: {
      handler(nv, ov) {
        this.getCol();
      },
    },
    deep: true,
  },
  mounted() {
    this.getMeasuringName();
    this.getMeasuringNames();
  },
  methods: {
    //平原流域模糊查询
    getMeasuringName() {
      let obj = {
        stName: this.reservoirName,
        type: 1
      }
      this.getAllStWatLevByType(obj);
    },
    //流域水位
    getMeasuringNames() {
      let obj = {
        stName: this.basinName,
        type: 2
      }
      this.getAllStWatLevByType(obj);
    },
    getCol() {
      this.colId = this.$route.query.id;
      if (this.colId == 4 || this.colId == 1) {
        this.getList();
      } else if (this.colId == 5 || this.colId == 2) {
        // 流域
        // this.tableData = [
        //   {
        //     stName: "温州",
        //     z: "0.7",
        //     warnLevel: "-0.89",
        //     sureLevel: "-",
        //     highestLevel: "5.55",
        //     lng: 120.64,
        //     lat: 28.03,
        //   },
        //   {
        //     stName: "灵昆",
        //     z: "-0.09",
        //     warnLevel: "3.85",
        //     sureLevel: "-",
        //     highestLevel: "-",
        //     lng: 120.85,
        //     lat: 27.97,
        //   },
        //   {
        //     stName: "瑞安",
        //     z: "0.11",
        //     warnLevel: "-1.17",
        //     sureLevel: "-",
        //     highestLevel: "5.03",
        //     lng: 120.62,
        //     lat: 27.79,
        //   },
        //   {
        //     stName: "鳌江",
        //     z: "0.11",
        //     warnLevel: "3.85",
        //     sureLevel: "-",
        //     highestLevel: "5.22",
        //     lng: 120.55,
        //     lat: 27.59,
        //   },
        //   {
        //     stName: "埭头",
        //     z: "10.78",
        //     warnLevel: "18.92",
        //     sureLevel: "19.92",
        //     highestLevel: "17.92",
        //     lng: 120.28,
        //     lat: 27.6,
        //   },
        //   {
        //     stName: "石柱",
        //     z: "16.54",
        //     warnLevel: "31.79",
        //     sureLevel: "30.79",
        //     highestLevel: "29.79",
        //     lng: 120.75,
        //     lat: 28.26,
        //   },
        //   {
        //     stName: "峃口",
        //     z: "21.12",
        //     warnLevel: "38.5",
        //     sureLevel: "39.5",
        //     highestLevel: "37.5",
        //     lng: 120.15,
        //     lat: 27.75,
        //   },
        // ];
      } else if (this.colId == 6 || this.colId == 3) {
        // 平原
        // this.tableData = [
        //   {
        //     stName: "西山",
        //     z: "2.59",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "4.42",
        //   },
        //   {
        //     stName: "永强",
        //     z: "2.39",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "5.93",
        //   },
        //   {
        //     stName: "林垟",
        //     z: "2.72",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "5.29",
        //   },
        //   {
        //     stName: "平阳",
        //     z: "2.69",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "3.85",
        //   },
        //   {
        //     stName: "鳌江内河",
        //     z: "2.77",
        //     warnLevel: "3",
        //     sureLevel: "3.6",
        //     highestLevel: "3.65",
        //   },
        //   {
        //     stName: "宜山",
        //     z: "2.36",
        //     warnLevel: "3",
        //     sureLevel: "3.5",
        //     highestLevel: "3.55",
        //   },
        // ];
      } else {
        // this.tableData = [
        //   {
        //     stName: "西山",
        //     z: "2.59",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "4.42",
        //   },
        //   {
        //     stName: "永强",
        //     z: "2.39",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "5.93",
        //   },
        //   {
        //     stName: "林垟",
        //     z: "2.72",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "5.29",
        //   },
        //   {
        //     stName: "平阳",
        //     z: "2.69",
        //     warnLevel: "3.1",
        //     sureLevel: "3.6",
        //     highestLevel: "3.85",
        //   },
        //   {
        //     stName: "鳌江内河",
        //     z: "2.77",
        //     warnLevel: "3",
        //     sureLevel: "3.6",
        //     highestLevel: "3.65",
        //   },
        //   {
        //     stName: "宜山",
        //     z: "2.36",
        //     warnLevel: "3",
        //     sureLevel: "3.5",
        //     highestLevel: "3.55",
        //   },
        //   {
        //     stName: "温州",
        //     z: "0.7",
        //     warnLevel: "-0.89",
        //     sureLevel: "-",
        //     highestLevel: "5.55",
        //     lng: 120.64,
        //     lat: 28.03,
        //   },
        //   {
        //     stName: "灵昆",
        //     z: "-0.09",
        //     warnLevel: "3.85",
        //     sureLevel: "-",
        //     highestLevel: "-",
        //     lng: 120.85,
        //     lat: 27.97,
        //   },
        //   {
        //     stName: "瑞安",
        //     z: "0.11",
        //     warnLevel: "-1.17",
        //     sureLevel: "-",
        //     highestLevel: "5.03",
        //     lng: 120.62,
        //     lat: 27.79,
        //   },
        //   {
        //     stName: "鳌江",
        //     z: "0.11",
        //     warnLevel: "3.85",
        //     sureLevel: "-",
        //     highestLevel: "5.22",
        //     lng: 120.55,
        //     lat: 27.59,
        //   },
        //   {
        //     stName: "埭头",
        //     z: "10.78",
        //     warnLevel: "18.92",
        //     sureLevel: "19.92",
        //     highestLevel: "17.92",
        //     lng: 120.28,
        //     lat: 27.6,
        //   },
        //   {
        //     stName: "石柱",
        //     z: "16.54",
        //     warnLevel: "31.79",
        //     sureLevel: "30.79",
        //     highestLevel: "29.79",
        //     lng: 120.75,
        //     lat: 28.26,
        //   },
        //   {
        //     stName: "峃口",
        //     z: "21.12",
        //     warnLevel: "38.5",
        //     sureLevel: "39.5",
        //     highestLevel: "37.5",
        //     lng: 120.15,
        //     lat: 27.75,
        //   },
        // ];
      }
    },
    async getList() {
      this.loading = true;
      let res = await waterApi.mapPoint({
        stName: this.name,
        // type: this.$route.query.id || 1,
        type: 1,
      });
      if (res) {
        res.data.map((item) => {
          if (item.highestLevel == null) {
            item.highestLevel = "-";
          }
          if (item.appearTime == null) {
            item.appearTime = "-";
          }
        });
        this.tableData = res.data;
        this.loading = false;
      }
    },
    //获取平原/流域水位
    async getAllStWatLevByType(obj) {
      await precipitation.watLevOfPlainBasInnerRiv(obj).then(res => {
        if (obj.type == 1) {//平原水位
          this.plainTableData = res.data;
        } else {
          this.tableData = res.data;
        }
      })
    }
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
</style>
<style lang="scss">
$searchInpHeight: 25px;
$mainColor: #3f9bfc;
$tdHeight: 37px;
.sidebar-box {
  .sidebar-top,
  .sidebar-bottom {
    width: 100%;
    height: 50%;
    // overflow-y: auto;
  }
  .sidebar-title {
    line-height: 50px;
    i {
      vertical-align: middle;
    }
  }
  .el-input__inner,
  .el-input__icon {
    color: $mainColor;
    height: $searchInpHeight;
    line-height: $searchInpHeight;
    &::-webkit-input-placeholder {
      color: $mainColor;
    }

    &::-moz-placeholder {
      color: $mainColor;
    }

    &:-ms-input-placeholder {
      color: $mainColor;
    }
  }
  .el-select__caret.el-input__icon.el-icon-arrow-up {
    color: $mainColor;
  }
  .sidebar-list {
    .el-table .cell {
      height: $tdHeight;
      line-height: $tdHeight;
    }
    .el-table th,
    .el-table td {
      padding: 0;
      font-size: 16px;
      color: #fff;
      text-align: left;
    }
    .el-table th {
      padding: 3px 0;
      background-color: transparent;
      font-weight: bold;
    }
    .el-table__body tr.current-row > td {
      color: #fff;
      background-color: transparent;
    }
  }
}
.bigScreen-sel {
  .el-select-dropdown__item.selected {
    color: $mainColor;
  }
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
</style>