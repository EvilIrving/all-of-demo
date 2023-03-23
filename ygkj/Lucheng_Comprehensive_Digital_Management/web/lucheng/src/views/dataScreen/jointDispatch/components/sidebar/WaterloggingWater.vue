<!--
 * @Author: dyn
 * @Date: 2021-02-27 15:56:00
 * @LastEditTime: 2021-03-01 09:06:16
 * @LastEditors: Please set LastEditors
 * @Description: 洪涝分析-降雨分析-左侧
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudgeNew\components\sidebar\RainfallAnalysisLeft.vue
-->
<template>
  <div class="sidebar-box">
    <div class="sidebar-top">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 水位</span>
      </p>
      <div class="right-tab">
        <template>
          <span v-for="(name, index) in sidebarTabArr" :key="index" @click="sidebarTabChange(name)" :class="activeSidebar == name ? 'active' : ''">{{ name }}</span>
        </template>
      </div>
      <div class="sidebar-list" v-if="activeSidebar == '内河水位'">
        <el-table :data="watLevTableData" style="width: 100%" height="185" highlight-current-row v-loading="loading">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="stName" label="名称" width="140px"></el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="realLevel" label="水位" width="80px"></el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="warningLevel" label="警戒水位" width="100px"></el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="guaranteeLevel" label="保证水位" width="100px"></el-table-column>
        </el-table>
      </div>
      <!-- 潮位 -->
      <div class="sidebar-list" v-else>
        <el-table :data="watLevTableList" style="width: 100%" height="185" highlight-current-row v-loading="loading">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="stName" label="名称" width="160px"></el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="tdz" label="潮位" width="100px"></el-table-column>
          <!-- <el-table-column :show-overflow-tooltip="true" prop="warningLevel" label="警戒水位" width="100px"></el-table-column> -->
          <!-- <el-table-column :show-overflow-tooltip="true" prop="guaranteeLevel" label="保证水位" width="100px"></el-table-column> -->
        </el-table>
      </div>
    </div>
    <div class="sidebar-bottom">
      <div class="sidebar-title">
        <div><i class="icon-judge-icon"></i> 内涝积水点(mm) </div>
      </div>
      <div class="sidebar-list">
        <el-table :data="tableList" style="width: 100%" height="230" highlight-current-row v-loading="loading">
          <el-table-column type="index" label width="55">
            <template slot-scope="scope">
              <span class="table-order">{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="waterLevel" label="水深(cm)"></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { waterApi, rainApi,precipitation } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      sidebarTabArr: ["内河水位", "潮位"],
      activeSidebar: "内河水位",
      reservoirName: "",
      reservoirValue: true,
      basinName: "",
      basinValue: true,
      plainName: "",
      plainValue: true,
      tableData: [],
      rainTableData: [],
      timeTabArr: [
        {
          label: "1h",
          value: 60,
        },
        {
          label: "3h",
          value: 180,
        },
        {
          label: "24h",
          value: 1440,
        },
        {
          label: "72h",
          value: 4320,
        }
      ],
      activeTime: 60,
      loading: false,
      colId: null,
      watLevTableData: [],
      watLevTableList: [],
      tableList: []
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
    this.$nextTick(()=> {
      this.newestFloodPoint();
      this.watLevOfPlainBasInnerRiv();
    });
    
    this.rainTableData=[{
    type:"0-10",
    num:"0",
    z:"0" 
    },{
    type:"10-25",
    num:"0",
    z:"0"
    },{
    type:"25-50",
    num:"0",
    z:"0"
    },{
    type:"50-100",
    num:"0",
    z:"0"
    },{
    type:"100-150",
    num:"0",
    z:"0"
    },{
    type:"150",
    num:"0",
    z:"0"
    }]

    this.getCol();
    // this.getPpStNumStatistic();
  },
  methods: {
    //内涝积水点
    async newestFloodPoint() {
      let opt = {
        pageNum: '',
        pageSize: ''
      }
      let res = await precipitation.newestFloodPoint(opt);
      if (res.code == 0) {
        this.tableList = res.data.list;
      }
    },
    //潮位
    async waterStations() {
      let opt = {
        stationType: 'TT'
      }
      let res = await precipitation.waterStations(opt);
      if (res.code == 0) {
        this.watLevTableList = res.data;
      }
    },
    //内河水位
    async watLevOfPlainBasInnerRiv() {
      let opt = {
        type: 3
      }
      let res = await precipitation.watLevOfPlainBasInnerRiv(opt);
      if (res.code == 0) {
        this.watLevTableData = res.data;
      }
    },
    handleTime(item) {
      this.activeTime = item.value;
      this.getPpStNumStatistic();
    },
    sidebarTabChange(name) {
      this.activeSidebar = name;
      if (name == '内河水位') {
        this.watLevOfPlainBasInnerRiv();
      }else {
        this.waterStations();
      }
    },
    getCol() {
      this.colId = this.$route.query.id;
      if (this.colId == 4 || this.colId == 1) {
        this.getList();
        // 大中型水库
        // this.tableData = [
        //   {
        //     stName: "珊溪",
        //     level: "125.4",
        //     floodLevel: "377.8",
        //     highestLevel: "377.8",
        //   },
        //   {
        //     stName: "赵山渡",
        //     level: "20.2",
        //     floodLevel: "0",
        //     highestLevel: "-",
        //   },
        //   {
        //     stName: "泽雅",
        //     level: "80.4",
        //     floodLevel: "406.2",
        //     highestLevel: "406.2",
        //   },
        //   {
        //     stName: "百丈漈",
        //     level: "620.4",
        //     floodLevel: "406.2",
        //     highestLevel: "406.2",
        //   },
        //   {
        //     stName: "仰义",
        //     level: "52.2",
        //     floodLevel: "584.8",
        //     highestLevel: "584.8",
        //   },
        //   {
        //     stName: "福溪",
        //     level: "210.1",
        //     floodLevel: "373.7",
        //     highestLevel: "373.7",
        //   },
        //   {
        //     stName: "白石",
        //     level: "15.4",
        //     floodLevel: "210.7",
        //     highestLevel: "210.7",
        //   },

        //   {
        //     stName: "淡溪",
        //     level: "27.5",
        //     floodLevel: "591.4",
        //     highestLevel: "591.4",
        //   },
        //   {
        //     stName: "林溪",
        //     level: "67.0",
        //     floodLevel: "63.3",
        //     highestLevel: "63.3",
        //   },
        //   {
        //     stName: "金溪",
        //     level: "242.4",
        //     floodLevel: "97",
        //     highestLevel: "97",
        //   },
        //   {
        //     stName: "北溪",
        //     level: "391.3",
        //     floodLevel: "133.6",
        //     highestLevel: "133.6",
        //   },
        //   {
        //     stName: "桥墩",
        //     level: "30.6",
        //     floodLevel: "348.6",
        //     highestLevel: "348.6",
        //   },
        //   {
        //     stName: "吴家园",
        //     level: "27.4",
        //     floodLevel: "523.6",
        //     highestLevel: "523.6",
        //   },
        //   {
        //     stName: "高岭头",
        //     level: "759.5",
        //     floodLevel: "510.9",
        //     highestLevel: "510.9",
        //   },
        //   {
        //     stName: "高二电",
        //     level: "303.9",
        //     floodLevel: "99.9",
        //     highestLevel: "99.9",
        //   },
        //   {
        //     stName: "仙居",
        //     level: "243.9",
        //     floodLevel: "97.9",
        //     highestLevel: "97.9",
        //   },
        //   {
        //     stName: "双涧溪",
        //     level: "376.9",
        //     floodLevel: "89.5",
        //     highestLevel: "89.5",
        //   },
        //   {
        //     stName: "三插溪",
        //     level: "314.1",
        //     floodLevel: "122.5",
        //     highestLevel: "122.5",
        //   },
        // ];
      } else if (this.colId == 5 || this.colId == 2) {
        // 流域
        this.tableData = [
          {
            stName: "温州",
            z: "0.7",
            warnLevel: "-0.89",
            sureLevel: "-",
            highestLevel: "5.55",
            lng: 120.64,
            lat: 28.03,
          },
          {
            stName: "灵昆",
            z: "-0.09",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "-",
            lng: 120.85,
            lat: 27.97,
          },
          {
            stName: "瑞安",
            z: "0.11",
            warnLevel: "-1.17",
            sureLevel: "-",
            highestLevel: "5.03",
            lng: 120.62,
            lat: 27.79,
          },
          {
            stName: "鳌江",
            z: "0.11",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "5.22",
            lng: 120.55,
            lat: 27.59,
          },
          {
            stName: "埭头",
            z: "10.78",
            warnLevel: "18.92",
            sureLevel: "19.92",
            highestLevel: "17.92",
            lng: 120.28,
            lat: 27.6,
          },
          {
            stName: "石柱",
            z: "16.54",
            warnLevel: "31.79",
            sureLevel: "30.79",
            highestLevel: "29.79",
            lng: 120.75,
            lat: 28.26,
          },
          {
            stName: "峃口",
            z: "21.12",
            warnLevel: "38.5",
            sureLevel: "39.5",
            highestLevel: "37.5",
            lng: 120.15,
            lat: 27.75,
          },
        ];
      } else if (this.colId == 6 || this.colId == 3) {
        // 平原
        this.tableData = [
          {
            stName: "西山",
            z: "2.59",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "4.42",
          },
          {
            stName: "永强",
            z: "2.39",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.93",
          },
          {
            stName: "林垟",
            z: "2.72",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.29",
          },
          {
            stName: "平阳",
            z: "2.69",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "3.85",
          },
          {
            stName: "鳌江内河",
            z: "2.77",
            warnLevel: "3",
            sureLevel: "3.6",
            highestLevel: "3.65",
          },
          {
            stName: "宜山",
            z: "2.36",
            warnLevel: "3",
            sureLevel: "3.5",
            highestLevel: "3.55",
          },
        ];
      } else {
        this.tableData = [
          {
            stName: "西山",
            z: "2.59",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "4.42",
          },
          {
            stName: "永强",
            z: "2.39",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.93",
          },
          {
            stName: "林垟",
            z: "2.72",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "5.29",
          },
          {
            stName: "平阳",
            z: "2.69",
            warnLevel: "3.1",
            sureLevel: "3.6",
            highestLevel: "3.85",
          },
          {
            stName: "鳌江内河",
            z: "2.77",
            warnLevel: "3",
            sureLevel: "3.6",
            highestLevel: "3.65",
          },
          {
            stName: "宜山",
            z: "2.36",
            warnLevel: "3",
            sureLevel: "3.5",
            highestLevel: "3.55",
          },
          {
            stName: "温州",
            z: "0.7",
            warnLevel: "-0.89",
            sureLevel: "-",
            highestLevel: "5.55",
            lng: 120.64,
            lat: 28.03,
          },
          {
            stName: "灵昆",
            z: "-0.09",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "-",
            lng: 120.85,
            lat: 27.97,
          },
          {
            stName: "瑞安",
            z: "0.11",
            warnLevel: "-1.17",
            sureLevel: "-",
            highestLevel: "5.03",
            lng: 120.62,
            lat: 27.79,
          },
          {
            stName: "鳌江",
            z: "0.11",
            warnLevel: "3.85",
            sureLevel: "-",
            highestLevel: "5.22",
            lng: 120.55,
            lat: 27.59,
          },
          {
            stName: "埭头",
            z: "10.78",
            warnLevel: "18.92",
            sureLevel: "19.92",
            highestLevel: "17.92",
            lng: 120.28,
            lat: 27.6,
          },
          {
            stName: "石柱",
            z: "16.54",
            warnLevel: "31.79",
            sureLevel: "30.79",
            highestLevel: "29.79",
            lng: 120.75,
            lat: 28.26,
          },
          {
            stName: "峃口",
            z: "21.12",
            warnLevel: "38.5",
            sureLevel: "39.5",
            highestLevel: "37.5",
            lng: 120.15,
            lat: 27.75,
          },
        ];
      }
      this.tableData = this.tableData.map((item) => {
        item.z = 0;
        return item;
      });
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
    //查询各个雨量站累计降雨值
    async getPpStNumStatistic() {
      let opt = {
        minutes: this.activeTime
      }
      let res = await rainApi.getPpStNumStatistic(opt);
      if (res.code == 0) {
        this.rainTableData = res.data;
      }
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
    display: flex;
    line-height: 50px;
    align-items: center;
    justify-content: space-between;
    &-tab {
      display: flex;
      > div {
        font-size: 12px;
        height: 24px;
        line-height: 24px;
        width: 44px;
        border-radius: 12px;
        text-align: center;
        cursor: pointer;
        background: #073b5b;
        margin-left: 6px;
        &.active {
          background-color: #129cdc;
        }
      }
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
    .type-icon {
      display: inline-block;
      width: 12px;
      height: 12px;
      vertical-align: middle;
      border-radius: 50%;
      margin-right: 8px;
      &.type-0-10 {
        background: #39a0ff;
      }
      &.type-10-25 {
        background: #43dc00;
      }
      &.type-25-50 {
        background: #fcee4e;
      }
      &.type-50-100 {
        background: #f7994a;
      }
      &.type-100-150 {
        background: #9303bc;
      }
      &.type-150 {
        background: #f40221;
      }
    }
    .type-label {
      vertical-align: middle;
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