<!--
 * @Author: hanyu dyn
 * @LastEditTime: 2021-01-27 11:59:34
 * @Description: 防汛仓库右边栏
 * @FilePath: /FloodDisasterDispatch/src/views/dataScreen/rescueSupport/components/sidebar/FloodWarehouse.vue
-->
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
        >
        </el-option>
      </el-select>
    </div>
    <div class="sidebar-list">
      <el-table
        :data="tableData"
        style="width: 100%"
        height="380"
        highlight-current-row
      >
        <el-table-column type="index" label="" width="55">
          <template slot-scope="scope">
            <span class="table-order">{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="仓库名称"> </el-table-column>
        <el-table-column prop="chargePerson" label="联系人"> </el-table-column>
        <el-table-column prop="chargePhone" label="联系方式"> </el-table-column>
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
    };
  },
  computed: {
    tableData: {
      get(){
        return this.$store.state.warehouseList
      },
      set(val){
        this.tableData = val
      }
    }
  },
  mounted() {
    if(this.tableData.length === 0){
      this.getList()
    }else{
      this.$parent.$refs.map.showPoint("warehouse", this.tableData);
    }
  },
  methods: {
    getList(){
      let that = this
      this.$store.dispatch('getFloodWarehouseList',{
        params: {
          areaCode: this.areaCode,
          name: this.name
        },
        callback: (list) => {
          that.$parent.$refs.map.showPoint("warehouse", list);
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