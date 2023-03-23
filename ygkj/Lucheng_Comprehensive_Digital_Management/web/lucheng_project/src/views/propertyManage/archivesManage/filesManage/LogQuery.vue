<template>
  <div class="wrap log_query">
    <div class="wrap_left">
      <div class="left_top">
        <el-date-picker
          v-model="selectMonth"
          type="month"
          format="yyyy-MM"
          value-format="yyyy-MM"
          @change="changeMonth"
        >
        </el-date-picker>
      </div>
      <div class="left_list">
        <ul>
          <li
            v-for="(item, index) in dateList"
            :key="index"
            :class="dateIndex == index ? 'active' : ''"
            @click="dateClick(index)"
          >
            {{ item }}
          </li>
        </ul>
      </div>
    </div>
    <div class="wrap_right">
      <div class="table_input">
        <span>
          {{ dateSelect }}
          <b>（访问记录：{{ total }}次）</b>
        </span>
        <i class="demonstration">操作类型：</i>
        <el-select
          v-model="typeSelect"
          placeholder="选择操作类型"
          @change="handleFilter"
          class="edSelect"
        >
          <el-option
            v-for="item in typeSelectOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <div style="float: right">
          <primary-table
            ref="fileTable"
            title=""
            :hideAdd="true"
            @doSearch="doSearch"
          >
          </primary-table>
        </div>
      </div>
      <div class="table_wrap">
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          style="width: 100%"
          class="deep-table"
          border
          :header-row-style="{ height: '43px' }"
        >
          <el-table-column
            type="index"
            label="序号"
            width="50"
          ></el-table-column>
          <el-table-column prop="createTime" label="操作时间"></el-table-column>
          <el-table-column prop="ip" label="IP地址"></el-table-column>
          <el-table-column prop="createName" label="操作人员"></el-table-column>
          <el-table-column prop="fileName" label="档案名称"></el-table-column>
          <el-table-column
            prop="archiveName"
            label="目录归属"
          ></el-table-column>
          <el-table-column prop="status" label="操作类型">
            <template slot-scope="scope">
              <span v-if="scope.row.status == 1"><i></i>查看</span>
              <span v-if="scope.row.status == 2"><i></i>下载</span>
              <span v-if="scope.row.status == 3"><i></i>删除</span>
              <span v-if="scope.row.status == 4"><i></i>更新</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination_area">
          <el-pagination
            class="right"
            background
            layout="total,prev, pager, next"
            :total="total"
            :page-sizes="[10, 20, 30, 40]"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PrimaryTable from "../primary_table/index";
import { getTimeListApi, getFileListApi } from "@/api/propertyManage";
export default {
  name: "LogQuery",
  props: {},
  components: {
    PrimaryTable,
  },
  data() {
    return {
      loading: false,
      selectMonth: new Date().format("yyyy-MM"),
      typeSelect: "",
      typeSelectOptions: [
        { label: "全部", value: "" },
        { label: "查看", value: "1" },
        { label: "下载", value: "2" },
        { label: "删除", value: "3" },
        { label: "更新", value: "4" },
      ],
      dateIndex: 0,
      dateSelect: "",
      dateList: [],
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
    };
  },
  mounted() {
    this.getTimeList();
  },
  methods: {
    //左侧时间列表
    async getTimeList() {
      let res = await getTimeListApi({
        time: this.selectMonth,
      });
      if (res.code == 0) {
        this.dateSelect = "";
        this.dateList = res.data;
        if (res.data.length > 0) {
          this.dateSelect = res.data[0];
          this.getFileList();
        }
      }
    },
    //日志访问表格数据
    async getFileList() {
      const name = this.$refs.fileTable.searchName;
      let res = await getFileListApi({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        fileName: name,
        queryTime: this.dateSelect,
        status: this.typeSelect,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.total = res.data.total;
      }
    },
    changeMonth(val) {
      this.selectMonth = val;
      this.getTimeList();
    },
    dateClick(index) {
      this.dateIndex = index;
      this.dateSelect = this.dateList[index];
      this.pageNum = 1;
      this.total = 0;
      this.getFileList();
    },
    handleFilter() {
      this.getFileList();
    },
    doSearch() {
      this.getFileList();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getFileList();
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.getFileList();
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding: 16px;
  @include flexbox();
  @include flexJC(space-between);
  > .wrap_left {
    width: 256px;
    height: auto;
    border: 1px solid #e6e6e6;
    background: #fff;
    padding: 15px;
    box-sizing: border-box;
    .left_list {
      width: 100%;
      height: calc(100% - 50px);
      margin-top: 10px;
      overflow-y: auto;
      ul {
        li {
          font-size: 14px;
          color: #95969b;
          line-height: 32px;
          letter-spacing: 0.8px;
          padding: 0 32px;
          cursor: pointer;
        }
        li.active {
          color: #000;
        }
      }
    }
  }
  > .wrap_right {
    width: calc(100% - 264px);
    height: 100%;
    .table_input {
      height: 36px;
      background: #fff;
      border: 1px solid #e6e6e6;
      padding: 12px 16px;
      span {
        font-size: 18px;
        color: #000;
        b {
          font-size: 14px;
          font-weight: normal;
        }
      }
      i {
        font-style: normal;
        margin-left: 25px;
      }
      .wrap {
        padding: 0;
      }
    }
    .table_wrap {
      width: 100%;
      height: calc(100% - 72px);
      background: #fff;
      border: 1px solid #e6e6e6;
      margin-top: 8px;
      padding: 12px;
      overflow-y: auto;
      box-sizing: border-box;
    }
  }
  ::v-deep .el-input__inner {
    -webkit-appearance: none;
    background-color: #fff;
    background-image: none;
    border-radius: 0.208vw;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    height: 2.083vw;
    line-height: 2.083vw;
    outline: 0;
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
  }
}
.pagination_area {
  width: 100%;
  height: 32px;
  background: #f8f8f8;
  border: 1px solid #d6d6d6;
  border-top: 0;
  .el-pagination {
    height: 100%;
    padding: 0;
    ::v-deep button,
    ::v-deep ul {
      height: 30px;
      line-height: 30px;
    }
    ::v-deep .el-pager {
      margin-top: 3px;
      li {
        height: 24px;
        min-width: 24px;
        line-height: 24px;
        border: 1px solid #d6d6d6;
        border-radius: 4px;
        margin: 0 4px 0 0;
      }
    }
    ::v-deep span:not([class*="suffix"]) {
      margin-top: 2px;
      vertical-align: middle;
    }
    ::v-deep .el-input__inner {
      background: transparent;
      border: 1px solid #1492ff;
      border-radius: 4px;
      font-size: 12px;
      color: rgba(0, 0, 0, 0.88);
    }
    ::v-deep .el-select .el-input {
      width: 80px;
      // width: 200px;
    }
    ::v-deep .btn-prev i,
    ::v-deep .btn-next i {
      width: 24px;
      line-height: 22px;
      border: 1px solid #d6d6d6;
      border-radius: 4px;
      margin: 0;
    }
    .el-pagination__sizes {
      margin: 0;
    }
  }
}
</style>
<style lang='scss'>
.log_query .table_input .el-button {
  margin-right: -20px;
}
.log_query .table_input .el-input {
  margin-right: 0;
}
.log_query .table_input .wrap .table-view-head {
  height: 38px;
}
</style>

