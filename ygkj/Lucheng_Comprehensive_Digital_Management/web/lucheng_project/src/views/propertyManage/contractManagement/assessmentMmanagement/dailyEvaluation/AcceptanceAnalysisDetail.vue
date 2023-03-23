<!--
 * @Date: 2022-04-27 14:45:04
 * @Author: 
 * @Description: 日常考评-考勤分析
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-08-10 10:54:15
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\dailyEvaluation\AcceptanceAnalysisDetail.vue
-->
<template>
  <div class="wrap">
    <div class="tb_wrapper">
      <el-table :data="tableData" style="width: 100%" height="50vh">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="deviceName"
          label="设备名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="checkingType"
          label="考勤方式"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="temperature"
          label="温度"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="attendanceTime"
          label="考勤时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="checkingDesc"
          label="描述"
          align="center"
        ></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { supervisedCheck } from "@/api/propertyManage";
export default {
  name: "AcceptanceAnalysis",
  props: {
    detail: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      tableData: [],
    };
  },
  mounted() {
    console.log(this.detail, "detaildetaildetail");
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.attendancePeople({
        name: this.detail.name,
        yearMonth: this.detail.year,
      });
      console.log(res, 88);
      if (res.code == 0) {
        this.tableData = res.data;
      }
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
</style>