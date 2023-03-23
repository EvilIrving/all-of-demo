<!-- 实时雨情 -->
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
      <el-select
        class="search-sel"
        popper-class="bigScreen-sel"
        v-model="areaCode"
        placeholder="请选择"
        @change="getList"
      >
        <el-option
          v-for="item in areaArr"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
    </div>
    <p class="tab-label">降雨历时</p>
    <div class="sidebar-tab">
      <span
        v-for="(item, index) in timeTabArr"
        :key="index"
        @click="handleClick(item.value)"
        :class="item.value == activeTime ? 'active' : ''"
        >{{ item.label }}</span
      >
    </div>
    <p class="tab-label">雨量范围</p>
    <div class="sidebar-search">
      <el-select
        class="search-sel"
        popper-class="bigScreen-sel"
        v-model="rainRange"
        placeholder="请选择"
        style="margin-left: 0; margin-right: 16px"
        @change="rainRangeHandle"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-input
        class="mini-inp"
        v-model="startRange"
        @change="getList"
        :disabled="disabled"
      >
        <span slot="suffix">mm</span>
      </el-input>
      <span class="symbol">-</span>
      <el-input
        class="mini-inp"
        v-model="endRange"
        @change="getList"
        :disabled="disabled"
      >
        <span slot="suffix">mm</span>
      </el-input>
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="230"
        highlight-current-row
        v-loading="loading"
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stationName" label="站名"></el-table-column>
        <el-table-column prop="drp" label="雨量(mm)" sortable width="120">
          <template slot-scope="scope">
            <span>{{ scope.row.drp == -1 ? "-" : scope.row.drp }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="tm" label="时间" width="180"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { rainApi } from "@/api/dataScreen/floodJudge/index";
export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      name: "",
      startRange: null,
      endRange: null,
      sidebarTabArr: [
        {
          label: "防汛仓库",
          value: 0,
        },
        {
          label: "防汛队伍",
          value: 1,
        },
      ],
      activeSidebar: 0,
      areaCode: "",
      areaArr: [
        {
          label: "全部",
          value: "",
        },
        {
          label: "市本级",
          value: "330300000000",
        },
        {
          label: "鹿城区",
          value: "330302000000",
        },
        {
          label: "龙湾区",
          value: "330303000000",
        },
        {
          label: "瓯海区",
          value: "330304000000",
        },
        {
          label: "洞头区",
          value: "330322000000",
        },
        {
          label: "瑞安市",
          value: "330381000000",
        },
        {
          label: "乐清市",
          value: "330382000000",
        },
        {
          label: "永嘉县",
          value: "330324000000",
        },
        {
          label: "平阳县",
          value: "330326000000",
        },
        {
          label: "苍南县",
          value: "330327000000",
        },
        {
          label: "文成县",
          value: "330328000000",
        },
        {
          label: "泰顺县",
          value: "330329000000",
        },
      ],
      tableData: [],
      timeTabArr: [
        {
          label: "10min",
          value: 10,
        },
        {
          label: "30min",
          value: 30,
        },
        {
          label: "1h",
          value: 60,
        },
        {
          label: "3h",
          value: 180,
        },
      ],
      activeTime: 10,
      options: [
        {
          value: 0,
          label: "无降雨",
        },
        {
          value: 1,
          label: "<10",
        },
        {
          value: 2,
          label: "10~25",
        },
        {
          value: 3,
          label: "25~50",
        },
        {
          value: 4,
          label: "50~100",
        },
        {
          value: 5,
          label: "100~250",
        },
        {
          value: 6,
          label: ">250",
        },
        {
          value: "自定义",
          label: "自定义",
        },
      ],
      disabled: true,
      rainRange: 1,
      loading: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      if (this.disabled) {
        if (this.rainRange === 0) {
          this.startRange = -999999;
          this.endRange = 0;
        } else if (this.rainRange === 1) {
          this.startRange = 0;
          this.endRange = 10;
        } else if (this.rainRange === 2) {
          this.startRange = 10;
          this.endRange = 25;
        } else if (this.rainRange === 3) {
          this.startRange = 25;
          this.endRange = 50;
        } else if (this.rainRange === 4) {
          this.startRange = 50;
          this.endRange = 100;
        } else if (this.rainRange === 5) {
          this.startRange = 100;
          this.endRange = 250;
        } else if (this.rainRange === 6) {
          this.startRange = 250;
          this.endRange = 9999999;
        }
      }
      let res = await rainApi.realRain({
        minutes: this.activeTime,
        stationName: this.name,
        areaCode: this.areaCode,
        min: this.startRange,
        max: this.endRange,
      });
      if (res) {
        res.data.map((item) => {
          if (item.stationName == "桥墩(北斗)") {
            item.drp = item.drp == -1 ? 200 : item.drp + 200;
          }
        });
        this.loading = false;
        //父组件传值给地图Point(res.data);
        if (!this.$route.query.dmyl) {
          this.$parent.$refs.map.rainPoint(res.data);
        }
        this.tableData = res.data;
      }
    },
    handleClick(val) {
      this.activeTime = val;
      this.getList();
    },
    rainRangeHandle(val) {
      this.disabled = val != "自定义";
      if (this.disabled) this.getList();
    },
    handleRowClick(row) {
      let { latitude, longitude } = row;
      if (latitude && longitude) {
        this.$parent.$refs.map.setView(longitude, latitude);
      } else {
        this.$message.warning("暂无经纬度！");
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
      color: #00a1ff;
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