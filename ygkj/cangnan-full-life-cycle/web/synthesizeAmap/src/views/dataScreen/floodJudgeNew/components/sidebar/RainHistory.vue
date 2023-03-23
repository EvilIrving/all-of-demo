<!-- 雨量-历史极值 -->
<template>
  <div class="sidebar-box">
    <div class="sidebar-search">
      <el-input
        class="search-inp"
        v-model="name"
        placeholder="请输入关键字进行搜索"
        @change="getList"
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
        <el-table-column prop="stName" label="站名"></el-table-column>
        <el-table-column
          prop="extremum"
          label="历史雨量(mm)"
          width="124"
        ></el-table-column>
        <el-table-column
          prop="occurredTime"
          label="时间"
          width="110"
        ></el-table-column>
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
      value: true,
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市",
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市",
        },
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
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let res = await rainApi.histroy({
        stName: this.name,
      });
      if (res) {
        res.data.map((item) => {
          if (item.drpSum == null) {
            item.highestLevel = "-";
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