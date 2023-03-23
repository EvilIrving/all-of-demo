<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-12-08 14:55:15
 * @Description: 
 * @FilePath: \lucheng_project\src\views\dataMaintenance\changeRecord\changeRecord.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>变更记录</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">名称：</span>
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
          prop="projectName"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="changeTime"
          label="变更时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="type"
          label="责任人类型"
          align="center"
        ></el-table-column>
        <el-table-column label="责任人姓名" align="center">
          <el-table-column
            prop="chargeNameBefore"
            label="变更前"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="chargeNameAfter"
            label="变更后"
            align="center"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="责任人部门" align="center">
          <el-table-column
            prop="chargeDeptBefore"
            label="变更前"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="chargeDeptAfter"
            label="变更后"
            align="center"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="责任人职务" align="center">
          <el-table-column
            prop="chargeJobBefore"
            label="变更前"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="chargeJobAfter"
            label="变更后"
            align="center"
          ></el-table-column>
        </el-table-column>
        <el-table-column label="责任人手机号" align="center">
          <el-table-column
            prop="chargePhoneBefore"
            label="变更前"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="chargePhoneAfter"
            label="变更后"
            align="center"
          ></el-table-column>
        </el-table-column>
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
import { queryChangeRecordApi } from '@/api/dataMaintenance';
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
      let res = await queryChangeRecordApi({
        keyWord: this.dispatchName,
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