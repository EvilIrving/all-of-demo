<!-- 水情-历史极值 -->
<template>
  <div class="sidebar-box">
    <div class="sidebar-search">
      <el-input
        class="search-inp"
        v-model="name"
        @change="getList"
        placeholder="请输入关键字进行搜索"
      >
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
      </el-input>
      <el-switch
        v-model="value"
        active-color="#106CE0"
        inactive-color="#ff4949"
        class="sidebar-switch"
      ></el-switch>
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="368"
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
          prop="z"
          label="实时水位(m)"
          v-if="colId == 4 || colId == 5 || colId == 6"
          width="100"
        ></el-table-column>
        <el-table-column
          prop="sureLevel"
          label="保证水位(m)"
          width="100"
          v-if="colId == 5 || colId == 6"
        ></el-table-column>
        <el-table-column
          prop="warnLevel"
          label="警戒水位(m)"
          width="100"
          v-if="colId == 5 || colId == 6"
        ></el-table-column>
        <el-table-column
          prop="limitLevel"
          label="汛限水位(m)"
          v-if="colId == 4"
        ></el-table-column>

        <el-table-column
          prop="highestLevel"
          label="历史最高水位(m)"
          v-if="colId == 7"
        ></el-table-column>
        <!-- <el-table-column prop="appearTime" label="时间"></el-table-column> -->
      </el-table>
    </div>
  </div>
</template>

<script>
import { waterApi } from "@/api/dataScreen/floodJudge/index";

export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      name: "",
      value: true,
      tableData: [
        // {
        //   date: "2016-05-02",
        //   name: "王小虎",
        //   address: "上海市",
        // },
        // {
        //   date: "2016-05-04",
        //   name: "王小虎",
        //   address: "上海市",
        // },
      ],
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
  },
  methods: {
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
      color: #00A1FF;
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