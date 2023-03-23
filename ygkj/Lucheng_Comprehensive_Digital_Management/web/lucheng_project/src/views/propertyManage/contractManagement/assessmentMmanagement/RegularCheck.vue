<!--
 * @Date: 2022-04-24 13:49:24
 * @Author: 
 * @Description: 合同管理-考核管理-定期考评
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:54:59
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\RegularCheck.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>定期考评</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        size="mini"
        placeholder="请输入"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="staffAppraisal"
        style="width: 180px; margin-right: 15px"
        clearable
      />
      <span style="font-size: 14px; margin-left: 15px">年度：</span>
      <el-date-picker
        size="mini"
        v-model="year"
        value-format="yyyy"
        format="yyyy"
        type="year"
        placeholder="请选择："
        @change="getList()"
      >
      </el-date-picker>
      <span style="font-size: 14px; margin-left: 15px">考评时间：</span>
      <el-date-picker
        size="mini"
        v-model="examTime"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        type="datetime"
        placeholder="请选择："
        @change="getList()"
      >
      </el-date-picker>
      <span style="font-size: 14px; margin-left: 15px">考评单位：</span>
      <el-input
        size="mini"
        placeholder="请输入"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="examUnit"
        style="width: 180px; margin-right: 15px"
        clearable
      />

      <!-- <el-select size="mini" v-model="examUnit" placeholder="请选择" @change="getList" clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in yearOptions" :key="item.label" :label="item.label" :value="item.label">
        </el-option>
      </el-select> -->
      <span style="font-size: 14px; margin-left: 15px">被考评单位：</span>
      <!-- <el-input size="mini" placeholder="请输入" prefix-icon="el-icon-search" @change="getList()" v-model="evaluatedUnit" style="width: 180px; margin-right: 15px" clearable /> -->
      <el-select
        size="mini"
        v-model="evaluatedUnitId"
        placeholder="请选择被考评单位："
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in evaluatedUnitOptions"
          :key="item.id"
          :value="item.id"
          :label="item.companyName"
        >
        </el-option>
      </el-select>
      <el-button
        size="mini"
        type="primary"
        class="right"
        v-permission="'regularCheck:add'"
        @click="editTable('新增', '')"
        >新增</el-button
      >
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
          prop="examTime"
          label="考评时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="examUnit"
          label="考评单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="evaluatedUnit"
          label="被考评单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="staffAppraisal"
          label="考核人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="synthesisScore"
          label="综合得分"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_up"
                v-permission="'regularCheck:export'"
                @click="exportTable(scope.row.id)"
                >导出</span
              >
              <span
                class="deal_btn deal_btn_look"
                v-permission="'regularCheck:edit'"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'regularCheck:del'"
                @click="delTable(scope.row.id)"
                >删除</span
              >
            </div>
          </template>
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
    <el-dialog
      top="3vh"
      :title="title"
      :visible.sync="showEditDialog"
      width="1200px"
    >
      <regular-check-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></regular-check-form>
    </el-dialog>
  </div>
</template>

<script>
import { regularCheck, propertyUnitApi } from "@/api/propertyManage";
import SupervisedCheckForm from "./SupervisedCheckForm.vue";
import { exportGetByIdToExcel } from "@/utils/export.js";
import RegularCheckForm from "./RegularCheckForm.vue";
export default {
  name: "RegularCheck",
  props: {},
  components: {
    SupervisedCheckForm,
    RegularCheckForm,
  },
  data() {
    return {
      year: "",
      examUnit: "",
      evaluatedUnitId: "",
      examTime: "",
      staffAppraisal: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      evaluatedUnitOptions: [],
    };
  },
  mounted() {
    this.$route.query.year ? (this.year = this.$route.query.year) : "";
    this.$route.query.evaluatedUnitId
      ? (this.evaluatedUnitId = this.$route.query.evaluatedUnitId)
      : "";
    // console.log(this.$route)
    this.$parent.$parent.$parent.$parent.selectFourthMenu({
      fullPath: this.$route.fullPath,
      path: this.$route.path,
      title: "定期考评",
      routeType: true,
    });
    this.getList();
    this.getUnitList();
  },
  methods: {
    async getUnitList() {
      let res = await propertyUnitApi.list({ fetchAll: true });
      if (res.code == 0) {
        this.evaluatedUnitOptions = res.data.list;
      }
    },
    async getList() {
      let res = await regularCheck.list({
        examUnit: this.examUnit,
        evaluatedUnitId: this.evaluatedUnitId,
        examTime: this.examTime,
        year: this.year,
        staffAppraisal: this.staffAppraisal,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, id) {
      this.id = id;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    delTable(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        regularCheck
          .del({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
    exportTable(id) {
      // let userInfo = sessionData("get", "userInfo");
      // let src = `${this.$config.baseUrl}/project-manage/regularReview/getByIdToExcel?id=${id}&authorization=${userInfo.accessToken}`
      // window.open('/project-manage/regularReview/getByIdToExcel?id=' + id)
      let obj = { id: id };
      exportGetByIdToExcel(obj);
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