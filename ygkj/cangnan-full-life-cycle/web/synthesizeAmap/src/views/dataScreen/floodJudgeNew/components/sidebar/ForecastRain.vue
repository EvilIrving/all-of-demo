<!-- 预报雨量 -->
<template>
  <div class="sidebar-box">
    <div class="sidebar-search">
      <el-input
        class="search-inp"
        v-model="name"
        placeholder="请输入关键字进行搜索"
      >
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
      </el-input>
    </div>
    <div class="sidebar-tab" style="margin-top: 16px">
      <span
        v-for="(item, index) in timeTabArr"
        :key="index"
        @click="timeTabHandle(item)"
        :class="item.value == activeTime ? 'active' : ''"
      >
        {{ item.label }}
      </span>
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="320"
        highlight-current-row
        v-loading="loading"
      >
        <el-table-column type="index" label="" width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="adnm" label="地区"> </el-table-column>
        <el-table-column prop="drpSum" label="面雨量(mm)"> </el-table-column>
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
      tableDataTemp: [
        { adnm: "龙港市", adcd: "330383000000", drpSum: 0 },
        { adnm: "生态园管理委员会", adcd: "330354000000", drpSum: 0 },
        { adnm: "瓯江口产业集聚区", adcd: "330355000000", drpSum: 0 },
        { adnm: "苍南县", adcd: "330327000000", drpSum: 0 },
        { adnm: "乐清市", adcd: "330382000000", drpSum: 0 },
        { adnm: "鹿城区", adcd: "330302000000", drpSum: 0 },
        { adnm: "市本级", adcd: "330301000000", drpSum: 0 },
        { adnm: "瑞安市", adcd: "330381000000", drpSum: 0 },
        { adnm: "平阳县", adcd: "330326000000", drpSum: 0 },
        { adnm: "浙南产业集聚区（经开区）", adcd: "330351000000", drpSum: 0 },
        { adnm: "龙湾区", adcd: "330303000000", drpSum: 0 },
        { adnm: "洞头区", adcd: "330322000000", drpSum: 0 },
        { adnm: "泰顺县", adcd: "330329000000", drpSum: 0 },
        { adnm: "永嘉县", adcd: "330324000000", drpSum: 0 },
        { adnm: "温州浙南科技城管理委员会", adcd: "330353000000", drpSum: 0 },
        { adnm: "瓯海区", adcd: "330304000000", drpSum: 0 },
        { adnm: "文成县", adcd: "330328000000", drpSum: 0 },
      ],
      timeTabArr: [
        {
          label: "2小时",
          value: 2,
        },
        {
          label: "1天",
          value: 1,
        },
        {
          label: "3天",
          value: 3,
        },
        {
          label: "7天",
          value: 7,
        },
      ],
      activeTime: 2,
      loading: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    timeTabHandle(item) {
      this.activeTime = item.value;
      this.getList();
    },
    getList() {
      this.loading = true;
      this.tableData = [];
      setTimeout(() => {
        this.tableData = this.tableDataTemp;
        this.loading = false;
      }, 500);
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
.sidebar-box .el-loading-mask {
  background-color: transparent;
}
</style>