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
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="385"
        highlight-current-row
        @row-click="handleRowClick"
      >
        <el-table-column type="index" label width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="data" label="水位(m)" sortable></el-table-column>
        <el-table-column prop="time" label="时间"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "Sidebar",
  props: {},
  data() {
    return {
      name: "",
      tableData: [
        {
          name: "积水点1",
          data: 0.12,
          time: "2021-02-07",
          latitude: 28.03914575,
          longitude: 120.5795858,
        },
        {
          name: "积水点2",
          data: 0.26,
          time: "2021-02-07",
          latitude: 28.03913661,
          longitude: 120.5796705,
        },
        {
          name: "积水点3",
          data: 0.172,
          time: "2021-02-07",
          latitude: 28.03911531,
          longitude: 120.5794466,
        },
        {
          name: "积水点4",
          data: 0.13,
          time: "2021-02-07",
          latitude: 28.03904428,
          longitude: 120.5796424,
        },
        {
          name: "积水点5",
          data: 0.1,
          time: "2021-02-07",
          latitude: 28.03158784,
          longitude: 120.5963369,
        },
        {
          name: "积水点6",
          data: 0.1,
          time: "2021-02-07",
          latitude: 28.00978983,
          longitude: 120.6065906,
        },
        {
          name: "积水点7",
          data: 0.25,
          time: "2021-02-07",
          latitude: 27.99076401,
          longitude: 120.6323262,
        },
        {
          name: "积水点8",
          data: 0.1,
          time: "2021-02-07",
          latitude: 27.9925,
          longitude: 120.63555,
        },
        {
          name: "积水点9",
          data: 0,
          time: "2021-02-07",
          latitude: 27.99332742,
          longitude: 120.6356262,
        },
        {
          name: "积水点10",
          data: 0,
          time: "2021-02-07",
          latitude: 27.99642063,
          longitude: 120.590802,
        },
      ],
      stationData: [
        {
          name: "丰门河口站",
          data: 0.6,
          time: "2021-02-07",
          latitude: 27.815746,
          longitude: 119.928272,
        },
        {
          name: "卧祺水闸上游",
          data: 0.26,
          time: "2021-02-07",
          latitude: 27.973622,
          longitude: 120.369628,
        },
        {
          name: "双屿康龙",
          data: 0.172,
          time: "2021-02-07",
          latitude: 27.845775,
          longitude: 120.390432,
        },
        {
          name: "西山2",
          data: 0.55,
          time: "2021-02-07",
          latitude: 27.76599,
          longitude: 120.652127,
        },
        {
          name: "西山（北斗）",
          data: 0.1,
          time: "2021-02-07",
          latitude: 27.77098,
          longitude: 120.680514,
        },
        {
          name: "梅屿大河",
          data: 0.1,
          time: "2021-02-07",
          latitude: 28.155854,
          longitude: 120.864527,
        },
      ],
    };
  },
  mounted() {
    //父组件传值给地图Point;
    if (!this.$route.query.dmyl) {
      this.$parent.$refs.map.rainPoint(this.tableData, this.stationData);
    }
  },
  methods: {
    getList() {},

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