<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-12-07 10:21:53
 * @Description: 
 * @FilePath: \lucheng_project\src\views\systemManagement\operationLog\index.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>操作日志</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">用户名：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          @change="search"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
        <span style="font-size: 14px">时间：</span>
        <el-date-picker
          @change="search"
          v-model="dispatchYear"
          value-format="yyyy-MM-dd"
          clearable
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 320px; margin: 0 20px 0 15px"
        >
        </el-date-picker>
      </div>
      <el-button @click="exportTable()">导出</el-button>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="username"
          label="用户名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="operation"
          label="用户操作"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="module"
          label="操作模块"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="time"
          label="响应时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="method"
          label="请求方法"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="params"
          label="请求参数"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="ip"
          label="IP地址"
          align="center"
        ></el-table-column>
      </el-table>
      <pagination
        :total="pageTotal"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        @pagination="getList"
      />
    </div>
  </div>
</template>

<script>
import { operationListApi } from "@/api/systemManagement.js";
import { exportlogs } from "@/utils/export.js";
export default {
  data() {
    return {
      dispatchYear: "",
      dispatchName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await operationListApi({
        userName: this.dispatchName,
        startTime: this.dispatchYear ? this.dispatchYear[0] : "",
        endTime: this.dispatchYear ? this.dispatchYear[1] : "",
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    search() {
      this.pageNum = 1;
      this.getList();
    },
    exportTable() {
      let obj = {
        userName: this.dispatchName,
        startTime: this.dispatchYear ? `${this.dispatchYear[0]} 00:00:00` : "",
        endTime: this.dispatchYear ? `${this.dispatchYear[1]} 00:00:00` : "",
      };
      exportlogs(obj);
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
}
</style>