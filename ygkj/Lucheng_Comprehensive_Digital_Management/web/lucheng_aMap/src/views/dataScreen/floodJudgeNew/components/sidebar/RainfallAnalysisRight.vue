<!--
 * @Author: dyn
 * @Date: 2021-02-27 15:56:23
 * @LastEditTime: 2021-02-27 18:56:48
 * @LastEditors: Please set LastEditors
 * @Description: 洪涝分析-降雨分析-右侧
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudgeNew\components\sidebar\RainfallAnalysisRight.vue
-->
<template>
  <div class="sidebar-box">
    <div class="sidebar-top">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 点雨量(mm)</span>
      </p>
      <div class="right-tab">
        <template>
          <span
            v-for="(name, index) in sidebarTabArr"
            :key="index"
            @click="sidebarTabChange(name)"
            :class="activeSidebar == name ? 'active' : ''"
            >{{ name }}</span>
        </template>
      </div>
      <div class="sidebar-list">
        <el-table
          :data="tableData"
          style="width: 100%"
          height="230"
          highlight-current-row
          v-loading="loading"
          v-if="activeSidebar == '水库'">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称" width="120"></el-table-column>
          <el-table-column prop="drp1H" label="1h"></el-table-column>
          <el-table-column prop="drp3H" label="3h"></el-table-column>
          <el-table-column prop="drp6H" label="6h"></el-table-column>
         
        </el-table>
        <el-table
          :data="pointTableData"
          style="width: 100%"
          height="230"
          highlight-current-row
          v-loading="loading"
          v-else>
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="pointName" label="名称"></el-table-column>
          <el-table-column prop="oneHourRainfall" label="1h"></el-table-column>
          <el-table-column prop="threeHourRainfall" label="3h"></el-table-column>
          <el-table-column prop="twentyFourHourRainfall" label="6h"></el-table-column>
        </el-table>
      </div>
    </div>
    <div class="sidebar-bottom">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 历史极值</span>
      </p>
      <div class="sidebar-list">
        <el-table
          :data="historyTableData"
          style="width: 100%"
          height="260"
          highlight-current-row
          v-loading="loading">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="bas"
            label="名称"
            width="100"
          ></el-table-column>
          <el-table-column prop="drcd" label="时段类型"></el-table-column>
          <el-table-column
            prop="drp"
            width="130"
            label="降雨量(mm)"
          ></el-table-column>
          <el-table-column prop="tm" width="110" label="日期">
            <template slot-scope="scope">
              {{ scope.row.tm ? scope.row.tm.split(" ")[0] : "-" }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { waterApi, rainApi, precipitation } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      sidebarTabArr: ["水库", "重点关注"],
      activeSidebar: "水库",
      tableData: [],
      pointTableData: [],
      historyTableData: [],
      // timeTabArr: [
      //   {
      //     label: "1小时",
      //     value: 1,
      //   },
      //   {
      //     label: "3小时",
      //     value: 3,
      //   },
      //   {
      //     label: "24小时",
      //     value: 24,
      //   },
      //   {
      //     label: "72小时",
      //     value: 72,
      //   },
      // ],
      // activeTime: 1,
      loading: false,
      colId: null,
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
    this.getCol();
    this.getHistoricRainfallExtremum();
    this.$nextTick(()=> {
      this.getResPointRainfall();
    })
  },
  methods: {
    sidebarTabChange(name) {
      this.activeSidebar = name;
      if (name == '水库') {
        this.getResPointRainfall();
      }else {
        this.getPointList();
      }
    },
    getCol() {
      this.colId = this.$route.query.id;
      if (this.colId == 4 || this.colId == 1) {
        this.getList();
        
      } else if (this.colId == 5 || this.colId == 2) {
       
      } else if (this.colId == 6 || this.colId == 3) {
       
      } else {
       
      }
      // this.tableData = this.tableData.map((item) => {
      //   item.z = 0;
      //   return item;
      // });
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
    //历史极值
    async getHistoricRainfallExtremum() {
      await rainApi.getHistoricRainfallExtremum().then((res) => {
        if (res.code == 0) {
          this.historyTableData = res.data;
        }
      })
    },
    //点雨量 水库
    async getResPointRainfall() {
      await precipitation.getResPointRainfall().then((res) => {
        if (res.code == 0) {
            this.tableData = res.data;
        }
      })
    },
    //点雨量 重点关注
    async getPointList() {
      await precipitation.getCollectPointRainfall().then((res) => {
        if (res.code == 0) {
            this.pointTableData = res.data;
        }
      })
    }
  }
}
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
  .right-tab {
    > span {
      display: inline-block;
      width: 120px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      cursor: pointer;
      color: #4898fb;
      background: url(../../../../../assets/images/bigScreen-big-tab.png)
        no-repeat;
      background-size: 100% 100%;
      margin-right: 6px;
      &.active {
        color: #a6e9fb;
        background: url(../../../../../assets/images/bigScreen-big-tab-check.png)
          no-repeat;
        background-size: 100% 100%;
      }
    }
  }
  .sidebar-title {
    line-height: 50px;
    a {
      color: white;
    }
    .icon-animation-arrow {
      background-image: url(~assets/arrow.gif);
      display: inline-block;
      vertical-align: middle;
      width: 16px;
      height: 16px;
      background-repeat: no-repeat;
      background-size: 100%;
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