<!--
 * @Author: dyn
 * @Date: 2021-02-27 19:12:17
 * @LastEditTime: 2021-02-28 17:15:08
 * @LastEditors: Please set LastEditors
 * @Description: 洪涝分析-风险分析-右侧
 * @FilePath: \FloodDisasterDispatch\src\views\dataScreen\floodJudgeNew\components\sidebar\RiskAnalysisRight.vue
-->
<template>
  <div class="sidebar-box">
    <div class="sidebar-top" style="height: 45%">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 淹没风险</span>
      </p>
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
          <el-table-column prop="stName" label="区域"></el-table-column>
          <el-table-column
            prop="z"
            label="淹没面积"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="limitLevel"
            label="淹没耕地"
            width="100"
          ></el-table-column>
          <el-table-column
            prop="z"
            width="100"
            label="影响人口"
          ></el-table-column>
          <el-table-column
            prop="z"
            width="100"
            label="影响GDP"
          ></el-table-column>
        </el-table>
      </div>
    </div>
    <div class="sidebar-bottom" style="height: 55%">
      <p class="sidebar-title">
        <span><i class="icon-judge-icon"></i> 堤防风险</span>
      </p>
      <div class="dike-box">
        <div v-for="(item, index) in dikeTab" class="dike-tab" :key="index">
          <i :class="item.icon"></i>
          <div>
            <p>{{ item.name }}</p>
            <p>
              {{ item.long }}
              <span>km</span>
            </p>
          </div>
        </div>
      </div>
      <div class="river-box">
        <div class="river-item" v-for="(item, index) in riverArr" :key="index">
          <div class="river-index">{{ index + 1 }}</div>
          <div class="river-info">
            <div>
              <span>{{ item.name }}</span>
              <span>共{{ item.long }}km</span>
            </div>
            <div class="divide"></div>
            <div>
              <div v-for="(subitem, subindex) in item.risk" :key="subindex">
                <div>
                  <i
                    :style="{
                      background:
                        subindex == 0
                          ? '#E83D53'
                          : subindex == 1
                          ? '#F7B500'
                          : subindex == 1
                          ? '#32C5FF'
                          : '#51FEBF',
                    }"
                  ></i>
                  <span>{{ subitem }}km</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
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
      reservoirName: "",
      dikeTab: [
        {
          name: "高风险(<0)",
          long: 36,
          icon: "icon-risk-high",
        },
        {
          name: "中风险(0-0.8)",
          long: 36,
          icon: "icon-risk-soso",
        },
        {
          name: "低风险(0-0.8)",
          long: 36,
          icon: "icon-risk-low",
        },
        {
          name: "安全(>1.2)",
          long: 36,
          icon: "icon-risk-safe",
        },
      ],
      riverArr: [
        {
          name: "瓯江",
          long: 213,
          risk: [24, 45, 21, 56],
        },
        {
          name: "鳌江",
          long: 213,
          risk: [24, 45, 21, 56],
        },
        {
          name: "飞云江",
          long: 213,
          risk: [24, 45, 21, 56],
        },
      ],
      reservoirValue: true,
      basinName: "",
      basinValue: true,
      plainName: "",
      plainValue: true,
      tableData: [],
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
  .sidebar-top,
  .sidebar-bottom {
    width: 100%;
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
  .dike-box {
    height: 124px;
    width: 100%;
    background: url(../../../../../assets/images/risk-tab.png) no-repeat;
    background-size: 100% 100%;
    display: flex;
    justify-content: space-around;
    align-items: center;
    flex-wrap: wrap;
    .dike-tab {
      width: 50%;
      display: flex;
      justify-content: center;
      i {
        margin-right: 10px;
      }
      > div {
        > p:first-child {
          font-size: 10px;
          line-height: 16px;
          color: rgba(255, 255, 255, 0.8);
        }
        > p:last-child {
          font-size: 15px;
          color: #ffffff;
          span {
            font-size: 10px;
          }
        }
      }
    }
  }
}
.river-box {
  width: 100%;
  .river-item {
    width: 100%;
    display: flex;
    margin-top: 10px;
    .river-index {
      height: 18px;
      width: 24px;
      line-height: 18px;
      text-align: center;
      box-shadow: inset 0 0 13px 0 #3c93f8;
      font-size: 12px;
      color: #a6dff6;
    }
    .river-info {
      width: calc(100% - 34px);
      margin-left: 10px;
      > div {
        display: flex;
        &:first-child {
          display: flex;
          justify-content: space-between;
          height: 24px;
          line-height: 24px;
          font-size: 14px;
          > span:last-child {
            color: #23e3fb;
            font-size: 12px;
          }
        }
        &:last-child {
          display: flex;
          justify-content: space-between;
          height: 24px;
          line-height: 24px;
          font-size: 12px;
          i {
            display: inline-block;
            height: 4px;
            width: 4px;
            border-radius: 2px;
            vertical-align: middle;
            margin-right: 6px;
          }
        }
      }
      .divide {
        height: 2px;
        width: 100%;
        background: url(../../../../../assets/images/divide-line.png) no-repeat;
        background-size: 100% 100%;
      }
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