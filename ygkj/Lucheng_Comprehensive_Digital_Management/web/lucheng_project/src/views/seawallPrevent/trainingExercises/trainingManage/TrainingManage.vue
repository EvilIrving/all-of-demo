<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:15:38
 * @Description: 演练管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\trainingExercises\trainingManage\TrainingManage.vue
-->

<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>演练管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入演练事由"
        prefix-icon="el-icon-search"
        @change="handerChangeSearch"
        v-model="reasons"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-date-picker
        v-model="searchTime"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"
        @change="handerChangeTime"
        clearable
      >
      </el-date-picker>
      <el-button
        type="primary"
        v-permission="'trainingManage:add'"
        class="right"
        @click="editTable('新增', '')"
        >新增</el-button
      >
      <el-button
        style="margin-right: 10px"
        v-permission="'trainingManage:export'"
        class="right"
        @click="exportTable()"
        >导出</el-button
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
          prop="tm"
          label="演练时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="address"
          label="演练地点"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="reasons"
          label="演练事由"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="person"
          label="参与人员"
          align="center"
        ></el-table-column>
        <el-table-column prop="contacts" label="相关附件" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              @click="lookFiles(scope.row.fileurl)"
              v-if="scope.row.fileurl"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'trainingManage:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'trainingManage:del'"
                @click="delCard(scope.row.id)"
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
        :page-sizes="[10, 20, 30, 40]"
        @pagination="getList"
      />
    </div>
    <el-dialog :title="title" :visible.sync="showEditDialog" width="960px">
      <training-manage-form
        v-if="showEditDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></training-manage-form>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeEditDialog="closeEditDialog"
      ></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { trainingExercisesApi } from "@/api/seawallPrevent";
import TrainingManageForm from "./TrainingManageForm";
import LookFile from "@/components/LookFile.vue";
import { exportPractice } from "@/utils/export.js";
export default {
  name: "TrainingManage",
  props: {},
  components: {
    TrainingManageForm,
    LookFile,
  },
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      showEditDialog: false,
      fileDialog: false,
      reasons: "",
      starttime: "",
      endtime: "",
      searchTime: "",
      token: "",
    };
  },
  mounted() {
    this.getList();
    this.token = this.$sessionData("get", "userInfo").accessToken;
  },
  methods: {
    lookFiles(url) {
      this.url = url;
      this.fileDialog = true;
    },
    exportTable() {
      let obj = {
        reasons: this.reasons,
        starttime: this.starttime,
        endtime: this.endtime,
      };
      exportPractice(obj);
    },
    async getList() {
      let res = await trainingExercisesApi.rehearsalList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        reasons: this.reasons,
        starttime: this.starttime,
        endtime: this.endtime,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    handerChangeSearch() {
      this.pageNum = 1;
      this.getList();
    },
    handerChangeTime(times) {
      if (times) {
        this.starttime = times[0];
        this.endtime = times[1];
      } else {
        this.starttime = "";
        this.endtime = "";
      }
      this.getList();
    },
    editTable(title, id) {
      this.id = id;
      this.title = title;
      this.showEditDialog = true;
    },
    delCard(id) {
      this.$confirm("此操作将删除该信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        trainingExercisesApi.rehearsalDelete({ id: id }).then((res) => {
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--;
            }
            this.getList();
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          } else {
            this.$message({
              type: "error",
              message: "删除失败！",
            });
          }
        });
      });
    },
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.showEditDialog = false;
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>