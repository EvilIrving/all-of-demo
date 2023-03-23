<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-25 14:30:04
 * @Description: 
 * @FilePath: \lucheng_project\src\views\systemManagement\registerLog\RegisterLog.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>登录日志</p>
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
        <span style="font-size: 14px">部门：</span>
        <el-input
          placeholder="请输入"
          v-model="department"
          @change="search"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
        <span style="font-size: 14px">登录时间：</span>
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
          prop="userName"
          label="登录人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="department"
          label="所属部门"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="operateInfo"
          label="操作内容"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="loginTime"
          label="操作时间"
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
import { loadPageApi } from "@/api/systemManagement.js";
export default {
  data() {
    return {
      dispatchYear: "",
      department: '',
      dispatchName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
    };
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      let res = await loadPageApi({
        userName: this.dispatchName,
        department: this.department,
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